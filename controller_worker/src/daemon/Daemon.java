package daemon;

import database.DatabaseConnection;

/**
 * Daemon main - Always running background service
 * Starts the controller to monitor and process requests
 */
public class Daemon {
    
    public static void main(String[] args) {
        System.out.println("=== Banking System Daemon ===");
        System.out.println("Starting background processing service...\n");
        
        try {
            // Initialize database schema (optional, run once)
            // DatabaseConnection.initializeSchema();
            
            // Create and start controller
            Controller controller = new Controller();
            
            // Add shutdown hook for graceful termination
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutdown signal received");
                controller.stop();
            }));
            
            // Start monitoring for requests
            controller.start();
            
        } catch (Exception e) {
            System.err.println("Failed to start daemon: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
