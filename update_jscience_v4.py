#!/usr/bin/env python3
"""
JScience Mass Updater Script v4
Simple and brutal: finds Java files, checks needed methods, appends them if missing.
"""

import os
from pathlib import Path

BASE_DIR = Path(r"c:\Silvere\Encours\Developpement\JScience")

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

READER_TEMPLATE = """
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

def get_category(path):
    p = str(path).lower().replace("\\", "/")
    if "/physics/" in p: return "physics"
    if "/semiconductor/" in p: return "physics"
    if "/chemistry/" in p: return "chemistry"
    if "/biology/" in p: return "biology"
    if "/earth/" in p: return "earth"
    if "/geography/" in p: return "geography"
    if "/mathematics/" in p: return "mathematics"
    if "jscience-natural" in p: return "physics"
    if "jscience-social" in p: return "sociology"
    return "data"

def process(path):
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()
    
    # Check type
    if "abstract class" in content: return False
    
    is_viewer = "extends AbstractViewer" in content or "implements Viewer" in content
    is_reader = "extends AbstractResourceReader" in content
    is_writer = "extends AbstractResourceWriter" in content
    
    if not (is_viewer or is_reader or is_writer): return False
    
    # Check if missing methods
    # We check mainly getCategory and getLongDescription
    has_cat = "public String getCategory()" in content
    has_desc = "public String getDescription()" in content
    has_long = "public String getLongDescription()" in content
    has_name = "public String getName()" in content
    
    # Logic: if everything present, skip.
    # Note: Readers/Writers don't need getLongDescription/getViewerParams, Viewers do.
    
    if is_viewer and has_cat and has_desc and has_long and has_name: return False
    if (is_reader or is_writer) and has_cat and has_desc and has_name: return False
    
    print(f"Update needed: {path.name}")
    
    # Prepare code
    res_type = "viewer" if is_viewer else ("reader" if is_reader else "writer")
    res_id = path.stem.lower().replace("viewer", "").replace("reader", "").replace("writer", "")
    cat_id = get_category(path)
    
    template = METHODS_TEMPLATE if is_viewer else READER_TEMPLATE
    
    code = template % {"type": res_type, "id": res_id, "category": cat_id}
    
    # Crude insertion: before last }
    idx = content.rfind("}")
    if idx == -1: return False
    
    # Remove duplicates if partial implementation exists strictly to avoid dupe methods
    # This script is brutal: if it detects missing, it appends ALL. 
    # This might cause compilation error "Duplicate method" if one was present.
    # So we should only append MISSING ones. But template has all.
    # Let's simplify: construct the code block dynamically.
    
    code_parts = []
    
    if not has_cat:
        code_parts.append(f'    @Override public String getCategory() {{ return org.jscience.ui.i18n.I18n.getInstance().get("category.{cat_id}"); }}')
    if not has_name:
        key = f"{res_type}.{res_id}.title" if is_viewer else f"{res_type}.{res_id}.name"
        code_parts.append(f'    @Override public String getName() {{ return org.jscience.ui.i18n.I18n.getInstance().get("{key}"); }}')
    if not has_desc:
        key = f"{res_type}.{res_id}.desc" if is_viewer else f"{res_type}.{res_id}.description"
        code_parts.append(f'    @Override public String getDescription() {{ return org.jscience.ui.i18n.I18n.getInstance().get("{key}"); }}')
    
    if is_viewer:
        if not has_long:
            code_parts.append(f'    @Override public String getLongDescription() {{ return org.jscience.ui.i18n.I18n.getInstance().get("{res_type}.{res_id}.longdesc"); }}')
        if "getViewerParameters" not in content:
             code_parts.append('    @Override public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() { return new java.util.ArrayList<>(); }')
    
    if not code_parts: return False
    
    injection = "\n" + "\n".join(code_parts) + "\n"
    new_content = content[:idx] + injection + content[idx:]
    
    with open(path, "w", encoding="utf-8") as f:
        f.write(new_content)
        
    return True

def main():
    dirs = [
        BASE_DIR / "jscience-natural/src/main/java",
        BASE_DIR / "jscience-social/src/main/java"
    ]
    count = 0
    for d in dirs:
        for root, _, files in os.walk(d):
            for f in files:
                if f.endswith(".java"):
                    if process(Path(root)/f): count += 1
    print(f"Updated {count} files.")

if __name__ == "__main__":
    main()
