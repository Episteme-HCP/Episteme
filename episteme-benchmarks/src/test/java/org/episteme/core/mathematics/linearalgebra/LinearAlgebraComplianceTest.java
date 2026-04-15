package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.algorithm.AlgorithmService;
import org.episteme.core.technical.algorithm.TestingAlgorithmService;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Systematic compliance test for all LinearAlgebraProvider implementations.
 * Generates a markdown report of supported features and correctness.
 */
public class LinearAlgebraComplianceTest {

    private static final double TOL_STRICT = 1e-16;   // Basic Arithmetic, Dot, Norm
    private static final double TOL_STANDARD = 1e-15; // Transpose, Multiply
    private static final double TOL_SOLVER = 1e-13;   // LU, QR, Cholesky, Solve
    private static final double TOL_RELAXED = 1e-10;  // Eigen, SVD, GMRES, BiCGSTAB
    private static final double TOL_FLOAT = 1e-7;     // Legacy Float Suite Tolerance
    private static final int SIZE = 12; 

    private static final String PROJECT_NAME = System.getProperty("org.episteme.project.name", "Episteme");
    private static final String REPORT_PATH = System.getProperty("org.episteme.report.path", "../docs/LINEAR_ALGEBRA_COMPLIANCE_REPORT.md");

    private static class ComplianceResult {
        String providerName;
        String environment;
        Map<String, String> status = new LinkedHashMap<>();
    }

    private LinearAlgebraProvider<Real> realGroundTruth;
    private LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex> complexGroundTruth;

    @Test
    public void generateComplianceReport() {
        List<LinearAlgebraProvider<?>> rawProviders = new ArrayList<>();
        @SuppressWarnings({ "unchecked", "rawtypes" })
        ServiceLoader<LinearAlgebraProvider<?>> loader = ServiceLoader.load((Class) (Object) LinearAlgebraProvider.class);
        Iterator<LinearAlgebraProvider<?>> it = loader.iterator();
        while(true) {
            try {
                if (!it.hasNext()) break;
                LinearAlgebraProvider<?> p = it.next();
                String name = "Unknown";
                try { name = p.getName(); } catch(Throwable t) {}
                System.out.println("[ComplianceTest] Discovered via SPI: " + p.getClass().getName() + " (" + name + ")");
                rawProviders.add(p);
            } catch (Throwable e) {
                System.err.println("Error loading SPI provider:");
                e.printStackTrace();
            }
        }
        try {
            System.out.println("[ComplianceTest] Discovering via BackendDiscovery...");
            for (org.episteme.core.technical.backend.Backend backend : org.episteme.core.technical.backend.BackendDiscovery.getInstance().getProviders()) {
                if (backend == null) continue;
                String bName = "Unknown";
                try { bName = backend.getName(); } catch (Throwable t) {}
                System.out.println("[ComplianceTest] Probing backend: " + bName + " [" + backend.getClass().getName() + "]");
                
                List<org.episteme.core.technical.algorithm.AlgorithmProvider> providersList = null;
                try {
                    providersList = backend.getAlgorithmProviders();
                } catch (Throwable t) {
                    System.err.println("Warning: Backend " + bName + " failed to provide algorithm providers: " + t.getMessage());
                }
                
                if (providersList == null) continue;
                
                for (org.episteme.core.technical.algorithm.AlgorithmProvider ap : providersList) {
                    if (ap == null) continue;
                    if (ap instanceof LinearAlgebraProvider<?> p) {
                        String name = "Unknown";
                        try { name = p.getName(); } catch(Throwable t) {}
                        System.out.println("[ComplianceTest]   Found provider: " + ap.getClass().getName() + " (" + name + ")");
                            rawProviders.add(p);
                    }
                }
            }
        } catch (Throwable e) {
             System.err.println("Warning: BackendDiscovery traversal interrupted: " + e.getMessage());
             e.printStackTrace(); 
        }
                
        List<LinearAlgebraProvider<?>> providers = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        String excludeProp = System.getProperty("org.episteme.exclude.provider", "");
        String includeProp = System.getProperty("org.episteme.include.provider", "");
        
        Set<String> excludes = excludeProp.isEmpty() ? Set.of() : Set.of(excludeProp.split(","));
        Set<String> includes = includeProp.isEmpty() ? Set.of() : Set.of(includeProp.split(","));

        for (LinearAlgebraProvider<?> p : rawProviders) {
            String name = p.getName();
            
            // Check includes first
            if (!includes.isEmpty()) {
                boolean found = false;
                for (String inc : includes) {
                    if (name.contains(inc)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Skipping non-included provider: " + name);
                    continue;
                }
            }

            // Then check excludes
            boolean isExcluded = false;
            for (String exc : excludes) {
                 if (name.contains(exc)) {
                     isExcluded = true;
                     break;
                 }
            }
            if (isExcluded) {
                System.out.println("Skipping excluded provider: " + name);
                continue;
            }

            if (seen.add(name)) {
                providers.add(p);
            }
        }

        // Discover Ground Truths
        for (LinearAlgebraProvider<?> p : providers) {
            String name = p.getName();
            if (p.isCompatible(org.episteme.core.mathematics.sets.Reals.getInstance()) || 
                (p.getName().contains("MPFR") && p.isAvailable())) {
                if (name.contains("EJML") && realGroundTruth == null) {
                    @SuppressWarnings("unchecked")
                    LinearAlgebraProvider<Real> typed = (LinearAlgebraProvider<Real>) p;
                    realGroundTruth = typed;
                    System.out.println("[ComplianceTest] Selected Real Ground Truth: " + name);
                }
            }
            if (p.isCompatible(org.episteme.core.mathematics.sets.Complexes.getInstance())) {
                if ((name.contains("MPFR") || name.contains("BigMath")) && complexGroundTruth == null) {
                    @SuppressWarnings("unchecked")
                    LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex> typed = (LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>) p;
                    complexGroundTruth = typed;
                    System.out.println("[ComplianceTest] Selected Complex Ground Truth: " + name);
                }
            }
        }

        List<ComplianceResult> results = new ArrayList<>();

        for (LinearAlgebraProvider<?> provider : providers) {
            System.out.println("Starting compliance tests for provider: " + provider.getName());
            ComplianceResult res = new ComplianceResult();
            res.providerName = provider.getName();
            
            if (!provider.isAvailable()) {
                res.environment = "DISABLED";
                results.add(res);
                continue;
            }
            res.environment = provider.getEnvironmentInfo();

            // Run Real Suite
            if (provider.isCompatible(org.episteme.core.mathematics.sets.Reals.getInstance())) {
                @SuppressWarnings("unchecked")
                LinearAlgebraProvider<Real> typed = (LinearAlgebraProvider<Real>) provider;
                runSuite(res, typed, rawProviders, "Real", realGroundTruth);
            }
            
            // Run Complex Suite
            if (provider.isCompatible(org.episteme.core.mathematics.sets.Complexes.getInstance())) {
                @SuppressWarnings("unchecked")
                LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex> typed = (LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>) provider;
                runSuite(res, typed, rawProviders, "Complex", complexGroundTruth);
            }
            
            // Run Float Suite (Single Precision) - uses Real suite but with relaxed ground truth comparison
            if (provider.isCompatible(org.episteme.core.mathematics.sets.Reals.getInstance())) {
                 @SuppressWarnings("unchecked")
                 LinearAlgebraProvider<Real> typed = (LinearAlgebraProvider<Real>) provider;
                 runFloatSuite(res, typed, rawProviders, "Float", realGroundTruth);
            }

            results.add(res);
        }
        printMarkdownReport(results);
    }

    private <E> void runSuite(ComplianceResult res, LinearAlgebraProvider<E> provider, List<LinearAlgebraProvider<?>> rawProviders, String typeLabel, LinearAlgebraProvider<E> groundTruth) {
        // Strictly isolate the provider under test, but allow delegation for decorators
        List<AlgorithmProvider> allowed = new ArrayList<>();
        allowed.add(provider);
        if (groundTruth != null) allowed.add(groundTruth);

        if (provider instanceof org.episteme.core.mathematics.linearalgebra.providers.DistributedLinearAlgebraProvider || 
            provider.getName().contains("Remote")) {
            for (LinearAlgebraProvider<?> p : rawProviders) {
                if (p != provider) allowed.add((AlgorithmProvider) p);
            }
        }
        AlgorithmService oldService = AlgorithmManager.getService();
        AlgorithmManager.setService(new TestingAlgorithmService(allowed));
        
        try {
            String suffix = " (" + typeLabel + ")";
            boolean isComplex = typeLabel.equals("Complex");
            @SuppressWarnings("unchecked")
            Ring<E> ring = (Ring<E>) (isComplex ? org.episteme.core.mathematics.sets.Complexes.getInstance() : org.episteme.core.mathematics.sets.Reals.getInstance());

            testOperation(res, "Add" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(SIZE, SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> b = randomMatrix(SIZE, SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> result = provider.add(a, b);
                if (groundTruth != null) verifyMatrix(groundTruth.add(a, b), result, TOL_STRICT, ring);
                else verifyMatrix(a.add(b), result, TOL_STRICT, ring);
            });

            testOperation(res, "Sub" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(SIZE, SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> b = randomMatrix(SIZE, SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> result = provider.subtract(a, b);
                if (groundTruth != null) verifyMatrix(groundTruth.subtract(a, b), result, TOL_STRICT, ring);
                else verifyMatrix(a.subtract(b), result, TOL_STRICT, ring);
            });

            testOperation(res, "Scale" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(SIZE, SIZE, rand, ring);
                @SuppressWarnings("unchecked")
                E s = isComplex ? (E)org.episteme.core.mathematics.numbers.complex.Complex.of(2.5, 0.5) : (E)Real.of(2.5);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> result = provider.scale(s, a);
                if (groundTruth != null) verifyMatrix(groundTruth.scale(s, a), result, TOL_STRICT, ring);
                else verifyMatrix(a.scale(s), result, TOL_STRICT, ring);
            });

            testOperation(res, "Transpose" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(SIZE, SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> result = provider.transpose(a);
                verifyTranspose(a, result, TOL_STANDARD, ring);
            });

            testOperation(res, "Multiply" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(SIZE, SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> b = randomMatrix(SIZE, SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> result = provider.multiply(a, b);
                verifyMultiply(a, b, result, TOL_STANDARD, ring);
            });

            testOperation(res, "Inverse" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomInvertibleMatrix(SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> result = provider.inverse(a);
                verifyInverse(a, result, TOL_SOLVER, ring);
            });

            testOperation(res, "Solve (Hilbert)" + suffix, () -> {
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = hilbertMatrix(8, ring); 
                org.episteme.core.mathematics.linearalgebra.Vector<E> b = randomVector(8, new Random(42), ring);
                org.episteme.core.mathematics.linearalgebra.Vector<E> x = provider.solve(a, b);
                org.episteme.core.mathematics.linearalgebra.Vector<E> ax = a.multiply(x);
                double normDiff = 0, normB = 0;
                for (int i = 0; i < b.dimension(); i++) {
                    double dv = absValueDouble(ring.subtract(ax.get(i), b.get(i)), ring);
                    normDiff += dv * dv;
                    double db = absValueDouble(b.get(i), ring);
                    normB += db * db;
                }
                double residual = Math.sqrt(normDiff) / (Math.sqrt(normB) + 1e-18);
                assertTrue(residual < 1e-2, "Hilbert solve residual too high: " + residual);
            });

            testOperation(res, "LU (Identity)" + suffix, () -> {
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = identityMatrix(SIZE, ring);
                LUResult<E> result = provider.lu(a);
                verifyLU(a, result, ring);
            });

            testOperation(res, "QR (Identity)" + suffix, () -> {
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = identityMatrix(SIZE, ring);
                QRResult<E> result = provider.qr(a);
                verifyQR(a, result, TOL_STRICT, ring);
            });

            testOperation(res, "Determinant" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(8, 8, rand, ring);
                E result = provider.determinant(a);
                if (groundTruth != null) assertScalarEquals(groundTruth.determinant(a), result, TOL_SOLVER, ring);
            });

            testOperation(res, "Solve" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomInvertibleMatrix(SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Vector<E> b = randomVector(SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Vector<E> x = provider.solve(a, b);
                org.episteme.core.mathematics.linearalgebra.Vector<E> ax = a.multiply(x);
                double normDiff = 0, normB = 0;
                for (int i = 0; i < b.dimension(); i++) {
                    double dv = absValueDouble(ring.subtract(ax.get(i), b.get(i)), ring);
                    normDiff += dv * dv;
                    double db = absValueDouble(b.get(i), ring);
                    normB += db * db;
                }
                double residual = (normB > 1e-18) ? Math.sqrt(normDiff)/Math.sqrt(normB) : Math.sqrt(normDiff);
                assertTrue(residual < TOL_SOLVER, "Solver residual too high: " + residual);
            });

            testOperation(res, "LU" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(SIZE, SIZE, rand, ring);
                LUResult<E> result = provider.lu(a);
                verifyLU(a, result, ring);
            });

            testOperation(res, "QR" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(SIZE, SIZE, rand, ring);
                QRResult<E> result = provider.qr(a);
                verifyQR(a, result, TOL_SOLVER, ring);
            });

            testOperation(res, "SVD" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(20, 15, rand, ring);
                SVDResult<E> result = provider.svd(a);
                verifySVD(a, result, ring);
            });

            testOperation(res, "Cholesky" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomSPDMatrix(SIZE, rand, ring);
                CholeskyResult<E> result = provider.cholesky(a);
                verifyCholesky(a, result, TOL_SOLVER, ring);
            });

            testOperation(res, "Eigen" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomSPDMatrix(SIZE, rand, ring);
                EigenResult<E> result = provider.eigen(a);
                verifyEigen(a, result, TOL_RELAXED, ring);
            });

            testOperation(res, "Dot" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Vector<E> a = randomVector(SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Vector<E> bArr = randomVector(SIZE, rand, ring);
                E result = provider.dot(a, bArr);
                E expected = ring.zero();
                for(int i=0; i<SIZE; i++) expected = ring.add(expected, ring.multiply(a.get(i), bArr.get(i)));
                assertScalarEquals(expected, result, TOL_STRICT, ring);
            });

            testOperation(res, "Norm" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Vector<E> a = randomVector(SIZE, rand, ring);
                E normVal = provider.norm(a);
                double sumSq = 0;
                for(int i=0; i<SIZE; i++) {
                    E val = a.get(i);
                    double d = absValueDouble(val, ring);
                    sumSq += d*d;
                }
                assertRelativeEquals(Math.sqrt(sumSq), absValueDouble(normVal, ring), TOL_STRICT);
            });

            testOperation(res, "Transpose (Rect)" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(25, 10, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> result = provider.transpose(a);
                verifyTranspose(a, result, TOL_STANDARD, ring);
            });

            testOperation(res, "Multiply (Rect)" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(20, 15, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> b = randomMatrix(15, 25, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> result = provider.multiply(a, b);
                verifyMultiply(a, b, result, TOL_STANDARD, ring);
            });

            testOperation(res, "Solve (Rect)" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(30, 10, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Vector<E> b = randomVector(30, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Vector<E> x = provider.solve(a, b);
                org.episteme.core.mathematics.linearalgebra.Vector<E> lhs = provider.transpose(a).multiply(a.multiply(x));
                org.episteme.core.mathematics.linearalgebra.Vector<E> rhs = provider.transpose(a).multiply(b);
                double normDiff = 0, normRHS = 0;
                for (int i = 0; i < rhs.dimension(); i++) {
                    double dv = absValueDouble(ring.subtract(lhs.get(i), rhs.get(i)), ring);
                    normDiff += dv * dv;
                    double dr = absValueDouble(rhs.get(i), ring);
                    normRHS += dr * dr;
                }
                double residual = (normRHS > 1e-18) ? Math.sqrt(normDiff)/Math.sqrt(normRHS) : Math.sqrt(normDiff);
                assertTrue(residual < TOL_RELAXED, "Least squares residual too high: " + residual);
            });

            if (provider instanceof org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider<E> sparseProvider) {
                @SuppressWarnings("unchecked")
                E eps = isComplex ? (E)org.episteme.core.mathematics.numbers.complex.Complex.of(1e-8, 0) : (E)Real.of(1e-8);
                @SuppressWarnings("unchecked")
                E twenty = isComplex ? (E)org.episteme.core.mathematics.numbers.complex.Complex.of(20, 0) : (E)Real.of(20);
                testOperation(res, "BiCGSTAB" + suffix, () -> {
                    Random rand = new Random(42);
                    int n = 30;
                    org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(n, n, rand, ring);
                    @SuppressWarnings("unchecked")
                    E[][] diagData = (E[][]) new Object[n][n];
                    for(int i=0; i<n; i++) for(int j=0; j<n; j++) diagData[i][j] = (i==j) ? twenty : ring.zero();
                    a = a.add(org.episteme.core.mathematics.linearalgebra.Matrix.of(diagData, ring));

                    org.episteme.core.mathematics.linearalgebra.Vector<E> b = randomVector(n, rand, ring);
                    org.episteme.core.mathematics.linearalgebra.Vector<E> x0 = org.episteme.core.mathematics.linearalgebra.Vector.of(new ArrayList<>(Collections.nCopies(n, ring.zero())), ring);
                    org.episteme.core.mathematics.linearalgebra.Vector<E> x = sparseProvider.bicgstab(a, b, x0, eps, 2000);
                    org.episteme.core.mathematics.linearalgebra.Vector<E> ax = provider.multiply(a, x);
                    double normDiff = 0, normB = 0;
                    for (int i = 0; i < b.dimension(); i++) {
                        double dv = absValueDouble(ring.subtract(b.get(i), ax.get(i)), ring);
                        normDiff += dv * dv;
                        double db = absValueDouble(b.get(i), ring);
                        normB += db * db;
                    }
                    double residual = Math.sqrt(normDiff) / (Math.sqrt(normB) + 1e-18);
                    assertTrue(residual < TOL_RELAXED, "BiCGSTAB residual too high: " + residual);
                });

                testOperation(res, "GMRES" + suffix, () -> {
                    Random rand = new Random(42);
                    int n = 30;
                    org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(n, n, rand, ring);
                    @SuppressWarnings("unchecked")
                    E[][] diagData = (E[][]) new Object[n][n];
                    for(int i=0; i<n; i++) for(int j=0; j<n; j++) diagData[i][j] = (i==j) ? twenty : ring.zero();
                    a = a.add(org.episteme.core.mathematics.linearalgebra.Matrix.of(diagData, ring));

                    org.episteme.core.mathematics.linearalgebra.Vector<E> b = randomVector(n, rand, ring);
                    org.episteme.core.mathematics.linearalgebra.Vector<E> x0 = org.episteme.core.mathematics.linearalgebra.Vector.of(new ArrayList<>(Collections.nCopies(n, ring.zero())), ring);
                    org.episteme.core.mathematics.linearalgebra.Vector<E> x = sparseProvider.gmres(a, b, x0, eps, 2000, 30);
                    org.episteme.core.mathematics.linearalgebra.Vector<E> ax = provider.multiply(a, x);
                    double normDiff = 0, normB = 0;
                    for (int i = 0; i < b.dimension(); i++) {
                        double dv = absValueDouble(ring.subtract(b.get(i), ax.get(i)), ring);
                        normDiff += dv * dv;
                        double db = absValueDouble(b.get(i), ring);
                        normB += db * db;
                    }
                    double residual = Math.sqrt(normDiff) / (Math.sqrt(normB) + 1e-18);
                    assertTrue(residual < TOL_RELAXED, "GMRES residual too high: " + residual);
                });

                testOperation(res, "ConjGrad" + suffix, () -> {
                    Random rand = new Random(42);
                    int n = 30;
                    org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomSPDMatrix(n, rand, ring);
                    org.episteme.core.mathematics.linearalgebra.Vector<E> b = randomVector(n, rand, ring);
                    org.episteme.core.mathematics.linearalgebra.Vector<E> x0 = org.episteme.core.mathematics.linearalgebra.Vector.of(new ArrayList<>(Collections.nCopies(n, ring.zero())), ring);
                    org.episteme.core.mathematics.linearalgebra.Vector<E> x = sparseProvider.conjugateGradient(a, b, x0, eps, 2000);
                    org.episteme.core.mathematics.linearalgebra.Vector<E> ax = provider.multiply(a, x);
                    double normDiff = 0, normB = 0;
                    for (int i = 0; i < b.dimension(); i++) {
                        double dv = absValueDouble(ring.subtract(b.get(i), ax.get(i)), ring);
                        normDiff += dv * dv;
                        double db = absValueDouble(b.get(i), ring);
                        normB += db * db;
                    }
                    double residual = Math.sqrt(normDiff) / (Math.sqrt(normB) + 1e-18);
                    assertTrue(residual < TOL_SOLVER, "ConjGrad residual too high: " + residual);
                });
            }

        } finally {
            AlgorithmManager.setService(oldService);
        }
    }

    private <E> void runFloatSuite(ComplianceResult res, LinearAlgebraProvider<E> provider, List<LinearAlgebraProvider<?>> rawProviders, String typeLabel, LinearAlgebraProvider<E> groundTruth) {
        AlgorithmService oldService = AlgorithmManager.getService();
        List<AlgorithmProvider> allowed = new ArrayList<>();
        allowed.add(provider);
        if (groundTruth != null) allowed.add(groundTruth);
        AlgorithmManager.setService(new TestingAlgorithmService(allowed));

        try {
            String suffix = " (" + typeLabel + ")";
            @SuppressWarnings("unchecked")
            Ring<E> ring = (Ring<E>) (Object) org.episteme.core.mathematics.sets.Reals.getInstance();

            testOperation(res, "Add" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(SIZE, SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> b = randomMatrix(SIZE, SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> result = provider.add(a, b);
                if (groundTruth != null) verifyMatrix(groundTruth.add(a, b), result, TOL_FLOAT, ring);
            });

            testOperation(res, "Multiply" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(SIZE, SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> b = randomMatrix(SIZE, SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> result = provider.multiply(a, b);
                if (groundTruth != null) verifyMatrix(groundTruth.multiply(a, b), result, TOL_FLOAT, ring);
            });

            testOperation(res, "Solve" + suffix, () -> {
                Random rand = new Random(42);
                org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomInvertibleMatrix(SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Vector<E> b = randomVector(SIZE, rand, ring);
                org.episteme.core.mathematics.linearalgebra.Vector<E> x = provider.solve(a, b);
                if (groundTruth != null) verifyVector(groundTruth.solve(a, b), x, TOL_FLOAT, ring);
            });

        } finally {
            AlgorithmManager.setService(oldService);
        }
    }

    private <E> org.episteme.core.mathematics.linearalgebra.Matrix<E> randomMatrix(int rows, int cols, Random rand, Ring<E> ring) {
        @SuppressWarnings("unchecked")
        E[][] data = (E[][]) new Object[rows][cols];
        boolean isComplexInside = ring instanceof org.episteme.core.mathematics.sets.Complexes;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isComplexInside) {
                    @SuppressWarnings("unchecked")
                    E val = (E) org.episteme.core.mathematics.numbers.complex.Complex.of(rand.nextDouble()*2-1, rand.nextDouble()*2-1);
                    data[i][j] = val;
                } else {
                    @SuppressWarnings("unchecked")
                    E val = (E) Real.of(rand.nextDouble()*2-1);
                    data[i][j] = val;
                }
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(data, ring);
    }

    private <E> org.episteme.core.mathematics.linearalgebra.Matrix<E> randomSPDMatrix(int n, Random rand, Ring<E> ring) {
        org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(n, n, rand, ring);
        org.episteme.core.mathematics.linearalgebra.Matrix<E> result = a.multiply(a.transpose());
        boolean isComplexInside = ring instanceof org.episteme.core.mathematics.sets.Complexes;
        @SuppressWarnings("unchecked")
        E ten = isComplexInside ? (E)org.episteme.core.mathematics.numbers.complex.Complex.of(10, 0) : (E)Real.of(10);
        @SuppressWarnings("unchecked")
        E[][] diagData = (E[][]) new Object[n][n];
        for(int r=0; r<n; r++) for(int c=0; c<n; c++) diagData[r][c] = (r==c) ? ten : ring.zero();
        return result.add(org.episteme.core.mathematics.linearalgebra.Matrix.of(diagData, ring));
    }

    private <E> org.episteme.core.mathematics.linearalgebra.Matrix<E> hilbertMatrix(int n, Ring<E> ring) {
        @SuppressWarnings("unchecked")
        E[][] data = (E[][]) new Object[n][n];
        boolean isComplexInside = ring instanceof org.episteme.core.mathematics.sets.Complexes;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double val = 1.0 / (i + j + 1);
                data[i][j] = isComplexInside ? (E)org.episteme.core.mathematics.numbers.complex.Complex.of(val, 0) : (E)Real.of(val);
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(data, ring);
    }

    private <E> org.episteme.core.mathematics.linearalgebra.Matrix<E> identityMatrix(int n, Ring<E> ring) {
        return org.episteme.core.mathematics.linearalgebra.Matrix.identity(n, ring);
    }

    private <E> org.episteme.core.mathematics.linearalgebra.Matrix<E> randomInvertibleMatrix(int n, Random rand, Ring<E> ring) {
        org.episteme.core.mathematics.linearalgebra.Matrix<E> a = randomMatrix(n, n, rand, ring);
        boolean isComplexInside = ring instanceof org.episteme.core.mathematics.sets.Complexes;
        @SuppressWarnings("unchecked")
        E ten = isComplexInside ? (E)org.episteme.core.mathematics.numbers.complex.Complex.of(10, 0) : (E)Real.of(10);
        @SuppressWarnings("unchecked")
        E[][] diagData = (E[][]) new Object[n][n];
        for(int r=0; r<n; r++) for(int c=0; c<n; c++) diagData[r][c] = (r==c) ? ten : ring.zero();
        return a.add(org.episteme.core.mathematics.linearalgebra.Matrix.of(diagData, ring));
    }

    private <E> org.episteme.core.mathematics.linearalgebra.Vector<E> randomVector(int n, Random rand, Ring<E> ring) {
         @SuppressWarnings("unchecked")
         E[] data = (E[]) new Object[n];
         boolean isComplexInside = ring instanceof org.episteme.core.mathematics.sets.Complexes;
         for (int i = 0; i < n; i++) {
             if (isComplexInside) {
                 @SuppressWarnings("unchecked")
                 E val = (E) org.episteme.core.mathematics.numbers.complex.Complex.of(rand.nextDouble()*2-1, rand.nextDouble()*2-1);
                 data[i] = val;
             } else {
                 @SuppressWarnings("unchecked")
                 E val = (E) Real.of(rand.nextDouble()*2-1);
                 data[i] = val;
             }
         }
         return org.episteme.core.mathematics.linearalgebra.Vector.of(Arrays.asList(data), ring);
    }

    private <E> void verifyTranspose(org.episteme.core.mathematics.linearalgebra.Matrix<E> a, org.episteme.core.mathematics.linearalgebra.Matrix<E> result, double tol, Ring<E> ring) {
        assertEquals(a.rows(), result.cols());
        assertEquals(a.cols(), result.rows());
        for(int i=0; i<a.rows(); i++) for(int j=0; j<a.cols(); j++) assertScalarEquals(a.get(i, j), result.get(j, i), tol, ring);
    }

    private <E> void verifyMultiply(org.episteme.core.mathematics.linearalgebra.Matrix<E> a, org.episteme.core.mathematics.linearalgebra.Matrix<E> b, org.episteme.core.mathematics.linearalgebra.Matrix<E> result, double tol, Ring<E> ring) {
        assertEquals(a.rows(), result.rows());
        assertEquals(b.cols(), result.cols());
        for(int i=0; i<a.rows(); i++) {
            for(int j=0; j<b.cols(); j++) {
                E sum = ring.zero();
                for(int k=0; k<a.cols(); k++) sum = ring.add(sum, ring.multiply(a.get(i, k), b.get(k, j)));
                assertScalarEquals(sum, result.get(i, j), tol, ring);
            }
        }
    }

    private <E> void verifyInverse(org.episteme.core.mathematics.linearalgebra.Matrix<E> a, org.episteme.core.mathematics.linearalgebra.Matrix<E> inv, double tol, Ring<E> ring) {
        org.episteme.core.mathematics.linearalgebra.Matrix<E> id = inv.multiply(a);
        org.episteme.core.mathematics.linearalgebra.Matrix<E> expectedId = org.episteme.core.mathematics.linearalgebra.Matrix.identity(a.rows(), ring);
        assertResidualNorm(expectedId, id, tol, ring);
    }

    private <E> void verifyLU(org.episteme.core.mathematics.linearalgebra.Matrix<E> a, LUResult<E> lu, Ring<E> ring) {
        org.episteme.core.mathematics.linearalgebra.Matrix<E> product = lu.L().multiply(lu.U());
        int n = a.rows();
        @SuppressWarnings("unchecked")
        E[][] permutedData = (E[][]) new Object[n][a.cols()];
        org.episteme.core.mathematics.linearalgebra.Vector<E> P = lu.P();
        for (int i = 0; i < n; i++) {
            int pivot = scalarToInt(P.get(i), ring);
            if (pivot < 0 || pivot >= n) pivot = i;
            for (int j = 0; j < a.cols(); j++) permutedData[i][j] = a.get(pivot, j);
        }
        org.episteme.core.mathematics.linearalgebra.Matrix<E> PA = org.episteme.core.mathematics.linearalgebra.Matrix.of(permutedData, ring);
        assertResidualNorm(PA, product, TOL_SOLVER, ring);
    }

    private <E> int scalarToInt(E element, Ring<E> ring) {
        return (int) Math.round(absValueDouble(element, ring));
    }

    private <E> void verifyQR(org.episteme.core.mathematics.linearalgebra.Matrix<E> a, QRResult<E> res, double tol, Ring<E> ring) {
        org.episteme.core.mathematics.linearalgebra.Matrix<E> recon = res.Q().multiply(res.R());
        assertResidualNorm(a, recon, tol, ring);
    }

    private <E> void verifySVD(org.episteme.core.mathematics.linearalgebra.Matrix<E> a, SVDResult<E> res, Ring<E> ring) {
        int k = res.S().dimension();
        @SuppressWarnings("unchecked")
        E[][] sData = (E[][]) new Object[res.U().cols()][res.V().cols()];
        for(int i=0; i<sData.length; i++) {
            for(int j=0; j<sData[0].length; j++) {
                sData[i][j] = (i==j && i<k) ? (E)res.S().get(i) : ring.zero();
            }
        }
        org.episteme.core.mathematics.linearalgebra.Matrix<E> S = new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(sData, ring);
        
        org.episteme.core.mathematics.linearalgebra.Matrix<E> VT = res.V().transpose();
        if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
            @SuppressWarnings("unchecked")
            E[][] vtData = (E[][]) new Object[VT.rows()][VT.cols()];
            for(int i=0; i<VT.rows(); i++) {
                for(int j=0; j<VT.cols(); j++) {
                    @SuppressWarnings("unchecked")
                    E conj = (E) ((org.episteme.core.mathematics.numbers.complex.Complex)VT.get(i, j)).conjugate();
                    vtData[i][j] = conj;
                }
            }
            VT = new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(vtData, ring);
        }
        
        org.episteme.core.mathematics.linearalgebra.Matrix<E> recon = res.U().multiply(S).multiply(VT);
        assertResidualNorm(a, recon, TOL_RELAXED, ring);
    }

    private <E> void verifyMatrix(org.episteme.core.mathematics.linearalgebra.Matrix<E> expected, org.episteme.core.mathematics.linearalgebra.Matrix<E> actual, double tol, Ring<E> ring) {
        assertEquals(expected.rows(), actual.rows());
        assertEquals(expected.cols(), actual.cols());
        for(int i=0; i<expected.rows(); i++) for(int j=0; j<expected.cols(); j++) assertScalarEquals(expected.get(i, j), actual.get(i, j), tol, ring);
    }

    private <E> void verifyVector(org.episteme.core.mathematics.linearalgebra.Vector<E> expected, org.episteme.core.mathematics.linearalgebra.Vector<E> actual, double tol, Ring<E> ring) {
        assertEquals(expected.dimension(), actual.dimension());
        for(int i=0; i<expected.dimension(); i++) assertScalarEquals(expected.get(i), actual.get(i), tol, ring);
    }

    private <E> void assertScalarEquals(E expected, E actual, double tol, Ring<E> ring) {
        assertRelativeEquals(absValueDouble(expected, ring), absValueDouble(actual, ring), tol);
    }

    private <E> void verifyCholesky(org.episteme.core.mathematics.linearalgebra.Matrix<E> a, CholeskyResult<E> res, double tol, Ring<E> ring) {
        org.episteme.core.mathematics.linearalgebra.Matrix<E> recon = res.L().multiply(res.L().transpose());
        if (ring instanceof org.episteme.core.mathematics.sets.Complexes) {
             org.episteme.core.mathematics.linearalgebra.Matrix<E> LH = res.L().transpose();
             @SuppressWarnings("unchecked")
             E[][] lhData = (E[][]) new Object[LH.rows()][LH.cols()];
             for(int i=0; i<LH.rows(); i++) {
                 for(int j=0; j<LH.cols(); j++) {
                     @SuppressWarnings("unchecked")
                     E conj = (E) ((org.episteme.core.mathematics.numbers.complex.Complex)LH.get(i, j)).conjugate();
                     lhData[i][j] = conj;
                 }
             }
             LH = new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(lhData, ring);
             recon = res.L().multiply(LH);
        }
        assertResidualNorm(a, recon, tol, ring);
    }

    private <E> void verifyEigen(org.episteme.core.mathematics.linearalgebra.Matrix<E> a, EigenResult<E> res, double tol, Ring<E> ring) {
        int n = res.D().dimension();
        @SuppressWarnings("unchecked")
        E[][] dData = (E[][]) new Object[n][n];
        for(int i=0; i<n; i++) for(int j=0; j<n; j++) dData[i][j] = (i==j) ? (E)res.D().get(i) : ring.zero();
        org.episteme.core.mathematics.linearalgebra.Matrix<E> D = new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(dData, ring);
        
        org.episteme.core.mathematics.linearalgebra.Matrix<E> AV = a.multiply(res.V());
        org.episteme.core.mathematics.linearalgebra.Matrix<E> VD = res.V().multiply(D);
        assertResidualNorm(AV, VD, tol, ring); 
    }

    private <E> void assertResidualNorm(org.episteme.core.mathematics.linearalgebra.Matrix<E> original, org.episteme.core.mathematics.linearalgebra.Matrix<E> recon, double tol, Ring<E> ring) {
        double normDiff = 0, normOrig = 0;
        for (int i = 0; i < original.rows(); i++) {
            for (int j = 0; j < original.cols(); j++) {
                double dOrig = absValueDouble(original.get(i, j), ring);
                double diff = absValueDouble(ring.subtract(original.get(i, j), recon.get(i, j)), ring);
                normDiff += diff * diff;
                normOrig += dOrig * dOrig;
            }
        }
        double residual = (normOrig > 1e-18) ? Math.sqrt(normDiff) / Math.sqrt(normOrig) : Math.sqrt(normDiff);
        assertTrue(residual < tol, "Residual norm failure: " + residual + " (Tol: " + tol + ")");
    }

    private void assertRelativeEquals(double expected, double actual, double tol) {
        if (Double.isNaN(expected) && Double.isNaN(actual)) return;
        double diff = Math.abs(expected - actual);
        double relDiff = diff / (Math.abs(expected) + 1e-18);
        assertTrue(relDiff < tol, "Expected: " + expected + ", Actual: " + actual + " (RelDiff: " + relDiff + ", Tol: " + tol + ")");
    }

    private void testOperation(ComplianceResult res, String opName, Runnable test) {
        try {
            test.run();
            res.status.put(opName, "✅ PASS");
        } catch (Throwable e) {
            Throwable cause = e;
            if (e instanceof org.episteme.core.technical.algorithm.AlgorithmException && e.getCause() != null) cause = e.getCause();
            if (cause instanceof NoSuchElementException || cause instanceof UnsupportedOperationException) {
                res.status.put(opName, "❌ N/A (" + cause.getMessage() + ")");
                return;
            }
            System.err.println("Test failed for operation " + opName + ":");
            cause.printStackTrace();
            String label = cause.getClass().getSimpleName();
            String msg = cause.getMessage();
            if (msg != null && !msg.isBlank()) {
                String cleanMsg = msg.replace("\n", " ").replace("|", "/");
                if (cleanMsg.length() > 40) cleanMsg = cleanMsg.substring(0, 37) + "...";
                label += ": " + cleanMsg;
            }
            res.status.put(opName, "⚠️ FAIL (" + label + ")");
        }
    }

    private void printMarkdownReport(List<ComplianceResult> results) {
        if (results.isEmpty()) return;
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(PROJECT_NAME).append(" Linear Algebra Provider Compliance Report\n\n");
        Set<String> allOps = new LinkedHashSet<>();
        for (ComplianceResult res : results) allOps.addAll(res.status.keySet());
        sb.append("| Provider | Environment |");
        for (String op : allOps) sb.append(" ").append(op).append(" |");
        sb.append("\n| --- | --- |").append(" --- |".repeat(allOps.size())).append("\n");
        for (ComplianceResult res : results) {
            sb.append("| ").append(res.providerName).append(" | ").append(res.environment).append(" |");
            for (String op : allOps) sb.append(" ").append(res.status.getOrDefault(op, "❌ N/A")).append(" |");
            sb.append("\n");
        }
        sb.append("\n*Generated by LinearAlgebraComplianceTest on ").append(new Date()).append("*\n");
        String report = sb.toString();
        System.out.println(report);
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(REPORT_PATH);
            if (path.getParent() != null) java.nio.file.Files.createDirectories(path.getParent());
            java.nio.file.Files.writeString(path, report);
        } catch (IOException e) { e.printStackTrace(); }
        AlgorithmManager.getService().shutdown();
    }

    private <E> double absValueDouble(E element, Ring<E> ring) {
        if (element instanceof Real) return ((Real) element).doubleValue();
        if (element instanceof org.episteme.core.mathematics.numbers.complex.Complex) return ((org.episteme.core.mathematics.numbers.complex.Complex) element).abs().doubleValue();
        if (element instanceof Double) return (Double) element;
        if (element instanceof Number) return ((Number) element).doubleValue();
        try {
            return Double.parseDouble(element.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }
}
