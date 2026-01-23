/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media;

import org.jscience.media.backends.JavaSoundBackend;
import org.jscience.technical.backend.BackendDiscovery;
import org.jscience.technical.backend.BackendProvider;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Manager for Audio Backends.
 * Discovers backends implementing BackendProvider with type="audio".
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
