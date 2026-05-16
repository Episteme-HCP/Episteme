# Universal Multimodal Performance Audit

## Performance Benchmarking Summary

| Provider | Domain | Status | FAST (SIMD):error | FAST (Object):error | NORMAL (Object):error | EXACT:error | NORMAL (SIMD):error | FAST (SIMD):latency | FAST (Object):latency | NORMAL (SIMD):latency | NORMAL (Object):latency | EXACT:latency | FAST (SIMD):throughput | FAST (Object):throughput | NORMAL (SIMD):throughput | NORMAL (Object):throughput | EXACT:throughput |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| MPIDistributedBackend | Distributed Computing | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| SparkDistributedBackend | Distributed Computing | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| StandardBooleanAlgebraProvider | Boolean Algebra | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| StandardFFTProvider | Fast Fourier Transform | SUCCESS | - | - | - | - | - | 1.70128 | 2.21342 | 2.68451 | 1.65105 | 89.86375 | 587.7927207749459 | 451.7895383614497 | 372.5074594618757 | 605.6751764028952 | 11.127957602481535 |
| MulticoreGraphAlgorithmProvider | Graph Algorithm | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| StandardGraphAlgorithmProvider | Graph Algorithm | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| DenseNeuralProvider | Neural Operations | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| ONNXBackend | Machine Learning (ONNX) | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| ONNXRuntimeBackend | Neural Inference (ONNX) | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| MulticoreFEMProvider | Finite Element Method | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| RungeKuttaODEProvider | ODE Solver | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| MulticoreReduceProvider | reduce | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| MulticoreGeneticAlgorithmProvider | Genetic Algorithm | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| MulticoreBayesianInferenceProvider | Bayesian Inference | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| MulticoreFuzzyProvider | fuzzy logic | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| VariableEliminationProvider | Bayesian Inference | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| MulticoreMonteCarloProvider | Monte Carlo Simulation | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| MulticoreLatticeBoltzmannProvider | Lattice Boltzmann | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| MulticoreNavierStokesProvider | Navier-Stokes | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| MulticoreSPHFluidProvider | SPH Fluid Dynamics | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| MulticoreMolecularDynamicsProvider | Molecular Dynamics | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| DistributedNBodyProvider | N-Body Simulation | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| MulticoreNBodyProvider | N-Body Simulation | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| MulticoreMaxwellProvider | Maxwell Equations | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| MulticoreWaveProvider | Wave Equation | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| AmazonBraketBackend | Quantum Algorithms | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| IBMQBackend | Quantum Algorithms | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| PythonQuantumBackend | Quantum Algorithms | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| QiskitAerBackend | Quantum Algorithms | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| QiskitBackend | Quantum Algorithms | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| Quantum4JBackend | Quantum Algorithms | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| StrangeBackend | Quantum Algorithms | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| MulticoreSCFProvider | Self-Consistent Field | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| NativeFFMBLASBackend | Linear Algebra | SUCCESS | - | - | - | - | - | 4.01782 | 4.84088 | 4.85635 | 7.71391 | 0.63039 | 248.89118974966522 | 206.57401133678172 | 205.91596569440011 | 129.63594338020536 | 1586.3195799425753 |
| NativeMPFRSparseLinearAlgebraBackend | Linear Algebra (Sparse) | SUCCESS | - | - | - | - | - | 21443.06141 | 18245.90306 | 2095.18754 | 2043.69328 | 9.23135 | 0.046635132030804556 | 0.0548068241243851 | 0.47728424349068055 | 0.48931021586566065 | 108.32651778992238 |
| NativeSIMDLinearAlgebraBackend | LinearAlgebra | SUCCESS | - | - | - | - | - | 4.14057 | 6.91125 | 13.02365 | 3.49993 | 0.07516 | 241.5126419792444 | 144.69162597214685 | 76.78339021702826 | 285.720000114288 | 13304.949441192122 |
| NativeND4JTensorBackend | Tensor Operations | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| NativeJBulletBackend | Physics/Collision | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| NativeQuantumBackend | Quantum Computing | SUCCESS | - | - | - | - | - | -1.0 | -1.0 | -1.0 | -1.0 | -1.0 | - | - | - | - | - |
| ColtBackend | Linear Algebra | SUCCESS | - | - | - | - | - | 11.58243 | 14.81417 | 4.53656 | 1.9553 | 16.70685 | 86.33766834766107 | 67.5029380653793 | 220.43134004620242 | 511.4304710274638 | 59.85568793638538 |
| CommonsMathBackend | Linear Algebra | SUCCESS | - | - | - | - | - | 15.61541 | 31.34028 | 2.84321 | 4.52386 | 26.02312 | 64.03930476369176 | 31.907819585530188 | 351.7151388747226 | 221.05016512447335 | 38.42736766383124 |
| EJMLBackend | Linear Algebra | SUCCESS | - | - | - | - | - | 18.53123 | 6.53041 | 2.07957 | 2.53661 | 25.01054 | 53.96295874585767 | 153.12974223670489 | 480.8686411133071 | 394.2269406806722 | 39.983143106866144 |
| EpistemeLinearAlgebraBackend | Linear Algebra (Sparse) | SUCCESS | - | - | - | - | - | 7.84652 | 4.1333 | 0.28984 | 2.14851 | 1.3279 | 127.44503295728552 | 241.93743497931433 | 3450.179409329285 | 465.43883900936 | 753.0687551773476 |
| JBlasBackend | Linear Algebra | SUCCESS | - | - | - | - | - | 9.03997 | 10.5137 | 1.99999 | 1.24172 | 23.406 | 110.61983612777476 | 95.11399412195516 | 500.0025000125001 | 805.3345359662404 | 42.7240878407246 |
| CARMALinearAlgebraProvider | Linear Algebra | SUCCESS | java.util.NoSuchElementException: No provider satisfying filters for: LinearAlgebraProvider | java.util.NoSuchElementException: No provider satisfying filters for: LinearAlgebraProvider | java.util.NoSuchElementException: No provider satisfying filters for: LinearAlgebraProvider | java.util.NoSuchElementException: No provider satisfying filters for: LinearAlgebraProvider | - | - | - | 3.49263 | - | - | - | - | 286.31718790710727 | - | - |
| CPUDenseLinearAlgebraProvider | Linear Algebra | SUCCESS | - | - | - | - | - | 6.02915 | 4.79559 | 0.1349 | 1.22546 | 0.58555 | 165.8608593251122 | 208.5249155995404 | 7412.898443291328 | 816.02010673543 | 1707.7960891469559 |
| CPUSparseLinearAlgebraProvider | Linear Algebra (Sparse) | SUCCESS | - | - | - | - | - | 567.98186 | 414.72764 | 42.29408 | 43.28739 | 1.02337 | 1.760619608520596 | 2.41122101242155 | 23.643970976552747 | 23.10141590888247 | 977.1636846888223 |
| DistributedLinearAlgebraProvider | Linear Algebra (Sparse) | SUCCESS | java.lang.RuntimeException: All 1 providers for LinearAlgebraProvider failed. | java.lang.RuntimeException: All 1 providers for LinearAlgebraProvider failed. | java.lang.RuntimeException: All 1 providers for LinearAlgebraProvider failed. | java.lang.RuntimeException: All 1 providers for LinearAlgebraProvider failed. | java.lang.RuntimeException: All 1 providers for LinearAlgebraProvider failed. | - | - | - | - | - | - | - | - | - | - |
| StandardLinearAlgebraProvider | Linear Algebra | SUCCESS | - | - | - | - | - | 4.74188 | 4.45117 | 0.94308 | 2.1019 | 0.3256 | 210.88682126076577 | 224.6600332047529 | 1060.3554311405182 | 475.76002664256146 | 3071.253071253071 |
| StrassenLinearAlgebraProvider | Linear Algebra | SUCCESS | java.util.NoSuchElementException: No provider satisfying filters for: LinearAlgebraProvider | - | - | java.util.NoSuchElementException: No provider satisfying filters for: LinearAlgebraProvider | - | - | 4.59399 | 3.31315 | 0.43073 | - | - | 217.67570238507267 | 301.8275659115947 | 2321.640006500592 | - |
| NativeCPULinearAlgebraBackend | Linear Algebra | SUCCESS | - | - | - | - | - | 10.42086 | 5.92472 | 1.98925 | 1.84149 | 0.73983 | 95.96136979097695 | 168.7843476147396 | 502.7020233756441 | 543.038517722062 | 1351.6618682670344 |
| NativeFFMBLASBackend | Linear Algebra | SUCCESS | - | - | - | - | - | 8.35123 | 7.12355 | 1.23475 | 0.7315 | 0.4812 | 119.7428402762228 | 140.37944564156916 | 809.8805426199635 | 1367.0539986329459 | 2078.137988362427 |
| NativeMPFRDenseLinearAlgebraBackend | Linear Algebra | SUCCESS | - | - | - | - | - | 2404.20004 | 2120.08825 | 313.24592 | 422.00969 | 6.24382 | 0.41593876689229237 | 0.4716784784784313 | 3.192379967790163 | 2.369613835170467 | 160.15836459090747 |
| NativeMPFRSparseLinearAlgebraBackend | Linear Algebra (Sparse) | SUCCESS | - | - | - | - | - | 16267.35866 | 18457.32246 | 2227.27105 | 2435.1915 | 4.21051 | 0.061472794748105714 | 0.05417903935780293 | 0.4489799299461105 | 0.4106453229653602 | 237.5009203160662 |
| NativeND4JLinearAlgebraBackend | linear algebra | SUCCESS | - | - | - | - | - | 21.83305 | 15.76862 | 9.42799 | 2.72515 | 2.69086 | 45.802121096227964 | 63.41709039852568 | 106.06714686799626 | 366.95227785626474 | 371.6284013289432 |
| NativeSIMDLinearAlgebraBackend | LinearAlgebra | SUCCESS | - | - | - | - | - | 0.8139 | 1.75782 | 0.13974 | 0.55006 | 0.06645 | 1228.6521685710775 | 568.8864616399859 | 7156.147130385 | 1817.9834927098861 | 15048.908954100829 |
| GRPCLinearAlgebraBackend | Linear Algebra (Sparse) | SUCCESS | java.lang.RuntimeException: Remote server is unavailable at localhost:50051. Check if the Episteme server is running. | java.lang.RuntimeException: Remote server is unavailable at localhost:50051. Check if the Episteme server is running. | java.lang.RuntimeException: Remote server is unavailable at localhost:50051. Check if the Episteme server is running. | java.lang.RuntimeException: Remote server is unavailable at localhost:50051. Check if the Episteme server is running. | java.lang.RuntimeException: Remote server is unavailable at localhost:50051. Check if the Episteme server is running. | - | - | - | - | - | - | - | - | - | - |
| MulticoreFFTProvider | Fast Fourier Transform | SUCCESS | - | - | - | - | - | 2.54022 | 0.45207 | 0.1569 | 0.18564 | 34.41371 | 393.6666902866681 | 2212.046806910434 | 6373.4862970044605 | 5386.770092652446 | 29.0581864030353 |
| StandardFFTProvider | Fast Fourier Transform | SUCCESS | - | - | - | - | - | 0.27783 | 0.29904 | 0.12988 | 0.21524 | 57.68938 | 3599.3233272144835 | 3344.0342429106477 | 7699.41484447182 | 4645.976584278015 | 17.334212986861708 |

