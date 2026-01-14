/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.chemistry.PeriodicTableViewer;

/**
 * Periodic Table Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PeriodicTableDemo extends AbstractDemo {

    @Override
    public boolean isDemo() { return true; }

    @Override
    public String getCategory() { return "Chemistry"; }

    @Override
    public String getName() { return I18n.getInstance().get("PeriodicTable.title", "Periodic Table"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("PeriodicTable.desc", "Interactive Periodic Table of Elements"); }
    
    @Override
    protected String getLongDescription() { 
        return "Displays all chemical elements in standard periodic table layout with 3D atomic structure visualization."; 
    }

    @Override
    public Node createViewerNode() {
        return new PeriodicTableViewer();
    }
}
