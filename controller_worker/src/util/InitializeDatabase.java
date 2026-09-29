package util;

import database.DatabaseConnection;
import java.sql.*;

/**
 * Database Initialization Utility
 * Run this once to set up the database schema
 */
public class InitializeDatabase {
    
    public static void main(String[] args) {
        System.out.println("=== Initializing Banking System Database ===\n");
        
        try {
            initializeSchema();
            System.out.println("\n✓ Database initialization completed successfully!");
            System.out.println("You can now run the daemon and client.");
            
        } catch (SQLException e) {
            System.err.println("✗ Database initialization failed!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Create database tables and sequences
     */
    private static void initializeSchema() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Creating sequence...");
            try {
                stmt.execute("CREATE SEQUENCE bank_requests_seq START WITH 1 INCREMENT BY 1");
                System.out.println("✓ Sequence created");
            } catch (SQLException e) {
                if (e.getErrorCode() == 955) { // Sequence already exists
                    System.out.println("  Sequence already exists (OK)");
                } else {
                    throw e;
                }
            }
            
            System.out.println("Creating bank_requests table...");
            String createTableSQL = """
                CREATE TABLE bank_requests (
                    request_id NUMBER PRIMARY KEY,
                    request_type VARCHAR2(50) NOT NULL,
                    status VARCHAR2(20) DEFAULT 'NEW',
                    account_number VARCHAR2(50) NOT NULL,
                    account_holder VARCHAR2(100) NOT NULL,
                    initial_balance NUMBER(15,2),
                    parameters CLOB,
                    submitted_at TIMESTAMP DEFAULT SYSTIMESTAMP,
                    processed_at TIMESTAMP,
                    result CLOB
                )
                """;
            
            try {
                stmt.execute(createTableSQL);
                System.out.println("✓ Table created");
            } catch (SQLException e) {
                if (e.getErrorCode() == 955) { // Table already exists
                    System.out.println("  Table already exists (OK)");
                } else {
                    throw e;
                }
            }
            
            System.out.println("Creating indexes...");
            try {
                stmt.execute("CREATE INDEX idx_status ON bank_requests(status)");
                System.out.println("✓ Index on status created");
            } catch (SQLException e) {
                if (e.getErrorCode() == 955) {
                    System.out.println("  Index already exists (OK)");
                } else {
                    throw e;
                }
            }
            
            System.out.println("\nVerifying schema...");
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) as count FROM user_tables WHERE table_name = 'BANK_REQUESTS'")) {
                if (rs.next() && rs.getInt("count") > 0) {
                    System.out.println("✓ Table verified");
                }
            }
        }
    }
}
