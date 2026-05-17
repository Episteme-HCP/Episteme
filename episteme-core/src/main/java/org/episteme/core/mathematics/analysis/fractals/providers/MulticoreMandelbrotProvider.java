/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.mathematics.analysis.fractals.providers;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.analysis.fractals.MandelbrotProvider;
import com.google.auto.service.AutoService;
import java.util.stream.IntStream;

/**
 * Multicore implementation of MandelbrotProvider.
 * Uses parallel streams to distribute row computation across CPU cores.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({MandelbrotProvider.class})
public class MulticoreMandelbrotProvider implements MandelbrotProvider {

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public int[][] compute(float xMin, float xMax, float yMin, float yMax, int width, int height,
            int maxIterations) {
        int[][] result = new int[width][height];
        float dx = (xMax - xMin) / width;
        float dy = (yMax - yMin) / height;

        IntStream.range(0, width).parallel().forEach(px -> {
            float x0 = xMin + px * dx;
            for (int py = 0; py < height; py++) {
                float y0 = yMin + py * dy;
                float x = 0;
                float y = 0;
                int iter = 0;
                float x2 = 0;
                float y2 = 0;
                while (x2 + y2 <= 4.0f && iter < maxIterations) {
                    y = 2 * x * y + y0;
                    x = x2 - y2 + x0;
                    x2 = x * x;
                    y2 = y * y;
                    iter++;
                }
                result[px][py] = iter;
            }
        });

        return result;
    }

    @Override
    public int[][] compute(double xMin, double xMax, double yMin, double yMax, int width, int height,
            int maxIterations) {
        int[][] result = new int[width][height];
        double dx = (xMax - xMin) / width;
        double dy = (yMax - yMin) / height;

        IntStream.range(0, width).parallel().forEach(px -> {
            double x0 = xMin + px * dx;
            for (int py = 0; py < height; py++) {
                double y0 = yMin + py * dy;
                double x = 0;
                double y = 0;
                int iter = 0;
                double x2 = 0;
                double y2 = 0;
                while (x2 + y2 <= 4.0 && iter < maxIterations) {
                    y = 2 * x * y + y0;
                    x = x2 - y2 + x0;
                    x2 = x * x;
                    y2 = y * y;
                    iter++;
                }
                result[px][py] = iter;
            }
        });

        return result;
    }

    @Override
    public int[][] compute(Real xMin, Real xMax, Real yMin, Real yMax, int width, int height, int maxIterations) {
        int[][] result = new int[width][height];
        final Real FOUR = Real.of(4.0);
        final Real TWO = Real.of(2.0);

        Real dx = xMax.subtract(xMin).divide(Real.of(width));
        Real dy = yMax.subtract(yMin).divide(Real.of(height));

        IntStream.range(0, width).parallel().forEach(px -> {
            Real x0 = xMin.add(dx.multiply(Real.of(px)));
            for (int py = 0; py < height; py++) {
                Real y0 = yMin.add(dy.multiply(Real.of(py)));
                Real x = Real.ZERO;
                Real y = Real.ZERO;
                int iter = 0;
                Real x2 = Real.ZERO;
                Real y2 = Real.ZERO;
                while (x2.add(y2).compareTo(FOUR) <= 0 && iter < maxIterations) {
                    y = x.multiply(y).multiply(TWO).add(y0);
                    x = x2.subtract(y2).add(x0);
                    x2 = x.multiply(x);
                    y2 = y.multiply(y);
                    iter++;
                }
                result[px][py] = iter;
            }
        });

        return result;
    }

    @Override
    public float[][] computeSmooth(float xMin, float xMax, float yMin, float yMax, int width, int height, int maxIterations) {
        float[][] result = new float[width][height];
        float dx = (xMax - xMin) / width;
        float dy = (yMax - yMin) / height;

        IntStream.range(0, width).parallel().forEach(px -> {
            float x0 = xMin + px * dx;
            for (int py = 0; py < height; py++) {
                float y0 = yMin + py * dy;
                float x = 0;
                float y = 0;
                int iter = 0;
                float x2 = 0;
                float y2 = 0;
                while (x2 + y2 <= 1024.0f && iter < maxIterations) {
                    y = 2 * x * y + y0;
                    x = x2 - y2 + x0;
                    x2 = x * x;
                    y2 = y * y;
                    iter++;
                }
                if (iter < maxIterations) {
                    float log_zn = (float) Math.log(x2 + y2) / 2.0f;
                    float nu = (float) (Math.log(log_zn / Math.log(2.0f)) / Math.log(2.0f));
                    result[px][py] = iter + 1.0f - nu;
                } else {
                    result[px][py] = maxIterations;
                }
            }
        });
        return result;
    }

    @Override
    public double[][] computeSmooth(double xMin, double xMax, double yMin, double yMax, int width, int height, int maxIterations) {
        double[][] result = new double[width][height];
        double dx = (xMax - xMin) / width;
        double dy = (yMax - yMin) / height;

        IntStream.range(0, width).parallel().forEach(px -> {
            double x0 = xMin + px * dx;
            for (int py = 0; py < height; py++) {
                double y0 = yMin + py * dy;
                double x = 0;
                double y = 0;
                int iter = 0;
                double x2 = 0;
                double y2 = 0;
                while (x2 + y2 <= 1024.0 && iter < maxIterations) {
                    y = 2 * x * y + y0;
                    x = x2 - y2 + x0;
                    x2 = x * x;
                    y2 = y * y;
                    iter++;
                }
                if (iter < maxIterations) {
                    double log_zn = Math.log(x2 + y2) / 2.0;
                    double nu = Math.log(log_zn / Math.log(2.0)) / Math.log(2.0);
                    result[px][py] = iter + 1.0 - nu;
                } else {
                    result[px][py] = maxIterations;
                }
            }
        });
        return result;
    }

    @Override
    public Real[][] computeSmooth(Real xMin, Real xMax, Real yMin, Real yMax, int width, int height, int maxIterations) {
        Real[][] result = new Real[width][height];
        final Real ESCAPE = Real.of(1024.0);
        final Real TWO = Real.of(2.0);

        Real dx = xMax.subtract(xMin).divide(Real.of(width));
        Real dy = yMax.subtract(yMin).divide(Real.of(height));

        IntStream.range(0, width).parallel().forEach(px -> {
            Real x0 = xMin.add(dx.multiply(Real.of(px)));
            for (int py = 0; py < height; py++) {
                Real y0 = yMin.add(dy.multiply(Real.of(py)));
                Real x = Real.ZERO;
                Real y = Real.ZERO;
                int iter = 0;
                Real x2 = Real.ZERO;
                Real y2 = Real.ZERO;
                while (x2.add(y2).compareTo(ESCAPE) <= 0 && iter < maxIterations) {
                    Real xTemp = x2.subtract(y2).add(x0);
                    y = x.multiply(y).multiply(TWO).add(y0);
                    x = xTemp;
                    x2 = x.multiply(x);
                    y2 = y.multiply(y);
                    iter++;
                }
                if (iter < maxIterations) {
                    Real zn2 = x2.add(y2);
                    Real log_zn = zn2.log().divide(TWO);
                    Real ln2 = Real.of(2.0).log();
                    Real nu = log_zn.divide(ln2).log().divide(ln2);
                    result[px][py] = Real.of(iter + 1.0).subtract(nu);
                } else {
                    result[px][py] = Real.of(maxIterations);
                }
            }
        });
        return result;
    }

    @Override
    public String getName() {
        return "Multicore Mandelbrot (CPU)";
    }
}
