package models;

import java.time.LocalDateTime;

/**
 * Represents a bank request in the system
 */
public class BankRequest {
    
    private long requestId;
    private String requestType; // "BUILD_ACCOUNT", "UPDATE_BALANCE", etc.
    private String status; // "NEW", "PROCESSING", "PROCESSED", "FAILED"
    private String accountNumber;
    private String accountHolder;
    private double initialBalance;
    private String parameters; // JSON string for additional parameters
    private LocalDateTime submittedAt;
    private LocalDateTime processedAt;
    private String result; // JSON string with processing result

    public BankRequest() {}

    public BankRequest(String requestType, String accountNumber, String accountHolder, double initialBalance) {
        this.requestType = requestType;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.initialBalance = initialBalance;
        this.status = "NEW";
        this.submittedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public long getRequestId() { return requestId; }
    public void setRequestId(long requestId) { this.requestId = requestId; }

    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getAccountHolder() { return accountHolder; }
    public void setAccountHolder(String accountHolder) { this.accountHolder = accountHolder; }

    public double getInitialBalance() { return initialBalance; }
    public void setInitialBalance(double initialBalance) { this.initialBalance = initialBalance; }

    public String getParameters() { return parameters; }
    public void setParameters(String parameters) { this.parameters = parameters; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    @Override
    public String toString() {
        return "BankRequest{" +
                "requestId=" + requestId +
                ", requestType='" + requestType + '\'' +
                ", status='" + status + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountHolder='" + accountHolder + '\'' +
                ", initialBalance=" + initialBalance +
                ", submittedAt=" + submittedAt +
                '}';
    }
}
