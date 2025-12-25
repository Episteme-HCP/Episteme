/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.fractals.MandelbrotViewer;

public class MandelbrotDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return org.jscience.natural.i18n.I18n.getInstance().get("mandelbrot.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.natural.i18n.I18n.getInstance().get("mandelbrot.desc");
    }

    @Override
    public void show(Stage stage) {
        MandelbrotViewer.show(stage);
    }
}
