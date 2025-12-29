/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.biology.ecology.SpeciesBrowserViewer;
import org.jscience.ui.i18n.I18n;

public class SpeciesBrowserDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.biology");
    }

    @Override
    public String getName() {
        return "Species Browser";
    }

    @Override
    public String getDescription() {
        return "Explore biological species data using the Global Biodiversity Information Facility (GBIF) API.";
    }

    @Override
    public void show(Stage stage) {
        SpeciesBrowserViewer.show(stage);
    }
}
