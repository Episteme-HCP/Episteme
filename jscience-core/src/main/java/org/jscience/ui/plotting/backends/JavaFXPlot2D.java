package org.jscience.ui.plotting.backends;

import org.jscience.ui.plotting.Plot2D;
import org.jscience.ui.plotting.PlotFormat;
import org.jscience.ui.plotting.PlottingBackend;
import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.numbers.real.Real;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.util.List;

/**
 * JavaFX-based 2D plotting implementation.
 * <p>
 * Always available (built into Java 8+).
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class JavaFXPlot2D implements Plot2D {

    private final String title;
    private final NumberAxis xAxis;
    private final NumberAxis yAxis;
    private final LineChart<Number, Number> chart;
    @SuppressWarnings("unused") // State maintained by chart, field kept for potential future use
    private boolean gridEnabled = true;
    @SuppressWarnings("unused") // State maintained by chart, field kept for potential future use
    private boolean legendEnabled = true;

    public JavaFXPlot2D(String title) {
        this.title = title;
        this.xAxis = new NumberAxis();
        this.yAxis = new NumberAxis();
        this.chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setCreateSymbols(false); // No markers by default
    }

    @Override
    public Plot2D addFunction(Function<Real, Real> function, Real xMin, Real xMax, String label) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(label);

        // Sample 500 points
        int numPoints = 500;
        Real dx = xMax.subtract(xMin).divide(Real.of(numPoints - 1));

        for (int i = 0; i < numPoints; i++) {
            Real x = xMin.add(dx.multiply(Real.of(i)));
            Real y = function.evaluate(x);
            series.getData().add(new XYChart.Data<>(x.doubleValue(), y.doubleValue()));
        }

        chart.getData().add(series);
        return this;
    }

    @Override
    public Plot2D addData(List<Real> xData, List<Real> yData, String label) {
        if (xData.size() != yData.size()) {
            throw new IllegalArgumentException("xData and yData must have same size");
        }

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(label);

        for (int i = 0; i < xData.size(); i++) {
            series.getData().add(new XYChart.Data<>(
                    xData.get(i).doubleValue(),
                    yData.get(i).doubleValue()));
        }

        chart.getData().add(series);
        return this;
    }

    @Override
    public Plot2D setTitle(String title) {
        chart.setTitle(title);
        return this;
    }

    @Override
    public Plot2D setAxisLabels(String xLabel, String yLabel) {
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
        return this;
    }

    @Override
    public Plot2D setGrid(boolean enabled) {
        this.gridEnabled = enabled;
        xAxis.setTickMarkVisible(enabled);
        yAxis.setTickMarkVisible(enabled);
        return this;
    }

    @Override
    public Plot2D setLegend(boolean enabled) {
        this.legendEnabled = enabled;
        chart.setLegendVisible(enabled);
        return this;
    }

    @Override
    public Plot2D setXRange(Real min, Real max) {
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(min.doubleValue());
        xAxis.setUpperBound(max.doubleValue());
        return this;
    }

    @Override
    public Plot2D setYRange(Real min, Real max) {
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(min.doubleValue());
        yAxis.setUpperBound(max.doubleValue());
        return this;
    }

    @Override
    public void show() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(chart, 800, 600));
            stage.show();
        });

        // Initialize JavaFX if not already
        try {
            Platform.startup(() -> {
            });
        } catch (IllegalStateException e) {
            // Already started
        }
    }

    @Override
    public void save(String filename, PlotFormat format) {
        System.err.println(
                "WARNING: Snapshot export not implemented (requires javafx.swing module). File not saved: " + filename);
    }

    @Override
    public PlottingBackend getBackend() {
        return PlottingBackend.JAVAFX;
    }

    @Override
    public Plot2D addSeries(List<Real> xData, List<Real> yData, String label, SeriesStyle style) {
        // Basic implementation - ignore styling for now
        return addData(xData, yData, label);
    }
}
