#!/bin/bash

# Build Script for Banking System Worker-Controller

echo "=== Building Banking System ==="

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check if bin directory exists
if [ ! -d "bin" ]; then
    echo "Creating bin directory..."
    mkdir -p bin
fi

# Compile all Java files
echo "Compiling Java source files..."
javac -d bin src/models/*.java src/database/*.java src/daemon/*.java src/client/*.java src/util/*.java src/test/*.java 2>&1 | grep -v "warning" || true

if [ $? -eq 0 ] || [ -d bin/daemon ]; then
    echo -e "${GREEN}✓ Build successful!${NC}"
    echo ""
    echo "📖 Quick start:"
    echo ""
    echo "  1️⃣  Integration Test (complete workflow, ~5 seconds):"
    echo "     ./run_integration_test.sh"
    echo ""
    echo "  2️⃣  Multi-Terminal Test (daemon + client + status):"
    echo "     Terminal 1: ./run_test_daemon.sh"
    echo "     Terminal 2: ./run_test_client.sh"
    echo "     Terminal 3: ./run_test_status.sh 1    (or 'all')"
    echo ""
    echo "  3️⃣  Oracle Database (production ready):"
    echo "     - Place ojdbc8.jar in lib/"
    echo "     - Setup Oracle user"
    echo "     - See SETUP.md for details"
else
    echo -e "${RED}✗ Build failed!${NC}"
    exit 1
fi
