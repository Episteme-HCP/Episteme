package org.jscience.ui.demos;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import org.jscience.ui.SimulationDemo;
import org.jscience.ui.viewers.physics.classical.waves.AudioViewer;
import org.jscience.ui.i18n.I18n;

import java.io.File;

public class AudioViewerDemo extends SimulationDemo {

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.physics");
    }

    @Override
    public String getName() {
        return "Audio Viewer";
    }

    @Override
    public String getDescription() {
        return "Audio Spectrogram Decoder";
    }

    @Override
    protected String getLongDescription() {
        return "A demonstration of audio file processing, showing waveform and spectrogram analysis.";
    }

    @Override
    protected Node createViewerNode() {
        BorderPane root = new BorderPane();
        AudioViewer viewer = new AudioViewer();
        
        Button openButton = new Button("Open Audio File");
        openButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Audio File");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.aif", "*.au", "*.snd")
            );
            // We use the scene window as owner
            File file = fileChooser.showOpenDialog(root.getScene().getWindow());
            if (file != null) {
                viewer.loadAudio(file);
            }
        });

        // Use a toolbar styled container if possible, or just the button
        root.setTop(openButton);
        root.setCenter(viewer);
        
        return root;
    }
}
