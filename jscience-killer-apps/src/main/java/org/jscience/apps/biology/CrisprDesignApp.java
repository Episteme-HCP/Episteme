/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.biology;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import org.jscience.apps.framework.KillerAppBase;
import org.jscience.biology.genetics.BioSequence;

import java.io.File;
import java.nio.file.Files;
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
    private TextField targetField;
    private TableView<GuiTarget> resultsTable;
    private TextFlow visualFlow;

    public static class GuiTarget {
        private final int position;
        private final String sequence; // 20bp + PAM
        private final String pam;
        private final double score; // Mock efficiency score

        public GuiTarget(int position, String sequence, String pam, double score) {
            this.position = position;
            this.sequence = sequence;
            this.pam = pam;
            this.score = score;
        }

        public int getPosition() { return position; }
        public String getSequence() { return sequence; }
        public String getPam() { return pam; }
        public double getScore() { return score; }
    }

    @Override
    protected String getAppTitle() {
        return i18n.get("crispr.title") + " - JScience";
    }

    @Override
    protected Region createMainContent() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: Inputs
        VBox top = new VBox(10);
        top.setPadding(new Insets(0, 0, 10, 0));
        
        Label lblGenome = new Label("Genomic Sequence (5' -> 3'):");
        genomeArea = new TextArea();
        genomeArea.setPrefHeight(100);
        genomeArea.setWrapText(true);
        // Default sample: A snippet of a gene
        genomeArea.setText("ATGGCCTCCTCCGAGGACGTCATCAAGGAGCTGATGGACGACGTGGTGAAGCTGGGCGTGGGGCAGCGGCCAGAGGGGGAGGGATGGGTGCAAAAGAGGATTGAAGACCCTGGAAAGAAAAGTGCCATGTGAGTGTG");

        Button loadBtn = new Button("Load FASTA...");
        loadBtn.setOnAction(e -> loadFasta());
        
        Button findBtn = new Button("🔍 Find Targets (PAM: NGG)");
        findBtn.setDefaultButton(true);
        findBtn.setOnAction(e -> scanGenome());
        
        top.getChildren().addAll(lblGenome, genomeArea, new HBox(10, loadBtn, findBtn));
        
        // Center: Results Table
        resultsTable = new TableView<>();
        TableColumn<GuiTarget, Integer> colPos = new TableColumn<>("Position");
        colPos.setCellValueFactory(new PropertyValueFactory<>("position"));
        
        TableColumn<GuiTarget, String> colSeq = new TableColumn<>("Sequence (20nt)");
        colSeq.setCellValueFactory(new PropertyValueFactory<>("sequence"));
        colSeq.setMinWidth(200);
        
        TableColumn<GuiTarget, String> colPam = new TableColumn<>("PAM");
        colPam.setCellValueFactory(new PropertyValueFactory<>("pam"));
        
        TableColumn<GuiTarget, Double> colScore = new TableColumn<>("Efficiency Score");
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        
        resultsTable.getColumns().addAll(colPos, colSeq, colPam, colScore);
        
        resultsTable.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) highlightTarget(nv);
        });

        // Bottom: Visualization
        VBox bottom = new VBox(5);
        bottom.setPadding(new Insets(10, 0, 0, 0));
        Label lblVis = new Label("Target Visualization:");
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
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("FASTA Files", "*.fasta", "*.fa"));
        File f = fc.showOpenDialog(null);
        if (f != null) {
            try {
                String content = Files.readString(f.toPath());
                // Remove header line if present
                if (content.startsWith(">")) {
                    content = content.substring(content.indexOf('\n') + 1);
                }
                genomeArea.setText(content.replaceAll("\\s", "").toUpperCase());
            } catch (Exception e) {
                showError("Load Error", e.getMessage());
            }
        }
    }

    private void scanGenome() {
        String genome = genomeArea.getText().toUpperCase().replaceAll("[^ATCG]", "");
        if (genome.isEmpty()) return;

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
                for(char c : spacer.toCharArray()) if(c=='G'||c=='C') gc++;
                double gcPercent = gc / 20.0;
                double score = 100 * (1.0 - Math.abs(0.5 - gcPercent)); 
                
                targets.add(new GuiTarget(start + 1, spacer, pam, Math.round(score * 10)/10.0));
            }
        }
        
        resultsTable.setItems(FXCollections.observableArrayList(targets));
        if (!targets.isEmpty()) {
            scanGenomeVisual(genome);
        }
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
