package org.jscience.physics.classical.mechanics;

import java.util.ArrayList;
import java.util.List;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.physics.PhysicalConstants;

import org.jscience.measure.Units;

/**
 * Direct N-body gravitational simulation (O(n²)).
 * <p>
 * Use {@link BarnesHutSimulation} for O(n log n) with many particles.
 * </p>
 * Modernized to JScience.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class NBodySimulation {

    private final List<Particle> particles;
    private Real G = PhysicalConstants.G; // Use constant
    private Real softening = Real.of(0.01);

    public NBodySimulation() {
        this.particles = new ArrayList<>();
    }

    public NBodySimulation(Real G) {
        this();
        this.G = G;
    }

    public void addParticle(Particle p) {
        particles.add(p);
    }

    public void addParticle(double x, double y, double z, double mass) {
        particles.add(new Particle(x, y, z, mass));
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setSoftening(Real s) {
        this.softening = s;
    }

    public void computeForces() {
        int n = particles.size();
        // Reset accelerations
        for (Particle p : particles) {
            // Assuming 3D zero vector
            p.setAcceleration(Real.ZERO, Real.ZERO, Real.ZERO);
        }

        for (int i = 0; i < n; i++) {
            Particle pi = particles.get(i);
            Vector<Real> posI = pi.getPosition();

            for (int j = i + 1; j < n; j++) {
                Particle pj = particles.get(j);
                Vector<Real> posJ = pj.getPosition();

                Vector<Real> rVec = posJ.subtract(posI);
                Real r2 = rVec.norm().pow(2).add(softening.pow(2));
                Real r = r2.sqrt();
                Real r3 = r2.multiply(r);

                // F = G * m1 * m2 / r^2
                // a = F / m
                // a_i += G * m_j * r_vec / r^3
                Real factor = G.divide(r3);

                Vector<Real> accPartI = rVec
                        .multiply(factor.multiply(Real.of(pj.getMass().to(Units.KILOGRAM).getValue().doubleValue())));
                Vector<Real> accPartJ = rVec
                        .multiply(factor.multiply(Real.of(pi.getMass().to(Units.KILOGRAM).getValue().doubleValue())));

                pi.setAcceleration(pi.getAcceleration().add(accPartI));
                pj.setAcceleration(pj.getAcceleration().subtract(accPartJ));
            }
        }
    }

    public void step(Real dt) {
        for (Particle p : particles) {
            Vector<Real> a = p.getAcceleration();
            Vector<Real> v = p.getVelocity();
            p.setVelocity(v.add(a.multiply(dt.multiply(Real.of(0.5)))));
        }

        for (Particle p : particles)
            p.updatePosition(dt);

        computeForces();

        for (Particle p : particles) {
            Vector<Real> a = p.getAcceleration();
            Vector<Real> v = p.getVelocity();
            p.setVelocity(v.add(a.multiply(dt.multiply(Real.of(0.5)))));
        }
    }

    public void run(Real dt, int steps) {
        computeForces();
        for (int i = 0; i < steps; i++)
            step(dt);
    }

    public Real kineticEnergy() {
        Real ke = Real.ZERO;
        for (Particle p : particles)
            ke = ke.add(Real.of(p.kineticEnergy().to(Units.JOULE).getValue().doubleValue()));
        return ke;
    }

    public Real potentialEnergy() {
        Real pe = Real.ZERO;
        int n = particles.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Particle pi = particles.get(i);
                Particle pj = particles.get(j);
                Real r = pi.distanceTo(pj);
                if (r.compareTo(softening) > 0)
                    pe = pe.subtract(G.multiply(Real.of(pi.getMass().to(Units.KILOGRAM).getValue().doubleValue()))
                            .multiply(Real.of(pj.getMass().to(Units.KILOGRAM).getValue().doubleValue())).divide(r));
            }
        }
        return pe;
    }

    public Real totalEnergy() {
        return kineticEnergy().add(potentialEnergy());
    }

    public static NBodySimulation sunEarth() {
        NBodySimulation sim = new NBodySimulation();
        Particle sun = new Particle(0, 0, 0, 1.989e30);
        Particle earth = new Particle(1.496e11, 0, 0, 5.972e24);
        earth.setVelocity(0, 29780, 0);
        sim.addParticle(sun);
        sim.addParticle(earth);
        return sim;
    }
}
