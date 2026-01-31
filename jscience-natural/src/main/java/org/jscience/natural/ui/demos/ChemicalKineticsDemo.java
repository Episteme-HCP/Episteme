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

package org.jscience.natural.ui.demos;

import javafx.scene.Node;
import org.jscience.core.ui.AbstractSimulationDemo;
import org.jscience.core.ui.i18n.I18N;
import org.jscience.natural.ui.viewers.chemistry.ReactionKineticsViewer;

/**
 * Chemical Reaction Kinetics Demo.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ChemicalKineticsDemo extends AbstractSimulationDemo {

    private ReactionKineticsViewer viewer;

    @Override
    public String getCategory() {
        return I18N.getInstance().get("category.chemistry", "Chemistry");
    }

    @Override
    public String getName() {
        return I18N.getInstance().get("demo.kinetics.name", "Reaction Kinetics Simulator");
    }

    @Override
    public String getDescription() {
        return I18N.getInstance().get("demo.kinetics.desc", "Real-time chemical rate simulation");
    }

    @Override
    public String getLongDescription() {
        return I18N.getInstance().get("demo.kinetics.longdesc", "Predict concentration changes over time for A -> B reactions.");
    }

    @Override
    protected Node createViewerNode() {
        if (viewer == null) viewer = new ReactionKineticsViewer();
        return viewer;
    }
}


