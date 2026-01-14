/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.mathematics.analysis.real.FunctionExplorer2DViewer;

/**
 * Thermodynamics Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ThermodynamicsDemo extends AbstractDemo {

    @Override
    public boolean isDemo() { return true; }

    @Override
    public String getCategory() { return "Physics"; }

    @Override
    public String getName() { return I18n.getInstance().get("Thermodynamics.title", "Thermodynamics Explorer"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("thermo.desc", "Explore thermodynamic functions (Isotherms etc.)"); }
    
    @Override
    protected String getLongDescription() { 
        return "Visualizes thermodynamic relationships. Default view shows Ideal Gas Law Isotherms usually plotted as P vs V."; 
    }

    @Override
    public Node createViewerNode() {
        // Ideal Gas Law: P = nRT/V. Let n=1, R=8.314.
        // T1 = 300K, T2 = 400K.
        // f(x) (P) = (1 * 8.314 * 300) / x  -> 2494.2 / x
        // g(x) (P) = (1 * 8.314 * 400) / x  -> 3325.6 / x
        // x represents Volume (V).
        return new FunctionExplorer2DViewer("2494.2 / x", "3325.6 / x");
    }
}
