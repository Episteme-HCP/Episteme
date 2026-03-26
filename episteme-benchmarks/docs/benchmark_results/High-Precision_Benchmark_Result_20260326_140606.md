# High-Precision Benchmark Result - Numerical Correctness

> Generated on Thu Mar 26 14:06:06 CET 2026

| Provider | RB:Add_Comm | RB:Mul_Identity | RB:Inv_Identity | RB:Solve_Verify | RB:SinCosId | RB:LogExp | C:Inv_Identity | C:SinCosId | RB:Sparse_Solve |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Episteme (CARMA) | ✅ PASS | ❌ FAIL | ❌ FAIL | ✅ PASS | ❌ FAIL | ❌ FAIL | ✅ PASS | ❌ FAIL | ⬜ N/A |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ FAIL | ❌ FAIL | ✅ PASS | ❌ FAIL | ⬜ N/A |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ FAIL | ❌ FAIL | ✅ PASS | ❌ FAIL | ✅ PASS |
| Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ FAIL | ❌ FAIL | ✅ PASS | ❌ FAIL | ⬜ N/A |
| Episteme (Strassen) | ✅ PASS | ❌ FAIL | ❌ FAIL | ✅ PASS | ❌ FAIL | ❌ FAIL | ✅ PASS | ❌ FAIL | ⬜ N/A |
| Native MPFR Dense Linear Algebra Backend | ❌ FAIL | ✅ PASS | ❌ FAIL | ✅ PASS | ✅ PASS | ✅ PASS | ❌ FAIL | ❌ FAIL | ⬜ N/A |
| Native MPFR Sparse Linear Algebra Backend | ❌ FAIL | ✅ PASS | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL |
| gRPC Remote (localhost:50051) | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL | ❌ FAIL |


### Failure Details

#### Episteme (CARMA)
- **RB:Mul_Identity**: NoSuchElementException: No provider satisfying filters for: LinearAlgebraProvider
- **RB:Inv_Identity**: NoSuchElementException: No provider satisfying filters for: LinearAlgebraProvider
- **RB:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)
- **RB:LogExp**: UnsupportedOperationException: Transcendental 'exp' failed or was unavailable (no fallback allowed)
- **C:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)

#### Episteme CPU (Dense)
- **RB:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)
- **RB:LogExp**: UnsupportedOperationException: Transcendental 'exp' failed or was unavailable (no fallback allowed)
- **C:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)

#### Episteme CPU (Sparse)
- **RB:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)
- **RB:LogExp**: UnsupportedOperationException: Transcendental 'exp' failed or was unavailable (no fallback allowed)
- **C:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)

#### Distributed Linear Algebra Provider (LocalDistributedContext)
- **RB:Add_Comm**: RuntimeException: All 1 providers for LinearAlgebraProvider failed.
- **RB:Mul_Identity**: RuntimeException: All 1 providers for LinearAlgebraProvider failed.
- **RB:Inv_Identity**: RuntimeException: All 1 providers for LinearAlgebraProvider failed.
- **RB:Solve_Verify**: RuntimeException: All 1 providers for LinearAlgebraProvider failed.
- **RB:Sparse_Solve**: RuntimeException: All 1 providers for SparseLinearAlgebraProvider failed.
- **RB:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)
- **RB:LogExp**: RuntimeException: All 1 providers for LinearAlgebraProvider failed.
- **C:Inv_Identity**: RuntimeException: All 1 providers for LinearAlgebraProvider failed.
- **C:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)

#### Episteme (Standard)
- **RB:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)
- **RB:LogExp**: UnsupportedOperationException: Transcendental 'exp' failed or was unavailable (no fallback allowed)
- **C:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)

#### Episteme (Strassen)
- **RB:Mul_Identity**: NoSuchElementException: No provider satisfying filters for: LinearAlgebraProvider
- **RB:Inv_Identity**: NoSuchElementException: No provider satisfying filters for: LinearAlgebraProvider
- **RB:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)
- **RB:LogExp**: UnsupportedOperationException: Transcendental 'exp' failed or was unavailable (no fallback allowed)
- **C:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)

#### Native MPFR Dense Linear Algebra Backend
- **RB:Add_Comm**: UnsupportedOperationException: Native MPFR Dense Linear Algebra Backend does not support Vector add()
- **RB:Inv_Identity**: ClassCastException: class org.episteme.core.mathematics.numbers.real.RealDouble cannot be cast to class org.episteme.core.mathematics.numbers.real.RealBig (org.episteme.core.mathematics.numbers.real.RealDouble and org.episteme.core.mathematics.numbers.real.RealBig are in unnamed module of loader 'app')
- **C:Inv_Identity**: NumberFormatException: Infinite or NaN
- **C:SinCosId**: UnsupportedOperationException: Transcendental 'exp' failed or was unavailable (no fallback allowed)

#### Native MPFR Sparse Linear Algebra Backend
- **RB:Add_Comm**: UnsupportedOperationException: Native MPFR Sparse Linear Algebra Backend does not support Vector add()
- **RB:Inv_Identity**: UnsupportedOperationException: Native MPFR Sparse Linear Algebra Backend does not support inverse()
- **RB:Solve_Verify**: UnsupportedOperationException: Native MPFR Sparse Linear Algebra Backend does not support solve()
- **RB:Sparse_Solve**: UnsupportedOperationException: Native MPFR Sparse Linear Algebra Backend does not support Vector subtract()
- **RB:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)
- **RB:LogExp**: UnsupportedOperationException: Transcendental 'exp' failed or was unavailable (no fallback allowed)
- **C:Inv_Identity**: UnsupportedOperationException: Native MPFR Sparse Linear Algebra Backend does not support inverse()
- **C:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)

#### gRPC Remote (localhost:50051)
- **RB:Add_Comm**: RuntimeException: Remote server is unavailable at localhost:50051. Check if the Episteme server is running.
- **RB:Mul_Identity**: RuntimeException: Remote server is unavailable at localhost:50051. Check if the Episteme server is running.
- **RB:Inv_Identity**: RuntimeException: Remote server is unavailable at localhost:50051. Check if the Episteme server is running.
- **RB:Solve_Verify**: RuntimeException: Remote server is unavailable at localhost:50051. Check if the Episteme server is running.
- **RB:Sparse_Solve**: RuntimeException: Remote server is unavailable at localhost:50051. Check if the Episteme server is running.
- **RB:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)
- **RB:LogExp**: UnsupportedOperationException: Transcendental 'exp' failed or was unavailable (no fallback allowed)
- **C:Inv_Identity**: RuntimeException: Remote server is unavailable at localhost:50051. Check if the Episteme server is running.
- **C:SinCosId**: UnsupportedOperationException: Transcendental 'sin' failed or was unavailable (no fallback allowed)

