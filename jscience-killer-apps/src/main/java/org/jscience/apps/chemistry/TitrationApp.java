/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.chemistry;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.jscience.apps.framework.ChartFactory;
import org.jscience.apps.framework.KillerAppBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Virtual Titration Lab.
 * <p>
 * Simulates an acid-base titration experiment (e.g., HCl with NaOH).
 * Features a dynamic pH curve and color indicator simulation.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TitrationApp extends KillerAppBase {

    // Simulation State
    private double volumeAcid = 50.0; // mL
    private double concAcid = 0.1; // M
    private double concBase = 0.1; // M
    private double volumeBaseAdded = 0.0; // mL
    private double maxBuretteVol = 50.0; // mL
    
    private boolean valveOpen = false;
    private double flowRate = 0.0; // mL per tick
    
    // UI
    private Canvas labCanvas;
    private XYChart.Series<Number, Number> phSeries;
    private Label phLabel;
    private Label volumeLabel;
    private Slider valveSlider;
    private Color indicatorColor = Color.TRANSPARENT;
    
    // Constants
    private static final double KW = 1.0e-14;

    @Override
    protected String getAppTitle() {
        return i18n.get("titration.title") + " - JScience";
    }

    @Override
    protected Region createMainContent() {
        SplitPane split = new SplitPane();
        split.setOrientation(Orientation.HORIZONTAL);
        
        // Left: Lab Equipment Visualization
        VBox labPane = createLabPane();
        
        // Right: Data & Controls
        VBox dataPane = createDataPane();
        
        split.getItems().addAll(labPane, dataPane);
        split.setDividerPositions(0.4);
        
        startSimulation();
        return split;
    }

    private VBox createLabPane() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #ddd;");
        
        labCanvas = new Canvas(300, 600);
        drawLab();
        
        box.getChildren().add(labCanvas);
        VBox.setVgrow(labCanvas, Priority.ALWAYS);
        return box;
    }

    private VBox createDataPane() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        
        // Chart
        LineChart<Number, Number> phChart = ChartFactory.createLineChart("Titration Curve", "Volume Base (mL)", "pH");
        phSeries = new XYChart.Series<>();
        phSeries.setName("pH");
        phChart.getData().add(phSeries);
        
        // Status
        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        
        phLabel = new Label("pH: 1.00");
        phLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        volumeLabel = new Label("Vol Added: 0.00 mL");
        
        valveSlider = new Slider(0, 1.0, 0);
        valveSlider.setShowTickLabels(true);
        valveSlider.setShowTickMarks(true);
        valveSlider.valueProperty().addListener((o, ov, nv) -> {
            flowRate = nv.doubleValue() * 0.2; // Max 0.2 mL/frame
            valveOpen = flowRate > 0;
        });
        
        Button resetButton = new Button("Reset Experiment");
        resetButton.setOnAction(e -> reset());
        
        box.getChildren().addAll(
            new Label("🧪 Titration Data"),
            phChart,
            phLabel, volumeLabel,
            new Separator(),
            new Label("🚰 Burette Valve Control"),
            valveSlider,
            resetButton
        );
        VBox.setVgrow(phChart, Priority.ALWAYS);
        return box;
    }

    private void startSimulation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                drawLab();
            }
        };
        timer.start();
    }
    
    private void update() {
        if (valveOpen && volumeBaseAdded < maxBuretteVol) {
            volumeBaseAdded += flowRate;
            if (volumeBaseAdded > maxBuretteVol) volumeBaseAdded = maxBuretteVol;
            
            calculatePH();
        }
    }
    
    private void calculatePH() {
        // Strong Acid (HCl) + Strong Base (NaOH) approximation
        // Moles Acid initial
        double molesA = volumeAcid / 1000.0 * concAcid;
        double molesB = volumeBaseAdded / 1000.0 * concBase;
        double totalVolL = (volumeAcid + volumeBaseAdded) / 1000.0;
        
        double ph;
        if (molesA > molesB) {
            // Excess Acid
            double concH = (molesA - molesB) / totalVolL;
            ph = -Math.log10(concH);
        } else if (molesB > molesA) {
            // Excess Base
            double concOH = (molesB - molesA) / totalVolL;
            double pOH = -Math.log10(concOH);
            ph = 14.0 - pOH;
        } else {
            // Neutral
            ph = 7.0;
        }
        
        // Phenolphthalein Indicator: Colorless < 8.2, Pink > 8.2, Red/Purple > 10
        if (ph < 8.2) {
             indicatorColor = Color.web("#ffffff", 0.3); // Clear/Watery
        } else if (ph < 10.0) {
             // Gradient Pink
             double intensity = (ph - 8.2) / (10.0 - 8.2);
             indicatorColor = Color.HOTPINK.deriveColor(0, 1, 1, 0.3 + 0.5 * intensity);
        } else {
             indicatorColor = Color.DEEPPINK.deriveColor(0, 1, 1, 0.8);
        }
        
        // Update UI
        phLabel.setText(String.format("pH: %.2f", ph));
        volumeLabel.setText(String.format("Vol Added: %.2f mL", volumeBaseAdded));
        
        // Chart sampling (don't flood chart)
        if (phSeries.getData().isEmpty() || 
            Math.abs(phSeries.getData().get(phSeries.getData().size()-1).getXValue().doubleValue() - volumeBaseAdded) > 0.5) {
            phSeries.getData().add(new XYChart.Data<>(volumeBaseAdded, ph));
        }
    }
    
    private void drawLab() {
        GraphicsContext gc = labCanvas.getGraphicsContext2D();
        double w = labCanvas.getWidth();
        double h = labCanvas.getHeight();
        
        gc.clearRect(0, 0, w, h);
        
        double centerX = w/2;
        
        // Stand
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(centerX - 60, h - 20, 120, 20); // Base
        gc.fillRect(centerX - 50, 50, 10, h - 70); // Rod
        gc.fillRect(centerX - 50, 100, 60, 5); // Clamp
        
        // Burette
        double burX = centerX + 10;
        double burY = 50;
        double burW = 20;
        double burH = 300;
        
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(burX, burY, burW, burH);
        
        // Liquid in Burette
        double liquidH = burH * ((maxBuretteVol - volumeBaseAdded) / maxBuretteVol);
        gc.setFill(Color.web("#ccf", 0.5));
        gc.fillRect(burX, burY + (burH - liquidH), burW, liquidH);
        
        // Beaker
        double beakW = 80;
        double beakH = 100;
        double beakX = centerX - 20; // Under burette tip roughly
        double beakY = h - 130;
        
        gc.setStroke(Color.BLACK);
        gc.strokePolyline(new double[]{beakX, beakX, beakX+beakW, beakX+beakW}, 
                          new double[]{beakY, beakY+beakH, beakY+beakH, beakY}, 4);
                          
        // Liquid in Beaker
        // Volume simulates liquid height
        double baseLiquidHeight = 40; // Initial
        double addedHeight = (volumeBaseAdded / volumeAcid) * 20; 
        double totalH = baseLiquidHeight + addedHeight;
        
        gc.setFill(indicatorColor);
        gc.fillRect(beakX+1, beakY+beakH - totalH, beakW-2, totalH);
        
        // Drops
        if (valveOpen) {
            gc.setFill(Color.AQUA);
            gc.fillOval(burX + 5, burY + burH + (System.currentTimeMillis() % 200 / 200.0) * 50, 6, 6);
        }
    }
    
    private void reset() {
        volumeBaseAdded = 0;
        phSeries.getData().clear();
        calculatePH();
        valveSlider.setValue(0);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
