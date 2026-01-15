/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
import org.jscience.apps.framework.FeaturedAppBase;
import org.jscience.ui.i18n.I18n;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;

public class CrystalStructureApp extends FeaturedAppBase {
    private Group root3D;
    private Rotate rx, ry;
    private Translate tz;
    private double mouseOldX, mouseOldY;

    // UI
    private ComboBox<LatticeType> latticeCombo;
    private Label infoLabel;
    private CheckBox showAtoms;
    private CheckBox showBonds;
    private CheckBox showUnitCell;
    private Slider rotateXSlider;
    private Slider rotateYSlider;

    // Localization Fields
    private Label viewTitleLabel;
    private Label controlsTitleLabel;
    private Label rotationLabel; // fixed name to rotationLabel
    private Label axisXLabel;
    private Label axisYLabel;
    private Button loadCifBtn;
    private Button sampleBtn;

    // Data
    private static class AtomRecord {
        Vector<Real> position;
        String type; // "Na", "Cl", "C", "Si"

        AtomRecord(double x, double y, double z, String t) {
            this.position = org.jscience.mathematics.linearalgebra.vectors.VectorFactory.of(Real.class, Real.of(x), Real.of(y), Real.of(z));
            this.type = t;
        }
    }

    public CrystalStructureApp() {
        super();
        try {
            // No complex field initializations to move, but ensuring constructor exists for SPI safety
        } catch (Throwable t) {
            System.err.println("CRITICAL: Failed to initialize CrystalStructureApp: " + t.getMessage());
            t.printStackTrace();
        }
    }

    private enum LatticeType {
        SC("crystal.info.sc"),
        BCC("crystal.info.bcc"),
        FCC("crystal.info.fcc"),
        HCP("crystal.info.hcp"),
        DIAMOND("crystal.info.diamond"),
        NACL("crystal.info.nacl"),
        CSCL("crystal.info.cscl"),
        CIF("crystal.info.cif");

        private final String key;

        LatticeType(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return I18n.getInstance().get(key);
        }

    }

    @Override
    protected String getAppTitle() {
        return i18n.get("crystal.title");
    }

    @Override
    public String getDescription() {
        return i18n.get("crystal.desc");
    }

    @Override
    public boolean hasEditMenu() {
        return false;
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

        loadStructure(LatticeType.DIAMOND); // Initial load

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

        viewTitleLabel = new Label("💎 " + i18n.get("crystal.title"));
        viewTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Structure Selection
        latticeCombo = new ComboBox<>();
        latticeCombo.getItems().addAll(LatticeType.DIAMOND, LatticeType.NACL, LatticeType.CSCL, LatticeType.CIF);
        latticeCombo.setValue(LatticeType.DIAMOND);
        latticeCombo.setOnAction(e -> {
            if (latticeCombo.getValue() == LatticeType.CIF) {
                loadCif();
            } else {
                loadStructure(latticeCombo.getValue());
            }
        });
        latticeCombo.setMaxWidth(Double.MAX_VALUE);

        loadCifBtn = new Button(i18n.get("crystal.button.loadcif"));
        loadCifBtn.setOnAction(e -> loadCif());
        loadCifBtn.setMaxWidth(Double.MAX_VALUE);

        sampleBtn = new Button(
                MessageFormat.format(i18n.get("crystal.button.loadsample"), LatticeType.DIAMOND.toString()));
        sampleBtn.setOnAction(e -> loadSample());
        sampleBtn.setMaxWidth(Double.MAX_VALUE);

        infoLabel = new Label();
        infoLabel.setWrapText(true);

        // Toggles
        VBox toggles = new VBox(5);
        showAtoms = new CheckBox(i18n.get("crystal.check.atoms"));
        showAtoms.setSelected(true);
        showAtoms.setOnAction(e -> drawStructure());

        showBonds = new CheckBox(i18n.get("crystal.check.bonds"));
        showBonds.setSelected(true);
        showBonds.setOnAction(e -> drawStructure());

        showUnitCell = new CheckBox(i18n.get("crystal.check.unitcell"));
        showUnitCell.setSelected(true);
        showUnitCell.setOnAction(e -> drawStructure());

        toggles.getChildren().addAll(showAtoms, showBonds, showUnitCell);

        // Rotation Sliders
        rotateXSlider = new Slider(-180, 180, 0);
        rotateXSlider.setShowTickLabels(true);
        rotateXSlider.valueProperty().addListener((obs, oldVal, newVal) -> rx.setAngle(newVal.doubleValue()));

        rotateYSlider = new Slider(-180, 180, 0);
        rotateYSlider.setShowTickLabels(true);
        rotateYSlider.valueProperty().addListener((obs, oldVal, newVal) -> ry.setAngle(newVal.doubleValue()));

        controlsTitleLabel = new Label(i18n.get("crystal.panel.controls"));
        rotationLabel = new Label(i18n.get("crystal.label.rotation"));
        axisXLabel = new Label(i18n.get("crystal.axis.x"));
        axisYLabel = new Label(i18n.get("crystal.axis.y"));

        box.getChildren().addAll(
                viewTitleLabel,
                controlsTitleLabel,
                latticeCombo,
                loadCifBtn,
                sampleBtn,
                new Separator(),
                rotationLabel,
                axisXLabel, rotateXSlider,
                axisYLabel, rotateYSlider,
                new Separator(),
                toggles,
                new Separator(),
                infoLabel);
        return box;
    }

    @Override
    protected void updateLocalizedUI() {
        if (viewTitleLabel != null)
            viewTitleLabel.setText("💎 " + i18n.get("crystal.title"));
        if (controlsTitleLabel != null)
            controlsTitleLabel.setText(i18n.get("crystal.panel.controls"));
        if (loadCifBtn != null)
            loadCifBtn.setText(i18n.get("crystal.button.loadcif"));
        if (sampleBtn != null)
            sampleBtn.setText(
                    MessageFormat.format(i18n.get("crystal.button.loadsample"), LatticeType.DIAMOND.toString()));
        if (showAtoms != null)
            showAtoms.setText(i18n.get("crystal.check.atoms"));
        if (showBonds != null)
            showBonds.setText(i18n.get("crystal.check.bonds"));
        if (showUnitCell != null)
            showUnitCell.setText(i18n.get("crystal.check.unitcell"));
        if (rotationLabel != null)
            rotationLabel.setText(i18n.get("crystal.label.rotation"));
        if (axisXLabel != null)
            axisXLabel.setText(i18n.get("crystal.axis.x"));
        if (axisYLabel != null)
            axisYLabel.setText(i18n.get("crystal.axis.y"));

        if (latticeCombo != null) {
            LatticeType selected = latticeCombo.getValue();
            List<LatticeType> items = new ArrayList<>(latticeCombo.getItems());
            latticeCombo.getItems().clear();
            latticeCombo.getItems().addAll(items);
            latticeCombo.setValue(selected);
        }
    }

    private void loadCifStructure(java.io.InputStream is) {
        try {
            org.jscience.chemistry.loaders.CIFReader.CrystalStructure cif = org.jscience.chemistry.loaders.CIFReader
                    .load(is);

            if (cif == null)
                return;

            root3D.getChildren().clear();
            infoLabel.setText(MessageFormat.format(i18n.get("crystal.details.formula"), cif.chemicalFormula) + "\n" +
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
                        for (org.jscience.chemistry.loaders.CIFReader.AtomSite site : cif.atoms) {
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
                double vx = (a.position.get(0).doubleValue() - 1.0) * unitScaleX; // 1.0 is centering offset for 2x2x2
                double vy = (a.position.get(1).doubleValue() - 1.0) * unitScaleY;
                double vz = (a.position.get(2).doubleValue() - 1.0) * unitScaleZ;

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
            if (showBonds.isSelected()) {
                double bondCutoff = 1.8; // Angstroms
                // Na-Cl is 2.8, so this cutoff is small.
                // Dynamic cutoff?
                if (cif.chemicalFormula != null && cif.chemicalFormula.contains("Na"))
                    bondCutoff = 3.0; // NaCl

                List<javafx.scene.Node> nodes = new ArrayList<>(root3D.getChildren());
                for (int i = 0; i < nodes.size(); i++) {
                    for (int j = i + 1; j < nodes.size(); j++) {
                        if (nodes.get(i) instanceof Sphere && nodes.get(j) instanceof Sphere) {
                            Sphere s1 = (Sphere) nodes.get(i);
                            Sphere s2 = (Sphere) nodes.get(j);

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
            infoLabel.setText(i18n.get("crystal.error.load") + ": " + e.getMessage());
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
        try (java.io.InputStream is = getClass().getResourceAsStream("/org/jscience/chemistry/diamond.cif")) {
            if (is != null) {
                loadCifStructure(is);
            } else {
                infoLabel.setText(i18n.get("crystal.error.sample"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawStructure() {
        if (latticeCombo.getValue() != null && latticeCombo.getValue() != LatticeType.CIF) {
            loadStructure(latticeCombo.getValue());
        } else if (latticeCombo.getValue() == LatticeType.CIF) {
            // See previous discussion on re-drawing CIF
        }
    }

    private void loadStructure(LatticeType type) {
        root3D.getChildren().clear();
        infoLabel.setText("");

        List<AtomRecord> atoms = new ArrayList<>();
        // Base lattice constant (normalized), used for scaling
        @SuppressWarnings("unused")
        double latticeConstant = 1.0;

        if (type == LatticeType.SC) {
            // Simple Cubic
            infoLabel.setText(i18n.get("crystal.details.sc"));
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    for (int k = 0; k < 2; k++)
                        atoms.add(new AtomRecord(i, j, k, "Po"));
        } else if (type == LatticeType.BCC) {
            // BCC
            infoLabel.setText(i18n.get("crystal.details.bcc"));
            // corners
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    for (int k = 0; k < 2; k++) {
                        atoms.add(new AtomRecord(i, j, k, "Fe"));
                        // center
                        if (i < 1 && j < 1 && k < 1)
                            atoms.add(new AtomRecord(i + 0.5, j + 0.5, k + 0.5, "Fe"));
                    }
        } else if (type == LatticeType.FCC) {
            // FCC
            infoLabel.setText(i18n.get("crystal.details.fcc"));
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    for (int k = 0; k < 2; k++) {
                        atoms.add(new AtomRecord(i, j, k, "Cu"));
                        if (i < 1 && j < 1 && k < 1) {
                            atoms.add(new AtomRecord(i + 0.5, j + 0.5, k, "Cu"));
                            atoms.add(new AtomRecord(i + 0.5, j, k + 0.5, "Cu"));
                            atoms.add(new AtomRecord(i, j + 0.5, k + 0.5, "Cu"));
                        }
                    }
        } else if (type == LatticeType.NACL) {
            infoLabel.setText(i18n.get("crystal.details.nacl"));
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    for (int k = 0; k < 2; k++)
                        addNaClUnit(atoms, i, j, k);
        } else if (type == LatticeType.DIAMOND) {
            infoLabel.setText(i18n.get("crystal.details.diamond"));
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    for (int k = 0; k < 2; k++)
                        addDiamondUnit(atoms, i, j, k, "C");
        } else if (type == LatticeType.CSCL) {
            infoLabel.setText(i18n.get("crystal.details.cscl"));
            atoms.add(new AtomRecord(0, 0, 0, "Cl"));
            atoms.add(new AtomRecord(0.5, 0.5, 0.5, "Cs"));
        }

        double unitScale = 2.0;

        if (showAtoms.isSelected()) {
            for (AtomRecord rec : atoms) {
                double x = (rec.position.get(0).doubleValue() - 0.5) * 2 * unitScale;
                double y = (rec.position.get(1).doubleValue() - 0.5) * 2 * unitScale;
                double z = (rec.position.get(2).doubleValue() - 0.5) * 2 * unitScale;

                Sphere s = new Sphere(0.4);
                s.setMaterial(getMaterial(rec.type));
                s.setTranslateX(x);
                s.setTranslateY(y);
                s.setTranslateZ(z);
                root3D.getChildren().add(s);
            }
        }

        if (showBonds.isSelected()) {
            List<javafx.scene.Node> nodes = new ArrayList<>(root3D.getChildren());
            for (int i = 0; i < nodes.size(); i++) {
                for (int j = i + 1; j < nodes.size(); j++) {
                    if (nodes.get(i) instanceof Sphere && nodes.get(j) instanceof Sphere) {
                        Sphere s1 = (Sphere) nodes.get(i);
                        Sphere s2 = (Sphere) nodes.get(j);

                        Point3D p1 = new Point3D(s1.getTranslateX(), s1.getTranslateY(), s1.getTranslateZ());
                        Point3D p2 = new Point3D(s2.getTranslateX(), s2.getTranslateY(), s2.getTranslateZ());
                        double dist = p1.distance(p2);

                        // Rough bond estimation
                        if (dist < 4.0) { // arbitrary visual cutoff in view units
                            Cylinder bond = createLine(p1, p2);
                            root3D.getChildren().add(bond);
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

        Cylinder line = new Cylinder(0.03, height);
        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
        line.setMaterial(new PhongMaterial(Color.LIGHTGRAY));

        return line;
    }

    @Override
    protected void doNew() {
        if (latticeCombo != null) {
            latticeCombo.setValue(LatticeType.DIAMOND);
        }
        if (root3D != null) {
            loadStructure(LatticeType.DIAMOND);
        }
    }

    @Override
    protected void addAppHelpTopics(org.jscience.apps.framework.HelpDialog dialog) {
        dialog.addTopic("Structures", "Lattice Types",
                "Explore various crystal lattice structures:\n\n" +
                        "Ã¢â‚¬Â¢ **Simple Cubic (SC)**: Simplest repeating unit (e.g. Polonium).\n" +
                        "Ã¢â‚¬Â¢ **Body-Centered Cubic (BCC)**: Atoms at corners and center (e.g. Iron).\n" +
                        "Ã¢â‚¬Â¢ **Face-Centered Cubic (FCC)**: Atoms at corners and faces (e.g. Copper).\n" +
                        "Ã¢â‚¬Â¢ **Diamond**: Tetrahedral coordination (e.g. Carbon).\n" +
                        "Ã¢â‚¬Â¢ **NaCl**: Rock salt structure.\n" +
                        "Ã¢â‚¬Â¢ **CsCl**: Cesium Chloride structure.",
                null);
    }

    @Override
    protected void addAppTutorials(org.jscience.apps.framework.HelpDialog dialog) {
        dialog.addTopic("Tutorial", "Navigating 3D Space",
                "1. **Rotate**: Drag with the mouse to rotate the crystal structure.\n" +
                        "2. **Zoom**: Use the scroll wheel to zoom in and out.\n" +
                        "3. **Select Structure**: Use the dropdown menu to switch between lattice types.\n" +
                        "4. **Toggles**: Show/Hide Atoms, Bonds, or Unit Cell outlines.\n" +
                        "5. **Load CIF**: Import custom .cif files for advanced visualization.",
                null);
    }

    @Override
    protected byte[] serializeState() {
        java.util.Properties props = new java.util.Properties();
        if (latticeCombo.getValue() != null) {
            props.setProperty("lattice", latticeCombo.getValue().name());
        }
        props.setProperty("showAtoms", String.valueOf(showAtoms.isSelected()));
        props.setProperty("showBonds", String.valueOf(showBonds.isSelected()));
        props.setProperty("showUnitCell", String.valueOf(showUnitCell.isSelected()));
        props.setProperty("rotateX", String.valueOf(rotateXSlider.getValue()));
        props.setProperty("rotateY", String.valueOf(rotateYSlider.getValue()));

        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try {
            props.store(baos, "Crystal State");
            return baos.toByteArray();
        } catch (java.io.IOException e) {
            return null;
        }
    }

    @Override
    protected void deserializeState(byte[] data) {
        java.util.Properties props = new java.util.Properties();
        try {
            props.load(new java.io.ByteArrayInputStream(data));

            String latticeName = props.getProperty("lattice");
            if (latticeName != null) {
                latticeCombo.setValue(LatticeType.valueOf(latticeName));
            }

            showAtoms.setSelected(Boolean.parseBoolean(props.getProperty("showAtoms", "true")));
            showBonds.setSelected(Boolean.parseBoolean(props.getProperty("showBonds", "true")));
            showUnitCell.setSelected(Boolean.parseBoolean(props.getProperty("showUnitCell", "true")));
            rotateXSlider.setValue(Double.parseDouble(props.getProperty("rotateX", "0")));
            rotateYSlider.setValue(Double.parseDouble(props.getProperty("rotateY", "0")));

            loadStructure(latticeCombo.getValue());
        } catch (Exception e) {
            showError("Load Error", "Failed to restore state: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getCategory() {
        return "Chemistry";
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("CrystalStructureApp.name", "CrystalStructure");
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }
}
