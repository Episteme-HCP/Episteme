#!/usr/bin/env python3
"""
JScience Mass Updater Script v3
Injects mandatory abstract methods. Improved regex.
"""

import os
import re
from pathlib import Path

BASE_DIR = Path(r"c:\Silvere\Encours\Developpement\JScience")

# Regex allowing generics <...> and newlines
CLASS_DEF_REGEX = re.compile(r"class\s+(\w+)(<[^>]+>)?\s*(extends|implements)", re.MULTILINE | re.DOTALL)

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
    
    @Override
    public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() {
        return new java.util.ArrayList<>();
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
    if "jscience-social" in path_str: return "sociology"
    if "jscience-natural" in path_str: return "physics"
    return "mathematics"

def process_file(file_path):
    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()

    # Skip if methods already present
    if "public String getCategory()" in content:
        return False
        
    match = CLASS_DEF_REGEX.search(content)
    if not match:
        return False

    class_name = match.group(1)
    
    res_type = None
    template = METHODS_TEMPLATE
    
    if "Viewer" in class_name: res_type = "viewer"
    elif "Reader" in class_name: res_type = "reader"; template = READER_METHODS_TEMPLATE
    elif "Writer" in class_name: res_type = "writer"; template = READER_METHODS_TEMPLATE
    elif "Demo" in class_name: res_type = "demo"
    else: return False

    # Skip abstract classes
    if "abstract class" in content: return False

    res_id = class_name.lower().replace(res_type, "")
    category = guess_category(file_path)

    print(f"Patching {class_name} ({res_type}) in {category}")
    
    code = template % {
        "category": category,
        "type": res_type,
        "id": res_id
    }

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
