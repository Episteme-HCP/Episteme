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

package org.jscience.ui.chemistry;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Bond;
import org.jscience.chemistry.Molecule;
import org.jscience.chemistry.MolecularDynamics;
import org.jscience.ui.i18n.I18n;
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
 * Refactored to use MolecularDynamics engine for simulation.
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
    private final Translate t = new Translate(0, 0, 30);

    private double mouseX, mouseY;
    private Label detailLabel = new Label("Select a molecule...");
    private boolean showOrbitals = false;
    private boolean planarMode = false;

    private static final Map<String, Color> CPK_COLORS = new HashMap<>();
    private static final Map<String, Double> VDW_RADII = new HashMap<>();

    static {
        CPK_COLORS.put("H", Color.WHITE);
        CPK_COLORS.put("C", Color.DARKGREY);
        CPK_COLORS.put("N", Color.BLUE);
        CPK_COLORS.put("O", Color.RED);
        CPK_COLORS.put("S", Color.YELLOW);
        CPK_COLORS.put("P", Color.ORANGE);
        CPK_COLORS.put("?", Color.PINK);
        VDW_RADII.put("C", 1.7);
        VDW_RADII.put("H", 1.2);
        VDW_RADII.put("O", 1.5);
        VDW_RADII.put("N", 1.55);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        SubScene subScene = create3DScene();
        root.setCenter(subScene);

        VBox controls = new VBox(15);
        controls.setPadding(new Insets(15));
        controls.setStyle("-fx-background-color: #eee;");
        controls.setPrefWidth(280);

        ComboBox<String> selector = new ComboBox<>(FXCollections.observableArrayList(
                "Benzene", "DNA", "Water", "Methane", "Ethanol", "Caffeine", "Protein Folding"));
        selector.setValue("Benzene");
        selector.setOnAction(e -> loadModel(selector.getValue()));

        Slider foldSlider = new Slider(0, 100, 0);
        Button foldBtn = new Button("Animate Folding");
        foldBtn.setOnAction(e -> simulateFolding());
        VBox foldControls = new VBox(5, new Label("Folding Strength:"), foldSlider, foldBtn);
        foldControls.setVisible(false);

        selector.valueProperty().addListener((obs, ov, nv) -> {
            boolean isFolding = nv != null && nv.equals("Protein Folding");
            foldControls.setVisible(isFolding);
            if (!isFolding && foldingTimer != null)
                foldingTimer.stop();
        });

        controls.getChildren().addAll(new Label("Load Model:"), selector, new Separator(), detailLabel, foldControls);
        root.setRight(controls);

        loadModel("Benzene");

        primaryStage.setScene(new Scene(root, 1100, 800));
        primaryStage.setTitle("JScience Molecular Viewer");
        primaryStage.show();
    }

    private SubScene create3DScene() {
        world.getChildren().setAll(moleculeGroup, new javafx.scene.AmbientLight(Color.DARKGRAY),
                new javafx.scene.PointLight(Color.WHITE));
        moleculeGroup.getTransforms().setAll(t, rx, ry);
        SubScene subScene = new SubScene(world, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);
        PerspectiveCamera cam = new PerspectiveCamera(true);
        cam.setTranslateZ(-50);
        subScene.setCamera(cam);
        subScene.setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        subScene.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - mouseX, dy = e.getSceneY() - mouseY;
            if (e.isSecondaryButtonDown()) {
                ry.setAngle(ry.getAngle() + dx * 0.2);
                rx.setAngle(rx.getAngle() - dy * 0.2);
            } else {
                t.setX(t.getX() + dx * 0.1);
                t.setY(t.getY() + dy * 0.1);
            }
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        subScene.setOnScroll(e -> t.setZ(t.getZ() + (e.getDeltaY() > 0 ? 3 : -3)));
        return subScene;
    }

    private void loadModel(String name) {
        if (foldingTimer != null)
            foldingTimer.stop();
        moleculeGroup.getChildren().clear();
        if ("Benzene".equals(name))
            createBenzene();
        else if ("DNA".equals(name))
            createDNA();
        else if ("Protein Folding".equals(name))
            simulateFolding();
    }

    private void createBenzene() {
        double r = 4.0;
        for (int i = 0; i < 6; i++) {
            double ang = i * Math.PI / 3;
            addAtomNode(PeriodicTable.bySymbol("C"), r * Math.cos(ang), r * Math.sin(ang), 0);
            addAtomNode(PeriodicTable.bySymbol("H"), (r + 2) * Math.cos(ang), (r + 2) * Math.sin(ang), 0);
        }
    }

    private void createDNA() {
        for (int i = 0; i < 20; i++) {
            double ang = i * 0.5, y = i * 2 - 20;
            addAtomNode(PeriodicTable.bySymbol("P"), 8 * Math.cos(ang), y, 8 * Math.sin(ang));
            addAtomNode(PeriodicTable.bySymbol("P"), 8 * Math.cos(ang + Math.PI), y, 8 * Math.sin(ang + Math.PI));
        }
    }

    private void addAtomNode(org.jscience.chemistry.Element e, double x, double y, double z) {
        Sphere s = new Sphere(VDW_RADII.getOrDefault(e.getSymbol(), 1.0));
        s.setMaterial(new PhongMaterial(CPK_COLORS.getOrDefault(e.getSymbol(), Color.PINK)));
        s.setTranslateX(x);
        s.setTranslateY(y);
        s.setTranslateZ(z);
        moleculeGroup.getChildren().add(s);
    }

    // --- Dynamic Simulation ---
    private Molecule foldingMolecule;
    private List<Sphere> foldingNodes = new ArrayList<>();
    private List<Cylinder> dynamicBonds = new ArrayList<>();
    private AnimationTimer foldingTimer;

    private void simulateFolding() {
        moleculeGroup.getChildren().clear();
        foldingNodes.clear();
        dynamicBonds.clear();
        foldingMolecule = new Molecule("Mini-Protein");
        int count = 25;
        for (int i = 0; i < count; i++) {
            Atom a = new Atom(PeriodicTable.bySymbol("C"), vec(i * 2 - 25, 0, 0));
            foldingMolecule.addAtom(a);
            Sphere s = new Sphere(1.5);
            s.setMaterial(new PhongMaterial(Color.LIGHTSKYBLUE));
            foldingNodes.add(s);
            moleculeGroup.getChildren().add(s);
        }
        for (int i = 0; i < count - 1; i++) {
            foldingMolecule.addBond(new Bond(foldingMolecule.getAtoms().get(i), foldingMolecule.getAtoms().get(i + 1)));
        }
        if (foldingTimer != null)
            foldingTimer.stop();
        foldingTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double radius = 4.0, rise = 1.0, step = 0.5;
                for (int i = 0; i < count; i++) {
                    Atom a = foldingMolecule.getAtoms().get(i);
                    Vector<Real> target = vec(Math.cos(i * step) * radius, i * rise - 12.5,
                            Math.sin(i * step) * radius);
                    a.addForce(target.subtract(a.getPosition()).multiply(Real.of(60.0)));
                }
                MolecularDynamics.step(foldingMolecule, 0.016);
                for (int i = 0; i < count; i++) {
                    Atom a = foldingMolecule.getAtoms().get(i);
                    Sphere s = foldingNodes.get(i);
                    s.setTranslateX(a.getPosition().get(0).doubleValue());
                    s.setTranslateY(a.getPosition().get(1).doubleValue());
                    s.setTranslateZ(a.getPosition().get(2).doubleValue());
                }
                updateBonds();
            }
        };
        foldingTimer.start();
    }

    private void updateBonds() {
        moleculeGroup.getChildren().removeAll(dynamicBonds);
        dynamicBonds.clear();
        PhongMaterial mat = new PhongMaterial(Color.SILVER);
        for (Bond b : foldingMolecule.getBonds()) {
            Vector<Real> p1 = b.getAtom1().getPosition(), p2 = b.getAtom2().getPosition();
            javafx.geometry.Point3D v1 = new javafx.geometry.Point3D(p1.get(0).doubleValue(), p1.get(1).doubleValue(),
                    p1.get(2).doubleValue());
            javafx.geometry.Point3D v2 = new javafx.geometry.Point3D(p2.get(0).doubleValue(), p2.get(1).doubleValue(),
                    p2.get(2).doubleValue());
            Cylinder c = createBondNode(v1, v2, mat);
            dynamicBonds.add(c);
            moleculeGroup.getChildren().add(c);
        }
    }

    private Cylinder createBondNode(javafx.geometry.Point3D p1, javafx.geometry.Point3D p2, PhongMaterial mat) {
        javafx.geometry.Point3D diff = p2.subtract(p1);
        double len = diff.magnitude();
        Cylinder c = new Cylinder(0.4, len);
        c.setMaterial(mat);
        c.setTranslateX(p1.midpoint(p2).getX());
        c.setTranslateY(p1.midpoint(p2).getY());
        c.setTranslateZ(p1.midpoint(p2).getZ());
        javafx.geometry.Point3D axis = diff.crossProduct(new javafx.geometry.Point3D(0, 1, 0));
        double angle = Math.acos(diff.normalize().dotProduct(new javafx.geometry.Point3D(0, 1, 0)));
        c.setRotationAxis(axis.magnitude() < 1e-4 ? new javafx.geometry.Point3D(1, 0, 0) : axis);
        c.setRotate(-Math.toDegrees(angle));
        return c;
    }

    private Vector<Real> vec(double x, double y, double z) {
        List<Real> l = new ArrayList<>();
        l.add(Real.of(x));
        l.add(Real.of(y));
        l.add(Real.of(z));
        return DenseVector.of(l, Real.ZERO);
    }

    public static void show(Stage stage) {
        new MolecularViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
