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
import javafx.scene.effect.Glow;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import org.jscience.mathematics.numbers.real.Real;
import javafx.scene.effect.Bloom;

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
    private Box heavyMetalElectrode;
    private Group pinnedArrow;
    private Group freeArrow;

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Group world = new Group();

    public Spintronic3DRenderer(double width, double height) {
        subScene = new SubScene(world, width, height, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#2c3e50"));
        world.getChildren().add(root);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-500);
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);
        subScene.setCamera(camera);

        root.getTransforms().addAll(rotateX, rotateY);
        rotateX.setAngle(-20);
        rotateY.setAngle(20);

        setupLights();
        setupMouseControls();
    }

    private void setupMouseControls() {
        subScene.setOnMousePressed(event -> {
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            mouseOldX = event.getSceneX();
            mouseOldY = event.getSceneY();
        });

        subScene.setOnMouseDragged(event -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            double mouseDeltaX = (mousePosX - mouseOldX);
            double mouseDeltaY = (mousePosY - mouseOldY);

            if (event.isPrimaryButtonDown()) {
                rotateY.setAngle(rotateY.getAngle() + mouseDeltaX * 0.2);
                rotateX.setAngle(rotateX.getAngle() - mouseDeltaY * 0.2);
            }
        });

        subScene.setOnScroll(event -> {
            double delta = event.getDeltaY();
            subScene.getCamera().setTranslateZ(subScene.getCamera().getTranslateZ() + delta);
        });
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

        // --- Heavy Metal Electrode (SOT) ---
        heavyMetalElectrode = new Box(500, 10, 200);
        heavyMetalElectrode.setTranslateY(100);
        heavyMetalElectrode.setMaterial(new PhongMaterial(Color.web("#7f8c8d"))); // Platinum Grey
        root.getChildren().add(heavyMetalElectrode);

        // --- Free Layer (Now closer to electrode if needed, but let's keep stack order) ---
        // Let's assume electrode is at the bottom of the stack
        // Stack Y: Pinned -> Spacer -> Free -> Electrode
        // We need to shift everything.
        
        // Electrode at Y=80
        heavyMetalElectrode.setTranslateY(80);
        
        // Free Layer at Y=50
        freeBox = new Box(200, 30, 200);
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

    public void update(SpinValve valve, double current) {
        // Update rotations based on magnetization vectors
        Real[] mP = valve.getPinnedLayer().getMagnetization();
        Real[] mF = valve.getFreeLayer().getMagnetization();

        if (valve.isSafEnabled()) {
             mP = valve.getSafPinnedLayer2().getMagnetization();
        }

        updateArrowRotation(pinnedArrow, mP);
        updateArrowRotation(freeArrow, mF);
        
        applyGlowEffect(current);
    }
    
    private void applyGlowEffect(double current) {
        if (freeBox == null) return;
        
        // Normalize current (typical max 1-10mA for these devices)
        double absI = Math.abs(current);
        double intensity = Math.min(1.0, absI * 1000.0); // 1mA = 1.0 glow intensity
        
        PhongMaterial mat = (PhongMaterial) freeBox.getMaterial();
        Color baseColor = Color.web("#c0392b"); // Red
        
        // Increase brightness and specularity with current
        mat.setDiffuseColor(baseColor.deriveColor(0, 1.0, 1.0 + intensity, 1.0));
        mat.setSpecularColor(Color.WHITE.deriveColor(0, 1.0, 1.0, intensity));
    }

    private void updateArrowRotation(Group arrow, Real[] m) {
        if (arrow == null) return;
        
        // Magnetization vector M = (mx, my, mz)
        double mx = m[0].doubleValue();
        double my = m[1].doubleValue();
        double mz = m[2].doubleValue();
        
        // Arrow points UP by default (Y-axis in JavaFX cylinder), but usually M along X is 0 angle.
        // Let's align arrow along X axis by default or rotate it properly.
        // Standard JavaFX Cylinder axis is Y. Rotation needed to align with M.
        
        // Spherical coordinates
        double r = Math.sqrt(mx*mx + my*my + mz*mz);
        if (r < 1e-6) return;
        
        // Angle in XY plane (Azimuth)
        double phi = Math.atan2(my, mx);
        // Angle from Z axis (Inclination) - but here Y is Up/Down structure stack.
        // Let's assume M is mostly in plane (XZ plane relative to user view? or XY?).
        // In the app, layers are stacked on Y. magnetization is in plane (XZ).
        
        // Let's just rotate around Y axis (Azimuth).
        double angleDeg = Math.toDegrees(phi);
        
        // Reset transforms
        arrow.getTransforms().clear();
        
        // Rotate to match direction. 
        // Initial arrow is Y-aligned. Rotate -90 on Z to make it X-aligned.
        arrow.getTransforms().add(new Rotate(-90, Rotate.Z_AXIS)); 
        // Then rotate around Y (vertical axis of stack) by phi
        arrow.getTransforms().add(new Rotate(-angleDeg, Rotate.Y_AXIS));
    }

    public SubScene getSubScene() {
        return subScene;
    }
}
