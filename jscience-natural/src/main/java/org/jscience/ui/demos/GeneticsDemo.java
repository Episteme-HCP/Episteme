/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.biology.genetics.GeneticsViewer;

/**
 * Genetics Demo.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeneticsDemo extends AbstractDemo {

    private GeneticsViewer viewer;

    @Override
    public boolean isDemo() { return true; }

    @Override
    public String getCategory() { return "Biology"; }

    @Override
    public String getName() { return I18n.getInstance().get("Genetics.title", "Genetics"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("Genetics.desc", "Genetic drift and Mendelian inheritance simulations."); }

    @Override
    protected String getLongDescription() {
        return "Explore population genetics with genetic drift simulation and Mendelian inheritance calculator with Punnett squares.";
    }

    @Override
    public Node createViewerNode() {
        if (viewer == null) viewer = new GeneticsViewer();
        return viewer;
    }
}
