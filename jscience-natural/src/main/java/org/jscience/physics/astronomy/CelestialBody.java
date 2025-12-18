package org.jscience.physics.astronomy;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;

import org.jscience.physics.classical.mechanics.Particle;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Time;
import org.jscience.measure.quantity.Acceleration;
import org.jscience.measure.Units;

/**
 * Represents a celestial body (Star, Planet, Moon, etc.).
 * Extends Particle for kinematic simulation.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class CelestialBody extends Particle {

    private String name;
    private Quantity<Length> radius;
    private Quantity<Time> rotationPeriod;

    private java.util.Map<String, String> texturePaths = new java.util.HashMap<>();
    private CelestialBody parent; // Primary body (e.g. Sun for Earth, Earth for Moon)
    private java.util.List<CelestialBody> children = new java.util.ArrayList<>();

    /**
     * Creates a new CelestialBody.
     * 
     * @param name     Name of the body
     * @param mass     Mass in kg
     * @param radius   Mean radius in meters
     * @param position Initial position vector
     * @param velocity Initial velocity vector
     */
    public CelestialBody(String name, Quantity<Mass> mass, Quantity<Length> radius, Vector<Real> position,
            Vector<Real> velocity) {
        super(position, velocity, mass);
        this.name = name;
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Quantity<Length> getRadius() {
        return radius;
    }

    public void setRadius(Quantity<Length> radius) {
        this.radius = radius;
    }

    public Quantity<Time> getRotationPeriod() {
        return rotationPeriod;
    }

    public void setRotationPeriod(Quantity<Time> rotationPeriod) {
        this.rotationPeriod = rotationPeriod;
    }

    public String getTexturePath() {
        return texturePaths.get("diffuse");
    }

    public void setTexturePath(String texturePath) {
        this.texturePaths.put("diffuse", texturePath);
    }

    public String getTexture(String type) {
        return texturePaths.get(type);
    }

    public void setTexture(String type, String path) {
        this.texturePaths.put(type, path);
    }

    public java.util.Map<String, String> getTextures() {
        return texturePaths;
    }

    public CelestialBody getParent() {
        return parent;
    }

    public void setParent(CelestialBody parent) {
        this.parent = parent;
    }

    public java.util.List<CelestialBody> getChildren() {
        return children;
    }

    public void addChild(CelestialBody child) {
        if (!children.contains(child)) {
            children.add(child);
            child.setParent(this);
        }
    }

    /**
     * Surface gravity: g = GM/r²
     */
    public Quantity<Acceleration> getSurfaceGravity() {
        // g = GM/r^2

        // Check PhysicalConstants.G type. It was Real or Quantity?
        // Viewed before: it uses Quantity.

        // Let's rely on Quantity math if possible or extract double
        double m = getMass().to(Units.KILOGRAM).getValue().doubleValue();
        double r = radius.to(Units.METER).getValue().doubleValue();
        // PhysicalConstants.G is likely N*m^2/kg^2
        // For now, let's extract G value manually if getting G symbol is hard
        double gVal = 6.67430e-11 * m / (r * r);
        return org.jscience.measure.Quantities.create(gVal, Units.METERS_PER_SECOND_SQUARED);
    }

    @Override
    public String toString() {
        return String.format("%s (m=%s)", name, getMass());
    }
}
