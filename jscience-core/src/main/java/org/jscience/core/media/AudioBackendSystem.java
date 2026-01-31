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

package org.jscience.core.media;

import org.jscience.core.media.backends.JavaSoundBackend;
import org.jscience.core.technical.backend.BackendDiscovery;
import org.jscience.core.technical.backend.BackendProvider;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Manager for Audio Backends.
 * Discovers backends implementing BackendProvider with type="audio".
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AudioBackendSystem {


    private static AudioBackend currentBackend;

    static {
        refresh();
    }

    public static void refresh() {
        Optional<BackendProvider> provider = BackendDiscovery.getInstance()
            .getPreferredProvider(BackendDiscovery.TYPE_AUDIO);
        
        if (provider.isPresent()) {
            selectBackend(provider.get().getId());
        } else {
            // Default fallback
            currentBackend = new JavaSoundBackend();
        }
    }

    public static List<String> getAvailableBackends() {
        return BackendDiscovery.getInstance()
            .getAvailableProvidersByType(BackendDiscovery.TYPE_AUDIO)
            .stream()
            .map(BackendProvider::getName)
            .collect(Collectors.toList());
    }

    public static AudioBackend getAudioBackend() {
        // Ensure backend is current with preferences if changed externally
        Optional<BackendProvider> pref = BackendDiscovery.getInstance()
            .getPreferredProvider(BackendDiscovery.TYPE_AUDIO);
            
        if (pref.isPresent() && (currentBackend == null || !currentBackend.getBackendName().equals(pref.get().getName()))) {
            // This check is a bit loose (Name vs ID), but BackendProvider mismatch implies change.
            // Ideally we check ID. 
            // For now, if preference exists, try to load it. 
            // But createBackend() creates a NEW instance. reusing singletons?
            // The BackendProvider.createBackend() usually creates new.
            // If we want a singleton audio engine, we should manage it here.
             selectBackend(pref.get().getId());
        }
        
        if (currentBackend == null) refresh();
        return currentBackend;
    }

    public static void selectBackend(String backendNameOrId) {
        // Try finding by Name first (for UI compat) then ID
        Optional<BackendProvider> provider = BackendDiscovery.getInstance().getProvidersByType(BackendDiscovery.TYPE_AUDIO)
            .stream()
            .filter(p -> p.getName().equals(backendNameOrId) || p.getId().equals(backendNameOrId))
            .findFirst();

        if (provider.isPresent()) {
            try {
                Object backendObj = provider.get().createBackend();
                if (backendObj instanceof AudioBackend) {
                    currentBackend = (AudioBackend) backendObj;
                    // Update preference if successful
                    BackendDiscovery.getInstance().setPreferredProvider(BackendDiscovery.TYPE_AUDIO, provider.get().getId());
                    System.out.println("Audio Backend switched to: " + currentBackend.getBackendName());
                }
            } catch (Exception e) {
                System.err.println("Failed to initialize backend: " + backendNameOrId);
                e.printStackTrace();
            }
        }
    }
}

