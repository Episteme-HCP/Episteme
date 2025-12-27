/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.chemistry.MolecularViewer;

import org.jscience.ui.i18n.I18n;

public class MolecularViewerDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.chemistry");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("MolecularViewer.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("MolecularViewer.desc");
    }

    @Override
    public void show(Stage stage) {
        MolecularViewer.show(stage);
    }
}
