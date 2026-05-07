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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MPFRTranscendentalTest {
    private static final Logger logger = LoggerFactory.getLogger(MPFRTranscendentalTest.class);

    @Test
    public void testExpPrecision() {
        try {
            java.nio.file.Files.writeString(java.nio.file.Paths.get("/home/admin/episteme_diag.log"), 
                "[DIAG] Available RealProviders: " + org.episteme.core.technical.algorithm.AlgorithmManager.getProviders(org.episteme.core.mathematics.numbers.real.RealProvider.class) + "\n",
                java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
        } catch (Exception e) {}
        MathContext.exact().compute(() -> {
            Real one = Real.of(1.0);
            Real e = one.exp();
            
            logger.info("exp(1) = {}", e);
            
            // Check first 50 digits of e: 2.7182818284590452353602874713526624977572470936999
            String expected = "2.7182818284590452353602874713526624977572470936999";
            String eStr = e.toString();
            assertTrue(eStr.startsWith(expected), "exp(1) should match e with high precision, but was: " + eStr);
            
            return null;
        });
    }

    @Test
    public void testLogPrecision() {
        MathContext.exact().compute(() -> {
            Real e = Real.of(1.0).exp();
            Real logE = e.log();
            
            logger.info("log(e) = {}", logE);
            String logEStr = logE.toString();
            assertThat(logE.doubleValue()).isCloseTo(1.0, org.assertj.core.data.Offset.offset(1e-15));
            assertTrue(logEStr.startsWith("1.0000000000"), "log(e) should be 1.0 with high precision, but was: " + logEStr);
            
            return null;
        });
    }

    @Test
    public void testSinCosPrecision() {
        MathContext.exact().compute(() -> {
            // High-precision PI (100 digits)
            BigDecimal piVal = new BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679");
            Real pi = Real.of(piVal); 
            Real angle = pi.divide(Real.of(4));
            
            Real s = angle.sin();
            Real c = angle.cos();
            
            logger.info("sin(pi/4) = {}", s);
            logger.info("cos(pi/4) = {}", c);
            
            // sin(pi/4)^2 + cos(pi/4)^2 should be 1.0
            Real one = s.multiply(s).add(c.multiply(c));
            String oneStr = one.toString();
            logger.info("sin^2 + cos^2 = {}", oneStr);
            
            assertThat(one.doubleValue()).isCloseTo(1.0, org.assertj.core.data.Offset.offset(1e-15));
            assertTrue(oneStr.startsWith("1.0000000000"), "Identity sin^2 + cos^2 = 1 should hold at high precision, but was: " + oneStr);
            
            return null;
        });
    }

    @Test
    public void testPowPrecision() {
        MathContext.exact().compute(() -> {
            Real two = Real.of(2.0);
            Real half = Real.of(0.5);
            Real sqrtTwo = two.pow(half);
            
            logger.info("2^0.5 = {}", sqrtTwo);
            
            // sqrt(2) * sqrt(2) = 2
            Real res = sqrtTwo.multiply(sqrtTwo);
            String resStr = res.toString();
            logger.info("sqrt(2)^2 = {}", resStr);
            
            assertThat(res.doubleValue()).isCloseTo(2.0, org.assertj.core.data.Offset.offset(1e-15));
            assertTrue(resStr.startsWith("2.0000000000"), "sqrt(2)^2 should be 2.0 at high precision, but was: " + resStr);
            
            return null;
        });
    }
}
