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

package org.jscience.ui.viewers.mathematics.statistics;

import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Parameter;
import java.util.List;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.ui.i18n.I18n;

/**
 * Visualization of Statistical Distributions.
 * Supports Normal, Poisson, and Binomial distributions with interactive
 * parameters.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DistributionsViewer extends AbstractViewer {

    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("distributions.title", "Statistical Distributions");
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        return new ArrayList<>();
    }

    private TabPane tabPane;

    public DistributionsViewer() {
        BorderPane layout = new BorderPane();
        layout.getStyleClass().add("dark-viewer-root");

        // Header
        Label header = new Label(I18n.getInstance().get("distributions.title"));
        header.setStyle("-fx-font-size: 24px; -fx-padding: 15; -fx-font-weight: bold;");
        layout.setTop(header);

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(
                createNormalTab(),
                createPoissonTab(),
                createBinomialTab());

        layout.setCenter(tabPane);
        getChildren().add(layout);
    }

    private Tab createNormalTab() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));

        // Controls
        VBox controls = new VBox(10);
        controls.setPrefWidth(250);
        controls.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 10;");

        Slider meanSlider = new Slider(-5, 5, 0);
        Label meanLabel = new Label(I18n.getInstance().get("distributions.mean") + ": 0.0");
        meanSlider.valueProperty()
                .addListener((o, ov, nv) -> meanLabel.setText(
                        String.format(I18n.getInstance().get("distributions.mean") + ": %.1f", nv.doubleValue())));

        Slider stdSlider = new Slider(0.1, 3, 1.0);
        Label stdLabel = new Label(I18n.getInstance().get("distributions.std") + ": 1.0");
        stdSlider.valueProperty()
                .addListener((o, ov, nv) -> stdLabel.setText(
                        String.format(I18n.getInstance().get("distributions.std") + ": %.1f", nv.doubleValue())));

        controls.getChildren().addAll(new Label(I18n.getInstance().get("distributions.params")), new Separator(),
                meanLabel, meanSlider, stdLabel,
                stdSlider);

        // Chart
        NumberAxis xAxis = new NumberAxis(-5, 5, 0.5);
        xAxis.setLabel(I18n.getInstance().get("distributions.axis.value"));
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(I18n.getInstance().get("distributions.axis.pdf"));

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(I18n.getInstance().get("distributions.chart.normal"));
        chart.setCreateSymbols(false);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("N(\u03BC, \u03C3\u00B2)");
        chart.getData().add(series);

        // Update logic
        Runnable update = () -> {
            double mu = meanSlider.getValue();
            double sigma = stdSlider.getValue();
            series.getData().clear();
            double minX = mu - 4 * sigma;
            double maxX = mu + 4 * sigma;
            if (minX < -5)
                minX = -5;
            if (maxX > 5)
                maxX = 5;

            for (double x = -5; x <= 5; x += 0.1) {
                double pdf = (1.0 / (sigma * Math.sqrt(2 * Math.PI))) * Math.exp(-0.5 * Math.pow((x - mu) / sigma, 2));
                series.getData().add(new XYChart.Data<>(x, pdf));
            }
        };

        meanSlider.valueProperty().addListener(e -> update.run());
        stdSlider.valueProperty().addListener(e -> update.run());
        update.run();

        pane.setCenter(chart);
        pane.setRight(controls);
        return new Tab(I18n.getInstance().get("distributions.tab.normal"), pane);
    }

    private Tab createPoissonTab() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));

        VBox controls = new VBox(10);
        controls.setPrefWidth(250);
        controls.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 10;");

        Slider lambdaSlider = new Slider(0.1, 20, 5);
        Label lambdaLabel = new Label(I18n.getInstance().get("distributions.lambda") + ": 5.0");
        lambdaSlider.valueProperty()
                .addListener((o, ov, nv) -> lambdaLabel.setText(
                        String.format(I18n.getInstance().get("distributions.lambda") + ": %.1f", nv.doubleValue())));

        controls.getChildren().addAll(new Label(I18n.getInstance().get("distributions.params")), new Separator(),
                lambdaLabel, lambdaSlider);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(I18n.getInstance().get("distributions.axis.k_occ"));
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(I18n.getInstance().get("distributions.axis.pmf"));

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(I18n.getInstance().get("distributions.chart.poisson"));
        chart.setAnimated(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("P(k; \u03BB)");
        chart.getData().add(series);

        Runnable update = () -> {
            double lambda = lambdaSlider.getValue();
            series.getData().clear();
            int maxK = (int) (lambda + 3 * Math.sqrt(lambda));
            maxK = Math.max(10, maxK);

            for (int k = 0; k <= maxK; k++) {
                double pmf = (Math.pow(lambda, k) * Math.exp(-lambda)) / factorial(k);
                series.getData().add(new XYChart.Data<>(String.valueOf(k), pmf));
            }
        };

        lambdaSlider.valueProperty().addListener(e -> update.run());
        update.run();

        pane.setCenter(chart);
        pane.setRight(controls);
        return new Tab(I18n.getInstance().get("distributions.tab.poisson"), pane);
    }

    private Tab createBinomialTab() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));

        VBox controls = new VBox(10);
        controls.setPrefWidth(250);
        controls.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 10;");

        Slider nSlider = new Slider(1, 40, 10);
        Label nLabel = new Label(I18n.getInstance().get("distributions.trials") + ": 10");
        nSlider.valueProperty()
                .addListener((o, ov, nv) -> nLabel.setText(
                        String.format(I18n.getInstance().get("distributions.trials") + ": %d", nv.intValue())));

        Slider pSlider = new Slider(0, 1, 0.5);
        Label pLabel = new Label(I18n.getInstance().get("distributions.prob") + ": 0.5");
        pSlider.valueProperty()
                .addListener((o, ov, nv) -> pLabel.setText(
                        String.format(I18n.getInstance().get("distributions.prob") + ": %.2f", nv.doubleValue())));

        controls.getChildren().addAll(new Label(I18n.getInstance().get("distributions.params")), new Separator(),
                nLabel, nSlider, pLabel, pSlider);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(I18n.getInstance().get("distributions.axis.k_suc"));
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(I18n.getInstance().get("distributions.axis.pmf"));

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(I18n.getInstance().get("distributions.chart.binomial"));
        chart.setAnimated(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("B(n, p)");
        chart.getData().add(series);

        Runnable update = () -> {
            int n = (int) nSlider.getValue();
            double p = pSlider.getValue();
            series.getData().clear();

            for (int k = 0; k <= n; k++) {
                double pmf = combinations(n, k) * Math.pow(p, k) * Math.pow(1 - p, n - k);
                series.getData().add(new XYChart.Data<>(String.valueOf(k), pmf));
            }
        };

        nSlider.valueProperty().addListener(e -> update.run());
        pSlider.valueProperty().addListener(e -> update.run());
        update.run();

        pane.setCenter(chart);
        pane.setRight(controls);
        return new Tab(I18n.getInstance().get("distributions.tab.binomial"), pane);
    }

    private long factorial(int n) {
        if (n <= 1)
            return 1;
        long res = 1;
        for (int i = 2; i <= n; i++)
            res *= i;
        return res;
    }

    private long combinations(int n, int k) {
        if (k < 0 || k > n)
            return 0;
        if (k == 0 || k == n)
            return 1;
        if (k > n / 2)
            k = n - k;

        long res = 1;
        for (int i = 1; i <= k; i++) {
            res = res * (n - i + 1) / i;
        }
        return res;
    }

}
