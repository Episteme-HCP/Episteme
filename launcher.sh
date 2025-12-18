#!/bin/bash

while true; do
    clear
    echo "=========================================="
    echo "     JScience Demo Launcher"
    echo "=========================================="
    echo ""
    echo "1. Run 3D Solar System Viewer"
    echo "2. Run Molecular Viewer (Water H2O)"
    echo "3. Run Matrix Viewer"
    echo "4. Run Plotting Demo (Sine Wave)"
    echo "5. Run Matrix Benchmarks"
    echo "6. Exit"
    echo ""
    read -p "Enter your choice (1-6): " choice

    case $choice in
        1)
            echo ""
            echo "Launching 3D Solar System Viewer..."
            echo "------------------------------------------"
            mvn exec:java -Dexec.mainClass="org.jscience.ui.astronomy.AstronomyViewer"
            read -p "Press Enter to continue..."
            ;;
        2)
            echo ""
            echo "Launching Molecular Viewer..."
            echo "------------------------------------------"
            mvn exec:java -Dexec.mainClass="org.jscience.ui.chemistry.MolecularViewerDemo"
            read -p "Press Enter to continue..."
            ;;
        3)
            echo ""
            echo "Launching Matrix Viewer..."
            echo "------------------------------------------"
            mvn exec:java -Dexec.mainClass="org.jscience.ui.matrix.MatrixViewerDemo"
            read -p "Press Enter to continue..."
            ;;
        4)
            echo ""
            echo "Launching Plotting Demo..."
            echo "------------------------------------------"
            mvn exec:java -Dexec.mainClass="org.jscience.ui.plotting.PlottingDemo"
            read -p "Press Enter to continue..."
            ;;
        5)
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
        6)
            exit 0
            ;;
        *)
            echo "Invalid choice. Please try again."
            read -p "Press Enter to continue..."
            ;;
    esac
done
