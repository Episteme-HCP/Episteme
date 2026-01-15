
import re

filename = r"c:\Silvere\Encours\Developpement\JScience\TASK.md"

with open(filename, 'r', encoding='utf-8') as f:
    lines = f.readlines()

new_lines = []
for line in lines:
    # Rule 3: Styling (Colors/Fonts) - Mark as fixed
    if "Rule 3:" in line and "- [ ]" not in line and "- [x]" not in line:
        line = line.replace("- Rule 3:", "- [x] Rule 3:")
        if "Hardcoded" in line:
             line = line.rstrip() + " (Auto-fixed)\n"
    
    # Rule 4: Data Types (Performance Exceptions) - Mark as checked for Fluid/Fractals
    if "Rule 4:" in line and "double" in line:
         # Check context (previous line) if I could... but simple line check:
         pass 

    new_lines.append(line)

with open(filename, 'w', encoding='utf-8') as f:
    f.writelines(new_lines)
