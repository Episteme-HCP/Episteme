/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.benchmarks.benchmark.benchmarks;

import com.google.auto.service.AutoService;
import org.episteme.benchmarks.benchmark.RunnableBenchmark;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.mathematics.structures.rings.Ring;

import java.math.BigDecimal;
import java.util.*;

/**
 * Systematic benchmark that executes a comprehensive performance audit 
 * of a High-Precision provider across 70+ operations.
 */
@AutoService(RunnableBenchmark.class)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class HighPrecisionOperationsBenchmark implements SystematicBenchmark<LinearAlgebraProvider> {

    private LinearAlgebraProvider currentProvider;
    private int matrixSize = 8; // Small default for quick verification (User request)
    private boolean dryRun = false;
    private final Map<String, Object> latestMetrics = new LinkedHashMap<>();

    @Override
    public String getId() {
        return getIdPrefix() + (currentProvider != null ? "-" + currentProvider.getName().toLowerCase().replace(" ", "-") : "");
    }

    @Override
    public String getName() {
        return getNameBase() + (currentProvider != null ? " (" + currentProvider.getName() + ")" : "");
    }

    @Override
    public String getDescription() {
        return "Benchmarks 70+ linear algebra and transcendental operations across RealBig and Complex domains.";
    }

    @Override
    public String getDomain() {
        return "Linear Algebra (High-Precision Audit)";
    }

    @Override
    public String getIdPrefix() {
        return "hp-audit";
    }

    @Override
    public String getNameBase() {
        return "High-Precision Comprehensive Audit";
    }

    @Override
    public Class<LinearAlgebraProvider> getProviderClass() {
        return LinearAlgebraProvider.class;
    }

    @Override
    public void setProvider(LinearAlgebraProvider provider) {
        this.currentProvider = provider;
    }

    @Override
    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
        this.matrixSize = dryRun ? 8 : 64;
    }

    @Override
    public boolean isDryRun() {
        return dryRun;
    }

    @Override
    public void setup() {
        latestMetrics.clear();
    }

    @Override
    public void run() {
        if (currentProvider == null) return;
        
        // Ensure the provider is compatible with HP types
        Ring<RealBig> rbRing = (Ring<RealBig>) (Object) RealBig.create(BigDecimal.ZERO).getScalarRing();
        if (currentProvider.isCompatible(rbRing)) {
            runRealBigAudit(latestMetrics, (LinearAlgebraProvider<RealBig>) currentProvider);
        }

        Ring<Complex> cRing = Complex.of(0, 0).getScalarRing();
        if (currentProvider.isCompatible(cRing)) {
            runComplexAudit(latestMetrics, (LinearAlgebraProvider<Complex>) currentProvider);
        }
    }

    @Override
    public void teardown() {
        // Nothing to cleanup
    }

    @Override
    public Map<String, Object> getMetadata() {
        // This is a special benchmark that reports detailed metrics
        return latestMetrics;
    }

    @Override
    public int getSuggestedIterations() {
        return 1; // The suite itself is large
    }

    @Override
    public boolean isAvailable() {
        if (currentProvider == null) return true;
        
        String name = currentProvider.getName();
        // Exclude double-only providers to match HighPrecisionComplianceTest
        if (name.contains("EJML") || name.contains("Colt") || name.contains("Commons Math") || 
            name.contains("JBlas") || name.contains("ND4J") || name.contains("CUDA") ||
            name.contains("OpenCL") || name.contains("SIMD") || name.contains("FFMBLAS") ||
            name.contains("Native BLAS Provider FFM") || name.contains("Native CPU-BLAS") ||
            name.contains("Unified")) return false;
            
        return currentProvider.isAvailable();
    }

    private void runRealBigAudit(Map<String, Object> metrics, LinearAlgebraProvider<RealBig> p) {
        Matrix<RealBig> A = createRealBigMatrix(matrixSize);
        Matrix<RealBig> B = createRealBigMatrix(matrixSize);
        Vector<RealBig> v = createRealBigVector(matrixSize);
        
        // Groups will be used by the reporter
        measure(metrics, "Arithmetic:Add", () -> p.add(A, B));
        measure(metrics, "Arithmetic:Sub", () -> p.subtract(A, B));
        measure(metrics, "Arithmetic:Scale", () -> p.scale(RealBig.create(BigDecimal.valueOf(0.1)), A));
        measure(metrics, "Arithmetic:Mul", () -> p.multiply(A, B));
        measure(metrics, "Arithmetic:MatVec", () -> p.multiply(A, v));
        measure(metrics, "Arithmetic:Trans", () -> p.transpose(A));
        measure(metrics, "Arithmetic:Dot", () -> p.dot(v, v));
        measure(metrics, "Arithmetic:Norm", () -> p.norm(v));
        
        int decompSize = Math.max(8, matrixSize / 2);
        Matrix<RealBig> Ad = createInvertibleRealBigMatrix(decompSize);
        Vector<RealBig> vd = createRealBigVector(decompSize);
        
        measure(metrics, "Solvers:Inv", () -> p.inverse(Ad));
        measure(metrics, "Solvers:Det", () -> p.determinant(Ad));
        measure(metrics, "Solvers:Solve", () -> p.solve(Ad, vd));
        
        // Sparse Solvers (Corrected names and casting)
        if (p instanceof SparseLinearAlgebraProvider sp) {
            measure(metrics, "Sparse:BiCGSTAB", () -> sp.bicgstab(Ad, vd, vd, RealBig.create(new BigDecimal("1e-10")), 100));
            measure(metrics, "Sparse:ConjGrad", () -> sp.conjugateGradient(Ad, vd, vd, RealBig.create(new BigDecimal("1e-10")), 100));
            measure(metrics, "Sparse:GMRES", () -> sp.gmres(Ad, vd, vd, RealBig.create(new BigDecimal("1e-10")), 100, 10));
        }
        
        measure(metrics, "Decompositions:LU", () -> p.lu(Ad));
        measure(metrics, "Decompositions:QR", () -> p.qr(Ad));
        measure(metrics, "Decompositions:SVD", () -> p.svd(Ad));
        measure(metrics, "Decompositions:Chol", () -> p.cholesky(p.multiply((Matrix<RealBig>)(Matrix<?>)Ad.transpose(), Ad)));
        measure(metrics, "Decompositions:Eigen", () -> p.eigen(Ad));
        
        RealBig val = RealBig.create(new BigDecimal("0.5"));
        Matrix<RealBig> M1 = Matrix.of(new RealBig[][]{{val}}, (org.episteme.core.mathematics.structures.rings.Ring<RealBig>)(Object)val.getScalarRing());
        measure(metrics, "Transcendental:Exp", () -> p.exp(M1));
        measure(metrics, "Transcendental:Log", () -> p.log((Matrix<RealBig>)(Matrix<?>)p.add(M1, M1)));
        measure(metrics, "Transcendental:Log10", () -> p.log10(M1));
        measure(metrics, "Transcendental:Sin", () -> p.sin(M1));
        measure(metrics, "Transcendental:Cos", () -> p.cos(M1));
        measure(metrics, "Transcendental:Tan", () -> p.tan(M1));
        measure(metrics, "Transcendental:Asin", () -> p.asin(M1));
        measure(metrics, "Transcendental:Acos", () -> p.acos(M1));
        measure(metrics, "Transcendental:Atan", () -> p.atan(M1));
        measure(metrics, "Transcendental:Sinh", () -> p.sinh(M1));
        measure(metrics, "Transcendental:Cosh", () -> p.cosh(M1));
        measure(metrics, "Transcendental:Tanh", () -> p.tanh(M1));
        measure(metrics, "Transcendental:Asinh", () -> p.asinh(M1));
        measure(metrics, "Transcendental:Acosh", () -> p.acosh(M1));
        measure(metrics, "Transcendental:Atanh", () -> p.atanh(M1));
        measure(metrics, "Transcendental:Sqrt", () -> p.sqrt(M1));
        measure(metrics, "Transcendental:Cbrt", () -> p.cbrt(M1));
        measure(metrics, "Transcendental:Pow", () -> p.pow(M1, RealBig.create(new BigDecimal("2"))));
    }

    private void runComplexAudit(Map<String, Object> metrics, LinearAlgebraProvider<Complex> p) {
        Matrix<Complex> A = createComplexMatrix(matrixSize);
        Matrix<Complex> B = createComplexMatrix(matrixSize);

        measure(metrics, "Complex:Add", () -> p.add(A, B));
        measure(metrics, "Complex:Sub", () -> p.subtract(A, B));
        measure(metrics, "Complex:Mul", () -> p.multiply(A, B));
        
        int decompSize = Math.max(8, matrixSize / 2);
        Matrix<Complex> Ad = createInvertibleComplexMatrix(decompSize);
        Vector<Complex> vd = createComplexVector(decompSize);
        
        measure(metrics, "Complex:Inv", () -> p.inverse(Ad));
        measure(metrics, "Complex:Det", () -> p.determinant(Ad));
        measure(metrics, "Complex:Solve", () -> p.solve(Ad, vd));
        
        // Complex Sparse (New)
        if (p instanceof SparseLinearAlgebraProvider sp) {
            measure(metrics, "Complex:BiCGSTAB", () -> sp.bicgstab(Ad, vd, vd, Complex.of(1e-10, 0), 100));
            measure(metrics, "Complex:ConjGrad", () -> sp.conjugateGradient(Ad, vd, vd, Complex.of(1e-10, 0), 100));
            measure(metrics, "Complex:GMRES", () -> sp.gmres(Ad, vd, vd, Complex.of(1e-10, 0), 100, 10));
        }
        
        Complex val = Complex.of(0.5, 0.5);
        Matrix<Complex> M1 = Matrix.of(new Complex[][]{{val}}, val.getScalarRing());
        measure(metrics, "Complex:Exp", () -> p.exp(M1));
        measure(metrics, "Complex:Log", () -> p.log(p.add(M1, M1)));
        measure(metrics, "Complex:Log10", () -> p.log10(M1));
        measure(metrics, "Complex:Sin", () -> p.sin(M1));
        measure(metrics, "Complex:Cos", () -> p.cos(M1));
        measure(metrics, "Complex:Tan", () -> p.tan(M1));
        measure(metrics, "Complex:Asin", () -> p.asin(M1));
        measure(metrics, "Complex:Acos", () -> p.acos(M1));
        measure(metrics, "Complex:Atan", () -> p.atan(M1));
        measure(metrics, "Complex:Sinh", () -> p.sinh(M1));
        measure(metrics, "Complex:Cosh", () -> p.cosh(M1));
        measure(metrics, "Complex:Tanh", () -> p.tanh(M1));
        measure(metrics, "Complex:Sqrt", () -> p.sqrt(M1));
        measure(metrics, "Complex:Cbrt", () -> p.cbrt(M1));
        measure(metrics, "Complex:Pow", () -> p.pow(M1, Complex.of(2, 0)));
    }

    private void measure(Map<String, Object> metrics, String key, Runnable r) {
        try {
            long start = System.nanoTime();
            r.run();
            double latency = (System.nanoTime() - start) / 1_000_000.0;
            metrics.put(key, latency);
        } catch (Throwable e) {
            metrics.put(key, -1.0);
        }
    }

    // --- Helpers ---
    private Matrix<RealBig> createRealBigMatrix(int n) {
        RealBig[][] d = new RealBig[n][n];
        for(int i=0; i<n; i++) for(int j=0; j<n; j++) d[i][j] = RealBig.create(new BigDecimal(i+j+1));
        return Matrix.of(d, (org.episteme.core.mathematics.structures.rings.Ring<RealBig>)(Object)d[0][0].getScalarRing());
    }

    private Matrix<RealBig> createInvertibleRealBigMatrix(int n) {
        RealBig[][] d = new RealBig[n][n];
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if (i == j) d[i][j] = RealBig.create(new BigDecimal(n*2+i));
                else d[i][j] = RealBig.create(new BigDecimal(0.1*(i+j+1)));
            }
        }
        return Matrix.of(d, (org.episteme.core.mathematics.structures.rings.Ring<RealBig>)(Object)d[0][0].getScalarRing());
    }

    private Vector<RealBig> createRealBigVector(int n) {
        RealBig[] d = new RealBig[n];
        for(int i=0; i<n; i++) d[i] = RealBig.create(new BigDecimal(i+1));
        return Vector.of(java.util.List.of(d), (org.episteme.core.mathematics.structures.rings.Ring<RealBig>)(Object)d[0].getScalarRing());
    }

    private Matrix<Complex> createComplexMatrix(int n) {
        Complex[][] d = new Complex[n][n];
        for(int i=0; i<n; i++) for(int j=0; j<n; j++) d[i][j] = Complex.of(i+1, j+1);
        return Matrix.of(d, Complex.of(0, 0).getScalarRing());
    }

    private Matrix<Complex> createInvertibleComplexMatrix(int n) {
        Complex[][] d = new Complex[n][n];
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if (i == j) d[i][j] = Complex.of(n*2+i, 0.1);
                else d[i][j] = Complex.of(0.1, 0.05);
            }
        }
        return Matrix.of(d, Complex.of(0, 0).getScalarRing());
    }

    private Vector<Complex> createComplexVector(int n) {
        Complex[] d = new Complex[n];
        for(int i=0; i<n; i++) d[i] = Complex.of(i+1, 0.1);
        return Vector.of(d, Complex.of(0, 0).getScalarRing());
    }
}
