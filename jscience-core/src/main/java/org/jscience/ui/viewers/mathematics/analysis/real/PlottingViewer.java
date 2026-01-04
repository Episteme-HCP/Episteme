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

package org.jscience.ui.viewers.mathematics.analysis.real;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.plotting.Plot2D;
import org.jscience.ui.plotting.PlotFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Full-featured 2D Plotting Viewer with multiple function support.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PlottingViewer extends Application {

    private ComboBox<String> functionSelector;
    private TextField xMinField, xMaxField;
    private CheckBox gridCheck, legendCheck;
    private VBox seriesListBox;
    private List<PlotSeries> activeSeries = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Header
        Label header = new Label("JScience 2D Function Plotter");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        HBox headerBox = new HBox(header);
        headerBox.setPadding(new Insets(10));
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setStyle("-fx-background-color: #16213e;");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #00d9ff;");
        root.setTop(headerBox);

        // Left Panel - Controls
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setPrefWidth(250);
        leftPanel.setStyle("-fx-background-color: #16213e;");

        // Function selector
        Label funcLabel = new Label("Add Function:");
        funcLabel.setStyle("-fx-text-fill: #aaa;");
        functionSelector = new ComboBox<>();
        functionSelector.getItems().addAll(
                "sin(x)", "cos(x)", "tan(x)",
                "xÃ‚Â²", "xÃ‚Â³", "Ã¢Ë†Å¡x",
                "e^x", "ln(x)", "1/x",
                "sin(x)/x", "xÃ‚Â·sin(x)");
        functionSelector.setValue("sin(x)");
        functionSelector.setMaxWidth(Double.MAX_VALUE);

        // Range inputs
        Label rangeLabel = new Label("X Range:");
        rangeLabel.setStyle("-fx-text-fill: #aaa;");
        HBox rangeBox = new HBox(5);
        xMinField = new TextField("-10");
        xMinField.setPrefWidth(60);
        xMaxField = new TextField("10");
        xMaxField.setPrefWidth(60);
        rangeBox.getChildren().addAll(new Label("Min:"), xMinField, new Label("Max:"), xMaxField);

        // Add button
        Button addBtn = new Button("Add to Plot");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addBtn.setOnAction(e -> addFunction());

        // Options
        gridCheck = new CheckBox("Show Grid");
        gridCheck.setSelected(true);
        legendCheck = new CheckBox("Show Legend");
        legendCheck.setSelected(true);

        // Active series list
        Label seriesLabel = new Label("Active Series:");
        seriesListBox = new VBox(5);
        seriesListBox.setStyle("-fx-background-color: #0f3460; -fx-padding: 5;");

        // Clear all button
        Button clearBtn = new Button("Clear All");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setOnAction(e -> {
            activeSeries.clear();
            seriesListBox.getChildren().clear();
        });

        // Plot button
        Button plotBtn = new Button("Generate Plot");
        plotBtn.setMaxWidth(Double.MAX_VALUE);
        plotBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        plotBtn.setOnAction(e -> generatePlot());

        leftPanel.getChildren().addAll(
                funcLabel, functionSelector,
                new Separator(),
                rangeLabel, rangeBox,
                addBtn,
                new Separator(),
                gridCheck, legendCheck,
                new Separator(),
                seriesLabel, seriesListBox,
                clearBtn,
                new Separator(),
                plotBtn);

        root.setLeft(leftPanel);

        // Center - Instructions/Preview placeholder
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(40));

        Label instructLabel = new Label("Add functions and click 'Generate Plot'");
        instructLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #aaa;");

        Label tipLabel = new Label(
                "Tips:\n" +
                        "Ã¢â‚¬Â¢ Add multiple functions to compare\n" +
                        "Ã¢â‚¬Â¢ Adjust X range for different views\n" +
                        "Ã¢â‚¬Â¢ Toggle grid and legend options\n" +
                        "Ã¢â‚¬Â¢ Plot opens in new window");
        tipLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888;");

        centerBox.getChildren().addAll(instructLabel, tipLabel);
        root.setCenter(centerBox);

        // Add default sin(x)
        addFunction();

        Scene scene = new Scene(root, 800, 500);
        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("viewer.plotting"));
        stage.setScene(scene);
        stage.show();
    }

    private void addFunction() {
        String funcName = functionSelector.getValue();
        double xMin, xMax;
        try {
            xMin = Double.parseDouble(xMinField.getText());
            xMax = Double.parseDouble(xMaxField.getText());
        } catch (NumberFormatException e) {
            xMin = -10;
            xMax = 10;
        }

        PlotSeries series = new PlotSeries(funcName, xMin, xMax);
        activeSeries.add(series);

        // Update UI
        HBox seriesRow = new HBox(5);
        seriesRow.setAlignment(Pos.CENTER_LEFT);
        Label lbl = new Label(funcName + " [" + xMin + ", " + xMax + "]");
        Button removeBtn = new Button("Ãƒâ€”");
        removeBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 0 5;");
        removeBtn.setOnAction(e -> {
            activeSeries.remove(series);
            seriesListBox.getChildren().remove(seriesRow);
        });
        seriesRow.getChildren().addAll(lbl, removeBtn);
        seriesListBox.getChildren().add(seriesRow);
    }

    private void generatePlot() {
        if (activeSeries.isEmpty())
            return;

        Plot2D plot = PlotFactory.create2D("JScience Function Plot");
        plot.setGrid(gridCheck.isSelected());
        plot.setLegend(legendCheck.isSelected());

        for (PlotSeries s : activeSeries) {
            List<Real> xData = new ArrayList<>();
            List<Real> yData = new ArrayList<>();

            double step = (s.xMax - s.xMin) / 500.0;
            for (double x = s.xMin; x <= s.xMax; x += step) {
                double y = evalFunction(s.funcName, x);
                if (!Double.isNaN(y) && !Double.isInfinite(y)) {
                    xData.add(Real.of(x));
                    yData.add(Real.of(y));
                }
            }

            plot.addData(xData, yData, s.funcName);
        }

        plot.setAxisLabels("x", "f(x)");
        plot.show();
    }

    private double evalFunction(String name, double x) {
        return switch (name) {
            case "sin(x)" -> Math.sin(x);
            case "cos(x)" -> Math.cos(x);
            case "tan(x)" -> Math.tan(x);
            case "xÃ‚Â²" -> x * x;
            case "xÃ‚Â³" -> x * x * x;
            case "Ã¢Ë†Å¡x" -> Math.sqrt(x);
            case "e^x" -> Math.exp(x);
            case "ln(x)" -> Math.log(x);
            case "1/x" -> 1.0 / x;
            case "sin(x)/x" -> x == 0 ? 1 : Math.sin(x) / x;
            case "xÃ‚Â·sin(x)" -> x * Math.sin(x);
            default -> 0;
        };
    }

    private static class PlotSeries {
        String funcName;
        double xMin, xMax;

        PlotSeries(String funcName, double xMin, double xMax) {
            this.funcName = funcName;
            this.xMin = xMin;
            this.xMax = xMax;
        }
    }

    public static void show(Stage stage) {
        new PlottingViewer().start(stage);
    }
}


