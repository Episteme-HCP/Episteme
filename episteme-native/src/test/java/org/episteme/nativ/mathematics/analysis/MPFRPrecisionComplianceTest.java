/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.analysis;

import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.episteme.nativ.mathematics.numbers.real.backends.NativeMPFRNumbers;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

/**
 * Validation tests for 1000-digit precision compliance using MPFR.
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class MPFRPrecisionComplianceTest {
    private static final Logger logger = LoggerFactory.getLogger(MPFRPrecisionComplianceTest.class);

    @BeforeAll
    public static void setup() {
        System.setProperty("org.episteme.core.math.precision.exact", "1000");
    }

    @Test
    public void testHighPrecisionExp() {
        if (!NativeMPFRNumbers.AVAILABLE) {
            logger.warn("MPFR not available, skipping precision test.");
            return;
        }

        MathContext.exact().compute(() -> {
            Real x = Real.of("1.0");
            Real expX = x.exp();
            
            String digits = expX.toString();
            // e = 2.71828182845904523536028747135266249775724709369995...
            assertTrue(digits.startsWith("2.71828182845904523536"), "e value mismatch");
            assertTrue(digits.length() >= 1000, "Should have at least 1000 digits of precision");
            return null;
        });
    }

    @Test
    public void testHighPrecisionLog() {
        if (!NativeMPFRNumbers.AVAILABLE) return;
        MathContext.exact().compute(() -> {
            Real x = Real.of("2.0");
            Real logX = x.log();
            String digits = logX.toString();
            // ln(2) = 0.69314718055994530941723212145817656807550013436025...
            assertTrue(digits.startsWith("0.69314718055994530941"), "ln(2) mismatch");
            assertTrue(digits.length() >= 1000, "Should have 1000 digits");
            return null;
        });
    }

    @Test
    public void testHighPrecisionSinCos() {
        if (!NativeMPFRNumbers.AVAILABLE) return;
        MathContext.exact().compute(() -> {
            Real pi = Real.PI;
            Real x = pi.divide(Real.of(4)); // 45 degrees
            Real sinX = x.sin();
            Real cosX = x.cos();
            
            // sin(pi/4) = cos(pi/4) = 1/sqrt(2) = 0.70710678118654752440...
            assertTrue(sinX.toString().startsWith("0.70710678118654752440"), "sin(pi/4) mismatch");
            assertTrue(cosX.toString().startsWith("0.70710678118654752440"), "cos(pi/4) mismatch");
            
            // sin^2 + cos^2 = 1
            Real one = sinX.multiply(sinX).add(cosX.multiply(cosX));
            assertTrue(one.subtract(Real.ONE).abs().compareTo(Real.of("1E-995")) < 0, "Identity sin^2 + cos^2 = 1 failed at high precision");
            return null;
        });
    }

    @Test
    public void testHighPrecisionPow() {
        if (!NativeMPFRNumbers.AVAILABLE) return;
        MathContext.exact().compute(() -> {
            Real base = Real.of("2.0");
            Real exponent = Real.of("0.5");
            Real result = base.pow(exponent); // sqrt(2)
            
            String digits = result.toString();
            // sqrt(2) = 1.41421356237309504880...
            assertTrue(digits.startsWith("1.41421356237309504880"), "pow(2, 0.5) mismatch");
            return null;
        });
    }

    @Test
    public void testHighPrecisionAtan2() {
        if (!NativeMPFRNumbers.AVAILABLE) return;
        MathContext.exact().compute(() -> {
            Real y = Real.of("1.0");
            Real x = Real.of("1.0");
            Real result = y.atan2(x); // pi/4
            
            Real pi4 = Real.PI.divide(Real.of(4));
            assertTrue(result.subtract(pi4).abs().compareTo(Real.of("1E-995")) < 0, "atan2(1, 1) mismatch");
            return null;
        });
    }

    @Test
    public void testExactContextTrigger() {
        MathContext.exact().compute(() -> {
            Real a = Real.of("1.1");
            Real b = Real.of("2.2");
            Real c = a.add(b);
            // Since precision is 1000, it should be exact for these simple values
            assertTrue(c.toString().startsWith("3.3"), "Exact context addition failed");
            return null;
        });
    }
}
