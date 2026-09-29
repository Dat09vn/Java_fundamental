package database;

import models.BankRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for BankRequest
 */
public class BankRequestDAO {
    
    /**
     * Insert a new bank request into the database
     */
    public static long insertRequest(BankRequest request) throws SQLException {
        // anonymous PL/SQL block.
        String insertSQL = """
            BEGIN
              INSERT INTO bank_requests (request_id, request_type, status, account_number, 
                                         account_holder, initial_balance, submitted_at)
              VALUES (bank_requests_seq.NEXTVAL, ?, ?, ?, ?, ?, SYSTIMESTAMP)
              RETURNING request_id INTO ?;
            END;
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(insertSQL)) {
            
            cstmt.setString(1, request.getRequestType());
            cstmt.setString(2, request.getStatus());
            cstmt.setString(3, request.getAccountNumber());
            cstmt.setString(4, request.getAccountHolder());
            cstmt.setDouble(5, request.getInitialBalance());
            cstmt.registerOutParameter(6, java.sql.Types.NUMERIC);
            
            cstmt.executeUpdate();
            
            long generatedId = cstmt.getLong(6);
            return generatedId > 0 ? generatedId : -1;
        }
    }
    
    /**
     * Fetch all NEW requests from the database
     */
    public static List<BankRequest> getNewRequests() throws SQLException {
        List<BankRequest> requests = new ArrayList<>();
        String selectSQL = "SELECT * FROM bank_requests WHERE status = 'NEW' ORDER BY submitted_at";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            
            while (rs.next()) {
                BankRequest request = mapResultSetToRequest(rs);
                requests.add(request);
            }
        }
        return requests;
    }
    
    /**
     * Update request status
     */
    public static void updateRequestStatus(long requestId, String status) throws SQLException {
        String updateSQL = "UPDATE bank_requests SET status = ?, processed_at = SYSTIMESTAMP WHERE request_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            
            pstmt.setString(1, status);
            pstmt.setLong(2, requestId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Update request with result
     */
    public static void updateRequestResult(long requestId, String status, String result) throws SQLException {
        String updateSQL = "UPDATE bank_requests SET status = ?, result = ?, processed_at = SYSTIMESTAMP WHERE request_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, result);
            pstmt.setLong(3, requestId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Get request by ID
     */
    public static BankRequest getRequestById(long requestId) throws SQLException {
        String selectSQL = "SELECT * FROM bank_requests WHERE request_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            
            pstmt.setLong(1, requestId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRequest(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * Helper method to map ResultSet to BankRequest object
     */
    private static BankRequest mapResultSetToRequest(ResultSet rs) throws SQLException {
        BankRequest request = new BankRequest();
        request.setRequestId(rs.getLong("request_id"));
        request.setRequestType(rs.getString("request_type"));
        request.setStatus(rs.getString("status"));
        request.setAccountNumber(rs.getString("account_number"));
        request.setAccountHolder(rs.getString("account_holder"));
        request.setInitialBalance(rs.getDouble("initial_balance"));
        request.setParameters(rs.getString("parameters"));
        request.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
        
        Timestamp processedAt = rs.getTimestamp("processed_at");
        if (processedAt != null) {
            request.setProcessedAt(processedAt.toLocalDateTime());
        }
        
        request.setResult(rs.getString("result"));
        return request;
    }
}
