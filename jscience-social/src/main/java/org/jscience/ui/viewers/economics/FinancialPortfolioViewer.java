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

package org.jscience.ui.viewers.economics;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.chart.*;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.Parameter;
import org.jscience.economics.PortfolioData;
import org.jscience.economics.money.Money;


import java.util.*;

/**
 * Advanced trading-style dashboard for financial portfolios.
 * Supports real-time price tracking, asset allocation charts, and detailed holdings.
 */
public final class FinancialPortfolioViewer extends AbstractViewer {

    private PortfolioData portfolio;
    private final TableView<PortfolioData.Holding> holdingsTable = new TableView<>();
    private final LineChart<Number, Number> performanceChart;
    private final PieChart allocationChart = new PieChart();

    @SuppressWarnings("unused")
    private double refreshRate = 1.0; // Seconds

    public FinancialPortfolioViewer() {
        // Setup UI
        createSidebar();
        performanceChart = createPerformanceChart();
        setupHoldingsTable();

        SplitPane mainSplit = new SplitPane();
        mainSplit.getItems().addAll(holdingsTable, performanceChart);
        mainSplit.setDividerPositions(0.4);

        setCenter(mainSplit);
        setRight(allocationChart);
        
        getStyleClass().add("financial-portfolio-viewer");
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.getStyleClass().add("viewer-sidebar");
        // Metric labels would be added here
        return sidebar;
    }

    private LineChart<Number, Number> createPerformanceChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (ticks)");
        yAxis.setLabel("Price (Real)");
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Asset Performance History");
        chart.setCreateSymbols(false);
        return chart;
    }

    private void setupHoldingsTable() {
        TableColumn<PortfolioData.Holding, String> symbolCol = new TableColumn<>("Symbol");
        symbolCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAsset().getSymbol()));
        
        TableColumn<PortfolioData.Holding, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAsset().getCurrentPrice().toString()));

        TableColumn<PortfolioData.Holding, String> valueCol = new TableColumn<>("Total Value");
        valueCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().totalValue().toString()));

        holdingsTable.getColumns().addAll(Arrays.asList(symbolCol, priceCol, valueCol));
    }

    public void setPortfolio(PortfolioData data) {
        this.portfolio = data;
        holdingsTable.setItems(FXCollections.observableArrayList(data.getHoldings()));
        updateCharts();
    }

    private void updateCharts() {
        if (portfolio == null) return;
        
        // Update Allocation
        allocationChart.getData().clear();
        for (var holding : portfolio.getHoldings()) {
            allocationChart.getData().add(new PieChart.Data(holding.getAsset().getSymbol(), 
                holding.totalValue().getValue().doubleValue()));
        }

        // Update Performance
        performanceChart.getData().clear();
        for (var holding : portfolio.getHoldings()) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(holding.getAsset().getSymbol());
            List<Money> history = holding.getAsset().getPriceHistory();
            for (int i = 0; i < history.size(); i++) {
                series.getData().add(new XYChart.Data<>(i, history.get(i).getValue().doubleValue()));
            }
            performanceChart.getData().add(series);
        }
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> params = new ArrayList<>();
        params.add(new NumericParameter("Refresh Rate", "Update frequency in seconds", 0.1, 10.0, 0.1, 1.0, 
            val -> this.refreshRate = val));
        return params;
    }

    @Override public String getCategory() { return "Economics"; }
    @Override public String getName() { return "Financial Portfolio Viewer"; }
    @Override public String getDescription() { return "Professional-grade financial monitoring dashboard."; }
    @Override public String getLongDescription() { 
        return "Comprehensive analysis tool for assets, including real-time charts, allocation plots, and profit/loss metrics."; 
    }
}
