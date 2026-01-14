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

package org.jscience.ui.viewers.mathematics.analysis.plotting.backends;

import org.jscience.technical.backend.BackendProvider;

/**
 * BackendProvider for JavaFX 2D plotting.
 * Always available as JavaFX is a core dependency.
 */
public class JavaFXPlot2DBackendProvider implements BackendProvider {

    @Override
    public String getType() {
        return "plotting";
    }

    @Override
    public String getId() {
        return "javafx";
    }

    @Override
    public String getName() {
        return "JavaFX Charts";
    }

    @Override
    public String getDescription() {
        return "Native JavaFX charts, always available.";
    }

    @Override
    public boolean isAvailable() {
        return true; // JavaFX is always available
    }

    @Override
    public int getPriority() {
        return 10; // Base priority, fallback option
    }

    @Override
    public Object createBackend() {
        return new JavaFXPlot2D(""); // Title set later
    }
}
