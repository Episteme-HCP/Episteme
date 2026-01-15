
import os
import re
import sys

def analyze_file(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8', errors='ignore') as f:
            content = f.read()
    except Exception as e:
        print(f"Error reading {filepath}: {e}")
        return None
    
    filename = os.path.basename(filepath)
    is_viewer = "Viewer" in filename
    is_demo = "Demo" in filename
    # Apps might just be "App" suffix
    is_app = "App" in filename and not is_demo and not is_viewer
    
    if not (is_viewer or is_demo or is_app):
        return None

    violations = []
    
    # Rule 1: Viewers are reusable panels
    if is_viewer:
        if "getParameters" not in content:
            violations.append("Rule 1: Viewer does not implement getParameters()")
        # Note: We skip the main() check as it's not a hard failure condition for a 'list of violations' purely based on the user prompt "exposed and shown by the corresponding demo".
        # But let's check for I18n
        if "ResourceBundle" not in content and "I18n" not in content and "getBundle" not in content:
             violations.append("Rule 1: No evidence of I18n (ResourceBundle/I18n)")

    # Rule 2: API Usage
    # Check for usage of double[] arrays/logic instead of JScience objects if they are doing math
    # Matches 'double ' or 'double[]' but we want to allow it if it's wrapping a JScience object or simple params.
    # The user said: "use underlying API (matrices, loaders, simulations...)"
    # A heuristic: if it has "double[][]" it might be ignoring Matrix.
    if is_viewer or is_demo:
        if "double[][]" in content and "Matrix" not in content:
             violations.append("Rule 2: Potential raw double[][] usage without Matrix encapsulation")
        if "float[][]" in content:
             violations.append("Rule 2: Potential raw float[][] usage")

    # Rule 3: Styling
    # Check for hardcoded colors
    if "Color.RED" in content or "Color.BLUE" in content or "Color.GREEN" in content or "Color.black" in content or "Color.white" in content:
        violations.append("Rule 3: Uses standard hardcoded Colors (Color.RED, etc). Should use Theme.")
    if "setBackground(Color." in content:
        violations.append("Rule 3: Hardcoded background color set.")
    if "setFont(" in content:
        violations.append("Rule 3: Hardcoded font set.")

    # Rule 4: Real vs Double
    # Exception: Explicit Primitive vs Real demos
    is_comparison_demo = "Primitive" in filename or "ExactVsApproximate" in filename or "Performance" in filename or "Benchmark" in filename
    
    if not is_comparison_demo:
        # Check for numeric definitions.
        # Strict: 'double ' anywhere is suspicious in core/natural/social if we want 'Real' support.
        # But we must be careful about false positives (e.g. comments, random vars).
        # Look for field declarations or method returns.
        if re.search(r'(private|public|protected)\s+(static\s+)?(final\s+)?double\b', content):
             violations.append("Rule 4: Field/Method using primitive double (should be Real)")
        elif re.search(r'List<Double>', content) or re.search(r'ArrayList<Double>', content):
             violations.append("Rule 4: List<Double> usage (should be List<Real> or Vector)")

    if violations:
        print(f"VIOLATION_START|{filepath}|{filename}|{is_viewer}|{is_demo}|{is_app}")
        for v in violations:
            print(f"VIOLATION_DETAIL|{v}")
        print("VIOLATION_END")
        sys.stdout.flush()

root_dirs = [
    r"c:\Silvere\Encours\Developpement\JScience\jscience-core\src\main\java",
    r"c:\Silvere\Encours\Developpement\JScience\jscience-natural\src\main\java",
    r"c:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\java",
    r"c:\Silvere\Encours\Developpement\JScience\jscience-featured-apps\src\main\java"
]

print("Starting scan...")
for root_dir in root_dirs:
    if not os.path.exists(root_dir):
        # clean path check
        continue
        
    for root, dirs, files in os.walk(root_dir):
        for file in files:
            if file.endswith(".java"):
                analyze_file(os.path.join(root, file))
print("Scan complete.")
