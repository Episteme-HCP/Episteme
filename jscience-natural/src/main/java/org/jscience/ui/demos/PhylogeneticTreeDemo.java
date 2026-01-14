/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.biology.phylogeny.PhylogeneticTreeViewer;

/**
 * Phylogenetic Tree Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PhylogeneticTreeDemo extends AbstractDemo {

    @Override
    public boolean isDemo() { return true; }

    @Override
    public String getCategory() { return "Biology"; }

    @Override
    public String getName() { return I18n.getInstance().get("PhylogeneticTree.title", "Phylogenetic Tree"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("PhylogeneticTree.desc", "Visualizes evolutionary relationships."); }
    
    @Override
    protected String getLongDescription() { 
        return "Interactive phylogenetic tree visualization with radial and linear layouts, highlighting genetic marker data."; 
    }

    @Override
    public Node createViewerNode() {
        return new PhylogeneticTreeViewer();
    }
}
