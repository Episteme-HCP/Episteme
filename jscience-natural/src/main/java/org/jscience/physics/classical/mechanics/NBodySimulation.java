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

import java.util.ArrayList;
import java.util.List;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.physics.PhysicalConstants;
import org.jscience.measure.Units;

import org.jscience.technical.backend.algorithms.NBodyProvider;
import org.jscience.technical.backend.algorithms.MulticoreNBodyProvider;

/**
 * Direct N-body gravitational simulation (O(n²)).
 * 
 * Simulates particle interactions using Newton's law of universal gravitation.
 * For large-scale simulations, use {@link BarnesHutSimulation} for O(N log N)
 * complexity.
 * 
 * <p>
 * References:
 * <ul>
 * <li>Newton, I. (1687). Philosophiae Naturalis Principia Mathematica.</li>
 * <li>Aarseth, S. J. (2003). Gravitational N-body simulations: tools and
 * algorithms. Cambridge University Press.</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NBodySimulation {

    private final List<Particle> particles;
    private Real G = PhysicalConstants.G;
    private Real softening = Real.of(0.01);
    private NBodyProvider provider;

    public void setProvider(NBodyProvider p) {
        this.provider = p;
    }

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
        if (provider == null) {
            provider = new MulticoreNBodyProvider();
        }

        int n = particles.size();
        Real[] positions = new Real[n * 3];
        Real[] masses = new Real[n];
        Real[] forces = new Real[n * 3];

        // Pack data
        for (int i = 0; i < n; i++) {
            Particle p = particles.get(i);
            Vector<Real> pos = p.getPosition();
            // Assuming Vector<Real> usually has 3 components for 3D simulation
            // We need to access components. Vector abstraction might make this tricky if
            // not careful.
            // Let's assume get(i) works or similar. Actually, Particle usually stores x,y,z
            // or Vector.
            // In typical JScience Vector usage:
            positions[i * 3] = pos.get(0);
            positions[i * 3 + 1] = pos.get(1);
            positions[i * 3 + 2] = pos.get(2);

            masses[i] = Real.of(p.getMass().to(Units.KILOGRAM).getValue().doubleValue());
        }

        // Compute
        provider.computeForces(positions, masses, forces, G, softening);

        // Unpack and update acceleration (a = F/m)
        for (int i = 0; i < n; i++) {
            Particle p = particles.get(i);
            Real mx = forces[i * 3];
            Real my = forces[i * 3 + 1];
            Real mz = forces[i * 3 + 2];
            Real mass = masses[i];

            // a = F / m
            Real ax = mx.divide(mass);
            Real ay = my.divide(mass);
            Real az = mz.divide(mass);

            p.setAcceleration(ax, ay, az);
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
