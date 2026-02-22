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

        // 1. Show Splash Screen immediately
        Stage splashStage = new Stage();
        splashStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
        
        javafx.scene.control.Label titleLabel = new javafx.scene.control.Label("JScience Benchmarks");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        
        javafx.scene.control.Label loadLabel = new javafx.scene.control.Label("Initializing Core Modules...");
        loadLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");
        
        javafx.scene.control.ProgressBar splashProgress = new javafx.scene.control.ProgressBar();
        splashProgress.setPrefWidth(250);
        splashProgress.setStyle("-fx-accent: #0078d4;");

        javafx.scene.layout.VBox splashLayout = new javafx.scene.layout.VBox(15, titleLabel, splashProgress, loadLabel);
        splashLayout.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 30; -fx-border-color: #444; -fx-border-width: 1;");
        splashLayout.setAlignment(javafx.geometry.Pos.CENTER);
        
        Scene splashScene = new Scene(splashLayout, 320, 150);
        splashStage.setScene(splashScene);
        splashStage.show();

        // Start metrics server background
        new Thread(() -> 
            org.jscience.core.technical.monitoring.DistributedMonitor.getInstance().startServer()
        , "Monitor-Starter").start();

        // 2. Load Main UI in background (emulated)
        new Thread(() -> {
            try {
                // Short delay to ensure splash renders (and emulate init time if JVM is fast)
                Thread.sleep(100); 
                
                javafx.application.Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
                        BorderPane root = loader.load();
                        
                        Scene scene = new Scene(root, 1200, 800);
                        scene.setFill(Color.TRANSPARENT);

                        try {
                            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/org/jscience/core/ui/icon.png"));
                            splashStage.getIcons().add(icon);
                            primaryStage.getIcons().add(icon);
                        } catch (Exception ex) {
                            // Icon missing, continue
                        }

                        primaryStage.setTitle("JScience Benchmarking Suite");
                        primaryStage.setScene(scene);
                        
                        splashStage.close();
                        primaryStage.show();
                    } catch (Throwable e) {
                        e.printStackTrace();
                        // If main load fails, show alert on splash or console?
                        // For now print trace, the splash will close if we don't catch properly in runLater
                    }
                });
            } catch (InterruptedException e) {}
        }).start();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Stopping JScience Benchmarking Suite...");
        org.jscience.core.technical.monitoring.DistributedMonitor.getInstance().stopServer();
        super.stop();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
