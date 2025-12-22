/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.biology.phylogeny.PhylogeneticTreeViewer;

public class PhylogeneticTreeDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getName() {
        return "Phylogenetic Tree";
    }

    @Override
    public String getDescription() {
        return "Interactive evolutionary tree browser showing taxonomic relationships.";
    }

    @Override
    public void show(Stage stage) {
        PhylogeneticTreeViewer.show(stage);
    }
}
