/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.linearalgebra.backends;

import com.google.auto.service.AutoService;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import org.episteme.core.mathematics.structures.rings.Field;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.backends.LinearAlgebraBackend;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.real.RealBig;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * High-performance Arbitrary Precision Linear Algebra backend using libmpfr.
 * Binds directly to MPFR via Project Panama (FFM).
 */
@AutoService({Backend.class, ComputeBackend.class, NativeBackend.class, LinearAlgebraProvider.class, LinearAlgebraBackend.class, CPUBackend.class})
@SuppressWarnings("unchecked")
public class NativeMPFRDenseLinearAlgebraBackend<E> implements LinearAlgebraBackend<E>, NativeBackend, CPUBackend {

    private static final Logger logger = LoggerFactory.getLogger("org.episteme.core.mathematics.NativeDiagnostics");
    private static final Linker LINKER = Linker.nativeLinker();
    private static boolean AVAILABLE = false;

    // MPFR Handles
    public static MethodHandle MPFR_INIT2;
    public static MethodHandle MPFR_CLEAR;
    public static MethodHandle MPFR_SET_STR;
    public static MethodHandle MPFR_GET_STR;
    public static MethodHandle MPFR_ADD;
    public static MethodHandle MPFR_SUB;
    public static MethodHandle MPFR_MUL;
    public static MethodHandle MPFR_DIV;
    public static MethodHandle MPFR_SET;
    public static MethodHandle MPFR_CMP_ABS;
    public static MethodHandle MPFR_SET_UI;
    public static MethodHandle MPFR_CMP;
    public static MethodHandle MPFR_ZERO_P;
    public static MethodHandle MPFR_NEG;
    public static MethodHandle MPFR_SET_D;
    public static MethodHandle MPFR_EXP;
    public static MethodHandle MPFR_LOG;
    public static MethodHandle MPFR_LOG10;
    public static MethodHandle MPFR_SIN;
    public static MethodHandle MPFR_COS;
    public static MethodHandle MPFR_TAN;
    public static MethodHandle MPFR_ASIN;
    public static MethodHandle MPFR_ACOS;
    public static MethodHandle MPFR_ATAN;
    public static MethodHandle MPFR_SINH;
    public static MethodHandle MPFR_COSH;
    public static MethodHandle MPFR_TANH;
    public static MethodHandle MPFR_ASINH;
    public static MethodHandle MPFR_ACOSH;
    public static MethodHandle MPFR_ATANH;
    public static MethodHandle MPFR_SQRT;
    public static MethodHandle MPFR_CBRT;
    public static MethodHandle MPFR_POW;
    public static MethodHandle MPFR_CONST_PI;
    public static MethodHandle MPFR_ATAN2;

    public static final StructLayout MPFR_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_INT.withName("prec"),
        ValueLayout.JAVA_INT.withName("sign"),
        ValueLayout.JAVA_INT.withName("exp"),
        MemoryLayout.paddingLayout(4),
        ValueLayout.ADDRESS.withName("d")
    );

    static {
        try {
            // Using the Arena-based version which returns Optional<SymbolLookup>
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
                MPFR_SET = lookup(mpfr, "mpfr_set", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_CMP_ABS = lookup(mpfr, "mpfr_cmpabs", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
                MPFR_SET_UI = lookup(mpfr, "mpfr_set_ui", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_INT));
                MPFR_CMP = lookup(mpfr, "mpfr_cmp", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
                MPFR_ZERO_P = lookup(mpfr, "mpfr_zero_p", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
                MPFR_NEG = lookup(mpfr, "mpfr_neg", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_SET_D = lookup(mpfr, "mpfr_set_d", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_INT));
                MPFR_EXP = lookup(mpfr, "mpfr_exp", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_LOG = lookup(mpfr, "mpfr_log", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_LOG10 = lookup(mpfr, "mpfr_log10", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_SIN = lookup(mpfr, "mpfr_sin", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_COS = lookup(mpfr, "mpfr_cos", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_TAN = lookup(mpfr, "mpfr_tan", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ASIN = lookup(mpfr, "mpfr_asin", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ACOS = lookup(mpfr, "mpfr_acos", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ATAN = lookup(mpfr, "mpfr_atan", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_SINH = lookup(mpfr, "mpfr_sinh", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_COSH = lookup(mpfr, "mpfr_cosh", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_TANH = lookup(mpfr, "mpfr_tanh", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ASINH = lookup(mpfr, "mpfr_asinh", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ACOSH = lookup(mpfr, "mpfr_acosh", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ATANH = lookup(mpfr, "mpfr_atanh", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_SQRT = lookup(mpfr, "mpfr_sqrt", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_CBRT = lookup(mpfr, "mpfr_cbrt", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_POW = lookup(mpfr, "mpfr_pow", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_CONST_PI = lookup(mpfr, "mpfr_const_pi", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
                MPFR_ATAN2 = lookup(mpfr, "mpfr_atan2", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));

                AVAILABLE = MPFR_INIT2 != null && MPFR_ADD != null && MPFR_MUL != null && MPFR_CMP_ABS != null && MPFR_SUB != null && MPFR_SET != null && MPFR_DIV != null && MPFR_SET_UI != null && MPFR_SET_D != null;
                if (AVAILABLE) {
                    logger.info("Native MPFR Dense Backend initialized (Panama).");
                } else {
                    logger.warn("Native MPFR Dense Backend initialization partial - some handles missing:");
                    if (MPFR_INIT2 == null) logger.warn("  - mpfr_init2 NOT FOUND");
                    if (MPFR_ADD == null) logger.warn("  - mpfr_add NOT FOUND");
                    if (MPFR_MUL == null) logger.warn("  - mpfr_mul NOT FOUND");
                    if (MPFR_CMP_ABS == null) logger.warn("  - mpfr_cmpabs NOT FOUND");
                    if (MPFR_SUB == null) logger.warn("  - mpfr_sub NOT FOUND");
                    if (MPFR_SET == null) logger.warn("  - mpfr_set NOT FOUND");
                    if (MPFR_DIV == null) logger.warn("  - mpfr_div NOT FOUND");
                }
            }
        } catch (Throwable t) {
            logger.warn("Failed to initialize MPFR Backend: {}", t.getMessage());
        }
    }

    private static MethodHandle lookup(SymbolLookup lookup, String name, FunctionDescriptor desc) {
        return lookup.find(name).map(s -> LINKER.downcallHandle(s, desc)).orElse(null);
    }

    @Override public boolean isAvailable() { return AVAILABLE && !isExplicitlyDisabled(); }
    @Override public String getId() { return "native-mpfr-dense"; }
    @Override public String getName() { return "Native MPFR Dense Linear Algebra Backend"; }
    @Override public String getDescription() { return "High-performance Arbitrary Precision Linear Algebra backend using libmpfr bound via Project Panama."; }
    
    @Override
    public String getType() {
        return "linearalgebra";
    }

    @Override
    public int getPriority() {
        return 120;
    }

    @Override
    public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.CPU;
    }
    @Override public boolean isLoaded() { return AVAILABLE; }
    @Override public String getNativeLibraryName() { return "mpfr"; }
    @Override public Object createBackend() { return this; }
    @Override public String getEnvironmentInfo() { return AVAILABLE ? "CPU (Panama/MPFR)" : "N/A"; }

    @Override
    public boolean isCompatible(org.episteme.core.mathematics.structures.rings.Ring<?> ring) {
        return true; 
    }

    @Override
    public double score(OperationContext context) {
        if (!AVAILABLE) return -1.0;
        
        double base = getPriority(); // 120
        if (org.episteme.core.mathematics.context.MathContext.getCurrent().isHighPrecision()) {
            base += 1000.0; // King of precision
        }
        return base;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.ExecutionContext() {
            @Override public <T> T execute(org.episteme.core.technical.backend.Operation<T> op) { return op.compute(this); }
            @Override public void close() {}
        };
    }

    @Override public void shutdown() {}

    @Override
    public java.util.Map<String, String> getMetadata() {
        return java.util.Map.of(
            "environment", getEnvironmentInfo(),
            "precision", "full (MPFR)",
            "capabilities", "Transpose,Add,Subtract,Scale,Multiply,Inverse,Determinant,Solve,Dot,Norm,LU,QR,Cholesky,SVD,Eigen,Exp,Log,Log10,Sin,Cos,Tan,Asin,Acos,Atan,Sinh,Cosh,Tanh,Asinh,Acosh,Atanh,Sqrt,Cbrt,Pow"
        );
    }

    private boolean isComplex(Matrix<E> a) {
        return ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    private void diag(String msg) {
        logger.debug("[MPFR-DIAG] {}", msg);
    }

    private long getPrecision() {
        org.episteme.core.mathematics.context.MathContext ctx = org.episteme.core.mathematics.context.MathContext.getCurrent();
        int digits = ctx.getJavaMathContext().getPrecision();
        if (digits <= 0) digits = 256; 
        long prec = (long) (digits * 3.322) + 1;
        diag("[MPFR-DIAG] Requested Precision: " + prec + " bits (from " + digits + " digits)");
        return prec;
    }

    private Matrix<E> transcendentalOp(Matrix<E> a, MethodHandle handle) {
        if (handle == null) return null; // Let the caller decide the fallback
        int m = a.rows();
        int n = a.cols();
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, false);
            MemorySegment h_C = allocateMatrix(m, n, arena, prec, false);
            int rnd = 0; // MPFR_RNDN
            for (int i = 0; i < m * n; i++) {
                MemorySegment ra = h_A.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rc = h_C.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                NativeSafe.invoke(handle, rc, ra, rnd);
            }
            Matrix<E> res = (Matrix<E>) backToMatrix_internal(h_C, m, n, arena, a.getScalarRing(), false);
            // clear handled by arena? No, MPFR needs explicit clear for its internal allocs
            clearMPFRArray(h_A, m * n);
            clearMPFRArray(h_C, m * n);
            return res;
        } catch (Throwable t) {
            throw new RuntimeException("MPFR transcendental op failed", t);
        }
    }

    private Matrix<E> transcendentalOp2(Matrix<E> a, org.episteme.core.mathematics.numbers.real.Real exponent, MethodHandle handle) {
        if (handle == null) return LinearAlgebraBackend.super.pow(a, (E)exponent);
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        if (isComplex) return LinearAlgebraBackend.super.pow(a, (E)exponent);
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, false);
            MemorySegment h_C = allocateMatrix(m, n, arena, prec, false);
            MemorySegment h_Exp = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, h_Exp, prec);
            NativeSafe.invoke(MPFR_SET_STR, h_Exp, arena.allocateFrom(exponent.bigDecimalValue().toPlainString()), 10, 0);
            
            int rnd = 0;
            for (int i = 0; i < m * n; i++) {
                MemorySegment ra = h_A.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rc = h_C.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                NativeSafe.invoke(handle, rc, ra, h_Exp, rnd);
            }
            Matrix<E> res = (Matrix<E>) backToMatrix_internal(h_C, m, n, arena, a.getScalarRing(), false);
            NativeSafe.invoke(MPFR_CLEAR, h_Exp);
            clearMPFRArray(h_A, m * n);
            clearMPFRArray(h_C, m * n);
            return res;
        } catch (Throwable t) {
            throw new RuntimeException("MPFR transcendental op2 failed", t);
        }
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initVector(a, arena, prec, isComplex);
            MemorySegment h_B = initVector(b, arena, prec, isComplex);
            MemorySegment sumR = arena.allocate(MPFR_LAYOUT);
            MemorySegment sumI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            NativeSafe.invoke(MPFR_INIT2, sumR, prec);
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, sumI, prec);
            NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, 0);
            if (isComplex) NativeSafe.invoke(MPFR_SET_STR, sumI, arena.allocateFrom("0"), 10, 0);

            MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
            MemorySegment t2 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t1, prec);
            NativeSafe.invoke(MPFR_INIT2, t2, prec);

            int n = a.dimension();
            for (int i = 0; i < n; i++) {
                if (isComplex) {
                    MemorySegment ar = getMPFRVector(h_A, i, 0, true);
                    MemorySegment ai = getMPFRVector(h_A, i, 1, true);
                    MemorySegment br = getMPFRVector(h_B, i, 0, true);
                    MemorySegment bi = getMPFRVector(h_B, i, 1, true);
                    // Hermitian dot: sum(a_i^H * b_i) = sum((ar - i*ai) * (br + i*bi))
                    // Real part: ar*br + ai*bi
                    // Imag part: ar*bi - ai*br
                    NativeSafe.invoke(MPFR_MUL, t1, ar, br, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, ai, bi, 0);
                    NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);

                    NativeSafe.invoke(MPFR_MUL, t1, ar, bi, 0);
                    NativeSafe.invoke(MPFR_MUL, t2, ai, br, 0);
                    NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
                    NativeSafe.invoke(MPFR_ADD, sumI, sumI, t1, 0);
                } else {
                    NativeSafe.invoke(MPFR_MUL, t1, getMPFRVector(h_A, i, 0, false), getMPFRVector(h_B, i, 0, false), 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                }
            }
            MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
            if (isComplex) {
                Real r = readMPFR(sumR, expPtr, arena);
                Real im = readMPFR(sumI, expPtr, arena);
                return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, im);
            } else {
                return (E) (Object) readMPFR(sumR, expPtr, arena);
            }
        } catch (Throwable t) {
            throw new RuntimeException("MPFR dot failed", t);
        }
    }

    @Override
    public E norm(Vector<E> a) {
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initVector(a, arena, prec, isComplex);
            MemorySegment sumR = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, sumR, prec);
            NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, 0);

            MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, t1, prec);

            int n = a.dimension();
            for (int i = 0; i < n; i++) {
                if (isComplex) {
                    MemorySegment ar = getMPFRVector(h_A, i, 0, true);
                    MemorySegment ai = getMPFRVector(h_A, i, 1, true);
                    NativeSafe.invoke(MPFR_MUL, t1, ar, ar, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                    NativeSafe.invoke(MPFR_MUL, t1, ai, ai, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                } else {
                    MemorySegment ar = getMPFRVector(h_A, i, 0, false);
                    NativeSafe.invoke(MPFR_MUL, t1, ar, ar, 0);
                    NativeSafe.invoke(MPFR_ADD, sumR, sumR, t1, 0);
                }
            }
            // Add mpfr_sqrt handle if not present
            SymbolLookup mpfr = NativeFFMLoader.loadLibrary("mpfr", Arena.global()).get();
            MethodHandle sqrt = lookup(mpfr, "mpfr_sqrt", FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            NativeSafe.invoke(sqrt, sumR, sumR, 0);

            MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
            if (isComplex) {
                Real r = readMPFR(sumR, expPtr, arena);
                return (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, Real.ZERO);
            } else {
                return (E) (Object) readMPFR(sumR, expPtr, arena);
            }
        } catch (Throwable t) {
            throw new RuntimeException("MPFR norm failed", t);
        }
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        int m = a.rows();
        int k = a.cols();
        int n = b.cols();

        if (k != b.rows()) throw new IllegalArgumentException("Matrix dimensions do not match.");

        // Detect complex using the scalar ring instead of probing elements to avoid ClassCastException
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0; // MPFR_RNDN
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_B = initMatrix(b, arena, prec, isComplex);
            MemorySegment h_C = allocateMatrix(m, n, arena, prec, isComplex);

            MemorySegment temp1 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp1, prec);
            MemorySegment temp2 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp2, prec); // MPFR_RNDN
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isComplex) {
                        MemorySegment sumR = getMPFR(h_C, i, j, n, 0, true);
                        MemorySegment sumI = getMPFR(h_C, i, j, n, 1, true);
                        NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, rnd);
                        NativeSafe.invoke(MPFR_SET_STR, sumI, arena.allocateFrom("0"), 10, rnd);

                        for (int l = 0; l < k; l++) {
                            MemorySegment ar = getMPFR(h_A, i, l, k, 0, true);
                            MemorySegment ai = getMPFR(h_A, i, l, k, 1, true);
                            MemorySegment br = getMPFR(h_B, l, j, n, 0, true);
                            MemorySegment bi = getMPFR(h_B, l, j, n, 1, true);

                            // ar*br - ai*bi
                            NativeSafe.invoke(MPFR_MUL, temp1, ar, br, rnd);
                            NativeSafe.invoke(MPFR_MUL, temp2, ai, bi, rnd);
                            NativeSafe.invoke(MPFR_SUB, temp1, temp1, temp2, rnd);
                            NativeSafe.invoke(MPFR_ADD, sumR, sumR, temp1, rnd);

                            // ar*bi + ai*br
                            NativeSafe.invoke(MPFR_MUL, temp1, ar, bi, rnd);
                            NativeSafe.invoke(MPFR_MUL, temp2, ai, br, rnd);
                            NativeSafe.invoke(MPFR_ADD, temp1, temp1, temp2, rnd);
                            NativeSafe.invoke(MPFR_ADD, sumI, sumI, temp1, rnd);
                        }
                    } else {
                        MemorySegment sum = getMPFR(h_C, i, j, n, 0, false);
                        NativeSafe.invoke(MPFR_SET_STR, sum, arena.allocateFrom("0"), 10, rnd);

                        for (int l = 0; l < k; l++) {
                            MemorySegment a1 = getMPFR(h_A, i, l, k, 0, false);
                            MemorySegment b1 = getMPFR(h_B, l, j, n, 0, false);
                            NativeSafe.invoke(MPFR_MUL, temp1, a1, b1, rnd);
                            NativeSafe.invoke(MPFR_ADD, sum, sum, temp1, rnd);
                        }
                    }
                }
            }

            NativeSafe.invoke(MPFR_CLEAR, temp1);
            NativeSafe.invoke(MPFR_CLEAR, temp2);
            Matrix<E> res = (Matrix<E>) backToMatrix_internal(h_C, m, n, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, m * k * (isComplex ? 2 : 1));
            clearMPFRArray(h_B, k * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_C, m * n * (isComplex ? 2 : 1));

            return res;
        } catch (Throwable t) {
            logger.error("MPFR multiply failed: {}", t.getMessage(), t);
            throw new RuntimeException("MPFR multiply failed: " + t.getMessage(), t);
        }
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (a.cols() != b.dimension()) throw new IllegalArgumentException("Matrix-Vector dimension mismatch");
        int m = a.rows();
        int k = a.cols();
        
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        int rnd = 0; // MPFR_RNDN
        long prec = getPrecision();
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_X = initVector(b, arena, prec, isComplex);
            MemorySegment h_Y = allocateVector(m, arena, prec, isComplex);
            
            MemorySegment temp1 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp1, prec);
            MemorySegment temp2 = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, temp2, prec);
            for (int i = 0; i < m; i++) {
                if (isComplex) {
                    MemorySegment sumR = getMPFRVector(h_Y, i, 0, true);
                    MemorySegment sumI = getMPFRVector(h_Y, i, 1, true);
                    NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, rnd);
                    NativeSafe.invoke(MPFR_SET_STR, sumI, arena.allocateFrom("0"), 10, rnd);
                    
                    for (int l = 0; l < k; l++) {
                        MemorySegment ar = getMPFR(h_A, i, l, k, 0, true);
                        MemorySegment ai = getMPFR(h_A, i, l, k, 1, true);
                        MemorySegment xr = getMPFRVector(h_X, l, 0, true);
                        MemorySegment xi = getMPFRVector(h_X, l, 1, true);
                        
                        // ar*xr - ai*xi
                        NativeSafe.invoke(MPFR_MUL, temp1, ar, xr, rnd);
                        NativeSafe.invoke(MPFR_MUL, temp2, ai, xi, rnd);
                        NativeSafe.invoke(MPFR_SUB, temp1, temp1, temp2, rnd);
                        NativeSafe.invoke(MPFR_ADD, sumR, sumR, temp1, rnd);
                        
                        // ar*xi + ai*xr
                        NativeSafe.invoke(MPFR_MUL, temp1, ar, xi, rnd);
                        NativeSafe.invoke(MPFR_MUL, temp2, ai, xr, rnd);
                        NativeSafe.invoke(MPFR_ADD, temp1, temp1, temp2, rnd);
                        NativeSafe.invoke(MPFR_ADD, sumI, sumI, temp1, rnd);
                    }
                } else {
                    MemorySegment sum = getMPFRVector(h_Y, i, 0, false);
                    NativeSafe.invoke(MPFR_SET_STR, sum, arena.allocateFrom("0"), 10, rnd);
                    for (int l = 0; l < k; l++) {
                        NativeSafe.invoke(MPFR_MUL, temp1, getMPFR(h_A, i, l, k, 0, false), getMPFRVector(h_X, l, 0, false), rnd);
                        NativeSafe.invoke(MPFR_ADD, sum, sum, temp1, rnd);
                    }
                }
            }
            
            NativeSafe.invoke(MPFR_CLEAR, temp1);
            NativeSafe.invoke(MPFR_CLEAR, temp2);
            
            Vector<E> res = (Vector<E>) backToVector_internal(h_Y, m, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, m * k * (isComplex ? 2 : 1));
            clearMPFRArray(h_X, k * (isComplex ? 2 : 1));
            clearMPFRArray(h_Y, m * (isComplex ? 2 : 1));
            return res;
        } catch (Throwable t) {
            throw new RuntimeException("MPFR Matrix-Vector multiply failed", t);
        }
    }

    private MemorySegment allocateVector(int dimension, Arena arena, long prec, boolean isComplex) throws Throwable {
        int multiplier = isComplex ? 2 : 1;
        MemorySegment vec = arena.allocate(MPFR_LAYOUT, (long) dimension * multiplier);
        for (int i = 0; i < dimension * multiplier; i++) {
            MemorySegment slot = vec.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, slot, prec);
            NativeSafe.invoke(MPFR_SET_UI, slot, 0L, 0); // Crucial: Zero-init because init2 sets to NaN
        }
        return vec;
    }

    private MemorySegment initVector(Vector<E> v, Arena arena, long prec, boolean isComplex) throws Throwable {
        int dim = v.dimension();
        MemorySegment vec = allocateVector(dim, arena, prec, isComplex);
        for (int i = 0; i < dim; i++) {
            Object val = v.get(i);
            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) val;
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 0, true), arena.allocateFrom(cv.getReal().bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 1, true), arena.allocateFrom(cv.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
            } else {
                Real rv = (Real) val;
                NativeSafe.invoke(MPFR_SET_STR, getMPFRVector(vec, i, 0, false), arena.allocateFrom(rv.bigDecimalValue().toPlainString()), 10, 0);
            }
        }
        return vec;
    }

    private MemorySegment getMPFRVector(MemorySegment vec, int idx, int component, boolean isComplex) {
        int stride = isComplex ? 2 : 1;
        long index = (long) idx * stride + component;
        return vec.asSlice(index * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
    }

    private Vector<E> backToVector_internal(MemorySegment vec, int dim, Arena arena, org.episteme.core.mathematics.structures.rings.Ring<E> ring, boolean isComplex) throws Throwable {
        java.util.List<E> list = new java.util.ArrayList<E>(dim);
        MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
        for (int i = 0; i < dim; i++) {
            if (isComplex) {
                Real r = readMPFR(getMPFRVector(vec, i, 0, true), expPtr, arena);
                Real img = readMPFR(getMPFRVector(vec, i, 1, true), expPtr, arena);
                list.add((E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, img));
            } else {
                list.add((E) (Object) readMPFR(getMPFRVector(vec, i, 0, false), expPtr, arena));
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(list), (LinearAlgebraProvider<E>) this, ring);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        checkDimensionsAdd(a, b);
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_B = initMatrix(b, arena, prec, isComplex);
            MemorySegment h_C = allocateMatrix(m, n, arena, prec, isComplex);

            int multiplier = isComplex ? 2 : 1;
            for (int i = 0; i < m * n * multiplier; i++) {
                MemorySegment ra = h_A.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rb = h_B.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rc = h_C.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_ADD, rc, ra, rb, 0);
            }
            Matrix<E> res = backToMatrix_internal(h_C, m, n, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, m * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_B, m * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_C, m * n * (isComplex ? 2 : 1));
            return res;
        } catch (Throwable t) {
            throw new RuntimeException("MPFR add failed", t);
        }
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        checkDimensionsAdd(a, b);
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision(); // MPFR_RNDN

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_B = initMatrix(b, arena, prec, isComplex);
            MemorySegment h_C = allocateMatrix(m, n, arena, prec, isComplex);

            int multiplier = isComplex ? 2 : 1;
            for (int i = 0; i < m * n * multiplier; i++) {
                MemorySegment ra = h_A.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rb = h_B.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                MemorySegment rc = h_C.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_SUB, rc, ra, rb, 0);
            }
            Matrix<E> res = backToMatrix_internal(h_C, m, n, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, m * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_B, m * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_C, m * n * (isComplex ? 2 : 1));
            return res;
        } catch (Throwable t) {
            throw new RuntimeException("MPFR subtract failed", t);
        }
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
    
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_C = allocateMatrix(m, n, arena, prec, isComplex);
    
            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex cs = (org.episteme.core.mathematics.numbers.complex.Complex)(Object)scalar;
                MemorySegment sR = arena.allocate(MPFR_LAYOUT);
                MemorySegment sI = arena.allocate(MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, sR, prec);
                NativeSafe.invoke(MPFR_INIT2, sI, prec);
                NativeSafe.invoke(MPFR_SET_STR, sR, arena.allocateFrom(cs.getReal().bigDecimalValue().toPlainString()), 10, 0);
                NativeSafe.invoke(MPFR_SET_STR, sI, arena.allocateFrom(cs.getImaginary().bigDecimalValue().toPlainString()), 10, 0);
    
                for (int i = 0; i < m * n; i++) {
                    MemorySegment aR = h_A.asSlice((long) i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment aI = h_A.asSlice((long) (i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment cR = h_C.asSlice((long) i * 2 * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment cI = h_C.asSlice((long) (i * 2 + 1) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    complexMultiply(cR, cI, aR, aI, sR, sI, prec, arena);
                }
            } else {
                MemorySegment s = arena.allocate(MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, s, prec);
                String valStr = (scalar instanceof Real rs) ? rs.bigDecimalValue().toPlainString() : scalar.toString();
                NativeSafe.invoke(MPFR_SET_STR, s, arena.allocateFrom(valStr), 10, 0);
    
                for (int i = 0; i < m * n; i++) {
                    MemorySegment ra = h_A.asSlice((long) i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    MemorySegment rc = h_C.asSlice((long) i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_MUL, rc, ra, s, 0);
                }
            }
            Matrix<E> res = backToMatrix_internal(h_C, m, n, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, m * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_C, m * n * (isComplex ? 2 : 1));
            return res;
        } catch (Throwable t) {
            throw new RuntimeException("MPFR scale failed", t);
        }
    }


    private MemorySegment allocateMatrix(int rows, int cols, Arena arena, long prec, boolean isComplex) throws Throwable {
        int multiplier = isComplex ? 2 : 1;
        MemorySegment mat = arena.allocate(MPFR_LAYOUT, (long) rows * cols * multiplier);
        for (int i = 0; i < rows * cols * multiplier; i++) {
            MemorySegment slot = mat.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, slot, prec);
            NativeSafe.invoke(MPFR_SET_UI, slot, 0L, 0); // Crucial: Zero-init
        }
        return mat;
    }

    private MemorySegment initMatrix(Matrix<E> m, Arena arena, long prec, boolean isComplex) throws Throwable {
        int rows = m.rows();
        int cols = m.cols();
        MemorySegment mat = allocateMatrix(rows, cols, arena, prec, isComplex);
        MatrixStorage<?> storage = m.getStorage(); // Bypasses implicit GenericMatrix casts
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Object val = storage.get(i, j);
                if (isComplex) {
                    org.episteme.core.mathematics.numbers.complex.Complex cv = (org.episteme.core.mathematics.numbers.complex.Complex) val;
                    setMPFR(getMPFR(mat, i, j, cols, 0, true), cv.getReal(), arena, 0);
                    setMPFR(getMPFR(mat, i, j, cols, 1, true), cv.getImaginary(), arena, 0);
                } else {
                    setMPFR(getMPFR(mat, i, j, cols, 0, false), (Real) val, arena, 0);
                }
            }
        }
        
        // Debug sample
        MemorySegment sample = getMPFR(mat, 0, 0, cols, 0, false);
        MemorySegment ePtr = arena.allocate(ValueLayout.JAVA_LONG);
        Real checkVal = readMPFR(sample, ePtr, arena);
        Object expected = storage.get(0,0);
        
        boolean mismatch = true;
        if (expected instanceof Real r) {
            mismatch = checkVal.subtract(r).abs().doubleValue() > 1e-30;
        }
        
        if (mismatch) {
            diag("initMatrix verify[0,0]: expected=" + expected + ", actuallyReads=" + checkVal);
        }
        
        return mat;
    }

    private void setMPFR(MemorySegment mpfr, Real val, Arena arena, int rnd) throws Throwable {
        if (val instanceof org.episteme.core.mathematics.numbers.real.RealDouble rd) {
            NativeSafe.invoke(MPFR_SET_D, mpfr, rd.doubleValue(), rnd);
        } else {
            NativeSafe.invoke(MPFR_SET_STR, mpfr, arena.allocateFrom(val.bigDecimalValue().toPlainString()), 10, rnd);
        }
    }

    private MemorySegment getMPFR(MemorySegment mat, int row, int col, int cols, int component, boolean isComplex) {
        int stride = isComplex ? 2 : 1;
        long index = ((long) row * cols + col) * stride + component;
        return mat.asSlice(index * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        int m = a.rows();
        int n = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_C = allocateMatrix(n, m, arena, prec, isComplex);
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isComplex) {
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_C, j, i, m, 0, true), getMPFR(h_A, i, j, n, 0, true), 0);
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_C, j, i, m, 1, true), getMPFR(h_A, i, j, n, 1, true), 0);
                    } else {
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_C, j, i, m, 0, false), getMPFR(h_A, i, j, n, 0, false), 0);
                    }
                }
            }
            Matrix<E> res = backToMatrix_internal(h_C, n, m, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, m * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_C, n * m * (isComplex ? 2 : 1));
            return res;
        } catch (Throwable t) {
            logger.error("MPFR transpose failed: {}", t.getMessage(), t);
            throw new RuntimeException("MPFR transpose failed", t);
        }
    }

    private Matrix<E> backToMatrix_internal(MemorySegment mat, int rows, int cols, Arena arena, org.episteme.core.mathematics.structures.rings.Ring<E> ring, boolean isComplex) throws Throwable {
        org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E> storage = 
            new org.episteme.core.mathematics.linearalgebra.matrices.storage.DenseMatrixStorage<E>(rows, cols, (E)ring.zero());
        MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isComplex) {
                    Real r = readMPFR(getMPFR(mat, i, j, cols, 0, true), expPtr, arena);
                    Real img = readMPFR(getMPFR(mat, i, j, cols, 1, true), expPtr, arena);
                    storage.set(i, j, (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(r, img));
                } else {
                    storage.set(i, j, (E) (Object) readMPFR(getMPFR(mat, i, j, cols, 0, false), expPtr, arena));
                }
            }
        }
        return new org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>(storage, (LinearAlgebraProvider<E>) this, ring);
    }
    
    private void clearMPFRArray(MemorySegment mat, int count) {
        if (mat == MemorySegment.NULL) return;
        for (int i = 0; i < count; i++) {
            try { NativeSafe.invoke(MPFR_CLEAR, mat.asSlice(i * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT)); } catch (Throwable t) {}
        }
    }

    private Real readMPFR(MemorySegment mpfr_t, MemorySegment expPtr, Arena arena) {
        MemorySegment strPtr = (MemorySegment) NativeSafe.invoke(MPFR_GET_STR, MemorySegment.NULL, expPtr, 10, 0, mpfr_t, 0);
        if (strPtr == null || strPtr.equals(MemorySegment.NULL)) return RealBig.NaN;
        
        String digits = NativeSafe.scavenge(strPtr, 1024, arena, "mpfr_get_str").segment().getString(0);
        long exp = expPtr.get(ValueLayout.JAVA_LONG, 0);
        
        // Special handling for MPFR NaN/Inf in the string itself (rare but possible)
        String su = digits.toUpperCase();
        if (su.equals("NAN") || su.contains("@NAN@")) return RealBig.NaN;
        if (su.equals("INF") || su.contains("@INF@") || su.contains("INFINITY")) {
            return RealBig.NaN; // RealBig doesn't support Inf yet, use NaN as safe fallback for now
        }

        String sign = "";
        if (digits.startsWith("-")) {
            sign = "-";
            digits = digits.substring(1);
        }
        
        if (digits.isEmpty() || digits.equals("0")) return RealBig.of("0");
        
        // Guard against exponent overflow for BigDecimal (int range)
        // The exponent from mpfr_get_str represents where the decimal point goes:
        // digits = "12345", exp = 3 means 123.45
        // We construct: sign + "0." + digits, then adjust by exp
        // Effective exponent for BigDecimal = exp - digits.length()
        long effectiveScale = (long) digits.length() - exp;
        if (effectiveScale > Integer.MAX_VALUE || effectiveScale < Integer.MIN_VALUE) {
            // Truly extreme exponent — value is effectively 0 or infinity for our purposes
            if (exp > 0) return RealBig.NaN; // overflow  
            return RealBig.of("0"); // underflow
        }
        
        try {
            java.math.BigInteger unscaled = new java.math.BigInteger(sign + digits);
            java.math.BigDecimal raw = new java.math.BigDecimal(unscaled, (int) effectiveScale);
            // Explicitly round to the requested precision context for bit-perfect agreement
            return RealBig.create(raw.round(org.episteme.core.mathematics.context.MathContext.getCurrent().getJavaMathContext()));
        } catch (NumberFormatException e) {
            logger.warn("readMPFR: Failed to parse MPFR value (digits={}, exp={}): {}", 
                digits.length(), exp, e.getMessage());
            return RealBig.NaN;
        }
    }
    @Override
    public QRResult<E> qr(Matrix<E> a) {
        return GenericQR.decompose(a, (Field<E>) a.getScalarRing());
    }

    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        return GenericCholesky.decompose(a, (Field<E>) a.getScalarRing());
    }

    @Override
    public SVDResult<E> svd(Matrix<E> a) {
        return GenericSVD.decompose(a, (Field<E>) a.getScalarRing());
    }

    @Override
    public EigenResult<E> eigen(Matrix<E> a) {
        return GenericEigen.decompose(a, (Field<E>) a.getScalarRing());
    }

    @Override
    public LUResult<E> lu(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        int n = a.rows();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0;

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            int[] perm = new int[n];
            for (int i = 0; i < n; i++) perm[i] = i;

            MemorySegment factorR = arena.allocate(MPFR_LAYOUT);
            MemorySegment factorI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            NativeSafe.invoke(MPFR_INIT2, factorR, prec);
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, factorI, prec);

            for (int k = 0; k < n; k++) {
                int pivotRow = k;
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        if (compareComplexMagnitude(h_A, i, pivotRow, k, n, arena, prec)) pivotRow = i;
                    } else {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivotRow, k, n, 0, false)) > 0) pivotRow = i;
                    }
                }

                if (pivotRow != k) {
                    swapRows(h_A, k, pivotRow, n, isComplex, arena, prec);
                    int tmpP = perm[k];
                    perm[k] = perm[pivotRow];
                    perm[pivotRow] = tmpP;
                }

                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        complexDivide(factorR, factorI, 
                            getMPFR(h_A, i, k, n, 0, true), getMPFR(h_A, i, k, n, 1, true),
                            getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), prec, arena);
                        
                        // Store factor in L part
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_A, i, k, n, 0, true), factorR, rnd);
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_A, i, k, n, 1, true), factorI, rnd);

                        for (int j = k + 1; j < n; j++) {
                            complexSubtractMul(h_A, i, j, factorR, factorI, h_A, k, j, n, arena, prec);
                        }
                    } else {
                        NativeSafe.invoke(MPFR_DIV, factorR, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, k, k, n, 0, false), rnd);
                        NativeSafe.invoke(MPFR_SET, getMPFR(h_A, i, k, n, 0, false), factorR, rnd);
                        
                        for (int j = k + 1; j < n; j++) {
                            subtractMulReal(h_A, i, j, factorR, h_A, k, j, n, arena, prec);
                        }
                    }
                }
            }

            // Extract L and U
            MemorySegment h_L = allocateMatrix(n, n, arena, prec, isComplex);
            MemorySegment h_U = allocateMatrix(n, n, arena, prec, isComplex);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i > j) {
                        if (isComplex) {
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_L, i, j, n, 0, true), getMPFR(h_A, i, j, n, 0, true), rnd);
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_L, i, j, n, 1, true), getMPFR(h_A, i, j, n, 1, true), rnd);
                        } else {
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_L, i, j, n, 0, false), getMPFR(h_A, i, j, n, 0, false), rnd);
                        }
                    } else if (i == j) {
                        if (isComplex) {
                            NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_L, i, j, n, 0, true), 1L, rnd);
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_U, i, j, n, 0, true), getMPFR(h_A, i, j, n, 0, true), rnd);
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_U, i, j, n, 1, true), getMPFR(h_A, i, j, n, 1, true), rnd);
                        } else {
                            NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_L, i, j, n, 0, false), 1L, rnd);
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_U, i, j, n, 0, false), getMPFR(h_A, i, j, n, 0, false), rnd);
                        }
                    } else {
                        if (isComplex) {
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_U, i, j, n, 0, true), getMPFR(h_A, i, j, n, 0, true), rnd);
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_U, i, j, n, 1, true), getMPFR(h_A, i, j, n, 1, true), rnd);
                        } else {
                            NativeSafe.invoke(MPFR_SET, getMPFR(h_U, i, j, n, 0, false), getMPFR(h_A, i, j, n, 0, false), rnd);
                        }
                    }
                }
            }

            Matrix<E> lMat = backToMatrix_internal(h_L, n, n, arena, a.getScalarRing(), isComplex);
            Matrix<E> uMat = backToMatrix_internal(h_U, n, n, arena, a.getScalarRing(), isComplex);
            
            E[] pData = (E[]) java.lang.reflect.Array.newInstance(a.getScalarRing().zero().getClass(), n);
            for (int i = 0; i < n; i++) {
                if (isComplex) pData[i] = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(Real.of(perm[i]));
                else pData[i] = (E) (Object) Real.of(perm[i]);
            }
            Vector<E> pVec = new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(new org.episteme.core.mathematics.linearalgebra.vectors.storage.DenseVectorStorage<>(java.util.Arrays.asList(pData)), (LinearAlgebraProvider<E>) this, a.getScalarRing());

            NativeSafe.invoke(MPFR_CLEAR, factorR);
            if (isComplex) NativeSafe.invoke(MPFR_CLEAR, factorI);
            clearMPFRArray(h_A, n * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_L, n * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_U, n * n * (isComplex ? 2 : 1));

            return new LUResult<>(lMat, uMat, pVec);
        } catch (Throwable t) {
            logger.error("MPFR LU failed: {}", t.getMessage());
            throw new RuntimeException("MPFR LU failed", t);
        }
    }

    @Override
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        int n = lu.L().rows();
        boolean isComplex = ((Object)lu.L().getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0;

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_L = initMatrix(lu.L(), arena, prec, isComplex);
            MemorySegment h_U = initMatrix(lu.U(), arena, prec, isComplex);
            MemorySegment h_b = initVector(b, arena, prec, isComplex);
            MemorySegment h_pb = allocateVector(n, arena, prec, isComplex);
            
            Vector<E> p = lu.P();
            for (int i = 0; i < n; i++) {
                int idx = 0;
                Object pVal = p.get(i);
                if (pVal instanceof Real r) idx = (int) r.doubleValue();
                else if (pVal instanceof Number nmb) idx = nmb.intValue();
                
                if (isComplex) {
                    NativeSafe.invoke(MPFR_SET, getMPFRVector(h_pb, i, 0, true), getMPFRVector(h_b, idx, 0, true), rnd);
                    NativeSafe.invoke(MPFR_SET, getMPFRVector(h_pb, i, 1, true), getMPFRVector(h_b, idx, 1, true), rnd);
                } else {
                    NativeSafe.invoke(MPFR_SET, getMPFRVector(h_pb, i, 0, false), getMPFRVector(h_b, idx, 0, false), rnd);
                }
            }

            MemorySegment h_y = allocateVector(n, arena, prec, isComplex);
            MemorySegment sumR = arena.allocate(MPFR_LAYOUT);
            MemorySegment sumI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            NativeSafe.invoke(MPFR_INIT2, sumR, prec);
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, sumI, prec);

            // Forward Substitution Ly = Pb
            for (int i = 0; i < n; i++) {
                NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, rnd);
                if (isComplex) NativeSafe.invoke(MPFR_SET_UI, sumI, 0L, rnd);
                for (int j = 0; j < i; j++) {
                    if (isComplex) {
                        complexAddMul(sumR, sumI, getMPFR(h_L, i, j, n, 0, true), getMPFR(h_L, i, j, n, 1, true), getMPFRVector(h_y, j, 0, true), getMPFRVector(h_y, j, 1, true), prec, arena);
                    } else {
                        MemorySegment term = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, term, prec);
                        NativeSafe.invoke(MPFR_MUL, term, getMPFR(h_L, i, j, n, 0, false), getMPFRVector(h_y, j, 0, false), rnd);
                        NativeSafe.invoke(MPFR_ADD, sumR, sumR, term, rnd);
                        NativeSafe.invoke(MPFR_CLEAR, term);
                    }
                }
                if (isComplex) {
                    NativeSafe.invoke(MPFR_SUB, getMPFRVector(h_y, i, 0, true), getMPFRVector(h_pb, i, 0, true), sumR, rnd);
                    NativeSafe.invoke(MPFR_SUB, getMPFRVector(h_y, i, 1, true), getMPFRVector(h_pb, i, 1, true), sumI, rnd);
                } else {
                    NativeSafe.invoke(MPFR_SUB, getMPFRVector(h_y, i, 0, false), getMPFRVector(h_pb, i, 0, false), sumR, rnd);
                }
            }

            // Backward Substitution Ux = y
            MemorySegment h_x = allocateVector(n, arena, prec, isComplex);
            for (int i = n - 1; i >= 0; i--) {
                NativeSafe.invoke(MPFR_SET_UI, sumR, 0L, rnd);
                if (isComplex) NativeSafe.invoke(MPFR_SET_UI, sumI, 0L, rnd);
                for (int j = i + 1; j < n; j++) {
                    if (isComplex) {
                        complexAddMul(sumR, sumI, getMPFR(h_U, i, j, n, 0, true), getMPFR(h_U, i, j, n, 1, true), getMPFRVector(h_x, j, 0, true), getMPFRVector(h_x, j, 1, true), prec, arena);
                    } else {
                        MemorySegment term = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, term, prec);
                        NativeSafe.invoke(MPFR_MUL, term, getMPFR(h_U, i, j, n, 0, false), getMPFRVector(h_x, j, 0, false), rnd);
                        NativeSafe.invoke(MPFR_ADD, sumR, sumR, term, rnd);
                        NativeSafe.invoke(MPFR_CLEAR, term);
                    }
                }
                
                MemorySegment xiR = getMPFRVector(h_x, i, 0, isComplex);
                MemorySegment xiI = isComplex ? getMPFRVector(h_x, i, 1, true) : null;
                
                if (isComplex) {
                    NativeSafe.invoke(MPFR_SUB, xiR, getMPFRVector(h_y, i, 0, true), sumR, rnd);
                    NativeSafe.invoke(MPFR_SUB, xiI, getMPFRVector(h_y, i, 1, true), sumI, rnd);
                    complexDivide(xiR, xiI, xiR, xiI, getMPFR(h_U, i, i, n, 0, true), getMPFR(h_U, i, i, n, 1, true), prec, arena);
                } else {
                    NativeSafe.invoke(MPFR_SUB, xiR, getMPFRVector(h_y, i, 0, false), sumR, rnd);
                    NativeSafe.invoke(MPFR_DIV, xiR, xiR, getMPFR(h_U, i, i, n, 0, false), rnd);
                }
            }

            Vector<E> res = backToVector_internal(h_x, n, arena, lu.L().getScalarRing(), isComplex);
            NativeSafe.invoke(MPFR_CLEAR, sumR);
            if (isComplex) NativeSafe.invoke(MPFR_CLEAR, sumI);
            clearMPFRArray(h_L, n * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_U, n * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_b, n * (isComplex ? 2 : 1));
            clearMPFRArray(h_pb, n * (isComplex ? 2 : 1));
            clearMPFRArray(h_y, n * (isComplex ? 2 : 1));
            clearMPFRArray(h_x, n * (isComplex ? 2 : 1));
            return res;
        } catch (Throwable t) {
            logger.error("MPFR LU Solve failed: {}", t.getMessage());
            throw new RuntimeException("MPFR LU Solve failed", t);
        }
    }

    @Override public Matrix<E> exp(Matrix<E> a) { return executeTranscendental(a, "exp"); }
    @Override public Matrix<E> log(Matrix<E> a) { return executeTranscendental(a, "log"); }
    @Override public Matrix<E> log10(Matrix<E> a) { return executeTranscendental(a, "log10"); }
    @Override public Matrix<E> sin(Matrix<E> a) { return executeTranscendental(a, "sin"); }
    @Override public Matrix<E> cos(Matrix<E> a) { return executeTranscendental(a, "cos"); }
    @Override public Matrix<E> tan(Matrix<E> a) { return executeTranscendental(a, "tan"); }
    @Override public Matrix<E> asin(Matrix<E> a) { return executeTranscendental(a, "asin"); }
    @Override public Matrix<E> acos(Matrix<E> a) { return executeTranscendental(a, "acos"); }
    @Override public Matrix<E> atan(Matrix<E> a) { return executeTranscendental(a, "atan"); }
    @Override public Matrix<E> sinh(Matrix<E> a) { return executeTranscendental(a, "sinh"); }
    @Override public Matrix<E> cosh(Matrix<E> a) { return executeTranscendental(a, "cosh"); }
    @Override public Matrix<E> tanh(Matrix<E> a) { return executeTranscendental(a, "tanh"); }
    @Override public Matrix<E> sqrt(Matrix<E> a) { return executeTranscendental(a, "sqrt"); }
    @Override public Matrix<E> cbrt(Matrix<E> a) { return executeTranscendental(a, "cbrt"); }
    @Override public Matrix<E> asinh(Matrix<E> a) { return executeTranscendental(a, "asinh"); }
    @Override public Matrix<E> acosh(Matrix<E> a) { return executeTranscendental(a, "acosh"); }
    @Override public Matrix<E> atanh(Matrix<E> a) { return executeTranscendental(a, "atanh"); }
    @Override public Matrix<E> pow(Matrix<E> a, E exponent) { return executePow(a, exponent); }

    private Matrix<E> executeTranscendental(Matrix<E> a, String op) {
        int rows = a.rows();
        int cols = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0;

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_Res = allocateMatrix(rows, cols, arena, prec, isComplex);
            MethodHandle handle = getHandle(op);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (isComplex) {
                        MemorySegment resR = getMPFR(h_Res, i, j, cols, 0, true);
                        MemorySegment resI = getMPFR(h_Res, i, j, cols, 1, true);
                        MemorySegment aR = getMPFR(h_A, i, j, cols, 0, true);
                        MemorySegment aI = getMPFR(h_A, i, j, cols, 1, true);
                        invokeComplexOp(op, resR, resI, aR, aI, prec, arena);
                    } else if (handle != null) {
                        NativeSafe.invoke(handle, getMPFR(h_Res, i, j, cols, 0, false), getMPFR(h_A, i, j, cols, 0, false), rnd);
                    } else {
                        throw new UnsupportedOperationException("Native helper for " + op + " not found");
                    }
                }
            }
            Matrix<E> res = backToMatrix_internal(h_Res, rows, cols, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, rows * cols * (isComplex ? 2 : 1));
            clearMPFRArray(h_Res, rows * cols * (isComplex ? 2 : 1));
            return res;
        } catch (Throwable t) {
            logger.error("MPFR transcendental {} failed: {}", op, t.getMessage());
            throw new RuntimeException("MPFR transcendental " + op + " failed", t);
        }
    }

    private Matrix<E> executePow(Matrix<E> a, E exponent) {
        int rows = a.rows();
        int cols = a.cols();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0;

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_Res = allocateMatrix(rows, cols, arena, prec, isComplex);
            
            MemorySegment h_ExponentR = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, h_ExponentR, prec);
            MemorySegment h_ExponentI = isComplex ? arena.allocate(MPFR_LAYOUT) : null;
            if (isComplex) NativeSafe.invoke(MPFR_INIT2, h_ExponentI, prec);

            if (isComplex) {
                org.episteme.core.mathematics.numbers.complex.Complex ce = (org.episteme.core.mathematics.numbers.complex.Complex) exponent;
                NativeSafe.invoke(MPFR_SET_STR, h_ExponentR, arena.allocateFrom(ce.getReal().bigDecimalValue().toPlainString()), 10, rnd);
                NativeSafe.invoke(MPFR_SET_STR, h_ExponentI, arena.allocateFrom(ce.getImaginary().bigDecimalValue().toPlainString()), 10, rnd);
            } else {
                NativeSafe.invoke(MPFR_SET_STR, h_ExponentR, arena.allocateFrom(((org.episteme.core.mathematics.numbers.real.Real)exponent).bigDecimalValue().toPlainString()), 10, rnd);
            }

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (isComplex) {
                        MemorySegment resR = getMPFR(h_Res, i, j, cols, 0, true);
                        MemorySegment resI = getMPFR(h_Res, i, j, cols, 1, true);
                        MemorySegment aR = getMPFR(h_A, i, j, cols, 0, true);
                        MemorySegment aI = getMPFR(h_A, i, j, cols, 1, true);
                        
                        MemorySegment logAR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, logAR, prec);
                        MemorySegment logAI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, logAI, prec);
                        complexLog(logAR, logAI, aR, aI, prec, arena);
                        
                        MemorySegment prodR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, prodR, prec);
                        MemorySegment prodI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, prodI, prec);
                        complexMultiply(prodR, prodI, h_ExponentR, h_ExponentI, logAR, logAI, prec, arena);
                        
                        complexExp(resR, resI, prodR, prodI, prec, arena);
                        
                        NativeSafe.invoke(MPFR_CLEAR, logAR); NativeSafe.invoke(MPFR_CLEAR, logAI);
                        NativeSafe.invoke(MPFR_CLEAR, prodR); NativeSafe.invoke(MPFR_CLEAR, prodI);
                    } else {
                        NativeSafe.invoke(MPFR_POW, getMPFR(h_Res, i, j, cols, 0, false), getMPFR(h_A, i, j, cols, 0, false), h_ExponentR, rnd);
                    }
                }
            }
            Matrix<E> res = backToMatrix_internal(h_Res, rows, cols, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, rows * cols * (isComplex ? 2 : 1));
            clearMPFRArray(h_Res, rows * cols * (isComplex ? 2 : 1));
            NativeSafe.invoke(MPFR_CLEAR, h_ExponentR);
            if (isComplex) NativeSafe.invoke(MPFR_CLEAR, h_ExponentI);
            return res;
        } catch (Throwable t) {
            logger.error("MPFR pow failed: {}", t.getMessage());
            throw new RuntimeException("MPFR pow failed", t);
        }
    }

    @Override
    public Vector<E> solve(QRResult<E> qr, Vector<E> b) {
        return GenericQR.solve(qr, b, (Field<E>) b.getScalarRing());
    }

    @Override
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        return GenericCholesky.solve(cholesky, b, (Field<E>) b.getScalarRing());
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        if (a.rows() != b.dimension()) throw new IllegalArgumentException("Dimension mismatch");
        int n = a.rows();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision();
        int rnd = 0; // MPFR_RNDN

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_B = initVector(b, arena, prec, isComplex);
            
            
            MemorySegment tempFactorR = MemorySegment.NULL, tempFactorI = MemorySegment.NULL;
            MemorySegment tempFactor = MemorySegment.NULL, sumR = MemorySegment.NULL, sumI = MemorySegment.NULL;
            MemorySegment sum = MemorySegment.NULL, term = MemorySegment.NULL;
            
            if (isComplex) {
                tempFactorR = arena.allocate(MPFR_LAYOUT); tempFactorI = arena.allocate(MPFR_LAYOUT);
                sumR = arena.allocate(MPFR_LAYOUT); sumI = arena.allocate(MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, tempFactorR, prec); NativeSafe.invoke(MPFR_INIT2, tempFactorI, prec);
                NativeSafe.invoke(MPFR_INIT2, sumR, prec); NativeSafe.invoke(MPFR_INIT2, sumI, prec);
            } else {
                tempFactor = arena.allocate(MPFR_LAYOUT); sum = arena.allocate(MPFR_LAYOUT); term = arena.allocate(MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, tempFactor, prec); NativeSafe.invoke(MPFR_INIT2, sum, prec); NativeSafe.invoke(MPFR_INIT2, term, prec);
            }
            
            // Partial Pivoting GEPP
            for (int k = 0; k < n; k++) {
                int pivot = k;
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        if (compareComplexMagnitude(h_A, i, pivot, k, n, arena, prec)) {
                            pivot = i;
                        }
                    } else {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivot, k, n, 0, false)) > 0) {
                            pivot = i;
                        }
                    }
                }
                
                if (pivot != k) {
                    swapRows(h_A, k, pivot, n, isComplex, arena, prec);
                    swapRowsVector(h_B, k, pivot, isComplex, arena, prec);
                }
                
                // Gaussian Elimination
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        complexDivide(tempFactorR, tempFactorI, 
                            getMPFR(h_A, i, k, n, 0, true), getMPFR(h_A, i, k, n, 1, true),
                            getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true),
                            prec, arena);
                        
                        for (int j = k; j < n; j++) {
                            complexSubtractMul(h_A, i, j, tempFactorR, tempFactorI, h_A, k, j, n, arena, prec);
                        }
                        complexSubtractMulVector(h_B, i, tempFactorR, tempFactorI, h_B, k, arena, prec);
                    } else {
                        NativeSafe.invoke(MPFR_DIV, tempFactor, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, k, k, n, 0, false), rnd);
                        
                        for (int j = k; j < n; j++) {
                            subtractMulReal(h_A, i, j, tempFactor, h_A, k, j, n, arena, prec);
                        }
                        subtractMulVectorReal(h_B, i, tempFactor, h_B, k, arena, prec);
                    }
                }
            }
            
            // Back substitution
            MemorySegment h_X = allocateVector(n, arena, prec, isComplex);
            for (int i = n - 1; i >= 0; i--) {
                if (isComplex) {
                    NativeSafe.invoke(MPFR_SET_STR, sumR, arena.allocateFrom("0"), 10, 0);
                    NativeSafe.invoke(MPFR_SET_STR, sumI, arena.allocateFrom("0"), 10, 0);
                    
                    for (int j = i + 1; j < n; j++) {
                        complexAddMul(sumR, sumI, 
                            getMPFR(h_A, i, j, n, 0, true), getMPFR(h_A, i, j, n, 1, true),
                            getMPFRVector(h_X, j, 0, true), getMPFRVector(h_X, j, 1, true),
                            prec, arena);
                    }
                    
                    MemorySegment xiR = getMPFRVector(h_X, i, 0, true);
                    MemorySegment xiI = getMPFRVector(h_X, i, 1, true);
                    NativeSafe.invoke(MPFR_SUB, xiR, getMPFRVector(h_B, i, 0, true), sumR, 0);
                    NativeSafe.invoke(MPFR_SUB, xiI, getMPFRVector(h_B, i, 1, true), sumI, 0);
                    
                    complexDivide(xiR, xiI, xiR, xiI, 
                        getMPFR(h_A, i, i, n, 0, true), getMPFR(h_A, i, i, n, 1, true),
                        prec, arena);
                } else {
                    NativeSafe.invoke(MPFR_SET_STR, sum, arena.allocateFrom("0"), 10, 0);
                    
                    for (int j = i + 1; j < n; j++) {
                        NativeSafe.invoke(MPFR_MUL, term, getMPFR(h_A, i, j, n, 0, false), getMPFRVector(h_X, j, 0, false), 0);
                        NativeSafe.invoke(MPFR_ADD, sum, sum, term, 0);
                    }
                    
                    MemorySegment xi = getMPFRVector(h_X, i, 0, false);
                    NativeSafe.invoke(MPFR_SUB, xi, getMPFRVector(h_B, i, 0, false), sum, rnd);
                    NativeSafe.invoke(MPFR_DIV, xi, xi, getMPFR(h_A, i, i, n, 0, false), rnd);
                }
            }
            
            Vector<E> res = backToVector_internal(h_X, n, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, n * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_B, n * (isComplex ? 2 : 1));
            clearMPFRArray(h_X, n * (isComplex ? 2 : 1));
            
            if (isComplex) {
                NativeSafe.invoke(MPFR_CLEAR, tempFactorR); NativeSafe.invoke(MPFR_CLEAR, tempFactorI);
                NativeSafe.invoke(MPFR_CLEAR, sumR); NativeSafe.invoke(MPFR_CLEAR, sumI);
            } else {
                NativeSafe.invoke(MPFR_CLEAR, tempFactor); NativeSafe.invoke(MPFR_CLEAR, sum); NativeSafe.invoke(MPFR_CLEAR, term);
            }
            
            return res;
        } catch (Throwable t) {
            logger.error("MPFR solve failed: {}", t.getMessage(), t);
            throw new RuntimeException("MPFR solve failed", t);
        }
    }


    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        int n = a.rows();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision(); // MPFR_RNDN

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment h_Inv = allocateMatrix(n, n, arena, prec, isComplex);
            int rnd = 0;
            // Initialize Identity
            for (int i = 0; i < n; i++) {
                if (isComplex) {
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_Inv, i, i, n, 0, true), 1L, rnd);
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_Inv, i, i, n, 1, true), 0L, rnd);
                } else {
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_Inv, i, i, n, 0, false), 1L, rnd);
                }
            }

            // Gauss-Jordan Elimination
            for (int k = 0; k < n; k++) {
                // Pivot search
                int pivot = k;
                if (isComplex) {
                    for (int i = k + 1; i < n; i++) {
                        if (compareComplexMagnitude(h_A, i, pivot, k, n, arena, prec)) pivot = i;
                    }
                } else {
                    for (int i = k + 1; i < n; i++) {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivot, k, n, 0, false)) > 0) pivot = i;
                    }
                }

                if (pivot != k) {
                    swapRows(h_A, k, pivot, n, isComplex, arena, prec);
                    swapRows(h_Inv, k, pivot, n, isComplex, arena, prec);
                }
                
                // Check for singular matrix
                if (isComplex) {
                    if ((int) NativeSafe.invoke(MPFR_ZERO_P, getMPFR(h_A, k, k, n, 0, true)) != 0 &&
                        (int) NativeSafe.invoke(MPFR_ZERO_P, getMPFR(h_A, k, k, n, 1, true)) != 0) {
                        throw new ArithmeticException("Singular matrix");
                    }
                } else {
                    if ((int) NativeSafe.invoke(MPFR_ZERO_P, getMPFR(h_A, k, k, n, 0, false)) != 0) {
                        throw new ArithmeticException("Singular matrix");
                    }
                }

                // Normalize pivot row
                if (isComplex) {
                    MemorySegment pR = arena.allocate(MPFR_LAYOUT);
                    MemorySegment pI = arena.allocate(MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_INIT2, pR, prec);
                    NativeSafe.invoke(MPFR_INIT2, pI, prec);
                    NativeSafe.invoke(MPFR_SET, pR, getMPFR(h_A, k, k, n, 0, true), rnd);
                    NativeSafe.invoke(MPFR_SET, pI, getMPFR(h_A, k, k, n, 1, true), rnd);
                    
                    for (int j = 0; j < n; j++) {
                        complexDivide(getMPFR(h_A, k, j, n, 0, true), getMPFR(h_A, k, j, n, 1, true),
                            getMPFR(h_A, k, j, n, 0, true), getMPFR(h_A, k, j, n, 1, true),
                            pR, pI, prec, arena);
                        complexDivide(getMPFR(h_Inv, k, j, n, 0, true), getMPFR(h_Inv, k, j, n, 1, true),
                            getMPFR(h_Inv, k, j, n, 0, true), getMPFR(h_Inv, k, j, n, 1, true),
                            pR, pI, prec, arena);
                    }
                    // Set A[k][k] to 1+0i explicitly for stability
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_A, k, k, n, 0, true), 1L, rnd);
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_A, k, k, n, 1, true), 0L, rnd);
                } else {
                    MemorySegment pivotVal = arena.allocate(MPFR_LAYOUT);
                    NativeSafe.invoke(MPFR_INIT2, pivotVal, prec);
                    NativeSafe.invoke(MPFR_SET, pivotVal, getMPFR(h_A, k, k, n, 0, false), rnd);
                    
                    for (int j = 0; j < n; j++) {
                        NativeSafe.invoke(MPFR_DIV, getMPFR(h_A, k, j, n, 0, false), getMPFR(h_A, k, j, n, 0, false), pivotVal, rnd);
                        NativeSafe.invoke(MPFR_DIV, getMPFR(h_Inv, k, j, n, 0, false), getMPFR(h_Inv, k, j, n, 0, false), pivotVal, rnd);
                    }
                    NativeSafe.invoke(MPFR_SET_UI, getMPFR(h_A, k, k, n, 0, false), 1L, rnd);
                }
                
                // Eliminate other rows
                for (int i = 0; i < n; i++) {
                    if (i == k) continue;
                    if (isComplex) {
                        MemorySegment fR = arena.allocate(MPFR_LAYOUT);
                        MemorySegment fI = arena.allocate(MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, fR, prec);
                        NativeSafe.invoke(MPFR_INIT2, fI, prec);
                        NativeSafe.invoke(MPFR_SET, fR, getMPFR(h_A, i, k, n, 0, true), 0);
                        NativeSafe.invoke(MPFR_SET, fI, getMPFR(h_A, i, k, n, 1, true), 0);
                        
                        for (int j = 0; j < n; j++) {
                            complexSubtractMul(h_A, i, j, fR, fI, h_A, k, j, n, arena, prec);
                            complexSubtractMul(h_Inv, i, j, fR, fI, h_Inv, k, j, n, arena, prec);
                        }
                    } else {
                        MemorySegment factor = arena.allocate(MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, factor, prec);
                        NativeSafe.invoke(MPFR_SET, factor, getMPFR(h_A, i, k, n, 0, false), 0);
                        
                        for (int j = 0; j < n; j++) {
                            subtractMulReal(h_A, i, j, factor, h_A, k, j, n, arena, prec);
                            subtractMulReal(h_Inv, i, j, factor, h_Inv, k, j, n, arena, prec);
                        }
                    }
                }
            }
            Matrix<E> res = backToMatrix_internal(h_Inv, n, n, arena, a.getScalarRing(), isComplex);
            clearMPFRArray(h_A, n * n * (isComplex ? 2 : 1));
            clearMPFRArray(h_Inv, n * n * (isComplex ? 2 : 1));
            return res;
        } catch (Throwable t) {
            logger.error("MPFR inverse failed: {}", t.getMessage());
            throw new RuntimeException("MPFR inverse failed: " + t.toString(), t);
        }
    }

    @Override
    public E determinant(Matrix<E> a) {
        if (a.rows() != a.cols()) throw new IllegalArgumentException("Matrix must be square");
        int n = a.rows();
        boolean isComplex = ((Object)a.getScalarRing().zero()) instanceof org.episteme.core.mathematics.numbers.complex.Complex;
        long prec = getPrecision(); // MPFR_RNDN

        try (Arena arena = Arena.ofConfined()) {
            MemorySegment h_A = initMatrix(a, arena, prec, isComplex);
            MemorySegment[] detComp = new MemorySegment[2]; // detComp[0] for real, detComp[1] for imag
            detComp[0] = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, detComp[0], prec);
            NativeSafe.invoke(MPFR_SET_UI, detComp[0], 1L, 0); // Initialize det to 1
            if (isComplex) {
                detComp[1] = arena.allocate(MPFR_LAYOUT);
                NativeSafe.invoke(MPFR_INIT2, detComp[1], prec);
                NativeSafe.invoke(MPFR_SET_UI, detComp[1], 0L, 0); // Initialize imag part to 0
            }

            for (int k = 0; k < n; k++) {
                int pivot = k;
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        if (compareComplexMagnitude(h_A, i, pivot, k, n, arena, prec)) pivot = i;
                    } else {
                        if ((int) NativeSafe.invoke(MPFR_CMP_ABS, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, pivot, k, n, 0, false)) > 0) pivot = i;
                    }
                }
                
                if (pivot != k) {
                    swapRows(h_A, k, pivot, n, isComplex, arena, prec);
                    NativeSafe.invoke(MPFR_NEG, detComp[0], detComp[0], 0); // Flip sign of determinant
                    if (isComplex) NativeSafe.invoke(MPFR_NEG, detComp[1], detComp[1], 0);
                }
                
                // Multiply determinant by A[k][k]
                if (isComplex) {
                    complexMultiply(detComp[0], detComp[1], detComp[0], detComp[1], 
                        getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), prec, arena);
                } else {
                    NativeSafe.invoke(MPFR_MUL, detComp[0], detComp[0], getMPFR(h_A, k, k, n, 0, false), 0);
                }
                
                // Check for zero determinant
                if (isComplex) {
                    if ((int) NativeSafe.invoke(MPFR_ZERO_P, getMPFR(h_A, k, k, n, 0, true)) != 0 &&
                        (int) NativeSafe.invoke(MPFR_ZERO_P, getMPFR(h_A, k, k, n, 1, true)) != 0) {
                        return (E) a.getScalarRing().zero(); // Determinant is 0
                    }
                } else {
                    if ((int) NativeSafe.invoke(MPFR_ZERO_P, getMPFR(h_A, k, k, n, 0, false)) != 0) {
                        return (E) a.getScalarRing().zero(); // Determinant is 0
                    }
                }

                // Eliminate
                for (int i = k + 1; i < n; i++) {
                    if (isComplex) {
                        MemorySegment fR = arena.allocate(MPFR_LAYOUT);
                        MemorySegment fI = arena.allocate(MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, fR, prec);
                        NativeSafe.invoke(MPFR_INIT2, fI, prec);
                        complexDivide(fR, fI, getMPFR(h_A, i, k, n, 0, true), getMPFR(h_A, i, k, n, 1, true),
                            getMPFR(h_A, k, k, n, 0, true), getMPFR(h_A, k, k, n, 1, true), prec, arena);
                        for (int j = k; j < n; j++) complexSubtractMul(h_A, i, j, fR, fI, h_A, k, j, n, arena, prec);
                    } else {
                        MemorySegment factor = arena.allocate(MPFR_LAYOUT);
                        NativeSafe.invoke(MPFR_INIT2, factor, prec);
                        NativeSafe.invoke(MPFR_DIV, factor, getMPFR(h_A, i, k, n, 0, false), getMPFR(h_A, k, k, n, 0, false), 0);
                        for (int j = k; j < n; j++) subtractMulReal(h_A, i, j, factor, h_A, k, j, n, arena, prec);
                    }
                }
            }
            
            // The determinant is now in detComp
            MemorySegment expPtr = arena.allocate(ValueLayout.JAVA_LONG); 
            E resDet;
            if (isComplex) {
                resDet = (E) (Object) org.episteme.core.mathematics.numbers.complex.Complex.of(
                    readMPFR(detComp[0], expPtr, arena),
                    readMPFR(detComp[1], expPtr, arena)
                );
            } else {
                resDet = (E) (Object) readMPFR(detComp[0], expPtr, arena);
            }
            NativeSafe.invoke(MPFR_CLEAR, detComp[0]);
            if (isComplex) NativeSafe.invoke(MPFR_CLEAR, detComp[1]);
            clearMPFRArray(h_A, n * n * (isComplex ? 2 : 1));
            return resDet;
        } catch (Throwable t) {
            logger.error("MPFR determinant failed: {}", t.getMessage());
            throw new RuntimeException("MPFR determinant failed", t);
        }
    }



    private void subtractMulReal(MemorySegment h_Mat, int r, int c, MemorySegment factor, MemorySegment h_Source, int rs, int cs, int cols, Arena arena, long prec) {
        MemorySegment dst = getMPFR(h_Mat, r, c, cols, 0, false);
        MemorySegment src = getMPFR(h_Source, rs, cs, cols, 0, false);
        MemorySegment term = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, term, prec);
        NativeSafe.invoke(MPFR_MUL, term, factor, src, 0);
        NativeSafe.invoke(MPFR_SUB, dst, dst, term, 0);
    }

    private void subtractMulVectorReal(MemorySegment h_Vec, int r, MemorySegment factor, MemorySegment h_Source, int rs, Arena arena, long prec) {
        MemorySegment dst = getMPFRVector(h_Vec, r, 0, false);
        MemorySegment src = getMPFRVector(h_Source, rs, 0, false);
        MemorySegment term = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, term, prec);
        NativeSafe.invoke(MPFR_MUL, term, factor, src, 0);
        NativeSafe.invoke(MPFR_SUB, dst, dst, term, 0);
    }

    private boolean compareComplexMagnitude(MemorySegment h_Mat, int r1, int r2, int col, int n, Arena arena, long prec) throws Throwable {
        MemorySegment mag1 = arena.allocate(MPFR_LAYOUT);
        MemorySegment mag2 = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, mag1, prec);
        NativeSafe.invoke(MPFR_INIT2, mag2, prec);
        
        complexMagnitudeSquared(mag1, getMPFR(h_Mat, r1, col, n, 0, true), getMPFR(h_Mat, r1, col, n, 1, true), prec, arena);
        complexMagnitudeSquared(mag2, getMPFR(h_Mat, r2, col, n, 0, true), getMPFR(h_Mat, r2, col, n, 1, true), prec, arena);
        
        return (int) NativeSafe.invoke(MPFR_CMP, mag1, mag2) > 0;
    }

    private void complexMagnitudeSquared(MemorySegment res, MemorySegment r, MemorySegment i, long prec, Arena arena) {
        MemorySegment t = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, t, prec);
        NativeSafe.invoke(MPFR_MUL, res, r, r, 0);
        NativeSafe.invoke(MPFR_MUL, t, i, i, 0);
        NativeSafe.invoke(MPFR_ADD, res, res, t, 0);
    }

    private void complexDivide(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment bR, MemorySegment bI, long prec, Arena arena) {
        MemorySegment denom = arena.allocate(MPFR_LAYOUT);
        MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT);
        MemorySegment outR = arena.allocate(MPFR_LAYOUT);
        MemorySegment outI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, denom, prec);
        NativeSafe.invoke(MPFR_INIT2, t1, prec);
        NativeSafe.invoke(MPFR_INIT2, t2, prec);
        NativeSafe.invoke(MPFR_INIT2, outR, prec);
        NativeSafe.invoke(MPFR_INIT2, outI, prec);
        
        // denom = bR^2 + bI^2
        NativeSafe.invoke(MPFR_MUL, denom, bR, bR, 0);
        NativeSafe.invoke(MPFR_MUL, t1, bI, bI, 0);
        NativeSafe.invoke(MPFR_ADD, denom, denom, t1, 0);
        
        // outR = (aR*bR + aI*bI) / denom
        NativeSafe.invoke(MPFR_MUL, t1, aR, bR, 0);
        NativeSafe.invoke(MPFR_MUL, t2, aI, bI, 0);
        NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
        NativeSafe.invoke(MPFR_DIV, outR, t1, denom, 0);
        
        // outI = (aI*bR - aR*bI) / denom
        NativeSafe.invoke(MPFR_MUL, t1, aI, bR, 0);
        NativeSafe.invoke(MPFR_MUL, t2, aR, bI, 0);
        NativeSafe.invoke(MPFR_SUB, t1, t1, t2, 0);
        NativeSafe.invoke(MPFR_DIV, outI, t1, denom, 0);

        NativeSafe.invoke(MPFR_SET, resR, outR, 0);
        NativeSafe.invoke(MPFR_SET, resI, outI, 0);

        NativeSafe.invoke(MPFR_CLEAR, denom);
        NativeSafe.invoke(MPFR_CLEAR, t1);
        NativeSafe.invoke(MPFR_CLEAR, t2);
        NativeSafe.invoke(MPFR_CLEAR, outR);
        NativeSafe.invoke(MPFR_CLEAR, outI);
    }

    private void complexSubtractMul(MemorySegment h_Mat, int r, int c, MemorySegment fR, MemorySegment fI, MemorySegment h_Src, int rs, int cs, int n, Arena arena, long prec) {
        MemorySegment dstR = getMPFR(h_Mat, r, c, n, 0, true);
        MemorySegment dstI = getMPFR(h_Mat, r, c, n, 1, true);
        MemorySegment srcR = getMPFR(h_Src, rs, cs, n, 0, true);
        MemorySegment srcI = getMPFR(h_Src, rs, cs, n, 1, true);
        
        MemorySegment tR = arena.allocate(MPFR_LAYOUT);
        MemorySegment tI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, tR, prec);
        NativeSafe.invoke(MPFR_INIT2, tI, prec);
        
        // t = f * src
        NativeSafe.invoke(MPFR_MUL, tR, fR, srcR, 0);
        NativeSafe.invoke(MPFR_MUL, tI, fI, srcI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tI, 0); // tR = fR*srcR - fI*srcI
        
        NativeSafe.invoke(MPFR_MUL, tI, fR, srcI, 0);
        MemorySegment tEmp = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tEmp, prec);
        NativeSafe.invoke(MPFR_MUL, tEmp, fI, srcR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0); // tI = fR*srcI + fI*srcR
        
        // dst = dst - t
        NativeSafe.invoke(MPFR_SUB, dstR, dstR, tR, 0);
        NativeSafe.invoke(MPFR_SUB, dstI, dstI, tI, 0);
    }

    private void complexSubtractMulVector(MemorySegment h_Vec, int r, MemorySegment fR, MemorySegment fI, MemorySegment h_Src, int rs, Arena arena, long prec) {
        MemorySegment dstR = getMPFRVector(h_Vec, r, 0, true);
        MemorySegment dstI = getMPFRVector(h_Vec, r, 1, true);
        MemorySegment srcR = getMPFRVector(h_Src, rs, 0, true);
        MemorySegment srcI = getMPFRVector(h_Src, rs, 1, true);
        
        MemorySegment tR = arena.allocate(MPFR_LAYOUT);
        MemorySegment tI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, tR, prec);
        NativeSafe.invoke(MPFR_INIT2, tI, prec);
        
        NativeSafe.invoke(MPFR_MUL, tR, fR, srcR, 0);
        NativeSafe.invoke(MPFR_MUL, tI, fI, srcI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tI, 0);
        
        NativeSafe.invoke(MPFR_MUL, tI, fR, srcI, 0);
        MemorySegment tEmp = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tEmp, prec);
        NativeSafe.invoke(MPFR_MUL, tEmp, fI, srcR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0);
        
        NativeSafe.invoke(MPFR_SUB, dstR, dstR, tR, 0);
        NativeSafe.invoke(MPFR_SUB, dstI, dstI, tI, 0);
    }

    private void complexMultiply(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment bR, MemorySegment bI, long prec, Arena arena) {
        MemorySegment tR = arena.allocate(MPFR_LAYOUT);
        MemorySegment tI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, tR, prec);
        NativeSafe.invoke(MPFR_INIT2, tI, prec);
        
        // tR = aR*bR - aI*bI
        NativeSafe.invoke(MPFR_MUL, tR, aR, bR, 0);
        MemorySegment tEmp = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tEmp, prec);
        NativeSafe.invoke(MPFR_MUL, tEmp, aI, bI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tEmp, 0);
        
        // tI = aR*bI + aI*bR
        NativeSafe.invoke(MPFR_MUL, tI, aR, bI, 0);
        NativeSafe.invoke(MPFR_MUL, tEmp, aI, bR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0);
        
        NativeSafe.invoke(MPFR_SET, resR, tR, 0);
        NativeSafe.invoke(MPFR_SET, resI, tI, 0);
    }

    private void complexAddMul(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment bR, MemorySegment bI, long prec, Arena arena) {
        MemorySegment tR = arena.allocate(MPFR_LAYOUT);
        MemorySegment tI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, tR, prec);
        NativeSafe.invoke(MPFR_INIT2, tI, prec);
        
        // t = a * b
        NativeSafe.invoke(MPFR_MUL, tR, aR, bR, 0);
        NativeSafe.invoke(MPFR_MUL, tI, aI, bI, 0);
        NativeSafe.invoke(MPFR_SUB, tR, tR, tI, 0);
        
        NativeSafe.invoke(MPFR_MUL, tI, aR, bI, 0);
        MemorySegment tEmp = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, tEmp, prec);
        NativeSafe.invoke(MPFR_MUL, tEmp, aI, bR, 0);
        NativeSafe.invoke(MPFR_ADD, tI, tI, tEmp, 0);
        
        // res = res + t
        NativeSafe.invoke(MPFR_ADD, resR, resR, tR, 0);
        NativeSafe.invoke(MPFR_ADD, resI, resI, tI, 0);
    }

    private void swapRows(MemorySegment h_Mat, int r1, int r2, int n, boolean isComplex, Arena arena, long prec) {
        int multiplier = isComplex ? 2 : 1;
        for (int j = 0; j < n * multiplier; j++) {
            MemorySegment m1 = h_Mat.asSlice(((long)r1 * n * multiplier + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            MemorySegment m2 = h_Mat.asSlice(((long)r2 * n * multiplier + j) * MPFR_LAYOUT.byteSize(), MPFR_LAYOUT);
            MemorySegment tmp = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, tmp, prec);
            NativeSafe.invoke(MPFR_SET, tmp, m1, 0);
            NativeSafe.invoke(MPFR_SET, m1, m2, 0);
            NativeSafe.invoke(MPFR_SET, m2, tmp, 0);
        }
    }

    private void swapRowsVector(MemorySegment h_Vec, int r1, int r2, boolean isComplex, Arena arena, long prec) {
        int multiplier = isComplex ? 2 : 1;
        for (int j = 0; j < multiplier; j++) {
            MemorySegment m1 = getMPFRVector(h_Vec, r1, j, isComplex);
            MemorySegment m2 = getMPFRVector(h_Vec, r2, j, isComplex);
            MemorySegment tmp = arena.allocate(MPFR_LAYOUT);
            NativeSafe.invoke(MPFR_INIT2, tmp, prec);
            NativeSafe.invoke(MPFR_SET, tmp, m1, 0);
            NativeSafe.invoke(MPFR_SET, m1, m2, 0);
            NativeSafe.invoke(MPFR_SET, m2, tmp, 0);
        }
    }

    // --- Complex Transcendental Arithmetic ---

    public void complexExp(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        MemorySegment expR = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, expR, prec);
        NativeSafe.invoke(MPFR_EXP, expR, aR, 0);
        
        MemorySegment cosI = arena.allocate(MPFR_LAYOUT);
        MemorySegment sinI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, cosI, prec);
        NativeSafe.invoke(MPFR_INIT2, sinI, prec);
        NativeSafe.invoke(MPFR_COS, cosI, aI, 0);
        NativeSafe.invoke(MPFR_SIN, sinI, aI, 0);
        
        NativeSafe.invoke(MPFR_MUL, resR, expR, cosI, 0);
        NativeSafe.invoke(MPFR_MUL, resI, expR, sinI, 0);
        
        NativeSafe.invoke(MPFR_CLEAR, expR);
        NativeSafe.invoke(MPFR_CLEAR, cosI);
        NativeSafe.invoke(MPFR_CLEAR, sinI);
    }

    public void complexLog(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // resR = 0.5 * log(aR^2 + aI^2)
        MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, t1, prec);
        NativeSafe.invoke(MPFR_INIT2, t2, prec);
        
        NativeSafe.invoke(MPFR_MUL, t1, aR, aR, 0);
        NativeSafe.invoke(MPFR_MUL, t2, aI, aI, 0);
        NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
        NativeSafe.invoke(MPFR_LOG, t1, t1, 0);
        NativeSafe.invoke(MPFR_SET_D, t2, 0.5, 0);
        NativeSafe.invoke(MPFR_MUL, resR, t1, t2, 0);
        
        // resI = atan2(aI, aR)
        if (MPFR_ATAN2 != null) {
            NativeSafe.invoke(MPFR_ATAN2, resI, aI, aR, 0);
        } else {
            // High-precision manual atan2 fallback
            MemorySegment pi = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, pi, prec);
            NativeSafe.invoke(MPFR_CONST_PI, pi, 0);
            
            MemorySegment zero = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, zero, prec);
            NativeSafe.invoke(MPFR_SET_UI, zero, 0L, 0);
            
            int cmpX = (int) NativeSafe.invoke(MPFR_CMP, aR, zero, 0);
            int cmpY = (int) NativeSafe.invoke(MPFR_CMP, aI, zero, 0);
            
            if (cmpX > 0) {
                // atan2 = atan(y/x)
                NativeSafe.invoke(MPFR_DIV, resI, aI, aR, 0);
                NativeSafe.invoke(MPFR_ATAN, resI, resI, 0);
            } else if (cmpX < 0) {
                // atan(y/x) + (y>=0 ? pi : -pi)
                NativeSafe.invoke(MPFR_DIV, resI, aI, aR, 0);
                NativeSafe.invoke(MPFR_ATAN, resI, resI, 0);
                if (cmpY >= 0) NativeSafe.invoke(MPFR_ADD, resI, resI, pi, 0);
                else NativeSafe.invoke(MPFR_SUB, resI, resI, pi, 0);
            } else {
                // x == 0
                if (cmpY > 0) {
                    NativeSafe.invoke(MPFR_SET_UI, resI, 2L, 0);
                    NativeSafe.invoke(MPFR_DIV, resI, pi, resI, 0); // pi/2
                } else if (cmpY < 0) {
                    NativeSafe.invoke(MPFR_SET_UI, resI, 2L, 0);
                    NativeSafe.invoke(MPFR_DIV, resI, pi, resI, 0);
                    NativeSafe.invoke(MPFR_NEG, resI, resI, 0); // -pi/2
                } else {
                    NativeSafe.invoke(MPFR_SET_UI, resI, 0L, 0);
                }
            }
            NativeSafe.invoke(MPFR_CLEAR, pi);
            NativeSafe.invoke(MPFR_CLEAR, zero);
        }
        
        NativeSafe.invoke(MPFR_CLEAR, t1);
        NativeSafe.invoke(MPFR_CLEAR, t2);
    }

    public void complexLog10(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        complexLog(resR, resI, aR, aI, prec, arena);
        MemorySegment log10 = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, log10, prec);
        NativeSafe.invoke(MPFR_SET_STR, log10, arena.allocateFrom("2.3025850929940456840179914546843642076011"), 10, 0); // ln(10) approx
        NativeSafe.invoke(MPFR_DIV, resR, resR, log10, 0);
        NativeSafe.invoke(MPFR_DIV, resI, resI, log10, 0);
        NativeSafe.invoke(MPFR_CLEAR, log10);
    }

    public void complexSin(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // sin(x + iy) = sin(x)cosh(y) + i cos(x)sinh(y)
        MemorySegment sR = arena.allocate(MPFR_LAYOUT);
        MemorySegment cR = arena.allocate(MPFR_LAYOUT);
        MemorySegment shI = arena.allocate(MPFR_LAYOUT);
        MemorySegment chI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, sR, prec);
        NativeSafe.invoke(MPFR_INIT2, cR, prec);
        NativeSafe.invoke(MPFR_INIT2, shI, prec);
        NativeSafe.invoke(MPFR_INIT2, chI, prec);
        
        NativeSafe.invoke(MPFR_SIN, sR, aR, 0);
        NativeSafe.invoke(MPFR_COS, cR, aR, 0);
        NativeSafe.invoke(MPFR_SINH, shI, aI, 0);
        NativeSafe.invoke(MPFR_COSH, chI, aI, 0);
        
        NativeSafe.invoke(MPFR_MUL, resR, sR, chI, 0);
        NativeSafe.invoke(MPFR_MUL, resI, cR, shI, 0);
        
        NativeSafe.invoke(MPFR_CLEAR, sR);
        NativeSafe.invoke(MPFR_CLEAR, cR);
        NativeSafe.invoke(MPFR_CLEAR, shI);
        NativeSafe.invoke(MPFR_CLEAR, chI);
    }

    public void complexCos(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // cos(x + iy) = cos(x)cosh(y) - i sin(x)sinh(y)
        MemorySegment sR = arena.allocate(MPFR_LAYOUT);
        MemorySegment cR = arena.allocate(MPFR_LAYOUT);
        MemorySegment shI = arena.allocate(MPFR_LAYOUT);
        MemorySegment chI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, sR, prec);
        NativeSafe.invoke(MPFR_INIT2, cR, prec);
        NativeSafe.invoke(MPFR_INIT2, shI, prec);
        NativeSafe.invoke(MPFR_INIT2, chI, prec);
        
        NativeSafe.invoke(MPFR_SIN, sR, aR, 0);
        NativeSafe.invoke(MPFR_COS, cR, aR, 0);
        NativeSafe.invoke(MPFR_SINH, shI, aI, 0);
        NativeSafe.invoke(MPFR_COSH, chI, aI, 0);
        
        NativeSafe.invoke(MPFR_MUL, resR, cR, chI, 0);
        NativeSafe.invoke(MPFR_MUL, resI, sR, shI, 0);
        NativeSafe.invoke(MPFR_NEG, resI, resI, 0);
        
        NativeSafe.invoke(MPFR_CLEAR, sR);
        NativeSafe.invoke(MPFR_CLEAR, cR);
        NativeSafe.invoke(MPFR_CLEAR, shI);
        NativeSafe.invoke(MPFR_CLEAR, chI);
    }

    public void complexTan(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // tan(z) = sin(z) / cos(z)
        MemorySegment sR = arena.allocate(MPFR_LAYOUT);
        MemorySegment sI = arena.allocate(MPFR_LAYOUT);
        MemorySegment cR = arena.allocate(MPFR_LAYOUT);
        MemorySegment cI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, sR, prec);
        NativeSafe.invoke(MPFR_INIT2, sI, prec);
        NativeSafe.invoke(MPFR_INIT2, cR, prec);
        NativeSafe.invoke(MPFR_INIT2, cI, prec);
        
        complexSin(sR, sI, aR, aI, prec, arena);
        complexCos(cR, cI, aR, aI, prec, arena);
        complexDivide(resR, resI, sR, sI, cR, cI, prec, arena);
        
        NativeSafe.invoke(MPFR_CLEAR, sR);
        NativeSafe.invoke(MPFR_CLEAR, sI);
        NativeSafe.invoke(MPFR_CLEAR, cR);
        NativeSafe.invoke(MPFR_CLEAR, cI);
    }

    public void complexSinh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // sinh(x + iy) = sinh(x)cos(y) + i cosh(x)sin(y)
        MemorySegment shR = arena.allocate(MPFR_LAYOUT);
        MemorySegment chR = arena.allocate(MPFR_LAYOUT);
        MemorySegment sI = arena.allocate(MPFR_LAYOUT);
        MemorySegment cI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, shR, prec);
        NativeSafe.invoke(MPFR_INIT2, chR, prec);
        NativeSafe.invoke(MPFR_INIT2, sI, prec);
        NativeSafe.invoke(MPFR_INIT2, cI, prec);
        
        NativeSafe.invoke(MPFR_SINH, shR, aR, 0);
        NativeSafe.invoke(MPFR_COSH, chR, aR, 0);
        NativeSafe.invoke(MPFR_SIN, sI, aI, 0);
        NativeSafe.invoke(MPFR_COS, cI, aI, 0);
        
        NativeSafe.invoke(MPFR_MUL, resR, shR, cI, 0);
        NativeSafe.invoke(MPFR_MUL, resI, chR, sI, 0);
        
        NativeSafe.invoke(MPFR_CLEAR, shR);
        NativeSafe.invoke(MPFR_CLEAR, chR);
        NativeSafe.invoke(MPFR_CLEAR, sI);
        NativeSafe.invoke(MPFR_CLEAR, cI);
    }

    public void complexCosh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // cosh(x + iy) = cosh(x)cos(y) + i sinh(x)sin(y)
        MemorySegment shR = arena.allocate(MPFR_LAYOUT);
        MemorySegment chR = arena.allocate(MPFR_LAYOUT);
        MemorySegment sI = arena.allocate(MPFR_LAYOUT);
        MemorySegment cI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, shR, prec);
        NativeSafe.invoke(MPFR_INIT2, chR, prec);
        NativeSafe.invoke(MPFR_INIT2, sI, prec);
        NativeSafe.invoke(MPFR_INIT2, cI, prec);
        
        NativeSafe.invoke(MPFR_SINH, shR, aR, 0);
        NativeSafe.invoke(MPFR_COSH, chR, aR, 0);
        NativeSafe.invoke(MPFR_SIN, sI, aI, 0);
        NativeSafe.invoke(MPFR_COS, cI, aI, 0);
        
        NativeSafe.invoke(MPFR_MUL, resR, chR, cI, 0);
        NativeSafe.invoke(MPFR_MUL, resI, shR, sI, 0);
        
        NativeSafe.invoke(MPFR_CLEAR, shR);
        NativeSafe.invoke(MPFR_CLEAR, chR);
        NativeSafe.invoke(MPFR_CLEAR, sI);
        NativeSafe.invoke(MPFR_CLEAR, cI);
    }

    public void complexTanh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // tanh(z) = sinh(z) / cosh(z)
        MemorySegment sR = arena.allocate(MPFR_LAYOUT);
        MemorySegment sI = arena.allocate(MPFR_LAYOUT);
        MemorySegment cR = arena.allocate(MPFR_LAYOUT);
        MemorySegment cI = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, sR, prec);
        NativeSafe.invoke(MPFR_INIT2, sI, prec);
        NativeSafe.invoke(MPFR_INIT2, cR, prec);
        NativeSafe.invoke(MPFR_INIT2, cI, prec);
        
        complexSinh(sR, sI, aR, aI, prec, arena);
        complexCosh(cR, cI, aR, aI, prec, arena);
        complexDivide(resR, resI, sR, sI, cR, cI, prec, arena);
        NativeSafe.invoke(MPFR_CLEAR, sR);
        NativeSafe.invoke(MPFR_CLEAR, sI);
        NativeSafe.invoke(MPFR_CLEAR, cR);
        NativeSafe.invoke(MPFR_CLEAR, cI);
    }    private static MethodHandle MPFR_HYPOT;

    public void complexSqrt(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // sqrt(x + iy) = sqrt((|z| + x)/2) + i sign(y)sqrt((|z| - x)/2)
        MemorySegment norm = arena.allocate(MPFR_LAYOUT);
        MemorySegment t1 = arena.allocate(MPFR_LAYOUT);
        MemorySegment t2 = arena.allocate(MPFR_LAYOUT);
        NativeSafe.invoke(MPFR_INIT2, norm, prec);
        NativeSafe.invoke(MPFR_INIT2, t1, prec);
        NativeSafe.invoke(MPFR_INIT2, t2, prec);
        
        // norm = hypot(x, y) = sqrt(x^2 + y^2) - more robust
        if (MPFR_HYPOT != null) {
            NativeSafe.invoke(MPFR_HYPOT, norm, aR, aI, 0);
        } else {
            NativeSafe.invoke(MPFR_MUL, t1, aR, aR, 0);
            NativeSafe.invoke(MPFR_MUL, t2, aI, aI, 0);
            NativeSafe.invoke(MPFR_ADD, t1, t1, t2, 0);
            NativeSafe.invoke(MPFR_SQRT, norm, t1, 0);
        }
        
        // resR = sqrt((norm + x)/2)
        NativeSafe.invoke(MPFR_ADD, t1, norm, aR, 0);
        NativeSafe.invoke(MPFR_SET_D, t2, 0.5, 0);
        NativeSafe.invoke(MPFR_MUL, t1, t1, t2, 0);
        
        // Domain guard: Ensure t1 >= 0 due to precision noise
        MemorySegment zero = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, zero, prec);
        NativeSafe.invoke(MPFR_SET_UI, zero, 0L, 0);
        if ((int) NativeSafe.invoke(MPFR_CMP, t1, zero) < 0) NativeSafe.invoke(MPFR_SET, t1, zero, 0);
        NativeSafe.invoke(MPFR_SQRT, resR, t1, 0);
        
        // resI = sign(y) * sqrt((norm - x)/2)
        NativeSafe.invoke(MPFR_SUB, t1, norm, aR, 0);
        NativeSafe.invoke(MPFR_MUL, t1, t1, t2, 0);
        if ((int) NativeSafe.invoke(MPFR_CMP, t1, zero) < 0) NativeSafe.invoke(MPFR_SET, t1, zero, 0);
        NativeSafe.invoke(MPFR_SQRT, resI, t1, 0);
        
        if ((int) NativeSafe.invoke(MPFR_CMP, aI, zero, 0) < 0) { 
             NativeSafe.invoke(MPFR_NEG, resI, resI, 0);
        }
        
        NativeSafe.invoke(MPFR_CLEAR, zero);
        NativeSafe.invoke(MPFR_CLEAR, norm);
        NativeSafe.invoke(MPFR_CLEAR, t1);
        NativeSafe.invoke(MPFR_CLEAR, t2);
    }

    
    public void complexAsin(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // asin(z) = -i log(iz + sqrt(1 - z^2))
        MemorySegment izR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, izR, prec);
        MemorySegment izI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, izI, prec);
        // iz = (0 + i)*(aR + i*aI) = -aI + i*aR
        NativeSafe.invoke(MPFR_NEG, izR, aI, 0);
        NativeSafe.invoke(MPFR_SET, izI, aR, 0);
        
        MemorySegment z2R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, z2R, prec);
        MemorySegment z2I = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, z2I, prec);
        complexMultiply(z2R, z2I, aR, aI, aR, aI, prec, arena);
        
        MemorySegment oneMinusZ2R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, oneMinusZ2R, prec);
        MemorySegment oneMinusZ2I = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, oneMinusZ2I, prec);
        NativeSafe.invoke(MPFR_SET_UI, oneMinusZ2R, 1L, 0);
        NativeSafe.invoke(MPFR_SET_UI, oneMinusZ2I, 0L, 0);
        NativeSafe.invoke(MPFR_SUB, oneMinusZ2R, oneMinusZ2R, z2R, 0);
        NativeSafe.invoke(MPFR_SUB, oneMinusZ2I, oneMinusZ2I, z2I, 0);
        
        MemorySegment sqrtR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sqrtR, prec);
        MemorySegment sqrtI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sqrtI, prec);
        complexSqrt(sqrtR, sqrtI, oneMinusZ2R, oneMinusZ2I, prec, arena);
        
        MemorySegment sumR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sumR, prec);
        MemorySegment sumI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sumI, prec);
        NativeSafe.invoke(MPFR_ADD, sumR, izR, sqrtR, 0);
        NativeSafe.invoke(MPFR_ADD, sumI, izI, sqrtI, 0);
        
        MemorySegment logR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, logR, prec);
        MemorySegment logI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, logI, prec);
        complexLog(logR, logI, sumR, sumI, prec, arena);
        
        // res = -i * log = -i * (logR + i*logI) = logI - i*logR
        NativeSafe.invoke(MPFR_SET, resR, logI, 0);
        NativeSafe.invoke(MPFR_NEG, resI, logR, 0);
        
        NativeSafe.invoke(MPFR_CLEAR, izR); NativeSafe.invoke(MPFR_CLEAR, izI);
        NativeSafe.invoke(MPFR_CLEAR, z2R); NativeSafe.invoke(MPFR_CLEAR, z2I);
        NativeSafe.invoke(MPFR_CLEAR, oneMinusZ2R); NativeSafe.invoke(MPFR_CLEAR, oneMinusZ2I);
        NativeSafe.invoke(MPFR_CLEAR, sqrtR); NativeSafe.invoke(MPFR_CLEAR, sqrtI);
        NativeSafe.invoke(MPFR_CLEAR, sumR); NativeSafe.invoke(MPFR_CLEAR, sumI);
        NativeSafe.invoke(MPFR_CLEAR, logR); NativeSafe.invoke(MPFR_CLEAR, logI);
    }
    
    public void complexAcos(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // acos(z) = pi/2 - asin(z)
        MemorySegment asinR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, asinR, prec);
        MemorySegment asinI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, asinI, prec);
        complexAsin(asinR, asinI, aR, aI, prec, arena);
        
        MemorySegment piHalf = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, piHalf, prec);
        NativeSafe.invoke(MPFR_CONST_PI, piHalf, 0);
        MemorySegment two = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, two, prec);
        NativeSafe.invoke(MPFR_SET_UI, two, 2L, 0);
        NativeSafe.invoke(MPFR_DIV, piHalf, piHalf, two, 0);
        
        NativeSafe.invoke(MPFR_SUB, resR, piHalf, asinR, 0);
        NativeSafe.invoke(MPFR_NEG, resI, asinI, 0);
        
        NativeSafe.invoke(MPFR_CLEAR, asinR); NativeSafe.invoke(MPFR_CLEAR, asinI);
        NativeSafe.invoke(MPFR_CLEAR, piHalf); NativeSafe.invoke(MPFR_CLEAR, two);
    }
    
    public void complexAtan(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // atan(z) = i/2 log((1-iz)/(1+iz))
        // iz = -aI + i*aR
        MemorySegment izR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, izR, prec);
        MemorySegment izI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, izI, prec);
        NativeSafe.invoke(MPFR_NEG, izR, aI, 0);
        NativeSafe.invoke(MPFR_SET, izI, aR, 0);
        
        MemorySegment numR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, numR, prec);
        MemorySegment numI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, numI, prec);
        NativeSafe.invoke(MPFR_SET_UI, numR, 1L, 0);
        NativeSafe.invoke(MPFR_SET_UI, numI, 0L, 0);
        NativeSafe.invoke(MPFR_SUB, numR, numR, izR, 0);
        NativeSafe.invoke(MPFR_SUB, numI, numI, izI, 0);
        
        MemorySegment denR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, denR, prec);
        MemorySegment denI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, denI, prec);
        NativeSafe.invoke(MPFR_SET_UI, denR, 1L, 0);
        NativeSafe.invoke(MPFR_SET_UI, denI, 0L, 0);
        NativeSafe.invoke(MPFR_ADD, denR, denR, izR, 0);
        NativeSafe.invoke(MPFR_ADD, denI, denI, izI, 0);
        
        MemorySegment divR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, divR, prec);
        MemorySegment divI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, divI, prec);
        complexDivide(divR, divI, numR, numI, denR, denI, prec, arena);
        
        MemorySegment logR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, logR, prec);
        MemorySegment logI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, logI, prec);
        complexLog(logR, logI, divR, divI, prec, arena);
        
        // res = i/2 * log = i/2 * (logR + i*logI) = -logI/2 + i*logR/2
        NativeSafe.invoke(MPFR_SET, resR, logI, 0);
        NativeSafe.invoke(MPFR_NEG, resR, resR, 0);
        MemorySegment check = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, check, prec);
        NativeSafe.invoke(MPFR_SET_UI, check, 2L, 0);
        NativeSafe.invoke(MPFR_DIV, resR, resR, check, 0);
        
        NativeSafe.invoke(MPFR_SET, resI, logR, 0);
        NativeSafe.invoke(MPFR_DIV, resI, resI, check, 0);
        
        NativeSafe.invoke(MPFR_CLEAR, izR); NativeSafe.invoke(MPFR_CLEAR, izI);
        NativeSafe.invoke(MPFR_CLEAR, numR); NativeSafe.invoke(MPFR_CLEAR, numI);
        NativeSafe.invoke(MPFR_CLEAR, denR); NativeSafe.invoke(MPFR_CLEAR, denI);
        NativeSafe.invoke(MPFR_CLEAR, divR); NativeSafe.invoke(MPFR_CLEAR, divI);
        NativeSafe.invoke(MPFR_CLEAR, logR); NativeSafe.invoke(MPFR_CLEAR, logI);
        NativeSafe.invoke(MPFR_CLEAR, check);
    }

    public void complexAsinh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // asinh(z) = log(z + sqrt(z^2 + 1))
        MemorySegment z2R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, z2R, prec);
        MemorySegment z2I = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, z2I, prec);
        complexMultiply(z2R, z2I, aR, aI, aR, aI, prec, arena);
        
        NativeSafe.invoke(MPFR_SET_UI, resR, 1L, 0);
        NativeSafe.invoke(MPFR_ADD, z2R, z2R, resR, 0);
        
        MemorySegment sqrtR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sqrtR, prec);
        MemorySegment sqrtI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, sqrtI, prec);
        complexSqrt(sqrtR, sqrtI, z2R, z2I, prec, arena);
        
        NativeSafe.invoke(MPFR_ADD, z2R, aR, sqrtR, 0);
        NativeSafe.invoke(MPFR_ADD, z2I, aI, sqrtI, 0);
        
        complexLog(resR, resI, z2R, z2I, prec, arena);
        
        NativeSafe.invoke(MPFR_CLEAR, z2R); NativeSafe.invoke(MPFR_CLEAR, z2I);
        NativeSafe.invoke(MPFR_CLEAR, sqrtR); NativeSafe.invoke(MPFR_CLEAR, sqrtI);
    }
    
    public void complexAcosh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // acosh(z) = log(z + sqrt(z + 1)sqrt(z - 1))
        MemorySegment zp1R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, zp1R, prec);
        MemorySegment zm1R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, zm1R, prec);
        NativeSafe.invoke(MPFR_SET_UI, zp1R, 1L, 0);
        NativeSafe.invoke(MPFR_ADD, zp1R, aR, zp1R, 0);
        NativeSafe.invoke(MPFR_SET_UI, zm1R, 1L, 0);
        NativeSafe.invoke(MPFR_SUB, zm1R, aR, zm1R, 0);
        
        MemorySegment s1R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, s1R, prec);
        MemorySegment s1I = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, s1I, prec);
        MemorySegment s2R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, s2R, prec);
        MemorySegment s2I = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, s2I, prec);
        complexSqrt(s1R, s1I, zp1R, aI, prec, arena);
        complexSqrt(s2R, s2I, zm1R, aI, prec, arena);
        
        MemorySegment prodR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, prodR, prec);
        MemorySegment prodI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, prodI, prec);
        complexMultiply(prodR, prodI, s1R, s1I, s2R, s2I, prec, arena);
        
        NativeSafe.invoke(MPFR_ADD, prodR, aR, prodR, 0);
        NativeSafe.invoke(MPFR_ADD, prodI, aI, prodI, 0);
        
        complexLog(resR, resI, prodR, prodI, prec, arena);
        
        NativeSafe.invoke(MPFR_CLEAR, zp1R); NativeSafe.invoke(MPFR_CLEAR, zm1R);
        NativeSafe.invoke(MPFR_CLEAR, s1R); NativeSafe.invoke(MPFR_CLEAR, s1I);
        NativeSafe.invoke(MPFR_CLEAR, s2R); NativeSafe.invoke(MPFR_CLEAR, s2I);
        NativeSafe.invoke(MPFR_CLEAR, prodR); NativeSafe.invoke(MPFR_CLEAR, prodI);
    }
    
    public void complexAtanh(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // atanh(z) = 0.5 log((1+z)/(1-z))
        MemorySegment p1R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, p1R, prec);
        MemorySegment m1R = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, m1R, prec);
        NativeSafe.invoke(MPFR_SET_UI, p1R, 1L, 0);
        NativeSafe.invoke(MPFR_ADD, p1R, p1R, aR, 0);
        NativeSafe.invoke(MPFR_SET_UI, m1R, 1L, 0);
        NativeSafe.invoke(MPFR_SUB, m1R, m1R, aR, 0);
        
        MemorySegment m1I = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, m1I, prec);
        NativeSafe.invoke(MPFR_NEG, m1I, aI, 0);
        
        MemorySegment divR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, divR, prec);
        MemorySegment divI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, divI, prec);
        complexDivide(divR, divI, p1R, aI, m1R, m1I, prec, arena);
        
        complexLog(resR, resI, divR, divI, prec, arena);
        
        MemorySegment half = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, half, prec);
        NativeSafe.invoke(MPFR_SET_D, half, 0.5, 0);
        NativeSafe.invoke(MPFR_MUL, resR, resR, half, 0);
        NativeSafe.invoke(MPFR_MUL, resI, resI, half, 0);
        
        NativeSafe.invoke(MPFR_CLEAR, p1R); NativeSafe.invoke(MPFR_CLEAR, m1R);
        NativeSafe.invoke(MPFR_CLEAR, m1I); NativeSafe.invoke(MPFR_CLEAR, divR);
        NativeSafe.invoke(MPFR_CLEAR, divI); NativeSafe.invoke(MPFR_CLEAR, half);
    }
    
    public void complexCbrt(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, long prec, Arena arena) {
        // cbrt(z) = exp(1/3 * log(z))
        MemorySegment logR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, logR, prec);
        MemorySegment logI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, logI, prec);
        complexLog(logR, logI, aR, aI, prec, arena);
        
        MemorySegment three = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, three, prec);
        NativeSafe.invoke(MPFR_SET_UI, three, 3L, 0);
        
        NativeSafe.invoke(MPFR_DIV, logR, logR, three, 0);
        NativeSafe.invoke(MPFR_DIV, logI, logI, three, 0);
        
        complexExp(resR, resI, logR, logI, prec, arena);
        
        NativeSafe.invoke(MPFR_CLEAR, logR); NativeSafe.invoke(MPFR_CLEAR, logI);
        NativeSafe.invoke(MPFR_CLEAR, three);
    }

    private void checkDimensionsAdd(Matrix<?> a, Matrix<?> b) {
        if (a.rows() != b.rows() || a.cols() != b.cols()) throw new IllegalArgumentException("Matrix dimensions do not match");
    }

    public static MethodHandle getHandle(String function) {
        return switch (function.toLowerCase()) {
            case "exp" -> MPFR_EXP;
            case "log" -> MPFR_LOG;
            case "log10" -> MPFR_LOG10;
            case "sin" -> MPFR_SIN;
            case "cos" -> MPFR_COS;
            case "tan" -> MPFR_TAN;
            case "asin" -> MPFR_ASIN;
            case "acos" -> MPFR_ACOS;
            case "atan" -> MPFR_ATAN;
            case "sinh" -> MPFR_SINH;
            case "cosh" -> MPFR_COSH;
            case "tanh" -> MPFR_TANH;
            case "asinh" -> MPFR_ASINH;
            case "acosh" -> MPFR_ACOSH;
            case "atanh" -> MPFR_ATANH;
            case "sqrt" -> MPFR_SQRT;
            case "cbrt" -> MPFR_CBRT;
            case "pow" -> MPFR_POW;
            default -> null;
        };
    }

    public static void invokeComplexOp(String function, MemorySegment rcR, MemorySegment rcI, MemorySegment raR, MemorySegment raI, long prec, Arena arena) {
        NativeMPFRDenseLinearAlgebraBackend<Object> backend = new NativeMPFRDenseLinearAlgebraBackend<>();
        switch (function.toLowerCase()) {
            case "exp" -> backend.complexExp(rcR, rcI, raR, raI, prec, arena);
            case "log" -> backend.complexLog(rcR, rcI, raR, raI, prec, arena);
            case "log10" -> backend.complexLog10(rcR, rcI, raR, raI, prec, arena);
            case "sin" -> backend.complexSin(rcR, rcI, raR, raI, prec, arena);
            case "cos" -> backend.complexCos(rcR, rcI, raR, raI, prec, arena);
            case "tan" -> backend.complexTan(rcR, rcI, raR, raI, prec, arena);
            case "sinh" -> backend.complexSinh(rcR, rcI, raR, raI, prec, arena);
            case "cosh" -> backend.complexCosh(rcR, rcI, raR, raI, prec, arena);
            case "tanh" -> backend.complexTanh(rcR, rcI, raR, raI, prec, arena);
            case "sqrt" -> backend.complexSqrt(rcR, rcI, raR, raI, prec, arena);
            case "asin" -> backend.complexAsin(rcR, rcI, raR, raI, prec, arena);
            case "acos" -> backend.complexAcos(rcR, rcI, raR, raI, prec, arena);
            case "atan" -> backend.complexAtan(rcR, rcI, raR, raI, prec, arena);
            case "asinh" -> backend.complexAsinh(rcR, rcI, raR, raI, prec, arena);
            case "acosh" -> backend.complexAcosh(rcR, rcI, raR, raI, prec, arena);
            case "atanh" -> backend.complexAtanh(rcR, rcI, raR, raI, prec, arena);
            case "cbrt" -> backend.complexCbrt(rcR, rcI, raR, raI, prec, arena);
            default -> throw new UnsupportedOperationException("Native complex op not implemented: " + function);
        }
    }
    public void complexPow(MemorySegment resR, MemorySegment resI, MemorySegment aR, MemorySegment aI, MemorySegment eR, MemorySegment eI, long prec, Arena arena) {
        // z^w = exp(w * log(z))
        MemorySegment logR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, logR, prec);
        MemorySegment logI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, logI, prec);
        complexLog(logR, logI, aR, aI, prec, arena);
        
        MemorySegment wLogR = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, wLogR, prec);
        MemorySegment wLogI = arena.allocate(MPFR_LAYOUT); NativeSafe.invoke(MPFR_INIT2, wLogI, prec);
        complexMultiply(wLogR, wLogI, eR, eI, logR, logI, prec, arena);
        
        complexExp(resR, resI, wLogR, wLogI, prec, arena);
        
        NativeSafe.invoke(MPFR_CLEAR, logR); NativeSafe.invoke(MPFR_CLEAR, logI);
        NativeSafe.invoke(MPFR_CLEAR, wLogR); NativeSafe.invoke(MPFR_CLEAR, wLogI);
    }
}
