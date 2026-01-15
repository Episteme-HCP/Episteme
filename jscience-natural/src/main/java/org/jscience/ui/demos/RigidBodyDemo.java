/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
    public String getLongDescription() {
        return "Simulate 2D rigid bodies with gravity, collisions, and bouncing. Uses the JScience RigidBody physics engine.";
    }

    @Override
    public Node createViewerNode() {
        if (viewer == null) viewer = new RigidBodyViewer();
        return viewer;
    }
}
