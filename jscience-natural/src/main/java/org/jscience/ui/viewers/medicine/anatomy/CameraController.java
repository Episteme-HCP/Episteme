package org.jscience.ui.viewers.medicine.anatomy;

import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;

import javafx.scene.SubScene;

import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Manages 3D camera navigation: Orbit, Zoom, Pan, and Framing.
 */
@SuppressWarnings("unused")
public class CameraController {

    private final Camera camera;
    private final Group cameraXform; // The node to transform (usually a Group holding the Camera)
    private final Group cameraXform2; // Nested group for rotation
    private final Group cameraXform3; // Nested group for rotation

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Translate translate = new Translate(0, 0, -500); // Initial zoom

    // Constructor using internal hierarchy management relative to rootGroup
    public CameraController(Camera camera, Group rootGroup) {
        this.camera = camera;
        this.cameraXform = new Group();
        this.cameraXform2 = new Group();
        this.cameraXform3 = new Group();
        
        rootGroup.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        
        cameraXform.getTransforms().addAll(rotateY, rotateX); // Rotate Camera pivot
        camera.getTransforms().add(translate); // Zoom (distance from pivot)
        
        // Initial Pose
        rotateX.setAngle(-10);
        rotateY.setAngle(180);
    }

    public void handleMouseEvents(SubScene scene) {
        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });

        scene.setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            if (me.isPrimaryButtonDown()) {
                // Orbit
                rotateY.setAngle(rotateY.getAngle() + mouseDeltaX * 0.5);
                rotateX.setAngle(rotateX.getAngle() - mouseDeltaY * 0.5);
            } else if (me.isSecondaryButtonDown() || me.isMiddleButtonDown()) {
                // Pan (Move pivot)
                 double panFactor = 0.5; // Scale by zoom level in real app
                 cameraXform.setTranslateX(cameraXform.getTranslateX() - mouseDeltaX * panFactor); // Inverted for drag
                 cameraXform.setTranslateY(cameraXform.getTranslateY() - mouseDeltaY * panFactor);
            }
        });

        scene.setOnScroll(se -> {
            double zoomFactor = 1.05;
            double deltaY = se.getDeltaY();
            if (deltaY > 0) {
                zoomFactor = 1 / zoomFactor;
            }
            
            // Move camera closer/further
            double newZ = translate.getZ() * zoomFactor;
            // Clamp zoom
            if (newZ < -5 && newZ > -3000) {
                 translate.setZ(newZ);
            }
        });
    }

    public void reset() {
        rotateX.setAngle(0);
        rotateY.setAngle(180);
        translate.setZ(-500);
        cameraXform.setTranslateX(0);
        cameraXform.setTranslateY(0);
        cameraXform.setTranslateZ(0);
    }
    
    public void centerOn(Point3D point) {
        // Animate or set pivot to point
         cameraXform.setTranslateX(point.getX());
         cameraXform.setTranslateY(point.getY());
         cameraXform.setTranslateZ(point.getZ());
         // Keeping Z (depth) same or framing could be added
    }
}
