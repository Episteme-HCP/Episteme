# JScience Project Status & Task Report

This document provides a comprehensive summary of the current state of the JScience project, including audit results, recent fixes, and a detailed task list. This is intended to facilitate the transition to a new project session.

---

## 🚀 Active & Pending Tasks

- [ ] **Task 30: Fix Native Provider Fidelity**
    - **Status**: HIGH PRIORITY.
    - **Goal**: Prevent ND4J, SIMD, and Bullet providers from silently degrading precision.
    - **Action**: Implement "Precision Guards" that throw `UnsupportedOperationException` or return null if the current `MathContext` requires precision higher than `double`. This will trigger the `ProviderSelector` to fallback to `CPUDenseLinearAlgebraProvider`.

- [ ] **Task 31: Python Binding for Genesis**
    - **Status**: **BLOCKED**.
    - **Blocker**: The "Genesis" component cannot be found in the current repository, git history, or documentation.
    - **Next Step**: Await user clarification on the location of this component.

- [ ] **Task 28: Enforce Precision Context**
    - **Goal**: Ensure all providers respect the `MathContext` passed via `OperationContext`.

- [ ] **Task 25: Fix Unavailable Libraries (Benchmarks)**
    - **Goal**: Identify and resolve why libraries like JBlas show as "Unavailable" in the UI despite being in `pom.xml`.
    - **Current Finding**: Likely a native `.dll` / `.so` path issue, not a dependency issue.

---

## ✅ Recent Critical Fixes

### 1. Multicore Providers (`StackOverflowError`)
- **Fix**: Resolved infinite recursion in `MulticoreMonteCarloProvider` and `MulticoreGeneticAlgorithmProvider`.
- **Solution**: Implemented a **Lazy Fallback Lookup** pattern. The `getFallback()` method now uses `AlgorithmManager` to dynamically discover a *different* provider at runtime, breaking the circular static initialization.

### 2. Audio Backend Stability (`NoClassDefFoundError`)
- **Fix**: Prevented UI crashes when optional `TarsosDSP` or `Minim` libraries are missing.
- **Solution**: Applied the **Isolated Engine Delegate** pattern. Moved library-dependent code into static inner classes (`TarsosEngine`, `MinimEngine`) that are only loaded if `isAvailable()` returns true.

### 3. Benchmark UI Status Fix
- **Fix**: Resolved the "Ready" status showing while benchmarks were "Running" or "Queued".
- **Solution**: Modified `MainController.java` to include "Queued" in active status checks and added proactive UI refreshes (`updateCategoryStatuses`) when benchmarks start.

### 4. CI/CD & Build
- **JaCoCo**: Disabled `jacoco-maven-plugin` to resolve compatibility issues with JDK 25.
- **MPJ**: Manually installed `mpj.jar` (v0.44) for distributed N-Body simulation.

---

## 🔍 Provider Audit Summary

The recent **Precision Fidelity Audit** (Task 26) revealed a systemic issue:

| Provider | Result | Precision Level | Root Cause |
| :--- | :---: | :--- | :--- |
| **ND4J** | ❌ FAIL | `double` | Implicit conversion to `INDArray` |
| **SIMD** | ❌ FAIL | `double` | Uses JDK Vector API `DoubleVector` |
| **Bullet** | ❌ FAIL | `float` | `libbulletc` uses float precision |
| **JBlas** | ❌ FAIL | `double` | Library-level restriction |
| **CPU (Dense)** | ✅ PASS | `Generic (E)` | Honors `Field<E>`/`Real` precision |

**Tooling**: `ProviderVerificationSuite.java` has been added to `jscience-core` tests to enable future automated correctness checks.

---

## 📋 Comprehensive Task List (Master)

### Infrastructure & Core
- [x] Task 10: `jscience-core` `mvn clean compile`
- [x] Task 13: Address `@AutoService` generic warnings
- [x] Task 16: Fix `TarsosBackend` unused fields
- [x] Task 17: Verify `MulticoreNBodyProvider` singleton status
- [x] Task 29: **Fix UI Provider Detection** (StackOverflow & NoClassDefFound)
- [x] Task 33: **Fix CI/CD Workflows** (JaCoCo & Artifact upload)
- [ ] Task 4: Refactor `ComputeContext` God Object (High Complexity)

### Native & Performance
- [x] Task 1 & 19: Implement/Move `ND4JLinearAlgebraProvider` to native
- [x] Task 6: Fix missing `bullet_capi` DLL loading
- [x] Task 20: Implement `NativeBulletBackend` (Panama FFM)
- [x] Task 18: Complete `NativeFFMBLASBackend`
- [x] Task 9: Implement `score()` for GPU (OpenCL/CUDA)
- [ ] Task 30: **Fix Native Provider Fidelity** (Precision Guards)

### Benchmarks & UI
- [x] Task 21: Benchmark UI: Multiselection (Ctrl+Click)
- [x] Task 22: Benchmark UI: Results Disambiguation
- [x] Task 23: Benchmark Export: System Context headers
- [x] Task 32: **Fix Benchmark UI Status** (Real-time updates)
- [ ] Task 25: Fix Unavailable Libraries (Native path audit)

### Verification
- [x] Task 26: **Full Provider Audit** (Manual + Initial automated logic)
- [x] Task 27: Implement `ProviderVerificationSuite`
- [ ] Task 28: Enforce Precision Context

---

## 📌 Context for Resumption
- **Working Dir**: `c:\Silvere\Encours\Developpement\JScience`
- **Main Controller**: `MainController.java` (UI Logic)
- **Algorithm Entry**: `AlgorithmManager.java` (Provider Discovery)
- **Critical File**: `audit_report.md` (Contains precision failure details)
