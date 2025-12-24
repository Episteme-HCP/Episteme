/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.mathematics.numbers.real;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.plotting.PlotFactory;
import org.jscience.ui.plotting.Plot3D;

/**
 * Interactive 3D Surface Plotter.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class Surface3DViewer extends Application {

    private ComboBox<String> functionSelector;
    private TextField xRangeField, yRangeField;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #eee;");

        Label title = new Label("3D Surface Plotter");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        functionSelector = new ComboBox<>();
        functionSelector.getItems().addAll(
                "z = sin(x) * cos(y)",
                "z = x² + y²",
                "z = sin(√(x²+y²)) / √(x²+y²)",
                "z = exp(-(x²+y²)/10) * cos(x+y)",
                "z = x * y",
                "z = cos(x) + cos(y)");
        functionSelector.setValue("z = sin(x) * cos(y)");
        functionSelector.setMaxWidth(Double.MAX_VALUE);

        HBox settings = new HBox(10);
        xRangeField = new TextField("10");
        xRangeField.setPrefWidth(50);
        yRangeField = new TextField("10");
        yRangeField.setPrefWidth(50);
        settings.getChildren().addAll(new Label("X Range: ±"), xRangeField, new Label("Y Range: ±"), yRangeField);

        Button plotBtn = new Button("Open 3D Viewer");
        plotBtn.setMaxWidth(Double.MAX_VALUE);
        plotBtn.setStyle(
                "-fx-background-color: #ff9800; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
        plotBtn.setOnAction(e -> open3DPlot());

        root.getChildren().addAll(title, new Label("Select Function:"), functionSelector, settings, plotBtn);

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("JScience 3D Charting");
        stage.setScene(scene);
        stage.show();
    }

    private void open3DPlot() {
        double rx = Double.parseDouble(xRangeField.getText());
        double ry = Double.parseDouble(yRangeField.getText());
        String func = functionSelector.getValue();

        Plot3D plot = PlotFactory.create3D("3D Surface: " + func);
        plot.setInteractive(true);

        plot.addSurface(args -> {
            double x = args[0].doubleValue();
            double y = args[1].doubleValue();
            return Real.of(evaluateSurface(func, x, y));
        }, Real.of(-rx), Real.of(rx), Real.of(-ry), Real.of(ry), func);

        plot.show();
    }

    private double evaluateSurface(String func, double x, double y) {
        return switch (func) {
            case "z = sin(x) * cos(y)" -> Math.sin(x) * Math.cos(y);
            case "z = x² + y²" -> (x * x + y * y) / 10.0;
            case "z = sin(√(x²+y²)) / √(x²+y²)" -> {
                double r = Math.sqrt(x * x + y * y);
                yield r == 0 ? 1.0 : Math.sin(r) * 5.0 / r;
            }
            case "z = exp(-(x²+y²)/10) * cos(x+y)" -> Math.exp(-(x * x + y * y) / 10.0) * Math.cos(x + y) * 5.0;
            case "z = x * y" -> (x * y) / 5.0;
            case "z = cos(x) + cos(y)" -> (Math.cos(x) + Math.cos(y)) * 2.0;
            default -> 0;
        };
    }

    public static void show(Stage stage) {
        new Surface3DViewer().start(stage);
    }
}
