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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Special Relativity Spaceflight Simulator Demo.
 * Ported from Killer App.
 */
public class RelativisticFlightDemo extends AbstractDemo {

    private Canvas spaceCanvas;
    private Slider speedSlider;
    private Label speedLabel;
    private Label gammaLabel;
    private CheckBox dopplerCheck;
    private CheckBox aberrationCheck;
    private Label earthClockLabel;
    private Label shipClockLabel;
    private Label explanationLabel;

    private static class Star {
        double theta;
        double phi;
        Color baseColor;
        double size;

        Star(double t, double p, Color c, double s) {
            theta = t;
            phi = p;
            baseColor = c;
            size = s;
        }
    }

    private final List<Star> stars = new ArrayList<>();
    private static final int STAR_COUNT = 2000;
    private double beta = 0.0; // v/c

    @Override
    public String getName() {
        return I18n.getInstance().get("relativity.title", "Vol Relativiste");
    }

    @Override
    public String getLongDescription() {
        return I18n.getInstance().get("relativity.desc",
                "Simulation de vol ÃƒÂ  vitesse relativiste montrant la dilatation temporelle et l'effet Doppler.");
    }

    @Override
    public String getCategory() { return "Physics"; }

    @Override
    public Node createViewerNode() {
        initStars();

        StackPane root = new StackPane();

        spaceCanvas = new Canvas(800, 600);
        root.getChildren().add(spaceCanvas);

        VBox controls = createOverlayControls();
        root.getChildren().add(controls);
        StackPane.setAlignment(controls, Pos.BOTTOM_CENTER);

        // Bind canvas size
        spaceCanvas.widthProperty().bind(root.widthProperty());
        spaceCanvas.heightProperty().bind(root.heightProperty());

        startLoop();

        return root;
    }

    private void initStars() {
        stars.clear();
        Random rand = new Random(42);
        for (int i = 0; i < STAR_COUNT; i++) {
            double theta = Math.acos(1 - 2 * rand.nextDouble());
            double phi = 2 * Math.PI * rand.nextDouble();
            double hue = rand.nextDouble() * 60 - 30;
            if (hue < 0)
                hue += 360;
            double saturation = rand.nextDouble() * 0.3;
            double brightness = 0.5 + rand.nextDouble() * 0.5;
            Color color = Color.hsb(hue, saturation, brightness);
            double size = 1 + rand.nextDouble() * 2;
            stars.add(new Star(theta, phi, color, size));
        }
    }

    private VBox createOverlayControls() {
        VBox box = new VBox(10);
        box.setMaxHeight(220);
        box.setMaxWidth(700);
        box.getStyleClass().add("info-panel");
        box.setStyle("-fx-background-color: rgba(253, 251, 247, 0.85); -fx-padding: 15; -fx-background-radius: 10;");

        I18n i18n = I18n.getInstance();

        Label title = new Label("Ã°Å¸Å¡â‚¬ " + i18n.get("relativity.panel.warp", "Warp Drive"));
        title.getStyleClass().add("header-label");

        speedLabel = new Label(i18n.get("relativity.label.speed_fmt", "Speed: {0}c", "0.000"));
        speedLabel.getStyleClass().add("description-label");

        gammaLabel = new Label(i18n.get("relativity.label.gamma_fmt", "Gamma: {0}", "1.00"));
        gammaLabel.getStyleClass().add("description-label");

        earthClockLabel = new Label(
                MessageFormat.format(i18n.get("relativity.clock.earth", "Earth Time: {0}s"), "1.00"));
        earthClockLabel.setTextFill(Color.LIGHTBLUE);

        shipClockLabel = new Label(MessageFormat.format(i18n.get("relativity.clock.ship", "Ship Time: {0}s"), "1.00"));
        shipClockLabel.getStyleClass().add("description-label");

        explanationLabel = new Label(
                MessageFormat.format(i18n.get("relativity.label.explanation", "At {0}% light speed..."), 0, "1.00"));
        explanationLabel.setWrapText(true);
        explanationLabel.getStyleClass().add("description-label");

        speedSlider = new Slider(0, 0.999, 0);
        speedSlider.setShowTickLabels(false);
        speedSlider.valueProperty().addListener((o, ov, nv) -> {
            beta = nv.doubleValue();
            updateLabels();
            double gamma = 1.0 / Math.sqrt(1 - beta * beta);
            gammaLabel.setText(i18n.get("relativity.label.gamma_fmt", "Gamma: {0}", String.format("%.2f", gamma)));

            double shipTime = 1.0;
            double earthTime = shipTime * gamma;

            earthClockLabel.setText(MessageFormat.format(i18n.get("relativity.clock.earth", "Earth Time: {0}s"),
                    String.format("%.2f", earthTime)));
            shipClockLabel.setText(MessageFormat.format(i18n.get("relativity.clock.ship", "Ship Time: {0}s"),
                    String.format("%.2f", shipTime)));
            explanationLabel.setText(MessageFormat.format(i18n.get("relativity.label.explanation", ""),
                    (int) (beta * 100), String.format("%.2f", earthTime)));
        });

        HBox toggles = new HBox(20);
        toggles.setAlignment(Pos.CENTER);

        dopplerCheck = new CheckBox(i18n.get("relativity.check.doppler", "Doppler Effect"));
        dopplerCheck.getStyleClass().add("description-label");
        dopplerCheck.setSelected(true);

        aberrationCheck = new CheckBox(i18n.get("relativity.check.aberration", "Relativistic Aberration"));
        aberrationCheck.getStyleClass().add("description-label");
        aberrationCheck.setSelected(true);

        toggles.getChildren().addAll(dopplerCheck, aberrationCheck);

        HBox clocks = new HBox(30, earthClockLabel, shipClockLabel);
        clocks.setAlignment(Pos.CENTER);

        box.getChildren().addAll(title, speedSlider, new HBox(20, speedLabel, gammaLabel), clocks, explanationLabel,
                toggles);
        return box;
    }

    private void updateLabels() {
        I18n i18n = I18n.getInstance();
        speedLabel.setText(i18n.get("relativity.label.speed_fmt", "Speed: {0}c", String.format("%.3f", beta)));
        if (beta >= 1.0)
            beta = 0.9999;
        double gamma = 1.0 / Math.sqrt(1 - beta * beta);
        gammaLabel.setText(i18n.get("relativity.label.gamma_fmt", "Gamma: {0}", String.format("%.2f", gamma)));
    }

    private void startLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
            }
        };
        timer.start();
    }

    private void render() {
        if (spaceCanvas == null)
            return;
        GraphicsContext gc = spaceCanvas.getGraphicsContext2D();
        double w = spaceCanvas.getWidth();
        double h = spaceCanvas.getHeight();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w, h);

        double cx = w / 2;
        double cy = h / 2;
        double radius = Math.min(w, h) / 2;
        double gamma = 1.0 / Math.sqrt(1 - beta * beta);

        for (Star star : stars) {
            double thetaPrime = star.theta;
            Color colorPrime = star.baseColor;

            if (aberrationCheck.isSelected() && beta > 0) {
                double cosTheta = Math.cos(star.theta);
                double cosThetaPrime = (cosTheta + beta) / (1 + beta * cosTheta);
                thetaPrime = Math.acos(cosThetaPrime);
            }

            if (dopplerCheck.isSelected() && beta > 0) {
                double cosTp = Math.cos(thetaPrime);
                double D = 1.0 / (gamma * (1.0 - beta * cosTp));
                double shift = Math.log(D);
                double newHue = star.baseColor.getHue() + shift * 100;
                double brightness = star.baseColor.getBrightness();
                if (aberrationCheck.isSelected()) {
                    brightness *= D * D;
                }
                brightness = Math.min(1.0, Math.max(0.0, brightness));
                colorPrime = Color.hsb(newHue % 360, star.baseColor.getSaturation(), brightness);
            }

            double r = radius * Math.tan(thetaPrime / 2.0);
            double x = cx + r * Math.cos(star.phi);
            double y = cy + r * Math.sin(star.phi);

            if (x >= 0 && x < w && y >= 0 && y < h) {
                gc.setFill(colorPrime);
                double s = star.size;
                if (dopplerCheck.isSelected())
                    s *= (gamma / 2.0);
                gc.fillOval(x, y, s, s);
            }
        }

        if (beta > 0.1) {
            gc.setStroke(Color.web("#00ff00", 0.2));
            gc.setLineWidth(2);
            gc.strokeOval(cx - 50, cy - 50, 100, 100);
            gc.strokeLine(cx - 20, cy, cx - 60, cy);
            gc.strokeLine(cx + 20, cy, cx + 60, cy);
            gc.strokeLine(cx, cy - 20, cx, cy - 60);
            gc.strokeLine(cx, cy + 20, cx, cy + 60);
        }
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("RelativisticFlightDemo.desc", "RelativisticFlightDemo description");
    }

    
}