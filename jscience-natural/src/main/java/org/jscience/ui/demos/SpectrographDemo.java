/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.physics.waves.SpectrographViewer;

public class SpectrographDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getName() {
        return org.jscience.natural.i18n.I18n.getInstance().get("Spectrograph.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.natural.i18n.I18n.getInstance().get("Spectrograph.desc");
    }

    @Override
    public void show(Stage stage) {
        SpectrographViewer.show(stage);
    }
}
