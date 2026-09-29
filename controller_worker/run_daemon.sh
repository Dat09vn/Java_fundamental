#!/bin/bash

# Run Daemon Script

echo "=== Starting Banking System Daemon ==="
echo "Press Ctrl+C to stop the daemon"
echo ""

# Check if bin directory exists
if [ ! -d "bin" ]; then
    echo "Error: bin directory not found. Run './build.sh' first."
    exit 1
fi

# Run daemon
# java -cp bin:./lib/ojdbc8.jar daemon.Daemon
java -cp "bin:lib/ojdbc11-23.4.0.24.05.jar" daemon.Daemon

