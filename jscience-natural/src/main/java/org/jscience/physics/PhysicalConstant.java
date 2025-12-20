package org.jscience.physics;

import org.jscience.measure.Quantity;
import org.jscience.measure.Unit;
import org.jscience.measure.Quantities;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a physical constant with uncertainty.
 * 
 * @author Silvere Martin-Michiellot
 * @since 5.0
 * @param <Q> the type of quantity.
 */
public class PhysicalConstant<Q extends Quantity<Q>> {

    private final String name;
    private final double value;
    private final Unit<Q> unit;
    private final Object source; // Changed to Object to accept CODATA enum or String
    private final double uncertainty;

    public PhysicalConstant(String name, double value, Unit<Q> unit, Object source, double uncertainty) {
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.source = source;
        this.uncertainty = uncertainty;
    }

    public Quantity<Q> toQuantity() {
        return Quantities.create(value, unit);
    }

    public Real getValue() {
        return Real.of(value);
    }

    public String getName() {
        return name;
    }

    public Unit<Q> getUnit() {
        return unit;
    }

    public Object getSource() {
        return source;
    }

    public double getUncertainty() {
        return uncertainty;
    }
}
