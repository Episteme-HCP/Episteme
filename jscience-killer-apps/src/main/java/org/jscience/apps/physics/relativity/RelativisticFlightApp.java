/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.physics.relativity;

import javafx.animation.AnimationTimer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import org.jscience.apps.framework.KillerAppBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Relativistic Flight Simulator.
 * <p>
 * Visualizes the effects of traveling near the speed of light.
 * Demonstraits Relativistic Aberration (searchlight effect) and Doppler Shift.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RelativisticFlightApp extends KillerAppBase {

    private Canvas spaceCanvas;
    private Slider speedSlider;
    private Label speedLabel;
    private Label gammaLabel;
    private CheckBox dopplerCheck;
    private CheckBox aberrationCheck;

    // Starfield
    private static class Star {
        double theta; // Angle from forward direction (0 to PI)
        double phi; // Azimuthal angle (0 to 2PI)
        Color baseColor;
        double size;

        Star(double t, double p, Color c, double s) {
            theta = t;
            phi = p;
            baseColor = c;
            size = s;
        }
    }

    private List<Star> stars = new ArrayList<>();
    private static final int STAR_COUNT = 2000;

    // Simulation
    private double beta = 0.0; // v/c

    @Override
    protected String getAppTitle() {
        return i18n.get("relativity.title") + " - JScience";
    }

    @Override
    protected Region createMainContent() {
        initStars();

        StackPane root = new StackPane();

        // Canton
        spaceCanvas = new Canvas(800, 600);
        root.getChildren().add(spaceCanvas);

        // Overlay Controls
        VBox controls = createControls();
        root.getChildren().add(controls);
        StackPane.setAlignment(controls, javafx.geometry.Pos.BOTTOM_CENTER);

        // Re-size
        spaceCanvas.widthProperty().bind(root.widthProperty());
        spaceCanvas.heightProperty().bind(root.heightProperty());

        startLoop();

        return root;
    }

    private void initStars() {
        // 1. Load Real Stars
        try (var is = getClass().getResourceAsStream("data/stars.csv")) {
            if (is != null) {
                List<org.jscience.physics.astronomy.loaders.StarLoader.Star> realStars = org.jscience.physics.astronomy.loaders.StarLoader
                        .loadCSV(is);

                for (org.jscience.physics.astronomy.loaders.StarLoader.Star s : realStars) {
                    // Convert RA/Dec to Theta/Phi
                    // RA 0-360 -> Phi 0-2PI
                    double phi = Math.toRadians(s.ra);
                    // Dec +90 to -90 -> Theta 0 to PI
                    double theta = Math.toRadians(90 - s.dec);

                    // Color based on Spectral Type
                    Color c = getColorForSpectralType(s.spectralType);

                    // Size based on magnitude (smaller mag = brighter/larger)
                    // limit size between 2 and 6
                    double size = Math.max(2, 6 - s.mag);

                    stars.add(new Star(theta, phi, c, size));
                }
                // System.out.println("Loaded " + realStars.size() + " real stars.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Fill background with random faint stars
        Random rand = new Random();
        for (int i = 0; i < STAR_COUNT; i++) {
            double z = rand.nextDouble() * 2 - 1;
            double theta = Math.acos(z);
            double phi = rand.nextDouble() * 2 * Math.PI;

            Color c = Color.rgb(200, 200, 255, rand.nextDouble() * 0.5 + 0.1); // Faint
            stars.add(new Star(theta, phi, c, rand.nextDouble() * 1.5 + 0.5));
        }
    }

    private Color getColorForSpectralType(String type) {
        if (type == null)
            return Color.WHITE;
        if (type.startsWith("O"))
            return Color.web("#9bb0ff"); // Blue
        if (type.startsWith("B"))
            return Color.web("#aabfff"); // Blue-white
        if (type.startsWith("A"))
            return Color.web("#cad7ff"); // White
        if (type.startsWith("F"))
            return Color.web("#f8f7ff"); // Yellow-white
        if (type.startsWith("G"))
            return Color.web("#fff4ea"); // Yellow
        if (type.startsWith("K"))
            return Color.web("#ffd2a1"); // Orange
        if (type.startsWith("M"))
            return Color.web("#ffcc6f"); // Red
        return Color.WHITE;
    }

    private VBox createControls() {
        VBox box = new VBox(10);
        box.setMaxHeight(200);
        box.setMaxWidth(700);
        box.setStyle("-fx-background-color: rgba(30, 30, 30, 0.85); -fx-padding: 15; -fx-background-radius: 10;");

        Label title = new Label("🚀 " + i18n.get("relativity.panel.warp"));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        speedLabel = new Label(i18n.get("relativity.label.speed") + ": 0.000 c");
        speedLabel.setTextFill(Color.CYAN);

        gammaLabel = new Label(i18n.get("relativity.label.gamma") + ": 1.00");
        gammaLabel.setTextFill(Color.ORANGE);

        // Time dilation visualization
        Label timeDilationTitle = new Label("⏱️ Time Dilation Clocks");
        timeDilationTitle.setTextFill(Color.LIGHTGRAY);
        timeDilationTitle.setStyle("-fx-font-weight: bold;");

        Label earthClockLabel = new Label("🌍 Earth: 1.00 year");
        earthClockLabel.setTextFill(Color.LIGHTGREEN);

        Label shipClockLabel = new Label("🛸 Ship: 1.00 year");
        shipClockLabel.setTextFill(Color.LIGHTBLUE);

        Label dilationExplanation = new Label("At this speed, 1 year on Earth = 1.00 year on ship");
        dilationExplanation.setTextFill(Color.GRAY);
        dilationExplanation.setStyle("-fx-font-size: 11px;");

        speedSlider = new Slider(0, 0.999, 0);
        speedSlider.setShowTickLabels(false);
        speedSlider.valueProperty().addListener((o, ov, nv) -> {
            beta = nv.doubleValue();
            updateLabels();
            // Update time dilation display
            double gamma = 1.0 / Math.sqrt(1 - beta * beta);
            earthClockLabel.setText(String.format("🌍 Earth: %.2f years", gamma));
            shipClockLabel.setText("🛸 Ship: 1.00 year");
            dilationExplanation
                    .setText(String.format("At %.1f%% c: 1 year on ship = %.2f years on Earth", beta * 100, gamma));
        });

        HBox toggles = new HBox(20);
        toggles.setAlignment(javafx.geometry.Pos.CENTER);

        dopplerCheck = new CheckBox(i18n.get("relativity.check.doppler"));
        dopplerCheck.setTextFill(Color.WHITE);
        dopplerCheck.setSelected(true);

        aberrationCheck = new CheckBox(i18n.get("relativity.check.aberration"));
        aberrationCheck.setTextFill(Color.WHITE);
        aberrationCheck.setSelected(true);

        toggles.getChildren().addAll(dopplerCheck, aberrationCheck);

        HBox clocks = new HBox(30, earthClockLabel, shipClockLabel);
        clocks.setAlignment(javafx.geometry.Pos.CENTER);

        box.getChildren().addAll(title, speedSlider, new HBox(20, speedLabel, gammaLabel),
                timeDilationTitle, clocks, dilationExplanation, toggles);
        return box;
    }

    private void updateLabels() {
        speedLabel.setText(String.format(i18n.get("relativity.label.speed") + ": %.3f c", beta));
        if (beta >= 1.0)
            beta = 0.9999;
        double gamma = 1.0 / Math.sqrt(1 - beta * beta);
        gammaLabel.setText(String.format("γ: %.2f", gamma));
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
        GraphicsContext gc = spaceCanvas.getGraphicsContext2D();
        double w = spaceCanvas.getWidth();
        double h = spaceCanvas.getHeight();

        // Clear background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w, h);

        double cx = w / 2;
        double cy = h / 2;
        double radius = Math.min(w, h) / 2;

        // Pre-calc gamma factors
        double gamma = 1.0 / Math.sqrt(1 - beta * beta);

        for (Star star : stars) {
            double thetaPrime = star.theta;
            Color colorPrime = star.baseColor;

            // 1. Aberration: cos(theta') = (cos(theta) + beta) / (1 + beta*cos(theta))
            if (aberrationCheck.isSelected() && beta > 0) {
                double cosTheta = Math.cos(star.theta);
                double cosThetaPrime = (cosTheta + beta) / (1 + beta * cosTheta);
                thetaPrime = Math.acos(cosThetaPrime);
            }

            // 2. Doppler Shift
            // D = 1 / (gamma * (1 - beta * cos(theta_source)))
            // Note: cos(theta) is in observer frame if using transformed angle?
            // Formula D = sqrt((1+beta)/(1-beta)) for head-on...
            // Standard relativistic Doppler factor D = 1 / (gamma(1 - beta
            // cos(theta_observer)))
            // where theta_observer is the angle we see (thetaPrime).

            if (dopplerCheck.isSelected() && beta > 0) {
                double cosTp = Math.cos(thetaPrime);
                double D = 1.0 / (gamma * (1.0 - beta * cosTp));

                // Shift Hue: Blue shift (D > 1), Red shift (D < 1)
                // Visible spectrum ~400-700nm.
                // shift factor D means frequency f' = D * f.
                // Hue on HSB is angle.
                double shift = Math.log(D); // Log scale for effect
                double newHue = star.baseColor.getHue() + shift * 100; // Arbitrary scale factor

                // Clamp/Wrap?
                // Realistically, stars shift into UV/X-ray (invisible) or IR (invisible).
                // For demo, we just color shift wildly and fade out if too extreme.

                double brightness = star.baseColor.getBrightness();
                // Headlight effect: brighter ahead
                if (aberrationCheck.isSelected()) {
                    brightness *= D * D; // Boost brightness ahead, dim behind
                }
                brightness = Math.min(1.0, Math.max(0.0, brightness));

                colorPrime = Color.hsb(newHue % 360, star.baseColor.getSaturation(), brightness);
            }

            // Project to 2D
            // Fisheye/Stereographic projection of the sphere onto the screen
            // r = f * tan(thetaPrime / 2)
            double r = radius * Math.tan(thetaPrime / 2.0);

            double x = cx + r * Math.cos(star.phi);
            double y = cy + r * Math.sin(star.phi);

            if (x >= 0 && x < w && y >= 0 && y < h) {
                gc.setFill(colorPrime);
                double s = star.size;
                if (dopplerCheck.isSelected())
                    s *= (gamma / 2.0); // Stretch stars?
                gc.fillOval(x, y, s, s);
            }
        }

        // Draw HUD overlay
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

    public static void main(String[] args) {
        launch(args);
    }
}
