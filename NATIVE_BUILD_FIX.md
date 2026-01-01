# GraalVM Native Image Build - CI/CD Fix

## Problem

GitHub CI was failing with:

```
'gu' tool was not found in your JAVA_HOME. 
This probably means that the JDK at '/opt/hostedtoolcache/Java_Temurin-Hotspot_jdk/21.0.9-10/x64' 
is not a GraalVM distribution.
```

## Solution

Moved GraalVM native image build to an **optional Maven profile** named `native`.

---

## Building the Project

### Standard Build (GitHub CI, Local Development)

```bash
# Build with regular JDK (no native image)
mvn clean package

# Or just compile  
mvn clean compile
```

**Result**: Standard JAR file in `target/jscience-server-1.0.0-SNAPSHOT.jar`

### Native Image Build (with GraalVM)

```bash
# Explicit profile activation
mvn clean package -Pnative

# Or set environment variable for auto-activation
export GRAALVM_HOME=/path/to/graalvm
mvn clean package
```

**Result**: Native executable in `target/jscience-server` (Linux/Mac) or `target/jscience-server.exe` (Windows)

---

## GitHub CI Configuration

### Option 1: Standard Build (Recommended for CI)

Current GitHub Actions workflow should work without changes:

```yaml
- name: Setup Java
  uses: actions/setup-java@v3
  with:
    distribution: 'temurin'
    java-version: '21'

- name: Build with Maven
  run: mvn clean package -DskipTests
```

**This will now work!** ✅ The native profile won't activate without GraalVM.

### Option 2: Native Build in CI (Optional)

If you want to build native images in CI:

```yaml
- name: Setup GraalVM
  uses: graalvm/setup-graalvm@v1
  with:
    java-version: '21'
    distribution: 'graalvm'
    github-token: ${{ secrets.GITHUB_TOKEN }}

- name: Build Native Image
  run: mvn clean package -Pnative -DskipTests
  env:
    GRAALVM_HOME: ${{ env.JAVA_HOME }}
```

---

## Profile Activation

The `native` profile activates when:

1. **Explicitly requested**: `mvn package -Pnative`
2. **Auto-activation**: When `GRAALVM_HOME` environment variable is set

### Check if Profile is Active

```bash
mvn help:active-profiles
```

---

## Local Development

### With Regular JDK

```bash
# Standard build - works everywhere
mvn clean package
java -jar target/jscience-server-1.0.0-SNAPSHOT.jar
```

### With GraalVM (for native builds)

```bash
# Install GraalVM
sdk install java 21-graalvm  # using SDKMAN
# OR download from: https://www.graalvm.org/downloads/

# Set environment
export GRAALVM_HOME=$JAVA_HOME

# Build native
mvn clean package -Pnative

# Run native executable (much faster startup!)
./target/jscience-server
```

---

## Benefits of This Approach

### ✅ Pros

- **CI/CD compatible**: Works with any JDK 21+
- **Optional native builds**: Developers can choose
- **No breaking changes**: Existing builds unchanged
- **Flexible**: Easy to enable/disable

### ⚠️  Native Build Considerations

- **Build time**: Native compilation takes 5-10x longer
- **Memory**: Requires 8GB+ RAM for native build
- **Platform-specific**: Native executables aren't portable
- **Limited reflection**: May need reflection configuration for some libraries

---

## When to Use Native Images

### Good Use Cases ✅

- **Production deployments**: Faster startup, lower memory
- **Serverless/FaaS**: Quick cold starts
- **CLI tools**: Instant startup feels native
- **Microservices**: Reduced resource footprint

### Not Recommended ❌

- **Development**: Slow rebuild cycles
- **CI/CD testing**: Adds build time
- **Highly dynamic code**: Extensive reflection/proxies

---

## Troubleshooting

### Profile Not Activating

```bash
# Check active profiles
mvn help:active-profiles

# Force activation
mvn package -Pnative

# Verify GRAALVM_HOME
echo $GRAALVM_HOME
```

### Native Build Fails

```bash
# Check GraalVM version
java -version  # Should show "GraalVM"

# Install native-image component
gu install native-image

# Build with verbose output
mvn package -Pnative -X
```

### CI Still Failing

Ensure your `.github/workflows/*.yml` uses:

```yaml
- run: mvn clean package -DskipTests
  # NOT: mvn clean package -Pnative -DskipTests
```

---

## File Changes Made

### pom.xml

```xml
<profiles>
    <profile>
        <id>native</id>
        <activation>
            <property>
                <name>env.GRAALVM_HOME</name>
            </property>
        </activation>
        <build>
            <plugins>
                <!-- GraalVM Native Image Plugin -->
                <plugin>
                    <groupId>org.graalvm.buildtools</groupId>
                    <artifactId>native-maven-plugin</artifactId>
                    <version>0.9.28</version>
                    <!-- ... configuration ... -->
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

**Before**: Plugin always executed during `package` phase  
**After**: Plugin only executes when `native` profile is active

---

## Summary

✅ **Fixed**: CI builds now work with regular JDK  
✅ **Optional**: Native builds available when needed  
✅ **Flexible**: Auto-activates with `GRAALVM_HOME`  
✅ **Documented**: Clear instructions for all scenarios  

**Your GitHub CI should now pass!** 🎉

---

*Created: 2026-01-01*  
*Issue: GraalVM native-maven-plugin CI failure*  
*Resolution: Optional Maven profile*
