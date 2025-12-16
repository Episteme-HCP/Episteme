package org.jscience.mathematics.statistics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jscience.mathematics.numbers.real.Real;

import org.jscience.mathematics.statistics.distributions.NormalDistribution;
import org.jscience.mathematics.statistics.distributions.UniformDistribution;

public class StatisticsTest {

    @Test
    public void testNormalDistribution() {
        NormalDistribution normal = new NormalDistribution(Real.ZERO, Real.ONE);

        assertEquals(0.0, normal.mean().doubleValue(), 1e-10);
        assertEquals(1.0, normal.getStdDev().doubleValue(), 1e-10);

        // PDF at mean should be maximum (≈0.3989)
        Real pdfAtMean = normal.density(Real.ZERO);
        assertTrue(pdfAtMean.doubleValue() > 0.39 && pdfAtMean.doubleValue() < 0.40);

        // CDF at mean should be 0.5
        Real cdfAtMean = normal.cdf(Real.ZERO);
        assertEquals(0.5, cdfAtMean.doubleValue(), 0.01);
    }

    @Test
    public void testUniformDistribution() {
        UniformDistribution uniform = new UniformDistribution(Real.ZERO, Real.ONE);

        assertEquals(0.5, uniform.mean().doubleValue(), 1e-10);

        // PDF should be constant 1.0 in [0,1]
        assertEquals(1.0, uniform.density(Real.of(0.5)).doubleValue(), 1e-10);
        assertEquals(0.0, uniform.density(Real.of(1.5)).doubleValue(), 1e-10); // Outside range

        // CDF
        assertEquals(0.0, uniform.cdf(Real.of(-0.5)).doubleValue(), 1e-10);
        assertEquals(0.5, uniform.cdf(Real.of(0.5)).doubleValue(), 1e-10);
        assertEquals(1.0, uniform.cdf(Real.of(1.5)).doubleValue(), 1e-10);

        // Sampling
        Real sample = uniform.sample();
        assertTrue(sample.compareTo(Real.ZERO) >= 0 && sample.compareTo(Real.ONE) <= 0);
    }
}
