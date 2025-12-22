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
import javafx.scene.input.MouseEvent;
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
            this.x = x; this.y = y; this.z = z; this.type = t;
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
        
        // Camera setup
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(100.0);
        camera.setTranslateZ(-15);
        
        rx = new Rotate(0, Rotate.X_AXIS);
        ry = new Rotate(0, Rotate.Y_AXIS);
        tz = new Translate(0, 0, -15);
        
        root3D.getTransforms().addAll(tz, rx, ry);
        
        SubScene ss = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        ss.setCamera(camera);
        ss.setFill(Color.web("#222"));
        
        // Mouse handling
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
            tz.setZ(tz.getZ() + e.getDeltaY() * 0.05);
        });
        
        return ss;
    }

    private VBox createControls() {
        VBox box = new VBox(15);
        box.setStyle("-fx-padding: 15; -fx-background-color: #f4f4f4;");

        Label title = new Label("💎 Crystal Explorer");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        structureBox = new ComboBox<>();
        structureBox.getItems().addAll("Diamond", "Sodium Chloride (NaCl)", "Silicon", "Cesium Chloride (CsCl)");
        structureBox.setValue("Diamond");
        structureBox.setOnAction(e -> loadStructure(structureBox.getValue()));
        
        infoLabel = new Label();
        infoLabel.setWrapText(true);
        
        showBondsCheck = new CheckBox("Show Bonds");
        showBondsCheck.setSelected(true);
        showBondsCheck.setOnAction(e -> loadStructure(structureBox.getValue()));

        box.getChildren().addAll(title, new Label("Structure:"), structureBox, showBondsCheck, new Separator(), infoLabel);
        return box;
    }

    private void loadStructure(String name) {
        root3D.getChildren().clear();
        List<AtomRecord> atoms = new ArrayList<>();
        UnitCell cell = null;
        double bondLimit = 2.0; 
        
        if (name.contains("NaCl")) {
            cell = UnitCell.NACL;
            bondLimit = 3.0;
            // NaCl FCC Basis
            // Na: (0,0,0) + FCC translations
            // Cl: (0.5, 0, 0) + FCC translations
            generateFCC(atoms, 0, 0, 0, "Na");
            generateFCC(atoms, 0.5, 0, 0, "Cl"); // Offset
            generateFCC(atoms, 0, 0.5, 0, "Cl");
            generateFCC(atoms, 0, 0, 0.5, "Cl");
            generateFCC(atoms, 0.5, 0.5, 0.5, "Cl"); // Center
            // Actually standard NaCl basis is Na at 000, Cl at 0.5,0.5,0.5 ?
            // Let's use standard basis: 
            // Na: 000, 0.5,0.5,0, 0.5,0,0.5, 0,0.5,0.5
            // Cl: 0.5,0.5,0.5, 0,0,0.5, 0,0.5,0, 0.5,0,0
            atoms.clear();
            double[][] ffc = {{0,0,0}, {0.5,0.5,0}, {0.5,0,0.5}, {0,0.5,0.5}};
            for(double[] p : ffc) atoms.add(new AtomRecord(p[0], p[1], p[2], "Na"));
            for(double[] p : ffc) atoms.add(new AtomRecord(p[0]+0.5, p[1], p[2], "Cl")); // 0.5 shift X
            for(double[] p : ffc) atoms.add(new AtomRecord(p[0], p[1]+0.5, p[2], "Cl"));
            for(double[] p : ffc) atoms.add(new AtomRecord(p[0], p[1], p[2]+0.5, "Cl"));
            // Simplification: Standard lattice
            
            // Clean Re-gen
            atoms.clear();
            for(int i=0; i<2; i++) for(int j=0; j<2; j++) for(int k=0; k<2; k++) {
                   // 2x2x2 Supercell for visualization
                   addNaClUnit(atoms, i, j, k);
            }
            
        } else if (name.equals("Diamond") || name.equals("Silicon")) {
            cell = name.equals("Diamond") ? UnitCell.DIAMOND : UnitCell.SILICON;
            bondLimit = 2.5; 
            // Diamond Structure: FCC + (1/4, 1/4, 1/4) basis
            // 0,0,0 and 0.25, 0.25, 0.25
            for(int i=0; i<2; i++) for(int j=0; j<2; j++) for(int k=0; k<2; k++) {
                addDiamondUnit(atoms, i, j, k, name.equals("Diamond") ? "C" : "Si");
            }
        } else if (name.contains("CsCl")) {
             cell = UnitCell.cubic(4.123);
             bondLimit = 3.5;
             // Simple Cubic with basis
             // Cs at 0,0,0
             // Cl at 0.5, 0.5, 0.5
              for(int i=0; i<2; i++) for(int j=0; j<2; j++) for(int k=0; k<2; k++) {
                atoms.add(new AtomRecord(i, j, k, "Cs"));
                atoms.add(new AtomRecord(i+0.5, j+0.5, k+0.5, "Cl"));
            }
        }

        if (cell != null) {
            infoLabel.setText("System: " + cell.getSystem() + "\n" +
                              "a = " + cell.getA() + " Å\n" +
                              "Volume = " + String.format("%.2f", cell.volume().doubleValue()) + " Å³");
            
            double scale = 2.0; // Visual scale
            // Draw Atoms
            for (AtomRecord a : atoms) {
                // Center visualization around 0
                double x = (a.x - 1.0) * scale * (cell.getA().doubleValue() / 3.0);
                double y = (a.y - 1.0) * scale * (cell.getB().doubleValue() / 3.0);
                double z = (a.z - 1.0) * scale * (cell.getC().doubleValue() / 3.0);
                
                Sphere s = new Sphere(0.4);
                s.setMaterial(getMaterial(a.type));
                s.setTranslateX(x);
                s.setTranslateY(y);
                s.setTranslateZ(z);
                root3D.getChildren().add(s);
            }
            
            // Draw Bonds (O(N^2) naive but N is small)
            if (showBondsCheck.isSelected()) {
                for (int i=0; i<root3D.getChildren().size(); i++) {
                    for (int j=i+1; j<root3D.getChildren().size(); j++) {
                        if (root3D.getChildren().get(i) instanceof Sphere s1 && root3D.getChildren().get(j) instanceof Sphere s2) {
                             Point3D p1 = new Point3D(s1.getTranslateX(), s1.getTranslateY(), s1.getTranslateZ());
                             Point3D p2 = new Point3D(s2.getTranslateX(), s2.getTranslateY(), s2.getTranslateZ());
                             double dist = p1.distance(p2);
                             if (dist < bondLimit) { // Threshold based on scale
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
        // Na
        atoms.add(new AtomRecord(dx, dy, dz, "Na"));
        atoms.add(new AtomRecord(dx+0.5, dy+0.5, dz, "Na"));
        atoms.add(new AtomRecord(dx+0.5, dy, dz+0.5, "Na"));
        atoms.add(new AtomRecord(dx, dy+0.5, dz+0.5, "Na"));
        // Cl
        atoms.add(new AtomRecord(dx+0.5, dy, dz, "Cl"));
        atoms.add(new AtomRecord(dx, dy+0.5, dz, "Cl"));
        atoms.add(new AtomRecord(dx, dy, dz+0.5, "Cl"));
        atoms.add(new AtomRecord(dx+0.5, dy+0.5, dz+0.5, "Cl"));
    }
    
    private void addDiamondUnit(List<AtomRecord> atoms, double dx, double dy, double dz, String type) {
        double[][] basis = {
            {0,0,0}, {0.5,0.5,0}, {0.5,0,0.5}, {0,0.5,0.5}, // FCC
            {0.25,0.25,0.25}, {0.75,0.75,0.25}, {0.75,0.25,0.75}, {0.25,0.75,0.75} // Offset FCC
        };
        for(double[] p : basis) {
             atoms.add(new AtomRecord(dx+p[0], dy+p[1], dz+p[2], type));
        }
    }
    
    private PhongMaterial getMaterial(String type) {
        PhongMaterial m = new PhongMaterial();
        m.setSpecularColor(Color.WHITE);
        switch(type) {
            case "C": m.setDiffuseColor(Color.GRAY); break;
            case "Si": m.setDiffuseColor(Color.DARKGRAY); break;
            case "Na": m.setDiffuseColor(Color.PURPLE); break;
            case "Cl": m.setDiffuseColor(Color.GREEN); break;
            case "Cs": m.setDiffuseColor(Color.GOLD); break;
            default: m.setDiffuseColor(Color.RED);
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

        Cylinder line = new Cylinder(0.1, height); // Bond thickness
        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
        line.setMaterial(new PhongMaterial(Color.LIGHTGRAY));

        return line;
    }
    
    private void generateFCC(List<AtomRecord> atoms, double ox, double oy, double oz, String type) {
         // Helper unused in final logic but good for ref
    }

    public static void main(String[] args) {
        launch(args);
    }
}
