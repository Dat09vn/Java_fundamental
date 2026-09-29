package database;

import java.sql.*;

/**
 * Database connection utility for Oracle DB
 */
public class DatabaseConnection {
    
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "Oracle123";
    
    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found");
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * Initialize database schema (run once)
     */
    public static void initializeSchema() throws SQLException {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS bank_requests (
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
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Database schema initialized successfully");
        }
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
