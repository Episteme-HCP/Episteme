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
import org.jscience.ui.Parameter;
import org.jscience.ui.RealParameter;
import org.jscience.ui.viewers.mathematics.analysis.plotting.backends.JavaFXPlot3D;
import org.jscience.ui.theme.ThemeColors;
import java.util.*;

public class FunctionExplorer3DViewer extends AbstractViewer {

    private static final String DEFAULT_3D_FUNC = "sin(sqrt(x^2+y^2))";

    private final Parameter<String> funcExpression;
    private final RealParameter range;

    public FunctionExplorer3DViewer() {
        this.funcExpression = new Parameter<>("Function", "f(x,y)", DEFAULT_3D_FUNC, v -> {}); // No auto-update for 3D (heavy)
        this.range = new RealParameter("Range", "Range", Real.of(1), Real.of(100), Real.of(1), Real.of(10), v -> {}); 
        initUI();
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        return List.of(funcExpression, range);
    }

    private void initUI() {
        SplitPane layout = new SplitPane();

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(15));
        sidebar.setMinWidth(300);
        sidebar.setMaxWidth(350);
        sidebar.getStyleClass().add("viewer-sidebar");

        Label funcLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("funcexplorer.label.z_fx", "z = f(x, y) ="));
        TextField funcInput = new TextField(funcExpression.getValue());
        funcInput.textProperty().addListener((obs, old, newVal) -> funcExpression.setValue(newVal));

        Label rangeLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("funcexplorer.range3d", "Range [\u00B1X, \u00B1Y]"));
        TextField rangeField = new TextField(String.valueOf(range.getValue().doubleValue()));
        rangeField.textProperty().addListener((obs, old, val) -> { 
            try { range.setValue(Real.of(Double.parseDouble(val))); } catch(Exception e){} 
        });

        CheckBox gridChk = new CheckBox(org.jscience.ui.i18n.I18n.getInstance().get("funcexplorer.grid", "Show Grid/Axes"));
        gridChk.setSelected(true);

        Button plotBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("funcexplorer.btn.plot3d", "Plot 3D Surface"));
        plotBtn.setMaxWidth(Double.MAX_VALUE);
        plotBtn.getStyleClass().add("accent-button-orange");

        // Analysis 3D
        Separator sep = new Separator();
        Label analysisLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("funcexplorer.analysis3d", "Analysis"));
        TextField xEval = new TextField("0");
        xEval.setPromptText("x");
        TextField yEval = new TextField("0");
        yEval.setPromptText("y");
        Label zRes = new Label(org.jscience.ui.i18n.I18n.getInstance().get("funcexplorer.label.z", "z = -"));
        Button calcBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("funcexplorer.btn.calcpt", "Calculate Point"));

        calcBtn.setOnAction(e -> {
            try {
                SimpleExpressionParser p = new SimpleExpressionParser(funcInput.getText());
                double val = evaluate3D(p, Double.parseDouble(xEval.getText()), Double.parseDouble(yEval.getText()));
                zRes.setText(String.format("z = %.4f", val));
            } catch (Exception ex) {
                zRes.setText(org.jscience.ui.i18n.I18n.getInstance().get("funcexplorer.status.error", "Error"));
            }
        });

        sidebar.getChildren().addAll(funcLabel, funcInput, rangeLabel, rangeField, gridChk, plotBtn,
                sep, analysisLabel, new HBox(5, xEval, yEval), calcBtn, zRes);

        StackPane chartContainer = new StackPane();
        chartContainer.getStyleClass().add("viewer-root");
        Label placeholder = new Label(org.jscience.ui.i18n.I18n.getInstance().get("funcexplorer.placeholder", "Enter function and click Plot"));
        placeholder.setTextFill(ThemeColors.SIDEBAR_BG);
        chartContainer.getChildren().add(placeholder);

        plotBtn.setOnAction(e -> {
            try {
                String expr = funcExpression.getValue();
                double rangeVal = range.getValue().doubleValue();
                SimpleExpressionParser parser = new SimpleExpressionParser(expr);

                JavaFXPlot3D plot = new JavaFXPlot3D("3D Surface");
                plot.setInteractive(true);

                plot.addSurface(args -> {
                    try {
                        return Real.of(evaluate3D(parser, args[0].doubleValue(), args[1].doubleValue()));
                    } catch (Exception ex) {
                        return Real.ZERO;
                    }
                }, Real.of(-rangeVal), Real.of(rangeVal), Real.of(-rangeVal), Real.of(rangeVal), "Surface");

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

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.mathematics", "Mathematics");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer3dviewer.name", "Function Explorer 3D");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer3dviewer.desc", "Explore 3D mathematical surfaces.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer3dviewer.longdesc", "Visualize functions of two variables z = f(x, y) as 3D surfaces. Rotate, zoom, and analyze points on complex mathematical terrains.");
    }
}

