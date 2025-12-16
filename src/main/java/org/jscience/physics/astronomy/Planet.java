package org.jscience.physics.astronomy;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Length;

/**
 * Represents a Planet.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Planet extends CelestialBody {

    private boolean habitable;
    private String atmosphereSummary;

    public Planet(String name, Quantity<Mass> mass, Quantity<Length> radius, Vector<Real> position,
            Vector<Real> velocity) {
        super(name, mass, radius, position, velocity);
    }

    public boolean isHabitable() {
        return habitable;
    }

    public void setHabitable(boolean habitable) {
        this.habitable = habitable;
    }

    public String getAtmosphereSummary() {
        return atmosphereSummary;
    }

    public void setAtmosphereSummary(String atmosphereSummary) {
        this.atmosphereSummary = atmosphereSummary;
    }

    @Override
    public String toString() {
        return "Planet " + super.toString();
    }
}
