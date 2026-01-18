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

package org.jscience.ui.viewers.biology.genetics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jscience.ui.AbstractViewer;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.jscience.biology.loaders.FASTAReader;
import org.jscience.biology.loaders.UniProtReader;

/**
 * Enhanced Genetics Viewer.
 * Features: Population Genetics (Drift Simulation) and Mendelian Inheritance.
 *
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Holland, J. H. (1975). <i>Adaptation in Natural and Artificial Systems</i>. University of Michigan Press.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeneticsViewer extends AbstractViewer {

    private int popSize = 100;
    private int generations = 200;
    private double initialFreq = 0.5;
    private double[] history;
    private Canvas driftCanvas;
    private Label driftStatusLabel;

    private Canvas mendelCanvas;
    private TextField parent1Field;
    private TextField parent2Field;
    private Label mendelResultsLabel;

    @Override
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.geneticsviewer.name", "Genetics Viewer"); }
    
    @Override
    public String getCategory() { return org.jscience.ui.i18n.I18n.getInstance().get("category.biology", "Biology"); }

    public GeneticsViewer() {
        initUI();
    }

    private void initUI() {
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("demo-tab-pane");

        Tab driftTab = new Tab(org.jscience.ui.i18n.I18n.getInstance().get("genetics.tab.drift", "Genetic Drift"));
        driftTab.setContent(createDriftTab());
        driftTab.setClosable(false);

        Tab sequenceTab = new Tab(org.jscience.ui.i18n.I18n.getInstance().get("genetics.tab.sequence", "Sequence Browser"));
        sequenceTab.setContent(createSequenceTab());
        sequenceTab.setClosable(false);
        Tab mendelTab = new Tab(org.jscience.ui.i18n.I18n.getInstance().get("genetics.tab.mendel", "Mendelian Inheritance"));
        mendelTab.setContent(createMendelTab());
        mendelTab.setClosable(false);

        tabPane.getTabs().addAll(driftTab, mendelTab, sequenceTab);
        this.setCenter(tabPane);
    }

    private BorderPane createDriftTab() {
        driftCanvas = new Canvas(700, 400);
        history = new double[generations];

        Button runBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("genetics.run", "Run Simulation"));
        runBtn.setOnAction(e -> runDriftSimulation());

        Spinner<Integer> popSpinner = new Spinner<>(10, 1000, 100, 10);
        popSpinner.setEditable(true);
        popSpinner.valueProperty().addListener((o, old, val) -> popSize = val);

        Spinner<Integer> genSpinner = new Spinner<>(50, 500, 200, 50);
        genSpinner.setEditable(true);
        genSpinner.valueProperty().addListener((o, old, val) -> {
            generations = val;
            history = new double[generations];
        });

        Slider freqSlider = new Slider(0.01, 0.99, 0.5);
        freqSlider.setShowTickLabels(true);
        freqSlider.setMajorTickUnit(0.25);
        freqSlider.valueProperty().addListener((o, old, val) -> initialFreq = val.doubleValue());

        HBox controls = new HBox(15,
                new Label(org.jscience.ui.i18n.I18n.getInstance().get("genetics.popsize", "Pop Size")), popSpinner,
                new Label(org.jscience.ui.i18n.I18n.getInstance().get("genetics.generations", "Generations")), genSpinner,
                new Label(org.jscience.ui.i18n.I18n.getInstance().get("genetics.initialfreq", "Initial Freq")), freqSlider,
                runBtn);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER_LEFT);

        VBox infoPanel = new VBox(10);
        infoPanel.setPadding(new Insets(10));
        infoPanel.getStyleClass().add("viewer-sidebar");
        infoPanel.setPrefWidth(200);

        Label titleLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("genetics.subtitle", "Genetic Drift"));
        titleLabel.getStyleClass().add("header-label");

        Label explanationLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("genetics.explanation", "Simulates allele frequency changes in a population over generations due to random sampling."));
        explanationLabel.setWrapText(true);
        explanationLabel.setStyle("-fx-font-size: 11px;");

        driftStatusLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("genetics.status.start", "Click Run to start"));
        driftStatusLabel.getStyleClass().add("font-italic"); // Replaced inline style: -fx-font-style: italic;

        infoPanel.getChildren().addAll(titleLabel, new Separator(), explanationLabel, new Separator(), driftStatusLabel);

        BorderPane root = new BorderPane();
        root.setCenter(driftCanvas);
        root.setBottom(controls);
        root.setRight(infoPanel);
        root.getStyleClass().add("viewer-root");

        drawDriftAxes();
        return root;
    }

    private void runDriftSimulation() {
        Random rand = new Random();
        double freq = initialFreq;

        for (int gen = 0; gen < generations; gen++) {
            history[gen] = freq;
            int count = 0;
            for (int i = 0; i < popSize; i++) {
                if (rand.nextDouble() < freq) count++;
            }
            freq = (double) count / popSize;
        }

        String outcome = freq >= 0.99 ? org.jscience.ui.i18n.I18n.getInstance().get("genetics.outcome.fixed", "Fixed")
                : freq <= 0.01 ? org.jscience.ui.i18n.I18n.getInstance().get("genetics.outcome.lost", "Lost") : String.format("%.3f", freq);
        driftStatusLabel.setText(String.format(org.jscience.ui.i18n.I18n.getInstance().get("genetics.frequency", "Final Freq: %s"), outcome));

        drawDriftAxes();
        drawDriftHistory();
    }

    private void drawDriftAxes() {
        GraphicsContext gc = driftCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, driftCanvas.getWidth(), driftCanvas.getHeight());

        gc.setStroke(Color.web("#444444"));
        gc.setLineWidth(1.5);
        gc.strokeLine(50, 350, 680, 350);
        gc.strokeLine(50, 50, 50, 350);

        gc.setFill(Color.web("#222222"));
        gc.fillText(org.jscience.ui.i18n.I18n.getInstance().get("genetics.axis.generation", "Generation"), 350, 385);
        gc.fillText(org.jscience.ui.i18n.I18n.getInstance().get("genetics.axis.allele", "Allele"), 5, 180);
        gc.fillText(org.jscience.ui.i18n.I18n.getInstance().get("genetics.axis.frequency", "Frequency"), 5, 195);

        gc.setStroke(Color.web("#888888"));
        gc.setLineWidth(0.5);
        for (double f = 0; f <= 1.0; f += 0.25) {
            double y = 350 - (f * 300);
            gc.strokeLine(45, y, 680, y);
            gc.setFill(Color.web("#333333"));
            gc.fillText(String.format("%.2f", f), 20, y + 4);
        }
    }

    private void drawDriftHistory() {
        GraphicsContext gc = driftCanvas.getGraphicsContext2D();
        gc.setStroke(Color.web("#00d9ff"));
        gc.setLineWidth(2);
        gc.beginPath();
        for (int i = 0; i < generations; i++) {
            double x = 50 + (i * 630.0 / generations);
            double y = 350 - (history[i] * 300);
            if (i == 0) gc.moveTo(x, y);
            else gc.lineTo(x, y);
        }
        gc.stroke();
    }

    private BorderPane createMendelTab() {
        mendelCanvas = new Canvas(600, 400);

        VBox inputPanel = new VBox(15);
        inputPanel.setPadding(new Insets(20));
        inputPanel.setPrefWidth(300);
        inputPanel.getStyleClass().add("viewer-sidebar");
        inputPanel.setAlignment(Pos.TOP_LEFT);

        Label title = new Label(org.jscience.ui.i18n.I18n.getInstance().get("genetics.tab.mendel", "Mendelian Inheritance"));
        title.getStyleClass().add("header-label");

        parent1Field = new TextField("Aa");
        parent2Field = new TextField("Aa");

        Button calcBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("genetics.mendel.calculate", "Calculate"));
        calcBtn.setMaxWidth(Double.MAX_VALUE);
        calcBtn.setOnAction(e -> calculatePunnettSquare());

        mendelResultsLabel = new Label();
        mendelResultsLabel.setWrapText(true);
        mendelResultsLabel.setStyle("-fx-font-size: 13px;");

        inputPanel.getChildren().addAll(
                title, new Separator(),
                new Label(org.jscience.ui.i18n.I18n.getInstance().get("genetics.mendel.parent1", "Parent 1 Genotype")), parent1Field,
                new Label(org.jscience.ui.i18n.I18n.getInstance().get("genetics.mendel.parent2", "Parent 2 Genotype")), parent2Field,
                new Separator(), calcBtn, new Separator(), mendelResultsLabel);

        BorderPane root = new BorderPane();
        root.setLeft(inputPanel);
        root.setCenter(mendelCanvas);
        root.getStyleClass().add("viewer-root");

        calculatePunnettSquare();
        return root;
    }

    private void calculatePunnettSquare() {
        String p1 = parent1Field.getText().trim();
        String p2 = parent2Field.getText().trim();

        if (p1.length() != 2 || p2.length() != 2) {
            mendelResultsLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("genetics.mendel.error", "Enter 2-character genotypes"));
            return;
        }

        char[] g1 = p1.toCharArray();
        char[] g2 = p2.toCharArray();

        String[] offspring = new String[4];
        offspring[0] = sortAlleles(g1[0], g2[0]);
        offspring[1] = sortAlleles(g1[0], g2[1]);
        offspring[2] = sortAlleles(g1[1], g2[0]);
        offspring[3] = sortAlleles(g1[1], g2[1]);

        Map<String, Integer> counts = new HashMap<>();
        for (String s : offspring) {
            counts.put(s, counts.getOrDefault(s, 0) + 1);
        }

        StringBuilder results = new StringBuilder(org.jscience.ui.i18n.I18n.getInstance().get("genetics.mendel.ratios", "Ratios:") + "\n");
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            double percent = (entry.getValue() / 4.0) * 100;
            results.append(String.format("  %s : %.0f%%\n", entry.getKey(), percent));
        }

        mendelResultsLabel.setText(results.toString());
        drawPunnettSquare(g1, g2, offspring);
    }

    private String sortAlleles(char a, char b) {
        if (Character.isUpperCase(a) && Character.isLowerCase(b)) return "" + a + b;
        if (Character.isLowerCase(a) && Character.isUpperCase(b)) return "" + b + a;
        return (a < b) ? "" + a + b : "" + b + a;
    }

    private void drawPunnettSquare(char[] p1, char[] p2, String[] offspring) {
        GraphicsContext gc = mendelCanvas.getGraphicsContext2D();
        double w = mendelCanvas.getWidth();
        double h = mendelCanvas.getHeight();
        gc.clearRect(0, 0, w, h);

        double size = 200;
        double startX = (w - size) / 2;
        double startY = (h - size) / 2;

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("System", FontWeight.BOLD, 24));

        gc.strokeRect(startX, startY, size, size);
        gc.strokeLine(startX + size / 2, startY, startX + size / 2, startY + size);
        gc.strokeLine(startX, startY + size / 2, startX + size, startY + size / 2);

        gc.setFill(Color.BLACK);
        gc.fillText("" + p1[0], startX + size / 4 - 8, startY - 20);
        gc.fillText("" + p1[1], startX + 3 * size / 4 - 8, startY - 20);
        gc.fillText("" + p2[0], startX - 30, startY + size / 4 + 8);
        gc.fillText("" + p2[1], startX - 30, startY + 3 * size / 4 + 8);

        gc.setFill(Color.BLUE);
        gc.fillText(offspring[0], startX + size / 4 - 15, startY + size / 4 + 8);
        gc.fillText(offspring[1], startX + 3 * size / 4 - 15, startY + size / 4 + 8);
        gc.fillText(offspring[2], startX + size / 4 - 15, startY + 3 * size / 4 + 8);
        gc.fillText(offspring[3], startX + 3 * size / 4 - 15, startY + 3 * size / 4 + 8);
    }

    private BorderPane createSequenceTab() {
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(300);
        sidebar.getStyleClass().add("viewer-sidebar");

        Label title = new Label(org.jscience.ui.i18n.I18n.getInstance().get("genetics.sequence.browser", "Sequence Browser"));
        title.getStyleClass().add("header-label");

        TextField accessionField = new TextField("P01308"); // Insulin
        Button queryBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("genetics.uniprot.query", "Query UniProt"));
        queryBtn.setMaxWidth(Double.MAX_VALUE);

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setFont(Font.font("Monospaced", 12));

        Label statusLabel = new Label();
        statusLabel.getStyleClass().add("description-label");

        queryBtn.setOnAction(e -> {
            String acc = accessionField.getText().trim();
            statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("genetics.querying", "Querying..."));
            new Thread(() -> {
                Map<String, String> data = UniProtReader.fetchByAccession(acc);
                javafx.application.Platform.runLater(() -> {
                    if (data != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Accession: ").append(data.get("accession")).append("\n");
                        sb.append("Protein: ").append(data.getOrDefault("protein_name", "N/A")).append("\n");
                        sb.append("Organism: ").append(data.getOrDefault("organism", "N/A")).append("\n");
                        sb.append("----------------\n");
                        sb.append(data.get("raw_json"));
                        resultArea.setText(sb.toString());
                        statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("genetics.success", "Success!"));
                    } else {
                        resultArea.setText(org.jscience.ui.i18n.I18n.getInstance().get("genetics.error.accession", "Accession not found."));
                        statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("genetics.error", "Error"));
                    }
                });
            }).start();
        });

        Button loadFastaBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("genetics.fasta.load", "Load FASTA"));
        loadFastaBtn.setMaxWidth(Double.MAX_VALUE);
        loadFastaBtn.setOnAction(e -> {
            javafx.stage.FileChooser chooser = new javafx.stage.FileChooser();
            chooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("FASTA", "*.fasta", "*.fa"));
            java.io.File file = chooser.showOpenDialog(getScene().getWindow());
            if (file != null) {
                try {
                    java.util.List<FASTAReader.Sequence> seqs = FASTAReader.load(new java.io.FileInputStream(file));
                    StringBuilder sb = new StringBuilder();
                    for (FASTAReader.Sequence s : seqs) {
                        sb.append(">").append(s.header).append("\n");
                        sb.append(s.data).append("\n\n");
                    }
                    resultArea.setText(sb.toString());
                    statusLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("genetics.loaded", "Loaded ") + seqs.size() + " sequences");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        sidebar.getChildren().addAll(
                title, new Separator(),
                new Label(org.jscience.ui.i18n.I18n.getInstance().get("genetics.uniprot.id", "UniProt ID / Accession")), accessionField,
                queryBtn, new Separator(),
                loadFastaBtn, new Separator(),
                statusLabel);

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(resultArea);
        root.getStyleClass().add("viewer-root");
        return root;
    }

    @Override public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.geneticsviewer.desc", "Unified viewer for population genetics and Mendelian inheritance."); }
    @Override public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.geneticsviewer.longdesc", "Features Wright-Fisher model simulations for genetic drift and Punnett square calculations for Mendelian inheritance patterns. Includes tools for UniProt accession queries and FASTA file browsing."); }
    @Override public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() { return new java.util.ArrayList<>(); }
}
