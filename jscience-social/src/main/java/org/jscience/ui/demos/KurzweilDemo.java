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

package org.jscience.ui.demos;

import javafx.animation.AnimationTimer;
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
import org.jscience.ui.AppProvider;

import java.util.*;

/**
 * Kurzweil Time Visualization Demo - Exponential vs Linear Time.
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
public class KurzweilDemo implements AppProvider {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;

    // Time tracking
    private double realTimeSeconds = 0;
    private double kurzweilTime = 0;
    private double accelerationFactor = 2.0;

    // Historical milestones
    private static final List<Milestone> MILESTONES = List.of(
            new Milestone(-3000000, "kurzweil.milestone.tools"),
            new Milestone(-100000, "kurzweil.milestone.language"),
            new Milestone(-10000, "kurzweil.milestone.agriculture"),
            new Milestone(-3000, "kurzweil.milestone.writing"),
            new Milestone(-500, "kurzweil.milestone.philosophy"),
            new Milestone(1, "kurzweil.milestone.common"),
            new Milestone(1440, "kurzweil.milestone.print"),
            new Milestone(1750, "kurzweil.milestone.industrial"),
            new Milestone(1879, "kurzweil.milestone.bulb"),
            new Milestone(1903, "kurzweil.milestone.flight"),
            new Milestone(1945, "kurzweil.milestone.computer"),
            new Milestone(1969, "kurzweil.milestone.moon"),
            new Milestone(1971, "kurzweil.milestone.micro"),
            new Milestone(1989, "kurzweil.milestone.www"),
            new Milestone(2007, "kurzweil.milestone.phone"),
            new Milestone(2012, "kurzweil.milestone.dl"),
            new Milestone(2022, "kurzweil.milestone.llm"),
            new Milestone(2025, "kurzweil.milestone.now"),
            new Milestone(2045, "kurzweil.milestone.singularity"));

    private Canvas linearCanvas;
    private Canvas logCanvas;
    private Label realTimeLabel;
    private Label kurzweilTimeLabel;
    private Label accelerationLabel;
    private boolean running = true;

    @Override
    public boolean isDemo() {
        return true;
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.economics", "Economics");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.desc");
    }

    @Override
    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dark-viewer-root");

        // Title
        Label title = new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.header"));
        title.setFont(Font.font("System", FontWeight.BOLD, 22));
        title.getStyleClass().add("dark-label-accent");
        title.setPadding(new Insets(15));
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        // Main content - two timelines
        VBox centerBox = new VBox(20);
        centerBox.setPadding(new Insets(10));
        centerBox.setAlignment(Pos.CENTER);

        // Linear timeline
        VBox linearBox = new VBox(5);
        Label linearLabel = new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.label.linear"));
        linearLabel.getStyleClass().add("dark-label");
        linearLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        linearCanvas = new Canvas(WIDTH - 250, 180);
        linearBox.getChildren().addAll(linearLabel, linearCanvas);

        // Logarithmic timeline
        VBox logBox = new VBox(5);
        Label logLabel = new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.label.log"));
        logLabel.getStyleClass().add("dark-label");
        logLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        logCanvas = new Canvas(WIDTH - 250, 180);
        logBox.getChildren().addAll(logLabel, logCanvas);

        centerBox.getChildren().addAll(linearBox, logBox);
        root.setCenter(centerBox);

        // Right sidebar
        VBox sidebar = createSidebar();
        root.setRight(sidebar);

        // Description at bottom
        TextArea description = new TextArea(
                org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.desc.text"));
        description.setWrapText(true);
        description.setEditable(false);
        description.setPrefHeight(100);
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
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(220);
        sidebar.getStyleClass().add("dark-viewer-sidebar");

        // Dual clocks
        Label clockTitle = new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.sidebar.clock"));
        clockTitle.getStyleClass().add("dark-label");
        clockTitle.setFont(Font.font("System", FontWeight.BOLD, 14));

        VBox linearClock = new VBox(5);
        Label linearClockLabel = new Label(
                org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.sidebar.linear"));
        linearClockLabel.setTextFill(Color.LIGHTGRAY);
        realTimeLabel = new Label("0.00");
        realTimeLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 24));
        realTimeLabel.setTextFill(Color.LIGHTGREEN);
        linearClock.getChildren().addAll(linearClockLabel, realTimeLabel);

        VBox kurzweilClock = new VBox(5);
        Label kurzweilClockLabel = new Label(
                org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.sidebar.kurzweil"));
        kurzweilClockLabel.setTextFill(Color.LIGHTGRAY);
        kurzweilTimeLabel = new Label("0.00");
        kurzweilTimeLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 24));
        kurzweilTimeLabel.setTextFill(Color.ORANGE);
        kurzweilClock.getChildren().addAll(kurzweilClockLabel, kurzweilTimeLabel);

        accelerationLabel = new Label(
                org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.sidebar.accel") + " 1.00x");
        accelerationLabel.setTextFill(Color.YELLOW);
        accelerationLabel.setFont(Font.font("System", 12));

        // Controls
        Separator sep = new Separator();

        Label controlLabel = new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.sidebar.base"));
        controlLabel.getStyleClass().add("dark-label-muted");

        Slider accelSlider = new Slider(1.1, 5.0, accelerationFactor);
        accelSlider.setShowTickLabels(true);
        accelSlider.setShowTickMarks(true);
        accelSlider.setMajorTickUnit(1);
        accelSlider.valueProperty().addListener((o, ov, nv) -> {
            accelerationFactor = nv.doubleValue();
        });

        Button resetBtn = new Button(org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.btn.reset"));
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.getStyleClass().add("accent-button-blue");
        resetBtn.setOnAction(e -> {
            realTimeSeconds = 0;
            kurzweilTime = 0;
        });

        ToggleButton pauseBtn = new ToggleButton(
                org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.btn.pause"));
        pauseBtn.setMaxWidth(Double.MAX_VALUE);
        pauseBtn.setOnAction(e -> running = !pauseBtn.isSelected());

        sidebar.getChildren().addAll(
                clockTitle, linearClock, kurzweilClock, accelerationLabel,
                sep, controlLabel, accelSlider, resetBtn, pauseBtn);

        return sidebar;
    }

    private void updateTime(double deltaSeconds) {
        realTimeSeconds += deltaSeconds;

        double acceleration = Math.pow(accelerationFactor, realTimeSeconds / 10.0);
        kurzweilTime += deltaSeconds * acceleration;

        realTimeLabel.setText(String.format("%.2f s", realTimeSeconds));
        kurzweilTimeLabel.setText(String.format("%.2f", kurzweilTime));
        accelerationLabel.setText(String.format(
                org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.sidebar.accel") + " %.2fx", acceleration));
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

        double y = h / 2;
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(2);
        gc.strokeLine(30, y, w - 30, y);

        double minYear = -100000;
        double maxYear = 2100;

        for (Milestone m : MILESTONES) {
            if (m.year < minYear)
                continue;
            double x = 30 + (m.year - minYear) / (maxYear - minYear) * (w - 60);

            gc.setFill(m.year <= 2025 ? Color.CYAN : Color.ORANGE);
            gc.fillOval(x - 4, y - 4, 8, 8);

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("System", 9));
            gc.save();
            gc.translate(x, y - 10);
            gc.rotate(-45);
            String label = org.jscience.ui.i18n.SocialI18n.getInstance().get(m.label);
            gc.fillText(label.substring(0, Math.min(20, label.length())), 0, 0);
            gc.restore();
        }

        gc.setFill(Color.GRAY);
        gc.setFont(Font.font("System", 10));
        for (int year : List.of(-100000, -10000, 0, 1000, 1900, 2000, 2050)) {
            double x = 30 + (year - minYear) / (maxYear - minYear) * (w - 60);
            gc.fillText(
                    year > 0 ? String.valueOf(year)
                            : year + " " + org.jscience.ui.i18n.I18n.getInstance().get("hist.era.bce", "BCE"),
                    x - 20, y + 25);
        }
    }

    private void drawLogTimeline() {
        GraphicsContext gc = logCanvas.getGraphicsContext2D();
        double w = logCanvas.getWidth();
        double h = logCanvas.getHeight();

        gc.setFill(Color.rgb(20, 10, 10));
        gc.fillRect(0, 0, w, h);

        double y = h / 2;
        gc.setStroke(Color.ORANGE);
        gc.setLineWidth(2);
        gc.strokeLine(30, y, w - 30, y);

        double logMin = Math.log(1);
        double logMax = Math.log(3000000);
        int currentYear = 2025;

        for (Milestone m : MILESTONES) {
            double yearsAgo = Math.max(1, currentYear - m.year);
            double logYears = Math.log(yearsAgo);
            double x = w - 30 - (logYears - logMin) / (logMax - logMin) * (w - 60);

            double size = 6 + (1 - logYears / logMax) * 6;
            gc.setFill(m.year >= 2000 ? Color.YELLOW : (m.year >= 1900 ? Color.ORANGE : Color.DARKORANGE));
            gc.fillOval(x - size / 2, y - size / 2, size, size);

            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("System", 9));
            gc.save();
            gc.translate(x, y - 12);
            gc.rotate(-45);
            String label = org.jscience.ui.i18n.SocialI18n.getInstance().get(m.label);
            gc.fillText(label.substring(0, Math.min(18, label.length())), 0, 0);
            gc.restore();
        }

        gc.setFill(Color.ORANGE);
        gc.setFont(Font.font("System", 11));
        gc.fillText(org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.chart.more"), w - 250, h - 15);
        gc.fillText(org.jscience.ui.i18n.SocialI18n.getInstance().get("kurzweil.chart.past"), 30, h - 15);
    }

    private static record Milestone(int year, String label) {
    }
}
