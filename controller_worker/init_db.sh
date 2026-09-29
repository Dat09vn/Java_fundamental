#!/bin/bash

# Database Initialization Script

echo "=== Initializing Database ==="
echo ""

# Check if bin directory exists
if [ ! -d "bin" ]; then
    echo "Error: bin directory not found. Run './build.sh' first."
    exit 1
fi

# Run database initialization
# java -cp bin:./lib/ojdbc8.jar util.InitializeDatabase
java -cp "bin:lib/ojdbc11-23.4.0.24.05.jar" util.InitializeDatabase