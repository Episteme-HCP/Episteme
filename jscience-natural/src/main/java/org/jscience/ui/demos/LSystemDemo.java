/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.biology.lsystems.LSystemViewer;

import org.jscience.ui.i18n.I18n;

public class LSystemDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.biology");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("LSystem.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("LSystem.desc");
    }

    @Override
    public void show(Stage stage) {
        LSystemViewer.show(stage);
    }
}
