package client;

import models.BankRequest;
import database.BankRequestDAO;
import java.sql.SQLException;

/**
 * StatusChecker - Query and display request status
 * Allows monitoring of request processing progress
 */
public class StatusChecker {
    
    public static void main(String[] args) {
        System.out.println("=== Request Status Checker ===\n");
        
        if (args.length == 0) {
            System.out.println("Usage: java StatusChecker <request_id>");
            System.out.println("Example: java StatusChecker 1");
            System.exit(1);
        }
        
        try {
            long requestId = Long.parseLong(args[0]);
            checkRequestStatus(requestId);
        } catch (NumberFormatException e) {
            System.err.println("Invalid request ID: " + args[0]);
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Check and display request status
     */
    private static void checkRequestStatus(long requestId) throws SQLException {
        BankRequest request = BankRequestDAO.getRequestById(requestId);
        
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
        System.out.println("  Initial Balance: " + request.getInitialBalance());
        System.out.println("  Submitted: " + request.getSubmittedAt());
        
        if (request.getProcessedAt() != null) {
            System.out.println("  Processed: " + request.getProcessedAt());
        }
        
        if (request.getResult() != null) {
            System.out.println("  Result: " + request.getResult());
        }
    }
}
