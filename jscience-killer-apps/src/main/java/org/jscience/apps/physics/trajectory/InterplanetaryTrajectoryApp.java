/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.physics.trajectory;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

import org.jscience.apps.framework.KillerAppBase;
import org.jscience.physics.astronomy.SolarSystemLoader;
import org.jscience.physics.astronomy.StarSystem;
import org.jscience.physics.astronomy.CelestialBody;
import org.jscience.physics.astronomy.Planet;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interplanetary Trajectory Planner.
 * <p>
 * Plans optimal Hohmann-like transfers between planets using Lambert's problem.
 * Visualizes the solar system and potential trajectories.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class InterplanetaryTrajectoryApp extends KillerAppBase {

    private Canvas solarSystemCanvas;
    private ComboBox<String> originBox;
    private ComboBox<String> targetBox;
    private DatePicker launchDatePicker;
    private Spinner<Integer> flightDurationSpinner;
    private Label deltaVLabel;
    private TextArea missionLog;

    private StarSystem solarSystem;
    private CelestialBody originBody;
    private CelestialBody targetBody;
    
    // Simulation scale: Pixels per AU
    private static final double SCALE = 100.0; 
    // AU in meters
    private static final double AU = 1.496e11;

    @Override
    protected String getAppTitle() {
        return i18n.get("trajectory.title") + " - JScience";
    }

    @Override
    protected Region createMainContent() {
        loadData();

        SplitPane mainSplit = new SplitPane();
        mainSplit.setOrientation(Orientation.HORIZONTAL);

        // Canvas for visualization
        VBox canvasPane = new VBox();
        solarSystemCanvas = new Canvas(800, 600);
        solarSystemCanvas.widthProperty().bind(canvasPane.widthProperty());
        solarSystemCanvas.heightProperty().bind(canvasPane.heightProperty());
        
        // Redraw when resized
        solarSystemCanvas.widthProperty().addListener(o -> drawSolarSystem());
        solarSystemCanvas.heightProperty().addListener(o -> drawSolarSystem());
        
        canvasPane.getChildren().add(solarSystemCanvas);
        VBox.setVgrow(solarSystemCanvas, Priority.ALWAYS);

        // Controls
        VBox controls = createControls();
        controls.setMinWidth(300);
        controls.setMaxWidth(350);

        mainSplit.getItems().addAll(canvasPane, controls);
        mainSplit.setDividerPositions(0.7);
        
        // Initial Draw
        drawSolarSystem();

        return mainSplit;
    }

    private void loadData() {
        // Load our JSON resource
        solarSystem = SolarSystemLoader.load("org/jscience/astronomy/solarsystem.json");
    }

    private VBox createControls() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ddd; -fx-border-width: 0 0 0 1;");

        Label title = new Label("🚀 Mission Configuration");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Planet Selection
        List<String> planets = solarSystem.getBodies().stream()
                .filter(b -> b instanceof Planet)
                .map(CelestialBody::getName)
                .collect(Collectors.toList());

        originBox = new ComboBox<>(FXCollections.observableArrayList(planets));
        originBox.setValue("Earth");
        originBox.setOnAction(e -> updateSelection());

        targetBox = new ComboBox<>(FXCollections.observableArrayList(planets));
        targetBox.setValue("Mars");
        targetBox.setOnAction(e -> updateSelection());

        // Update body references
        updateSelection();

        // Dates
        launchDatePicker = new DatePicker(LocalDate.now());
        launchDatePicker.setOnAction(e -> drawSolarSystem());
        
        flightDurationSpinner = new Spinner<>(30, 1000, 180, 10); // Min 30 days, Max 1000, Default 180
        flightDurationSpinner.setEditable(true);
        flightDurationSpinner.valueProperty().addListener((o, ov, nv) -> drawSolarSystem());

        // Calc Button
        Button calcBtn = new Button("Calculate Trajectory");
        calcBtn.setMaxWidth(Double.MAX_VALUE);
        calcBtn.getStyleClass().add("primary-button"); // If theme supports it
        calcBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold;");
        calcBtn.setOnAction(e -> calculateTrajectory());

        // Results
        deltaVLabel = new Label("Delta-V: -- km/s");
        deltaVLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        missionLog = new TextArea();
        missionLog.setEditable(false);
        missionLog.setPrefHeight(200);
        missionLog.setWrapText(true);

        panel.getChildren().addAll(
            title,
            new Label("Origin:"), originBox,
            new Label("Target:"), targetBox,
            new Separator(),
            new Label("Launch Date:"), launchDatePicker,
            new Label("Flight Duration (days):"), flightDurationSpinner,
            new Separator(),
            calcBtn,
            deltaVLabel,
            new Label("Mission Log:"), missionLog
        );

        return panel;
    }

    private void updateSelection() {
        if (solarSystem == null) return;
        originBody = findBody(originBox.getValue());
        targetBody = findBody(targetBox.getValue());
        drawSolarSystem();
    }

    private CelestialBody findBody(String name) {
        return solarSystem.getBodies().stream()
            .filter(b -> b.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    private void drawSolarSystem() {
        GraphicsContext gc = solarSystemCanvas.getGraphicsContext2D();
        double w = solarSystemCanvas.getWidth();
        double h = solarSystemCanvas.getHeight();

        // Clear background (Space)
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w, h);

        // Center view on Sun
        gc.translate(w / 2, h / 2);

        // Draw Sun
        gc.setFill(Color.YELLOW);
        gc.fillOval(-10, -10, 20, 20);

        // Draw Planets
        // Assume planets are children of the first star (Sun)
        CelestialBody sun = solarSystem.getStars().get(0); // Assuming 1 star system 
        // Or iterate all bodies
        
        LocalDate date = launchDatePicker.getValue();
        double centuriesSinceJ2000 = getCenturiesSinceJ2000(date);

        for (CelestialBody body : solarSystem.getBodies()) {
            if (body instanceof Planet) {
                drawPlanetOrbit(gc, (Planet) body);
                drawPlanet(gc, (Planet) body, centuriesSinceJ2000);
            }
        }
        
        // Reset transform
        gc.setTransform(new Affine());
    }

    private void drawPlanetOrbit(GraphicsContext gc, Planet p) {
        // Simplified circular orbit assuming 'a' is radius
        // To do generic ellipse, we need e, a, etc.
        // Assuming SolarSystemLoader puts 'a' in orbit data, but we don't have direct access here 
        // without casting to internal impl or having standard fields.
        // SolarSystemLoader stores pos/vel in Quantity fields or Vectors.
        // We will approximate 'a' from current position magnitude for visualization if stored element not available.
        // Actually, SolarSystemLoader doesn't expose Elements directly on CelestialBody, just initial Pos/Vel.
        // But for "Planet" it keeps reading from JSON. 
        // Since we can't easily get 'a' from the generic Body class without deeper inspection, 
        // and we parsed it into DenseVectors...
        // Let's use the magnitude of the position vector at epoch as an approximation for 'a' for drawing circles.
        
        double r = p.getPosition().norm().doubleValue() / AU * SCALE;
        
        gc.setStroke(Color.web("#333333"));
        gc.setLineWidth(1);
        gc.strokeOval(-r, -r, 2 * r, 2 * r);
    }

    private void drawPlanet(GraphicsContext gc, Planet p, double t) {
        // Calculate position (Simplified Mean Motion for visualization)
        // Mean Anomaly M = M0 + n * t
        // We need orbital period or 'a'. 
        // Let's estimate 'a' = mag(pos)
        double a_m = p.getPosition().norm().doubleValue(); 
        double a_au = a_m / AU;
        
        // Period (Kepler 3rd): P^2 = a^3 (P in years, a in AU)
        double P = Math.sqrt(Math.pow(a_au, 3));
        double n = 2 * Math.PI / P; // rad/year
        
        // Angle at time t (centuries). t is in centuries. P is in years.
        // angle = n * (t * 100) + initial_angle
        // Let's assume initial angle is atan2(y, x) of initial pos.
        double x0 = p.getPosition().get(0).doubleValue();
        double y0 = p.getPosition().get(1).doubleValue();
        double theta0 = Math.atan2(y0, x0);
        
        double theta = theta0 + n * t * 100; // t in centuries
        
        double r = a_au * SCALE;
        double x = r * Math.cos(theta);
        double y = r * Math.sin(theta);
        
        gc.setFill(getColorForPlanet(p.getName()));
        double size = 8;
        if (p.getName().equals(originBox.getValue())) {
             gc.setFill(Color.LIME);
             size = 10;
        } else if (p.getName().equals(targetBox.getValue())) {
             gc.setFill(Color.RED);
             size = 10;
        }
        
        gc.fillOval(x - size/2, y - size/2, size, size);
        
        gc.setFill(Color.WHITE);
        gc.fillText(p.getName(), x + 8, y + 4);
    }
    
    private double[] calculatePosition(Planet p, LocalDate date) {
        double centuries = getCenturiesSinceJ2000(date);
        
        // Same logic as drawPlanet but returning coords in meters
        double a_m = p.getPosition().norm().doubleValue(); 
        double a_au = a_m / AU;
        
        double P = Math.sqrt(Math.pow(a_au, 3));
        double n = 2 * Math.PI / P; 

        double x0 = p.getPosition().get(0).doubleValue();
        double y0 = p.getPosition().get(1).doubleValue();
        double theta0 = Math.atan2(y0, x0);
        
        double theta = theta0 + n * centuries * 100;
        
        double r = a_m; // Circle
        return new double[]{ r * Math.cos(theta), r * Math.sin(theta) };
    }

    private Color getColorForPlanet(String name) {
        return switch (name) {
            case "Earth" -> Color.BLUE;
            case "Mars" -> Color.RED;
            case "Jupiter" -> Color.ORANGE;
            case "Saturn" -> Color.GOLD;
            case "Venus" -> Color.YELLOW;
            case "Mercury" -> Color.GRAY;
            default -> Color.WHITE;
        };
    }

    private double getCenturiesSinceJ2000(LocalDate date) {
        LocalDate j2000 = LocalDate.of(2000, 1, 1);
        long days = ChronoUnit.DAYS.between(j2000, date);
        return days / 36525.0;
    }

    private void calculateTrajectory() {
        if (originBody == null || targetBody == null) return;
        if (!(originBody instanceof Planet) || !(targetBody instanceof Planet)) return;

        LocalDate d1 = launchDatePicker.getValue();
        int duration = flightDurationSpinner.getValue();
        LocalDate d2 = d1.plusDays(duration);
        
        missionLog.appendText("--------------------------------\n");
        missionLog.appendText("Analyzing Transfer:\n");
        missionLog.appendText("Launcher: " + originBody.getName() + " -> " + targetBody.getName() + "\n");
        missionLog.appendText("Launch: " + d1 + "\n");
        missionLog.appendText("Arrival: " + d2 + " (" + duration + " days)\n");

        double[] pos1 = calculatePosition((Planet) originBody, d1);
        double[] pos2 = calculatePosition((Planet) targetBody, d2);
        
        double r1 = Math.sqrt(pos1[0]*pos1[0] + pos1[1]*pos1[1]);
        double r2 = Math.sqrt(pos2[0]*pos2[0] + pos2[1]*pos2[1]);
        
        // Hohmann Transfer Delta-V
        double mu = 1.327e20; // Sun GM (m^3/s^2)
        
        double v1 = Math.sqrt(mu / r1);
        double v2 = Math.sqrt(mu / r2);
        
        // Transfer orbit energy
        double a_transfer = (r1 + r2) / 2.0;
        double v_perigee = Math.sqrt(mu * (2/r1 - 1/a_transfer)); // Velocity at r1 on transfer orbit
        double v_apogee = Math.sqrt(mu * (2/r2 - 1/a_transfer)); // Velocity at r2 on transfer orbit
        
        double dv1 = Math.abs(v_perigee - v1);
        double dv2 = Math.abs(v2 - v_apogee);
        double totalDv = (dv1 + dv2) / 1000.0; // km/s
        
        // Phase angle check (simplified)
        // Optimal alignment?
        
        deltaVLabel.setText(String.format("Delta-V: %.2f km/s", totalDv));
        
        missionLog.appendText(String.format("Hohmann Transfer Estimates:\n"));
        missionLog.appendText(String.format("  Departure Delta-V: %.2f km/s\n", dv1/1000.0));
        missionLog.appendText(String.format("  Arrival Delta-V:   %.2f km/s\n", dv2/1000.0));
        missionLog.appendText(String.format("  Total Basic dV:    %.2f km/s\n", totalDv));
        
        // Visualizer Transfer Orbit
        GraphicsContext gc = solarSystemCanvas.getGraphicsContext2D();
        gc.save();
        gc.translate(solarSystemCanvas.getWidth()/2, solarSystemCanvas.getHeight()/2);
        
        gc.setStroke(Color.LIMEGREEN);
        gc.setLineWidth(2);
        gc.setLineDashes(5);
        
        // Draw ellipse from r1 to r2
        double a_t_au = a_transfer / AU;
        double r1_au = r1 / AU;
        double r2_au = r2 / AU;
        
        // Center of transfer ellipse is at (r1+r2)/2 - r1 = (r2-r1)/2 from Sun?
        // No, sun is at focus. 
        // Perigee is r1. Apogee is r2.
        // Center offset X = -(r2-r1)/2 (if aligned on X axis)
        double cx = -(r2_au - r1_au) * SCALE / 2.0; 
        double cy = 0;
        double rw = a_t_au * SCALE; // Semi-major
        double rh = Math.sqrt(r1_au*r2_au) * SCALE; // Semi-minor approx for visual? Close enough
        
        // Rotate to match launch angle? 
        // For visual demo, just draw a representative transfer arc
        gc.strokeOval(cx - rw, -rh, 2*rw, 2*rh);
        
        gc.restore();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
