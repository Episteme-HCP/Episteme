
import os

target_dir = r"C:\Silvere\Encours\Developpement\JScience\jscience-io\src\main\java\org\jscience\io\mathml"

count = 0
for filename in os.listdir(target_dir):
    if filename.endswith(".java"):
        filepath = os.path.join(target_dir, filename)
        with open(filepath, 'r') as f:
            content = f.read()
        
        new_content = content.replace("package org.jscience.ml.mathml;", "package org.jscience.io.mathml;")
        # Also fix the import for MathMLDocumentImpl if it refers to the old package from other files
        new_content = new_content.replace("import org.jscience.ml.mathml.", "import org.jscience.io.mathml.")
        
        if new_content != content:
            with open(filepath, 'w') as f:
                f.write(new_content)
            count += 1
            print(f"Updated {filename}")

print(f"Total files updated: {count}")
