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

package org.jscience.ui.viewers.physics.astronomy;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.astronomy.coordinates.*;
import org.jscience.physics.loaders.*;
import org.jscience.physics.astronomy.time.JulianDate;
import org.jscience.physics.astronomy.time.SiderealTime;
import org.jscience.ui.ThemeManager;
import org.jscience.ui.i18n.I18n;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Night Sky Visualizer (Zenith Projection).
 * Supports:
 * - Real-time star and planet positions
 * - Time Travel
 * - Location (Lat/Lon)
 * - Constellations
 * - Interactive Selection
 * - View Rotation
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StellarSkyViewer extends Application {

    private static final int WIDTH = 1100;
    private static final int HEIGHT = 800;

    // Data
    private List<StarReader.Star> stars;
    private List<ConstellationLine> constellations = new ArrayList<>();
    private List<PlanetData> planets = new ArrayList<>();
    private List<MoonData> moons = new ArrayList<>();

    // State
    private LocalDateTime simulationTime = LocalDateTime.now();
    private double observerLat = 48.8566; // Paris
    private double observerLon = 2.3522;

    private double viewAzimuthOffset = 0; // User rotation
    private double fovScale = 1.0; // 1.0 = normal (180 deg view), higher = zoom in

    // UI
    private Canvas skyCanvas;
    private Label infoLabel;
    private Label timeLabel;
    private CheckBox showConstellations;
    private CheckBox showPlanets;
    private CheckBox showDSO;
    private CheckBox showTrails;

    // Interaction
    private double lastMouseX;
    private StarReader.Star hoveredStar;
    private StarReader.Star selectedStar;
    private PlanetData hoveredPlanet;
    private PlanetData selectedPlanet;
    private ConstellationLine hoveredConstellation;
    private ConstellationLine selectedConstellation;

    private static class ConstellationLine {
        String star1, star2;
        String name;

        ConstellationLine(String s1, String s2, String name) {
            this.star1 = s1;
            this.star2 = s2;
            this.name = name;
        }
    }

    private void loadData() {
        InputStream isStar = getClass().getResourceAsStream("/org/jscience/physics/astronomy/data/stars.csv");
        stars = (isStar != null) ? StarReader.loadCSV(isStar) : new ArrayList<>();

        try (InputStream isConst = getClass()
                .getResourceAsStream("/org/jscience/physics/astronomy/data/constellations.csv")) {
            if (isConst != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(isConst));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        String name = (parts.length >= 3) ? parts[2].trim()
                                : I18n.getInstance().get("sky.unknown", "Unknown");
                        constellations.add(new ConstellationLine(parts[0].trim(), parts[1].trim(), name));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private static class PlanetData {
        String name;
        Color color;
        // Orbital elements (Mean J2000)
        double N, i, w, a, e, M;
        // Rates
        double nN, ni, nw, na, ne, nM;

        PlanetData(String name, Color color, double N, double i, double w, double a, double e, double M,
                double nN, double ni, double nw, double na, double ne, double nM) {
            this.name = name;
            this.color = color;
            this.N = N;
            this.i = i;
            this.w = w;
            this.a = a;
            this.e = e;
            this.M = M;
            this.nN = nN;
            this.ni = ni;
            this.nw = nw;
            this.na = na;
            this.ne = ne;
            this.nM = nM;
        }
    }

    private static class MoonData {
        // String name;
        String parentName;
        Color color;
        double a;
        double e;
        // double i;
        double P;
        double M0;

        MoonData(String name, String parentName, Color color, double a_km, double e, double i, double P, double M0) {
            // this.name = name;
            this.parentName = parentName;
            this.color = color;
            this.a = a_km / 149597870.7;
            this.e = e;
            // this.i = i;
            this.P = P;
            this.M0 = M0;
        }
    }

    @Override
    public void start(Stage stage) {
        loadData();
        initPlanets();
        initMoons();
        initDeepSkyObjects();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #000;"); // Black window background for sky

        // Canvas
        skyCanvas = new Canvas(WIDTH, HEIGHT);
        root.setCenter(skyCanvas);

        // Interaction
        skyCanvas.setOnMousePressed(e -> lastMouseX = e.getX());
        skyCanvas.setOnMouseDragged(this::handleDrag);
        skyCanvas.setOnMouseMoved(this::handleHover);
        skyCanvas.setOnMouseClicked(this::handleClick);

        // Sidebar
        VBox sidebar = createSidebar();
        root.setRight(sidebar);

        // Date/Time Overlay (Top Left)
        timeLabel = new Label();
        timeLabel.setTextFill(Color.LIGHTGRAY);
        timeLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 14));
        timeLabel.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-padding: 5;");
        BorderPane.setAlignment(timeLabel, Pos.TOP_LEFT);

        StackPane overlay = new StackPane(timeLabel);
        overlay.setAlignment(Pos.TOP_LEFT);
        overlay.setPadding(new Insets(10));
        overlay.setMouseTransparent(true);

        StackPane centerStack = new StackPane(skyCanvas, overlay);
        root.setCenter(centerStack);

        drawSky();

        Scene scene = new Scene(root, WIDTH + 280, HEIGHT);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(I18n.getInstance().get("sky.window.title", "Night Sky Visualizer").replace("JScience - ", ""));
        stage.setScene(scene);
        stage.show();
    }

    // Lock on selection removed by user request

    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(280);
        sidebar.getStyleClass().add("dark-viewer-sidebar");

        Label title = new Label(I18n.getInstance().get("sky.title"));
        title.getStyleClass().add("dark-header");
        title.setWrapText(true);

        // Location
        Label locLabel = new Label(I18n.getInstance().get("sky.location"));
        locLabel.setStyle("-fx-font-weight: bold;");

        Slider latSlider = createLabeledSlider(I18n.getInstance().get("sky.lat"), -90, 90, observerLat, val -> {
            observerLat = val;
            drawSky();
        });
        Slider lonSlider = createLabeledSlider(I18n.getInstance().get("sky.lon"), -180, 180, observerLon, val -> {
            observerLon = val;
            drawSky();
        });

        // Search/Focus
        // Search/Focus removed

        // Zoom/FOV
        // Zoom removed

        // Time Control
        Label timeCtrlLabel = new Label(I18n.getInstance().get("starsystem.date"));
        timeCtrlLabel.setStyle("-fx-font-weight: bold;");

        DatePicker datePicker = new DatePicker(simulationTime.toLocalDate());
        datePicker.setOnAction(e -> {
            simulationTime = simulationTime.with(datePicker.getValue());
            drawSky();
        });

        // Hour Slider (0-24)
        Slider hourSlider = createLabeledSlider(I18n.getInstance().get("sky.hour", "Hour"), 0, 23,
                simulationTime.getHour(), val -> {
                    int h = (int) val;
                    simulationTime = simulationTime.withHour(h);
                    drawSky();
                });

        // Toggles
        showConstellations = new CheckBox(I18n.getInstance().get("sky.stars"));
        showConstellations.setSelected(true);
        showConstellations.setOnAction(e -> drawSky());

        showPlanets = new CheckBox(I18n.getInstance().get("sky.planets"));
        showPlanets.setText(I18n.getInstance().get("sky.solar_system"));
        showPlanets.setSelected(true);
        showPlanets.setOnAction(e -> drawSky());

        showDSO = new CheckBox(I18n.getInstance().get("sky.dso"));
        showDSO.setSelected(true);
        showDSO.setOnAction(e -> drawSky());

        showTrails = new CheckBox(I18n.getInstance().get("sky.trails", "Orbit Trails"));
        showTrails.setSelected(false);
        showTrails.setOnAction(e -> drawSky());

        // Info Panel
        infoLabel = new Label(I18n.getInstance().get("sky.info.select"));
        infoLabel.setWrapText(true);
        infoLabel.setStyle("-fx-font-size: 13px;");
        infoLabel.getStyleClass().add("dark-label-muted");
        infoLabel.setPadding(new Insets(10, 0, 0, 0));

        sidebar.getChildren().addAll(
                title, new Separator(),
                locLabel, latSlider.getParent(), lonSlider.getParent(),
                new Separator(),
                // focusBtn, zoomSlider.getParent(), removed
                new Separator(),
                timeCtrlLabel, datePicker, hourSlider.getParent(),
                new Separator(),
                new Separator(),
                showConstellations, showPlanets, showDSO, showTrails,
                new Separator(),
                new Separator(),
                infoLabel);

        return sidebar;
    }

    private Slider createLabeledSlider(String name, double min, double max, double val,
            java.util.function.DoubleConsumer action) {
        VBox box = new VBox(2);
        Label label = new Label(String.format("%s: %.2f", name, val));
        Slider slider = new Slider(min, max, val);
        slider.valueProperty().addListener((o, ov, nv) -> {
            action.accept(nv.doubleValue());
            label.setText(String.format("%s: %.2f", name, nv.doubleValue()));
        });
        // Hack to return slider but attached to VBox.
        // Better pattern: return Pane, but caller expects slider behavior?
        // I'll attach userData to slider to find parent? No.
        // I'll make the caller handle parent.
        slider.setUserData(box);
        box.getChildren().addAll(label, slider);
        return slider;
    }

    private void initPlanets() {
        // Approximate orbital elements (Keplerian)
        // N = Long asc. node, i = inclination, w = arg perihelion, a = semi-major(AU),
        // e = ecc, M = mean anomaly
        // Rates per century (Cy) or day? Let's use simplified J2000 + rate * d method
        // Using values from JPL approximate ephemerides (3000 BC - 3000 AD)
        // Rates are per day

        // Mercury
        planets.add(new PlanetData("Mercury", Color.LIGHTGRAY,
                48.331, 7.005, 29.124, 0.3871, 0.2056, 168.656,
                3.24587e-5, 3.2e-7, 1.014e-5, 0, 0, 4.0923344));

        // Venus
        planets.add(new PlanetData("Venus", Color.LIGHTYELLOW,
                76.680, 3.394, 54.884, 0.7233, 0.0068, 48.005,
                3.24587e-5, 2.75e-7, 1.383e-5, 0, 0, 1.602136));

        // Mars
        planets.add(new PlanetData("Mars", Color.RED,
                49.558, 1.850, 286.502, 1.5237, 0.0934, 18.602,
                2.122e-5, 6.7e-7, 1.76e-5, 0, 0, 0.5240208));

        // Jupiter
        planets.add(new PlanetData("Jupiter", Color.ORANGE,
                100.556, 1.303, 274.197, 5.2026, 0.0485, 20.020,
                2.768e-5, 4.3e-7, 4.41e-6, 0, 0, 0.0830853));

        // Saturn
        planets.add(new PlanetData("Saturn", Color.GOLDENROD,
                113.715, 2.484, 339.392, 9.5549, 0.0555, 317.020,
                2.39e-5, 1.6e-7, 8.1e-6, 0, 0, 0.033444));

        // Uranus
        planets.add(new PlanetData("Uranus", Color.LIGHTBLUE,
                74.006, 0.773, 96.661, 19.1817, 0.0473, 142.590,
                4.04e-5, 4.1e-7, 4e-6, 0, 0, 0.0117258));

        // Neptune
        planets.add(new PlanetData("Neptune", Color.BLUE,
                131.780, 1.770, 272.846, 30.0583, 0.0086, 260.247,
                3e-5, 9e-8, 1e-6, 0, 0, 0.0059951));
    }

    private void initMoons() {
        // Earth
        moons.add(new MoonData("Moon", "Earth", Color.WHITESMOKE, 384400, 0.0549, 5.145, 27.321, 0));

        // Jupiter (Galilean)
        moons.add(new MoonData("Io", "Jupiter", Color.YELLOW, 421700, 0.0041, 0.05, 1.769, 0));
        moons.add(new MoonData("Europa", "Jupiter", Color.WHITE, 671034, 0.009, 0.47, 3.551, 90));
        moons.add(new MoonData("Ganymede", "Jupiter", Color.LIGHTGRAY, 1070412, 0.0013, 0.20, 7.154, 180));
        moons.add(new MoonData("Callisto", "Jupiter", Color.DARKGRAY, 1882709, 0.0074, 0.20, 16.689, 270));

        // Saturn
        moons.add(new MoonData("Titan", "Saturn", Color.ORANGE, 1221870, 0.0288, 0.348, 15.945, 0));
    }

    private List<DeepSkyObject> deepSkyObjects = new ArrayList<>();

    private static class DeepSkyObject {
        String name;
        String type; // "Galaxy", "Nebula", "Cluster"
        double ra; // Degrees
        double dec; // Degrees

        DeepSkyObject(String name, String type, double ra, double dec, double mag) {
            this.name = name;
            this.type = type;
            this.ra = ra;
            this.dec = dec;
        }
    }

    private void initDeepSkyObjects() {
        // M31 Andromeda Galaxy: 0h 42m 44s | +41d 16m 9s
        deepSkyObjects.add(new DeepSkyObject("M31 (Andromeda)", "Galaxy",
                (0 + 42.0 / 60 + 44.0 / 3600) * 15, (41 + 16.0 / 60 + 9.0 / 3600), 3.4));

        // M42 Orion Nebula: 5h 35m 17s | -5d 23m 28s
        deepSkyObjects.add(new DeepSkyObject("M42 (Orion Nebula)", "Nebula",
                (5 + 35.0 / 60 + 17.0 / 3600) * 15, -(5 + 23.0 / 60 + 28.0 / 3600), 4.0));

        // M45 Pleiades: 3h 47m 24s | +24d 7m 0s
        deepSkyObjects.add(new DeepSkyObject("M45 (Pleiades)", "Cluster",
                (3 + 47.0 / 60 + 24.0 / 3600) * 15, (24 + 7.0 / 60), 1.6));

        // M1 Crab Nebula: 5h 34m 31s | +22d 0m 52s
        deepSkyObjects.add(new DeepSkyObject("M1 (Crab Nebula)", "Nebula",
                (5 + 34.0 / 60 + 31.0 / 3600) * 15, (22 + 0.0 / 60 + 52.0 / 3600), 8.4));

        // M13 Hercules Cluster: 16h 41m 41s | +36d 27m 35s
        deepSkyObjects.add(new DeepSkyObject("M13 (Hercules Cluster)", "Cluster",
                (16 + 41.0 / 60 + 41.0 / 3600) * 15, (36 + 27.0 / 60 + 35.0 / 3600), 5.8));
    }

    private void handleDrag(MouseEvent e) {
        double dx = e.getX() - lastMouseX;
        viewAzimuthOffset += dx * 0.5; // Rotate view
        lastMouseX = e.getX();
        drawSky();
    }

    private void handleHover(MouseEvent e) {
        double mx = e.getX();
        double my = e.getY();

        // Calculate positions first to support hit testing outside draw loop?
        // Or re-calculate? Re-calculating is safer for correctness, though less
        // efficient.
        // But findStarAt re-projects.
        // For Constellations we need map of all stars.
        // Let's optimize: We can't easily get the star map without drawing or
        // recalculating all.
        // We will call drawSky() to refresh but we need the data.
        // Actually, let's just re-calculate the necessary map for hit testing just like
        // we do for stars/planets.

        // 1. Check Planets
        PlanetData newPlanetHover = findPlanetAt(mx, my);
        hoveredPlanet = newPlanetHover;

        // 2. Check Stars (if no planet)
        if (hoveredPlanet == null) {
            hoveredStar = findStarAt(mx, my);
        } else {
            hoveredStar = null;
        }

        // 3. Check Constellations (if no planet or star)
        // We need star positions for this.
        if (hoveredPlanet == null && hoveredStar == null && showConstellations.isSelected()) {
            // Generate map for hit testing
            Map<String, double[]> starPos = new HashMap<>();
            double w = skyCanvas.getWidth();
            double h = skyCanvas.getHeight();
            double cx = w / 2;
            double cy = h / 2;
            double radius = Math.min(w, h) / 2 - 20;

            for (StarReader.Star s : stars) {
                double[] pos = calculateProjectedPosition(s, cx, cy, radius);
                if (pos != null)
                    starPos.put(s.name, pos);
            }
            hoveredConstellation = findConstellationAt(mx, my, starPos);
        } else {
            hoveredConstellation = null;
        }

        drawSky();
    }

    private void handleClick(MouseEvent e) {
        if (hoveredPlanet != null) {
            selectedPlanet = hoveredPlanet;
            selectedStar = null;
            selectedConstellation = null;
        } else if (hoveredStar != null) {
            selectedStar = hoveredStar;
            selectedPlanet = null;
            selectedConstellation = null;
        } else if (hoveredConstellation != null) {
            selectedConstellation = hoveredConstellation;
            selectedStar = null;
            selectedPlanet = null;
        } else {
            selectedStar = null;
            selectedPlanet = null;
            selectedConstellation = null;
        }
        updateInfoPanel();
        drawSky();
    }

    private StarReader.Star findStarAt(double mx, double my) {
        double closest = 10.0;
        StarReader.Star found = null;

        // Optimize: verify logic matches drawSky
        // Need to replicate projection logic
        double w = skyCanvas.getWidth();
        double h = skyCanvas.getHeight();
        double cx = w / 2;
        double cy = h / 2;
        double radius = Math.min(w, h) / 2 - 20;

        for (StarReader.Star s : stars) {
            double[] pos = calculateProjectedPosition(s, cx, cy, radius);
            if (pos != null) {
                double dist = Math.hypot(mx - pos[0], my - pos[1]);
                if (dist < closest) {
                    closest = dist;
                    found = s;
                }
            }
        }
        return found;
    }

    private PlanetData findPlanetAt(double mx, double my) {
        double closest = 10.0;
        PlanetData found = null;

        double w = skyCanvas.getWidth();
        double h = skyCanvas.getHeight();
        double cx = w / 2;
        double cy = h / 2;
        double radius = Math.min(w, h) / 2 - 20;

        for (PlanetData p : planets) {
            double[] pos = calculatePlanetProjectedPosition(p, cx, cy, radius);
            if (pos != null) {
                double dist = Math.hypot(mx - pos[0], my - pos[1]);
                if (dist < closest) {
                    closest = dist;
                    found = p;
                }
            }
        }
        return found;
    }

    private ConstellationLine findConstellationAt(double mx, double my, Map<String, double[]> starPos) {
        for (ConstellationLine line : constellations) {
            double[] p1 = starPos.get(line.star1);
            double[] p2 = starPos.get(line.star2);
            if (p1 != null && p2 != null) {
                double dist = distanceToSegment(mx, my, p1[0], p1[1], p2[0], p2[1]);
                if (dist < 5.0) {
                    return line;
                }
            }
        }
        return null;
    }

    private double distanceToSegment(double x, double y, double x1, double y1, double x2, double y2) {
        double A = x - x1;
        double B = y - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = -1;
        if (len_sq != 0) // in case of 0 length line
            param = dot / len_sq;

        double xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        double dx = x - xx;
        double dy = y - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void updateInfoPanel() {
        if (selectedPlanet != null) {
            String type = I18n.getInstance().get("sky.type.planet");
            String orbit = I18n.getInstance().get("sky.orbit.heliocentric");
            infoLabel.setText(I18n.getInstance().get("sky.info.planet_fmt",
                    I18n.getInstance().get("sky.planet." + selectedPlanet.name.toLowerCase(), selectedPlanet.name),
                    type, orbit));
        } else if (selectedStar != null) {
            infoLabel.setText(I18n.getInstance().get("sky.info.star_fmt",
                    selectedStar.name, selectedStar.mag, selectedStar.spectralType, selectedStar.dist));
        } else if (selectedConstellation != null) {
            infoLabel.setText(I18n.getInstance().get("sky.info.constellation_fmt",
                    selectedConstellation.name, selectedConstellation.star1, selectedConstellation.star2));
        } else {
            infoLabel.setText(I18n.getInstance().get("sky.info.select"));
        }
    }

    private double[] calculateProjectedPosition(StarReader.Star star, double cx, double cy, double radius) {
        double days = getDaysSinceJ2000();
        double[] eq = Precession.apply(star.ra, star.dec, days / 365.25);

        HorizontalCoordinate hor = calculateHorizontal(eq[0], eq[1]);
        if (hor.getAltitude() <= 0)
            return null;

        return project(hor, cx, cy, radius);
    }

    private double[] calculatePlanetProjectedPosition(PlanetData p, double cx, double cy, double radius) {
        double d = getDaysSinceJ2000();

        // 1. Get Heliocentric Coordinates of Planet
        double[] hPlanet = calculateHeliocentricPos(p, d);

        // 2. Get Heliocentric Coordinates of Earth
        // Earth elements (J2000)
        PlanetData earth = new PlanetData("Earth", null,
                174.873, 0.00005, 102.947, 1.0000, 0.0167, 100.464,
                0, 0, 0, 0, 0, 0.9856);
        // Note: Simple Earth model. For strictly J2000, i=0. But over time it changes
        // slightly.
        // For this visual demo, assuming Earth i=0 relative to Ecliptic is sufficient.
        double[] hEarth = calculateHeliocentricPos(earth, d);

        // 3. Geocentric Coordinates (Planet - Earth)
        double x_g = hPlanet[0] - hEarth[0];
        double y_g = hPlanet[1] - hEarth[1];
        double z_g = hPlanet[2] - hEarth[2];

        // 4. Equatorial Coordinates (Rotate by Obliquity of Ecliptic)
        double obl = Math.toRadians(23.4393); // Obliquity J2000
        double x_eq = x_g;
        double y_eq = y_g * Math.cos(obl) - z_g * Math.sin(obl);
        double z_eq = y_g * Math.sin(obl) + z_g * Math.cos(obl);

        // 5. RA / Dec
        double r_geo = Math.sqrt(x_eq * x_eq + y_eq * y_eq + z_eq * z_eq);
        double ra = Math.toDegrees(Math.atan2(y_eq, x_eq));
        if (ra < 0)
            ra += 360;
        double dec = Math.toDegrees(Math.asin(z_eq / r_geo));

        // 6. To Horizontal
        HorizontalCoordinate hor = calculateHorizontal(ra, dec);
        if (hor.getAltitude() <= 0)
            return null;

        return project(hor, cx, cy, radius);
    }

    private double[] calculateHeliocentricPos(PlanetData p, double d) {
        // Elements
        double N = p.N + p.nN * d;
        double i = p.i + p.ni * d;
        double w = p.w + p.nw * d;
        double a = p.a + p.na * d;
        double e = p.e + p.ne * d;
        double M = p.M + p.nM * d;

        // Ensure M in 0-360
        M = M % 360;
        if (M < 0)
            M += 360;

        // Solve Kepler Eq: M = E - e*sin(E)
        double E = Math.toRadians(M);
        for (int k = 0; k < 5; k++) { // 5 iterations is plenty for visual
            E = E - (E - e * Math.sin(E) - Math.toRadians(M)) / (1 - e * Math.cos(E));
        }

        // True Anomaly v
        double xv = a * (Math.cos(E) - e);
        double yv = a * (Math.sqrt(1.0 - e * e) * Math.sin(E));
        double v = Math.atan2(yv, xv); // Radians
        double r = Math.sqrt(xv * xv + yv * yv);

        // Heliocentric coordinates
        double ind = Math.toRadians(i);
        double Nd = Math.toRadians(N);
        double wd = Math.toRadians(w);
        double u = v + wd; // Argument of Latitude

        double x = r * (Math.cos(Nd) * Math.cos(u) - Math.sin(Nd) * Math.sin(u) * Math.cos(ind));
        double y = r * (Math.sin(Nd) * Math.cos(u) + Math.cos(Nd) * Math.sin(u) * Math.cos(ind));
        double z = r * (Math.sin(u) * Math.sin(ind));

        return new double[] { x, y, z };
    }

    private HorizontalCoordinate calculateHorizontal(double ra, double dec) {
        double days = getDaysSinceJ2000();
        double jdVal = JulianDate.J2000 + days;
        // Add time of day fraction? LocalDateTime simulationTime...
        double fraction = (simulationTime.getHour() + simulationTime.getMinute() / 60.0
                + simulationTime.getSecond() / 3600.0) / 24.0;
        jdVal += fraction - 0.5; // JD starts at noon

        JulianDate jd = new JulianDate(jdVal);
        Real lmst = SiderealTime.lmstDegrees(jd, Real.of(observerLon));

        EquatorialCoordinate eq = new EquatorialCoordinate(ra, dec);
        return CoordinateConverter.equatorialToHorizontal(eq, observerLat, lmst.doubleValue());
    }

    private double[] project(HorizontalCoordinate hor, double cx, double cy, double radius) {
        double alt = hor.getAltitude();
        double az = hor.getAzimuth() + viewAzimuthOffset;

        // Simple Zenith projection: r is proportional to zenith angle (90 - alt)
        // Apply zoom: radius of projection sphere effectively increases
        double r = radius * (90 - alt) / 90.0 * fovScale;

        // If zoomed in, clip? Canvas clips automatically.

        double x = cx + r * Math.sin(Math.toRadians(az));
        double y = cy - r * Math.cos(Math.toRadians(az));
        return new double[] { x, y };
    }

    private double[] calculateMoonProjectedPosition(MoonData m, double cx, double cy, double radius) {
        double d = getDaysSinceJ2000();

        // 1. Find Parent Planet Position (Heliocentric)
        PlanetData parent = null;
        if (m.parentName.equals("Earth")) {
            // Special case: Earth isn't in 'planets' list usually used for rendering,
            // but we construct it on the fly in calculatePlanetProjectedPosition.
            // We need a helper to get Earth's Helio pos.
            parent = new PlanetData("Earth", null,
                    174.873, 0.00005, 102.947, 1.0000, 0.0167, 100.464,
                    0, 0, 0, 0, 0, 0.9856);
        } else {
            for (PlanetData p : planets) {
                if (p.name.equals(m.parentName)) {
                    parent = p;
                    break;
                }
            }
        }

        if (parent == null)
            return null;

        double[] hParent = calculateHeliocentricPos(parent, d);

        // 2. Calculate Moon Position relative to Parent
        // Simplified Keplerian for Moon
        double n = 360.0 / m.P; // Mean motion (deg/day)
        double M = m.M0 + n * d;
        M = M % 360;

        // E
        double E = Math.toRadians(M);
        // Iterate... simplified: E ~ M + e sin M
        E = E + m.e * Math.sin(E);

        double xv = m.a * (Math.cos(E) - m.e);
        double yv = m.a * (Math.sqrt(1 - m.e * m.e) * Math.sin(E));

        // Rotate by Inclination (simplified)
        // Assume orbit is in parent's orbital plane for now, or simplified Ecliptic
        // plane + i
        // For visual validation of moons (like Jupiter's), they are roughly in
        // ecliptic.
        // We add this vector to Parent

        // x_moon_helio = x_parent + xv
        // y_moon_helio = y_parent + yv
        // z_moon_helio = z_parent + ... (inclination)

        // Simplified rotation logic
        double[] hMoon = new double[3];
        hMoon[0] = hParent[0] + xv;
        hMoon[1] = hParent[1] + yv;
        hMoon[2] = hParent[2];

        // 3. Geocentric and Project (Same as Planet)
        // Earth elements (J2000)
        PlanetData earth = new PlanetData("Earth", null,
                174.873, 0.00005, 102.947, 1.0000, 0.0167, 100.464,
                0, 0, 0, 0, 0, 0.9856);
        double[] hEarth = calculateHeliocentricPos(earth, d);

        double x_g = hMoon[0] - hEarth[0];
        double y_g = hMoon[1] - hEarth[1];
        double z_g = hMoon[2] - hEarth[2];

        double obl = Math.toRadians(23.4393);
        double x_eq = x_g;
        double y_eq = y_g * Math.cos(obl) - z_g * Math.sin(obl);
        double z_eq = y_g * Math.sin(obl) + z_g * Math.cos(obl);

        double r_geo = Math.sqrt(x_eq * x_eq + y_eq * y_eq + z_eq * z_eq);
        double ra = Math.toDegrees(Math.atan2(y_eq, x_eq));
        if (ra < 0)
            ra += 360;
        double dec = Math.toDegrees(Math.asin(z_eq / r_geo));

        HorizontalCoordinate hor = calculateHorizontal(ra, dec);
        if (hor.getAltitude() <= 0)
            return null; // Below horizon

        return project(hor, cx, cy, radius);
    }

    private double getDaysSinceJ2000() {
        long epoch = java.time.LocalDate.of(2000, 1, 1).toEpochDay();
        long current = simulationTime.toLocalDate().toEpochDay();
        return (double) (current - epoch);
    }

    private void drawOrbitTrails(GraphicsContext gc, double cx, double cy, double radius) {
        gc.setLineWidth(1.0);
        double steps = 50;
        double rangeDays = 60; // +/- 30 days

        for (PlanetData p : planets) {
            gc.setStroke(p.color.deriveColor(0, 1, 1, 0.3));

            double[] prevPos = null;
            double currentDay = getDaysSinceJ2000();

            for (int i = 0; i <= steps; i++) {
                double t = i / steps; // 0 to 1
                double d = currentDay - (rangeDays / 2) + (t * rangeDays);

                double[] pos = calculatePlanetProjectedPosition(p, cx, cy, radius, d);

                if (pos != null) {
                    if (prevPos != null) {
                        if (Math.hypot(pos[0] - prevPos[0], pos[1] - prevPos[1]) < 100) {
                            gc.strokeLine(prevPos[0], prevPos[1], pos[0], pos[1]);
                        }
                    }
                    prevPos = pos;
                } else {
                    prevPos = null;
                }
            }
        }
    }

    private double[] calculatePlanetProjectedPosition(PlanetData p, double cx, double cy, double radius, double days) {
        double[] hPlanet = calculateHeliocentricPos(p, days);

        PlanetData earth = new PlanetData("Earth", null,
                174.873, 0.00005, 102.947, 1.0000, 0.0167, 100.464,
                0, 0, 0, 0, 0, 0.9856);
        double[] hEarth = calculateHeliocentricPos(earth, days);

        double x_g = hPlanet[0] - hEarth[0];
        double y_g = hPlanet[1] - hEarth[1];
        double z_g = hPlanet[2] - hEarth[2];

        double obl = Math.toRadians(23.4393);
        double x_eq = x_g;
        double y_eq = y_g * Math.cos(obl) - z_g * Math.sin(obl);
        double z_eq = y_g * Math.sin(obl) + z_g * Math.cos(obl);

        double r_geo = Math.sqrt(x_eq * x_eq + y_eq * y_eq + z_eq * z_eq);
        double ra = Math.toDegrees(Math.atan2(y_eq, x_eq));
        if (ra < 0)
            ra += 360;
        double dec = Math.toDegrees(Math.asin(z_eq / r_geo));

        HorizontalCoordinate hor = calculateHorizontal(ra, dec, getDaysSinceJ2000());
        if (hor.getAltitude() <= 0)
            return null;

        return project(hor, cx, cy, radius);
    }

    private HorizontalCoordinate calculateHorizontal(double ra, double dec, double currentDaysForST) {
        double jdVal = JulianDate.J2000 + currentDaysForST;
        double fraction = (simulationTime.getHour() + simulationTime.getMinute() / 60.0
                + simulationTime.getSecond() / 3600.0) / 24.0;
        jdVal += fraction - 0.5;

        JulianDate jd = new JulianDate(jdVal);
        Real lmst = SiderealTime.lmstDegrees(jd, Real.of(observerLon));

        EquatorialCoordinate eq = new EquatorialCoordinate(ra, dec);
        return CoordinateConverter.equatorialToHorizontal(eq, observerLat, lmst.doubleValue());
    }

    private void drawSky() {
        timeLabel.setText(simulationTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        GraphicsContext gc = skyCanvas.getGraphicsContext2D();
        double w = skyCanvas.getWidth();
        double h = skyCanvas.getHeight();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, w, h);

        double cx = w / 2;
        double cy = h / 2;
        double radius = Math.min(w, h) / 2 - 20;

        // Horizon
        gc.setStroke(Color.DARKBLUE);
        gc.strokeOval(cx - radius, cy - radius, radius * 2, radius * 2);

        // Cardinal points (Rotated by offset)
        drawCardinal(gc, "N", 0, cx, cy, radius);
        drawCardinal(gc, "E", 90, cx, cy, radius);
        drawCardinal(gc, "S", 180, cx, cy, radius);
        drawCardinal(gc, "W", 270, cx, cy, radius);

        // Stars
        Map<String, double[]> starPos = new HashMap<>(); // For constellations
        for (StarReader.Star s : stars) {
            double[] pos = calculateProjectedPosition(s, cx, cy, radius);
            if (pos != null) {
                starPos.put(s.name, pos);

                double size = Math.max(1.5, 5 - s.mag);
                Color c = Color.WHITE; // Simplify colors for now or use lookup

                if (s == selectedStar) {
                    gc.setStroke(Color.CYAN);
                    gc.strokeOval(pos[0] - size - 2, pos[1] - size - 2, size * 2 + 4, size * 2 + 4);
                } else if (s == hoveredStar) {
                    gc.setStroke(Color.YELLOW);
                    gc.strokeOval(pos[0] - size, pos[1] - size, size * 2, size * 2);
                }

                gc.setFill(c);
                gc.fillOval(pos[0] - size / 2, pos[1] - size / 2, size, size);
            }
        }

        // Constellations
        if (showConstellations.isSelected()) {
            gc.setLineWidth(1.0);
            for (ConstellationLine line : constellations) {
                double[] p1 = starPos.get(line.star1);
                double[] p2 = starPos.get(line.star2);
                if (p1 != null && p2 != null) {
                    if (line == selectedConstellation) {
                        gc.setStroke(Color.CYAN);
                        gc.setLineWidth(2.5);
                    } else if (line == hoveredConstellation) {
                        gc.setStroke(Color.YELLOW);
                        gc.setLineWidth(2.0);
                    } else {
                        gc.setStroke(Color.rgb(135, 206, 250, 0.6)); // LightSkyBlue, more visible
                        gc.setLineWidth(1.5);
                    }
                    gc.strokeLine(p1[0], p1[1], p2[0], p2[1]);
                }
            }
        }

        // Planets
        if (showPlanets.isSelected()) {
            if (showTrails.isSelected()) {
                drawOrbitTrails(gc, cx, cy, radius);
            }
            for (PlanetData p : planets) {
                double[] pos = calculatePlanetProjectedPosition(p, cx, cy, radius);
                if (pos != null) {
                    double size = 4; // Standard planet size
                    if (p.name.equals("Jupiter") || p.name.equals("Saturn"))
                        size = 6;

                    if (p == selectedPlanet) {
                        gc.setStroke(Color.CYAN);
                        gc.strokeOval(pos[0] - size - 2, pos[1] - size - 2, size * 2 + 4, size * 2 + 4);
                    } else if (p == hoveredPlanet) {
                        gc.setStroke(Color.YELLOW);
                        gc.strokeOval(pos[0] - size, pos[1] - size, size * 2, size * 2);
                    }

                    gc.setFill(p.color);
                    gc.fillOval(pos[0] - size, pos[1] - size, size * 2, size * 2);

                    // Label
                    gc.setFill(Color.LIGHTGRAY);
                    gc.setFont(Font.font("Arial", 10));
                    gc.fillText(I18n.getInstance().get("sky.planet." + p.name.toLowerCase(), p.name), pos[0] + 5,
                            pos[1] - 5);
                }
            }

            // Moons (if visible)
            // Using a simple visibility check or just drawing them
            for (MoonData m : moons) {
                double[] pos = calculateMoonProjectedPosition(m, cx, cy, radius);
                if (pos != null) {
                    double size = 2;
                    gc.setFill(m.color);
                    gc.fillOval(pos[0] - size, pos[1] - size, size * 2, size * 2);
                }
            }
        }
        // Deep Sky Objects
        if (showDSO.isSelected()) {
            for (DeepSkyObject dso : deepSkyObjects) {
                // Determine RA/Dec for projection
                HorizontalCoordinate hor = calculateHorizontal(dso.ra, dso.dec, getDaysSinceJ2000());
                if (hor.getAltitude() > 0) {
                    double[] pos = project(hor, cx, cy, radius);

                    // Shape based on type
                    double size = 8;
                    gc.setLineWidth(1.5);

                    if (dso.type.equals("Galaxy")) {
                        gc.setStroke(Color.VIOLET);
                        gc.strokeOval(pos[0] - size, pos[1] - size / 2, size * 2, size); // Ellipse
                    } else if (dso.type.equals("Nebula")) {
                        gc.setStroke(Color.LIGHTGREEN);
                        gc.strokeRect(pos[0] - size, pos[1] - size, size * 2, size * 2); // Square for now
                    } else {
                        gc.setStroke(Color.YELLOW);
                        gc.setLineDashes(3);
                        gc.strokeOval(pos[0] - size, pos[1] - size, size * 2, size * 2); // Dashed Circle
                        gc.setLineDashes(null);
                    }

                    gc.setFill(Color.WHITE);
                    gc.setFont(Font.font("Arial", 10));
                    gc.fillText(dso.name, pos[0] + 10, pos[1]);
                }
            }
        }
    }

    private void drawCardinal(GraphicsContext gc, String txt, double angle, double cx, double cy, double r) {
        double az = angle + viewAzimuthOffset;
        double x = cx + (r + 15) * Math.sin(Math.toRadians(az));
        double y = cy - (r + 15) * Math.cos(Math.toRadians(az));
        gc.setFill(Color.GRAY);
        gc.fillText(txt, x - 5, y + 5);
    }

    /**
     * Static show method for demo integration.
     */
    public static void show(Stage stage) {
        new StellarSkyViewer().start(stage);
    }
}
