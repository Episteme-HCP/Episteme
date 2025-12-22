/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.biology.genetics.GeneticsViewer;

public class GeneticsDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getName() {
        return "Genetic Drift";
    }

    @Override
    public String getDescription() {
        return "Simulates allele frequency changes over generations (Drift).";
    }

    @Override
    public void show(Stage stage) {
        GeneticsViewer.show(stage);
    }
}
