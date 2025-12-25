/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.biology.genetics.GeneticsViewer;

public class GeneticsDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getName() {
        return org.jscience.natural.i18n.I18n.getInstance().get("Genetics.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.natural.i18n.I18n.getInstance().get("Genetics.desc");
    }

    @Override
    public void show(Stage stage) {
        GeneticsViewer.show(stage);
    }
}
