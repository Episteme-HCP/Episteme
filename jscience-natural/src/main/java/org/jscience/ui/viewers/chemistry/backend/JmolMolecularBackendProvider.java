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

package org.jscience.ui.viewers.chemistry.backend;

import org.jscience.technical.backend.BackendProvider;

/**
 * BackendProvider for Jmol molecular renderer.
 * Available only when Jmol library is on the classpath.
 */
public class JmolMolecularBackendProvider implements BackendProvider {

    @Override
    public String getType() {
        return "molecular";
    }

    @Override
    public String getId() {
        return "jmol";
    }

    @Override
    public String getName() {
        return "Jmol";
    }

    @Override
    public String getDescription() {
        return "Open-source Java viewer for chemical structures with scripting support.";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.jmol.viewer.Viewer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 50; // Higher priority than JavaFX when available
    }

    @Override
    public Object createBackend() {
        return new JmolMolecularRenderer();
    }
}
