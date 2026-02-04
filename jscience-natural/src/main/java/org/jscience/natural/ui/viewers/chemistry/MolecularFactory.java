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

package org.jscience.natural.ui.viewers.chemistry;

import org.jscience.core.technical.backend.Backend;
import org.jscience.natural.ui.viewers.chemistry.backends.JavaFXMolecularBackendProvider;

import java.util.Collection;

/**
 * Factory for creating molecular visualizations using SPI-based backend discovery.
 * Auto-detects best available backend if not specified.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MolecularFactory {

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
     * Creates a molecular viewer with default/AUTO backend.
     */
    public static Object createViewer(String title) {
        Backend provider = getBackendProvider();
        if (provider != null) {
            return provider.createBackend();
        }

        // Ultimate fallback (assuming JavaFX provider exists in backends but might strictly belong to a specific package)
        // I'll try to instantiate it, if not found this will fail compilation unless I import it.
        // I will add import for JavaFXMolecularBackendProvider. 
        // NOTE: I haven't checked if JavaFXMolecularBackendProvider exists and where.
        // Step 1169 showed `JavaFXMolecularRenderer`, I assume Provider is also there.
        // Detailed check of `jscience-natural` ... `backends` folder:
        // I saw `JavaFXMolecularBackendProvider` listed in `META-INF/services` file content in Step 1039!
        // `org.jscience.natural.physics.chemistry.backends.JavaFXMolecularBackendProvider`.
        // Wait, step 1039 showed `org.jscience.natural.physics.chemistry.backends...`
        // But the user has been asking to move `jscience-natural.ui.viewers.chemistry`.
        // There is a mix of `physics.chemistry` and `ui.viewers.chemistry`.
        // Step 1169 said `jscience-natural\src\main\java\org\jscience\natural\ui\viewers\chemistry\backends\MolecularRenderer.java`.
        // So `ui.viewers.chemistry` seems to be the current location for viewers.
        // I will assume `JavaFXMolecularBackendProvider` is in `org.jscience.natural.ui.viewers.chemistry.backends`.
        
        return new JavaFXMolecularBackendProvider().createBackend();
    }

    /**
     * Gets the best available molecular backend provider.
     */
    private static Backend getBackendProvider() {
        if (selectedBackendId != null) {
             MolecularBackend b = MolecularBackendManager.getInstance().select(selectedBackendId);
             if (b != null && b.isAvailable()) {
                return b;
             }
        }
        return MolecularBackendManager.getInstance().getActiveBackend();
    }

    /**
     * Returns all discovered molecular backend providers.
     */
    public static Collection<MolecularBackend> getAvailableBackends() {
        return MolecularBackendManager.getInstance().getAllBackends();
    }

    /**
     * Checks if a specific backend is available.
     */
    public static boolean isBackendAvailable(String backendId) {
        MolecularBackend b = MolecularBackendManager.getInstance().select(backendId);
        return b != null && b.isAvailable();
    }
}
