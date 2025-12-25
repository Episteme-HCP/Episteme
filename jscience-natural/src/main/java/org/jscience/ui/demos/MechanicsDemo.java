/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.physics.mechanics.MechanicsViewer;

public class MechanicsDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getName() {
        return org.jscience.natural.i18n.I18n.getInstance().get("Mechanics.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.natural.i18n.I18n.getInstance().get("Mechanics.desc");
    }

    @Override
    public void show(Stage stage) {
        MechanicsViewer.show(stage);
    }
}
