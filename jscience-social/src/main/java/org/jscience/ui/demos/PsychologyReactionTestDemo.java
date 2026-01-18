/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import org.jscience.ui.i18n.I18n;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jscience.ui.AbstractSimulationDemo;

import java.util.Random;

public class PsychologyReactionTestDemo extends AbstractSimulationDemo {

    @Override
    public boolean isDemo() {
        return true;
    }

    @Override
    public String getCategory() { return I18n.getInstance().get("category.psychology", "Psychology"); }

    @Override
    public String getName() {
        return I18n.getInstance().get("demo.psychologyreactiontestdemo.name", "Reaction Test");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("demo.psychologyreactiontestdemo.desc", "Visual reaction time measurement.");
    }

    private long startTime = 0;
    private boolean waiting = false;

    @Override
    public void start(Stage stage) {
        initUI();
        super.start(stage);
    }
    
    @Override
    public Node createViewerNode() {
        initUI();
        return viewer;
    }
    
    private void initUI() {
        StackPane root = new StackPane();
        Label instruction = new Label(I18n.getInstance().get("demo.psychologyreactiontestdemo.inst.start", "Click 'Start', then wait for GREEN background."));
        instruction.setStyle("-fx-font-size: 20px;");

        Button mainBtn = new Button(I18n.getInstance().get("demo.psychologyreactiontestdemo.btn.start", "Start Test"));
        mainBtn.setStyle("-fx-font-size: 16px;");

        VBox center = new VBox(20, instruction, mainBtn);
        center.setAlignment(javafx.geometry.Pos.CENTER);
        root.getChildren().add(center);

        mainBtn.setOnAction(e -> {
            if (mainBtn.getText().equals(I18n.getInstance().get("demo.psychologyreactiontestdemo.btn.start", "Start Test")) ||
                    mainBtn.getText().equals(I18n.getInstance().get("demo.psychologyreactiontestdemo.btn.try", "Try Again"))) {
                instruction.setText(I18n.getInstance().get("demo.psychologyreactiontestdemo.inst.wait", "Wait for it..."));
                root.setStyle("-fx-background-color: #cc3333;");
                instruction.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
                mainBtn.setVisible(false);
                waiting = true;
                
                new Thread(() -> {
                    try { Thread.sleep(2000 + new Random().nextInt(3000)); } catch (InterruptedException ex) {}
                    Platform.runLater(() -> {
                        if (waiting) {
                            root.setStyle("-fx-background-color: #33cc33;");
                            instruction.setText(I18n.getInstance().get("demo.psychologyreactiontestdemo.inst.click", "CLICK NOW!"));
                            startTime = System.currentTimeMillis();
                            waiting = false;
                            root.setOnMouseClicked(ev -> {
                                if (startTime > 0) {
                                    long elapsed = System.currentTimeMillis() - startTime;
                                    instruction.setText(String.format(I18n.getInstance().get("demo.psychologyreactiontestdemo.result.fmt", "Reaction Time: %d ms"), elapsed));
                                    root.setStyle(""); instruction.setStyle("-fx-font-size: 20px;");
                                    mainBtn.setText(I18n.getInstance().get("demo.psychologyreactiontestdemo.btn.try", "Try Again"));
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

        root.setOnMouseClicked(e -> {
            if (waiting && startTime == 0) {
                waiting = false;
                instruction.setText(I18n.getInstance().get("demo.psychologyreactiontestdemo.text.penalty", "Too early! Penalized."));
                root.setStyle(""); instruction.setStyle("-fx-font-size: 20px;");
                mainBtn.setText(I18n.getInstance().get("demo.psychologyreactiontestdemo.btn.try", "Try Again"));
                mainBtn.setVisible(true);
            }
        });
        
        this.viewer = root;
    }

    @Override
    public String getLongDescription() {
        return I18n.getInstance().get("demo.psychologyreactiontestdemo.longdesc", "Measure your visual reaction time with this interactive test.");
    }
}

