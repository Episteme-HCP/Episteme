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

import org.jscience.ui.i18n.I18nManager;
import org.jscience.apps.framework.ChartFactory;
import org.jscience.apps.framework.FeaturedAppBase;
import java.text.MessageFormat;

public class TitrationApp extends FeaturedAppBase {
    private double volumeBaseAdded = 0.0; // mL
    private double maxBuretteVol = 50.0; // mL
    private double volumeAcid = 50.0; // mL
    private double concAcid = 0.1; // M
    private double concBase = 0.1; // M

    private boolean valveOpen;
    private double flowRate;
    private AcidType selectedAcid;

    public TitrationApp() {
        super();
        try {
            this.valveOpen = false;
            this.flowRate = 0.0;
            this.selectedAcid = AcidType.HCL;
        } catch (Throwable t) {
            System.err.println("CRITICAL: Failed to initialize TitrationApp: " + t.getMessage());
            t.printStackTrace();
        }
    }

    // Multi-protic acid types with pKa values
    private enum AcidType {
        HCL("titration.acid.hcl", "Hydrochloric Acid (HCl)", new double[] { -7.0 }), // Strong acid
        H2SO4("titration.acid.h2so4", "Sulfuric Acid (H2SO4)", new double[] { -3.0, 1.99 }), // Diprotic
        H3PO4("titration.acid.h3po4", "Phosphoric Acid (H3PO4)", new double[] { 2.15, 7.20, 12.35 }), // Triprotic
        H2CO3("titration.acid.h2co3", "Carbonic Acid (H2CO3)", new double[] { 6.35, 10.33 }), // Diprotic
        ACETIC("titration.acid.acetic", "Acetic Acid (CH3COOH)", new double[] { 4.76 }); // Weak monoprotic

        final String key;
        final String def;
        final double[] pKa; // pKa values for each dissociation step

        AcidType(String key, String def, double[] pKa) {
            this.key = key;
            this.def = def;
            this.pKa = pKa;
        }

        public int getProtonCount() {
            return pKa.length;
        }

        @Override
        public String toString() {
            return I18nManager.getInstance().get(key, def);
        }
    }

    // UI
    private Canvas labCanvas;
    private XYChart.Series<Number, Number> phSeries;
    private Label phLabel;
    private Label volumeLabel;
    private Slider valveSlider;
    private ComboBox<Indicator> indicatorSelector;
    private ComboBox<AcidType> acidSelector;
    private Color indicatorColor = Color.TRANSPARENT;

    // UI references for localization
    private Label setupTitleLabel;
    private Label acidTypeLabel;
    private Label indicatorLabel;
    private Label titrantLabel;
    private LineChart<Number, Number> phChart;

    // Indicators with their pH ranges and colors
    private enum Indicator {
        PHENOLPHTHALEIN("titration.ind.phen", "Phenolphthalein", 8.2, 10.0, Color.TRANSPARENT, Color.DEEPPINK),
        METHYL_ORANGE("titration.ind.methyl", "Methyl Orange", 3.1, 4.4, Color.RED, Color.YELLOW),
        BROMOTHYMOL_BLUE("titration.ind.brom", "Bromothymol Blue", 6.0, 7.6, Color.YELLOW, Color.BLUE),
        LITMUS("titration.ind.litmus", "Litmus", 5.0, 8.0, Color.RED, Color.BLUE),
        UNIVERSAL("titration.ind.univ", "Universal Indicator", 1.0, 14.0, Color.RED, Color.VIOLET);

        final String key;
        final String def;
        final double pHLow;
        final double pHHigh;
        final Color colorAcid;
        final Color colorBase;

        Indicator(String key, String def, double pHLow, double pHHigh, Color colorAcid, Color colorBase) {
            this.key = key;
            this.def = def;
            this.pHLow = pHLow;
            this.pHHigh = pHHigh;
            this.colorAcid = colorAcid;
            this.colorBase = colorBase;
        }

        @Override
        public String toString() {
            return I18nManager.getInstance().get(key, def);
        }
    }

    // Constants

    @Override
    protected String getAppTitle() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.titrationapp.name", "Acid-Base Titration");
    }

    @Override
    public String getName() {
        return getAppTitle();
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.titrationapp.desc", "Simulate titration experiments with various acids and indicators.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.titrationapp.longdesc", "Virtual chemistry laboratory for performing acid-base titrations. Supports monoprotic and multi-protic acids (HCl, H2SO4, H3PO4, etc.) with a range of indicators including Phenolphthalein and Methyl Orange. Features real-time pH curve plotting and laboratory equipment visualization.");
    }

    @Override
    public boolean hasEditMenu() {
        return false;
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
        reset();
        return split;
    }

    private VBox createLabPane() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.getStyleClass().add("viewer-root");

        labCanvas = new Canvas(300, 600);
        drawLab();

        box.getChildren().add(labCanvas);
        VBox.setVgrow(labCanvas, Priority.ALWAYS);
        return box;
    }

    private VBox createDataPane() {
        VBox box = new VBox(15);
        box.getStyleClass().add("viewer-sidebar");

        // Chart
        phChart = ChartFactory.createLineChart(i18n.get("titration.panel.chart", "pH Curve"),
                i18n.get("titration.label.volume", "Volume Base Added (mL)"), i18n.get("titration.label.ph_axis", "pH"));
        phSeries = new XYChart.Series<>();
        phSeries.setName(i18n.get("titration.series.ph", "pH Level"));

        phChart.getData().add(phSeries);

        // Status
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        phLabel = new Label();
        phLabel.getStyleClass().add("header-label");
        phLabel.setStyle("-fx-font-size: 24px;");

        volumeLabel = new Label();

        valveSlider = new Slider(0, 1.0, 0);
        valveSlider.setShowTickLabels(true);
        valveSlider.setShowTickMarks(true);
        valveSlider.valueProperty().addListener((o, ov, nv) -> {
            flowRate = nv.doubleValue() * 0.2; // Max 0.2 mL/frame
            valveOpen = flowRate > 0;
        });

        // Acid type selector for multi-protic acids
        acidSelector = new ComboBox<>();
        acidSelector.getItems().addAll(AcidType.values());
        acidSelector.setValue(AcidType.HCL);
        acidSelector.setMaxWidth(Double.MAX_VALUE);
        acidSelector.setOnAction(e -> {
            selectedAcid = acidSelector.getValue();
            reset();
        });

        // Indicator selector
        indicatorSelector = new ComboBox<>();
        indicatorSelector.getItems().addAll(Indicator.values());
        indicatorSelector.setValue(Indicator.PHENOLPHTHALEIN);
        indicatorSelector.setMaxWidth(Double.MAX_VALUE);
        indicatorSelector.setOnAction(e -> calculatePH()); // Update color immediately

        setupTitleLabel = new Label(i18n.get("titration.panel.setup", "Setup"));
        setupTitleLabel.getStyleClass().add("header-label");
        acidTypeLabel = new Label(i18n.get("titration.label.acidtype", "Acid Solution:"));
        acidTypeLabel.getStyleClass().add("description-label");
        indicatorLabel = new Label(i18n.get("titration.label.indicator", "Indicator:"));
        indicatorLabel.getStyleClass().add("description-label");
        titrantLabel = new Label(i18n.get("titration.label.titrant", "Titrant Valve:"));
        titrantLabel.getStyleClass().add("description-label");

        box.getChildren().addAll(
                setupTitleLabel,
                acidTypeLabel,
                acidSelector,
                phChart,
                phLabel, volumeLabel,
                new Separator(),
                indicatorLabel,
                indicatorSelector,
                new Separator(),
                titrantLabel,
                valveSlider);
        VBox.setVgrow(phChart, Priority.ALWAYS);

        // Initial update
        calculatePH();
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
            if (volumeBaseAdded > maxBuretteVol)
                volumeBaseAdded = maxBuretteVol;

            calculatePH();
        }
    }

    private void calculatePH() {
        // Multi-protic acid titration calculation
        double molesAcidInitial = volumeAcid / 1000.0 * concAcid;
        double molesBaseAdded = volumeBaseAdded / 1000.0 * concBase;
        double totalVolL = (volumeAcid + volumeBaseAdded) / 1000.0;

        int protonCount = selectedAcid.getProtonCount();
        double[] pKa = selectedAcid.pKa;

        double ph;

        // Calculate equivalence point for multi-protic acids
        // Each equivalence point occurs at n * molesAcidInitial moles of base
        double molesPerProton = molesAcidInitial;


        if (pKa[0] < 0) {
            // Strong acid - complete dissociation
            if (molesAcidInitial > molesBaseAdded) {
                double concH = (molesAcidInitial - molesBaseAdded) / totalVolL;
                ph = -Math.log10(Math.max(1e-14, concH));
                if (ph > 6.9)
                    ph = 6.9; // Avoid jumping too high before equivalence
            } else if (molesBaseAdded > molesAcidInitial * protonCount + 1e-9) {
                double concOH = (molesBaseAdded - molesAcidInitial * protonCount) / totalVolL;
                double pOH = -Math.log10(Math.max(1e-7, concOH));
                ph = 14.0 - pOH;
                if (ph < 7.1)
                    ph = 7.1; // Ensure it stays basic
            } else {
                ph = 7.0;
            }
        } else {
            // Weak/multi-protic acid - use Henderson-Hasselbalch
            double molesRatio = molesBaseAdded / molesPerProton;

            if (molesRatio < 0.01) {
                // Before any significant titration - weak acid solution
                ph = 0.5 * (pKa[0] - Math.log10(concAcid * 1000.0 / totalVolL / 1000.0));
            } else if (molesRatio >= protonCount) {
                // Past final equivalence point - excess base
                double excessBase = molesBaseAdded - molesAcidInitial * protonCount;
                double concOH = excessBase / totalVolL;
                ph = 14.0 + Math.log10(Math.max(1e-7, concOH));
                if (ph < 7.1 && molesRatio > protonCount)
                    ph = 7.1;
                else if (molesRatio == protonCount)
                    ph = 8.5; // Approximation for weak acid eq. point
            } else {
                // Between start and final equivalence point
                int step = (int) molesRatio; // Which dissociation step
                double stepProgress = molesRatio - step; // Progress within step

                if (step < protonCount) {
                    double currentPKa = pKa[step];
                    if (Math.abs(stepProgress - 0.5) < 0.01) {
                        // At half-equivalence point: pH = pKa
                        ph = currentPKa;
                    } else if (stepProgress < 0.5) {
                        // Before half-equivalence: more acid form
                        double ratio = (0.5 - stepProgress) / (0.5 + stepProgress);
                        ph = currentPKa - Math.log10(Math.max(0.01, ratio));
                    } else {
                        // After half-equivalence: more conjugate base form
                        double ratio = (stepProgress - 0.5) / (1.5 - stepProgress);
                        ph = currentPKa + Math.log10(Math.max(0.01, ratio));
                    }
                } else {
                    ph = 7.0;
                }
            }
        }

        // Clamp pH to reasonable range
        ph = Math.max(0, Math.min(14, ph));

        // Update indicator color based on selected indicator
        Indicator ind = indicatorSelector != null ? indicatorSelector.getValue() : Indicator.PHENOLPHTHALEIN;
        if (ind == null)
            ind = Indicator.PHENOLPHTHALEIN;

        if (ph <= ind.pHLow) {
            indicatorColor = ind.colorAcid.deriveColor(0, 1, 1, 0.6);
        } else if (ph >= ind.pHHigh) {
            indicatorColor = ind.colorBase.deriveColor(0, 1, 1, 0.6);
        } else {
            // Interpolate between acid and base colors
            double t = (ph - ind.pHLow) / (ind.pHHigh - ind.pHLow);
            indicatorColor = ind.colorAcid.interpolate(ind.colorBase, t).deriveColor(0, 1, 1, 0.6);
        }

        // Update UI
        phLabel.setText(MessageFormat.format(i18n.get("titration.label.ph", "pH: {0}"), String.format("%.2f", ph)));
        volumeLabel.setText(
                MessageFormat.format(i18n.get("titration.label.voladded", "Volume: {0} mL"), String.format("%.2f", volumeBaseAdded)));

        // Chart sampling (don't flood chart)
        if (phSeries.getData().isEmpty() ||
                Math.abs(phSeries.getData().get(phSeries.getData().size() - 1).getXValue().doubleValue()
                        - volumeBaseAdded) > 0.5) {
            phSeries.getData().add(new XYChart.Data<>(volumeBaseAdded, ph));
        }
    }

    @Override
    protected void updateLocalizedUI() {
        if (setupTitleLabel != null)
            setupTitleLabel.setText(i18n.get("titration.panel.setup", "Setup"));
        if (acidTypeLabel != null)
            acidTypeLabel.setText(i18n.get("titration.label.acidtype", "Acid Solution:"));
        if (indicatorLabel != null)
            indicatorLabel.setText(i18n.get("titration.label.indicator", "Indicator:"));
        if (titrantLabel != null)
            titrantLabel.setText(i18n.get("titration.label.titrant", "Titrant Valve:"));

        if (phChart != null) {
            phChart.setTitle(i18n.get("titration.panel.chart", "pH Curve"));
            if (phChart.getXAxis() instanceof NumberAxis) {
                ((NumberAxis) phChart.getXAxis()).setLabel(i18n.get("titration.label.volume", "Volume Base Added (mL)"));
            }
            if (phChart.getYAxis() instanceof NumberAxis) {
                ((NumberAxis) phChart.getYAxis()).setLabel(i18n.get("titration.label.ph_axis", "pH"));
            }
            if (!phChart.getData().isEmpty()) {
                phChart.getData().get(0).setName(i18n.get("titration.series.ph", "pH Level"));
            }
        }
        calculatePH(); // Refresh labels
    }

    private void drawLab() {
        GraphicsContext gc = labCanvas.getGraphicsContext2D();
        double w = labCanvas.getWidth();
        double h = labCanvas.getHeight();

        gc.clearRect(0, 0, w, h);

        double centerX = w / 2;

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
        gc.strokePolyline(new double[] { beakX, beakX, beakX + beakW, beakX + beakW },
                new double[] { beakY, beakY + beakH, beakY + beakH, beakY }, 4);

        // Liquid in Beaker
        // Volume simulates liquid height
        double baseLiquidHeight = 40; // Initial
        double addedHeight = (volumeBaseAdded / volumeAcid) * 20;
        double totalH = baseLiquidHeight + addedHeight;

        gc.setFill(indicatorColor);
        gc.fillRect(beakX + 1, beakY + beakH - totalH, beakW - 2, totalH);

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

    @Override
    public void onReset() {
        reset();
    }

    @Override
    protected void doNew() {
        reset();
    }

    @Override
    protected void addAppHelpTopics(org.jscience.apps.framework.HelpDialog dialog) {
        dialog.addTopic("Chemistry", "Acid-Base Titration",
                "Simulate titration experiments:\n\n" +
                        "\u2022 **Titration**: Determining concentration of an acid by neutralizing it with a base of known concentration.\n"
                        +
                        "\u2022 **Equivalence Point**: Where moles of acid equals moles of base (adjusted for stoichiometry).\n"
                        +
                        "\u2022 **Indicators**: Change color at specific pH ranges to visually signal the end point.\n"
                        +
                        "\u2022 **pH Curve**: Shows the change in pH as titrant is added.",
                null);
    }

    @Override
    protected void addAppTutorials(org.jscience.apps.framework.HelpDialog dialog) {
        dialog.addTopic("Tutorial", "Performing a Titration",
                "1. **Select Acid**: Choose the acid type (e.g., HCl, H2SO4).\n" +
                        "2. **Select Indicator**: Choose an appropriate indicator for the expected pH change.\n" +
                        "3. **Open Valve**: Move the slider to start adding the base titrant.\n" +
                        "4. **Observe**: Watch the beaker color and the pH curve.\n" +
                        "5. **Stop**: Close the valve when the color changes permanently.",
                null);
    }

    @Override
    protected byte[] serializeState() {
        java.util.Properties props = new java.util.Properties();
        props.setProperty("volumeBaseAdded", String.valueOf(volumeBaseAdded));
        if (selectedAcid != null) {
            props.setProperty("acid", selectedAcid.name());
        }
        if (indicatorSelector.getValue() != null) {
            props.setProperty("indicator", indicatorSelector.getValue().name());
        }
        props.setProperty("valve", String.valueOf(valveSlider.getValue()));

        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try {
            props.store(baos, "Titration State");
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

            volumeBaseAdded = Double.parseDouble(props.getProperty("volumeBaseAdded", "0"));

            String acidName = props.getProperty("acid");
            if (acidName != null) {
                acidSelector.setValue(AcidType.valueOf(acidName));
                selectedAcid = acidSelector.getValue();
            }

            String indName = props.getProperty("indicator");
            if (indName != null) {
                indicatorSelector.setValue(Indicator.valueOf(indName));
            }

            valveSlider.setValue(Double.parseDouble(props.getProperty("valve", "0")));

            // Refresh plot
            phSeries.getData().clear();
            calculatePH();
        } catch (Exception e) {
            showError("Load Error", "Failed to restore state: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.chemistry", "Chemistry");
    }
}
