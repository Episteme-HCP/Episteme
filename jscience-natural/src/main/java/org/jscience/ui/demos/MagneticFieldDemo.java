/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.physics.classical.waves.electromagnetism.field.MagneticFieldViewer;

/**
 * Demo Provider for Spintronics Magnetic Field Visualization.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MagneticFieldDemo extends AbstractDemo {

    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getName() {
        // Fallback if key missing, but should be added to properties
        String title = I18n.getInstance().get("demo.magnetic.title");
        return title != null && !title.startsWith("!") ? title : "Magnetic Vector Field";
    }

    @Override
    public String getDescription() {
        String desc = I18n.getInstance().get("demo.magnetic.desc");
        return desc != null && !desc.startsWith("!") ? desc : "3D visualization of magnetic B-fields using vector arrows and color gradients.";
    }

    @Override
    public Node createViewerNode() {
        return new MagneticFieldViewer();
    }
}
