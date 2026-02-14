/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.video;

import org.jscience.core.technical.algorithm.AlgorithmProvider;
import com.google.auto.service.AutoService;

/**
 * VLCJ Video Processing Provider.
 * Wraps VLCJ (libVLC) for video playback and processing.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class VLCJVideoProvider implements AlgorithmProvider {

    private boolean available;

    public VLCJVideoProvider() {
        try {
            Class.forName("uk.co.caprica.vlcj.factory.MediaPlayerFactory");
            available = true;
        } catch (ClassNotFoundException e) {
            available = false;
        }
    }

    @Override
    public String getName() {
        return "Native VLCJ (Wrapper)";
    }

    @Override
    public String getAlgorithmType() {
        return "Video Processing";
    }

    @Override
    public boolean isAvailable() {
        return available;
    }
}
