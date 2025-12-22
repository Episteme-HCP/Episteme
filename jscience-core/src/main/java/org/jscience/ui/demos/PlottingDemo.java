/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.numbers.real.PlottingViewer;

public class PlottingDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return "Plotting Demo";
    }

    @Override
    public String getDescription() {
        return "2D Plotting of functions using JScience Real types.";
    }

    @Override
    public void show(Stage stage) {
        PlottingViewer.show(stage);
    }
}
