package org.jscience.benchmarks.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.core.ui.i18n.I18N;

import java.io.IOException;

/**
 * JScience Studio - Master Control for Benchmarking and Performance Analysis.
 * Standardized JavaFX application using FXML and CSS.
 */
public class JScienceBenchmarkingApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Register benchmark I18N bundle
        I18N.getInstance().addBundle("org.jscience.benchmarks.i18n.messages_benchmarks");

        // Start metrics server for real-time monitoring
        org.jscience.benchmarks.monitoring.DistributedMonitor.getInstance().startServer();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            BorderPane root = loader.load();
            
            Scene scene = new Scene(root, 1200, 800);
            scene.setFill(Color.TRANSPARENT);

            primaryStage.setTitle("JScience Studio - Benchmarking Suite");
            primaryStage.setScene(scene);
            
            // Apply premium styling
            primaryStage.show();
            
        } catch (IOException e) {
            System.err.println("Failed to load JScience Studio UI: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
