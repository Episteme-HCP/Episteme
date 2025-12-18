package org.jscience.mathematics.dynamical;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DynamicalSystemsTest {

    @Test
    public void testLogisticMap() {
        LogisticMap map = new LogisticMap(4.0);
        double[] state = { 0.2 };

        // x_1 = 4 * 0.2 * 0.8 = 0.64
        double[] next = map.map(state);
        assertEquals(0.64, next[0], 1e-9);

        assertEquals(1, map.dimensions());
    }

    @Test
    public void testHenonMap() {
        HenonMap map = new HenonMap(1.4, 0.3);
        double[] state = { 0.0, 0.0 };

        // x_1 = 1 - 1.4*0 + 0 = 1
        // y_1 = 0.3 * 0 = 0
        double[] next = map.map(state);
        assertEquals(1.0, next[0], 1e-9);
        assertEquals(0.0, next[1], 1e-9);

        assertEquals(2, map.dimensions());
    }
}
