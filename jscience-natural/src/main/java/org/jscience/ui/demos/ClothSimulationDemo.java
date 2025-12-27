/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.computing.simulation.ClothSimulationViewer;
import org.jscience.ui.i18n.I18n;

public class ClothSimulationDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.physics");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("ClothSimulation.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("ClothSimulation.desc");
    }

    @Override
    public void show(Stage stage) {
        ClothSimulationViewer.show(stage);
    }
}
