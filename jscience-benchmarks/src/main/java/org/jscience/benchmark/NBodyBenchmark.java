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

package org.jscience.benchmark;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.structures.rings.Field;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Time;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * N-body gravitational simulation using JScience API.
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NBodyBenchmark implements RunnableBenchmark {
    /** Gravitational constant (mÃ‚Â³/(kgÃ‚Â·sÃ‚Â²)) */
    public static final Real G = Real.of(6.67430e-11);

    /** Real number field for vector operations */
    private static final Field<Real> REAL_FIELD = Real.ZERO;

    private final List<Body> bodies;

    /**
     * Creates a 3D Real vector.
     */
    private static Vector<Real> vec3(Real x, Real y, Real z) {
        return DenseVector.of(Arrays.asList(x, y, z), REAL_FIELD);
    }

    /**
     * Creates a 3D Real vector from doubles.
     */
    private static Vector<Real> vec3(double x, double y, double z) {
        return vec3(Real.of(x), Real.of(y), Real.of(z));
    }

    /**
     * Represents a celestial body with mass, position, and velocity.
     */
    public static class Body {
        private final String name;
        private final Quantity<Mass> mass;
        private Vector<Real> position; // 3D position in meters
        private Vector<Real> velocity; // 3D velocity in m/s
        private Vector<Real> force; // Accumulated force

        public Body(String name, Quantity<Mass> mass, Vector<Real> position, Vector<Real> velocity) {
            this.name = name;
            this.mass = mass;
            this.position = position;
            this.velocity = velocity;
            this.force = vec3(Real.ZERO, Real.ZERO, Real.ZERO);
        }

        public String getName() {
            return name;
        }

        public Quantity<Mass> getMass() {
            return mass;
        }

        public Real getMassKg() {
            return Real.of(mass.getValue(Units.KILOGRAM).doubleValue());
        }

        public Vector<Real> getPosition() {
            return position;
        }

        public Vector<Real> getVelocity() {
            return velocity;
        }

        public Vector<Real> getForce() {
            return force;
        }

        public void setPosition(Vector<Real> p) {
            this.position = p;
        }

        public void setVelocity(Vector<Real> v) {
            this.velocity = v;
        }

        public void setForce(Vector<Real> f) {
            this.force = f;
        }

        public void resetForce() {
            this.force = vec3(Real.ZERO, Real.ZERO, Real.ZERO);
        }
    }

    /**
     * Creates empty simulation.
     */
    public NBodyBenchmark() {
        this.bodies = new ArrayList<>();
    }

    /**
     * Adds a body to the simulation.
     */
    public void addBody(Body body) {
        bodies.add(body);
    }

    /**
     * Creates a body with convenient parameters.
     */
    public void addBody(String name, double massKg, double[] positionM, double[] velocityMps) {
        Quantity<Mass> mass = Quantities.create(massKg, Units.KILOGRAM);
        Vector<Real> pos = vec3(positionM[0], positionM[1], positionM[2]);
        Vector<Real> vel = vec3(velocityMps[0], velocityMps[1], velocityMps[2]);
        bodies.add(new Body(name, mass, pos, vel));
    }

    /**
     * Calculate gravitational forces between all pairs.
     * Uses Newton's law of gravitation: F = G*m1*m2/rÃ‚Â²
     */
    public void calculateForces() {
        // Reset all forces
        for (Body b : bodies) {
            b.resetForce();
        }

        // Pairwise force calculation
        for (int i = 0; i < bodies.size(); i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
                Body a = bodies.get(i);
                Body b = bodies.get(j);

                // Distance vector: r = b.pos - a.pos
                Vector<Real> r = b.getPosition().subtract(a.getPosition());

                // Distance magnitude with softening
                Real distSq = r.get(0).multiply(r.get(0))
                        .add(r.get(1).multiply(r.get(1)))
                        .add(r.get(2).multiply(r.get(2)));
                Real softening = Real.of(1e18); // Prevent singularity (1e9 m)Ã‚Â²
                Real denominator = distSq.add(softening);
                Real dist = Real.of(Math.sqrt(distSq.doubleValue()));

                // Force magnitude: F = G * m1 * m2 / rÃ‚Â²
                Real forceMag = G.multiply(a.getMassKg()).multiply(b.getMassKg())
                        .divide(denominator);

                // Force vector (unit direction * magnitude)
                Real invDist = (dist.doubleValue() > 0) ? Real.of(1.0 / dist.doubleValue()) : Real.ZERO;
                Vector<Real> forceVec = r.multiply(forceMag.multiply(invDist));

                // Apply equal and opposite forces (Newton's 3rd law)
                a.setForce(a.getForce().add(forceVec));
                b.setForce(b.getForce().subtract(forceVec));
            }
        }
    }

    /**
     * Update velocities using calculated forces.
     * a = F/m, v = v + a*dt
     */
    public void updateVelocities(Quantity<Time> dt) {
        Real dtSeconds = Real.of(dt.getValue(Units.SECOND).doubleValue());

        for (Body body : bodies) {
            Real massKg = body.getMassKg();
            Vector<Real> acceleration = body.getForce().multiply(Real.of(1.0 / massKg.doubleValue()));
            Vector<Real> deltaV = acceleration.multiply(dtSeconds);
            body.setVelocity(body.getVelocity().add(deltaV));
        }
    }

    /**
     * Update positions using velocities.
     * x = x + v*dt
     */
    public void updatePositions(Quantity<Time> dt) {
        Real dtSeconds = Real.of(dt.getValue(Units.SECOND).doubleValue());

        for (Body body : bodies) {
            Vector<Real> deltaP = body.getVelocity().multiply(dtSeconds);
            body.setPosition(body.getPosition().add(deltaP));
        }
    }

    /**
     * Perform one simulation step using leapfrog integration.
     */
    public void step(Quantity<Time> dt) {
        Quantity<Time> halfDt = Quantities.create(
                dt.getValue(Units.SECOND).doubleValue() / 2.0, Units.SECOND);

        updateVelocities(halfDt);
        updatePositions(dt);
        calculateForces();
        updateVelocities(halfDt);
    }

    /**
     * Calculate total system energy for validation.
     */
    public Real totalEnergy() {
        Real kinetic = Real.ZERO;
        for (Body body : bodies) {
            Vector<Real> v = body.getVelocity();
            Real v2 = v.get(0).multiply(v.get(0))
                    .add(v.get(1).multiply(v.get(1)))
                    .add(v.get(2).multiply(v.get(2)));
            kinetic = kinetic.add(body.getMassKg().multiply(v2).multiply(Real.of(0.5)));
        }

        Real potential = Real.ZERO;
        for (int i = 0; i < bodies.size(); i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
                Body a = bodies.get(i);
                Body b = bodies.get(j);
                Vector<Real> r = b.getPosition().subtract(a.getPosition());
                Real dist = Real.of(Math.sqrt(
                        r.get(0).multiply(r.get(0))
                                .add(r.get(1).multiply(r.get(1)))
                                .add(r.get(2).multiply(r.get(2))).doubleValue()));
                if (dist.doubleValue() > 0) {
                    potential = potential.subtract(
                            G.multiply(a.getMassKg()).multiply(b.getMassKg()).divide(dist));
                }
            }
        }

        return kinetic.add(potential);
    }

    /**
     * Initialize with solar system bodies.
     */
    public static NBodyBenchmark createSolarSystem() {
        NBodyBenchmark sim = new NBodyBenchmark();

        // Sun at origin
        sim.addBody("Sun", 1.989e30, new double[] { 0, 0, 0 }, new double[] { 0, 0, 0 });

        // Earth
        sim.addBody("Earth", 5.972e24,
                new double[] { 1.496e11, 0, 0 }, // 1 AU
                new double[] { 0, 29780, 0 }); // Orbital velocity

        // Mars
        sim.addBody("Mars", 6.39e23,
                new double[] { 2.279e11, 0, 0 }, // 1.52 AU
                new double[] { 0, 24077, 0 });

        // Jupiter
        sim.addBody("Jupiter", 1.898e27,
                new double[] { 7.785e11, 0, 0 }, // 5.2 AU
                new double[] { 0, 13070, 0 });

        return sim;
    }

    public List<Body> getBodies() {
        return bodies;
    }

    public int size() {
        return bodies.size();
    }

    /**
     * Benchmark runner comparing with naive implementation.
     */
    public static void main(String[] args) {
        System.out.println(I18n.getInstance().get("benchmark.nbody.title"));
        System.out.println("=========================================");

        NBodyBenchmark sim = createSolarSystem();
        Quantity<Time> dt = Quantities.create(3600.0, Units.SECOND); // 1 hour

        sim.calculateForces();
        Real initialEnergy = sim.totalEnergy();
        System.out.println(I18n.getInstance().get("benchmark.nbody.energy.initial", initialEnergy.doubleValue()));

        int steps = 100;
        long start = System.nanoTime();
        for (int i = 0; i < steps; i++) {
            sim.step(dt);
        }
        long end = System.nanoTime();

        Real finalEnergy = sim.totalEnergy();
        double energyDrift = Math.abs((finalEnergy.doubleValue() - initialEnergy.doubleValue())
                / initialEnergy.doubleValue()) * 100;

        System.out.println(I18n.getInstance().get("benchmark.nbody.energy.final", finalEnergy.doubleValue()));
        System.out.println(I18n.getInstance().get("benchmark.nbody.energy.drift", energyDrift));
        System.out.println(I18n.getInstance().get("benchmark.nbody.time", steps, (end - start) / 1_000_000));
        System.out.println(I18n.getInstance().get("benchmark.nbody.bodies", sim.size()));
    }
    // --- RunnableBenchmark Implementation ---

    @Override
    public String getName() {
        return I18n.getInstance().get("benchmark.nbody.name");
    }

    @Override
    public String getDomain() {
        return I18n.getInstance().get("benchmark.domain.physics");
    }

    @Override
    public void setup() {
        // Reset or initialize
        this.bodies.clear();
        // Setup solar system
        NBodyBenchmark sim = createSolarSystem();
        this.bodies.addAll(sim.getBodies());
    }

    @Override
    public void run() {
        // Run one step or a few steps
        Quantity<Time> dt = Quantities.create(3600.0, Units.SECOND);
        step(dt);
    }

    @Override
    public void teardown() {
        this.bodies.clear();
    }
}

