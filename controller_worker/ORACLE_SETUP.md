# Option 2: Oracle Database Setup Guide

## Step 1: Get ojdbc8.jar

### Method A: Download from Maven Repository (Easiest)

```bash
# Create lib directory if it doesn't exist
mkdir -p lib

# Download ojdbc8.jar using wget
cd lib
wget https://repo1.maven.org/maven2/com/oracle/database/jdbc/ojdbc8/21.0.0.0/ojdbc8-21.0.0.0.jar

# Rename it to ojdbc8.jar for simplicity
mv ojdbc8-21.0.0.0.jar ojdbc8.jar

# Verify download
ls -lh ojdbc8.jar
```

### Method B: Use curl (Alternative)

```bash
cd lib
curl -O https://repo1.maven.org/maven2/com/oracle/database/jdbc/ojdbc8/21.0.0.0/ojdbc8-21.0.0.0.jar
mv ojdbc8-21.0.0.0.jar ojdbc8.jar
```

### Method C: From Oracle Official Website

1. Visit: https://www.oracle.com/database/technologies/appdev/jdbc.html
2. Download ojdbc8.jar
3. Place in `lib/` directory

---

## Step 2: Prepare Oracle Database

You'll need access to an Oracle database with administrator privileges.

### Option A: Using Local Oracle Database

If you have Oracle Database installed locally:

```bash
# Connect as SYSDBA
sqlplus / as sysdba

# Then run the setup commands below
```

### Option B: Using Oracle Cloud or Remote Database

If you have a remote Oracle database:

```bash
sqlplus username@connection_string
```

---

## Step 3: Create Database User and Schema

Run these SQL commands as SYSDBA:

```sql
-- Create user for banking system
CREATE USER banking_system IDENTIFIED BY password;

-- Grant basic permissions
GRANT CREATE SESSION TO banking_system;
GRANT CREATE TABLE TO banking_system;
GRANT CREATE SEQUENCE TO banking_system;
GRANT UNLIMITED TABLESPACE TO banking_system;

-- Create sequence for request IDs
CREATE SEQUENCE banking_system.bank_requests_seq 
    START WITH 1 
    INCREMENT BY 1
    NOCACHE
    NOORDER;

-- Verify creation
SELECT * FROM dba_users WHERE username = 'BANKING_SYSTEM';
```

---

## Step 4: Configure Database Connection

Edit `config.properties` with your database details:

```properties
# Oracle Database Connection
db.driver=oracle.jdbc.driver.OracleDriver
db.url=jdbc:oracle:thin:@localhost:1521:XE
db.username=banking_system
db.password=password
db.polling_interval=2000
db.max_workers=3

# Change these values to match your setup:
# - localhost -> your Oracle host
# - 1521 -> your Oracle port
# - XE -> your database SID
# - password -> your banking_system user password
```

Common Oracle configurations:
- **Local Oracle XE**: `jdbc:oracle:thin:@localhost:1521:XE`
- **Local Oracle 12c/19c**: `jdbc:oracle:thin:@localhost:1521:ORCL`
- **Docker Oracle**: `jdbc:oracle:thin:@oracle-container:1521:XE`

---

## Step 5: Initialize Database Schema

```bash
# First, build the project
./build.sh

# Then initialize the database (creates tables)
./init_db.sh
```

You should see:
```
=== Initializing Database ===
Connected to Oracle Database successfully!
Creating bank_requests table...
Table created successfully!
```

---

## Step 6: Run the System

### Terminal 1: Start Daemon
```bash
./run_daemon.sh
```

### Terminal 2: Submit Requests
```bash
./run_client.sh
```

### Terminal 3: Check Status
```bash
./check_status.sh 1      # Check specific request
./check_status.sh all    # View all requests
```

---

## Troubleshooting

### "ojdbc8.jar not found"
```bash
# Verify jar is in lib/
ls -l lib/ojdbc8.jar

# If missing, download it (see Step 1)
```

### "Connection refused"
- Check Oracle database is running
- Verify hostname, port, and SID in config.properties
- Test connection: `sqlplus banking_system/password@localhost:1521/XE`

### "User does not have CREATE TABLE privilege"
- Ensure user has `CREATE TABLE` grant
- Run as SYSDBA again if needed

### "Sequence not found"
- Re-run the SQL commands in Step 3
- Verify sequence name matches code

### Cannot download ojdbc8.jar
- Check internet connection
- Try alternative download method
- If behind corporate proxy, configure wget/curl with proxy settings

---

## Quick Test After Setup

```bash
# Verify database connection works
java -cp bin:./lib/ojdbc8.jar database.DatabaseConnection

# Should output:
# Connected to Oracle Database successfully!
```

## Return to Normal Operation

Once setup is complete, use these commands:

```bash
./build.sh              # Compile code
./run_daemon.sh         # Start daemon
./run_client.sh         # Submit requests  
./check_status.sh 1     # Check status
```

---

## Next: Continue from SETUP.md

For more details, see [SETUP.md](SETUP.md) Option 2 section.
