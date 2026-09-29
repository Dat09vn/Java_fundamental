# Banking System Worker-Controller - Draft Implementation

## Project Structure

```
src/
├── models/
│   └── BankRequest.java              # Data model for bank requests
├── database/
│   ├── DatabaseConnection.java       # Oracle DB connection manager
│   └── BankRequestDAO.java           # Data Access Object for requests
├── daemon/
│   ├── Daemon.java                   # Daemon main entry point
│   ├── Controller.java               # Parent process managing workers
│   └── Worker.java                   # Child process handling individual requests
└── client/
    ├── Client.java                   # Submit batch requests
    └── StatusChecker.java            # Query request status
```

## Setup Instructions

### 1. Database Setup

Create Oracle database user and schema:

```sql
-- Create user
CREATE USER banking_system IDENTIFIED BY password;

-- Grant permissions
GRANT CREATE SESSION TO banking_system;
GRANT CREATE TABLE TO banking_system;
GRANT CREATE SEQUENCE TO banking_system;

-- Create sequence for request IDs
CREATE SEQUENCE banking_system.bank_requests_seq START WITH 1 INCREMENT BY 1;
```

### 2. Compile Code

```bash
# Ensure Oracle JDBC driver is in your classpath
javac -d bin src/models/*.java src/database/*.java src/daemon/*.java src/client/*.java
```

**Note**: Add Oracle JDBC driver to classpath:
```bash
javac -cp ojdbc8.jar:. -d bin src/**/*.java
```

### 3. Database Initialization (Optional)

Uncomment the initialization line in `Daemon.java` and run once:
```bash
java -cp .:ojdbc8.jar database.DatabaseConnection
```

## Running the System

### Terminal 1: Start Daemon (Always Running)

```bash
java -cp bin:ojdbc8.jar daemon.Daemon
```

**Expected Output:**
```
=== Banking System Daemon ===
Starting background processing service...

[CONTROLLER] Daemon started. Monitoring for new requests...
[CONTROLLER] Max workers: 3
```

### Terminal 2: Submit Client Requests (Simulates 9 PM cron job)

```bash
java -cp bin:ojdbc8.jar client.Client
```

**Expected Output:**
```
=== Banking System Client ===
Submitting batch requests...

Request 1 submitted - ID: 1 | Account: ACC001
Request 2 submitted - ID: 2 | Account: ACC002
Request 3 submitted - ID: 3 | Account: ACC003

Requests submitted successfully!
```

### Monitor Daemon Processing

In Terminal 1, you should see:
```
[CONTROLLER] Found 3 new request(s)
[CONTROLLER] Assigned worker for request: 1
[CONTROLLER] Assigned worker for request: 2
[CONTROLLER] Assigned worker for request: 3
[WORKER-12] Processing request: 1
[WORKER-13] Processing request: 2
[WORKER-14] Processing request: 3
  - Account created: ACC001
  - Account created: ACC002
  - Account created: ACC003
  - Balance updated: 5000.0
  - Balance updated: 10000.0
  - Balance updated: 7500.5
[WORKER-12] Completed request: 1
[WORKER-13] Completed request: 2
[WORKER-14] Completed request: 3
[CONTROLLER] Worker completed for request: 1
```

### Terminal 3: Check Request Status

```bash
java -cp bin:ojdbc8.jar client.StatusChecker 1
java -cp bin:ojdbc8.jar client.StatusChecker 2
java -cp bin:ojdbc8.jar client.StatusChecker 3
```

**Expected Output:**
```
=== Request Status Checker ===

Request Details:
  Request ID: 1
  Type: BUILD_ACCOUNT
  Status: PROCESSED
  Account Number: ACC001
  Account Holder: John Doe
  Initial Balance: 5000.0
  Submitted: 2026-06-21T10:30:15.123456
  Processed: 2026-06-21T10:30:20.789012
  Result: {"status": "success", "account_id": "ACC-ACC001"}
```

## Basic Case Features

✅ **Client**: Submits 3 sample bank requests  
✅ **Daemon**: Monitors database for new requests  
✅ **Controller**: Manages up to 3 concurrent workers  
✅ **Workers**: Process requests in parallel  
✅ **Database**: Stores request status, tracks processing  
✅ **Status Checker**: Query request progress  

## Customization

### Change Max Workers
In `Controller.java`:
```java
private final int MAX_WORKERS = 5; // Increase concurrency
```

### Change Polling Interval
In `Controller.java`:
```java
private final int POLLING_INTERVAL_MS = 3000; // Check every 3 seconds
```

### Add Cron Job (Linux)
Edit crontab with `crontab -e` and add:
```cron
0 21 * * * cd /path/to/project && java -cp bin:ojdbc8.jar client.Client
```

## Database Queries

Check request status manually:
```sql
SELECT * FROM bank_requests;
SELECT * FROM bank_requests WHERE status = 'NEW';
SELECT * FROM bank_requests WHERE status = 'PROCESSED';
```

## Next Steps

1. Improve error handling and logging
2. Add request parameter support
3. Implement transaction handling
4. Add configuration file support
5. Enhance database schema with account and transaction tables
