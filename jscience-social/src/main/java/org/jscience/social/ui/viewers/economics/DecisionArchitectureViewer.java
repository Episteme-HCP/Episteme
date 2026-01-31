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

package org.jscience.social.ui.viewers.economics;

import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.core.ui.AbstractViewer;
import org.jscience.core.ui.i18n.I18N;
import org.jscience.social.economics.models.StrategicModel;

/**
 * Visualizes game theory matrices, decision trees, and Pareto optimality.
 */
public final class DecisionArchitectureViewer extends AbstractViewer {

    private final TabPane tabs = new TabPane();
    private final GridPane matrixGrid = new GridPane();
    private final ScatterChart<Number, Number> paretoChart;

    public DecisionArchitectureViewer() {
        // Tab 1: Payoff Matrix
        ScrollPane scrollMatrix = new ScrollPane(matrixGrid);
        matrixGrid.setHgap(10); matrixGrid.setVgap(10);
        Tab matrixTab = new Tab("Payoff Matrix", scrollMatrix);
        matrixTab.setClosable(false);

        // Tab 2: Pareto Efficiency
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Player 1 Utility");
        yAxis.setLabel("Player 2 Utility");
        paretoChart = new ScatterChart<>(xAxis, yAxis);
        paretoChart.setTitle(I18N.getInstance().get("viewer.decision.pareto", "Pareto Frontier"));
        Tab paretoTab = new Tab("Pareto Optimality", paretoChart);
        paretoTab.setClosable(false);

        tabs.getTabs().addAll(matrixTab, paretoTab);
        setCenter(tabs);
        getStyleClass().add("decision-architecture-viewer");
    }

    public void setModel(StrategicModel model) {
        // Update Matrix
        matrixGrid.getChildren().clear();
        int row = 0;
        for (java.util.Map.Entry<String, StrategicModel.Payoff> entry : model.getPayoffMatrix().entrySet()) {
            Label lbl = new Label(entry.getKey() + ": (" + entry.getValue().getPlayer1() + ", " + 
                                  entry.getValue().getPlayer2() + ")");
            lbl.setStyle("-fx-border-color: Gray; -fx-padding: 10; -fx-background-color: #222;");
            matrixGrid.add(lbl, row % 2, row / 2);
            row++;
        }

        // Update Pareto
        paretoChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Scenarios");
        for (org.jscience.core.mathematics.numbers.real.Real[] point : model.getParetoFrontier()) {
            series.getData().add(new XYChart.Data<>(point[0].doubleValue(), point[1].doubleValue()));
        }
        paretoChart.getData().add(series);
    }

    @Override public String getCategory() { return I18N.getInstance().get("category.shared", "General Purpose"); }
    @Override public String getName() { return I18N.getInstance().get("viewer.decision.name", "Decision Architecture Viewer"); }
    @Override public String getDescription() { return I18N.getInstance().get("viewer.decision.desc", "Analyzes strategic choices and game theory."); }
    @Override public String getLongDescription() { 
        return I18N.getInstance().get("viewer.decision.longdesc", "Strategic analysis workstation for choice theory and political strategy. Featuring interactive payoff matrices and Pareto efficiency charts."); 
    }
}

