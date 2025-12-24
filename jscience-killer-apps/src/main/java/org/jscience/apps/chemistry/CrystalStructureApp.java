/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.chemistry;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.jscience.apps.framework.KillerAppBase;
import org.jscience.chemistry.crystallography.UnitCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Crystal Structure Explorer.
 * <p>
 * Visualization of 3D crystal lattices using JavaFX 3D.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CrystalStructureApp extends KillerAppBase {

    private Group root3D;
    private Rotate rx, ry;
    private Translate tz;
    private double mouseOldX, mouseOldY;

    // UI
    private ComboBox<String> structureBox;
    private Label infoLabel;
    private CheckBox showBondsCheck;

    // Data
    private static class AtomRecord {
        double x, y, z;
        String type; // "Na", "Cl", "C", "Si"

        AtomRecord(double x, double y, double z, String t) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.type = t;
        }
    }

    @Override
    protected String getAppTitle() {
        return i18n.get("crystal.title") + " - JScience";
    }

    @Override
    protected Region createMainContent() {
        SplitPane split = new SplitPane();

        // 3D View
        SubScene subScene = create3DScene();
        StackPane viewPane = new StackPane(subScene);
        subScene.widthProperty().bind(viewPane.widthProperty());
        subScene.heightProperty().bind(viewPane.heightProperty());

        // Controls
        VBox controls = createControls();

        split.getItems().addAll(viewPane, controls);
        split.setDividerPositions(0.75);

        loadStructure("Diamond");

        return split;
    }

    private SubScene create3DScene() {
        root3D = new Group();

        // Camera setup - zoomed out further by default (-40 instead of -15)
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        camera.setTranslateZ(-40);

        rx = new Rotate(0, Rotate.X_AXIS);
        ry = new Rotate(0, Rotate.Y_AXIS);
        tz = new Translate(0, 0, -40);

        root3D.getTransforms().addAll(tz, rx, ry);

        SubScene ss = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        ss.setCamera(camera);
        ss.setFill(Color.web("#222"));

        // Mouse handling (Rotate)
        ss.setOnMousePressed(e -> {
            mouseOldX = e.getSceneX();
            mouseOldY = e.getSceneY();
        });

        ss.setOnMouseDragged(e -> {
            ry.setAngle(ry.getAngle() - (e.getSceneX() - mouseOldX) * 0.5);
            rx.setAngle(rx.getAngle() + (e.getSceneY() - mouseOldY) * 0.5);
            mouseOldX = e.getSceneX();
            mouseOldY = e.getSceneY();
        });

        // Zoom
        ss.setOnScroll((ScrollEvent e) -> {
            tz.setZ(tz.getZ() + e.getDeltaY() * 0.1);
        });

        return ss;
    }

    private VBox createControls() {
        VBox box = new VBox(15);
        box.setStyle("-fx-padding: 15; -fx-background-color: #f4f4f4;");

        Label title = new Label(i18n.get("crystal.panel.view")); // "3D View" or better "Crystal Explorer"
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        title.setText("💎 " + i18n.get("crystal.title"));

        structureBox = new ComboBox<>();
        structureBox.getItems().addAll("Diamond", "Sodium Chloride (NaCl)", "Silicon", "Cesium Chloride (CsCl)");
        structureBox.setValue("Diamond");
        structureBox.setOnAction(e -> loadStructure(structureBox.getValue()));
        structureBox.setMaxWidth(Double.MAX_VALUE);

        Button loadCifBtn = new Button("Load CIF...");
        loadCifBtn.setOnAction(e -> loadCif());
        loadCifBtn.setMaxWidth(Double.MAX_VALUE);

        Button sampleBtn = new Button("Load Sample (Diamond)");
        sampleBtn.setOnAction(e -> loadSample());
        sampleBtn.setMaxWidth(Double.MAX_VALUE);

        infoLabel = new Label();
        infoLabel.setWrapText(true);

        showBondsCheck = new CheckBox(i18n.get("crystal.label.lattice") + " (Bonds)"); // Reuse or new key?
        showBondsCheck.setText("Show Bonds"); // Hardcoded for now as I missed adding specific key for "Show Bonds"
        showBondsCheck.setSelected(true);
        showBondsCheck.setOnAction(e -> loadStructure(structureBox.getValue()));

        box.getChildren().addAll(title, new Label(i18n.get("crystal.panel.controls")), structureBox, loadCifBtn,
                sampleBtn, showBondsCheck,
                new Separator(), infoLabel);
        return box;
    }

    private void loadCifStructure(java.io.InputStream is) {
        try {
            org.jscience.chemistry.loaders.CIFLoader.CrystalStructure cif = org.jscience.chemistry.loaders.CIFLoader
                    .load(is);

            if (cif == null)
                return;

            root3D.getChildren().clear();
            infoLabel.setText("Formula: " + cif.chemicalFormula + "\n" +
                    "a=" + cif.a + ", b=" + cif.b + ", c=" + cif.c + "\n" +
                    "alpha=" + cif.alpha + ", beta=" + cif.beta + ", gamma=" + cif.gamma);

            List<AtomRecord> atoms = new ArrayList<>();
            // Generate 2x2x2 supercell
            int repeat = 2;

            // Simplified orthogonal rendering for now
            // Visualization scale: Normalize approx to 1 unit = 3 Angstroms * scale 2
            double scale = 2.0;
            double unitScaleX = scale * (cif.a / 3.0);
            double unitScaleY = scale * (cif.b / 3.0);
            double unitScaleZ = scale * (cif.c / 3.0);

            for (int i = 0; i < repeat; i++) {
                for (int j = 0; j < repeat; j++) {
                    for (int k = 0; k < repeat; k++) {
                        for (org.jscience.chemistry.loaders.CIFLoader.AtomSite site : cif.atoms) {
                            double x = (site.x + i);
                            double y = (site.y + j);
                            double z = (site.z + k);

                            // Store atom with lattice coordinates for draw loop
                            atoms.add(new AtomRecord(x, y, z, site.symbol));
                        }
                    }
                }
            }

            // Draw Atoms
            for (AtomRecord a : atoms) {
                double vx = (a.x - 1.0) * unitScaleX; // 1.0 is centering offset for 2x2x2
                double vy = (a.y - 1.0) * unitScaleY;
                double vz = (a.z - 1.0) * unitScaleZ;

                Sphere s = new Sphere(0.4);
                s.setMaterial(getMaterial(a.type));
                s.setTranslateX(vx);
                s.setTranslateY(vy);
                s.setTranslateZ(vz);
                root3D.getChildren().add(s);
            }

            // Draw Bonds
            // Estimate bond limit: ~ 1.6 Angstrom typically C-C is 1.54
            // Let's say if dist < 1.8 Angstrom (normalized to lattice units?)
            // Lattice units are hard because a,b,c differ.
            // visual distance threshold?
            // Diamond C-C is ~1.54 A.
            // If we use Angstroms for distance check:
            if (showBondsCheck.isSelected()) {
                double bondCutoff = 1.8; // Angstroms
                // Na-Cl is 2.8, so this cutoff is small.
                // Dynamic cutoff?
                if (cif.chemicalFormula != null && cif.chemicalFormula.contains("Na"))
                    bondCutoff = 3.0; // NaCl

                List<javafx.scene.Node> nodes = new ArrayList<>(root3D.getChildren());
                for (int i = 0; i < nodes.size(); i++) {
                    for (int j = i + 1; j < nodes.size(); j++) {
                        if (nodes.get(i) instanceof Sphere s1 && nodes.get(j) instanceof Sphere s2) {
                            Point3D p1 = new Point3D(s1.getTranslateX(), s1.getTranslateY(), s1.getTranslateZ());
                            Point3D p2 = new Point3D(s2.getTranslateX(), s2.getTranslateY(), s2.getTranslateZ());

                            // Revert to Angstroms to check distance
                            // vx = (coord - off) * scale * (a/3)
                            // dx_angs = dx_vis / (scale/3)
                            // APPROXIMATION assuming cubic/isotropic for simple distance check

                            double distVis = p1.distance(p2);
                            // Convert back to Angstroms roughly
                            double distAng = distVis / (scale / 3.0);

                            if (distAng < bondCutoff) {
                                Cylinder bond = createLine(p1, p2);
                                root3D.getChildren().add(bond);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            infoLabel.setText("Error loading CIF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadCif() {
        javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
        fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("CIF Files", "*.cif"));
        java.io.File f = fc.showOpenDialog(null);
        if (f != null) {
            try (java.io.FileInputStream fis = new java.io.FileInputStream(f)) {
                loadCifStructure(fis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadSample() {
        try (java.io.InputStream is = getClass().getResourceAsStream("data/diamond.cif")) {
            if (is != null) {
                loadCifStructure(is);
            } else {
                infoLabel.setText("Sample diamond.cif not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStructure(String name) {
        if (name.equals("Load CIF...")) {
            loadCif();
            return;
        }

        root3D.getChildren().clear();
        List<AtomRecord> atoms = new ArrayList<>();
        UnitCell cell = null;
        double bondLimit = 0.0;

        // Visual scale factor
        double scale = 2.0;

        if (name.contains("NaCl")) {
            cell = UnitCell.NACL;
            bondLimit = 2.2;
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    for (int k = 0; k < 2; k++)
                        addNaClUnit(atoms, i, j, k);

        } else if (name.equals("Diamond") || name.equals("Silicon")) {
            cell = name.equals("Diamond") ? UnitCell.DIAMOND : UnitCell.SILICON;
            bondLimit = 1.2;
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    for (int k = 0; k < 2; k++)
                        addDiamondUnit(atoms, i, j, k, name.equals("Diamond") ? "C" : "Si");
        } else if (name.contains("CsCl")) {
            cell = UnitCell.cubic(4.123);
            bondLimit = 2.5;
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    for (int k = 0; k < 2; k++) {
                        atoms.add(new AtomRecord(i, j, k, "Cs"));
                        atoms.add(new AtomRecord(i + 0.5, j + 0.5, k + 0.5, "Cl"));
                    }
        }

        if (cell != null) {
            infoLabel.setText("System: " + cell.getSystem() + "\n" +
                    "a = " + cell.getA() + " Å\n" +
                    "Volume = " + String.format("%.2f", cell.volume().doubleValue()) + " Å³");

            double unitScale = scale * (cell.getA().doubleValue() / 3.0);

            // Draw Atoms
            for (AtomRecord a : atoms) {
                double x = (a.x - 1.0) * unitScale;
                double y = (a.y - 1.0) * unitScale;
                double z = (a.z - 1.0) * unitScale;

                Sphere s = new Sphere(0.4);
                s.setMaterial(getMaterial(a.type));
                s.setTranslateX(x);
                s.setTranslateY(y);
                s.setTranslateZ(z);
                root3D.getChildren().add(s);
            }

            if (showBondsCheck.isSelected()) {
                List<javafx.scene.Node> nodes = new ArrayList<>(root3D.getChildren());
                for (int i = 0; i < nodes.size(); i++) {
                    for (int j = i + 1; j < nodes.size(); j++) {
                        if (nodes.get(i) instanceof Sphere s1 && nodes.get(j) instanceof Sphere s2) {
                            Point3D p1 = new Point3D(s1.getTranslateX(), s1.getTranslateY(), s1.getTranslateZ());
                            Point3D p2 = new Point3D(s2.getTranslateX(), s2.getTranslateY(), s2.getTranslateZ());
                            double dist = p1.distance(p2);
                            if (dist < bondLimit + 0.01) {
                                Cylinder bond = createLine(p1, p2);
                                root3D.getChildren().add(bond);
                            }
                        }
                    }
                }
            }
        }
    }

    private void addNaClUnit(List<AtomRecord> atoms, double dx, double dy, double dz) {
        // Na (000 basis)
        atoms.add(new AtomRecord(dx, dy, dz, "Na"));
        atoms.add(new AtomRecord(dx + 0.5, dy + 0.5, dz, "Na"));
        atoms.add(new AtomRecord(dx + 0.5, dy, dz + 0.5, "Na"));
        atoms.add(new AtomRecord(dx, dy + 0.5, dz + 0.5, "Na"));
        // Cl (0.5 0 0 basis)
        atoms.add(new AtomRecord(dx + 0.5, dy, dz, "Cl"));
        atoms.add(new AtomRecord(dx, dy + 0.5, dz, "Cl"));
        atoms.add(new AtomRecord(dx, dy, dz + 0.5, "Cl"));
        atoms.add(new AtomRecord(dx + 0.5, dy + 0.5, dz + 0.5, "Cl"));
    }

    private void addDiamondUnit(List<AtomRecord> atoms, double dx, double dy, double dz, String type) {
        double[][] basis = {
                { 0, 0, 0 }, { 0.5, 0.5, 0 }, { 0.5, 0, 0.5 }, { 0, 0.5, 0.5 }, // FCC
                { 0.25, 0.25, 0.25 }, { 0.75, 0.75, 0.25 }, { 0.75, 0.25, 0.75 }, { 0.25, 0.75, 0.75 } // Offset FCC
        };
        for (double[] p : basis) {
            atoms.add(new AtomRecord(dx + p[0], dy + p[1], dz + p[2], type));
        }
    }

    private PhongMaterial getMaterial(String type) {
        PhongMaterial m = new PhongMaterial();
        m.setSpecularColor(Color.WHITE);
        switch (type) {
            case "C":
                m.setDiffuseColor(Color.web("#333333")); // Dark Grey for Carbon
                break;
            case "Si":
                m.setDiffuseColor(Color.web("#666666"));
                break;
            case "Na":
                m.setDiffuseColor(Color.PURPLE);
                break;
            case "Cl":
                m.setDiffuseColor(Color.GREEN);
                break;
            case "Cs":
                m.setDiffuseColor(Color.GOLD);
                break;
            default:
                m.setDiffuseColor(Color.RED);
        }
        return m;
    }

    private Cylinder createLine(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.1, height);
        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
        line.setMaterial(new PhongMaterial(Color.LIGHTGRAY));

        return line;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
