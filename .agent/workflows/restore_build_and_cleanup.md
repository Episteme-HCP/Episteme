# Implementation Plan - Restoring Build & Cleaning Up

This plan addresses the immediate build blocker and performs the cleanup of unused files.

## 1. Fix Maven Compiler Configuration
The current root `pom.xml` incorrectly bundles multiple compiler arguments into a single property used within a single `<arg>` tag. This causes `javac` to fail when parsing the `--add-modules` option.

- **Action**: Modify `pom.xml` to correctly separate compiler arguments.
- **File**: `C:\Silvere\Encours\Developpement\JScience\pom.xml`

## 2. Verify Build
Run a clean compile of the core module and the entire project to ensure the fix works and to see if SIMD warnings persist.

- **Command**: `mvn clean compile -pl :jscience-core`
- **Command**: `mvn clean compile -DskipTests`

## 3. Root Directory Cleanup
The files `benchmark_data.h5` and `benchmark_read_data.h5` have been confirmed as unused.

- **Action**: Delete these files.
- **Files**:
    - `C:\Silvere\Encours\Developpement\JScience\benchmark_data.h5`
    - `C:\Silvere\Encours\Developpement\JScience\benchmark_read_data.h5`

## 4. Resolve Persistent SIMD Warnings
If the build succeeds but the IDE/compiler still reports "VectorSpecies cannot be resolved", I will verify if the environment (JDK 21+) and the compiler flags are actually active.

- **Action**: Investigate `SIMDRealDoubleMatrix.java` diagnostics.
