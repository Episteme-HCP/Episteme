/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.client.client.politics.geopolitics;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.episteme.server.server.tasks.economics.DistributedEconomyTask;
import org.episteme.natural.economics.growth.EconomyParameters;
import org.episteme.social.politics.loaders.WorldBankReader;
import org.episteme.server.server.tasks.politics.GeopoliticalEngineTask;
import org.episteme.natural.politics.GeopoliticalParameters;
import org.episteme.social.politics.loaders.FactbookReader;
import org.episteme.server.server.proto.*;
import org.episteme.core.ui.ThemeManager;
import org.episteme.core.mathematics.numbers.real.Real;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Demo Application for Distributed Geopolitics simulation.
 */
public class DistributedGeopoliticsApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(DistributedGeopoliticsApp.class);

    private DistributedEconomyTask economyTask;
    private GeopoliticalEngineTask politicsTask;
    private LineChart<Number, Number> gdpChart;
    private XYChart.Series<Number, Number> gdpSeries;
    private ListView<String> console;
    private Label economyLabel;
    private long step = 0;

    @Override
    public void start(Stage stage) {
        try { stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/org/episteme/core/ui/icon.png"))); } catch (Exception e) {}
        stage.setTitle(org.episteme.core.ui.i18n.I18N.getInstance().get("demo.apps.distributedgeopoliticsapp.title", "ðŸ“‰ Episteme Social Grid - Global Economics & Politics"));

        // Initialization
        double gdp = 100e12; // 100 Trillion global GDP proxy
        try {
            WorldBankReader wb = new WorldBankReader();
            gdp = wb.getGlobalGDP();
        } catch (Exception e) {
            logger.error("Failed to fetch WB data: {}", e.getMessage());
        }

        economyTask = new DistributedEconomyTask("Global Core",
                Real.of(gdp / 1e12), // Scaled capital
                EconomyParameters.standard());

        List<GeopoliticalEngineTask.NationState> nations = new ArrayList<>();
        // Load nations from FactbookReader
        FactbookReader loader = new FactbookReader();
        List<org.episteme.social.politics.Country> countries = loader.getMiniCatalog().getAll().get(0);

        for (org.episteme.social.politics.Country c : countries) {
            double stability = 0.5;
            if (c.getGovernmentType() != null && c.getGovernmentType().name().toLowerCase().contains("republic")) {
                stability = 0.8;
            }
            // Estimate military as fraction of population * GDP proxy
            double military = c.getPopulationLong() * 500.0;

            nations.add(
                    new GeopoliticalEngineTask.NationState(c.getName(), stability, military));
        }
        politicsTask = new GeopoliticalEngineTask(nations, GeopoliticalParameters.standard());

        console = new ListView<>();
        economyLabel = new Label(org.episteme.core.ui.i18n.I18N.getInstance().get("demo.apps.distributedgeopoliticsapp.economy_label", "GDP: -- | Inflation: --"));
        economyLabel.getStyleClass().add("header-label");

        Button exportBtn = new Button(org.episteme.core.ui.i18n.I18N.getInstance().get("demo.apps.distributedgeopoliticsapp.btn.export", "ðŸ“„ Export Report"));
        exportBtn.setOnAction(e -> exportReport(stage));

        // UI Setup
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("main-container");

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(org.episteme.core.ui.i18n.I18N.getInstance().get("demo.apps.distributedgeopoliticsapp.chart.time", "Time (Steps)"));
        yAxis.setLabel(org.episteme.core.ui.i18n.I18N.getInstance().get("demo.apps.distributedgeopoliticsapp.chart.gdp", "Global GDP ($T)"));

        gdpChart = new LineChart<>(xAxis, yAxis);
        gdpChart.setTitle(org.episteme.core.ui.i18n.I18N.getInstance().get("demo.apps.distributedgeopoliticsapp.chart.title", "Global Economic Growth"));
        gdpSeries = new XYChart.Series<>();
        gdpSeries.setName(org.episteme.core.ui.i18n.I18N.getInstance().get("demo.apps.distributedgeopoliticsapp.chart.series.gdp", "GDP"));
        gdpChart.getData().add(gdpSeries);

        Button stepBtn = new Button(org.episteme.core.ui.i18n.I18N.getInstance().get("demo.apps.distributedgeopoliticsapp.btn.step", "â–¶ Run Next Step"));
        stepBtn.setOnAction(e -> runStep());

        root.getChildren().addAll(economyLabel, gdpChart, new Label(org.episteme.core.ui.i18n.I18N.getInstance().get("demo.apps.distributedgeopoliticsapp.label.geopolitical_events", "Geopolitical Events")), console, stepBtn, exportBtn);

        Scene scene = new Scene(root, 1000, 800);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setScene(scene);
        stage.show();
    }

    private void runStep() {
        step++;
        economyTask.run();
        politicsTask.run();

        updateUI();
    }

    private void updateUI() {
        economyLabel.setText(String.format(org.episteme.core.ui.i18n.I18N.getInstance().get("demo.apps.distributedgeopoliticsapp.status.format", "GDP: $%.2fT | Capital: $%.2fT"),
                economyTask.getGDP().doubleValue(),
                economyTask.getCapital().doubleValue()));

        gdpSeries.getData().add(new XYChart.Data<>(step, economyTask.getGDP().doubleValue()));
        if (gdpSeries.getData().size() > 50)
            gdpSeries.getData().remove(0);

        for (GeopoliticalEngineTask.NationState n : politicsTask.getNations()) {
            console.getItems().add(0, String.format(org.episteme.core.ui.i18n.I18N.getInstance().get("demo.apps.distributedgeopoliticsapp.status.nation_format", "[%d] %s: Stability=%.2f, Military=%.0f"),
                    step, n.name, n.getStability(), n.getMilitary()));
        }
        if (console.getItems().size() > 50)
            console.getItems().remove(50, console.getItems().size());
    }

    private void exportReport(Stage stage) {
        File file = org.episteme.client.client.util.FileHelper.showSaveDialog(stage, org.episteme.core.ui.i18n.I18N.getInstance().get("demo.apps.distributedgeopoliticsapp.file.export_report", "Export Report"), org.episteme.core.ui.i18n.I18N.getInstance().get("demo.apps.distributedgeopoliticsapp.file.csv", "CSV Files"), "*.csv");
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("Nation,Stability,MilitaryPower");
                for (GeopoliticalEngineTask.NationState n : politicsTask.getNations()) {
                    writer.printf("%s,%.4f,%.2f\n", n.name, n.getStability(), n.getMilitary());
                }
            } catch (Exception e) {
                logger.error("Export failed", e);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
