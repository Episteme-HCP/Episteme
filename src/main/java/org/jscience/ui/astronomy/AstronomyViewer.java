package org.jscience.ui.astronomy;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import org.jscience.physics.astronomy.CelestialBody;
import org.jscience.physics.astronomy.StarSystem;
import org.jscience.physics.astronomy.SolarSystemLoader;
import org.jscience.mathematics.numbers.real.Real;

import java.util.HashMap;
import java.util.Map;

/**
 * 3D Viewer for Celestial Systems.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class AstronomyViewer extends Application {

    private static final double SCALE_DISTANCE = 1.0e-9; // Scale down meters to pixels
    private static final double SCALE_RADIUS = 1.0e-6; // Scale down radii less strictly to be visible

    private final Group root = new Group();
    private final Group world = new Group();
    private final PerspectiveCamera camera = new PerspectiveCamera(true);

    // Camera controls
    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Translate translate = new Translate(0, 0, -1000);

    private StarSystem currentSystem;
    private final Map<CelestialBody, Sphere> bodyShapes = new HashMap<>();

    @Override
    public void start(Stage stage) {
        // Load default if available, or just empty
        // In real app, user might load via menu. For demo, we try to load
        // "solarsystem.json"
        try {
            currentSystem = SolarSystemLoader.load("/org/jscience/astronomy/solarsystem.json");
            buildSceneGraph(currentSystem);
        } catch (Exception e) {
            System.err.println("Could not load default system: " + e.getMessage());
        }

        root.getChildren().add(world);
        world.getChildren().add(new javafx.scene.AmbientLight(Color.WHITE)); // Basic lighting

        Scene scene = new Scene(root, 1024, 768, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);
        scene.setCamera(camera);

        // Camera Setup
        camera.getTransforms().addAll(rotateY, rotateX, translate);
        camera.setNearClip(0.1);
        camera.setFarClip(100000.0);

        handleInput(scene);

        stage.setTitle("JScience Astronomy Viewer");
        stage.setScene(scene);
        stage.show();

        // Animation Loop could go here using AnimationTimer to update positions
    }

    private void buildSceneGraph(StarSystem system) {
        if (system == null)
            return;

        for (CelestialBody body : system.getBodies()) {
            addBodyToScene(body);
        }
    }

    private void addBodyToScene(CelestialBody body) {
        double r = body.getRadius().getValue().doubleValue() * SCALE_RADIUS;
        // Minimum size to be visible
        if (r < 0.5)
            r = 0.5;

        Sphere sphere = new Sphere(r);

        PhongMaterial material = new PhongMaterial();
        if (body.getTexturePath() != null) {
            try {
                material.setDiffuseMap(new Image(getClass().getResourceAsStream(body.getTexturePath())));
            } catch (Exception e) {
                material.setDiffuseColor(Color.GRAY);
            }
        } else {
            // Fallback colors based on type?
            material.setDiffuseColor(Color.BLUE);
        }
        sphere.setMaterial(material);

        // Position
        double x = body.getPosition().get(0).doubleValue() * SCALE_DISTANCE;
        double y = body.getPosition().get(1).doubleValue() * SCALE_DISTANCE;
        double z = body.getPosition().get(2).doubleValue() * SCALE_DISTANCE; // If 2D, z is 0
        // If vector has 2 dims, get(2) throws or returns 0?
        if (body.getPosition().dimension() > 2) {
            z = body.getPosition().get(2).doubleValue() * SCALE_DISTANCE;
        } else {
            z = 0;
        }

        sphere.setTranslateX(x);
        sphere.setTranslateY(y);
        sphere.setTranslateZ(z);

        bodyShapes.put(body, sphere);
        world.getChildren().add(sphere);
    }

    private void handleInput(Scene scene) {
        scene.setOnMousePressed(me -> {
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });

        scene.setOnMouseDragged(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            rotateX.setAngle(rotateX.getAngle() - (mousePosY - mouseOldY));
            rotateY.setAngle(rotateY.getAngle() + (mousePosX - mouseOldX));
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
        });

        scene.setOnScroll((ScrollEvent event) -> {
            double delta = event.getDeltaY();
            translate.setZ(translate.getZ() + delta * 2);
        });

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
