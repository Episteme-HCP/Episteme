/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.audio;

import org.jscience.core.technical.algorithm.AlgorithmProvider;
import com.google.auto.service.AutoService;

/**
 * Minim Audio Processing Provider.
 * Wraps Minim library for audio synthesis and analysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class MinimAudioProvider implements AlgorithmProvider {

    private boolean available;

    public MinimAudioProvider() {
        try {
            Class.forName("ddf.minim.Minim");
            available = true;
        } catch (ClassNotFoundException e) {
            available = false;
        }
    }

    @Override
    public String getName() {
        return "Minim (Wrapper)";
    }

    @Override
    public String getAlgorithmType() {
        return "Audio Processing";
    }

    @Override
    public boolean isAvailable() {
        return available;
    }
}
