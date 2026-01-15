#!/usr/bin/env python3
import os
import re
from pathlib import Path

BASE_DIR = Path(r"c:\Silvere\Encours\Developpement\JScience")

def process_file(path):
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()
    
    original_content = content
    class_name = path.stem
    app_key_base = class_name.replace("Distributed", "").replace("App", "").lower()
    
    # helper for replacement
    def i18n_get(key, default):
        return f'org.jscience.ui.i18n.I18n.getInstance().get("{key}", "{default}")'

    # 1. Common UI Elements
    replacements = {
        'setPromptText("Session ID")': f'setPromptText({i18n_get("collab.prompt.session", "Session ID")})',
        'new Button("Join Session")': f'new Button({i18n_get("collab.btn.join", "Join Session")})',
        'new Button("Create Session")': f'new Button({i18n_get("collab.btn.create", "Create Session")})',
        'new Label("Brush:")': f'new Label({i18n_get("collab.label.brush", "Brush:")})',
        'new Button("Clear")': f'new Button({i18n_get("collab.btn.clear", "Clear")})',
        'new Label("Not connected to any session")': f'new Label({i18n_get("status.not_connected", "Not connected to any session")})',
        'statusLabel.setText("Internal Error")': f'statusLabel.setText({i18n_get("status.error", "Internal Error")})',
    }
    
    for old, new in replacements.items():
        content = content.replace(old, new)
        
    # 2. Main Title (in Toolbar)
    # Pattern: new Label("... Some Title");
    # We want to capture the string, clean it (remove emojis), and key it.
    title_regex = re.compile(r'new Label\("([^"]+)"\);')
    
    def title_replacer(match):
        text = match.group(1)
        if "Session ID" in text or "Brush" in text or "Not connected" in text: return match.group(0) # Already handled or ignored
        
        # Clean emojis and weird chars if present (simple ASCII check or just use as is)
        clean_text = text.encode('ascii', 'ignore').decode().strip()
        if not clean_text: clean_text = text # Fallback
        
        # Heuristic for app title
        if "Collaborative" in text or "Distributed" in text or "App" in class_name:
            key = f"app.title.{app_key_base}"
            return f'new Label({i18n_get(key, clean_text)});'
        return match.group(0)

    content = title_regex.sub(title_replacer, content)

    # 3. Status messages with logic (HARD!)
    # createSession success: statusLabel.setText("... Created session: " + sessionId);
    # Regex for "String" + var
    concat_regex = re.compile(r'statusLabel\.setText\("([^"]+)" \+ ([a-zA-Z0-9_().]+)\);')
    
    def concat_replacer(match):
        text = match.group(1)
        var = match.group(2)
        
        # Identify context
        if "Created session" in text: key = "status.session_created"
        elif "Connected to session" in text: key = "status.session_connected"
        elif "Failed to create" in text: key = "status.session_create_error"
        elif "Connection error" in text: key = "status.connection_error"
        else: return match.group(0) # Skip unknown
        
        # Clean text
        clean_text = text.encode('ascii', 'ignore').decode().strip()
        if not clean_text: clean_text = text
        if clean_text.endswith(":"): clean_text = clean_text[:-1]
        
        # Param format
        # If text ends with ": ", we assume "{0}"
        fmt = clean_text + ": {0}"
        
        return f'statusLabel.setText(java.text.MessageFormat.format({i18n_get(key, fmt)}, {var}));'

    content = concat_regex.sub(concat_replacer, content)

    # 4. Connected to session complex case: "Connected...: " + sessionId + " as " + userId
    # This is "String" + var + "String" + var
    complex_regex = re.compile(r'statusLabel\.setText\("([^"]+)" \+ ([^+]+) \+ "([^"]+)" \+ ([^;]+)\);')
    
    def complex_replacer(match):
        p1 = match.group(1) # Connected to session: 
        v1 = match.group(2) # sessionId
        p2 = match.group(3) # " as "
        v2 = match.group(4) # userId
        
        if "Connected to session" in p1:
            key = "status.session_connected_user"
            fmt = "Connected to session: {0} as {1}"
            return f'statusLabel.setText(java.text.MessageFormat.format({i18n_get(key, fmt)}, {v1}, {v2}));'
        return match.group(0)
        
    content = complex_regex.sub(complex_replacer, content)

    # 5. Window Title
    # primaryStage.setTitle("JScience ... - " + userId);
    win_title_regex = re.compile(r'primaryStage\.setTitle\("([^"]+)" \+ userId\);')
    def win_title_replacer(match):
        base_title = match.group(1)
        key = f"window.title.{app_key_base}"
        fmt = base_title.strip().replace(" -", "") + " - {0}"
        return f'primaryStage.setTitle(java.text.MessageFormat.format({i18n_get(key, fmt)}, userId));'
    
    content = win_title_regex.sub(win_title_replacer, content)
    
    # Write back if changed
    if content != original_content:
        with open(path, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Updated {path.name}")
        return True
    return False

def main():
    target_dir = BASE_DIR / "jscience-client/src/main/java/org/jscience/client"
    count = 0
    for root, _, files in os.walk(target_dir):
        for file in files:
            if file.startswith("Distributed") and file.endswith("App.java"):
                if process_file(Path(root) / file):
                    count += 1
    print(f"Total updated: {count}")

if __name__ == "__main__":
    main()
