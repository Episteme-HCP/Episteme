#!/usr/bin/env python3
"""
I18n Sync Tool for JScience
Synchronizes missing keys across all language files.
"""
import os
import re
from pathlib import Path
from collections import defaultdict

def extract_keys(filepath):
    """Extract non-comment, non-empty keys from a .properties file."""
    keys = set()
    if not os.path.exists(filepath):
        return keys
    with open(filepath, 'r', encoding='utf-8') as f:
        for line in f:
            line = line.strip()
            if line and not line.startswith('#') and '=' in line:
                key = line.split('=', 1)[0].strip()
                keys.add(key)
    return keys

def get_value(filepath, key):
    """Get value for a specific key."""
    with open(filepath, 'r', encoding='utf-8') as f:
        for line in f:
            line = line.strip()
            if line.startswith(key + '='):
                return line.split('=', 1)[1]
    return None

def sync_module(base_path, prefix, languages=['en', 'fr', 'de', 'es', 'zh']):
    """Sync all language files for a module."""
    files = {lang: os.path.join(base_path, f"{prefix}_{lang}.properties") for lang in languages}
    
    # Collect all keys from all files
    all_keys = set()
    keys_per_lang = {}
    for lang, path in files.items():
        keys = extract_keys(path)
        keys_per_lang[lang] = keys
        all_keys.update(keys)
    
    print(f"\n=== Module: {prefix} ===")
    print(f"Total unique keys: {len(all_keys)}")
    
    # Find missing keys per language
    missing = {}
    for lang, keys in keys_per_lang.items():
        missing[lang] = all_keys - keys
        print(f"  {lang}: {len(keys)} keys, {len(missing[lang])} missing")
    
    # Get reference values from EN (or FR as fallback)
    reference_lang = 'en' if 'en' in files else list(files.keys())[0]
    
    # Add missing keys to each file
    for lang, miss_keys in missing.items():
        if miss_keys:
            filepath = files[lang]
            with open(filepath, 'a', encoding='utf-8') as f:
                f.write(f"\n# === Auto-synced missing keys ({len(miss_keys)} entries) ===\n")
                for key in sorted(miss_keys):
                    # Get value from reference language
                    ref_val = get_value(files[reference_lang], key)
                    if ref_val is None:
                        # Try other languages
                        for other_lang in languages:
                            ref_val = get_value(files[other_lang], key)
                            if ref_val:
                                break
                    if ref_val:
                        # Mark as TODO for translation
                        f.write(f"# TODO: Translate\n{key}={ref_val}\n")
                    else:
                        f.write(f"{key}=!{key}!\n")
            print(f"    -> Added {len(miss_keys)} missing keys to {lang}")

def main():
    base = Path(".")
    
    modules = [
        ("jscience-core/src/main/resources/org/jscience/ui/i18n", "messages_core"),
        ("jscience-natural/src/main/resources/org/jscience/ui/i18n", "messages_natural"),
        ("jscience-social/src/main/resources/org/jscience/ui/i18n", "messages_social"),
        ("jscience-featured-apps/src/main/resources/org/jscience/apps/i18n", "messages_apps"),
        ("jscience-featured-apps/src/main/resources/org/jscience/apps/i18n", "messages"),
    ]
    
    for path, prefix in modules:
        full_path = base / path
        if full_path.exists():
            sync_module(str(full_path), prefix)
        else:
            print(f"Path not found: {full_path}")

if __name__ == "__main__":
    main()
