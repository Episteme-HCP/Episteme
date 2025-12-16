package org.jscience.physics.classical.waves.electromagnetism.components;

import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;

/**
 * A simple circuit that aggregates multiple {@link CircuitComponent}s.
 * It provides basic methods to add components and compute total voltage
 * and current assuming ideal connections.
 */
public class SimpleCircuit implements CircuitComponent {
    private final List<CircuitComponent> components = new ArrayList<>();

    public SimpleCircuit() {
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
