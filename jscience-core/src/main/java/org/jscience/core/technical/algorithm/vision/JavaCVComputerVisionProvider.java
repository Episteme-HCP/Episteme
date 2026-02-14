/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.vision;

import org.jscience.core.technical.algorithm.AlgorithmProvider;
import com.google.auto.service.AutoService;

/**
 * JavaCV Computer Vision Provider.
 * Wraps OpenCV via JavaCV for computer vision algorithms.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class JavaCVComputerVisionProvider implements AlgorithmProvider {

    private boolean available;

    public JavaCVComputerVisionProvider() {
        try {
            Class.forName("org.bytedeco.opencv.global.opencv_core");
            available = true;
        } catch (ClassNotFoundException e) {
            available = false;
        }
    }

    @Override
    public String getName() {
        return "Native JavaCV (OpenCV Wrapper)";
    }

    @Override
    public String getAlgorithmType() {
        return "Computer Vision";
    }

    @Override
    public boolean isAvailable() {
        return available;
    }
}
