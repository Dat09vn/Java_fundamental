package daemon;

import models.BankRequest;
import database.BankRequestDAO;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Controller - manages worker threads and coordinates request processing
 * Parent process that supervises worker lifecycle
 */
public class Controller {
    
    private ExecutorService executorService;
    private final int MAX_WORKERS = 3; // Maximum concurrent workers
    private Map<Long, Future<?>> activeWorkers;
    private volatile boolean running = true;
    private final int POLLING_INTERVAL_MS = 5000; // Check for new requests every 5 seconds
    
    public Controller() {
        this.executorService = Executors.newFixedThreadPool(MAX_WORKERS);
        this.activeWorkers = Collections.synchronizedMap(new HashMap<>());
    }
    
    /**
     * Start the daemon - continuously monitor for new requests
     */
    public void start() {
        System.out.println("[CONTROLLER] Daemon started. Monitoring for new requests...");
        System.out.println("[CONTROLLER] Max workers: " + MAX_WORKERS);
        
        while (running) {
            try {
                // Fetch all NEW requests from database
                List<BankRequest> newRequests = BankRequestDAO.getNewRequests();
                
                if (!newRequests.isEmpty()) {
                    System.out.println("[CONTROLLER] Found " + newRequests.size() + " new request(s)");
                    
                    // Dispatch each request to an available worker
                    for (BankRequest request : newRequests) {
                        if (activeWorkers.size() < MAX_WORKERS) {
                            assignWorker(request);
                        } else {
                            System.out.println("[CONTROLLER] Max workers reached, waiting for completion...");
                            break;
                        }
                    }
                }
                
                // Clean up completed workers
                cleanupCompletedWorkers();
                
                // Wait before next poll
                Thread.sleep(POLLING_INTERVAL_MS);
                
            } catch (SQLException e) {
                System.err.println("[CONTROLLER] Database error: " + e.getMessage());
                try {
                    Thread.sleep(POLLING_INTERVAL_MS);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } catch (InterruptedException e) {
                System.out.println("[CONTROLLER] Interrupted, shutting down...");
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        shutdown();
    }
    
    /**
     * Assign a worker to process a request
     */
    private void assignWorker(BankRequest request) {
        Worker worker = new Worker(request, this);
        Future<?> future = executorService.submit(worker);
        activeWorkers.put(request.getRequestId(), future);
        System.out.println("[CONTROLLER] Assigned worker for request: " + request.getRequestId());
    }
    
    /**
     * Called by worker when it completes processing
     */
    public synchronized void workerCompleted(long requestId) {
        System.out.println("[CONTROLLER] Worker completed for request: " + requestId);
        activeWorkers.remove(requestId);
    }
    
    /**
     * Remove completed futures from active workers map
     */
    private void cleanupCompletedWorkers() {
        List<Long> completedRequestIds = new ArrayList<>();
        
        for (Map.Entry<Long, Future<?>> entry : activeWorkers.entrySet()) {
            if (entry.getValue().isDone()) {
                completedRequestIds.add(entry.getKey());
            }
        }
        
        for (Long requestId : completedRequestIds) {
            activeWorkers.remove(requestId);
        }
    }
    
    /**
     * Graceful shutdown
     */
    public void shutdown() {
        System.out.println("[CONTROLLER] Shutting down daemon...");
        running = false;
        
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                System.out.println("[CONTROLLER] Force shutdown after timeout");
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("[CONTROLLER] Daemon shutdown complete");
    }
    
    /**
     * Stop the daemon gracefully
     */
    public void stop() {
        running = false;
    }
}
