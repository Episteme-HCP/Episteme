package org.jscience.apps;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main Launcher for JScience Killer Apps.
 */
public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("JScience Killer Apps Launcher");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("JScience Killer Apps");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
