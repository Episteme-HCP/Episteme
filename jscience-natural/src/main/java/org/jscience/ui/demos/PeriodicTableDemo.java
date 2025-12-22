/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.chemistry.PeriodicTableViewer;

public class PeriodicTableDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Chemistry";
    }

    @Override
    public String getName() {
        return "Periodic Table";
    }

    @Override
    public String getDescription() {
        return "Interactive periodic table with all 118 elements, color-coded by category.";
    }

    @Override
    public void show(Stage stage) {
        PeriodicTableViewer.show(stage);
    }
}
