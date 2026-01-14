/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.physics.classical.waves.electromagnetism.circuit.ResistorColorCodeViewer;

/**
 * Resistor Color Code Demo.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ResistorColorCodeDemo extends AbstractDemo {

    @Override
    public String getName() { return I18n.getInstance().get("resistor.title", "Resistor Color Code"); }

    @Override
    public String getCategory() { return "Electronics"; }

    @Override
    public Node createViewerNode() {
        return new ResistorColorCodeViewer();
    }
}
