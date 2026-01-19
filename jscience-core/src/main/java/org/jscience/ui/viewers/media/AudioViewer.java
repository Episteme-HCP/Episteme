/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.media;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Parameter;

import org.jscience.ui.BooleanParameter;
import org.jscience.media.AudioBackend;
import org.jscience.media.AudioBackendSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Premium Audio Visualization Tool.
 * Demonstrates wave physics and FFT spectral analysis using pluggable backends.
 */
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
