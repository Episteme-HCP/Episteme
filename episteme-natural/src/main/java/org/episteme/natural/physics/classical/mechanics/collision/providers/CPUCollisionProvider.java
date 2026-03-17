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
import org.episteme.natural.physics.classical.mechanics.collision.CollisionProvider;

/**
 * CPU-based implementation of CollisionProvider.
 */
@AutoService(CollisionProvider.class)
public class CPUCollisionProvider implements CollisionProvider {

    @Override
    public int detectSphereCollisions(double[] positions, double[] radii, int n, int[] collisions) {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double x1 = positions[i * 3];
                double y1 = positions[i * 3 + 1];
                double z1 = positions[i * 3 + 2];
                
                double x2 = positions[j * 3];
                double y2 = positions[j * 3 + 1];
                double z2 = positions[j * 3 + 2];
                
                double dx = x2 - x1;
                double dy = y2 - y1;
                double dz = z2 - z1;
                double distSq = dx * dx + dy * dy + dz * dz;
                
                double r1 = radii[i];
                double r2 = radii[j];
                double minDist = r1 + r2;
                
                if (distSq < minDist * minDist) {
                    collisions[count * 2] = i;
                    collisions[count * 2 + 1] = j;
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void resolveCollisions(double[] positions, double[] velocities, double[] masses, int n, int[] collisions, int numCollisions) {
        for (int k = 0; k < numCollisions; k++) {
            int i = collisions[k * 2];
            int j = collisions[k * 2 + 1];
            
            double x1 = positions[i * 3];
            double y1 = positions[i * 3 + 1];
            double z1 = positions[i * 3 + 2];
            
            double x2 = positions[j * 3];
            double y2 = positions[j * 3 + 1];
            double z2 = positions[j * 3 + 2];
            
            double dx = x2 - x1;
            double dy = y2 - y1;
            double dz = z2 - z1;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            
            if (dist == 0) continue;
            
            double nx = dx / dist;
            double ny = dy / dist;
            double nz = dz / dist;
            
            double vx1 = velocities[i * 3];
            double vy1 = velocities[i * 3 + 1];
            double vz1 = velocities[i * 3 + 2];
            
            double vx2 = velocities[j * 3];
            double vy2 = velocities[j * 3 + 1];
            double vz2 = velocities[j * 3 + 2];
            
            double relVelX = vx2 - vx1;
            double relVelY = vy2 - vy1;
            double relVelZ = vz2 - vz1;
            double velAlongNormal = relVelX * nx + relVelY * ny + relVelZ * nz;
            
            if (velAlongNormal > 0) continue;
            
            double e = 0.8; // Standard bounciness
            double m1 = masses[i];
            double m2 = masses[j];
            
            double jImpulse = -(1 + e) * velAlongNormal / (1 / m1 + 1 / m2);
            
            double impulseX = jImpulse * nx;
            double impulseY = jImpulse * ny;
            double impulseZ = jImpulse * nz;
            
            velocities[i * 3] = vx1 - 1 / m1 * impulseX;
            velocities[i * 3 + 1] = vy1 - 1 / m1 * impulseY;
            velocities[i * 3 + 2] = vz1 - 1 / m1 * impulseZ;
            
            velocities[j * 3] = vx2 + 1 / m2 * impulseX;
            velocities[j * 3 + 1] = vy2 + 1 / m2 * impulseY;
            velocities[j * 3 + 2] = vz2 + 1 / m2 * impulseZ;
            
            // Positional correction
            double percent = 0.2;
            double slop = 0.01;
            double r1 = Math.sqrt(m1); // Approximated from area/mass
            double r2 = Math.sqrt(m2);
            double minDist = r1 + r2;
            
            double correction = Math.max(minDist - dist - slop, 0) / (1 / m1 + 1 / m2) * percent;
            positions[i * 3] = x1 - 1 / m1 * nx * correction;
            positions[i * 3 + 1] = y1 - 1 / m1 * ny * correction;
            positions[i * 3 + 2] = z1 - 1 / m1 * nz * correction;
            
            positions[j * 3] = x2 + 1 / m2 * nx * correction;
            positions[j * 3 + 1] = y2 + 1 / m2 * ny * correction;
            positions[j * 3 + 2] = z2 + 1 / m2 * nz * correction;
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
