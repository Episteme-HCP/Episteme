/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.units.UnitConverterViewer;

public class UnitConverterDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return "Measure Converter";
    }

    @Override
    public String getDescription() {
        return "Scientific unit conversion tool for physical quantities like length, mass, energy, and temperature.";
    }

    @Override
    public void show(Stage stage) {
        UnitConverterViewer.show(stage);
    }
}
