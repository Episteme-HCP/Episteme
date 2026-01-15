package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;
import org.jscience.mathematics.algebraic.matrices.DoubleVector;

import org.jscience.util.IllegalDimensionException;


/**
 * The ClassicalParticle2D class provides an object for encapsulating
 * classical point particles that live in 2D.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class ClassicalParticle2D extends AbstractClassicalParticle {
    /** Mass. */
    private double mass;

    /** Charge. */
    private double charge;

    /** Position coordinates. */
    private double x;

    /** Position coordinates. */
    private double y;

    /** Velocity coordinates. */
    private double vx;

    /** Velocity coordinates. */
    private double vy;

/**
     * Constructs a classical particle.
     */
    public ClassicalParticle2D() {
        mass = 0;
        charge = 0;
        x = 0;
        y = 0;
        vx = 0;
        vy = 0;
    }

/**
     * Creates a new ClassicalParticle2D object.
     *
     * @param m DOCUMENT ME!
     */
    public ClassicalParticle2D(double m) {
        mass = m;
        charge = 0;
        x = 0;
        y = 0;
        vx = 0;
        vy = 0;
    }

    /**
     * Sets the mass of this particle.
     *
     * @param m DOCUMENT ME!
     */
    public void setMass(double m) {
        mass = m;
    }

    /**
     * Returns the mass of this particle.
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
     * Sets the position of this particle.
     *
     * @param xPos DOCUMENT ME!
     * @param yPos DOCUMENT ME!
     */
    public void setPosition(double xPos, double yPos) {
        x = xPos;
        y = yPos;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getPosition() {
        return new DoubleVector(new double[] { x, y });
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
     * Sets the velocity of this particle.
     *
     * @param xVel DOCUMENT ME!
     * @param yVel DOCUMENT ME!
     */
    public void setVelocity(double xVel, double yVel) {
        vx = xVel;
        vy = yVel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getVelocity() {
        return new DoubleVector(new double[] { vx, vy });
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
     * Returns the speed of this particle.
     *
     * @return DOCUMENT ME!
     */
    public double speed() {
        return Math.sqrt((vx * vx) + (vy * vy));
    }

    /**
     * Sets the momentum of this particle.
     *
     * @param xMom DOCUMENT ME!
     * @param yMom DOCUMENT ME!
     */
    public void setMomentum(double xMom, double yMom) {
        vx = xMom / mass;
        vy = yMom / mass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getMomentum() {
        return new DoubleVector(new double[] { mass * vx, mass * vy });
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
     * Returns the kinetic energy.
     *
     * @return DOCUMENT ME!
     */
    public double energy() {
        return (mass * ((vx * vx) + (vy * vy))) / 2.0;
    }

    //the dimension of this particle
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDimension() {
        return 2;
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
    public AbstractClassicalParticle translate(double dt) {
        x += (vx * dt);
        y += (vy * dt);

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
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public AbstractClassicalParticle accelerate(double ax, double ay, double dt) {
        vx += (ax * dt);
        vy += (ay * dt);

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
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public AbstractClassicalParticle applyForce(double Fx, double Fy, double dt) {
        return accelerate(Fx / mass, Fy / mass, dt);
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
            if (f.getDimension() == 2) {
                return applyForce(f.getVector(t).getPrimitiveElement(0),
                    f.getVector(t).getPrimitiveElement(1), dt);
            } else {
                throw new IllegalDimensionException(
                    "The Force is not in 2 dimensions.");
            }
        } else {
            throw new IllegalArgumentException("You can't apply a null force.");
        }
    }

    /**
     * Collides this particle with another (elastic collision). This
     * method calculates the resultant velocities.
     *
     * @param p DOCUMENT ME!
     * @param theta centre of mass deflection angle.
     *
     * @return this.
     */
    public AbstractClassicalParticle collide(ClassicalParticle2D p, double theta) {
        final double totalMass = mass + p.mass;
        final double deltaVx = p.vx - vx;
        final double deltaVy = p.vy - vy;
        final double cos = Math.cos(theta);
        final double sin = Math.sin(theta);
        vx += ((p.mass * ((deltaVx * cos) + (deltaVy * sin) + deltaVx)) / totalMass);
        vy += ((p.mass * ((deltaVy * cos) - (deltaVx * sin) + deltaVy)) / totalMass);
        p.vx -= ((mass * ((deltaVx * cos) + (deltaVy * sin) + deltaVx)) / totalMass);
        p.vy -= ((mass * ((deltaVy * cos) - (deltaVx * sin) + deltaVy)) / totalMass);

        return this;
    }
}
