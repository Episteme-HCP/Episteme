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

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import org.jscience.physics.astronomy.CelestialBody;
import org.jscience.physics.astronomy.Planet;
import org.jscience.physics.astronomy.SolarSystemReader;
import org.jscience.physics.astronomy.StarSystem;
import org.jscience.physics.loaders.HorizonsEphemerisReader;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.i18n.I18n;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interplanetary Trajectory Planner Demo.
 * Ported from Killer App.
 */
public class InterplanetaryTrajectoryDemo extends AbstractDemo {

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

    private static final double SCALE = 100.0;
    private static final double AU = 1.496e11;

    @Override
    public String getName() {
        return I18n.getInstance().get("trajectory.title", "Interplanetary Trajectory");
    }

    @Override
    public String getLongDescription() {
        return I18n.getInstance().get("trajectory.desc", "Planning of optimal Hohmann transfers between planets.");
    }

    @Override
    public String getCategory() { return "Physics"; }

    @Override
    public Node createViewerNode() {
        loadData();

        SplitPane mainSplit = new SplitPane();
        mainSplit.setOrientation(Orientation.HORIZONTAL);

        VBox canvasPane = new VBox();
        solarSystemCanvas = new Canvas(800, 600);
        solarSystemCanvas.widthProperty().bind(canvasPane.widthProperty());
        solarSystemCanvas.heightProperty().bind(canvasPane.heightProperty());

        solarSystemCanvas.widthProperty().addListener(o -> drawSolarSystem());
        solarSystemCanvas.heightProperty().addListener(o -> drawSolarSystem());

        canvasPane.getChildren().add(solarSystemCanvas);
        VBox.setVgrow(solarSystemCanvas, Priority.ALWAYS);

        VBox controls = createControls();
        controls.setMinWidth(300);
        controls.setMaxWidth(350);

        mainSplit.getItems().addAll(canvasPane, controls);
        mainSplit.setDividerPositions(0.7);

        drawSolarSystem();

        return mainSplit;
    }

    private void loadData() {
        solarSystem = SolarSystemReader.load("org/jscience/physics/astronomy/solarsystem.json");

        try (var is = getClass().getResourceAsStream("/org/jscience/physics/astronomy/mars_horizons.txt")) {
            if (is != null) {
                HorizonsEphemerisReader.loadEphemeris(is);
            }
        } catch (Exception e) {
            // Log or handle the exception if necessary, but realEphemerisPoints is unused.
        }
    }

    private VBox createControls() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        I18n i18n = I18n.getInstance();

        Label title = new Label("🚀 " + i18n.get("trajectory.panel.config", "Configuration"));
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        List<String> planets = solarSystem.getBodies().stream()
                .filter(b -> b instanceof Planet)
                .map(CelestialBody::getName)
                .collect(Collectors.toList());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label(i18n.get("trajectory.label.origin", "Origin")), 0, 0);
        originBox = new ComboBox<>(FXCollections.observableArrayList(planets));
        originBox.setValue(i18n.get("planet.earth", "Earth"));
        originBox.setOnAction(e -> updateSelection());
        grid.add(originBox, 1, 0);

        grid.add(new Label(i18n.get("trajectory.label.target", "Target")), 0, 1);
        targetBox = new ComboBox<>(FXCollections.observableArrayList(planets));
        targetBox.setValue(i18n.get("planet.mars", "Mars"));

        targetBox.setOnAction(e -> updateSelection());
        grid.add(targetBox, 1, 1);

        grid.add(new Label(i18n.get("trajectory.label.departure", "Departure")), 0, 2);
        launchDatePicker = new DatePicker(LocalDate.now());
        launchDatePicker.setOnAction(e -> drawSolarSystem());
        grid.add(launchDatePicker, 1, 2);

        grid.add(new Label(i18n.get("trajectory.label.duration", "Duration (days)")), 0, 3);
        flightDurationSpinner = new Spinner<>(30, 1000, 180, 10); // Min 30 days, Max 1000, Default 180
        flightDurationSpinner.setEditable(true);
        flightDurationSpinner.valueProperty().addListener((o, ov, nv) -> drawSolarSystem());
        grid.add(flightDurationSpinner, 1, 3);

        Button calcBtn = new Button(i18n.get("trajectory.button.calculate", "Calculate"));
        calcBtn.setMaxWidth(Double.MAX_VALUE);
        calcBtn.getStyleClass().add("primary-button");
        calcBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold;");
        calcBtn.setOnAction(
                e -> calculateTrajectory(originBox.getValue(), targetBox.getValue(), flightDurationSpinner.getValue()));
        grid.add(calcBtn, 0, 4, 2, 1);

        grid.add(new Label(i18n.get("trajectory.label.deltav", "Delta-V")), 0, 5);
        deltaVLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.interplanetarytrajectory.", "--"));
        deltaVLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        grid.add(deltaVLabel, 1, 5);

        missionLog = new TextArea();
        missionLog.setEditable(false);
        missionLog.setPrefHeight(200);
        missionLog.setWrapText(true);

        panel.getChildren().addAll(
                title,
                grid,
                new Separator(),
                new Label(i18n.get("trajectory.label.log", "Mission Log") + ":"), missionLog);

        updateSelection();
        return panel;
    }

    private void updateSelection() {
        if (solarSystem == null)
            return;
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
        if (solarSystemCanvas == null)
            return;
        GraphicsContext gc = solarSystemCanvas.getGraphicsContext2D();
        double w = solarSystemCanvas.getWidth();
        double h = solarSystemCanvas.getHeight();

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w, h);
        gc.translate(w / 2, h / 2);

        gc.setFill(Color.YELLOW);
        gc.fillOval(-10, -10, 20, 20);

        LocalDate date = launchDatePicker.getValue();
        double centuriesSinceJ2000 = getCenturiesSinceJ2000(date);

        for (CelestialBody body : solarSystem.getBodies()) {
            if (body instanceof Planet) {
                drawPlanetOrbit(gc, (Planet) body);
                drawPlanet(gc, (Planet) body, centuriesSinceJ2000);
            }
        }

        gc.setTransform(new Affine());
    }

    private void drawPlanetOrbit(GraphicsContext gc, Planet p) {
        double r = p.getPosition().norm().doubleValue() / AU * SCALE;
        gc.setStroke(Color.web("#333333"));
        gc.setLineWidth(1);
        gc.strokeOval(-r, -r, 2 * r, 2 * r);
    }

    private void drawPlanet(GraphicsContext gc, Planet p, double t) {
        double a_m = p.getPosition().norm().doubleValue();
        double a_au = a_m / AU;
        double P = Math.sqrt(Math.pow(a_au, 3));
        double n = 2 * Math.PI / P;
        double x0 = p.getPosition().get(0).doubleValue();
        double y0 = p.getPosition().get(1).doubleValue();
        double theta0 = Math.atan2(y0, x0);
        double theta = theta0 + n * t * 100;
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

        gc.fillOval(x - size / 2, y - size / 2, size, size);
        gc.setFill(Color.WHITE);
        gc.fillText(p.getName(), x + 8, y + 4);
    }

    private double[] calculatePosition(Planet p, LocalDate date) {
        double centuries = getCenturiesSinceJ2000(date);
        double a_m = p.getPosition().norm().doubleValue();
        double a_au = a_m / AU;
        double P = Math.sqrt(Math.pow(a_au, 3));
        double n = 2 * Math.PI / P;
        double x0 = p.getPosition().get(0).doubleValue();
        double y0 = p.getPosition().get(1).doubleValue();
        double theta0 = Math.atan2(y0, x0);
        double theta = theta0 + n * centuries * 100;
        double r = a_m;
        return new double[] { r * Math.cos(theta), r * Math.sin(theta) };
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

    private void calculateTrajectory(String origin, String target, double durationDays) {
        if (originBody == null || targetBody == null)
            return;
        if (!(originBody instanceof Planet) || !(targetBody instanceof Planet))
            return;
        I18n i18n = I18n.getInstance();

        LocalDate d1 = launchDatePicker.getValue();
        LocalDate d2 = d1.plusDays((long) durationDays);

        missionLog.appendText("--------------------------------\n");
        missionLog.appendText(i18n.get("trajectory.log.analyzing", "Analyzing...") + "\n");
        missionLog.appendText(
                MessageFormat.format(i18n.get("trajectory.log.launcher", "Launcher: {0} -> {1}"), origin, target)
                        + "\n");
        missionLog.appendText(MessageFormat.format(i18n.get("trajectory.log.launch", "Launch: {0}"), d1) + "\n");

        double[] pos1 = calculatePosition((Planet) originBody, d1);
        double[] pos2 = calculatePosition((Planet) targetBody, d2);
        double r1 = Math.sqrt(pos1[0] * pos1[0] + pos1[1] * pos1[1]);
        double r2 = Math.sqrt(pos2[0] * pos2[0] + pos2[1] * pos2[1]);
        double mu = 1.327e20;
        double v1 = Math.sqrt(mu / r1);
        double v2 = Math.sqrt(mu / r2);
        double a_transfer = (r1 + r2) / 2.0;
        double v_perigee = Math.sqrt(mu * (2 / r1 - 1 / a_transfer));
        double v_apogee = Math.sqrt(mu * (2 / r2 - 1 / a_transfer));
        double dv1 = Math.abs(v_perigee - v1);
        double dv2 = Math.abs(v2 - v_apogee);
        double totalDv = (dv1 + dv2) / 1000.0;

        deltaVLabel.setText(i18n.get("trajectory.label.deltav", "Total Delta-V: {0}", String.format("%.2f", totalDv)));
        missionLog.appendText(i18n.get("trajectory.log.estimates", "Delta-V Estimates:") + "\n");
        missionLog.appendText(
                i18n.get("trajectory.log.dep_dv", "Departure Burn: {0} km/s", String.format("%.2f", dv1 / 1000.0))
                        + "\n");
        missionLog.appendText(
                i18n.get("trajectory.log.arr_dv", "Arrival Burn: {0} km/s", String.format("%.2f", dv2 / 1000.0))
                        + "\n");

        GraphicsContext gc = solarSystemCanvas.getGraphicsContext2D();
        gc.save();
        gc.translate(solarSystemCanvas.getWidth() / 2, solarSystemCanvas.getHeight() / 2);
        gc.setStroke(Color.LIMEGREEN);
        gc.setLineWidth(2);
        gc.setLineDashes(5);
        double a_t_au = a_transfer / AU;
        double r1_au = r1 / AU;
        double r2_au = r2 / AU;
        double cx = -(r2_au - r1_au) * SCALE / 2.0;
        double rw = a_t_au * SCALE;
        double rh = Math.sqrt(r1_au * r2_au) * SCALE;
        gc.strokeOval(cx - rw, -rh, 2 * rw, 2 * rh);
        gc.restore();
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("InterplanetaryTrajectoryDemo.desc", "InterplanetaryTrajectoryDemo description");
    }

    @Override
}