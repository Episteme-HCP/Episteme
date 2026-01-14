/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.physics.classical.mechanics;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;
import org.jscience.mathematics.structures.SpatialOctree;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Acceleration;
import org.jscience.measure.quantity.Energy;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Velocity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

import java.util.ArrayList;
import java.util.List;

/**
 * A particle in N-body simulation using Generic Linear Algebra.
 * Modernized to JScience.
 * 
 * <p>
 * Implements {@link SpatialOctree.SpatialObject} for high-performance
 * Barnes-Hut simulations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Particle implements SpatialOctree.SpatialObject {

    private Vector<Real> position;
    private Vector<Real> velocity;
    private Vector<Real> acceleration;
    private Quantity<Mass> mass;

    public Particle(double x, double y, double z, double massKg) {
        this.mass = Quantities.create(massKg, Units.KILOGRAM);
        this.position = createVector(x, y, z);
        this.velocity = createVector(0, 0, 0);
        this.acceleration = createVector(0, 0, 0);
    }

    public Particle(Vector<Real> position, Vector<Real> velocity, Quantity<Mass> mass) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = createVector(0, 0, 0);
        if (acceleration.dimension() != position.dimension()) {
            this.acceleration = DenseVector.of(java.util.Collections.nCopies(position.dimension(), Real.ZERO),
                    Reals.getInstance());
        }
        this.mass = mass;
    }

    public Particle(Vector<Real> position, Vector<Real> velocity, Real mass) {
        this(position, velocity, Quantities.create(mass.doubleValue(), Units.KILOGRAM));
    }

    private Vector<Real> createVector(double... values) {
        List<Real> list = new ArrayList<>();
        for (double v : values) {
            list.add(Real.of(v));
        }
        return DenseVector.of(list, Reals.getInstance());
    }

    public Vector<Real> getPosition() {
        return position;
    }

    public Vector<Real> getVelocity() {
        return velocity;
    }

    public Vector<Real> getAcceleration() {
        return acceleration;
    }

    public Quantity<Mass> getMass() {
        return mass;
    }

    public void setPosition(Vector<Real> position) {
        this.position = position;
    }

    public void setVelocity(Vector<Real> velocity) {
        this.velocity = velocity;
    }

    public void setAcceleration(Vector<Real> acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public double getX() {
        return position.get(0).doubleValue();
    }

    @Override
    public double getY() {
        return (position.dimension() > 1) ? position.get(1).doubleValue() : 0;
    }

    @Override
    public double getZ() {
        return (position.dimension() > 2) ? position.get(2).doubleValue() : 0;
    }

    @Override
    public double getMassValue() { // Note: changed from getMass to avoid conflict with getMass() returning
                                   // Quantity
        return mass.to(Units.KILOGRAM).getValue().doubleValue();
    }

    public void setPosition(Real x, Real y, Real z) {
        this.position = createVector(x.doubleValue(), y.doubleValue(), z.doubleValue());
    }

    public Real distanceTo(Particle other) {
        return this.position.subtract(other.position).norm();
    }

    public void updatePosition(Real dt) {
        this.position = this.position.add(this.velocity.multiply(dt));
    }

    public void updateVelocity(Real dt) {
        this.velocity = this.velocity.add(this.acceleration.multiply(dt));
    }

    public Quantity<Energy> kineticEnergy() {
        // E = 0.5 * m * v^2
        double m = mass.to(Units.KILOGRAM).getValue().doubleValue();
        double v = velocity.norm().doubleValue();
        return Quantities.create(0.5 * m * v * v, Units.JOULE);
    }

    public void setVelocity(Real x, Real y, Real z) {
        this.velocity = createVector(x.doubleValue(), y.doubleValue(), z.doubleValue());
    }

    public void setAcceleration(Real x, Real y, Real z) {
        this.acceleration = createVector(x.doubleValue(), y.doubleValue(), z.doubleValue());
    }

    public void setVelocity(double x, double y, double z) {
        setVelocity(Real.of(x), Real.of(y), Real.of(z));
    }

    public void setAcceleration(double x, double y, double z) {
        setAcceleration(Real.of(x), Real.of(y), Real.of(z));
    }

    public Particle(Quantity<Length> x, Quantity<Length> y, Quantity<Length> z, Quantity<Mass> mass) {
        this.mass = mass;
        this.position = createVector(
                x.to(Units.METER).getValue().doubleValue(),
                y.to(Units.METER).getValue().doubleValue(),
                z.to(Units.METER).getValue().doubleValue());
        this.velocity = createVector(0, 0, 0);
        this.acceleration = createVector(0, 0, 0);
    }

    public void setPosition(Quantity<Length> x, Quantity<Length> y, Quantity<Length> z) {
        this.position = createVector(
                x.to(Units.METER).getValue().doubleValue(),
                y.to(Units.METER).getValue().doubleValue(),
                z.to(Units.METER).getValue().doubleValue());
    }

    public void setVelocity(Quantity<Velocity> x, Quantity<Velocity> y, Quantity<Velocity> z) {
        this.velocity = createVector(
                x.to(Units.METER_PER_SECOND).getValue().doubleValue(),
                y.to(Units.METER_PER_SECOND).getValue().doubleValue(),
                z.to(Units.METER_PER_SECOND).getValue().doubleValue());
    }

    public void setAcceleration(Quantity<Acceleration> x, Quantity<Acceleration> y, Quantity<Acceleration> z) {
        this.acceleration = createVector(
                x.to(Units.METERS_PER_SECOND_SQUARED).getValue().doubleValue(),
                y.to(Units.METERS_PER_SECOND_SQUARED).getValue().doubleValue(),
                z.to(Units.METERS_PER_SECOND_SQUARED).getValue().doubleValue());
    }

    @Override
    public String toString() {
        return "Particle(m=" + mass + ", pos=" + position + ")";
    }
}
