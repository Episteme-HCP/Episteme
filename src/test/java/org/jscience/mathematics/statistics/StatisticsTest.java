package org.jscience.mathematics.statistics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jscience.mathematics.number.Real;

public class StatisticsTest {

    @Test
    public void testNormalDistribution() {
        NormalDistribution normal = NormalDistribution.standard();

        assertEquals(Real.ZERO, normal.getMean());
        assertEquals(Real.ONE, normal.getStdDev());

        // PDF at mean should be maximum (≈0.3989)
        Real pdfAtMean = normal.pdf(Real.ZERO);
        assertTrue(pdfAtMean.doubleValue() > 0.39 && pdfAtMean.doubleValue() < 0.40);

        // CDF at mean should be 0.5
        Real cdfAtMean = normal.cdf(Real.ZERO);
        assertEquals(0.5, cdfAtMean.doubleValue(), 0.01);
    }

    @Test
    public void testUniformDistribution() {
        UniformDistribution uniform = UniformDistribution.standard();

        assertEquals(Real.of(0.5), uniform.getMean());

        // PDF should be constant 1.0 in [0,1]
        assertEquals(Real.ONE, uniform.pdf(Real.of(0.5)));
        assertEquals(Real.ZERO, uniform.pdf(Real.of(1.5))); // Outside range

        // CDF
        assertEquals(Real.ZERO, uniform.cdf(Real.of(-0.5)));
        assertEquals(Real.of(0.5), uniform.cdf(Real.of(0.5)));
        assertEquals(Real.ONE, uniform.cdf(Real.of(1.5)));

        // Sampling
        Real sample = uniform.sample();
        assertTrue(sample.compareTo(Real.ZERO) >= 0 && sample.compareTo(Real.ONE) <= 0);
    }
}
