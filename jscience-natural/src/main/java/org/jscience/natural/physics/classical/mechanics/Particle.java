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

package org.jscience.natural.physics.classical.mechanics;

import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.mathematics.structures.SpatialOctree;

import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.quantity.Energy;
import org.jscience.core.measure.quantity.Mass;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;

import java.util.ArrayList;
import java.util.List;

/**
 * A particle in N-body simulation using Generic Linear Algebra.
 * Modernized to use Real for all physical properties.
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
@Persistent
public class Particle implements SpatialOctree.SpatialObject {

    @Id
    private Identification identification;
    @Attribute
    private Vector<Real> position;
    @Attribute
    private Vector<Real> velocity;
    @Attribute
    private Vector<Real> acceleration;
    @Attribute
    private Quantity<Mass> mass;

    public Particle(double x, double y, double z, double massKg) {
        this.identification = new SimpleIdentification("P" + System.nanoTime());
        this.mass = Quantities.create(massKg, Units.KILOGRAM);
        this.position = createVector(x, y, z);
        this.velocity = createVector(0, 0, 0);
        this.acceleration = createVector(0, 0, 0);
    }

    public Particle(Vector<Real> position, Vector<Real> velocity, Quantity<Mass> mass) {
        this.identification = new SimpleIdentification("P" + System.nanoTime());
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
        this(position, velocity, Quantities.create(mass, Units.KILOGRAM));
    }

    private Vector<Real> createVector(double... values) {
        List<Real> list = new ArrayList<>();
        for (double v : values) {
            list.add(Real.of(v));
        }
        return DenseVector.of(list, Reals.getInstance());
    }

    private Vector<Real> createVector(Real... values) {
        return DenseVector.of(List.of(values), Reals.getInstance());
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
    public double getMassValue() {
        return mass.to(Units.KILOGRAM).getValue().doubleValue();
    }

    public void setPosition(Real x, Real y, Real z) {
        this.position = createVector(x, y, z);
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

    /**
     * Calculates kinetic energy.
     * E = 0.5 * m * v^2
     */
    public Quantity<Energy> kineticEnergy() {
        Real m = mass.to(Units.KILOGRAM).getValue();
        Real v = velocity.norm();
        Real e = Real.of(0.5).multiply(m).multiply(v.pow(2));
        return Quantities.create(e, Units.JOULE);
    }

    public void setVelocity(Real x, Real y, Real z) {
        this.velocity = createVector(x, y, z);
    }

    public void setAcceleration(Real x, Real y, Real z) {
        this.acceleration = createVector(x, y, z);
    }

    public void setVelocity(double x, double y, double z) {
        setVelocity(Real.of(x), Real.of(y), Real.of(z));
    }

    public void setAcceleration(double x, double y, double z) {
        setAcceleration(Real.of(x), Real.of(y), Real.of(z));
    }

    @Override
    public String toString() {
        return "Particle(m=" + mass + ", pos=" + position + ")";
    }
}

