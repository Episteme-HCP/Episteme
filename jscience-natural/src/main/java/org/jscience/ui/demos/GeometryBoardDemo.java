/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.geometry.GeometryBoardViewer;

public class GeometryBoardDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return org.jscience.natural.i18n.I18n.getInstance().get("GeometryBoard.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.natural.i18n.I18n.getInstance().get("GeometryBoard.desc");
    }

    @Override
    public void show(Stage stage) {
        GeometryBoardViewer.show(stage);
    }
}
