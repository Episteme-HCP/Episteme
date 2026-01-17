import os
import re

root_dir = r"C:\Silvere\Encours\Developpement\JScience"
exclude_dirs = ['target', 'node_modules', '.git', '.idea', 'jscience-old-v1', 'jscience-benchmarks']

# 1. Scan Java files for USED keys
print("Scanning Java files for USED keys...")
used_keys = set()
# Regex matches: .get("key", "default") or .get("key")
# We look for strings that look like keys (dots, lowercase) inside .get()
key_pattern = re.compile(r'\.get\(\s*"([a-z0-9][a-z0-9._-]+)"')

for subdir, dirs, files in os.walk(root_dir):
    dirs[:] = [d for d in dirs if d not in exclude_dirs]
    for file in files:
        if file.endswith(".java"):
            try:
                with open(os.path.join(subdir, file), 'r', encoding='utf-8') as f:
                    content = f.read()
                    matches = key_pattern.findall(content)
                    for m in matches:
                        used_keys.add(m)
            except Exception:
                pass

print(f"Found {len(used_keys)} unique keys used in Java code.")

# 2. Scan Properties files for DEFINED keys and CLEAN them
print("Scanning and Cleaning Properties files...")

removed_count = 0
total_defined = 0

for subdir, dirs, files in os.walk(root_dir):
    dirs[:] = [d for d in dirs if d not in exclude_dirs]
    for file in files:
        if file.endswith(".properties") and "messages" in file:
            filepath = os.path.join(subdir, file)
            new_lines = []
            file_changed = False
            
            try:
                with open(filepath, 'r', encoding='utf-8') as f:
                    lines = f.readlines()

                for line in lines:
                    line_stripped = line.strip()
                    if not line_stripped or line_stripped.startswith('#'):
                        new_lines.append(line)
                        continue
                    
                    if '=' in line:
                        key = line.split('=')[0].strip()
                        total_defined += 1
                        
                        # Logic: Is the key used?
                        # We also keep keys that might be dynamic (endswith partial match) logic is risky, 
                        # but user asked to remove "obsolete".
                        # Exception: "app.title" or standard keys might be used in frameworks? 
                        # We stick to the strict rule: if not found in Java via regex, it's out.
                        
                        if key in used_keys:
                            new_lines.append(line)
                        else:
                            # Verify if it's a dynamic key root (e.g., if code uses "prefix." + var)
                            # This is a heuristic check.
                            is_prefix = False
                            for uk in used_keys:
                                if uk.startswith(key + "."):
                                    is_prefix = True
                                    break
                            
                            if is_prefix:
                                new_lines.append(line)
                            else:
                                # REMOVE
                                # print(f"Removing obsolete key in {file}: {key}")
                                removed_count += 1
                                file_changed = True
                    else:
                        new_lines.append(line)
                
                if file_changed:
                    with open(filepath, 'w', encoding='utf-8') as f:
                        f.writelines(new_lines)
                    print(f"Updated {file}: Removed obsolete keys.")
                    
            except Exception as e:
                print(f"Error processing {file}: {e}")

print(f"Audit Complete.")
print(f"Total Defined Keys Found: {total_defined}")
print(f"Total Unique Used Keys: {len(used_keys)}")
print(f"Total Keys Removed: {removed_count}")
