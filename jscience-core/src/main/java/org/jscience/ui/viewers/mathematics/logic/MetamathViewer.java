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

package org.jscience.ui.viewers.mathematics.logic;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactive Symbolic Proof Explorer (Metamath-style).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MetamathViewer extends Application {

    private static class Theorem {
        String name;
        String formula;
        String[] steps;

        Theorem(String name, String formula, String[] steps) {
            this.name = name;
            this.formula = formula;
            this.steps = steps;
        }
    }

    private List<Theorem> theorems = new ArrayList<>();
    private Theorem currentTheorem;
    private List<String> currentSteps = new ArrayList<>();

    private VBox proofStepsBox;
    private ComboBox<String> theoremSelector;
    private Label theoremLabel;

    @Override
    public void start(Stage stage) {
        initTheorems();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Header
        Label header = new Label(I18n.getInstance().get("metamath.title"));
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        root.setTop(header);

        // Center
        VBox center = new VBox(20);
        center.setPadding(new Insets(20));

        // Theorem Selection
        HBox selectorBox = new HBox(10);
        selectorBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        theoremSelector = new ComboBox<>();
        for (Theorem t : theorems) {
            theoremSelector.getItems().add(t.name);
        }
        theoremSelector.getSelectionModel().selectFirst();
        theoremSelector.setOnAction(e -> {
            int idx = theoremSelector.getSelectionModel().getSelectedIndex();
            if (idx >= 0 && idx < theorems.size()) {
                loadTheorem(theorems.get(idx));
            }
        });

        selectorBox.getChildren().addAll(new Label(I18n.getInstance().get("metamath.select")), theoremSelector);

        theoremLabel = new Label();
        theoremLabel.setStyle(
                "-fx-font-size: 16px; -fx-background-color: #e3f2fd; -fx-padding: 10; -fx-background-radius: 5;");
        theoremLabel.setMaxWidth(Double.MAX_VALUE);

        proofStepsBox = new VBox(10);
        ScrollPane scroll = new ScrollPane(proofStepsBox);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(400);

        center.getChildren().addAll(selectorBox, theoremLabel, new Label(I18n.getInstance().get("metamath.proof")),
                scroll);
        root.setCenter(center);

        // Sidebar
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #f5f5f5;");

        Button nextStepBtn = new Button(I18n.getInstance().get("metamath.btn.next"));
        nextStepBtn.setMaxWidth(Double.MAX_VALUE);
        nextStepBtn.setOnAction(e -> applyTactic());

        Button resetBtn = new Button(I18n.getInstance().get("metamath.btn.reset"));
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setOnAction(e -> loadTheorem(currentTheorem));

        sidebar.getChildren().addAll(new Label(I18n.getInstance().get("metamath.tactics")), nextStepBtn, resetBtn);
        root.setRight(sidebar);

        // Load initial
        if (!theorems.isEmpty()) {
            loadTheorem(theorems.get(0));
        }

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle(I18n.getInstance().get("viewer.metamath"));
        stage.setScene(scene);
        stage.show();
    }

    private void initTheorems() {
        theorems.add(new Theorem(
                "Disjunctive Syllogism",
                "(p \u2228 q) \u2227 \u00ACp \u22A2 q",
                new String[] {
                        "1. (p \u2228 q) \u2227 \u00ACp [Hypothesis]",
                        "2. (p \u2228 q) [Simplification (1)]",
                        "3. \u00ACp [Simplification (1)]",
                        "4. p \u2192 q [Material Implication (2)]",
                        "5. q [Modus Ponens (3, 4)]",
                        "Q.E.D."
                }));
        theorems.add(new Theorem(
                "Modus Tollens",
                "(p \u2192 q) \u2227 \u00ACq \u22A2 \u00ACp",
                new String[] {
                        "1. (p \u2192 q) \u2227 \u00ACq [Hypothesis]",
                        "2. p \u2192 q [Simplification (1)]",
                        "3. \u00ACq [Simplification (1)]",
                        "4. \u00ACq \u2192 \u00ACp [Transposition (2)]",
                        "5. \u00ACp [Modus Ponens (3, 4)]",
                        "Q.E.D."
                }));
        theorems.add(new Theorem(
                "Double Negation",
                "p \u22A2 \u00AC\u00ACp",
                new String[] {
                        "1. p [Hypothesis]",
                        "2. p \u2228 p [Addition]",
                        "3. \u00ACp \u2192 p [Material Implication (2)]",
                        "4. \u00AC\u00ACp \u2228 p [Material Implication]",
                        "5. \u00AC\u00ACp [Derived Step]",
                        "Q.E.D."
                }));
    }

    private void loadTheorem(Theorem t) {
        currentTheorem = t;
        currentSteps.clear();
        proofStepsBox.getChildren().clear();
        theoremLabel.setText("Theorem: " + t.formula + " (" + t.name + ")");
    }

    private void applyTactic() {
        if (currentTheorem == null)
            return;

        if (currentSteps.size() < currentTheorem.steps.length) {
            String step = currentTheorem.steps[currentSteps.size()];
            currentSteps.add(step);

            Label l = new Label(step);
            l.setStyle("-fx-font-family: monospace; -fx-font-size: 14px; -fx-background-color: " +
                    (step.contains("Q.E.D") ? "#c8e6c9" : "white")
                    + "; -fx-padding: 5; -fx-border-color: #eee; -fx-border-width: 0 0 1 0;");
            proofStepsBox.getChildren().add(l);
        }
    }

    public static void show(Stage stage) {
        new MetamathViewer().start(stage);
    }
}
