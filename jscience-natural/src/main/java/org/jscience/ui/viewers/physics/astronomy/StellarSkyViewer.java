/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.viewers.physics.astronomy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.astronomy.coordinates.*;
import org.jscience.physics.loaders.*;
import org.jscience.physics.astronomy.time.JulianDate;
import org.jscience.physics.astronomy.time.SiderealTime;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.i18n.I18n;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Stellar Sky Viewer.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StellarSkyViewer extends AbstractViewer {

    private static final int WIDTH = 1100;
    private static final int HEIGHT = 800;

    // Data
    private List<StarReader.Star> stars;
    private List<ConstellationLine> constellations = new ArrayList<>();
    private List<PlanetData> planets = new ArrayList<>();
    private List<DeepSkyObject> deepSkyObjects = new ArrayList<>();

    // State
    private LocalDateTime simulationTime = LocalDateTime.now();
    private double observerLat = 48.8566; // Paris
    private double observerLon = 2.3522;
    private double viewAzimuthOffset = 0;
    private double fovScale = 1.0;

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
    private StarReader.Star hoveredStar, selectedStar;
    private PlanetData hoveredPlanet, selectedPlanet;
    private ConstellationLine hoveredConstellation, selectedConstellation;

    public StellarSkyViewer() {
        loadData();
        initPlanets();
        initDeepSkyObjects();

        skyCanvas = new Canvas(WIDTH, HEIGHT);
        this.setStyle("-fx-background-color: #000;"); // Black background

        setupInteraction();
        
        // Layout
        StackPane overlay = new StackPane();
        timeLabel = new Label();
        timeLabel.setTextFill(Color.LIGHTGRAY);
        timeLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 14));
        timeLabel.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-padding: 5;");
        overlay.getChildren().add(timeLabel);
        overlay.setAlignment(Pos.TOP_LEFT);
        overlay.setPadding(new Insets(10));
        overlay.setMouseTransparent(true);
        
        StackPane centerStack = new StackPane(skyCanvas, overlay);
        setCenter(centerStack);
        
        setRight(createSidebar());

        // Resize
        widthProperty().addListener(o -> { if(getWidth()>0) { skyCanvas.setWidth(getWidth()-300); drawSky(); }});
        heightProperty().addListener(o -> { if(getHeight()>0) { skyCanvas.setHeight(getHeight()); drawSky(); }});

        drawSky();
    }

    private void setupInteraction() {
        skyCanvas.setOnMousePressed(e -> lastMouseX = e.getX());
        skyCanvas.setOnMouseDragged(e -> {
            double dx = e.getX() - lastMouseX;
            viewAzimuthOffset += dx * 0.5;
            lastMouseX = e.getX();
            drawSky();
        });
        skyCanvas.setOnMouseMoved(e -> handleHover(e));
        skyCanvas.setOnMouseClicked(e -> handleClick(e));
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(280);
        sidebar.getStyleClass().add("dark-viewer-sidebar");

        Label title = new Label(I18n.getInstance().get("sky.title", "Sky Controls"));
        title.getStyleClass().add("dark-header");

        Label locLabel = new Label(I18n.getInstance().get("sky.location", "Location"));
        locLabel.setStyle("-fx-font-weight: bold;");

        Slider latSlider = createLabeledSlider(I18n.getInstance().get("sky.lat", "Lat"), -90, 90, observerLat, val -> { observerLat = val; drawSky(); });
        Slider lonSlider = createLabeledSlider(I18n.getInstance().get("sky.lon", "Lon"), -180, 180, observerLon, val -> { observerLon = val; drawSky(); });

        Label timeCtrlLabel = new Label(I18n.getInstance().get("starsystem.date", "Time"));
        timeCtrlLabel.setStyle("-fx-font-weight: bold;");
        DatePicker datePicker = new DatePicker(simulationTime.toLocalDate());
        datePicker.setOnAction(e -> { simulationTime = simulationTime.with(datePicker.getValue()); drawSky(); });
        Slider hourSlider = createLabeledSlider(I18n.getInstance().get("sky.hour", "Hour"), 0, 23, simulationTime.getHour(), val -> {
            simulationTime = simulationTime.withHour((int)val); drawSky();
        });

        showConstellations = new CheckBox(I18n.getInstance().get("sky.stars", "Constellations")); showConstellations.setSelected(true); showConstellations.setOnAction(e -> drawSky());
        showPlanets = new CheckBox(I18n.getInstance().get("sky.planets", "Solar System")); showPlanets.setSelected(true); showPlanets.setOnAction(e -> drawSky());
        showDSO = new CheckBox(I18n.getInstance().get("sky.dso", "Deep Sky")); showDSO.setSelected(true); showDSO.setOnAction(e -> drawSky());
        showTrails = new CheckBox(I18n.getInstance().get("sky.trails", "Trails")); showTrails.setSelected(false); showTrails.setOnAction(e -> drawSky());

        infoLabel = new Label(I18n.getInstance().get("sky.info.select", "Select an object..."));
        infoLabel.setWrapText(true);
        infoLabel.getStyleClass().add("dark-label-muted");

        sidebar.getChildren().addAll(title, new Separator(), locLabel, latSlider.getParent(), lonSlider.getParent(), new Separator(),
                timeCtrlLabel, datePicker, hourSlider.getParent(), new Separator(),
                showConstellations, showPlanets, showDSO, showTrails, new Separator(), infoLabel);
        return sidebar;
    }

    private Slider createLabeledSlider(String name, double min, double max, double val, java.util.function.DoubleConsumer action) {
        VBox box = new VBox(2);
        Label label = new Label(String.format("%s: %.2f", name, val));
        Slider slider = new Slider(min, max, val);
        slider.valueProperty().addListener((o, ov, nv) -> { action.accept(nv.doubleValue()); label.setText(String.format("%s: %.2f", name, nv.doubleValue())); });
        slider.setUserData(box);
        box.getChildren().addAll(label, slider);
        return slider;
    }

    private void loadData() {
        InputStream isStar = getClass().getResourceAsStream("/org/jscience/physics/astronomy/data/stars.csv");
        stars = (isStar != null) ? StarReader.loadCSV(isStar) : new ArrayList<>();
        try (InputStream isConst = getClass().getResourceAsStream("/org/jscience/physics/astronomy/data/constellations.csv")) {
            if (isConst != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(isConst));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        String name = (parts.length >= 3) ? parts[2].trim() : "Unknown";
                        constellations.add(new ConstellationLine(parts[0].trim(), parts[1].trim(), name));
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static class ConstellationLine { String star1, star2, name; ConstellationLine(String s1, String s2, String n) { star1=s1; star2=s2; name=n; } }
    private static class PlanetData { String name; Color color; double N, i, w, a, e, M, nN, ni, nw, na, ne, nM;
        PlanetData(String name, Color color, double N, double i, double w, double a, double e, double M, double nN, double ni, double nw, double na, double ne, double nM) {
            this.name=name; this.color=color; this.N=N; this.i=i; this.w=w; this.a=a; this.e=e; this.M=M; this.nN=nN; this.ni=ni; this.nw=nw; this.na=na; this.ne=ne; this.nM=nM;
        }
    }
    private static class DeepSkyObject { String name, type; double ra, dec; DeepSkyObject(String n, String t, double r, double d, double m) { name=n; type=t; ra=r; dec=d; } }

    private void initPlanets() {
        planets.add(new PlanetData("Mercury", Color.LIGHTGRAY, 48.331, 7.005, 29.124, 0.3871, 0.2056, 168.656, 3.24587e-5, 3.2e-7, 1.014e-5, 0, 0, 4.0923344));
        planets.add(new PlanetData("Venus", Color.LIGHTYELLOW, 76.680, 3.394, 54.884, 0.7233, 0.0068, 48.005, 3.24587e-5, 2.75e-7, 1.383e-5, 0, 0, 1.602136));
        planets.add(new PlanetData("Mars", Color.RED, 49.558, 1.850, 286.502, 1.5237, 0.0934, 18.602, 2.122e-5, 6.7e-7, 1.76e-5, 0, 0, 0.5240208));
        planets.add(new PlanetData("Jupiter", Color.ORANGE, 100.556, 1.303, 274.197, 5.2026, 0.0485, 20.020, 2.768e-5, 4.3e-7, 4.41e-6, 0, 0, 0.0830853));
        planets.add(new PlanetData("Saturn", Color.GOLDENROD, 113.715, 2.484, 339.392, 9.5549, 0.0555, 317.020, 2.39e-5, 1.6e-7, 8.1e-6, 0, 0, 0.033444));
        planets.add(new PlanetData("Uranus", Color.LIGHTBLUE, 74.006, 0.773, 96.661, 19.1817, 0.0473, 142.590, 4.04e-5, 4.1e-7, 4e-6, 0, 0, 0.0117258));
        planets.add(new PlanetData("Neptune", Color.BLUE, 131.780, 1.770, 272.846, 30.0583, 0.0086, 260.247, 3e-5, 9e-8, 1e-6, 0, 0, 0.0059951));
    }

    private void initDeepSkyObjects() {
        deepSkyObjects.add(new DeepSkyObject("M31", "Galaxy", (0 + 42.0/60 + 44.0/3600)*15, (41 + 16.0/60 + 9.0/3600), 3.4));
        deepSkyObjects.add(new DeepSkyObject("M42", "Nebula", (5 + 35.0/60 + 17.0/3600)*15, -(5 + 23.0/60 + 28.0/3600), 4.0));
        deepSkyObjects.add(new DeepSkyObject("M45", "Cluster", (3 + 47.0/60 + 24.0/3600)*15, (24 + 7.0/60), 1.6));
        deepSkyObjects.add(new DeepSkyObject("M1", "Nebula", (5 + 34.0/60 + 31.0/3600)*15, (22 + 0.0/60 + 52.0/3600), 8.4));
        deepSkyObjects.add(new DeepSkyObject("M13", "Cluster", (16 + 41.0/60 + 41.0/3600)*15, (36 + 27.0/60 + 35.0/3600), 5.8));
    }

    private void handleHover(MouseEvent e) {
        double mx = e.getX(), my = e.getY();
        PlanetData newPlanet = findPlanetAt(mx, my);
        hoveredPlanet = newPlanet;
        if (hoveredPlanet == null) hoveredStar = findStarAt(mx, my); else hoveredStar = null;

        if (hoveredPlanet == null && hoveredStar == null && showConstellations.isSelected()) {
            Map<String, double[]> starPos = new HashMap<>();
            double w = skyCanvas.getWidth(), h = skyCanvas.getHeight(), cx = w / 2, cy = h / 2;
            double radius = Math.min(w, h) / 2 - 20;
            for (StarReader.Star s : stars) {
                double[] pos = calculateProjectedPosition(s, cx, cy, radius);
                if (pos != null) starPos.put(s.name, pos);
            }
            hoveredConstellation = findConstellationAt(mx, my, starPos);
        } else hoveredConstellation = null;
        drawSky();
    }

    private void handleClick(MouseEvent e) {
        if (hoveredPlanet != null) { selectedPlanet = hoveredPlanet; selectedStar = null; selectedConstellation = null; }
        else if (hoveredStar != null) { selectedStar = hoveredStar; selectedPlanet = null; selectedConstellation = null; }
        else if (hoveredConstellation != null) { selectedConstellation = hoveredConstellation; selectedStar = null; selectedPlanet = null; }
        else { selectedStar = null; selectedPlanet = null; selectedConstellation = null; }
        updateInfoPanel();
        drawSky();
    }

    private PlanetData findPlanetAt(double mx, double my) {
        double w = skyCanvas.getWidth(), h = skyCanvas.getHeight(), cx = w / 2, cy = h / 2, radius = Math.min(w, h) / 2 - 20;
        PlanetData found = null; double closest = 10.0;
        for (PlanetData p : planets) {
            double[] pos = calculatePlanetProjectedPosition(p, cx, cy, radius);
            if (pos != null && Math.hypot(mx - pos[0], my - pos[1]) < closest) { closest = Math.hypot(mx - pos[0], my - pos[1]); found = p; }
        }
        return found;
    }

    private StarReader.Star findStarAt(double mx, double my) {
        double w = skyCanvas.getWidth(), h = skyCanvas.getHeight(), cx = w / 2, cy = h / 2, radius = Math.min(w, h) / 2 - 20;
        StarReader.Star found = null; double closest = 10.0;
        for (StarReader.Star s : stars) {
            double[] pos = calculateProjectedPosition(s, cx, cy, radius);
            if (pos != null && Math.hypot(mx - pos[0], my - pos[1]) < closest) { closest = Math.hypot(mx - pos[0], my - pos[1]); found = s; }
        }
        return found;
    }

    private ConstellationLine findConstellationAt(double mx, double my, Map<String, double[]> starPos) {
        for (ConstellationLine line : constellations) {
            double[] p1 = starPos.get(line.star1);
            double[] p2 = starPos.get(line.star2);
            if (p1 != null && p2 != null && distanceToSegment(mx, my, p1[0], p1[1], p2[0], p2[1]) < 5.0) return line;
        }
        return null;
    }

    private double distanceToSegment(double x, double y, double x1, double y1, double x2, double y2) {
        double A = x - x1, B = y - y1, C = x2 - x1, D = y2 - y1;
        double dot = A * C + B * D, len_sq = C * C + D * D, param = -1;
        if (len_sq != 0) param = dot / len_sq;
        double xx, yy;
        if (param < 0) { xx = x1; yy = y1; } else if (param > 1) { xx = x2; yy = y2; } else { xx = x1 + param * C; yy = y1 + param * D; }
        return Math.hypot(x - xx, y - yy);
    }

    private void updateInfoPanel() {
        if (selectedPlanet != null) infoLabel.setText("Planet: " + selectedPlanet.name);
        else if (selectedStar != null) infoLabel.setText("Star: " + selectedStar.name + " (" + selectedStar.mag + ")");
        else if (selectedConstellation != null) infoLabel.setText("Constellation: " + selectedConstellation.name);
        else infoLabel.setText(I18n.getInstance().get("sky.info.select", "Select an object"));
    }

    private double getDaysSinceJ2000() {
        long epoch = java.time.LocalDate.of(2000, 1, 1).toEpochDay();
        return (double) (simulationTime.toLocalDate().toEpochDay() - epoch);
    }

    private double[] calculateHeliocentricPos(PlanetData p, double d) {
        double N = p.N + p.nN * d; double i = p.i + p.ni * d; double w = p.w + p.nw * d;
        double a = p.a + p.na * d; double e = p.e + p.ne * d; double M = (p.M + p.nM * d) % 360;
        if (M < 0) M += 360;
        double E = Math.toRadians(M);
        for (int k = 0; k < 5; k++) E = E - (E - e * Math.sin(E) - Math.toRadians(M)) / (1 - e * Math.cos(E));
        double xv = a * (Math.cos(E) - e), yv = a * (Math.sqrt(1.0 - e * e) * Math.sin(E));
        double v = Math.atan2(yv, xv), r = Math.hypot(xv, yv);
        double ind = Math.toRadians(i), Nd = Math.toRadians(N), wd = Math.toRadians(w), u = v + wd;
        return new double[] {
            r * (Math.cos(Nd) * Math.cos(u) - Math.sin(Nd) * Math.sin(u) * Math.cos(ind)),
            r * (Math.sin(Nd) * Math.cos(u) + Math.cos(Nd) * Math.sin(u) * Math.cos(ind)),
            r * (Math.sin(u) * Math.sin(ind))
        };
    }

    private HorizontalCoordinate calculateHorizontal(double ra, double dec, double days) {
        double jdVal = JulianDate.J2000 + days + ((simulationTime.getHour() + simulationTime.getMinute()/60.0 + simulationTime.getSecond()/3600.0)/24.0) - 0.5;
        Real lmst = SiderealTime.lmstDegrees(new JulianDate(jdVal), Real.of(observerLon));
        return CoordinateConverter.equatorialToHorizontal(new EquatorialCoordinate(ra, dec), observerLat, lmst.doubleValue());
    }

    private double[] calculateProjectedPosition(StarReader.Star star, double cx, double cy, double radius) {
        double days = getDaysSinceJ2000();
        double[] eq = Precession.apply(star.ra, star.dec, days / 365.25);
        HorizontalCoordinate hor = calculateHorizontal(eq[0], eq[1], days);
        if (hor.getAltitude() <= 0) return null;
        return project(hor, cx, cy, radius);
    }

    private double[] calculatePlanetProjectedPosition(PlanetData p, double cx, double cy, double radius) {
        return calculatePlanetProjectedPosition(p, cx, cy, radius, getDaysSinceJ2000());
    }
    
    private double[] calculatePlanetProjectedPosition(PlanetData p, double cx, double cy, double radius, double days) {
        double[] hPlanet = calculateHeliocentricPos(p, days);
        PlanetData earth = new PlanetData("Earth", null, 174.873, 0.00005, 102.947, 1.0, 0.0167, 100.464, 0,0,0,0,0, 0.9856);
        double[] hEarth = calculateHeliocentricPos(earth, days);
        double xg = hPlanet[0] - hEarth[0], yg = hPlanet[1] - hEarth[1], zg = hPlanet[2] - hEarth[2];
        double obl = Math.toRadians(23.4393);
        double xeq = xg, yeq = yg * Math.cos(obl) - zg * Math.sin(obl), zeq = yg * Math.sin(obl) + zg * Math.cos(obl);
        double rgeo = Math.sqrt(xeq*xeq + yeq*yeq + zeq*zeq);
        double ra = Math.toDegrees(Math.atan2(yeq, xeq)); if(ra<0) ra+=360;
        double dec = Math.toDegrees(Math.asin(zeq/rgeo));
        HorizontalCoordinate hor = calculateHorizontal(ra, dec, days);
        if(hor.getAltitude() <= 0) return null;
        return project(hor, cx, cy, radius);
    }

    private double[] project(HorizontalCoordinate hor, double cx, double cy, double radius) {
        double az = hor.getAzimuth() + viewAzimuthOffset;
        double r = radius * (90 - hor.getAltitude()) / 90.0 * fovScale;
        return new double[] { cx + r * Math.sin(Math.toRadians(az)), cy - r * Math.cos(Math.toRadians(az)) };
    }

    private void drawSky() {
        timeLabel.setText(simulationTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        GraphicsContext gc = skyCanvas.getGraphicsContext2D();
        double w = skyCanvas.getWidth(), h = skyCanvas.getHeight(), cx = w/2, cy = h/2, radius = Math.min(w,h)/2 - 20;
        gc.setFill(Color.BLACK); gc.fillRect(0,0,w,h);
        gc.setStroke(Color.DARKBLUE); gc.strokeOval(cx-radius, cy-radius, radius*2, radius*2);
        drawCardinal(gc, "N", 0, cx, cy, radius); drawCardinal(gc, "E", 90, cx, cy, radius);
        drawCardinal(gc, "S", 180, cx, cy, radius); drawCardinal(gc, "W", 270, cx, cy, radius);

        for(StarReader.Star s : stars) {
            double[] pos = calculateProjectedPosition(s, cx, cy, radius);
            if(pos != null) {
                double size = Math.max(1.5, 5 - s.mag);
                gc.setFill(Color.WHITE); gc.fillOval(pos[0]-size/2, pos[1]-size/2, size, size);
            }
        }

        if(showConstellations.isSelected()) {
            gc.setStroke(Color.rgb(135, 206, 250, 0.4)); gc.setLineWidth(1);
             Map<String, double[]> sMap = new HashMap<>();
             for(StarReader.Star s : stars) {
                 double[] pos = calculateProjectedPosition(s, cx, cy, radius);
                 if(pos!=null) sMap.put(s.name, pos);
             }
             for(ConstellationLine l : constellations) {
                 double[] p1 = sMap.get(l.star1); double[] p2 = sMap.get(l.star2);
                 if(p1!=null && p2!=null) gc.strokeLine(p1[0], p1[1], p2[0], p2[1]);
             }
        }

        if(showPlanets.isSelected()) {
            if(showTrails.isSelected()) drawOrbitTrails(gc, cx, cy, radius);
            for(PlanetData p : planets) {
                double[] pos = calculatePlanetProjectedPosition(p, cx, cy, radius);
                if(pos!=null) {
                    double size=4; gc.setFill(p.color); gc.fillOval(pos[0]-size, pos[1]-size, size*2, size*2);
                    gc.setFill(Color.LIGHTGRAY); gc.fillText(p.name, pos[0]+5, pos[1]-5);
                }
            }
        }

        if(showDSO.isSelected()) {
            for(DeepSkyObject d : deepSkyObjects) {
                 HorizontalCoordinate hor = calculateHorizontal(d.ra, d.dec, getDaysSinceJ2000());
                 if(hor.getAltitude()>0) {
                     double[] pos = project(hor, cx, cy, radius);
                     gc.setStroke(Color.LIGHTGREEN); gc.strokeRect(pos[0]-4, pos[1]-4, 8, 8);
                     gc.fillText(d.name, pos[0]+6, pos[1]);
                 }
            }
        }
    }

    private void drawCardinal(GraphicsContext gc, String txt, double angle, double cx, double cy, double r) {
        double az = angle + viewAzimuthOffset;
        double x = cx + (r + 15) * Math.sin(Math.toRadians(az));
        double y = cy - (r + 15) * Math.cos(Math.toRadians(az));
        gc.setFill(Color.GRAY); gc.fillText(txt, x-5, y+5);
    }
    
    private void drawOrbitTrails(GraphicsContext gc, double cx, double cy, double radius) {
        gc.setLineWidth(1.0);
        for(PlanetData p : planets) {
            gc.setStroke(p.color.deriveColor(0, 1, 1, 0.3));
            double[] prev = null;
            double now = getDaysSinceJ2000();
            for(int i=0; i<=20; i++) {
                double d = now - 30 + (i/20.0)*60;
                double[] pos = calculatePlanetProjectedPosition(p, cx, cy, radius, d);
                if(pos!=null) {
                    if(prev!=null && Math.hypot(pos[0]-prev[0], pos[1]-prev[1]) < 100) gc.strokeLine(prev[0], prev[1], pos[0], pos[1]);
                    prev = pos;
                } else prev=null;
            }
        }
    }
    
    @Override public String getName() { return "Stellar Viewer"; }
    @Override public String getCategory() { return "Physics"; }
}
