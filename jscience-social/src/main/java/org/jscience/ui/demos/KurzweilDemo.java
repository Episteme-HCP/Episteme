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
import org.jscience.ui.i18n.SocialI18n;
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
        return SocialI18n.getInstance().get("category.history", "History");
    }

    @Override
    public String getName() {
        return SocialI18n.getInstance().get("demo.kurzweildemo.name");
    }

    @Override
    public String getDescription() {
        return SocialI18n.getInstance().get("demo.kurzweildemo.desc");
    }

    @Override
    public String getLongDescription() {
        return SocialI18n.getInstance().get("demo.kurzweildemo.longdesc");
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
            timeline.addEvent(new HistoricalEvent(m.label, date, HistoricalEvent.Category.SCIENTIFIC));
        }

        // Main content - two viewers
        VBox centerBox = new VBox(20);
        centerBox.setPadding(new Insets(10));
        centerBox.setAlignment(Pos.CENTER);

        // Linear timeline
        VBox linearBox = new VBox(5);
        Label linearLabel = new Label(SocialI18n.getInstance().get("kurzweil.label.linear"));
        linearLabel.getStyleClass().add("header-label");

        linearViewer = new TimelineViewer();
        linearViewer.setTimeline(timeline);
        linearViewer.setLogScale(false);
        linearViewer.setPrefHeight(180);

        linearBox.getChildren().addAll(linearLabel, linearViewer);

        // Logarithmic timeline
        VBox logBox = new VBox(5);
        Label logLabel = new Label(SocialI18n.getInstance().get("kurzweil.label.log"));
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
        Label clockTitle = new Label(SocialI18n.getInstance().get("kurzweil.sidebar.clock"));
        clockTitle.getStyleClass().add("header-label");

        VBox linearClock = new VBox(5);
        Label linearClockLabel = new Label(
                SocialI18n.getInstance().get("kurzweil.sidebar.linear"));
        linearClockLabel.getStyleClass().add("text-secondary");
        realTimeLabel = new Label(I18n.getInstance().get("generated.kurzweil.000", "0.00"));
        realTimeLabel.getStyleClass().add("font-title");
        realTimeLabel.getStyleClass().add("text-success");
        linearClock.getChildren().addAll(linearClockLabel, realTimeLabel);

        VBox kurzweilClock = new VBox(5);
        Label kurzweilClockLabel = new Label(
                SocialI18n.getInstance().get("kurzweil.sidebar.kurzweil"));
        kurzweilClockLabel.getStyleClass().add("text-secondary");
        kurzweilTimeLabel = new Label(I18n.getInstance().get("generated.kurzweil.000", "0.00"));
        kurzweilTimeLabel.getStyleClass().add("font-title");
        kurzweilTimeLabel.getStyleClass().add("text-warning");
        kurzweilClock.getChildren().addAll(kurzweilClockLabel, kurzweilTimeLabel);

        accelerationLabel = new Label(
                SocialI18n.getInstance().get("kurzweil.sidebar.accel") + " 1.00x");
        accelerationLabel.getStyleClass().add("text-highlight");
        accelerationLabel.setFont(Font.font("System", 12));

        // Controls
        Separator sep = new Separator();

        Label controlLabel = new Label(SocialI18n.getInstance().get("kurzweil.sidebar.base"));
        controlLabel.getStyleClass().add("description-label");

        Slider accelSlider = new Slider(1.1, 5.0, accelerationFactor);
        accelSlider.setShowTickLabels(true);
        accelSlider.setShowTickMarks(true);
        accelSlider.setMajorTickUnit(1);
        accelSlider.valueProperty().addListener((o, ov, nv) -> {
            accelerationFactor = nv.doubleValue();
        });

        Button resetBtn = new Button(SocialI18n.getInstance().get("kurzweil.btn.reset"));
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.getStyleClass().add("accent-button-blue");
        resetBtn.setOnAction(e -> {
            realTimeSeconds = 0;
            kurzweilTime = 0;
        });

        ToggleButton pauseBtn = new ToggleButton(
                SocialI18n.getInstance().get("kurzweil.btn.pause"));
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
                SocialI18n.getInstance().get("kurzweil.sidebar.accel") + " %.2fx", acceleration));
    }

    private static record Milestone(int year, String label) {
    }
}