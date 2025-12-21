package org.jscience.ui.engineering.circuits;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Logic Gate Simulator.
 * Interactive circuit builder.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LogicGateSimulator extends Application {

    private Pane root;
    private final List<Gate> gates = new ArrayList<>();

    private static abstract class Gate extends Pane {
        protected boolean output;

        Gate() {
            setStyle("-fx-border-color: black; -fx-background-color: lightblue;");
            setPrefSize(60, 40);
        }

        abstract void evaluate();

        boolean getOutput() {
            return output;
        }
    }

    private static class AndGate extends Gate {
        AndGate() {
            getChildren().add(new Label("AND"));
        }

        @Override
        void evaluate() {
            output = true; // Placeholder logic
        }
    }

    private static class Switch extends Gate {
        boolean state = false;

        Switch() {
            setPrefSize(30, 30);
            Rectangle rect = new Rectangle(30, 30, Color.RED);
            getChildren().add(rect);
            setOnMouseClicked(e -> {
                state = !state;
                rect.setFill(state ? Color.GREEN : Color.RED);
            });
        }

        @Override
        void evaluate() {
            output = state;
        }
    }

    @Override
    public void start(Stage stage) {
        root = new Pane();
        root.setPrefSize(800, 600);

        Label instructions = new Label("Click to add SW, Drag to move (Demo Mockup)");
        instructions.setLayoutX(10);
        instructions.setLayoutY(10);
        root.getChildren().add(instructions);

        // Add some default items
        addSwitch(50, 50);
        addSwitch(50, 150);
        addGate(200, 100);

        Scene scene = new Scene(root);
        stage.setTitle("JScience Logic Simulator");
        stage.setScene(scene);
        stage.show();
    }

    private void addSwitch(double x, double y) {
        Switch s = new Switch();
        s.setLayoutX(x);
        s.setLayoutY(y);
        makeDraggable(s);
        gates.add(s);
        root.getChildren().add(s);
    }

    private void addGate(double x, double y) {
        AndGate g = new AndGate();
        g.setLayoutX(x);
        g.setLayoutY(y);
        makeDraggable(g);
        gates.add(g);
        root.getChildren().add(g);
    }

    private void makeDraggable(javafx.scene.Node node) {
        final double[] anchor = new double[2];
        node.setOnMousePressed(e -> {
            anchor[0] = e.getSceneX() - node.getLayoutX();
            anchor[1] = e.getSceneY() - node.getLayoutY();
        });
        node.setOnMouseDragged(e -> {
            node.setLayoutX(e.getSceneX() - anchor[0]);
            node.setLayoutY(e.getSceneY() - anchor[1]);
        });
    }

    public static void show(Stage stage) {
        new LogicGateSimulator().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
