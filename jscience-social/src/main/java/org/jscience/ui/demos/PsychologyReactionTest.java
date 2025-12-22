package org.jscience.ui.demos;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;

import java.util.Random;

public class PsychologyReactionTest implements DemoProvider {

    @Override
    public String getCategory() {
        return "Social Sciences";
    }

    @Override
    public String getName() {
        return "Psychology: Reaction Time Test";
    }

    @Override
    public String getDescription() {
        return "Measures human reaction time to visual stimuli.";
    }

    private long startTime = 0;
    private boolean waiting = false;

    @Override
    public void show(Stage stage) {
        StackPane root = new StackPane();
        Label instruction = new Label("Click 'Start', then wait for GREEN background.");
        instruction.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        Button mainBtn = new Button("Start Test");
        mainBtn.setStyle("-fx-font-size: 16px;");

        VBox center = new VBox(20, instruction, mainBtn);
        center.setAlignment(javafx.geometry.Pos.CENTER);
        root.getChildren().add(center);

        root.setStyle("-fx-background-color: #333;");

        mainBtn.setOnAction(e -> {
            if (mainBtn.getText().equals("Start Test") || mainBtn.getText().equals("Try Again")) {
                instruction.setText("Wait for it...");
                root.setStyle("-fx-background-color: #cc3333;"); // Red
                mainBtn.setVisible(false);
                waiting = true;

                // Random delay 2-5 sec
                new Thread(() -> {
                    try {
                        Thread.sleep(2000 + new Random().nextInt(3000));
                    } catch (InterruptedException ex) {
                    }

                    Platform.runLater(() -> {
                        if (waiting) {
                            root.setStyle("-fx-background-color: #33cc33;"); // Green
                            instruction.setText("CLICK NOW!");
                            startTime = System.currentTimeMillis();
                            waiting = false;

                            // Make whole screen clickable effectively by adding handler to root
                            root.setOnMouseClicked(ev -> {
                                if (startTime > 0) {
                                    long elapsed = System.currentTimeMillis() - startTime;
                                    instruction.setText("Reaction Time: " + elapsed + " ms");
                                    root.setStyle("-fx-background-color: #333;");
                                    mainBtn.setText("Try Again");
                                    mainBtn.setVisible(true);
                                    startTime = 0;
                                    root.setOnMouseClicked(null);
                                }
                            });
                        }
                    });
                }).start();

            }
        });

        // Anti-cheat (early click)
        root.setOnMouseClicked(e -> {
            if (waiting && startTime == 0) {
                waiting = false;
                instruction.setText("Too early! Penalized.");
                root.setStyle("-fx-background-color: #333;");
                mainBtn.setText("Try Again");
                mainBtn.setVisible(true);
            }
        });

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }
}
