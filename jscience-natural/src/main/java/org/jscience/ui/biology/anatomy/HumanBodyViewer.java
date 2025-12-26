/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
import org.jscience.natural.i18n.I18n;

import java.io.File;

/**
 * 3D Human Body Anatomy Viewer.
 * Displays anatomical layers (skeleton, muscles, organs, skin) with visibility
 * controls.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
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
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Build 3D scene
        build3DBody();

        SubScene subScene = create3DSubScene();
        root.setCenter(subScene);

        // Control Panel
        VBox controls = createControlPanel();
        root.setRight(controls);

        Scene scene = new Scene(root, 1200, 800);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(org.jscience.natural.i18n.I18n.getInstance().get("viewer.humanbody"));
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
        controls.setStyle("-fx-background-color: #2a2a4a;");
        controls.setPrefWidth(280);

        // Title
        Label title = new Label(I18n.getInstance().get("humanbody.layers"));
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #8af;");

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
        presetsLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

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
        infoTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        infoLabel = new Label(I18n.getInstance().get("humanbody.clickinfo"));
        infoLabel.setWrapText(true);
        infoLabel.setStyle("-fx-text-fill: #aaa;");

        // Instructions
        Label instructLabel = new Label(I18n.getInstance().get("humanbody.controls"));
        instructLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 11px;");

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
        // Build skeleton
        buildSkeleton();

        // Build muscles
        buildMuscles();

        // Build organs
        buildOrgans();

        // Build nervous system
        buildNervousSystem();

        // Build circulatory system
        buildCirculatorySystem();

        // Build skin
        buildSkin();
    }

    private void buildSkeleton() {
        PhongMaterial boneMat = new PhongMaterial(Color.IVORY);
        boneMat.setSpecularColor(Color.WHITE);

        // Skull
        Sphere skull = new Sphere(25);
        skull.setMaterial(boneMat);
        skull.setTranslateY(-150);
        skull.setOnMouseClicked(e -> showInfo("Skull (Cranium)", "Protects the brain. Composed of 22 bones."));

        // Spine
        Group spine = new Group();
        for (int i = 0; i < 24; i++) {
            Cylinder vertebra = new Cylinder(8, 6);
            vertebra.setMaterial(boneMat);
            vertebra.setTranslateY(-120 + i * 8);
            spine.getChildren().add(vertebra);
        }
        spine.setOnMouseClicked(e -> showInfo("Spine (Vertebral Column)", "33 vertebrae protecting the spinal cord."));

        // Ribcage
        Group ribcage = new Group();
        for (int i = 0; i < 12; i++) {
            // Left rib
            Cylinder ribL = new Cylinder(2, 60);
            ribL.setMaterial(boneMat);
            ribL.setTranslateX(-35);
            ribL.setTranslateY(-100 + i * 7);
            ribL.setRotationAxis(Rotate.Z_AXIS);
            ribL.setRotate(60);
            ribcage.getChildren().add(ribL);

            // Right rib
            Cylinder ribR = new Cylinder(2, 60);
            ribR.setMaterial(boneMat);
            ribR.setTranslateX(35);
            ribR.setTranslateY(-100 + i * 7);
            ribR.setRotationAxis(Rotate.Z_AXIS);
            ribR.setRotate(-60);
            ribcage.getChildren().add(ribR);
        }
        ribcage.setOnMouseClicked(e -> showInfo("Ribcage", "12 pairs of ribs protecting heart and lungs."));

        // Pelvis
        Box pelvis = new Box(80, 30, 30);
        pelvis.setMaterial(boneMat);
        pelvis.setTranslateY(70);
        pelvis.setOnMouseClicked(e -> showInfo("Pelvis", "Supports the spine and protects pelvic organs."));

        // Arms
        Cylinder armL = new Cylinder(5, 100);
        armL.setMaterial(boneMat);
        armL.setTranslateX(-60);
        armL.setTranslateY(-50);

        Cylinder armR = new Cylinder(5, 100);
        armR.setMaterial(boneMat);
        armR.setTranslateX(60);
        armR.setTranslateY(-50);

        // Legs
        Cylinder legL = new Cylinder(7, 150);
        legL.setMaterial(boneMat);
        legL.setTranslateX(-25);
        legL.setTranslateY(160);

        Cylinder legR = new Cylinder(7, 150);
        legR.setMaterial(boneMat);
        legR.setTranslateX(25);
        legR.setTranslateY(160);

        skeletonLayer.getChildren().addAll(skull, spine, ribcage, pelvis, armL, armR, legL, legR);
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
