package org.jscience.physics.classical.waves.electromagnetism.components;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Ideal current source providing a constant current.
 */
public class CurrentSource implements CircuitComponent {
    private final Real current;

    public CurrentSource(double current) {
        this.current = Real.of(current);
    }

    public CurrentSource(Real current) {
        this.current = current;
    }

    @Override
    public Real getCurrent(Real voltageAcross, Real dt) {
        // Ideal current source defines its current regardless of voltage.
        return current;
    }

    public Real getCurrent() {
        return current;
    }
}
