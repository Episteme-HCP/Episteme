/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.biology.ecology.LotkaVolterraViewer;

/**
 * Lotka-Volterra Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LotkaVolterraDemo extends AbstractSimulationDemo {

    private LotkaVolterraViewer viewer;

    @Override
    public boolean isDemo() { return true; }

    @Override
    public String getCategory() { return "Biology"; }

    @Override
    public String getName() { return I18n.getInstance().get("LotkaVolterra.title", "Lotka-Volterra"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("LotkaVolterra.desc", "Predator-prey population dynamics simulation."); }
    
    @Override
    protected String getLongDescription() {
        return "Simulate the interaction between predator and prey populations using Lotka-Volterra equations. Visualizes time series and phase portraits.";
    }

    @Override
    public Node createViewerNode() {
        if (viewer == null) viewer = new LotkaVolterraViewer();
        return viewer;
    }
}
