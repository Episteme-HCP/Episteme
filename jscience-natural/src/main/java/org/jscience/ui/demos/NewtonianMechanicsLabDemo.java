/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.physics.mechanics.NewtonianMechanicsLabViewer;
import org.jscience.ui.i18n.I18n;

/**
 * Demo provider for the Newtonian Mechanics Lab.
 */
public class NewtonianMechanicsLabDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.physics");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("mechanics.lab.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("mechanics.lab.desc");
    }

    @Override
    public void show(Stage stage) {
        NewtonianMechanicsLabViewer.show(stage);
    }
}
