/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.logic.MetamathViewer;

public class MetamathDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return "Metamath Logic Prover";
    }

    @Override
    public String getDescription() {
        return "Interactive symbolic proof explorer demonstrating step-by-step logical deductions (Metamath-style).";
    }

    @Override
    public void show(Stage stage) {
        MetamathViewer.show(stage);
    }
}
