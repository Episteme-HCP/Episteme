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

package org.jscience.social.ui.viewers.sports;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.jscience.core.ui.AbstractViewer;
import org.jscience.core.ui.i18n.I18N;
import org.jscience.social.sports.TrajectoryDataSet;

/**
 * High-precision performance tracking viewer for human evolution trajectories.
 * Visualizes cycles (Sleep/Vigilance) and performance trends.
 */
public final class EvolutionTrajectoryViewer extends AbstractViewer {

    private final LineChart<Number, Number> chart;

    public EvolutionTrajectoryViewer() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time / Cycle");
        yAxis.setLabel("Intensity / Performance");

        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(I18N.getInstance().get("viewer.trajectory.title", "Evolution Trajectory"));
        chart.setCreateSymbols(false);
        
        setCenter(chart);
        getStyleClass().add("evolution-trajectory-viewer");
    }

    public void setData(TrajectoryDataSet data) {
        chart.getData().clear();
        for (var series : data.getSeries()) {
            XYChart.Series<Number, Number> s = new XYChart.Series<>();
            s.setName(series.label());
            for (var p : series.points()) {
                s.getData().add(new XYChart.Data<>(p.time(), p.value().doubleValue()));
            }
            chart.getData().add(s);
        }
    }

    @Override public String getCategory() { return I18N.getInstance().get("category.shared", "General Purpose"); }
    @Override public String getName() { return I18N.getInstance().get("viewer.trajectory.name", "Evolution Trajectory Viewer"); }
    @Override public String getDescription() { return I18N.getInstance().get("viewer.trajectory.desc", "Tracks human performance and biological evolution cycles."); }
    @Override public String getLongDescription() { 
        return I18N.getInstance().get("viewer.trajectory.longdesc", "Advanced monitoring tool for health and sports science. Supports comparative analysis of physiological cycles and performance forecasting."); 
    }
}

