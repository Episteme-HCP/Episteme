/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.mathematics.analysis.fractals.providers;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.analysis.fractals.MandelbrotProvider;
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
                while (x.multiply(x).add(y.multiply(y)).compareTo(FOUR) <= 0 && iter < maxIterations) {
                    Real xTemp = x.multiply(x).subtract(y.multiply(y)).add(x0);
                    y = x.multiply(y).multiply(TWO).add(y0);
                    x = xTemp;
                    iter++;
                }
                result[px][py] = iter;
            }
        });

        return result;
    }

    @Override
    public String getName() {
        return "Multicore Mandelbrot (CPU)";
    }
}
