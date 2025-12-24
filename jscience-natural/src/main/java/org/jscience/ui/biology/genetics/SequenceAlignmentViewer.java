/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.biology.genetics;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.stage.Stage;

/**
 * DNA/RNA Sequence Alignment Visualization (Needleman-Wunsch).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class SequenceAlignmentViewer extends Application {

    private TextField seq1Field, seq2Field;
    private GridPane matrixGrid;
    private Label scoreLabel, align1Label, align2Label;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Input
        VBox inputs = new VBox(10);
        seq1Field = new TextField("GATTACA");
        seq2Field = new TextField("GCATGCU");
        Button alignBtn = new Button("Align Sequences");
        alignBtn.setMaxWidth(Double.MAX_VALUE);
        alignBtn.setStyle("-fx-background-color: #3f51b5; -fx-text-fill: white;");
        alignBtn.setOnAction(e -> runAlignment());

        inputs.getChildren().addAll(new Label("Sequence 1:"), seq1Field, new Label("Sequence 2:"), seq2Field, alignBtn);
        root.setTop(inputs);

        // Result Matrix
        matrixGrid = new GridPane();
        matrixGrid.setPadding(new Insets(20));
        matrixGrid.setHgap(2);
        matrixGrid.setVgap(2);

        ScrollPane scroll = new ScrollPane(matrixGrid);
        scroll.setFitToWidth(true);
        root.setCenter(scroll);

        // Alignment result
        VBox results = new VBox(5);
        results.setPadding(new Insets(10));
        results.setStyle("-fx-background-color: #16213e; -fx-border-color: #333;");
        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-text-fill: #00d9ff; -fx-font-size: 14px;");
        align1Label = new Label("---");
        align2Label = new Label("---");
        align1Label.setStyle("-fx-font-family: monospace; -fx-font-size: 16px; -fx-text-fill: lime;");
        align2Label.setStyle("-fx-font-family: monospace; -fx-font-size: 16px; -fx-text-fill: cyan;");
        results.getChildren().addAll(scoreLabel, align1Label, align2Label);
        root.setBottom(results);

        runAlignment();

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("DNA Sequence Alignment");
        stage.setScene(scene);
        stage.show();
    }

    private void runAlignment() {
        String s1 = seq1Field.getText().toUpperCase();
        String s2 = seq2Field.getText().toUpperCase();

        int n = s1.length();
        int m = s2.length();
        int[][] score = new int[n + 1][m + 1];

        int match = 1, mismatch = -1, gap = -1;

        // Init
        for (int i = 0; i <= n; i++)
            score[i][0] = i * gap;
        for (int j = 0; j <= m; j++)
            score[0][j] = j * gap;

        // Fill
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int s = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? match : mismatch;
                score[i][j] = Math.max(score[i - 1][j - 1] + s,
                        Math.max(score[i - 1][j] + gap, score[i][j - 1] + gap));
            }
        }

        // Traceback
        StringBuilder a1 = new StringBuilder();
        StringBuilder a2 = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0
                    && score[i][j] == score[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? match : mismatch)) {
                a1.append(s1.charAt(i - 1));
                a2.append(s2.charAt(j - 1));
                i--;
                j--;
            } else if (i > 0 && score[i][j] == score[i - 1][j] + gap) {
                a1.append(s1.charAt(i - 1));
                a2.append('-');
                i--;
            } else {
                a1.append('-');
                a2.append(s2.charAt(j - 1));
                j--;
            }
        }

        scoreLabel.setText("Score: " + score[n][m]);
        align1Label.setText(a1.reverse().toString());
        align2Label.setText(a2.reverse().toString());

        updateMatrix(score, s1, s2);
    }

    private void updateMatrix(int[][] score, String s1, String s2) {
        matrixGrid.getChildren().clear();

        // Header
        for (int i = 0; i < s1.length(); i++)
            matrixGrid.add(createHeaderLabel(String.valueOf(s1.charAt(i))), i + 2, 0);
        for (int j = 0; j < s2.length(); j++)
            matrixGrid.add(createHeaderLabel(String.valueOf(s2.charAt(j))), 0, j + 2);

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                Label l = new Label(String.valueOf(score[i][j]));
                l.setPrefSize(40, 30);
                l.setAlignment(javafx.geometry.Pos.CENTER);
                l.setStyle("-fx-border-color: #444; -fx-background-color: #2a2a4a; -fx-text-fill: white;");
                matrixGrid.add(l, i + 1, j + 1);
            }
        }
    }

    private Label createHeaderLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-weight: bold; -fx-text-fill: #3f51b5;");
        l.setPrefSize(40, 30);
        l.setAlignment(javafx.geometry.Pos.CENTER);
        return l;
    }

    public static void show(Stage stage) {
        new SequenceAlignmentViewer().start(stage);
    }
}
