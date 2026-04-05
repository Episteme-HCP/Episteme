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
    
    // Platform-dependent C 'long' size
    public static final ValueLayout.OfLong JAVA_LONG = ValueLayout.JAVA_LONG;
    public static final ValueLayout C_LONG = System.getProperty("os.name").toLowerCase().contains("win") 
                                            ? ValueLayout.JAVA_INT : ValueLayout.JAVA_LONG;
    
    // MPFR Function Handles
    public static final MethodHandle MPFR_EXP;
    public static final MethodHandle MPFR_LOG;
    public static final MethodHandle MPFR_LOG10;
    public static final MethodHandle MPFR_SIN;
    public static final MethodHandle MPFR_COS;
    public static final MethodHandle MPFR_TAN;
    public static final MethodHandle MPFR_ASIN;
    public static final MethodHandle MPFR_ACOS;
    public static final MethodHandle MPFR_ATAN;
    public static final MethodHandle MPFR_ATAN2;
    public static final MethodHandle MPFR_SINH;
    public static final MethodHandle MPFR_COSH;
    public static final MethodHandle MPFR_TANH;
    public static final MethodHandle MPFR_ASINH;
    public static final MethodHandle MPFR_ACOSH;
    public static final MethodHandle MPFR_ATANH;
    public static final MethodHandle MPFR_CBRT;
    public static final MethodHandle MPFR_SQRT;
    public static final MethodHandle MPFR_HYPOT;
    public static final MethodHandle MPFR_POW;
    
    // Arithmetic Handles
    public static final MethodHandle MPFR_ADD;
    public static final MethodHandle MPFR_SUB;
    public static final MethodHandle MPFR_MUL;
    public static final MethodHandle MPFR_DIV;
    public static final MethodHandle MPFR_NEG;
    public static final MethodHandle MPFR_ABS;
    public static final MethodHandle MPFR_CMP;
    public static final MethodHandle MPFR_SET;
    public static final MethodHandle MPFR_SET_UI;
    public static final MethodHandle MPFR_CMP_ABS;
    public static final MethodHandle MPFR_SET_D;
    public static final MethodHandle MPFR_CONST_PI;
    public static final MethodHandle MPFR_ZERO_P;
    public static final MethodHandle MPFR_NAN_P;
    public static final MethodHandle MPFR_INF_P;
    public static final MethodHandle MPFR_NUMBER_P;
    public static final MethodHandle MPFR_CMP_SI;

    public static final MemoryLayout MPFR_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_INT.withName("prec"),
        ValueLayout.JAVA_INT.withName("sign"),
        ValueLayout.JAVA_INT.withName("exp"),
        MemoryLayout.paddingLayout(4),
        ValueLayout.ADDRESS.withName("d")
    );
    
    // Core MPFR management
    public static final MethodHandle MPFR_INIT2;
    public static final MethodHandle MPFR_CLEAR;
    public static final MethodHandle MPFR_SET_STR;
    public static final MethodHandle MPFR_GET_STR;
    public static final MethodHandle MPFR_FREE_STR;

    public static final boolean AVAILABLE;

    static {
        boolean available = false;
        MethodHandle exp = null, log = null, log10 = null, sin = null, cos = null, tan = null;
        MethodHandle asin = null, acos = null, atan = null, atan2 = null;
        MethodHandle sinh = null, cosh = null, tanh = null, asinh = null, acosh = null, atanh = null;
        MethodHandle cbrt = null, sqrt = null, hypot = null, pow = null;
        MethodHandle add = null, sub = null, mul = null, div = null, neg = null, abs = null, cmp = null;
        MethodHandle set = null, set_ui = null, cmp_abs = null, zero_p = null, set_d = null, const_pi = null;
        MethodHandle nan_p = null, inf_p = null, number_p = null, cmp_si = null;
        MethodHandle init2 = null, clear = null, setStr = null, getStr = null, freeStr = null;

        try {
            Optional<SymbolLookup> mpfrLookup = NativeFFMLoader.loadLibrary("mpfr", Arena.global());
            if (mpfrLookup.isPresent()) {
                SymbolLookup mpfr = mpfrLookup.get();
                
                // One-arg functions: (rop, op, rnd)
                FunctionDescriptor oneArg = FunctionDescriptor.of(JAVA_INT, ADDRESS, ADDRESS, JAVA_INT);
                exp = lookup(mpfr, "mpfr_exp", oneArg);
                log = lookup(mpfr, "mpfr_log", oneArg);
                log10 = lookup(mpfr, "mpfr_log10", oneArg);
                sin = lookup(mpfr, "mpfr_sin", oneArg);
                cos = lookup(mpfr, "mpfr_cos", oneArg);
                tan = lookup(mpfr, "mpfr_tan", oneArg);
                asin = lookup(mpfr, "mpfr_asin", oneArg);
                acos = lookup(mpfr, "mpfr_acos", oneArg);
                atan = lookup(mpfr, "mpfr_atan", oneArg);
                sinh = lookup(mpfr, "mpfr_sinh", oneArg);
                cosh = lookup(mpfr, "mpfr_cosh", oneArg);
                tanh = lookup(mpfr, "mpfr_tanh", oneArg);
                asinh = lookup(mpfr, "mpfr_asinh", oneArg);
                acosh = lookup(mpfr, "mpfr_acosh", oneArg);
                atanh = lookup(mpfr, "mpfr_atanh", oneArg);
                cbrt = lookup(mpfr, "mpfr_cbrt", oneArg);
                sqrt = lookup(mpfr, "mpfr_sqrt", oneArg);
                
                // Two-arg functions: (rop, op1, op2, rnd)
                FunctionDescriptor twoArg = FunctionDescriptor.of(JAVA_INT, ADDRESS, ADDRESS, ADDRESS, JAVA_INT);
                atan2 = lookup(mpfr, "mpfr_atan2", twoArg);
                hypot = lookup(mpfr, "mpfr_hypot", twoArg);
                pow = lookup(mpfr, "mpfr_pow", twoArg);

                // Arithmetic functions: (rop, op1, op2, rnd)
                add = lookup(mpfr, "mpfr_add", twoArg);
                sub = lookup(mpfr, "mpfr_sub", twoArg);
                mul = lookup(mpfr, "mpfr_mul", twoArg);
                div = lookup(mpfr, "mpfr_div", twoArg);

                // Arithmetic functions: (rop, op, rnd)
                neg = lookup(mpfr, "mpfr_neg", oneArg);
                abs = lookup(mpfr, "mpfr_abs", oneArg);

                // Comparison: (op1, op2)
                cmp = lookup(mpfr, "mpfr_cmp", FunctionDescriptor.of(JAVA_INT, ADDRESS, ADDRESS));

                // Set functions
                set = lookup(mpfr, "mpfr_set", FunctionDescriptor.of(JAVA_INT, ADDRESS, ADDRESS, JAVA_INT));
                set_ui = lookup(mpfr, "mpfr_set_ui", FunctionDescriptor.of(JAVA_INT, ADDRESS, JAVA_LONG, JAVA_INT));
                set_d = lookup(mpfr, "mpfr_set_d", FunctionDescriptor.of(JAVA_INT, ADDRESS, ValueLayout.JAVA_DOUBLE, JAVA_INT));

                // Misc/Analysis
                cmp_abs = lookup(mpfr, "mpfr_cmpabs", FunctionDescriptor.of(JAVA_INT, ADDRESS, ADDRESS));
                zero_p = lookup(mpfr, "mpfr_zero_p", FunctionDescriptor.of(JAVA_INT, ADDRESS));
                nan_p = lookup(mpfr, "mpfr_nan_p", FunctionDescriptor.of(JAVA_INT, ADDRESS));
                inf_p = lookup(mpfr, "mpfr_inf_p", FunctionDescriptor.of(JAVA_INT, ADDRESS));
                number_p = lookup(mpfr, "mpfr_number_p", FunctionDescriptor.of(JAVA_INT, ADDRESS));
                cmp_si = lookup(mpfr, "mpfr_cmp_si", FunctionDescriptor.of(JAVA_INT, ADDRESS, C_LONG));
                const_pi = lookup(mpfr, "mpfr_const_pi", FunctionDescriptor.of(JAVA_INT, ADDRESS, JAVA_INT));
                
                // Management functions
                init2 = lookup(mpfr, "mpfr_init2", FunctionDescriptor.ofVoid(ADDRESS, ValueLayout.JAVA_LONG));
                clear = lookup(mpfr, "mpfr_clear", FunctionDescriptor.ofVoid(ADDRESS));
                setStr = lookup(mpfr, "mpfr_set_str", FunctionDescriptor.of(JAVA_INT, ADDRESS, ADDRESS, JAVA_INT, JAVA_INT));
                getStr = lookup(mpfr, "mpfr_get_str", FunctionDescriptor.of(ADDRESS, ADDRESS, ADDRESS, JAVA_INT, ValueLayout.JAVA_LONG, ADDRESS, JAVA_INT));
                freeStr = lookup(mpfr, "mpfr_free_str", FunctionDescriptor.ofVoid(ADDRESS));
                
                available = init2 != null && exp != null && log != null && sin != null && cos != null;
                if (available) {
                    logger.info("Native MPFR Transcendental Backend initialized (Panama).");
                } else {
                    logger.warn("Native MPFR Transcendental Backend partial loading - check handles.");
                }
            }
        } catch (Throwable t) {
            logger.warn("Failed to initialize Native MPFR Numbers: {}", t.getMessage());
        }
        
        MPFR_EXP = exp; MPFR_LOG = log; MPFR_LOG10 = log10; MPFR_SIN = sin; MPFR_COS = cos; MPFR_TAN = tan;
        MPFR_ASIN = asin; MPFR_ACOS = acos; MPFR_ATAN = atan; MPFR_ATAN2 = atan2;
        MPFR_SINH = sinh; MPFR_COSH = cosh; MPFR_TANH = tanh; MPFR_ASINH = asinh; MPFR_ACOSH = acosh; MPFR_ATANH = atanh;
        MPFR_CBRT = cbrt; MPFR_SQRT = sqrt; MPFR_HYPOT = hypot; MPFR_POW = pow;
        MPFR_ADD = add; MPFR_SUB = sub; MPFR_MUL = mul; MPFR_DIV = div; MPFR_NEG = neg; MPFR_ABS = abs; MPFR_CMP = cmp;
        MPFR_SET = set; MPFR_SET_UI = set_ui; MPFR_CMP_ABS = cmp_abs; MPFR_ZERO_P = zero_p; MPFR_SET_D = set_d; MPFR_CONST_PI = const_pi;
        MPFR_NAN_P = nan_p; MPFR_INF_P = inf_p; MPFR_NUMBER_P = number_p; MPFR_CMP_SI = cmp_si;
        MPFR_INIT2 = init2; MPFR_CLEAR = clear; MPFR_SET_STR = setStr; MPFR_GET_STR = getStr; MPFR_FREE_STR = freeStr;
        AVAILABLE = available;
        if (AVAILABLE) {
            logger.info("NativeMPFRNumbers successfully initialized with MPFR library.");
        } else {
            logger.warn("NativeMPFRNumbers failed to initialize. High-precision transcendental functions will be unavailable.");
        }
    }

    public static MethodHandle lookup(SymbolLookup lookup, String name, FunctionDescriptor desc) {
        return lookup.find(name).map(s -> LINKER.downcallHandle(s, desc)).orElse(null);
    }

    public static boolean isAvailable() {
        return AVAILABLE;
    }

    private NativeMPFRNumbers() {}
}
