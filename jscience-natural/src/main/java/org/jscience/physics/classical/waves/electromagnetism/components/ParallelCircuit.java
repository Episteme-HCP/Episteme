package org.jscience.physics.classical.waves.electromagnetism.components;

import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a parallel connection of circuit components.
 * The total voltage across each component is the same, and the total current is
 * the sum of the currents.
 */
public class ParallelCircuit implements CircuitComponent {
    private final List<CircuitComponent> components = new ArrayList<>();

    public ParallelCircuit() {
    }

    public void addComponent(CircuitComponent component) {
        components.add(component);
    }

    public List<CircuitComponent> getComponents() {
        return new ArrayList<>(components);
    }

    @Override
    public Real getCurrent(Real voltage, Real dt) {
        // Sum currents of all components for the given voltage.
        Real total = Real.ZERO;
        for (CircuitComponent c : components) {
            total = total.add(c.getCurrent(voltage, dt));
        }
        return total;
    }
}
