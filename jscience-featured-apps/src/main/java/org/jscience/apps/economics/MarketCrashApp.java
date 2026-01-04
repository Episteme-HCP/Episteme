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

package org.jscience.apps.economics;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import org.jscience.apps.framework.ChartFactory;
import org.jscience.apps.framework.FeaturedAppBase;
import org.jscience.economics.analysis.TechnicalIndicators;
import org.jscience.economics.loaders.FinancialMarketLoader;
import org.jscience.economics.loaders.FinancialMarketLoader.Candle;
import org.jscience.mathematics.numbers.real.Real;

import java.util.ArrayList;
import java.util.List;

public class MarketCrashApp extends FeaturedAppBase {
    private LineChart<Number, Number> priceChart;
    private LineChart<Number, Number> rsiChart;
    private XYChart.Series<Number, Number> priceSeries;
    private XYChart.Series<Number, Number> smaSeries;
    private XYChart.Series<Number, Number> upperBB;
    private XYChart.Series<Number, Number> lowerBB;
    private XYChart.Series<Number, Number> rsiSeries;
    private ListView<String> alertLog;
    private Label riskLabel, currentPriceLabel, smaLabel, rsiLabel;
    private Slider smaPeriodSlider, rsiPeriodSlider;
    private CheckBox showSMA, showBollinger;

    // Data
    private List<Candle> marketData;
    private Timeline animationTimeline;
    private int currentIndex = 0;

    @Override
    protected String getAppTitle() {
        return i18n.get("market.title");
    }

    @Override
    public String getDescription() {
        return i18n.get("market.desc");
    }

    @Override
    protected Region createMainContent() {
        SplitPane mainSplit = new SplitPane();
        mainSplit.setOrientation(Orientation.HORIZONTAL);

        // Left: Charts
        VBox chartArea = createChartArea();

        // Right: Controls and indicators
        VBox controlPanel = createControlPanel();
        controlPanel.setPrefWidth(320);
        controlPanel.setMinWidth(280);

        mainSplit.getItems().addAll(chartArea, controlPanel);
        mainSplit.setDividerPositions(0.72);

        // Generate sample data after UI is ready (so log works)
        generateSampleData();

        return mainSplit;
    }

    @SuppressWarnings("unchecked")
    private VBox createChartArea() {
        VBox area = new VBox(10);
        area.setPadding(new Insets(10));

        // Price Chart
        Label priceTitle = new Label("Ã°Å¸â€œË† " + i18n.get("market.panel.chart"));
        priceTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        priceChart = ChartFactory.createLineChart(i18n.get("market.panel.chart"), i18n.get("market.label.day"),
                i18n.get("market.label.price_axis"));

        priceChart.setAnimated(false);
        priceChart.setCreateSymbols(false);

        priceSeries = ChartFactory.createSeries(i18n.get("market.series.price"));
        smaSeries = ChartFactory.createSeries(i18n.get("market.series.sma"));
        upperBB = ChartFactory.createSeries(i18n.get("market.series.upperbb"));
        lowerBB = ChartFactory.createSeries(i18n.get("market.series.lowerbb"));

        priceChart.getData().addAll(priceSeries, smaSeries, upperBB, lowerBB);
        VBox.setVgrow(priceChart, Priority.ALWAYS);

        // RSI Chart
        Label rsiTitle = new Label("Ã°Å¸â€œÅ  " + i18n.get("market.indicator.rsi"));
        rsiTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        rsiChart = ChartFactory.createLineChart("", i18n.get("market.label.day"), i18n.get("market.indicator.rsi"));

        rsiChart.setAnimated(false);
        rsiChart.setCreateSymbols(false);
        rsiChart.setPrefHeight(150);
        rsiChart.setLegendVisible(false);

        rsiSeries = ChartFactory.createSeries(i18n.get("market.indicator.rsi"));
        rsiChart.getData().add(rsiSeries);

        area.getChildren().addAll(priceTitle, priceChart, rsiTitle, rsiChart);
        return area;
    }

    private VBox createControlPanel() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ddd; -fx-border-width: 0 0 0 1;");

        // Risk Assessment
        Label riskTitle = new Label(i18n.get("market.risk.title"));
        riskTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        riskLabel = new Label(i18n.get("market.risk.none"));
        riskLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10;");

        HBox riskBox = new HBox(riskLabel);
        riskBox.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 8; -fx-padding: 15;");

        // Current Stats
        Label statsTitle = new Label(i18n.get("market.stats.title"));
        statsTitle.setStyle("-fx-font-weight: bold;");

        currentPriceLabel = new Label(java.text.MessageFormat.format(i18n.get("market.label.price"), "--"));
        smaLabel = new Label(java.text.MessageFormat.format(i18n.get("market.label.sma"), 20, "--"));
        rsiLabel = new Label(java.text.MessageFormat.format(i18n.get("market.label.rsi"), 14, "--"));

        VBox statsBox = new VBox(5, currentPriceLabel, smaLabel, rsiLabel);
        statsBox.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10; -fx-background-radius: 5;");

        // Indicator Settings
        Label settingsTitle = new Label(i18n.get("market.settings.title"));
        settingsTitle.setStyle("-fx-font-weight: bold;");

        VBox smaBox = createSliderBox(i18n.get("market.slider.sma"), 5, 50, 20);
        smaPeriodSlider = (Slider) smaBox.getChildren().get(1);

        VBox rsiBox = createSliderBox(i18n.get("market.slider.rsi"), 7, 21, 14);
        rsiPeriodSlider = (Slider) rsiBox.getChildren().get(1);

        showSMA = new CheckBox(i18n.get("market.check.sma"));
        showSMA.setSelected(true);
        showSMA.setOnAction(e -> smaSeries.getNode().setVisible(showSMA.isSelected()));

        showBollinger = new CheckBox(i18n.get("market.check.bb"));
        showBollinger.setSelected(true);
        showBollinger.setOnAction(e -> {
            upperBB.getNode().setVisible(showBollinger.isSelected());
            lowerBB.getNode().setVisible(showBollinger.isSelected());
        });

        // Alert Log
        Label alertTitle = new Label(i18n.get("market.log.title"));
        alertTitle.setStyle("-fx-font-weight: bold;");

        alertLog = new ListView<>();
        alertLog.setPrefHeight(180);

        panel.getChildren().addAll(riskTitle, riskBox, new Separator(), statsTitle, statsBox, new Separator(),
                settingsTitle, smaBox, rsiBox, showSMA, showBollinger, new Separator(), alertTitle, alertLog);

        return panel;
    }

    private VBox createSliderBox(String name, double min, double max, double initial) {
        VBox box = new VBox(2);
        Label label = new Label(name + " " + (int) initial);
        Slider slider = new Slider(min, max, initial);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit((max - min) / 5);
        slider.valueProperty().addListener((o, ov, nv) -> label.setText(name + " " + nv.intValue()));
        box.getChildren().addAll(label, slider);
        return box;
    }

    private void generateSampleData() {
        // Load Real Data (S&P 500 Sample)
        try (var is = getClass().getResourceAsStream("data/SP500_Sample.csv")) {
            if (is != null) {
                marketData = FinancialMarketLoader.loadCSV(is, "USD");
                marketData = FinancialMarketLoader.loadCSV(is, "USD");
                log(java.text.MessageFormat.format(i18n.get("market.log.loaded"), marketData.size()));
            } else {
                log(i18n.get("market.error.data_not_found"));
                marketData = new ArrayList<>();
            }
        } catch (Exception e) {
            log(i18n.get("market.error.load", e.getMessage()));
            marketData = new ArrayList<>();
        }

    }

    @Override
    public void onRun() {
        clearCharts();
        currentIndex = 50; // Start after enough data for indicators

        animationTimeline = new Timeline(new KeyFrame(Duration.millis(30), e -> {
            if (currentIndex < marketData.size()) {
                addDataPoint(currentIndex);
                updateIndicators();
                currentIndex++;
                setProgress((double) currentIndex / marketData.size());
            } else {
                stopAnimation();
            }
        }));
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.play();
        setStatus(i18n.get("status.running"));
    }

    private void addDataPoint(int idx) {
        double close = marketData.get(idx).close.getAmount().doubleValue();
        priceSeries.getData().add(new XYChart.Data<>(idx, close));

        // Calculate and add SMA
        int smaPeriod = (int) smaPeriodSlider.getValue();
        if (idx >= smaPeriod) {
            List<Candle> subset = marketData.subList(0, idx + 1);
            Real sma = TechnicalIndicators.sma(subset, smaPeriod);
            if (sma != null) {
                smaSeries.getData().add(new XYChart.Data<>(idx, sma.doubleValue()));
            }

            // Bollinger Bands
            Real[] bb = TechnicalIndicators.bollingerBands(subset, smaPeriod, 2.0);
            if (bb != null) {
                lowerBB.getData().add(new XYChart.Data<>(idx, bb[0].doubleValue()));
                upperBB.getData().add(new XYChart.Data<>(idx, bb[2].doubleValue()));
            }
        }

        // RSI
        int rsiPeriod = (int) rsiPeriodSlider.getValue();
        if (idx >= rsiPeriod + 1) {
            List<Candle> subset = marketData.subList(0, idx + 1);
            Real rsi = TechnicalIndicators.rsi(subset, rsiPeriod);
            if (rsi != null) {
                rsiSeries.getData().add(new XYChart.Data<>(idx, rsi.doubleValue()));
            }
        }
    }

    private void updateIndicators() {
        if (currentIndex >= marketData.size())
            return;

        double close = marketData.get(currentIndex).close.getAmount().doubleValue();
        currentPriceLabel
                .setText(java.text.MessageFormat.format(i18n.get("market.label.price"), String.format("$%.2f", close)));

        List<Candle> subset = marketData.subList(0, currentIndex + 1);

        Real sma = TechnicalIndicators.sma(subset, (int) smaPeriodSlider.getValue());
        if (sma != null) {
            smaLabel.setText(java.text.MessageFormat.format(i18n.get("market.label.sma"),
                    (int) smaPeriodSlider.getValue(), String.format("$%.2f", sma.doubleValue())));
        }

        Real rsi = TechnicalIndicators.rsi(subset, (int) rsiPeriodSlider.getValue());
        if (rsi != null) {
            double rsiVal = rsi.doubleValue();
            rsiLabel.setText(java.text.MessageFormat.format(i18n.get("market.label.rsi"),
                    (int) rsiPeriodSlider.getValue(), String.format("%.1f", rsiVal)));

            if (rsiVal < 30) {
                log(java.text.MessageFormat.format(i18n.get("market.log.oversold"), currentIndex,
                        String.format("%.1f", rsiVal)));
            } else if (rsiVal > 70) {
                log(java.text.MessageFormat.format(i18n.get("market.log.overbought"), currentIndex,
                        String.format("%.1f", rsiVal)));
            }
        }

        // Risk assessment
        updateRiskLevel(subset, close, sma, rsi);
    }

    private void updateRiskLevel(List<Candle> candles, double price, Real sma, Real rsi) {
        int score = 0;

        // Below SMA
        if (sma != null && price < sma.doubleValue() * 0.95)
            score += 2;

        // RSI extreme
        if (rsi != null) {
            double r = rsi.doubleValue();
            if (r < 25 || r > 75)
                score += 2;
            else if (r < 30 || r > 70)
                score += 1;
        }

        // Volatility
        Real vol = TechnicalIndicators.volatility(candles, 20);
        Real volLong = TechnicalIndicators.volatility(candles, 50);
        if (vol != null && volLong != null && vol.doubleValue() > volLong.doubleValue() * 2) {
            score += 2;
        }

        // Price decline
        Real pctChange = TechnicalIndicators
                .percentChange(candles.subList(Math.max(0, candles.size() - 20), candles.size()));
        if (pctChange != null && pctChange.doubleValue() < -10)
            score += 2;

        String riskText;
        Color color;
        if (score >= 5) {
            riskText = i18n.get("market.risk.extreme");
            color = Color.DARKRED;
            log(java.text.MessageFormat.format(i18n.get("market.log.extreme"), currentIndex));
        } else if (score >= 4) {
            riskText = i18n.get("market.risk.high");
            color = Color.DARKORANGE;
        } else if (score >= 2) {
            riskText = i18n.get("market.risk.moderate");
            color = Color.GOLDENROD;
        } else {
            riskText = i18n.get("market.risk.low");
            color = Color.DARKGREEN;
        }

        riskLabel.setText(riskText);
        riskLabel.setTextFill(color);
    }

    private void stopAnimation() {
        if (animationTimeline != null)
            animationTimeline.stop();
        setStatus(i18n.get("status.complete"));
        setProgress(0);
        log(java.text.MessageFormat.format(i18n.get("market.log.complete"), currentIndex));
    }

    @Override
    public void onPause() {
        if (animationTimeline != null)
            animationTimeline.pause();
        setStatus(i18n.get("status.paused"));
    }

    @Override
    public void onStop() {
        stopAnimation();
    }

    @Override
    public void onReset() {
        stopAnimation();
        clearCharts();
        currentIndex = 0;
        riskLabel.setText("--");
        setStatus(i18n.get("status.ready"));
    }

    private void clearCharts() {
        priceSeries.getData().clear();
        smaSeries.getData().clear();
        upperBB.getData().clear();
        lowerBB.getData().clear();
        rsiSeries.getData().clear();
    }

    private void log(String msg) {
        String ts = java.time.LocalTime.now().toString().substring(0, 8);
        alertLog.getItems().add(0, "[" + ts + "] " + msg);
        if (alertLog.getItems().size() > 100) {
            alertLog.getItems().remove(alertLog.getItems().size() - 1);
        }
    }

    @Override
    protected void configureFileChooser(FileChooser chooser) {
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
