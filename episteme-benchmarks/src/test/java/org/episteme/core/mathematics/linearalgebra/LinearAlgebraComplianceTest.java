package org.episteme.core.mathematics.linearalgebra;

import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.algorithm.AlgorithmService;
import org.episteme.core.technical.algorithm.TestingAlgorithmService;

/**
 * Systematic compliance test for all LinearAlgebraProvider implementations.
 * Generates a markdown report of supported features and correctness.
 */
public class LinearAlgebraComplianceTest {

    private static final double TOLERANCE = 1e-7;
    private static final int SIZE = 12; // Reduced for numerical stability in Eigen with random matrices

    private static final String PROJECT_NAME = System.getProperty("org.episteme.project.name", "Episteme");
    private static final String REPORT_PATH = System.getProperty("org.episteme.report.path", "../docs/LINEAR_ALGEBRA_COMPLIANCE_REPORT.md");

    private static class ComplianceResult {
        String providerName;
        String environment;
        Map<String, String> status = new LinkedHashMap<>();
    }

    @Test
    public void generateComplianceReport() {
        List<LinearAlgebraProvider<?>> rawProviders = new ArrayList<>();
        @SuppressWarnings({ "unchecked", "rawtypes" })
        ServiceLoader<LinearAlgebraProvider<?>> loader = ServiceLoader.load((Class) LinearAlgebraProvider.class);
        Iterator<LinearAlgebraProvider<?>> it = loader.iterator();
        while(true) {
            try {
                if (!it.hasNext()) break;
                LinearAlgebraProvider<?> p = it.next();
                String name = "Unknown";
                try { name = p.getName(); } catch(Throwable t) {}
                System.out.println("[ComplianceTest] Discovered via SPI: " + p.getClass().getName() + " (" + name + ")");
                if (p.isCompatible(org.episteme.core.mathematics.sets.Reals.getInstance())) {
                    @SuppressWarnings("unchecked")
                    LinearAlgebraProvider<Real> typed = (LinearAlgebraProvider<Real>) p;
                    rawProviders.add(typed);
                }
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
                        if (p.isCompatible(org.episteme.core.mathematics.sets.Reals.getInstance())) {
                            @SuppressWarnings("unchecked")
                            LinearAlgebraProvider<Real> typed = (LinearAlgebraProvider<Real>) p;
                            rawProviders.add(typed);
                        }
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
                runSuite(res, typed, rawProviders, "Real");
            }
            
            // Run Complex Suite
            if (provider.isCompatible(org.episteme.core.mathematics.sets.Complexes.getInstance())) {
                @SuppressWarnings("unchecked")
                LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex> typed = (LinearAlgebraProvider<org.episteme.core.mathematics.numbers.complex.Complex>) provider;
                runSuite(res, typed, rawProviders, "Complex");
            }

            results.add(res);
        }
        printMarkdownReport(results);
    }

    private <E> void runSuite(ComplianceResult res, LinearAlgebraProvider<E> provider, List<LinearAlgebraProvider<?>> rawProviders, String typeLabel) {
        // Strictly isolate the provider under test, but allow delegation for decorators
        List<AlgorithmProvider> allowed = new ArrayList<>();
        allowed.add(provider);
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
            org.episteme.core.mathematics.structures.rings.Ring<E> ring = (org.episteme.core.mathematics.structures.rings.Ring<E>) (isComplex ? org.episteme.core.mathematics.sets.Complexes.getInstance() : org.episteme.core.mathematics.sets.Reals.getInstance());

            testOperation(res, "Transpose" + suffix, () -> {
                Random rand = new Random(42);
                Matrix<E> a = randomMatrix(SIZE, SIZE, rand, ring);
                Matrix<E> result = provider.transpose(a);
                verifyTranspose(a, result, ring);
            });

            testOperation(res, "Multiply" + suffix, () -> {
                Random rand = new Random(42);
                Matrix<E> a = randomMatrix(SIZE, SIZE, rand, ring);
                Matrix<E> b = randomMatrix(SIZE, SIZE, rand, ring);
                Matrix<E> result = provider.multiply(a, b);
                verifyMultiply(a, b, result, ring);
            });

            testOperation(res, "Inverse" + suffix, () -> {
                Random rand = new Random(42);
                Matrix<E> a = randomMatrix(SIZE, SIZE, rand, ring);
                // Ensure non-singular by adding to diagonal
                @SuppressWarnings("unchecked")
                E ten = isComplex ? (E)org.episteme.core.mathematics.numbers.complex.Complex.of(10) : (E)Real.of(10);
                @SuppressWarnings("unchecked")
                E[][] diagData = (E[][]) new Object[SIZE][SIZE];
                for(int i=0; i<SIZE; i++) for(int j=0; j<SIZE; j++) diagData[i][j] = (i==j) ? ten : ring.zero();
                a = a.add(org.episteme.core.mathematics.linearalgebra.Matrix.of(diagData, ring));
                
                Matrix<E> result = provider.inverse(a);
                verifyInverse(a, result, ring);
            });

            testOperation(res, "LU" + suffix, () -> {
                Random rand = new Random(42);
                Matrix<E> a = randomMatrix(SIZE, SIZE, rand, ring);
                LUResult<E> result = provider.lu(a);
                verifyLU(a, result, ring);
            });

            testOperation(res, "QR" + suffix, () -> {
                Random rand = new Random(42);
                Matrix<E> a = randomMatrix(SIZE, SIZE, rand, ring);
                QRResult<E> result = provider.qr(a);
                verifyQR(a, result, ring);
            });

            testOperation(res, "SVD" + suffix, () -> {
                Random rand = new Random(42);
                Matrix<E> a = randomMatrix(20, 15, rand, ring);
                SVDResult<E> result = provider.svd(a);
                verifySVD(a, result, ring);
            });

            testOperation(res, "Dot" + suffix, () -> {
                Random rand = new Random(42);
                Vector<E> a = randomVector(SIZE, rand, ring);
                Vector<E> b = randomVector(SIZE, rand, ring);
                E result = provider.dot(a, b);
                E expected = ring.zero();
                for(int i=0; i<SIZE; i++) expected = ring.add(expected, ring.multiply(a.get(i), b.get(i)));
                assertScalarEquals(expected, result, TOLERANCE, ring);
            });

            testOperation(res, "Norm" + suffix, () -> {
                Random rand = new Random(42);
                Vector<E> a = randomVector(SIZE, rand, ring);
                E normVal = provider.norm(a);
                // Norm is magnitude for complex, absolute value for real
                double sumSq = 0;
                for(int i=0; i<SIZE; i++) {
                    E val = a.get(i);
                    if (isComplex) {
                        org.episteme.core.mathematics.numbers.complex.Complex c = (org.episteme.core.mathematics.numbers.complex.Complex) (Object) val;
                        sumSq += c.real()*c.real() + c.imaginary()*c.imaginary();
                    } else {
                        double d = ((org.episteme.core.mathematics.numbers.real.Real)(Object)val).doubleValue();
                        sumSq += d*d;
                    }
                }
                assertRelativeEquals(Math.sqrt(sumSq), absValueDouble(normVal, ring), TOLERANCE);
            });
            
            if (provider instanceof SparseLinearAlgebraProvider<E> sparseProvider) {
                @SuppressWarnings("unchecked")
                E eps = isComplex ? (E)org.episteme.core.mathematics.numbers.complex.Complex.of(1e-8) : (E)Real.of(1e-8);
                @SuppressWarnings("unchecked")
                E twenty = isComplex ? (E)org.episteme.core.mathematics.numbers.complex.Complex.of(20) : (E)Real.of(20);
                testOperation(res, "BiCGSTAB" + suffix, () -> {
                    Random rand = new Random(42);
                    int n = 30;
                    Matrix<E> a = randomMatrix(n, n, rand, ring);
                    // Boosting diagonal for stability
                    @SuppressWarnings("unchecked")
                    E[][] diagData = (E[][]) new Object[n][n];
                    for(int i=0; i<n; i++) for(int j=0; j<n; j++) diagData[i][j] = (i==j) ? twenty : ring.zero();
                    a = a.add(org.episteme.core.mathematics.linearalgebra.Matrix.of(diagData, ring));

                    Vector<E> b = randomVector(n, rand, ring);
                    Vector<E> x0 = Vector.of(new ArrayList<>(Collections.nCopies(n, ring.zero())), ring);
                    Vector<E> x = sparseProvider.bicgstab(a, b, x0, eps, 2000);
                    Vector<E> ax = provider.multiply(a, x);
                    verifyVector(b, ax, 1e-4, ring);
                });

                testOperation(res, "GMRES" + suffix, () -> {
                    Random rand = new Random(42);
                    int n = 30;
                    Matrix<E> a = randomMatrix(n, n, rand, ring);
                    @SuppressWarnings("unchecked")
                    E[][] diagData = (E[][]) new Object[n][n];
                    for(int i=0; i<n; i++) for(int j=0; j<n; j++) diagData[i][j] = (i==j) ? twenty : ring.zero();
                    a = a.add(org.episteme.core.mathematics.linearalgebra.Matrix.of(diagData, ring));

                    Vector<E> b = randomVector(n, rand, ring);
                    Vector<E> x0 = Vector.of(new ArrayList<>(Collections.nCopies(n, ring.zero())), ring);
                    Vector<E> x = sparseProvider.gmres(a, b, x0, eps, 2000, 30);
                    Vector<E> ax = provider.multiply(a, x);
                    verifyVector(b, ax, 1e-4, ring);
                });
            }

        } finally {
            AlgorithmManager.setService(oldService);
        }
    }

    private <E> Matrix<E> randomMatrix(int rows, int cols, Random rand, org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
        @SuppressWarnings("unchecked")
        E[][] data = (E[][]) new Object[rows][cols];
        boolean isComplex = ring instanceof org.episteme.core.mathematics.sets.Complexes;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isComplex) {
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

    private <E> Vector<E> randomVector(int n, Random rand, org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
         @SuppressWarnings("unchecked")
         E[] data = (E[]) new Object[n];
         boolean isComplex = ring instanceof org.episteme.core.mathematics.sets.Complexes;
         for (int i = 0; i < n; i++) {
             if (isComplex) {
                 @SuppressWarnings("unchecked")
                 E val = (E) org.episteme.core.mathematics.numbers.complex.Complex.of(rand.nextDouble()*2-1, rand.nextDouble()*2-1);
                 data[i] = val;
             } else {
                 @SuppressWarnings("unchecked")
                 E val = (E) Real.of(rand.nextDouble()*2-1);
                 data[i] = val;
             }
         }
         return Vector.of(Arrays.asList(data), ring);
    }

    private <E> void verifyTranspose(Matrix<E> a, Matrix<E> result, org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
        assertEquals(a.rows(), result.cols());
        assertEquals(a.cols(), result.rows());
        for(int i=0; i<a.rows(); i++) for(int j=0; j<a.cols(); j++) assertScalarEquals(a.get(i, j), result.get(j, i), 1e-12, ring);
    }

    private <E> void verifyMultiply(Matrix<E> a, Matrix<E> b, Matrix<E> result, org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
        assertEquals(a.rows(), result.rows());
        assertEquals(b.cols(), result.cols());
        // Simple O(N^3) check for compliance
        for(int i=0; i<a.rows(); i++) {
            for(int j=0; j<b.cols(); j++) {
                E sum = ring.zero();
                for(int k=0; k<a.cols(); k++) sum = ring.add(sum, ring.multiply(a.get(i, k), b.get(k, j)));
                assertScalarEquals(sum, result.get(i, j), 1e-7, ring);
            }
        }
    }

    private <E> void verifyInverse(Matrix<E> a, Matrix<E> inv, org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
        Matrix<E> id = a.multiply(inv);
        for(int i=0; i<a.rows(); i++) {
            for(int j=0; j<a.cols(); j++) {
                assertScalarEquals(i == j ? ring.one() : ring.zero(), id.get(i, j), 1e-6, ring);
            }
        }
    }

    private <E> void verifyLU(Matrix<E> a, LUResult<E> res, org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
        Matrix<E> recon = res.L().multiply(res.U());
        // PA = LU
        int n = a.rows();
        for(int i=0; i<n; i++) {
            E pVal = res.P().get(i);
            int pivot = (pVal instanceof Number nptr) ? nptr.intValue() : (int)Double.parseDouble(pVal.toString());
            for(int j=0; j<n; j++) assertScalarEquals(a.get(pivot, j), recon.get(i, j), 1e-7, ring);
        }
    }

    private <E> void verifyQR(Matrix<E> a, QRResult<E> res, org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
        Matrix<E> recon = res.Q().multiply(res.R());
        verifyMatrix(a, recon, 1e-7, ring);
    }

    private <E> void verifySVD(Matrix<E> a, SVDResult<E> res, org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
        int k = res.S().dimension();
        @SuppressWarnings("unchecked")
        E[][] sData = (E[][]) new Object[res.U().cols()][res.V().cols()];
        for(int i=0; i<sData.length; i++) {
            for(int j=0; j<sData[0].length; j++) {
                sData[i][j] = (i==j && i<k) ? (E)res.S().get(i) : ring.zero();
            }
        }
        Matrix<E> S = new org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix<>(sData, ring);
        
        Matrix<E> VT = res.V().transpose();
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
        
        Matrix<E> recon = res.U().multiply(S).multiply(VT);
        verifyMatrix(a, recon, 1e-6, ring);
    }

    private <E> void verifyMatrix(Matrix<E> expected, Matrix<E> actual, double tol, org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
        assertEquals(expected.rows(), actual.rows());
        assertEquals(expected.cols(), actual.cols());
        for(int i=0; i<expected.rows(); i++) for(int j=0; j<expected.cols(); j++) assertScalarEquals(expected.get(i, j), actual.get(i, j), tol, ring);
    }

    private <E> void verifyVector(Vector<E> expected, Vector<E> actual, double tol, org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
        assertEquals(expected.dimension(), actual.dimension());
        for(int i=0; i<expected.dimension(); i++) assertScalarEquals(expected.get(i), actual.get(i), tol, ring);
    }

    private <E> void assertScalarEquals(E expected, E actual, double tol, org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) {
            assertRelativeEquals(((Real)expected).doubleValue(), ((Real)actual).doubleValue(), tol);
        } else {
            org.episteme.core.mathematics.numbers.complex.Complex e = (org.episteme.core.mathematics.numbers.complex.Complex)expected;
            org.episteme.core.mathematics.numbers.complex.Complex a = (org.episteme.core.mathematics.numbers.complex.Complex)actual;
            assertRelativeEquals(e.real(), a.real(), tol, "Real part mismatch");
            assertRelativeEquals(e.imaginary(), a.imaginary(), tol, "Imag part mismatch");
        }
    }

    private void assertRelativeEquals(double expected, double actual, double tol) {
        assertRelativeEquals(expected, actual, tol, null);
    }

    private void assertRelativeEquals(double expected, double actual, double tol, String msg) {
        if (Double.isNaN(expected) && Double.isNaN(actual)) return;
        double diff = Math.abs(expected - actual);
        if (diff < 1e-12) return;
        double denom = Math.max(Math.abs(expected), Math.abs(actual));
        double relDiff = (denom > 1e-12) ? diff / denom : diff;
        String fullMsg = (msg == null ? "" : msg + " | ") + "Expected: " + expected + ", Actual: " + actual + " (RelDiff: " + relDiff + ", Tol: " + tol + ")";
        assertTrue(relDiff < tol, fullMsg);
    }

    private void testOperation(ComplianceResult res, String opName, Runnable test) {
        try {
            test.run();
            res.status.put(opName, "✅ PASS");
        } catch (UnsupportedOperationException | NoSuchElementException e) {
            res.status.put(opName, "❌ N/A");
        } catch (Throwable e) {
            Throwable cause = e;
            if (e instanceof org.episteme.core.technical.algorithm.AlgorithmException && e.getCause() != null) cause = e.getCause();
            if (cause instanceof NoSuchElementException) { res.status.put(opName, "❌ N/A"); return; }
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
        
        // Collect all operation names from all results to ensure we have all columns
        Set<String> allOps = new LinkedHashSet<>();
        for (ComplianceResult res : results) allOps.addAll(res.status.keySet());
        
        sb.append("| Provider | Environment |");
        for (String op : allOps) sb.append(" ").append(op).append(" |");
        sb.append("\n| --- | --- |").append(" --- |".repeat(allOps.size())).append("\n");

        for (ComplianceResult res : results) {
            sb.append("| ").append(res.providerName).append(" | ").append(res.environment).append(" |");
            for (String op : allOps) {
                sb.append(" ").append(res.status.getOrDefault(op, "❌ N/A")).append(" |");
            }
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
    }

    private <E> double absValueDouble(E element, org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
        if (element instanceof Real) return ((Real) element).doubleValue();
        if (element instanceof org.episteme.core.mathematics.numbers.complex.Complex) return ((org.episteme.core.mathematics.numbers.complex.Complex) element).abs().doubleValue();
        if (element instanceof Number) return ((Number) element).doubleValue();
        try {
            return Double.parseDouble(element.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }
}
