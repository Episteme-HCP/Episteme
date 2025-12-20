package org.jscience.ui.chemistry;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.MolecularGraph;
import org.jscience.chemistry.PeriodicTable;
import org.jscience.chemistry.loaders.ChemistryDataLoader;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Unified Molecule and Biological Structure Viewer.
 * Features:
 * - Professional 3D Rendering (Bonds, Atoms)
 * - Combo Box Switching (Molecules, DNA, Amino Acids)
 * - Interactive Details
 * - Sheets/Coils Visualization (Mockup for Proteins)
 */
public class MoleculeViewerDemo extends Application {

    private final Group world = new Group();
    private final Rotate rx = new Rotate(0, new javafx.geometry.Point3D(1, 0, 0));
    private final Rotate ry = new Rotate(0, new javafx.geometry.Point3D(0, 1, 0));
    private final Translate t = new Translate();
    private double mouseX, mouseY;

    private final Label detailLabel = new Label("Hover over an atom...");

    @Override
    public void start(Stage primaryStage) {
        ChemistryDataLoader.loadElements();

        // Debug: Check Element Count
        System.out.println("DEBUG: Loaded " + PeriodicTable.getElementCount() + " elements.");
        Element rf = PeriodicTable.bySymbol("Rf");
        System.out.println("DEBUG: Rutherfordium (104) loaded: " + (rf != null));

        BorderPane root = new BorderPane();

        // --- 3D Scene ---
        SubScene subScene = create3DScene();
        root.setCenter(subScene);

        // --- Controls ---
        VBox controls = new VBox(10);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setStyle("-fx-background-color: #f0f0f0;");

        ComboBox<String> modelSelector = new ComboBox<>();
        modelSelector.setItems(FXCollections.observableArrayList(
                "Ethanol (C2H6O)",
                "Caffeine (C8H10N4O2)",
                "Water (H2O)",
                "DNA Fragment (Sample)",
                "Protein Alpha-Helix (Sample)"));
        modelSelector.setValue("Ethanol (C2H6O)");
        modelSelector.setOnAction(e -> loadModel(modelSelector.getValue()));

        CheckBox showBonds = new CheckBox("Show Bonds");
        showBonds.setSelected(true);
        showBonds.setOnAction(e -> refreshView(showBonds.isSelected()));

        detailLabel.setWrapText(true);
        detailLabel.setPrefWidth(200);

        controls.getChildren().addAll(new Label("Select Model:"), modelSelector, showBonds, new Separator(),
                new Label("Details:"), detailLabel);
        root.setLeft(controls);

        // Initial Load
        loadModel("Ethanol (C2H6O)");

        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setTitle("JScience Molecule & Bio-Structure Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private SubScene create3DScene() {
        world.getTransforms().addAll(t, rx, ry);

        SubScene subScene = new SubScene(world, 800, 768, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        camera.setTranslateZ(-50);
        subScene.setCamera(camera);

        // Mouse Handling
        subScene.setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        subScene.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - mouseX;
            double dy = e.getSceneY() - mouseY;
            if (e.isPrimaryButtonDown()) {
                ry.setAngle(ry.getAngle() - dx * 0.2);
                rx.setAngle(rx.getAngle() + dy * 0.2);
            } else if (e.isSecondaryButtonDown()) {
                t.setZ(t.getZ() + dy * 0.5);
            }
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        return subScene;
    }

    private void loadModel(String modelName) {
        world.getChildren().clear();
        MolecularGraph graph = null;

        if (modelName.contains("Ethanol")) {
            graph = createEthanol();
        } else if (modelName.contains("Caffeine")) {
            graph = createCaffeine();
        } else if (modelName.contains("Water")) {
            graph = createWater();
        } else if (modelName.contains("DNA")) {
            createDNAVisualization(); // Special rendering
            return;
        } else if (modelName.contains("Protein")) {
            createProteinVisualization(); // Special rendering
            return;
        }

        if (graph != null) {
            renderGraph(graph);
        }
    }

    private void renderGraph(MolecularGraph graph) {
        PhongMaterial bondMaterial = new PhongMaterial(Color.LIGHTGRAY);

        for (Atom atom : graph.getAtoms()) {
            Sphere sphere = new Sphere(getAtomRadius(atom));
            sphere.setMaterial(getAtomMaterial(atom));

            // Assuming Atom position is stored in its Vector<Real>
            // We need to ensure Atom has valid coordinates.
            // Since we are creating them manually, we will populate them.
            Vector<Real> pos = atom.getPosition();
            sphere.setTranslateX(pos.get(0).doubleValue() * 10);
            sphere.setTranslateY(pos.get(1).doubleValue() * 10);
            sphere.setTranslateZ(pos.get(2).doubleValue() * 10);

            sphere.setOnMouseClicked(e -> {
                String info = atom.getElement().getName() + " (" + atom.getElement().getSymbol() + ")\n" +
                        "Mass: " + atom.getElement().getAtomicMass();
                detailLabel.setText(info);
            });

            // Hover effect
            sphere.setOnMouseEntered(e -> sphere.setScaleX(1.1));
            sphere.setOnMouseExited(e -> sphere.setScaleX(1.0));

            world.getChildren().add(sphere);
        }

        // Bonds
        for (MolecularGraph.Bond bond : graph.getBonds()) {
            createBond(bond.source, bond.target);
        }
    }

    // --- Sample Data Creators ---

    private MolecularGraph createWater() {
        MolecularGraph g = new MolecularGraph();
        Atom o = new Atom(PeriodicTable.bySymbol("O"), vec(0, 0, 0));
        Atom h1 = new Atom(PeriodicTable.bySymbol("H"), vec(0.75, 0.58, 0));
        Atom h2 = new Atom(PeriodicTable.bySymbol("H"), vec(-0.75, 0.58, 0));
        g.addAtom(o);
        g.addAtom(h1);
        g.addAtom(h2);
        g.addBond(o, h1, MolecularGraph.BondType.SINGLE);
        g.addBond(o, h2, MolecularGraph.BondType.SINGLE);
        return g;
    }

    private MolecularGraph createEthanol() {
        // C2H6O
        MolecularGraph g = new MolecularGraph();
        Atom c1 = new Atom(PeriodicTable.bySymbol("C"), vec(0, 0, 0));
        Atom c2 = new Atom(PeriodicTable.bySymbol("C"), vec(1.5, 0, 0));
        Atom o = new Atom(PeriodicTable.bySymbol("O"), vec(2.2, 1.2, 0));
        Atom h1 = new Atom(PeriodicTable.bySymbol("H"), vec(-0.5, 0.9, 0)); // attached to C1
        Atom h2 = new Atom(PeriodicTable.bySymbol("H"), vec(-0.5, -0.9, 0)); // attached to C1
        Atom h3 = new Atom(PeriodicTable.bySymbol("H"), vec(0, 0, 1)); // attached to C1
        // Simplified coords
        g.addAtom(c1);
        g.addAtom(c2);
        g.addAtom(o);
        g.addAtom(h1);
        g.addAtom(h2);
        g.addAtom(h3);

        g.addBond(c1, c2, MolecularGraph.BondType.SINGLE);
        g.addBond(c2, o, MolecularGraph.BondType.SINGLE);
        g.addBond(c1, h1, MolecularGraph.BondType.SINGLE);
        return g;
    }

    private MolecularGraph createCaffeine() {
        // Placeholder coords for caffeine
        MolecularGraph g = new MolecularGraph();
        Atom n1 = new Atom(PeriodicTable.bySymbol("N"), vec(0, 0, 0));
        Atom c1 = new Atom(PeriodicTable.bySymbol("C"), vec(1.4, 0, 0));
        // ... simplified
        g.addAtom(n1);
        g.addAtom(c1);
        g.addBond(n1, c1, MolecularGraph.BondType.SINGLE);
        return g;
    }

    private void createDNAVisualization() {
        // Double Helix Mockup
        PhongMaterial backboneMat = new PhongMaterial(Color.DARKBLUE);
        PhongMaterial baseMatA = new PhongMaterial(Color.GREEN);
        PhongMaterial baseMatT = new PhongMaterial(Color.RED);

        for (int i = 0; i < 20; i++) {
            double angle = i * 0.5;
            double y = i * 0.8;

            // Strand 1
            Sphere s1 = new Sphere(0.4);
            s1.setMaterial(backboneMat);
            s1.setTranslateX(Math.cos(angle) * 4);
            s1.setTranslateZ(Math.sin(angle) * 4);
            s1.setTranslateY(y * 2);
            world.getChildren().add(s1);

            // Strand 2
            Sphere s2 = new Sphere(0.4);
            s2.setMaterial(backboneMat);
            s2.setTranslateX(Math.cos(angle + Math.PI) * 4);
            s2.setTranslateZ(Math.sin(angle + Math.PI) * 4);
            s2.setTranslateY(y * 2);
            world.getChildren().add(s2);

            // Rungs (Base Pairs)
            Cylinder rung = new Cylinder(0.2, 8);
            rung.setMaterial(i % 2 == 0 ? baseMatA : baseMatT);
            rung.setTranslateX(0); // Center
            rung.setTranslateY(y * 2);
            rung.setTranslateZ(0);
            // Rotate to match angle
            Rotate r = new Rotate(Math.toDegrees(-angle), new javafx.geometry.Point3D(0, 1, 0));
            rung.getTransforms().addAll(new Translate(0, 0, 0), r,
                    new Rotate(90, new javafx.geometry.Point3D(0, 0, 1)));

            // Simplification: just draw lines or cylinders between s1 and s2
            createBond(new javafx.geometry.Point3D(s1.getTranslateX(), s1.getTranslateY(), s1.getTranslateZ()),
                    new javafx.geometry.Point3D(s2.getTranslateX(), s2.getTranslateY(), s2.getTranslateZ()));
        }
    }

    private void createProteinVisualization() {
        // Alpha Helix Ribbon (Sheets/Coils)
        // Represented as a series of connected cylinders forming a spiral
        PhongMaterial coilMat = new PhongMaterial(Color.PURPLE);
        double radius = 3.0;

        javafx.geometry.Point3D prev = null;

        for (int i = 0; i < 50; i++) {
            double angle = i * 0.3;
            double height = i * 0.4;
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;

            javafx.geometry.Point3D curr = new javafx.geometry.Point3D(x, height, z);

            if (prev != null) {
                // Draw ribbon segment
                Cylinder seg = createCylinderBetween(prev, curr, 0.3);
                seg.setMaterial(coilMat);
                world.getChildren().add(seg);
            }
            prev = curr;
        }
    }

    private void createBond(Atom a, Atom b) {
        Vector<Real> posA = a.getPosition();
        Vector<Real> posB = b.getPosition();
        javafx.geometry.Point3D pA = new javafx.geometry.Point3D(posA.get(0).doubleValue() * 10,
                posA.get(1).doubleValue() * 10, posA.get(2).doubleValue() * 10);
        javafx.geometry.Point3D pB = new javafx.geometry.Point3D(posB.get(0).doubleValue() * 10,
                posB.get(1).doubleValue() * 10, posB.get(2).doubleValue() * 10);

        Cylinder bond = createCylinderBetween(pA, pB, 0.15); // Bond thickness
        bond.setMaterial(new PhongMaterial(Color.GRAY));
        world.getChildren().add(bond);
    }

    private void createBond(javafx.geometry.Point3D pA, javafx.geometry.Point3D pB) {
        Cylinder bond = createCylinderBetween(pA, pB, 0.15); // Bond thickness
        bond.setMaterial(new PhongMaterial(Color.LIGHTBLUE));
        world.getChildren().add(bond);
    }

    private Cylinder createCylinderBetween(javafx.geometry.Point3D p1, javafx.geometry.Point3D p2, double radius) {
        javafx.geometry.Point3D diff = p2.subtract(p1);
        double length = diff.magnitude();
        javafx.geometry.Point3D mid = p1.midpoint(p2);

        Cylinder cylinder = new Cylinder(radius, length);
        cylinder.setTranslateX(mid.getX());
        cylinder.setTranslateY(mid.getY());
        cylinder.setTranslateZ(mid.getZ());

        javafx.geometry.Point3D axis = diff.crossProduct(new javafx.geometry.Point3D(0, 1, 0));
        double angle = Math.acos(diff.normalize().dotProduct(new javafx.geometry.Point3D(0, 1, 0)));
        cylinder.setRotationAxis(axis);
        cylinder.setRotate(-Math.toDegrees(angle));

        return cylinder;
    }

    private double getAtomRadius(Atom atom) {
        // Simplified radii based on atomic number or pre-defined map
        String symbol = atom.getElement().getSymbol();
        if (symbol.equals("H"))
            return 0.5;
        if (symbol.equals("C"))
            return 0.8;
        if (symbol.equals("N"))
            return 0.8;
        if (symbol.equals("O"))
            return 0.8;
        return 1.0;
    }

    private PhongMaterial getAtomMaterial(Atom atom) {
        String symbol = atom.getElement().getSymbol();
        Color c = Color.GRAY;
        switch (symbol) {
            case "H":
                c = Color.WHITE;
                break;
            case "C":
                c = Color.BLACK;
                break;
            case "N":
                c = Color.BLUE;
                break;
            case "O":
                c = Color.RED;
                break;
        }
        return new PhongMaterial(c);
    }

    // Helper to create vector from doubles
    private Vector<Real> vec(double x, double y, double z) {
        List<Real> coords = new ArrayList<>();
        coords.add(Real.of(x));
        coords.add(Real.of(y));
        coords.add(Real.of(z));
        return DenseVector.of(coords, Real.ZERO);
    }

    private void refreshView(boolean showBonds) {
        // Re-render
    }

    public static void main(String[] args) {
        launch(args);
    }
}
