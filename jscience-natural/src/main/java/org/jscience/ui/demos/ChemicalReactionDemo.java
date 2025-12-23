/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.chemistry.ChemicalReactionViewer;

/**
 * Demo provider for the Chemical Reaction Parser.
 */
public class ChemicalReactionDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return "Chemistry";
    }

    @Override
    public String getName() {
        return "Chemical Reaction Parser";
    }

    @Override
    public String getDescription() {
        return "Parse and analyze chemical equations, check if reactions are balanced, and view element counts.";
    }

    @Override
    public void show(Stage stage) {
        ChemicalReactionViewer.show(stage);
    }
}
