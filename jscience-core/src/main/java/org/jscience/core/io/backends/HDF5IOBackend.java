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

package org.jscience.core.io.backends;

import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.BackendDiscovery;
import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.ExecutionContext;
import org.jscience.core.technical.backend.HardwareAccelerator;
import com.google.auto.service.AutoService;

/**
 * HDF5 I/O backend using the JHDF library.
 * <p>
 * Available when {@code io.jhdf.HdfFile} is on the classpath.
 * Provides hierarchical data format (HDF5) read/write support as a
 * discoverable JScience backend.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService({Backend.class, ComputeBackend.class})
public class HDF5IOBackend implements ComputeBackend {

    @Override
    public String getType() {
        return BackendDiscovery.TYPE_IO;
    }

    @Override
    public String getId() {
        return "hdf5";
    }

    @Override
    public String getName() {
        return "HDF5 I/O";
    }

    @Override
    public String getDescription() {
        return "HDF5 hierarchical data format I/O backend using the JHDF library.";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("io.jhdf.HdfFile");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 60;
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    public ExecutionContext createContext() {
        return null;
    }

    @Override
    public Object createBackend() {
        return this;
    }

    @Override
    public boolean supportsFloatingPoint() {
        return true;
    }

    @Override
    public boolean supportsComplexNumbers() {
        return false;
    }

}
