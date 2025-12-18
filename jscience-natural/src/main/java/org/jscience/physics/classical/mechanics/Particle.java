package org.jscience.physics.classical.mechanics;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Energy;

import java.util.ArrayList;
import java.util.List;

/**
 * A particle in N-body simulation using Generic Linear Algebra.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Particle {

    private Vector<Real> position;
    private Vector<Real> velocity;
    private Vector<Real> acceleration;
    private Quantity<Mass> mass;

    /**
     * Creates a particle from coordinates and mass.
     * 
     * @param x    x-coordinate (m)
     * @param y    y-coordinate (m)
     * @param z    z-coordinate (m)
     * @param mass mass (kg)
     */
    public Particle(double x, double y, double z, double massKg) {
        this.mass = Quantities.create(massKg, Units.KILOGRAM);
        this.position = createVector(x, y, z);
        this.velocity = createVector(0, 0, 0);
        this.acceleration = createVector(0, 0, 0);
    }

    /**
     * Creates a particle from vectors.
     * 
     * @param position position vector
     * @param velocity velocity vector
     * @param mass     mass
     */
    public Particle(Vector<Real> position, Vector<Real> velocity, Quantity<Mass> mass) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = createVector(0, 0, 0); // Need zero vector of same dim? Assuming 3D here mostly.
        // If passed vectors are not 3D, behavior is defined by Vector impl.
        if (acceleration.dimension() != position.dimension()) {
            // quick fix to ensure dims match if not 3D
            this.acceleration = DenseVector.of(java.util.Collections.nCopies(position.dimension(), Real.ZERO),
                    Reals.getInstance());
        }
        this.mass = mass;
    }

    // Legacy support
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

    // --- Accessors ---

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

    // Convenience for 3D
    public Real getX() {
        return position.get(0);
    }

    public Real getY() {
        return position.get(1);
    }

    public Real getZ() {
        return (position.dimension() > 2) ? position.get(2) : Real.ZERO;
    }

    public void setPosition(Real x, Real y, Real z) {
        // This effectively replaces the vector, assuming 3D
        this.position = createVector(x.doubleValue(), y.doubleValue(), z.doubleValue());
    }

    public Real distanceTo(Particle other) {
        return this.position.subtract(other.position).norm();
    }

    /**
     * Euluer integration step.
     * pos = pos + vel * dt
     */
    public void updatePosition(Real dt) {
        this.position = this.position.add(this.velocity.multiply(dt));
    }

    /**
     * Euler integration step.
     * vel = vel + acc * dt
     */
    public void updateVelocity(Real dt) {
        this.velocity = this.velocity.add(this.acceleration.multiply(dt));
    }

    public Quantity<Energy> kineticEnergy() {
        return Kinematics.kineticEnergy(mass, velocity);
    }

    public void setVelocity(Real x, Real y, Real z) {
        this.velocity = createVector(x.doubleValue(), y.doubleValue(), z.doubleValue());
    }

    public void setAcceleration(
            Real x,
            Real y,
            Real z) {
        this.acceleration = createVector(x.doubleValue(), y.doubleValue(), z.doubleValue());
    }

    // Legacy double setters for convenience (delegating to Real)
    public void setVelocity(double x, double y, double z) {
        setVelocity(Real.of(x), Real.of(y), Real.of(z));
    }

    public void setAcceleration(double x, double y, double z) {
        setAcceleration(Real.of(x), Real.of(y), Real.of(z));
    }

    @Override
    public String toString() {
        return String.format("Particle(m=%s, pos=%s)", mass, position);
    }
}
