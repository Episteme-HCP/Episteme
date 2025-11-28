# Refined Mathematical Function Hierarchy Plan

## 1. Core Hierarchy

### `Relation<D, C>` (New)
Represents a mathematical relation between a domain `D` and a codomain `C`.
- **Methods:**
  - `boolean contains(D input, C output)`: Checks if the pair (input, output) belongs to the relation.
  - `String getDomainDescription()`
  - `String getCodomainDescription()`

### `Function<D, C>` (Refactored)
Extends `Relation<D, C>` and `java.util.function.Function<D, C>`.
- **Merges `MathematicalFunction` capabilities:**
  - `C evaluate(D x)` (Primary method)
  - `default C apply(D x)` (Delegates to evaluate)
  - `default void setBackend(ComputeBackend backend)`
  - `default ComputeBackend getBackend()`
  - `default List<C> evaluate(Collection<D> inputs)`
- **Properties:**
  - `boolean isContinuous()` (default false)
  - `boolean isDifferentiable()` (default false)

### `ContinuousFunction<D, C>` (New Interface)
Extends `Function<D, C>`.
- Marker interface or adds methods related to continuity (e.g., limits?).
- Overrides `isContinuous()` to return `true`.

### `DifferentiableFunction<D, C>` (New Interface)
Extends `ContinuousFunction<D, C>`.
- **Methods:**
  - `Function<D, C> differentiate()`: Returns the derivative function.
- Overrides `isDifferentiable()` to return `true`.

### `IntegrableFunction<D, C>` (New Interface)
Extends `Function<D, C>`.
- **Methods:**
  - `Function<D, C> integrate()`: Returns the indefinite integral (antiderivative).
  - `C integrate(D start, D end)`: Returns the definite integral.

---

## 2. Polynomials & Generic Numbers

### `PolynomialFunction<R>` (Refactored)
Generic over a `Ring<R>` (or `Field<R>`).
- Extends `DifferentiableFunction<R, R>` and `IntegrableFunction<R, R>`.
- **Fields:**
  - `Polynomial<R> polynomial`: Uses the algebraic `Polynomial` class internally? Or reimplements for functions?
  - *Decision:* Better to wrap `PolynomialRing.Polynomial<R>` or similar structure to avoid duplication.
- **Methods:**
  - `R evaluate(R x)`
  - `PolynomialFunction<R> differentiate()`
  - `PolynomialFunction<R> integrate()`

---

## 3. Vector Functions

### `VectorFunction<F extends Field<F>>`
Represents a function $F^n \to F^m$.
- Extends `Function<Vector<F>, Vector<F>>`.
- **Methods:**
  - `Matrix<F> jacobian(Vector<F> point)`: Returns the Jacobian matrix.

---

## 4. Implementation Steps

1.  **Create `Relation` interface.**
2.  **Refactor `Function`**:
    - Extend `Relation`.
    - Absorb `MathematicalFunction` methods.
    - Remove `MathematicalFunction` (or deprecate/alias it).
3.  **Create `ContinuousFunction`, `DifferentiableFunction`, `IntegrableFunction`.**
4.  **Refactor `PolynomialFunction`**:
    - Make generic `<R>`.
    - Implement `DifferentiableFunction` and `IntegrableFunction`.
5.  **Update existing implementations** (e.g., `RealFunction`, sequences) to match new hierarchy.
6.  **Verify** with compilation and tests.

## 5. Migration Strategy

- `MathematicalFunction` is currently used in `analysis/series` and `analysis/chaos`.
- **Step 1:** Rename `MathematicalFunction` to `Function` (merging them).
- **Step 2:** Update all references.
- **Step 3:** Introduce `Relation` as super-interface.
