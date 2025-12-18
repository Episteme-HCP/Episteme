package org.jscience.physics.astronomy;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Temperature;
import org.jscience.measure.quantity.Power;

/**
 * Represents a Star.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Star extends CelestialBody {

    private String spectralType;
    private Quantity<Power> luminosity;
    private Quantity<Temperature> temperature;

    public Star(String name, Quantity<Mass> mass, Quantity<Length> radius, Vector<Real> position,
            Vector<Real> velocity) {
        super(name, mass, radius, position, velocity);
    }

    public String getSpectralType() {
        return spectralType;
    }

    public void setSpectralType(String spectralType) {
        this.spectralType = spectralType;
    }

    public Quantity<Power> getLuminosity() {
        return luminosity;
    }

    public void setLuminosity(Quantity<Power> luminosity) {
        this.luminosity = luminosity;
    }

    public Quantity<Temperature> getTemperature() {
        return temperature;
    }

    public void setTemperature(Quantity<Temperature> temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Star " + super.toString();
    }
}
