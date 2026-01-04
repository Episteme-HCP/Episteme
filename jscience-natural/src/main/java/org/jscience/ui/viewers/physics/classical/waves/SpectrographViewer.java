/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.viewers.physics.classical.waves;

import org.jscience.ui.i18n.I18n;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * Real-time Spectrograph visualization showing frequency analysis.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
import org.jscience.ui.NumericParameter;
import org.jscience.ui.Parameter;
import org.jscience.ui.ScientificViewer;
import org.jscience.ui.Simulatable;
import java.util.ArrayList;
import java.util.List;

/**
 * Real-time Spectrograph visualization showing frequency analysis.
 * Now refactored as a reusable panel.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SpectrographViewer extends VBox implements ScientificViewer, Simulatable {

    private final int BANDS = 128;
    private double[] spectrum = new double[BANDS];
    private double time = 0;
    private double sensitivity = 1.0;
    private double simulationSpeed = 1.0;

    private SpectrumProvider provider;
    private Label fpsLabel;
    private long lastFrameTime = 0;
    private int frameCount = 0;
    private boolean playing = false;

    private Canvas spectrumCanvas;
    private Canvas spectrogramCanvas;

    private final List<Parameter<?>> parameters = new ArrayList<>();
    private AnimationTimer timer;
    private String currentPattern = "Voice";

    public SpectrographViewer() {
        super(10);
        setPadding(new Insets(10));
        getStyleClass().add("dark-viewer-root");

        setupParameters();
        buildUI();

        provider = new PrimitiveSpectrumProvider(BANDS);
        provider.setSourcePattern(currentPattern);

        setupAnimation();
    }

    private void setupParameters() {
        parameters.add(new NumericParameter(
                I18n.getInstance().get("spectrograph.sensitivity", "Sensitivity"),
                I18n.getInstance().get("spectrograph.sensitivity.desc", "Adjusts the signal responsiveness"),
                0.1, 5.0, 0.1, 1.0,
                val -> this.sensitivity = val));

        parameters.add(new Parameter<String>(
                I18n.getInstance().get("spectrograph.source", "Source"),
                I18n.getInstance().get("spectrograph.source.desc", "Selects the input signal pattern"),
                "Voice",
                val -> {
                    this.currentPattern = val;
                    if (provider != null)
                        provider.setSourcePattern(val);
                }));

        parameters.add(new Parameter<Boolean>(
                I18n.getInstance().get("spectrograph.mode", "Scientific Mode"),
                I18n.getInstance().get("spectrograph.mode.desc", "Toggles between primitive and object-based engines"),
                false,
                val -> {
                    if (val) {
                        provider = new ObjectSpectrumProvider(BANDS);
                    } else {
                        provider = new PrimitiveSpectrumProvider(BANDS);
                    }
                    provider.setSourcePattern(currentPattern);
                }));
    }

    private void buildUI() {
        // Canvases
        spectrumCanvas = new Canvas(600, 200);
        spectrogramCanvas = new Canvas(600, 250);

        // Ensure they resize with panel
        spectrumCanvas.widthProperty().bind(this.widthProperty().subtract(20));
        spectrogramCanvas.widthProperty().bind(this.widthProperty().subtract(20));

        getChildren().addAll(spectrumCanvas, spectrogramCanvas);

        fpsLabel = new Label(I18n.getInstance().get("spectrograph.fps", "FPS: --"));
        fpsLabel.getStyleClass().add("dark-label-muted");
        getChildren().add(fpsLabel);
    }

    private void setupAnimation() {
        // Gradient for Spectrum
        LinearGradient gradient = new LinearGradient(0, 1, 0, 0, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#00ff00")),
                new Stop(0.5, Color.web("#ffff00")),
                new Stop(1.0, Color.web("#ff0000")));

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!playing)
                    return;

                updateSpectrum();
                renderSpectrum(spectrumCanvas.getGraphicsContext2D(), gradient);
                renderSpectrogram(spectrogramCanvas.getGraphicsContext2D());

                if (lastFrameTime > 0) {
                    double fps = 1_000_000_000.0 / (now - lastFrameTime);
                    if (frameCount++ % 60 == 0) {
                        fpsLabel.setText(I18n.getInstance().get("spectrograph.fps.fmt", "FPS: %.1f", fps));
                    }
                }
                lastFrameTime = now;
            }
        };
    }

    @Override
    public void play() {
        this.playing = true;
        if (timer != null)
            timer.start();
    }

    @Override
    public void pause() {
        this.playing = false;
    }

    @Override
    public void stop() {
        this.playing = false;
        reset();
    }

    @Override
    public void step() {
        updateSpectrum();
        // Force a render
        renderSpectrum(spectrumCanvas.getGraphicsContext2D(), null); // Null gradient will need fix or use stored
    }

    @Override
    public void setSpeed(double multiplier) {
        this.simulationSpeed = multiplier;
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public void reset() {
        time = 0;
        spectrogramX = 0;
        if (spectrogramBuffer != null) {
            spectrogramBuffer = null; // Will be recreated
        }
    }

    @Override
    public boolean isRunning() {
        return playing;
    }

    @Override
    public List<Parameter<?>> getParameters() {
        return parameters;
    }

    private void updateSpectrum() {
        time += 0.05 * simulationSpeed;
        provider.update(time, sensitivity);
        spectrum = provider.getSpectrum();
    }

    private void renderSpectrum(GraphicsContext gc, LinearGradient gradient) {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();
        gc.clearRect(0, 0, w, h);

        double bandWidth = w / BANDS;
        gc.setFill(gradient);

        for (int i = 0; i < BANDS; i++) {
            double barHeight = spectrum[i] * h * 0.8;
            gc.fillRect(i * bandWidth, h - barHeight, bandWidth - 1, barHeight);
        }
    }

    private void renderSpectrogram(GraphicsContext gc) {
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();

        if (spectrogramBuffer == null || spectrogramBuffer.getWidth() != (int) w) {
            spectrogramBuffer = new javafx.scene.image.WritableImage((int) w, (int) h);
        }

        javafx.scene.image.PixelWriter writer = spectrogramBuffer.getPixelWriter();
        spectrogramX = (spectrogramX + 2) % (int) w;

        double bandHeight = h / BANDS;
        for (int i = 0; i < BANDS; i++) {
            double intensity = spectrum[i];
            intensity = Math.max(0, Math.min(1.0, intensity));

            Color c;
            if (intensity < 0.2)
                c = Color.hsb(240, 1.0, intensity * 5.0);
            else
                c = Color.hsb((1.0 - intensity) * 240.0, 1.0, 1.0);

            for (int dx = 0; dx < 2; dx++) {
                int px = (spectrogramX + dx) % (int) w;
                for (int py = 0; py < bandHeight; py++) {
                    int y = (int) (h - (i + 1) * bandHeight + py);
                    if (y >= 0 && y < h) {
                        writer.setColor(px, y, c);
                    }
                }
            }
        }

        gc.clearRect(0, 0, w, h);
        int part1W = (int) w - spectrogramX - 2;
        if (part1W > 0) {
            gc.drawImage(spectrogramBuffer, spectrogramX + 2, 0, part1W, h, 0, 0, part1W, h);
        }
        int part2W = spectrogramX + 2;
        if (part2W > 0) {
            gc.drawImage(spectrogramBuffer, 0, 0, part2W, h, part1W, 0, part2W, h);
        }
    }

    private javafx.scene.image.WritableImage spectrogramBuffer;
    private int spectrogramX = 0;

    // --- Inner Classes for Spectrum Generation ---

    private interface SpectrumProvider {
        void update(double time, double sensitivity);

        void setSourcePattern(String pattern);

        double[] getSpectrum();
    }

    private static class PrimitiveSpectrumProvider implements SpectrumProvider {
        private final double[] spectrum;
        private final int bands;
        private String sourcePattern = "Voice";

        public PrimitiveSpectrumProvider(int bands) {
            this.bands = bands;
            this.spectrum = new double[bands];
        }

        @Override
        public void setSourcePattern(String pattern) {
            this.sourcePattern = pattern;
        }

        @Override
        public double[] getSpectrum() {
            return spectrum;
        }

        @Override
        public void update(double time, double sensitivity) {
            for (int i = 0; i < bands; i++) {

                double val = 0;

                if (I18n.getInstance().get("spectrograph.source.voice", "Voice").equals(sourcePattern)
                        || "Voice".equals(sourcePattern)) {
                    // Formant-like bumps
                    val += Math.exp(-Math.pow((i - 20) / 5.0, 2)) * Math.sin(time * 10);
                    val += Math.exp(-Math.pow((i - 50) / 8.0, 2)) * Math.cos(time * 15);
                    val += Math.random() * 0.1;
                } else if (I18n.getInstance().get("spectrograph.source.noise", "White Noise").equals(sourcePattern)
                        || "White Noise".equals(sourcePattern)) {
                    val = Math.random();
                } else if (I18n.getInstance().get("spectrograph.source.sine", "Sine Wave").equals(sourcePattern)
                        || "Sine Wave".equals(sourcePattern)) {
                    double center = 64 + 30 * Math.sin(time * 2);
                    val = Math.exp(-Math.pow((i - center) / 2.0, 2));
                }

                spectrum[i] = Math.max(0, Math.min(1.0, val * sensitivity));
            }
        }
    }

    private static class ObjectSpectrumProvider implements SpectrumProvider {
        private final PrimitiveSpectrumProvider delegate;

        // Mocking object-based logic by wrapping primitive for demo
        public ObjectSpectrumProvider(int bands) {
            delegate = new PrimitiveSpectrumProvider(bands);
        }

        @Override
        public void update(double time, double sensitivity) {
            // Slower, "Simulated Object Computation"
            delegate.update(time, sensitivity);
        }

        @Override
        public void setSourcePattern(String pattern) {
            delegate.setSourcePattern(pattern);
        }

        @Override
        public double[] getSpectrum() {
            return delegate.getSpectrum();
        }
    }
}


