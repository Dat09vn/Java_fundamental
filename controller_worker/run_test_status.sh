#!/bin/bash

# Test Status Checker with Pure Memory (NO dependencies!)

if [ $# -eq 0 ]; then
    echo "Usage: ./run_test_status.sh <request_id>"
    echo "   or: ./run_test_status.sh all    (to view all requests)"
    echo ""
    echo "Examples:"
    echo "   ./run_test_status.sh 1"
    echo "   ./run_test_status.sh all"
    exit 1
fi

echo "=== Request Status Checker (Memory-Based - NO DB NEEDED!) ==="
echo ""

if [ ! -d "bin" ]; then
    echo "Error: bin directory not found. Run './build.sh' first."
    exit 1
fi

java -cp bin client.StatusCheckerMemory $1
