package org.jscience.devices.sim;

import org.jscience.devices.sensors.PHMeter;
import org.jscience.mathematics.numbers.real.Real;
import java.io.IOException;

/**
 * Simulated implementation of PHMeter.
 */
public class SimulatedPHMeter extends SimulatedDevice implements PHMeter {

    private final Real accuracy;
    private Real lastReading = NEUTRAL_PH;

    public SimulatedPHMeter(String name, Real accuracy) {
        super(name);
        this.accuracy = accuracy;
    }

    @Override
    public Real getAccuracy() {
        return accuracy;
    }

    @Override
    public Real measure(Real actualPH) {
        if (!isConnected())
            throw new IllegalStateException("Device not connected");

        if (actualPH.compareTo(MIN_PH) < 0 || actualPH.compareTo(MAX_PH) > 0) {
            throw new IllegalArgumentException("pH must be between 0 and 14");
        }

        double noise = (Math.random() - 0.5) * accuracy.doubleValue();
        lastReading = actualPH.add(Real.of(noise));
        return lastReading;
    }

    @Override
    public Real readValue() throws IOException {
        if (!isConnected())
            throw new IOException("Device not connected");
        return lastReading;
    }

    @Override
    public String classify(Real pH) {
        double val = pH.doubleValue();
        if (val < 7.0) {
            if (val < 3.0)
                return "Strongly Acidic";
            if (val < 5.0)
                return "Moderately Acidic";
            return "Weakly Acidic";
        } else if (val > 7.0) {
            if (val > 11.0)
                return "Strongly Alkaline";
            if (val > 9.0)
                return "Moderately Alkaline";
            return "Weakly Alkaline";
        }
        return "Neutral";
    }

    @Override
    public Real getHydrogenConcentration(Real pH) {
        return Real.of(Math.pow(10, -pH.doubleValue()));
    }
}
