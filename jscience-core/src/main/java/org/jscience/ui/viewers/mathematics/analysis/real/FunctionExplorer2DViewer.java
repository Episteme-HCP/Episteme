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

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.symbolic.parsing.SimpleExpressionParser;
import org.jscience.mathematics.analysis.SymbolicUtil;
import org.jscience.ui.Parameter;
import org.jscience.ui.RealParameter;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.viewers.mathematics.analysis.plotting.Plot2D;
import org.jscience.ui.viewers.mathematics.analysis.plotting.backends.JavaFXPlot2D;
import java.util.*;
import org.jscience.ui.i18n.I18n;

public class FunctionExplorer2DViewer extends AbstractViewer {

    private static final String DEFAULT_FX = "sin(x)";
    private static final String DEFAULT_GX = "x^2/10";

    private final Parameter<String> funcF;
    private final Parameter<String> funcG;
    private final RealParameter xMin;
    private final RealParameter xMax;

    public FunctionExplorer2DViewer() {
        this(DEFAULT_FX, DEFAULT_GX);
    }

    public FunctionExplorer2DViewer(String defaultF, String defaultG) {
        // Initialize Parameters
        // We define a shared update/plot trigger
        // Note: We can't easily access the plotRunnable from here before initUI.
        // So we pass a placeholder that checks if logic is ready, or we init UI first?
        // If we init UI first, we can't pass params to it easily if they are final.
        // We will init params with a no-op, then init UI, then set the Consumers?
        // Parameter fields are final. We can't change the consumer.
        // Solution: Use a wrapper "Runnable" that we populate later.
        
        this.updateTrigger = new Runnable() { public void run() {} }; // Placeholder
        
        this.funcF = new Parameter<>("f(x)", "Function F", defaultF, v -> runUpdate());
        this.funcG = new Parameter<>("g(x)", "Function G", defaultG, v -> runUpdate());
        this.xMin = new RealParameter("X Min", "X Min", Real.of(-1000), Real.of(1000), Real.of(1), Real.of(-10), v -> runUpdate());
        this.xMax = new RealParameter("X Max", "X Max", Real.of(-1000), Real.of(1000), Real.of(1), Real.of(10), v -> runUpdate());

        initUI();
    }
    
    private Runnable updateTrigger; // Logic to run on update
    private void runUpdate() { updateTrigger.run(); }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        return List.of(funcF, funcG, xMin, xMax);
    }

    private void initUI() {
        SplitPane layout = new SplitPane();
        I18n i18n = org.jscience.ui.i18n.I18n.getInstance();

        // --- Sidebar ---
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(15));
        sidebar.setMinWidth(300);
        sidebar.setMaxWidth(350);
        sidebar.getStyleClass().add("viewer-sidebar");

        // Input Section
        Label fLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.label.fx", "f(x) ="));
        TextField fInput = new TextField(funcF.getValue());
        fInput.textProperty().addListener((obs, old, newVal) -> funcF.setValue(newVal)); 

        Label gLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.label.gx", "g(x) ="));
        TextField gInput = new TextField(funcG.getValue());
        gInput.textProperty().addListener((obs, old, newVal) -> funcG.setValue(newVal));

        Label opLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.label.plotop", "Plot Operation:"));
        ComboBox<String> opCombo = new ComboBox<>(FXCollections.observableArrayList(
                "f(x)", "g(x)", "f(x) + g(x)", "f(x) * g(x)"));
        opCombo.setValue("f(x)");

        Label rangeLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.label.range", "Range [Min, Max]"));
        HBox rangeBox = new HBox(5);
        TextField xMinField = new TextField(String.valueOf(xMin.getValue().doubleValue()));
        TextField xMaxField = new TextField(String.valueOf(xMax.getValue().doubleValue()));
        xMinField.setPrefWidth(60);
        xMaxField.setPrefWidth(60);
        
        // Bind Range Fields
        xMinField.textProperty().addListener((obs, old, val) -> { try { xMin.setValue(Real.of(Double.parseDouble(val))); } catch(Exception e){} });
        xMaxField.textProperty().addListener((obs, old, val) -> { try { xMax.setValue(Real.of(Double.parseDouble(val))); } catch(Exception e){} });
        
        rangeBox.getChildren().addAll(xMinField, xMaxField);

        Button plotBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.button.plot", "Plot Function"));
        plotBtn.setMaxWidth(Double.MAX_VALUE);
        plotBtn.getStyleClass().add("accent-button-blue");

        // Analysis
        Separator sep1 = new Separator();
        Label analysisLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.label.analysis", "Analysis (Click Chart)"));
        analysisLabel.getStyleClass().add("header-label");
        analysisLabel.setStyle("-fx-font-size: 14px;");

        Label cursorVal = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.label.cursor", "Cursor:") + " -");

        Label symTitle = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.label.symderiv", "Symbolic Derivative:"));
        TextArea symResult = new TextArea();
        symResult.setPrefRowCount(2);
        symResult.setEditable(false);
        symResult.setWrapText(true);

        Button symbBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.button.derivative", "Calculate Derivative (Symbolic)"));
        symbBtn.setMaxWidth(Double.MAX_VALUE);

        sidebar.getChildren().addAll(
                fLabel, fInput,
                gLabel, gInput,
                opLabel, opCombo,
                rangeLabel, rangeBox,
                plotBtn,
                sep1, analysisLabel, cursorVal,
                symTitle, symbBtn, symResult);

        // --- Main Area (Chart) ---
        StackPane chartContainer = new StackPane();
        chartContainer.getStyleClass().add("viewer-root");
        Label placeholder = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.text.placeholder", "Enter function and click Plot"));
        chartContainer.getChildren().add(placeholder);

        // Logic
        Runnable doPlot = () -> {
            try {
                String fStr = funcF.getValue();
                String gStr = funcG.getValue();
                String op = opCombo.getValue();
                double min = xMin.getValue().doubleValue();
                double max = xMax.getValue().doubleValue();

                Plot2D plot = new JavaFXPlot2D("Function Plot");
                plot.setGrid(true);
                plot.setLegend(true);
                plot.setXRange(Real.of(min), Real.of(max));
                // ... (rest is same)

                SimpleExpressionParser pF = new SimpleExpressionParser(fStr);
                SimpleExpressionParser pG = new SimpleExpressionParser(gStr);

                plot.addFunction(xArg -> {
                    double x = xArg.doubleValue();
                    double valF = evaluateSafe(pF, x);
                    double valG = evaluateSafe(pG, x);

                    if (op.equals("f(x)"))
                        return Real.of(valF);
                    if (op.equals("g(x)"))
                        return Real.of(valG);
                    if (op.equals("f(x) + g(x)"))
                        return Real.of(valF + valG);
                    if (op.equals("f(x) * g(x)"))
                        return Real.of(valF * valG);
                    return Real.ZERO;
                }, Real.of(min), Real.of(max), op);

                if (plot instanceof JavaFXPlot2D) {
                    LineChart<Number, Number> node = ((JavaFXPlot2D) plot).getNode();
                    node.setCreateSymbols(true);

                    node.setOnMouseClicked(me -> {
                        cursorVal.setText(String.format(org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.label.cursor", "Cursor:") + " (%.1f, %.1f)",
                                me.getX(), me.getY()));
                    });

                    chartContainer.getChildren().setAll(node);
                }

            } catch (Exception ex) {
                // Ignore errors during typing or log
            }
        };

        // Wire up the trigger
        this.updateTrigger = doPlot;
        plotBtn.setOnAction(e -> doPlot.run());
        // ...

        symbBtn.setOnAction(e -> {
            String op = opCombo.getValue();
            String fStr = funcF.getValue();
            String gStr = funcG.getValue();

            String res = "";
            if (op.equals("f(x)"))
                res = SymbolicUtil.differentiate(fStr);
            else if (op.equals("g(x)"))
                res = SymbolicUtil.differentiate(gStr);
            else if (op.equals("f(x) + g(x)")) {
                res = SymbolicUtil.differentiate(fStr) + " + " + SymbolicUtil.differentiate(gStr);
            } else {
                res = org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.error.complex", "Complex operations not supported in demo.");
            }
            symResult.setText(res);
        });

        layout.getItems().addAll(sidebar, chartContainer);
        layout.setDividerPositions(0.3);
        getChildren().add(layout);
    }

    private double evaluateSafe(SimpleExpressionParser p, double x) {
        try {
            Map<String, Double> vars = new HashMap<>();
            vars.put("x", x);
            return p.parse(vars);
        } catch (Exception e) {
            return 0.0;
        }
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.mathematics", "Mathematics");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.name", "Function Explorer 2D");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.desc", "A 2D viewer for plotting and analyzing mathematical functions.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.functionexplorer2dviewer.longdesc", "Enter and plot 2D mathematical functions f(x) and g(x). Perform operations like addition and multiplication, and calculate symbolic derivatives with real-time visualization.");
    }
}
