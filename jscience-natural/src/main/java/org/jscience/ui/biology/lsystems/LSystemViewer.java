package org.jscience.ui.biology.lsystems;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * L-System (Lindenmayer System) Viewer (3D Version).
 * Generates fractal plants and structures in 3D.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class LSystemViewer extends Application {

    private Group world = new Group();
    private int iterations = 4;
    private String currentRule = "Plant";

    // Camera
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Rotate cameraX = new Rotate(-20, Rotate.X_AXIS);
    private final Rotate cameraY = new Rotate(0, Rotate.Y_AXIS);
    private final Translate cameraZ = new Translate(0, 0, -300);

    private static class Rule {
        String axiom;
        Map<Character, String> productions;
        double angle;
        // double widthRatio = 0.7; // Unused

        Rule(String axiom, double angle) {
            this.axiom = axiom;
            this.angle = angle;
            this.productions = new HashMap<>();
        }

        void add(char k, String v) {
            productions.put(k, v);
        }
    }

    private final Map<String, Rule> rules = new HashMap<>();

    public LSystemViewer() {
        // 3D Tree
        Rule plant = new Rule("X", 25);
        plant.add('X', "F+[[X]-X]-F[-FX]+X");
        plant.add('F', "FF");
        rules.put("Plant", plant);

        // 3D Bush (Stochastic rules mocked by deterministic demo for now)
        Rule bush = new Rule("F", 22.5);
        bush.add('F', "FF-[-F+F+F]+[+F-F-F]");
        rules.put("Bush", bush);

        // Dragon Curve (Classic 2D -> 3D plane)
        Rule dragon = new Rule("FX", 90);
        dragon.add('X', "X+YF+");
        dragon.add('Y', "-FX-Y");
        rules.put("Dragon Curve", dragon);
    }

    // Turtle State
    private static class State {
        double x, y, z;
        double[] dir; // Forward vector
        double[] up; // Up vector
        double width;

        State(double x, double y, double z, double[] dir, double[] up, double width) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.dir = dir;
            this.up = up;
            this.width = width;
        }
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        // 3D Scene
        Group root3D = new Group();
        Group camGroup = new Group(camera);
        camGroup.getTransforms().addAll(cameraY, cameraX, cameraZ);
        root3D.getChildren().addAll(world, camGroup, new PointLight(Color.WHITE));

        SubScene subScene = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.LIGHTSKYBLUE);
        subScene.setCamera(camera);

        setupInput(subScene);
        root.setCenter(subScene);

        // UI
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new javafx.geometry.Insets(10));

        ComboBox<String> selector = new ComboBox<>();
        selector.getItems().addAll(rules.keySet());
        selector.setValue("Plant");
        selector.setOnAction(e -> {
            currentRule = selector.getValue();
            build();
        });

        Slider iterSlider = new Slider(1, 6, 4);
        iterSlider.setShowTickLabels(true);
        iterSlider.setShowTickMarks(true);
        iterSlider.valueProperty().addListener((o, old, v) -> {
            iterations = v.intValue();
            build();
        });

        controls.getChildren().addAll(new Label("System:"), selector, new Label("Iterations:"), iterSlider);
        root.setBottom(controls);

        build();

        Scene scene = new Scene(root, 900, 700);
        stage.setTitle("JScience L-System 3D Viewer");
        stage.setScene(scene);
        stage.show();
    }

    private void setupInput(SubScene info) {
        info.setOnMouseDragged(e -> {
            cameraY.setAngle(cameraY.getAngle() + e.getX() * 0.1);
            cameraX.setAngle(cameraX.getAngle() - e.getY() * 0.1);
        });
        info.setOnScroll(e -> {
            cameraZ.setZ(cameraZ.getZ() + e.getDeltaY());
        });
    }

    private void build() {
        world.getChildren().clear();

        Rule r = rules.get(currentRule);
        String s = r.axiom;
        for (int i = 0; i < iterations; i++) {
            StringBuilder sb = new StringBuilder();
            for (char c : s.toCharArray()) {
                sb.append(r.productions.getOrDefault(c, String.valueOf(c)));
            }
            s = sb.toString();
        }

        Stack<State> stack = new Stack<>();
        // Initial State: Position 0,0,0, Up-vector Y (0,1,0), Forward Z (0,0,-1)? Or Y
        // is up.
        // Let's say Y is up. Start at base.
        double[] dir = { 0, -1, 0 }; // Growing UP (negative Y in 3D usually? or positive. JavaFX Y is down. So UP is
                                     // -Y)
        double[] up = { 0, 0, 1 }; // Arbitrary perp

        State current = new State(0, 200, 0, dir, up, 5.0); // Start lower
        double len = 20.0 / iterations * 2;

        PhongMaterial wood = new PhongMaterial(Color.SADDLEBROWN);

        for (char c : s.toCharArray()) {
            switch (c) {
                case 'F':
                    double nx = current.x + current.dir[0] * len;
                    double ny = current.y + current.dir[1] * len;
                    double nz = current.z + current.dir[2] * len;

                    // Create Cylinder
                    Cylinder cyl = createCylinder(current.x, current.y, current.z, nx, ny, nz, current.width);
                    cyl.setMaterial(wood);
                    world.getChildren().add(cyl);

                    current.x = nx;
                    current.y = ny;
                    current.z = nz;
                    break;
                case 'X': // Leaf node often
                    // Draw leaf?
                    break;
                case '+': // Rot Z
                    current.dir = rotate(current.dir, current.up, r.angle);
                    break;
                case '-': // Rot Z
                    current.dir = rotate(current.dir, current.up, -r.angle);
                    break;
                case '&': // Pitch down
                {
                    double[] right = cross(current.dir, current.up);
                    current.dir = rotate(current.dir, right, r.angle);
                    current.up = cross(right, current.dir);
                }
                    break;
                case '^': // Pitch up
                {
                    double[] right = cross(current.dir, current.up);
                    current.dir = rotate(current.dir, right, -r.angle);
                    current.up = cross(right, current.dir);
                }
                    break;
                case '/': // Roll
                    current.up = rotate(current.up, current.dir, r.angle);
                    break;
                case '\\': // Roll
                    current.up = rotate(current.up, current.dir, -r.angle);
                    break;
                case '[':
                    stack.push(new State(current.x, current.y, current.z, current.dir.clone(), current.up.clone(),
                            current.width * 0.8));
                    break;
                case ']':
                    current = stack.pop();
                    break;
            }
        }

        // Ground
        Cylinder ground = new Cylinder(500, 1);
        ground.setTranslateY(200);
        ground.setMaterial(new PhongMaterial(Color.DARKGREEN));
        world.getChildren().add(ground);
    }

    private Cylinder createCylinder(double x1, double y1, double z1, double x2, double y2, double z2, double r) {
        javafx.geometry.Point3D p1 = new javafx.geometry.Point3D(x1, y1, z1);
        javafx.geometry.Point3D p2 = new javafx.geometry.Point3D(x2, y2, z2);
        javafx.geometry.Point3D diff = p2.subtract(p1);
        double len = diff.magnitude();

        javafx.geometry.Point3D mid = p1.midpoint(p2);

        Cylinder c = new Cylinder(r, len);
        c.setTranslateX(mid.getX());
        c.setTranslateY(mid.getY());
        c.setTranslateZ(mid.getZ());

        javafx.geometry.Point3D axis = diff.normalize();
        javafx.geometry.Point3D yAxis = new javafx.geometry.Point3D(0, 1, 0);
        javafx.geometry.Point3D rotAxis = yAxis.crossProduct(axis);
        double angle = Math.acos(yAxis.dotProduct(axis));

        c.setRotationAxis(rotAxis);
        c.setRotate(Math.toDegrees(angle));

        return c;
    }

    // Math Helpers
    private double[] cross(double[] a, double[] b) {
        return new double[] {
                a[1] * b[2] - a[2] * b[1],
                a[2] * b[0] - a[0] * b[2],
                a[0] * b[1] - a[1] * b[0]
        };
    }

    private double[] rotate(double[] v, double[] axis, double angleDeg) {
        // Axis-Angle rotation
        // Normalize axis
        double len = Math.sqrt(axis[0] * axis[0] + axis[1] * axis[1] + axis[2] * axis[2]);
        double ux = axis[0] / len, uy = axis[1] / len, uz = axis[2] / len;

        double rad = Math.toRadians(angleDeg);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        double x = v[0], y = v[1], z = v[2];

        double dot = x * ux + y * uy + z * uz;

        double rx = ux * dot * (1 - cos) + x * cos + (-uz * y + uy * z) * sin;
        double ry = uy * dot * (1 - cos) + y * cos + (uz * x - ux * z) * sin;
        double rz = uz * dot * (1 - cos) + z * cos + (-uy * x + ux * y) * sin;

        return new double[] { rx, ry, rz };
    }

    public static void show(Stage stage) {
        new LSystemViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
