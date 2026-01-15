#!/usr/bin/env python3
"""
JScience Mass Updater Script v2
Injects mandatory abstract methods into Viewers, Demos, Readers, Writers.
Generates I18n keys for them.
"""

import os
import re
from pathlib import Path

BASE_DIR = Path(r"c:\Silvere\Encours\Developpement\JScience")
PYTHON_EXE = r"C:\Users\silve\AppData\Local\Programs\Python\Python314\python.exe"

# Patterns to detect component type
VIEWER_PATTERN = re.compile(r"class\s+(\w+Viewer)\s+(extends|implements)")
DEMO_PATTERN = re.compile(r"class\s+(\w+Demo)\s+(extends|implements)")
READER_PATTERN = re.compile(r"class\s+(\w+Reader)\s+(extends|implements)")
WRITER_PATTERN = re.compile(r"class\s+(\w+Writer)\s+(extends|implements)")

# Templates
METHODS_TEMPLATE = """
    // --- Mandatory Abstract Methods (I18n) ---

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.%(category)s");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("%(type)s.%(id)s.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("%(type)s.%(id)s.desc");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("%(type)s.%(id)s.longdesc");
    }
"""

READER_METHODS_TEMPLATE = """
    // --- Mandatory Abstract Methods (I18n) ---

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.%(category)s");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("%(type)s.%(id)s.name");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("%(type)s.%(id)s.description");
    }
"""

PACKAGE_CATEGORIES = {
    "mathematics": "mathematics", "math": "mathematics",
    "physics": "physics",
    "chemistry": "chemistry",
    "biology": "biology",
    "earth": "earth", "geography": "geography",
    "computing": "computing",
    "engineering": "engineering",
    "medicine": "medicine",
    "economics": "economics", "finance": "economics",
    "politics": "politics",
    "sociology": "sociology",
    "psychology": "psychology",
    "history": "history",
    "arts": "arts",
    "measure": "measure",
    "device": "device",
    "data": "data",
}

def guess_category(path):
    path_str = str(path).replace("\\", "/").lower()
    for key, cat in PACKAGE_CATEGORIES.items():
        if f"/{key}/" in path_str:
            return cat
    # Fallback based on module
    if "jscience-social" in path_str: return "sociology"
    if "jscience-natural" in path_str: return "physics" # Broad default
    if "jscience-core" in path_str: return "mathematics"
    return "data"

def process_file(file_path):
    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()
    
    # Check if already implements methods
    if "public String getCategory()" in content:
        return False

    res_type = None
    template = METHODS_TEMPLATE
    
    v_match = VIEWER_PATTERN.search(content)
    d_match = DEMO_PATTERN.search(content)
    r_match = READER_PATTERN.search(content)
    w_match = WRITER_PATTERN.search(content)

    if v_match: res_type = "viewer"; class_name = v_match.group(1)
    elif r_match: res_type = "reader"; class_name = r_match.group(1); template = READER_METHODS_TEMPLATE
    elif w_match: res_type = "writer"; class_name = w_match.group(1); template = READER_METHODS_TEMPLATE
    elif d_match: res_type = "demo"; class_name = d_match.group(1)
    else: return False
    
    # Extract ID (remove suffix)
    res_id = class_name.lower().replace(res_type, "") 
    category = guess_category(file_path)

    # Clean up demo ID logic if needed (e.g. LorenzDemo -> demo.lorenz.title)
    if res_type == "demo":
        res_id = res_id # e.g. lorenz
        # Demos use a slightly different key pattern sometimes, but we standardize here
        # Actually existing demos used "demo.<name>.title", let's stick to standard "%(type)s.%(id)s.title"
    
    print(f"Patching {class_name} ({res_type}) in {category}")

    code = template % {
        "category": category,
        "type": res_type,
        "id": res_id
    }

    # Insert before last brace
    last_brace_index = content.rfind("}")
    if last_brace_index == -1: return False
    
    new_content = content[:last_brace_index] + code + content[last_brace_index:]
    
    with open(file_path, "w", encoding="utf-8") as f:
        f.write(new_content)
    
    return True

def main():
    target_dirs = [
        BASE_DIR / "jscience-natural/src/main/java",
        BASE_DIR / "jscience-social/src/main/java",
        # BASE_DIR / "jscience-core/src/main/java" # Already mostly done
    ]
    
    count = 0
    for d in target_dirs:
        print(f"Scanning {d}...")
        for root, dirs, files in os.walk(d):
            for file in files:
                if file.endswith(".java"):
                    if process_file(Path(root) / file):
                        count += 1
    print(f"Total updated: {count}")

if __name__ == "__main__":
    main()
