/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.physics.classical.mechanics.RigidBodyViewer;

/**
 * Rigid Body Physics Demo.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RigidBodyDemo extends AbstractSimulationDemo {

    private RigidBodyViewer viewer;

    @Override
    public boolean isDemo() { return true; }

    @Override
    public String getCategory() { return "Physics"; }

    @Override
    public String getName() { return I18n.getInstance().get("RigidBody.title", "Rigid Body Physics"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("RigidBody.desc", "2D rigid body simulation with collisions."); }

    @Override
    protected String getLongDescription() {
        return "Simulate 2D rigid bodies with gravity, collisions, and bouncing. Uses the JScience RigidBody physics engine.";
    }

    @Override
    public Node createViewerNode() {
        if (viewer == null) viewer = new RigidBodyViewer();
        return viewer;
    }
}
