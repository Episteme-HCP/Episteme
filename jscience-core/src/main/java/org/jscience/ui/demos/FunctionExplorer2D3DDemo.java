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

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.viewers.mathematics.analysis.real.FunctionExplorer2DViewer;
import org.jscience.ui.viewers.mathematics.analysis.real.FunctionExplorer3DViewer;
import org.jscience.ui.i18n.I18n;


/**
 * Unified Demo for 2D and 3D Function Exploration.
 */
public class FunctionExplorer2D3DDemo extends AbstractDemo {

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.mathematics");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("demo.functionexplorer2d3ddemo.name");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("demo.functionexplorer2d3ddemo.desc");
    }

    @Override
    protected javafx.scene.Node createViewerNode() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tab2D = new Tab(I18n.getInstance().get("funcexplorer.tab.2d", "2D Explorer"));
        FunctionExplorer2DViewer v2D = new FunctionExplorer2DViewer();
        tab2D.setContent(v2D);

        Tab tab3D = new Tab(I18n.getInstance().get("funcexplorer.tab.3d", "3D Explorer"));
        FunctionExplorer3DViewer v3D = new FunctionExplorer3DViewer();
        tab3D.setContent(v3D);

        tabPane.getTabs().addAll(tab2D, tab3D);

        // Set the primary viewer for parameter panel delegation (optional)
        this.viewer = v2D;

        return tabPane;
    }

    @Override
    protected String getLongDescription() {
        return I18n.getInstance().get("demo.functionexplorer2d3ddemo.longdesc");
    }


}
