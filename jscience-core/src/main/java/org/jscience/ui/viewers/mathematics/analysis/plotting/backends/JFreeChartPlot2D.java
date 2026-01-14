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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.viewers.mathematics.analysis.plotting.Plot2D;
import org.jscience.ui.viewers.mathematics.analysis.plotting.PlotFormat;
import org.jscience.ui.viewers.mathematics.analysis.plotting.PlottingBackend;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * JFreeChart-based 2D plotting implementation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JFreeChartPlot2D implements Plot2D {

    private final String title;
    private final XYSeriesCollection dataset;
    private final JFreeChart chart;
    private String xLabel = "X";
    private String yLabel = "Y";

    public JFreeChartPlot2D(String title) {
        this.title = title;
        this.dataset = new XYSeriesCollection();
        this.chart = ChartFactory.createXYLineChart(
                title,
                xLabel,
                yLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
    }

    @Override
    public Plot2D addFunction(Function<Real, Real> function, Real xMin, Real xMax, String label) {
        XYSeries series = new XYSeries(label);

        // Sample 500 points
        int numPoints = 500;
        Real dx = xMax.subtract(xMin).divide(Real.of(numPoints - 1));

        for (int i = 0; i < numPoints; i++) {
            Real x = xMin.add(dx.multiply(Real.of(i)));
            Real y = function.evaluate(x);
            series.add(x.doubleValue(), y.doubleValue());
        }

        dataset.addSeries(series);
        return this;
    }

    @Override
    public Plot2D addData(List<Real> xData, List<Real> yData, String label) {
        if (xData.size() != yData.size()) {
            throw new IllegalArgumentException("xData and yData must have same size");
        }

        XYSeries series = new XYSeries(label);
        for (int i = 0; i < xData.size(); i++) {
            series.add(xData.get(i).doubleValue(), yData.get(i).doubleValue());
        }
        dataset.addSeries(series);
        return this;
    }

    @Override
    public Plot2D addSeries(List<Real> xData, List<Real> yData, String label, SeriesStyle style) {
        // Basic mapping of style could be added here
        return addData(xData, yData, label);
    }

    @Override
    public Plot2D setTitle(String title) {
        chart.setTitle(title);
        return this;
    }

    @Override
    public Plot2D setAxisLabels(String xLabel, String yLabel) {
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        chart.getXYPlot().getDomainAxis().setLabel(xLabel);
        chart.getXYPlot().getRangeAxis().setLabel(yLabel);
        return this;
    }

    @Override
    public Plot2D setGrid(boolean enabled) {
        chart.getXYPlot().setDomainGridlinesVisible(enabled);
        chart.getXYPlot().setRangeGridlinesVisible(enabled);
        return this;
    }

    @Override
    public Plot2D setLegend(boolean enabled) {
        // JFreeChart legend visibility control
        if (chart.getLegend() != null) {
            chart.getLegend().setVisible(enabled);
        }
        return this;
    }

    @Override
    public Plot2D setXRange(Real min, Real max) {
        chart.getXYPlot().getDomainAxis().setRange(min.doubleValue(), max.doubleValue());
        return this;
    }

    @Override
    public Plot2D setYRange(Real min, Real max) {
        chart.getXYPlot().getRangeAxis().setRange(min.doubleValue(), max.doubleValue());
        return this;
    }

    @Override
    public void show() {
        ChartFrame frame = new ChartFrame(title, chart);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void save(String filename, PlotFormat format) {
        try {
            File outputFile = new File(filename);
            if (filename.toLowerCase().endsWith(".png")) {
                ChartUtils.saveChartAsPNG(outputFile, chart, 800, 600);
            } else if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
                ChartUtils.saveChartAsJPEG(outputFile, chart, 800, 600);
            } else {
                System.err.println("Unsupported format for JFreeChart save: " + filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PlottingBackend getBackend() {
        return PlottingBackend.JFREECHART;
    }
}
