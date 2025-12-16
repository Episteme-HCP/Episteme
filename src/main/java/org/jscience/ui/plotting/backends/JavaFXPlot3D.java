package org.jscience.ui.plotting.backends;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.plotting.Plot3D;
import org.jscience.ui.plotting.PlotFormat;
// import org.jscience.ui.plotting.Colormap;

import java.util.List;

/**
 * JavaFX implementation of Plot3D.
 * <p>
 * Uses JavaFX 3D (TriangleMesh, Sphere) for visualization.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JavaFXPlot3D implements Plot3D {

    private final String title;
    private final Group root = new Group();
    private final Group world = new Group();
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Rotate cameraXForm = new Rotate(0, Rotate.X_AXIS);
    private final Rotate cameraYForm = new Rotate(0, Rotate.Y_AXIS);
    private final Group cameraGroup = new Group();
    private double mouseOldX, mouseOldY;
    private boolean interactive = false;

    private static boolean javaFxInitialized = false;

    public JavaFXPlot3D(String title) {
        this.title = title;
        initializeJavaFX();
    }

    private static synchronized void initializeJavaFX() {
        if (!javaFxInitialized) {
            try {
                Platform.startup(() -> {
                });
                javaFxInitialized = true;
            } catch (IllegalStateException e) {
                // JavaFX already initialized
                javaFxInitialized = true;
            }
        }
    }

    @Override
    public Plot3D addSurface(Function<Real[], Real> function, Real xMin, Real xMax, Real yMin, Real yMax,
            String label) {
        // Create a mesh
        int divisions = 50;
        float minX = (float) xMin.doubleValue();
        float maxX = (float) xMax.doubleValue();
        float minY = (float) yMin.doubleValue();
        float maxY = (float) yMax.doubleValue();
        float diffX = maxX - minX;
        float diffY = maxY - minY;

        TriangleMesh mesh = new TriangleMesh();

        // Points
        for (int y = 0; y <= divisions; y++) {
            float dy = y / (float) divisions;
            float cy = minY + dy * diffY;
            for (int x = 0; x <= divisions; x++) {
                float dx = x / (float) divisions;
                float cx = minX + dx * diffX;

                Real[] args = { Real.of(cx), Real.of(cy) };
                float z = (float) function.evaluate(args).doubleValue();

                mesh.getPoints().addAll(cx, -z, cy); // JavaFX Y is down, Z is depth. We map Z->Y (up) and Y->Z (depth)
            }
        }

        // Texture coordinates (dummy)
        mesh.getTexCoords().addAll(0, 0);

        // Faces
        for (int y = 0; y < divisions; y++) {
            for (int x = 0; x < divisions; x++) {
                int p00 = y * (divisions + 1) + x;
                int p01 = p00 + 1;
                int p10 = p00 + (divisions + 1);
                int p11 = p10 + 1;

                mesh.getFaces().addAll(p00, 0, p10, 0, p01, 0);
                mesh.getFaces().addAll(p01, 0, p10, 0, p11, 0);
            }
        }

        MeshView meshView = new MeshView(mesh);
        meshView.setDrawMode(DrawMode.FILL);
        meshView.setCullFace(CullFace.NONE);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.BLUE);
        material.setSpecularColor(Color.WHITE);
        meshView.setMaterial(material);

        Platform.runLater(() -> world.getChildren().add(meshView));
        return this;
    }

    @Override
    public Plot3D addScatter(List<Real> xData, List<Real> yData, List<Real> zData, String label) {
        Platform.runLater(() -> {
            PhongMaterial material = new PhongMaterial(Color.RED);
            for (int i = 0; i < xData.size(); i++) {
                double y = yData.get(i).doubleValue();
                double z = zData.get(i).doubleValue();

                Sphere sphere = new Sphere(0.1); // Fixed radius for now
                sphere.setMaterial(material);
                sphere.setTranslateY(-z); // Map Z to Y (up)
                sphere.setTranslateZ(y); // Map Y to Z (depth)

                world.getChildren().add(sphere);
            }
        });
        return this;
    }

    @Override
    public Plot3D addParametricCurve(Function<Real, Real> xFunc, Function<Real, Real> yFunc, Function<Real, Real> zFunc,
            Real tMin, Real tMax, String label) {
        // Approximate curve with cylinders
        int segments = 100;
        double tStart = tMin.doubleValue();
        double tEnd = tMax.doubleValue();
        double dt = (tEnd - tStart) / segments;

        PhongMaterial material = new PhongMaterial(Color.GREEN);

        Platform.runLater(() -> {
            for (int i = 0; i < segments; i++) {
                double t1 = tStart + i * dt;
                double t2 = tStart + (i + 1) * dt;

                double x1 = xFunc.evaluate(Real.of(t1)).doubleValue();
                double y1 = yFunc.evaluate(Real.of(t1)).doubleValue();
                double z1 = zFunc.evaluate(Real.of(t1)).doubleValue();

                // double x2 = xFunc.evaluate(Real.of(t2)).doubleValue();
                // double z2 = zFunc.evaluate(Real.of(t2)).doubleValue();

                // Draw segment from p1 to p2 (simplified as small spheres for now to avoid
                // complex cylinder rotation math)
                // A proper implementation would align cylinders.
                // For "Best Effort", spheres at points are easier and look like a dotted line.
                Sphere s = new Sphere(0.05);
                s.setMaterial(material);
                s.setTranslateX(x1);
                s.setTranslateY(-z1);
                s.setTranslateZ(y1);
                world.getChildren().add(s);
            }
        });
        return this;
    }

    @Override
    public Plot3D setTitle(String title) {
        // Title rendering not yet implemented in this version
        return this;
    }

    @Override
    public Plot3D setAxisLabels(String xLabel, String yLabel, String zLabel) {
        // Axis labels not yet implemented in this version
        return this;
    }

    @Override
    public Plot3D setViewAngle(double azimuth, double elevation) {
        cameraXForm.setAngle(elevation);
        cameraYForm.setAngle(azimuth);
        return this;
    }

    @Override
    public Plot3D setInteractive(boolean enabled) {
        this.interactive = enabled;
        return this;
    }

    @Override
    public Plot3D setColormap(Plot3D.Colormap colormap) {
        // Colormap support pending
        return this;
    }

    @Override
    public void show() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle(title);

            root.getChildren().add(world);

            // Camera setup
            cameraGroup.getChildren().add(camera);
            root.getChildren().add(cameraGroup);

            Scene scene = new Scene(root, 800, 600, true);
            scene.setCamera(camera);

            camera.setTranslateZ(-50); // Move back
            cameraGroup.getTransforms().addAll(cameraYForm, cameraXForm);

            stage.setScene(scene);

            if (interactive) {
                scene.setOnMousePressed(me -> {
                    mouseOldX = me.getSceneX();
                    mouseOldY = me.getSceneY();
                });
                scene.setOnMouseDragged(me -> {
                    cameraYForm.setAngle(cameraYForm.getAngle() - (me.getSceneX() - mouseOldX));
                    cameraXForm.setAngle(cameraXForm.getAngle() + (me.getSceneY() - mouseOldY));
                    mouseOldX = me.getSceneX();
                    mouseOldY = me.getSceneY();
                });
            }

            stage.show();
        });
    }

    @Override
    public void save(String filename, PlotFormat format) {
        // Snapshot export requires javafx.swing module which is currently not available
        System.err.println("Snapshot export not supported in this environment.");
    }
}
