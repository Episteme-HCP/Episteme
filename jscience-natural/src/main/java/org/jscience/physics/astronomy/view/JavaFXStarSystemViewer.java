package org.jscience.physics.astronomy.view;

import javafx.animation.AnimationTimer;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
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
import javafx.scene.PointLight;
import javafx.stage.Stage;

import org.jscience.physics.astronomy.CelestialBody;
import org.jscience.physics.astronomy.Planet;
import org.jscience.physics.astronomy.RingSystem;
import org.jscience.physics.astronomy.Star;
import org.jscience.physics.astronomy.StarSystem;
import org.jscience.physics.astronomy.visualizer.ProceduralTextureGenerator;
import org.jscience.measure.Units;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;

import java.util.HashMap;
import java.util.Map;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;

/**
 * JavaFX implementation of StarSystemViewer.
 * Provides 3D visualization with interactive controls.
 */
public class JavaFXStarSystemViewer implements StarSystemViewer {

    private final Group root3D = new Group();
    private final Group world = new Group();
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Rotate cameraX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate cameraY = new Rotate(0, Rotate.Y_AXIS);
    private final Translate cameraZ = new Translate(0, 0, -500); // Zoom

    private StarSystem currentSystem;
    // private double timeScale = 1.0;
    private AnimationTimer timer;

    // UI Elements
    private Stage stage;
    private Label overlayLabel;
    private boolean overlayEnabled = true;

    // View Mapping
    private Map<CelestialBody, javafx.scene.Node> bodyNodes = new HashMap<>();
    private double scaleFactor = 1e-9; // Scale meters to screen units (very rough) - planets are huge, space is huger
    // We probably need non-linear scaling or just scale positions and radius
    // differently for visualization
    // For this demo, let's try a uniform scale but maybe log-scale distances if
    // needed.
    // Or just make planets visible (scale up radius)

    private double planetScale = 1000.0; // Artificial multiplier for planet visibility

    private double mouseOldX, mouseOldY;

    public JavaFXStarSystemViewer(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void display(StarSystem system) {
        this.currentSystem = system;

        // Build Scene
        build3DWorld();

        // UI
        SubScene subScene = new SubScene(root3D, 800, 600, true, javafx.scene.SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        overlayLabel = new Label("Select a body...");
        overlayLabel.setStyle("-fx-text-fill: white; -fx-background-color: rgba(0,0,0,0.5); -fx-padding: 10;");
        overlayLabel.setVisible(overlayEnabled);

        CheckBox overlayToggle = new CheckBox("Show Overlay");
        overlayToggle.setSelected(true);
        overlayToggle.setStyle("-fx-text-fill: white;");
        overlayToggle.selectedProperty().addListener((obs, old, val) -> {
            overlayEnabled = val;
            overlayLabel.setVisible(val && !overlayLabel.getText().isEmpty());
        });

        VBox uiLayer = new VBox(10, overlayToggle, overlayLabel);
        uiLayer.setPickOnBounds(false); // Let clicks pass through transparent areas

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(subScene);
        mainLayout.setRight(uiLayer); // Simple overlay placement

        // Controls
        setupControls(subScene);

        Scene scene = new Scene(mainLayout, 800, 600, true);
        scene.setFill(Color.BLACK);

        // Resize listener
        scene.widthProperty().addListener((o, old, v) -> subScene.setWidth(v.doubleValue()));
        scene.heightProperty().addListener((o, old, v) -> subScene.setHeight(v.doubleValue()));

        stage.setTitle("JScience 3D Solar System: " + system.getName());
        stage.setScene(scene);
        stage.show();

        // Start animation loop
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    private void build3DWorld() {
        root3D.getChildren().clear();
        world.getChildren().clear();
        bodyNodes.clear();

        // Camera setup
        Group cameraGroup = new Group();
        cameraGroup.getChildren().add(camera);
        cameraGroup.getTransforms().addAll(cameraY, cameraX, cameraZ);
        root3D.getChildren().addAll(world, cameraGroup);

        // Add bodies
        for (CelestialBody body : currentSystem.getBodies()) {
            if (body instanceof RingSystem)
                continue; // Handle rings as children

            // Visual Radius (scaled)
            double r = body.getRadius().to(Units.METER).getValue().doubleValue() * scaleFactor * planetScale;
            if (body instanceof Star)
                r *= 0.1; // Shrink star a bit visually relative to planets? Or strictly scale.

            Sphere sphere = new Sphere(Math.max(0.5, r));

            // Material
            PhongMaterial material = new PhongMaterial();
            String texName = body.getTexture("diffuse");

            if (texName != null) {
                try {
                    // Try primitive resource loading
                    String path = "/org/jscience/physics/astronomy/" + texName;
                    material.setDiffuseMap(new Image(getClass().getResourceAsStream(path)));

                    String norm = body.getTexture("normal");
                    if (norm != null)
                        material.setBumpMap(
                                new Image(getClass().getResourceAsStream("/org/jscience/physics/astronomy/" + norm)));

                    String spec = body.getTexture("specular");
                    if (spec != null)
                        material.setSpecularMap(
                                new Image(getClass().getResourceAsStream("/org/jscience/physics/astronomy/" + spec)));

                } catch (Exception e) {
                    System.err.println("Could not load texture: " + texName);
                    material.setDiffuseColor(Color.GRAY);
                }
            } else {
                // Procedural if moon/planet?
                if (body instanceof Planet || body.getParent() instanceof Planet) {
                    long seed = body.getName().hashCode();
                    BufferedImage gen = ProceduralTextureGenerator.generateMoonTexture(512, 256, seed);
                    material.setDiffuseMap(SwingFXUtils.toFXImage(gen, null));
                } else {
                    material.setDiffuseColor(Color.BLUE);
                }
            }

            if (body instanceof Star) {
                material.setSelfIlluminationMap(material.getDiffuseMap()); // Glowing
                PointLight light = new PointLight(Color.WHITE);
                world.getChildren().add(light);
            }

            sphere.setMaterial(material);
            world.getChildren().add(sphere);
            bodyNodes.put(body, sphere);

            // Interaction
            sphere.setOnMouseClicked(e -> {
                if (!overlayEnabled)
                    return;
                selectBody(body);
                e.consume();
            });

            // Handle Rings
            for (CelestialBody child : body.getChildren()) {
                if (child instanceof RingSystem) {
                    createRingVisual((RingSystem) child, sphere);
                }
            }
        }

        // Initial Positions
        updatePositions();
    }

    private void createRingVisual(RingSystem rings, Sphere parentSphere) {
        double outer = rings.getOuterRadius().to(Units.METER).getValue().doubleValue() * scaleFactor * planetScale;

        // Cylinder as ring? (Flattened)
        Cylinder ring = new Cylinder(outer, 0.1);
        // Need a tube or disk. Cylinder is solid.
        // For now, large flattened cylinder to represent disk.

        PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseColor(Color.rgb(200, 200, 200, 0.5));
        // Texture?
        String ringsTex = rings.getTexture("diffuse");
        if (ringsTex != null) {
            try {
                mat.setDiffuseMap(
                        new Image(getClass().getResourceAsStream("/org/jscience/physics/astronomy/" + ringsTex)));
            } catch (Exception e) {
            }
        }

        ring.setMaterial(mat);
        // Bind to parent? No, just update in update loop.
        // Actually, if we add to world, we need to manually sync pos.
        // If we add to parentSphere, it rotates with parent... which might be OK for
        // rings.
        // But planet rotation != ring rotation maybe?
        // For sim, let's keep separate but put in map.

        world.getChildren().add(ring);
        bodyNodes.put(rings, ring);
    }

    @Override
    public void update() {
        // Here we would advance physics simulation time
        // currentSystem.update(timeScale);

        updatePositions();
    }

    private void updatePositions() {
        for (Map.Entry<CelestialBody, javafx.scene.Node> entry : bodyNodes.entrySet()) {
            CelestialBody body = entry.getKey();
            javafx.scene.Node node = entry.getValue();

            Vector<Real> pos = body.getPosition(); // Meters
            // Convert to View Coords
            double x = pos.get(0).doubleValue() * scaleFactor;
            double z = pos.get(1).doubleValue() * scaleFactor; // Swap Y/Z for screen usually
            double y = pos.get(2).doubleValue() * scaleFactor;

            if (body instanceof RingSystem) {
                // Sync with parent
                if (body.getParent() != null) {
                    Vector<Real> pPos = body.getParent().getPosition();
                    x = pPos.get(0).doubleValue() * scaleFactor;
                    z = pPos.get(1).doubleValue() * scaleFactor;
                    y = pPos.get(2).doubleValue() * scaleFactor;
                }
                // Flat rings on X-Z plane?
                node.setRotationAxis(Rotate.X_AXIS);
                // node.setRotate(90); // Cylinder is upright by default?
                // Cylinder axis is Y. To make it flat on XZ, no rotation needed? or Scale Y to
                // 0?
                node.setScaleY(0.01);
            }

            node.setTranslateX(x);
            node.setTranslateY(y);
            node.setTranslateZ(z);

            if (body instanceof Star) { // Rotate star
                node.setRotate((System.currentTimeMillis() / 100.0) % 360);
                node.setRotationAxis(Rotate.Y_AXIS);
            }
        }
    }

    private void selectBody(CelestialBody body) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(body.getName()).append("\n");
        sb.append("Mass: ").append(body.getMass()).append("\n");
        sb.append("Radius: ").append(body.getRadius()).append("\n");
        if (body instanceof Planet) {
            Planet p = (Planet) body;
            if (p.getSurfaceTemperature() != null)
                sb.append("Temp: ").append(p.getSurfaceTemperature()).append("\n");
            if (p.getSurfacePressure() != null)
                sb.append("Pressure: ").append(p.getSurfacePressure()).append("\n");
            if (!p.getAtmosphereComposition().isEmpty())
                sb.append("Atmosphere: ").append(p.getAtmosphereComposition().keySet()).append("\n");
        }
        overlayLabel.setText(sb.toString());
        overlayLabel.setVisible(overlayEnabled);
    }

    private void setupControls(SubScene scene) {
        scene.setOnMousePressed(e -> {
            mouseOldX = e.getSceneX();
            mouseOldY = e.getSceneY();
        });

        scene.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - mouseOldX;
            double dy = e.getSceneY() - mouseOldY;

            if (e.getButton() == MouseButton.PRIMARY) { // Orbit
                cameraX.setAngle(cameraX.getAngle() - dy * 0.2);
                cameraY.setAngle(cameraY.getAngle() + dx * 0.2);
            } else if (e.getButton() == MouseButton.SECONDARY) { // Pan (Move world)
                world.setTranslateX(world.getTranslateX() + dx);
                world.setTranslateY(world.getTranslateY() + dy);
            }

            mouseOldX = e.getSceneX();
            mouseOldY = e.getSceneY();
        });

        scene.setOnScroll(e -> {
            double zoomFactor = 1.1;
            double delta = e.getDeltaY();
            if (delta < 0)
                zoomFactor = 1 / 1.1;

            cameraZ.setZ(cameraZ.getZ() * zoomFactor);
            // Or TranslateZ
        });
    }

    @Override
    public void setTimeScale(double scale) {
        // this.timeScale = scale;
    }
}
