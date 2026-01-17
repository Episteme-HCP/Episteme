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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
    public String getCategory() { return "Biology"; }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("SequenceAlignment.title", "Sequence Alignment");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("SequenceAlignment.desc", "Needleman-Wunsch Global Alignment Algorithm");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("SequenceAlignment.long_desc", 
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
            getStyleClass().add("viewer-root");
            // Top Input
            VBox inputs = new VBox(10);
            seq1Field = new TextField("GATTACA");
            seq2Field = new TextField("GCATGCU");
            Button alignBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("align.btn.align", "Align Sequences"));
            alignBtn.setMaxWidth(Double.MAX_VALUE);
            alignBtn.setOnAction(e -> runAlignment());

            Label l1 = new Label(org.jscience.ui.i18n.I18n.getInstance().get("align.seq1", "Sequence 1:"));
            l1.getStyleClass().add("header-label");
            Label l2 = new Label(org.jscience.ui.i18n.I18n.getInstance().get("align.seq2", "Sequence 2:"));
            l2.getStyleClass().add("header-label");

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
            results.getStyleClass().add("info-panel");

            scoreLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.sequencealignment.score.0", "Score: 0"));
            scoreLabel.getStyleClass().add("header-label");

            align1Label = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.sequencealignment.", "---"));
            align2Label = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.sequencealignment.", "---"));

            align1Label.getStyleClass().add("description-label");
            align2Label.getStyleClass().add("description-label");

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

            scoreLabel.setText(String.format(org.jscience.ui.i18n.I18n.getInstance().get("align.score.fmt", "Alignment Score: %d"), score[n][m]));
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
                    l.getStyleClass().add("cell");
                    matrixGrid.add(l, i + 1, j + 1);
                }
            }
        }

        private Label createHeaderLabel(String text) {
            Label l = new Label(text);
            l.getStyleClass().add("header-label");
            l.setPrefSize(40, 30);
            l.setAlignment(Pos.CENTER);
            return l;
        }
        
        @Override
        public String getName() { return "Sequence Viewer"; }
        @Override
    public String getCategory() { return "Biology"; }
    
        @Override
        public String getDescription() { return "InternalSequenceViewer Internal Viewer"; }

        @Override
        public String getLongDescription() { return getDescription(); }

        

    @Override public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() { return new java.util.ArrayList<>(); }
}
}
