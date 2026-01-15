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

import org.jscience.ui.AbstractDemo;

public class EconomicsGDPDemo extends AbstractDemo {

    @Override
    public boolean isDemo() {
        return true;
    }

    @Override
    public String getCategory() { return "Economics"; }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("demo.economicsgdpdemo.name");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("demo.economicsgdpdemo.desc");
    }

    @Override
    protected javafx.scene.Node createViewerNode() {
        // User requested: "should use FunctionExplorer2DViewer"
        // P = P0 * (1+r)^t -> y = 25 * (1 + 0.02)^x
        org.jscience.ui.viewers.mathematics.analysis.real.FunctionExplorer2DViewer v = new org.jscience.ui.viewers.mathematics.analysis.real.FunctionExplorer2DViewer();
        this.viewer = v;
        return v;
    }

    @Override
    protected String getLongDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("demo.economicsgdpdemo.longdesc");
    }
}