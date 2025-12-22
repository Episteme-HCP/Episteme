/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.computing.GameOfLifeViewer;

public class GameOfLifeDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Computing";
    }

    @Override
    public String getName() {
        return "Game of Life";
    }

    @Override
    public String getDescription() {
        return "Conway's cellular automaton with pattern presets and zoom controls.";
    }

    @Override
    public void show(Stage stage) {
        GameOfLifeViewer.show(stage);
    }
}
