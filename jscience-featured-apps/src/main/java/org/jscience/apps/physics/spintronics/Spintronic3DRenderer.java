/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import org.jscience.mathematics.numbers.real.Real;

/**
 * 3D Renderer for Spintronics structures using JavaFX 3D.
 */
public class Spintronic3DRenderer {

    private final Group root = new Group();
    private final SubScene subScene;

    // 3D Objects
    private Box pinnedBox;
    private Box spacerBox;
    private Box freeBox;
    private Group pinnedArrow;
    private Group freeArrow;

    public Spintronic3DRenderer(double width, double height) {
        subScene = new SubScene(root, width, height, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#2c3e50"));

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-500);
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);
        subScene.setCamera(camera);

        setupLights();
        // Initial empty structure, wait for update
    }

    private void setupLights() {
        javafx.scene.AmbientLight ambient = new javafx.scene.AmbientLight(Color.color(0.3, 0.3, 0.3));
        javafx.scene.PointLight point = new javafx.scene.PointLight(Color.color(0.8, 0.8, 0.8));
        point.setTranslateZ(-300);
        point.setTranslateY(-200);
        root.getChildren().addAll(ambient, point);
    }



    public void rebuildStructure(SpinValve valve) {
        root.getChildren().clear();
        setupLights();

        // --- Bottom Structure (Pinned / SAF) ---
        if (valve.isSafEnabled()) {
            // SAF Bottom (Pinned 1)
            Box safari1 = new Box(200, 30, 200);
            safari1.setTranslateY(-90);
            safari1.setMaterial(new PhongMaterial(Color.web("#2c3e50"))); // Dark Blue

            // SAF Spacer (Ru)
            Box safariSpacer = new Box(200, 10, 200);
            safariSpacer.setTranslateY(-70);
            safariSpacer.setMaterial(new PhongMaterial(Color.web("#95a5a6"))); // Grey

            // SAF Top (Pinned 2 - Reference)
            Box safari2 = new Box(200, 30, 200);
            safari2.setTranslateY(-50);
            safari2.setMaterial(new PhongMaterial(Color.web("#34495e"))); // Blue

            root.getChildren().addAll(safari1, safariSpacer, safari2);
            pinnedBox = safari2; // For reference
        } else {
            // Simple Pinned
            pinnedBox = new Box(200, 40, 200);
            pinnedBox.setTranslateY(-60);
            pinnedBox.setMaterial(new PhongMaterial(Color.web("#34495e")));
            root.getChildren().add(pinnedBox);
        }

        // --- Main Spacer ---
        spacerBox = new Box(200, 20, 200);
        spacerBox.setMaterial(new PhongMaterial(Color.web("#e67e22"))); // Copper color

        // --- Free Layer ---
        freeBox = new Box(200, 40, 200);
        freeBox.setTranslateY(60);
        freeBox.setMaterial(new PhongMaterial(Color.web("#c0392b"))); // Red

        // --- Arrows ---
        pinnedArrow = createArrow(Color.WHITE);
        pinnedArrow.setTranslateY(valve.isSafEnabled() ? -50 : -60);
        pinnedArrow.setTranslateZ(-110);

        freeArrow = createArrow(Color.YELLOW);
        freeArrow.setTranslateY(60);
        freeArrow.setTranslateZ(-110);

        root.getChildren().addAll(spacerBox, freeBox, pinnedArrow, freeArrow);

        // Interaction
        root.setRotationAxis(Rotate.Y_AXIS);
        root.setRotate(20);
    }

    private Group createArrow(Color color) {
        Group arrow = new Group();
        Cylinder shaft = new Cylinder(2, 40);
        shaft.setMaterial(new PhongMaterial(color));

        javafx.scene.shape.Sphere head = new javafx.scene.shape.Sphere(6);
        head.setTranslateY(-20);
        head.setMaterial(new PhongMaterial(color));

        arrow.getChildren().addAll(shaft, head);
        arrow.setRotationAxis(Rotate.Z_AXIS);
        return arrow;
    }

    public void update(SpinValve valve) {
        // Update rotations based on magnetization vectors
        Real[] mP = valve.getPinnedLayer().getMagnetization();
        Real[] mF = valve.getFreeLayer().getMagnetization();

        updateArrowRotation(pinnedArrow, mP);
        updateArrowRotation(freeArrow, mF);
    }

    private void updateArrowRotation(Group arrow, Real[] m) {
        if (arrow == null) return;
        // Simple 2D-to-3D projection of the arrow for this demo
        double angle = Math.toDegrees(Math.atan2(m[1].doubleValue(), m[0].doubleValue()));
        arrow.setRotate(angle + 90);
    }

    public SubScene getSubScene() {
        return subScene;
    }
}
