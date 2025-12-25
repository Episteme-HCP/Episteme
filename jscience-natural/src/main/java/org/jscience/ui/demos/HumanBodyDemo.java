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
        return org.jscience.natural.i18n.I18n.getInstance().get("HumanBody.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.natural.i18n.I18n.getInstance().get("HumanBody.desc");
    }

    @Override
    public void show(Stage stage) {
        HumanBodyViewer.show(stage);
    }
}
