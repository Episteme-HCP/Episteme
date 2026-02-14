/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.audio;

import org.jscience.core.technical.algorithm.AlgorithmProvider;
import com.google.auto.service.AutoService;

/**
 * TarsosDSP Audio Processing Provider.
 * Wraps TarsosDSP library for audio analysis and synthesis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class TarsosAudioProvider implements AlgorithmProvider {

    private boolean available;

    public TarsosAudioProvider() {
        try {
            Class.forName("be.tarsos.dsp.AudioDispatcher");
            available = true;
        } catch (ClassNotFoundException e) {
            available = false;
        }
    }

    @Override
    public String getName() {
        return "TarsosDSP (Wrapper)";
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
