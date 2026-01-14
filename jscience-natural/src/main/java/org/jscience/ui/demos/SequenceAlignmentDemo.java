/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.ui.demos;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jscience.ui.AbstractDemo;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.i18n.I18n;

/**
 * DNA/RNA Sequence Alignment Visualization (Needleman-Wunsch).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SequenceAlignmentDemo extends AbstractDemo {

    @Override
    public boolean isDemo() {
        return true;
    }

    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("SequenceAlignment.title", "Sequence Alignment");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("SequenceAlignment.desc", "Needleman-Wunsch Global Alignment Algorithm");
    }

    @Override
    protected String getLongDescription() {
        return I18n.getInstance().get("SequenceAlignment.long_desc", 
            "The Needleman-Wunsch algorithm performs global alignment on two sequences. " +
            "It is commonly used in bioinformatics to align protein or nucleotide sequences.");
    }

    @Override
    public Node createViewerNode() {
        return new InternalSequenceViewer();
    }

    private static class InternalSequenceViewer extends AbstractViewer {

        private TextField seq1Field, seq2Field;
        private GridPane matrixGrid;
        private Label scoreLabel, align1Label, align2Label;

        public InternalSequenceViewer() {
            setPadding(new Insets(20));
            // Top Input
            VBox inputs = new VBox(10);
            seq1Field = new TextField("GATTACA");
            seq2Field = new TextField("GCATGCU");
            Button alignBtn = new Button(I18n.getInstance().get("align.btn.align", "Align Sequences"));
            alignBtn.setMaxWidth(Double.MAX_VALUE);
            alignBtn.setOnAction(e -> runAlignment());

            Label l1 = new Label(I18n.getInstance().get("align.seq1", "Sequence 1:"));
            l1.getStyleClass().add("dark-label");
            Label l2 = new Label(I18n.getInstance().get("align.seq2", "Sequence 2:"));
            l2.getStyleClass().add("dark-label");

            inputs.getChildren().addAll(l1, seq1Field, l2, seq2Field, alignBtn);
            setTop(inputs);

            // Center Matrix
            matrixGrid = new GridPane();
            matrixGrid.setPadding(new Insets(20));
            matrixGrid.setHgap(2);
            matrixGrid.setVgap(2);

            ScrollPane scroll = new ScrollPane(matrixGrid);
            scroll.setFitToWidth(true);
            setCenter(scroll);

            // Bottom Result
            VBox results = new VBox(5);
            results.setPadding(new Insets(10));
            results.getStyleClass().add("dark-panel");

            scoreLabel = new Label("Score: 0");
            scoreLabel.getStyleClass().add("dark-label-info");

            align1Label = new Label("---");
            align2Label = new Label("---");

            align1Label.getStyleClass().add("dark-label-mono");
            align2Label.getStyleClass().add("dark-label-mono");

            results.getChildren().addAll(scoreLabel, align1Label, align2Label);
            setBottom(results);

            runAlignment();
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

            scoreLabel.setText(String.format(I18n.getInstance().get("align.score.fmt", "Alignment Score: %d"), score[n][m]));
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
                    l.setAlignment(Pos.CENTER);
                    l.getStyleClass().add("dark-cell");
                    matrixGrid.add(l, i + 1, j + 1);
                }
            }
        }

        private Label createHeaderLabel(String text) {
            Label l = new Label(text);
            l.getStyleClass().add("dark-header");
            l.setPrefSize(40, 30);
            l.setAlignment(Pos.CENTER);
            return l;
        }
        
        @Override
        public String getName() { return "Sequence Viewer"; }
        @Override
        public String getCategory() { return "Biology"; }
    }
}
