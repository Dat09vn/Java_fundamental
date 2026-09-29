package client;

import models.BankRequest;
import database.BankRequestDAOMemory;

/**
 * Status Checker for Memory-Based Testing - No database needed!
 */
public class StatusCheckerMemory {
    
    public static void main(String[] args) {
        System.out.println("=== Request Status Checker (Memory-Based Testing) ===\n");
        
        if (args.length == 0) {
            System.out.println("Usage: java StatusCheckerMemory <request_id>");
            System.out.println("Example: java StatusCheckerMemory 1");
            System.out.println("\nOr view all requests: java StatusCheckerMemory all");
            System.exit(1);
        }
        
        try {
            if ("all".equals(args[0])) {
                showAllRequests();
            } else {
                long requestId = Long.parseLong(args[0]);
                checkRequestStatus(requestId);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid request ID: " + args[0]);
        }
    }
    
    private static void checkRequestStatus(long requestId) {
        BankRequest request = BankRequestDAOMemory.getRequestById(requestId);
        
        if (request == null) {
            System.out.println("Request not found - ID: " + requestId);
            return;
        }
        
        System.out.println("Request Details:");
        System.out.println("  Request ID: " + request.getRequestId());
        System.out.println("  Type: " + request.getRequestType());
        System.out.println("  Status: " + request.getStatus());
        System.out.println("  Account Number: " + request.getAccountNumber());
        System.out.println("  Account Holder: " + request.getAccountHolder());
        System.out.println("  Initial Balance: $" + request.getInitialBalance());
        System.out.println("  Submitted: " + request.getSubmittedAt());
        
        if (request.getProcessedAt() != null) {
            System.out.println("  Processed: " + request.getProcessedAt());
        }
        
        if (request.getResult() != null) {
            System.out.println("  Result: " + request.getResult());
        }
    }
    
    private static void showAllRequests() {
        System.out.println("All Requests:");
        System.out.println("─".repeat(120));
        System.out.printf("%-5s %-20s %-15s %-15s %-20s %-15s\n", 
            "ID", "Type", "Status", "Account", "Holder", "Balance");
        System.out.println("─".repeat(120));
        
        for (BankRequest req : BankRequestDAOMemory.getAllRequests()) {
            System.out.printf("%-5d %-20s %-15s %-15s %-20s $%-14.2f\n",
                req.getRequestId(),
                req.getRequestType(),
                req.getStatus(),
                req.getAccountNumber(),
                req.getAccountHolder(),
                req.getInitialBalance());
        }
        System.out.println("─".repeat(120));
        System.out.println("Total: " + BankRequestDAOMemory.getCount() + " requests");
    }
}
