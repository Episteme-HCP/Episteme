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

package org.jscience.ui.computing.ai;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.jscience.computing.ai.automata.ConwayLife;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.Parameter;
import org.jscience.ui.ScientificViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Game of Life Viewer refactored to the new architecture.
 * Visualizes Cellular Automata with consistent parameter controls.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GameOfLifeViewer extends VBox implements ScientificViewer, Simulatable {

    // Base constants for the simulation logic
    private static final int WIDTH = 160;
    private static final int HEIGHT = 100;

    private ConwayLife life;
    private Canvas canvas;
    private boolean playing = false;
    private long lastUpdate = 0;
    private double simulationSpeed = 1.0;
    private double initialDensity = 0.2;
    private Color cellColor = Color.LIME;

    private final List<Parameter<?>> parameters = new ArrayList<>();
    private AnimationTimer timer;

    public GameOfLifeViewer() {
        super(10);
        setPadding(new Insets(10));
        getStyleClass().add("dark-viewer-root");

        life = new ConwayLife(WIDTH, HEIGHT);
        setupParameters();
        buildUI();
        setupAnimation();

        randomize();
    }

    private void setupParameters() {
        parameters.add(new NumericParameter(
                I18n.getInstance().get("life.param.density", "Initial Density"),
                "Density of alive cells when randomizing",
                0.01, 1.0, 0.05, 0.2,
                val -> this.initialDensity = val));

        parameters.add(new Parameter<Color>(
                I18n.getInstance().get("life.param.color", "Cell Color"),
                "Color of the alive cells",
                Color.LIME,
                val -> {
                    this.cellColor = val;
                    draw();
                }));
    }

    private void buildUI() {
        // Initial size, will be bound later
        canvas = new Canvas(WIDTH * 5, HEIGHT * 5);

        // Ensure it fills the container
        canvas.widthProperty().bind(this.widthProperty().subtract(20));
        canvas.heightProperty().bind(this.heightProperty().subtract(20));

        // Redraw when Canvas is resized
        canvas.widthProperty().addListener(obs -> draw());
        canvas.heightProperty().addListener(obs -> draw());

        canvas.setOnMouseClicked(e -> {
            double cellW = canvas.getWidth() / WIDTH;
            double cellH = canvas.getHeight() / HEIGHT;
            int x = (int) (e.getX() / cellW);
            int y = (int) (e.getY() / cellH);
            if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                life.setState(x, y, !life.getState(x, y));
                draw();
            }
        });

        canvas.setOnMouseDragged(e -> {
            double cellW = canvas.getWidth() / WIDTH;
            double cellH = canvas.getHeight() / HEIGHT;
            int x = (int) (e.getX() / cellW);
            int y = (int) (e.getY() / cellH);
            if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                life.setState(x, y, true);
                draw();
            }
        });

        getChildren().add(canvas);
    }

    private void setupAnimation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!playing)
                    return;

                // Speed factor: 1.0 = ~460ms interval, 5.0 = fast, 0.1 = slow
                // Baseline: 500ms / speed
                long intervalNs = (long) ((500.0 / simulationSpeed) * 1_000_000);

                if (now - lastUpdate > intervalNs) {
                    life.nextGeneration();
                    draw();
                    lastUpdate = now;
                }
            }
        };
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        if (w <= 0 || h <= 0)
            return;

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w, h);

        double cellW = w / WIDTH;
        double cellH = h / HEIGHT;

        gc.setFill(cellColor);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (life.getState(x, y)) {
                    gc.fillRect(x * cellW, y * cellH, cellW - 0.5, cellH - 0.5);
                }
            }
        }
    }

    @Override
    public void play() {
        this.playing = true;
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
        life.nextGeneration();
        draw();
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
        life = new ConwayLife(WIDTH, HEIGHT);
        randomize();
    }

    @Override
    public boolean isRunning() {
        return playing;
    }

    @Override
    public List<Parameter<?>> getParameters() {
        return parameters;
    }

    private void randomize() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                life.setState(x, y, Math.random() < initialDensity);
            }
        }
        draw();
    }
}
