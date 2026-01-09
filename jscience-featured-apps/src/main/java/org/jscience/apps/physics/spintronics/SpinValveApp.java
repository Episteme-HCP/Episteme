/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jscience.apps.framework.FeaturedAppBase;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;

public class SpinValveApp extends FeaturedAppBase {

    private SpinValve spinValve;
    private ComboBox<SpintronicMaterial> pinnedMaterialCombo;
    private Slider pinnedThicknessSlider;
    private ComboBox<SpintronicMaterial> spacerMaterialCombo;
    private Slider spacerThicknessSlider;
    private ComboBox<SpintronicMaterial> freeMaterialCombo;
    private Slider freeThicknessSlider;
    private Slider freeAngleSlider;
    private Slider temperatureSlider;
    private Slider dampingSlider;
    private Slider pmaSlider;
    private Slider sotCurrentSlider;
    private Slider sotHallSlider;
    private Slider areaSlider;
    private CheckBox pmaCheckBox;
    private CheckBox safCheckBox;

    private Label resistanceValueLabel;
    private Label gmrRatioLabel;
    private Label sttLabel;
    @SuppressWarnings("unused") // Reserved for future status display
    private Label statusLabelInfo;

    private Spintronic3DRenderer renderer3D;
    private Canvas visualizationCanvas = new Canvas(800, 600);
    private javafx.animation.AnimationTimer simulationTimer;
    private Real currentStep = Real.of(1e-12); // 1 ps
    @SuppressWarnings("unused") // Reserved for future LLG integration
    private Real alpha = Real.of(0.01); // Damping factor
    @SuppressWarnings("unused") // Reserved for future LLG integration 
    private Real gamma = Real.of(1.76e11); // Gyromagnetic ratio
    private LineChart<Number, Number> resistanceChart;
    private XYChart.Series<Number, Number> resistanceSeries;
    
    // STNO / RF Spectrum
    private STNOAnalyzer stnoAnalyzer;
    private LineChart<Number, Number> spectrumChart;
    private XYChart.Series<Number, Number> spectrumSeries;
    private Label peakFreqLabel;
    private int frameCount = 0;
    private static final int SPECTRUM_UPDATE_INTERVAL = 64; // Update spectrum every N frames

    @Override
    protected String getAppTitle() {
        return i18n.get("spintronics.title");
    }

    @Override
    protected Region createMainContent() {
        initModel();
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(15));

        VBox leftPanel = createConfigPanel();
        mainPane.setLeft(leftPanel);

        VBox centerPanel = new VBox(15);
        centerPanel.setPadding(new Insets(0, 0, 0, 15));

        TabPane tabPane = new TabPane();

        // Tab 1: Visualization
        Tab vizTab = new Tab(i18n.get("spintronics.tab.visualization"));
        vizTab.setClosable(false);
        renderer3D = new Spintronic3DRenderer(600, 300);
        vizTab.setContent(renderer3D.getSubScene());
        tabPane.getTabs().add(vizTab);

        // Tab 2: Chart
        Tab chartTab = new Tab(i18n.get("spintronics.tab.chart"));
        chartTab.setClosable(false);
        resistanceChart = createChart();
        chartTab.setContent(resistanceChart);
        tabPane.getTabs().add(chartTab);

        // Tab 3: RF Spectrum (STNO Mode)
        Tab spectrumTab = new Tab("RF Spectrum");
        spectrumTab.setClosable(false);
        VBox spectrumBox = new VBox(10);
        spectrumBox.setPadding(new Insets(10));
        
        NumberAxis freqAxis = new NumberAxis(0, 20, 2); // 0-20 GHz
        freqAxis.setLabel("Frequency (GHz)");
        NumberAxis powerAxis = new NumberAxis();
        powerAxis.setLabel("Power (a.u.)");
        spectrumChart = new LineChart<>(freqAxis, powerAxis);
        spectrumChart.setTitle("Magnetization RF Spectrum");
        spectrumChart.setCreateSymbols(false);
        spectrumChart.setAnimated(false);
        spectrumSeries = new XYChart.Series<>();
        spectrumSeries.setName("PSD");
        spectrumChart.getData().add(spectrumSeries);
        
        peakFreqLabel = new Label("Peak: -- GHz");
        peakFreqLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        spectrumBox.getChildren().addAll(spectrumChart, peakFreqLabel);
        spectrumTab.setContent(spectrumBox);
        tabPane.getTabs().add(spectrumTab);

        centerPanel.getChildren().add(tabPane);

        VBox resultsBox = createResultsBox();
        centerPanel.getChildren().add(resultsBox);

        mainPane.setCenter(centerPanel);
        updateModel();
        return mainPane;
    }

    private void initModel() {
        FerromagneticLayer pinned = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(5e-9), true);
        FerromagneticLayer free = new FerromagneticLayer(SpintronicMaterial.PERMALLOY, Real.of(5e-9), false);
        spinValve = new SpinValve(pinned, SpintronicMaterial.COPPER, Real.of(3e-9), free);
        
        // Initialize STNO Analyzer
        // Buffer size 2048, sample rate = 1/dt = 1e12 Hz
        stnoAnalyzer = new STNOAnalyzer(2048, currentStep.doubleValue());
    }

    private VBox createConfigPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(280);

        pinnedMaterialCombo = new ComboBox<>();
        pinnedMaterialCombo.getItems().addAll(SpintronicMaterial.COBALT, SpintronicMaterial.PERMALLOY, SpintronicMaterial.IRON, SpintronicMaterial.NICKEL);
        pinnedMaterialCombo.setValue(SpintronicMaterial.COBALT);

        pinnedThicknessSlider = new Slider(1, 20, 5);
        spacerMaterialCombo = new ComboBox<>();
        spacerMaterialCombo.getItems().addAll(SpintronicMaterial.COPPER, SpintronicMaterial.SILVER, SpintronicMaterial.ALUMINUM, SpintronicMaterial.RUTHENIUM,
                SpintronicMaterial.PLATINUM, SpintronicMaterial.TANTALUM, SpintronicMaterial.TUNGSTEN);
        spacerMaterialCombo.setValue(SpintronicMaterial.COPPER);

        spacerThicknessSlider = new Slider(1, 10, 3);
        freeMaterialCombo = new ComboBox<>();
        freeMaterialCombo.getItems().addAll(SpintronicMaterial.PERMALLOY, SpintronicMaterial.COBALT, SpintronicMaterial.IRON, SpintronicMaterial.NICKEL);
        freeMaterialCombo.setValue(SpintronicMaterial.PERMALLOY);

        freeThicknessSlider = new Slider(1, 20, 5);
        freeAngleSlider = new Slider(0, 180, 0);

        pinnedMaterialCombo.setOnAction(e -> updateModel());
        pinnedThicknessSlider.valueProperty().addListener((o, ov, nv) -> updateModel());
        spacerMaterialCombo.setOnAction(e -> updateModel());
        spacerThicknessSlider.valueProperty().addListener((o, ov, nv) -> updateModel());
        freeMaterialCombo.setOnAction(e -> updateModel());
        freeThicknessSlider.valueProperty().addListener((o, ov, nv) -> updateModel());
        freeAngleSlider.valueProperty().addListener((o, ov, nv) -> updateModel());

        dampingSlider = new Slider(0.001, 0.5, 0.01);
        dampingSlider.setShowTickLabels(true);

        pmaSlider = new Slider(0, 2e6, 0); // up to 2 MJ/m3
        pmaSlider.setShowTickLabels(true);
        pmaCheckBox = new CheckBox("Enable PMA (Perpendicular)");
        
        dampingSlider.valueProperty().addListener((o, ov, nv) -> updateModel());
        pmaSlider.valueProperty().addListener((o, ov, nv) -> updateModel());
        pmaCheckBox.setOnAction(e -> updateModel());

        temperatureSlider = new Slider(0, 500, 300); // 0 to 500 K
        temperatureSlider.setShowTickLabels(true);
        temperatureSlider.valueProperty().addListener((o, ov, nv) -> updateModel()); 

        sotCurrentSlider = new Slider(0, 1e12, 0); // up to 10^12 A/m2
        sotCurrentSlider.setShowTickLabels(true);
        sotHallSlider = new Slider(-0.5, 0.5, 0.1); // Spin Hall Angle
        sotHallSlider.setShowTickLabels(true);
        sotCurrentSlider.valueProperty().addListener((o, ov, nv) -> updateModel());
        sotHallSlider.valueProperty().addListener((o, ov, nv) -> updateModel());

        areaSlider = new Slider(20, 500, 100); // 20nm to 500nm
        areaSlider.setShowTickLabels(true);
        areaSlider.valueProperty().addListener((o, ov, nv) -> updateModel());

        safCheckBox = new CheckBox("Enable SAF (Co/Ru/Co)");
        safCheckBox.setOnAction(e -> {
            updateModelStructure(); 
            updateModel();
        });

        Button hysteresisBtn = new Button("Run Hysteresis Loop");
        hysteresisBtn.setMaxWidth(Double.MAX_VALUE);
        hysteresisBtn.setOnAction(e -> runHysteresis());

        Button exportBtn = new Button("Export Trace (CSV)");
        exportBtn.getStyleClass().add("button-secondary");
        exportBtn.setMaxWidth(Double.MAX_VALUE);
        exportBtn.setOnAction(e -> exportHistoryToCSV());

        panel.getChildren().addAll(
                new Label(i18n.get("spintronics.label.pinned_layer")), pinnedMaterialCombo, pinnedThicknessSlider,
                safCheckBox,
                new Separator(),
                new Label(i18n.get("spintronics.label.spacer_layer")), spacerMaterialCombo, spacerThicknessSlider,
                new Separator(),
                new Label(i18n.get("spintronics.label.free_layer")), freeMaterialCombo, freeThicknessSlider,
                new Label(i18n.get("spintronics.label.angle")), freeAngleSlider,
                new HBox(10, new Label("Damping (\u03B1)"), dampingSlider),
                new HBox(10, pmaCheckBox, pmaSlider),
                new Separator(),
                new Label("SOT Physics (SHE)"),
                new HBox(10, new Label("J_SOT"), sotCurrentSlider),
                new HBox(10, new Label("\u03B8_SH"), sotHallSlider),
                new Separator(),
                new Label("Geometry"),
                new HBox(10, new Label("Area (nm)"), areaSlider),
                new Separator(),
                new Label("Temperature (K)"), temperatureSlider,
                new Separator(),
                hysteresisBtn,
                exportBtn);
        return panel;
    }

    // Split init into structure creation and parameter update
    private void updateModelStructure() {
        FerromagneticLayer pinned = new FerromagneticLayer(pinnedMaterialCombo.getValue(),
                Real.of(pinnedThicknessSlider.getValue() * 1e-9), true);
        FerromagneticLayer free = new FerromagneticLayer(freeMaterialCombo.getValue(),
                Real.of(freeThicknessSlider.getValue() * 1e-9), false);

        if (safCheckBox.isSelected()) {
            // Create SAF: Pinned1 / Ru / Pinned2
            FerromagneticLayer pinned1 = new FerromagneticLayer(SpintronicMaterial.COBALT, Real.of(2e-9), true);
            spinValve = new SpinValve(pinned1, pinned, SpintronicMaterial.RUTHENIUM, Real.of(0.8e-9),
                    spacerMaterialCombo.getValue(), Real.of(spacerThicknessSlider.getValue() * 1e-9), free);
        } else {
            spinValve = new SpinValve(pinned, spacerMaterialCombo.getValue(),
                    Real.of(spacerThicknessSlider.getValue() * 1e-9), free);
        }
        
        // Sync parameters to layers
        updateModelParameters(spinValve);

        // --- Create Netlist and Initialize Simulator ---
        StringBuilder sb = new StringBuilder();
        sb.append("* Spin Valve App Circuit\n");
        sb.append("V1 1 0 0.5 DC\n"); // 500mV bias
        sb.append("R1 1 2 50\n");     // 50 Ohm source impedance
        sb.append("MTJ1 2 0 0\n");    // MTJ from node 2 to ground
        sb.append(".TRAN 0 10n 1p\n");
        sb.append(".END\n");
        
        SpintronicNetlist parsedNetlist = SpintronicNetlist.parse(sb.toString());
        simulator = new SpintronicCircuitSimulator(parsedNetlist);
        simulator.registerPhysicsModel("MTJ1", spinValve);
        simulator.initialize();

        renderer3D.rebuildStructure(spinValve);
    }

    private VBox createResultsBox() {
        HBox hbox = new HBox(40);
        hbox.setAlignment(Pos.CENTER);
        resistanceValueLabel = new Label("0.0");
        gmrRatioLabel = new Label("0.0 %");
        sttLabel = new Label("0.0");
        statusLabelInfo = new Label("Ready");

        hbox.getChildren().addAll(
                new VBox(5, new Label(i18n.get("spintronics.label.resistance")), resistanceValueLabel),
                new VBox(5, new Label(i18n.get("spintronics.label.gmr_ratio")), gmrRatioLabel),
                new VBox(5, new Label("STT Vector (N\u00B7m)"), sttLabel));
        return new VBox(10, new Separator(), hbox);
    }

    private LineChart<Number, Number> createChart() {
        NumberAxis xAxis = new NumberAxis(0, 180, 45);
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setPrefHeight(250);
        resistanceSeries = new XYChart.Series<>();
        chart.getData().add(resistanceSeries);
        return chart;
    }

    private void updateModel() {
        // Real thicknessP = Real.of(pinnedThicknessSlider.getValue() * 1e-9);
        // Real thicknessS = Real.of(spacerThicknessSlider.getValue() * 1e-9);
        // Real thicknessF = Real.of(freeThicknessSlider.getValue() * 1e-9);
        Real angleRad = Real.of(Math.toRadians(freeAngleSlider.getValue()));

        // FerromagneticLayer pinned = new
        // FerromagneticLayer(pinnedMaterialCombo.getValue(), thicknessP, true);
        // FerromagneticLayer free = new
        // FerromagneticLayer(freeMaterialCombo.getValue(), thicknessF, false);
        // free.setMagnetization(angleRad.cos(), angleRad.sin(), Real.ZERO);

        // spinValve = new SpinValve(pinned, spacerMaterialCombo.getValue(), thicknessS,
        // free);

        // Ensure model structure is up-to-date
        if (spinValve == null ||
                (safCheckBox.isSelected() != spinValve.isSafEnabled())) {
            updateModelStructure();
        } else {
            // Update existing layers' properties if structure is the same
            spinValve.getPinnedLayer().setMaterial(pinnedMaterialCombo.getValue());
            spinValve.getPinnedLayer().setThickness(Real.of(pinnedThicknessSlider.getValue() * 1e-9));
            spinValve.getFreeLayer().setMaterial(freeMaterialCombo.getValue());
            spinValve.getFreeLayer().setThickness(Real.of(freeThicknessSlider.getValue() * 1e-9));
            spinValve.getFreeLayer().setMagnetization(angleRad.cos(), angleRad.sin(), Real.ZERO);
            spinValve.setSpacerMaterial(spacerMaterialCombo.getValue());
            spinValve.setSpacerThickness(Real.of(spacerThicknessSlider.getValue() * 1e-9));
        }

        updateModelParameters(spinValve);

        // Physics Calculation
        Real r = GMREffect.valetFertResistance(spinValve);
        Real rap = GMREffect.valetFertResistance(
                new SpinValve(spinValve.getPinnedLayer(), spinValve.getSpacerMaterial(), spinValve.getSpacerThickness(),
                        createFL(spinValve.getFreeLayer(), Real.PI))); // Sim AP
        Real rp = GMREffect.valetFertResistance(
                new SpinValve(spinValve.getPinnedLayer(), spinValve.getSpacerMaterial(), spinValve.getSpacerThickness(),
                        createFL(spinValve.getFreeLayer(), Real.ZERO))); // Sim P
        Real gmr = rap.subtract(rp).divide(rp);

        resistanceValueLabel.setText(String.format("%.2f \u03A9\u00B7nm\u00B2", r.doubleValue() * 1e18)); // AR product
        gmrRatioLabel.setText(String.format("%.1f %%", gmr.doubleValue() * 100));

        // Torque Calculation (J = 10^11 A/m²)
        Real j = Real.of(1e11);
        Real[] stt = SpinTransport.calculateSTT(j, spinValve.getFreeLayer(), spinValve.getPinnedLayer());
        sttLabel.setText(String.format("[%.1e, %.1e]", stt[0].doubleValue(), stt[1].doubleValue()));

        updateChart();
        renderer3D.update(spinValve, 0.0);

        if (simulationTimer == null) {
            setupSimulation();
        }
    }

    private void updateModelParameters(SpinValve valve) {
        FerromagneticLayer free = valve.getFreeLayer();
        free.setDamping(Real.of(dampingSlider.getValue()));
        free.setTemperature(Real.of(temperatureSlider.getValue()));
        free.setPmaEnergy(Real.of(pmaSlider.getValue()));
        free.setPerpendicularAnisotropy(pmaCheckBox.isSelected());
        
        // SOT Params
        free.setSotCurrentDensity(Real.of(sotCurrentSlider.getValue()));
        free.setSpinHallAngle(Real.of(sotHallSlider.getValue()));
        
        // Geometry - use square area for now
        double side = areaSlider.getValue() * 1e-9;
        valve.setArea(Real.of(side * side));

        if (valve.isSafEnabled()) {
             valve.getSafPinnedLayer1().setTemperature(Real.of(temperatureSlider.getValue()));
             valve.getSafPinnedLayer2().setTemperature(Real.of(temperatureSlider.getValue()));
        } else {
             valve.getPinnedLayer().setTemperature(Real.of(temperatureSlider.getValue()));
        }
    }

    private java.util.List<double[]> historyM = new java.util.ArrayList<>();
    
    private void exportHistoryToCSV() {
        java.io.File file = new java.io.File("spintronics_trace.csv");
        try (java.io.PrintWriter pw = new java.io.PrintWriter(file)) {
            pw.println("Sample,mx,my,mz");
            for (int i=0; i < historyM.size(); i++) {
                double[] m = historyM.get(i);
                pw.println(String.format("%d,%.6f,%.6f,%.6f", i, m[0], m[1], m[2]));
            }
            pw.flush();
            statusLabelInfo.setText("Exported " + historyM.size() + " samples to " + file.getName());
        } catch (Exception e) {
            statusLabelInfo.setText("Export failed: " + e.getMessage());
        }
    }

    private void runHysteresis() {
        resistanceChart.getData().clear();
        java.util.List<XYChart.Series<Number, Number>> results = HysteresisExperiment.runSweep(spinValve, -10000, 10000,
                200);
        resistanceChart.getData().addAll(results);
    }

    private SpintronicCircuitSimulator simulator;
    
    // ...

    private void setupSimulation() {
        // Initialize Circuit and Physics
        updateModelStructure(); // Ensure SpinValve and Netlist are in sync
        
        simulationTimer = new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                // Placeholder for simulation controls visibility
                boolean simulationControlsVisible = true; 
                if (simulationControlsVisible) {
                    runPhysicsStep();
                }
            }
        };
    }

    private void runPhysicsStep() {
        if (simulator == null) {
            updateModelStructure();
        }
    
        // Sim Step
        simulator.step(currentStep.doubleValue());
        
        // Update UI 
        FerromagneticLayer free = spinValve.getFreeLayer();
        Real[] m = free.getMagnetization();
        
        Real r = GMREffect.valetFertResistance(spinValve);
        resistanceValueLabel.setText(String.format("%.2f Ohms", r.doubleValue() * 1e18));

        // Compute Current for Visual Glow
        double currentVal = 0;
        Vector<Real> vLast = simulator.getLastVoltage();
        if (vLast != null && vLast.dimension() >= 2) {
             double v2 = vLast.get(1).doubleValue(); // Node 2 voltage
             currentVal = v2 / r.doubleValue();
        }

        renderer3D.update(spinValve, currentVal);
        
        // STNO Analyze & History
        stnoAnalyzer.recordSample(m[0]);
        if (historyM.size() < 10000) {
            historyM.add(new double[]{m[0].doubleValue(), m[1].doubleValue(), m[2].doubleValue()});
        }
        
        // Update spectrum periodically
        frameCount++;
        if (stnoAnalyzer.isReady() && frameCount % SPECTRUM_UPDATE_INTERVAL == 0) {
            updateSpectrum();
        }
    }
    
    private void updateSpectrum() {
        Real[] psd = stnoAnalyzer.computePowerSpectrum();
        double[] freq = stnoAnalyzer.getFrequencyAxis();
        
        spectrumSeries.getData().clear();
        
        // Only plot up to 20 GHz (or Nyquist/2)
        int maxIndex = Math.min(freq.length, (int)(20e9 / (stnoAnalyzer.getSampleRate() / stnoAnalyzer.getBufferSize())));
        
        for (int i = 1; i < maxIndex; i++) { // Skip DC
            double freqGHz = freq[i] / 1e9;
            spectrumSeries.getData().add(new XYChart.Data<>(freqGHz, psd[i].doubleValue()));
        }
        
        // Update peak frequency label
        double peakGHz = stnoAnalyzer.getPeakFrequency() / 1e9;
        peakFreqLabel.setText(String.format("Peak: %.2f GHz", peakGHz));
        
        // WOW: Color peak label based on freq (Red: low, Blue: high)
        double hue = Math.max(0, Math.min(240, (peakGHz / 20.0) * 240));
        peakFreqLabel.setTextFill(Color.hsb(hue, 0.8, 0.9));
        
        // Apply styling to serie
        if (spectrumSeries.getNode() != null) {
            spectrumSeries.getNode().setStyle("-fx-stroke: hsb(" + hue + ", 80%, 90%); -fx-stroke-width: 3px;");
        }
    }

    @Override
    public void onRun() {
        if (simulationTimer != null)
            simulationTimer.start();
    }

    @Override
    public void onStop() {
        if (simulationTimer != null)
            simulationTimer.stop();
    }

    private FerromagneticLayer createFL(FerromagneticLayer template, Real angle) {
        FerromagneticLayer fl = new FerromagneticLayer(template.getMaterial(), template.getThickness(), false);
        fl.setMagnetization(angle.cos(), angle.sin(), Real.ZERO);
        return fl;
    }

    private void updateChart() {
        resistanceSeries.getData().clear();
        for (double a = 0; a <= 180; a += 10) {
            FerromagneticLayer fl = createFL(spinValve.getFreeLayer(), Real.of(Math.toRadians(a)));
            SpinValve temp = new SpinValve(spinValve.getPinnedLayer(), spinValve.getSpacerMaterial(),
                    spinValve.getSpacerThickness(), fl);
            resistanceSeries.getData()
                    .add(new XYChart.Data<>(a, GMREffect.valetFertResistance(temp).doubleValue() * 1e18));
        }
    }

    @SuppressWarnings("unused") // Reserved for 2D canvas fallback mode
    private void draw() {
        GraphicsContext gc = visualizationCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 800, 300);
        // ... (Drawing logic scaled to nm)
        gc.setFill(Color.BLUE);
        gc.fillRect(100, 50, 400, 40); // Layer 1
        gc.setFill(Color.ORANGE);
        gc.fillRect(100, 90, 400, 20); // Spacer
        gc.setFill(Color.RED);
        gc.fillRect(100, 110, 400, 40); // Free
    }
}
