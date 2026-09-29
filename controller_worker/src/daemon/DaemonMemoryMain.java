package daemon;

/**
 * Main entry point for Memory-based Testing
 * No database or driver needed!
 */
public class DaemonMemoryMain {
    
    public static void main(String[] args) {
        System.out.println("=== Banking System Daemon (Memory-Based Testing) ===");
        System.out.println("✓ Pure in-memory storage - No external dependencies!");
        System.out.println("Starting background processing service...\n");
        
        try {
            ControllerMemory controller = new ControllerMemory();
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutdown signal received");
                controller.stop();
            }));
            
            controller.start();
            
        } catch (Exception e) {
            System.err.println("Failed to start daemon: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
