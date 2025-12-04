package org.jscience.measure.converters;

import org.jscience.mathematics.number.Real;
import org.jscience.measure.UnitConverter;

/**
 * A converter that combines two converters.
 */
public class CompositeConverter implements UnitConverter {

    private final UnitConverter first;
    private final UnitConverter second;

    public CompositeConverter(UnitConverter first, UnitConverter second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public Real convert(Real value) {
        return second.convert(first.convert(value));
    }

    @Override
    public UnitConverter inverse() {
        return new CompositeConverter(second.inverse(), first.inverse());
    }

    @Override
    public UnitConverter concatenate(UnitConverter other) {
        return new CompositeConverter(this, other);
    }

    @Override
    public boolean isIdentity() {
        return first.isIdentity() && second.isIdentity();
    }

    @Override
    public boolean isLinear() {
        return first.isLinear() && second.isLinear();
    }

    @Override
    public Real derivative(Real value) {
        Real firstDerivative = first.derivative(value);
        Real convertedValue = first.convert(value);
        Real secondDerivative = second.derivative(convertedValue);
        return firstDerivative.multiply(secondDerivative);
    }
}
