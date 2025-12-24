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
        return "Frequency Spectrograph";
    }

    @Override
    public String getDescription() {
        return "Real-time spectrum analyzer visualizing frequency distribution and harmonic signals.";
    }

    @Override
    public void show(Stage stage) {
        SpectrographViewer.show(stage);
    }
}
