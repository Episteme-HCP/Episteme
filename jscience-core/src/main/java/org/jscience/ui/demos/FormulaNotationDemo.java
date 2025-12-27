/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.FormulaNotationViewer;

import org.jscience.ui.i18n.I18n;

public class FormulaNotationDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.mathematics");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("viewer.formula");
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
