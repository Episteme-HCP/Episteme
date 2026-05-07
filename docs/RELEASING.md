# Episteme Release Guide

This document explains the multi-JDK release strategy for Episteme, ensuring compatibility across different Java environments while leveraging modern features.

## Multi-JDK Release Strategy

Episteme follows a dual-target release strategy for its core modules, specifically targeting **JDK 21 (LTS)** and **JDK 25 (Latest/Preview)**.

### 1. Module Compatibility Matrix

| Module | Target JDK | Rationale |
| :--- | :--- | :--- |
| `episteme-core` | **21 & 25** | Core mathematical logic must be available to legacy and modern systems. |
| `episteme-server` | **25** | Leverages latest gRPC optimizations and Spring Boot 3.5. |
| `episteme-client` | **25** | Uses modern JavaFX and Vector API optimizations. |
| `episteme-jni` | **21** | Maximum compatibility for native library linkage. |

### 2. Maven Profile Configuration

The `episteme-core/pom.xml` contains profiles to target different JDK versions.

- **`release-jdk21`**: Sets `maven.compiler.release` to 21 and adds the `jdk21` classifier.
- **`release-jdk25`**: Sets `maven.compiler.release` to 25 and adds the `jdk25` classifier (default).

```bash
# Build for JDK 21
mvn package -Prelease-jdk21

# Build for JDK 25
mvn package -Prelease-jdk25
```

### 3. GitHub Actions Integration

The `.github/workflows/release.yml` is configured to build and upload both versions automatically when a tag is pushed.

```yaml
jobs:
  build:
    strategy:
      matrix:
        java-version: [21, 25]
    steps:
      - name: Build and Package
        run: mvn package -DskipTests -Prelease-jdk${{ matrix.java-version }}
```

### 4. Release Naming Convention

Artifacts are published with classifiers:
- `episteme-core-1.0.0-jdk21.jar`
- `episteme-core-1.0.0-jdk25.jar`

## Releasing a New Version

1.  **Update Version**: Use `mvn versions:set -DnewVersion=1.0.1` to update all modules.
2.  **Tag**: Create a git tag `v1.0.1`.
3.  **Push**: Push the tag to GitHub.
4.  **Verification**: Ensure the "Release" workflow completes successfully and artifacts are attached to the GitHub Release.
