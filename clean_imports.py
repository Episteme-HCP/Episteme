import json
import os

with open("problems.json", "r") as f:
    problems = json.load(f)

# Group by file
files = {}
for p in problems:
    if "The import" in p["message"] and "is never used" in p["message"]:
        path = p["path"]
        line = p["startLine"]
        if path not in files:
            files[path] = []
        files[path].append(line)

# Process each file
for path, lines in files.items():
    if not os.path.exists(path):
        print(f"File not found: {path}")
        continue
    
    # Read file
    with open(path, "r", encoding="utf-8") as f:
        content = f.readlines()
    
    # Remove lines (sorted descending)
    lines_to_remove = sorted(list(set(lines)), reverse=True)
    count = 0
    for line_num in lines_to_remove:
        # line_num is 1-based
        idx = line_num - 1
        if 0 <= idx < len(content):
            # Verify it is an import line to be safe
            if content[idx].strip().startswith("import "):
                 # print(f"Removing line {line_num} from {path}: {content[idx].strip()}")
                 del content[idx]
                 count += 1
            else:
                 print(f"Skipping line {line_num} in {path} because it is not an import: {content[idx].strip()}")
            
    # Write back
    if count > 0:
        print(f"Removed {count} unused imports from {path}")
        with open(path, "w", encoding="utf-8") as f:
            f.writelines(content)
