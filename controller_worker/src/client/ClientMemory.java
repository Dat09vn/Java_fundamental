package client;

import models.BankRequest;
import database.BankRequestDAOMemory;

/**
 * Client for Memory-Based Testing - No database needed!
 */
public class ClientMemory {
    
    public static void main(String[] args) {
        System.out.println("=== Banking System Client (Memory-Based Testing) ===");
        System.out.println("Submitting batch requests...\n");
        
        try {
            submitSampleRequests();
            
            System.out.println("\nRequests submitted successfully!");
            System.out.println("Daemon will process them automatically.");
            
        } catch (Exception e) {
            System.err.println("Error submitting requests: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void submitSampleRequests() {
        
        BankRequest request1 = new BankRequest(
            "BUILD_ACCOUNT",
            "ACC001",
            "John Doe",
            5000.00
        );
        long requestId1 = BankRequestDAOMemory.insertRequest(request1);
        System.out.println("Request 1 submitted - ID: " + requestId1 + " | Account: " + request1.getAccountNumber());
        
        BankRequest request2 = new BankRequest(
            "BUILD_ACCOUNT",
            "ACC002",
            "Jane Smith",
            10000.00
        );
        long requestId2 = BankRequestDAOMemory.insertRequest(request2);
        System.out.println("Request 2 submitted - ID: " + requestId2 + " | Account: " + request2.getAccountNumber());
        
        BankRequest request3 = new BankRequest(
            "BUILD_ACCOUNT",
            "ACC003",
            "Bob Johnson",
            7500.50
        );
        long requestId3 = BankRequestDAOMemory.insertRequest(request3);
        System.out.println("Request 3 submitted - ID: " + requestId3 + " | Account: " + request3.getAccountNumber());
    }
}
