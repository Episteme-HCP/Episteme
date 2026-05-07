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
package org.episteme.core.mathematics.loaders;

import org.episteme.core.io.AbstractResourceReader;
import org.episteme.core.mathematics.ml.neural.backends.ONNXRuntimeBackend;
import org.episteme.core.mathematics.ml.neural.backends.ONNXRuntimeBackend.ONNXSession;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Standardized Loader for ONNX (Open Neural Network Exchange) models.
 * <p>
 * Implements {@link AbstractResourceReader} to provide caching and unified access.
 * Delegates actual model execution to {@link OnnxRuntimeProvider}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class ONNXModelReader extends AbstractResourceReader<ONNXSession> {

    private static final Logger logger = LoggerFactory.getLogger(ONNXModelReader.class);
    private static final ONNXModelReader INSTANCE = new ONNXModelReader();

    public static ONNXModelReader getInstance() {
        return INSTANCE;
    }

    @Override
    protected ONNXSession loadFromSource(String id) throws Exception {
        // ID is expected to be a file path
        Path modelPath = Paths.get(id);
        
        ONNXRuntimeBackend provider = new ONNXRuntimeBackend(); // Could be singleton/injected
        logger.info("Loading ONNX model from: {}", modelPath);
        ONNXSession session = provider.loadModel(modelPath);
        
        if (session == null) {
            throw new Exception("Failed to load ONNX model from: " + id);
        }
        
        return session;
    }

    @Override
    public String getName() {
        return "ONNX Model Loader";
    }

    @Override
    public String getDescription() {
        return "Loads ONNX models using ONNX Runtime";
    }

    @Override
    public String getLongDescription() {
        return "Provides support for loading Open Neural Network Exchange (ONNX) models for inference.";
    }

    @Override
    public String getCategory() {
        return "AI/ML";
    }

    @Override
    public Class<ONNXSession> getResourceType() {
        return ONNXSession.class;
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] { "1.x" };
    }

    @Override
    public String getResourcePath() {
        return null; // Not applicable for file-based loader
    }
}
