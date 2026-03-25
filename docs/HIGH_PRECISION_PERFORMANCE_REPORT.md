# Episteme High-Precision Performance Audit

> Generated on Wed Mar 25 14:29:32 CET 2026

## Methodology

- Matrix size for Basic Arithmetic: 8x8
- Matrix size for Decompositions: 4x4
- Average over 1 iterations (post warmup)

| Provider | RB:Add | RB:Mul | RB:Det | RB:Inv | RB:Solve | RB:LU | RB:QR | RB:Chol | RB:SVD | RB:Eigen | RB:Exp | RB:Sin | RB:Cos | RB:Tan | RB:Log | RB:Sqrt | RB:Asin | C:Add | C:Mul | C:Exp | RB:BiCGSTAB |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Episteme (CARMA) | 3,34 ms | 225,89 ms | 24,03 ms | 55,63 ms | 32,74 ms | 4,43 ms | 104,05 ms | 53,10 ms | 2,61 s | 98,04 ms | 81,97 ms | 50,45 ms | 12,89 ms | 38,61 ms | 26,90 ms | 987,80 µs | 77,64 ms | 653,20 µs | 24,11 ms | 77,16 ms | - |
| Episteme CPU (Dense) | 382,80 µs | 4,01 ms | 3,19 ms | 6,96 ms | 2,63 ms | 532,50 µs | 17,06 ms | 40,96 ms | 2,88 s | 186,08 ms | 73,12 ms | 43,34 ms | 7,19 ms | 5,28 ms | 15,69 ms | 563,40 µs | 35,09 ms | 505,40 µs | 22,09 ms | 65,80 ms | - |
| Episteme CPU (Sparse) | 18,71 ms | 8,38 ms | 552,00 µs | 5,54 ms | 41,52 ms | 601,30 µs | 25,29 ms | 70,82 ms | 103,34 ms | 49,28 ms | 4,85 ms | 4,82 ms | 2,70 ms | 3,97 ms | 30,62 ms | 1,21 ms | 18,50 ms | 10,19 ms | 17,96 ms | 84,20 ms | 20,39 ms |
| Distributed Linear Algebra Provider (LocalDistributedContext) | 2,07 ms | 62,46 ms | N/A | N/A | N/A | N/A | N/A | N/A | N/A | N/A | 3,37 ms | 13,44 ms | 4,07 ms | 4,82 ms | 20,31 ms | 6,35 ms | 9,13 ms | 795,00 µs | 64,65 ms | 30,15 ms | N/A |
| Episteme (Standard) | 119,50 µs | 2,86 ms | 524,60 µs | 7,26 ms | 1,64 ms | 251,20 µs | 10,00 ms | 36,50 ms | 1,76 s | 3,29 s | 2,41 ms | 2,14 ms | 4,52 ms | 2,27 ms | 4,89 ms | 382,20 µs | 4,81 ms | 124,40 µs | 10,47 ms | 17,31 ms | - |
| Episteme (Strassen) | 110,90 µs | 10,33 ms | 507,80 µs | 4,51 ms | 1,56 ms | 609,30 µs | 27,02 ms | 11,14 ms | 47,18 ms | 24,33 ms | 8,85 ms | 5,68 ms | 974,60 µs | 5,67 ms | 11,42 ms | 727,30 µs | 5,57 ms | 116,90 µs | 9,97 ms | 16,04 ms | - |
| Native MPFR Dense Linear Algebra Backend | 5,90 ms | 10,58 ms | 8,21 ms | 1,38 ms | 2,75 ms | N/A | 13,01 ms | 23,44 ms | 1,74 s | 2,78 s | 5,03 ms | 3,79 ms | 2,51 ms | 11,52 ms | 16,79 ms | 1,74 ms | 7,46 ms | 4,64 ms | 16,92 ms | 28,01 ms | - |
| Native MPFR Sparse Linear Algebra Backend | 483,80 µs | 7,52 ms | N/A | N/A | N/A | N/A | N/A | N/A | N/A | N/A | 4,63 ms | 16,21 ms | 1,95 ms | 6,75 ms | 10,73 ms | 347,30 µs | 18,07 ms | 1,36 ms | 27,22 ms | 32,47 ms | N/A |
| gRPC Remote (localhost:50051) | ERR | ERR | ERR | ERR | ERR | ERR | ERR | ERR | ERR | ERR | 3,99 ms | 4,67 ms | 2,40 ms | 6,69 ms | 16,60 ms | 5,94 ms | 8,31 ms | ERR | ERR | 17,14 ms | ERR |
