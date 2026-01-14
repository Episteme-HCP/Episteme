/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.medicine.anatomy.HumanBodyViewer;

/**
 * Human Body Anatomy Demo.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HumanBodyDemo extends AbstractDemo {

    private HumanBodyViewer viewer;

    @Override
    public boolean isDemo() { return true; }

    @Override
    public String getCategory() { return "Biology"; }

    @Override
    public String getName() { return I18n.getInstance().get("HumanBody.title", "Human Body Anatomy"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("HumanBody.desc", "3D anatomy viewer based on Z-Anatomy."); }

    @Override
    protected String getLongDescription() {
        return "Explore detailed 3D human anatomy with skeletal, muscular, and organ systems. Based on Z-Anatomy data.";
    }

    @Override
    public Node createViewerNode() {
        if (viewer == null) viewer = new HumanBodyViewer();
        return viewer;
    }
}
