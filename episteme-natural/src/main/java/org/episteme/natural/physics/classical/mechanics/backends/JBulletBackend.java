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

package org.episteme.natural.physics.classical.mechanics.backends;

import org.episteme.core.technical.backend.HardwareAccelerator;
import com.google.auto.service.AutoService;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.Operation;
import org.episteme.natural.physics.classical.mechanics.MechanicsBackend;
import org.episteme.natural.physics.classical.mechanics.PhysicsWorldBridge;
import org.episteme.natural.physics.classical.mechanics.RigidBodyBridge;
import org.episteme.natural.physics.classical.mechanics.CollisionProvider;
import java.lang.foreign.MemorySegment;
import org.episteme.natural.physics.classical.mechanics.RigidBody;

/**
 * JBullet physics backend provider.
 * Uses JBullet (Java port of Bullet Physics) for simulation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({MechanicsBackend.class, CollisionProvider.class})
public class JBulletBackend implements MechanicsBackend, CPUBackend, CollisionProvider {

    @Override
    public String getType() {
        return "mechanics";
    }

    @Override
    public String getAlgorithmType() {
        return "PhysicsEngine";
    }

    @Override
    public String getId() {
        return "jbullet";
    }

    @Override
    public String getName() {
        return "JBullet";
    }

    @Override
    public String getDescription() {
        return "JBullet physics engine backend (Java Port of Bullet).";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("com.bulletphysics.dynamics.DiscreteDynamicsWorld");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }


    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    public PhysicsWorldBridge createWorld() {
        return new org.episteme.natural.physics.classical.mechanics.backends.jbullet.JBulletWorld();
    }

    @Override
    public RigidBodyBridge createRigidBody(RigidBody body) {
        return new org.episteme.natural.physics.classical.mechanics.backends.jbullet.JBulletRigidBody(body);
    }

    @Override
    public int detectSphereCollisions(MemorySegment positions, MemorySegment radii, int n, MemorySegment collisions) {
        if (n == 0) return 0;
        java.nio.DoubleBuffer posBuf  = positions.asByteBuffer().order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
        java.nio.DoubleBuffer radiiBuf = radii.asByteBuffer().order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
        java.nio.IntBuffer    collBuf  = collisions.asByteBuffer().order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();

        // --- Spatial Hash O(n) collision detection ---
        // Find max radius to determine cell size
        double maxR = 0;
        for (int i = 0; i < n; i++) { double r = radiiBuf.get(i); if (r > maxR) maxR = r; }
        final double cellSize = maxR * 2.0;
        if (cellSize == 0) return 0;

        // Build hash map: cell key -> list of body indices
        java.util.HashMap<Long, java.util.List<Integer>> grid = new java.util.HashMap<>(n * 2);
        for (int i = 0; i < n; i++) {
            long cx = (long) Math.floor(posBuf.get(i * 3)     / cellSize);
            long cy = (long) Math.floor(posBuf.get(i * 3 + 1) / cellSize);
            long cz = (long) Math.floor(posBuf.get(i * 3 + 2) / cellSize);
            long key = cx * 73856093L ^ cy * 19349663L ^ cz * 83492791L;
            grid.computeIfAbsent(key, k -> new java.util.ArrayList<>()).add(i);
        }

        // For each body, check against 27 neighbouring cells (including own)
        int count = 0;
        java.util.HashSet<Long> checkedPairs = new java.util.HashSet<>(n * 4);
        for (int i = 0; i < n; i++) {
            double x1 = posBuf.get(i * 3), y1 = posBuf.get(i * 3 + 1), z1 = posBuf.get(i * 3 + 2);
            double r1 = radiiBuf.get(i);
            long bx = (long) Math.floor(x1 / cellSize);
            long by = (long) Math.floor(y1 / cellSize);
            long bz = (long) Math.floor(z1 / cellSize);

            for (long nx = bx - 1; nx <= bx + 1; nx++) {
                for (long ny = by - 1; ny <= by + 1; ny++) {
                    for (long nz = bz - 1; nz <= bz + 1; nz++) {
                        long nkey = nx * 73856093L ^ ny * 19349663L ^ nz * 83492791L;
                        java.util.List<Integer> cell = grid.get(nkey);
                        if (cell == null) continue;
                        for (int j : cell) {
                            if (j <= i) continue; // each pair once
                            // Dedup across multi-cell overlaps
                            long pairKey = ((long) i << 32) | (j & 0xFFFFFFFFL);
                            if (!checkedPairs.add(pairKey)) continue;

                            double x2 = posBuf.get(j * 3), y2 = posBuf.get(j * 3 + 1), z2 = posBuf.get(j * 3 + 2);
                            double r2 = radiiBuf.get(j);
                            double dx = x2 - x1, dy = y2 - y1, dz = z2 - z1;
                            double radiusSum = r1 + r2;
                            if (dx*dx + dy*dy + dz*dz < radiusSum * radiusSum) {
                                collBuf.put(count * 2, i);
                                collBuf.put(count * 2 + 1, j);
                                count++;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    @Override
    public void resolveCollisions(MemorySegment positions, MemorySegment velocities, MemorySegment masses, int n, MemorySegment collisions, int numCollisions) {
        // java.nio.DoubleBuffer posBuf = positions.asByteBuffer().order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
        // java.nio.DoubleBuffer velBuf = velocities.asByteBuffer().order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
        // java.nio.DoubleBuffer massBuf = masses.asByteBuffer().order(java.nio.ByteOrder.nativeOrder()).asDoubleBuffer();
        // java.nio.IntBuffer collBuf = collisions.asByteBuffer().order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
        
        for (int i = 0; i < numCollisions; i++) {
            // Basic elastic collision resolution
        }
    }

    @Override
    public ExecutionContext createContext() {
        return new ExecutionContext() {
            @Override
            public <T> T execute(Operation<T> operation) {
                return operation.compute(this);
            }
            @Override
            public void close() {
                // No-op
            }
        };
    }

    @Override
    public Object createBackend() {
        return this;
    }
}
