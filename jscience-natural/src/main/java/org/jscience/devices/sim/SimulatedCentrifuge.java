package org.jscience.devices.sim;

import org.jscience.devices.actuators.Centrifuge;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Simulated implementation of Centrifuge.
 */
public class SimulatedCentrifuge extends SimulatedDevice implements Centrifuge {

    private final Real maxRPM;
    private final Real maxRCF;
    private final RotorType rotorType;
    private Real currentRPM = Real.ZERO;
    private boolean running = false;

    public SimulatedCentrifuge(String name, Real maxRPM, Real maxRCF, RotorType rotorType) {
        super(name);
        this.maxRPM = maxRPM;
        this.maxRCF = maxRCF;
        this.rotorType = rotorType;
    }

    @Override
    public void start(Real rpm) {
        if (rpm.compareTo(maxRPM) > 0) {
            throw new IllegalArgumentException("RPM exceeds maximum: " + maxRPM);
        }
        this.currentRPM = rpm;
        this.running = true;
    }

    @Override
    public void stop() {
        this.currentRPM = Real.ZERO;
        this.running = false;
    }

    @Override
    public Real calculateRCF(Real radiusCm) {
        // RCF = 1.118e-5 * r * rpm^2
        double r = radiusCm.doubleValue();
        double rpm = currentRPM.doubleValue();
        return Real.of(1.118e-5 * r * rpm * rpm);
    }

    @Override
    public Real getMaxRPM() {
        return maxRPM;
    }

    @Override
    public Real getMaxRCF() {
        return maxRCF;
    }

    @Override
    public RotorType getRotorType() {
        return rotorType;
    }

    @Override
    public Real getCurrentRPM() {
        return currentRPM;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
