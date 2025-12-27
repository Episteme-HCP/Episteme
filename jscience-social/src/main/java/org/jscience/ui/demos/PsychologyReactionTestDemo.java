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

public class PsychologyReactionTestDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("category.psychology");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("PsychologyReaction.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("PsychologyReaction.desc");
    }

    private long startTime = 0;
    private boolean waiting = false;

    @Override
    public void show(Stage stage) {
        StackPane root = new StackPane();
        Label instruction = new Label(org.jscience.ui.i18n.SocialI18n.getInstance().get("psych.inst.start"));
        instruction.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        Button mainBtn = new Button(org.jscience.ui.i18n.SocialI18n.getInstance().get("psych.btn.start"));
        mainBtn.setStyle("-fx-font-size: 16px;");

        VBox center = new VBox(20, instruction, mainBtn);
        center.setAlignment(javafx.geometry.Pos.CENTER);
        root.getChildren().add(center);

        root.setStyle("-fx-background-color: #333;");

        mainBtn.setOnAction(e -> {
            if (mainBtn.getText().equals(org.jscience.ui.i18n.SocialI18n.getInstance().get("psych.btn.start")) ||
                    mainBtn.getText().equals(org.jscience.ui.i18n.SocialI18n.getInstance().get("psych.btn.try"))) {
                instruction.setText(org.jscience.ui.i18n.SocialI18n.getInstance().get("psych.inst.wait"));
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
                            instruction.setText(org.jscience.ui.i18n.SocialI18n.getInstance().get("psych.inst.click"));
                            startTime = System.currentTimeMillis();
                            waiting = false;

                            // Make whole screen clickable effectively by adding handler to root
                            root.setOnMouseClicked(ev -> {
                                if (startTime > 0) {
                                    long elapsed = System.currentTimeMillis() - startTime;
                                    instruction.setText(String.format(
                                            org.jscience.ui.i18n.SocialI18n.getInstance().get("psych.result.fmt"),
                                            elapsed));
                                    root.setStyle("-fx-background-color: #333;");
                                    mainBtn.setText(org.jscience.ui.i18n.SocialI18n.getInstance().get("psych.btn.try"));
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
                instruction.setText(org.jscience.ui.i18n.SocialI18n.getInstance().get("psych.penalty"));
                root.setStyle("-fx-background-color: #333;");
                mainBtn.setText(org.jscience.ui.i18n.SocialI18n.getInstance().get("psych.btn.try"));
                mainBtn.setVisible(true);
            }
        });

        Scene scene = new Scene(root, 600, 400);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }
}
