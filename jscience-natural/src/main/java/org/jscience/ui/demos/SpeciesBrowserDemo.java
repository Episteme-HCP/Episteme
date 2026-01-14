/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.biology.ecology.SpeciesBrowserViewer;

/**
 * Species Browser Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpeciesBrowserDemo extends AbstractDemo {

    @Override
    public boolean isDemo() { return true; }

    @Override
    public String getCategory() { return I18n.getInstance().get("category.biology"); }

    @Override
    public String getName() { return I18n.getInstance().get("SpeciesBrowser.title", "Species Browser"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("SpeciesBrowser.desc", "Explore biological species data using the Global Biodiversity Information Facility (GBIF) API."); }
    
    @Override
    protected String getLongDescription() {
        return "Search and browse species taxonomy and images via the GBIF network.";
    }

    @Override
    public Node createViewerNode() {
        return new SpeciesBrowserViewer();
    }
}
