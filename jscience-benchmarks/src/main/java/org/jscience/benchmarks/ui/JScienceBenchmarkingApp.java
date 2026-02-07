package org.jscience.benchmarks.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.core.ui.i18n.I18N;


/**
 * JScience Benchmarking Suite - Master Control for Benchmarking and Performance Analysis.
 * Standardized JavaFX application using FXML and CSS.
 */
public class JScienceBenchmarkingApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Register benchmark I18N bundle
        I18N.getInstance().addBundle("org.jscience.benchmarks.i18n.messages_benchmarks");

        // Start metrics server for real-time monitoring
        org.jscience.core.technical.monitoring.DistributedMonitor.getInstance().startServer();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            BorderPane root = loader.load();
            
            Scene scene = new Scene(root, 1200, 800);
            scene.setFill(Color.TRANSPARENT);

            primaryStage.setTitle("JScience Benchmarking Suite");
            primaryStage.setScene(scene);
            
            // Apply premium styling
            primaryStage.show();
            
        } catch (Throwable e) {
            System.err.println("CRITICAL: Failed to load JScience Studio UI");
            System.err.println("Source: " + getClass().getResource("MainView.fxml"));
            System.err.println("Reason: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Caused by: " + e.getCause().getMessage());
            }
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
