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

import org.jscience.mathematics.MathConstants;


/**
 * The RigidBody2D class provides an object for encapsulating rigid bodies
 * that live in 2D.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class RigidBody2D extends ClassicalParticle2D {
    /** Moment of inertia. */
    private double angMass;

    /** Angle (orientation). */
    private double ang;

    /** Angular velocity. */
    private double angVel;

/**
     * Constructs a rigid body.
     */
    public RigidBody2D() {
    }

    /**
     * Sets the moment of inertia.
     *
     * @param MoI DOCUMENT ME!
     */
    public void setMomentOfInertia(double MoI) {
        angMass = MoI;
    }

    /**
     * Returns the moment of inertia.
     *
     * @return DOCUMENT ME!
     */
    public double getMomentOfInertia() {
        return angMass;
    }

    /**
     * Sets the angle (orientation) of this body.
     *
     * @param angle an angle in radians.
     */
    public void setAngle(double angle) {
        ang = angle;
    }

    /**
     * Returns the angle (orientation) of this body.
     *
     * @return an angle in radians.
     */
    public double getAngle() {
        return ang;
    }

    /**
     * Sets the angular velocity.
     *
     * @param angleVel DOCUMENT ME!
     */
    public void setAngularVelocity(double angleVel) {
        angVel = angleVel;
    }

    /**
     * Returns the angular velocity.
     *
     * @return DOCUMENT ME!
     */
    public double getAngularVelocity() {
        return angVel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param angleMom DOCUMENT ME!
     */
    public void setAngularMomentum(double angleMom) {
        angVel = angleMom / angMass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAngularMomentum() {
        return angMass * angVel;
    }

    /**
     * Returns the kinetic and rotational energy.
     *
     * @return DOCUMENT ME!
     */
    public double energy() {
        return ((getMass() * ((getXVelocity() * getXVelocity()) +
        (getYVelocity() * getYVelocity()))) + (angMass * angVel * angVel)) / 2.0;
    }

    /**
     * Evolves this particle forward according to its kinematics. This
     * method changes the particle's position and orientation.
     *
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public AbstractClassicalParticle move(double dt) {
        return rotate(dt).translate(dt);
    }

    /**
     * Evolves this particle forward according to its rotational
     * kinematics. This method changes the particle's orientation.
     *
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public RigidBody2D rotate(double dt) {
        ang += (angVel * dt);

        if (ang > MathConstants.TWO_PI) {
            ang -= MathConstants.TWO_PI;
        } else if (ang < 0.0) {
            ang += MathConstants.TWO_PI;
        }

        return this;
    }

    /**
     * Accelerates this particle. This method changes the particle's
     * angular velocity. It is additive, that is <code>angularAccelerate(a1,
     * dt).angularAccelerate(a2, dt)</code> is equivalent to
     * <code>angularAccelerate(a1+a2, dt)</code>.
     *
     * @param a DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public RigidBody2D angularAccelerate(double a, double dt) {
        angVel += (a * dt);

        return this;
    }

    /**
     * Applies a torque to this particle. This method changes the
     * particle's angular velocity. It is additive, that is
     * <code>applyTorque(T1, dt).applyTorque(T2, dt)</code> is equivalent to
     * <code>applyTorque(T1+T2, dt)</code>.
     *
     * @param T DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public RigidBody2D applyTorque(double T, double dt) {
        return angularAccelerate(T / angMass, dt);
    }

    /**
     * DOCUMENT ME!
     *
     * @param T DOCUMENT ME!
     * @param t DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RigidBody2D applyTorque(Torque2D T, double t, double dt) {
        return angularAccelerate(T.getVector(t).getPrimitiveElement(0) / angMass,
            dt);
    }

    /**
     * Applies a force acting at a point away from the centre of mass.
     * Any resultant torques are also applied. This method changes the
     * particle's angular velocity.
     *
     * @param fx DOCUMENT ME!
     * @param fy DOCUMENT ME!
     * @param x x-coordinate from centre of mass.
     * @param y y-coordinate from centre of mass.
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public RigidBody2D applyForce(double fx, double fy, double x, double y,
        double dt) {
        applyTorque((x * fy) - (y * fx), dt); // T = r x F

        final double k = ((x * fx) + (y * fy)) / ((x * x) + (y * y)); // r.F/|r|^2
        applyForce(k * x, k * y, dt);

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param F DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param t DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RigidBody2D applyForce(Force2D F, double x, double y, double t,
        double dt) {
        return applyForce(F.getVector(t).getPrimitiveElement(0),
            F.getVector(t).getPrimitiveElement(1), x, y, dt);
    }

    /**
     * Collides this particle with another. This method calculates the
     * resultant velocities.
     *
     * @param p DOCUMENT ME!
     * @param theta centre of mass deflection angle.
     * @param e coefficient of restitution.
     *
     * @return this.
     */
    public RigidBody2D collide(RigidBody2D p, double theta, double e) {
        final double totalMass = getMass() + p.getMass();
        final double deltaVx = p.getXVelocity() - getXVelocity();
        final double deltaVy = p.getYVelocity() - getYVelocity();
        final double cos = Math.cos(theta);
        final double sin = Math.sin(theta);
        setXVelocity(getXVelocity() +
            ((p.getMass() * ((e * ((deltaVx * cos) + (deltaVy * sin))) +
            deltaVx)) / totalMass));
        setYVelocity(getYVelocity() +
            ((p.getMass() * ((e * ((deltaVy * cos) - (deltaVx * sin))) +
            deltaVy)) / totalMass));
        p.setXVelocity(getXVelocity() -
            ((getMass() * ((e * ((deltaVx * cos) + (deltaVy * sin))) + deltaVx)) / totalMass));
        p.setYVelocity(getYVelocity() -
            ((getMass() * ((e * ((deltaVy * cos) - (deltaVx * sin))) + deltaVy)) / totalMass));

        return this;
    }

    /**
     * Collides this particle with another. This method calculates the
     * resultant angular velocities.
     *
     * @param p DOCUMENT ME!
     * @param e coefficient of restitution.
     *
     * @return this.
     */
    public RigidBody2D angularCollide(RigidBody2D p, double e) {
        final double meanMass = (angMass + p.angMass) / (e + 1.0);
        final double delta = p.angVel - angVel;
        angVel += ((p.angMass * delta) / meanMass);
        p.angVel -= ((angMass * delta) / meanMass);

        return this;
    }
}
