/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.context;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated baseline test for MathContext.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathContextTest {

    @Test
    public void testExactPrecision() {
        MathContext exact = MathContext.exact();
        assertEquals(1000, exact.getJavaMathContext().getPrecision());
    }

    @Test
    public void testHighPrecisionCalculation() {
        // Test 1/3 with 1000 digits
        java.math.BigDecimal one = new java.math.BigDecimal("1");
        java.math.BigDecimal three = new java.math.BigDecimal("3");
        
        java.math.BigDecimal result = one.divide(three, MathContext.exact().getJavaMathContext());
        
        // Result should have 1000 digits of 3
        String s = result.toPlainString();
        assertTrue(s.startsWith("0.3333333333"), "Result should start with 0.3333333333");
        assertEquals(1002, s.length()); // "0." + 1000 digits
        
        // Count number of '3' characters after decimal point
        long count = s.substring(2).chars().filter(ch -> ch == '3').count();
        assertEquals(1000, count, "Should have 1000 '3' digits");
    }

    @Test
    public void testCurrentContextPrecision() {
        MathContext.exact().compute(() -> {
            MathContext current = MathContext.getCurrent();
            assertEquals(1000, current.getJavaMathContext().getPrecision());
            return null;
        });
    }

}

