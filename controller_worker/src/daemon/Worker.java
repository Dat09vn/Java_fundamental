package daemon;

import models.BankRequest;
import database.BankRequestDAO;
import java.sql.SQLException;

/**
 * Worker - processes a single bank request
 * Each worker handles one request independently
 */
public class Worker implements Runnable {
    
    private BankRequest request;
    private Controller controller;
    
    public Worker(BankRequest request, Controller controller) {
        this.request = request;
        this.controller = controller;
    }
    
    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("[WORKER-" + currentThread.getId() + "] Processing request: " + request.getRequestId());
        
        try {
            // Mark request as PROCESSING
            BankRequestDAO.updateRequestStatus(request.getRequestId(), "PROCESSING");
            
            // Simulate processing (build account, update balance, etc.)
            processRequest();
            
            // Mark request as PROCESSED
            String result = "{\"status\": \"success\", \"account_id\": \"ACC-" + request.getAccountNumber() + "\"}";
            BankRequestDAO.updateRequestResult(request.getRequestId(), "PROCESSED", result);
            
            System.out.println("[WORKER-" + currentThread.getId() + "] Completed request: " + request.getRequestId());
            
            // Notify controller that this worker is done
            controller.workerCompleted(request.getRequestId());
            
        } catch (SQLException e) {
            System.err.println("[WORKER-" + currentThread.getId() + "] Error processing request: " + e.getMessage());
            try {
                BankRequestDAO.updateRequestResult(request.getRequestId(), "FAILED", e.getMessage());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            controller.workerCompleted(request.getRequestId());
        }
    }
    
    /**
     * Simulate the actual account building process
     */
    private void processRequest() throws SQLException {
        // Simulate some processing time
        System.out.println("[WORKER] Building account for: " + request.getAccountHolder());
        
        // Simulate account creation steps
        simulateAccountCreation();
        simulateBalanceUpdate();
        
        System.out.println("[WORKER] Account processing completed for: " + request.getAccountNumber());
    }
    
    private void simulateAccountCreation() {
        try {
            // Simulate 1-2 second processing
            Thread.sleep((long)(Math.random() * 1000 + 500));
            System.out.println("  - Account created: " + request.getAccountNumber());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void simulateBalanceUpdate() {
        try {
            // Simulate 1-2 second processing
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
