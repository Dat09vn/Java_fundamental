package database;

import models.BankRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.time.LocalDateTime;

/**
 * In-Memory DAO for BankRequest
 * Perfect for testing - NO database driver needed!
 */
public class BankRequestDAOMemory {
    
    private static final Map<Long, BankRequest> requestStore = new ConcurrentHashMap<>();
    private static final AtomicLong requestIdCounter = new AtomicLong(1);
    
    /**
     * Insert a new bank request into memory
     */
    public static long insertRequest(BankRequest request) {
        long requestId = requestIdCounter.getAndIncrement();
        
        request.setRequestId(requestId);
        request.setStatus("NEW");
        request.setSubmittedAt(LocalDateTime.now());
        
        requestStore.put(requestId, request);
        return requestId;
    }
    
    /**
     * Fetch all NEW requests from memory
     */
    public static List<BankRequest> getNewRequests() {
        List<BankRequest> requests = new ArrayList<>();
        
        for (BankRequest request : requestStore.values()) {
            if ("NEW".equals(request.getStatus())) {
                requests.add(request);
            }
        }
        
        return requests;
    }
    
    /**
     * Update request status
     */
    public static void updateRequestStatus(long requestId, String status) {
        BankRequest request = requestStore.get(requestId);
        if (request != null) {
            request.setStatus(status);
        }
    }
    
    /**
     * Update request with result
     */
    public static void updateRequestResult(long requestId, String status, String result) {
        BankRequest request = requestStore.get(requestId);
        if (request != null) {
            request.setStatus(status);
            request.setResult(result);
            request.setProcessedAt(LocalDateTime.now());
        }
    }
    
    /**
     * Get request by ID
     */
    public static BankRequest getRequestById(long requestId) {
        return requestStore.get(requestId);
    }
    
    /**
     * Get all requests
     */
    public static List<BankRequest> getAllRequests() {
        return new ArrayList<>(requestStore.values());
    }
    
    /**
     * Clear all data (for testing)
     */
    public static void clear() {
        requestStore.clear();
        requestIdCounter.set(1);
    }
    
    /**
     * Get total count
     */
    public static int getCount() {
        return requestStore.size();
    }
}
