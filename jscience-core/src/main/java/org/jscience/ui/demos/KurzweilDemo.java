/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.futurology.KurzweilViewer;

public class KurzweilDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("matrix.category"); // Reusing category
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("kurzweil.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("kurzweil.desc");
    }

    @Override
    public void show(Stage stage) {
        KurzweilViewer.show(stage);
    }

    @Override
    public boolean isViewer() {
        return true;
    }
}
