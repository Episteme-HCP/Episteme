/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.chemistry.ReactionKineticsViewer;

/**
 * Chemical Reaction Kinetics Demo.
 */
public class ChemicalKineticsDemo extends AbstractSimulationDemo {

    private ReactionKineticsViewer viewer;

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.chemistry", "Chemistry");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("demo.kinetics.name", "Reaction Kinetics Simulator");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("demo.kinetics.desc", "Real-time chemical rate simulation");
    }

    @Override
    public String getLongDescription() {
        return I18n.getInstance().get("demo.kinetics.longdesc", "Predict concentration changes over time for A -> B reactions.");
    }

    @Override
    protected Node createViewerNode() {
        if (viewer == null) viewer = new ReactionKineticsViewer();
        return viewer;
    }
}
