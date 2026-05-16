#!/bin/bash
source "$(dirname "$0")/scripts/setup/env_setup.sh"

show_menu() {
    clear
    echo "============================================"
    echo "  Episteme Multi-Launcher"
    echo "============================================"
    echo ""
    echo "Please select an application to launch:"
    echo ""
    echo " 1) Web Browser"
    echo " 2) Chemistry Simulation"
    echo " 3) Civilization Model"
    echo " 4) Crystal Lattice Viewer"
    echo " 5) Episteme Demos Suite"
    echo " 6) Grid Computing Dashboard"
    echo " 7) Life Science Simulation"
    echo " 8) Market Economy Model"
    echo " 9) Pandemic Simulator"
    echo " 10) Quantum Computing Workbench"
    echo " 11) Spin Valve Simulation"
    echo " 12) Stability Analyzer"
    echo " 13) Titration Simulation"
    echo " 14) Verification Suite"
    echo ""
    echo " Q) Quit"
    echo ""
}

while true; do
    show_menu
    read -p "Enter choice (1-14 or Q): " choice
    
    case $choice in
        [Qq]) exit 0 ;;
        1)  bash "$(dirname "$0")/episteme-featured-apps/launchers/run_browser.sh" ;;
        2)  bash "$(dirname "$0")/episteme-featured-apps/launchers/run_chemistry.sh" ;;
        3)  bash "$(dirname "$0")/episteme-featured-apps/launchers/run_civilization.sh" ;;
        4)  bash "$(dirname "$0")/episteme-featured-apps/launchers/run_crystal.sh" ;;
        5)  bash "$(dirname "$0")/episteme-featured-apps/launchers/run_episteme_demos.sh" ;;
        6)  bash "$(dirname "$0")/episteme-featured-apps/launchers/run_grid.sh" ;;
        7)  bash "$(dirname "$0")/episteme-featured-apps/launchers/run_life_science.sh" ;;
        8)  bash "$(dirname "$0")/episteme-featured-apps/launchers/run_market.sh" ;;
        9)  bash "$(dirname "$0")/episteme-featured-apps/launchers/run_pandemic.sh" ;;
        10) bash "$(dirname "$0")/episteme-featured-apps/launchers/run_quantum.sh" ;;
        11) bash "$(dirname "$0")/episteme-featured-apps/launchers/run_spin_valve.sh" ;;
        12) bash "$(dirname "$0")/episteme-featured-apps/launchers/run_stability.sh" ;;
        13) bash "$(dirname "$0")/episteme-featured-apps/launchers/run_titration.sh" ;;
        14) bash "$(dirname "$0")/episteme-featured-apps/launchers/run_verify.sh" ;;
        *) echo "Invalid choice." ;;
    esac
    
    echo ""
    read -p "Press Enter to return to menu..."
done

