package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;
import org.jscience.mathematics.algebraic.matrices.DoubleVector;


/**
 * The ClassicalParticle class provides an object for encapsulating
 * classical point particles. This class is suitable for representing
 * particles that live in an arbitrary number of dimensions.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class ClassicalParticleND extends AbstractClassicalParticle {
    /** DOCUMENT ME! */
    private double mass;

    /** DOCUMENT ME! */
    private double charge;

    /** DOCUMENT ME! */
    private double[] x;

    /** DOCUMENT ME! */
    private double[] v;

/**
     * Constructs a classical particle.
     *
     * @param n number of dimensions.
     */
    public ClassicalParticleND(int n) {
        mass = 0;
        charge = 0;
        x = new double[n];
        v = new double[n];
    }

/**
     * Creates a new ClassicalParticleND object.
     *
     * @param n DOCUMENT ME!
     * @param m DOCUMENT ME!
     */
    public ClassicalParticleND(int n, double m) {
        mass = m;
        charge = 0;
        x = new double[n];
        v = new double[n];
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
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     */
    public void setPosition(double[] pos) {
        x = pos;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getPosition() {
        return new DoubleVector(x);
    }

    /**
     * DOCUMENT ME!
     *
     * @param vel DOCUMENT ME!
     */
    public void setVelocity(double[] vel) {
        v = vel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getVelocity() {
        return new DoubleVector(v);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double speedSqr() {
        double vv = v[0] * v[0];

        for (int i = 1; i < v.length; i++)
            vv += (v[i] * v[i]);

        return vv;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double speed() {
        return Math.sqrt(speedSqr());
    }

    /**
     * DOCUMENT ME!
     *
     * @param mom DOCUMENT ME!
     */
    public void setMomentum(double[] mom) {
        for (int i = 0; i < v.length; i++)
            v[i] = mom[i] / mass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractDoubleVector getMomentum() {
        double[] mom = new double[v.length];

        for (int i = 0; i < v.length; i++)
            mom[i] = mass * v[i];

        return new DoubleVector(mom);
    }

    /**
     * Returns the energy of this particle.
     *
     * @return DOCUMENT ME!
     */
    public double energy() {
        return (mass * speedSqr()) / 2.0;
    }

    //the dimension of this particle
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDimension() {
        return x.length;
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
        for (int i = 0; i < x.length; i++)
            x[i] += (v[i] * dt);

        return this;
    }

    /**
     * Accelerates this particle. This method changes the particle's
     * velocity. It is additive, that is <code>accelerate(a1,
     * dt).accelerate(a2, dt)</code> is equivalent to <code>accelerate(a1+a2,
     * dt)</code>.
     *
     * @param a DOCUMENT ME!
     * @param dt DOCUMENT ME!
     *
     * @return this.
     */
    public AbstractClassicalParticle accelerate(double[] a, double dt) {
        for (int i = 0; i < x.length; i++)
            v[i] += (a[i] * dt);

        return this;
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
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AbstractClassicalParticle applyForce(Force f, double t, double dt) {
        AbstractDoubleVector abstractDoubleVector;

        if (f != null) {
            if (f.getDimension() == getDimension()) {
                abstractDoubleVector = f.getVector(t);

                for (int i = 0; i < x.length; i++)
                    v[i] += ((abstractDoubleVector.getPrimitiveElement(i) * dt) / mass);

                return this;
            } else {
                throw new IllegalArgumentException(
                    "Force and Particle must have the same dimension");
            }
        } else {
            throw new IllegalArgumentException("You can't apply a null force.");
        }
    }
}
