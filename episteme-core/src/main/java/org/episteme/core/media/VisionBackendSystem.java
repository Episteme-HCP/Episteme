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

import org.episteme.core.media.VisionBackend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.episteme.core.technical.backend.Backend;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * System manager for Vision Backends.
 * Discovers backends implementing Backend with type="vision".
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class VisionBackendSystem {

    private static VisionBackend currentBackend;

    static {
        refresh();
    }

    public static void refresh() {
        Optional<Backend> provider = BackendDiscovery.getInstance()
            .getPreferredProvider(BackendDiscovery.TYPE_VISION);
        
        if (provider.isPresent()) {
            selectBackend(provider.get().getId());
        }
    }

    public static List<String> getAvailableBackends() {
        return BackendDiscovery.getInstance()
            .getAvailableProvidersByType(BackendDiscovery.TYPE_VISION)
            .stream()
            .map(Backend::getName)
            .collect(Collectors.toList());
    }

    public static VisionBackend getVisionBackend() {
        Optional<Backend> pref = BackendDiscovery.getInstance()
            .getPreferredProvider(BackendDiscovery.TYPE_VISION);
            
        if (pref.isPresent() && (currentBackend == null || !currentBackend.getBackendName().equals(pref.get().getName()))) {
             selectBackend(pref.get().getId());
        }
        
        if (currentBackend == null) refresh();
        return currentBackend;
    }

    public static void selectBackend(String backendNameOrId) {
        Optional<Backend> provider = BackendDiscovery.getInstance().getProvidersByType(BackendDiscovery.TYPE_VISION)
            .stream()
            .filter(p -> p.getName().equals(backendNameOrId) || p.getId().equals(backendNameOrId))
            .findFirst();

        if (provider.isPresent()) {
            try {
                Object backendObj = provider.get().createBackend();
                if (backendObj instanceof VisionBackend) {
                    currentBackend = (VisionBackend) backendObj;
                    BackendDiscovery.getInstance().setPreferredProvider(BackendDiscovery.TYPE_VISION, provider.get().getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
