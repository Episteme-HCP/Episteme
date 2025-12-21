/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
import javafx.embed.swing.SwingFXUtils;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
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
import org.jscience.physics.astronomy.CelestialBody;
import org.jscience.physics.astronomy.Planet;
import org.jscience.physics.astronomy.RingSystem;
import org.jscience.physics.astronomy.SolarSystemLoader;
import org.jscience.physics.astronomy.Star;
import org.jscience.physics.astronomy.StarSystem;
import org.jscience.physics.astronomy.mechanics.EphemerisCalculator;
import org.jscience.physics.astronomy.time.JulianDate;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * 3D Star System Viewer.
 * Consolidates functionality from SolarSystemExplorer and StarSystemViewer.
 * Features:
 * - Ephemeris-based placement (when available).
 * - Procedural textures.
 * - Interactive camera.
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
    private double timeScale = 1.0; // Days per frame
    private boolean paused = false;

    // Visuals
    private Map<CelestialBody, Node> bodyNodes = new HashMap<>();
    private double scaleFactor = 1e-9;
    private double planetScale = 1000.0;

    // Input state
    private double mouseOldX, mouseOldY;

    // UI
    private Label infoLabel;
    private Label dateLabel;

    @Override
    public void start(Stage primaryStage) {
        // Default to Solar System if run standalone
        try {
            StarSystem sys = SolarSystemLoader.load("/org/jscience/ui/astronomy/solar_system_demo.json");
            display(primaryStage, sys);
        } catch (Exception e) {
            System.err.println("Could not load default solar system: " + e.getMessage());
            // Create dummy system?
            StarSystem dummy = new StarSystem("Empty System");
            display(primaryStage, dummy);
        }
    }

    public void display(Stage stage, StarSystem system) {
        this.currentSystem = system;

        BorderPane root = new BorderPane();

        // 3D View
        build3DWorld();
        SubScene subScene = new SubScene(root3D, 1024, 768, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);
        subScene.setCamera(camera);
        setupInput(subScene);

        root.setCenter(subScene);

        // Overlay
        VBox overlay = createOverlay();
        root.setRight(overlay);

        Scene scene = new Scene(root, 1280, 800);
        scene.setFill(Color.BLACK);

        // Resize handling
        scene.widthProperty().addListener((o, old, v) -> subScene.setWidth(v.doubleValue() - 200)); // Subtract sidebar
        scene.heightProperty().addListener((o, old, v) -> subScene.setHeight(v.doubleValue()));

        stage.setTitle("JScience Star System Viewer: " + system.getName());
        stage.setScene(scene);
        stage.show();

        // Loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!paused) {
                    currentDate = new JulianDate(currentDate.getValue() + timeScale);
                    updatePositions();
                    updateLabels();
                }
                // Rotate stars/etc even if paused?
                updateVisuals();
            }
        }.start();
    }

    private void build3DWorld() {
        root3D.getChildren().clear();
        world.getChildren().clear();
        bodyNodes.clear();

        // Camera setup
        Group camGroup = new Group(camera);
        camGroup.getTransforms().addAll(cameraY, cameraX, cameraZ);
        root3D.getChildren().addAll(world, camGroup);

        for (CelestialBody body : currentSystem.getBodies()) {
            if (body instanceof RingSystem)
                continue;

            Node node = createBodyNode(body);
            world.getChildren().add(node);
            bodyNodes.put(body, node);

            // Handle children (rings/moons if nested in API, but current API is flat list
            // mostly)
            for (CelestialBody child : body.getChildren()) {
                if (child instanceof RingSystem) {
                    Node ringNode = createRingNode((RingSystem) child);
                    world.getChildren().add(ringNode);
                    bodyNodes.put(child, ringNode);
                }
            }
        }

        updatePositions();
    }

    private Node createBodyNode(CelestialBody body) {
        double r = body.getRadius().to(Units.METER).getValue().doubleValue() * scaleFactor * planetScale;
        if (body instanceof Star)
            r *= 0.1; // Reduce star visual size
        r = Math.max(0.2, r); // Min size

        Sphere sphere = new Sphere(r);
        PhongMaterial mat = new PhongMaterial();

        // Texture or Color
        String tex = body.getTexture("diffuse");
        if (tex != null) {
            try {
                mat.setDiffuseMap(new Image(getClass().getResourceAsStream("/org/jscience/physics/astronomy/" + tex)));
            } catch (Exception e) {
                // Ignore
                mat.setDiffuseColor(getColorForBody(body));
            }
        } else {
            if (body instanceof Planet) {
                // Procedural
                long seed = body.getName().hashCode();
                BufferedImage img = ProceduralTextureGenerator.generateMoonTexture(256, 128, seed);
                mat.setDiffuseMap(SwingFXUtils.toFXImage(img, null));
            } else {
                mat.setDiffuseColor(getColorForBody(body));
            }
        }

        if (body instanceof Star) {
            mat.setSelfIlluminationMap(mat.getDiffuseMap());
            PointLight light = new PointLight(Color.WHITE);
            world.getChildren().add(light); // Add light to world, will track star in update
        }

        sphere.setMaterial(mat);
        sphere.setOnMouseClicked(e -> showInfo(body));
        return sphere;
    }

    private Node createRingNode(RingSystem input) {
        double outer = input.getOuterRadius().to(Units.METER).getValue().doubleValue() * scaleFactor * planetScale;
        Cylinder ring = new Cylinder(outer, 0.1);
        PhongMaterial mat = new PhongMaterial(Color.rgb(200, 200, 200, 0.4));
        ring.setMaterial(mat);
        return ring;
    }

    private Color getColorForBody(CelestialBody b) {
        String n = b.getName().toLowerCase();
        if (n.contains("sun"))
            return Color.YELLOW;
        if (n.contains("earth"))
            return Color.BLUE;
        if (n.contains("mars"))
            return Color.RED;
        return Color.GRAY;
    }

    private void updatePositions() {
        double scaleAU = 100.0; // View units per AU

        for (Map.Entry<CelestialBody, Node> entry : bodyNodes.entrySet()) {
            CelestialBody body = entry.getKey();
            Node node = entry.getValue();

            double x = 0, y = 0, z = 0;

            // 1. Try Ephemeris
            EphemerisCalculator.Planet p = EphemerisCalculator.Planet.get(body.getName().toUpperCase());
            if (p != null) {
                Vector<Real> pos = EphemerisCalculator.heliocentricPositionVector(p, currentDate);
                x = pos.get(0).doubleValue() * scaleAU;
                y = pos.get(1).doubleValue() * scaleAU;
                z = pos.get(2).doubleValue() * scaleAU;
            } else if (body.getName().equalsIgnoreCase("Moon")) {
                // Simple approximation relative to Earth
                EphemerisCalculator.Planet earth = EphemerisCalculator.Planet.get("EARTH");
                if (earth != null) {
                    Vector<Real> posE = EphemerisCalculator.heliocentricPositionVector(earth, currentDate);
                    double r = 0.00257 * scaleAU;
                    double t = currentDate.getValue() * 13.0; // Month
                    x = posE.get(0).doubleValue() * scaleAU + r * Math.cos(t);
                    y = posE.get(1).doubleValue() * scaleAU + r * Math.sin(t);
                    z = posE.get(2).doubleValue() * scaleAU;
                }
            } else {
                // Use stored position if any (static bodies)
                Vector<Real> pos = body.getPosition();
                if (pos != null && pos.dimension() >= 3) {
                    x = pos.get(0).doubleValue() * scaleFactor;
                    y = pos.get(1).doubleValue() * scaleFactor;
                    z = pos.get(2).doubleValue() * scaleFactor;
                }
            }

            // Coordinate mapping: Astro Z -> View Y (up/down), Astro Y -> View Z (depth)
            // But usually, X/Y is plane, Z is up.
            // Let's assume standard X, Z plane for orbits.
            node.setTranslateX(x);
            node.setTranslateZ(y);
            node.setTranslateY(-z);

            if (body instanceof RingSystem) {
                node.setScaleY(0.01); // Flat
                // Sync with parent
                if (body.getParent() != null && bodyNodes.containsKey(body.getParent())) {
                    Node pn = bodyNodes.get(body.getParent());
                    node.setTranslateX(pn.getTranslateX());
                    node.setTranslateY(pn.getTranslateY());
                    node.setTranslateZ(pn.getTranslateZ());
                }
            }
        }
    }

    private void updateVisuals() {
        // Star rotation
        for (Map.Entry<CelestialBody, Node> entry : bodyNodes.entrySet()) {
            if (entry.getKey() instanceof Star) {
                entry.getValue().setRotate((System.currentTimeMillis() / 50.0) % 360);
                entry.getValue().setRotationAxis(Rotate.Y_AXIS);
            }
        }
    }

    private void updateLabels() {
        dateLabel.setText(String.format("JD: %.2f", currentDate.getValue()));
    }

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
            } else if (e.getButton() == MouseButton.SECONDARY) {
                cameraZ.setZ(cameraZ.getZ() + dy);
            }
            mouseOldX = e.getSceneX();
            mouseOldY = e.getSceneY();
        });
        info.setOnScroll(e -> {
            cameraZ.setZ(cameraZ.getZ() + (e.getDeltaY() > 0 ? 50 : -50));
        });
    }

    private VBox createOverlay() {
        VBox box = new VBox(15);
        box.setPadding(new javafx.geometry.Insets(10));
        box.setStyle("-fx-background-color: #222;");
        box.setPrefWidth(200);

        dateLabel = new Label("JD: ");
        dateLabel.setTextFill(Color.WHITE);

        CheckBox pauseBox = new CheckBox("Pause");
        pauseBox.setTextFill(Color.WHITE);
        pauseBox.selectedProperty().addListener((o, old, v) -> paused = v);

        Slider speedSlider = new Slider(-2, 2, 1);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.valueProperty().addListener((o, old, v) -> timeScale = v.doubleValue());

        infoLabel = new Label("Select Body");
        infoLabel.setWrapText(true);
        infoLabel.setTextFill(Color.LIGHTGRAY);

        box.getChildren().addAll(new Label("Controls"), dateLabel, pauseBox, new Label("Time Speed"), speedSlider,
                infoLabel);
        return box;
    }

    private void showInfo(CelestialBody body) {
        String txt = "Name: " + body.getName() + "\n" +
                "Mass: " + body.getMass() + "\n" +
                "Radius: " + body.getRadius();
        infoLabel.setText(txt);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
