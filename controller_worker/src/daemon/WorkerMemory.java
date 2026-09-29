package daemon;

import models.BankRequest;
import database.BankRequestDAOMemory;

/**
 * Worker - processes a single bank request (Memory-based, no DB needed)
 */
public class WorkerMemory implements Runnable {
    
    private BankRequest request;
    private ControllerMemory controller;
    
    public WorkerMemory(BankRequest request, ControllerMemory controller) {
        this.request = request;
        this.controller = controller;
    }
    
    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("[WORKER-" + currentThread.getId() + "] Processing request: " + request.getRequestId());
        
        try {
            // Mark request as PROCESSING
            BankRequestDAOMemory.updateRequestStatus(request.getRequestId(), "PROCESSING");
            
            // Simulate processing
            processRequest();
            
            // Mark request as PROCESSED
            String result = "{\"status\": \"success\", \"account_id\": \"ACC-" + request.getAccountNumber() + "\"}";
            BankRequestDAOMemory.updateRequestResult(request.getRequestId(), "PROCESSED", result);
            
            System.out.println("[WORKER-" + currentThread.getId() + "] Completed request: " + request.getRequestId());
            
            // Notify controller
            controller.workerCompleted(request.getRequestId());
            
        } catch (Exception e) {
            System.err.println("[WORKER-" + currentThread.getId() + "] Error: " + e.getMessage());
            BankRequestDAOMemory.updateRequestResult(request.getRequestId(), "FAILED", e.getMessage());
            controller.workerCompleted(request.getRequestId());
        }
    }
    
    private void processRequest() {
        System.out.println("[WORKER] Building account for: " + request.getAccountHolder());
        simulateAccountCreation();
        simulateBalanceUpdate();
        System.out.println("[WORKER] Account processing completed for: " + request.getAccountNumber());
    }
    
    private void simulateAccountCreation() {
        try {
            Thread.sleep((long)(Math.random() * 1000 + 500));
            System.out.println("  - Account created: " + request.getAccountNumber());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void simulateBalanceUpdate() {
        try {
            Thread.sleep((long)(Math.random() * 1000 + 500));
            System.out.println("  - Balance updated: " + request.getInitialBalance());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public BankRequest getRequest() {
        return request;
    }
}
