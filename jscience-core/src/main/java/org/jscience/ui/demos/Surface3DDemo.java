/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.numbers.real.Surface3DViewer;

import org.jscience.ui.i18n.I18n;

public class Surface3DDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.mathematics");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("viewer.surface3d");
    }

    @Override
    public String getDescription() {
        return "Interactive 3D visualization of mathematical surfaces z = f(x, y) with rotation and zoom.";
    }

    @Override
    public void show(Stage stage) {
        Surface3DViewer.show(stage);
    }
}
