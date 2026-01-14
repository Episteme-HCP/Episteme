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

package org.jscience.technical.backend.algorithms;

import org.jscience.mathematics.geometry.Vector4D;

import java.util.ArrayList;
import java.util.List;

/**
 * Multicore (Analytical) implementation of MaxwellProvider.
 * Computes the electromagnetic field as a superposition of oscillating dipoles.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MulticoreMaxwellProvider implements MaxwellProvider {

    private final List<DipoleSource> sources = new ArrayList<>();

    public MulticoreMaxwellProvider() {
        // Add some default oscillating sources (e.g., a simple antenna)
        sources.add(new DipoleSource(new double[] { 0, 0, 0 }, new double[] { 0, 0, 1 }, 1.0, 1.0));
    }

    public void addSource(DipoleSource source) {
        sources.add(source);
    }

    @Override
    public double[][] computeTensor(Vector4D point) {
        double t = point.getCt(); // Assuming c=1
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();

        // Use parallel stream to sum contributions if there are many sources
        double[] totalE = new double[3];
        double[] totalB = new double[3];

        sources.parallelStream().forEach(source -> {
            double[] EB = source.computeFields(t, x, y, z);
            synchronized (totalE) {
                totalE[0] += EB[0];
                totalE[1] += EB[1];
                totalE[2] += EB[2];
                totalB[0] += EB[3];
                totalB[1] += EB[4];
                totalB[2] += EB[5];
            }
        });

        // Construct F_mu_nu (antisymmetric tensor)
        // Indices: 0=t, 1=x, 2=y, 3=z
        double[][] f = new double[4][4];

        // F_0i = -E_i
        f[0][1] = -totalE[0];
        f[0][2] = -totalE[1];
        f[0][3] = -totalE[2];

        // F_i0 = E_i
        f[1][0] = totalE[0];
        f[2][0] = totalE[1];
        f[3][0] = totalE[2];

        // F_12 = B_z, F_21 = -B_z
        f[1][2] = totalB[2];
        f[2][1] = -totalB[2];

        // F_13 = -B_y, F_31 = B_y
        f[1][3] = -totalB[1];
        f[3][1] = totalB[1];

        // F_23 = B_x, F_32 = -B_x
        f[2][3] = totalB[0];
        f[3][2] = -totalB[0];

        return f;
    }

    @Override
    public String getName() {
        return "Multicore Maxwell Analytical";
    }

    /**
     * Represents an oscillating electric dipole source.
     */
    public static class DipoleSource {
        private final double[] pos;
        private final double[] p0; // Dipole moment amplitude vector
        private final double omega;
        private final double phase;

        public DipoleSource(double[] pos, double[] p0, double omega, double phase) {
            this.pos = pos;
            this.p0 = p0;
            this.omega = omega;
            this.phase = phase;
        }

        /**
         * Computes E and B field contributions at (t, x, y, z).
         * Using simplified far-field approximation or full Hertzian dipole formulas.
         */
        public double[] computeFields(double t, double x, double y, double z) {
            double rx = x - pos[0];
            double ry = y - pos[1];
            double rz = z - pos[2];
            double r = Math.sqrt(rx * rx + ry * ry + rz * rz);

            if (r < 1e-9)
                return new double[6]; // Avoid singularity

            double tRet = t - r; // Retarded time (c=1)
            double arg = omega * tRet + phase;
            double sinArg = Math.sin(arg);

            // p(t) = p0 * sin(omega * t + phase)
            // p_dot = p0 * omega * cos(arg)
            // p_ddot = -p0 * omega^2 * sin(arg)

            // Radiation fields (far field ~ 1/r)
            // E = 1/r * (r_hat cross (r_hat cross p_ddot))
            // B = -1/r * (r_hat cross p_ddot)

            double p_dd_x = -p0[0] * omega * omega * sinArg;
            double p_dd_y = -p0[1] * omega * omega * sinArg;
            double p_dd_z = -p0[2] * omega * omega * sinArg;

            double nx = rx / r;
            double ny = ry / r;
            double nz = rz / r;

            // B = - (n x p_ddot) / r
            double bx = -(ny * p_dd_z - nz * p_dd_y) / r;
            double by = -(nz * p_dd_x - nx * p_dd_z) / r;
            double bz = -(nx * p_dd_y - ny * p_dd_x) / r;

            // E = (n x (n x p_ddot)) / r
            // n x (n x p_ddot) = n(n.p_ddot) - p_ddot(n.n) = n(n.p_ddot) - p_ddot
            double nDotPdd = nx * p_dd_x + ny * p_dd_y + nz * p_dd_z;
            double ex = (nx * nDotPdd - p_dd_x) / r;
            double ey = (ny * nDotPdd - p_dd_y) / r;
            double ez = (nz * nDotPdd - p_dd_z) / r;

            return new double[] { ex, ey, ez, bx, by, bz };
        }
    }
}
