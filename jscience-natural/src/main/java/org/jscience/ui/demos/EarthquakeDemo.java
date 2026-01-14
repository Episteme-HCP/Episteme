/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.earth.seismology.EarthquakeMapViewer;

/**
 * Earthquake Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EarthquakeDemo extends AbstractDemo {

    @Override
    public boolean isDemo() { return true; }

    @Override
    public String getCategory() { return "Earth Sciences"; }

    @Override
    public String getName() { return I18n.getInstance().get("earthquake.label.title", "Earthquake Map"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("earthquake.desc", "Interactive Seismic Map"); }
    
    @Override
    protected String getLongDescription() { 
        return "Visualizes seismic data on an interactive world map with explanations and controls."; 
    }

    @Override
    public Node createViewerNode() {
        return new EarthquakeMapViewer();
    }
}
