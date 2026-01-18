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

package org.jscience.ui.viewers.mathematics.statistics;

import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Parameter;
import org.jscience.ui.ChoiceParameter;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.i18n.I18n;

import java.util.List;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.layout.*;

/**
 * Visualization of Statistical Distributions.
 * Refactored to be 100% parameter-based.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DistributionsViewer extends AbstractViewer {

    private final StackPane chartContainer = new StackPane();
    private String currentType = "Normal";
    private double mu = 0.0, sigma = 1.0, lambda = 5.0, p = 0.5;
    private int n = 10;
    
    private final List<Parameter<?>> parameters = new ArrayList<>();

    public DistributionsViewer() {
        setupParameters();
        initUI();
        updateChart();
    }

    private void setupParameters() {
        List<String> types = List.of("Normal", "Poisson", "Binomial");
        parameters.add(new ChoiceParameter("dist.type", I18n.getInstance().get("distributions.type", "Distribution Type"), types, "Normal", v -> {
            currentType = v;
            updateChart();
        }));

        parameters.add(new NumericParameter("dist.mu", I18n.getInstance().get("distributions.mean", "Normal: Mean"), -5, 5, 0.1, mu, v -> { mu = v; updateChart(); }));
        parameters.add(new NumericParameter("dist.sigma", I18n.getInstance().get("distributions.std", "Normal: Std Dev"), 0.1, 5, 0.1, sigma, v -> { sigma = v; updateChart(); }));
        parameters.add(new NumericParameter("dist.lambda", I18n.getInstance().get("distributions.lambda", "Poisson: Lambda"), 0.1, 20, 0.5, lambda, v -> { lambda = v; updateChart(); }));
        parameters.add(new NumericParameter("dist.n", I18n.getInstance().get("distributions.trials", "Binomial: n"), 1, 40, 1, (double)n, v -> { n = v.intValue(); updateChart(); }));
        parameters.add(new NumericParameter("dist.p", I18n.getInstance().get("distributions.prob", "Binomial: p"), 0.01, 1, 0.01, p, v -> { p = v; updateChart(); }));
    }

    private void initUI() {
        getStyleClass().add("viewer-root");
        setPadding(new Insets(10));
        setCenter(chartContainer);
    }

    private void updateChart() {
        chartContainer.getChildren().clear();
        if ("Normal".equals(currentType)) {
            chartContainer.getChildren().add(createNormalChart());
        } else if ("Poisson".equals(currentType)) {
            chartContainer.getChildren().add(createPoissonChart());
        } else {
            chartContainer.getChildren().add(createBinomialChart());
        }
    }

    private LineChart<Number, Number> createNormalChart() {
        NumberAxis xAxis = new NumberAxis(-5, 5, 1);
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setCreateSymbols(false);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (double x = -5; x <= 5; x += 0.1) {
            double pdf = (1.0 / (sigma * Math.sqrt(2 * Math.PI))) * Math.exp(-0.5 * Math.pow((x - mu) / sigma, 2));
            series.getData().add(new XYChart.Data<>(x, pdf));
        }
        chart.getData().add(series);
        return chart;
    }

    private BarChart<String, Number> createPoissonChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        int maxK = Math.max(10, (int)(lambda + 4 * Math.sqrt(lambda)));
        for (int k = 0; k <= maxK; k++) {
            double pmf = (Math.pow(lambda, k) * Math.exp(-lambda)) / factorial(k);
            series.getData().add(new XYChart.Data<>(String.valueOf(k), pmf));
        }
        chart.getData().add(series);
        return chart;
    }

    private BarChart<String, Number> createBinomialChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int k = 0; k <= n; k++) {
            double pmf = combinations(n, k) * Math.pow(p, k) * Math.pow(1 - p, n - k);
            series.getData().add(new XYChart.Data<>(String.valueOf(k), pmf));
        }
        chart.getData().add(series);
        return chart;
    }

    private long factorial(int k) {
        long r = 1; for (int i = 2; i <= k; i++) r *= i; return r;
    }

    private long combinations(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k == 0 || k == n) return 1;
        if (k > n / 2) k = n - k;
        long res = 1;
        for (int i = 1; i <= k; i++) res = res * (n - i + 1) / i;
        return res;
    }

    @Override public String getCategory() { return I18n.getInstance().get("category.mathematics", "Mathematics"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.distributionsviewer.name", "Statistical Distributions"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.distributionsviewer.desc", "Statistical distributions explorer."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.distributionsviewer.longdesc", "Detailed visualization of various statistical distributions including Normal, Poisson, and Binomial. Interactively adjust parameters like mean, standard deviation, and lambda to see how they affect the shape and characteristics of the distribution curves."); }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
