import os

HEADER = """/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
"""

AUTHOR_TAGS = """ * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */"""

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # 1. Update/Add License Header
    if content.strip().startswith("/*"):
        # Find end of comment
        end_idx = content.find("*/")
        if end_idx != -1:
             # Check if it looks like a license header (starts before package decl)
            package_idx = content.find("package ")
            if package_idx != -1 and end_idx < package_idx:
                # Replace existing header
                new_content = HEADER + content[end_idx+2:].lstrip()
            else:
                 # Comment is likely javadoc for class or package, prepend header
                new_content = HEADER + content
        else:
            new_content = HEADER + content
    else:
        new_content = HEADER + content

    # 2. Update Class Javadoc Authors/Since
    # This is trickier regex. We want to replace existing @author and @version/@since blocks 
    # or append them if missing in the class javadoc.
    # For simplicity/safety, let's look for the pattern of the bottom of class javadoc.
    
    # Heuristic: Find explicit @author tags and replace the block
    import re
    
    # Regex to match the block of authors/version/since at end of javadoc
    # Matches: * @author ... (multi names) ... */
    # We replace it with our standard block.
    
    doc_end_pattern = re.compile(r'(?:\s*\*\s*@author.*(?:\r?\n\s*\*\s*@.*)*\s*)\*\/')
    
    # Check if we find standard javadoc ending
    if "/**" in new_content:
        # We only want to target the CLASS javadoc, which usually precedes 'public class' or 'public interface' or 'public abstract'
        # But scanning for that is hard.
        # Let's just do a naive replace of the author block if found.
        
        match = doc_end_pattern.search(new_content)
        if match:
             # Found an author block, replace it
             # We use a lambda to ensure we replace the whole match group 0 but keep the '*/'
            new_content = doc_end_pattern.sub(AUTHOR_TAGS, new_content, 1) # Only first one (class level usually)

    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(new_content)

def main():
    root_dir = os.getcwd()
    print(f"Scanning {root_dir}")
    for dirpath, dirnames, filenames in os.walk(root_dir):
        if "target" in dirpath or ".git" in dirpath or ".gemini" in dirpath or "jscience-old-v1" in dirpath:
            continue
            
        for filename in filenames:
            if filename.endswith(".java") and filename != "package-info.java":
                process_file(os.path.join(dirpath, filename))
                
if __name__ == "__main__":
    main()
