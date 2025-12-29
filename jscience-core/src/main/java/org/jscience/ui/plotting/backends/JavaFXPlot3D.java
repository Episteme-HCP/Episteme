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

package org.jscience.ui.plotting.backends;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;

import javafx.scene.AmbientLight;
import javafx.scene.PointLight;
import javafx.stage.Stage;
import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.plotting.Plot3D;
import org.jscience.ui.plotting.PlotFormat;

import java.util.List;

/**
 * JavaFX implementation of Plot3D.
 * <p>
 * Uses JavaFX 3D (TriangleMesh, Sphere) for visualization.
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
    private final Rotate cameraXForm = new Rotate(-20, Rotate.X_AXIS);
    private final Rotate cameraYForm = new Rotate(-20, Rotate.Y_AXIS);
    private final Group cameraGroup = new Group();
    private double mouseOldX, mouseOldY;
    private boolean interactive = false;
    private SubScene subScene;

    private static boolean javaFxInitialized = false;

    public JavaFXPlot3D(String title) {
        this.title = title;
        initializeJavaFX();
        buildScene();
        buildAxes();
        addLighting();
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

    private void buildScene() {
        // Camera setup
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        camera.setTranslateZ(-50); // Move back

        cameraGroup.getChildren().add(camera);
        cameraGroup.getTransforms().addAll(cameraYForm, cameraXForm);

        root.getChildren().add(world);
        root.getChildren().add(cameraGroup);

        // SubScene for 3D content
        // Width/Height will be bound by container if possible, else defaults.
        subScene = new SubScene(root, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.WHITESMOKE);

        // Interactive controls on the SubScene
        subScene.setOnMousePressed(me -> {
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        subScene.setOnMouseDragged(me -> {
            if (interactive) {
                cameraYForm.setAngle(cameraYForm.getAngle() - (me.getSceneX() - mouseOldX));
                cameraXForm.setAngle(cameraXForm.getAngle() + (me.getSceneY() - mouseOldY));
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });

        // Zoom with scroll
        subScene.setOnScroll(se -> {
            if (interactive) {
                double z = camera.getTranslateZ();
                double newZ = z + se.getDeltaY() * 0.1;
                camera.setTranslateZ(newZ);
            }
        });
    }

    public Node getNode() {
        return subScene;
    }

    private void buildAxes() {
        final double axisLength = 20.0;
        final double radius = 0.05;

        // X Axis (Red)
        Cylinder xAxis = new Cylinder(radius, axisLength);
        xAxis.setRotationAxis(Rotate.Z_AXIS);
        xAxis.setRotate(90);
        xAxis.setMaterial(new PhongMaterial(Color.RED));
        xAxis.setTranslateX(axisLength / 2);

        // Y Axis (Green) - In this visualization, Y is Depth (Z in JavaFX)
        Cylinder yAxis = new Cylinder(radius, axisLength);
        yAxis.setRotationAxis(Rotate.X_AXIS);
        yAxis.setRotate(90);
        yAxis.setMaterial(new PhongMaterial(Color.GREEN));
        yAxis.setTranslateZ(axisLength / 2);

        // Z Axis (Blue) - In this visualization, Z is Up (-Y in JavaFX)
        Cylinder zAxis = new Cylinder(radius, axisLength);
        zAxis.setMaterial(new PhongMaterial(Color.BLUE));
        zAxis.setTranslateY(-axisLength / 2);

        world.getChildren().addAll(xAxis, yAxis, zAxis);

        // Grid on XZ (JavaFX) plane => User's XY plane
        PhongMaterial gridMat = new PhongMaterial(Color.LIGHTGRAY);
        double range = 10.0;
        for (double i = -range; i <= range; i += 1.0) {
            // Lines along X (constant Z)
            Box lineX = new Box(range * 2, 0.02, 0.02);
            lineX.setTranslateZ(i);
            lineX.setMaterial(gridMat);

            // Lines along Z (constant X)
            Box lineZ = new Box(0.02, 0.02, range * 2);
            lineZ.setTranslateX(i);
            lineZ.setMaterial(gridMat);

            world.getChildren().addAll(lineX, lineZ);
        }
    }

    private void addLighting() {
        AmbientLight ambientLight = new AmbientLight(Color.rgb(80, 80, 80));
        world.getChildren().add(ambientLight);

        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateX(-50);
        pointLight.setTranslateY(-50);
        pointLight.setTranslateZ(-50);
        world.getChildren().add(pointLight);
    }

    @Override
    public Plot3D addSurface(Function<Real[], Real> function, Real xMin, Real xMax, Real yMin, Real yMax,
            String label) {
        // Create a mesh
        int divisions = 50; // Higher resolution
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

                // JavaFX Y is down, Z is depth. We map:
                // User X -> FX X
                // User Y -> FX Z (Depth)
                // User Z -> FX -Y (Up)
                mesh.getPoints().addAll(cx, -z, cy);
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
        material.setDiffuseColor(Color.DODGERBLUE); // Brighter color
        material.setSpecularColor(Color.WHITE);
        meshView.setMaterial(material);

        Platform.runLater(() -> world.getChildren().add(meshView));
        return this;
    }

    @Override
    public Plot3D addScatter(List<Real> xData, List<Real> yData, List<Real> zData, String label) {
        final PhongMaterial material = new PhongMaterial(Color.RED);
        Platform.runLater(() -> {
            for (int i = 0; i < xData.size(); i++) {
                double x = xData.get(i).doubleValue();
                double y = yData.get(i).doubleValue();
                double z = zData.get(i).doubleValue();

                Sphere sphere = new Sphere(0.1);
                sphere.setMaterial(material);

                // Keep consistent mapping
                sphere.setTranslateX(x);
                sphere.setTranslateZ(y); // User Y -> FX Z
                sphere.setTranslateY(-z); // User Z -> FX -Y

                world.getChildren().add(sphere);
            }
        });
        return this;
    }

    @Override
    public Plot3D addParametricCurve(Function<Real, Real> xFunc, Function<Real, Real> yFunc, Function<Real, Real> zFunc,
            Real tMin, Real tMax, String label) {
        int segments = 100;
        double tStart = tMin.doubleValue();
        double tEnd = tMax.doubleValue();
        double dt = (tEnd - tStart) / segments;

        final PhongMaterial material = new PhongMaterial(Color.FORESTGREEN);

        Platform.runLater(() -> {
            for (int i = 0; i < segments; i++) {
                double t1 = tStart + i * dt;

                double x1 = xFunc.evaluate(Real.of(t1)).doubleValue();
                double y1 = yFunc.evaluate(Real.of(t1)).doubleValue();
                double z1 = zFunc.evaluate(Real.of(t1)).doubleValue();

                Sphere s = new Sphere(0.05);
                s.setMaterial(material);
                s.setTranslateX(x1);
                s.setTranslateZ(y1); // User Y -> FX Z
                s.setTranslateY(-z1); // User Z -> FX -Y
                world.getChildren().add(s);
            }
        });
        return this;
    }

    @Override
    public Plot3D setTitle(String title) {
        return this;
    }

    @Override
    public Plot3D setAxisLabels(String xLabel, String yLabel, String zLabel) {
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

            // Layout container
            javafx.scene.layout.StackPane pane = new javafx.scene.layout.StackPane(subScene);
            // Bind Size
            subScene.widthProperty().bind(pane.widthProperty());
            subScene.heightProperty().bind(pane.heightProperty());

            Scene scene = new Scene(pane, 800, 600);
            stage.setScene(scene);
            stage.show();
        });
    }

    @Override
    public void save(String filename, PlotFormat format) {
        System.err.println("Snapshot export not supported.");
    }
}
