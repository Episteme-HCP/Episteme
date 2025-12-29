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

package org.jscience.ui.biology.anatomy;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jscience.biology.loaders.StlMeshLoader;
import org.jscience.ui.i18n.I18n;

import java.io.File;

/**
 * 3D Human Body Anatomy Viewer.
 * Displays anatomical layers (skeleton, muscles, organs, skin) with visibility
 * controls.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HumanBodyViewer extends Application {

    private final Group world = new Group();
    private final Group bodyGroup = new Group();

    // Anatomical layers
    private final Group skeletonLayer = new Group();
    private final Group muscleLayer = new Group();
    private final Group organLayer = new Group();
    private final Group skinLayer = new Group();
    private final Group nervousLayer = new Group();
    private final Group circulatoryLayer = new Group();

    // Camera controls
    private final Rotate cameraX = new Rotate(-10, Rotate.X_AXIS);
    private final Rotate cameraY = new Rotate(0, Rotate.Y_AXIS);
    private final Translate cameraZ = new Translate(0, 0, -500);
    private double mouseX, mouseY;

    // Info display
    private Label infoLabel;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dark-viewer-root");

        // Build 3D scene
        build3DBody();

        SubScene subScene = create3DSubScene();
        root.setCenter(subScene);

        // Control Panel
        VBox controls = createControlPanel();
        root.setRight(controls);

        Scene scene = new Scene(root, 1200, 800);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("viewer.humanbody"));
        stage.setScene(scene);
        stage.show();
    }

    private SubScene create3DSubScene() {
        // Lights
        PointLight mainLight = new PointLight(Color.WHITE);
        mainLight.setTranslateZ(-300);
        mainLight.setTranslateY(-200);

        AmbientLight ambient = new AmbientLight(Color.rgb(60, 60, 80));

        // Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);
        camera.setFieldOfView(45);

        Group cameraGroup = new Group(camera);
        cameraGroup.getTransforms().addAll(cameraY, cameraX, cameraZ);

        world.getChildren().addAll(bodyGroup, mainLight, ambient, cameraGroup);
        bodyGroup.getChildren().addAll(skeletonLayer, muscleLayer, organLayer,
                nervousLayer, circulatoryLayer, skinLayer);

        SubScene subScene = new SubScene(world, 900, 800, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#0a0a15"));
        subScene.setCamera(camera);

        // Mouse controls
        subScene.setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        subScene.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - mouseX;
            double dy = e.getSceneY() - mouseY;
            if (e.isPrimaryButtonDown()) {
                cameraY.setAngle(cameraY.getAngle() + dx * 0.3);
                cameraX.setAngle(cameraX.getAngle() - dy * 0.3);
            }
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        subScene.setOnScroll(e -> {
            cameraZ.setZ(cameraZ.getZ() + e.getDeltaY());
        });

        return subScene;
    }

    private VBox createControlPanel() {
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(20));
        controls.getStyleClass().add("dark-viewer-sidebar");
        controls.setPrefWidth(280);

        // Title
        Label title = new Label(I18n.getInstance().get("humanbody.layers"));
        title.getStyleClass().add("dark-label-accent");

        // Layer checkboxes
        CheckBox skeletonCheck = createLayerCheckbox(I18n.getInstance().get("humanbody.skeleton"), skeletonLayer,
                Color.IVORY, true);
        CheckBox muscleCheck = createLayerCheckbox(I18n.getInstance().get("humanbody.muscle"), muscleLayer,
                Color.INDIANRED, false);
        CheckBox organCheck = createLayerCheckbox(I18n.getInstance().get("humanbody.organs"), organLayer, Color.CORAL,
                false);
        CheckBox nervousCheck = createLayerCheckbox(I18n.getInstance().get("humanbody.nervous"), nervousLayer,
                Color.YELLOW, false);
        CheckBox circulatoryCheck = createLayerCheckbox(I18n.getInstance().get("humanbody.circulatory"),
                circulatoryLayer, Color.DARKRED, false);
        CheckBox skinCheck = createLayerCheckbox(I18n.getInstance().get("humanbody.skin"), skinLayer, Color.PEACHPUFF,
                false);

        // Preset buttons
        Label presetsLabel = new Label(I18n.getInstance().get("humanbody.presets"));
        presetsLabel.getStyleClass().add("dark-label");

        Button skeletonOnlyBtn = new Button(I18n.getInstance().get("humanbody.preset.skeleton"));
        skeletonOnlyBtn.setMaxWidth(Double.MAX_VALUE);
        skeletonOnlyBtn.setOnAction(e -> {
            skeletonCheck.setSelected(true);
            muscleCheck.setSelected(false);
            organCheck.setSelected(false);
            nervousCheck.setSelected(false);
            circulatoryCheck.setSelected(false);
            skinCheck.setSelected(false);
            updateLayerVisibility();
        });

        Button muscularBtn = new Button(I18n.getInstance().get("humanbody.preset.muscle"));
        muscularBtn.setMaxWidth(Double.MAX_VALUE);
        muscularBtn.setOnAction(e -> {
            skeletonCheck.setSelected(true);
            muscleCheck.setSelected(true);
            organCheck.setSelected(false);
            nervousCheck.setSelected(false);
            circulatoryCheck.setSelected(false);
            skinCheck.setSelected(false);
            updateLayerVisibility();
        });

        Button fullBodyBtn = new Button(I18n.getInstance().get("humanbody.preset.full"));
        fullBodyBtn.setMaxWidth(Double.MAX_VALUE);
        fullBodyBtn.setOnAction(e -> {
            skeletonCheck.setSelected(true);
            muscleCheck.setSelected(true);
            organCheck.setSelected(true);
            nervousCheck.setSelected(true);
            circulatoryCheck.setSelected(true);
            skinCheck.setSelected(true);
            updateLayerVisibility();
        });

        // Info panel
        Label infoTitle = new Label(I18n.getInstance().get("humanbody.info"));
        infoTitle.getStyleClass().add("dark-label");

        infoLabel = new Label(I18n.getInstance().get("humanbody.clickinfo"));
        infoLabel.setWrapText(true);
        infoLabel.setStyle("-fx-text-fill: #cccccc;");

        // Instructions
        Label instructLabel = new Label(I18n.getInstance().get("humanbody.controls"));
        instructLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 11px;");

        // Load STL Model section
        Label loadTitle = new Label(I18n.getInstance().get("humanbody.loadmodel"));
        loadTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button loadStlBtn = new Button(I18n.getInstance().get("humanbody.loadstl"));
        loadStlBtn.setMaxWidth(Double.MAX_VALUE);
        loadStlBtn.setStyle("-fx-background-color: #4a9eff; -fx-text-fill: white;");
        loadStlBtn.setOnAction(e -> loadStlModel());

        controls.getChildren().addAll(
                title, new Separator(),
                skeletonCheck, muscleCheck, organCheck,
                nervousCheck, circulatoryCheck, skinCheck,
                new Separator(),
                presetsLabel, skeletonOnlyBtn, muscularBtn, fullBodyBtn,
                new Separator(),
                loadTitle, loadStlBtn,
                new Separator(),
                infoTitle, infoLabel,
                new Separator(),
                instructLabel);

        return controls;
    }

    private CheckBox createLayerCheckbox(String name, Group layer, Color indicatorColor, boolean selected) {
        CheckBox cb = new CheckBox(name);
        cb.setSelected(selected);
        cb.setStyle("-fx-text-fill: white;");
        layer.setVisible(selected);

        cb.setOnAction(e -> layer.setVisible(cb.isSelected()));

        return cb;
    }

    private void updateLayerVisibility() {
        // Called after preset buttons
    }

    /**
     * Opens a file chooser to load an external STL anatomical model.
     */
    private void loadStlModel() {
        FileChooser fc = new FileChooser();
        fc.setTitle(I18n.getInstance().get("humanbody.loadstl.title"));
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("STL Files", "*.stl"));

        File file = fc.showOpenDialog(null);
        if (file != null) {
            try {
                MeshView mesh = StlMeshLoader.load(file);
                PhongMaterial material = new PhongMaterial(Color.BEIGE);
                material.setSpecularColor(Color.WHITE);
                mesh.setMaterial(material);
                mesh.setDrawMode(DrawMode.FILL);
                mesh.setId(file.getName());

                // Add to skeleton layer for visibility control
                skeletonLayer.getChildren().add(mesh);

                infoLabel.setText(I18n.getInstance().get("humanbody.loaded") + ": " + file.getName());
            } catch (Exception e) {
                infoLabel.setText(I18n.getInstance().get("humanbody.loaderror") + ": " + e.getMessage());
            }
        }
    }

    private void build3DBody() {
        // Try loading Z-Anatomy OBJ models first, fallback to procedural geometry
        if (!tryLoadObjModel("/org/jscience/medicine/anatomy/models/SkeletalSystem100.obj", skeletonLayer,
                Color.IVORY)) {
            buildSkeleton();
        }

        if (!tryLoadObjModel("/org/jscience/medicine/anatomy/models/MuscularSystem100.obj", muscleLayer,
                Color.INDIANRED)) {
            buildMuscles();
        }

        if (!tryLoadObjModel("/org/jscience/medicine/anatomy/models/VisceralSystem100.obj", organLayer, Color.CORAL)) {
            buildOrgans();
        }

        if (!tryLoadObjModel("/org/jscience/medicine/anatomy/models/NervousSystem100.obj", nervousLayer, Color.GOLD)) {
            buildNervousSystem();
        }

        if (!tryLoadObjModel("/org/jscience/medicine/anatomy/models/CardioVascular41.obj", circulatoryLayer,
                Color.DARKRED)) {
            buildCirculatorySystem();
        }

        // Skin is always procedural (semi-transparent)
        buildSkin();
    }

    /**
     * Attempts to load an OBJ model from resources into the specified layer.
     * 
     * @return true if successful, false if model not found
     */
    private boolean tryLoadObjModel(String resourcePath, Group layer, Color color) {
        try {
            java.net.URL url = getClass().getResource(resourcePath);
            if (url == null) {
                return false;
            }
            Group model = org.jscience.biology.loaders.ObjMeshLoader.load(url);
            if (model.getChildren().isEmpty()) {
                return false;
            }
            // Apply material color
            PhongMaterial mat = new PhongMaterial(color);
            mat.setSpecularColor(Color.WHITE);
            for (javafx.scene.Node node : model.getChildren()) {
                if (node instanceof MeshView) {
                    ((MeshView) node).setMaterial(mat);
                }
            }
            // Scale and center the model
            model.setScaleX(0.5);
            model.setScaleY(0.5);
            model.setScaleZ(0.5);
            layer.getChildren().add(model);
            System.out.println("Loaded Z-Anatomy model: " + resourcePath);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to load OBJ model " + resourcePath + ": " + e.getMessage());
            return false;
        }
    }

    private void buildSkeleton() {
        PhongMaterial boneMat = new PhongMaterial(Color.IVORY);
        boneMat.setSpecularColor(Color.WHITE);

        PhongMaterial boneDark = new PhongMaterial(Color.ANTIQUEWHITE);
        boneDark.setSpecularColor(Color.LIGHTGRAY);

        // ===== SKULL =====
        Sphere skull = new Sphere(25);
        skull.setMaterial(boneMat);
        skull.setTranslateY(-150);
        skull.setOnMouseClicked(e -> showInfo("Skull (Cranium)",
                "Protects the brain. Composed of 22 bones:\n• 8 cranial bones\n• 14 facial bones\nWeight: ~1 kg"));

        // Mandible (jaw)
        Box mandible = new Box(30, 8, 15);
        mandible.setMaterial(boneDark);
        mandible.setTranslateY(-130);
        mandible.setTranslateZ(10);
        mandible.setOnMouseClicked(e -> showInfo("Mandible (Jaw)",
                "Strongest facial bone. Only movable skull bone.\nContains 16 teeth (lower)."));

        // ===== SPINE =====
        Group spine = new Group();
        // Cervical (7), Thoracic (12), Lumbar (5), Sacral (5 fused)
        String[] regions = { "Cervical", "Cervical", "Cervical", "Cervical", "Cervical", "Cervical", "Cervical",
                "Thoracic", "Thoracic", "Thoracic", "Thoracic", "Thoracic", "Thoracic",
                "Thoracic", "Thoracic", "Thoracic", "Thoracic", "Thoracic", "Thoracic",
                "Lumbar", "Lumbar", "Lumbar", "Lumbar", "Lumbar" };
        for (int i = 0; i < 24; i++) {
            double radius = i < 7 ? 6 : (i < 19 ? 8 : 10);
            Cylinder vertebra = new Cylinder(radius, 5);
            vertebra.setMaterial((i % 2 == 0) ? boneMat : boneDark);
            vertebra.setTranslateY(-120 + i * 7);
            int idx = i;
            vertebra.setOnMouseClicked(e -> showInfo(regions[idx] + " Vertebra",
                    "Part of " + regions[idx].toLowerCase() + " spine region.\n" +
                            (idx < 7 ? "C" + (idx + 1) : (idx < 19 ? "T" + (idx - 6) : "L" + (idx - 18)))));
            spine.getChildren().add(vertebra);
        }

        // Sacrum
        Box sacrum = new Box(35, 25, 15);
        sacrum.setMaterial(boneDark);
        sacrum.setTranslateY(55);
        sacrum.setOnMouseClicked(e -> showInfo("Sacrum", "5 fused vertebrae. Connects spine to pelvis."));

        // Coccyx (tailbone)
        Sphere coccyx = new Sphere(6);
        coccyx.setMaterial(boneDark);
        coccyx.setTranslateY(72);
        coccyx.setOnMouseClicked(e -> showInfo("Coccyx (Tailbone)", "3-5 fused vertebrae. Vestigial tail."));

        // ===== SHOULDER GIRDLE =====
        // Clavicles
        Cylinder clavicleL = new Cylinder(3, 55);
        clavicleL.setMaterial(boneMat);
        clavicleL.setTranslateX(-30);
        clavicleL.setTranslateY(-115);
        clavicleL.setRotationAxis(Rotate.Z_AXIS);
        clavicleL.setRotate(85);
        clavicleL.setOnMouseClicked(e -> showInfo("Clavicle (Collarbone)",
                "Connects arm to body. Most commonly fractured bone."));

        Cylinder clavicleR = new Cylinder(3, 55);
        clavicleR.setMaterial(boneMat);
        clavicleR.setTranslateX(30);
        clavicleR.setTranslateY(-115);
        clavicleR.setRotationAxis(Rotate.Z_AXIS);
        clavicleR.setRotate(-85);

        // Scapulae (shoulder blades)
        Box scapulaL = new Box(25, 35, 5);
        scapulaL.setMaterial(boneDark);
        scapulaL.setTranslateX(-45);
        scapulaL.setTranslateY(-95);
        scapulaL.setTranslateZ(-15);
        scapulaL.setOnMouseClicked(e -> showInfo("Scapula (Shoulder Blade)",
                "Triangular bone connecting humerus to clavicle.\n17 muscles attach to it."));

        Box scapulaR = new Box(25, 35, 5);
        scapulaR.setMaterial(boneDark);
        scapulaR.setTranslateX(45);
        scapulaR.setTranslateY(-95);
        scapulaR.setTranslateZ(-15);

        // ===== RIBCAGE =====
        Group ribcage = new Group();
        // True ribs (1-7), False ribs (8-10), Floating ribs (11-12)
        for (int i = 0; i < 12; i++) {
            double length = 55 - i * 2;
            double angle = 50 + i * 3;

            Cylinder ribL = new Cylinder(2, length);
            ribL.setMaterial((i < 7) ? boneMat : boneDark);
            ribL.setTranslateX(-35);
            ribL.setTranslateY(-100 + i * 7);
            ribL.setRotationAxis(Rotate.Z_AXIS);
            ribL.setRotate(angle);
            int ribNum = i + 1;
            ribL.setOnMouseClicked(e -> showInfo(
                    "Rib #" + ribNum + (ribNum <= 7 ? " (True)" : ribNum <= 10 ? " (False)" : " (Floating)"),
                    ribNum <= 7 ? "Directly attached to sternum."
                            : ribNum <= 10 ? "Attached to sternum via cartilage."
                                    : "Floating rib, not attached to sternum."));
            ribcage.getChildren().add(ribL);

            Cylinder ribR = new Cylinder(2, length);
            ribR.setMaterial((i < 7) ? boneMat : boneDark);
            ribR.setTranslateX(35);
            ribR.setTranslateY(-100 + i * 7);
            ribR.setRotationAxis(Rotate.Z_AXIS);
            ribR.setRotate(-angle);
            ribcage.getChildren().add(ribR);
        }

        // Sternum (breastbone)
        Cylinder sternum = new Cylinder(8, 60);
        sternum.setMaterial(boneMat);
        sternum.setTranslateY(-75);
        sternum.setTranslateZ(25);
        sternum.setOnMouseClicked(e -> showInfo("Sternum (Breastbone)",
                "3 parts: Manubrium, Body, Xiphoid process.\nProtects heart."));
        ribcage.getChildren().add(sternum);

        // ===== PELVIS =====
        // Hip bones (Ilium, Ischium, Pubis)
        Box hipL = new Box(35, 40, 20);
        hipL.setMaterial(boneMat);
        hipL.setTranslateX(-25);
        hipL.setTranslateY(75);
        hipL.setOnMouseClicked(e -> showInfo("Hip Bone (Os Coxae)",
                "3 fused bones: Ilium, Ischium, Pubis.\nForms hip socket (acetabulum)."));

        Box hipR = new Box(35, 40, 20);
        hipR.setMaterial(boneMat);
        hipR.setTranslateX(25);
        hipR.setTranslateY(75);

        // ===== ARMS =====
        // Humerus (upper arm)
        Cylinder humerusL = new Cylinder(6, 80);
        humerusL.setMaterial(boneMat);
        humerusL.setTranslateX(-60);
        humerusL.setTranslateY(-55);
        humerusL.setOnMouseClicked(e -> showInfo("Humerus",
                "Upper arm bone. Articulates with scapula (shoulder) and radius/ulna (elbow)."));

        Cylinder humerusR = new Cylinder(6, 80);
        humerusR.setMaterial(boneMat);
        humerusR.setTranslateX(60);
        humerusR.setTranslateY(-55);

        // Radius and Ulna (forearm)
        Cylinder radiusL = new Cylinder(4, 70);
        radiusL.setMaterial(boneDark);
        radiusL.setTranslateX(-58);
        radiusL.setTranslateY(15);
        Cylinder ulnaL = new Cylinder(4, 75);
        ulnaL.setMaterial(boneMat);
        ulnaL.setTranslateX(-62);
        ulnaL.setTranslateY(15);

        Cylinder radiusR = new Cylinder(4, 70);
        radiusR.setMaterial(boneDark);
        radiusR.setTranslateX(58);
        radiusR.setTranslateY(15);
        Cylinder ulnaR = new Cylinder(4, 75);
        ulnaR.setMaterial(boneMat);
        ulnaR.setTranslateX(62);
        ulnaR.setTranslateY(15);

        // ===== HANDS =====
        Group handL = buildHand(boneMat, boneDark, -60, 60);
        Group handR = buildHand(boneMat, boneDark, 60, 60);

        // ===== LEGS =====
        // Femur (thigh)
        Cylinder femurL = new Cylinder(9, 120);
        femurL.setMaterial(boneMat);
        femurL.setTranslateX(-25);
        femurL.setTranslateY(150);
        femurL.setOnMouseClicked(e -> showInfo("Femur (Thigh Bone)",
                "Longest, strongest bone in body.\nLength: ~48 cm. Supports body weight."));

        Cylinder femurR = new Cylinder(9, 120);
        femurR.setMaterial(boneMat);
        femurR.setTranslateX(25);
        femurR.setTranslateY(150);

        // Patella (kneecap)
        Sphere patellaL = new Sphere(8);
        patellaL.setMaterial(boneDark);
        patellaL.setTranslateX(-25);
        patellaL.setTranslateY(215);
        patellaL.setTranslateZ(10);
        patellaL.setOnMouseClicked(e -> showInfo("Patella (Kneecap)",
                "Largest sesamoid bone. Protects knee joint."));

        Sphere patellaR = new Sphere(8);
        patellaR.setMaterial(boneDark);
        patellaR.setTranslateX(25);
        patellaR.setTranslateY(215);
        patellaR.setTranslateZ(10);

        // Tibia and Fibula (lower leg)
        Cylinder tibiaL = new Cylinder(7, 100);
        tibiaL.setMaterial(boneMat);
        tibiaL.setTranslateX(-22);
        tibiaL.setTranslateY(275);
        tibiaL.setOnMouseClicked(e -> showInfo("Tibia (Shinbone)",
                "Second largest bone. Bears body weight."));

        Cylinder fibulaL = new Cylinder(4, 95);
        fibulaL.setMaterial(boneDark);
        fibulaL.setTranslateX(-30);
        fibulaL.setTranslateY(275);

        Cylinder tibiaR = new Cylinder(7, 100);
        tibiaR.setMaterial(boneMat);
        tibiaR.setTranslateX(22);
        tibiaR.setTranslateY(275);

        Cylinder fibulaR = new Cylinder(4, 95);
        fibulaR.setMaterial(boneDark);
        fibulaR.setTranslateX(30);
        fibulaR.setTranslateY(275);

        // ===== FEET =====
        Group footL = buildFoot(boneMat, boneDark, -25, 330);
        Group footR = buildFoot(boneMat, boneDark, 25, 330);

        skeletonLayer.getChildren().addAll(
                skull, mandible,
                spine, sacrum, coccyx,
                clavicleL, clavicleR, scapulaL, scapulaR,
                ribcage,
                hipL, hipR,
                humerusL, humerusR, radiusL, ulnaL, radiusR, ulnaR,
                handL, handR,
                femurL, femurR, patellaL, patellaR,
                tibiaL, fibulaL, tibiaR, fibulaR,
                footL, footR);
    }

    private Group buildHand(PhongMaterial boneMat, PhongMaterial boneDark, double x, double y) {
        Group hand = new Group();
        // Carpals (wrist - 8 bones)
        Box carpals = new Box(18, 10, 8);
        carpals.setMaterial(boneDark);
        carpals.setTranslateX(x);
        carpals.setTranslateY(y);
        carpals.setOnMouseClicked(e -> showInfo("Carpals (Wrist)", "8 bones in 2 rows. Allows wrist flexibility."));

        // Metacarpals and Phalanges
        for (int f = 0; f < 5; f++) {
            double fingerX = x + (f - 2) * 4;
            double length = f == 0 ? 12 : 20; // thumb shorter

            Cylinder metacarpal = new Cylinder(2, length);
            metacarpal.setMaterial(boneMat);
            metacarpal.setTranslateX(fingerX);
            metacarpal.setTranslateY(y + 12);
            hand.getChildren().add(metacarpal);

            Cylinder phalanx = new Cylinder(1.5, length * 0.8);
            phalanx.setMaterial(boneDark);
            phalanx.setTranslateX(fingerX);
            phalanx.setTranslateY(y + 25);
            hand.getChildren().add(phalanx);
        }
        hand.getChildren().add(carpals);
        return hand;
    }

    private Group buildFoot(PhongMaterial boneMat, PhongMaterial boneDark, double x, double y) {
        Group foot = new Group();
        // Tarsals (ankle - 7 bones)
        Box tarsals = new Box(25, 15, 12);
        tarsals.setMaterial(boneDark);
        tarsals.setTranslateX(x);
        tarsals.setTranslateY(y);
        tarsals.setTranslateZ(8);
        tarsals.setOnMouseClicked(e -> showInfo("Tarsals (Ankle)", "7 bones including calcaneus (heel)."));

        // Metatarsals
        for (int t = 0; t < 5; t++) {
            double toeX = x + (t - 2) * 5;
            Cylinder metatarsal = new Cylinder(2, 18);
            metatarsal.setMaterial(boneMat);
            metatarsal.setTranslateX(toeX);
            metatarsal.setTranslateY(y + 15);
            metatarsal.setTranslateZ(15);
            metatarsal.setRotationAxis(Rotate.X_AXIS);
            metatarsal.setRotate(70);
            foot.getChildren().add(metatarsal);
        }
        foot.getChildren().add(tarsals);
        return foot;
    }

    private void buildMuscles() {
        PhongMaterial muscleMat = new PhongMaterial(Color.INDIANRED);
        muscleMat.setSpecularColor(Color.PINK);

        // Pectorals
        Box pectoralL = new Box(30, 40, 15);
        pectoralL.setMaterial(muscleMat);
        pectoralL.setTranslateX(-25);
        pectoralL.setTranslateY(-80);
        pectoralL.setTranslateZ(15);
        pectoralL.setOnMouseClicked(e -> showInfo("Pectoralis Major", "Chest muscle for arm movement."));

        Box pectoralR = new Box(30, 40, 15);
        pectoralR.setMaterial(muscleMat);
        pectoralR.setTranslateX(25);
        pectoralR.setTranslateY(-80);
        pectoralR.setTranslateZ(15);

        // Biceps
        Cylinder bicepL = new Cylinder(8, 50);
        bicepL.setMaterial(muscleMat);
        bicepL.setTranslateX(-60);
        bicepL.setTranslateY(-50);
        bicepL.setTranslateZ(8);
        bicepL.setOnMouseClicked(e -> showInfo("Biceps Brachii", "Upper arm muscle for flexion."));

        Cylinder bicepR = new Cylinder(8, 50);
        bicepR.setMaterial(muscleMat);
        bicepR.setTranslateX(60);
        bicepR.setTranslateY(-50);
        bicepR.setTranslateZ(8);

        // Quadriceps
        Cylinder quadL = new Cylinder(15, 100);
        quadL.setMaterial(muscleMat);
        quadL.setTranslateX(-25);
        quadL.setTranslateY(130);
        quadL.setTranslateZ(10);
        quadL.setOnMouseClicked(e -> showInfo("Quadriceps", "Thigh muscles for leg extension."));

        Cylinder quadR = new Cylinder(15, 100);
        quadR.setMaterial(muscleMat);
        quadR.setTranslateX(25);
        quadR.setTranslateY(130);
        quadR.setTranslateZ(10);

        // Abs
        for (int i = 0; i < 4; i++) {
            Box absL = new Box(15, 12, 8);
            absL.setMaterial(muscleMat);
            absL.setTranslateX(-10);
            absL.setTranslateY(-20 + i * 18);
            absL.setTranslateZ(20);

            Box absR = new Box(15, 12, 8);
            absR.setMaterial(muscleMat);
            absR.setTranslateX(10);
            absR.setTranslateY(-20 + i * 18);
            absR.setTranslateZ(20);

            muscleLayer.getChildren().addAll(absL, absR);
        }

        muscleLayer.getChildren().addAll(pectoralL, pectoralR, bicepL, bicepR, quadL, quadR);
    }

    private void buildOrgans() {
        // Heart
        PhongMaterial heartMat = new PhongMaterial(Color.DARKRED);
        Sphere heart = new Sphere(15);
        heart.setMaterial(heartMat);
        heart.setTranslateX(-10);
        heart.setTranslateY(-75);
        heart.setTranslateZ(5);
        heart.setOnMouseClicked(
                e -> showInfo("Heart", "Pumps blood through the circulatory system.\n~100,000 beats per day."));

        // Lungs
        PhongMaterial lungMat = new PhongMaterial(Color.LIGHTPINK);
        Sphere lungL = new Sphere(25);
        lungL.setMaterial(lungMat);
        lungL.setTranslateX(-35);
        lungL.setTranslateY(-75);
        lungL.setScaleY(1.3);
        lungL.setOnMouseClicked(
                e -> showInfo("Lungs", "Exchange oxygen and carbon dioxide.\n~15,000 liters of air per day."));

        Sphere lungR = new Sphere(30);
        lungR.setMaterial(lungMat);
        lungR.setTranslateX(30);
        lungR.setTranslateY(-75);
        lungR.setScaleY(1.3);

        // Liver
        PhongMaterial liverMat = new PhongMaterial(Color.BROWN);
        Box liver = new Box(50, 25, 20);
        liver.setMaterial(liverMat);
        liver.setTranslateX(15);
        liver.setTranslateY(-30);
        liver.setOnMouseClicked(
                e -> showInfo("Liver", "Processes nutrients and detoxifies blood.\nLargest internal organ."));

        // Stomach
        PhongMaterial stomachMat = new PhongMaterial(Color.SANDYBROWN);
        Sphere stomach = new Sphere(20);
        stomach.setMaterial(stomachMat);
        stomach.setTranslateX(-20);
        stomach.setTranslateY(-20);
        stomach.setScaleX(0.8);
        stomach.setOnMouseClicked(e -> showInfo("Stomach", "Digests food with acids and enzymes."));

        // Kidneys
        PhongMaterial kidneyMat = new PhongMaterial(Color.INDIANRED);
        Sphere kidneyL = new Sphere(10);
        kidneyL.setMaterial(kidneyMat);
        kidneyL.setTranslateX(-35);
        kidneyL.setTranslateY(10);
        kidneyL.setScaleY(1.5);

        Sphere kidneyR = new Sphere(10);
        kidneyR.setMaterial(kidneyMat);
        kidneyR.setTranslateX(35);
        kidneyR.setTranslateY(10);
        kidneyR.setScaleY(1.5);
        kidneyL.setOnMouseClicked(
                e -> showInfo("Kidneys", "Filter blood and produce urine.\n~180 liters filtered per day."));

        organLayer.getChildren().addAll(heart, lungL, lungR, liver, stomach, kidneyL, kidneyR);
    }

    private void buildNervousSystem() {
        PhongMaterial nerveMat = new PhongMaterial(Color.YELLOW);

        // Brain
        Sphere brain = new Sphere(22);
        brain.setMaterial(nerveMat);
        brain.setTranslateY(-150);
        brain.setOnMouseClicked(e -> showInfo("Brain", "Control center of the nervous system.\n~86 billion neurons."));

        // Spinal cord
        Cylinder spinalCord = new Cylinder(3, 180);
        spinalCord.setMaterial(nerveMat);
        spinalCord.setTranslateY(-30);
        spinalCord.setOnMouseClicked(e -> showInfo("Spinal Cord", "Transmits signals between brain and body."));

        // Main nerve branches
        for (int i = 0; i < 8; i++) {
            Cylinder nerveL = new Cylinder(1, 40);
            nerveL.setMaterial(nerveMat);
            nerveL.setTranslateX(-25);
            nerveL.setTranslateY(-100 + i * 25);
            nerveL.setRotationAxis(Rotate.Z_AXIS);
            nerveL.setRotate(90);

            Cylinder nerveR = new Cylinder(1, 40);
            nerveR.setMaterial(nerveMat);
            nerveR.setTranslateX(25);
            nerveR.setTranslateY(-100 + i * 25);
            nerveR.setRotationAxis(Rotate.Z_AXIS);
            nerveR.setRotate(90);

            nervousLayer.getChildren().addAll(nerveL, nerveR);
        }

        nervousLayer.getChildren().addAll(brain, spinalCord);
    }

    private void buildCirculatorySystem() {
        PhongMaterial arteryMat = new PhongMaterial(Color.DARKRED);
        PhongMaterial veinMat = new PhongMaterial(Color.DARKBLUE);

        // Aorta
        Cylinder aorta = new Cylinder(5, 100);
        aorta.setMaterial(arteryMat);
        aorta.setTranslateX(5);
        aorta.setTranslateY(-20);
        aorta.setOnMouseClicked(e -> showInfo("Aorta", "Largest artery. Carries oxygenated blood from heart."));

        // Vena cava
        Cylinder venaCava = new Cylinder(5, 100);
        venaCava.setMaterial(veinMat);
        venaCava.setTranslateX(-5);
        venaCava.setTranslateY(-20);
        venaCava.setOnMouseClicked(e -> showInfo("Vena Cava", "Largest vein. Returns deoxygenated blood to heart."));

        // Leg vessels
        Cylinder femArtL = new Cylinder(3, 140);
        femArtL.setMaterial(arteryMat);
        femArtL.setTranslateX(-20);
        femArtL.setTranslateY(150);

        Cylinder femArtR = new Cylinder(3, 140);
        femArtR.setMaterial(arteryMat);
        femArtR.setTranslateX(20);
        femArtR.setTranslateY(150);

        circulatoryLayer.getChildren().addAll(aorta, venaCava, femArtL, femArtR);
    }

    private void buildSkin() {
        PhongMaterial skinMat = new PhongMaterial(Color.PEACHPUFF);
        skinMat.setSpecularColor(Color.WHITE);

        // Simple cylindrical body shell with transparency
        Cylinder torso = new Cylinder(55, 180);
        torso.setMaterial(skinMat);
        torso.setTranslateY(-20);
        torso.setOpacity(0.3);

        // Head
        Sphere head = new Sphere(30);
        head.setMaterial(skinMat);
        head.setTranslateY(-150);
        head.setOpacity(0.3);

        skinLayer.getChildren().addAll(torso, head);
    }

    private void showInfo(String title, String description) {
        if (infoLabel != null) {
            infoLabel.setText(title + "\n\n" + description);
        }
    }

    public static void show(Stage stage) {
        new HumanBodyViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
