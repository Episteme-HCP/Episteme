package org.jscience.ui.chemistry;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Molecule;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.HashMap;
import java.util.Map;

/**
 * A viewer for molecular structures.
 * Supports both 2D (Canvas) and 3D (JavaFX 3D) visualizations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class MolecularViewer {

    private final Molecule molecule;
    private static final Map<String, Color> CPK_COLORS = new HashMap<>();

    // Scale factor for coordinates (Meters -> Screen/World units)
    // Angstroms usually used in viewers.
    // If coords are in meters (1e-10), we multiply by 1e10 * scale.
    // Let's assume we convert to logical Angstroms for display.
    private static final double COORD_CONVERSION = 1e10; // Meters to Angstroms

    static {
        CPK_COLORS.put("H", Color.WHITE);
        CPK_COLORS.put("C", Color.DARKGREY);
        CPK_COLORS.put("N", Color.BLUE);
        CPK_COLORS.put("O", Color.RED);
        CPK_COLORS.put("S", Color.YELLOW);
        CPK_COLORS.put("P", Color.ORANGE);
        CPK_COLORS.put("Cl", Color.GREEN);
        CPK_COLORS.put("F", Color.LIGHTGREEN);
        CPK_COLORS.put("Br", Color.DARKRED);
        CPK_COLORS.put("I", Color.PURPLE);
        CPK_COLORS.put("He", Color.CYAN);
        CPK_COLORS.put("Ne", Color.CYAN);
        CPK_COLORS.put("Fe", Color.ORANGERED);
        // Default
        CPK_COLORS.put("?", Color.PINK);
    }

    public MolecularViewer(Molecule molecule) {
        this.molecule = molecule;
    }

    /**
     * Creates a 2D Canvas representation.
     * Projects 3D coordinates to 2D (X/Y).
     */
    public Node getView2D(double width, double height) {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Basic centering logic
        double cx = width / 2;
        double cy = height / 2;
        double viewScale = 30.0; // Pixels per Angstrom

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);

        // Draw Bonds first (behind atoms)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2.0);

        for (Bond bond : molecule.getBonds()) {
            Atom a1 = bond.getAtom1();
            Atom a2 = bond.getAtom2();

            double x1 = getX(a1) * viewScale + cx;
            double y1 = getY(a1) * viewScale + cy;
            double x2 = getX(a2) * viewScale + cx;
            double y2 = getY(a2) * viewScale + cy;

            gc.strokeLine(x1, y1, x2, y2);
        }

        // Draw Atoms
        for (Atom atom : molecule.getAtoms()) {
            double x = getX(atom) * viewScale + cx;
            double y = getY(atom) * viewScale + cy;
            double r = getRadius(atom) * viewScale;

            Color c = getColor(atom);
            gc.setFill(c);
            gc.fillOval(x - r, y - r, r * 2, r * 2);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1.0);
            gc.strokeOval(x - r, y - r, r * 2, r * 2);

            // Label
            gc.setFill(Color.BLACK);
            // Center text roughly
            gc.fillText(atom.getElement().getSymbol(), x - 3, y + 4);
        }

        return canvas;
    }

    /**
     * Creates a 3D SubScene representation.
     */
    public SubScene getView3D(double width, double height) {
        Group root = new Group();
        Group moleculeGroup = new Group();

        // Draw Atoms
        for (Atom atom : molecule.getAtoms()) {
            double r = getRadius(atom); // Angstroms
            Sphere sphere = new Sphere(r * 0.4); // Scale down slightly for stick visibility

            Color c = getColor(atom);
            PhongMaterial material = new PhongMaterial(c);
            material.setSpecularColor(Color.WHITE);
            sphere.setMaterial(material);

            sphere.setTranslateX(getX(atom));
            sphere.setTranslateY(getY(atom));
            sphere.setTranslateZ(getZ(atom));

            moleculeGroup.getChildren().add(sphere);
        }

        // Draw Bonds
        for (Bond bond : molecule.getBonds()) {
            Atom a1 = bond.getAtom1();
            Atom a2 = bond.getAtom2();

            Node cyl = createBondCylinder(a1, a2);
            moleculeGroup.getChildren().add(cyl);
        }

        root.getChildren().add(moleculeGroup);

        // Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        camera.setTranslateZ(-30);

        SubScene subScene = new SubScene(root, width, height, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        // Simple Mouse Rotation
        initMouseControl(moleculeGroup, subScene);

        return subScene;
    }

    // --- Helpers ---

    private Node createBondCylinder(Atom a1, Atom a2) {
        javafx.geometry.Point3D p1 = new javafx.geometry.Point3D(getX(a1), getY(a1), getZ(a1));
        javafx.geometry.Point3D p2 = new javafx.geometry.Point3D(getX(a2), getY(a2), getZ(a2));
        javafx.geometry.Point3D diff = p2.subtract(p1);
        double len = diff.magnitude();

        javafx.geometry.Point3D mid = p1.midpoint(p2);

        // Cylinder aligned along Y axis by default
        Cylinder cylinder = new Cylinder(0.1, len);
        PhongMaterial mat = new PhongMaterial(Color.GRAY);
        cylinder.setMaterial(mat);

        // Rotation logic to align cylinder (Y-axis) with diff vector
        javafx.geometry.Point3D yAxis = new javafx.geometry.Point3D(0, 1, 0);
        javafx.geometry.Point3D axisOfRotation = yAxis.crossProduct(diff);
        double angle = Math.toDegrees(Math.acos(yAxis.dotProduct(diff) / len));

        cylinder.getTransforms().addAll(
                new Translate(mid.getX(), mid.getY(), mid.getZ()),
                new Rotate(angle, axisOfRotation));

        return cylinder;
    }

    private void initMouseControl(Group group, SubScene scene) {
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        group.getTransforms().addAll(xRotate, yRotate);

        final double[] anchor = new double[2];

        scene.setOnMousePressed(event -> {
            anchor[0] = event.getSceneX();
            anchor[1] = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            double dx = event.getSceneX() - anchor[0];
            double dy = event.getSceneY() - anchor[1];

            yRotate.setAngle(yRotate.getAngle() + dx);
            xRotate.setAngle(xRotate.getAngle() - dy);

            anchor[0] = event.getSceneX();
            anchor[1] = event.getSceneY();
        });
    }

    private double getX(Atom a) {
        return a.getPosition().get(0).doubleValue() * COORD_CONVERSION;
    }

    private double getY(Atom a) {
        return a.getPosition().get(1).doubleValue() * COORD_CONVERSION;
    }

    private double getZ(Atom a) {
        return (a.getPosition().dimension() > 2 ? a.getPosition().get(2).doubleValue() : 0.0) * COORD_CONVERSION;
    }

    private double getRadius(Atom a) {
        // Heuristic based on atomic number or category
        // Could use real VDW radii if added to Element
        return 0.5 + 0.1 * a.getElement().getPeriod();
    }

    private Color getColor(Atom a) {
        return CPK_COLORS.getOrDefault(a.getElement().getSymbol(), CPK_COLORS.get("?"));
    }
}
