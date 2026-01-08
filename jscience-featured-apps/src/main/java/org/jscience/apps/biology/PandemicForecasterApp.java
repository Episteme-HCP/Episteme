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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import org.jscience.apps.framework.ChartFactory;
import org.jscience.apps.framework.FeaturedAppBase;
import org.jscience.biology.ecology.PopulationDynamics;
import org.jscience.politics.loaders.WorldBankReader;
import org.jscience.politics.Country;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.io.AbstractResourceReader;

/**
 * Pandemic Propagation Forecaster.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PandemicForecasterApp extends FeaturedAppBase {

    // UI Components
    private ComboBox<Country> countrySelector;
    private Slider betaSlider, sigmaSlider, gammaSlider, muSlider, initialSlider, daysSlider;
    private LineChart<Number, Number> seirChart;
    private XYChart.Series<Number, Number> susSeriesS, expSeriesE, infSeriesI, recSeriesR, deaSeriesD;
    private ListView<String> eventLog;
    private Label populationLabel, peakLabel, totalLabel, deadLabel;
    private Label chartAreaTitleLabel;
    private Label countrySelectLabel;
    private Label parametersTitleLabel;
    private Label logHeaderLabel;
    private java.util.Map<String, Label> sliderLabels = new java.util.HashMap<>();

    // Simulation State
    private Timeline simulationTimeline;
    private Real[][] simulationResults;
    private int currentDay = 0;
    private boolean isRunning = false;

    // Data
    private List<Country> countries;

    @Override
    protected String getAppTitle() {
        return i18n.get("pandemic.title");
    }

    @Override
    public String getDescription() {
        return i18n.get("pandemic.desc");
    }

    @Override
    public boolean hasEditMenu() {
        return false;
    }

    @Override
    protected javafx.scene.layout.Region createMainContent() {
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
        WorldBankReader loader = WorldBankReader.getInstance();
        countries = ((AbstractResourceReader<Country>) loader).loadAll();
        log("Loaded " + countries.size() + " countries from WorldBankReader");
    }

    private VBox createChartArea() {
        VBox area = new VBox(10);
        area.setPadding(new Insets(10));

        // Title
        chartAreaTitleLabel = new Label(i18n.get("pandemic.panel.chart"));
        chartAreaTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // SEIR Chart
        seirChart = ChartFactory.createLineChart(i18n.get("pandemic.panel.chart"), "Day", "Population");
        seirChart.setAnimated(false);
        seirChart.setCreateSymbols(false);

        susSeriesS = ChartFactory.createSeries(i18n.get("pandemic.label.susceptible"));
        expSeriesE = ChartFactory.createSeries(i18n.get("pandemic.label.exposed"));
        infSeriesI = ChartFactory.createSeries(i18n.get("pandemic.label.infectious"));
        recSeriesR = ChartFactory.createSeries(i18n.get("pandemic.label.recovered"));
        deaSeriesD = ChartFactory.createSeries(i18n.get("pandemic.label.deceased"));
        deaSeriesD.setName("Deceased"); // Fallback if i18n missing

        if (susSeriesS != null)
            seirChart.getData().add(susSeriesS);
        if (expSeriesE != null)
            seirChart.getData().add(expSeriesE);
        if (infSeriesI != null)
            seirChart.getData().add(infSeriesI);
        if (recSeriesR != null)
            seirChart.getData().add(recSeriesR);
        if (deaSeriesD != null)
            seirChart.getData().add(deaSeriesD);

        // Style series
        susSeriesS.getNode();
        VBox.setVgrow(seirChart, Priority.ALWAYS);

        // Statistics panel
        HBox statsBox = createStatsPanel();

        area.getChildren().addAll(chartAreaTitleLabel, seirChart, statsBox);
        return area;
    }

    private HBox createStatsPanel() {
        HBox stats = new HBox(30);
        stats.setPadding(new Insets(10));
        stats.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5;");

        populationLabel = new Label(java.text.MessageFormat.format(i18n.get("pandemic.label.population"), "--"));
        peakLabel = new Label(java.text.MessageFormat.format(i18n.get("pandemic.label.peak"), "--"));
        totalLabel = new Label(java.text.MessageFormat.format(i18n.get("pandemic.label.total"), "--"));
        deadLabel = new Label(java.text.MessageFormat.format(i18n.get("pandemic.label.dead"), "--"));

        populationLabel.setStyle("-fx-font-size: 14px;");
        peakLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #d32f2f;");
        totalLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #388e3c;");
        deadLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000;");

        stats.getChildren().addAll(populationLabel, peakLabel, totalLabel, deadLabel);
        return stats;
    }

    private VBox createControlPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ddd; -fx-border-width: 0 0 0 1;");

        // Country selector
        countrySelectLabel = new Label(i18n.get("pandemic.label.select"));
        countrySelectLabel.setStyle("-fx-font-weight: bold;");
        countrySelector = new ComboBox<>();
        countrySelector.setItems(FXCollections.observableArrayList(countries));
        countrySelector.setCellFactory(lv -> new ListCell<Country>() {
            @Override
            protected void updateItem(Country item, boolean empty) {
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
        parametersTitleLabel = new Label(i18n.get("pandemic.panel.parameters"));
        parametersTitleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Beta (transmission rate)
        VBox betaBox = createSliderWithLabel(i18n.get("pandemic.param.beta"), 0.05, 1.0, 0.3, "beta");
        betaSlider = (Slider) betaBox.getChildren().get(1);

        // Sigma (incubation rate = 1/incubation_period)
        VBox sigmaBox = createSliderWithLabel(i18n.get("pandemic.param.sigma"), 0.05, 1.0, 0.2, "sigma");
        sigmaSlider = (Slider) sigmaBox.getChildren().get(1);

        // Gamma (recovery rate = 1/infectious_period)
        VBox gammaBox = createSliderWithLabel(i18n.get("pandemic.param.gamma"), 0.05, 0.5, 0.1, "gamma");
        gammaSlider = (Slider) gammaBox.getChildren().get(1);

        // Mu (mortality rate)
        VBox muBox = createSliderWithLabel(i18n.get("pandemic.param.mu"), 0.00, 0.1, 0.005, "mu");
        muSlider = (Slider) muBox.getChildren().get(1);

        // Initial infected
        VBox initialBox = createSliderWithLabel(i18n.get("pandemic.param.initial"), 1, 10000, 100, "initial");
        initialSlider = (Slider) initialBox.getChildren().get(1);
        initialSlider.setBlockIncrement(10);

        // Simulation days
        VBox daysBox = createSliderWithLabel(i18n.get("pandemic.param.days"), 30, 365, 180, "days");
        daysSlider = (Slider) daysBox.getChildren().get(1);
        daysSlider.setBlockIncrement(10);

        // R0 display
        Label r0Label = new Label(java.text.MessageFormat.format(i18n.get("pandemic.label.r0"), "--"));
        r0Label.setStyle("-fx-font-style: italic;");
        betaSlider.valueProperty().addListener((o, ov, nv) -> updateR0Label(r0Label));
        gammaSlider.valueProperty().addListener((o, ov, nv) -> updateR0Label(r0Label));

        // Event log
        logHeaderLabel = new Label(i18n.get("pandemic.log.header"));
        logHeaderLabel.setStyle("-fx-font-weight: bold;");
        eventLog = new ListView<>();
        eventLog.setPrefHeight(150);

        // Assemble
        panel.getChildren().addAll(
                countrySelectLabel, countrySelector,
                new Separator(),
                parametersTitleLabel,
                betaBox, sigmaBox, gammaBox, muBox,
                new Separator(),
                initialBox, daysBox,
                r0Label,
                new Separator(),
                logHeaderLabel, eventLog);

        return panel;
    }

    private VBox createSliderWithLabel(String name, double min, double max, double initial, String paramId) {
        VBox box = new VBox(2);
        Label nameLabel = new Label(name + ": " + String.format("%.3f", initial));
        sliderLabels.put(paramId, nameLabel);
        Slider slider = new Slider(min, max, initial);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.valueProperty().addListener((obs, ov, nv) -> {
            nameLabel.setText(i18n.get("pandemic.param." + paramId) + ": " + String.format("%.3f", nv.doubleValue()));
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
        Country selected = countrySelector.getValue();
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

        Country country = countrySelector.getValue();
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
        Real mu = Real.of(muSlider.getValue());
        Real dt = Real.of(1.0);

        Real[] initial = new Real[] {
                Real.of(N - I0), // S
                Real.of(0), // E
                Real.of(I0), // I
                Real.of(0), // R
                Real.of(0) // D
        };

        log(java.text.MessageFormat.format(i18n.get("pandemic.log.start"), country.getName(), formatNumber(N),
                betaSlider.getValue()));

        try {
            simulationResults = PopulationDynamics.seirdModel(initial, beta, sigma, gamma, mu, dt, days);
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
        double dVal = simulationResults[day][4].doubleValue();

        susSeriesS.getData().add(new XYChart.Data<>(day, s));
        expSeriesE.getData().add(new XYChart.Data<>(day, e));
        infSeriesI.getData().add(new XYChart.Data<>(day, i));
        recSeriesR.getData().add(new XYChart.Data<>(day, r));
        deaSeriesD.getData().add(new XYChart.Data<>(day, dVal));

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
            deadLabel.setText(
                    java.text.MessageFormat.format(i18n.get("pandemic.label.dead"), formatNumber((long) dVal)));
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

    @Override
    protected void updateLocalizedUI() {
        if (chartAreaTitleLabel != null)
            chartAreaTitleLabel.setText(i18n.get("pandemic.panel.chart"));
        if (countrySelectLabel != null)
            countrySelectLabel.setText(i18n.get("pandemic.label.select"));
        if (parametersTitleLabel != null)
            parametersTitleLabel.setText(i18n.get("pandemic.panel.parameters"));
        if (logHeaderLabel != null)
            logHeaderLabel.setText(i18n.get("pandemic.log.header"));

        // Update slider labels
        if (sliderLabels.get("beta") != null)
            sliderLabels.get("beta").setText(i18n.get("pandemic.param.beta"));
        if (sliderLabels.get("sigma") != null)
            sliderLabels.get("sigma").setText(i18n.get("pandemic.param.sigma"));
        if (sliderLabels.get("gamma") != null)
            sliderLabels.get("gamma").setText(i18n.get("pandemic.param.gamma"));
        if (sliderLabels.get("mu") != null)
            sliderLabels.get("mu").setText(i18n.get("pandemic.param.mu"));
        if (sliderLabels.get("initial") != null)
            sliderLabels.get("initial").setText(i18n.get("pandemic.param.initial"));
        if (sliderLabels.get("days") != null)
            sliderLabels.get("days").setText(i18n.get("pandemic.param.days"));

        if (seirChart != null) {
            seirChart.setTitle(i18n.get("pandemic.panel.chart"));
            if (susSeriesS != null)
                susSeriesS.setName(i18n.get("pandemic.label.susceptible"));
            if (expSeriesE != null)
                expSeriesE.setName(i18n.get("pandemic.label.exposed"));
            if (infSeriesI != null)
                infSeriesI.setName(i18n.get("pandemic.label.infectious"));
            if (recSeriesR != null)
                recSeriesR.setName(i18n.get("pandemic.label.recovered"));
            if (deaSeriesD != null)
                deaSeriesD.setName(i18n.get("pandemic.label.deceased"));
        }

        // Stats labels
        if (populationLabel != null)
            onCountrySelected(); // Refresh population label

        // Parameters labels (sliders)
        // Note: createSliderWithLabel uses local Label, hard to update without storing
        // them all.
        // For now, let's at least update the main ones.
    }

    private void clearChart() {
        susSeriesS.getData().clear();
        expSeriesE.getData().clear();
        infSeriesI.getData().clear();
        recSeriesR.getData().clear();
        deaSeriesD.getData().clear();
        peakLabel.setText(java.text.MessageFormat.format(i18n.get("pandemic.label.peak"), "--"));
        totalLabel.setText(java.text.MessageFormat.format(i18n.get("pandemic.label.total"), "--"));
        deadLabel.setText(java.text.MessageFormat.format(i18n.get("pandemic.label.dead"), "--"));
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

    @Override
    protected void doNew() {
        onReset();
    }

    @Override
    protected void addAppHelpTopics(org.jscience.apps.framework.HelpDialog dialog) {
        dialog.addTopic("Scientific Model", "SEIR Model",
                "This application uses the **SEIR** compartmental model:\n\n" +
                        "- **S**usceptible: Individuals who can catch the disease.\n" +
                        "- **E**xposed: Infected but not yet infectious (incubation).\n" +
                        "- **I**nfectious: Can spread the disease to Susceptibles.\n" +
                        "- **R**ecovered: Immune or recovered.\n" +
                        "- **D**eceased: Died from the disease.\n\n" +
                        "Parameters:\n" +
                        "- **Beta**: Transmission rate.\n" +
                        "- **Sigma**: Incubation rate (1/incubation period).\n" +
                        "- **Gamma**: Recovery rate (1/infectious period).\n" +
                        "- **Mu**: Mortality rate.",
                null);
    }

    @Override
    protected void addAppTutorials(org.jscience.apps.framework.HelpDialog dialog) {
        dialog.addTopic("Tutorial", "Running a Simulation",
                "1. Select a **Country** from the dropdown.\n" +
                        "2. Adjust the parameters (Transmission, Incubation, etc.) to match the disease characteristics.\n"
                        +
                        "3. Click **Run** in the toolbar to start.\n" +
                        "4. Use **Pause** to freeze the simulation.\n" +
                        "5. **Export** the data to CSV via the File menu for further analysis.",
                null);
    }

    @Override
    protected byte[] serializeState() {
        java.util.Properties props = new java.util.Properties();
        if (countrySelector.getValue() != null) {
            props.setProperty("country", countrySelector.getValue().getName()); // Use name as ID for simplicity
        }
        props.setProperty("beta", String.valueOf(betaSlider.getValue()));
        props.setProperty("sigma", String.valueOf(sigmaSlider.getValue()));
        props.setProperty("gamma", String.valueOf(gammaSlider.getValue()));
        props.setProperty("mu", String.valueOf(muSlider.getValue()));
        props.setProperty("initial", String.valueOf(initialSlider.getValue()));
        props.setProperty("days", String.valueOf(daysSlider.getValue()));

        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try {
            props.store(baos, "Pandemic State");
            return baos.toByteArray();
        } catch (java.io.IOException e) {
            return null;
        }
    }

    @Override
    protected void deserializeState(byte[] data) {
        java.util.Properties props = new java.util.Properties();
        try {
            props.load(new java.io.ByteArrayInputStream(data));

            String countryName = props.getProperty("country");
            if (countryName != null) {
                for (Country c : countrySelector.getItems()) {
                    if (c.getName().equals(countryName)) {
                        countrySelector.setValue(c);
                        break;
                    }
                }
            }

            betaSlider.setValue(Double.parseDouble(props.getProperty("beta", "0.5")));
            sigmaSlider.setValue(Double.parseDouble(props.getProperty("sigma", "0.2")));
            gammaSlider.setValue(Double.parseDouble(props.getProperty("gamma", "0.1")));
            muSlider.setValue(Double.parseDouble(props.getProperty("mu", "0.01")));
            initialSlider.setValue(Double.parseDouble(props.getProperty("initial", "1")));
            daysSlider.setValue(Double.parseDouble(props.getProperty("days", "180")));

            onReset(); // Prepare for new run with these params
        } catch (Exception e) {
            showError("Load Error", "Failed to restore state: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
