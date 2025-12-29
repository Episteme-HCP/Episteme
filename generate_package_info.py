import os

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

def create_package_info(path, package_name):
    content = HEADER + "\n"
    content += "/**\n"
    content += f" * This package contains classes for {package_name}.\n"
    content += " *\n"
    content += f" * {AUTHOR_SILVERE}\n"
    content += f" * {AUTHOR_GEMINI}\n"
    content += f" * {SINCE_TAG}\n"
    content += " */\n"
    content += f"package {package_name};\n"
    
    with open(os.path.join(path, "package-info.java"), "w", encoding="utf-8") as f:
        f.write(content)

def main():
    exclude_dirs = {'jscience-old-v1', 'target', '.git', '.mvn', '.gemini'}
    for root, dirs, files in os.walk('.'):
        dirs[:] = [d for d in dirs if d not in exclude_dirs]
        
        if "src\\main\\java" in root or "src/main/java" in root:
            # Check if there are java files in this dir
            java_files = [f for f in files if f.endswith(".java") and f != "package-info.java"]
            
            if java_files or any(d for d in dirs): # If it has java files OR subdirectories that might have java files
                # Determine package name
                parts = root.replace("\\", "/").split("/src/main/java/")
                if len(parts) > 1:
                    package_path = parts[1]
                    package_name = package_path.replace("/", ".")
                    
                    if not package_name: # Root package
                        continue
                    
                    if "package-info.java" not in files:
                        print(f"Creating package-info for {package_name} in {root}")
                        create_package_info(root, package_name)

if __name__ == "__main__":
    main()
