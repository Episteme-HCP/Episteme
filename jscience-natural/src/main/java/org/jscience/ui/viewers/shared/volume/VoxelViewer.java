/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.viewers.shared.volume;

import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import org.jscience.mathematics.geometry.volume.VoxelModel;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.Parameter;
import org.jscience.ui.RealParameter;
import org.jscience.ui.i18n.I18n;

import java.util.List;

/**
 * A high-fidelity 3D Voxel Viewer supporting orthogonal slicing and volumetric rendering.
 * Inspired by professional Geoscience and Medical imaging software.
 */
public class VoxelViewer extends org.jscience.ui.AbstractViewer {

    @Override
    public String getCategory() { return I18n.getInstance().get("category.medicine", "Medicine"); }

    @Override
    public String getName() { return I18n.getInstance().get("viewer.voxelviewer.name", "Voxel Viewer"); }

    private final VoxelModel model;
    private final Group root3D = new Group();
    private final Group volume3D = new Group();
    private final Group slicesGroup = new Group();
    
    // UI HUD
    private final VBox sidebar = new VBox(15);
    private final Canvas axialCanvas = new Canvas(150, 150);
    private final Canvas coronalCanvas = new Canvas(150, 150);
    private final Canvas sagittalCanvas = new Canvas(150, 150);

    // Rotation Control
    private final Rotate rotateX = new Rotate(20, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(-45, Rotate.Y_AXIS);
    private double mouseOldX, mouseOldY;

    // Parameters
    private final RealParameter thresholdParam;
    private final Parameter<Boolean> showVolumeParam;
    private final Parameter<Boolean> showSlicesParam;
    private final RealParameter sliceXParam;
    private final RealParameter sliceYParam;
    private final RealParameter sliceZParam;

    public VoxelViewer(VoxelModel model) {
        this.model = model;
        getStyleClass().add("viewer-root");

        this.thresholdParam = new RealParameter("Density Threshold", "Minimum density to render as solid", Real.of(0.0), Real.of(1.0), Real.of(0.01), Real.of(0.3), this::onThresholdChange);
        this.showVolumeParam = new Parameter<>("Show Volumetric", "Display 3D point cloud", true, this::onShowVolumeChange);
        this.showSlicesParam = new Parameter<>("Show Slices", "Display orthogonal planes", true, this::onShowSlicesChange);
        
        this.sliceXParam = new RealParameter("Sagittal (X)", "X-Slice position", Real.of(0), Real.of(model.getWidth() - 1), Real.of(1), Real.of(model.getWidth()/2), this::onSliceChange);
        this.sliceYParam = new RealParameter("Coronal (Y)", "Y-Slice position", Real.of(0), Real.of(model.getHeight() - 1), Real.of(1), Real.of(model.getHeight()/2), this::onSliceChange);
        this.sliceZParam = new RealParameter("Axial (Z)", "Z-Slice position", Real.of(0), Real.of(model.getDepth() - 1), Real.of(1), Real.of(model.getDepth()/2), this::onSliceChange);

        // 3D Scene Setup
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-400);

        SubScene subScene = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#111"));
        subScene.setCamera(camera);

        root3D.getChildren().addAll(new AmbientLight(Color.web("#555")), volume3D, slicesGroup);
        root3D.getTransforms().addAll(rotateX, rotateY);

        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateZ(-1000);
        root3D.getChildren().add(light);

        // Interaction
        subScene.setOnMousePressed(e -> { mouseOldX = e.getSceneX(); mouseOldY = e.getSceneY(); });
        subScene.setOnMouseDragged(e -> {
            rotateY.setAngle(rotateY.getAngle() + (e.getSceneX() - mouseOldX));
            rotateX.setAngle(rotateX.getAngle() - (e.getSceneY() - mouseOldY));
            mouseOldX = e.getSceneX(); mouseOldY = e.getSceneY();
        });

        // Sidebar Setup
        setupSidebar();

        setCenter(subScene);
        setRight(sidebar);

        update3D();
        updateSlices();
    }

    private void onThresholdChange(Real v) { update3D(); }
    private void onShowVolumeChange(Boolean v) { update3D(); }
    private void onShowSlicesChange(Boolean v) { update3D(); }
    private void onSliceChange(Real v) { updateSlices(); }

    private void setupSidebar() {
        sidebar.setStyle("-fx-background-color: rgba(30,30,30,0.9); -fx-padding: 20; -fx-min-width: 250;");
        sidebar.setAlignment(Pos.TOP_CENTER);

        Label title = new Label(model.getName());
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        VBox slicePreview = new VBox(10);
        slicePreview.setAlignment(Pos.CENTER);
        slicePreview.getChildren().addAll(
            createSliceBox("Axial (Z)", axialCanvas),
            createSliceBox("Coronal (Y)", coronalCanvas),
            createSliceBox("Sagittal (X)", sagittalCanvas)
        );

        sidebar.getChildren().addAll(title, new javafx.scene.shape.Line(0, 0, 200, 0), slicePreview);
    }

    private VBox createSliceBox(String title, Canvas c) {
        Label l = new Label(title);
        l.setTextFill(Color.web("#AAA"));
        l.setStyle("-fx-font-size: 10px;");
        VBox v = new VBox(2, l, c);
        v.setStyle("-fx-border-color: #444; -fx-border-width: 1; -fx-padding: 2;");
        return v;
    }

    private void update3D() {
        volume3D.getChildren().clear();
        slicesGroup.getChildren().clear();

        if (showVolumeParam.getValue()) {
            double threshold = thresholdParam.getValue().doubleValue();
            int step = 2; // Performance optimization
            for (int x = 0; x < model.getWidth(); x += step) {
                for (int y = 0; y < model.getHeight(); y += step) {
                    for (int z = 0; z < model.getDepth(); z += step) {
                        double val = model.getValue(x, y, z).doubleValue();
                        if (val > threshold) {
                            Box b = new Box(step, step, step);
                            Color c = Color.hsb(map(val, 0, 1, 240, 0), 0.8, 1.0, 0.4);
                            b.setMaterial(new PhongMaterial(c));
                            b.setTranslateX(x - model.getWidth()/2.0);
                            b.setTranslateY(y - model.getHeight()/2.0);
                            b.setTranslateZ(z - model.getDepth()/2.0);
                            volume3D.getChildren().add(b);
                        }
                    }
                }
            }
        }
    }

    private void updateSlices() {
        renderCanvas(axialCanvas, (u, v) -> model.getValue(u, v, sliceZParam.getValue().intValue()).doubleValue());
        renderCanvas(coronalCanvas, (u, v) -> model.getValue(u, sliceYParam.getValue().intValue(), v).doubleValue());
        renderCanvas(sagittalCanvas, (u, v) -> model.getValue(sliceXParam.getValue().intValue(), u, v).doubleValue());
    }

    private void renderCanvas(Canvas c, java.util.function.BiFunction<Integer, Integer, Double> pixelProvider) {
        GraphicsContext gc = c.getGraphicsContext2D();
        double cw = c.getWidth();
        double ch = c.getHeight();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, cw, ch);

        int mw = model.getWidth();
        int mh = model.getHeight();
        double xScale = cw / mw;
        double yScale = ch / mh;

        for (int x = 0; x < mw; x += 2) {
            for (int y = 0; y < mh; y += 2) {
                double val = pixelProvider.apply(x, y);
                if (val > 0.05) {
                    gc.setFill(Color.hsb(map(val, 0, 1, 240, 0), 0.8, 1.0));
                    gc.fillRect(x * xScale, y * yScale, 2 * xScale, 2 * yScale);
                }
            }
        }
    }

    private double map(double val, double min1, double max1, double min2, double max2) {
        return min2 + (max2 - min2) * ((val - min1) / (max1 - min1));
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        return List.of(thresholdParam, showVolumeParam, showSlicesParam, sliceXParam, sliceYParam, sliceZParam);
    }

    @Override
    public String getDescription() { return I18n.getInstance().get("viewer.voxelviewer.desc", "Scientific 3D volumetric data visualizer."); }

    @Override
    public String getLongDescription() {
        return I18n.getInstance().get("viewer.voxelviewer.longdesc", 
            "A high-fidelity 3D Voxel Viewer designed for professional scientific visualization.\n" +
            "It supports:\n" +
            "- Volumetric point-cloud rendering with density thresholding.\n" +
            "- Orthogonal slicing (Axial, Coronal, Sagittal) with 2D previews.\n" +
            "- Interactive 3D camera controls and real-time parameter adjustment.");
    }
}

