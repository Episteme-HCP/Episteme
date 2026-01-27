package org.jscience.ui.viewers.economics;

import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.i18n.I18n;
import org.jscience.economics.StrategicModel;

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
        paretoChart.setTitle(I18n.getInstance().get("viewer.decision.pareto", "Pareto Frontier"));
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
        for (var entry : model.getPayoffMatrix().entrySet()) {
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
        for (var point : model.getParetoFrontier()) {
            series.getData().add(new XYChart.Data<>(point[0].doubleValue(), point[1].doubleValue()));
        }
        paretoChart.getData().add(series);
    }

    @Override public String getCategory() { return I18n.getInstance().get("category.shared", "General Purpose"); }
    @Override public String getName() { return I18n.getInstance().get("viewer.decision.name", "Decision Architecture Viewer"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.decision.desc", "Analyzes strategic choices and game theory."); }
    @Override public String getLongDescription() { 
        return I18n.getInstance().get("viewer.decision.longdesc", "Strategic analysis workstation for choice theory and political strategy. Featuring interactive payoff matrices and Pareto efficiency charts."); 
    }
}
