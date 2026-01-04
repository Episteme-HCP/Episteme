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

package org.jscience.apps.framework;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating charts used in JScience Killer Apps.
 * Provides line charts, bar charts, area charts, and real-time updates.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class ChartFactory {

    private ChartFactory() {
    }

    /**
     * Creates a line chart with the given title and axis labels.
     */
    public static LineChart<Number, Number> createLineChart(String title, String xLabel, String yLabel) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(xLabel);
        xAxis.setAutoRanging(true);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yLabel);
        yAxis.setAutoRanging(true);

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setCreateSymbols(false);
        chart.setAnimated(false);
        chart.setLegendVisible(true);

        return chart;
    }

    /**
     * Creates a series for a line chart.
     */
    public static XYChart.Series<Number, Number> createSeries(String name) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);
        return series;
    }

    /**
     * Creates an area chart (useful for stacked visualizations).
     */
    public static AreaChart<Number, Number> createAreaChart(String title, String xLabel, String yLabel) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(xLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yLabel);

        AreaChart<Number, Number> chart = new AreaChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setAnimated(false);

        return chart;
    }

    /**
     * Creates a bar chart for categorical data.
     */
    public static BarChart<String, Number> createBarChart(String title, String xLabel, String yLabel) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(xLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yLabel);

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setAnimated(false);

        return chart;
    }

    /**
     * Creates a scatter chart for correlation visualization.
     */
    public static ScatterChart<Number, Number> createScatterChart(String title, String xLabel, String yLabel) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(xLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yLabel);

        ScatterChart<Number, Number> chart = new ScatterChart<>(xAxis, yAxis);
        chart.setTitle(title);

        return chart;
    }

    /**
     * Creates a pie chart.
     */
    public static PieChart createPieChart(String title) {
        PieChart chart = new PieChart();
        chart.setTitle(title);
        return chart;
    }

    /**
 * Helper class for real-time chart updates.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public static class RealTimeChart {
        private final LineChart<Number, Number> chart;
        private final List<XYChart.Series<Number, Number>> seriesList;
        private Timeline timeline;
        private int currentX = 0;
        private final int maxDataPoints;
        private RealTimeDataProvider dataProvider;

        public interface RealTimeDataProvider {
            double[] getNextValues();
        }

        public RealTimeChart(LineChart<Number, Number> chart, int maxDataPoints) {
            this.chart = chart;
            this.seriesList = new ArrayList<>();
            this.maxDataPoints = maxDataPoints;
        }

        public void addSeries(String name) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(name);
            seriesList.add(series);
            chart.getData().add(series);
        }

        public void setDataProvider(RealTimeDataProvider provider) {
            this.dataProvider = provider;
        }

        public void start(int intervalMs) {
            if (timeline != null) {
                timeline.stop();
            }

            timeline = new Timeline(new KeyFrame(Duration.millis(intervalMs), e -> updateChart()));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }

        public void stop() {
            if (timeline != null) {
                timeline.stop();
            }
        }

        public void reset() {
            stop();
            currentX = 0;
            for (XYChart.Series<Number, Number> series : seriesList) {
                series.getData().clear();
            }
        }

        private void updateChart() {
            if (dataProvider == null)
                return;

            double[] values = dataProvider.getNextValues();
            for (int i = 0; i < Math.min(values.length, seriesList.size()); i++) {
                XYChart.Series<Number, Number> series = seriesList.get(i);
                series.getData().add(new XYChart.Data<>(currentX, values[i]));

                // Limit data points to prevent memory issues
                if (series.getData().size() > maxDataPoints) {
                    series.getData().remove(0);
                }
            }
            currentX++;
        }

        public LineChart<Number, Number> getChart() {
            return chart;
        }
    }

    /**
     * Creates a styled candlestick-like visual using rectangles.
     * Note: JavaFX doesn't have built-in candlestick charts, so we provide
     * a simplified bar representation.
     */
    public static StackPane createCandlestickPlaceholder(String title) {
        StackPane pane = new StackPane();
        javafx.scene.control.Label label = new javafx.scene.control.Label(
                "Candlestick Chart: " + title + "\n(Use external library for full OHLC support)");
        label.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");
        pane.getChildren().add(label);
        pane.setStyle("-fx-border-color: #ccc; -fx-border-width: 1; -fx-padding: 20;");
        return pane;
    }

    /**
     * Applies dark theme styling to a chart.
     */
    public static void applyDarkTheme(Chart chart) {
        chart.setStyle(
                "-fx-background-color: #2b2b2b; " +
                        "-fx-text-fill: white;");
        chart.lookup(".chart-plot-background").setStyle("-fx-background-color: #1e1e1e;");
        chart.lookup(".chart-legend").setStyle("-fx-background-color: #2b2b2b;");
    }
}


