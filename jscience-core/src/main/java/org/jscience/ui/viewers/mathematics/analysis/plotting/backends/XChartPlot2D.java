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
import org.jscience.ui.viewers.mathematics.analysis.plotting.Plot2D;
import org.jscience.ui.viewers.mathematics.analysis.plotting.PlotFormat;
import org.jscience.ui.viewers.mathematics.analysis.plotting.PlottingBackend;
// import org.knowm.xchart.BitmapEncoder; // Disabled for build stability
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

import java.util.ArrayList;
import java.util.List;

/**
 * XChart-based 2D plotting implementation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class XChartPlot2D implements Plot2D {

    private final XYChart chart;

    public XChartPlot2D(String title) {
        this.chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title(title)
                .xAxisTitle("X")
                .yAxisTitle("Y")
                .theme(Styler.ChartTheme.Matlab)
                .build();
    }

    @Override
    public Plot2D addFunction(Function<Real, Real> function, Real xMin, Real xMax, String label) {
        List<Double> xData = new ArrayList<>();
        List<Double> yData = new ArrayList<>();

        // Sample 500 points
        int numPoints = 500;
        Real dx = xMax.subtract(xMin).divide(Real.of(numPoints - 1));

        for (int i = 0; i < numPoints; i++) {
            Real x = xMin.add(dx.multiply(Real.of(i)));
            Real y = function.evaluate(x);
            xData.add(x.doubleValue());
            yData.add(y.doubleValue());
        }

        chart.addSeries(label, xData, yData);
        return this;
    }

    @Override
    public Plot2D addData(List<Real> xData, List<Real> yData, String label) {
        if (xData.size() != yData.size()) {
            throw new IllegalArgumentException("xData and yData must have same size");
        }

        List<Double> x = new ArrayList<>(xData.size());
        List<Double> y = new ArrayList<>(yData.size());

        for (int i = 0; i < xData.size(); i++) {
            x.add(xData.get(i).doubleValue());
            y.add(yData.get(i).doubleValue());
        }

        chart.addSeries(label, x, y);
        return this;
    }

    @Override
    public Plot2D addSeries(List<Real> xData, List<Real> yData, String label, SeriesStyle style) {
        List<Double> x = new ArrayList<>(xData.size());
        List<Double> y = new ArrayList<>(yData.size());

        for (Real r : xData)
            x.add(r.doubleValue());
        for (Real r : yData)
            y.add(r.doubleValue());

        chart.addSeries(label, x, y);
        // Map rudimentary styles if necessary
        return this;
    }

    @Override
    public Plot2D setTitle(String title) {
        chart.setTitle(title);
        return this;
    }

    @Override
    public Plot2D setAxisLabels(String xLabel, String yLabel) {
        chart.setXAxisTitle(xLabel);
        chart.setYAxisTitle(yLabel);
        return this;
    }

    @Override
    public Plot2D setGrid(boolean enabled) {
        chart.getStyler().setChartTitleVisible(true); // Re-enforce defaults
        // XChart doesn't have a simple boolean for "grid", but assuming standard
        // visibility
        return this;
    }

    @Override
    public Plot2D setLegend(boolean enabled) {
        chart.getStyler().setLegendVisible(enabled);
        return this;
    }

    @Override
    public Plot2D setXRange(Real min, Real max) {
        chart.getStyler().setXAxisMin(min.doubleValue());
        chart.getStyler().setXAxisMax(max.doubleValue());
        return this;
    }

    @Override
    public Plot2D setYRange(Real min, Real max) {
        chart.getStyler().setYAxisMin(min.doubleValue());
        chart.getStyler().setYAxisMax(max.doubleValue());
        return this;
    }

    @Override
    public void show() {
        new SwingWrapper<>(chart).displayChart();
    }

    @Override
    public void save(String filename, PlotFormat format) {
        System.err.println("WARNING: XChart export not fully implemented yet. File not saved: " + filename);
    }

    @Override
    public PlottingBackend getBackend() {
        return PlottingBackend.XCHART;
    }
}
