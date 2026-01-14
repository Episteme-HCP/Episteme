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
 * BackendProvider for PyMOL molecular renderer.
 * PyMOL is an external application that requires a bridge.
 */
public class PyMOLMolecularBackendProvider implements BackendProvider {

    @Override
    public String getType() {
        return "molecular";
    }

    @Override
    public String getId() {
        return "pymol";
    }

    @Override
    public String getName() {
        return "PyMOL";
    }

    @Override
    public String getDescription() {
        return "Molecular visualization system with advanced rendering (external process).";
    }

    @Override
    public boolean isAvailable() {
        // Check if pymol executable exists in PATH
        // For now, return false as not yet implemented
        return false;
    }

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public Object createBackend() {
        // Would create a PyMOL bridge renderer
        throw new UnsupportedOperationException("PyMOL backend not yet implemented");
    }
}
