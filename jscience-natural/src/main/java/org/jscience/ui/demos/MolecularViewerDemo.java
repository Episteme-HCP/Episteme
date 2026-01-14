/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.chemistry.MolecularViewer;

/**
 * Molecular Viewer Demo.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MolecularViewerDemo extends AbstractDemo {

    @Override
    public String getName() { return I18n.getInstance().get("molecular.title", "Molecular Viewer"); }

    @Override
    public String getCategory() { return "Chemistry"; }

    @Override
    public Node createViewerNode() {
        return new MolecularViewer();
    }
}
