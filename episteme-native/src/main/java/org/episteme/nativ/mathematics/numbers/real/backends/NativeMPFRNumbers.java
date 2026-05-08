/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.numbers.real.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

/**
 * Native MethodHandles for MPFR transcendental functions.
 * Provides access to high-precision exp, log, sin, cos, etc.
 *
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class NativeMPFRNumbers {
    private static final Logger logger = LoggerFactory.getLogger(NativeMPFRNumbers.class);
    private static final Linker LINKER = Linker.nativeLinker();
    
    // Platform-dependent C 'long' size - Windows 'long' is always 32-bit (LLP64), Linux/macOS 'long' is 64-bit (LP64)
    public static final ValueLayout JAVA_LONG = ValueLayout.JAVA_LONG;
    public static final ValueLayout C_LONG = (Linker.nativeLinker().canonicalLayouts().get("long") != null && 
                                              Linker.nativeLinker().canonicalLayouts().get("long").byteSize() == 4)
                                              ? ValueLayout.JAVA_INT : ValueLayout.JAVA_LONG;
    
    public static Object c_long(long value) {
        if (C_LONG.byteSize() == 4) return (int) value;
        return value;
    }
    
    // MPFR Function Handles
    public static MethodHandle MPFR_EXP;
    public static MethodHandle MPFR_LOG;
    public static MethodHandle MPFR_LOG10;
    public static MethodHandle MPFR_SIN;
    public static MethodHandle MPFR_COS;
    public static MethodHandle MPFR_TAN;
    public static MethodHandle MPFR_ASIN;
    public static MethodHandle MPFR_ACOS;
    public static MethodHandle MPFR_ATAN;
    public static MethodHandle MPFR_ATAN2;
    public static MethodHandle MPFR_SINH;
    public static MethodHandle MPFR_COSH;
    public static MethodHandle MPFR_TANH;
    public static MethodHandle MPFR_ASINH;
    public static MethodHandle MPFR_ACOSH;
    public static MethodHandle MPFR_ATANH;
    public static MethodHandle MPFR_CBRT;
    public static MethodHandle MPFR_SQRT;
    public static MethodHandle MPFR_HYPOT;
    public static MethodHandle MPFR_POW;
    
    // Arithmetic Handles
    public static MethodHandle MPFR_ADD;
    public static MethodHandle MPFR_SUB;
    public static MethodHandle MPFR_MUL;
    public static MethodHandle MPFR_DIV;
    public static MethodHandle MPFR_NEG;
    public static MethodHandle MPFR_ABS;
    public static MethodHandle MPFR_CMP;
    public static MethodHandle MPFR_SET;
    public static MethodHandle MPFR_SET_UI;
    public static MethodHandle MPFR_SET_SI;
    public static MethodHandle MPFR_CMP_ABS;
    public static MethodHandle MPFR_SET_D;
    public static MethodHandle MPFR_CONST_PI;
    public static MethodHandle MPFR_ZERO_P;
    public static MethodHandle MPFR_NAN_P;
    public static MethodHandle MPFR_INF_P;
    public static MethodHandle MPFR_NUMBER_P;
    public static MethodHandle MPFR_CMP_SI;
    public static MethodHandle MPFR_SET_INF;
    public static MethodHandle MPFR_SET_NAN;
    public static MethodHandle MPFR_SET_ZERO;

    public static final MemoryLayout MPFR_LAYOUT;
    
    // Core MPFR management
    public static MethodHandle MPFR_INIT2;
    public static MethodHandle MPFR_CLEAR;
    public static MethodHandle MPFR_SET_STR;
    public static MethodHandle MPFR_GET_STR;
    public static MethodHandle MPFR_FREE_STR;
    public static MethodHandle MPFR_GET_D;

    private static boolean initAttempted = false;
    private static boolean AVAILABLE = false;

    public static synchronized void ensureInitialized() {
        if (initAttempted) return;
        initAttempted = true;

        if (Boolean.getBoolean("episteme.backend.native.disabled") ||
            Boolean.getBoolean("episteme.backend.mpfr.disabled") || 
            (Boolean.getBoolean("episteme.backend.mpfr-dense.disabled") && Boolean.getBoolean("episteme.backend.mpfr-sparse.disabled"))) {
            logger.info("Native MPFR: Disabled by system property.");
            return;
        }

        try {
            Optional<SymbolLookup> mpfrLookup = NativeFFMLoader.loadLibrary("mpfr", Arena.global());
            if (mpfrLookup.isPresent()) {
                SymbolLookup mpfr = mpfrLookup.get();
                
                // One-arg functions: (rop, op, rnd)
                FunctionDescriptor oneArg = FunctionDescriptor.of(JAVA_INT, ADDRESS, ADDRESS, JAVA_INT);
                MPFR_EXP = lookup(mpfr, "mpfr_exp", oneArg);
                MPFR_LOG = lookup(mpfr, "mpfr_log", oneArg);
                MPFR_LOG10 = lookup(mpfr, "mpfr_log10", oneArg);
                MPFR_SIN = lookup(mpfr, "mpfr_sin", oneArg);
                MPFR_COS = lookup(mpfr, "mpfr_cos", oneArg);
                MPFR_TAN = lookup(mpfr, "mpfr_tan", oneArg);
                MPFR_ASIN = lookup(mpfr, "mpfr_asin", oneArg);
                MPFR_ACOS = lookup(mpfr, "mpfr_acos", oneArg);
                MPFR_ATAN = lookup(mpfr, "mpfr_atan", oneArg);
                MPFR_SINH = lookup(mpfr, "mpfr_sinh", oneArg);
                MPFR_COSH = lookup(mpfr, "mpfr_cosh", oneArg);
                MPFR_TANH = lookup(mpfr, "mpfr_tanh", oneArg);
                MPFR_ASINH = lookup(mpfr, "mpfr_asinh", oneArg);
                MPFR_ACOSH = lookup(mpfr, "mpfr_acosh", oneArg);
                MPFR_ATANH = lookup(mpfr, "mpfr_atanh", oneArg);
                MPFR_CBRT = lookup(mpfr, "mpfr_cbrt", oneArg);
                MPFR_SQRT = lookup(mpfr, "mpfr_sqrt", oneArg);
                
                // Two-arg functions: (rop, op1, op2, rnd)
                FunctionDescriptor twoArg = FunctionDescriptor.of(JAVA_INT, ADDRESS, ADDRESS, ADDRESS, JAVA_INT);
                MPFR_ATAN2 = lookup(mpfr, "mpfr_atan2", twoArg);
                MPFR_HYPOT = lookup(mpfr, "mpfr_hypot", twoArg);
                MPFR_POW = lookup(mpfr, "mpfr_pow", twoArg);

                // Arithmetic functions: (rop, op1, op2, rnd)
                MPFR_ADD = lookup(mpfr, "mpfr_add", twoArg);
                MPFR_SUB = lookup(mpfr, "mpfr_sub", twoArg);
                MPFR_MUL = lookup(mpfr, "mpfr_mul", twoArg);
                MPFR_DIV = lookup(mpfr, "mpfr_div", twoArg);

                // Arithmetic functions: (rop, op, rnd)
                MPFR_NEG = lookup(mpfr, "mpfr_neg", oneArg);
                MPFR_ABS = lookup(mpfr, "mpfr_abs", oneArg);

                // Comparison: (op1, op2)
                MPFR_CMP = lookup(mpfr, "mpfr_cmp", FunctionDescriptor.of(JAVA_INT, ADDRESS, ADDRESS));

                // Set functions
                MPFR_SET = lookup(mpfr, "mpfr_set", FunctionDescriptor.of(JAVA_INT, ADDRESS, ADDRESS, JAVA_INT));
                MPFR_SET_UI = lookup(mpfr, "mpfr_set_ui", FunctionDescriptor.of(JAVA_INT, ADDRESS, C_LONG, JAVA_INT));
                MPFR_SET_SI = lookup(mpfr, "mpfr_set_si", FunctionDescriptor.of(JAVA_INT, ADDRESS, C_LONG, JAVA_INT));
                MPFR_SET_D = lookup(mpfr, "mpfr_set_d", FunctionDescriptor.of(JAVA_INT, ADDRESS, ValueLayout.JAVA_DOUBLE, JAVA_INT));

                // Misc/Analysis
                MPFR_CMP_ABS = lookup(mpfr, "mpfr_cmpabs", FunctionDescriptor.of(JAVA_INT, ADDRESS, ADDRESS));
                MPFR_ZERO_P = lookup(mpfr, "mpfr_zero_p", FunctionDescriptor.of(JAVA_INT, ADDRESS));
                MPFR_NAN_P = lookup(mpfr, "mpfr_nan_p", FunctionDescriptor.of(JAVA_INT, ADDRESS));
                MPFR_INF_P = lookup(mpfr, "mpfr_inf_p", FunctionDescriptor.of(JAVA_INT, ADDRESS));
                MPFR_NUMBER_P = lookup(mpfr, "mpfr_number_p", FunctionDescriptor.of(JAVA_INT, ADDRESS));
                MPFR_CMP_SI = lookup(mpfr, "mpfr_cmp_si", FunctionDescriptor.of(JAVA_INT, ADDRESS, C_LONG));
                MPFR_CONST_PI = lookup(mpfr, "mpfr_const_pi", FunctionDescriptor.of(JAVA_INT, ADDRESS, JAVA_INT));
                MPFR_SET_INF = lookup(mpfr, "mpfr_set_inf", FunctionDescriptor.ofVoid(ADDRESS, JAVA_INT));
                MPFR_SET_NAN = lookup(mpfr, "mpfr_set_nan", FunctionDescriptor.ofVoid(ADDRESS));
                MPFR_SET_ZERO = lookup(mpfr, "mpfr_set_zero", FunctionDescriptor.ofVoid(ADDRESS, JAVA_INT));
                MPFR_GET_D = lookup(mpfr, "mpfr_get_d", FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE, ADDRESS, JAVA_INT));
                
                // Management functions
                MPFR_INIT2 = lookup(mpfr, "mpfr_init2", FunctionDescriptor.ofVoid(ADDRESS, C_LONG));
                MPFR_CLEAR = lookup(mpfr, "mpfr_clear", FunctionDescriptor.ofVoid(ADDRESS));
                MPFR_SET_STR = lookup(mpfr, "mpfr_set_str", FunctionDescriptor.of(JAVA_INT, ADDRESS, ADDRESS, JAVA_INT, JAVA_INT));
                MPFR_GET_STR = lookup(mpfr, "mpfr_get_str", FunctionDescriptor.of(ADDRESS, ADDRESS, ADDRESS, JAVA_INT, ValueLayout.JAVA_LONG, ADDRESS, JAVA_INT));
                MPFR_FREE_STR = lookup(mpfr, "mpfr_free_str", FunctionDescriptor.ofVoid(ADDRESS));
                
                AVAILABLE = MPFR_INIT2 != null && MPFR_EXP != null && MPFR_LOG != null && MPFR_SIN != null && MPFR_COS != null;
            }
        } catch (Throwable t) {
            logger.warn("Failed to initialize Native MPFR Numbers: {}", t.getMessage());
        }
    }

    static {
        // Define layout dynamically based on C_LONG size
        if (C_LONG.byteSize() == 8) {
            MPFR_LAYOUT = MemoryLayout.structLayout(
                C_LONG.withName("prec"),      // offset 0
                JAVA_INT.withName("sign"),    // offset 8
                MemoryLayout.paddingLayout(4), // offset 12
                C_LONG.withName("exp"),       // offset 16
                ADDRESS.withName("d")         // offset 24
            ).withName("__mpfr_struct_64");
        } else {
            MPFR_LAYOUT = MemoryLayout.structLayout(
                C_LONG.withName("prec"),      // offset 0 (4 bytes)
                JAVA_INT.withName("sign"),    // offset 4 (4 bytes)
                C_LONG.withName("exp"),       // offset 8 (4 bytes)
                MemoryLayout.paddingLayout(4), // offset 12 (4 bytes padding)
                ADDRESS.withName("d")         // offset 16 (8 bytes)
            ).withName("__mpfr_struct_32");
        }
    }

    public static MethodHandle lookup(SymbolLookup lookup, String name, FunctionDescriptor desc) {
        return lookup.find(name).map(s -> LINKER.downcallHandle(s, desc)).orElse(null);
    }

    public static boolean isAvailable() {
        ensureInitialized();
        return AVAILABLE;
    }

    // --- Safe Utility Wrappers ---
    
    /**
     * Centralized mpfr_sqrt implementation with safety checks.
     */
    public static int sqrt(MemorySegment rop, MemorySegment op, int rnd) {
        ensureInitialized();
        if (!AVAILABLE) throw new IllegalStateException("MPFR not available");
        try {
            return (int) MPFR_SQRT.invokeExact(rop, op, rnd);
        } catch (Throwable t) {
            throw new RuntimeException("mpfr_sqrt failed", t);
        }
    }

    /**
     * Universal comparison utility for mpfr_t.
     */
    public static int compare(MemorySegment op1, MemorySegment op2) {
        ensureInitialized();
        if (!AVAILABLE) throw new IllegalStateException("MPFR not available");
        try {
            return (int) MPFR_CMP.invokeExact(op1, op2);
        } catch (Throwable t) {
            throw new RuntimeException("mpfr_cmp failed", t);
        }
    }

    /**
     * Returns a native constant by name at the specified precision.
     */
    public static org.episteme.nativ.mathematics.numbers.real.NativeRealBig getConstant(String name, long precision) {
        ensureInitialized();
        if (!AVAILABLE) return null;
        
        org.episteme.nativ.mathematics.numbers.real.NativeRealBig res = org.episteme.nativ.mathematics.numbers.real.NativeRealBig.createEmpty(precision);
        
        try {
            if ("pi".equals(name)) {
                MPFR_CONST_PI.invokeExact(res.getPtr(), 0); // 0 = RNDN
                return res;
            }
            if ("e".equals(name)) {
                // e = exp(1)
                org.episteme.nativ.mathematics.numbers.real.NativeRealBig one = org.episteme.nativ.mathematics.numbers.real.NativeRealBig.of("1.0", precision);
                MPFR_EXP.invokeExact(res.getPtr(), one.getPtr(), 0);
                return res;
            }
        } catch (Throwable t) {
            logger.error("Failed to get native constant {}: {}", name, t.getMessage());
        }
        return null;
    }

    private NativeMPFRNumbers() {}
}
