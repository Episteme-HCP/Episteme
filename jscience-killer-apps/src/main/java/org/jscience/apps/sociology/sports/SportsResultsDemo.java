/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.sociology.sports;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;

/**
 * Demo Provider for Sports Results Management.
 *
 * @author Silvere Martin-Michiellot
 */
public class SportsResultsDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return "Sociology";
    }

    @Override
    public String getName() {
        return "Sports League Manager";
    }

    @Override
    public String getDescription() {
        return "Manage match results and view automatically calculated league standings.";
    }

    @Override
    public void show(Stage stage) {
        SportsResultsViewer.show(stage);
    }
}
