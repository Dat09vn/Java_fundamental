# 🚀 Quick Start Guide - NO SETUP REQUIRED!

## ⭐ Run Immediately (No Database Setup!)

Your banking system is ready to test right now with pure in-memory storage.

### Quick Test (30 seconds):

```bash
# Build the project
./build.sh

# Run the complete integration test (all in one command!)
./run_integration_test.sh
```

That's it! You'll see:
1. ✓ Client submitting 3 requests
2. ⚙️ Daemon processing them in parallel (3 workers)
3. 📊 Final status showing all processed requests

## 🎯 Two Testing Options

### Option 1: ⭐ IN-MEMORY TESTING (No Dependencies!)
**Perfect for immediate testing - No database needed at all**

**Single-process integration test:**
```bash
./run_integration_test.sh
```

**Multi-terminal testing (realistic setup):**
```bash
# Terminal 1: Start daemon
./run_test_daemon.sh

# Terminal 2: Submit requests (in another terminal)
./run_test_client.sh

# Terminal 3: Check status (in another terminal)
./run_test_status.sh 1              # Check request 1
./run_test_status.sh all             # View all requests
```

### Option 2: Oracle Database
**For production use (requires ojdbc8.jar)**

1. Place `ojdbc8.jar` in `lib/` directory
2. Setup Oracle user:
   ```sql
   CREATE USER banking_system IDENTIFIED BY password;
   GRANT CREATE SESSION TO banking_system;
   GRANT CREATE TABLE TO banking_system;
   CREATE SEQUENCE banking_system.bank_requests_seq START WITH 1;
   ```
3. Run:
   ```bash
   ./init_db.sh           # Initialize database (one-time)
   ./run_daemon.sh        # Terminal 1
   ./run_client.sh        # Terminal 2
   ./check_status.sh 1    # Terminal 3
   ```

## 📊 What Happens in the Integration Test?

```
REQUEST FLOW:
┌─────────────────────────────────────────────────────────┐
│ CLIENT: Submits 3 requests                              │
│   - John Doe (ACC001, $5,000)                           │
│   - Jane Smith (ACC002, $10,000)                        │
│   - Bob Johnson (ACC003, $7,500.50)                     │
└────────────────┬────────────────────────────────────────┘
                 │ All requests stored with status: NEW
                 ▼
┌─────────────────────────────────────────────────────────┐
│ DAEMON: Finds NEW requests                              │
│   - Controller picks up 3 requests                      │
│   - Spawns 3 workers in parallel                        │
└────────────────┬────────────────────────────────────────┘
                 │ Each worker processes independently
                 ▼
      ┌──────────┬──────────┬──────────┐
      ▼          ▼          ▼          ▼
    WORKER1    WORKER2    WORKER3   (parallel)
  Request 1   Request 2   Request 3
   (1-2 sec)   (1-2 sec)   (1-2 sec)
      │          │          │
      └──────────┼──────────┘
                 ▼
┌─────────────────────────────────────────────────────────┐
│ DATABASE: All requests marked PROCESSED                 │
│   ✓ ACC001 - Success - Result stored                    │
│   ✓ ACC002 - Success - Result stored                    │
│   ✓ ACC003 - Success - Result stored                    │
└─────────────────────────────────────────────────────────┘
```

## 📝 Testing Scenarios

### Test 1: View Specific Request Status
```bash
./run_test_status.sh 1
```

Output:
```
=== Request Status Checker ===

Request Details:
  Request ID: 1
  Type: BUILD_ACCOUNT
  Status: PROCESSED
  Account Number: ACC001
  Account Holder: John Doe
  Initial Balance: 5000.0
  Submitted: 2026-06-21T10:30:15
  Processed: 2026-06-21T10:30:20
  Result: {"status": "success", "account_id": "ACC-ACC001"}
```

### Test 2: View All Requests
```bash
./run_test_status.sh all
```

Output: Table format showing all requests and status

### Test 3: Daemon Real-Time Monitoring
```bash
./run_test_daemon.sh
```

You'll see live output:
```
[CONTROLLER] Found 3 new request(s)
[CONTROLLER] Assigned worker for request: 1
[WORKER-12] Processing request: 1
[WORKER-12]   - Account created: ACC001
[WORKER-12]   - Balance updated: 5000.0
[WORKER-12] Completed request: 1
```

## 🔧 Architecture Explained

```
┌──────────────────────────────────────────────────────┐
│                    SYSTEM                            │
├──────────────────────────────────────────────────────┤
│                                                      │
│  ┌─────────────┐         ┌──────────────────────┐  │
│  │   CLIENT    │────────▶│   IN-MEMORY DATABASE │  │
│  │ (9 PM cron) │         │  (Thread-Safe Map)   │  │
│  └─────────────┘         └──────────────────────┘  │
│                                  ▲                   │
│  ┌─────────────────────────────────────────────┐  │
│  │          DAEMON (Always Running)             │  │
│  ├─────────────────────────────────────────────┤  │
│  │  Controller (polls every 2 sec)             │  │
│  │  │                                          │  │
│  │  ├──▶ Worker-1 (Request 1)                 │  │
│  │  ├──▶ Worker-2 (Request 2)                 │  │
│  │  └──▶ Worker-3 (Request 3)                 │  │
│  │  │                                          │  │
│  │  └── Updates status to PROCESSED            │  │
│  └─────────────────────────────────────────────┘  │
│                                                      │
└──────────────────────────────────────────────────────┘
```

## 📁 File Structure

```
src/
├── models/
│   └── BankRequest.java            # Data model
├── database/
│   ├── DatabaseConnection.java     # Oracle connection
│   ├── BankRequestDAO.java         # Oracle data access
│   └── BankRequestDAOMemory.java   # In-memory storage (for testing)
├── daemon/
│   ├── Daemon.java                 # Oracle daemon entry point
│   ├── DaemonMemoryMain.java       # Memory daemon entry point
│   ├── Controller.java             # Oracle controller
│   ├── ControllerMemory.java       # Memory controller
│   ├── Worker.java                 # Oracle worker
│   └── WorkerMemory.java           # Memory worker
├── client/
│   ├── Client.java                 # Oracle client
│   ├── ClientMemory.java           # Memory client
│   ├── StatusChecker.java          # Oracle status checker
│   └── StatusCheckerMemory.java    # Memory status checker
└── test/
    └── IntegrationTest.java        # Full workflow test
```

## ✨ Features

✅ **Parallel Processing** - Up to 3 concurrent workers  
✅ **Thread-Safe** - ConcurrentHashMap for memory storage  
✅ **Request Tracking** - NEW → PROCESSING → PROCESSED  
✅ **Zero External Dependencies** - Just Java! (for memory mode)  
✅ **Scalable** - Easy to switch between memory and Oracle backends  
✅ **Clean Code** - DAO pattern, separation of concerns  

## 🎓 What You Learned

1. **Architecture**: Client-Daemon with Controller-Worker pattern
2. **Concurrency**: Java ExecutorService for thread management
3. **Design Patterns**: DAO pattern for data abstraction
4. **Testing**: In-memory testing vs database-backed systems

## 🚀 Next Steps

1. **Run the integration test**: `./run_integration_test.sh`
2. **Try multi-terminal testing**: Run daemon, client, and checker in separate terminals
3. **Add Oracle**: Place ojdbc8.jar and run with real database
4. **Customize**: Modify request types, add business logic to workers
5. **Production**: Add logging, error handling, REST API

## 📞 Troubleshooting

**"Build failed"**
- Ensure Java 11+ is installed: `java -version`
- Ensure javac is in PATH: `which javac`

**"ClassNotFoundException"**
- Run `./build.sh` first
- Check bin/ directory has compiled classes

**"No threads running"**
- Integration test runs sequentially (by design)
- For real parallel testing, run daemon and client in separate terminals

---

**Ready? Start here:**
```bash
./run_integration_test.sh
```

See requests being submitted, processed in parallel, and completed! 🎉
