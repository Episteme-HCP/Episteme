/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
import org.jscience.ui.i18n.I18n;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Enhanced Genetics Viewer.
 * Features: Population Genetics (Drift Simulation) and Mendelian Inheritance.
 *
 * @author Silvere Martin-Michiellot
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
    public String getName() { return I18n.getInstance().get("viewer.genetics", "Genetics"); }
    
    @Override
    public String getCategory() { return "Biology"; }

    public GeneticsViewer() {
        initUI();
    }

    private void initUI() {
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("dark-tab-pane");

        Tab driftTab = new Tab(I18n.getInstance().get("genetics.tab.drift", "Genetic Drift"));
        driftTab.setContent(createDriftTab());
        driftTab.setClosable(false);

        Tab mendelTab = new Tab(I18n.getInstance().get("genetics.tab.mendel", "Mendelian"));
        mendelTab.setContent(createMendelTab());
        mendelTab.setClosable(false);

        tabPane.getTabs().addAll(driftTab, mendelTab);
        this.setCenter(tabPane);
    }

    private BorderPane createDriftTab() {
        driftCanvas = new Canvas(700, 400);
        history = new double[generations];

        Button runBtn = new Button(I18n.getInstance().get("genetics.run", "Run Simulation"));
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
                new Label(I18n.getInstance().get("genetics.popsize", "Pop Size")), popSpinner,
                new Label(I18n.getInstance().get("genetics.generations", "Generations")), genSpinner,
                new Label(I18n.getInstance().get("genetics.initialfreq", "Initial Freq")), freqSlider,
                runBtn);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER_LEFT);

        VBox infoPanel = new VBox(10);
        infoPanel.setPadding(new Insets(10));
        infoPanel.getStyleClass().add("dark-viewer-sidebar");
        infoPanel.setPrefWidth(200);

        Label titleLabel = new Label(I18n.getInstance().get("genetics.subtitle", "Genetic Drift"));
        titleLabel.getStyleClass().add("dark-header");

        Label explanationLabel = new Label(I18n.getInstance().get("genetics.explanation", "Simulates allele frequency changes in a population over generations due to random sampling."));
        explanationLabel.setWrapText(true);
        explanationLabel.setStyle("-fx-font-size: 11px;");

        driftStatusLabel = new Label(I18n.getInstance().get("genetics.status.start", "Click Run to start"));
        driftStatusLabel.setStyle("-fx-font-style: italic;");

        infoPanel.getChildren().addAll(titleLabel, new Separator(), explanationLabel, new Separator(), driftStatusLabel);

        BorderPane root = new BorderPane();
        root.setCenter(driftCanvas);
        root.setBottom(controls);
        root.setRight(infoPanel);
        root.getStyleClass().add("dark-viewer-root");

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

        String outcome = freq >= 0.99 ? I18n.getInstance().get("genetics.outcome.fixed", "Fixed")
                : freq <= 0.01 ? I18n.getInstance().get("genetics.outcome.lost", "Lost") : String.format("%.3f", freq);
        driftStatusLabel.setText(String.format(I18n.getInstance().get("genetics.frequency", "Final Freq: %s"), outcome));

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
        gc.fillText(I18n.getInstance().get("genetics.axis.generation", "Generation"), 350, 385);
        gc.fillText(I18n.getInstance().get("genetics.axis.allele", "Allele"), 5, 180);
        gc.fillText(I18n.getInstance().get("genetics.axis.frequency", "Frequency"), 5, 195);

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
        inputPanel.getStyleClass().add("dark-viewer-sidebar");
        inputPanel.setAlignment(Pos.TOP_LEFT);

        Label title = new Label(I18n.getInstance().get("genetics.tab.mendel", "Mendelian Inheritance"));
        title.getStyleClass().add("dark-header");

        parent1Field = new TextField("Aa");
        parent2Field = new TextField("Aa");

        Button calcBtn = new Button(I18n.getInstance().get("genetics.mendel.calculate", "Calculate"));
        calcBtn.setMaxWidth(Double.MAX_VALUE);
        calcBtn.setOnAction(e -> calculatePunnettSquare());

        mendelResultsLabel = new Label();
        mendelResultsLabel.setWrapText(true);
        mendelResultsLabel.setStyle("-fx-font-size: 13px;");

        inputPanel.getChildren().addAll(
                title, new Separator(),
                new Label(I18n.getInstance().get("genetics.mendel.parent1", "Parent 1 Genotype")), parent1Field,
                new Label(I18n.getInstance().get("genetics.mendel.parent2", "Parent 2 Genotype")), parent2Field,
                new Separator(), calcBtn, new Separator(), mendelResultsLabel);

        BorderPane root = new BorderPane();
        root.setLeft(inputPanel);
        root.setCenter(mendelCanvas);
        root.getStyleClass().add("dark-viewer-root");

        calculatePunnettSquare();
        return root;
    }

    private void calculatePunnettSquare() {
        String p1 = parent1Field.getText().trim();
        String p2 = parent2Field.getText().trim();

        if (p1.length() != 2 || p2.length() != 2) {
            mendelResultsLabel.setText(I18n.getInstance().get("genetics.mendel.error", "Enter 2-character genotypes"));
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

        StringBuilder results = new StringBuilder(I18n.getInstance().get("genetics.mendel.ratios", "Ratios:") + "\n");
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
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));

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
}
