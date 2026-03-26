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
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.sparse.GenericSparseSolvers;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.algorithm.OperationContext.Hint;
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
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, CPUBackend.class})
@SuppressWarnings("unchecked")
public class NativeMPFRSparseLinearAlgebraProvider<E> implements LinearAlgebraBackend<E>, SparseLinearAlgebraProvider<E>, NativeBackend, CPUBackend {

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
    private static MethodHandle MPFR_SQRT;
    private static MethodHandle MPFR_CMP;
    private static MethodHandle MPFR_SET;
    private static MethodHandle MPFR_SET_D;
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
                MPFR_SET_D = lookup(mpfr, "mpfr_set_d", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_INT));
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
        return java.util.Set.of("Transpose", "Multiply", "Add", "Subtract", "Dot", "Norm", "ConjugateGradient", "BiCGSTAB", "GMRES");
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
    public boolean isAvailable() {
        return AVAILABLE && !isExplicitlyDisabled();
    }

    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        return true;
    }

    @Override
    public String getNativeLibraryName() {
        return "mpfr";
    }

    @Override
    public boolean isLoaded() {
        return AVAILABLE;
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
    public double score(OperationContext context) {
        if (!isAvailable()) return -1.0;
        if (context.hasHint(Hint.DENSE)) return -1.0;
        
        double base = getPriority();
        if (context.hasHint(Hint.SPARSE)) {
            base += 50.0; // MPFR loves sparse
        }
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
            base += 100.0; // MPFR is the king of precision
        }
        return base;
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        Matrix<E> res = multiply(a, b.toMatrix());
        int m = res.rows();
        
        org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E>(m, (E) a.getScalarRing().zero());
        for (int i = 0; i < m; i++) {
            E val = res.get(i, 0);
            if (!val.equals((E) a.getScalarRing().zero())) {
                storage.set(i, val);
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<E>(storage, this, a.getScalarRing());
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " is not available.");
        if (!(a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix)) {
            a = toSparse(a);
        }
        
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = 
            (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) a;
        
        int n = b.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;

        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined()) {
            if (n == 1) {
                // SpMV
                return spmv(sa, b, arena, prec, isComplex);
            }
            // General Sparse-Dense Multiply
            return spmm(sa, b, arena, prec, isComplex);
        } catch (Throwable t) {
            throw new RuntimeException("MPFR Sparse multiply failed", t);
        }
    }

    private Matrix<E> spmv(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> a, Matrix<E> x, Arena arena, long prec, boolean isComplex) throws Throwable {
        int m = a.rows();
        int[] rowPtr = a.getRowPointers();
        int[] colIdx = a.getColIndices();
        Object[] vals = a.getValues();

        MemorySegment h_x = initVector(x, arena, prec, isComplex);
        MemorySegment h_y = allocateVector(m, arena, prec, isComplex);

        MemorySegment termR = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, termR, prec);
        MemorySegment termI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
        if (isComplex) NativeSafe.invoke(MPFR_INIT2, termI, prec);
        
        MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, t1, prec);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, t2, prec);

        for (int i = 0; i < m; i++) {
            if (isComplex) {
                MemorySegment sumR = getMPFR(h_y, i, 0, true);
                MemorySegment sumI = getMPFR(h_y, i, 1, true);
                NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, sumI, arena.allocateFrom("0"), 10, 0);

                for (int idx = rowPtr[i]; idx < rowPtr[i+1]; idx++) {
                    int col = colIdx[idx];
                    org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) vals[idx];
                    
                    MemorySegment xr = getMPFR(h_x, col, 0, true);
                    MemorySegment xi = getMPFR(h_x, col, 1, true);
                    
                    // Complex multiplication: (cv.R + i*cv.I) * (xr + i*xi)
                    // Real part: cv.R*xr - cv.I*xi
                    setMPFR(t1, (E) cv.getReal(), arena, 0);
                    NativeSafe.invoke(MPFR_MUL, t1, t1, xr, 0);
                    setMPFR(t2, (E) cv.getImaginary(), arena, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, t2, xi, 0);
                    NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                    
                    // Imag part: cv.R*xi + cv.I*xr
                    setMPFR(t1, (E) cv.getReal(), arena, 0);
                    NativeSafe.invoke(MPFR_MUL, t1, t1, xi, 0);
                    setMPFR(t2, (E) cv.getImaginary(), arena, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, t2, xr, 0);
                    NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumI, sumI, t1, 0);
                }
            } else {
                MemorySegment sum = getMPFR(h_y, i, 0, false);
                NativeSafe.invoke(MPFR_SET_STR, sum, arena.allocateFrom("0"), 10, 0);

                for (int idx = rowPtr[i]; idx < rowPtr[i+1]; idx++) {
                    int col = colIdx[idx];
                    setMPFR(termR, (E) vals[idx], arena, 0);
                    
                    NativeSafe.invoke(MPFR_MUL, termR, termR, getMPFR(h_x, col, 0, false), 0);
                    NativeSafe.invoke(MPFR_ADD, sum, sum, termR, 0);
                }
            }
        }

        NativeSafe.invoke(MPFR_CLEAR, termR);
        if (isComplex) NativeSafe.invoke(MPFR_CLEAR, termI);
        NativeSafe.invoke(MPFR_CLEAR, t1);
        NativeSafe.invoke(MPFR_CLEAR, t2);

        return backToSparseMatrix(h_y, m, 1, arena, a.getScalarRing(), isComplex);
    }

    private Matrix<E> spmm(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> a, Matrix<E> b, Arena arena, long prec, boolean isComplex) throws Throwable {
        int m = a.rows();
        int n = b.cols();
        
        // Collect results in a SparseMatrixStorage
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(m, n, (E) a.getScalarRing().zero());
        
        for (int j = 0; j < n; j++) {
            Vector<E> colB = b.getColumn(j);
            Matrix<E> resCol = multiply(a, colB.toMatrix());
            
            for (int i = 0; i < m; i++) {
                E val = resCol.get(i, 0);
                if (!val.equals((E) a.getScalarRing().zero())) {
                    storage.set(i, j, val);
                }
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>(storage, a.getScalarRing());
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " is not available.");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        int rows = sa.rows();
        int cols = sa.cols();
        boolean isComplex = ((Object)sa.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(rows, cols, (E) sa.getScalarRing().zero());
        
        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        Object[] vals = sa.getValues();

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment sR = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, sR, prec);
            MemorySegment sI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, sI, prec);
            
            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex sc = (org.episteme.core.mathematics.numbers.complex.Complex) scalar;
                setMPFR(sR, (E) sc.getReal(), arena, 0);
                setMPFR(sI, (E) sc.getImaginary(), arena, 0);
            } else {
                setMPFR(sR, scalar, arena, 0);
            }

            MemorySegment vR = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, vR, prec);
            MemorySegment vI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, vI, prec);

            MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t1, prec);
            MemorySegment t2 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t2, prec);
            MemorySegment resR = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, resR, prec);
            MemorySegment resI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, resI, prec);

            MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);

            for (int i = 0; i < rows; i++) {
                for (int idx = rowPtr[i]; idx < rowPtr[i+1]; idx++) {
                    if (isComplex) {
                        org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) vals[idx];
                        setMPFR(vR, (E) cv.getReal(), arena, 0);
                        setMPFR(vI, (E) cv.getImaginary(), arena, 0);
                        
                        // resR = scR*vR - scI*vI
                        NativeSafe.invoke(MPFR_MUL, t1, sR, vR, 0);
                        NativeSafe.invoke(MPFR_MUL, t2, sI, vI, 0);
                        NativeSafe.invoke(MPFR_SUB, resR, t1, t2, 0);
                        
                        // resI = scR*vI + scI*vR
                        NativeSafe.invoke(MPFR_MUL, t1, sR, vI, 0);
                        NativeSafe.invoke(MPFR_MUL, t2, sI, vR, 0);
                        NativeSafe.invoke(MPFR_ADD, resI, t1, t2, 0);
                        
                        Real r = readMPFR(resR, expPtr, arena);
                        Real im = readMPFR(resI, expPtr, arena);
                        storage.set(i, colIdx[idx], (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, im));
                    } else {
                        setMPFR(vR, (E) vals[idx], arena, 0);
                        NativeSafe.invoke(MPFR_MUL, resR, sR, vR, 0);
                        Real r = readMPFR(resR, expPtr, arena);
                        storage.set(i, colIdx[idx], (E) r);
                    }
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("MPFR Sparse scale failed", t);
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>(storage, sa.getScalarRing());
    }

    private long getPrecision() {
        int digits = org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext().getPrecision();
        if (digits <= 0) digits = 50; 
        return (long) (digits * 3.322) + 1;
    }

    private MemorySegment allocateVector(int n, Arena arena, long prec, boolean isComplex) throws Throwable {
        int multiplier = isComplex ? 2 : 1;
        MemorySegment vec = arena.allocate(MPFR_LAYOUT, (long) n * multiplier);
        for (int i = 0; i < n * multiplier; i++) {
            NativeSafe.invoke(MPFR_INIT2, vec.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), prec);
        }
        return vec;
    }

    private MemorySegment initVector(Matrix<E> v, Arena arena, long prec, boolean isComplex) throws Throwable {
        int n = v.rows(); // Vector as col-matrix
        MemorySegment vec = allocateVector(n, arena, prec, isComplex);
        for (int i = 0; i < n; i++) {
            E val = v.get(i, 0);
            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) val;
                setMPFR(getMPFR(vec, i, 0, true), (E) cv.getReal(), arena, 0);
                setMPFR(getMPFR(vec, i, 1, true), (E) cv.getImaginary(), arena, 0);
            } else {
                setMPFR(getMPFR(vec, i, 0, false), val, arena, 0);
            }
        }
        return vec;
    }

    private void setMPFR(MemorySegment mpfr, E val, Arena arena, int rnd) throws Throwable {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealDouble rd) {
            NativeSafe.invoke(MPFR_SET_D, mpfr, rd.doubleValue(), rnd);
        } else if (val instanceof Real rv) {
            NativeSafe.invoke(MPFR_SET_STR, mpfr, arena.allocateFrom(rv.bigDecimalValue().toPlainString()), 10, rnd);
        }
    }

    private MemorySegment getMPFR(MemorySegment vec, int idx, int component, boolean isComplex) {
        int stride = isComplex ? 2 : 1;
        return vec.asSlice((long) (idx * stride + component) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
    }

    private Matrix<E> backToSparseMatrix(MemorySegment vec, int rows, int cols, Arena arena, org.episteme.core.mathematics.structures.rings.Ring<E> ring, boolean isComplex) throws Throwable {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(rows, cols, ring.zero());
        
        MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
        for (int i = 0; i < rows * cols; i++) {
            if (isComplex) {
                Real r = readMPFR(getMPFR(vec, i, 0, true), expPtr, arena);
                Real im = readMPFR(getMPFR(vec, i, 1, true), expPtr, arena);
                if (!r.equals(Real.ZERO) || !im.equals(Real.ZERO)) {
                    storage.set(i / cols, i % cols, (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, im));
                }
            } else {
                Real val = readMPFR(getMPFR(vec, i, 0, false), expPtr, arena);
                if (!val.equals(Real.ZERO)) {
                    storage.set(i / cols, i % cols, (E) val);
                }
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>(storage, ring);
    }

    private Real readMPFR(MemorySegment val, MemorySegment expPtr, Arena arena) throws Throwable {
        MemorySegment strPtr = (MemorySegment) NativeSafe.invoke(MPFR_GET_STR, MemorySegment.NULL, expPtr, 10, 0L, val, 0);
        if (strPtr == null || strPtr.equals(MemorySegment.NULL)) {
            return Real.ZERO;
        }
        
        String s = NativeSafe.scavenge(strPtr, 1024, arena, "mpfr_get_str").segment().getString(0);
        // mpfr_exp_t is long, but on Windows long is 32-bit.
        long exp = expPtr.get(ValueLayout.JAVA_INT, 0); 
        
        if (s.isEmpty() || s.equals("0")) {
             NativeSafe.invoke(MPFR_FREE_STR, strPtr);
             return Real.ZERO;
        }

        // Special handling for MPFR NaN/Inf
        String su = s.toUpperCase();
        if (su.equals("NAN") || su.contains("@NAN@")) {
            NativeSafe.invoke(MPFR_FREE_STR, strPtr);
            return Real.NaN;
        }
        if (su.equals("INF") || su.contains("@INF@") || su.contains("INFINITY")) {
            boolean neg = s.startsWith("-");
            NativeSafe.invoke(MPFR_FREE_STR, strPtr);
            return neg ? Real.NEGATIVE_INFINITY : Real.POSITIVE_INFINITY;
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

    private org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> toSparse(Matrix<E> m) {
        if (m instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) {
            return (org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>) m;
        }
        int rows = m.rows();
        int cols = m.cols();
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(rows, cols, (E) m.getScalarRing().zero());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                E val = m.get(i, j);
                if (!val.equals((E) m.getScalarRing().zero())) {
                    storage.set(i, j, val);
                }
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>(storage, m.getScalarRing());
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + " is not available.");
        
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        int n = sa.rows();
        long prec = getPrecision();

        try (Arena arena = Arena.ofConfined()) {
            boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
            MemorySegment h_b = initVector(b.toMatrix(), arena, prec, isComplex);
            MemorySegment h_x = (x0 != null) ? initVector(x0.toMatrix(), arena, prec, isComplex) : allocateVector(n, arena, prec, isComplex);
            
            MemorySegment h_r = allocateVector(n, arena, prec, isComplex);
            MemorySegment h_p = allocateVector(n, arena, prec, isComplex);
            MemorySegment h_Ap = allocateVector(n, arena, prec, isComplex);
            
            int resSize = isComplex ? 2 : 1;
            MemorySegment alpha = arena.allocate(MPFR_LAYOUT);
            MemorySegment beta = arena.allocate(MPFR_LAYOUT);
            MemorySegment rDotR = arena.allocate(MPFR_LAYOUT, resSize);
            MemorySegment rNextDotRNext = arena.allocate(MPFR_LAYOUT, resSize);
            MemorySegment pDotAp = arena.allocate(MPFR_LAYOUT, resSize);
            MemorySegment tol = arena.allocate(MPFR_LAYOUT);
            MemorySegment temp = arena.allocate(MPFR_LAYOUT);

            NativeSafe.invoke(MPFR_INIT2, alpha, prec);
            NativeSafe.invoke(MPFR_INIT2, beta, prec);
            for (int i=0; i<resSize; i++) {
                NativeSafe.invoke(MPFR_INIT2, rDotR.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), prec);
                NativeSafe.invoke(MPFR_INIT2, rNextDotRNext.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), prec);
                NativeSafe.invoke(MPFR_INIT2, pDotAp.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), prec);
            }
            NativeSafe.invoke(MPFR_INIT2, tol, prec);
            NativeSafe.invoke(MPFR_INIT2, temp, prec);

            String tolStr = (tolerance instanceof Real r) ? r.bigDecimalValue().toPlainString() : tolerance.toString();
            NativeSafe.invoke(MPFR_SET_STR, tol, arena.allocateFrom(tolStr), 10, 0);

            // r = b - A*x
            spmv_internal(sa, h_x, h_r, arena, prec, isComplex); // r = A*x
            int multiplier = isComplex ? 2 : 1;
            for (int i = 0; i < n * multiplier; i++) {
                NativeSafe.invoke(MPFR_SUB, getMPFR(h_r, i, 0, false), getMPFR(h_b, i, 0, false), getMPFR(h_r, i, 0, false), 0);
            }
            
            // p = r
            copyVector(h_p, h_r, n, isComplex);

            // rDotR = r . r
            dotProduct(h_r, h_r, n, rDotR, prec, arena, isComplex);

            int iter = 0;
            while (iter < maxIterations) {
                // Check convergence: sqrt(rDotR) < tolerance
                NativeSafe.invoke(MPFR_SQRT, temp, rDotR, 0);
                if ((int) NativeSafe.invoke(MPFR_CMP, temp, tol) < 0) break;

                // Ap = A * p
                spmv_internal(sa, h_p, h_Ap, arena, prec, isComplex);

                // alpha = rDotR / (p . Ap)
                dotProduct(h_p, h_Ap, n, pDotAp, prec, arena, isComplex);
                NativeSafe.invoke(MPFR_DIV, alpha, rDotR, pDotAp, 0);

                // x = x + alpha * p
                // r = r - alpha * Ap
                for (int i = 0; i < n * multiplier; i++) {
                    // x_i = x_i + alpha * p_i
                    NativeSafe.invoke(MPFR_MUL, temp, alpha, getMPFR(h_p, i, 0, false), 0);
                    NativeSafe.invoke(MPFR_ADD, getMPFR(h_x, i, 0, false), getMPFR(h_x, i, 0, false), temp, 0);
                    
                    // r_i = r_i - alpha * Ap_i
                    NativeSafe.invoke(MPFR_MUL, temp, alpha, getMPFR(h_Ap, i, 0, false), 0);
                    NativeSafe.invoke(MPFR_SUB, getMPFR(h_r, i, 0, false), getMPFR(h_r, i, 0, false), temp, 0);
                }

                // rNextDotRNext = r . r
                dotProduct(h_r, h_r, n, rNextDotRNext, prec, arena, isComplex);

                // beta = rNextDotRNext / rDotR
                NativeSafe.invoke(MPFR_DIV, beta, rNextDotRNext, rDotR, 0);

                // p = r + beta * p
                for (int i = 0; i < n * multiplier; i++) {
                    NativeSafe.invoke(MPFR_MUL, temp, beta, getMPFR(h_p, i, 0, false), 0);
                    NativeSafe.invoke(MPFR_ADD, getMPFR(h_p, i, 0, false), getMPFR(h_r, i, 0, false), temp, 0);
                }

                // rDotR = rNextDotRNext
                NativeSafe.invoke(MPFR_SET, rDotR, rNextDotRNext, 0);

                iter++;
            }

            if (iter >= maxIterations) {
                logger.warn("Conjugate Gradient did not converge after {} iterations.", maxIterations);
            } else {
                logger.info("Conjugate Gradient converged in {} iterations.", iter);
            }

            Matrix<E> resMatrix = backToSparseMatrix(h_x, n, 1, arena, a.getScalarRing(), isComplex);
            
            // Cleanup
            NativeSafe.invoke(MPFR_CLEAR, alpha);
            NativeSafe.invoke(MPFR_CLEAR, beta);
            NativeSafe.invoke(MPFR_CLEAR, rDotR);
            NativeSafe.invoke(MPFR_CLEAR, rNextDotRNext);
            NativeSafe.invoke(MPFR_CLEAR, pDotAp);
            NativeSafe.invoke(MPFR_CLEAR, tol);
            NativeSafe.invoke(MPFR_CLEAR, temp);

            int m = resMatrix.rows();
            org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E> storage = 
                new org.episteme.core.mathematics.linearalgebra.vectors.storage.SparseVectorStorage<E>(m, (E) a.getScalarRing().zero());
            for (int i = 0; i < m; i++) {
                E val = resMatrix.get(i, 0);
                if (!val.equals((E) a.getScalarRing().zero())) {
                    storage.set(i, val);
                }
            }
            return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<E>(storage, this, a.getScalarRing());

        } catch (Throwable t) {
            throw new RuntimeException("MPFR Conjugate Gradient failed", t);
        }
    }

    private void spmv_internal(org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> a, MemorySegment h_x, MemorySegment h_y, Arena arena, long prec, boolean isComplex) throws Throwable {
        int m = a.rows();
        int[] rowPtr = a.getRowPointers();
        int[] colIdx = a.getColIndices();
        Object[] vals = a.getValues();

        MemorySegment termR = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, termR, prec);
        MemorySegment termI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
        if (isComplex) NativeSafe.invoke(MPFR_INIT2, termI, prec);
        
        MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, t1, prec);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, t2, prec);

        for (int i = 0; i < m; i++) {
            if (isComplex) {
                MemorySegment sumR = getMPFR(h_y, i, 0, true);
                MemorySegment sumI = getMPFR(h_y, i, 1, true);
                NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, sumI, arena.allocateFrom("0"), 10, 0);

                for (int idx = rowPtr[i]; idx < rowPtr[i+1]; idx++) {
                    int col = colIdx[idx];
                    org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) vals[idx];
                    
                    MemorySegment xr = getMPFR(h_x, col, 0, true);
                    MemorySegment xi = getMPFR(h_x, col, 1, true);
                    
                    setMPFR(t1, (E) cv.getReal(), arena, 0);
                    NativeSafe.invoke(MPFR_MUL, t1, t1, xr, 0);
                    setMPFR(t2, (E) cv.getImaginary(), arena, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, t2, xi, 0);
                    NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                    
                    setMPFR(t1, (E) cv.getReal(), arena, 0);
                    NativeSafe.invoke(MPFR_MUL, t1, t1, xi, 0);
                    setMPFR(t2, (E) cv.getImaginary(), arena, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, t2, xr, 0);
                    NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumI, sumI, t1, 0);
                }
            } else {
                MemorySegment sum = getMPFR(h_y, i, 0, false);
                NativeSafe.invoke(MPFR_SET_STR, sum, arena.allocateFrom("0"), 10, 0);

                for (int idx = rowPtr[i]; idx < rowPtr[i+1]; idx++) {
                    int col = colIdx[idx];
                    setMPFR(termR, (E) vals[idx], arena, 0);
                    NativeSafe.invoke(MPFR_MUL, termR, termR, getMPFR(h_x, col, 0, false), 0);
                    NativeSafe.invoke(MPFR_ADD, sum, sum, termR, 0);
                }
            }
        }
        NativeSafe.invoke(MPFR_CLEAR, termR);
        if (isComplex) NativeSafe.invoke(MPFR_CLEAR, termI);
        NativeSafe.invoke(MPFR_CLEAR, t1);
        NativeSafe.invoke(MPFR_CLEAR, t2);
    }

    private void dotProduct(MemorySegment v1, MemorySegment v2, int n, MemorySegment result, long prec, Arena arena, boolean isComplex) throws Throwable {
        if (isComplex) {
            MemorySegment sumR = result.asSlice(0, MPFR_LAYOUT.byteSize());
            MemorySegment sumI = result.asSlice(MPFR_LAYOUT.byteSize(), MPFR_LAYOUT.byteSize());
            NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, 0);
            NativeSafe.invoke(MPFR_SET_STR, sumI, arena.allocateFrom("0"), 10, 0);

            MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t1, prec);
            MemorySegment t2 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t2, prec);

            for (int i = 0; i < n; i++) {
                MemorySegment aR = getMPFR(v1, i, 0, true);
                MemorySegment aI = getMPFR(v1, i, 1, true);
                MemorySegment bR = getMPFR(v2, i, 0, true);
                MemorySegment bI = getMPFR(v2, i, 1, true);

                // Real part: aR*bR + aI*bI (since conjugate(a) = aR - i*aI, (aR - i*aI)*(bR + i*bI) = (aR*bR + aI*bI) + i(aR*bI - aI*bR))
                NativeSafe.invoke(MPFR_MUL, t1, aR, bR, 0);
                NativeSafe.invoke(MPFR_MUL, t2, aI, bI, 0);
                NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);

                // Imag part: aR*bI - aI*bR
                NativeSafe.invoke(MPFR_MUL, t1, aR, bI, 0);
                NativeSafe.invoke(MPFR_MUL, t2, aI, bR, 0);
                NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                NativeSafe.invoke(MPFR_ADD, sumI, sumI, t1, 0);
            }
            NativeSafe.invoke(MPFR_CLEAR, t1);
            NativeSafe.invoke(MPFR_CLEAR, t2);
            return;
        }
        
        NativeSafe.invoke(MPFR_SET_STR, result, arena.allocateFrom("0"), 10, 0);
        MemorySegment term = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, term, prec);
        
        for (int i = 0; i < n; i++) {
            NativeSafe.invoke(MPFR_MUL, term, getMPFR(v1, i, 0, false), getMPFR(v2, i, 0, false), 0);
            NativeSafe.invoke(MPFR_ADD, result, result, term, 0);
        }
        NativeSafe.invoke(MPFR_CLEAR, term);
    }

    private void copyVector(MemorySegment dest, MemorySegment src, int n, boolean isComplex) throws Throwable {
        int multiplier = isComplex ? 2 : 1;
        for (int i = 0; i < n * multiplier; i++) {
            NativeSafe.invoke(MPFR_SET, getMPFR(dest, i, 0, false), getMPFR(src, i, 0, false), 0);
        }
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (a.rows() != b.rows() || a.cols() != b.cols()) throw new IllegalArgumentException("Dimension mismatch");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sb = toSparse(b);
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<>(sa.rows(), sa.cols(), (E) sa.getScalarRing().zero());
        
        // Simple merge addition for now
        java.util.Set<Integer> rows = new java.util.HashSet<>();
        for (int i=0; i<sa.rows(); i++) rows.add(i);
        for (int i=0; i<sb.rows(); i++) rows.add(i);

        for (int i : rows) {
            java.util.Map<Integer, E> rowMap = new java.util.HashMap<>();
            int[] rpa = sa.getRowPointers();
            int[] cia = sa.getColIndices();
            Object[] va = sa.getValues();
            for (int k=rpa[i]; k < rpa[i+1]; k++) rowMap.put(cia[k], (E) va[k]);

            int[] rpb = sb.getRowPointers();
            int[] cib = sb.getColIndices();
            Object[] vb = sb.getValues();
            org.episteme.core.mathematics.structures.rings.Ring<E> ring = (org.episteme.core.mathematics.structures.rings.Ring<E>) sa.getScalarRing();
            for (int k=rpb[i]; k < rpb[i+1]; k++) {
                rowMap.merge(cib[k], (E) vb[k], (v1, v2) -> ring.add(v1, v2));
            }
            for (java.util.Map.Entry<Integer, E> entry : rowMap.entrySet()) {
                if (!entry.getValue().equals(ring.zero())) {
                    storage.set(i, entry.getKey(), entry.getValue());
                }
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<>(storage, sa.getScalarRing());
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        org.episteme.core.mathematics.structures.rings.Ring<E> ring = (org.episteme.core.mathematics.structures.rings.Ring<E>) a.getScalarRing();
        return add(a, scale(ring.negate(ring.one()), b));
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        long prec = getPrecision();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment v1 = initVector(a.toMatrix(), arena, prec, isComplex);
            MemorySegment v2 = initVector(b.toMatrix(), arena, prec, isComplex);
            
            int resSize = isComplex ? 2 : 1;
            MemorySegment res = arena.allocate(MPFR_LAYOUT, resSize);
            for (int i=0; i<resSize; i++) NativeSafe.invoke(MPFR_INIT2, res.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), prec);
            
            dotProduct(v1, v2, a.dimension(), res, prec, arena, isComplex);
            
            MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
            if (isComplex) {
                Real r = readMPFR(res.asSlice(0, MPFR_LAYOUT.byteSize()), expPtr, arena);
                Real im = readMPFR(res.asSlice(MPFR_LAYOUT.byteSize(), MPFR_LAYOUT.byteSize()), expPtr, arena);
                return (E) (Object) Complex.of(r, im);
            } else {
                return (E) (Object) readMPFR(res, expPtr, arena);
            }
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR dot failed", t);
        }
    }

    @Override
    public E norm(Vector<E> a) {
        long prec = getPrecision();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment v1 = initVector(a.toMatrix(), arena, prec, isComplex);
            int resSize = isComplex ? 2 : 1;
            MemorySegment res = arena.allocate(MPFR_LAYOUT, resSize);
            for (int i = 0; i < resSize; i++) {
                NativeSafe.invoke(MPFR_INIT2, res.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT), prec);
            }
            dotProduct(v1, v1, a.dimension(), res, prec, arena, isComplex); // sum squares
            
            // Result is real for a.dot(a)
            MemorySegment realRes = res.asSlice(0, MPFR_LAYOUT.byteSize());
            NativeSafe.invoke(MPFR_SQRT, realRes, realRes, 0);
            return (E) (Object) readMPFR(realRes, arena.allocate(ValueLayout.JAVA_LONG), arena);
        } catch (Throwable t) {
            throw new RuntimeException("Sparse MPFR norm failed", t);
        }
    }

    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        return GenericSparseSolvers.bicgstab(this, a, b, x0, tolerance, maxIterations, (Field<E>) a.getScalarRing());
    }

    @Override
    public Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        return GenericSparseSolvers.gmres(this, a, b, x0, tolerance, maxIterations, restarts, (Field<E>) a.getScalarRing());
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        if (!isAvailable()) throw new UnsupportedOperationException(getName() + ": MPFR Sparse transpose not available");
        org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E> sa = toSparse(a);
        int rows = sa.rows();
        int cols = sa.cols();
        
        org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E> newStorage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage<E>(cols, rows, (E) sa.getScalarRing().zero());
        
        int[] rowPtr = sa.getRowPointers();
        int[] colIdx = sa.getColIndices();
        Object[] values = sa.getValues();
        
        for (int i = 0; i < rows; i++) {
            for (int k = rowPtr[i]; k < rowPtr[i+1]; k++) {
                newStorage.set(colIdx[k], i, (E) values[k]);
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix<E>(newStorage, (org.episteme.core.mathematics.structures.rings.Ring<E>) sa.getScalarRing());
    }
}
