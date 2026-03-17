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
    private static MethodHandle MPFR_SUB;
    private static MethodHandle MPFR_MUL;
    private static MethodHandle MPFR_DIV;
    private static MethodHandle MPFR_FREE_STR;

    public static final StructLayout MPFR_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_LONG.withName("prec"),
        ValueLayout.JAVA_INT.withName("sign"),
        MemoryLayout.paddingLayout(4),
        ValueLayout.JAVA_LONG.withName("exp"),
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
    public boolean isAvailable() {
        return AVAILABLE && !isExplicitlyDisabled();
    }

    @Override
    public String getId() {
        return "native-mpfr-sparse";
    }

    @Override
    public String getName() {
        return "Native MPFR Sparse Backend";
    }

    @Override
    public String getDescription() {
        return "Native high-performance Sparse Arbitrary Precision Linear Algebra backend using libmpfr bound via Project Panama.";
    }

    @Override
    public int getPriority() {
        return 120; // High priority for high-precision tasks
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
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        if (!(a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix)) {
            throw new UnsupportedOperationException("Matrix A must be sparse");
        }
        
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real> sa = 
            (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<Real>) a;
        
        int m = sa.rows();
        int k = sa.cols();
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
        int k = a.cols();
        int[] rowPtr = a.getRowPointers();
        int[] colIdx = a.getColIndices();
        Object[] vals = a.getValues();

        MemorySegment h_x = initVector(x, arena, prec);
        MemorySegment h_y = allocateVector(m, arena, prec);

        MemorySegment temp = arena.allocate(MPFR_LAYOUT);
        MPFR_INIT2.invoke(temp, prec);
        MemorySegment term = arena.allocate(MPFR_LAYOUT);
        MPFR_INIT2.invoke(term, prec);

        for (int i = 0; i < m; i++) {
            MemorySegment sum = getMPFR(h_y, i);
            MPFR_SET_STR.invoke(sum, arena.allocateFrom("0"), 10, 0);

            for (int idx = rowPtr[i]; idx < rowPtr[i+1]; idx++) {
                int col = colIdx[idx];
                String valStr = ((Real) vals[idx]).bigDecimalValue().toPlainString();
                MPFR_SET_STR.invoke(term, arena.allocateFrom(valStr), 10, 0);
                
                MPFR_MUL.invoke(term, term, getMPFR(h_x, col), 0);
                MPFR_ADD.invoke(sum, sum, term, 0);
            }
        }

        MPFR_CLEAR.invoke(temp);
        MPFR_CLEAR.invoke(term);

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
            MPFR_INIT2.invoke(vec.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), prec);
        }
        return vec;
    }

    private MemorySegment initVector(Matrix<Real> v, Arena arena, long prec) throws Throwable {
        int n = v.rows(); // Vector as col-matrix
        MemorySegment vec = allocateVector(n, arena, prec);
        for (int i = 0; i < n; i++) {
            String val = v.get(i, 0).bigDecimalValue().toPlainString();
            MPFR_SET_STR.invoke(getMPFR(vec, i), arena.allocateFrom(val), 10, 0);
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
            MemorySegment strPtr = (MemorySegment) MPFR_GET_STR.invoke(MemorySegment.NULL, expPtr, 10, 0L, getMPFR(vec, i), 0);
            String s = strPtr.reinterpret(1024).getString(0);
            long exp = expPtr.get(ValueLayout.JAVA_LONG, 0);
            
            StringBuilder sb = new StringBuilder();
            if (s.startsWith("-")) {
                sb.append("-0.");
                sb.append(s.substring(1));
            } else {
                sb.append("0.");
                sb.append(s);
            }
            sb.append("E").append(exp);
            
            storage.set(i / cols, i % cols, Real.of(new java.math.BigDecimal(sb.toString())));
            MPFR_FREE_STR.invoke(strPtr);
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<>(storage, this, Real.ZERO);
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        // Implement Conjugate Gradient or BiCGSTAB using MPFR
        throw new UnsupportedOperationException("Native MPFR Sparse solve not yet implemented");
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        throw new UnsupportedOperationException("Native MPFR Sparse transpose not yet implemented");
    }
}
