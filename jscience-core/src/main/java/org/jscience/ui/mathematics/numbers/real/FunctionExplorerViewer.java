/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.mathematics.numbers.real;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.collections.FXCollections;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.parser.SimpleExpressionParser;
import org.jscience.mathematics.analysis.SymbolicUtil;
import org.jscience.ui.ThemeManager;
import org.jscience.ui.plotting.Plot2D;
import org.jscience.ui.plotting.Plot3D;
import org.jscience.ui.plotting.PlotFactory;
import org.jscience.ui.plotting.backends.JavaFXPlot2D;
import org.jscience.ui.plotting.backends.JavaFXPlot3D;
import org.jscience.ui.i18n.I18n;

import java.util.HashMap;
import java.util.Map;

/**
 * Unified Function Explorer for 2D and 3D Mathematical Analysis.
 * Combines plotting, inspection, and calculus tools.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class FunctionExplorerViewer {

    private static final String DEFAULT_FX = "sin(x)";
    private static final String DEFAULT_GX = "x^2/10";
    private static final String DEFAULT_3D_FUNC = "sin(sqrt(x^2+y^2))";

    public static void show(Stage stage) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(
                create2DTab(),
                create3DTab());

        BorderPane root = new BorderPane(tabPane);
        root.setStyle("-fx-background-color: #f0f0f0;");

        Scene scene = new Scene(root, 1100, 750);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(I18n.getInstance().get("viewer.plotting"));
        stage.setScene(scene);
        stage.show();
    }

    // ==========================================
    // 2D FUNCTION EXPLORER
    // ==========================================
    private static Tab create2DTab() {
        Tab tab = new Tab("2D Explorer");
        SplitPane splitPane = new SplitPane();

        // --- Sidebar ---
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(15));
        sidebar.setMinWidth(300);
        sidebar.setMaxWidth(350);
        sidebar.setStyle("-fx-background-color: #fafafa;");

        // Input Section
        Label fLabel = new Label("f(x) =");
        TextField fInput = new TextField(DEFAULT_FX);
        Label gLabel = new Label("g(x) ="); // Second function support
        TextField gInput = new TextField(DEFAULT_GX);

        Label opLabel = new Label("Plot Operation:");
        ComboBox<String> opCombo = new ComboBox<>(FXCollections.observableArrayList(
                "f(x)", "g(x)", "f(x) + g(x)", "f(x) * g(x)"));
        opCombo.setValue("f(x)");

        Label rangeLabel = new Label("Range [Min, Max]");
        HBox rangeBox = new HBox(5);
        TextField xMinField = new TextField("-10");
        TextField xMaxField = new TextField("10");
        xMinField.setPrefWidth(60);
        xMaxField.setPrefWidth(60);
        rangeBox.getChildren().addAll(xMinField, xMaxField);

        Button plotBtn = new Button("Plot Function");
        plotBtn.setMaxWidth(Double.MAX_VALUE);
        plotBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

        // Analysis
        Separator sep1 = new Separator();
        Label analysisLabel = new Label("Analysis (Click Chart)");
        analysisLabel.setStyle("-fx-font-weight: bold;");

        Label cursorVal = new Label("Cursor: -");

        Label symTitle = new Label("Symbolic Derivative:");
        TextArea symResult = new TextArea(); // For potentially long formulas
        symResult.setPrefRowCount(2);
        symResult.setEditable(false);
        symResult.setWrapText(true);

        Button symbBtn = new Button("Calculate Derivative (Symbolic)");
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
        chartContainer.setStyle("-fx-background-color: white;");
        Label placeholder = new Label("Enter function and click Plot");
        chartContainer.getChildren().add(placeholder);

        // Logic
        Runnable doPlot = () -> {
            try {
                String fStr = fInput.getText();
                String gStr = gInput.getText();
                String op = opCombo.getValue();
                double min = Double.parseDouble(xMinField.getText());
                double max = Double.parseDouble(xMaxField.getText());

                Plot2D plot = new JavaFXPlot2D("Function Plot");
                plot.setGrid(true);
                plot.setLegend(true);
                plot.setXRange(Real.of(min), Real.of(max));

                // Parse Logic for combined functions
                // We'll create lambdas that use the parser
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

                // For comparison, maybe plot individual components lightly if combined?
                // For now keep simple.

                // Embed
                if (plot instanceof JavaFXPlot2D) {
                    LineChart<Number, Number> node = ((JavaFXPlot2D) plot).getNode();
                    node.setCreateSymbols(true); // Allow symbols for clicking? No, too many points.
                    // To handle click on chart background to get value
                    // We can listen to chart mouse events.

                    node.setOnMouseClicked(me -> {
                        // Conversion from scene to axis coordinates is available in Axis
                        // But axis are protected in standard chart, exposed in JavaFXPlot2D if we
                        // extended or modified it.
                        // Actually, JavaFXPlot2D does not expose Axis publically.
                        // However, we can use the relative position if we access axes from node lookup.
                        // Or just simplistic "approx" based on bounds.
                        // Better: The chart displays tooltips usually, or we can just report the pixel
                        // pos.
                        cursorVal.setText(String.format("Click at: (%.1f, %.1f) [Screen]", me.getX(), me.getY()));
                    });

                    chartContainer.getChildren().setAll(node);
                }

            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Plot Error: " + ex.getMessage());
                alert.show();
            }
        };

        plotBtn.setOnAction(e -> doPlot.run());

        symbBtn.setOnAction(e -> {
            String op = opCombo.getValue();
            String fStr = fInput.getText();
            String gStr = gInput.getText();

            String res = "";
            if (op.equals("f(x)"))
                res = SymbolicUtil.differentiate(fStr);
            else if (op.equals("g(x)"))
                res = SymbolicUtil.differentiate(gStr);
            else if (op.equals("f(x) + g(x)")) {
                res = SymbolicUtil.differentiate(fStr) + " + " + SymbolicUtil.differentiate(gStr);
            } else {
                res = "Symbolic product/complex operations not supported in demo.";
            }
            symResult.setText(res);
        });

        splitPane.getItems().addAll(sidebar, chartContainer);
        splitPane.setDividerPositions(0.3);
        tab.setContent(splitPane);
        return tab;
    }

    private static double evaluateSafe(SimpleExpressionParser p, double x) {
        try {
            return evaluate2D(p, x);
        } catch (Exception e) {
            return 0.0;
        }
    }

    // ==========================================
    // 3D FUNCTION EXPLORER
    // ==========================================
    private static Tab create3DTab() {
        Tab tab = new Tab("3D Explorer");
        SplitPane splitPane = new SplitPane();

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(15));
        sidebar.setMinWidth(300);
        sidebar.setMaxWidth(350);
        sidebar.setStyle("-fx-background-color: #fafafa;");

        Label funcLabel = new Label("z = f(x, y) =");
        TextField funcInput = new TextField(DEFAULT_3D_FUNC);

        Label rangeLabel = new Label("Range [±X, ±Y]");
        TextField rangeField = new TextField("10");

        CheckBox gridChk = new CheckBox("Show Grid/Axes");
        gridChk.setSelected(true);

        Button plotBtn = new Button("Plot 3D Surface");
        plotBtn.setMaxWidth(Double.MAX_VALUE);
        plotBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");

        // Analysis 3D
        Separator sep = new Separator();
        Label analysisLabel = new Label("Analysis");
        TextField xEval = new TextField("0");
        xEval.setPromptText("x");
        TextField yEval = new TextField("0");
        yEval.setPromptText("y");
        Label zRes = new Label("z = -");
        Button calcBtn = new Button("Calculate Point");

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
        chartContainer.setStyle("-fx-background-color: #222;"); // Dark background for 3D
        Label placeholder = new Label("Enter function and click Plot");
        placeholder.setTextFill(javafx.scene.paint.Color.WHITE);
        chartContainer.getChildren().add(placeholder);

        plotBtn.setOnAction(e -> {
            try {
                String expr = funcInput.getText();
                double range = Double.parseDouble(rangeField.getText());
                SimpleExpressionParser parser = new SimpleExpressionParser(expr);

                JavaFXPlot3D plot = new JavaFXPlot3D("3D Surface");
                plot.setInteractive(true);
                // Grid handling could be added to JavaFXPlot3D API, assuming defaults for now.

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

        splitPane.getItems().addAll(sidebar, chartContainer);
        splitPane.setDividerPositions(0.3);
        tab.setContent(splitPane);
        return tab;
    }

    // ==========================================
    // HELPERS
    // ==========================================

    private static double evaluate2D(SimpleExpressionParser parser, double x) {
        Map<String, Double> vars = new HashMap<>();
        vars.put("x", x);
        return parser.parse(vars);
    }

    private static double evaluate3D(SimpleExpressionParser parser, double x, double y) {
        Map<String, Double> vars = new HashMap<>();
        vars.put("x", x);
        vars.put("y", y);
        return parser.parse(vars);
    }
}
