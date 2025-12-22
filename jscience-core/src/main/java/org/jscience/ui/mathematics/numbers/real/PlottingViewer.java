/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.mathematics.numbers.real;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.plotting.Plot2D;
import org.jscience.ui.plotting.PlotFactory;

import java.util.ArrayList;
import java.util.List;

public class PlottingViewer extends Application {

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
        plot.show();
    }

    public static void show(Stage stage) {
        new PlottingViewer().start(stage);
    }
}
