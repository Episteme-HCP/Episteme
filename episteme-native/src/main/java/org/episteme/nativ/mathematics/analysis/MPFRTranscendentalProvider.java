/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.analysis;

import com.google.auto.service.AutoService;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.math.BigDecimal;
import java.math.MathContext;
import org.episteme.core.mathematics.numbers.real.TranscendentalProvider;
import org.episteme.nativ.technical.backend.nativ.NativeSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.episteme.nativ.mathematics.analysis.NativeMPFRNumbers.*;

/**
 * MPFR-backed implementation of TranscendentalProvider.
 * Provides high-precision transcendental functions via libmpfr.
 *
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService(TranscendentalProvider.class)
public final class MPFRTranscendentalProvider implements TranscendentalProvider {
    private static final Logger logger = LoggerFactory.getLogger(MPFRTranscendentalProvider.class);

    @Override public String getName() { return "Native MPFR Transcendental Provider"; }
    @Override public int getPriority() { return 120; }
    @Override public boolean isAvailable() { return AVAILABLE; }

    @Override
    public BigDecimal compute(String function, BigDecimal value, MathContext mc) {
        if (!AVAILABLE) throw new UnsupportedOperationException("MPFR not available");
        
        try (Arena arena = Arena.ofConfined()) {
            long prec = (long) (mc.getPrecision() * 3.322) + 12; // digits to bits + safety
            MemorySegment h_value = allocateMPFR(value, arena, prec);
            MemorySegment h_result = allocate(arena, prec);
            
            java.lang.invoke.MethodHandle handle = switch (function.toLowerCase()) {
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
                case "cbrt" -> MPFR_CBRT;
                default -> throw new UnsupportedOperationException("Function not supported: " + function);
            };

            NativeSafe.invoke(handle, h_result, h_value, 0); // 0 = RNDN (Round to nearest)
            
            return readMPFR(h_result, arena, mc);
        } catch (Throwable t) {
            logger.error("MPFR computation failed for {}: {}", function, t.getMessage());
            throw new RuntimeException("MPFR failure: " + t.getMessage(), t);
        }
    }

    @Override
    public BigDecimal compute(String function, BigDecimal v1, BigDecimal v2, MathContext mc) {
        if (!AVAILABLE) throw new UnsupportedOperationException("MPFR not available");
        
        try (Arena arena = Arena.ofConfined()) {
            long prec = (long) (mc.getPrecision() * 3.322) + 12;
            MemorySegment h_v1 = allocateMPFR(v1, arena, prec);
            MemorySegment h_v2 = allocateMPFR(v2, arena, prec);
            MemorySegment h_result = allocate(arena, prec);
            
            java.lang.invoke.MethodHandle handle = switch (function.toLowerCase()) {
                case "atan2" -> MPFR_ATAN2;
                case "hypot" -> MPFR_HYPOT;
                case "pow" -> MPFR_POW;
                default -> throw new UnsupportedOperationException("Two-arg function not supported: " + function);
            };

            NativeSafe.invoke(handle, h_result, h_v1, h_v2, 0);
            
            return readMPFR(h_result, arena, mc);
        } catch (Throwable t) {
            logger.error("MPFR binary computation failed for {}: {}", function, t.getMessage());
            throw new RuntimeException("MPFR failure: " + t.getMessage(), t);
        }
    }

    // --- Helper methods (consistent with NativeMPFRDenseLinearAlgebraProvider) ---

    private MemorySegment allocate(Arena arena, long prec) {
        MemorySegment ptr = arena.allocate(32); // mpfr_t size estimate
        NativeSafe.invoke(MPFR_INIT2, ptr, prec);
        return ptr;
    }

    private MemorySegment allocateMPFR(BigDecimal value, Arena arena, long prec) {
        MemorySegment ptr = allocate(arena, prec);
        MemorySegment str = arena.allocateFrom(value.toPlainString());
        NativeSafe.invoke(MPFR_SET_STR, ptr, str, 10, 0);
        return ptr;
    }

    private BigDecimal readMPFR(MemorySegment ptr, Arena arena, MathContext mc) {
        // Reuse logic from linear algebra provider
        MemorySegment expPtr = arena.allocate(java.lang.foreign.ValueLayout.JAVA_LONG);
        MemorySegment strPtr = (MemorySegment) NativeSafe.invoke(MPFR_GET_STR, MemorySegment.NULL, expPtr, 10, 0L, ptr, 0);
        
        try {
            String digits = strPtr.getString(0);
            long exp = expPtr.get(java.lang.foreign.ValueLayout.JAVA_LONG, 0);
            
            // Format: 0.[digits] * 10^exp
            StringBuilder sb = new StringBuilder();
            if (digits.startsWith("-")) {
                sb.append("-");
                digits = digits.substring(1);
            }
            sb.append("0.");
            sb.append(digits);
            sb.append("E");
            sb.append(exp);
            
            return new BigDecimal(sb.toString(), mc);
        } finally {
            NativeSafe.invoke(MPFR_FREE_STR, strPtr);
        }
    }
}
