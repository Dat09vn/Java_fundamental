package client;

import models.BankRequest;
import database.BankRequestDAO;
import java.sql.SQLException;

/**
 * Client - Submits bank requests to the system
 * Typically triggered by cron job at 9 PM daily
 */
public class Client {
    
    public static void main(String[] args) {
        System.out.println("=== Banking System Client ===");
        System.out.println("Submitting batch requests...\n");
        
        try {
            // Create sample requests (simulating 9 PM cron job execution)
            submitSampleRequests();
            
            System.out.println("\nRequests submitted successfully!");
            System.out.println("Daemon will process them automatically.");
            
        } catch (SQLException e) {
            System.err.println("Error submitting requests: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Submit sample bank requests
     */
    private static void submitSampleRequests() throws SQLException {
        
        // Sample request 1
        BankRequest request1 = new BankRequest(
            "BUILD_ACCOUNT",
            "ACC001",
            "John Doe",
            5000.00
        );
        long requestId1 = BankRequestDAO.insertRequest(request1);
        System.out.println("Request 1 submitted - ID: " + requestId1 + " | Account: " + request1.getAccountNumber());
        
        // Sample request 2
        BankRequest request2 = new BankRequest(
            "BUILD_ACCOUNT",
            "ACC002",
            "Jane Smith",
            10000.00
        );
        long requestId2 = BankRequestDAO.insertRequest(request2);
        System.out.println("Request 2 submitted - ID: " + requestId2 + " | Account: " + request2.getAccountNumber());
        
        // Sample request 3
        BankRequest request3 = new BankRequest(
            "BUILD_ACCOUNT",
            "ACC003",
            "Bob Johnson",
            7500.50
        );
        long requestId3 = BankRequestDAO.insertRequest(request3);
        System.out.println("Request 3 submitted - ID: " + requestId3 + " | Account: " + request3.getAccountNumber());
    }
}
