#!/bin/bash

# Run Client Script - Submit Batch Requests

echo "=== Banking System Client - Submitting Requests ==="
echo ""

# Check if bin directory exists
if [ ! -d "bin" ]; then
    echo "Error: bin directory not found. Run './build.sh' first."
    exit 1
fi

# Run client
# java -cp bin:./lib/ojdbc8.jar client.Client
java -cp "bin:lib/ojdbc11-23.4.0.24.05.jar" client.Client

