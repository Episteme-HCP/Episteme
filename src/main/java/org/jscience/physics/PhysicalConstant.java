
package org.jscience.physics;

import org.jscience.measure.Quantity;
import org.jscience.measure.Unit;
import org.jscience.measure.Quantities;
import org.jscience.bibliography.Citation;
import org.jscience.bibliography.Standard;
import org.jscience.mathematics.numbers.real.Real;

/**
 * A physical constant.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 * @param <Q> the type of quantity.
 */
public class PhysicalConstant<Q extends Quantity<Q>> {

    private final String name;
    private final Real value;
    private final Unit<Q> unit;
    private final Standard standard;
    private final Real unexpectedUncertainty;

    public PhysicalConstant(String name, Real value, Unit<Q> unit, Standard standard, Real unexpectedUncertainty) {
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.standard = standard;
        this.unexpectedUncertainty = unexpectedUncertainty;
    }

    // Convenience constructor for double
    public PhysicalConstant(String name, double value, Unit<Q> unit, Standard standard, double unexpectedUncertainty) {
        this(name, Real.of(value), unit, standard, Real.of(unexpectedUncertainty));
    }

    public String getName() {
        return name;
    }

    public Real getValue() {
        return value;
    }

    public Unit<Q> getUnit() {
        return unit;
    }

    public Standard getStandard() {
        return standard;
    }

    public Citation getCitation() {
        return standard != null ? standard.getCitation() : null;
    }

    public Real getStandardUncertainty() {
        return unexpectedUncertainty;
    }

    public Quantity<Q> toQuantity() {
        return Quantities.create(value, unit);
    }

    @Override
    public String toString() {
        return name + " = " + value + " " + unit + " [" + (standard != null ? standard.getName() : "Unknown") + "]";
    }
}
