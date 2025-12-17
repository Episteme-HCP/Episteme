package org.jscience.physics.astronomy;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Temperature;
import org.jscience.measure.quantity.Pressure;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents a Planet.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Planet extends CelestialBody {

    private boolean habitable;

    private String atmosphereSummary; // Deprecated/Legacy

    private Quantity<Temperature> surfaceTemperature;
    private Quantity<Pressure> surfacePressure;
    private Map<String, Double> atmosphereComposition = new HashMap<>();

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

    public Quantity<Temperature> getSurfaceTemperature() {
        return surfaceTemperature;
    }

    public void setSurfaceTemperature(Quantity<Temperature> surfaceTemperature) {
        this.surfaceTemperature = surfaceTemperature;
    }

    public Quantity<Pressure> getSurfacePressure() {
        return surfacePressure;
    }

    public void setSurfacePressure(Quantity<Pressure> surfacePressure) {
        this.surfacePressure = surfacePressure;
    }

    public Map<String, Double> getAtmosphereComposition() {
        return atmosphereComposition;
    }

    public void setAtmosphereComposition(Map<String, Double> atmosphereComposition) {
        this.atmosphereComposition = atmosphereComposition;
    }

    @Override
    public String toString() {
        return "Planet " + super.toString();
    }
}
