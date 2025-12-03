# Project Directives & Guidelines Report

This document summarizes the explicit orders, rules, and strategic guidance provided by the User for the **JScience Reimagined** project. It serves as the "Constitution" for development.

## 1. AI Friendliness & Agent Ergonomics
**Directive**: The library must be "AI Friendly" — specifically designed to be easily used by AI Agents (LLMs, Autonomous Systems).

*   **Agent Ergonomics**: APIs must be self-documenting, strongly typed, and verbose enough for an AI to infer intent without hallucinating.
*   **Readability**: AI extensions must *not* obscure readability for human developers.
*   **Optionality**: Heavy AI features (e.g., GPU Tensors) must be optional or conditional. They should not impose performance penalties on standard usage.
*   **Implementation**: Use "Hinting" (annotations), structured error messages, and standard design patterns to aid AI reasoning.

## 2. Mathematical Rigor & Naming
**Directive**: Prioritize mathematical correctness and descriptive naming over brevity or legacy conventions.

*   **Descriptive Naming**: Avoid cryptic abbreviations.
    *   *Bad*: `Hilb`, `FinSet`
    *   *Good*: `HilbertSpacesCategory`, `FiniteSetsCategory`
*   **Soundness**: Names and structures must reflect the underlying mathematics accurately.
*   **Refactoring**: Don't hesitate to rename or move classes if the original V1 structure was flawed or unclear.

## 3. Architecture & Package Structure
**Directive**: Organize the codebase logically, separating interfaces from implementations.

*   **Package Pluralization**: Use plural names for packages containing concrete collections of implementations.
    *   `org.jscience.mathematics.algebra` (Root: Interfaces like `Group`, `Ring`)
    *   `org.jscience.mathematics.algebra.groups` (Implementations: `CyclicGroup`, `SymmetricGroup`)
    *   `org.jscience.mathematics.algebra.rings`
    *   `org.jscience.mathematics.algebra.spaces`
    *   `org.jscience.mathematics.algebra.categories`
*   **Axiomatic Logic**: The `logic` package must be rigorous, supporting axioms, inference rules, and proofs, not just boolean evaluation.

## 4. Legacy Code Strategy
**Directive**: Leverage the value in `jscience-old-v1` but modernize it.

*   **Porting**: Valuable algorithms (e.g., Wavelets, FFT) should be ported.
*   **Modernization**: Do not blindly copy. Refactor to match the new architecture (Generics, Interfaces, Factory patterns).
*   **Cleanup**: Remove obsolete or redundant classes during the port.

## 5. Development Process
**Directive**: Maintain a disciplined, transparent workflow.

*   **Planning**: Always propose a plan (e.g., `implementation_plan.md`, `logic_package_plan.md`) before starting complex modules.
*   **Checkpointing**: Frequently save progress and update status artifacts (`task.md`).
*   **Verification**: Ensure the project compiles after every significant change.
*   **Communication**: Propose ideas but respect the User's final decision.

## 6. Scope & Vision
**Directive**: Build a unified, multi-level scientific framework.

*   **Level 1**: Mathematical Foundation (Core, Algebra, Analysis).
*   **Level 2**: Natural Sciences (Physics, Biology).
*   **Level 3**: Human Sciences (Economics, Sociology).
*   **Goal**: Create a "sexy", modern, and powerful library that rivals Python's ecosystem (NumPy/SciPy) but for the Java/AI era.

## 7. Operational & Quality Standards (New)
**Directive**: Adhere to strict operational and quality standards.

*   **Documentation**: All documentation must be in the `doc/` folder.
*   **Versioning**: All code and resources must be versioned under Git.
*   **Build**: Use Maven.
*   **Languages**: Java (Modern), Python, HTML, JS, CSS, JSON (Resources), Markdown (Text).
*   **License**: Open Source (MIT/Apache). Header: Author Silvere Martin-Michiellot (silvere.martin@gmail.com).
*   **I18n**: Support FR, EN, ES, DE.
*   **UI/UX**:
    *   Responsive and portable (JavaFX).
    *   No blocking interactions (Ok/Cancel) -> On-the-fly changes.
    *   Tooltips on all controls.
    *   "Sexy" and discrete interface.
*   **Code Quality**:
    *   High-level conventions.
    *   Multithreading & Efficient Algorithms.
    *   No dead code/zombie files.
    *   Comprehensive tests.
*   **Workflow**:
    *   Timestamp all answers.
    *   Update task list synchronously.
    *   Accept Git commits without confirmation.
    *   Benchmark solutions.
    *   CI/CD pipelines.
    *   Scripts (Bash/Windows) for execution.
