package org.jscience.ui.matrix;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Simple demo for the Matrix Viewer.
 */
public class MatrixViewerDemo extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label hello = new Label("Matrix Viewer Coming Soon!");
        StackPane root = new StackPane(hello);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("JScience Matrix Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
