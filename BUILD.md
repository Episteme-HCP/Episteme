ve thes launcher pacaked  int git# Build Instructions for JScience

This document describes how to build the JScience project, which consists of multiple modules targeting different Java versions.

## Prerequisites

*   **JDK 25 (Early Access)** or newer.
    *   The project requires a JDK capable of compiling Java 25 code (for Project Panama FFM API in `jscience-native`).
    *   Set your `JAVA_HOME` environment variable to point to this JDK.
    *   Verification: Run `java -version` and ensure it shows build 25+.
*   **Apache Maven 3.8+**.

## Project Structure & Java Versions

*   **jscience-core, jscience-natural, jscience-social**: Compiled with **Java 21** compatibility (`--release 21`).
*   **jscience-native**: Compiled with **Java 25** (`--release 25`) to leverage the Foreign Function & Memory API (Project Panama).

## How to Build

To build the entire project, run the following command from the root directory:

```bash
mvn clean install
```

This command will:
1.  Compile `jscience-core`, `jscience-natural`, and `jscience-social` using Java 21 compatibility.
2.  Compile `jscience-native` using Java 25 features (Preview/Panama enabled).
3.  Run tests (unless skipped with `-DskipTests`).
4.  Install artifacts to your local Maven repository.

### Skipping Tests

For a faster build that only validates compilation:

```bash
mvn clean install -DskipTests
```

## Troubleshooting

### `jscience-native` Compilation Errors
If you encounter errors related to `java.lang.foreign` or "restricted methods":
*   Ensure you are running with JDK 25+.
*   Ensure `maven-compiler-plugin` is configured to source/target 25 for the native module (this is handled in `jscience-native/pom.xml`).

### Module Visibility / Dependency Issues
The `jscience-native` module is configured to treat its dependencies (`core`, `natural`) as libraries on the classpath rather than strict JPMS modules. This ensures maximum compatibility with the legacy build mode of the core modules.
*   If you see `module-info.java.disabled` in `jscience-native`, this is intentional.

## Running Applications
When running applications that use `jscience-native`, you must provide the following JVM arguments to enable native access:

```bash
--enable-native-access=ALL-UNNAMED --enable-preview
```

Example (if running from an IDE or script):
Make sure your run configuration uses the Java 25 JDK.
