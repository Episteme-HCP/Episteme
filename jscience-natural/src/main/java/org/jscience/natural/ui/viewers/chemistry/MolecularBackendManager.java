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

import org.jscience.core.technical.backend.AbstractBackendManager;
import java.util.Comparator;

/**
 * Manager for molecular rendering backends.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public class MolecularBackendManager extends AbstractBackendManager<MolecularBackend> {

    private static final MolecularBackendManager INSTANCE = new MolecularBackendManager();

    public static MolecularBackendManager getInstance() {
        return INSTANCE;
    }

    private String preferredId = "auto";

    private MolecularBackendManager() {
        super(MolecularBackend.class);
    }

    /**
     * Gets the preferred molecular backend ID.
     */
    public String getPreferredId() {
        return preferredId;
    }

    /**
     * Sets the preferred molecular backend ID.
     */
    public void setPreferredId(String id) {
        this.preferredId = id;
    }

    /**
     * Returns the active molecular backend.
     */
    public MolecularBackend getActiveBackend() {
        if ("auto".equalsIgnoreCase(preferredId)) {
            return selectBestBackend();
        }
        MolecularBackend b = managerSelect(preferredId);
        return (b != null) ? b : selectBestBackend();
    }

    @Override
    protected MolecularBackend selectBestBackend() {
        return backends.values().stream()
                .filter(MolecularBackend::isAvailable)
                .max(Comparator.comparingInt(MolecularBackend::getPriority))
                .orElse(null);
    }
}
