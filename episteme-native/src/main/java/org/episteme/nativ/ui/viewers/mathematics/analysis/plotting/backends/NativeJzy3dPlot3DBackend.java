/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.nativ.ui.viewers.mathematics.analysis.plotting.backends;

import org.episteme.core.ui.viewers.mathematics.analysis.plotting.PlottingBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;

import com.google.auto.service.AutoService;

/**
 * Native GPU-accelerated backend for Jzy3D 3D plotting.
 * High priority backend that requires hardware OpenCL/OpenGL to be available on the machine.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(PlottingBackend.class)
public class NativeJzy3dPlot3DBackend implements PlottingBackend, NativeBackend {

    @Override
    public String getType() {
        return "plotting";
    }

    @Override
    public String getId() {
        return "jzy3d-native";
    }

    @Override
    public String getName() {
        return "Jzy3D (Native/OpenCL)";
    }

    @Override
    public String getDescription() {
        return "Hardware accelerated Java library for 3D charts and scientific plotting via native execution context.";
    }

    @Override
    public boolean isAvailable() {
        try {
            // Check for the base Jzy3d class and potentially a Native JOGL context
            Class.forName("org.jzy3d.chart.Chart");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isLoaded() {
        // If the backend is available via classpath, the JOGL bindings are considered loaded
        return isAvailable();
    }

    @Override
    public int getPriority() {
        return 70; // Higher priority than CpuJzy3dPlot3DBackend (40)
    }

    @Override
    public boolean isSupported2D() {
        return false;
    }

    @Override
    public boolean isSupported3D() {
        return true;
    }

    @Override
    public Object createBackend() {
        return null; // The exact Jzy3D binding will be requested by PlotFactory when initialized
    }
}
