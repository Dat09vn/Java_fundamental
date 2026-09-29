#!/bin/bash

# Test Daemon with Pure Memory (NO dependencies!)

echo "=== Banking System Daemon (Memory-Based - NO DB NEEDED!) ==="
echo "Press Ctrl+C to stop the daemon"
echo ""

if [ ! -d "bin" ]; then
    echo "Error: bin directory not found. Run './build.sh' first."
    exit 1
fi

java -cp bin daemon.DaemonMemoryMain
