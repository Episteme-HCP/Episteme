/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.physics.classical.waves.AudioViewer;

/**
 * Audio Analysis Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AudioViewerDemo extends AbstractSimulationDemo {

    private AudioViewer viewer;

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.physics");
    }

    @Override
    public String getName() {
        return "Audio Viewer";
    }

    @Override
    public String getDescription() {
        return "Audio Spectrogram Decoder";
    }

    @Override
    protected String getLongDescription() {
        return "A demonstration of audio file processing, showing waveform and spectrogram analysis.";
    }

    @Override
    protected Node createViewerNode() {
        if (viewer == null) viewer = new AudioViewer();
        return viewer;
    }
}
