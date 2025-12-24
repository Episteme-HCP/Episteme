/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.ui.mathematics.futurology;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.*;

/**
 * Kurzweil Time Visualization - Exponential vs Linear Time.
 * <p>
 * Demonstrates Ray Kurzweil's concept of accelerating change and the
 * technological singularity, showing how exponential time appears to
 * accelerate relative to linear (calendar) time.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class KurzweilViewer extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;

    // Time tracking
    private double realTimeSeconds = 0;
    private double kurzweilTime = 0;
    private double accelerationFactor = 2.0; // Doubling rate

    // Historical milestones (year, description)
    private static final List<Milestone> MILESTONES = List.of(
            new Milestone(-3000000, "First stone tools"),
            new Milestone(-100000, "Language development"),
            new Milestone(-10000, "Agricultural revolution"),
            new Milestone(-3000, "Writing invented"),
            new Milestone(-500, "Philosophy, mathematics"),
            new Milestone(1, "Common era begins"),
            new Milestone(1440, "Printing press"),
            new Milestone(1750, "Industrial Revolution"),
            new Milestone(1879, "Electric light bulb"),
            new Milestone(1903, "First powered flight"),
            new Milestone(1945, "First computer (ENIAC)"),
            new Milestone(1969, "Moon landing"),
            new Milestone(1971, "Microprocessor"),
            new Milestone(1989, "World Wide Web"),
            new Milestone(2007, "Smartphone revolution"),
            new Milestone(2012, "Deep learning breakthrough"),
            new Milestone(2022, "ChatGPT / Large Language Models"),
            new Milestone(2025, "Present day"),
            new Milestone(2045, "Predicted Singularity"));

    private Canvas linearCanvas;
    private Canvas logCanvas;
    private Label realTimeLabel;
    private Label kurzweilTimeLabel;
    private Label accelerationLabel;
    private boolean running = true;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0a0a1a;");

        // Title
        Label title = new Label(org.jscience.ui.i18n.I18n.getInstance().get("kurzweil.header"));
        title.setFont(Font.font("System", FontWeight.BOLD, 22));
        title.setTextFill(Color.WHITE);
        title.setPadding(new Insets(15));
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        // Main content - two timelines
        VBox centerBox = new VBox(20);
        centerBox.setPadding(new Insets(10));
        centerBox.setAlignment(Pos.CENTER);

        // Linear timeline
        VBox linearBox = new VBox(5);
        Label linearLabel = new Label("Linear (Calendar) Time");
        linearLabel.setTextFill(Color.LIGHTGRAY);
        linearLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        linearCanvas = new Canvas(WIDTH - 250, 180);
        linearBox.getChildren().addAll(linearLabel, linearCanvas);

        // Logarithmic timeline (Kurzweil perspective)
        VBox logBox = new VBox(5);
        Label logLabel = new Label("Kurzweil (Exponential) Time - Paradigm Shifts Accelerate");
        logLabel.setTextFill(Color.ORANGE);
        logLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        logCanvas = new Canvas(WIDTH - 250, 180);
        logBox.getChildren().addAll(logLabel, logCanvas);

        centerBox.getChildren().addAll(linearBox, logBox);
        root.setCenter(centerBox);

        // Right sidebar - clocks and controls
        VBox sidebar = createSidebar();
        root.setRight(sidebar);

        // Description at bottom
        TextArea description = new TextArea(
                "Ray Kurzweil's Law of Accelerating Returns states that technological progress " +
                        "is exponential, not linear. Each paradigm shift enables the next one faster.\n\n" +
                        "• Linear time: Calendar years pass at constant rate\n" +
                        "• Kurzweil time: Subjective technological progress accelerates exponentially\n" +
                        "• The Singularity: When technology changes so fast humans cannot keep up (~2045)");
        description.setWrapText(true);
        description.setEditable(false);
        description.setPrefHeight(100);
        description.setStyle("-fx-control-inner-background: #16213e; -fx-text-fill: #aaa;");
        root.setBottom(description);

        // Animation
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0)
                    lastTime = now;
                if (!running) {
                    lastTime = now;
                    return;
                }

                double deltaSeconds = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                updateTime(deltaSeconds);
                drawTimelines();
            }
        };
        timer.start();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("JScience - Kurzweil Time Visualization");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #16213e;");

        // Dual clocks
        Label clockTitle = new Label("Dual Clocks");
        clockTitle.setTextFill(Color.WHITE);
        clockTitle.setFont(Font.font("System", FontWeight.BOLD, 14));

        VBox linearClock = new VBox(5);
        Label linearClockLabel = new Label("Real Time (s):");
        linearClockLabel.setTextFill(Color.LIGHTGRAY);
        realTimeLabel = new Label("0.00");
        realTimeLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 24));
        realTimeLabel.setTextFill(Color.LIGHTGREEN);
        linearClock.getChildren().addAll(linearClockLabel, realTimeLabel);

        VBox kurzweilClock = new VBox(5);
        Label kurzweilClockLabel = new Label("Kurzweil Time:");
        kurzweilClockLabel.setTextFill(Color.LIGHTGRAY);
        kurzweilTimeLabel = new Label("0.00");
        kurzweilTimeLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 24));
        kurzweilTimeLabel.setTextFill(Color.ORANGE);
        kurzweilClock.getChildren().addAll(kurzweilClockLabel, kurzweilTimeLabel);

        accelerationLabel = new Label("Acceleration: 1.00x");
        accelerationLabel.setTextFill(Color.YELLOW);
        accelerationLabel.setFont(Font.font("System", 12));

        // Controls
        Separator sep = new Separator();

        Label controlLabel = new Label("Acceleration Base:");
        controlLabel.setTextFill(Color.WHITE);

        Slider accelSlider = new Slider(1.1, 5.0, accelerationFactor);
        accelSlider.setShowTickLabels(true);
        accelSlider.setShowTickMarks(true);
        accelSlider.setMajorTickUnit(1);
        accelSlider.valueProperty().addListener((o, ov, nv) -> {
            accelerationFactor = nv.doubleValue();
        });

        Button resetBtn = new Button("Reset");
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setOnAction(e -> {
            realTimeSeconds = 0;
            kurzweilTime = 0;
        });

        ToggleButton pauseBtn = new ToggleButton("Pause");
        pauseBtn.setMaxWidth(Double.MAX_VALUE);
        pauseBtn.setOnAction(e -> running = !pauseBtn.isSelected());

        sidebar.getChildren().addAll(
                clockTitle, linearClock, kurzweilClock, accelerationLabel,
                sep, controlLabel, accelSlider, resetBtn, pauseBtn);

        return sidebar;
    }

    private void updateTime(double deltaSeconds) {
        realTimeSeconds += deltaSeconds;

        // Kurzweil time accelerates exponentially
        double acceleration = Math.pow(accelerationFactor, realTimeSeconds / 10.0);
        kurzweilTime += deltaSeconds * acceleration;

        realTimeLabel.setText(String.format("%.2f s", realTimeSeconds));
        kurzweilTimeLabel.setText(String.format("%.2f", kurzweilTime));
        accelerationLabel.setText(String.format("Acceleration: %.2fx", acceleration));
    }

    private void drawTimelines() {
        drawLinearTimeline();
        drawLogTimeline();
    }

    private void drawLinearTimeline() {
        GraphicsContext gc = linearCanvas.getGraphicsContext2D();
        double w = linearCanvas.getWidth();
        double h = linearCanvas.getHeight();

        gc.setFill(Color.rgb(10, 10, 30));
        gc.fillRect(0, 0, w, h);

        // Timeline axis
        double y = h / 2;
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(2);
        gc.strokeLine(30, y, w - 30, y);

        // Draw milestones linearly from -3M to 2100
        double minYear = -100000;
        double maxYear = 2100;

        for (Milestone m : MILESTONES) {
            if (m.year < minYear)
                continue;
            double x = 30 + (m.year - minYear) / (maxYear - minYear) * (w - 60);

            // Event dot
            gc.setFill(m.year <= 2025 ? Color.CYAN : Color.ORANGE);
            gc.fillOval(x - 4, y - 4, 8, 8);

            // Label
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("System", 9));
            gc.save();
            gc.translate(x, y - 10);
            gc.rotate(-45);
            gc.fillText(m.label.substring(0, Math.min(20, m.label.length())), 0, 0);
            gc.restore();
        }

        // Year markers
        gc.setFill(Color.GRAY);
        gc.setFont(Font.font("System", 10));
        for (int year : List.of(-100000, -10000, 0, 1000, 1900, 2000, 2050)) {
            double x = 30 + (year - minYear) / (maxYear - minYear) * (w - 60);
            gc.fillText(year > 0 ? String.valueOf(year) : year + " BCE", x - 20, y + 25);
        }
    }

    private void drawLogTimeline() {
        GraphicsContext gc = logCanvas.getGraphicsContext2D();
        double w = logCanvas.getWidth();
        double h = logCanvas.getHeight();

        gc.setFill(Color.rgb(20, 10, 10));
        gc.fillRect(0, 0, w, h);

        // Timeline axis
        double y = h / 2;
        gc.setStroke(Color.ORANGE);
        gc.setLineWidth(2);
        gc.strokeLine(30, y, w - 30, y);

        // Logarithmic placement - recent events spread out
        double logMin = Math.log(1); // 1 year ago
        double logMax = Math.log(3000000); // 3M years ago

        int currentYear = 2025;

        for (Milestone m : MILESTONES) {
            double yearsAgo = Math.max(1, currentYear - m.year);
            double logYears = Math.log(yearsAgo);
            double x = w - 30 - (logYears - logMin) / (logMax - logMin) * (w - 60);

            // Event dot (larger for recent)
            double size = 6 + (1 - logYears / logMax) * 6;
            gc.setFill(m.year >= 2000 ? Color.YELLOW : (m.year >= 1900 ? Color.ORANGE : Color.DARKORANGE));
            gc.fillOval(x - size / 2, y - size / 2, size, size);

            // Label
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("System", 9));
            gc.save();
            gc.translate(x, y - 12);
            gc.rotate(-45);
            gc.fillText(m.label.substring(0, Math.min(18, m.label.length())), 0, 0);
            gc.restore();
        }

        // Annotations
        gc.setFill(Color.ORANGE);
        gc.setFont(Font.font("System", 11));
        gc.fillText("← More change per unit time (Singularity)", w - 250, h - 15);
        gc.fillText("Distant past →", 30, h - 15);
    }

    private static record Milestone(int year, String label) {
    }

    public static void show(Stage stage) {
        new KurzweilViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
