package org.jscience.device.impl.sim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import org.jscience.device.sim.SimulatedPressureGauge;

public class SimulatedPressureGaugeTest {

    @Test
    public void testDeviceMetadata() throws Exception {
        try (SimulatedPressureGauge gauge = new SimulatedPressureGauge()) {
            assertEquals("Simulated PressureGauge", gauge.getName());
            assertEquals("JScience Sims Inc.", gauge.getManufacturer());
            assertEquals("v2.1.4", gauge.getFirmware());
            assertNotNull(gauge.getId());
        }
    }

    @Test
    public void testReadings() throws Exception {
        try (SimulatedPressureGauge gauge = new SimulatedPressureGauge()) {
            Map<String, String> readings = gauge.getReadings();
            assertTrue(readings.containsKey("Power"), "Should have Power reading");
            assertTrue(readings.containsKey("Uptime"), "Should have Uptime reading");
            assertTrue(readings.containsKey("Error Code"), "Should have Error Code reading");

            assertEquals("ON", readings.get("Power"));
        }
    }

    @Test
    public void testCapabilities() throws Exception {
        try (SimulatedPressureGauge gauge = new SimulatedPressureGauge()) {
            Map<String, Boolean> caps = gauge.getCapabilities();
            assertTrue(caps.getOrDefault("Data Logging", false));
            assertTrue(caps.getOrDefault("Remote Control", false));
            assertFalse(caps.getOrDefault("High Voltage Protection", true));
        }
    }
}
