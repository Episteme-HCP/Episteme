/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.statistics.DistributionsViewer;

public class DistributionsDemo implements DemoProvider {
    @Override
    public String getName() {
        return "Statistical Distributions";
    }

    @Override
    public String getDescription() {
        return "Visualizer for Normal, Poisson, and Binomial distributions.";
    }

    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public void show(Stage stage) {
        DistributionsViewer.show(stage);
    }
}
