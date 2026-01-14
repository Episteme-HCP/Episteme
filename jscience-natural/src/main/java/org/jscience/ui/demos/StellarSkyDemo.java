/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.physics.astronomy.StellarSkyViewer;

/**
 * Stellar Sky Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StellarSkyDemo extends AbstractDemo {

    @Override
    public boolean isDemo() { return true; }

    @Override
    public String getCategory() { return "Physics"; }

    @Override
    public String getName() { return I18n.getInstance().get("sky.window.title", "Night Sky Visualizer"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("sky.title", "Stellar Sky"); }
    
    @Override
    protected String getLongDescription() { 
        return "Interactive Planetarium showing stars, planets, and constellations based on location and time."; 
    }

    @Override
    public Node createViewerNode() {
        return new StellarSkyViewer();
    }
}
