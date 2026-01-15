
import os
import re

ROOT = r"c:\Silvere\Encours\Developpement\JScience"

count = 0
for root, dirs, files in os.walk(ROOT):
    if "target" in root: continue
    for file in files:
        if file.endswith(".java"):
            path = os.path.join(root, file)
            try:
                with open(path, 'r', encoding='utf-8') as f:
                    content = f.read()
                
                # Check for duplicate @Override
                # Pattern: @Override followed by optional whitespace and another @Override
                # We need to look for @Override\s+@Override
                
                if re.search(r'@Override\s+@Override', content):
                    new_content = re.sub(r'(@Override\s+)+@Override', r'@Override', content)
                    
                    if new_content != content:
                        with open(path, 'w', encoding='utf-8') as f:
                            f.write(new_content)
                        print(f"Fixed {file}")
                        count += 1
            except Exception as e:
                print(f"Error reading {path}: {e}")

print(f"Fixed {count} files with duplicate Annotations.")
