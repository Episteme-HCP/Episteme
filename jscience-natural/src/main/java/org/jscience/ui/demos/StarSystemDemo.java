/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.physics.astronomy.StarSystemViewer;

/**
 * Star System Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StarSystemDemo extends AbstractSimulationDemo {

    @Override
    public String getCategory() { return "Physics"; }

    @Override
    public String getName() { return I18n.getInstance().get("starsystem.title", "Star System 3D"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("starsystem.desc", "3D Solar System Visualization"); }

    @Override
    protected String getLongDescription() {
        return "Features: Solar System, Black Hole, and other presets with orbit trails and realistic visuals.";
    }

    @Override
    protected Node createViewerNode() {
        return new StarSystemViewer();
    }
}
