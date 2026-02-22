import os
import glob

# Collect scripts
files = glob.glob('*.bat') + glob.glob('*.sh') + glob.glob('launchers/*.bat') + glob.glob('launchers/*.sh')

# Exclude ones that shouldn't be blindly replaced or don't run java directly
excluded = ['run-master-control.bat', 'run-master-control.sh', 'build_installer', 'install_', 'generate_']
files = [f for f in files if not any(x in f for x in excluded)]

flags = "--add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

for f in files:
    with open(f, 'r') as file:
        content = file.read()
        
    if 'jdk.incubator.vector' in content:
        continue # already updated
        
    # Replace literal 'java ' with the injected flags
    new_content = content.replace('java ', f'java {flags} ')
    
    if new_content != content:
        with open(f, 'w') as file:
            file.write(new_content)
        print("Updated " + f)
