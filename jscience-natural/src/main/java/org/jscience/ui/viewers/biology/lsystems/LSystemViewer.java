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

package org.jscience.ui.viewers.biology.lsystems;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.util.StringConverter;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;

import org.jscience.ui.Parameter;
import org.jscience.ui.i18n.I18n;
import org.jscience.mathematics.linearalgebra.vectors.VectorFactory;
import org.jscience.mathematics.linearalgebra.matrices.MatrixFactory;
import org.jscience.mathematics.linearalgebra.matrices.GenericMatrix;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.util.*;
import java.text.MessageFormat;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;

/**
 * Enhanced L-System Visualizer.
 * Only supports Fractal Plant, Dragon Curve, Sierpinski Triangle, Bush, Algae, Koch Curve, 3D Tree, Hilbert 3D.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LSystemViewer extends AbstractViewer implements Simulatable {

    public static class LSystem {
        String name;
        String axiom;
        Map<Character, String> rules = new HashMap<>();
        double angle;
        boolean is3D;

        public LSystem(String name, String axiom, double angle, boolean is3D) {
            this.name = name; this.axiom = axiom; this.angle = angle; this.is3D = is3D;
        }
        public void addRule(char c, String replacement) { rules.put(c, replacement); }
        public String generate(int iterations) {
            String current = axiom;
            for (int i = 0; i < iterations; i++) {
                StringBuilder next = new StringBuilder();
                for (char c : current.toCharArray()) next.append(rules.getOrDefault(c, String.valueOf(c)));
                current = next.toString();
            }
            return current;
        }
        @Override public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Axiom: ").append(axiom).append("\n");
            sb.append("Angle: ").append(angle).append("\n");
            rules.forEach((k, v) -> sb.append(k).append(" -> ").append(v).append("\n"));
            return sb.toString();
        }
    }

    private LSystem currentSystem;
    private int iterations = 4;
    private boolean is3DView = false;
    private boolean animateGrowth = false;

    private StackPane renderPane;
    private Canvas canvas2D;
    private SubScene subScene3D;
    private Group root3D;
    private TextArea rulesArea;
    private ComboBox<String> presetCombo;
    private Slider iterSlider;
    private CheckBox animCheck;
    private Label statusLabel;
    private AnimationTimer animationTimer;

    private Map<String, LSystem> presets = new LinkedHashMap<>();

    public LSystemViewer() {
        initPresets();
        initUI();
    }
    
    @Override public String getName() { return I18n.getInstance().get("viewer.lsystemviewer.name", "L-System Visualizer"); }
    @Override public String getCategory() { return I18n.getInstance().get("category.biology", "Biology"); }

    private void initPresets() {
        // Use internal IDs for keys
        LSystem plant = new LSystem("preset.fractal_plant", "X", 25, false);
        plant.addRule('X', "F+[[X]-X]-F[-FX]+X"); plant.addRule('F', "FF");
        presets.put(plant.name, plant);
        
        LSystem dragon = new LSystem("preset.dragon_curve", "FX", 90, false);
        dragon.addRule('X', "X+YF+"); dragon.addRule('Y', "-FX-Y");
        presets.put(dragon.name, dragon);
        
        LSystem sierpinski = new LSystem("preset.sierpinski_triangle", "F-G-G", 120, false);
        sierpinski.addRule('F', "F-G+F+G-F"); sierpinski.addRule('G', "GG");
        presets.put(sierpinski.name, sierpinski);
        
        LSystem bush = new LSystem("preset.bush", "X", 25, false);
        bush.addRule('X', "F-[[X]+X]+F[+FX]-X"); bush.addRule('F', "FF");
        presets.put(bush.name, bush);
        
        LSystem algae = new LSystem("preset.algae", "A", 0, false);
        algae.addRule('A', "AB"); algae.addRule('B', "A");
        presets.put(algae.name, algae);
        
        LSystem koch = new LSystem("preset.koch_curve", "F", 90, false);
        koch.addRule('F', "F+F-F-F+F");
        presets.put(koch.name, koch);
        
        LSystem tree3D = new LSystem("preset.tree3d", "F", 25, true);
        tree3D.addRule('F', "F[+F]F[-F]F"); 
        presets.put(tree3D.name, tree3D);
        
        LSystem hilbert3D = new LSystem("preset.hilbert3d", "X", 90, true);
        hilbert3D.addRule('X', "^<XF^<XFX-F^>>XFX&F+>>XFX-F>X->");
        presets.put(hilbert3D.name, hilbert3D);
    }

    private void initUI() {
        this.getStyleClass().add("viewer-root");

        renderPane = new StackPane();
        renderPane.getStyleClass().add("content-dark");

        canvas2D = new Canvas(800, 600);
        
        // Listen to size changes to resize canvas
        renderPane.widthProperty().addListener((o, old, val) -> canvas2D.setWidth(val.doubleValue()));
        renderPane.heightProperty().addListener((o, old, val) -> canvas2D.setHeight(val.doubleValue()));

        root3D = new Group();
        subScene3D = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        PerspectiveCamera cam = new PerspectiveCamera(true);
        cam.setTranslateZ(-600); cam.setTranslateY(-200);
        subScene3D.setCamera(cam);

        init3DNavigation(root3D); 

        renderPane.getChildren().add(canvas2D);
        this.setCenter(renderPane);

        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(280);
        sidebar.getStyleClass().add("viewer-sidebar");

        Label title = new Label(I18n.getInstance().get("viewer.lsystemviewer.settings", "L-System Settings"));
        title.getStyleClass().add("header-label");

        presetCombo = new ComboBox<>();
        presetCombo.getItems().addAll(presets.keySet());
        presetCombo.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return I18n.getInstance().get(object, object);
            }
            @Override
            public String fromString(String string) {
                return string; // Not needed for read-only combo
            }
        });
        presetCombo.setValue("preset.fractal_plant");
        presetCombo.setOnAction(e -> loadPreset(presetCombo.getValue()));

        iterSlider = new Slider(1, 10, 4);
        iterSlider.setShowTickLabels(true); iterSlider.setShowTickMarks(true);
        iterSlider.setMajorTickUnit(1); iterSlider.setSnapToTicks(true);

        Label iterLabel = new Label(MessageFormat.format(I18n.getInstance().get("viewer.lsystemviewer.iterations", "Iterations: {0}"), 4));
        iterSlider.valueProperty().addListener((obs, old, val) -> iterLabel.setText(MessageFormat.format(I18n.getInstance().get("viewer.lsystemviewer.iterations", "Iterations: {0}"), val.intValue())));

        animCheck = new CheckBox(I18n.getInstance().get("viewer.lsystemviewer.animate", "Animate Growth"));
        animCheck.setSelected(true);

        Button renderBtn = new Button(I18n.getInstance().get("viewer.lsystemviewer.generate", "Generate & Render"));
        renderBtn.setMaxWidth(Double.MAX_VALUE);
        renderBtn.setOnAction(e -> {
            iterations = (int) iterSlider.getValue();
            animateGrowth = animCheck.isSelected();
            generateAndRender();
        });

        rulesArea = new TextArea();
        rulesArea.setEditable(false);
        rulesArea.setPrefRowCount(8);
        rulesArea.setWrapText(true);
        rulesArea.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11px;");

        statusLabel = new Label(I18n.getInstance().get("viewer.lsystemviewer.ready", "Ready"));
        statusLabel.getStyleClass().add("description-label");
        statusLabel.setStyle("-fx-font-size: 10px;");

        sidebar.getChildren().addAll(title, new Label(I18n.getInstance().get("viewer.lsystemviewer.preset", "Preset:")), presetCombo, iterLabel, iterSlider, animCheck, new Separator(), new Label(I18n.getInstance().get("viewer.lsystemviewer.rules", "Rules:")), rulesArea, new Separator(), renderBtn, statusLabel);
        this.setRight(sidebar);
        
        loadPreset("preset.fractal_plant");
    }

    private void switchView() {
        renderPane.getChildren().clear();
        if (is3DView) {
            renderPane.getChildren().add(subScene3D);
            subScene3D.widthProperty().bind(renderPane.widthProperty());
            subScene3D.heightProperty().bind(renderPane.heightProperty());
        } else {
            renderPane.getChildren().add(canvas2D);
            canvas2D.widthProperty().bind(renderPane.widthProperty());
            canvas2D.heightProperty().bind(renderPane.heightProperty());
        }
    }

    private void loadPreset(String name) {
        currentSystem = presets.get(name);
        if (currentSystem != null) {
            rulesArea.setText(currentSystem.toString());
            if (currentSystem.is3D && !is3DView) { is3DView = true; switchView(); }
            else if (!currentSystem.is3D && is3DView) { is3DView = false; switchView(); }
            generateAndRender();
        }
    }

    private void generateAndRender() {
        if (currentSystem == null) return;
        long start = System.currentTimeMillis();
        String commands = currentSystem.generate(iterations);
        long dur = System.currentTimeMillis() - start;
        statusLabel.setText(MessageFormat.format(I18n.getInstance().get("viewer.lsystemviewer.generated", "Generated {0} chars in {1}ms"), commands.length(), dur));
        if (is3DView) render3D(commands);
        else render2D(commands);
    }
    
    // AbstractViewer controls map to generation if needed, but Simulatable is better for Animation.
    @Override public void play() { 
        if (!is3DView && animateGrowth) generateAndRender(); // Re-trigger animation
    }
    @Override public void stop() { 
         if (animationTimer != null) animationTimer.stop();
    }
    @Override public void pause() { if (animationTimer != null) animationTimer.stop(); }
    @Override public boolean isPlaying() { return animationTimer != null; /* simplified */ }
    @Override public void step() {}
    @Override public void setSpeed(double s) {}

    private void render2D(String commands) {
        GraphicsContext gc = canvas2D.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas2D.getWidth(), canvas2D.getHeight());
        gc.setStroke(Color.LIME);
        gc.setLineWidth(1.0);

        if (animateGrowth) animate2D(commands);
        else draw2DInstant(commands);
    }

    private void draw2DInstant(String commands) {
        GraphicsContext gc = canvas2D.getGraphicsContext2D();
        double step = Math.max(800.0 / Math.pow(3, iterations), 2.0);
        if (currentSystem.name.contains("dragon")) step = 5; // Updated check for lower case key

        Turtle2D turtle = new Turtle2D(canvas2D.getWidth()/2, canvas2D.getHeight(), -90);
        Stack<Turtle2D> stack = new Stack<>();
        for (char c : commands.toCharArray()) processCommand2D(c, turtle, stack, step, gc);
    }

    private void animate2D(String commands) {
        if (animationTimer != null) animationTimer.stop();
        final double step = Math.max(2.0, 10.0 / iterations);
        final char[] chars = commands.toCharArray();

        animationTimer = new AnimationTimer() {
            int index = 0;
            int batchSize = 10;
            Turtle2D turtle = new Turtle2D(canvas2D.getWidth()/2, canvas2D.getHeight(), -90);
            Stack<Turtle2D> stack = new Stack<>();
            GraphicsContext gc = canvas2D.getGraphicsContext2D();
            @Override public void handle(long now) {
                for (int i = 0; i < batchSize && index < chars.length; i++) {
                    processCommand2D(chars[index++], turtle, stack, step, gc);
                }
                if (index >= chars.length) stop();
            }
        };
        animationTimer.start();
    }

    private void processCommand2D(char c, Turtle2D turtle, Stack<Turtle2D> stack, double step, GraphicsContext gc) {
        switch (c) {
            case 'F': case 'G': case 'X':
                double x2 = turtle.x + Math.cos(Math.toRadians(turtle.angle)) * step;
                double y2 = turtle.y + Math.sin(Math.toRadians(turtle.angle)) * step;
                gc.strokeLine(turtle.x, turtle.y, x2, y2);
                turtle.x = x2; turtle.y = y2; break;
            case '+': turtle.angle += currentSystem.angle; break;
            case '-': turtle.angle -= currentSystem.angle; break;
            case '[': stack.push(new Turtle2D(turtle)); break;
            case ']': if (!stack.isEmpty()) turtle = stack.pop(); break;
        }
    }

    private static class Turtle2D {
        double x, y, angle;
        Turtle2D(double x, double y, double a) { this.x = x; this.y = y; this.angle = a; }
        Turtle2D(Turtle2D o) { this(o.x, o.y, o.angle); }
    }

    private void render3D(String commands) {
        root3D.getChildren().clear();
        root3D.getChildren().add(new AmbientLight(Color.rgb(100, 100, 100)));
        PointLight pl = new PointLight(Color.WHITE);
        pl.setTranslateY(-500);
        root3D.getChildren().add(pl);
        
        double stepLen = 15.0;
        double angleRad = Math.toRadians(currentSystem.angle);
        
        Stack<Turtle3D> stack = new Stack<>();
        Turtle3D turtle = new Turtle3D();
        
        for (char c : commands.toCharArray()) {
            switch (c) {
                case 'F': case 'G':
                    Vector<Real> startPos = turtle.position;
                    // Using Real.of(double)
                    Vector<Real> forward = VectorFactory.of(Real.class, Real.of(0), Real.of(-stepLen), Real.of(0));
                    Vector<Real> move = turtle.orientation.multiply(forward);
                    Vector<Real> endPos = startPos.add(move);
                    
                    drawBranch(startPos, endPos, 2.0);
                    turtle.position = endPos;
                    break;
                case '+': // Rotate around Z
                    turtle.rotate(angleRad, 0, 0, 1);
                    break;
                case '-': // Rotate around Z
                    turtle.rotate(-angleRad, 0, 0, 1);
                    break;
                case '&': // Pitch down (Rotate around X)
                    turtle.rotate(angleRad, 1, 0, 0);
                    break;
                case '^': // Pitch up (Rotate around X)
                    turtle.rotate(-angleRad, 1, 0, 0);
                    break;
                case '\\': // Roll left (Rotate around Y)
                    turtle.rotate(angleRad, 0, 1, 0);
                    break;
                case '/': // Roll right (Rotate around Y)
                    turtle.rotate(-angleRad, 0, 1, 0);
                    break;
                case '[':
                    stack.push(new Turtle3D(turtle));
                    break;
                case ']':
                    if (!stack.isEmpty()) turtle = stack.pop();
                    break;
            }
        }
    }

    private void drawBranch(Vector<Real> start, Vector<Real> end, double radius) {
        double dx = end.get(0).doubleValue() - start.get(0).doubleValue();
        double dy = end.get(1).doubleValue() - start.get(1).doubleValue();
        double dz = end.get(2).doubleValue() - start.get(2).doubleValue();
        double len = Math.sqrt(dx*dx + dy*dy + dz*dz);
        
        Cylinder c = new Cylinder(radius, len);
        c.setMaterial(new PhongMaterial(Color.BROWN));
        
        c.setTranslateX(start.get(0).doubleValue() + dx / 2.0);
        c.setTranslateY(start.get(1).doubleValue() + dy / 2.0);
        c.setTranslateZ(start.get(2).doubleValue() + dz / 2.0);
        
        // Orientation
        Point3D axis = new Point3D(dx, dy, dz).crossProduct(Rotate.Y_AXIS);
        double angle = new Point3D(dx, dy, dz).angle(Rotate.Y_AXIS);
        c.setRotationAxis(axis);
        c.setRotate(-angle);
        
        root3D.getChildren().add(c);
    }

    private static class Turtle3D {
        Vector<Real> position;
        Matrix<Real> orientation;

        Turtle3D() {
            position = VectorFactory.of(Real.class, Real.of(0), Real.of(0), Real.of(0));
            orientation = MatrixFactory.identity(3, Real.ZERO);
        }

        Turtle3D(Turtle3D other) {
            this.position = other.position;
            this.orientation = other.orientation;
        }

        void rotate(double angle, double ax, double ay, double az) {
            double c = Math.cos(angle);
            double s = Math.sin(angle);
            Real[][] rotArr = new Real[3][3];
            // Using Real.ZERO and Real.of()
            for (int i=0; i<3; i++) for (int j=0; j<3; j++) rotArr[i][j] = Real.ZERO;

            if (az == 1) { // Z-axis rotate
                rotArr[0][0] = Real.of(c);  rotArr[0][1] = Real.of(-s); rotArr[0][2] = Real.ZERO;
                rotArr[1][0] = Real.of(s);  rotArr[1][1] = Real.of(c);  rotArr[1][2] = Real.ZERO;
                rotArr[2][0] = Real.ZERO;  rotArr[2][1] = Real.ZERO;  rotArr[2][2] = Real.ONE;
            } else if (ax == 1) { // X-axis rotate
                rotArr[0][0] = Real.ONE;  rotArr[0][1] = Real.ZERO;  rotArr[0][2] = Real.ZERO;
                rotArr[1][0] = Real.ZERO;  rotArr[1][1] = Real.of(c);  rotArr[1][2] = Real.of(-s);
                rotArr[2][0] = Real.ZERO;  rotArr[2][1] = Real.of(s);  rotArr[2][2] = Real.of(c);
            } else if (ay == 1) { // Y-axis rotate
                rotArr[0][0] = Real.of(c);  rotArr[0][1] = Real.ZERO;  rotArr[0][2] = Real.of(s);
                rotArr[1][0] = Real.ZERO;  rotArr[1][1] = Real.ONE;  rotArr[1][2] = Real.ZERO;
                rotArr[2][0] = Real.of(-s); rotArr[2][1] = Real.ZERO;  rotArr[2][2] = Real.of(c);
            }
            orientation = orientation.multiply(GenericMatrix.of(rotArr, Real.ZERO));
        }
    }

    private Rotate rX = new Rotate(0, Rotate.X_AXIS);
    private Rotate rY = new Rotate(0, Rotate.Y_AXIS);
    private double mx, my;
    private void init3DNavigation(Group g) {
        g.getTransforms().addAll(rX, rY);
        subScene3D.setOnMousePressed(e -> { mx = e.getSceneX(); my = e.getSceneY(); });
        subScene3D.setOnMouseDragged(e -> {
            rY.setAngle(rY.getAngle() + (e.getSceneX() - mx) * 0.2);
            rX.setAngle(rX.getAngle() - (e.getSceneY() - my) * 0.2);
            mx = e.getSceneX(); my = e.getSceneY();
        });
    }

    @Override public String getDescription() { return I18n.getInstance().get("viewer.lsystemviewer.desc", "Visualizes Lindenmayer systems (fractal plants)."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.lsystemviewer.longdesc", "Explore the growth of complex structures using recursive L-System rules. Includes presets for fractal plants, dragon curves, and Sierpinski triangles in both 2D and 3D."); }
    @Override public List<Parameter<?>> getViewerParameters() { return new ArrayList<>(); }
}

