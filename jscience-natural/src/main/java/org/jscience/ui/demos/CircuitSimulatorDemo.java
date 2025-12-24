/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.engineering.circuit.CircuitSimulatorViewer;

public class CircuitSimulatorDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Engineering";
    }

    @Override
    public String getName() {
        return "Electrical Circuit Designer";
    }

    @Override
    public String getDescription() {
        return "Interactive schematic editor for electrical circuits with resistors, capacitors, and power sources.";
    }

    @Override
    public void show(Stage stage) {
        CircuitSimulatorViewer.show(stage);
    }
}
