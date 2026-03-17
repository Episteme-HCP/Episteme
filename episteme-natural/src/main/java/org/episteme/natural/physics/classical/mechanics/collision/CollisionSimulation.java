/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.physics.classical.mechanics.collision;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.sets.Reals;
import java.lang.foreign.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * High-performance Collision Simulation for multiple colliding Rigid Bodies.
 */
public class CollisionSimulation {

    public static class Body {
        public RigidBody physicsBody;
        public double radius;
        public double bounciness = 0.8;
        
        public Body(RigidBody rb, double r) {
            this.physicsBody = rb;
            this.radius = r;
        }
    }

    private final List<Body> bodies = new ArrayList<>();
    private double gravity = 0.5;
    private double width = 800;
    private double height = 600;
    private CollisionProvider backend;
    private Arena arena;
    private MemorySegment positions;
    private MemorySegment velocities;
    private MemorySegment masses;
    private MemorySegment radii;
    private MemorySegment collisions;

    public CollisionSimulation() {
        this(null);
    }

    public CollisionSimulation(CollisionProvider provider) {
        this.backend = provider;
    }

    public void addBody(Body body) {
        bodies.add(body);
    }

    public void clear() {
        bodies.clear();
    }

    public void update(double speed) {
        Real dt = Real.of(0.2 * speed);
        for (Body vb : bodies) {
            RigidBody b = vb.physicsBody;
            Vector<Real> grav = toVector(0, gravity, 0);
            b.setVelocity(b.getVelocity().add(grav));
            b.integrate(dt);

            double x = b.getPosition().get(0).doubleValue();
            double y = b.getPosition().get(1).doubleValue();
            double vx = b.getVelocity().get(0).doubleValue();
            double vy = b.getVelocity().get(1).doubleValue();

            if (y + vb.radius > height) {
                y = height - vb.radius;
                vy *= -vb.bounciness;
                b.setPosition(toVector(x, y, 0));
                b.setVelocity(toVector(vx, vy, 0));
            }
            if (x - vb.radius < 0) {
                x = vb.radius;
                vx *= -vb.bounciness;
                b.setPosition(toVector(x, y, 0));
                b.setVelocity(toVector(vx, vy, 0));
            }
            if (x + vb.radius > width) {
                x = width - vb.radius;
                vx *= -vb.bounciness;
                b.setPosition(toVector(x, y, 0));
                b.setVelocity(toVector(vx, vy, 0));
            }
        }

        if (backend != null) {
            runCollisionPass(dt.doubleValue());
        }
    }

    private void runCollisionPass(double dt) {
        int n = bodies.size();
        if (n == 0) return;

        if (arena == null) {
            arena = Arena.ofShared();
        }

        long posSize = n * 3 * Double.BYTES;
        long velSize = n * 3 * Double.BYTES;
        long massSize = n * Double.BYTES;
        long radiiSize = n * Double.BYTES;
        long collSize = (long) n * n * 2 * Integer.BYTES;

        if (positions == null || positions.byteSize() < posSize) {
            positions = arena.allocate(posSize);
            velocities = arena.allocate(velSize);
            masses = arena.allocate(massSize);
            radii = arena.allocate(radiiSize);
            collisions = arena.allocate(collSize);
        }

        // Sync to segments
        for (int i = 0; i < n; i++) {
            Body b = bodies.get(i);
            RigidBody rb = b.physicsBody;
            positions.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 3, rb.getPosition().get(0).doubleValue());
            positions.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 1, rb.getPosition().get(1).doubleValue());
            positions.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 2, rb.getPosition().get(2).doubleValue());
            
            velocities.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 3, rb.getVelocity().get(0).doubleValue());
            velocities.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 1, rb.getVelocity().get(1).doubleValue());
            velocities.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 2, rb.getVelocity().get(2).doubleValue());
            
            masses.setAtIndex(ValueLayout.JAVA_DOUBLE, i, b.radius * b.radius);
            radii.setAtIndex(ValueLayout.JAVA_DOUBLE, i, b.radius);
        }

        int numCollisions = backend.detectSphereCollisions(positions, radii, n, collisions);
        if (numCollisions > 0) {
            backend.resolveCollisions(positions, velocities, masses, n, collisions, numCollisions);
        }

        // Sync back
        for (int i = 0; i < n; i++) {
            Body b = bodies.get(i);
            RigidBody rb = b.physicsBody;
            rb.setPosition(toVector(
                positions.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3),
                positions.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 1),
                positions.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 2)
            ));
            rb.setVelocity(toVector(
                velocities.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3),
                velocities.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 1),
                velocities.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 2)
            ));
        }
    }

    private Vector<Real> toVector(double x, double y, double z) {
        return DenseVector.of(Arrays.asList(Real.of(x), Real.of(y), Real.of(z)), Reals.getInstance());
    }

    public List<Body> getBodies() { return bodies; }
    public void setGravity(double g) { this.gravity = g; }
    public void setBounds(double w, double h) { this.width = w; this.height = h; }
}

