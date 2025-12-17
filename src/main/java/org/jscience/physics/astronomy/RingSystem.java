package org.jscience.physics.astronomy;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Mass;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a planetary ring system.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class RingSystem extends CelestialBody {

    private Quantity<Length> innerRadius;
    private Quantity<Length> outerRadius;

    public RingSystem(String name, Quantity<Mass> mass, Quantity<Length> radius, Vector<Real> position,
            Vector<Real> velocity) {
        super(name, mass, radius, position, velocity);
    }

    public Quantity<Length> getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(Quantity<Length> innerRadius) {
        this.innerRadius = innerRadius;
    }

    public Quantity<Length> getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(Quantity<Length> outerRadius) {
        this.outerRadius = outerRadius;
    }
}
