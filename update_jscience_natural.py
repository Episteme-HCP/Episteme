#!/usr/bin/env python3
"""
JScience Mass Updater Script
Injects mandatory abstract methods into Java classes.
"""

import os
import re
from pathlib import Path

BASE_DIR = Path(r"c:\Silvere\Encours\Developpement\JScience")
NATURAL_DIR = BASE_DIR / "jscience-natural/src/main/java"

# Method templates
METHODS_TEMPLATE = """
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
    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.%(category)s");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.%(id)s.name");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.%(id)s.description");
    }
"""

# Heuristics to guess category based on package
PACKAGE_CATEGORIES = {
    "mathematics": "mathematics",
    "physics": "physics",
    "chemistry": "chemistry",
    "biology": "biology",
    "earth": "earth",
    "geography": "geography",
    "computing": "computing",
    "engineering": "engineering",
    "medicine": "medicine",
    "economics": "economics",
    "device": "device",
}

def guess_category(path):
    path_str = str(path).replace("\\", "/")
    for key, cat in PACKAGE_CATEGORIES.items():
        if f"/{key}/" in path_str:
            return cat
    return "data"

def process_file(file_path):
    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()
    
    # Check if needs update
    if "extends AbstractViewer" in content or "implements Viewer" in content:
        res_type = "viewer"
        template = METHODS_TEMPLATE
    elif "extends AbstractResourceReader" in content or "implements ResourceReader" in content:
        res_type = "reader"
        template = READER_METHODS_TEMPLATE
    elif "extends AbstractDemo" in content:
       res_type = "demo"
       # Demos might just need getLongDescription visibility fix or complete implementation
       template = METHODS_TEMPLATE 
    else:
        return False

    # Check if methods missing
    if "public String getCategory()" in content:
        return False

    # Metadata
    filename = file_path.stem
    res_id = filename.lower().replace("viewer", "").replace("reader", "").replace("demo", "")
    category = guess_category(file_path)
    
    # Prepare methods
    methods_code = template % {
        "category": category,
        "type": res_type,
        "id": res_id
    }
    
    # Insert before last brace
    last_brace_index = content.rfind("}")
    if last_brace_index == -1: return False
    
    new_content = content[:last_brace_index] + methods_code + content[last_brace_index:]
    
    with open(file_path, "w", encoding="utf-8") as f:
        f.write(new_content)
    
    print(f"Updated {file_path.name}")
    return True

def main():
    print("Scanning jscience-natural...")
    count = 0
    for root, dirs, files in os.walk(NATURAL_DIR):
        for file in files:
            if file.endswith(".java"):
                if process_file(Path(root) / file):
                    count += 1
    print(f"Total updated: {count}")

if __name__ == "__main__":
    main()
