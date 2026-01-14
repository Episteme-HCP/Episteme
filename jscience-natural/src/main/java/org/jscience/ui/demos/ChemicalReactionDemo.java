/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.chemistry.ChemicalReactionViewer;

/**
 * Demo for the Chemical Reaction Parser.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ChemicalReactionDemo extends AbstractDemo {

    @Override
    public String getCategory() { return "Chemistry"; }

    @Override
    public String getName() { return I18n.getInstance().get("ChemicalReaction.title", "Reaction Parser"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("ChemicalReaction.desc", "Interactive Chemical Equation Balancer"); }
    
    @Override
    protected String getLongDescription() { 
        return "Parse, balance, and analyze chemical equations. Supports formula inspection."; 
    }

    @Override
    public Node createViewerNode() {
        return new ChemicalReactionViewer();
    }
}
