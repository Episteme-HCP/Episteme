/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.computing.ai.swarm;

import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.numbers.real.Real;
import java.util.Random;

/**
 * Particle Swarm Optimization (PSO) - nature-inspired global optimizer.
 * <p>
 * Swarm of particles exploring search space.
 * Each particle: position, velocity, personal best, global best.
 * Update: v = ωv + c₁rand()(pbest - x) + c₂rand()(gbest - x)
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ParticleSwarmOptimization {

    private final Function<Real[], Real> objective;
    private final int swarmSize;
    private final int dimensions;
    private final Real[] lowerBounds;
    private final Real[] upperBounds;
    private final Random random = new Random();

    // PSO parameters
    private Real inertiaWeight = Real.of(0.7298); // ω: inertia
    private Real cognitiveCoeff = Real.of(1.49618); // c₁: personal attraction
    private Real socialCoeff = Real.of(1.49618); // c₂: global attraction

    /**
     * Particle in swarm.
     */
    private static class Particle {
        Real[] position;
        Real[] velocity;
        Real[] personalBest;
        Real personalBestFitness;
        Real currentFitness;

        Particle(int dimensions) {
            position = new Real[dimensions];
            velocity = new Real[dimensions];
            personalBest = new Real[dimensions];
            personalBestFitness = Real.of(Double.MAX_VALUE);
            currentFitness = Real.of(Double.MAX_VALUE);
        }
    }

    public ParticleSwarmOptimization(Function<Real[], Real> objective,
            int dimensions,
            Real[] lowerBounds,
            Real[] upperBounds,
            int swarmSize) {
        this.objective = objective;
        this.dimensions = dimensions;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.swarmSize = swarmSize;
    }

    /**
     * Runs PSO optimization.
     * 
     * @param iterations number of iterations
     * @return global best position found
     */
    public Real[] optimize(int iterations) {
        Particle[] swarm = new Particle[swarmSize];
        Real[] globalBest = new Real[dimensions];
        Real globalBestFitness = Real.of(Double.MAX_VALUE);

        // Initialize swarm
        for (int i = 0; i < swarmSize; i++) {
            swarm[i] = initializeParticle();
            swarm[i].currentFitness = objective.evaluate(swarm[i].position);

            if (swarm[i].currentFitness.compareTo(globalBestFitness) < 0) {
                globalBestFitness = swarm[i].currentFitness;
                System.arraycopy(swarm[i].position, 0, globalBest, 0, dimensions);
            }

            // Initialize personal best
            swarm[i].personalBestFitness = swarm[i].currentFitness;
            System.arraycopy(swarm[i].position, 0, swarm[i].personalBest, 0, dimensions);
        }

        // Main PSO loop
        for (int iter = 0; iter < iterations; iter++) {
            for (Particle particle : swarm) {
                updateVelocity(particle, globalBest);
                updatePosition(particle);

                // Evaluate new position
                particle.currentFitness = objective.evaluate(particle.position);

                // Update personal best
                if (particle.currentFitness.compareTo(particle.personalBestFitness) < 0) {
                    particle.personalBestFitness = particle.currentFitness;
                    System.arraycopy(particle.position, 0, particle.personalBest, 0, dimensions);
                }

                // Update global best
                if (particle.currentFitness.compareTo(globalBestFitness) < 0) {
                    globalBestFitness = particle.currentFitness;
                    System.arraycopy(particle.position, 0, globalBest, 0, dimensions);
                }
            }

            // Optional: adaptive inertia weight decay
            // inertiaWeight = inertiaWeight.multiply(Real.of(0.99));
        }

        return globalBest;
    }

    private Particle initializeParticle() {
        Particle p = new Particle(dimensions);

        for (int i = 0; i < dimensions; i++) {
            // Random position in bounds
            Real range = upperBounds[i].subtract(lowerBounds[i]);
            p.position[i] = lowerBounds[i].add(Real.of(random.nextDouble()).multiply(range));

            // Random velocity (fraction of range)
            Real maxVel = range.multiply(Real.of(0.1));
            p.velocity[i] = Real.of(random.nextDouble() * 2 - 1).multiply(maxVel);
        }

        return p;
    }

    private void updateVelocity(Particle particle, Real[] globalBest) {
        for (int i = 0; i < dimensions; i++) {
            Real r1 = Real.of(random.nextDouble());
            Real r2 = Real.of(random.nextDouble());

            // Cognitive component (personal attraction)
            Real cognitive = cognitiveCoeff
                    .multiply(r1)
                    .multiply(particle.personalBest[i].subtract(particle.position[i]));

            // Social component (global attraction)
            Real social = socialCoeff
                    .multiply(r2)
                    .multiply(globalBest[i].subtract(particle.position[i]));

            // Update velocity: v = ωv + c₁r₁(pbest - x) + c₂r₂(gbest - x)
            particle.velocity[i] = inertiaWeight.multiply(particle.velocity[i])
                    .add(cognitive)
                    .add(social);

            // Velocity clamping
            Real range = upperBounds[i].subtract(lowerBounds[i]);
            Real maxVel = range.multiply(Real.of(0.2));
            if (particle.velocity[i].abs().compareTo(maxVel) > 0) {
                particle.velocity[i] = particle.velocity[i].compareTo(Real.ZERO) > 0
                        ? maxVel
                        : maxVel.negate();
            }
        }
    }

    private void updatePosition(Particle particle) {
        for (int i = 0; i < dimensions; i++) {
            // x = x + v
            particle.position[i] = particle.position[i].add(particle.velocity[i]);

            // Boundary handling (reflect)
            if (particle.position[i].compareTo(lowerBounds[i]) < 0) {
                particle.position[i] = lowerBounds[i];
                particle.velocity[i] = particle.velocity[i].negate();
            } else if (particle.position[i].compareTo(upperBounds[i]) > 0) {
                particle.position[i] = upperBounds[i];
                particle.velocity[i] = particle.velocity[i].negate();
            }
        }
    }

    // Parameter setters
    public void setInertiaWeight(Real omega) {
        this.inertiaWeight = omega;
    }

    public void setCognitiveCoefficient(Real c1) {
        this.cognitiveCoeff = c1;
    }

    public void setSocialCoefficient(Real c2) {
        this.socialCoeff = c2;
    }

    /**
     * Constriction coefficient PSO variant.
     * <p>
     * χ = 2κ / |2 - φ - √(φ² - 4φ)|, φ = c₁ + c₂ > 4
     * Guarantees convergence.
     * </p>
     */
    public void useConstrictionCoefficients() {
        Real phi = cognitiveCoeff.add(socialCoeff);
        if (phi.compareTo(Real.of(4)) <= 0) {
            phi = Real.of(4.1);
        }

        Real kappa = Real.ONE;
        Real phiSq = phi.multiply(phi);
        Real sqrt = phiSq.subtract(phi.multiply(Real.of(4))).sqrt();
        Real denom = Real.of(2).subtract(phi).subtract(sqrt).abs();
        Real chi = Real.of(2).multiply(kappa).divide(denom);

        this.inertiaWeight = chi;
        this.cognitiveCoeff = chi.multiply(cognitiveCoeff);
        this.socialCoeff = chi.multiply(socialCoeff);
    }
}
