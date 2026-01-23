package org.jscience.ui.viewers.shared.scientific;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.i18n.I18n;
import org.jscience.mathematics.numbers.real.Real;

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

    @Override public String getCategory() { return I18n.getInstance().get("category.natural", "Natural Sciences"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.process.name", "Scientific Process Viewer"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.process.desc", "Dynamic monitoring of scientific simulations."); }
    @Override public String getLongDescription() { 
        return I18n.getInstance().get("viewer.process.longdesc", "Multi-trace dashboard for tracking metrics in real-time simulations such as climate modeling, molecular dynamics, or chemical kinetics."); 
    }
}
