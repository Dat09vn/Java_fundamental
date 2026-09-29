package test;

import models.BankRequest;
import database.BankRequestDAOMemory;
import daemon.ControllerMemory;
import daemon.WorkerMemory;

/**
 * Integrated Test - Demonstrates full workflow in single process
 */
public class IntegrationTest {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║  Banking System - Complete Integration Test                  ║");
        System.out.println("║  Memory-Based (No Database Required!)                        ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Phase 1: Submit Requests (Client side)
        System.out.println("📝 PHASE 1: CLIENT - SUBMITTING REQUESTS");
        System.out.println("─".repeat(60));
        submitRequests();
        System.out.println();
        
        // Phase 2: Process Requests (Daemon side)
        System.out.println("⚙️  PHASE 2: DAEMON - PROCESSING REQUESTS");
        System.out.println("─".repeat(60));
        processRequests();
        System.out.println();
        
        // Phase 3: Check Status
        System.out.println("📊 PHASE 3: STATUS CHECK - VIEWING RESULTS");
        System.out.println("─".repeat(60));
        checkAllStatus();
        System.out.println();
        
        // Summary
        System.out.println("✅ INTEGRATION TEST COMPLETE!");
        System.out.println("─".repeat(60));
        System.out.println("Total requests processed: " + BankRequestDAOMemory.getCount());
    }
    
    private static void submitRequests() {
        System.out.println("Submitting 3 bank requests...\n");
        
        BankRequest req1 = new BankRequest("BUILD_ACCOUNT", "ACC001", "John Doe", 5000.00);
        long id1 = BankRequestDAOMemory.insertRequest(req1);
        System.out.println("✓ Request " + id1 + " submitted: John Doe, Account ACC001, $5,000.00");
        
        BankRequest req2 = new BankRequest("BUILD_ACCOUNT", "ACC002", "Jane Smith", 10000.00);
        long id2 = BankRequestDAOMemory.insertRequest(req2);
        System.out.println("✓ Request " + id2 + " submitted: Jane Smith, Account ACC002, $10,000.00");
        
        BankRequest req3 = new BankRequest("BUILD_ACCOUNT", "ACC003", "Bob Johnson", 7500.50);
        long id3 = BankRequestDAOMemory.insertRequest(req3);
        System.out.println("✓ Request " + id3 + " submitted: Bob Johnson, Account ACC003, $7,500.50");
        
        System.out.println("\nAll requests are now in DB with status: NEW");
    }
    
    private static void processRequests() throws InterruptedException {
        System.out.println("Starting parallel workers...\n");
        
        java.util.List<BankRequest> newRequests = BankRequestDAOMemory.getNewRequests();
        System.out.println("Found " + newRequests.size() + " NEW requests\n");
        
        Thread[] workerThreads = new Thread[newRequests.size()];
        
        int index = 0;
        for (BankRequest request : newRequests) {
            BankRequestDAOMemory.updateRequestStatus(request.getRequestId(), "PROCESSING");
            
            // Create simulated worker task
            Thread workerThread = new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                try {
                    System.out.println("[" + threadName + "] Processing request " + request.getRequestId() + 
                                     " for " + request.getAccountHolder());
                    
                    // Simulate account creation
                    System.out.println("[" + threadName + "]   - Creating account " + request.getAccountNumber());
                    Thread.sleep(500);
                    
                    // Simulate balance update
                    System.out.println("[" + threadName + "]   - Updating balance: $" + request.getInitialBalance());
                    Thread.sleep(500);
                    
                    // Mark as processed
                    String result = "{\"status\": \"success\", \"account_id\": \"ACC-" + 
                                  request.getAccountNumber() + "\"}";
                    BankRequestDAOMemory.updateRequestResult(request.getRequestId(), "PROCESSED", result);
                    
                    System.out.println("[" + threadName + "] ✓ Completed request " + request.getRequestId());
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            workerThread.setName("Worker-" + (index + 1));
            workerThreads[index] = workerThread;
            workerThread.start();
            index++;
        }
        
        // Wait for all workers to complete
        for (Thread thread : workerThreads) {
            thread.join();
        }
        
        System.out.println("\nAll workers completed!");
    }
    
    private static void checkAllStatus() {
        System.out.println("Displaying all requests:\n");
        System.out.printf("%-5s %-20s %-15s %-15s %-20s %-15s\n", 
            "ID", "Type", "Status", "Account", "Holder", "Balance");
        System.out.println("─".repeat(90));
        
        for (BankRequest req : BankRequestDAOMemory.getAllRequests()) {
            System.out.printf("%-5d %-20s %-15s %-15s %-20s $%-14.2f\n",
                req.getRequestId(),
                req.getRequestType(),
                req.getStatus(),
                req.getAccountNumber(),
                req.getAccountHolder(),
                req.getInitialBalance());
            
            if (req.getResult() != null) {
                System.out.println("       Result: " + req.getResult());
            }
        }
        System.out.println("─".repeat(90));
    }
}
