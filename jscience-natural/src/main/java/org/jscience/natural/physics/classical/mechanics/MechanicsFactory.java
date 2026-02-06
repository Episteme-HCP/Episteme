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

package org.jscience.natural.physics.classical.mechanics;

import java.util.Collection;

/**
 * Factory for creating rigid body dynamics backends using SPI-based discovery.
 * Auto-detects best available backend if not specified.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MechanicsFactory {

    private static String selectedBackendId = null; // null = AUTO

    /**
     * Sets the preferred backend by ID.
     * 
     * @param backendId Backend ID or null for AUTO
     */
    public static void setBackend(String backendId) {
        selectedBackendId = backendId;
    }

    /**
     * Gets the currently selected backend ID.
     */
    public static String getSelectedBackendId() {
        return selectedBackendId;
    }

    /**
     * Creates a new physics world using the active backend.
     */
    public static NativePhysicsWorld createWorld() {
        MechanicsBackend backend = MechanicsBackendManager.getInstance().getActiveBackend();
        return backend != null ? backend.createWorld() : null;
    }
    
    /**
     * Helper to create a native rigid body directly (though usually done via PhysicsWorld).
     */
    public static NativeRigidBody createRigidBody(RigidBody body) {
        MechanicsBackend backend = MechanicsBackendManager.getInstance().getActiveBackend();
        // Since createRigidBody was in the original PhysicsEngineProvider, it should be in MechanicsBackend
        return backend != null ? backend.createRigidBody(body) : null;
    }

    /**
     * Returns all discovered mechanics backends.
     */
    public static Collection<MechanicsBackend> getAvailableBackends() {
        return MechanicsBackendManager.staticAllBackends();
    }

    /**
     * Checks if a specific backend is available.
     */
    public static boolean isBackendAvailable(String backendId) {
        MechanicsBackend b = MechanicsBackendManager.staticSelect(backendId);
        return b != null && b.isAvailable();
    }
}
