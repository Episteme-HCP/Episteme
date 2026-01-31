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

package org.jscience.natural.ui.viewers.shared.scientific;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import org.jscience.core.ui.AbstractViewer;
import org.jscience.core.ui.i18n.I18N;
import org.jscience.core.mathematics.numbers.real.Real;

import java.util.*;

/**
 * Universal viewer for natural scientific processes.
 * Visualizes multiple time-series trajectories (Temperature, Energy, Concentration).
 */
public final class ScientificProcessViewer extends AbstractViewer {

    private final VBox chartContainer = new VBox(10);
    private final Map<String, LineChart<Number, Number>> charts = new LinkedHashMap<>();
    private final Map<String, XYChart.Series<Number, Number>> seriesMap = new HashMap<>();

    public ScientificProcessViewer() {
        setCenter(chartContainer);
        getStyleClass().add("scientific-process-viewer");
    }

    public void addTrace(String group, String name, double time, Real value) {
        LineChart<Number, Number> chart = charts.computeIfAbsent(group, this::createChart);
        XYChart.Series<Number, Number> series = seriesMap.computeIfAbsent(group + ":" + name, k -> {
            XYChart.Series<Number, Number> s = new XYChart.Series<>();
            s.setName(name);
            chart.getData().add(s);
            return s;
        });
        
        series.getData().add(new XYChart.Data<>(time, value.doubleValue()));
        if (series.getData().size() > 200) { // Sliding window
            series.getData().remove(0);
        }
    }

    private LineChart<Number, Number> createChart(String title) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time / Step");
        
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setCreateSymbols(false);
        chart.setAnimated(false);
        chartContainer.getChildren().add(chart);
        return chart;
    }

    public void clear() {
        chartContainer.getChildren().clear();
        charts.clear();
        seriesMap.clear();
    }

    @Override public String getCategory() { return I18N.getInstance().get("category.natural", "Natural Sciences"); }
    @Override public String getName() { return I18N.getInstance().get("viewer.process.name", "Scientific Process Viewer"); }
    @Override public String getDescription() { return I18N.getInstance().get("viewer.process.desc", "Dynamic monitoring of scientific simulations."); }
    @Override public String getLongDescription() { 
        return I18N.getInstance().get("viewer.process.longdesc", "Multi-trace dashboard for tracking metrics in real-time simulations such as climate modeling, molecular dynamics, or chemical kinetics."); 
    }
}

