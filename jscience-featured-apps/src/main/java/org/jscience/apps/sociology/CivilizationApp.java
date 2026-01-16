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

package org.jscience.apps.sociology;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import org.jscience.apps.framework.FeaturedAppBase;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.*;

/**
 * Civilization Evolution Simulator.
 * <p>
 * A simplified System Dynamics model (Limits to Growth style) simulating
 * the interaction between Population, Resources, and Pollution.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CivilizationApp extends FeaturedAppBase {

    // Model State (Stocks)
    private Quantity<Dimensionless> population;
    private Quantity<Mass> resources;
    private Quantity<Dimensionless> pollution;

    // Parameters
    private double birthRateBase = 0.05;
    private double consumptionPerCapita = 1.0;
    private double pollutionFactor = 0.1;
    private double innovationRate = 0.0; // Reduces consumption/pollution
    private double regenerationRate = 0.0;
    private double aggression = 0.0;

    public CivilizationApp() {
        super();
        try {
            // Safe initialization of fields that might depend on ServiceLoader (like Units)
            this.population = Quantities.create(1000.0, Units.ONE);
            this.resources = Quantities.create(100000.0, Units.KILOGRAM);
            this.pollution = Quantities.create(0.0, Units.ONE);
            
            this.parameterSliders = new java.util.HashMap<>();
            this.sliderLabels = new java.util.HashMap<>();
        } catch (Throwable t) {
            System.err.println("CRITICAL: Failed to initialize CivilizationApp: " + t.getMessage());
            t.printStackTrace();
            // Fallback to prevent ServiceConfigurationError if possible, though functionality will be broken
            // But usually we want to rethrow to let the loader know, but maybe wrapped cleanly?
            // Actually, suppressing it might allow other apps to load if the loop continues?
            // But for now, let's just log it.
        }
    }


    private double time = 0;
    private boolean running = false;

    // UI
    private XYChart.Series<Number, Number> popSeries;
    private XYChart.Series<Number, Number> resSeries;
    private XYChart.Series<Number, Number> polSeries;
    private Label statusLabel;
    private Label paramsTitleLabel;
    private java.util.Map<String, Slider> parameterSliders;
    private java.util.Map<String, Label> sliderLabels;
    private LineChart<Number, Number> mainChart;

    @Override
    protected String getAppTitle() {
        return i18n.get("civilization.title");
    }

    @Override
    public String getDescription() {
        return i18n.get("civilization.desc");
    }

    @Override
    public boolean hasEditMenu() {
        return false;
    }

    @Override
    protected void onAppReady() {
        super.onAppReady();
        // Set running state default
        setSimulationControlsVisible(true);
    }

    @Override
    protected void doNew() {
        reset();
    }

    @Override
    protected void addAppHelpTopics(org.jscience.apps.framework.HelpDialog dialog) {
        dialog.addTopic("Dynamics", "Population Growth",
                "Population grows based on birth rate minus death rate.\n\nDeath rate is influenced by pollution and scarcity.",
                null);
        dialog.addTopic("Dynamics", "Resources",
                "Resources are consumed by the population.\n\nResources regenerate slowly over time.", null);
        dialog.addTopic("Dynamics", "Pollution", "Pollution is generated by consumption and causes death.", null);
    }

    @Override
    protected void addAppTutorials(org.jscience.apps.framework.HelpDialog dialog) {
        dialog.addTopic("Tutorials", "Avoiding Collapse",
                "To avoid collapse:\n1. Keep consumption moderate.\n2. Invest in innovation.\n3. Control birth rate if resources dwindle.",
                null);
    }

    @Override
    protected Region createMainContent() {
        BorderPane root = new BorderPane();

        // Chart
        root.setCenter(createChart());

        // Controls
        VBox controls = createControls();
        root.setBottom(controls);

        // Status Overlay
        statusLabel = new Label(i18n.get("civilization.status.stable"));
        statusLabel.getStyleClass().add("font-bold");
        statusLabel.getStyleClass().removeAll("text-success", "text-warning", "text-error", "text-info");
        statusLabel.getStyleClass().add("text-success");
        StackPane overlay = new StackPane(statusLabel);
        overlay.setPickOnBounds(false);
        overlay.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        overlay.setPadding(new Insets(20));

        StackPane mainStack = new StackPane(root, overlay);

        startLoop();

        return mainStack;
    }

    @SuppressWarnings("unchecked")
    private LineChart<Number, Number> createChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(i18n.get("civilization.label.years"));
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(i18n.get("civilization.label.value"));

        LineChart<Number, Number> lc = new LineChart<>(xAxis, yAxis);
        lc.setTitle(i18n.get("civilization.chart.title"));
        lc.setCreateSymbols(false);
        lc.setAnimated(false);

        popSeries = new XYChart.Series<>();
        popSeries.setName(i18n.get("civilization.label.population"));
        resSeries = new XYChart.Series<>();
        resSeries.setName(i18n.get("civilization.label.resources"));
        polSeries = new XYChart.Series<>();
        polSeries.setName(i18n.get("civilization.series.pollution"));

        lc.getData().addAll(popSeries, resSeries, polSeries);
        mainChart = lc;
        return lc;
    }

    private VBox createControls() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.getStyleClass().add("viewer-controls");

        HBox sliders = new HBox(20);

        sliders.getChildren().add(
                createSlider(i18n.get("civilization.label.consumption"), 0.5, 5.0, 1.0, v -> consumptionPerCapita = v,
                        "Consumption"));
        sliders.getChildren()
                .add(createSlider(i18n.get("civilization.label.birth"), 0.0, 0.2, 0.05, v -> birthRateBase = v,
                        "Birth Rate"));
        sliders.getChildren()
                .add(createSlider(i18n.get("civilization.label.innovation"), 0.0, 0.05, 0.0, v -> innovationRate = v,
                        "Innovation"));
        sliders.getChildren()
                .add(createSlider(i18n.get("civilization.label.regen"), 0.0, 1.0, 0.0, v -> regenerationRate = v,
                        "Regeneration"));
        sliders.getChildren()
                .add(createSlider(i18n.get("civilization.label.aggression"), 0.0, 1.0, 0.0, v -> aggression = v,
                        "Aggression"));

        paramsTitleLabel = new Label(i18n.get("civilization.label.params"));
        paramsTitleLabel.getStyleClass().add("header-label");
        paramsTitleLabel.setStyle("-fx-font-size: 14px;");
        box.getChildren().addAll(paramsTitleLabel, sliders);
        return box;
    }

    private VBox createSlider(String name, double min, double max, double val,
            java.util.function.DoubleConsumer consumer, String paramName) {
        VBox vb = new VBox(5);
        Label l = new Label(name);
        sliderLabels.put(paramName, l);
        Slider s = new Slider(min, max, val);
        s.setShowTickLabels(true);
        s.setShowTickMarks(true);
        s.setTooltip(new Tooltip("Adjust the " + name
                + " parameter.\nScientific impact: Changes the system dynamics equations directly."));

        // Better Undo Logic: Capture old value on press/start change
        final double[] oldValue = new double[1];
        s.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (isChanging) {
                oldValue[0] = s.getValue();
            } else {
                double newValue = s.getValue();
                if (Math.abs(newValue - oldValue[0]) > 0.0001) {
                    undoManager.push(new org.jscience.apps.framework.UndoManager.Action() {
                        @Override
                        public void undo() {
                            s.setValue(oldValue[0]);
                            consumer.accept(oldValue[0]);
                        }

                        @Override
                        public void redo() {
                            s.setValue(newValue);
                            consumer.accept(newValue);
                        }

                        @Override
                        public String getName() {
                            return "Change " + paramName;
                        }
                    });
                }
            }
        });

        s.valueProperty().addListener((o, ov, nv) -> consumer.accept(nv.doubleValue()));
        parameterSliders.put(paramName, s);
        vb.getChildren().addAll(l, s);
        return vb;
    }

    private void updateStatusLabel() {
        if (population.getValue().doubleValue() <= 0) {
            statusLabel.setText(i18n.get("civilization.status.extinct"));
            statusLabel.getStyleClass().add("description-label");
        } else if (population.getValue().doubleValue() < 500) {
            statusLabel.setText(i18n.get("civilization.status.collapse"));
            statusLabel.getStyleClass().add("text-error");
        } else if (birthRateBase < 0.03) {
            statusLabel.setText(i18n.get("civilization.status.declining"));
            statusLabel.getStyleClass().add("text-warning");
        } else {
            statusLabel.setText(i18n.get("civilization.status.thriving"));
            statusLabel.getStyleClass().add("text-success");
        }
    }

    @Override
    protected byte[] serializeState() {
        java.util.Properties props = new java.util.Properties();
        props.setProperty("time", String.valueOf(time));
        props.setProperty("pop", String.valueOf(population.getValue().doubleValue()));
        props.setProperty("res", String.valueOf(resources.getValue().doubleValue()));
        props.setProperty("pol", String.valueOf(pollution.getValue().doubleValue()));

        props.setProperty("param.consumption", String.valueOf(consumptionPerCapita));
        props.setProperty("param.birth", String.valueOf(birthRateBase));
        props.setProperty("param.innovation", String.valueOf(innovationRate));
        props.setProperty("param.regen", String.valueOf(regenerationRate));
        props.setProperty("param.aggression", String.valueOf(aggression));

        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try {
            props.store(baos, "Civilization State");
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
            time = Double.parseDouble(props.getProperty("time", "0"));
            population = Quantities.create(Double.parseDouble(props.getProperty("pop", "1000")), Units.ONE);
            resources = Quantities.create(Double.parseDouble(props.getProperty("res", "100000")), Units.KILOGRAM);
            pollution = Quantities.create(Double.parseDouble(props.getProperty("pol", "0")), Units.ONE);

            updateParam("Consumption", props.getProperty("param.consumption"));
            updateParam("Birth Rate", props.getProperty("param.birth"));
            updateParam("Innovation", props.getProperty("param.innovation"));
            updateParam("Regeneration", props.getProperty("param.regen"));
            updateParam("Aggression", props.getProperty("param.aggression"));

            // Clear and refresh chart or just continue?
            // For now, let's keep the history or clear it.
            popSeries.getData().clear();
            resSeries.getData().clear();
            polSeries.getData().clear();

        } catch (Exception e) {
            showError("Load Error", "Failed to restore state: " + e.getMessage());
        }
    }

    private void updateParam(String name, String value) {
        if (value == null)
            return;
        double val = Double.parseDouble(value);
        Slider s = parameterSliders.get(name);
        if (s != null) {
            s.setValue(val);
        }
    }

    private void startLoop() {
        AnimationTimer timer = new AnimationTimer() {
            private long last = 0;

            @Override
            public void handle(long now) {
                if (now - last > 50_000_000) { // 20 FPS
                    if (running && population.getValue().doubleValue() > 0) {
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

        // 1. Calculations - Using Real and Quantity
        double efficiency = 1.0 + (innovationRate * time); // Tech improves efficiency
        double effectiveConsumption = consumptionPerCapita / efficiency;

        // Resource Scarcity Factor
        double resValue = resources.getValue().doubleValue();
        double scarcity = Math.max(0, resValue) / 10000.0;
        if (scarcity > 1.0)
            scarcity = 1.0;

        // Pollution Impact
        double polValue = pollution.getValue().doubleValue();
        double pollutionDeath = polValue * 0.001;

        // Dynamics
        // Birth Rate decays with wealth (demographic transition) but drops with
        // scarcity
        double birthRate = birthRateBase * scarcity;

        // Death Rate rises with pollution and starvation
        // Aggression increases base death rate (wars)
        double deathRate = 0.02 + pollutionDeath + (1.0 - scarcity) * 0.1 + (aggression * 0.05);

        double popValue = population.getValue().doubleValue();
        double netRate = birthRate - deathRate;

        Quantity<Dimensionless> dPop = population.multiply(netRate);

        // Resource consumption (Mass)
        // Note: resources is Quantity<Mass>, population is Dimensionless.
        // We need to subtract Mass.
        Quantity<Mass> consumed = Quantities.create(popValue * effectiveConsumption, Units.KILOGRAM);

        // Pollution generation rate
        Quantity<Dimensionless> dPol = population.multiply(effectiveConsumption * pollutionFactor)
                .subtract(pollution.multiply(0.05));

        // Update stocks
        population = population.add(dPop);

        // Regeneration of resources
        Quantity<Mass> regen = Quantities.create(100 * regenerationRate, Units.KILOGRAM);
        resources = resources.subtract(consumed).add(regen);

        pollution = pollution.add(dPol);

        // Bounds check
        if (population.getValue().doubleValue() < 0)
            population = Quantities.create(0.0, Units.ONE);
        if (resources.getValue().doubleValue() < 0)
            resources = Quantities.create(0.0, Units.KILOGRAM);
        if (pollution.getValue().doubleValue() < 0)
            pollution = Quantities.create(0.0, Units.ONE);

        // Update Chart (downsample)
        if (time % 5 == 0) {
            popSeries.getData().add(new XYChart.Data<>(time, population.getValue().doubleValue()));
            resSeries.getData().add(new XYChart.Data<>(time, resources.getValue().doubleValue()));
            polSeries.getData().add(new XYChart.Data<>(time, pollution.getValue().doubleValue()));

            // Limit history
            if (popSeries.getData().size() > 200) {
                popSeries.getData().remove(0);
                resSeries.getData().remove(0);
                polSeries.getData().remove(0);
            }
        }

        updateStatus(dPop.getValue().doubleValue());
    }

    private void updateStatus(double dPop) {
        if (population.getValue().doubleValue() <= 10) {
            statusLabel.setText(i18n.get("civilization.status.extinct"));
            statusLabel.getStyleClass().add("description-label");
        } else if (dPop < -10) {
            statusLabel.setText(i18n.get("civilization.status.collapse"));
            statusLabel.getStyleClass().add("text-error");
        } else if (dPop < 0) {
            statusLabel.setText(i18n.get("civilization.status.declining"));
            statusLabel.getStyleClass().add("text-warning");
        } else {
            statusLabel.setText(i18n.get("civilization.status.thriving"));
            statusLabel.getStyleClass().add("text-success");
        }
    }

    @Override
    public void onRun() {
        running = true;
    }

    @Override
    public void onPause() {
        running = false;
    }

    @Override
    public void onStop() {
        running = false;
    }

    @Override
    public void onReset() {
        reset();
    }

    private void reset() {
        population = Quantities.create(1000.0, Units.ONE);
        resources = Quantities.create(100000.0, Units.KILOGRAM);
        pollution = Quantities.create(0.0, Units.ONE);

        time = 0;
        popSeries.getData().clear();
        resSeries.getData().clear();
        polSeries.getData().clear();
        statusLabel.setText(i18n.get("civilization.status.stable"));
    }

    @Override
    protected void updateLocalizedUI() {
        if (paramsTitleLabel != null)
            paramsTitleLabel.setText(i18n.get("civilization.label.params"));
        if (statusLabel != null) {
            // Refresh status text based on current state
            updateStatusLabel();
        }
        if (mainChart != null) {
            mainChart.setTitle(i18n.get("civilization.chart.title"));
            if (mainChart.getXAxis() instanceof NumberAxis) {
                ((NumberAxis) mainChart.getXAxis()).setLabel(i18n.get("civilization.label.years"));
            }
            if (mainChart.getYAxis() instanceof NumberAxis) {
                ((NumberAxis) mainChart.getYAxis()).setLabel(i18n.get("civilization.label.value"));
            }
            if (!mainChart.getData().isEmpty()) {
                popSeries.setName(i18n.get("civilization.label.population"));
                resSeries.setName(i18n.get("civilization.label.resources"));
                polSeries.setName(i18n.get("civilization.series.pollution"));
            }
        }

        // Update slider labels
        if (sliderLabels.get("Consumption") != null)
            sliderLabels.get("Consumption").setText(i18n.get("civilization.label.consumption"));
        if (sliderLabels.get("Birth Rate") != null)
            sliderLabels.get("Birth Rate").setText(i18n.get("civilization.label.birth"));
        if (sliderLabels.get("Innovation") != null)
            sliderLabels.get("Innovation").setText(i18n.get("civilization.label.innovation"));
        if (sliderLabels.get("Regeneration") != null)
            sliderLabels.get("Regeneration").setText(i18n.get("civilization.label.regen"));
        if (sliderLabels.get("Aggression") != null)
            sliderLabels.get("Aggression").setText(i18n.get("civilization.label.aggression"));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getCategory() {
        return "Sociology";
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }
}