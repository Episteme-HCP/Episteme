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

package org.jscience.ui.viewers.mathematics.analysis.plotting.backends;

import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.viewers.mathematics.analysis.plotting.Plot3D;
import org.jscience.ui.viewers.mathematics.analysis.plotting.PlotFormat;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.io.File;
import java.util.List;

/**
 * Jzy3d implementation for 3D plotting.
 * Compatible with Jzy3d 1.0.0.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Jzy3dPlot3D implements Plot3D {

    private final Chart chart;

    public Jzy3dPlot3D(String title) {
        // Jzy3d 1.0.0 compatible factory
        this.chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
    }

    @Override
    public Plot3D addSurface(Function<Real[], Real> function, Real xMin, Real xMax, Real yMin, Real yMax,
            String label) {
        // Build surface points
        int steps = 50;
        double xRange = xMax.doubleValue() - xMin.doubleValue();
        double yRange = yMax.doubleValue() - yMin.doubleValue();

        Coord3d[] points = new Coord3d[steps * steps];
        for (int i = 0; i < steps; i++) {
            double x = xMin.doubleValue() + (xRange * i / (steps - 1));
            for (int j = 0; j < steps; j++) {
                double y = yMin.doubleValue() + (yRange * j / (steps - 1));
                Real z = function.evaluate(new Real[] { Real.of(x), Real.of(y) });
                points[i * steps + j] = new Coord3d((float) x, (float) y, (float) z.doubleValue());
            }
        }

        // Simple scatter representation for now
        Scatter scatter = new Scatter(points, Color.BLUE);
        chart.getScene().add(scatter);
        return this;
    }

    @Override
    public Plot3D addScatter(List<Real> xData, List<Real> yData, List<Real> zData, String label) {
        int size = Math.min(xData.size(), Math.min(yData.size(), zData.size()));
        Coord3d[] points = new Coord3d[size];
        for (int i = 0; i < size; i++) {
            points[i] = new Coord3d(
                    (float) xData.get(i).doubleValue(),
                    (float) yData.get(i).doubleValue(),
                    (float) zData.get(i).doubleValue());
        }
        chart.getScene().add(new Scatter(points, Color.RED));
        return this;
    }

    @Override
    public Plot3D addParametricCurve(Function<Real, Real> xFunc, Function<Real, Real> yFunc, Function<Real, Real> zFunc,
            Real tMin, Real tMax, String label) {
        return this;
    }

    @Override
    public Plot3D setTitle(String title) {
        return this; // Jzy3d 1.0 doesn't have easy title setter on Chart
    }

    @Override
    public Plot3D setAxisLabels(String xLabel, String yLabel, String zLabel) {
        chart.getAxeLayout().setXAxeLabel(xLabel);
        chart.getAxeLayout().setYAxeLabel(yLabel);
        chart.getAxeLayout().setZAxeLabel(zLabel);
        return this;
    }

    @Override
    public Plot3D setViewAngle(double azimuth, double elevation) {
        chart.getView().rotate(new Coord2d((float) azimuth, (float) elevation));
        return this;
    }

    @Override
    public Plot3D setInteractive(boolean enabled) {
        return this;
    }

    @Override
    public Plot3D setColormap(Colormap colormap) {
        return this;
    }

    @Override
    public void show() {
        // Jzy3d 1.0.0 launcher logic
        try {
            org.jzy3d.analysis.AnalysisLauncher.open(new org.jzy3d.analysis.AbstractAnalysis() {
                @Override
                public void init() {
                    this.chart = Jzy3dPlot3D.this.chart;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(String filename, PlotFormat format) {
        try {
            chart.screenshot(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
