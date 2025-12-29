/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.physics.astronomy;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;

import org.jscience.physics.classical.mechanics.Particle;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Acceleration;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Time;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

/**
 * Represents a celestial body (Star, Planet, Moon, etc.).
 * Extends Particle for kinematic simulation.
 * Modernized to JScience.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CelestialBody extends Particle {

    private String name;
    private Quantity<Length> radius;
    private Quantity<Time> rotationPeriod;

    private java.util.Map<String, String> texturePaths = new java.util.HashMap<>();
    private CelestialBody parent;
    private java.util.List<CelestialBody> children = new java.util.ArrayList<>();

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
        double m = getMass().to(Units.KILOGRAM).getValue().doubleValue();
        double r = radius.to(Units.METER).getValue().doubleValue();

        // G = 6.67430e-11 N*m^2/kg^2
        double gVal = 6.67430e-11 * m / (r * r);
        // acceleration unit m/s^2 can be derived or constructed
        return Quantities.create(gVal, Units.METER.divide(Units.SECOND.pow(2)).asType(Acceleration.class));
    }

    public Quantity<Acceleration> getSurfaceGravitySafe() {
        return getSurfaceGravity();
    }

    @Override
    public String toString() {
        return String.format("%s (m=%s)", name, getMass());
    }
}
