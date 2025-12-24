package org.jscience.devices.sim;

import org.jscience.devices.sensors.TemperatureProbe;
import org.jscience.mathematics.numbers.real.Real;
import java.io.IOException;

/**
 * Simulated implementation of TemperatureProbe.
 */
public class SimulatedTemperatureProbe extends SimulatedDevice implements TemperatureProbe {

    private final ProbeType type;
    private final Real accuracy;
    private final Real minTemp;
    private final Real maxTemp;
    private Real lastReading = Real.of(293.15); // 20 C

    public SimulatedTemperatureProbe(String name, ProbeType type, Real accuracy, Real minTemp, Real maxTemp) {
        super(name);
        this.type = type;
        this.accuracy = accuracy;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    @Override
    public ProbeType getType() {
        return type;
    }

    @Override
    public Real getAccuracy() {
        return accuracy;
    }

    @Override
    public Real getMinTemp() {
        return minTemp;
    }

    @Override
    public Real getMaxTemp() {
        return maxTemp;
    }

    @Override
    public Real measure(Real actualTemp) {
        if (!isConnected())
            throw new IllegalStateException("Device not connected");
        // Simple noise simulation
        double noise = (Math.random() - 0.5) * accuracy.doubleValue();
        lastReading = actualTemp.add(Real.of(noise));
        return lastReading;
    }

    @Override
    public Real readValue() throws IOException {
        if (!isConnected())
            throw new IOException("Device not connected");
        return lastReading;
    }
}
