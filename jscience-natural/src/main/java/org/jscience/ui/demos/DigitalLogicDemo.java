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
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.computing.logic.DigitalLogicViewer;

/**
 * Digital Logic Circuit Simulator Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DigitalLogicDemo extends AbstractSimulationDemo {

    private DigitalLogicViewer viewer;

    @Override
    public boolean isDemo() { return true; }

    @Override
    public String getCategory() { return I18n.getInstance().get("category.computing", "Computing"); }

    @Override
    public String getName() { return I18n.getInstance().get("digital.title", "Digital Logic Simulator"); }

    @Override
    public String getDescription() { return I18n.getInstance().get("digital.desc", "Interactive Logic Circuit Simulator"); }
    
    @Override
    public String getLongDescription() { 
        return I18n.getInstance().get("digital.long_desc", "Design and simulate digital logic circuits using AND, OR, NOT, NAND gates."); 
    }

    @Override
    public Node createViewerNode() {
        if (viewer == null) viewer = new DigitalLogicViewer();
        return viewer;
    }

    // --- Simulatable Delegation ---

    @Override
    public void play() {
        if (viewer != null) viewer.play();
    }

    @Override
    public void pause() {
        if (viewer != null) viewer.pause();
    }

    @Override
    public void stop() {
        if (viewer != null) viewer.stop();
    }

    @Override
    public void step() {
        if (viewer != null) viewer.step();
    }

    @Override
    public void setSpeed(double speed) {
        if (viewer != null) viewer.setSpeed(speed);
    }

    @Override
    public boolean isPlaying() {
        return viewer != null && viewer.isPlaying();
    }
}