/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.social;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import org.jscience.apps.framework.ChartFactory;
import org.jscience.apps.framework.KillerAppBase;

/**
 * Civilization Collapse Model.
 * <p>
 * A simplified System Dynamics model (Limits to Growth style) simulating
 * the interaction between Population, Resources, and Pollution.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CivilizationApp extends KillerAppBase {

    // Model State (Stocks)
    private double population = 1000.0;
    private double resources = 100000.0;
    private double pollution = 0.0;
    
    // Parameters
    private double birthRateBase = 0.05;
    private double consumptionPerCapita = 1.0;
    private double pollutionFactor = 0.1;
    private double innovationRate = 0.0; // Reduces consumption/pollution
    
    private double time = 0;
    private boolean running = false;
    
    // UI
    private XYChart.Series<Number, Number> popSeries;
    private XYChart.Series<Number, Number> resSeries;
    private XYChart.Series<Number, Number> polSeries;
    private Label statusLabel;
    
    @Override
    protected String getAppTitle() {
        return i18n.get("civ.title") + " - JScience";
    }

    @Override
    protected Region createMainContent() {
        BorderPane root = new BorderPane();
        
        // Charts
        LineChart<Number, Number> chart = createChart();
        root.setCenter(chart);
        
        // Controls
        VBox controls = createControls();
        root.setBottom(controls);
        
        // Status Overlay
        statusLabel = new Label("STABLE");
        statusLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: green;");
        StackPane overlay = new StackPane(statusLabel);
        overlay.setPickOnBounds(false);
        overlay.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        overlay.setPadding(new Insets(20));
        
        StackPane mainStack = new StackPane(root, overlay);
        
        startLoop();
        
        return mainStack;
    }
    
    private LineChart<Number, Number> createChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Years");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value (Scaled)");
        
        LineChart<Number, Number> lc = new LineChart<>(xAxis, yAxis);
        lc.setTitle("Civilization Dynamics");
        lc.setCreateSymbols(false);
        lc.setAnimated(false);
        
        popSeries = new XYChart.Series<>(); popSeries.setName("Population");
        resSeries = new XYChart.Series<>(); resSeries.setName("Resources");
        polSeries = new XYChart.Series<>(); polSeries.setName("Pollution");
        
        lc.getData().addAll(popSeries, resSeries, polSeries);
        return lc;
    }
    
    private VBox createControls() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #eee;");
        
        HBox sliders = new HBox(20);
        
        sliders.getChildren().add(createSlider("Consumption Rate", 0.5, 5.0, 1.0, v -> consumptionPerCapita = v));
        sliders.getChildren().add(createSlider("Birth Rate", 0.0, 0.2, 0.05, v -> birthRateBase = v));
        sliders.getChildren().add(createSlider("Tech Innovation", 0.0, 0.05, 0.0, v -> innovationRate = v));
        
        HBox buttons = new HBox(10);
        Button btnReset = new Button("Reset Simulation");
        btnReset.setOnAction(e -> reset());
        
        ToggleButton btnPause = new ToggleButton("Run / Pause");
        btnPause.setSelected(false);
        btnPause.selectedProperty().addListener((o, ov, nv) -> running = nv);
        
        buttons.getChildren().addAll(btnReset, btnPause);
        
        box.getChildren().addAll(new Label("Simulation Parameters"), sliders, buttons);
        return box;
    }
    
    private VBox createSlider(String name, double min, double max, double val, java.util.function.DoubleConsumer consumer) {
        VBox vb = new VBox(5);
        Label l = new Label(name);
        Slider s = new Slider(min, max, val);
        s.setShowTickLabels(true);
        s.setShowTickMarks(true);
        s.valueProperty().addListener((o, ov, nv) -> consumer.accept(nv.doubleValue()));
        vb.getChildren().addAll(l, s);
        return vb;
    }
    
    private void startLoop() {
        AnimationTimer timer = new AnimationTimer() {
            private long last = 0;
            @Override
            public void handle(long now) {
                if (now - last > 50_000_000) { // 20 FPS
                    if (running && population > 0) {
                        step();
                    }
                    last = now;
                }
            }
        };
        timer.start();
    }
    
    private void step() {
        time += 1.0;
        
        // 1. Calculations
        double efficiency = 1.0 + (innovationRate * time); // Tech improves efficiency
        double effectiveConsumption = consumptionPerCapita / efficiency;
        
        // Resource Scarcity Factor
        double scarcity = Math.max(0, resources) / 10000.0;
        if (scarcity > 1.0) scarcity = 1.0;
        
        // Pollution Impact
        double pollutionDeath = pollution * 0.001;
        
        // Dynamics
        // Birth Rate decays with wealth (demographic transition) but drops with scarcity
        double birthRate = birthRateBase * scarcity; 
        
        // Death Rate rises with pollution and starvation
        double deathRate = 0.02 + pollutionDeath + (1.0 - scarcity) * 0.1;
        
        double dPop = population * (birthRate - deathRate);
        double dRes = -(population * effectiveConsumption);
        double dPol = (population * effectiveConsumption * 0.5) - (pollution * 0.05); // Generation - Decay
        
        // Update
        population += dPop;
        resources += dRes;
        pollution += dPol;
        
        if (population < 0) population = 0;
        if (resources < 0) resources = 0;
        if (pollution < 0) pollution = 0;
        
        // Update Chart (downsample)
        if (time % 5 == 0) {
            popSeries.getData().add(new XYChart.Data<>(time, population));
            resSeries.getData().add(new XYChart.Data<>(time, resources));
            polSeries.getData().add(new XYChart.Data<>(time, pollution));
            
            // Limit history
            if (popSeries.getData().size() > 200) {
                popSeries.getData().remove(0);
                resSeries.getData().remove(0);
                polSeries.getData().remove(0);
            }
        }
        
        updateStatus(dPop);
    }
    
    private void updateStatus(double dPop) {
        if (population <= 10) {
            statusLabel.setText("EXTINCT");
            statusLabel.setTextFill(Color.BLACK);
        } else if (dPop < -10) {
            statusLabel.setText("COLLAPSE");
            statusLabel.setTextFill(Color.RED);
        } else if (dPop < 0) {
            statusLabel.setText("DECLINING");
            statusLabel.setTextFill(Color.ORANGE);
        } else {
            statusLabel.setText("THRIVING");
            statusLabel.setTextFill(Color.GREEN);
        }
    }
    
    private void reset() {
        population = 1000;
        resources = 100000;
        pollution = 0;
        time = 0;
        popSeries.getData().clear();
        resSeries.getData().clear();
        polSeries.getData().clear();
        statusLabel.setText("STABLE");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
