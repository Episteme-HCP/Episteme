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

package org.jscience.ui.demos;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.viewers.physics.classical.waves.SpectrographViewer;

import java.io.File;

/**
 * Spectrograph Demo.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpectrographDemo extends AbstractSimulationDemo {

    private SpectrographViewer viewer;
    private double[] allSamples;
    private int playhead = 0;
    private String currentPattern = "Voice";
    private double time = 0;
    private AnimationTimer demoTimer;

    @Override public boolean isDemo() { return true; }
    @Override public String getCategory() { return I18n.getInstance().get("category.physics", "Physics"); }
    @Override public String getName() { return I18n.getInstance().get("demo.spectrographdemo.name", "Spectrograph"); }
    @Override public String getDescription() { return I18n.getInstance().get("demo.spectrographdemo.desc", "Real-time frequency analysis visualization."); }
    @Override public String getLongDescription() { 
        return I18n.getInstance().get("demo.spectrographdemo.longdesc", "Visualizes sound or signal frequencies over time using a spectrogram and spectrum analyzer."); 
    }

    @Override
    public Node createViewerNode() {
        if (viewer == null) {
            viewer = new SpectrographViewer();
            setupSignalLoop();
        }
        return viewer;
    }

    @Override
    public VBox createControlPanel() {
        VBox panel = super.createControlPanel();
        panel.getChildren().add(new Separator());

        Label srcLbl = new Label(I18n.getInstance().get("demo.spectrographdemo.label.source", "Signal Source:"));
        ComboBox<String> sourceCombo = new ComboBox<>();
        sourceCombo.getItems().addAll(
                I18n.getInstance().get("demo.spectrographdemo.source.voice", "Voice (Synthetic)"),
                I18n.getInstance().get("demo.spectrographdemo.source.sine", "Sine Wave (Synthetic)"),
                I18n.getInstance().get("demo.spectrographdemo.source.noise", "White Noise"),
                I18n.getInstance().get("demo.spectrographdemo.source.custom", "Custom WAV..."));
        sourceCombo.setValue(I18n.getInstance().get("demo.spectrographdemo.source.voice", "Voice (Synthetic)"));
        sourceCombo.setMaxWidth(Double.MAX_VALUE);
        sourceCombo.setOnAction(e -> {
            String val = sourceCombo.getValue();
            if (val.equals("Custom WAV...")) {
                loadWavFile();
            } else {
                currentPattern = val;
                allSamples = null; // Use synthetic
            }
        });

        Button loadBtn = new Button(I18n.getInstance().get("demo.spectrographdemo.btn.load", "Load WAV File"));
        loadBtn.setMaxWidth(Double.MAX_VALUE);
        loadBtn.setOnAction(e -> loadWavFile());

        panel.getChildren().addAll(srcLbl, sourceCombo, loadBtn);
        return panel;
    }

    private void setupSignalLoop() {
        demoTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (viewer != null && viewer.isPlaying()) {
                    double[] chunk = getNextChunk(256); // 256 samples for FFT
                    viewer.feedSamples(chunk);
                    time += 0.05;
                }
            }
        };
        demoTimer.start();
    }

    private double[] getNextChunk(int size) {
        double[] chunk = new double[size];
        if (allSamples != null) {
            // Replay from loaded file
            for (int i = 0; i < size; i++) {
                chunk[i] = allSamples[(playhead + i) % allSamples.length];
            }
            playhead = (playhead + size) % allSamples.length;
        } else {
            // Synthetic generation
            for (int i = 0; i < size; i++) {
                double t = time + (double)i / size;
                if (currentPattern.contains("Voice")) {
                    chunk[i] = 0.5 * Math.sin(t * 10 * 2 * Math.PI) + 0.3 * Math.sin(t * 25 * 2 * Math.PI) + 0.1 * Math.random();
                } else if (currentPattern.contains("Sine")) {
                    double freq = 10 + 5 * Math.sin(time);
                    chunk[i] = Math.sin(t * freq * 2 * Math.PI);
                } else if (currentPattern.contains("Noise")) {
                    chunk[i] = Math.random() * 2 - 1;
                }
            }
        }
        return chunk;
    }

    private void loadWavFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV Files", "*.wav"));
        File file = fc.showOpenDialog(null);
        if (file != null) {
            try {
                org.jscience.ui.viewers.physics.classical.waves.AudioAnalyzer analyzer = new org.jscience.ui.viewers.physics.classical.waves.AudioAnalyzer(file);
                allSamples = analyzer.getAudioData();
                playhead = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
