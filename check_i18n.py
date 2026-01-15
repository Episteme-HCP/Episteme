#!/usr/bin/env python3
import os
import re
from pathlib import Path

BASE_DIR = Path(r"c:\Silvere\Encours\Developpement\JScience")

def check_file(path):
    with open(path, "r", encoding="utf-8") as f:
        lines = f.readlines()
    
    suspicious = []
    # Simple heuristic regexes for hardcoded strings
    # We look for "Literal String" inside setText or new Label/Button/CheckBox/RadioButton
    # Ignoring empty strings "" or strings with only non-letters (like " ", ":", "-")
    
    patterns = [
        (re.compile(r'setText\s*\(\s*"([a-zA-Z].+?)"'), "setText"),
        (re.compile(r'new\s+Label\s*\(\s*"([a-zA-Z].+?)"'), "new Label"),
        (re.compile(r'new\s+Button\s*\(\s*"([a-zA-Z].+?)"'), "new Button"),
        (re.compile(r'new\s+CheckBox\s*\(\s*"([a-zA-Z].+?)"'), "new CheckBox"),
        (re.compile(r'setTitle\s*\(\s*"([a-zA-Z].+?)"'), "setTitle"),
        (re.compile(r'setHeaderText\s*\(\s*"([a-zA-Z].+?)"'), "setHeaderText"),
        (re.compile(r'setContentText\s*\(\s*"([a-zA-Z].+?)"'), "setContentText"),
    ]
    
    for i, line in enumerate(lines):
        line = line.strip()
        if line.startswith("//") or line.startswith("*"): continue # Skip comments
        
        for p, name in patterns:
            match = p.search(line)
            if match:
                s = match.group(1)
                # Ignore if it looks like a key, though keys are strings too.
                # Heuristic: Keys usually are "dot.separated" or "snake_case".
                # Real text usually has spaces or Capitalized Words.
                if " " in s or s[0].isupper(): 
                     # Ignore "Standard" font names or specific exclusions
                     if s in ["Arial", "Verdana", "Courier New", "Modena", "Caspian", "Dark", "HighContrast"]: continue
                     suspicious.append((i+1, name, s))

    if suspicious:
        print(f"File: {path.name}")
        for ln, type_, s in suspicious:
            print(f"  Line {ln}: {type_} found hardcoded '...{s[:30]}...'")
        return len(suspicious)
    return 0

def main():
    exclude_dirs = ["target", "test", ".git", ".idea"]
    total_violations = 0
    for root, dirs, files in os.walk(BASE_DIR):
        # Filter dirs
        dirs[:] = [d for d in dirs if d not in exclude_dirs]
        
        for file in files:
            if file.endswith(".java") and ("Viewer" in file or "Demo" in file or "App" in file):
                 total_violations += check_file(Path(root) / file)
                 
    print(f"\nTotal potential I18n violations found: {total_violations}")

if __name__ == "__main__":
    main()
