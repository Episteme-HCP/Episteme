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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import org.jscience.ui.AbstractDemo;
import org.jscience.history.Timeline;
import org.jscience.history.HistoricalEvent;
import org.jscience.history.FuzzyDate;
import org.jscience.ui.viewers.history.TimelineViewer;
import org.jscience.ui.i18n.I18n;

import java.util.List;

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
public class KurzweilDemo extends AbstractDemo {

    // Time tracking
    private double realTimeSeconds = 0;
    private double kurzweilTime = 0;
    private double accelerationFactor = 2.0;

    // Viewers
    private TimelineViewer linearViewer;
    private TimelineViewer logViewer;

    // Historical milestones
    private static final List<Milestone> MILESTONES = List.of(
            new Milestone(-3000000, "demo.kurzweildemo.milestone.tools"),
            new Milestone(-100000, "demo.kurzweildemo.milestone.language"),
            new Milestone(-10000, "demo.kurzweildemo.milestone.agriculture"),
            new Milestone(-3000, "demo.kurzweildemo.milestone.writing"),
            new Milestone(-500, "demo.kurzweildemo.milestone.philosophy"),
            new Milestone(1, "demo.kurzweildemo.milestone.common"),
            new Milestone(1440, "demo.kurzweildemo.milestone.print"),
            new Milestone(1750, "demo.kurzweildemo.milestone.industrial"),
            new Milestone(1879, "demo.kurzweildemo.milestone.bulb"),
            new Milestone(1903, "demo.kurzweildemo.milestone.flight"),
            new Milestone(1945, "demo.kurzweildemo.milestone.computer"),
            new Milestone(1969, "demo.kurzweildemo.milestone.moon"),
            new Milestone(1971, "demo.kurzweildemo.milestone.micro"),
            new Milestone(1989, "demo.kurzweildemo.milestone.www"),
            new Milestone(2007, "demo.kurzweildemo.milestone.phone"),
            new Milestone(2012, "demo.kurzweildemo.milestone.dl"),
            new Milestone(2022, "demo.kurzweildemo.milestone.llm"),
            new Milestone(2025, "demo.kurzweildemo.milestone.now"),
            new Milestone(2045, "demo.kurzweildemo.milestone.singularity"));

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
        return I18n.getInstance().get("category.history", "History");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("demo.kurzweildemo.name", "Kurzweil Singularity Watch");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("demo.kurzweildemo.desc", "Exponential growth of technology vs linear time.");
    }

    @Override
    public String getLongDescription() {
        return I18n.getInstance().get("demo.kurzweildemo.longdesc", "Visualizes the accelerating pace of paradigm shifts leading to the technological singularity.");
    }

    @Override
    protected Node createViewerNode() {
        // Create Timeline Data
        Timeline timeline = new Timeline("Kurzweil History");
        for (Milestone m : MILESTONES) {
            FuzzyDate date;
            if (m.year < 0) {
                date = FuzzyDate.bce(-m.year);
            } else {
                date = FuzzyDate.of(m.year);
            }
            // Category? Default to SCIENTIFIC
            // NOTE: Key is passed as label, TimelineViewer should translate it if it's a key.
            // Using I18n here directly means the stored event has the localized string.
            // Assuming HistoricalEvent doesn't store key but display string.
            timeline.addEvent(new HistoricalEvent(I18n.getInstance().get(m.label, "Milestone"), date, HistoricalEvent.Category.SCIENTIFIC));
        }

        // Main content - two viewers
        VBox centerBox = new VBox(20);
        centerBox.setPadding(new Insets(10));
        centerBox.setAlignment(Pos.CENTER);

        // Linear timeline
        VBox linearBox = new VBox(5);
        Label linearLabel = new Label(I18n.getInstance().get("demo.kurzweildemo.label.linear", "Linear Time (Calendar)"));
        linearLabel.getStyleClass().add("header-label");

        linearViewer = new TimelineViewer();
        linearViewer.setTimeline(timeline);
        linearViewer.setLogScale(false);
        linearViewer.setPrefHeight(180);

        linearBox.getChildren().addAll(linearLabel, linearViewer);

        // Logarithmic timeline
        VBox logBox = new VBox(5);
        Label logLabel = new Label(I18n.getInstance().get("demo.kurzweildemo.label.log", "Logarithmic Time (Technological Progress)"));
        logLabel.getStyleClass().add("header-label");

        logViewer = new TimelineViewer();
        logViewer.setTimeline(timeline);
        logViewer.setLogScale(true); // Using API
        linearViewer.setPrefHeight(180);

        logBox.getChildren().addAll(logLabel, logViewer);

        centerBox.getChildren().addAll(linearBox, logBox);
        centerBox.getStyleClass().add("viewer-root");

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
            }
        };
        timer.start();

        return centerBox;
    }

    @Override
    protected VBox createControlPanel() {
        return createSidebar();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(220);
        sidebar.getStyleClass().add("viewer-sidebar");

        // Dual clocks
        // Dual clocks
        Label clockTitle = new Label(I18n.getInstance().get("demo.kurzweildemo.label.clock", "Time Dilation"));
        clockTitle.getStyleClass().add("header-label");

        VBox linearClock = new VBox(5);
        Label linearClockLabel = new Label(
                I18n.getInstance().get("demo.kurzweildemo.label.linearclock", "Linear Clock:"));
        linearClockLabel.getStyleClass().add("text-secondary");
        realTimeLabel = new Label(I18n.getInstance().get("demo.kurzweildemo.text.placeholder.zero", "0.00"));
        realTimeLabel.getStyleClass().add("font-title");
        realTimeLabel.getStyleClass().add("text-success");
        linearClock.getChildren().addAll(linearClockLabel, realTimeLabel);

        VBox kurzweilClock = new VBox(5);
        Label kurzweilClockLabel = new Label(
                I18n.getInstance().get("demo.kurzweildemo.label.kurzweilclock", "Kurzweil Clock:"));
        kurzweilClockLabel.getStyleClass().add("text-secondary");
        kurzweilTimeLabel = new Label(I18n.getInstance().get("demo.kurzweildemo.text.placeholder.zero", "0.00"));
        kurzweilTimeLabel.getStyleClass().add("font-title");
        kurzweilTimeLabel.getStyleClass().add("text-warning");
        kurzweilClock.getChildren().addAll(kurzweilClockLabel, kurzweilTimeLabel);

        accelerationLabel = new Label(
                I18n.getInstance().get("demo.kurzweildemo.label.acceleration", "Acceleration:") + " 1.00x");
        accelerationLabel.getStyleClass().add("text-highlight");
        accelerationLabel.setFont(Font.font("System", 12));

        // Controls
        Separator sep = new Separator();

        Label controlLabel = new Label(I18n.getInstance().get("demo.kurzweildemo.label.basespeed", "Base Rate:"));
        controlLabel.getStyleClass().add("description-label");

        Slider accelSlider = new Slider(1.1, 5.0, accelerationFactor);
        accelSlider.setShowTickLabels(true);
        accelSlider.setShowTickMarks(true);
        accelSlider.setMajorTickUnit(1);
        accelSlider.valueProperty().addListener((o, ov, nv) -> {
            accelerationFactor = nv.doubleValue();
        });

        Button resetBtn = new Button(I18n.getInstance().get("demo.kurzweildemo.button.reset", "Reset Sim"));
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.getStyleClass().add("accent-button-blue");
        resetBtn.setOnAction(e -> {
            realTimeSeconds = 0;
            kurzweilTime = 0;
        });

        ToggleButton pauseBtn = new ToggleButton(
                I18n.getInstance().get("demo.kurzweildemo.button.pause", "Pause / Resume"));
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
                I18n.getInstance().get("demo.kurzweildemo.label.acceleration", "Acceleration:") + " %.2fx", acceleration));
    }

    private static record Milestone(int year, String label) {
    }
}
