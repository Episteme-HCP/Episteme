# Build Instructions for JScience

This document describes how to build the JScience project, which consists of multiple modules targeting different Java versions.

## Prerequisites

* **JDK 25 (Early Access)** or newer.
  * The project requires a JDK capable of compiling Java 25 code (for Project Panama FFM API in `jscience-native`).
  * Set your `JAVA_HOME` environment variable to point to this JDK.
  * Verification: Run `java -version` and ensure it shows build 25+.
* **Apache Maven 3.8+**.

### Local Library Installation

Some dependencies are not available in public Maven repositories and must be installed manually into your local repository.

**java-fbx-loader (v1.0)**:
This library is required by `jscience-natural`. Run the following command from the project root:

```bash
mvn install:install-file -Dfile=launchers/lib/java-fbx-loader-v1.0.jar -DgroupId=com.github.studygameengines -DartifactId=java-fbx-loader -Dversion=1.0 -Dpackaging=jar
```

## Project Structure & Java Versions

* **jscience-core, jscience-natural, jscience-social**: Compiled with **Java 21** compatibility (`--release 21`).
* **jscience-native**: Compiled with **Java 25** (`--release 25`) to leverage the Foreign Function & Memory API (Project Panama).

## How to Build

To build the entire project, run the following command from the root directory:

```bash
mvn clean install
```

This command will:

1. Compile `jscience-core`, `jscience-natural`, `jscience-social` using Java 21 compatibility.
2. Compile `jscience-native` using Java 25 features (Preview/Panama enabled).
3. Run tests (unless skipped with `-DskipTests`).
4. Install artifacts to your local Maven repository.

### Skipping Tests

For a faster build that only validates compilation:

```bash
mvn clean install -DskipTests
```

## Packaging for Distribution

To generate a distribution package containing the application JARs (Thin format), run the `BuildPackager` script from the root:

**Windows:**

```powershell
.\BuildPackager.ps1
```

**Linux/Mac:**

```bash
./build_packager.sh
```

This script will:

1. Perform a full clean build (`mvn clean install`).
2. Extract the **Thin JARs** (stripping out shaded dependencies) to `launchers/packaged`.
3. The resulting JARs are lightweight and expect dependencies to be present in `launchers/lib` (which is shared).

The `launchers/packaged` directory is tracked in git to facilitate releases.

## Troubleshooting

### `jscience-native` Compilation Errors

If you encounter errors related to `java.lang.foreign` or "restricted methods":

* Ensure you are running with JDK 25+.
* Ensure `maven-compiler-plugin` is configured to source/target 25 for the native module (this is handled in `jscience-native/pom.xml`).

### Module Visibility / Dependency Issues

The `jscience-native` module is configured to treat its dependencies (`core`, `natural`) as libraries on the classpath rather than strict JPMS modules. This ensures maximum compatibility with the legacy build mode of the core modules.

* If you see `module-info.java.disabled` in `jscience-native`, this is intentional.

## Running Applications

When running applications that use `jscience-native`, you must provide the following JVM arguments to enable native access:

```bash
--enable-native-access=ALL-UNNAMED --enable-preview
```

Example (if running from an IDE or script):
Make sure your run configuration uses the Java 25 JDK.
