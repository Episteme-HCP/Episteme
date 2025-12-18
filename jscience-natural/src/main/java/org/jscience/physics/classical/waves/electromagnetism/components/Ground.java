package org.jscience.physics.classical.waves.electromagnetism.components;

import org.jscience.physics.classical.waves.electromagnetism.circuits.CircuitElement;

/**
 * Represents a ground reference (0V potential).
 * 
 * @author Silvere Martin-Michiellot
 * @since 5.0
 */
public class Ground extends CircuitElement implements CircuitComponent {

    public Ground() {
        super();
    }

    @Override
    public int getPostCount() {
        return 1;
    }

    @Override
    public org.jscience.mathematics.numbers.real.Real getCurrent(org.jscience.mathematics.numbers.real.Real voltage,
            org.jscience.mathematics.numbers.real.Real dt) {
        return org.jscience.mathematics.numbers.real.Real.ZERO;
    }

    @Override
    public void stamp() {
        // Ground stamps node as 0V (voltage source to ground)
        if (circuit != null) {
            circuit.stampVoltageSource(nodes[0], 0, voltSource, 0);
        }
    }

    @Override
    public int getVoltageSourceCount() {
        return 1;
    }

    @Override
    public boolean hasGroundConnection(int n) {
        return true;
    }
}
