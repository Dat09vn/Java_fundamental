# Banking System Worker-Controller Architecture

## Overview
A distributed batch processing system for banking account management:
- **Client**: Scheduled application (runs at 9 PM daily via cron) that submits requests
- **Daemon**: Always-running background service that processes requests and builds/updates accounts
- **Database**: Oracle DB storing requests, status, account data, and transaction history

## 1. Client Program

**Purpose**: Submit batch account build/update requests

**Functionality**:
- Triggers automatically at 9 PM each day via cron job
- Sends requests to the daemon process (account builds, account updates, etc.)
- Stores requests in the database for tracking
- Allows monitoring of request status through the database

## 2. Daemon Program

**Purpose**: Process incoming requests and handle account operations

**Functionality**:
- Continuously monitors the database for new requests (status = "new")
- Implements a controller-worker pattern for parallel processing:
  - **Controller**: Parent process that manages request queue and worker lifecycle
  - **Workers**: Child processes, each handling one request independently
  - Workers report completion status back to the controller
- Process flow:
  1. Fetch new request from database
  2. Fork worker process to handle the request
  3. Worker processes the request (builds account, updates balance from transactions, etc.)
  4. Worker returns result to controller
  5. Controller updates request status to "processed" in the database
- Supports multiple concurrent requests through parallel worker execution

## 3. Database Schema

Stores the following entities:
- **Request metadata**: Request ID, type, parameters, submission timestamp
- **Request status**: Current state (new, processing, processed, failed)
- **Account information**: Account details created or updated by requests
- **Transaction history**: Record of each account build/update operation

## Tech Stack
- **Language**: Java
- **Database**: Oracle DB

