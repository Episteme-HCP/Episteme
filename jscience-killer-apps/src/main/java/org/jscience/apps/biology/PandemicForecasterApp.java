/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.biology;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javafx.util.Duration;

import org.jscience.apps.framework.ChartFactory;
import org.jscience.apps.framework.KillerAppBase;
import org.jscience.biology.ecology.Epidemiology;
import org.jscience.biology.ecology.PopulationDynamics;
import org.jscience.geography.loaders.WorldBankLoader;
import org.jscience.mathematics.numbers.real.Real;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 * Full-Featured Pandemic Forecaster Application.
 * <p>
 * Professional JavaFX application for epidemic simulation using the SEIR model.
 * Features world map, real-time charts, parameter control, and data export.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PandemicForecasterApp extends KillerAppBase {

    // UI Components
    private ComboBox<org.jscience.geography.Region> countrySelector;
    private Slider betaSlider, sigmaSlider, gammaSlider, initialSlider, daysSlider;
    private LineChart<Number, Number> seirChart;
    private XYChart.Series<Number, Number> susSeriesS, expSeriesE, infSeriesI, recSeriesR;
    private ListView<String> eventLog;
    private Label populationLabel, peakLabel, totalLabel;

    // Simulation State
    private Timeline simulationTimeline;
    private Real[][] simulationResults;
    private int currentDay = 0;
    private boolean isRunning = false;

    // Data
    private List<org.jscience.geography.Region> countries;

    @Override
    protected String getAppTitle() {
        return i18n.get("pandemic.title") + " - JScience";
    }

    @Override
    protected Region createMainContent() {
        // Initialize explicitly to empty list to avoid NPE in createControlPanel
        countries = new java.util.ArrayList<>();

        // Main split pane: Chart | Parameters
        SplitPane mainSplit = new SplitPane();
        mainSplit.setOrientation(Orientation.HORIZONTAL);

        // Left: Chart area
        VBox chartArea = createChartArea();
        VBox.setVgrow(chartArea, Priority.ALWAYS);

        // Right: Parameters and info
        VBox controlPanel = createControlPanel();
        controlPanel.setPrefWidth(350);
        controlPanel.setMinWidth(300);

        mainSplit.getItems().addAll(chartArea, controlPanel);
        mainSplit.setDividerPositions(0.7);

        // Load data after UI init
        loadCountryData();
        // Need to manually refresh country list in selector now that data is loaded
        if (countrySelector != null) {
            countrySelector.setItems(FXCollections.observableArrayList(countries));
            if (!countries.isEmpty()) {
                countrySelector.getSelectionModel().selectFirst();
            }
        }

        return mainSplit;
    }

    private void loadCountryData() {
        WorldBankLoader loader = WorldBankLoader.getInstance();
        countries = loader.loadAll();
        log("Loaded " + countries.size() + " countries from WorldBankLoader");
    }

    private VBox createChartArea() {
        VBox area = new VBox(10);
        area.setPadding(new Insets(10));

        // Title
        Label title = new Label(i18n.get("pandemic.panel.chart"));
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // SEIR Chart
        seirChart = ChartFactory.createLineChart(i18n.get("pandemic.panel.chart"), "Day", "Population");
        seirChart.setAnimated(false);
        seirChart.setCreateSymbols(false);

        susSeriesS = ChartFactory.createSeries(i18n.get("pandemic.label.susceptible"));
        expSeriesE = ChartFactory.createSeries(i18n.get("pandemic.label.exposed"));
        infSeriesI = ChartFactory.createSeries(i18n.get("pandemic.label.infectious"));
        recSeriesR = ChartFactory.createSeries(i18n.get("pandemic.label.recovered"));

        @SuppressWarnings("unchecked")
        XYChart.Series<Number, Number>[] series = new XYChart.Series[] { susSeriesS, expSeriesE, infSeriesI,
                recSeriesR };
        seirChart.getData().addAll(series);

        // Style series
        susSeriesS.getNode();
        VBox.setVgrow(seirChart, Priority.ALWAYS);

        // Statistics panel
        HBox statsBox = createStatsPanel();

        area.getChildren().addAll(title, seirChart, statsBox);
        return area;
    }

    private HBox createStatsPanel() {
        HBox stats = new HBox(30);
        stats.setPadding(new Insets(10));
        stats.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5;");

        populationLabel = new Label(java.text.MessageFormat.format(i18n.get("pandemic.label.population"), "--"));
        peakLabel = new Label(java.text.MessageFormat.format(i18n.get("pandemic.label.peak"), "--"));
        totalLabel = new Label(java.text.MessageFormat.format(i18n.get("pandemic.label.total"), "--"));

        populationLabel.setStyle("-fx-font-size: 14px;");
        peakLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #d32f2f;");
        totalLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #388e3c;");

        stats.getChildren().addAll(populationLabel, peakLabel, totalLabel);
        return stats;
    }

    private VBox createControlPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ddd; -fx-border-width: 0 0 0 1;");

        // Country selector
        Label countryLabel = new Label(i18n.get("pandemic.label.select"));
        countryLabel.setStyle("-fx-font-weight: bold;");
        countrySelector = new ComboBox<>();
        countrySelector.setItems(FXCollections.observableArrayList(countries));
        countrySelector.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(org.jscience.geography.Region item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? ""
                        : item.getName() + " (Pop: " + formatNumber(item.getPopulation()) + ")");
            }
        });
        countrySelector.setButtonCell(countrySelector.getCellFactory().call(null));
        countrySelector.setMaxWidth(Double.MAX_VALUE);
        countrySelector.setOnAction(e -> onCountrySelected());

        // Select first country
        if (!countries.isEmpty()) {
            countrySelector.getSelectionModel().selectFirst();
        }

        // Parameters section
        Label paramLabel = new Label("🎛️ " + i18n.get("pandemic.panel.parameters"));
        paramLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Beta (transmission rate)
        VBox betaBox = createSliderWithLabel(i18n.get("pandemic.param.beta"), 0.05, 1.0, 0.3);
        betaSlider = (Slider) betaBox.getChildren().get(1);

        // Sigma (incubation rate = 1/incubation_period)
        VBox sigmaBox = createSliderWithLabel(i18n.get("pandemic.param.sigma"), 0.05, 1.0, 0.2);
        sigmaSlider = (Slider) sigmaBox.getChildren().get(1);

        // Gamma (recovery rate = 1/infectious_period)
        VBox gammaBox = createSliderWithLabel(i18n.get("pandemic.param.gamma"), 0.05, 0.5, 0.1);
        gammaSlider = (Slider) gammaBox.getChildren().get(1);

        // Initial infected
        VBox initialBox = createSliderWithLabel(i18n.get("pandemic.param.initial"), 1, 10000, 100);
        initialSlider = (Slider) initialBox.getChildren().get(1);
        initialSlider.setBlockIncrement(10);

        // Simulation days
        VBox daysBox = createSliderWithLabel(i18n.get("pandemic.param.days"), 30, 365, 180);
        daysSlider = (Slider) daysBox.getChildren().get(1);
        daysSlider.setBlockIncrement(10);

        // R0 display
        Label r0Label = new Label(java.text.MessageFormat.format(i18n.get("pandemic.label.r0"), "--"));
        r0Label.setStyle("-fx-font-style: italic;");
        betaSlider.valueProperty().addListener((o, ov, nv) -> updateR0Label(r0Label));
        gammaSlider.valueProperty().addListener((o, ov, nv) -> updateR0Label(r0Label));

        // Event log
        Label logLabel = new Label(i18n.get("pandemic.log.header"));
        logLabel.setStyle("-fx-font-weight: bold;");
        eventLog = new ListView<>();
        eventLog.setPrefHeight(150);

        // Assemble
        panel.getChildren().addAll(
                countryLabel, countrySelector,
                new Separator(),
                paramLabel,
                betaBox, sigmaBox, gammaBox,
                new Separator(),
                initialBox, daysBox,
                r0Label,
                new Separator(),
                logLabel, eventLog);

        return panel;
    }

    private VBox createSliderWithLabel(String name, double min, double max, double initial) {
        VBox box = new VBox(2);
        Label nameLabel = new Label(name + ": " + String.format("%.3f", initial));
        Slider slider = new Slider(min, max, initial);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.valueProperty().addListener((obs, ov, nv) -> {
            nameLabel.setText(name + ": " + String.format("%.3f", nv.doubleValue()));
        });
        box.getChildren().addAll(nameLabel, slider);
        return box;
    }

    private void updateR0Label(Label label) {
        double beta = betaSlider.getValue();
        double gamma = gammaSlider.getValue();
        double r0 = beta / gamma;
        label.setText(java.text.MessageFormat.format(i18n.get("pandemic.label.r0"), String.format("%.2f", r0)));
        if (r0 > 1) {
            label.setTextFill(Color.DARKRED);
        } else {
            label.setTextFill(Color.DARKGREEN);
        }
    }

    private void onCountrySelected() {
        org.jscience.geography.Region selected = countrySelector.getValue();
        if (selected != null) {
            populationLabel.setText(
                    java.text.MessageFormat.format(i18n.get("pandemic.label.population"),
                            formatNumber(selected.getPopulation())));
            log(java.text.MessageFormat.format(i18n.get("pandemic.log.selected"), selected.getName()));
        }
    }

    // ===== Simulation Control =====

    @Override
    public void onRun() {
        if (isRunning)
            return;

        org.jscience.geography.Region country = countrySelector.getValue();
        if (country == null) {
            showError(i18n.get("dialog.error.title"), i18n.get("pandemic.error.select"));
            return;
        }

        // Clear previous
        clearChart();
        currentDay = 0;

        long N = country.getPopulation() > 0 ? country.getPopulation() : 1000000;
        int I0 = (int) initialSlider.getValue();
        int days = (int) daysSlider.getValue();

        Real beta = Real.of(betaSlider.getValue());
        Real sigma = Real.of(sigmaSlider.getValue());
        Real gamma = Real.of(gammaSlider.getValue());
        Real dt = Real.of(1.0);

        Real[] initial = new Real[] {
                Real.of(N - I0), // S
                Real.of(0), // E
                Real.of(I0), // I
                Real.of(0) // R
        };

        log(java.text.MessageFormat.format(i18n.get("pandemic.log.start"), country.getName(), formatNumber(N),
                betaSlider.getValue()));

        try {
            simulationResults = PopulationDynamics.seirModel(initial, beta, sigma, gamma, dt, days);
            startAnimation(days);
            isRunning = true;
            setStatus(i18n.get("status.running"));
        } catch (Exception e) {
            showError("Simulation Error", e.getMessage());
        }
    }

    private void startAnimation(int totalDays) {
        simulationTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            if (currentDay < totalDays && simulationResults != null) {
                addDataPoint(currentDay);
                currentDay++;
                setProgress((double) currentDay / totalDays);
            } else {
                stopAnimation();
            }
        }));
        simulationTimeline.setCycleCount(Timeline.INDEFINITE);
        simulationTimeline.play();
    }

    private void addDataPoint(int day) {
        double s = simulationResults[day][0].doubleValue();
        double e = simulationResults[day][1].doubleValue();
        double i = simulationResults[day][2].doubleValue();
        double r = simulationResults[day][3].doubleValue();

        susSeriesS.getData().add(new XYChart.Data<>(day, s));
        expSeriesE.getData().add(new XYChart.Data<>(day, e));
        infSeriesI.getData().add(new XYChart.Data<>(day, i));
        recSeriesR.getData().add(new XYChart.Data<>(day, r));

        // Update peak
        if (day > 0) {
            double maxI = 0;
            for (int d = 0; d <= day; d++) {
                maxI = Math.max(maxI, simulationResults[d][2].doubleValue());
            }
            peakLabel.setText(
                    java.text.MessageFormat.format(i18n.get("pandemic.label.peak"), formatNumber((long) maxI)));
            totalLabel
                    .setText(java.text.MessageFormat.format(i18n.get("pandemic.label.total"), formatNumber((long) r)));
        }
    }

    private void stopAnimation() {
        if (simulationTimeline != null) {
            simulationTimeline.stop();
        }
        isRunning = false;
        setStatus(i18n.get("status.complete"));
        setProgress(0);
        log(java.text.MessageFormat.format(i18n.get("pandemic.log.complete"), currentDay));
    }

    @Override
    public void onPause() {
        if (simulationTimeline != null) {
            simulationTimeline.pause();
            setStatus(i18n.get("status.paused"));
        }
    }

    @Override
    public void onStop() {
        stopAnimation();
    }

    @Override
    public void onReset() {
        stopAnimation();
        clearChart();
        currentDay = 0;
        simulationResults = null;
        setStatus(i18n.get("status.ready"));
        log(i18n.get("pandemic.log.reset"));
    }

    private void clearChart() {
        susSeriesS.getData().clear();
        expSeriesE.getData().clear();
        infSeriesI.getData().clear();
        recSeriesR.getData().clear();
        peakLabel.setText(java.text.MessageFormat.format(i18n.get("pandemic.label.peak"), "--"));
        totalLabel.setText(java.text.MessageFormat.format(i18n.get("pandemic.label.total"), "--"));
    }

    // ===== File Operations =====

    @Override
    protected void configureFileChooser(FileChooser chooser) {
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    }

    @Override
    protected void doExport(File file, String format) {
        if (simulationResults == null) {
            showInfo(i18n.get("menu.file.export"), i18n.get("pandemic.info.no_data"));
            return;
        }

        if ("csv".equals(format)) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                pw.println("Day,Susceptible,Exposed,Infectious,Recovered");
                for (int d = 0; d < currentDay; d++) {
                    pw.printf("%d,%.0f,%.0f,%.0f,%.0f%n",
                            d,
                            simulationResults[d][0].doubleValue(),
                            simulationResults[d][1].doubleValue(),
                            simulationResults[d][2].doubleValue(),
                            simulationResults[d][3].doubleValue());
                }
                log(java.text.MessageFormat.format(i18n.get("pandemic.log.export"), file.getName()));
                showInfo(i18n.get("menu.file.export"),
                        java.text.MessageFormat.format(i18n.get("pandemic.info.export"), file.getName()));
            } catch (Exception e) {
                showError("Export Error", e.getMessage());
            }
        }
    }

    // ===== Utilities =====

    private void log(String message) {
        String timestamp = java.time.LocalTime.now().toString().substring(0, 8);
        eventLog.getItems().add(0, "[" + timestamp + "] " + message);
        if (eventLog.getItems().size() > 100) {
            eventLog.getItems().remove(eventLog.getItems().size() - 1);
        }
    }

    private String formatNumber(long num) {
        if (num >= 1_000_000_000)
            return String.format("%.1fB", num / 1e9);
        if (num >= 1_000_000)
            return String.format("%.1fM", num / 1e6);
        if (num >= 1_000)
            return String.format("%.1fK", num / 1e3);
        return String.valueOf(num);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
