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
import org.jscience.ui.computing.ai.GameOfLifeViewer;
import org.jscience.ui.i18n.I18n;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GameOfLifeDemo extends SimulationDemo {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.computing");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("life.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("life.short_desc", "Conway's Game of Life");
    }

    @Override
    protected String getLongDescription() {
        return "Conway's Game of Life is a cellular automaton that demonstrates how complex patterns " +
                "can emerge from simple rules. Each cell follows three rules based on its neighbors: " +
                "survival, birth, or death by isolation or overpopulation. This interactive visualization " +
                "allows you to explore different starting densities and colors.";
    }

    @Override
    protected Node createViewerNode() {
        GameOfLifeViewer viewer = new GameOfLifeViewer();
        viewer.play();
        return viewer;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


