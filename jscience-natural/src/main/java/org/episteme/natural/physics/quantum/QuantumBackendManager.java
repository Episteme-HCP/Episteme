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

package org.episteme.natural.physics.quantum;

import org.episteme.core.technical.backend.AbstractBackendManager;
import org.episteme.natural.technical.backend.quantum.QuantumBackend;

import java.util.Comparator;

/**
 * Manager for quantum computing backends.
 * <p>
 * Manages discovery and selection of quantum backends including:
 * Qiskit, Amazon Braket, Strange, Quantum4J, and Python-based backends.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class QuantumBackendManager extends AbstractBackendManager<QuantumBackend> {

    private static final QuantumBackendManager INSTANCE = new QuantumBackendManager();

    public static QuantumBackendManager getInstance() {
        return INSTANCE;
    }

    public static QuantumBackend staticSelect(String name) {
        return INSTANCE.managerSelect(name);
    }

    public static java.util.Collection<QuantumBackend> staticAllBackends() {
        return INSTANCE.managerAll();
    }

    /**
     * @deprecated Use staticSelect
     */
    @Deprecated
    public static QuantumBackend select(String name) {
        return staticSelect(name);
    }

    /**
     * @deprecated Use staticAllBackends
     */
    @Deprecated
    public static java.util.Collection<QuantumBackend> getAllBackends() {
        return staticAllBackends();
    }

    private String preferredId = "auto";

    private QuantumBackendManager() {
        super(QuantumBackend.class);
    }

    /**
     * Gets the preferred backend ID.
     */
    public String getPreferredId() {
        return preferredId;
    }

    /**
     * Sets the preferred backend ID.
     * 
     * @param id Backend ID ("qiskit", "amazon-braket", "strange", "quantum4j", "python") or "auto"
     */
    public void setPreferredId(String id) {
        this.preferredId = id;
    }

    /**
     * Returns the active quantum backend.
     */
    public QuantumBackend getActiveBackend() {
        if ("auto".equalsIgnoreCase(preferredId)) {
            return selectBestBackend();
        }
        QuantumBackend backend = managerSelect(preferredId);
        return (backend != null) ? backend : selectBestBackend();
    }

    @Override
    protected QuantumBackend selectBestBackend() {
        return backends.values().stream()
                .filter(QuantumBackend::isAvailable)
                .max(Comparator.comparingInt(QuantumBackend::getPriority))
                .orElse(null);
    }
}
