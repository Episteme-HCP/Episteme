/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.mathematics.logic;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactive Symbolic Proof Explorer (Metamath-style).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class MetamathViewer extends Application {

    private VBox proofStepsBox;
    private List<String> currentSteps = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Header
        Label header = new Label("Metamath Proof Explorer");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        root.setTop(header);

        // Current Theorem
        VBox center = new VBox(20);
        center.setPadding(new Insets(20));

        Label theoremLabel = new Label("Theorem: (p ∨ q) ∧ ¬p ⊢ q (Disjunctive Syllogism)");
        theoremLabel.setStyle("-fx-font-size: 16px; -fx-background-color: #e3f2fd; -fx-padding: 10;");

        proofStepsBox = new VBox(10);
        ScrollPane scroll = new ScrollPane(proofStepsBox);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(400);

        center.getChildren().addAll(theoremLabel, new Label("Step-by-step proof:"), scroll);
        root.setCenter(center);

        // Actions
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #f5f5f5;");

        Button nextStepBtn = new Button("Apply Next Tactic");
        nextStepBtn.setMaxWidth(Double.MAX_VALUE);
        nextStepBtn.setOnAction(e -> applyTactic());

        Button resetBtn = new Button("Reset Proof");
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setOnAction(e -> {
            currentSteps.clear();
            proofStepsBox.getChildren().clear();
        });

        sidebar.getChildren().addAll(new Label("Logic Tactics"), nextStepBtn, resetBtn);
        root.setRight(sidebar);

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("viewer.metamath"));
        stage.setScene(scene);
        stage.show();
    }

    private void applyTactic() {
        String[] allSteps = {
                "1. (p ∨ q) ∧ ¬p [Hypothesis]",
                "2. (p ∨ q) [Simplification (1)]",
                "3. ¬p [Simplification (1)]",
                "4. p → q [Material Implication (2)]",
                "5. q [Modus Ponens (3, 4)]",
                "Q.E.D."
        };

        if (currentSteps.size() < allSteps.length) {
            String step = allSteps[currentSteps.size()];
            currentSteps.add(step);

            Label l = new Label(step);
            l.setStyle("-fx-font-family: monospace; -fx-font-size: 14px; -fx-background-color: " +
                    (step.contains("Q.E.D") ? "#c8e6c9" : "white") + "; -fx-padding: 5;");
            proofStepsBox.getChildren().add(l);
        }
    }

    public static void show(Stage stage) {
        new MetamathViewer().start(stage);
    }
}
