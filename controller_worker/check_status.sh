#!/bin/bash

# Check Request Status Script

if [ $# -eq 0 ]; then
    echo "Usage: ./check_status.sh <request_id>"
    echo "Example: ./check_status.sh 1"
    exit 1
fi

echo "=== Request Status Checker ==="
echo ""

# Check if bin directory exists
if [ ! -d "bin" ]; then
    echo "Error: bin directory not found. Run './build.sh' first."
    exit 1
fi

# Run status checker
# java -cp bin:./lib/ojdbc8.jar client.StatusChecker $1
java -cp "bin:lib/ojdbc11-23.4.0.24.05.jar" client.StatusChecker $1
