package org.jscience.apps;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.jscience.apps.framework.I18nManager;

/**
 * Main Launcher for JScience Killer Apps.
 */
public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) {
        String title = I18nManager.getInstance().get("launcher.title");
        Label label = new Label(title);
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        StackPane root = new StackPane(label);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
