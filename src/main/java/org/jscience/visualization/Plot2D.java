package org.jscience.visualization;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Utility for 2D data plotting.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Plot2D extends Application {

    private static String title = "Plot";
    private static List<Real> xData;
    private static List<Real> yData;

    /**
     * showing plot.
     * Note: This blocks until window is closed.
     * In a real application, this should be launched in a separate thread or part
     * of the main UI.
     */
    public static void show(String plotTitle, List<Real> x, List<Real> y) {
        title = plotTitle;
        xData = x;
        yData = y;
        launch(); // Launches JavaFX application
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle(title);

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X");
        yAxis.setLabel("Y");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Data");

        if (xData != null && yData != null && xData.size() == yData.size()) {
            for (int i = 0; i < xData.size(); i++) {
                series.getData().add(new XYChart.Data<>(xData.get(i).doubleValue(), yData.get(i).doubleValue()));
            }
        }

        lineChart.getData().add(series);

        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
