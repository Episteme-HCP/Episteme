/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.nativ;

import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.HardwareAccelerator;
import org.jscience.core.technical.backend.ExecutionContext;

/**
 * Base interface for native backends that use JNI, FFM/Panama, or other
 * native interop mechanisms.
 * <p>
 * Extends {@link ComputeBackend} to provide full compute capabilities
 * (context creation, hardware queries) alongside native library management.
 * The package name 'nativ' avoids collision with the Java 'native' keyword.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public interface NativeBackend extends ComputeBackend {

    /**
     * Checks if the native library is loaded and available.
     *
     * @return true if the native library has been successfully loaded
     */
    boolean isLoaded();

    @Override
    default boolean isAvailable() {
        return isLoaded();
    }

    @Override
    default HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU; // Default; subinterfaces may override for GPU/FPGA
    }

    @Override
    default String getType() {
        return "native";
    }
}
