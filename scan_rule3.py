import os
import re

ROOT_DIR = r"c:\Silvere\Encours\Developpement\JScience"

TARGET_FILES = [
    "UnitConverterViewer.java", "MetamathViewer.java", "MatrixViewer.java", "FunctionExplorer3DViewer.java",
    "FunctionExplorer2DViewer.java", "MandelbrotViewer.java", "LorenzViewer.java", "GeometryBoardViewer.java",
    "SpectrographViewer.java", "AudioViewer.java", "ResistorColorCodeViewer.java", "MagneticFieldViewer.java",
    "StellarSkyViewer.java", "StarSystemViewer.java", "BioMotionViewer.java", "RigidBodyViewer.java",
    "HumanBodyViewer.java", "ChemicalReactionViewer.java", "MolecularViewer.java", "PeriodicTableViewer.java",
    "DigitalLogicViewer.java", "MapViewer.java", "LotkaVolterraViewer.java", "PhylogeneticTreeViewer.java",
    "LSystemViewer.java", "GeneticsViewer.java", "VitalMonitorViewer.java", "ThermometerViewer.java",
    "SpectrometerViewer.java", "TelescopeViewer.java", "PressureGaugeViewer.java", "CarTrafficViewer.java",
    "ArtsColorTheoryViewer.java", "TimelineViewer.java", "SportsResultsViewer.java", "JavaFXSpintronic3DViewer.java",
    "Jzy3dSpintronic3DViewer.java", "SpinField2DViewer.java", "Spintronic3DViewer.java", "VRSpinViewer.java",
    "MetamathDemo.java", "MatrixDemo.java", "MandelbrotDemo.java", "LorenzDemo.java", "FunctionExplorer2D3DDemo.java",
    "FormulaNotationDemo.java", "ThermodynamicsDemo.java", "StarSystemDemo.java", "SpeciesBrowserDemo.java",
    "SequenceAlignmentDemo.java", "RigidBodyDemo.java", "ResistorColorCodeDemo.java", "RelativisticFlightDemo.java",
    "NewtonianMechanicsLabDemo.java", "MechanicsDemo.java", "InterplanetaryTrajectoryDemo.java", "GeneticAlgorithmDemo.java",
    "GameOfLifeDemo.java", "GaltonBoardDemo.java", "FluidDynamicsDemo.java", "DigitalLogicDemo.java",
    "ClothSimulationDemo.java", "ArchitectureStabilityDemo.java", "CarTrafficDemo.java", "KurzweilDemo.java",
    "CrystalStructureApp.java", "TitrationApp.java", "SmartGridApp.java", "FeaturedAppBase.java",
    "QuantumCircuitApp.java", "SpinValveApp.java", "CivilizationApp.java"
]

def find_file(name, search_path):
    for root, dirs, files in os.walk(search_path):
        if name in files:
            return os.path.join(root, name)
    return None

def scan_file(path):
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    violations = []
    lines = content.splitlines()
    for i, line in enumerate(lines):
        line_num = i + 1
        # Check rule 3: Colors
        if "Color." in line and not "import" in line and not "package" in line:
            # Check if it is a JavaFX Color or AWT Color usage
            if "javafx.scene.paint.Color" in line or "java.awt.Color" in line or "Color." in line:
                 violations.append((line_num, "Hardcoded Color", line.strip()))
        
        # Check rule 3: Fonts
        if "setFont(" in line:
            violations.append((line_num, "Hardcoded Font", line.strip()))
            
        # Check rule 3: setStyle (inline CSS)
        if "setStyle(" in line:
            violations.append((line_num, "Inline Style", line.strip()))
            
    return violations

def main():
    print("Scanning for Rule 3 Violations (Colors, Fonts, Inline Styles)...")
    found_count = 0
    for filename in TARGET_FILES:
        path = find_file(filename, ROOT_DIR)
        if path:
            vs = scan_file(path)
            if vs:
                print(f"\n{filename}:")
                for v in vs:
                    print(f"  Line {v[0]}: [{v[1]}] {v[2]}")
                    found_count += 1
        else:
            # print(f"Warning: Could not find {filename}")
            pass
            
    print(f"\nTotal violations found: {found_count}")

if __name__ == "__main__":
    main()
