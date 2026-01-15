#!/usr/bin/env python3
import os
import re
from pathlib import Path

BASE_DIR = Path(r"c:\Silvere\Encours\Developpement\JScience")
MSG_FILE = BASE_DIR / "jscience-core/src/main/resources/org/jscience/ui/i18n/messages_core_en.properties"

# Regex for finding hardcoded strings
# Group 1: The constructor or method call (e.g. "new Label(")
# Group 2: The string content
MATCH_REGEX = re.compile(r'(new\s+(?:Label|Button|CheckBox|RadioButton|MenuItem|Menu|TitledPane)|setText|setTitle|setHeaderText|setContentText)\s*\(\s*"([^"]+)"')

EXISTING_KEYS = set()

def load_keys():
    if MSG_FILE.exists():
        with open(MSG_FILE, "r", encoding="utf-8") as f:
            for line in f:
                if "=" in line and not line.strip().startswith("#"):
                    EXISTING_KEYS.add(line.split("=", 1)[0].strip())

def generate_key_and_default(class_name, text):
    # Sanitize text for key
    clean_text = re.sub(r'[^a-zA-Z0-9 ]', '', text).strip().lower()
    clean_text = clean_text.replace(" ", ".")
    if len(clean_text) > 20: clean_text = clean_text[:20]
    
    # Class based prefix
    prefix = class_name.lower().replace("viewer", "").replace("demo", "").replace("app", "")
    
    key = f"generated.{prefix}.{clean_text}"
    
    # Avoid collision (simple)
    counter = 1
    base_key = key
    while key in EXISTING_KEYS:
        key = f"{base_key}.{counter}"
        counter += 1
    
    return key, text

def process_file(path):
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()
    
    if "import" not in content and "package" not in content: return # Skip weird files
        
    class_name = path.stem
    new_content = content
    
    replacements = [] # (start_idx, end_idx, new_text)
    new_keys_to_add = {} # key -> default_text

    for match in MATCH_REGEX.finditer(content):
        full_match = match.group(0)
        prefix = match.group(1)
        text = match.group(2)
        
        # Filter heuristics
        if len(text) < 2: continue # too short
        if text.isupper() and len(text) < 5: continue # likely acronym
        if text.startswith("{") or text.startswith("#"): continue # likely format or color
        if "layout" in text.lower() or "center" in text.lower(): continue # likely internal
        
        # Don't touch if it looks like a file path or URL
        if "/" in text or ".css" in text or ".png" in text or "http" in text: continue
        
        # Don't touch if it looks like a regex
        if "\\" in text or "[" in text: continue

        # Special exclusions for known tech strings
        if text in ["Modena", "Caspian", "Dark", "HighContrast", "Arial", "System"]: continue
        
        # Generate I18n call
        k, default_text = generate_key_and_default(class_name, text)
        replacement = f'{prefix}(org.jscience.ui.i18n.I18n.getInstance().get("{k}", "{default_text}")'
        # Add closing parenthesis for the get() call
        replacement += ')'
        
        # Store for batch update
        # We need to replace `prefix("text"` with `prefix(I18n("key", "text")`
        # But wait, original was `prefix("text"`, replacements adds a closing paren?
        # No, MATCH_REGEX captures `new Label("text"` without the closing quote and paren?
        # Regex: `... \s* \(\s* "([^"]+)"`
        # So full match is `new Label("text"` (quote at end is not in group 0 because not matched?)
        # Wait, group 0 includes what is matched. 
        # `([^"]+)` captures content. The closing `"` is NOT in group but we used it in lookahead? No.
        # My regex: `... \s*\(\s* "([^"]+)"` -> matching stops at the LAST quote? No.
        
        # Let's simple str.replace based on precise match to avoid offset hell
        # But duplicates are tricky.
        
        new_fragment = f'{prefix}(org.jscience.ui.i18n.I18n.getInstance().get("{k}", "{default_text}")'
        # The match in content is `prefix("text"`
        # We need to replace `prefix("text"` with `prefix(I18n...)`
        # Oh, the closing `"` is NOT consumed by my regex group 1 or 2, but present in input.
        
        # Let's verify what match.group(0) is.
        # `new Label("My Text"` (without closing `"` if I didn't match it) is NOT valid regex logic.
        # My regex: `... "([^"]+)"` matches the closing quote.
        # So group 0 is `new Label("My Text"`
        
        # So I replace group 0 with `new Label(I18n.get("key", "My Text")`
        old_fragment = match.group(0) # `new Label("My Text"`
        
        # Check if we are inside a complex statement (concatenation)
        # Scan ahead in content from match.end() to see if next char is ` +`
        chunk_after = content[match.end():match.end()+10]
        if chunk_after.strip().startswith("+"):
            # It's a concatenation. Skip auto-fix for now as it requires MessageFormat logic 
            # and it is simpler to do manual or advanced script later. 
            # "go 4 all" -> I should try.
            # But simpler first.
            continue
            
        replacements.append((old_fragment, new_fragment))
        new_keys_to_add[k] = default_text
        EXISTING_KEYS.add(k)

    # Apply replacements
    changed = False
    for old, new in replacements:
        if old in new_content:
            new_content = new_content.replace(old, new)
            changed = True
    
    if changed:
        with open(path, "w", encoding="utf-8") as f:
            f.write(new_content)
        print(f"Patched {path.name} with {len(replacements)} keys.")
        return new_keys_to_add
    return {}

def main():
    load_keys()
    
    exclude_dirs = ["target", "test", ".git", ".idea"]
    all_new_keys = {}
    
    target_dirs = [BASE_DIR / "jscience-core", BASE_DIR / "jscience-natural", BASE_DIR / "jscience-social", BASE_DIR / "jscience-client"]
    
    for base in target_dirs:
        for root, dirs, files in os.walk(base):
            dirs[:] = [d for d in dirs if d not in exclude_dirs]
            for file in files:
                if file.endswith(".java") and ("Viewer" in file or "Demo" in file or "App" in file):
                    new_keys = process_file(Path(root) / file)
                    all_new_keys.update(new_keys)
                    
    # Append new keys to properties file
    if all_new_keys:
        with open(MSG_FILE, "a", encoding="utf-8") as f:
            f.write("\n# --- Auto Refactored Keys ---\n")
            for k, v in sorted(all_new_keys.items()):
                f.write(f"{k}={v}\n")
        print(f"Added {len(all_new_keys)} new keys to messages_core_en.properties")

if __name__ == "__main__":
    main()
