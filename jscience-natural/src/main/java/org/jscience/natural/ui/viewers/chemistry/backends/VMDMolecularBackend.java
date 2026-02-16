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

package org.jscience.natural.ui.viewers.chemistry.backends;

import org.jscience.natural.ui.viewers.chemistry.MolecularBackend;

/**
 * Backend for VMD (Visual Molecular Dynamics).
 * VMD is an external application for biomolecular simulations.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VMDMolecularBackend implements MolecularBackend {

    @Override
    public String getType() {
        return "molecular";
    }

    @Override
    public String getId() {
        return "vmd";
    }

    @Override
    public String getName() {
        return "VMD";
    }

    @Override
    public String getDescription() {
        return "Visual Molecular Dynamics for biomolecular simulation (external process).";
    }

    @Override
    public boolean isAvailable() {
        try {
            // Check for VMD executable in PATH
            ProcessBuilder pb = new ProcessBuilder(System.getProperty("os.name").toLowerCase().contains("win") ? "where" : "which", "vmd");
            Process p = pb.start();
            return p.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 30;
    }
    @Override public boolean isSupport3D() { return true; }
    @Override public boolean isSupportInteractive() { return true; }

    @Override public Object createBackend() {
        return new VMDMolecularRenderer();
    }
}
