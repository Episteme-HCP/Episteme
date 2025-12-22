/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.biology.lsystems.LSystemViewer;

public class LSystemDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getName() {
        return "L-Systems Viewer";
    }

    @Override
    public String getDescription() {
        return "Lindenmayer systems for generating fractal plants and biological structures.";
    }

    @Override
    public void show(Stage stage) {
        LSystemViewer.show(stage);
    }
}
