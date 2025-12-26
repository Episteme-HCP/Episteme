package org.jscience.ui.demos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;

public class EconomicsMarketDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return org.jscience.social.i18n.I18n.getInstance().get("category.economics");
    }

    @Override
    public String getName() {
        return org.jscience.social.i18n.I18n.getInstance().get("econ.market.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.social.i18n.I18n.getInstance().get("econ.market.desc");
    }

    @Override
    public void show(Stage stage) {
        BorderPane root = new BorderPane();

        // Axes
        NumberAxis xAxis = new NumberAxis(org.jscience.social.i18n.I18n.getInstance().get("econ.market.axis.quantity"),
                0, 100, 10);
        NumberAxis yAxis = new NumberAxis(org.jscience.social.i18n.I18n.getInstance().get("econ.market.axis.price"), 0,
                100, 10);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(org.jscience.social.i18n.I18n.getInstance().get("econ.market.chart.title"));
        lineChart.setAnimated(false); // crucial for smooth slider updates

        XYChart.Series<Number, Number> supplySeries = new XYChart.Series<>();
        supplySeries.setName(org.jscience.social.i18n.I18n.getInstance().get("econ.market.series.supply"));

        XYChart.Series<Number, Number> demandSeries = new XYChart.Series<>();
        demandSeries.setName(org.jscience.social.i18n.I18n.getInstance().get("econ.market.series.demand"));

        lineChart.getData().add(supplySeries);
        lineChart.getData().add(demandSeries);

        // Controls
        Slider supplyShift = new Slider(-20, 20, 0);
        Slider demandShift = new Slider(-20, 20, 0);

        Runnable updateGraph = () -> {
            org.jscience.mathematics.numbers.real.Real sShift = org.jscience.mathematics.numbers.real.Real
                    .of(supplyShift.getValue());
            org.jscience.mathematics.numbers.real.Real dShift = org.jscience.mathematics.numbers.real.Real
                    .of(demandShift.getValue());

            ObservableList<XYChart.Data<Number, Number>> sData = FXCollections.observableArrayList();
            ObservableList<XYChart.Data<Number, Number>> dData = FXCollections.observableArrayList();

            for (int q = 0; q <= 100; q += 10) {
                org.jscience.mathematics.numbers.real.Real quantity = org.jscience.mathematics.numbers.real.Real.of(q);

                // Supply: P = 0.5Q + 10 + shift
                org.jscience.mathematics.numbers.real.Real pSupply = quantity
                        .multiply(org.jscience.mathematics.numbers.real.Real.of(0.5))
                        .add(org.jscience.mathematics.numbers.real.Real.of(10))
                        .add(sShift);

                sData.add(new XYChart.Data<>(q, Math.max(0, pSupply.doubleValue())));

                // Demand: P = -0.5Q + 90 + shift
                org.jscience.mathematics.numbers.real.Real pDemand = quantity
                        .multiply(org.jscience.mathematics.numbers.real.Real.of(-0.5))
                        .add(org.jscience.mathematics.numbers.real.Real.of(90))
                        .add(dShift);

                dData.add(new XYChart.Data<>(q, Math.max(0, pDemand.doubleValue())));
            }

            supplySeries.setData(sData);
            demandSeries.setData(dData);
        };

        supplyShift.valueProperty().addListener(e -> updateGraph.run());
        demandShift.valueProperty().addListener(e -> updateGraph.run());
        updateGraph.run(); // Init

        VBox controls = new VBox(10,
                new Label(org.jscience.social.i18n.I18n.getInstance().get("econ.market.label.shift_supply")),
                supplyShift,
                new Label(org.jscience.social.i18n.I18n.getInstance().get("econ.market.label.shift_demand")),
                demandShift);
        controls.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");

        root.setCenter(lineChart);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }
}
