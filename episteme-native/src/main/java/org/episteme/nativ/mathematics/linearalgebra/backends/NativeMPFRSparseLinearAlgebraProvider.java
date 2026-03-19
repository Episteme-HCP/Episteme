/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.core.mathematics.linearalgebra.backends.LinearAlgebraBackend;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.auto.service.AutoService;

/**
 * High-performance Sparse Arbitrary Precision Linear Algebra backend using libmpfr.
 * Implements CSR-based logic with MPFR arithmetic via Project Panama (FFM).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService({LinearAlgebraBackend.class, Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, CPUBackend.class})
public class NativeMPFRSparseLinearAlgebraProvider implements LinearAlgebraBackend<Real>, SparseLinearAlgebraProvider<Real>, NativeBackend, CPUBackend {

    private static final Logger logger = LoggerFactory.getLogger(NativeMPFRSparseLinearAlgebraProvider.class);
    private static final Linker LINKER = Linker.nativeLinker();
    private static boolean AVAILABLE = false;

    // MPFR Handles
    private static MethodHandle MPFR_INIT2;
    private static MethodHandle MPFR_CLEAR;
    private static MethodHandle MPFR_SET_STR;
    private static MethodHandle MPFR_GET_STR;
    private static MethodHandle MPFR_ADD;
    @SuppressWarnings("unused")
    private static MethodHandle MPFR_SUB;
    private static MethodHandle MPFR_MUL;
    private static MethodHandle MPFR_DIV;
    private static MethodHandle MPFR_SQRT;
    private static MethodHandle MPFR_CMP;
    private static MethodHandle MPFR_SET;
    private static MethodHandle MPFR_FREE_STR;

    public static final StructLayout MPFR_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_INT.withName("prec"),
        ValueLayout.JAVA_INT.withName("sign"),
        ValueLayout.JAVA_INT.withName("exp"),
        MemoryLayout.paddingLayout(4),
        ValueLayout.ADDRESS.withName("d")
    );

    static {
        try {
            Optional<SymbolLookup> mpfrLookup = NativeFFMLoader.loadLibrary("mpfr", Arena.global());
            if (mpfrLookup.isPresent()) {
                SymbolLookup mpfr = mpfrLookup.get();
                MPFR_INIT2 = lookup(mpfr, "mpfr_init2", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));
                MPFR_CLEAR = lookup(mpfr, "mpfr_clear", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
                MPFR_SET_STR = lookup(mpfr, "mpfr_set_str", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));
                MPFR_GET_STR = lookup(mpfr, "mpfr_get_str", FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ADD = lookup(mpfr, "mpfr_add", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_SUB = lookup(mpfr, "mpfr_sub", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_MUL = lookup(mpfr, "mpfr_mul", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_DIV = lookup(mpfr, "mpfr_div", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_SQRT = lookup(mpfr, "mpfr_sqrt", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_CMP = lookup(mpfr, "mpfr_cmp", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
                MPFR_SET = lookup(mpfr, "mpfr_set", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_FREE_STR = lookup(mpfr, "mpfr_free_str", FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

                AVAILABLE = MPFR_INIT2 != null && MPFR_ADD != null;
                if (AVAILABLE) {
                    logger.info("Native MPFR Sparse Backend initialized (Panama).");
                }
            }
        } catch (Throwable t) {
            logger.warn("Failed to initialize MPFR Sparse Backend: {}", t.getMessage());
        }
    }

    private static MethodHandle lookup(SymbolLookup lookup, String name, FunctionDescriptor desc) {
        return lookup.find(name).map(s -> LINKER.downcallHandle(s, desc)).orElse(null);
    }

    @Override
    public String getName() {
        return "Native MPFR Sparse Linear Algebra Backend";
    }

    public java.util.Set<String> getCapabilities() {
        return java.util.Set.of("Transpose", "Multiply");
    }

    @Override
    public String getType() {
        return "linearalgebra";
    }

    @Override
    public int getPriority() {
        return 120; // High priority for high-precision tasks
    }

    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        if (ring instanceof org.episteme.core.mathematics.sets.Reals) return true;
        if (ring.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex) return true;
        return ring.zero() instanceof Real;
    }

    @Override
    public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.CPU;
    }

    @Override
    public boolean isLoaded() {
        return AVAILABLE;
    }

    @Override
    public String getNativeLibraryName() {
        return "mpfr";
    }

    @Override
    public Object createBackend() {
        return this;
    }

    @Override
    public String getEnvironmentInfo() {
        return AVAILABLE ? "CPU (Panama/MPFR/Sparse)" : "N/A";
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.ExecutionContext() {
            @Override
            public <T> T execute(org.episteme.core.technical.backend.Operation<T> operation) {
                return operation.compute(this);
            }
            @Override public void close() {}
        };
    }

    @Override
    public void shutdown() {}

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        Matrix<Real> res = multiply(a, b.toMatrix());
        java.util.List<Real> list = new java.util.ArrayList<>(res.rows());
        for (int i = 0; i < res.rows(); i++) {
            list.add(res.get(i, 0));
        }
        return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(list, a.getScalarRing());
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!(a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix)) {
            a = toSparse(a);
        }
        
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> sa = 
            (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) a;
        
        int n = b.cols();

        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined()) {
            if (n == 1) {
                // SpMV
                return spmv(sa, b, arena, prec);
            }
            // General Sparse-Dense Multiply
            return spmm(sa, b, arena, prec);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR Sparse multiply failed", t);
        }
    }

    private Matrix<Real> spmv(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> a, Matrix<Real> x, Arena arena, long prec) throws Throwable {
        int m = a.rows();
        int[] rowPtr = a.getRowPointers();
        int[] colIdx = a.getColIndices();
        Object[] vals = a.getValues();

        MemorySegment h_x = initVector(x, arena, prec);
        MemorySegment h_y = allocateVector(m, arena, prec);

        MemorySegment term = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, term, prec);

        for (int i = 0; i < m; i++) {
            MemorySegment sum = getMPFR(h_y, i);
            NativeSafe.invoke(MPFR_SET_STR, sum, arena.allocateFrom("0"), 10, 0);

            for (int idx = rowPtr[i]; idx < rowPtr[i+1]; idx++) {
                int col = colIdx[idx];
                String valStr = ((Real) vals[idx]).bigDecimalValue().toPlainString();
                NativeSafe.invoke(MPFR_SET_STR, term, arena.allocateFrom(valStr), 10, 0);
                
                NativeSafe.invoke(MPFR_MUL, term, term, getMPFR(h_x, col), 0);
                NativeSafe.invoke(MPFR_ADD, sum, sum, term, 0);
            }
        }

        NativeSafe.invoke(MPFR_CLEAR, term);

        return backToMatrix(h_y, m, 1, arena);
    }

    private Matrix<Real> spmm(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> a, Matrix<Real> b, Arena arena, long prec) throws Throwable {
        // Fallback to multiple SpMV for now, or implement blocked SPMM
        int m = a.rows();
        int n = b.cols();
        org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<Real> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<>(m, n, Real.ZERO);
        
        for (int j = 0; j < n; j++) {
            // Extract column j of B as a vector
            // This is slow, but functional for now. Optimized implementation should use direct memory access.
            Vector<Real> colB = b.getColumn(j);
            Matrix<Real> resCol = multiply(a, colB.toMatrix());
            
            for (int i = 0; i < m; i++) {
                storage.set(i, j, resCol.get(i, 0));
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<>(storage, this, Real.ZERO);
    }

    private long getPrecision() {
        int digits = MathContext.getCurrent().getJavaMathContext().getPrecision();
        if (digits <= 0) digits = 50; 
        return (long) (digits * 3.322) + 1;
    }

    private MemorySegment allocateVector(int n, Arena arena, long prec) throws Throwable {
        MemorySegment vec = arena.allocate(MPFR_LAYOUT, n);
        for (int i = 0; i < n; i++) {
            NativeSafe.invoke(MPFR_INIT2, vec.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), prec);
        }
        return vec;
    }

    private MemorySegment initVector(Matrix<Real> v, Arena arena, long prec) throws Throwable {
        int n = v.rows(); // Vector as col-matrix
        MemorySegment vec = allocateVector(n, arena, prec);
        for (int i = 0; i < n; i++) {
            String val = v.get(i, 0).bigDecimalValue().toPlainString();
            NativeSafe.invoke(MPFR_SET_STR, getMPFR(vec, i), arena.allocateFrom(val), 10, 0);
        }
        return vec;
    }

    private MemorySegment getMPFR(MemorySegment vec, int idx) {
        return vec.asSlice((long) idx * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
    }

    private Matrix<Real> backToMatrix(MemorySegment vec, int rows, int cols, Arena arena) throws Throwable {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<Real> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<>(rows, cols, Real.ZERO);
        
        MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
        for (int i = 0; i < rows * cols; i++) {
            storage.set(i / cols, i % cols, readMPFR(getMPFR(vec, i), expPtr, arena));
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<>(storage, this, Real.ZERO);
    }

    private Real readMPFR(MemorySegment val, MemorySegment expPtr, Arena arena) throws Throwable {
        MemorySegment strPtr = (MemorySegment) NativeSafe.invoke(MPFR_GET_STR, MemorySegment.NULL, expPtr, 10, 0L, val, 0);
        if (strPtr == null || strPtr.equals(MemorySegment.NULL)) {
            return Real.ZERO;
        }
        
        String s = NativeSafe.scavenge(strPtr, 1024, arena, "mpfr_get_str").segment().getString(0);
        long exp = expPtr.get(ValueLayout.JAVA_LONG, 0);
        
        if (s.isEmpty() || s.equals("0")) {
             NativeSafe.invoke(MPFR_FREE_STR, strPtr);
             return Real.ZERO;
        }

        StringBuilder sb = new StringBuilder();
        if (s.startsWith("-")) {
            if (s.length() > 1) {
                sb.append("-0.").append(s.substring(1));
            } else {
                sb.append("-0"); 
            }
        } else {
            sb.append("0.").append(s);
        }
        sb.append("E").append(exp);
        NativeSafe.invoke(MPFR_FREE_STR, strPtr);
        
        try {
            return Real.of(new java.math.BigDecimal(sb.toString()));
        } catch (NumberFormatException e) {
            logger.error("Failed to parse MPFR string: '{}' (exp={})", s, exp);
            throw new RuntimeException("Invalid MPFR numeric string: " + sb, e);
        }
    }

    private org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> toSparse(Matrix<Real> m) {
        if (m instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            return (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) m;
        }
        int rows = m.rows();
        int cols = m.cols();
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<Real> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(rows, cols, Real.ZERO);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Real val = m.get(i, j);
                if (!val.equals(Real.ZERO)) {
                    storage.set(i, j, val);
                }
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>(storage, Real.ZERO);
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        return conjugateGradient(a, b, null, Real.of(1e-15), 1000);
    }

    @Override
    public Vector<Real> conjugateGradient(Matrix<Real> a, Vector<Real> b, Vector<Real> x0, Real tolerance, int maxIterations) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " is not available.");
        
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> sa = toSparse(a);
        int n = sa.rows();
        long prec = getPrecision();

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_b = initVector(b.toMatrix(), arena, prec);
            MemorySegment h_x = (x0 != null) ? initVector(x0.toMatrix(), arena, prec) : allocateVector(n, arena, prec);
            
            MemorySegment h_r = allocateVector(n, arena, prec);
            MemorySegment h_p = allocateVector(n, arena, prec);
            MemorySegment h_Ap = allocateVector(n, arena, prec);
            
            MemorySegment alpha = arena.allocate(MPFR_LAYOUT);
            MemorySegment beta = arena.allocate(MPFR_LAYOUT);
            MemorySegment rDotR = arena.allocate(MPFR_LAYOUT);
            MemorySegment rNextDotRNext = arena.allocate(MPFR_LAYOUT);
            MemorySegment pDotAp = arena.allocate(MPFR_LAYOUT);
            MemorySegment tol = arena.allocate(MPFR_LAYOUT);
            MemorySegment temp = arena.allocate(MPFR_LAYOUT);

            NativeSafe.invoke(MPFR_INIT2, alpha, prec);
            NativeSafe.invoke(MPFR_INIT2, beta, prec);
            NativeSafe.invoke(MPFR_INIT2, rDotR, prec);
            NativeSafe.invoke(MPFR_INIT2, rNextDotRNext, prec);
            NativeSafe.invoke(MPFR_INIT2, pDotAp, prec);
            NativeSafe.invoke(MPFR_INIT2, tol, prec);
            NativeSafe.invoke(MPFR_INIT2, temp, prec);

            NativeSafe.invoke(MPFR_SET_STR, tol, arena.allocateFrom(tolerance.bigDecimalValue().toPlainString()), 10, 0);

            // r = b - A*x
            spmv_internal(sa, h_x, h_r, arena, prec); // r = A*x
            for (int i = 0; i < n; i++) {
                NativeSafe.invoke(MPFR_SUB, getMPFR(h_r, i), getMPFR(h_b, i), getMPFR(h_r, i), 0);
            }
            
            // p = r
            copyVector(h_p, h_r, n);

            // rDotR = r . r
            dotProduct(h_r, h_r, n, rDotR, prec, arena);

            int iter = 0;
            while (iter < maxIterations) {
                // Check convergence: sqrt(rDotR) < tolerance
                NativeSafe.invoke(MPFR_SQRT, temp, rDotR, 0);
                if ((int) NativeSafe.invoke(MPFR_CMP, temp, tol) < 0) break;

                // Ap = A * p
                spmv_internal(sa, h_p, h_Ap, arena, prec);

                // alpha = rDotR / (p . Ap)
                dotProduct(h_p, h_Ap, n, pDotAp, prec, arena);
                NativeSafe.invoke(MPFR_DIV, alpha, rDotR, pDotAp, 0);

                // x = x + alpha * p
                // r = r - alpha * Ap
                for (int i = 0; i < n; i++) {
                    // x_i = x_i + alpha * p_i
                    NativeSafe.invoke(MPFR_MUL, temp, alpha, getMPFR(h_p, i), 0);
                    NativeSafe.invoke(MPFR_ADD, getMPFR(h_x, i), getMPFR(h_x, i), temp, 0);
                    
                    // r_i = r_i - alpha * Ap_i
                    NativeSafe.invoke(MPFR_MUL, temp, alpha, getMPFR(h_Ap, i), 0);
                    NativeSafe.invoke(MPFR_SUB, getMPFR(h_r, i), getMPFR(h_r, i), temp, 0);
                }

                // rNextDotRNext = r . r
                dotProduct(h_r, h_r, n, rNextDotRNext, prec, arena);

                // beta = rNextDotRNext / rDotR
                NativeSafe.invoke(MPFR_DIV, beta, rNextDotRNext, rDotR, 0);

                // p = r + beta * p
                for (int i = 0; i < n; i++) {
                    NativeSafe.invoke(MPFR_MUL, temp, beta, getMPFR(h_p, i), 0);
                    NativeSafe.invoke(MPFR_ADD, getMPFR(h_p, i), getMPFR(h_r, i), temp, 0);
                }

                // rDotR = rNextDotRNext
                NativeSafe.invoke(MPFR_SET_STR, rDotR, arena.allocateFrom("0"), 10, 0);
                NativeSafe.invoke(MPFR_ADD, rDotR, rDotR, rNextDotRNext, 0);

                iter++;
            }

            if (iter >= maxIterations) {
                logger.warn("Conjugate Gradient did not converge after {} iterations.", maxIterations);
            } else {
                logger.info("Conjugate Gradient converged in {} iterations.", iter);
            }

            Matrix<Real> resMatrix = backToMatrix(h_x, n, 1, arena);
            
            // Cleanup
            NativeSafe.invoke(MPFR_CLEAR, alpha);
            NativeSafe.invoke(MPFR_CLEAR, beta);
            NativeSafe.invoke(MPFR_CLEAR, rDotR);
            NativeSafe.invoke(MPFR_CLEAR, rNextDotRNext);
            NativeSafe.invoke(MPFR_CLEAR, pDotAp);
            NativeSafe.invoke(MPFR_CLEAR, tol);
            NativeSafe.invoke(MPFR_CLEAR, temp);

            java.util.List<Real> list = new java.util.ArrayList<>(n);
            for (int i = 0; i < n; i++) list.add(resMatrix.get(i, 0));
            return org.episteme.core.mathematics.linearalgebra.vectors.DenseVector.of(list, a.getScalarRing());

        } catch (Throwable t) {
            throw new RuntimeException("MPFR Conjugate Gradient failed", t);
        }
    }

    private void spmv_internal(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> a, MemorySegment h_x, MemorySegment h_y, Arena arena, long prec) throws Throwable {
        int m = a.rows();
        int[] rowPtr = a.getRowPointers();
        int[] colIdx = a.getColIndices();
        Object[] vals = a.getValues();

        MemorySegment term = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, term, prec);

        for (int i = 0; i < m; i++) {
            MemorySegment sum = getMPFR(h_y, i);
            NativeSafe.invoke(MPFR_SET_STR, sum, arena.allocateFrom("0"), 10, 0);

            for (int idx = rowPtr[i]; idx < rowPtr[i+1]; idx++) {
                int col = colIdx[idx];
                String valStr = ((Real) vals[idx]).bigDecimalValue().toPlainString();
                NativeSafe.invoke(MPFR_SET_STR, term, arena.allocateFrom(valStr), 10, 0);
                
                NativeSafe.invoke(MPFR_MUL, term, term, getMPFR(h_x, col), 0);
                NativeSafe.invoke(MPFR_ADD, sum, sum, term, 0);
            }
        }
        NativeSafe.invoke(MPFR_CLEAR, term);
    }

    private void dotProduct(MemorySegment v1, MemorySegment v2, int n, MemorySegment result, long prec, Arena arena) throws Throwable {
        NativeSafe.invoke(MPFR_SET_STR, result, arena.allocateFrom("0"), 10, 0);
        MemorySegment term = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, term, prec);
        
        for (int i = 0; i < n; i++) {
            NativeSafe.invoke(MPFR_MUL, term, getMPFR(v1, i), getMPFR(v2, i), 0);
            NativeSafe.invoke(MPFR_ADD, result, result, term, 0);
        }
        NativeSafe.invoke(MPFR_CLEAR, term);
    }

    private void copyVector(MemorySegment dest, MemorySegment src, int n) throws Throwable {
        for (int i = 0; i < n; i++) {
            NativeSafe.invoke(MPFR_SET, getMPFR(dest, i), getMPFR(src, i), 0);
        }
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": MPFR Sparse transpose not available");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> sa = toSparse(a);
        int rows = sa.rows();
        int cols = sa.cols();
        
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<Real> newStorage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(cols, rows, Real.ZERO);
        
        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        Object[] values = sa.getValues();
        
        for (int i = 0; i < rows; i++) {
            for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                newStorage.set(colIdx[k], i, (Real) values[k]);
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(newStorage, Real.ZERO);
    }
}
