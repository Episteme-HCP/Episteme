package org.jscience.physics.classical.waves.electromagnetism.components;

import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a series connection of circuit components.
 * The total voltage is the sum of component voltages, and the current is the
 * same through all components.
 */
public class SeriesCircuit implements CircuitComponent {
    private final List<CircuitComponent> components = new ArrayList<>();

    public SeriesCircuit() {
    }

    public void addComponent(CircuitComponent component) {
        components.add(component);
    }

    public List<CircuitComponent> getComponents() {
        return new ArrayList<>(components);
    }

    @Override
    public Real getCurrent(Real voltage, Real dt) {
        // In a series circuit, current is the same through all components.
        // For simplicity, we return the current of the first component if any, else
        // zero.
        if (components.isEmpty()) {
            return Real.ZERO;
        }
        // Assuming ideal components, compute current based on total resistance if all
        // are resistors.
        // Here we just delegate to the first component.
        return components.get(0).getCurrent(voltage, dt);
    }
}
