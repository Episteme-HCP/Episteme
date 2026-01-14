/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.ui.demos;

import org.jscience.ui.AbstractDemo;
import org.jscience.ui.viewers.arts.ArtsColorTheoryViewer;
import org.jscience.ui.i18n.SocialI18n;

/**
 * Demo for Arts & Color Theory.
 * Uses ArtsColorTheoryViewer.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ArtsColorTheoryDemo extends AbstractDemo {

    @Override
    public String getCategory() {
        return "Arts";
    }

    @Override
    public String getName() {
        return SocialI18n.getInstance().get("ArtsColorTheory.title");
    }

    @Override
    public String getDescription() {
        return SocialI18n.getInstance().get("ArtsColorTheory.desc");
    }

    @Override
    protected javafx.scene.Node createViewerNode() {
        return new ArtsColorTheoryViewer();
    }
    
    @Override
    protected String getLongDescription() {
         return SocialI18n.getInstance().get("ArtsColorTheory.desc");
    }
}
