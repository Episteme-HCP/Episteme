package org.jscience.technical.backend.algorithms;

import org.junit.jupiter.api.Test;
import org.jscience.mathematics.geometry.Vector4D;
import static org.junit.jupiter.api.Assertions.*;

public class MulticoreMaxwellProviderTest {

    @Test
    public void testTensorAntisymmetry() {
        MulticoreMaxwellProvider provider = new MulticoreMaxwellProvider();
        Vector4D point = new Vector4D(1.0, 2.0, 3.0, 4.0);
        double[][] f = provider.computeTensor(point);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(f[i][j], -f[j][i], 1e-12, "Tensor must be antisymmetric at [" + i + "][" + j + "]");
            }
        }
    }

    @Test
    public void testNonZeroFieldNearSource() {
        MulticoreMaxwellProvider provider = new MulticoreMaxwellProvider();
        // Default source at origin, oscillating along Z axis
        Vector4D point = new Vector4D(10.0, 2.0, 0.0, 0.0); // 2 units away on X axis
        double[][] f = provider.computeTensor(point);

        boolean hasNonZero = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (Math.abs(f[i][j]) > 1e-15) {
                    hasNonZero = true;
                    break;
                }
            }
        }
        assertTrue(hasNonZero, "Field should be non-zero near source at t=10.0");
    }
}
