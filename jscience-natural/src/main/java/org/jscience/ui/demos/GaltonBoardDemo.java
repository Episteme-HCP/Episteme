/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.statistics.GaltonBoardViewer;

public class GaltonBoardDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return "Galton Board";
    }

    @Override
    public String getDescription() {
        return "Statistical demonstration of the normal distribution via ball simulation.";
    }

    @Override
    public void show(Stage stage) {
        GaltonBoardViewer.show(stage);
    }
}
