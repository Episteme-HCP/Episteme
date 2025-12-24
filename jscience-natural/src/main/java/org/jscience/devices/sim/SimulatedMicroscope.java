package org.jscience.devices.sim;

import org.jscience.devices.sensors.Microscope;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Simulated implementation of Microscope.
 */
public class SimulatedMicroscope extends SimulatedDevice implements Microscope {

    private final Type type;
    private final Real maxMagnification;
    private final Real resolution;
    private Real currentMagnification = Real.ONE;

    public SimulatedMicroscope(String name, Type type, Real maxMagnification, Real resolution) {
        super(name);
        this.type = type;
        this.maxMagnification = maxMagnification;
        this.resolution = resolution;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Real getMaxMagnification() {
        return maxMagnification;
    }

    @Override
    public Real getResolution() {
        return resolution;
    }

    @Override
    public Real getCurrentMagnification() {
        return currentMagnification;
    }

    @Override
    public void setMagnification(Real magnification) {
        if (magnification.compareTo(maxMagnification) > 0) {
            throw new IllegalArgumentException("Magnification exceeds maximum");
        }
        this.currentMagnification = magnification;
    }

    @Override
    public Real getApparentSize(Real actualSize) {
        return actualSize.multiply(currentMagnification);
    }

    @Override
    public boolean isResolvable(Real featureSize) {
        return featureSize.compareTo(resolution) >= 0;
    }
}
