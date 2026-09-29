#!/bin/bash

# Integration Test - Complete workflow in one process

echo ""
if [ ! -d "bin" ]; then
    echo "Error: bin directory not found. Run './build.sh' first."
    exit 1
fi

java -cp bin test.IntegrationTest
