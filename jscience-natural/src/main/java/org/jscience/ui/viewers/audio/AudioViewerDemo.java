package org.jscience.ui.viewers.audio;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AudioViewerDemo extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        SpectrogramViewer viewer = new SpectrogramViewer();
        
        Button openButton = new Button("Open Audio File");
        openButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Audio File");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.aif", "*.au", "*.snd")
            );
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                viewer.loadAudio(file);
            }
        });

        HBox topBar = new HBox(10, openButton);
        topBar.setStyle("-fx-padding: 10; -fx-background-color: #ddd;");
        
        root.setTop(topBar);
        root.setCenter(viewer);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("JScience Audio Viewer Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
