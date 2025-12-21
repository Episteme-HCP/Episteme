package org.jscience.ui.biology.lsystems;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * L-System (Lindenmayer System) Viewer.
 * Generates fractal plants and structures.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LSystemViewer extends Application {

    private Canvas canvas;
    private int iterations = 4;
    private String currentRule = "Plant";

    private static class Rule {
        String axiom;
        Map<Character, String> productions;
        double angle;

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
        Rule plant = new Rule("X", 25);
        plant.add('X', "F+[[X]-X]-F[-FX]+X");
        plant.add('F', "FF");
        rules.put("Plant", plant);

        Rule dragon = new Rule("FX", 90);
        dragon.add('X', "X+YF+");
        dragon.add('Y', "-FX-Y");
        rules.put("Dragon Curve", dragon);

        Rule snowflake = new Rule("F--F--F", 60);
        snowflake.add('F', "F+F--F+F");
        rules.put("Koch Snowflake", snowflake);
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        canvas = new Canvas(800, 600);
        root.setCenter(canvas);

        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new javafx.geometry.Insets(10));

        ComboBox<String> selector = new ComboBox<>();
        selector.getItems().addAll(rules.keySet());
        selector.setValue("Plant");
        selector.setOnAction(e -> {
            currentRule = selector.getValue();
            draw();
        });

        Slider iterSlider = new Slider(1, 6, 4);
        iterSlider.setShowTickLabels(true);
        iterSlider.setShowTickMarks(true);
        iterSlider.valueProperty().addListener((o, old, v) -> {
            iterations = v.intValue();
            draw();
        });

        controls.getChildren().addAll(new Label("System:"), selector, new Label("Iterations:"), iterSlider);
        root.setBottom(controls);

        draw();

        Scene scene = new Scene(root, 800, 700);
        stage.setTitle("JScience L-System Viewer");
        stage.setScene(scene);
        stage.show();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.FORESTGREEN);
        gc.setLineWidth(1);

        Rule r = rules.get(currentRule);
        String s = r.axiom;
        for (int i = 0; i < iterations; i++) {
            StringBuilder sb = new StringBuilder();
            for (char c : s.toCharArray()) {
                sb.append(r.productions.getOrDefault(c, String.valueOf(c)));
            }
            s = sb.toString();
        }

        // Turtle Graphics
        double x = 400, y = 550;
        double dir = -90; // Up
        if (currentRule.equals("Koch Snowflake")) {
            x = 200;
            y = 400;
            dir = 0;
        }
        if (currentRule.equals("Dragon Curve")) {
            x = 400;
            y = 300;
        }

        double len = 100 / Math.pow(iterations, 0.7); // Auto scale roughly
        if (currentRule.equals("Plant"))
            len = 5 * (6.0 / iterations);

        java.util.Stack<double[]> stack = new java.util.Stack<>();

        for (char c : s.toCharArray()) {
            switch (c) {
                case 'F':
                    double nx = x + len * Math.cos(Math.toRadians(dir));
                    double ny = y + len * Math.sin(Math.toRadians(dir));
                    gc.strokeLine(x, y, nx, ny);
                    x = nx;
                    y = ny;
                    break;
                case '+':
                    dir += r.angle;
                    break;
                case '-':
                    dir -= r.angle;
                    break;
                case '[':
                    stack.push(new double[] { x, y, dir });
                    break;
                case ']':
                    double[] state = stack.pop();
                    x = state[0];
                    y = state[1];
                    dir = state[2];
                    break;
            }
        }
    }

    public static void show(Stage stage) {
        new LSystemViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
