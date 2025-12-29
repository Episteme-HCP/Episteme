/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.biology.anatomy.BioMotionViewer;

public class BioMotionDemoProvider implements DemoProvider {
    @Override
    public String getName() {
        return "BioMotion Simulation";
    }

    @Override
    public String getDescription() {
        return "Physics-based biological motion simulation (Skeleton/Walker).";
    }

    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public void show(Stage stage) {
        BioMotionViewer.show(stage);
    }
}
