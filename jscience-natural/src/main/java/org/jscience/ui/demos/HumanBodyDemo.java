/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.biology.anatomy.HumanBodyViewer;

/**
 * Demo provider for the Human Body Anatomy Viewer.
 */
public class HumanBodyDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getName() {
        return "Human Body Anatomy";
    }

    @Override
    public String getDescription() {
        return "Interactive 3D viewer of human anatomy with layers for skeleton, muscles, organs, nervous and circulatory systems.";
    }

    @Override
    public void show(Stage stage) {
        HumanBodyViewer.show(stage);
    }
}
