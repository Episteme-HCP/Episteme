/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.shared.KnowledgeNavigatorViewer;

/**
 * Knowledge Navigator Demo.
 */
public class KnowledgeNavigatorDemo extends AbstractDemo {

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.general", "General");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("demo.navigator.name", "Knowledge Navigator");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("demo.navigator.desc", "Explore connections between scientific entities");
    }

    @Override
    public String getLongDescription() {
        return I18n.getInstance().get("demo.navigator.longdesc", "Multi-disciplinary navigation spanning from subatomic particles to social organizations.");
    }

    @Override
    protected Node createViewerNode() {
        return new KnowledgeNavigatorViewer();
    }
}
