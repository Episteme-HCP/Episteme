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

package org.jscience.ui.viewers.physics.classical.mechanics;

import org.jscience.ui.i18n.I18n;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Mechanics Viewer.
 * Visualizes a Mass-Spring-Damper system.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MechanicsViewer extends Application {

    private double mass = 5.0; // kg
    private double springConstant = 10.0; // N/m (Spring constant)
    private double damping = 0.5; // Ns/m (Damping)

    private double position = 100; // Displacement from equilibrium (pixels)
    private double velocity = 0;

    private Canvas canvas;
    private Label energyLabel; // Declared energyLabel

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dark-viewer-root");
        canvas = new Canvas(400, 600);
        root.setCenter(canvas);

        // Controls
        VBox controls = new VBox(10);
        controls.setPadding(new Insets(10));
        controls.setPrefWidth(200); // Added to maintain layout
        controls.getStyleClass().add("dark-viewer-controls");

        // Title
        Label title = new Label(I18n.getInstance().get("mechanics.title"));
        title.getStyleClass().add("dark-label-accent");

        // Mass Control
        Label massLabel = new Label(I18n.getInstance().get("mechanics.mass"));
        massLabel.setTextFill(Color.WHITE);
        Slider massSlider = new Slider(0.1, 10.0, 1.0);
        massSlider.setShowTickLabels(true);
        massSlider.setShowTickMarks(true);
        massSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            mass = newVal.doubleValue();
        });

        // Spring Constant Control
        Label kLabel = new Label(I18n.getInstance().get("mechanics.spring"));
        kLabel.setTextFill(Color.WHITE);
        Slider kSlider = new Slider(0.1, 50.0, 10.0);
        kSlider.setShowTickLabels(true);
        kSlider.setShowTickMarks(true);
        kSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            springConstant = newVal.doubleValue();
        });

        // Damping Control
        Label cLabel = new Label(I18n.getInstance().get("mechanics.damping"));
        cLabel.setTextFill(Color.WHITE);
        Slider cSlider = new Slider(0.0, 2.0, 0.1);
        cSlider.setShowTickLabels(true);
        cSlider.setShowTickMarks(true);
        cSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            damping = newVal.doubleValue();
        });

        // Energy Monitor
        energyLabel = new Label(I18n.getInstance().get("mechanics.energy"));
        energyLabel.setTextFill(Color.YELLOW);
        energyLabel.setFont(Font.font("Monospaced", 16));

        controls.getChildren().addAll(
                title, new Separator(), // Added Separator import
                massLabel, massSlider,
                kLabel, kSlider,
                cLabel, cSlider,
                energyLabel);
        root.setRight(controls);

        canvas.setOnMouseDragged(e -> {
            // Drag mass
            position = e.getY() - 300; // 300 is equilibrium Y
            velocity = 0;
        });

        new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dt = (now - last) / 1e9;
                last = now;
                update(dt);
                draw();
            }
        }.start();

        Scene scene = new Scene(root, 600, 600);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("viewer.mechanics"));
        stage.setScene(scene);
        stage.show();
    }

    private void update(double dt) {
        // Stage 1: Spring Mass
        double force = -springConstant * position - damping * velocity;
        double a = force / mass;
        velocity += a * dt * 50;
        position += velocity * dt * 50;

        // Stage 2: Ball Trigger (if mass hits platform at Y=200 relative to eq)
        if (position > 150 && !ballTriggered) {
            ballTriggered = true;
            // Impulse to ball
            ballVx = 5;
        }

        // Stage 3: Ball Physics
        if (ballTriggered) {
            ballX += ballVx * dt * 50;
            // Rolling friction?

            // Check Domino Collision
            if (ballX > 300 && !dominoFallen) {
                dominoAngle += 5 * dt * 50; // Topple
                if (dominoAngle > 90) {
                    dominoFallen = true;
                    dominoAngle = 90;
                    // Reset? Or show msg
                }
            }
        }
    }

    // Goldberg State
    private boolean ballTriggered = false;
    private double ballX = 50;
    private double ballY = 400; // Platform level
    private double ballVx = 0;

    private boolean dominoFallen = false;
    private double dominoAngle = 0;

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.web("#1a1a2e"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double centerX = 150; // Shifted left
        double anchorY = 50;
        double eqY = 250;
        double massY = eqY + position;

        // Draw Ceiling
        gc.setLineWidth(4);
        gc.setStroke(Color.LIGHTGRAY);
        gc.strokeLine(centerX - 50, anchorY, centerX + 50, anchorY);

        // Draw Spring
        gc.setLineWidth(2);
        gc.setStroke(Color.web("#aaa"));
        int segments = 15;
        double segHeight = (massY - anchorY) / segments;
        double w = 20;
        double px = centerX;
        double py = anchorY;
        for (int i = 0; i < segments; i++) {
            double ny = py + segHeight;
            double nx = centerX + ((i % 2 == 0) ? w : -w);
            if (i == segments - 1)
                nx = centerX;
            gc.strokeLine(px, py, nx, ny);
            px = nx;
            py = ny;
        }

        // Draw Mass
        gc.setFill(Color.RED);
        gc.fillRect(centerX - 20, massY, 40, 40);
        gc.setFill(Color.WHITE);
        gc.fillText(String.format("%.1f kg", mass), centerX - 15, massY + 25);

        // Draw Platform for Ball
        gc.setFill(Color.web("#555"));
        gc.fillRect(20, ballY + 20, 350, 10);

        // Draw Trigger Plate
        gc.setFill(Color.ORANGE);
        gc.fillRect(centerX - 30, ballY + 15, 60, 5); // Target for mass

        // Draw Ball
        gc.setFill(Color.BLUE);
        gc.fillOval(ballX, ballY, 20, 20);

        // Draw Domino
        gc.setFill(Color.web("#444"));
        gc.save();
        gc.translate(320, ballY + 20);
        gc.rotate(dominoAngle);
        gc.fillRect(0, -60, 10, 60);
        gc.restore();

        if (dominoFallen) {
            gc.setFill(Color.GREEN);
            gc.setFont(javafx.scene.text.Font.font(24));
            gc.fillText("SUCCESS!", 350, 200);
        }

        // Reset Button Hint
        if (dominoFallen) {
            gc.setFill(Color.GRAY);
            gc.setFont(javafx.scene.text.Font.font(12));
            gc.fillText("Drag mass up to reset... (Manual reset not impl yet)", 350, 230);
        }
    }

    public static void show(Stage stage) {
        new MechanicsViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}


