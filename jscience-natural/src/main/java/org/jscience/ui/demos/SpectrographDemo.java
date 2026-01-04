/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
import org.jscience.ui.SimulationDemo;
import org.jscience.ui.viewers.physics.classical.waves.SpectrographViewer;
import org.jscience.ui.i18n.I18n;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpectrographDemo extends SimulationDemo {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.physics");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("spectrograph.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("spectrograph.short_desc", "Real-time frequency analysis");
    }

    @Override
    protected String getLongDescription() {
        return "This demonstration showcases a real-time spectrograph that performs frequency analysis on various signal patterns. "
                +
                "It features two visualizations: a standard frequency spectrum (top) and a scrolling spectrogram (bottom) which "
                +
                "displays frequency intensity over time using a color heatmap. You can switch between a primitive mathematical "
                +
                "engine and a physics-based engine using the parameters on the right.";
    }

    @Override
    protected Node createViewerNode() {
        SpectrographViewer sv = new SpectrographViewer();
        sv.play(); // Start by default
        return sv;
    }
}


