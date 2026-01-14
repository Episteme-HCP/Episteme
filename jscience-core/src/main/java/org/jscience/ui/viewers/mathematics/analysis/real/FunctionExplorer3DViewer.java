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

package org.jscience.ui.viewers.mathematics.analysis.real;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.symbolic.parsing.SimpleExpressionParser;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.viewers.mathematics.analysis.plotting.backends.JavaFXPlot3D;
import org.jscience.ui.i18n.I18n;
import java.util.*;

public class FunctionExplorer3DViewer extends AbstractViewer {

    private static final String DEFAULT_3D_FUNC = "sin(sqrt(x^2+y^2))";



    public FunctionExplorer3DViewer() {
        initUI();
    }

    private void initUI() {
        SplitPane layout = new SplitPane();
        I18n i18n = I18n.getInstance();

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(15));
        sidebar.setMinWidth(300);
        sidebar.setMaxWidth(350);
        sidebar.setStyle("-fx-background-color: #fafafa;");

        Label funcLabel = new Label("z = f(x, y) =");
        TextField funcInput = new TextField(DEFAULT_3D_FUNC);

        Label rangeLabel = new Label(i18n.get("funcexplorer.range3d", "Range [\u00B1X, \u00B1Y]"));
        TextField rangeField = new TextField("10");

        CheckBox gridChk = new CheckBox(i18n.get("funcexplorer.grid", "Show Grid/Axes"));
        gridChk.setSelected(true);

        Button plotBtn = new Button(i18n.get("funcexplorer.btn.plot3d", "Plot 3D Surface"));
        plotBtn.setMaxWidth(Double.MAX_VALUE);
        plotBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");

        // Analysis 3D
        Separator sep = new Separator();
        Label analysisLabel = new Label(i18n.get("funcexplorer.analysis3d", "Analysis"));
        TextField xEval = new TextField("0");
        xEval.setPromptText("x");
        TextField yEval = new TextField("0");
        yEval.setPromptText("y");
        Label zRes = new Label("z = -");
        Button calcBtn = new Button(i18n.get("funcexplorer.btn.calcpt", "Calculate Point"));

        calcBtn.setOnAction(e -> {
            try {
                SimpleExpressionParser p = new SimpleExpressionParser(funcInput.getText());
                double val = evaluate3D(p, Double.parseDouble(xEval.getText()), Double.parseDouble(yEval.getText()));
                zRes.setText(String.format("z = %.4f", val));
            } catch (Exception ex) {
                zRes.setText("Error");
            }
        });

        sidebar.getChildren().addAll(funcLabel, funcInput, rangeLabel, rangeField, gridChk, plotBtn,
                sep, analysisLabel, new HBox(5, xEval, yEval), calcBtn, zRes);

        StackPane chartContainer = new StackPane();
        chartContainer.setStyle("-fx-background-color: #222;");
        Label placeholder = new Label(i18n.get("funcexplorer.placeholder", "Enter function and click Plot"));
        placeholder.setTextFill(javafx.scene.paint.Color.WHITE);
        chartContainer.getChildren().add(placeholder);

        plotBtn.setOnAction(e -> {
            try {
                String expr = funcInput.getText();
                double range = Double.parseDouble(rangeField.getText());
                SimpleExpressionParser parser = new SimpleExpressionParser(expr);

                JavaFXPlot3D plot = new JavaFXPlot3D("3D Surface");
                plot.setInteractive(true);

                plot.addSurface(args -> {
                    try {
                        return Real.of(evaluate3D(parser, args[0].doubleValue(), args[1].doubleValue()));
                    } catch (Exception ex) {
                        return Real.ZERO;
                    }
                }, Real.of(-range), Real.of(range), Real.of(-range), Real.of(range), "Surface");

                chartContainer.getChildren().setAll(plot.getNode());

            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error plotting: " + ex.getMessage());
                alert.show();
            }
        });

        layout.getItems().addAll(sidebar, chartContainer);
        layout.setDividerPositions(0.3);
        getChildren().add(layout);
    }

    private double evaluate3D(SimpleExpressionParser parser, double x, double y) {
        Map<String, Double> vars = new HashMap<>();
        vars.put("x", x);
        vars.put("y", y);
        return parser.parse(vars);
    }
}
