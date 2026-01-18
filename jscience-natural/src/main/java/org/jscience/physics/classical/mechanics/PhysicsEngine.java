/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.physics.classical.mechanics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.sets.Reals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple 2D Physics Engine for multiple colliding Rigid Bodies.
 */
public class PhysicsEngine {

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

        // Inter-body collisions
        for (int i = 0; i < bodies.size(); i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
                Body vb1 = bodies.get(i);
                Body vb2 = bodies.get(j);
                RigidBody b1 = vb1.physicsBody;
                RigidBody b2 = vb2.physicsBody;

                double x1 = b1.getPosition().get(0).doubleValue();
                double y1 = b1.getPosition().get(1).doubleValue();
                double x2 = b2.getPosition().get(0).doubleValue();
                double y2 = b2.getPosition().get(1).doubleValue();

                double dx = x2 - x1;
                double dy = y2 - y1;
                double dist = Math.sqrt(dx * dx + dy * dy);
                double minDist = vb1.radius + vb2.radius;

                if (dist < minDist && dist > 0) {
                    double nx = dx / dist;
                    double ny = dy / dist;

                    double vx1 = b1.getVelocity().get(0).doubleValue();
                    double vy1 = b1.getVelocity().get(1).doubleValue();
                    double vx2 = b2.getVelocity().get(0).doubleValue();
                    double vy2 = b2.getVelocity().get(1).doubleValue();

                    double relVelX = vx2 - vx1;
                    double relVelY = vy2 - vy1;
                    double velAlongNormal = relVelX * nx + relVelY * ny;

                    if (velAlongNormal > 0) continue;

                    double e = Math.min(vb1.bounciness, vb2.bounciness);
                    double m1 = vb1.radius * vb1.radius;
                    double m2 = vb2.radius * vb2.radius;

                    double jImpulse = -(1 + e) * velAlongNormal / (1 / m1 + 1 / m2);

                    double impulseX = jImpulse * nx;
                    double impulseY = jImpulse * ny;

                    vx1 -= 1 / m1 * impulseX;
                    vy1 -= 1 / m1 * impulseY;
                    vx2 += 1 / m2 * impulseX;
                    vy2 += 1 / m2 * impulseY;

                    b1.setVelocity(toVector(vx1, vy1, 0));
                    b2.setVelocity(toVector(vx2, vy2, 0));

                    double percent = 0.2, slop = 0.01;
                    double correction = Math.max(minDist - dist - slop, 0) / (1 / m1 + 1 / m2) * percent;

                    x1 -= 1 / m1 * nx * correction;
                    y1 -= 1 / m1 * ny * correction;
                    x2 += 1 / m2 * nx * correction;
                    y2 += 1 / m2 * ny * correction;

                    b1.setPosition(toVector(x1, y1, 0));
                    b2.setPosition(toVector(x2, y2, 0));
                }
            }
        }
    }

    private Vector<Real> toVector(double x, double y, double z) {
        return DenseVector.of(Arrays.asList(Real.of(x), Real.of(y), Real.of(z)), Reals.getInstance());
    }

    public List<Body> getBodies() { return bodies; }
    public void setGravity(double g) { this.gravity = g; }
    public void setBounds(double w, double h) { this.width = w; this.height = h; }
}
