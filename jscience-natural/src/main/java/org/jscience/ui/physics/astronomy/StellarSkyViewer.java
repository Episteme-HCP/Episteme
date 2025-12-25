package org.jscience.ui.physics.astronomy;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import org.jscience.natural.i18n.I18n;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import org.jscience.mathematics.numbers.real.Real;

import org.jscience.physics.astronomy.coordinates.*;
import org.jscience.physics.astronomy.loaders.StarLoader;
import org.jscience.physics.astronomy.time.JulianDate;
import org.jscience.physics.astronomy.time.SiderealTime;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interactive Stellar Sky Viewer (Zenith Projection).
 * Supports:
 * - Time Travel (Precession + Sidereal rotation)
 * - Location (Lat/Lon)
 * - Constellations
 * - Mouse Interaction (Hover Info, Drag to Rotate Time)
 */
public class StellarSkyViewer extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 800;

    // Data
    private List<StarLoader.Star> stars;
    private List<String[]> constellations = new ArrayList<>();
    private Map<String, StarLoader.Star> starMap = new HashMap<>();

    // State
    private double yearsFromJ2000 = 0; // Epoch offset
    private double hourOfDay = 22.0; // 10 PM default
    private double observerLat = 48.8566; // Paris
    private double observerLon = 2.3522;

    // UI
    private Canvas skyCanvas;

    private CheckBox showConstellations;
    private Label infoLabel;

    // Interaction
    private double lastMouseX;

    private StarLoader.Star hoveredStar;

    @Override
    public void start(Stage stage) {
        loadData();

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        // Canvas
        skyCanvas = new Canvas(WIDTH, HEIGHT);
        root.setCenter(skyCanvas);

        // Mouse Listeners
        skyCanvas.setOnMousePressed(e -> {
            lastMouseX = e.getX();

        });

        skyCanvas.setOnMouseDragged(this::handleDrag);
        skyCanvas.setOnMouseMoved(this::handleHover);
        skyCanvas.setOnMouseClicked(e -> {
            if (hoveredStar != null) {
                // Persistent info or action
                stage.setTitle(I18n.getInstance().get("sky.window.title"));
            }
        });

        // Controls
        VBox controls = new VBox(10);
        controls.setPadding(new Insets(10));
        controls.setStyle("-fx-background-color: rgba(30, 30, 40, 0.8); -fx-background-radius: 10;");
        controls.setMaxWidth(250);

        Label titleLabel = new Label(I18n.getInstance().get("sky.title"));
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        // Latitude Slider
        Label latLabel = new Label(I18n.getInstance().get("sky.lat"));
        latLabel.setStyle("-fx-text-fill: #aaa;");
        Slider latSlider = new Slider(-90, 90, observerLat);
        latSlider.setShowTickLabels(true);
        latSlider.valueProperty().addListener((o, old, val) -> {
            observerLat = val.doubleValue();
            drawSky(); // Changed from updateSky() to drawSky()
        });

        // Longitude Slider
        Label lonLabel = new Label(I18n.getInstance().get("sky.lon"));
        lonLabel.setStyle("-fx-text-fill: #aaa;");
        Slider lonSlider = new Slider(-180, 180, observerLon);
        lonSlider.setShowTickLabels(true);
        lonSlider.valueProperty().addListener((o, old, val) -> {
            observerLon = val.doubleValue();
            drawSky(); // Changed from updateSky() to drawSky()
        });

        // Time Travel
        Label timeLabel = new Label(I18n.getInstance().get("sky.instruction"));
        timeLabel.setWrapText(true);
        timeLabel.setStyle("-fx-text-fill: #aaa; -fx-font-style: italic;");
        Slider timeSlider = new Slider(-2000, 3000, 0); // Years from J2000
        timeSlider.setShowTickLabels(true);
        timeSlider.setMajorTickUnit(1000);
        timeSlider.valueProperty().addListener((o, old, val) -> {
            yearsFromJ2000 = val.doubleValue(); // Changed from simulatePrecession() to direct assignment
            drawSky(); // Added drawSky()
        });

        showConstellations = new CheckBox(I18n.getInstance().get("sky.stars")); // Replaced existing showConstellations
        showConstellations.setStyle("-fx-text-fill: white;");
        showConstellations.setSelected(true); // Set to true as per original default
        showConstellations.selectedProperty().addListener((o, old, val) -> {
            // showConstellations = val; // This is already the field itself
            drawSky(); // Changed from updateSky() to drawSky()
        });

        controls.getChildren().addAll(titleLabel, latLabel, latSlider, lonLabel, lonSlider,
                new Separator(), timeLabel, timeSlider, showConstellations); // Removed infoLabel, added
                                                                             // showConstellations

        // The original root is a BorderPane, so setRight is appropriate.
        // The instruction's StackPane.setAlignment/setMargin would not work directly.
        root.setRight(controls);

        drawSky();

        Scene scene = new Scene(root, WIDTH + 250, HEIGHT);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle("JScience Interactive Sky Viewer");
        stage.setScene(scene);
        stage.show();
    }

    private Slider createSlider(String tooltip, double min, double max, double val) {
        Slider s = new Slider(min, max, val);
        s.setTooltip(new Tooltip(tooltip));
        return s;
    }

    private void loadData() {
        // Load Stars
        InputStream isStar = getClass().getResourceAsStream("/org/jscience/physics/astronomy/data/stars.csv");
        stars = (isStar != null) ? StarLoader.loadCSV(isStar) : new ArrayList<>();
        for (StarLoader.Star s : stars) {
            starMap.put(s.name, s);
        }

        // Load Constellations
        try (InputStream isConst = getClass()
                .getResourceAsStream("/org/jscience/physics/astronomy/data/constellations.csv")) {
            if (isConst != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(isConst));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        constellations.add(new String[] { parts[0].trim(), parts[1].trim() });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDrag(MouseEvent e) {
        double dx = e.getX() - lastMouseX;
        // Dragging X changes Hour Angle (Time)
        // Sensitivity: 1 pixel = 0.05 hours?
        hourOfDay -= dx * 0.05;
        if (hourOfDay < 0)
            hourOfDay += 24;
        if (hourOfDay > 24)
            hourOfDay -= 24;

        lastMouseX = e.getX();

        drawSky();
    }

    private void handleHover(MouseEvent e) {
        double mx = e.getX();
        double my = e.getY();
        double w = skyCanvas.getWidth();
        double h = skyCanvas.getHeight();
        double cx = w / 2;
        double cy = h / 2;
        double radius = Math.min(w, h) / 2 - 20;

        hoveredStar = null;
        double minDist = 10.0; // 10px hit area

        // We need to re-project to check hit.
        // Optimization: Save projected coordinates?
        // For 30 stars, we can just re-iterate.

        infoLabel.setText("");

        for (StarLoader.Star star : stars) {
            HorizontalCoordinate hor = calculatePosition(star);
            if (hor.getAltitude() > 0) {
                // Projection
                double r = radius * (90 - hor.getAltitude()) / 90.0;

                // Azimuth: 0=N, 90=E.
                // Screen: Y up is -Y.
                // Let's use standard convention: N up (Y-), E left (X-)? Or E right?
                // Looking UP: E is LEFT.
                // 0 (N) -> (0, -r).
                // 90 (E) -> (-r, 0).
                // θ math: 0 -> -90 deg.

                double x = cx - r * Math.sin(Math.toRadians(hor.getAzimuth()));
                double y = cy - r * Math.cos(Math.toRadians(hor.getAzimuth()));

                double dist = Math.sqrt(Math.pow(mx - x, 2) + Math.pow(my - y, 2));
                if (dist < minDist) {
                    minDist = dist;
                    hoveredStar = star;

                    infoLabel.setText(String.format(
                            "Star: %s\nMag: %.2f\nType: %s\nAlt: %.1f°\nAz: %.1f°",
                            star.name, star.mag, star.spectralType, hor.getAltitude(), hor.getAzimuth()));
                }
            }
        }
    }

    private HorizontalCoordinate calculatePosition(StarLoader.Star star) {
        // 1. Precession
        double[] eqNow = Precession.apply(star.ra, star.dec, yearsFromJ2000);
        EquatorialCoordinate eq = new EquatorialCoordinate(eqNow[0], eqNow[1]);

        // 2. Local Sidereal Time
        // Approximate JD: J2000 + years * 365.25 + day fraction
        double jdVal = JulianDate.J2000 + (yearsFromJ2000 * 365.25);
        // Add Day rotation
        jdVal += (hourOfDay - 12.0) / 24.0; // JD starts at Noon

        JulianDate jd = new JulianDate(jdVal);
        Real lmst = SiderealTime.lmstDegrees(jd, Real.of(observerLon));

        // 3. Horizontal
        return CoordinateConverter.equatorialToHorizontal(eq, observerLat, lmst.doubleValue());
    }

    private void drawSky() {
        GraphicsContext gc = skyCanvas.getGraphicsContext2D();
        double w = skyCanvas.getWidth();
        double h = skyCanvas.getHeight();
        double cx = w / 2;
        double cy = h / 2;
        double radius = Math.min(w, h) / 2 - 20;

        // Background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w, h);

        // Horizon Circle
        gc.setStroke(Color.DARKBLUE);
        gc.setLineWidth(2);
        gc.strokeOval(cx - radius, cy - radius, radius * 2, radius * 2);

        // Cardinal Points
        gc.setFill(Color.GRAY);
        gc.setFont(Font.font(12));
        gc.fillText("N", cx, cy - radius - 5);
        gc.fillText("S", cx, cy + radius + 15);
        gc.fillText("E", cx - radius - 15, cy);
        gc.fillText("W", cx + radius + 5, cy);

        // Store positions for line drawing
        Map<String, double[]> positions = new HashMap<>();

        // Draw Stars
        for (StarLoader.Star star : stars) {
            HorizontalCoordinate hor = calculatePosition(star);

            if (hor.getAltitude() > 0) {
                // Projection: Zenith Equidistant
                // r is proportional to zenith distance (90 - Alt)
                double r = radius * (90 - hor.getAltitude()) / 90.0;

                // Azimuth 0 is North (Up). Azimuth 90 is East (Left in sky chart lookin up)
                // x = -sin(az)
                // y = -cos(az)
                double azRad = Math.toRadians(hor.getAzimuth());
                double x = cx - r * Math.sin(azRad);
                double y = cy - r * Math.cos(azRad);

                positions.put(star.name, new double[] { x, y });

                // Draw Star
                double size = Math.max(2, 6 - star.mag);
                Color col = getColor(star.spectralType);

                // Highlight hovered
                if (star == hoveredStar) {
                    gc.setStroke(Color.RED);
                    gc.strokeOval(x - size, y - size, size * 2, size * 2);
                    col = Color.WHITE;
                }

                gc.setFill(col);
                gc.fillOval(x - size / 2, y - size / 2, size, size);
            }
        }

        // Draw Constellations
        if (showConstellations.isSelected()) {
            gc.setStroke(Color.color(0.2, 0.2, 0.5, 0.5));
            gc.setLineWidth(1);
            for (String[] pair : constellations) {
                double[] p1 = positions.get(pair[0]);
                double[] p2 = positions.get(pair[1]);
                if (p1 != null && p2 != null) {
                    gc.strokeLine(p1[0], p1[1], p2[0], p2[1]);
                }
            }
        }
    }

    private Color getColor(String type) {
        if (type == null || type.isEmpty())
            return Color.WHITE;
        char c = type.charAt(0);
        switch (c) {
            case 'O':
                return Color.LIGHTBLUE;
            case 'B':
                return Color.LIGHTSKYBLUE;
            case 'A':
                return Color.WHITE;
            case 'F':
                return Color.LIGHTYELLOW;
            case 'G':
                return Color.YELLOW;
            case 'K':
                return Color.ORANGE;
            case 'M':
                return Color.RED;
            default:
                return Color.WHITE;
        }
    }

    public static void show(Stage stage) {
        new StellarSkyViewer().start(stage);
    }
}
