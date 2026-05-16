/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.episteme.natural.device.impl.sim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.episteme.natural.device.sim.SimulatedPressureGauge;
import org.episteme.natural.device.sensors.PressureGauge;
import org.episteme.core.util.identity.SimpleIdentification;

public class SimulatedPressureGaugeTest {

    private SimulatedPressureGauge createGauge() {
        return new SimulatedPressureGauge(
                new SimpleIdentification("test-pressure-gauge"),
                PressureGauge.GaugeType.BOURDON,
                80000, 120000);
    }

    @Test
    public void testDeviceMetadata() throws Exception {
        try (SimulatedPressureGauge gauge = createGauge()) {
            assertNotNull(gauge.getId());
        }
    }

    @Test
    public void testReadings() throws Exception {
        try (SimulatedPressureGauge gauge = createGauge()) {
            gauge.connect();
            var value = gauge.readValue();
            assertNotNull(value, "Should produce a pressure reading");
            assertTrue(value.getValue().doubleValue() > 0, "Pressure should be positive");
        }
    }

    @Test
    public void testPressureRange() throws Exception {
        try (SimulatedPressureGauge gauge = createGauge()) {
            assertNotNull(gauge.getMinPressure());
            assertNotNull(gauge.getMaxPressure());
            assertTrue(gauge.getMinPressure().getValue().doubleValue() < gauge.getMaxPressure().getValue().doubleValue());
        }
    }
}
