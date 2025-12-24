/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.FormulaNotationViewer;

public class FormulaNotationDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return "Scientific Formula Display";
    }

    @Override
    public String getDescription() {
        return "Visualizes complex mathematical notation and physical laws using custom rendering techniques.";
    }

    @Override
    public void show(Stage stage) {
        FormulaNotationViewer.show(stage);
    }
}
