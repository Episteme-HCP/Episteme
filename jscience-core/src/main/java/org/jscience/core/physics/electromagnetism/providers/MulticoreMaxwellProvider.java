/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.physics.electromagnetism.providers;

import org.jscience.core.mathematics.geometry.Vector4D;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.physics.electromagnetism.MaxwellProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.physics.electromagnetism.MaxwellSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Multicore (Analytical) implementation of MaxwellProvider.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class MulticoreMaxwellProvider implements MaxwellProvider {

    private final List<MaxwellSource> sources = new ArrayList<>();

    public List<MaxwellSource> getSources() {
        return sources;
    }

    public MulticoreMaxwellProvider() {
        sources.add(new DipoleSource(new double[] { 0, 0, 0 }, new double[] { 0, 0, 1 }, 1.0, 1.0));
    }

    public void addSource(MaxwellSource source) {
        sources.add(source);
    }

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public double[][] computeTensor(Vector4D point) {
        double t = point.getCt();
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();

        double[] totalE = new double[3];
        double[] totalB = new double[3];

        sources.parallelStream().forEach(source -> {
            double[] EB = source.computeFields(t, x, y, z);
            synchronized (totalE) {
                totalE[0] += EB[0]; totalE[1] += EB[1]; totalE[2] += EB[2];
                totalB[0] += EB[3]; totalB[1] += EB[4]; totalB[2] += EB[5];
            }
        });

        double[][] f = new double[4][4];
        f[0][1] = -totalE[0]; f[0][2] = -totalE[1]; f[0][3] = -totalE[2];
        f[1][0] = totalE[0]; f[2][0] = totalE[1]; f[3][0] = totalE[2];
        f[1][2] = totalB[2]; f[2][1] = -totalB[2];
        f[1][3] = -totalB[1]; f[3][1] = totalB[1];
        f[2][3] = totalB[0]; f[3][2] = -totalB[0];

        return f;
    }

    @Override
    public Real[][] computeTensorReal(Vector4D point) {
        // ... (preserving content)
        Real t = point.ct();
        Real x = point.x();
        Real y = point.y();
        Real z = point.z();

        Real[] totalE = new Real[] { Real.ZERO, Real.ZERO, Real.ZERO };
        Real[] totalB = new Real[] { Real.ZERO, Real.ZERO, Real.ZERO };

        sources.parallelStream().forEach(source -> {
            Real[] EB = source.computeFieldsReal(t, x, y, z);
            synchronized (totalE) {
                totalE[0] = totalE[0].add(EB[0]);
                totalE[1] = totalE[1].add(EB[1]);
                totalE[2] = totalE[2].add(EB[2]);
                totalB[0] = totalB[0].add(EB[3]);
                totalB[1] = totalB[1].add(EB[4]);
                totalB[2] = totalB[2].add(EB[5]);
            }
        });

        Real[][] f = new Real[4][4];
        for(int i=0; i<4; i++) for(int j=0; j<4; j++) f[i][j] = Real.ZERO;

        f[0][1] = totalE[0].negate(); f[0][2] = totalE[1].negate(); f[0][3] = totalE[2].negate();
        f[1][0] = totalE[0];          f[2][0] = totalE[1];          f[3][0] = totalE[2];
        f[1][2] = totalB[2];          f[2][1] = totalB[2].negate();
        f[1][3] = totalB[1].negate(); f[3][1] = totalB[1];
        f[2][3] = totalB[0];          f[3][2] = totalB[0].negate();

        return f;
    }

    @Override
    public String getName() {
        return "Multicore Maxwell Analytical (CPU)";
    }

    public static class DipoleSource implements MaxwellSource {
        private final double[] pos;
        private final double[] p0;
        private final double omega;
        private final double phase;

        public DipoleSource(double[] pos, double[] p0, double omega, double phase) {
            this.pos = pos;
            this.p0 = p0;
            this.omega = omega;
            this.phase = phase;
        }

        @Override
        public double[] getPosition() { return pos; }

        @Override
        public double[] computeFields(double t, double x, double y, double z) {
            double rx = x - pos[0], ry = y - pos[1], rz = z - pos[2];
            double r = Math.sqrt(rx * rx + ry * ry + rz * rz);
            if (r < 1e-9) return new double[6];
            double tRet = t - r;
            double arg = omega * tRet + phase;
            double sinArg = Math.sin(arg);
            double p_dd_x = -p0[0] * omega * omega * sinArg;
            double p_dd_y = -p0[1] * omega * omega * sinArg;
            double p_dd_z = -p0[2] * omega * omega * sinArg;
            double nx = rx / r, ny = ry / r, nz = rz / r;
            double bx = -(ny * p_dd_z - nz * p_dd_y) / r;
            double by = -(nz * p_dd_x - nx * p_dd_z) / r;
            double bz = -(nx * p_dd_y - ny * p_dd_x) / r;
            double nDotPdd = nx * p_dd_x + ny * p_dd_y + nz * p_dd_z;
            double ex = (nx * nDotPdd - p_dd_x) / r;
            double ey = (ny * nDotPdd - p_dd_y) / r;
            double ez = (nz * nDotPdd - p_dd_z) / r;
            return new double[] { ex, ey, ez, bx, by, bz };
        }

        @Override
        public Real[] computeFieldsReal(Real t, Real x, Real y, Real z) {
            Real rx = x.subtract(Real.of(pos[0])), ry = y.subtract(Real.of(pos[1])), rz = z.subtract(Real.of(pos[2]));
            Real r = rx.multiply(rx).add(ry.multiply(ry)).add(rz.multiply(rz)).sqrt();
            if (r.compareTo(Real.of(1e-9)) < 0) return new Real[] {Real.ZERO, Real.ZERO, Real.ZERO, Real.ZERO, Real.ZERO, Real.ZERO};
            
            Real tRet = t.subtract(r);
            // Real doesn't have sin yet? Let's check Real.java
            // Actually Real might have sin() if it's based on some BigDecimal math.
            // If not, we might need a workaround or just doubleValue() for trig.
            
            double d_tRet = tRet.doubleValue();
            double arg = omega * d_tRet + phase;
            double sinArg = Math.sin(arg);
            
            Real p_dd_x = Real.of(-p0[0] * omega * omega * sinArg);
            Real p_dd_y = Real.of(-p0[1] * omega * omega * sinArg);
            Real p_dd_z = Real.of(-p0[2] * omega * omega * sinArg);
            
            Real nx = rx.divide(r), ny = ry.divide(r), nz = rz.divide(r);
            
            Real bx = ny.multiply(p_dd_z).subtract(nz.multiply(p_dd_y)).negate().divide(r);
            Real by = nz.multiply(p_dd_x).subtract(nx.multiply(p_dd_z)).negate().divide(r);
            Real bz = nx.multiply(p_dd_y).subtract(ny.multiply(p_dd_x)).negate().divide(r);
            
            Real nDotPdd = nx.multiply(p_dd_x).add(ny.multiply(p_dd_y)).add(nz.multiply(p_dd_z));
            Real ex = nx.multiply(nDotPdd).subtract(p_dd_x).divide(r);
            Real ey = ny.multiply(nDotPdd).subtract(p_dd_y).divide(r);
            Real ez = nz.multiply(p_dd_x).subtract(p_dd_z).divide(r); // Wait, ez formula was nz*nDotPdd - p_dd_z
            ez = nz.multiply(nDotPdd).subtract(p_dd_z).divide(r);

            return new Real[] { ex, ey, ez, bx, by, bz };
        }
    }
}
