/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.physics.waves.SpectrographViewer;

import org.jscience.ui.i18n.I18n;

public class SpectrographDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.physics");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("spectrograph.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("spectrograph.window");
    }

    @Override
    public void show(Stage stage) {
        SpectrographViewer.show(stage);
    }
}
