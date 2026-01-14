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

package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;
import org.jscience.mathematics.algebraic.matrices.Double3Vector;

import org.jscience.util.IllegalDimensionException;


/**
 * The ClassicalParticle3D class provides an object for encapsulating
 * classical point particles that live in 3D.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class ClassicalParticle3D extends AbstractClassicalParticle {
    /** Mass. */
    private double mass;

    /** Charge. */
    private double charge;

    /** Position coordinates. */
    private double x;

    /** Position coordinates. */
    private double y;

    /** Position coordinates. */
    private double z;

    /** Velocity coordinates. */
    private double vx;

    /** Velocity coordinates. */
    private double vy;

    /** Velocity coordinates. */
    private double vz;

/**
     * Constructs a classical particle.
     */
    public ClassicalParticle3D() {
        mass = 0;
        charge = 0;
        x = 0;
        y = 0;
        z = 0;
        vx = 0;
        vy = 0;
        vz = 0;
    }

/**
     * Creates a new ClassicalParticle3D object.
     *
     * @param m DOCUMENT ME!
     */
    public ClassicalParticle3D(double m) {
        mass = m;
        charge = 0;
        x = 0;
        y = 0;
        z = 0;
        vx = 0;
        vy = 0;
        vz = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void setMass(double m) {
        mass = m;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMass() {
        return mass;
    }

    /**
     * Sets the charge of this particle.
     *
     * @param e DOCUMENT ME!
     */
    public void setCharge(double e) {
        charge = e;
    }

    /**
     * Returns the charge of this particle.
     *
     * @return DOCUMENT ME!
     */
    public double getCharge() {
        return charge;
    }

    /**
     * DOCUMENT ME!
     *
     * @param xPos DOCUMENT ME!
     * @param yPos DOCUMENT ME!
     * @param zPos DOCUMENT ME!
     */
    public void setPosition(double xPos, double yPos, double zPos) {
        x = xPos;
        y = yPos;
        z = zPos;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getPosition() {
        return new Double3Vector(x, y, z);
    }

    /**
     * DOCUMENT ME!
     *
     * @param xPos DOCUMENT ME!
     */
    public void setXPosition(double xPos) {
        x = xPos;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getXPosition() {
        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @param yPos DOCUMENT ME!
     */
    public void setYPosition(double yPos) {
        y = yPos;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getYPosition() {
        return y;
    }

    /**
     * DOCUMENT ME!
     *
     * @param zPos DOCUMENT ME!
     */
    public void setZPosition(double zPos) {
        z = zPos;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getZPosition() {
        return z;
    }

    /**
     * DOCUMENT ME!
     *
     * @param xVel DOCUMENT ME!
     * @param yVel DOCUMENT ME!
     * @param zVel DOCUMENT ME!
     */
    public void setVelocity(double xVel, double yVel, double zVel) {
        vx = xVel;
        vy = yVel;
        vz = zVel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getVelocity() {
        return new Double3Vector(vx, vy, vz);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getXVelocity() {
        return vx;
    }

    /**
     * DOCUMENT ME!
     *
     * @param xVel DOCUMENT ME!
     */
    public void setXVelocity(double xVel) {
        vx = xVel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getYVelocity() {
        return vy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param yVel DOCUMENT ME!
     */
    public void setYVelocity(double yVel) {
        vy = yVel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getZVelocity() {
        return vz;
    }

    /**
     * DOCUMENT ME!
     *
     * @param zVel DOCUMENT ME!
     */
    public void setZVelocity(double zVel) {
        vz = zVel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double speed() {
        return Math.sqrt((vx * vx) + (vy * vy) + (vz * vz));
    }

    /**
     * DOCUMENT ME!
     *
     * @param xMom DOCUMENT ME!
     * @param yMom DOCUMENT ME!
     * @param zMom DOCUMENT ME!
     */
    public void setMomentum(double xMom, double yMom, double zMom) {
        vx = xMom / mass;
        vy = yMom / mass;
        vz = zMom / mass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getMomentum() {
        return new Double3Vector(mass * vx, mass * vy, mass * vz);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getXMomentum() {
        return mass * vx;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getYMomentum() {
        return mass * vy;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getZMomentum() {
        return mass * vz;
    }

    /**
     * Returns the kinetic energy.
     *
     * @return DOCUMENT ME!
     */
    public double energy() {
        return (mass * ((vx * vx) + (vy * vy) + (vz * vz))) / 2.0;
    }

    //the dimension of this particle
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDimension() {
        return 3;
    }

    /**
     * Evolves this particle forward according to its kinematics. This
     * method changes the particle's position.
     *
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public AbstractClassicalParticle move(double dt) {
        return translate(dt);
    }

    /**
     * Evolves this particle forward according to its linear
     * kinematics. This method changes the particle's position.
     *
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public ClassicalParticle3D translate(double dt) {
        x += (vx * dt);
        y += (vy * dt);
        z += (vz * dt);

        return this;
    }

    /**
     * Accelerates this particle. This method changes the particle's
     * velocity. It is additive, that is <code>accelerate(a1,
     * dt).accelerate(a2, dt)</code> is equivalent to <code>accelerate(a1+a2,
     * dt)</code>.
     *
     * @param ax DOCUMENT ME!
     * @param ay DOCUMENT ME!
     * @param az DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public ClassicalParticle3D accelerate(double ax, double ay, double az,
        double dt) {
        vx += (ax * dt);
        vy += (ay * dt);
        vz += (az * dt);

        return this;
    }

    /**
     * Applies a force to this particle. This method changes the
     * particle's velocity. It is additive, that is <code>applyForce(F1,
     * dt).applyForce(F2, dt)</code> is equivalent to <code>applyForce(F1+F2,
     * dt)</code>.
     *
     * @param Fx DOCUMENT ME!
     * @param Fy DOCUMENT ME!
     * @param Fz DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public AbstractClassicalParticle applyForce(double Fx, double Fy,
        double Fz, double dt) {
        return accelerate(Fx / mass, Fy / mass, Fz / mass, dt);
    }

    /**
     * Applies a force to this particle. This method changes the
     * particle's velocity. It is additive, that is <code>applyForce(F1,
     * dt).applyForce(F2, dt)</code> is equivalent to <code>applyForce(F1+F2,
     * dt)</code>.
     *
     * @param f DOCUMENT ME!
     * @param t DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return this.
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbstractClassicalParticle applyForce(Force f, double t, double dt) {
        if (f != null) {
            if (f.getDimension() == 3) {
                return applyForce(f.getVector(t).getPrimitiveElement(0),
                    f.getVector(t).getPrimitiveElement(1),
                    f.getVector(t).getPrimitiveElement(2), dt);
            } else {
                throw new IllegalDimensionException(
                    "The Force is not in 3 dimensions.");
            }
        } else {
            throw new IllegalArgumentException("You can't apply a null force.");
        }
    }
}
