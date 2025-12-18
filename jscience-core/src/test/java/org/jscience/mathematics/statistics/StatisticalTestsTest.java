package org.jscience.mathematics.statistics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.jscience.mathematics.numbers.real.Real;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Tests for StatisticalTests.
 */
public class StatisticalTestsTest {

    private static final double DELTA = 1e-4;

    @Test
    public void testAnova() {
        // Group 1: 2, 3, 4 (mean 3)
        // Group 2: 6, 7, 8 (mean 7)
        // Grand mean = 5
        // SSB = 3 * (3-5)^2 + 3 * (7-5)^2 = 3*4 + 3*4 = 24
        // SSW = (2-3)^2 + (3-3)^2 + (4-3)^2 + (6-7)^2 + (7-7)^2 + (8-7)^2 = 1+0+1 +
        // 1+0+1 = 4
        // dfB = 1, dfW = 4
        // MSB = 24 / 1 = 24
        // MSW = 4 / 4 = 1
        // F = 24 / 1 = 24

        List<Real> g1 = Arrays.asList(Real.of(2), Real.of(3), Real.of(4));
        List<Real> g2 = Arrays.asList(Real.of(6), Real.of(7), Real.of(8));

        List<List<Real>> groups = new ArrayList<>();
        groups.add(g1);
        groups.add(g2);

        Real f = StatisticalTests.oneWayAnova(groups);
        assertEquals(24.0, f.doubleValue(), DELTA);
    }
}
