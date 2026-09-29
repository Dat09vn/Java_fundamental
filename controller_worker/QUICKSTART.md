# Quick Start Guide

## Prerequisites

1. **Java 11+** installed
2. **Oracle Database** running locally (or update DB connection details in `src/database/DatabaseConnection.java`)
3. **Oracle JDBC Driver** (`ojdbc8.jar`) in `lib/` directory

## Quick Setup (5 minutes)

### Step 1: Database Setup
```sql
-- Create database user
CREATE USER banking_system IDENTIFIED BY password;
GRANT CREATE SESSION TO banking_system;
GRANT CREATE TABLE TO banking_system;
GRANT CREATE SEQUENCE TO banking_system;

-- Create sequence
CREATE SEQUENCE banking_system.bank_requests_seq START WITH 1 INCREMENT BY 1;
```

### Step 2: Prepare JDBC Driver
```bash
mkdir -p lib
# Copy ojdbc8.jar to lib directory
cp /path/to/ojdbc8.jar lib/
```

### Step 3: Make Scripts Executable
```bash
chmod +x build.sh run_daemon.sh run_client.sh check_status.sh
```

### Step 4: Build
```bash
./build.sh
```

## Running the System

### Terminal 1: Start Daemon
```bash
./run_daemon.sh
```

Wait for: `[CONTROLLER] Max workers: 3`

### Terminal 2: Submit Requests
```bash
./run_client.sh
```

You should see requests submitted with IDs (1, 2, 3, etc.)

### Terminal 3: Check Status
```bash
./check_status.sh 1
./check_status.sh 2
./check_status.sh 3
```

Immediately after submission, requests are in "NEW" state.  
After processing, requests change to "PROCESSED" state.

## What's Happening?

1. **Client** submits 3 bank requests to the database (NEW status)
2. **Daemon Controller** polls the database every 5 seconds
3. **Controller** finds NEW requests and spawns Worker threads
4. **Workers** process requests in parallel (simulates 1-2 second work)
5. **Controller** marks requests as PROCESSED when workers complete
6. **StatusChecker** queries database to view progress

## Expected Timeline

```
T+0s    - Run client.sh → 3 requests inserted (NEW)
T+0s    - Terminal 1 shows: "Found 3 new request(s)"
T+0s    - 3 workers assigned, begin processing
T+1-2s  - Workers simulate account creation and balance update
T+2-3s  - Workers complete, mark requests PROCESSED
T+5s    - Controller checks again (no more NEW requests)
```

## Troubleshooting

**"Oracle JDBC Driver not found"**
- Copy `ojdbc8.jar` to `lib/` directory
- Run `build.sh` again

**"Database connection refused"**
- Check Oracle DB is running: `sqlplus system/password`
- Verify connection details in `src/database/DatabaseConnection.java`
- Ensure user `banking_system` exists

**"Table already exists"**
- Normal message, you can ignore it
- To recreate: `DROP TABLE banking_system.bank_requests;`

## Files Generated

After first run:
- `bin/` - Compiled classes
- Database tables created automatically
- Status stored in Oracle DB

## Next Commands

Monitor database directly:
```bash
sqlplus banking_system/password
SQL> SELECT * FROM bank_requests;
SQL> SELECT COUNT(*) FROM bank_requests WHERE status = 'PROCESSED';
```

## Architecture Summary

```
┌─────────┐     ┌──────────────┐     ┌────────────┐
│ Client  │────▶│   Database   │◀────│  Daemon    │
│ 9PM     │     │  (Requests)  │     │ (Always on)│
└─────────┘     └──────────────┘     └────────────┘
                                            │
                                      ┌─────┴────────┐
                                      │   Controller │
                                      └─────┬────────┘
                                            │
                         ┌──────────────────┼──────────────────┐
                         │                  │                  │
                    ┌────▼──┐         ┌────▼──┐         ┌────▼──┐
                    │Worker1│         │Worker2│         │Worker3│
                    └────────┘        └────────┘        └────────┘
                    Parallel Processing (up to 3 concurrent)
```

## Performance Tuning

Adjust in `src/daemon/Controller.java`:

```java
private final int MAX_WORKERS = 3;           // Increase for more parallelism
private final int POLLING_INTERVAL_MS = 5000; // Decrease for faster response
```

Example: For high-throughput, use:
```java
private final int MAX_WORKERS = 10;
private final int POLLING_INTERVAL_MS = 1000;
```

## Production Considerations

Before deploying to production:
- [ ] Add comprehensive logging (Log4j, SLF4j)
- [ ] Implement connection pooling (HikariCP)
- [ ] Add retry logic with exponential backoff
- [ ] Implement request timeout mechanisms
- [ ] Add health check endpoints
- [ ] Implement graceful shutdown hooks
- [ ] Add monitoring and metrics
- [ ] Secure database credentials (external config files)
- [ ] Add comprehensive error handling
- [ ] Implement request audit trail
