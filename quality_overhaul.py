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

PACKAGE_DESCRIPTIONS = {
    "org.jscience.mathematics.numbers.real": "Real numbers (ℝ) and arbitrary precision decimal arithmetic.",
    "org.jscience.mathematics.numbers.complex": "Complex numbers (ℂ) and related algebraic operations.",
    "org.jscience.mathematics.numbers.integers": "Integer numbers (ℤ) and modular arithmetic.",
    "org.jscience.mathematics.linearalgebra": "Linear algebra structures including matrices, vectors, and tensors.",
    "org.jscience.mathematics.analysis": "Mathematical analysis, including functions, calculus, and numerical methods.",
    "org.jscience.mathematics.geometry": "Geometric structures, transformations, and spatial algorithms.",
    "org.jscience.mathematics.discrete": "Discrete mathematics, including graph theory and combinatorics.",
    "org.jscience.mathematics.statistics": "Statistical analysis, probability distributions, and data modeling.",
    "org.jscience.measure": "Physical quantities and units of measure system (SI and others).",
    "org.jscience.physics": "Physical constants, models, and simulations for various branches of physics.",
    "org.jscience.biology": "Biological modeling, genetics, and life science simulations.",
    "org.jscience.chemistry": "Chemical elements, reactions, and molecular modeling.",
    "org.jscience.geography": "Geographic locations, coordinates, and spatial data modeling.",
    "org.jscience.history": "Temporal events, timelines, and historical data modeling.",
    "org.jscience.politics": "Political systems, elections, and jurisdictional modeling.",
    "org.jscience.economics": "Economic modeling, financial quantities, and market simulations.",
    "org.jscience.sociology": "Social networks, demographics, and cultural data modeling.",
    "org.jscience.linguistics": "Natural language processing and linguistic structures.",
    "org.jscience.philosophy": "Ontological structures and philosophical systems modeling.",
    "org.jscience.psychology": "Cognitive modeling and psychological data analysis.",
    "org.jscience.arts": "Aesthetic structures and artistic data modeling.",
    "org.jscience.sports": "Athletic competitions and sports statistics modeling.",
    "org.jscience.bibliography": "Bibliographic references and documentation metadata.",
    "org.jscience.ui": "JavaFX-based user interface components and scientific viewers.",
    "org.jscience.device": "Hardware abstraction layer for scientific instruments and sensors.",
    "org.jscience.distributed": "Distributed computing and gRPC-based scientific services.",
    "org.jscience.util": "Utility classes and core framework infrastructure.",
}

def clean_javadoc_lines(lines):
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
        
        # Skip existing author/since tags to re-add them at the end
        if any(tag in line for tag in ["@author", "@since"]):
            continue
            
        clean_lines.append(f" * {line}".rstrip())

    # Remove trailing empty javadoc lines
    while len(clean_lines) > 2 and clean_lines[-2].strip() == "*":
        clean_lines.pop(-2)
        
    return clean_lines

def enrich_javadoc(javadoc_lines, description=None):
    clean_lines = clean_javadoc_lines(javadoc_lines)
    
    # Extract existing description text (everything before the first @ tag)
    desc_lines = []
    found_tag = False
    for line in clean_lines:
        if line.startswith("/**"): continue
        if line.strip() == "*/": continue
        if "@" in line:
            found_tag = True
            continue
        if not found_tag:
            desc_lines.append(line)
            
    # Prune leading/trailing empty description lines
    while desc_lines and desc_lines[0].strip() == "*":
        desc_lines.pop(0)
    while desc_lines and desc_lines[-1].strip() == "*":
        desc_lines.pop()

    # If the description is a placeholder or empty, use the provided one
    existing_desc = " ".join([l.replace("*", "").strip() for l in desc_lines]).strip()
    placeholder_indicators = ["Provides the", "Contains classes for", "module", "package-info.java"]
    
    is_placeholder = not existing_desc or any(p in existing_desc.lower() for p in placeholder_indicators) and len(existing_desc) < 60
    
    if is_placeholder and description:
        final_desc_lines = [f" * {description}"]
    elif desc_lines:
        final_desc_lines = desc_lines
    else:
        final_desc_lines = [f" * {description}"] if description else [" * "]

    # Combine
    final_lines = ["/**"]
    final_lines.extend(final_desc_lines)
    
    # Add a separator before tags if the last line of description isn't empty
    if final_lines[-1].strip() != "*":
        final_lines.append(" *")
    
    final_lines.append(f" * {AUTHOR_SILVERE}")
    final_lines.append(f" * {AUTHOR_GEMINI}")
    final_lines.append(f" * {SINCE_TAG}")
    final_lines.append(" */")
    
    return "\n".join(final_lines)

def process_package_info(filepath, root):
    parts = root.replace("\\", "/").split("/src/main/java/")
    if len(parts) <= 1: return
    package_name = parts[1].replace("/", ".")
    
    description = next((v for k, v in PACKAGE_DESCRIPTIONS.items() if package_name.startswith(k)), 
                      f"Classes and interfaces for {package_name}.")

    if not os.path.exists(filepath):
        content = HEADER + "\n"
        content += enrich_javadoc([], description) + "\n"
        content += f"package {package_name};\n"
        with open(filepath, 'w', encoding='utf-8') as f: f.write(content)
        return

    with open(filepath, 'r', encoding='utf-8') as f: content = f.read()
    
    # Standardize header
    if content.startswith("/*"):
        end_header = content.find("*/")
        if end_header != -1 and ("Copyright" in content[:end_header+2] or "JScience" in content[:end_header+2]):
            content = content[end_header+2:].lstrip()
            
    # Standardize Javadoc
    javadoc_match = re.search(r'/\*\*.*?\*/', content, re.DOTALL)
    if javadoc_match:
        javadoc_full = javadoc_match.group(0)
        new_javadoc = enrich_javadoc(javadoc_full.splitlines(), description)
        content = content.replace(javadoc_full, new_javadoc)
    else:
        pkg_decl = re.search(r'package\s+[\w.]+;', content)
        if pkg_decl:
            new_javadoc = enrich_javadoc([], description) + "\n"
            content = content[:pkg_decl.start()] + new_javadoc + content[pkg_decl.start():]

    final_content = HEADER + "\n" + content.lstrip()
    with open(filepath, 'w', encoding='utf-8') as f: f.write(final_content)

def process_java_file(filepath):
    if filepath.endswith("package-info.java"): return
    
    with open(filepath, 'r', encoding='utf-8') as f: content = f.read()
    
    # 1. Standardize Header
    if content.startswith("/*"):
        end_header = content.find("*/")
        if end_header != -1 and ("Copyright" in content[:end_header+2] or "JScience" in content[:end_header+2]):
            content = content[end_header+2:].lstrip()
            
    # 2. Enrich Class Javadoc
    class_pattern = re.compile(r'(public\s+(?:abstract\s+)?(?:class|interface|enum|record)\s+\w+)')
    match = class_pattern.search(content)
    if match:
        class_start = match.start()
        javadoc_end = content.rfind("*/", 0, class_start)
        if javadoc_end != -1:
            javadoc_start = content.rfind("/**", 0, javadoc_end)
            if javadoc_start != -1:
                javadoc_full = content[javadoc_start:javadoc_end+2]
                new_javadoc = enrich_javadoc(javadoc_full.splitlines())
                content = content[:javadoc_start] + new_javadoc + content[javadoc_end+2:]
            else:
                new_javadoc = enrich_javadoc([]) + "\n"
                content = content[:class_start] + new_javadoc + content[class_start:]
        else:
            new_javadoc = enrich_javadoc([]) + "\n"
            content = content[:class_start] + new_javadoc + content[class_start:]

    final_content = HEADER + "\n" + content.lstrip()
    with open(filepath, 'w', encoding='utf-8') as f: f.write(final_content)

    final_content = HEADER + "\n" + content.lstrip()
    with open(filepath, 'w', encoding='utf-8') as f: f.write(final_content)

def main():
    exclude_dirs = {'jscience-old-v1', 'target', '.git', '.mvn', '.gemini'}
    for root, dirs, files in os.walk('.'):
        dirs[:] = [d for d in dirs if d not in exclude_dirs]
        
        # Determine if we are in a java source directory
        if "src\\main\\java" in root or "src/main/java" in root:
            # First, check if package-info exists or needs to be created
            java_files = [f for f in files if f.endswith(".java")]
            if java_files:
                process_package_info(os.path.join(root, "package-info.java"), root)
                
            for file in files:
                if file.endswith(".java") and file != "package-info.java":
                    process_java_file(os.path.join(root, file))

if __name__ == "__main__":
    main()
