/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
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
        instruction.setStyle("-fx-font-size: 20px;"); // Removed white text

        Button mainBtn = new Button(org.jscience.ui.i18n.SocialI18n.getInstance().get("psych.btn.start"));
        mainBtn.setStyle("-fx-font-size: 16px;");

        VBox center = new VBox(20, instruction, mainBtn);
        center.setAlignment(javafx.geometry.Pos.CENTER);
        root.getChildren().add(center);

        // root.setStyle("-fx-background-color: #333;"); // Removed dark background

        mainBtn.setOnAction(e -> {
            if (mainBtn.getText().equals(org.jscience.ui.i18n.SocialI18n.getInstance().get("psych.btn.start")) ||
                    mainBtn.getText().equals(org.jscience.ui.i18n.SocialI18n.getInstance().get("psych.btn.try"))) {
                instruction.setText(org.jscience.ui.i18n.SocialI18n.getInstance().get("psych.inst.wait"));
                root.setStyle("-fx-background-color: #cc3333;"); // Red (Keep for functionality)
                instruction.setStyle("-fx-font-size: 20px; -fx-text-fill: white;"); // Ensure text is white on red
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
                                    root.setStyle(""); // Reset to default
                                    instruction.setStyle("-fx-font-size: 20px;"); // Reset text color
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
                root.setStyle(""); // Reset to default
                instruction.setStyle("-fx-font-size: 20px;"); // Reset text color
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
