/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.ui.chemistry;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.*;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.jscience.chemistry.Atom;
import org.jscience.chemistry.MolecularGraph;
import org.jscience.chemistry.PeriodicTable;
import org.jscience.chemistry.loaders.ChemistryDataLoader;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unified Molecular and Biological Structure Viewer.
 * Consolidates MoleculeViewerDemo and MolecularViewer.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MolecularViewer extends Application {

    private final Group world = new Group();
    private final Group moleculeGroup = new Group();
    private final Rotate rx = new Rotate(0, Rotate.X_AXIS);
    private final Rotate ry = new Rotate(0, Rotate.Y_AXIS);
    private final Translate t = new Translate(0, 0, -30);

    private double mouseX, mouseY;
    private Label detailLabel = new Label("Select a molecule...");

    private static final Map<String, Color> CPK_COLORS = new HashMap<>();
    static {
        CPK_COLORS.put("H", Color.WHITE);
        CPK_COLORS.put("C", Color.DARKGREY);
        CPK_COLORS.put("N", Color.BLUE);
        CPK_COLORS.put("O", Color.RED);
        CPK_COLORS.put("S", Color.YELLOW);
        CPK_COLORS.put("P", Color.ORANGE);
        CPK_COLORS.put("Cl", Color.GREEN);
        CPK_COLORS.put("?", Color.PINK);
    }

    @Override
    public void start(Stage primaryStage) {
        ChemistryDataLoader.loadElements();

        BorderPane root = new BorderPane();

        // 3D Scene
        SubScene subScene = create3DScene();
        root.setCenter(subScene);

        // UI
        VBox controls = new VBox(10);
        controls.setPadding(new Insets(10));
        controls.setStyle("-fx-background-color: #eee;");

        ComboBox<String> selector = new ComboBox<>();
        selector.setItems(FXCollections.observableArrayList(
                "Ethanol (C2H6O)",
                "Caffeine (C8H10N4O2)",
                "Water (H2O)",
                "DNA Fragment",
                "Protein Helix"));
        selector.setValue("Ethanol (C2H6O)");
        selector.setOnAction(e -> loadModel(selector.getValue()));

        CheckBox showBonds = new CheckBox("Show Bonds");
        showBonds.setSelected(true);
        showBonds.setOnAction(e -> {
            /* Filter visuals */ }); // Not implemented yet

        detailLabel.setWrapText(true);

        controls.getChildren().addAll(new Label("Model:"), selector, showBonds, new Separator(), new Label("Details:"),
                detailLabel);
        root.setRight(controls);

        loadModel("Ethanol (C2H6O)");

        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setTitle("JScience Molecular Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Static show method for Master Demo
    public static void show(Stage stage) {
        new MolecularViewer().start(stage);
    }

    private SubScene create3DScene() {
        world.getChildren().add(moleculeGroup);
        moleculeGroup.getTransforms().addAll(t, rx, ry);

        SubScene subScene = new SubScene(world, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        subScene.setCamera(camera);

        subScene.setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        subScene.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - mouseX;
            double dy = e.getSceneY() - mouseY;
            if (e.isPrimaryButtonDown()) {
                ry.setAngle(ry.getAngle() + dx * 0.2);
                rx.setAngle(rx.getAngle() - dy * 0.2);
            } else if (e.isSecondaryButtonDown()) {
                t.setZ(t.getZ() + dy * 0.1);
            }
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        subScene.setOnScroll(e -> t.setZ(t.getZ() + (e.getDeltaY() > 0 ? 2 : -2)));

        return subScene;
    }

    private void loadModel(String name) {
        moleculeGroup.getChildren().clear();
        detailLabel.setText(name);

        if (name.contains("DNA")) {
            createDNA();
        } else if (name.contains("Protein")) {
            createProtein();
        } else {
            MolecularGraph g = null;
            if (name.contains("Ethanol"))
                g = createEthanol();
            else if (name.contains("Water"))
                g = createWater();
            else if (name.contains("Caffeine"))
                g = createCaffeine();

            if (g != null)
                renderGraph(g);
        }
    }

    private void renderGraph(MolecularGraph graph) {
        for (Atom atom : graph.getAtoms()) {
            Color color = CPK_COLORS.getOrDefault(atom.getElement().getSymbol(), CPK_COLORS.get("?"));
            double r = 0.5 + 0.1 * atom.getElement().getPeriod(); // Simple radius

            Sphere s = new Sphere(r);
            s.setMaterial(new PhongMaterial(color));

            Vector<Real> pos = atom.getPosition();
            s.setTranslateX(pos.get(0).doubleValue() * 5); // Scale up
            s.setTranslateY(pos.get(1).doubleValue() * 5);
            s.setTranslateZ(pos.get(2).doubleValue() * 5);

            s.setOnMouseClicked(e -> detailLabel.setText("Atom: " + atom.getElement().getName()));
            moleculeGroup.getChildren().add(s);
        }

        for (MolecularGraph.Bond bond : graph.getBonds()) {
            Vector<Real> p1 = bond.source.getPosition();
            Vector<Real> p2 = bond.target.getPosition();
            createBondCylinder(p1, p2);
        }
    }

    private void createBondCylinder(Vector<Real> v1, Vector<Real> v2) {
        javafx.geometry.Point3D p1 = new javafx.geometry.Point3D(v1.get(0).doubleValue() * 5,
                v1.get(1).doubleValue() * 5, v1.get(2).doubleValue() * 5);
        javafx.geometry.Point3D p2 = new javafx.geometry.Point3D(v2.get(0).doubleValue() * 5,
                v2.get(1).doubleValue() * 5, v2.get(2).doubleValue() * 5);

        javafx.geometry.Point3D diff = p2.subtract(p1);
        double len = diff.magnitude();
        javafx.geometry.Point3D mid = p1.midpoint(p2);

        Cylinder cyl = new Cylinder(0.2, len);
        cyl.setMaterial(new PhongMaterial(Color.GRAY));

        javafx.geometry.Point3D axis = diff.crossProduct(new javafx.geometry.Point3D(0, 1, 0));
        double angle = Math.acos(diff.normalize().dotProduct(new javafx.geometry.Point3D(0, 1, 0)));

        cyl.setRotationAxis(axis);
        cyl.setRotate(-Math.toDegrees(angle));
        cyl.setTranslateX(mid.getX());
        cyl.setTranslateY(mid.getY());
        cyl.setTranslateZ(mid.getZ());

        moleculeGroup.getChildren().add(cyl);
    }

    // --- Data Fabrication ---

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
        MolecularGraph g = new MolecularGraph();
        Atom c1 = new Atom(PeriodicTable.bySymbol("C"), vec(0, 0, 0));
        Atom c2 = new Atom(PeriodicTable.bySymbol("C"), vec(1.5, 0, 0));
        Atom o = new Atom(PeriodicTable.bySymbol("O"), vec(2.2, 1.2, 0));
        Atom h = new Atom(PeriodicTable.bySymbol("H"), vec(-0.5, 0.9, 0));
        g.addAtom(c1);
        g.addAtom(c2);
        g.addAtom(o);
        g.addAtom(h);
        g.addBond(c1, c2, MolecularGraph.BondType.SINGLE);
        g.addBond(c2, o, MolecularGraph.BondType.SINGLE);
        g.addBond(c1, h, MolecularGraph.BondType.SINGLE);
        return g;
    }

    private MolecularGraph createCaffeine() {
        MolecularGraph g = new MolecularGraph();
        // Simplified ring
        Atom c1 = new Atom(PeriodicTable.bySymbol("C"), vec(0, 0, 0));
        Atom n1 = new Atom(PeriodicTable.bySymbol("N"), vec(1, 0, 0));
        Atom c2 = new Atom(PeriodicTable.bySymbol("C"), vec(2, 0, 0));
        Atom n2 = new Atom(PeriodicTable.bySymbol("N"), vec(2, 1, 0));
        g.addAtom(c1);
        g.addAtom(n1);
        g.addAtom(c2);
        g.addAtom(n2);
        g.addBond(c1, n1, MolecularGraph.BondType.SINGLE);
        g.addBond(n1, c2, MolecularGraph.BondType.SINGLE);
        g.addBond(c2, n2, MolecularGraph.BondType.DOUBLE);
        g.addBond(n2, c1, MolecularGraph.BondType.SINGLE); // Close loop
        return g;
    }

    private void createDNA() {
        PhongMaterial bb = new PhongMaterial(Color.DARKBLUE);
        PhongMaterial bases = new PhongMaterial(Color.GREEN);

        for (int i = 0; i < 20; i++) {
            double a = i * 0.5;
            double y = i * 0.8 * 2;

            Sphere s1 = new Sphere(0.4);
            s1.setMaterial(bb);
            s1.setTranslateX(Math.cos(a) * 4);
            s1.setTranslateZ(Math.sin(a) * 4);
            s1.setTranslateY(y);

            Sphere s2 = new Sphere(0.4);
            s2.setMaterial(bb);
            s2.setTranslateX(Math.cos(a + Math.PI) * 4);
            s2.setTranslateZ(Math.sin(a + Math.PI) * 4);
            s2.setTranslateY(y);

            Cylinder rung = new Cylinder(0.2, 8);
            rung.setMaterial(bases);
            rung.setTranslateY(y);
            rung.setRotate(Math.toDegrees(-a));
            rung.setRotationAxis(Rotate.Y_AXIS);

            moleculeGroup.getChildren().addAll(s1, s2, rung);
        }
    }

    private void createProtein() {
        PhongMaterial mat = new PhongMaterial(Color.PURPLE);
        for (int i = 0; i < 50; i++) {
            double a = i * 0.3;
            double y = i * 0.3;
            Sphere s = new Sphere(0.6);
            s.setMaterial(mat);
            s.setTranslateX(Math.cos(a) * 3);
            s.setTranslateZ(Math.sin(a) * 3);
            s.setTranslateY(y * 4);
            moleculeGroup.getChildren().add(s);
        }
    }

    private Vector<Real> vec(double x, double y, double z) {
        List<Real> c = new ArrayList<>();
        c.add(Real.of(x));
        c.add(Real.of(y));
        c.add(Real.of(z));
        return DenseVector.of(c, Real.ZERO);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
