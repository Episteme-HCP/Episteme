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

package org.episteme.core.mathematics.ml.backends;

import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.core.technical.backend.ComputeBackend;
import com.google.auto.service.AutoService;

/**
 * ONNX Runtime inference backend.
 * <p>
 * Wraps the ONNX Runtime (ORT) Java API for running machine learning model
 * inference. Available when the {@code ai.onnxruntime} library is on the
 * classpath. Registers as a native-library-dependent backend.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService({Backend.class, ComputeBackend.class})
public class ONNXBackend implements ComputeBackend {

    @Override
    public String getType() {
        return BackendDiscovery.TYPE_ML;
    }

    @Override
    public String getId() {
        return "onnx";
    }

    @Override
    public String getName() {
        return "ONNX Runtime";
    }

    @Override
    public String getDescription() {
        return "ONNX Runtime inference backend for running ML models in ONNX format.";
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("ai.onnxruntime.OrtEnvironment");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU; // ORT supports CPU and GPU; default to CPU
    }

    @Override
    public ExecutionContext createContext() {
        return null; // Uses OrtSession internally
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
