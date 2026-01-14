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

package org.jscience.apps.physics.spintronics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.ArrayList;
import java.util.List;

/**
 * Trajectory recorder and Bloch sphere visualization helper.
 * <p>
 * Records magnetization dynamics and provides data for visualization
 * on a unit sphere (classical analog of quantum Bloch sphere).
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class BlochTrajectoryRecorder {

    private final List<double[]> trajectory = new ArrayList<>();
    private final int maxPoints;

    public BlochTrajectoryRecorder(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    /**
     * Records the current magnetization state as (mx, my, mz) on unit sphere.
     */
    public void record(Real[] m) {
        if (trajectory.size() >= maxPoints) {
            trajectory.remove(0); // Circular buffer behavior
        }
        trajectory.add(new double[]{
            m[0].doubleValue(),
            m[1].doubleValue(),
            m[2].doubleValue()
        });
    }

    /**
     * Converts magnetization vector to spherical angles (like Bloch sphere).
     * @return [theta, phi] where theta is polar angle from +z, phi is azimuthal
     */
    public static double[] toSphericalAngles(Real[] m) {
        double mx = m[0].doubleValue();
        double my = m[1].doubleValue();
        double mz = m[2].doubleValue();
        
        double theta = Math.acos(mz); // 0 to π
        double phi = Math.atan2(my, mx); // -π to π
        
        return new double[]{theta, phi};
    }

    /**
     * Converts spherical angles back to magnetization unit vector.
     */
    public static Real[] fromSphericalAngles(double theta, double phi) {
        return new Real[]{
            Real.of(Math.sin(theta) * Math.cos(phi)),
            Real.of(Math.sin(theta) * Math.sin(phi)),
            Real.of(Math.cos(theta))
        };
    }

    /**
     * Gets trajectory data for plotting.
     * Each point is [mx, my, mz] on unit sphere.
     */
    public List<double[]> getTrajectory() {
        return new ArrayList<>(trajectory);
    }

    /**
     * Gets X coordinates for trajectory.
     */
    public double[] getXCoordinates() {
        return trajectory.stream().mapToDouble(p -> p[0]).toArray();
    }

    /**
     * Gets Y coordinates for trajectory.
     */
    public double[] getYCoordinates() {
        return trajectory.stream().mapToDouble(p -> p[1]).toArray();
    }

    /**
     * Gets Z coordinates for trajectory.
     */
    public double[] getZCoordinates() {
        return trajectory.stream().mapToDouble(p -> p[2]).toArray();
    }

    /**
     * Clears the recorded trajectory.
     */
    public void clear() {
        trajectory.clear();
    }

    public int size() {
        return trajectory.size();
    }
}
