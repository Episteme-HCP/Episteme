#!/usr/bin/env python3
import os
from pathlib import Path
import re

BASE_DIR = Path(r"c:\Silvere\Encours\Developpement\JScience")
MSG_FILE = BASE_DIR / "jscience-core/src/main/resources/org/jscience/ui/i18n/messages_core_en.properties"
JAVA_FILE = BASE_DIR / "jscience-core/src/main/java/org/jscience/ui/JScienceMasterControl.java"

def main():
    # 1. Read Java file and identify keys to migrate
    with open(JAVA_FILE, "r", encoding="utf-8") as f:
        java_content = f.read()
    
    # Regex to find potential keys starting with dashboard.
    # We look for "dashboard.something"
    matches = set(re.findall(r'"(dashboard\.[a-zA-Z0-9_.]+)"', java_content))
    
    if not matches:
        print("No 'dashboard.' keys found in JScienceMasterControl.java")
        return

    mapping = {}
    for old_key in matches:
        new_key = old_key.replace("dashboard.", "mastercontrol.")
        mapping[old_key] = new_key
        
    print(f"Found {len(mapping)} keys to migrate:")
    for k, v in mapping.items():
        print(f"  {k} -> {v}")
        
    # 2. Update Java File
    new_java_content = java_content
    for old, new in mapping.items():
        new_java_content = new_java_content.replace(f'"{old}"', f'"{new}"')
        
    with open(JAVA_FILE, "w", encoding="utf-8") as f:
        f.write(new_java_content)
    print("Updated JScienceMasterControl.java")
    
    # 3. Update Properties File
    if MSG_FILE.exists():
        with open(MSG_FILE, "r", encoding="utf-8") as f:
            lines = f.readlines()
            
        new_lines = []
        for line in lines:
            line_stripped = line.strip()
            # Check if line starts with one of our old keys
            replaced = False
            for old, new in mapping.items():
                if line_stripped.startswith(old + "="):
                    new_lines.append(line.replace(old, new, 1))
                    replaced = True
                    break
            if not replaced:
                new_lines.append(line)
        
        with open(MSG_FILE, "w", encoding="utf-8") as f:
            f.writelines(new_lines)
        print("Updated messages_core_en.properties")
    else:
        print("Warning: messages_core_en.properties not found!")

if __name__ == "__main__":
    main()
