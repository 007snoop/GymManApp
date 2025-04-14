#!/bin/bash

# Navigate to the script's directory
cd "$(dirname "$0")" || exit

echo "Checking for Maven..."
if ! command -v mvn >/dev/null 2>&1; then
    echo "Maven is not installed or not added to PATH."
    read -r -p "Press Enter to exit..."
    exit 1
fi

echo "Installing dependencies..."
if ! mvn clean install; then
    echo "Failed to install dependencies. Check your pom.xml."
    read -r -p "Press Enter to exit..."
    exit 1
fi

echo "Launching JavaFX application..."
if ! mvn javafx:run; then
    echo "Failed to run the application."
    read -r -p "Press Enter to exit..."
    exit 1
fi

echo "Application exited."
read -r -p "Press Enter to close..."
