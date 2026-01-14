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

package org.jscience.ui.viewers.geography.backend;

import org.jscience.technical.backend.BackendProvider;

/**
 * BackendProvider for JavaFX map renderer.
 * Always available as JavaFX is a core dependency.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JavaFXMapBackendProvider implements BackendProvider {

    @Override
    public String getType() {
        return "map";
    }

    @Override
    public String getId() {
        return "javafx_map";
    }

    @Override
    public String getName() {
        return "JavaFX Map";
    }

    @Override
    public String getDescription() {
        return "Integrated JavaFX map visualization engine (basic support).";
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
        return new JavaFXMapRenderer();
    }
}
