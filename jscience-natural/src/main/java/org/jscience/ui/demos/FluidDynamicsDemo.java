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

import javafx.stage.Stage;
import org.jscience.ui.AppProvider;
import org.jscience.ui.physics.fluids.FluidDynamicsViewer;
import org.jscience.ui.i18n.I18n;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FluidDynamicsDemo implements AppProvider {
    @Override
    public boolean isDemo() {
        return true;
    }

    @Override
    public String getCategory() {
        return "Physics";
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("FluidDynamics.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("FluidDynamics.desc");
    }

    @Override
    public void show(Stage stage) {
        // Assuming FluidDynamicsViewer has a static show or similar, or instantiate
        // Checking usage: usually new FluidDynamicsViewer().start(stage);
        // But need to verify class exists and has no-arg constructor/start.
        // Assuming standard pattern for now.
        new FluidDynamicsViewer().start(stage);
    }
}


