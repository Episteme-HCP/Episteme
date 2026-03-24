/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.analysis;

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
        MPFR_INIT2 = init2; MPFR_CLEAR = clear; MPFR_SET_STR = setStr; MPFR_GET_STR = getStr; MPFR_FREE_STR = freeStr;
        AVAILABLE = available;
        if (AVAILABLE) {
            logger.info("NativeMPFRNumbers successfully initialized with MPFR library.");
        } else {
            logger.warn("NativeMPFRNumbers failed to initialize. High-precision transcendental functions will be unavailable.");
        }
    }

    private static MethodHandle lookup(SymbolLookup lookup, String name, FunctionDescriptor desc) {
        return lookup.find(name).map(s -> LINKER.downcallHandle(s, desc)).orElse(null);
    }

    private NativeMPFRNumbers() {}
}
