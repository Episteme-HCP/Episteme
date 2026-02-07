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

package org.jscience.core.ui.viewers.media;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jscience.core.ui.AbstractViewer;
import org.jscience.core.ui.Parameter;
import com.google.auto.service.AutoService;
import org.jscience.core.ui.Viewer;

import org.jscience.core.ui.BooleanParameter;
import org.jscience.core.media.AudioBackend;
import org.jscience.core.media.AudioBackendSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Premium Audio Visualization Tool.
 * Demonstrates wave physics and FFT spectral analysis using pluggable backends.
 */
@AutoService(Viewer.class)
public final class AudioViewer extends AbstractViewer {

    private final Canvas canvas = new Canvas(800, 400);
    private final Label statusLabel = new Label("Ready");
    
    // Abstracted Backend
    private AudioBackend backend;
    private boolean isPlaying = false;
    
    // Visualization Settings
    private String colorPalette = "Neon";
    private boolean showSpectrogram = true;

    public AudioViewer() {
        backend = AudioBackendSystem.getAudioBackend();

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #111;");

        Label title = new Label("QUANTUM AUDIO ANALYZER");
        title.setStyle("-fx-text-fill: #00ffcc; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Controls
        HBox controls = new HBox(10);
        Button btnPlay = new Button("Play");
        Button btnPause = new Button("Pause");
        Button btnStop = new Button("Stop");
        
        btnPlay.setOnAction(e -> { 
            // Re-fetch backend in case it changed
            backend = AudioBackendSystem.getAudioBackend();
            if (backend != null) {
                backend.play(); 
                isPlaying = true; 
                statusLabel.setText("Playing (" + backend.getBackendName() + ")");
            }
        });
        btnPause.setOnAction(e -> { 
            if (backend != null) backend.pause(); 
            isPlaying = false; 
            statusLabel.setText("Paused"); 
        });
        btnStop.setOnAction(e -> { 
            if (backend != null) backend.stop(); 
            isPlaying = false; 
            statusLabel.setText("Stopped"); 
        });
        
        controls.getChildren().addAll(btnPlay, btnPause, btnStop);

        // Visualization Loop
        new AnimationTimer() {
            @Override public void handle(long now) {
                // Keep backend sync
                backend = AudioBackendSystem.getAudioBackend();
                drawVisualization();
            }
        }.start();

        statusLabel.setStyle("-fx-text-fill: #aaa;");

        root.getChildren().addAll(title, canvas, controls, statusLabel);
        setCenter(root);
        
        // Canvas resizing
        canvas.widthProperty().bind(root.widthProperty().subtract(40));
    }

    private void drawVisualization() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w, h);

        if (isPlaying && backend != null) {
            float[] spectrum = backend.getSpectrum();
            double barWidth = w / spectrum.length;
            
            for (int i = 0; i < spectrum.length; i++) {
                double mag = spectrum[i] * h; // Scale appropriately
                if (colorPalette.equals("Neon")) gc.setFill(Color.hsb(i * 360.0 / spectrum.length, 1.0, 1.0));
                else gc.setFill(Color.WHITE);
                
                if (showSpectrogram) {
                    gc.fillRect(i * barWidth, h - mag, barWidth - 1, mag);
                } else {
                    gc.fillOval(i * barWidth, h / 2 - mag / 2, barWidth, mag);
                }
            }
        }
    }
    
    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> params = new ArrayList<>();
        params.add(new BooleanParameter("Spectrogram", "Toggle Bar/Wave mode", true, v -> showSpectrogram = v));
        return params;
    }

    @Override public String getName() { return "Quantum Audio"; }
    @Override public String getCategory() { return "Physics / Waves"; }
    @Override public String getDescription() { return "Audio visualization with pluggable backends."; }
    @Override public String getLongDescription() { return "Visualizes audio frequencies using a modular engine architecture (JavaSound, Tarsos, etc.)."; }
}

