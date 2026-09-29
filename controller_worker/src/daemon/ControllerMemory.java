package daemon;

import models.BankRequest;
import database.BankRequestDAOMemory;
import java.util.*;
import java.util.concurrent.*;

/**
 * Controller - manages worker threads (Memory-based, no DB needed)
 */
public class ControllerMemory {
    
    private ExecutorService executorService;
    private final int MAX_WORKERS = 3;
    private Map<Long, Future<?>> activeWorkers;
    private volatile boolean running = true;
    private final int POLLING_INTERVAL_MS = 2000;
    
    public ControllerMemory() {
        this.executorService = Executors.newFixedThreadPool(MAX_WORKERS);
        this.activeWorkers = Collections.synchronizedMap(new HashMap<>());
    }
    
    public void start() {
        System.out.println("[CONTROLLER] Daemon started. Monitoring for new requests...");
        System.out.println("[CONTROLLER] Max workers: " + MAX_WORKERS);
        
        while (running) {
            try {
                List<BankRequest> newRequests = BankRequestDAOMemory.getNewRequests();
                
                if (!newRequests.isEmpty()) {
                    System.out.println("[CONTROLLER] Found " + newRequests.size() + " new request(s)");
                    
                    for (BankRequest request : newRequests) {
                        if (activeWorkers.size() < MAX_WORKERS) {
                            assignWorker(request);
                        } else {
                            System.out.println("[CONTROLLER] Max workers reached, waiting...");
                            break;
                        }
                    }
                }
                
                cleanupCompletedWorkers();
                Thread.sleep(POLLING_INTERVAL_MS);
                
            } catch (InterruptedException e) {
                System.out.println("[CONTROLLER] Interrupted, shutting down...");
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        shutdown();
    }
    
    private void assignWorker(BankRequest request) {
        WorkerMemory worker = new WorkerMemory(request, this);
        Future<?> future = executorService.submit(worker);
        activeWorkers.put(request.getRequestId(), future);
        System.out.println("[CONTROLLER] Assigned worker for request: " + request.getRequestId());
    }
    
    public synchronized void workerCompleted(long requestId) {
        System.out.println("[CONTROLLER] Worker completed for request: " + requestId);
        activeWorkers.remove(requestId);
    }
    
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
    
    public void shutdown() {
        System.out.println("[CONTROLLER] Shutting down daemon...");
        running = false;
        
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("[CONTROLLER] Daemon shutdown complete");
    }
    
    public void stop() {
        running = false;
    }
}
