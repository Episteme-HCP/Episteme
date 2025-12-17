#!/bin/bash

while true; do
    clear
    echo "=========================================="
    echo "     JScience Demo Launcher"
    echo "=========================================="
    echo ""
    echo "1. Run 3D Solar System Viewer"
    echo "2. Run Matrix Benchmarks"
    echo "3. Exit"
    echo ""
    read -p "Enter your choice (1-3): " choice

    case $choice in
        1)
            echo ""
            echo "Launching 3D Solar System Viewer..."
            echo "------------------------------------------"
            mvn exec:java -Dexec.mainClass="org.jscience.physics.astronomy.view.SolarSystemDemo"
            read -p "Press Enter to continue..."
            ;;
        2)
            echo ""
            echo "Launching Matrix Benchmarks..."
            echo "------------------------------------------"
            if [ -f "./run-benchmarks.sh" ]; then
                ./run-benchmarks.sh
            else
                echo "run-benchmarks.sh not found. Running via Maven..."
                mvn exec:java -Dexec.mainClass="org.jscience.benchmark.BenchmarkRunner"
            fi
            read -p "Press Enter to continue..."
            ;;
        3)
            exit 0
            ;;
        *)
            echo "Invalid choice. Please try again."
            read -p "Press Enter to continue..."
            ;;
    esac
done
