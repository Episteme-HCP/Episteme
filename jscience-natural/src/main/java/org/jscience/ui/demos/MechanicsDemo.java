/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.physics.mechanics.MechanicsViewer;

import org.jscience.ui.i18n.I18n;

public class MechanicsDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.physics");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("mechanics.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.mechanics");
    }

    @Override
    public void show(Stage stage) {
        MechanicsViewer.show(stage);
    }
}
