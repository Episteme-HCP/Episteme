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

package org.jscience.media;

import org.jscience.media.backends.*;
import java.util.*;

/**
 * Factory and Registry for Audio Engines.
 * Uses ServiceLoader (SPI) for discovery.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
