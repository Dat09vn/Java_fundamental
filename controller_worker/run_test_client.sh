#!/bin/bash

# Test Client with Pure Memory (NO dependencies!)

echo "=== Banking System Client (Memory-Based - NO DB NEEDED!) ==="
echo ""

if [ ! -d "bin" ]; then
    echo "Error: bin directory not found. Run './build.sh' first."
    exit 1
fi

java -cp bin client.ClientMemory
