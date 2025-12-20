package org.jscience.ui.astronomy;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.jscience.measure.Units;
import org.jscience.physics.astronomy.CelestialBody;
import org.jscience.physics.astronomy.StarSystem;
import org.jscience.physics.astronomy.SolarSystemLoader;
import org.jscience.physics.astronomy.mechanics.EphemerisCalculator;
import org.jscience.physics.astronomy.time.JulianDate;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;

import java.util.HashMap;
import java.util.Map;

/**
 * 3D Solar System Explorer Demo.
 * Visualization of planetary orbits using EphemerisCalculator.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class SolarSystemExplorer extends Application {

    private final Group world = new Group();
    private final Rotate rx = new Rotate(-20, new javafx.geometry.Point3D(1, 0, 0));
    private final Rotate ry = new Rotate(0, new javafx.geometry.Point3D(0, 1, 0));
    private final Translate t = new Translate(0, 0, -500); // Zoom out to see orbits
    private double mouseX, mouseY;

    // Simulation State
    private JulianDate currentDate = new JulianDate(JulianDate.J2000); // Start at J2000
    private double timeScale = 1.0; // Days per frame
    private boolean paused = false;

    // Map visual nodes to data
    private Map<CelestialBody, Sphere> bodyVisuals = new HashMap<>();
    private StarSystem solarSystem;

    // UI Elements
    private Label dateLabel = new Label();
    private Slider speedSlider;

    @Override
    public void start(Stage primaryStage) {
        // Load System
        solarSystem = SolarSystemLoader.load("/org/jscience/ui/astronomy/solar_system_demo.json");
        System.out.println(
                "Loaded system: " + solarSystem.getName() + " with " + solarSystem.getBodies().size() + " bodies.");

        BorderPane root = new BorderPane();

        // --- 3D Scene ---
        SubScene subScene = create3DScene();
        root.setCenter(subScene);

        // --- HUD / Controls ---
        VBox overlay = createOverlay();
        root.setBottom(overlay);

        // --- Build 3D Objects ---
        buildSolarSystem();

        // --- Animation Loop ---
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!paused) {
                    currentDate = new JulianDate(currentDate.getValue() + timeScale);
                    updatePositions();
                    updateDateLabel();
                }
            }
        }.start();

        Scene scene = new Scene(root, 1280, 800);
        primaryStage.setTitle("JScience Solar System Explorer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private SubScene create3DScene() {
        world.getTransforms().addAll(t, rx, ry);

        // Starry Background handled by SubScene fill for now

        SubScene subScene = new SubScene(world, 1024, 768, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        subScene.setCamera(camera);

        // Mouse Controls
        subScene.setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        subScene.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - mouseX;
            double dy = e.getSceneY() - mouseY;
            if (e.isPrimaryButtonDown()) {
                ry.setAngle(ry.getAngle() - dx * 0.2);
                rx.setAngle(rx.getAngle() + dy * 0.2);
            } else if (e.isSecondaryButtonDown()) {
                t.setZ(t.getZ() + dy);
            }
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        subScene.setOnScroll(e -> {

            double val = t.getZ();
            if (e.getDeltaY() > 0)
                t.setZ(val + 50);
            else
                t.setZ(val - 50);
        });

        return subScene;
    }

    private VBox createOverlay() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: rgba(30,30,30,0.8);");
        box.setAlignment(Pos.CENTER);

        dateLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        updateDateLabel();

        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);

        Button playPause = new Button("Pause");
        playPause.setOnAction(e -> {
            paused = !paused;
            playPause.setText(paused ? "Play" : "Pause");
        });

        speedSlider = new Slider(-5.0, 5.0, 1.0);
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);
        speedSlider.setMajorTickUnit(1.0);
        speedSlider.setBlockIncrement(0.1);
        speedSlider.valueProperty().addListener((obs, old, val) -> {
            // Logarithmic scale for convenience? Or just linear multiplier
            // -5 to 5 corresponds to 10^x speed?
            // Simple mapping: val is days per frame
            timeScale = val.doubleValue();
        });

        controls.getChildren().addAll(playPause, new Label("Speed:"), speedSlider);
        box.getChildren().addAll(dateLabel, controls);
        return box;
    }

    private void buildSolarSystem() {
        // AU to Scene Units conversion
        // 1 AU = 100 units

        for (CelestialBody body : solarSystem.getBodies()) {
            double r = body.getRadius().to(Units.METER).getValue().doubleValue();
            // Scale visualization:
            // Sun R ~ 7e8 m ~ 0.0046 AU.
            // In 100 units/AU -> 0.46 units.
            // Let's scale up planets for visibility.
            // Factor 50 for planets, 5 for Sun?

            double visualRadius;
            if (body.getName().equalsIgnoreCase("Sun")) {
                visualRadius = 10.0; // Fixed large size
            } else {
                // Log scale or fixed factor?
                // Let's use log scale: log10(radius) ?
                // Earth 6e6 -> 6.8. Jupiter 7e7 -> 7.8.
                // Not enough difference.
                // Just use a multiplier.
                visualRadius = (r / 6.371e6) * 1.0; // Earth = 1 unit
                if (visualRadius > 5)
                    visualRadius = 5; // Cap giants
                if (visualRadius < 0.5)
                    visualRadius = 0.5; // Cap dwarfs
            }

            Sphere sphere = new Sphere(visualRadius);

            PhongMaterial material = new PhongMaterial();
            // Default color
            material.setDiffuseColor(Color.GREY);

            // Texture
            String texPath = body.getTexturePath(); // "textures/..." or absolute?
            if (texPath != null) {
                try {
                    // Try to load
                    Image img = new Image(getClass().getResourceAsStream(texPath));
                    material.setDiffuseMap(img);
                } catch (Exception e) {
                    System.err.println("Could not load texture: " + texPath);
                    if (body.getName().equals("Sun"))
                        material.setDiffuseColor(Color.YELLOW);
                    else if (body.getName().equals("Earth"))
                        material.setDiffuseColor(Color.BLUE);
                    else if (body.getName().equals("Mars"))
                        material.setDiffuseColor(Color.RED);
                }
            }
            if (body.getName().equalsIgnoreCase("Sun")) {
                material.setSelfIlluminationMap(material.getDiffuseMap()); // Glowing sun
            }

            sphere.setMaterial(material);

            // Tag user data
            sphere.setUserData(body);
            bodyVisuals.put(body, sphere);
            world.getChildren().add(sphere);

            // Tooltip handled by mouse events?
            Tooltip t = new Tooltip(body.getName());
            Tooltip.install(sphere, t);
        }
    }

    private void updatePositions() {
        double scaleAU = 100.0; // 100 scene units = 1 AU

        for (CelestialBody body : bodyVisuals.keySet()) {
            if (body.getName().equalsIgnoreCase("Sun"))
                continue; // Sun at 0,0,0

            // Find Ephemeris planet
            EphemerisCalculator.Planet ephemPlanet = EphemerisCalculator.Planet.get(body.getName().toUpperCase());

            double x = 0, y = 0, z = 0;

            if (ephemPlanet != null) {
                Vector<Real> pos = EphemerisCalculator.heliocentricPositionVector(ephemPlanet, currentDate);
                x = pos.get(0).doubleValue() * scaleAU;
                y = pos.get(1).doubleValue() * scaleAU;
                z = pos.get(2).doubleValue() * scaleAU;
            } else if (body.getName().equalsIgnoreCase("Moon")) {
                // Special case for moon?
                // Or find its parent?
                // For simplicity, skip moon precise calc or implement parent rel.
                // body.getParent() ?
                // SolarSystemLoader sets hierarchy, but CelestialBody has no getParent().
                // However, the JSON has nested structure.
                // Let's just assume Moon orbits Earth
                EphemerisCalculator.Planet earth = EphemerisCalculator.Planet.get("EARTH");
                Vector<Real> posE = EphemerisCalculator.heliocentricPositionVector(earth, currentDate);
                // Simple orbit for moon:
                // Distance ~ 384k km = 0.00257 AU
                double r = 0.00257 * scaleAU;
                double t = currentDate.getValue() * 13.0; // Fast orbit
                x = posE.get(0).doubleValue() * scaleAU + r * Math.cos(t);
                y = posE.get(1).doubleValue() * scaleAU + r * Math.sin(t);
                z = posE.get(2).doubleValue() * scaleAU;
            }

            Sphere s = bodyVisuals.get(body);
            s.setTranslateX(x);
            s.setTranslateZ(y); // Y is up in JavaFX, but typically Z is up in astronomy or Ecliptic is XY?
            // In EphemerisCalculator, heliocentricPositionVector returns XYZ.
            // Usually Z is perp to ecliptic.
            // In JavaFX, Y is down (screen) or up (3D).
            // Let's map Astro Z -> JavaFX Y (inverted)
            // Astro X -> JavaFX X
            // Astro Y -> JavaFX Z (Depth)

            s.setTranslateY(-z); // Flip Z to Y
            s.setTranslateZ(y); // Map Y to Z
        }
    }

    private void updateDateLabel() {
        // Convert JD to String
        // JD 2451545.0 is Jan 1 2000 12:00
        // Simple approx text
        dateLabel.setText(String.format("Julian Date: %.2f", currentDate.getValue()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
