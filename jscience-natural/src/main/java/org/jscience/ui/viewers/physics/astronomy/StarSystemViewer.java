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

package org.jscience.ui.viewers.physics.astronomy;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.jscience.measure.Units;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.astronomy.*;
import org.jscience.physics.astronomy.time.JulianDate;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.BooleanParameter;
import org.jscience.ui.Parameter;
import org.jscience.ui.i18n.I18n;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 3D Star System Viewer.
 * Features: Solar System, Black Hole etc.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class StarSystemViewer extends AbstractViewer implements Simulatable {
    
    private final Group root3D = new Group();
    private final Group world = new Group();
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Rotate cameraX = new Rotate(-20, Rotate.X_AXIS);
    private final Rotate cameraY = new Rotate(0, Rotate.Y_AXIS);
    private final Translate cameraZ = new Translate(0, 0, -500);

    private StarSystem currentSystem;
    private JulianDate currentDate = new JulianDate(JulianDate.J2000);
    private double timeScale = 1.0;
    private boolean paused = true;

    private Map<CelestialBody, Node> bodyNodes = new HashMap<>();
    private double scaleFactor = 1e-9;
    private double planetScale = 1000.0;
    private double mouseOldX, mouseOldY;

    private final Group trailGroup = new Group();
    private int updateCounter = 0;
    private Label dateLabel;
    private SubScene subScene;

    private enum Preset { SOLAR_SYSTEM, BLACK_HOLE, NEUTRON_STAR, SUPERGIANT }

    public StarSystemViewer() {
        // Camera
        Group camGroup = new Group(camera);
        camGroup.getTransforms().addAll(cameraY, cameraX, cameraZ);
        root3D.getChildren().addAll(trailGroup, world, camGroup);
        
        subScene = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);
        subScene.setCamera(camera);
        setupInput(subScene);
        
        setCenter(subScene);
        
        VBox overlay = createOverlay();
        setBottom(overlay);
        
        // Resize
        widthProperty().addListener(o -> { if(getWidth()>0) subScene.setWidth(getWidth() - 220); });
        heightProperty().addListener(o -> { if(getHeight()>0) subScene.setHeight(getHeight()); });

        loadSystem(Preset.SOLAR_SYSTEM);
        
        new AnimationTimer() {
            @Override public void handle(long now) {
                if (!paused) {
                    currentDate = new JulianDate(currentDate.getValue() + timeScale);
                    updatePositions();
                    updateLabels();
                }
                updateVisuals();
            }
        }.start();
    }

    private void setupInput(SubScene info) {
        info.setOnMousePressed(e -> { mouseOldX = e.getSceneX(); mouseOldY = e.getSceneY(); });
        info.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - mouseOldX; double dy = e.getSceneY() - mouseOldY;
            if (e.getButton() == MouseButton.PRIMARY) {
                cameraX.setAngle(cameraX.getAngle() - dy * 0.2);
                cameraY.setAngle(cameraY.getAngle() + dx * 0.2);
            } else {
                cameraZ.setZ(cameraZ.getZ() + dy);
            }
            mouseOldX = e.getSceneX(); mouseOldY = e.getSceneY();
        });
    }
    
    private VBox createOverlay() {
        VBox box = new VBox(5);
        box.setPadding(new Insets(5));
        box.getStyleClass().add("viewer-sidebar");
        
        dateLabel = new Label();
        dateLabel.getStyleClass().add("description-label");
        
        box.getChildren().add(dateLabel);
        return box;
    }

    private void loadSystem(Preset p) {
        if (p == Preset.SOLAR_SYSTEM) {
            currentSystem = createDefaultSolarSystem();
            planetScale = 1000.0; scaleFactor = 1e-9; cameraZ.setZ(-500);
        } else if (p == Preset.BLACK_HOLE) {
            currentSystem = createBlackHoleSystem();
            planetScale = 50.0; scaleFactor = 1e-8; cameraZ.setZ(-100);
        } else {
           currentSystem = createDefaultSolarSystem(); // Stubs
        }
        build3DWorld();
    }
    
    private void build3DWorld() {
        world.getChildren().clear(); bodyNodes.clear(); trailGroup.getChildren().clear();
        for (CelestialBody body : currentSystem.getBodies()) {
            Node node = createBodyNode(body);
            if (body.getName().contains("Black Hole")) {
                Cylinder disk = new Cylinder(70, 0.5); disk.setMaterial(new PhongMaterial(Color.ORANGE));
                PointLight glow = new PointLight(Color.ORANGERED); glow.setMaxRange(200); world.getChildren().add(glow);
                Cylinder jet = new Cylinder(1, 100); jet.setMaterial(new PhongMaterial(Color.CYAN));
                jet.setRotationAxis(Rotate.X_AXIS); jet.setRotate(90);
                Group bhGroup = new Group(node, disk, jet);
                world.getChildren().add(bhGroup); bodyNodes.put(body, bhGroup);
            } else {
                world.getChildren().add(node); bodyNodes.put(body, node);
            }
        }
        updatePositions();
    }
    
    private Node createBodyNode(CelestialBody body) {
        double r = body.getRadius().to(Units.METER).getValue().doubleValue() * scaleFactor * planetScale;
        if (body instanceof Star) r *= 0.1;
        if (body.getName().contains("Black Hole")) r = 5.0;
        r = Math.max(0.2, r);
        Sphere sphere = new Sphere(r);
        PhongMaterial mat = new PhongMaterial();
        if (body.getName().contains("Black Hole")) { mat.setDiffuseColor(Color.BLACK); mat.setSpecularColor(Color.WHITE); }
        else {
            mat.setDiffuseColor(getColorForBody(body));
            if (body instanceof Star) { mat.setSelfIlluminationMap(mat.getDiffuseMap()); world.getChildren().add(new PointLight(Color.WHITE)); }
        }
        sphere.setMaterial(mat); return sphere;
    }

    private Color getColorForBody(CelestialBody b) {
        String n = b.getName().toLowerCase();
        if (n.contains("sun")) return Color.YELLOW;
        if (n.contains("earth")) return Color.BLUE;
        if (n.contains("mars")) return Color.RED;
        if (n.contains("supergiant")) return Color.ALICEBLUE;
        return Color.GRAY;
    }

    private void updatePositions() {
        updateCounter++;
        for (Map.Entry<CelestialBody, Node> entry : bodyNodes.entrySet()) {
            CelestialBody body = entry.getKey(); Node node = entry.getValue();
            double x=0, y=0, z=0;
            // Simplified orbit logic for demo visual
            if (body.getName().contains("Black Hole")) { x=0; }
            else if (body.getName().contains("Supergiant") || body.getName().contains("Companion")) {
                double t = updateCounter * 0.02; x = Math.cos(t)*80; z = Math.sin(t)*80;
            } else if (body.getName().equalsIgnoreCase("Sun")) { x=0; }
            else if (body.getName().equalsIgnoreCase("Earth")) {
                double t = updateCounter * 0.01; x = Math.cos(t)*150; z = Math.sin(t)*150;
            } else if (body.getName().equalsIgnoreCase("Mars")) {
                double t = updateCounter * 0.006; x = Math.cos(t)*230; z = Math.sin(t)*230;
            }
            node.setTranslateX(x); node.setTranslateZ(z); node.setTranslateY(-y);
            
            if (updateCounter % 10 == 0 && !body.getName().contains("Black Hole") && !body.getName().contains("Sun")) {
                Sphere marker = new Sphere(0.3);
                marker.setTranslateX(x); marker.setTranslateY(-y); marker.setTranslateZ(z);
                marker.setMaterial(new PhongMaterial(Color.gray(0.5, 0.3)));
                trailGroup.getChildren().add(marker);
                if (trailGroup.getChildren().size() > 500) trailGroup.getChildren().remove(0);
            }
        }
    }
    
    private void updateLabels() { if(dateLabel!=null) dateLabel.setText(I18n.getInstance().get("starsystem.date", "Date") + ": " + String.format("%.2f", currentDate.getValue())); }
    private void updateVisuals() { /* star rotation */ }

    // --- Simulatable ---
    @Override public void play() { paused = false; }
    @Override public void pause() { paused = true; }
    @Override public void stop() { paused = true; }
    @Override public void step() { /* Single step logic could go here */ }
    @Override public void setSpeed(double s) { timeScale = s; if(Math.abs(timeScale)<0.1 && timeScale!=0) timeScale=0.1; }
    @Override public boolean isPlaying() { return !paused; }

    
    // --- Star System Creation (Merged) ---
    private StarSystem createDefaultSolarSystem() {
        StarSystem s = new StarSystem("Solar System");
        Vector<Real> o = DenseVector.of(Arrays.asList(Real.ZERO, Real.ZERO, Real.ZERO), org.jscience.mathematics.sets.Reals.getInstance());
        Vector<Real> z = DenseVector.of(Arrays.asList(Real.ZERO, Real.ZERO, Real.ZERO), org.jscience.mathematics.sets.Reals.getInstance());
        Star sun = new Star("Sun", org.jscience.measure.Quantities.create(Real.of(1.989e30), Units.KILOGRAM),
                org.jscience.measure.Quantities.create(Real.of(6.96e8), Units.METER), o, z);
        s.addBody(sun);
        s.addBody(new Planet("Earth", org.jscience.measure.Quantities.create(Real.of(5.9e24), Units.KILOGRAM),
                org.jscience.measure.Quantities.create(Real.of(6.3e6), Units.METER), o, z));
        s.addBody(new Planet("Mars", org.jscience.measure.Quantities.create(Real.of(6.4e23), Units.KILOGRAM),
                org.jscience.measure.Quantities.create(Real.of(3.4e6), Units.METER), o, z));
        return s;
    }
    
    private StarSystem createBlackHoleSystem() {
        StarSystem s = new StarSystem("Cygnus X-1");
        Vector<Real> o = DenseVector.of(Arrays.asList(Real.ZERO, Real.ZERO, Real.ZERO), org.jscience.mathematics.sets.Reals.getInstance());
        Vector<Real> z = DenseVector.of(Arrays.asList(Real.ZERO, Real.ZERO, Real.ZERO), org.jscience.mathematics.sets.Reals.getInstance());
        s.addBody(new Star("Black Hole", org.jscience.measure.Quantities.create(Real.of(2e31), Units.KILOGRAM),
                org.jscience.measure.Quantities.create(Real.of(30000), Units.METER), o, z));
        s.addBody(new Planet("Companion", org.jscience.measure.Quantities.create(Real.of(4e31), Units.KILOGRAM),
                org.jscience.measure.Quantities.create(Real.of(1e10), Units.METER), o, z));
        return s;
    }    

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.physics", "Physics");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("viewer.starsystemviewer.name", "Star System Viewer");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("viewer.starsystemviewer.desc", "3D celestial body and orbital dynamics simulator.");
    }

    @Override
    public String getLongDescription() {
        return I18n.getInstance().get("viewer.starsystemviewer.longdesc", "Explore stellar systems including our Solar System and exotic objects like black holes. features 3D navigation, orbital trails, and time scaling to observe long-term celestial mechanics.");
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> params = new ArrayList<>();
        params.add(new NumericParameter("starsystem.timescale", I18n.getInstance().get("starsystem.timescale", "Time Scale"), 0.0, 100.0, 0.1, timeScale, v -> timeScale = v));
        params.add(new NumericParameter("starsystem.planetscale", I18n.getInstance().get("starsystem.planetscale", "Planet Scale"), 1.0, 10000.0, 10.0, planetScale, v -> {
            planetScale = v;
            build3DWorld();
        }));
        params.add(new BooleanParameter("starsystem.paused", I18n.getInstance().get("starsystem.paused", "Paused"), paused, v -> paused = v));
        return params;
    }
}

