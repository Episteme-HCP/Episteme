/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.media;

import org.episteme.core.technical.backend.BackendDiscovery;
import org.episteme.core.technical.backend.Backend;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * System manager for Video Backends.
 */
public class VideoBackendSystem {

    private static VideoBackend currentBackend;

    static {
        refresh();
    }

    public static void refresh() {
        Optional<Backend> provider = BackendDiscovery.getInstance()
            .getPreferredProvider(BackendDiscovery.TYPE_VIDEO);
        
        if (provider.isPresent()) {
            selectBackend(provider.get().getId());
        }
    }

    public static List<String> getAvailableBackends() {
        return BackendDiscovery.getInstance()
            .getAvailableProvidersByType(BackendDiscovery.TYPE_VIDEO)
            .stream()
            .map(Backend::getName)
            .collect(Collectors.toList());
    }

    public static VideoBackend getVideoBackend() {
        Optional<Backend> pref = BackendDiscovery.getInstance()
            .getPreferredProvider(BackendDiscovery.TYPE_VIDEO);
            
        if (pref.isPresent() && (currentBackend == null || !currentBackend.getBackendName().equals(pref.get().getName()))) {
             selectBackend(pref.get().getId());
        }
        
        if (currentBackend == null) refresh();
        return currentBackend;
    }

    public static void selectBackend(String backendNameOrId) {
        Optional<Backend> provider = BackendDiscovery.getInstance().getProvidersByType(BackendDiscovery.TYPE_VIDEO)
            .stream()
            .filter(p -> p.getName().equals(backendNameOrId) || p.getId().equals(backendNameOrId))
            .findFirst();

        if (provider.isPresent()) {
            try {
                Object backendObj = provider.get().createBackend();
                if (backendObj instanceof VideoBackend) {
                    currentBackend = (VideoBackend) backendObj;
                    BackendDiscovery.getInstance().setPreferredProvider(BackendDiscovery.TYPE_VIDEO, provider.get().getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
