/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.engineering.circuits.LogicGateSimulator;

public class LogicGateDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Engineering";
    }

    @Override
    public String getName() {
        return "Logic Gate Simulator";
    }

    @Override
    public String getDescription() {
        return "Interactive digital logic circuit builder and simulator.";
    }

    @Override
    public void show(Stage stage) {
        LogicGateSimulator.show(stage);
    }
}
