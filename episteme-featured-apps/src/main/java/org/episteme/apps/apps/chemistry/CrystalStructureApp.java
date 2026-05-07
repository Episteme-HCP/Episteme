/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.apps.apps.chemistry;

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
import org.episteme.apps.apps.framework.FeaturedAppBase;
import org.episteme.core.ui.i18n.I18N;
import org.episteme.core.ui.Viewer;
import com.google.auto.service.AutoService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.sets.Reals;

@AutoService(Viewer.class)
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
    private Label rotationLabel; 
    private Label axisXLabel;
    private Label axisYLabel;
    private Button loadCifBtn;
    private Button sampleBtn;

    // Data
    private static class AtomRecord {
        Vector<Real> position;
        String type; // "Na", "Cl", "C", "Si", "Zn"

        AtomRecord(double x, double y, double z, String t) {
            this.position = Vector.of(java.util.Arrays.asList(Real.of(x), Real.of(y), Real.of(z)), Reals.getInstance());
            this.type = t;
        }
    }

    private org.episteme.natural.chemistry.loaders.CIFReader.CrystalStructure lastLoadedCif;

    public CrystalStructureApp() {
        super();
    }

    private enum LatticeType {
        SC("crystal.info.sc", "Simple Cubic (SC)"),
        BCC("crystal.info.bcc", "Body-Centered Cubic (BCC)"),
        FCC("crystal.info.fcc", "Face-Centered Cubic (FCC)"),
        HCP("crystal.info.hcp", "Hexagonal Close Packed (HCP)"),
        DIAMOND("crystal.info.diamond", "Diamond (FCC-based)"),
        NACL("crystal.info.nacl", "Sodium Chloride (NaCl)"),
        CSCL("crystal.info.cscl", "Cesium Chloride (CsCl)"),
        CIF("crystal.info.cif", "Custom CIF File");

        private final String key;
        private final String def;

        LatticeType(String key, String def) {
            this.key = key;
            this.def = def;
        }

        @Override
        public String toString() {
            return I18N.getInstance().get(key, def);
        }
    }

    @Override
    protected String getAppTitle() {
        return I18N.getInstance().get("viewer.crystalstructureapp.name", "Crystal Structure Viewer");
    }

    @Override
    public String getName() {
        return getAppTitle();
    }

    @Override
    public String getDescription() {
        return I18N.getInstance().get("viewer.crystalstructureapp.desc", "3D visualization of crystal lattices.");
    }

    @Override
    public String getLongDescription() {
        return I18N.getInstance().get("viewer.crystalstructureapp.longdesc", "Advanced 3D visualization tool for exploring crystalline structures at the atomic level. Supports standard lattice types including Simple Cubic, BCC, FCC, HCP, Diamond, NaCl, and CsCl. Features custom CIF file import, interactive 3D rotation and zoom, and configurable display of atoms, bonds, and unit cell boundaries.");
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

        ss.setOnScroll((ScrollEvent e) -> {
            tz.setZ(tz.getZ() + e.getDeltaY() * 0.1);
        });

        return ss;
    }

    private VBox createControls() {
        VBox box = new VBox(15);
        box.setStyle("-fx-padding: 15; -fx-background-color: #f4f4f4;");

        viewTitleLabel = new Label("💎 " + i18n.get("crystal.title", "Crystal Structure Viewer"));
        viewTitleLabel.getStyleClass().add("font-bold");

        latticeCombo = new ComboBox<>();
        latticeCombo.getItems().addAll(LatticeType.SC, LatticeType.BCC, LatticeType.FCC, LatticeType.HCP, LatticeType.DIAMOND, LatticeType.NACL, LatticeType.CSCL, LatticeType.CIF);
        latticeCombo.setValue(LatticeType.DIAMOND);
        latticeCombo.setOnAction(e -> {
            if (latticeCombo.getValue() == LatticeType.CIF) {
                loadCif();
            } else {
                loadStructure(latticeCombo.getValue());
            }
        });
        latticeCombo.setMaxWidth(Double.MAX_VALUE);

        loadCifBtn = new Button(i18n.get("crystal.button.loadcif", "Load CIF File..."));
        loadCifBtn.setOnAction(e -> loadCif());
        loadCifBtn.setMaxWidth(Double.MAX_VALUE);

        sampleBtn = new Button(
                MessageFormat.format(i18n.get("crystal.button.loadsample", "Load Sample ({0})"), LatticeType.DIAMOND.toString()));
        sampleBtn.setOnAction(e -> loadSample());
        sampleBtn.setMaxWidth(Double.MAX_VALUE);

        infoLabel = new Label();
        infoLabel.setWrapText(true);

        VBox toggles = new VBox(5);
        showAtoms = new CheckBox(i18n.get("crystal.check.atoms", "Show Atoms"));
        showAtoms.setSelected(true);
        showAtoms.setOnAction(e -> drawStructure());

        showBonds = new CheckBox(i18n.get("crystal.check.bonds", "Show Bonds"));
        showBonds.setSelected(true);
        showBonds.setOnAction(e -> drawStructure());

        showUnitCell = new CheckBox(i18n.get("crystal.check.unitcell", "Show Unit Cell"));
        showUnitCell.setSelected(true);
        showUnitCell.setOnAction(e -> drawStructure());

        toggles.getChildren().addAll(showAtoms, showBonds, showUnitCell);

        rotateXSlider = new Slider(-180, 180, 0);
        rotateXSlider.setShowTickLabels(true);
        rotateXSlider.valueProperty().addListener((obs, oldVal, newVal) -> rx.setAngle(newVal.doubleValue()));

        rotateYSlider = new Slider(-180, 180, 0);
        rotateYSlider.setShowTickLabels(true);
        rotateYSlider.valueProperty().addListener((obs, oldVal, newVal) -> ry.setAngle(newVal.doubleValue()));

        controlsTitleLabel = new Label(i18n.get("crystal.panel.controls", "Controls"));
        rotationLabel = new Label(i18n.get("crystal.label.rotation", "Rotation"));
        axisXLabel = new Label(i18n.get("crystal.axis.x", "X Axis"));
        axisYLabel = new Label(i18n.get("crystal.axis.y", "Y Axis"));

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
            viewTitleLabel.setText("💎 " + i18n.get("crystal.title", "Crystal Structure Viewer"));
        if (controlsTitleLabel != null)
            controlsTitleLabel.setText(i18n.get("crystal.panel.controls", "Controls"));
        if (loadCifBtn != null)
            loadCifBtn.setText(i18n.get("crystal.button.loadcif", "Load CIF File..."));
        if (sampleBtn != null)
            sampleBtn.setText(
                    MessageFormat.format(i18n.get("crystal.button.loadsample", "Load Sample ({0})"), LatticeType.DIAMOND.toString()));
        if (showAtoms != null)
            showAtoms.setText(i18n.get("crystal.check.atoms", "Show Atoms"));
        if (showBonds != null)
            showBonds.setText(i18n.get("crystal.check.bonds", "Show Bonds"));
        if (showUnitCell != null)
            showUnitCell.setText(i18n.get("crystal.check.unitcell", "Show Unit Cell"));
        if (rotationLabel != null)
            rotationLabel.setText(i18n.get("crystal.label.rotation", "Rotation"));
        if (axisXLabel != null)
            axisXLabel.setText(i18n.get("crystal.axis.x", "X Axis"));
        if (axisYLabel != null)
            axisYLabel.setText(i18n.get("crystal.axis.y", "Y Axis"));

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
            org.episteme.natural.chemistry.loaders.CIFReader.CrystalStructure cif = org.episteme.natural.chemistry.loaders.CIFReader.load(is);
            if (cif == null) return;
            this.lastLoadedCif = cif;
            renderCifInternal(cif);
        } catch (Exception e) {
            infoLabel.setText(i18n.get("crystal.error.load", "Error loading CIF") + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void renderCifInternal(org.episteme.natural.chemistry.loaders.CIFReader.CrystalStructure cif) {
        root3D.getChildren().clear();
        infoLabel.setText(MessageFormat.format(i18n.get("crystal.details.formula", "Formula: {0}"), cif.chemicalFormula) + "\n" +
                "a=" + cif.a + ", b=" + cif.b + ", c=" + cif.c + "\n" +
                "alpha=" + cif.alpha + ", beta=" + cif.beta + ", gamma=" + cif.gamma);

        List<AtomRecord> atoms = new ArrayList<>();
        int repeat = 2;
        double scale = 2.0;
        double unitScaleX = scale * (cif.a / 3.0);
        double unitScaleY = scale * (cif.b / 3.0);
        double unitScaleZ = scale * (cif.c / 3.0);

        for (int i = 0; i < repeat; i++) {
            for (int j = 0; j < repeat; j++) {
                for (int k = 0; k < repeat; k++) {
                    for (org.episteme.natural.chemistry.loaders.CIFReader.AtomSite site : cif.atoms) {
                        atoms.add(new AtomRecord(site.x + i, site.y + j, site.z + k, site.symbol));
                    }
                }
            }
        }

        if (showAtoms.isSelected()) {
            for (AtomRecord a : atoms) {
                double vx = (a.position.get(0).doubleValue() - 1.0) * unitScaleX;
                double vy = (a.position.get(1).doubleValue() - 1.0) * unitScaleY;
                double vz = (a.position.get(2).doubleValue() - 1.0) * unitScaleZ;

                Sphere s = new Sphere(0.4);
                s.setMaterial(getMaterial(a.type));
                s.setTranslateX(vx);
                s.setTranslateY(vy);
                s.setTranslateZ(vz);
                root3D.getChildren().add(s);
            }
        }

        if (showBonds.isSelected()) {
            double bondCutoff = (cif.chemicalFormula != null && cif.chemicalFormula.contains("Na")) ? 3.0 : 1.8;
            List<javafx.scene.Node> atomNodes = new ArrayList<>();
            for(javafx.scene.Node n : root3D.getChildren()) if(n instanceof Sphere) atomNodes.add(n);
            
            for (int i = 0; i < atomNodes.size(); i++) {
                for (int j = i + 1; j < atomNodes.size(); j++) {
                    Sphere s1 = (Sphere) atomNodes.get(i);
                    Sphere s2 = (Sphere) atomNodes.get(j);
                    Point3D p1 = new Point3D(s1.getTranslateX(), s1.getTranslateY(), s1.getTranslateZ());
                    Point3D p2 = new Point3D(s2.getTranslateX(), s2.getTranslateY(), s2.getTranslateZ());
                    double distAng = p1.distance(p2) / (scale / 3.0);
                    if (distAng < bondCutoff) {
                        root3D.getChildren().add(createLine(p1, p2));
                    }
                }
            }
        }
        
        if (showUnitCell.isSelected()) {
            drawUnitCell(-unitScaleX, -unitScaleY, -unitScaleZ, repeat * unitScaleX, repeat * unitScaleY, repeat * unitScaleZ);
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
        try (java.io.InputStream is = getClass().getResourceAsStream("/org.episteme.natural.chemistry/diamond.cif")) {
            if (is != null) {
                loadCifStructure(is);
            } else {
                infoLabel.setText(i18n.get("crystal.error.sample", "Sample file not found."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawStructure() {
        if (latticeCombo.getValue() != null && latticeCombo.getValue() != LatticeType.CIF) {
            loadStructure(latticeCombo.getValue());
        } else if (latticeCombo.getValue() == LatticeType.CIF && lastLoadedCif != null) {
            renderCifInternal(lastLoadedCif);
        }
    }

    private void loadStructure(LatticeType type) {
        root3D.getChildren().clear();
        infoLabel.setText("");

        List<AtomRecord> atoms = new ArrayList<>();
        double unitScale = 2.0;

        if (type == LatticeType.SC) {
            infoLabel.setText(i18n.get("crystal.details.sc", "Simple Cubic (Po model)"));
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    for (int k = 0; k < 2; k++)
                        atoms.add(new AtomRecord(i, j, k, "Po"));
        } else if (type == LatticeType.BCC) {
            infoLabel.setText(i18n.get("crystal.details.bcc", "Body-Centered Cubic (Fe model)"));
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    for (int k = 0; k < 2; k++) {
                        atoms.add(new AtomRecord(i, j, k, "Fe"));
                        if (i < 1 && j < 1 && k < 1)
                            atoms.add(new AtomRecord(i + 0.5, j + 0.5, k + 0.5, "Fe"));
                    }
        } else if (type == LatticeType.FCC) {
            infoLabel.setText(i18n.get("crystal.details.fcc", "Face-Centered Cubic (Cu model)"));
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
            infoLabel.setText(i18n.get("crystal.details.nacl", "Rock Salt (NaCl model)"));
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    for (int k = 0; k < 2; k++)
                        addNaClUnit(atoms, i, j, k);
        } else if (type == LatticeType.DIAMOND) {
            infoLabel.setText(i18n.get("crystal.details.diamond", "Diamond Cubic (C model)"));
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 2; j++)
                    for (int k = 0; k < 2; k++)
                        addDiamondUnit(atoms, i, j, k, "C");
        } else if (type == LatticeType.CSCL) {
            infoLabel.setText(i18n.get("crystal.details.cscl", "Cesium Chloride (CsCl model)"));
            atoms.add(new AtomRecord(0, 0, 0, "Cl"));
            atoms.add(new AtomRecord(0.5, 0.5, 0.5, "Cs"));
        } else if (type == LatticeType.HCP) {
            infoLabel.setText(i18n.get("crystal.details.hcp", "Hexagonal Close Packed (Zn/Mg model)"));
            addHCPUnit(atoms, 0, 0, 0);
        }

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
            List<javafx.scene.Node> atomNodes = new ArrayList<>();
            for(javafx.scene.Node n : root3D.getChildren()) if(n instanceof Sphere) atomNodes.add(n);
            for (int i = 0; i < atomNodes.size(); i++) {
                for (int j = i + 1; j < atomNodes.size(); j++) {
                    Sphere s1 = (Sphere) atomNodes.get(i);
                    Sphere s2 = (Sphere) atomNodes.get(j);
                    Point3D p1 = new Point3D(s1.getTranslateX(), s1.getTranslateY(), s1.getTranslateZ());
                    Point3D p2 = new Point3D(s2.getTranslateX(), s2.getTranslateY(), s2.getTranslateZ());
                    if (p1.distance(p2) < 4.0) {
                        root3D.getChildren().add(createLine(p1, p2));
                    }
                }
            }
        }

        if (showUnitCell.isSelected()) {
            drawUnitCell(-2 * unitScale, -2 * unitScale, -2 * unitScale, 4 * unitScale, 4 * unitScale, 4 * unitScale);
        }
    }

    private void addNaClUnit(List<AtomRecord> atoms, double dx, double dy, double dz) {
        atoms.add(new AtomRecord(dx, dy, dz, "Na"));
        atoms.add(new AtomRecord(dx + 0.5, dy + 0.5, dz, "Na"));
        atoms.add(new AtomRecord(dx + 0.5, dy, dz + 0.5, "Na"));
        atoms.add(new AtomRecord(dx, dy + 0.5, dz + 0.5, "Na"));
        atoms.add(new AtomRecord(dx + 0.5, dy, dz, "Cl"));
        atoms.add(new AtomRecord(dx, dy + 0.5, dz, "Cl"));
        atoms.add(new AtomRecord(dx, dy, dz + 0.5, "Cl"));
        atoms.add(new AtomRecord(dx + 0.5, dy + 0.5, dz + 0.5, "Cl"));
    }

    private void addDiamondUnit(List<AtomRecord> atoms, double dx, double dy, double dz, String type) {
        double[][] basis = {
                { 0, 0, 0 }, { 0.5, 0.5, 0 }, { 0.5, 0, 0.5 }, { 0, 0.5, 0.5 }, 
                { 0.25, 0.25, 0.25 }, { 0.75, 0.75, 0.25 }, { 0.75, 0.25, 0.75 }, { 0.25, 0.75, 0.75 }
        };
        for (double[] p : basis) {
            atoms.add(new AtomRecord(dx + p[0], dy + p[1], dz + p[2], type));
        }
    }

    private void addHCPUnit(List<AtomRecord> atoms, double dx, double dy, double dz) {
        double c_a = 1.633;
        double sqrt3_2 = Math.sqrt(3) / 2.0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    double ox = dx + i + 0.5 * j;
                    double oy = dy + j * sqrt3_2;
                    double oz = dz + k * c_a;
                    atoms.add(new AtomRecord(ox, oy, oz, "Zn"));
                    atoms.add(new AtomRecord(ox + 0.5, oy + sqrt3_2/3.0, oz + c_a/2.0, "Zn"));
                }
            }
        }
    }

    private PhongMaterial getMaterial(String type) {
        PhongMaterial m = new PhongMaterial();
        m.setSpecularColor(Color.WHITE);
        switch (type) {
            case "C": m.setDiffuseColor(Color.web("#333333")); break;
            case "Si": m.setDiffuseColor(Color.web("#666666")); break;
            case "Na": m.setDiffuseColor(Color.PURPLE); break;
            case "Cl": m.setDiffuseColor(Color.GREEN); break;
            case "Cs": m.setDiffuseColor(Color.GOLD); break;
            case "Zn": m.setDiffuseColor(Color.LIGHTBLUE); break;
            default: m.setDiffuseColor(Color.RED);
        }
        return m;
    }

    private void drawUnitCell(double x, double y, double z, double w, double h, double d) {
        Point3D p1 = new Point3D(x, y, z);
        Point3D p2 = new Point3D(x + w, y, z);
        Point3D p3 = new Point3D(x + w, y + h, z);
        Point3D p4 = new Point3D(x, y + h, z);
        Point3D p5 = new Point3D(x, y, z + d);
        Point3D p6 = new Point3D(x + w, y, z + d);
        Point3D p7 = new Point3D(x + w, y + h, z + d);
        Point3D p8 = new Point3D(x, y + h, z + d);
        root3D.getChildren().addAll(
            createLine(p1, p2), createLine(p2, p3), createLine(p3, p4), createLine(p4, p1),
            createLine(p5, p6), createLine(p6, p7), createLine(p7, p8), createLine(p8, p5),
            createLine(p1, p5), createLine(p2, p6), createLine(p3, p7), createLine(p4, p8)
        );
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
        if (latticeCombo != null) latticeCombo.setValue(LatticeType.DIAMOND);
        if (root3D != null) loadStructure(LatticeType.DIAMOND);
    }

    @Override
    protected void addAppHelpTopics(org.episteme.apps.apps.framework.HelpDialog dialog) {
        dialog.addTopic("Structures", "Lattice Types",
                "Explore various crystal lattice structures:\n\n" +
                        "• **Simple Cubic (SC)**: Simplest repeating unit (e.g. Polonium).\n" +
                        "• **Body-Centered Cubic (BCC)**: Atoms at corners and center (e.g. Iron).\n" +
                        "• **Face-Centered Cubic (FCC)**: Atoms at corners and faces (e.g. Copper).\n" +
                        "• **HCP**: Hexagonal Close Packed (e.g. Zinc).\n" +
                        "• **Diamond**: Tetrahedral coordination (e.g. Carbon).\n" +
                        "• **NaCl**: Rock salt structure.\n" +
                        "• **CsCl**: Cesium Chloride structure.",
                null);
    }

    @Override
    protected void addAppTutorials(org.episteme.apps.apps.framework.HelpDialog dialog) {
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
        if (latticeCombo.getValue() != null) props.setProperty("lattice", latticeCombo.getValue().name());
        props.setProperty("showAtoms", String.valueOf(showAtoms.isSelected()));
        props.setProperty("showBonds", String.valueOf(showBonds.isSelected()));
        props.setProperty("showUnitCell", String.valueOf(showUnitCell.isSelected()));
        props.setProperty("rotateX", String.valueOf(rotateXSlider.getValue()));
        props.setProperty("rotateY", String.valueOf(rotateYSlider.getValue()));
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try {
            props.store(baos, "Crystal State");
            return baos.toByteArray();
        } catch (java.io.IOException e) { return null; }
    }

    @Override
    protected void deserializeState(byte[] data) {
        java.util.Properties props = new java.util.Properties();
        try {
            props.load(new java.io.ByteArrayInputStream(data));
            String latticeName = props.getProperty("lattice");
            if (latticeName != null) latticeCombo.setValue(LatticeType.valueOf(latticeName));
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

    public static void main(String[] args) { launch(args); }

    @Override
    public String getCategory() {
        return org.episteme.core.ui.i18n.I18N.getInstance().get("category.chemistry", "Chemistry");
    }
}
