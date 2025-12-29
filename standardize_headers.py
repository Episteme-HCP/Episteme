import os
import re

HEADER = """/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

AUTHOR_SILVERE = "@author Silvere Martin-Michiellot"
AUTHOR_GEMINI = "@author Gemini AI (Google DeepMind)"
SINCE_TAG = "@since 1.0"

def standardize_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # 1. Standardize License Header
    new_header = HEADER + "\n"
    
    # Remove existing header if it exists
    if content.startswith("/*"):
        end_header = content.find("*/")
        if end_header != -1 and ("Copyright" in content[:end_header+2] or "JScience" in content[:end_header+2]):
            content = content[end_header+2:].lstrip()
    
    # 2. Add Author and Since tags to Class Javadoc
    class_pattern = re.compile(r'(public\s+(?:abstract\s+)?(?:class|interface|enum|record)\s+\w+)')
    match = class_pattern.search(content)
    
    if match:
        class_start = match.start()
        javadoc_end = content.rfind("*/", 0, class_start)
        if javadoc_end != -1:
            javadoc_start = content.rfind("/**", 0, javadoc_end)
            if javadoc_start != -1:
                # We found a Javadoc block
                javadoc_full = content[javadoc_start:javadoc_end+2]
                
                # Split into lines and clean up
                lines = javadoc_full.splitlines()
                clean_lines = []
                for line in lines:
                    line = line.strip()
                    if line.startswith("/**"):
                        clean_lines.append("/**")
                        continue
                    if line.endswith("*/"):
                        clean_lines.append(" */")
                        continue
                    
                    # Remove redundant markers like * *
                    line = re.sub(r'^\*+\s*\*+', '*', line)
                    if line.startswith("*"):
                        line = line[1:].strip()
                    
                    # Skip existing author/since tags
                    if any(tag in line for tag in ["@author", "@since"]):
                        continue
                    
                    clean_lines.append(f" * {line}".rstrip())
                
                # Remove trailing empty javadoc lines
                while len(clean_lines) > 2 and clean_lines[-2].strip() == "*":
                    clean_lines.pop(-2)
                
                # Add standardized tags
                clean_lines.insert(-1, " *")
                clean_lines.insert(-1, f" * {AUTHOR_SILVERE}")
                clean_lines.insert(-1, f" * {AUTHOR_GEMINI}")
                clean_lines.insert(-1, f" * {SINCE_TAG}")
                
                new_javadoc = "\n".join(clean_lines)
                content = content[:javadoc_start] + new_javadoc + content[javadoc_end+2:]
            else:
                # No Javadoc found, create one
                new_javadoc = f"/**\n * \n * {AUTHOR_SILVERE}\n * {AUTHOR_GEMINI}\n * {SINCE_TAG}\n */\n"
                content = content[:class_start] + new_javadoc + content[class_start:]

    final_content = new_header + content
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(final_content)

def main():
    exclude_dirs = {'jscience-old-v1', 'target', '.git', '.mvn', '.gemini'}
    for root, dirs, files in os.walk('.'):
        dirs[:] = [d for d in dirs if d not in exclude_dirs]
        for file in files:
            if file.endswith('.java'):
                standardize_file(os.path.join(root, file))

if __name__ == "__main__":
    main()
