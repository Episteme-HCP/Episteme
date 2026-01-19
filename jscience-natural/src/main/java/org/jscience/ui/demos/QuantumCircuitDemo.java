/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.physics.quantum.QuantumCircuitViewer;

/**
 * Quantum Circuit Simulation Demo.
 */
public class QuantumCircuitDemo extends AbstractDemo {

    private QuantumCircuitViewer viewer;

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.physics", "Physics") + " / " + I18n.getInstance().get("category.quantum", "Quantum");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("demo.quantum.name", "Quantum Register Simulation");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("demo.quantum.desc", "Universal Quantum Computing Simulator");
    }

    @Override
    public String getLongDescription() {
        return I18n.getInstance().get("demo.quantum.longdesc", "Visualization of qubit state probabilities and circuit architecture.");
    }

    @Override
    protected Node createViewerNode() {
        if (viewer == null) viewer = new QuantumCircuitViewer();
        return viewer;
    }
}
