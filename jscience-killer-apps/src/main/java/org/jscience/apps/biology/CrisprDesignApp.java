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

package org.jscience.apps.biology;

import javafx.collections.FXCollections;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import org.jscience.apps.framework.KillerAppBase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CRISPR Design Tool.
 * <p>
 * Scans genomic sequences for Cas9 targets (PAM 'NGG').
 * Evaluates on-target efficiency and potential off-target sites.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CrisprDesignApp extends KillerAppBase {

    private TextArea genomeArea;
    private TableView<GuiTarget> resultsTable;
    private TextFlow visualFlow;

    public static class GuiTarget {
        private final int position;
        private final String sequence; // 20bp + PAM
        private final String pam;
        private final double score; // Mock efficiency score
        private final int offTargets; // Number of potential off-target sites
        private final double specificity; // Specificity score (0-100)

        public GuiTarget(int position, String sequence, String pam, double score, int offTargets) {
            this.position = position;
            this.sequence = sequence;
            this.pam = pam;
            this.score = score;
            this.offTargets = offTargets;
            // Specificity: decreases with more off-targets
            this.specificity = Math.max(0, 100 - offTargets * 15);
        }

        public int getPosition() {
            return position;
        }

        public String getSequence() {
            return sequence;
        }

        public String getPam() {
            return pam;
        }

        public double getScore() {
            return score;
        }

        public int getOffTargets() {
            return offTargets;
        }

        public double getSpecificity() {
            return specificity;
        }
    }

    @Override
    protected String getAppTitle() {
        return i18n.get("crispr.title");
    }

    @Override
    public String getDescription() {
        return i18n.get("crispr.desc");
    }

    @Override
    protected Region createMainContent() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: Inputs
        VBox top = new VBox(10);
        top.setPadding(new Insets(0, 0, 10, 0));

        Label lblGenome = new Label(i18n.get("crispr.label.genome"));
        genomeArea = new TextArea();
        genomeArea.setPrefHeight(100);
        genomeArea.setWrapText(true);
        // Default sample: A snippet of a gene
        genomeArea.setText(
                "ATGGCCTCCTCCGAGGACGTCATCAAGGAGCTGATGGACGACGTGGTGAAGCTGGGCGTGGGGCAGCGGCCAGAGGGGGAGGGATGGGTGCAAAAGAGGATTGAAGACCCTGGAAAGAAAAGTGCCATGTGAGTGTG");

        Button loadBtn = new Button(i18n.get("crispr.button.load"));
        loadBtn.setOnAction(e -> loadFasta());

        Button sampleBtn = new Button(i18n.get("crispr.button.sample"));
        sampleBtn.setOnAction(e -> loadSample());

        Button findBtn = new Button(i18n.get("crispr.button.find"));
        findBtn.setDefaultButton(true);
        findBtn.setOnAction(e -> scanGenome());

        top.getChildren().addAll(lblGenome, genomeArea, new HBox(10, loadBtn, sampleBtn, findBtn));

        // Center: Results Table
        resultsTable = new TableView<>();
        TableColumn<GuiTarget, Integer> colPos = new TableColumn<>(i18n.get("crispr.table.pos"));
        colPos.setCellValueFactory(new PropertyValueFactory<>("position"));

        TableColumn<GuiTarget, String> colSeq = new TableColumn<>(i18n.get("crispr.table.seq"));
        colSeq.setCellValueFactory(new PropertyValueFactory<>("sequence"));
        colSeq.setMinWidth(200);

        TableColumn<GuiTarget, String> colPam = new TableColumn<>(i18n.get("crispr.table.pam"));
        colPam.setCellValueFactory(new PropertyValueFactory<>("pam"));

        TableColumn<GuiTarget, Double> colScore = new TableColumn<>(i18n.get("crispr.table.eff"));
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));

        TableColumn<GuiTarget, Integer> colOffTargets = new TableColumn<>(i18n.get("crispr.table.off"));
        colOffTargets.setCellValueFactory(new PropertyValueFactory<>("offTargets"));

        TableColumn<GuiTarget, Double> colSpecificity = new TableColumn<>(i18n.get("crispr.table.spec"));
        colSpecificity.setCellValueFactory(new PropertyValueFactory<>("specificity"));

        @SuppressWarnings("unchecked")
        TableColumn<GuiTarget, ?>[] columns = new TableColumn[] { colPos, colSeq, colPam, colScore, colOffTargets,
                colSpecificity };
        resultsTable.getColumns().addAll(columns);

        resultsTable.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null)
                highlightTarget(nv);
        });

        // Bottom: Visualization
        VBox bottom = new VBox(5);
        bottom.setPadding(new Insets(10, 0, 0, 0));
        Label lblVis = new Label(i18n.get("crispr.label.visual"));
        visualFlow = new TextFlow();
        ScrollPane scrollVis = new ScrollPane(visualFlow);
        scrollVis.setPrefHeight(60);
        scrollVis.setFitToWidth(true);

        bottom.getChildren().addAll(lblVis, scrollVis);

        root.setTop(top);
        root.setCenter(resultsTable);
        root.setBottom(bottom);

        return root;
    }

    private void loadFasta() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(i18n.get("crispr.file.fasta"), "*.fasta", "*.fa"));

        File f = fc.showOpenDialog(null);
        if (f != null) {
            try (java.io.FileInputStream fis = new java.io.FileInputStream(f)) {
                List<org.jscience.biology.loaders.FastaLoader.Sequence> seqs = org.jscience.biology.loaders.FastaLoader
                        .load(fis);
                if (!seqs.isEmpty()) {
                    genomeArea.setText(seqs.get(0).data.toUpperCase());
                }
            } catch (Exception e) {
                showError(i18n.get("crispr.error.load"), e.getMessage());
            }
        }
    }

    private void loadSample() {
        try (java.io.InputStream is = getClass().getResourceAsStream("data/crispr_sample.fasta")) {
            if (is != null) {
                List<org.jscience.biology.loaders.FastaLoader.Sequence> seqs = org.jscience.biology.loaders.FastaLoader
                        .load(is);
                if (!seqs.isEmpty()) {
                    genomeArea.setText(seqs.get(0).data.toUpperCase());
                }
            } else {
                showError(i18n.get("dialog.error.title"), i18n.get("crispr.error.sample"));
            }
        } catch (Exception e) {
            showError(i18n.get("crispr.error.load"), e.getMessage());
        }
    }

    private void scanGenome() {
        String genome = genomeArea.getText().toUpperCase().replaceAll("[^ATCG]", "");
        if (genome.isEmpty())
            return;

        List<GuiTarget> targets = new ArrayList<>();

        // Regex for NGG (any base, G, G)
        // We need 20bp before it + PAM = 23bp total
        Pattern pamPattern = Pattern.compile("(?=([ATCG]{21}GG))");
        Matcher m = pamPattern.matcher(genome);

        while (m.find()) {
            int start = m.start(); // This is the start of the 20bp spacer
            String match = m.group(1); // 23 chars

            if (match.length() == 23) {
                String spacer = match.substring(0, 20);
                String pam = match.substring(20);

                // Mock Scoring: GC content (40-60% ideal)
                double gc = 0;
                for (char c : spacer.toCharArray())
                    if (c == 'G' || c == 'C')
                        gc++;
                double gcPercent = gc / 20.0;
                double score = 100 * (1.0 - Math.abs(0.5 - gcPercent));

                // Off-target scoring: count similar sequences (>85% match)
                int offTargets = countOffTargets(genome, spacer);

                targets.add(new GuiTarget(start + 1, spacer, pam, Math.round(score * 10) / 10.0, offTargets));
            }
        }

        resultsTable.setItems(FXCollections.observableArrayList(targets));
        if (!targets.isEmpty()) {
            scanGenomeVisual(genome);
        }
    }

    /**
     * Counts potential off-target sites by finding sequences with >85% similarity.
     */
    private int countOffTargets(String genome, String spacer) {
        int count = 0;
        int seqLen = spacer.length();
        int threshold = (int) (seqLen * 0.85); // 85% match threshold

        for (int i = 0; i <= genome.length() - seqLen; i++) {
            String candidate = genome.substring(i, i + seqLen);
            if (candidate.equals(spacer))
                continue; // Skip exact match (on-target)

            int matches = 0;
            for (int j = 0; j < seqLen; j++) {
                if (candidate.charAt(j) == spacer.charAt(j)) {
                    matches++;
                }
            }

            if (matches >= threshold) {
                count++;
            }
        }
        return count;
    }

    private void scanGenomeVisual(String genome) {
        visualFlow.getChildren().clear();
        Text t = new Text(genome);
        t.setFont(javafx.scene.text.Font.font("Monospaced", 14));
        visualFlow.getChildren().add(t);
    }

    private void highlightTarget(GuiTarget target) {
        String genome = genomeArea.getText().toUpperCase().replaceAll("[^ATCG]", "");
        visualFlow.getChildren().clear();

        int s = target.getPosition() - 1;
        int e = s + 23;

        String pre = genome.substring(0, s);
        String tgt = genome.substring(s, e - 3); // Spacer
        String pam = genome.substring(e - 3, e); // PAM
        String post = genome.substring(e);

        Text t1 = new Text(pre);
        t1.setFill(Color.BLACK);

        Text t2 = new Text(tgt);
        t2.setFill(Color.BLUE);
        t2.setStyle("-fx-font-weight: bold;");

        Text t3 = new Text(pam);
        t3.setFill(Color.RED);
        t3.setStyle("-fx-font-weight: bold; -fx-underline: true;");

        Text t4 = new Text(post);
        t4.setFill(Color.BLACK);

        visualFlow.getChildren().addAll(t1, t2, t3, t4);
    }

    @Override
    protected void showError(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
