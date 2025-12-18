package org.jscience.ui.plotting;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jscience.mathematics.numbers.real.Real;

import java.util.ArrayList;
import java.util.List;

public class PlottingDemo extends Application {

    @Override
    public void start(Stage stage) {
        Plot2D plot = PlotFactory.create2D("Sine Wave Demo");

        List<Real> xData = new ArrayList<>();
        List<Real> yData = new ArrayList<>();

        for (double x = 0; x <= Math.PI * 4; x += 0.1) {
            xData.add(Real.of(x));
            yData.add(Real.of(Math.sin(x)));
        }

        plot.addData(xData, yData, "sin(x)");
        plot.setAxisLabels("Angle (rad)", "Value");
        plot.show(); // This spawns its own stage usually, but since we are in start(), it works with
                     // JavaFX initialized.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
