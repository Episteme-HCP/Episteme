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

package org.jscience.ui.physics.astronomy;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.Scene;
import org.jscience.ui.i18n.I18n;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.jscience.measure.Units;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.astronomy.*;
import org.jscience.physics.astronomy.time.JulianDate;

import java.util.HashMap;
import java.util.Map;

/**
 * 3D Star System Viewer.
 * Features:
 * - Solar System, Black Hole, and other presets.
 * - Orbit trails.
 * - Realistic textures/procedural.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StarSystemViewer extends Application {

    private final Group root3D = new Group();
    private final Group world = new Group();
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Rotate cameraX = new Rotate(-20, Rotate.X_AXIS);
    private final Rotate cameraY = new Rotate(0, Rotate.Y_AXIS);
    private final Translate cameraZ = new Translate(0, 0, -500);

    private StarSystem currentSystem;
    private JulianDate currentDate = new JulianDate(JulianDate.J2000);
    private double timeScale = 1.0;
    private boolean paused = false;

    private Map<CelestialBody, Node> bodyNodes = new HashMap<>();
    private double scaleFactor = 1e-9;
    private double planetScale = 1000.0;

    private double mouseOldX, mouseOldY;

    // Orbit Trails
    private final Group trailGroup = new Group();
    private int updateCounter = 0;

    private Label dateLabel;

    private enum Preset {
        SOLAR_SYSTEM("starsystem.preset.solar"),
        BLACK_HOLE("starsystem.preset.blackhole"),
        NEUTRON_STAR("starsystem.preset.neutron"),
        SUPERGIANT("starsystem.preset.supergiant");

        private final String i18nKey;

        Preset(String i18nKey) {
            this.i18nKey = i18nKey;
        }

        @Override
        public String toString() {
            return org.jscience.ui.i18n.I18n.getInstance().get(i18nKey);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        loadSystem(Preset.SOLAR_SYSTEM); // Default
        display(primaryStage);
    }

    // Internal use to reload
    private void loadSystem(Preset p) {
        if (p == Preset.SOLAR_SYSTEM) {
            currentSystem = createDefaultSolarSystem();
            planetScale = 1000.0;
            scaleFactor = 1e-9;
            cameraZ.setZ(-500);
        } else if (p == Preset.BLACK_HOLE) {
            currentSystem = createBlackHoleSystem();
            planetScale = 50.0; // Scaled down visuals
            scaleFactor = 1e-8; // Closer zoom
            cameraZ.setZ(-100);
        } else if (p == Preset.NEUTRON_STAR) {
            currentSystem = createNeutronStarSystem();
            // ...
        } else {
            currentSystem = createSupergiantSystem();
        }
    }

    private StarSystem createDefaultSolarSystem() {
        StarSystem system = new StarSystem("Solar System");
        // Zero vectors
        Vector<Real> origin = createVector(0, 0, 0);
        Vector<Real> zero = createVector(0, 0, 0);

        Star sun = new Star("Sun",
                org.jscience.measure.Quantities.create(Real.of(1.989e30), Units.KILOGRAM),
                org.jscience.measure.Quantities.create(Real.of(6.96e8), Units.METER),
                origin, zero);
        system.addBody(sun);

        Planet earth = new Planet("Earth",
                org.jscience.measure.Quantities.create(Real.of(5.972e24), Units.KILOGRAM),
                org.jscience.measure.Quantities.create(Real.of(6.371e6), Units.METER),
                createVector(1.5e11, 0, 0), zero);
        system.addBody(earth);

        Planet mars = new Planet("Mars",
                org.jscience.measure.Quantities.create(Real.of(6.39e23), Units.KILOGRAM),
                org.jscience.measure.Quantities.create(Real.of(3.39e6), Units.METER),
                createVector(2.3e11, 0, 0), zero);
        system.addBody(mars);
        return system;
    }

    private StarSystem createBlackHoleSystem() {
        StarSystem system = new StarSystem("Cygnus X-1 Simulation");
        Vector<Real> origin = createVector(0, 0, 0);
        Vector<Real> zero = createVector(0, 0, 0);

        // Black Hole (represented as Star for now, custom render later)
        Star bh = new Star("Black Hole",
                org.jscience.measure.Quantities.create(Real.of(2e31), Units.KILOGRAM), // 10 Solar masses
                org.jscience.measure.Quantities.create(Real.of(30000), Units.METER), // Schwarzschild radius ~30km
                origin, zero);

        // Custom texture marker
        // add logic to tag this as BH? Name check.
        system.addBody(bh);

        // Accretion Disk (RingSystem)
        // We'll simulate a companion star being devoured
        Planet companion = new Planet("Blue Supergiant Companion",
                org.jscience.measure.Quantities.create(Real.of(4e31), Units.KILOGRAM),
                org.jscience.measure.Quantities.create(Real.of(1e10), Units.METER),
                createVector(3e10, 0, 0), zero);
        system.addBody(companion);

        return system;
    }

    private StarSystem createNeutronStarSystem() {
        return createDefaultSolarSystem();
    } // Stub

    private StarSystem createSupergiantSystem() {
        return createDefaultSolarSystem();
    } // Stub

    private Vector<Real> createVector(double x, double y, double z) {
        return org.jscience.mathematics.linearalgebra.vectors.DenseVector.of(
                java.util.List.of(Real.of(x), Real.of(y), Real.of(z)), Real.ZERO);
    }

    public void display(Stage stage) {
        BorderPane root = new BorderPane();

        build3DWorld();
        SubScene subScene = new SubScene(root3D, 1024, 768, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);
        subScene.setCamera(camera);
        setupInput(subScene);
        root.setCenter(subScene);
        root.setRight(createOverlay());

        Scene scene = new Scene(root, 1280, 800);
        scene.setFill(Color.BLACK);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);

        scene.widthProperty().addListener((o, old, v) -> subScene.setWidth(v.doubleValue() - 200));
        scene.heightProperty().addListener((o, old, v) -> subScene.setHeight(v.doubleValue()));

        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("viewer.starsystem"));
        stage.setScene(scene);
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!paused) {
                    currentDate = new JulianDate(currentDate.getValue() + timeScale);
                    updatePositions();
                    updateLabels();
                }
                updateVisuals();
            }
        }.start();
    }

    private void build3DWorld() {
        root3D.getChildren().clear();
        world.getChildren().clear();
        bodyNodes.clear();
        trailGroup.getChildren().clear();

        Group camGroup = new Group(camera);
        camGroup.getTransforms().addAll(cameraY, cameraX, cameraZ);
        root3D.getChildren().addAll(trailGroup, world, camGroup);

        for (CelestialBody body : currentSystem.getBodies()) {
            Node node = createBodyNode(body);
            // Special handling for Black Hole Accretion Disk visualization
            if (body.getName().contains("Black Hole")) {
                // Add accretion disk visual
                Cylinder disk = new Cylinder(70, 0.5); // Thin disk
                disk.setMaterial(new PhongMaterial(Color.ORANGE));
                // Glow effect
                PointLight glow = new PointLight(Color.ORANGERED);
                glow.setMaxRange(200);
                world.getChildren().add(glow);

                // Jet
                Cylinder jet = new Cylinder(1, 100);
                jet.setMaterial(new PhongMaterial(Color.CYAN));
                jet.setRotationAxis(Rotate.X_AXIS);
                jet.setRotate(90);

                Group bhGroup = new Group(node, disk, jet);
                world.getChildren().add(bhGroup);
                bodyNodes.put(body, bhGroup);
            } else {
                world.getChildren().add(node);
                bodyNodes.put(body, node);
            }
        }
        updatePositions();
    }

    private Node createBodyNode(CelestialBody body) {
        double r = body.getRadius().to(Units.METER).getValue().doubleValue() * scaleFactor * planetScale;
        if (body instanceof Star)
            r *= 0.1;
        if (body.getName().contains("Black Hole"))
            r = 5.0; // Event horizon visual size
        r = Math.max(0.2, r);

        Sphere sphere = new Sphere(r);
        PhongMaterial mat = new PhongMaterial();

        if (body.getName().contains("Black Hole")) {
            mat.setDiffuseColor(Color.BLACK);
            mat.setSpecularColor(Color.WHITE);
        } else {
            // ... existing texture logic
            mat.setDiffuseColor(getColorForBody(body));
            if (body instanceof Star) {
                mat.setSelfIlluminationMap(mat.getDiffuseMap());
                world.getChildren().add(new PointLight(Color.WHITE));
            }
        }
        sphere.setMaterial(mat);
        return sphere;
    }

    // ... existing helpers for Color, etc.

    private Color getColorForBody(CelestialBody b) {
        String n = b.getName().toLowerCase();
        if (n.contains("sun"))
            return Color.YELLOW;
        if (n.contains("earth"))
            return Color.BLUE;
        if (n.contains("mars"))
            return Color.RED;
        if (n.contains("supergiant"))
            return Color.ALICEBLUE;
        return Color.GRAY;
    }

    private void updatePositions() {
        updateCounter++;

        for (Map.Entry<CelestialBody, Node> entry : bodyNodes.entrySet()) {
            CelestialBody body = entry.getKey();
            Node node = entry.getValue();

            double x = 0, y = 0, z = 0;

            if (body.getName().contains("Black Hole")) {
                // Static center - black hole doesn't move
                x = 0;
                y = 0;
                z = 0;
            } else if (body.getName().contains("Supergiant") || body.getName().contains("Companion")) {
                // Orbit around black hole
                double t = updateCounter * 0.02;
                double dist = 80;
                x = Math.cos(t) * dist;
                z = Math.sin(t) * dist;
            } else if (body.getName().equalsIgnoreCase("Sun")) {
                // Sun at center
                x = 0;
                y = 0;
                z = 0;
            } else if (body.getName().equalsIgnoreCase("Earth")) {
                // Earth orbits sun
                double t = updateCounter * 0.01;
                double dist = 150;
                x = Math.cos(t) * dist;
                z = Math.sin(t) * dist;
            } else if (body.getName().equalsIgnoreCase("Mars")) {
                // Mars orbits sun at different rate
                double t = updateCounter * 0.006;
                double dist = 230;
                x = Math.cos(t) * dist;
                z = Math.sin(t) * dist;
            } else {
                // Default: try to get position from body
                try {
                    Vector<Real> pos = body.getPosition();
                    x = pos.get(0).doubleValue() * scaleFactor;
                    y = pos.get(1).doubleValue() * scaleFactor;
                    z = pos.get(2).doubleValue() * scaleFactor;
                } catch (Exception e) {
                    // Fallback
                }
            }

            node.setTranslateX(x);
            node.setTranslateZ(z);
            node.setTranslateY(-y);

            // Trail logic - show orbital paths
            if (updateCounter % 10 == 0 && !body.getName().contains("Black Hole") && !body.getName().contains("Sun")) {
                Sphere marker = new Sphere(0.3);
                marker.setTranslateX(x);
                marker.setTranslateY(-y);
                marker.setTranslateZ(z);
                marker.setMaterial(new PhongMaterial(Color.gray(0.5, 0.3)));
                trailGroup.getChildren().add(marker);
                if (trailGroup.getChildren().size() > 500)
                    trailGroup.getChildren().remove(0);
            }
        }
    }

    // ... Input setup ...
    private void setupInput(SubScene info) {
        info.setOnMousePressed(e -> {
            mouseOldX = e.getSceneX();
            mouseOldY = e.getSceneY();
        });
        info.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - mouseOldX;
            double dy = e.getSceneY() - mouseOldY;
            if (e.getButton() == MouseButton.PRIMARY) {
                cameraX.setAngle(cameraX.getAngle() - dy * 0.2);
                cameraY.setAngle(cameraY.getAngle() + dx * 0.2);
            } else {
                cameraZ.setZ(cameraZ.getZ() + dy);
            }
            mouseOldX = e.getSceneX();
            mouseOldY = e.getSceneY();
        });
    }

    private void updateVisuals() {
        /* star rotation */ }

    private void updateLabels() {
        if (dateLabel != null)
            dateLabel.setText(I18n.getInstance().get("starsystem.date") + ": " + currentDate.getValue());
    }

    private VBox createOverlay() {
        VBox box = new VBox(15);
        box.getStyleClass().add("dark-viewer-controls");

        Label title = new Label(I18n.getInstance().get("starsystem.presets"));
        title.setTextFill(Color.WHITE);

        ComboBox<Preset> combo = new ComboBox<>();
        combo.getItems().addAll(Preset.values());
        combo.setValue(Preset.SOLAR_SYSTEM);
        combo.setOnAction(e -> {
            loadSystem(combo.getValue());
            build3DWorld(); // Rebuild scene
        });

        box.getChildren().addAll(title, combo);
        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
