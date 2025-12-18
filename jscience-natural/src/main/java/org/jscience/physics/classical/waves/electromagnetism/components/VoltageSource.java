package org.jscience.physics.classical.waves.electromagnetism.components;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.classical.waves.electromagnetism.circuits.CircuitElement;

/**
 * Ideal voltage source providing a constant voltage.
 */
public class VoltageSource extends CircuitElement implements CircuitComponent {
    private final Real voltage;

    public VoltageSource(double voltage) {
        super();
        this.voltage = Real.of(voltage);
    }

    public VoltageSource(Real voltage) {
        super();
        this.voltage = voltage;
    }

    @Override
    public Real getCurrent(Real voltageAcross, Real dt) {
        return Real.ZERO;
    }

    public Real getVoltage() {
        return voltage;
    }

    @Override
    public int getVoltageSourceCount() {
        return 1;
    }

    @Override
    public void stamp() {
        if (circuit != null) {
            circuit.stampVoltageSource(nodes[0], nodes[1], voltSource, voltage.doubleValue());
        }
    }

    @Override
    public void doStep() {
        // Constant voltage, nothing to update
    }
}
