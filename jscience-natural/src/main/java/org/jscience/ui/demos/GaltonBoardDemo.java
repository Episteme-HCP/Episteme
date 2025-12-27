/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.statistics.GaltonBoardViewer;
import org.jscience.ui.i18n.I18n;

public class GaltonBoardDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.mathematics");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("GaltonBoard.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("GaltonBoard.desc");
    }

    @Override
    public void show(Stage stage) {
        GaltonBoardViewer.show(stage);
    }
}
