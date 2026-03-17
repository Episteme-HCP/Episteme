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

package org.episteme.natural.physics.classical.mechanics.collision.providers;

import com.google.auto.service.AutoService;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import org.episteme.natural.physics.classical.mechanics.collision.CollisionProvider;

/**
 * Standard CPU implementation of the CollisionProvider.
 * Uses Java-based impulse resolution logic.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(CollisionProvider.class)
public class CPUCollisionProvider implements CollisionProvider {

    @Override
    public int detectSphereCollisions(MemorySegment positions, MemorySegment radii, int n, MemorySegment collisions) {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double x1 = positions.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3);
                double y1 = positions.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 1);
                double z1 = positions.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 2);
                
                double x2 = positions.getAtIndex(ValueLayout.JAVA_DOUBLE, j * 3);
                double y2 = positions.getAtIndex(ValueLayout.JAVA_DOUBLE, j * 3 + 1);
                double z2 = positions.getAtIndex(ValueLayout.JAVA_DOUBLE, j * 3 + 2);
                
                double dx = x2 - x1;
                double dy = y2 - y1;
                double dz = z2 - z1;
                double distSq = dx * dx + dy * dy + dz * dz;
                
                double r1 = radii.getAtIndex(ValueLayout.JAVA_DOUBLE, i);
                double r2 = radii.getAtIndex(ValueLayout.JAVA_DOUBLE, j);
                double minDist = r1 + r2;
                
                if (distSq < minDist * minDist) {
                    collisions.setAtIndex(ValueLayout.JAVA_INT, count * 2, i);
                    collisions.setAtIndex(ValueLayout.JAVA_INT, count * 2 + 1, j);
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void resolveCollisions(MemorySegment positions, MemorySegment velocities, MemorySegment masses, int n, MemorySegment collisions, int numCollisions) {
        for (int k = 0; k < numCollisions; k++) {
            int i = collisions.getAtIndex(ValueLayout.JAVA_INT, k * 2);
            int j = collisions.getAtIndex(ValueLayout.JAVA_INT, k * 2 + 1);
            
            double x1 = positions.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3);
            double y1 = positions.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 1);
            double z1 = positions.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 2);
            
            double x2 = positions.getAtIndex(ValueLayout.JAVA_DOUBLE, j * 3);
            double y2 = positions.getAtIndex(ValueLayout.JAVA_DOUBLE, j * 3 + 1);
            double z2 = positions.getAtIndex(ValueLayout.JAVA_DOUBLE, j * 3 + 2);
            
            double dx = x2 - x1;
            double dy = y2 - y1;
            double dz = z2 - z1;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            
            if (dist == 0) continue;
            
            double nx = dx / dist;
            double ny = dy / dist;
            double nz = dz / dist;
            
            double vx1 = velocities.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3);
            double vy1 = velocities.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 1);
            double vz1 = velocities.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 2);
            
            double vx2 = velocities.getAtIndex(ValueLayout.JAVA_DOUBLE, j * 3);
            double vy2 = velocities.getAtIndex(ValueLayout.JAVA_DOUBLE, j * 3 + 1);
            double vz2 = velocities.getAtIndex(ValueLayout.JAVA_DOUBLE, j * 3 + 2);
            
            double relVelX = vx2 - vx1;
            double relVelY = vy2 - vy1;
            double relVelZ = vz2 - vz1;
            double velAlongNormal = relVelX * nx + relVelY * ny + relVelZ * nz;
            
            if (velAlongNormal > 0) continue;
            
            double e = 0.8; // Standard bounciness
            double m1 = masses.getAtIndex(ValueLayout.JAVA_DOUBLE, i);
            double m2 = masses.getAtIndex(ValueLayout.JAVA_DOUBLE, j);
            
            double jImpulse = -(1 + e) * velAlongNormal / (1 / m1 + 1 / m2);
            
            double impulseX = jImpulse * nx;
            double impulseY = jImpulse * ny;
            double impulseZ = jImpulse * nz;
            
            velocities.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 3, vx1 - 1 / m1 * impulseX);
            velocities.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 1, vy1 - 1 / m1 * impulseY);
            velocities.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 2, vz1 - 1 / m1 * impulseZ);
            
            velocities.setAtIndex(ValueLayout.JAVA_DOUBLE, j * 3, vx2 + 1 / m2 * impulseX);
            velocities.setAtIndex(ValueLayout.JAVA_DOUBLE, j * 3 + 1, vy2 + 1 / m2 * impulseY);
            velocities.setAtIndex(ValueLayout.JAVA_DOUBLE, j * 3 + 2, vz2 + 1 / m2 * impulseZ);
            
            // Positional correction
            double percent = 0.2;
            double slop = 0.01;
            double r1 = Math.sqrt(m1); // Approximated from area/mass
            double r2 = Math.sqrt(m2);
            double minDist = r1 + r2;
            
            double correction = Math.max(minDist - dist - slop, 0) / (1 / m1 + 1 / m2) * percent;
            positions.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 3, x1 - 1 / m1 * nx * correction);
            positions.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 1, y1 - 1 / m1 * ny * correction);
            positions.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 3 + 2, z1 - 1 / m1 * nz * correction);
            
            positions.setAtIndex(ValueLayout.JAVA_DOUBLE, j * 3, x2 + 1 / m2 * nx * correction);
            positions.setAtIndex(ValueLayout.JAVA_DOUBLE, j * 3 + 1, y2 + 1 / m2 * ny * correction);
            positions.setAtIndex(ValueLayout.JAVA_DOUBLE, j * 3 + 2, z2 + 1 / m2 * nz * correction);
        }
    }

    @Override
    public String getName() {
        return "CPU Sphere Collision Provider";
    }

    @Override
    public String getId() {
        return "cpu-collision";
    }

    @Override
    public String getDescription() {
        return "Java-based collision detection and resolution for spheres.";
    }

    @Override
    public int getPriority() {
        return 10;
    }
}
