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

package org.episteme.core.media;

import java.awt.image.BufferedImage;
import org.episteme.core.media.vision.VisionAlgorithmProvider;
import org.episteme.core.media.vision.ImageOp;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;

/**
 * Interface for Vision Backends.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface VisionBackend extends ComputeBackend, VisionAlgorithmProvider<BufferedImage> {

    @Override
    default String getType() {
        return "vision";
    }

    @Override
    default org.episteme.core.technical.backend.ExecutionContext createContext() {
        return null;
    }

    @Override
    default String getName() {
        return getType();
    }

    /**
     * Returns the friendly name of this backend instance.
     */
    String getBackendName();

    @Override
    default String getId() {
        return getBackendName().toLowerCase().replace(" ", "-");
    }

    @Override
    default String getDescription() {
        return getBackendName() + " vision backend";
    }

    @Override
    default int getPriority() {
        return 0;
    }

    @Override
    default boolean isAvailable() {
        return true;
    }

    @Override
    default void shutdown() {
    }


    @Override
    default HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    default Object createBackend() {
        return this;
    }
}
