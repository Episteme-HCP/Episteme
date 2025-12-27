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
    private final Translate t = new Translate(0, 0, 30); // Z+ is into screen

    private double mouseX, mouseY;
    private Label detailLabel = new Label("Select a molecule...");
    private CheckBox showBondsBox;
    private CheckBox showOrbitalsBox;
    private boolean showOrbitals = false;

    private static final Map<String, Color> CPK_COLORS = new HashMap<>();
    private static final Map<String, Double> VDW_RADII = new HashMap<>();

    static {
        // Colors
        CPK_COLORS.put("H", Color.WHITE);
        CPK_COLORS.put("C", Color.web("#909090")); // Dark Grey
        CPK_COLORS.put("N", Color.web("#3050F8")); // Blue
        CPK_COLORS.put("O", Color.web("#FF0D0D")); // Red
        CPK_COLORS.put("S", Color.web("#FFFF30")); // Yellow
        CPK_COLORS.put("P", Color.web("#FFA500")); // Orange
        CPK_COLORS.put("F", Color.web("#90E050")); // Green
        CPK_COLORS.put("Cl", Color.web("#1FF01F")); // Green
        CPK_COLORS.put("Br", Color.web("#A62929")); // Dark Red
        CPK_COLORS.put("I", Color.web("#940094")); // Violet
        CPK_COLORS.put("?", Color.PINK);

        // Van der Waals Radii (Angstroms) - Scaled for visual appeal
        VDW_RADII.put("H", 1.2);
        VDW_RADII.put("C", 1.7);
        VDW_RADII.put("N", 1.55);
        VDW_RADII.put("O", 1.52);
        VDW_RADII.put("F", 1.47);
        VDW_RADII.put("P", 1.8);
        VDW_RADII.put("S", 1.8);
        VDW_RADII.put("Cl", 1.75);
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
        controls.getStyleClass().add("dark-viewer-sidebar");

        ComboBox<String> selector = new ComboBox<>();
        selector.setItems(FXCollections.observableArrayList(
                "Glycine",
                "Alanine",
                "Ethanol",
                "Caffeine",
                "Water",
                "Benzene (Orbitals)",
                "DNA",
                "Protein"));
        selector.setValue("Benzene (Orbitals)");
        selector.setMaxWidth(Double.MAX_VALUE);
        selector.setOnAction(e -> loadModel(selector.getValue()));

        showBondsBox = new CheckBox("Show Bonds");
        showBondsBox.setSelected(true);
        showBondsBox.setOnAction(e -> toggleBonds(showBondsBox.isSelected()));

        showOrbitalsBox = new CheckBox("Show Orbitals");
        showOrbitalsBox.setSelected(false);
        showOrbitalsBox.setOnAction(e -> {
            showOrbitals = showOrbitalsBox.isSelected();
            // Reload current model to apply orbitals
            loadModel(selector.getValue());
        });

        // Planar (2D) View Toggle
        CheckBox planarViewBox = new CheckBox("Planar (2D) View");
        planarViewBox.setSelected(false);
        planarViewBox.setOnAction(e -> {
            planarMode = planarViewBox.isSelected();
            if (planarMode) {
                root.setCenter(create2DView());
            } else {
                root.setCenter(subScene);
            }
            loadModel(selector.getValue());
        });

        detailLabel.setWrapText(true);
        detailLabel.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11px;");

        Label header = new Label(org.jscience.ui.i18n.I18n.getInstance().get("MolecularViewer.title"));
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        VBox legend = createLegend();

        controls.getChildren().addAll(header, new Separator(),
                new Label("Load Model:"), selector,
                showBondsBox, showOrbitalsBox, planarViewBox,
                new Separator(),
                new Label("Molecule Info:"),
                detailLabel,
                new Separator(),
                new Label("Legend:"),
                legend);
        root.setRight(controls);

        loadModel("Benzene (Orbitals)");

        Scene scene = new Scene(root, 1024, 768);
        primaryStage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("MolecularViewer.title"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Planar mode flag
    private boolean planarMode = false;
    private javafx.scene.canvas.Canvas planarCanvas;

    private javafx.scene.Node create2DView() {
        planarCanvas = new javafx.scene.canvas.Canvas(800, 600);
        javafx.scene.layout.StackPane container = new javafx.scene.layout.StackPane(planarCanvas);
        container.getStyleClass().add("viewer-root");
        return container;
    }

    // Static show method for Master Demo
    public static void show(Stage stage) {
        new MolecularViewer().start(stage);
    }

    private SubScene create3DScene() {
        // Add Lights
        javafx.scene.PointLight headLight = new javafx.scene.PointLight(Color.WHITE);
        headLight.setTranslateZ(-100);

        javafx.scene.AmbientLight ambientLight = new javafx.scene.AmbientLight(Color.rgb(100, 100, 100)); // Brighter
                                                                                                          // ambient

        world.getChildren().addAll(moleculeGroup, headLight, ambientLight);
        moleculeGroup.getTransforms().addAll(t, rx, ry);

        SubScene subScene = new SubScene(world, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#101010")); // Dark sleek background

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        camera.setTranslateZ(-40); // Camera back
        subScene.setCamera(camera);

        subScene.setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        subScene.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - mouseX;
            double dy = e.getSceneY() - mouseY;
            if (e.isSecondaryButtonDown()) { // Right Button -> Rotate
                ry.setAngle(ry.getAngle() + dx * 0.2);
                rx.setAngle(rx.getAngle() - dy * 0.2);
            } else if (e.isPrimaryButtonDown()) { // Left Button -> Translate (Pan)
                t.setX(t.getX() + dx * 0.1);
                t.setY(t.getY() + dy * 0.1);
            }
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        subScene.setOnScroll(e -> t.setZ(t.getZ() + (e.getDeltaY() > 0 ? 2 : -2)));

        return subScene;
    }

    private void loadModel(String name) {
        moleculeGroup.getChildren().clear();

        // Strip formula from name if present in old list (backward compatibility)
        if (name.contains("(")) {
            name = name.substring(0, name.indexOf("(")).trim();
        }

        if (name.contains("Benzene")) {
            createBenzene();
            detailLabel.setText("Benzene (C6H6)\nDelocalized Pi System\nRed/Green lobes represent p-orbitals.");
        } else if (name.contains("DNA")) {
            createDNA();
            detailLabel.setText("Deoxyribonucleic Acid (DNA)\nDouble Helix");
        } else if (name.contains("Protein")) {
            createProtein();
            detailLabel.setText("Generic Protein Structure\nAlpha Helix");
        } else {
            MolecularGraph g = null;
            if (name.contains("Ethanol"))
                g = createEthanol();
            else if (name.contains("Water"))
                g = createWater();
            else if (name.contains("Caffeine"))
                g = createCaffeine();
            else if (name.contains("Glycine"))
                g = createGlycine();
            else if (name.contains("Alanine"))
                g = createAlanine();

            if (g != null) {
                renderGraph(g);
                String formula = calculateFormula(g);
                detailLabel.setText("Name: " + name + "\nFormula: " + formula + "\nAtoms: " + g.getAtoms().size()
                        + "\nBonds: " + g.getBonds().size());
            }
        }
    }

    // Hill System Formula Calculation
    private String calculateFormula(MolecularGraph g) {
        Map<String, Integer> counts = new HashMap<>();
        for (Atom a : g.getAtoms()) {
            String sym = a.getElement().getSymbol();
            counts.put(sym, counts.getOrDefault(sym, 0) + 1);
        }

        StringBuilder sb = new StringBuilder();
        if (counts.containsKey("C")) {
            sb.append("C");
            if (counts.get("C") > 1)
                sb.append(counts.get("C"));
            counts.remove("C");

            if (counts.containsKey("H")) {
                sb.append("H");
                if (counts.get("H") > 1)
                    sb.append(counts.get("H"));
                counts.remove("H");
            }
        }

        // Alphabetical for others
        List<String> others = new ArrayList<>(counts.keySet());
        java.util.Collections.sort(others);
        for (String sym : others) {
            sb.append(sym);
            if (counts.get(sym) > 1)
                sb.append(counts.get(sym));
        }
        return sb.toString();
    }

    private void renderGraph(MolecularGraph graph) {
        double scale = 0.5; // Visual scaling factor

        for (Atom atom : graph.getAtoms()) {
            String symbol = atom.getElement().getSymbol();
            Color color = CPK_COLORS.getOrDefault(symbol, CPK_COLORS.get("?"));
            double radius = VDW_RADII.getOrDefault(symbol, 1.5) * scale;

            Sphere s = new Sphere(radius);
            PhongMaterial mat = new PhongMaterial(color);
            mat.setSpecularColor(Color.WHITE);
            mat.setSpecularPower(32); // Shiny plastic
            s.setMaterial(mat);

            Vector<Real> pos = atom.getPosition();
            // Scale position
            s.setTranslateX(pos.get(0).doubleValue() * 3);
            s.setTranslateY(pos.get(1).doubleValue() * 3);
            s.setTranslateZ(pos.get(2).doubleValue() * 3);

            s.setOnMouseClicked(e -> showAtomDetails(atom));

            // Hover effect
            s.setOnMouseEntered(e -> {
                mat.setDiffuseColor(color.brighter());
                s.setScaleX(1.1);
                s.setScaleY(1.1);
                s.setScaleZ(1.1);
            });
            s.setOnMouseExited(e -> {
                mat.setDiffuseColor(color);
                s.setScaleX(1.0);
                s.setScaleY(1.0);
                s.setScaleZ(1.0);
            });

            moleculeGroup.getChildren().add(s);
        }

        for (MolecularGraph.Bond bond : graph.getBonds()) {
            Vector<Real> p1 = bond.source.getPosition();
            Vector<Real> p2 = bond.target.getPosition();
            createBondCylinder(p1, p2, bond.type);
        }

        // Center the molecule
        // Simple average calc could go here, but for now we rely on model coords being
        // somewhat centered

        if (showOrbitals) {
            for (Atom atom : graph.getAtoms()) {
                // Add atomic orbital visual (Simplified generic orbital for all atoms)
                // Using VDW radius approximation as orbital shell
                String symbol = atom.getElement().getSymbol();
                createGenericOrbital(atom.getPosition(), symbol);
            }
        }
    }

    private void createGenericOrbital(Vector<Real> posVector, String symbol) {
        // Generic transparent shell to represent orbital probability cloud
        double posScale = 3.0;
        javafx.geometry.Point3D pos = new javafx.geometry.Point3D(
                posVector.get(0).doubleValue() * posScale,
                posVector.get(1).doubleValue() * posScale,
                posVector.get(2).doubleValue() * posScale);

        double radius = VDW_RADII.getOrDefault(symbol, 1.5) * 0.5 * 1.5; // Slightly larger than atom
        Sphere s = new Sphere(radius);
        PhongMaterial mat = new PhongMaterial(CPK_COLORS.getOrDefault(symbol, Color.GRAY));
        mat.setDiffuseColor(new Color(mat.getDiffuseColor().getRed(), mat.getDiffuseColor().getGreen(),
                mat.getDiffuseColor().getBlue(), 0.3)); // Transparent
        mat.setSpecularColor(Color.WHITE);

        s.setMaterial(mat);
        s.setTranslateX(pos.getX());
        s.setTranslateY(pos.getY());
        s.setTranslateZ(pos.getZ());
        s.setMouseTransparent(true); // Don't block clicks on atom
        moleculeGroup.getChildren().add(s);
    }

    private VBox createLegend() {
        VBox box = new VBox(5);
        box.setPadding(new Insets(5));
        box.setStyle("-fx-background-color: white; -fx-border-color: #ccc;");

        String[] elements = { "H", "C", "N", "O", "S", "P", "Halogen" };
        for (String el : elements) {
            HBox row = new HBox(5);
            row.setAlignment(Pos.CENTER_LEFT);
            javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(6);
            String lookup = el.equals("Halogen") ? "Cl" : el;
            c.setFill(CPK_COLORS.getOrDefault(lookup, Color.GRAY));
            Label l = new Label(el.equals("Halogen") ? "F, Cl, Br, I" : el);
            l.setStyle("-fx-font-size: 10px;");
            row.getChildren().addAll(c, l);
            box.getChildren().add(row);
        }
        return box;
    }

    private void showAtomDetails(Atom atom) {
        StringBuilder sb = new StringBuilder();
        sb.append("Element: ").append(atom.getElement().getName());
        sb.append(" (").append(atom.getElement().getSymbol()).append(")\n");
        sb.append("At No: ").append(atom.getElement().getAtomicNumber()).append("\n");
        if (atom.getElement().getAtomicMass() != null) {
            sb.append("Mass: ")
                    .append(String.format("%.4f u", atom.getElement().getAtomicMass().getValue().doubleValue()));
        }
        sb.append("\nPos: ").append(atom.getPosition().toString());

        // Append context to curr details
        String current = detailLabel.getText();
        if (current.contains("Formula:")) {
            detailLabel.setText(current.substring(0,
                    current.indexOf("\n\nSelected:") == -1 ? current.length() : current.indexOf("\n\nSelected:"))
                    + "\n\nSelected:\n" + sb.toString());
        } else {
            detailLabel.setText(sb.toString());
        }
    }

    private void createBondCylinder(Vector<Real> v1, Vector<Real> v2, MolecularGraph.BondType type) {
        double posScale = 3.0;
        javafx.geometry.Point3D p1 = new javafx.geometry.Point3D(v1.get(0).doubleValue() * posScale,
                v1.get(1).doubleValue() * posScale, v1.get(2).doubleValue() * posScale);
        javafx.geometry.Point3D p2 = new javafx.geometry.Point3D(v2.get(0).doubleValue() * posScale,
                v2.get(1).doubleValue() * posScale, v2.get(2).doubleValue() * posScale);

        javafx.geometry.Point3D diff = p2.subtract(p1);
        double len = diff.magnitude();
        javafx.geometry.Point3D mid = p1.midpoint(p2);

        javafx.geometry.Point3D axis = diff.crossProduct(new javafx.geometry.Point3D(0, 1, 0));
        if (axis.magnitude() < 1e-4)
            axis = new javafx.geometry.Point3D(1, 0, 0);

        double angle = Math.acos(diff.normalize().dotProduct(new javafx.geometry.Point3D(0, 1, 0)));

        double bondRadius = 0.15; // Much thinner than atoms

        if (type == MolecularGraph.BondType.DOUBLE || type == MolecularGraph.BondType.AROMATIC) {
            javafx.geometry.Point3D offset = axis.normalize().multiply(0.15);
            createCylinderShape(mid.add(offset), axis, angle, len, bondRadius);
            createCylinderShape(mid.subtract(offset), axis, angle, len, bondRadius);
        } else if (type == MolecularGraph.BondType.TRIPLE) {
            createCylinderShape(mid, axis, angle, len, bondRadius);
            javafx.geometry.Point3D offset = axis.normalize().multiply(0.25);
            createCylinderShape(mid.add(offset), axis, angle, len, bondRadius);
            createCylinderShape(mid.subtract(offset), axis, angle, len, bondRadius);
        } else {
            createCylinderShape(mid, axis, angle, len, bondRadius);
        }
    }

    private void createCylinderShape(javafx.geometry.Point3D pos, javafx.geometry.Point3D axis, double angle,
            double height, double radius) {
        Cylinder cyl = new Cylinder(radius, height);
        PhongMaterial mat = new PhongMaterial(Color.DARKGRAY);
        mat.setSpecularColor(Color.WHITE);
        cyl.setMaterial(mat);

        cyl.setRotationAxis(axis);
        cyl.setRotate(-Math.toDegrees(angle));
        cyl.setTranslateX(pos.getX());
        cyl.setTranslateY(pos.getY());
        cyl.setTranslateZ(pos.getZ());
        if (showBondsBox != null)
            cyl.setVisible(showBondsBox.isSelected());
        // Tag as bond for toggling
        cyl.setUserData("bond");
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
        // Double Helix - visual approximation using connected Cylinders (Tubes)
        double radius = 4.0; // Helix radius
        double rise = 0.8; // Rise per step
        double steps = 40;

        PhongMaterial backboneMat = new PhongMaterial(Color.DARKBLUE);
        backboneMat.setSpecularColor(Color.WHITE);
        PhongMaterial rungMat = new PhongMaterial(Color.LIMEGREEN);
        rungMat.setSpecularColor(Color.WHITE);

        // Previous points for connecting tube segments
        javafx.geometry.Point3D prevP1 = null;
        javafx.geometry.Point3D prevP2 = null;

        for (int i = 0; i < steps; i++) {
            double tVal = i * 0.5;
            double y = i * rise;

            double x1 = Math.cos(tVal) * radius;
            double z1 = Math.sin(tVal) * radius;
            javafx.geometry.Point3D p1 = new javafx.geometry.Point3D(x1, y, z1);

            double x2 = Math.cos(tVal + Math.PI) * radius;
            double z2 = Math.sin(tVal + Math.PI) * radius;
            javafx.geometry.Point3D p2 = new javafx.geometry.Point3D(x2, y, z2);

            // Draw Backbone Segments
            if (prevP1 != null) {
                createTubeSegment(prevP1, p1, 0.4, backboneMat);
                createTubeSegment(prevP2, p2, 0.4, backboneMat);
            }
            prevP1 = p1;
            prevP2 = p2;

            // Rungs (Base pairs)
            createTubeSegment(p1, p2, 0.2, rungMat);

            // Add Atoms at backbone joints for gloss
            Sphere s1 = new Sphere(0.5);
            s1.setMaterial(backboneMat);
            s1.setTranslateX(p1.getX());
            s1.setTranslateY(p1.getY());
            s1.setTranslateZ(p1.getZ());
            moleculeGroup.getChildren().add(s1);

            Sphere s2 = new Sphere(0.5);
            s2.setMaterial(backboneMat);
            s2.setTranslateX(p2.getX());
            s2.setTranslateY(p2.getY());
            s2.setTranslateZ(p2.getZ());
            moleculeGroup.getChildren().add(s2);
        }

        // Center the view
        t.setY(-steps * rise / 2.0);
    }

    private void createTubeSegment(javafx.geometry.Point3D start, javafx.geometry.Point3D end, double radius,
            PhongMaterial mat) {
        javafx.geometry.Point3D diff = end.subtract(start);
        double len = diff.magnitude();
        javafx.geometry.Point3D mid = start.midpoint(end);

        javafx.geometry.Point3D axis = diff.crossProduct(new javafx.geometry.Point3D(0, 1, 0));
        double angle = Math.acos(diff.normalize().dotProduct(new javafx.geometry.Point3D(0, 1, 0)));
        if (axis.magnitude() < 1e-4)
            axis = new javafx.geometry.Point3D(1, 0, 0);

        Cylinder c = new Cylinder(radius, len);
        c.setMaterial(mat);
        c.setRotationAxis(axis);
        c.setRotate(-Math.toDegrees(angle));
        c.setTranslateX(mid.getX());
        c.setTranslateY(mid.getY());
        c.setTranslateZ(mid.getZ());

        moleculeGroup.getChildren().add(c);
    }

    private void createProtein() {
        // Alpha Helix approximation using Tube
        PhongMaterial mat = new PhongMaterial(Color.web("#8A2BE2")); // BlueViolet
        mat.setSpecularColor(Color.WHITE);

        double radius = 3.0;
        double rise = 0.4;
        int steps = 60;

        javafx.geometry.Point3D prev = null;

        for (int i = 0; i < steps; i++) {
            double tVal = i * 0.3;
            double y = i * rise;

            double x = Math.cos(tVal) * radius;
            double z = Math.sin(tVal) * radius;
            javafx.geometry.Point3D cur = new javafx.geometry.Point3D(x, y, z);

            if (prev != null) {
                createTubeSegment(prev, cur, 0.8, mat); // Thick ribbon tube

                // Joint sphere for smoothness
                Sphere s = new Sphere(0.8);
                s.setMaterial(mat);
                s.setTranslateX(prev.getX());
                s.setTranslateY(prev.getY());
                s.setTranslateZ(prev.getZ());
                moleculeGroup.getChildren().add(s);
            }
            prev = cur;
        }
        // Center
        t.setY(-steps * rise / 2.0);
    }

    private void createBenzene() {
        // Benzene C6H6 with p-orbitals
        double radius = 2.4; // Ring radius

        // MoleculeRing ring = new MoleculeRing(6, radius, 0); // Helper not implemented

        List<javafx.geometry.Point3D> cPoints = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(i * 60);
            double x = Math.cos(angle) * radius * 3; // Scale for view
            double y = Math.sin(angle) * radius * 3;
            cPoints.add(new javafx.geometry.Point3D(x, y, 0));
        }

        PhongMaterial cMat = new PhongMaterial(CPK_COLORS.get("C"));
        PhongMaterial hMat = new PhongMaterial(CPK_COLORS.get("H"));
        PhongMaterial bondMat = new PhongMaterial(Color.DARKGRAY);

        for (int i = 0; i < 6; i++) {
            javafx.geometry.Point3D p = cPoints.get(i);

            // Carbon
            Sphere c = new Sphere(1.7 * 0.5); // Scaled VDW
            c.setMaterial(cMat);
            c.setTranslateX(p.getX());
            c.setTranslateY(p.getY());
            c.setTranslateZ(p.getZ());
            moleculeGroup.getChildren().add(c);

            // Hydrogen (radial outward)
            javafx.geometry.Point3D hDir = p.normalize();
            javafx.geometry.Point3D hPos = p.add(hDir.multiply(3.0));
            Sphere h = new Sphere(1.2 * 0.5);
            h.setMaterial(hMat);
            h.setTranslateX(hPos.getX());
            h.setTranslateY(hPos.getY());
            h.setTranslateZ(hPos.getZ());
            moleculeGroup.getChildren().add(h);

            // C-H Bond
            createCylinderShape(p, hDir.crossProduct(new javafx.geometry.Point3D(0, 0, 1)),
                    Math.acos(hDir.dotProduct(new javafx.geometry.Point3D(0, 1, 0))),
                    p.distance(hPos), 0.15); // Custom call or reuse createBondCylinder (need vector)

            // C-C Bonds (Aromatic)
            int next = (i + 1) % 6;
            javafx.geometry.Point3D pNext = cPoints.get(next);

            // javafx.geometry.Point3D diff = pNext.subtract(p); // Unused

            // Aromatic double ring look
            // plane
            // normal?
            // No, Z
            // is
            // normal
            // to
            // ring
            // Actually, simplified: just draw bonds. We have createBond helper but it takes
            // Atoms/Real Vectors.
            // We'll reimplement simple cylinder for this procedural demo.

            createCylinderBetween(p, pNext, 0.2, bondMat);

            // Orbitals! (P-orbitals perpendicular to ring, i.e., Z-axis)
            createOrbital(p, new javafx.geometry.Point3D(0, 0, 1), 2.5);
        }
    }

    private void createCylinderBetween(javafx.geometry.Point3D p1, javafx.geometry.Point3D p2, double radius,
            PhongMaterial mat) {
        javafx.geometry.Point3D diff = p2.subtract(p1);
        double len = diff.magnitude();
        javafx.geometry.Point3D mid = p1.midpoint(p2);
        javafx.geometry.Point3D axis = diff.crossProduct(new javafx.geometry.Point3D(0, 1, 0));
        double angle = Math.acos(diff.normalize().dotProduct(new javafx.geometry.Point3D(0, 1, 0)));

        Cylinder c = new Cylinder(radius, len);
        c.setMaterial(mat);
        c.setRotationAxis(axis);
        c.setRotate(-Math.toDegrees(angle));
        c.setTranslateX(mid.getX());
        c.setTranslateY(mid.getY());
        c.setTranslateZ(mid.getZ());
        moleculeGroup.getChildren().add(c);
    }

    private void createOrbital(javafx.geometry.Point3D center, javafx.geometry.Point3D axis, double size) {
        // Two lobes: Red (Positive) and Green (Negative)

        PhongMaterial redMat = new PhongMaterial(Color.web("#FF0000", 0.6)); // Transparent
        PhongMaterial greenMat = new PhongMaterial(Color.web("#00FF00", 0.6));

        // Use scaled spheres for lobes
        Sphere lobe1 = new Sphere(size * 0.4);
        lobe1.setMaterial(redMat);
        lobe1.setScaleY(1.5); // Elongate

        Sphere lobe2 = new Sphere(size * 0.4);
        lobe2.setMaterial(greenMat);
        lobe2.setScaleY(1.5);

        // Position
        javafx.geometry.Point3D offset = axis.normalize().multiply(size * 0.4);

        lobe1.setTranslateX(center.getX() + offset.getX());
        lobe1.setTranslateY(center.getY() + offset.getY());
        lobe1.setTranslateZ(center.getZ() + offset.getZ());

        lobe2.setTranslateX(center.getX() - offset.getX());
        lobe2.setTranslateY(center.getY() - offset.getY());
        lobe2.setTranslateZ(center.getZ() - offset.getZ());

        // Rotate to match axis (default sphere Y is up)
        // If axis is Z (0,0,1), we need to rotate 90 deg around X
        javafx.geometry.Point3D yAxis = new javafx.geometry.Point3D(0, 1, 0);
        javafx.geometry.Point3D rotAxis = yAxis.crossProduct(axis);
        double angle = Math.acos(yAxis.dotProduct(axis));

        if (rotAxis.magnitude() > 0.001) {
            lobe1.setRotationAxis(rotAxis);
            lobe1.setRotate(Math.toDegrees(angle));
            lobe2.setRotationAxis(rotAxis);
            lobe2.setRotate(Math.toDegrees(angle));
        }

        moleculeGroup.getChildren().addAll(lobe1, lobe2);
    }

    private MolecularGraph createGlycine() {
        MolecularGraph g = new MolecularGraph();
        Atom n = new Atom(PeriodicTable.bySymbol("N"), vec(-1.47, 0, 0));
        Atom ca = new Atom(PeriodicTable.bySymbol("C"), vec(0, 0, 0));
        Atom c = new Atom(PeriodicTable.bySymbol("C"), vec(1.53, 0, 0));
        Atom o1 = new Atom(PeriodicTable.bySymbol("O"), vec(2.1, 1.2, 0));
        Atom o2 = new Atom(PeriodicTable.bySymbol("O"), vec(2.1, -1.2, 0));

        g.addAtom(n);
        g.addAtom(ca);
        g.addAtom(c);
        g.addAtom(o1);
        g.addAtom(o2);

        // Hydrogens (Approx)
        Atom hn1 = new Atom(PeriodicTable.bySymbol("H"), vec(-2.0, 0.8, 0));
        Atom hn2 = new Atom(PeriodicTable.bySymbol("H"), vec(-2.0, -0.8, 0));
        Atom ha1 = new Atom(PeriodicTable.bySymbol("H"), vec(0, 1.03, 0));
        Atom ha2 = new Atom(PeriodicTable.bySymbol("H"), vec(0, -1.03, 0));
        Atom ho = new Atom(PeriodicTable.bySymbol("H"), vec(3.0, -1.2, 0));

        g.addAtom(hn1);
        g.addAtom(hn2);
        g.addAtom(ha1);
        g.addAtom(ha2);
        g.addAtom(ho);

        g.addBond(n, ca, MolecularGraph.BondType.SINGLE);
        g.addBond(ca, c, MolecularGraph.BondType.SINGLE);
        g.addBond(c, o1, MolecularGraph.BondType.DOUBLE);
        g.addBond(c, o2, MolecularGraph.BondType.SINGLE);

        g.addBond(n, hn1, MolecularGraph.BondType.SINGLE);
        g.addBond(n, hn2, MolecularGraph.BondType.SINGLE);
        g.addBond(ca, ha1, MolecularGraph.BondType.SINGLE);
        g.addBond(ca, ha2, MolecularGraph.BondType.SINGLE);
        g.addBond(o2, ho, MolecularGraph.BondType.SINGLE);

        return g;
    }

    private MolecularGraph createAlanine() {
        MolecularGraph g = new MolecularGraph();
        Atom n = new Atom(PeriodicTable.bySymbol("N"), vec(-1.47, 0, 0));
        Atom ca = new Atom(PeriodicTable.bySymbol("C"), vec(0, 0, 0));
        Atom c = new Atom(PeriodicTable.bySymbol("C"), vec(1.53, 0, 0));
        Atom o1 = new Atom(PeriodicTable.bySymbol("O"), vec(2.1, 1.2, 0));
        Atom o2 = new Atom(PeriodicTable.bySymbol("O"), vec(2.1, -1.2, 0));
        Atom cb = new Atom(PeriodicTable.bySymbol("C"), vec(0, -1.53, 0)); // Methyl

        g.addAtom(n);
        g.addAtom(ca);
        g.addAtom(c);
        g.addAtom(o1);
        g.addAtom(o2);
        g.addAtom(cb);

        // Hydrogens
        Atom hn1 = new Atom(PeriodicTable.bySymbol("H"), vec(-2.0, 0.8, 0));
        Atom hn2 = new Atom(PeriodicTable.bySymbol("H"), vec(-2.0, -0.8, 0));
        Atom ha = new Atom(PeriodicTable.bySymbol("H"), vec(0, 1.03, 0));
        Atom ho = new Atom(PeriodicTable.bySymbol("H"), vec(3.0, -1.2, 0));
        Atom hb1 = new Atom(PeriodicTable.bySymbol("H"), vec(-0.9, -1.9, 0));
        Atom hb2 = new Atom(PeriodicTable.bySymbol("H"), vec(0.9, -1.9, 0));
        Atom hb3 = new Atom(PeriodicTable.bySymbol("H"), vec(0, -1.9, 0.9));

        g.addAtom(hn1);
        g.addAtom(hn2);
        g.addAtom(ha);
        g.addAtom(ho);
        g.addAtom(hb1);
        g.addAtom(hb2);
        g.addAtom(hb3);

        g.addBond(n, ca, MolecularGraph.BondType.SINGLE);
        g.addBond(ca, c, MolecularGraph.BondType.SINGLE);
        g.addBond(ca, cb, MolecularGraph.BondType.SINGLE);
        g.addBond(c, o1, MolecularGraph.BondType.DOUBLE);
        g.addBond(c, o2, MolecularGraph.BondType.SINGLE);

        g.addBond(n, hn1, MolecularGraph.BondType.SINGLE);
        g.addBond(n, hn2, MolecularGraph.BondType.SINGLE);
        g.addBond(ca, ha, MolecularGraph.BondType.SINGLE);
        g.addBond(o2, ho, MolecularGraph.BondType.SINGLE);
        g.addBond(cb, hb1, MolecularGraph.BondType.SINGLE);
        g.addBond(cb, hb2, MolecularGraph.BondType.SINGLE);
        g.addBond(cb, hb3, MolecularGraph.BondType.SINGLE);

        return g;
    }

    private void toggleBonds(boolean show) {
        for (javafx.scene.Node n : moleculeGroup.getChildren()) {
            if ("bond".equals(n.getUserData())) {
                n.setVisible(show);
            }
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
