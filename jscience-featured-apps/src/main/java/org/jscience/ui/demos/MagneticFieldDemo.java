/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import org.jscience.apps.physics.spintronics.viewer.MagneticFieldViewer;
import org.jscience.ui.ViewerProvider;
import javafx.stage.Stage;

/**
 * Demo Provider for Spintronics Magnetic Field Visualization.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MagneticFieldDemo implements ViewerProvider {

    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getName() {
        // Fallback if key missing, but should be added to properties
        String title = org.jscience.ui.i18n.I18n.getInstance().get("demo.magnetic.title");
        return title != null && !title.startsWith("!") ? title : "Magnetic Vector Field";
    }

    @Override
    public String getDescription() {
        String desc = org.jscience.ui.i18n.I18n.getInstance().get("demo.magnetic.desc");
        return desc != null && !desc.startsWith("!") ? desc : "3D visualization of magnetic B-fields using vector arrows and color gradients.";
    }

    @Override
    public void show(Stage stage) {
        MagneticFieldViewer.show(stage);
    }
}
