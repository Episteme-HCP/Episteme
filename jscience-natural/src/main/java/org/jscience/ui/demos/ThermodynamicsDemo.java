/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
    public String getCategory() { return "Mathematics"; }

    @Override
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("Thermodynamics.title", "Thermodynamics Explorer"); }

    @Override
    public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("thermo.desc", "Explore thermodynamic functions (Isotherms etc.)"); }
    
    @Override
    public String getLongDescription() { 
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
