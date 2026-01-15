package org.jscience.physics.kinematics;

import org.jscience.mathematics.MathConstants;


/**
 * The RigidBody3D class provides an object for encapsulating rigid bodies
 * that live in 3D.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class RigidBody3D extends ClassicalParticle3D {
    /** Moment of inertia. */
    private double angMass;

    /** Angles (orientation). */
    private double angx;

    /** Angles (orientation). */
    private double angy;

    /** Angles (orientation). */
    private double angz;

    /** Angular velocity. */
    private double angxVel;

    /** Angular velocity. */
    private double angyVel;

    /** Angular velocity. */
    private double angzVel;

/**
     * Constructs a rigid body.
     */
    public RigidBody3D() {
    }

/**
     * Creates a new RigidBody3D object.
     *
     * @param m DOCUMENT ME!
     */
    public RigidBody3D(double m) {
        super(m);
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
     * Sets the angles (orientation) of this body.
     *
     * @param angleX an angle in radians.
     * @param angleY an angle in radians.
     * @param angleZ an angle in radians.
     */
    public void setOrientation(double angleX, double angleY, double angleZ) {
        angx = angleX;
        angy = angleY;
        angz = angleZ;
    }

    /**
     * Returns the x-axis angle of this body.
     *
     * @return an angle in radians.
     */
    public double getXOrientation() {
        return angx;
    }

    /**
     * Returns the y-axis angle of this body.
     *
     * @return an angle in radians.
     */
    public double getYOrientation() {
        return angy;
    }

    /**
     * Returns the z-axis angle of this body.
     *
     * @return an angle in radians.
     */
    public double getZOrientation() {
        return angz;
    }

    /**
     * DOCUMENT ME!
     *
     * @param angleXVel DOCUMENT ME!
     * @param angleYVel DOCUMENT ME!
     * @param angleZVel DOCUMENT ME!
     */
    public void setAngularVelocity(double angleXVel, double angleYVel,
        double angleZVel) {
        angxVel = angleXVel;
        angyVel = angleYVel;
        angzVel = angleZVel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getXAngularVelocity() {
        return angxVel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getYAngularVelocity() {
        return angyVel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getZAngularVelocity() {
        return angzVel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param angleXMom DOCUMENT ME!
     * @param angleYMom DOCUMENT ME!
     * @param angleZMom DOCUMENT ME!
     */
    public void setAngularMomentum(double angleXMom, double angleYMom,
        double angleZMom) {
        angxVel = angleXMom / angMass;
        angyVel = angleYMom / angMass;
        angzVel = angleZMom / angMass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getXAngularMomentum() {
        return angMass * angxVel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getYAngularMomentum() {
        return angMass * angyVel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getZAngularMomentum() {
        return angMass * angzVel;
    }

    /**
     * Returns the kinetic and rotational energy.
     *
     * @return DOCUMENT ME!
     */
    public double energy() {
        return ((getMass() * ((getXVelocity() * getXVelocity()) +
        (getYVelocity() * getYVelocity()) + (getZVelocity() * getZVelocity()))) +
        (angMass * ((angxVel * angxVel) + (angyVel * angyVel) +
        (angzVel * angzVel)))) / 2.0;
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
    public RigidBody3D rotate(double dt) {
        angx += (angxVel * dt);

        if (angx > MathConstants.TWO_PI) {
            angx -= MathConstants.TWO_PI;
        } else if (angx < 0.0) {
            angx += MathConstants.TWO_PI;
        }

        angy += (angyVel * dt);

        if (angy > MathConstants.TWO_PI) {
            angy -= MathConstants.TWO_PI;
        } else if (angy < 0.0) {
            angy += MathConstants.TWO_PI;
        }

        angz += (angzVel * dt);

        if (angz > MathConstants.TWO_PI) {
            angz -= MathConstants.TWO_PI;
        } else if (angz < 0.0) {
            angz += MathConstants.TWO_PI;
        }

        return this;
    }

    /**
     * Accelerates this particle. This method changes the particle's
     * angular velocity. It is additive, that is <code>angularAccelerate(a1,
     * dt).angularAccelerate(a2, dt)</code> is equivalent to
     * <code>angularAccelerate(a1+a2, dt)</code>.
     *
     * @param ax DOCUMENT ME!
     * @param ay DOCUMENT ME!
     * @param az DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public RigidBody3D angularAccelerate(double ax, double ay, double az,
        double dt) {
        angxVel += (ax * dt);
        angyVel += (ay * dt);
        angzVel += (az * dt);

        return this;
    }

    /**
     * Applies a torque to this particle. This method changes the
     * particle's angular velocity. It is additive, that is
     * <code>applyTorque(T1, dt).applyTorque(T2, dt)</code> is equivalent to
     * <code>applyTorque(T1+T2, dt)</code>.
     *
     * @param tx DOCUMENT ME!
     * @param ty DOCUMENT ME!
     * @param tz DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public RigidBody3D applyTorque(double tx, double ty, double tz, double dt) {
        return angularAccelerate(tx / angMass, ty / angMass, tz / angMass, dt);
    }

    /**
     * Applies a torque to this particle. This method changes the
     * particle's angular velocity. It is additive, that is
     * <code>applyTorque(T1, dt).applyTorque(T2, dt)</code> is equivalent to
     * <code>applyTorque(T1+T2, dt)</code>.
     *
     * @param T DOCUMENT ME!
     * @param t DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public RigidBody3D applyTorque(Torque3D T, double t, double dt) {
        return applyTorque(T.getVector(t).getPrimitiveElement(0),
            T.getVector(t).getPrimitiveElement(1),
            T.getVector(t).getPrimitiveElement(2), dt);
    }

    /**
     * Applies a force acting at a point away from the centre of mass.
     * Any resultant torques are also applied. This method changes the
     * particle's angular velocity.
     *
     * @param fx DOCUMENT ME!
     * @param fy DOCUMENT ME!
     * @param fz DOCUMENT ME!
     * @param x x-coordinate from centre of mass.
     * @param y y-coordinate from centre of mass.
     * @param z z-coordinate from centre of mass.
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public RigidBody3D applyForce(double fx, double fy, double fz, double x,
        double y, double z, double dt) {
        applyTorque((y * fz) - (z * fy), (z * fx) - (x * fz),
            (x * fy) - (y * fx), dt); // T = r x F

        final double k = ((x * fx) + (y * fy) + (z * fz)) / ((x * x) + (y * y) +
            (z * z)); // r.F/|r|^2
        applyForce(k * x, k * y, k * z, dt);

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param F DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     * @param t DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RigidBody3D applyForce(Force3D F, double x, double y, double z,
        double t, double dt) {
        return applyForce(F.getVector(t).getPrimitiveElement(0),
            F.getVector(t).getPrimitiveElement(1),
            F.getVector(t).getPrimitiveElement(2), x, y, z, dt);
    }
}
