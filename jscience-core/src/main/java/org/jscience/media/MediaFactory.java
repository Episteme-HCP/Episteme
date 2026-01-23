/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media;

import org.jscience.media.backends.*;
import java.util.*;

/**
 * Factory and Registry for Audio Engines.
 * Uses ServiceLoader (SPI) for discovery.
 */
public class MediaFactory {
    
    private static final Map<String, AudioEngine> backends = new LinkedHashMap<>();
    private static AudioEngine currentEngine;

    static {
        refresh();
        // Default
        if (backends.containsKey("Standard JavaSound")) {
            currentEngine = backends.get("Standard JavaSound");
        } else if (!backends.isEmpty()) {
            currentEngine = backends.values().iterator().next();
        }
    }

    public static void refresh() {
        backends.clear();
        ServiceLoader<AudioEngine> loader = ServiceLoader.load(AudioEngine.class);
        for (AudioEngine engine : loader) {
             backends.put(engine.getBackendName(), engine);
             System.out.println("Discovered Audio Backend: " + engine.getBackendName());
        }
    }

    public static void registerBackend(AudioEngine engine) {
        backends.put(engine.getBackendName(), engine);
    }

    public static Collection<String> getAvailableBackends() {
        return backends.keySet();
    }

    public static void setBackend(String name) {
        if (backends.containsKey(name)) {
            currentEngine = backends.get(name);
            System.out.println("Audio Backend switched to: " + name);
        }
    }

    public static AudioEngine getAudioEngine() {
        if (currentEngine == null) {
            refresh(); 
            if (currentEngine == null) {
                // Fallback if SPI failed
                currentEngine = new JavaSoundEngine();
            }
        }
        return currentEngine;
    }
}
