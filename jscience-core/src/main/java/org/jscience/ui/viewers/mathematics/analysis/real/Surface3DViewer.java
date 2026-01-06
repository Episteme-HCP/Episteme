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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.plotting.PlotFactory;
import org.jscience.ui.plotting.Plot3D;

/**
 * Interactive 3D Surface Plotter.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Surface3DViewer extends Application {

    private ComboBox<String> functionSelector;
    private TextField xRangeField, yRangeField;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #eee;");

        Label title = new Label(I18n.getInstance().get("surface3d.title"));
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        functionSelector = new ComboBox<>();
        functionSelector.getItems().addAll(
                "z = sin(x) * cos(y)",
                "z = x\u00B2 + y\u00B2",
                "z = sin(\u221A(x\u00B2+y\u00B2)) / \u221A(x\u00B2+y\u00B2)",
                "z = exp(-(x\u00B2+y\u00B2)/10) * cos(x+y)",
                "z = x * y",
                "z = cos(x) + cos(y)");
        functionSelector.setValue("z = sin(x) * cos(y)");
        functionSelector.setMaxWidth(Double.MAX_VALUE);

        HBox settings = new HBox(10);
        xRangeField = new TextField("10");
        xRangeField.setPrefWidth(50);
        yRangeField = new TextField("10");
        yRangeField.setPrefWidth(50);
        settings.getChildren().addAll(
                new Label(I18n.getInstance().get("surface3d.xrange") + " \u00B1"),
                xRangeField,
                new Label(I18n.getInstance().get("surface3d.yrange") + " \u00B1"),
                yRangeField);

        Button plotBtn = new Button(I18n.getInstance().get("surface3d.btn.open"));
        plotBtn.setMaxWidth(Double.MAX_VALUE);
        plotBtn.setStyle(
                "-fx-background-color: #ff9800; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
        plotBtn.setOnAction(e -> open3DPlot());

        root.getChildren().addAll(
                title,
                new Label(I18n.getInstance().get("surface3d.select")),
                functionSelector,
                settings,
                plotBtn);

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle(I18n.getInstance().get("surface3d.title"));
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
            case "z = x\u00B2 + y\u00B2" -> (x * x + y * y) / 10.0;
            case "z = sin(\u221A(x\u00B2+y\u00B2)) / \u221A(x\u00B2+y\u00B2)" -> {
                double r = Math.sqrt(x * x + y * y);
                yield r == 0 ? 1.0 : Math.sin(r) * 5.0 / r;
            }
            case "z = exp(-(x\u00B2+y\u00B2)/10) * cos(x+y)" ->
                Math.exp(-(x * x + y * y) / 10.0) * Math.cos(x + y) * 5.0;
            case "z = x * y" -> (x * y) / 5.0;
            case "z = cos(x) + cos(y)" -> (Math.cos(x) + Math.cos(y)) * 2.0;
            default -> 0;
        };
    }

    public static void show(Stage stage) {
        new Surface3DViewer().start(stage);
    }
}
