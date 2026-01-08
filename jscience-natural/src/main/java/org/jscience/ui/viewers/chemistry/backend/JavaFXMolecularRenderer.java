package org.jscience.ui.viewers.chemistry.backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;

import java.util.HashMap;
import java.util.Map;

public class JavaFXMolecularRenderer implements MolecularRenderer {

    private final Group world = new Group();
    private final Group moleculeGroup = new Group();
    private final Rotate rx = new Rotate(0, Rotate.X_AXIS);
    private final Rotate ry = new Rotate(0, Rotate.Y_AXIS);
    private final Translate t = new Translate(0, 0, -30); // Default zoom
    private SubScene subScene;
    private RenderStyle currentStyle = RenderStyle.BALL_AND_STICK;

    // Cache for materials to improve performance
    private final Map<String, PhongMaterial> materialCache = new HashMap<>();
    private final Map<String, Double> vdwCache = new HashMap<>();

    public JavaFXMolecularRenderer() {
        initScene();
        initDefaultData();
    }

    private void initScene() {
        moleculeGroup.getTransforms().setAll(t, rx, ry);
        world.getChildren().setAll(moleculeGroup, new javafx.scene.AmbientLight(Color.DARKGRAY),
                new javafx.scene.PointLight(Color.WHITE));

        // Default size, will resize if put in a Pane
        subScene = new SubScene(world, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);

        PerspectiveCamera cam = new PerspectiveCamera(true);
        cam.setNearClip(0.1);
        cam.setFarClip(1000.0);
        cam.setTranslateZ(-50);
        subScene.setCamera(cam);

        setupMouseControl();
    }

    private double mouseX, mouseY;

    private void setupMouseControl() {
        subScene.setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        subScene.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - mouseX, dy = e.getSceneY() - mouseY;
            if (e.isSecondaryButtonDown()) {
                t.setX(t.getX() + dx * 0.1);
                t.setY(t.getY() + dy * 0.1);
            } else {
                ry.setAngle(ry.getAngle() + dx * 0.5);
                rx.setAngle(rx.getAngle() - dy * 0.5);
            }
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        subScene.setOnScroll(e -> t.setZ(t.getZ() + (e.getDeltaY() > 0 ? 2 : -2)));
    }

    private void initDefaultData() {
        // CPK Colors
        cacheMaterial("H", Color.WHITE);
        cacheMaterial("C", Color.DARKGREY);
        cacheMaterial("N", Color.BLUE);
        cacheMaterial("O", Color.RED);
        cacheMaterial("S", Color.YELLOW);
        cacheMaterial("P", Color.ORANGE);
        cacheMaterial("Cl", Color.GREEN);
        cacheMaterial("Na", Color.PURPLE);
        cacheMaterial("?", Color.PINK);

        // VDW Radii
        vdwCache.put("H", 1.2);
        vdwCache.put("C", 1.7);
        vdwCache.put("N", 1.55);
        vdwCache.put("O", 1.52);
        vdwCache.put("P", 1.8);
        vdwCache.put("S", 1.8);
        vdwCache.put("Cl", 1.75);
        vdwCache.put("Na", 2.27);
    }

    private void cacheMaterial(String symbol, Color c) {
        materialCache.put(symbol, new PhongMaterial(c));
    }

    @Override
    public void clear() {
        moleculeGroup.getChildren().clear();
    }

    @Override
    public void setStyle(RenderStyle style) {
        this.currentStyle = style;
        // Re-rendering happens when drawAtom/drawBond is called externally,
        // or we could store the model and re-render.
        // For simplicity, we assume the controller calls clear() then re-draws when
        // style changes.
    }

    @Override
    public void drawAtom(Atom atom) {
        if (currentStyle == RenderStyle.WIREFRAME)
            return;

        String symbol = atom.getElement().getSymbol();
        double radius = getRadius(symbol);

        Sphere s = new Sphere(radius);
        s.setMaterial(materialCache.getOrDefault(symbol, materialCache.get("?")));

        Vector<Real> pos = atom.getPosition();
        s.setTranslateX(pos.get(0).doubleValue());
        s.setTranslateY(pos.get(1).doubleValue());
        s.setTranslateZ(pos.get(2).doubleValue());

        moleculeGroup.getChildren().add(s);
    }

    private double getRadius(String symbol) {
        double vdw = vdwCache.getOrDefault(symbol, 1.5);
        switch (currentStyle) {
            case SPACEFILL:
                return vdw;
            case BALL_AND_STICK:
                return vdw * 0.25; // 25% of VDW
            default:
                return vdw;
        }
    }

    @Override
    public void drawBond(Bond bond) {
        if (currentStyle == RenderStyle.SPACEFILL)
            return; // Usually hidden in spacefill

        Vector<Real> p1 = bond.getAtom1().getPosition();
        Vector<Real> p2 = bond.getAtom2().getPosition();

        javafx.geometry.Point3D v1 = new javafx.geometry.Point3D(p1.get(0).doubleValue(), p1.get(1).doubleValue(),
                p1.get(2).doubleValue());
        javafx.geometry.Point3D v2 = new javafx.geometry.Point3D(p2.get(0).doubleValue(), p2.get(1).doubleValue(),
                p2.get(2).doubleValue());

        double radius = (currentStyle == RenderStyle.WIREFRAME) ? 0.05 : 0.15;

        javafx.geometry.Point3D diff = v2.subtract(v1);
        double len = diff.magnitude();

        Cylinder c = new Cylinder(radius, len);
        c.setMaterial(new PhongMaterial(Color.LIGHTGRAY)); // Default bond color

        c.setTranslateX(v1.midpoint(v2).getX());
        c.setTranslateY(v1.midpoint(v2).getY());
        c.setTranslateZ(v1.midpoint(v2).getZ());

        javafx.geometry.Point3D axis = diff.crossProduct(new javafx.geometry.Point3D(0, 1, 0));
        double angle = Math.acos(diff.normalize().dotProduct(new javafx.geometry.Point3D(0, 1, 0)));
        c.setRotationAxis(axis.magnitude() < 1e-4 ? new javafx.geometry.Point3D(1, 0, 0) : axis);
        c.setRotate(-Math.toDegrees(angle));

        moleculeGroup.getChildren().add(c);
    }

    @Override
    public void setBackgroundColor(Color color) {
        subScene.setFill(color);
    }

    @Override
    public Object getViewComponent() {
        return subScene;
    }

    @Override
    public MolecularBackend getBackend() {
        return MolecularBackend.JAVAFX;
    }
}
