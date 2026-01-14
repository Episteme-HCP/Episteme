/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.physics.classical.waves.SpectrographViewer;

/**
 * Spectrograph Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpectrographDemo extends AbstractSimulationDemo {

    private SpectrographViewer viewer;

    @Override public boolean isDemo() { return true; }
    @Override public String getCategory() { return "Physics"; }
    @Override public String getName() { return I18n.getInstance().get("Spectrograph.title", "Spectrograph"); }
    @Override public String getDescription() { return I18n.getInstance().get("Spectrograph.desc", "Real-time frequency analysis visualization."); }
    @Override protected String getLongDescription() { 
        return "Visualizes sound or signal frequencies over time using a spectrogram and spectrum analyzer."; 
    }

    @Override
    public Node createViewerNode() {
        if (viewer == null) viewer = new SpectrographViewer();
        return viewer;
    }
}
