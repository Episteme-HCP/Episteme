/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.biology.medical.VitalMonitorViewer;

public class VitalMonitorDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getName() {
        return "Vital Constants Monitor";
    }

    @Override
    public String getDescription() {
        return "Medical monitor mockup showing real-time ECG and SpO2 waveforms with heart rate tracking.";
    }

    @Override
    public void show(Stage stage) {
        VitalMonitorViewer.show(stage);
    }
}
