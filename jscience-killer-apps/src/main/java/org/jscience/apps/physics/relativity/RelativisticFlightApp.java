/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.physics.relativity;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import org.jscience.apps.framework.KillerAppBase;
import org.jscience.physics.relativity.LorentzTransformation;
import org.jscience.mathematics.numbers.real.Real;

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
        double phi;   // Azimuthal angle (0 to 2PI)
        Color baseColor;
        double size;
        
        Star(double t, double p, Color c, double s) { 
            theta = t; phi = p; baseColor = c; size = s; 
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
        Random rand = new Random();
        for (int i = 0; i < STAR_COUNT; i++) {
            // Uniform distribution on sphere
            // theta = acos(2x - 1) is wrong for uniform sphere? 
            // z = cos(theta) is uniform -1 to 1.
            double z = rand.nextDouble() * 2 - 1;
            double theta = Math.acos(z); // 0 to PI. 0 is forward.
            double phi = rand.nextDouble() * 2 * Math.PI;
            
            // Random white/bluish colors
            Color c = Color.hsb(rand.nextDouble() * 60 + 200, rand.nextDouble() * 0.5, rand.nextDouble() * 0.5 + 0.5);
            stars.add(new Star(theta, phi, c, rand.nextDouble() * 2 + 1));
        }
    }

    private VBox createControls() {
        VBox box = new VBox(10);
        box.setMaxHeight(150);
        box.setMaxWidth(600);
        box.setStyle("-fx-background-color: rgba(30, 30, 30, 0.8); -fx-padding: 15; -fx-background-radius: 10;");
        
        Label title = new Label("🚀 Relativistic Warp Drive");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        speedLabel = new Label("Speed: 0.000 c");
        speedLabel.setTextFill(Color.CYAN);
        
        gammaLabel = new Label("γ (Time Dilation): 1.00");
        gammaLabel.setTextFill(Color.ORANGE);
        
        speedSlider = new Slider(0, 0.999, 0); // Cap at 0.999c to avoid inf
        speedSlider.setShowTickLabels(false);
        speedSlider.setShowTickMarks(false);
        speedSlider.valueProperty().addListener((o, ov, nv) -> {
            beta = nv.doubleValue();
            updateLabels();
        });
        
        HBox toggles = new HBox(20);
        toggles.setAlignment(javafx.geometry.Pos.CENTER);
        
        dopplerCheck = new CheckBox("Doppler Shift (Color)");
        dopplerCheck.setTextFill(Color.WHITE);
        dopplerCheck.setSelected(true);
        
        aberrationCheck = new CheckBox("Aberration (Beaming)");
        aberrationCheck.setTextFill(Color.WHITE);
        aberrationCheck.setSelected(true);
        
        toggles.getChildren().addAll(dopplerCheck, aberrationCheck);
        
        box.getChildren().addAll(title, speedSlider, new HBox(20, speedLabel, gammaLabel), toggles);
        return box;
    }
    
    private void updateLabels() {
        speedLabel.setText(String.format("Speed: %.3f c", beta));
        if (beta >= 1.0) beta = 0.9999;
        double gamma = 1.0 / Math.sqrt(1 - beta*beta);
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
        
        double cx = w/2;
        double cy = h/2;
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
            // Standard relativistic Doppler factor D = 1 / (gamma(1 - beta cos(theta_observer)))
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
                if (dopplerCheck.isSelected()) s *= (gamma/2.0); // Stretch stars?
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
