package org.jscience.ui.viewers.sports;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.i18n.I18n;
import org.jscience.sports.TrajectoryDataSet;

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
        chart.setTitle(I18n.getInstance().get("viewer.trajectory.title", "Evolution Trajectory"));
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

    @Override public String getCategory() { return I18n.getInstance().get("category.shared", "General Purpose"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.trajectory.name", "Evolution Trajectory Viewer"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.trajectory.desc", "Tracks human performance and biological evolution cycles."); }
    @Override public String getLongDescription() { 
        return I18n.getInstance().get("viewer.trajectory.longdesc", "Advanced monitoring tool for health and sports science. Supports comparative analysis of physiological cycles and performance forecasting."); 
    }
}
