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

package org.jscience.apps.physics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import org.jscience.apps.framework.KillerAppBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Quantum Circuit Simulator Application.
 * <p>
 * Interactive quantum circuit builder with state vector visualization
 * and probability estimation.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QuantumCircuitApp extends KillerAppBase {

    private VBox circuitPane;
    private Label stateVectorLabel;
    private BarChart<String, Number> probChart;
    private int selectedQubitIndex = -1;
    private List<HBox> qubitLines = new ArrayList<>();
    private final int NUM_QUBITS = 3;

    @Override
    protected String getAppTitle() {
        return i18n.get("quantum.title");
    }

    @Override
    public String getDescription() {
        return i18n.get("quantum.desc");
    }

    @Override
    protected Region createMainContent() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));

        // Header
        Label title = new Label(i18n.get("quantum.title"));
        title.setFont(Font.font(24));
        pane.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);

        // Center Content
        VBox centerBox = new VBox(15);

        // Gate Toolbar
        ToolBar gateToolbar = new ToolBar();
        gateToolbar.getItems().add(new Label(i18n.get("quantum.toolbar.gates")));

        String[] gates = { "H", "X", "Y", "Z", "CNOT", "M" };
        for (String g : gates) {
            Button b = new Button(g);
            b.setTooltip(new Tooltip(i18n.get("quantum.gate.tooltip." + g.toLowerCase())));
            b.setOnAction(e -> addGateToSelectedLine(g));
            gateToolbar.getItems().add(b);
        }

        gateToolbar.getItems().add(new Separator());
        Button runBtn = new Button("Ã¢â€“Â¶ " + i18n.get("quantum.button.run"));
        runBtn.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        runBtn.setOnAction(e -> runSimulation());
        gateToolbar.getItems().add(runBtn);

        Button clearBtn = new Button("Ã°Å¸â€”â€˜ " + i18n.get("quantum.button.clear"));
        clearBtn.setOnAction(e -> clearCircuit());
        gateToolbar.getItems().add(clearBtn);

        // Qubit lines
        circuitPane = new VBox(5);
        circuitPane.setPadding(new Insets(10));
        circuitPane.setStyle("-fx-background-color: white; -fx-border-color: #ccc;");

        initializeCircuitOrEmpty();

        // Results
        VBox resultPane = new VBox(5);
        resultPane.setPadding(new Insets(10));
        Label resTitle = new Label(i18n.get("quantum.label.results"));
        resTitle.setFont(Font.font(16));

        stateVectorLabel = new Label(i18n.get("quantum.label.statevector") + ": |0...0>");
        stateVectorLabel.setWrapText(true);
        stateVectorLabel.setFont(Font.font("Monospaced", 12));

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        probChart = new BarChart<>(xAxis, yAxis);
        probChart.setTitle(i18n.get("quantum.chart.probabilities"));
        probChart.setAnimated(false);
        probChart.setMinHeight(200);

        resultPane.getChildren().addAll(resTitle, stateVectorLabel, probChart);

        centerBox.getChildren().addAll(gateToolbar, circuitPane, new Separator(), resultPane);
        pane.setCenter(centerBox);

        return pane;
    }

    private void initializeCircuitOrEmpty() {
        circuitPane.getChildren().clear();
        qubitLines.clear();
        for (int i = 0; i < NUM_QUBITS; i++) {
            HBox line = new HBox(5);
            line.setAlignment(Pos.CENTER_LEFT);
            line.setPadding(new Insets(5));
            line.setStyle("-fx-border-width: 0 0 1 0; -fx-border-color: #eee;");

            // Qubit selector/label
            ToggleButton qBtn = new ToggleButton(i18n.get("quantum.label.qubit_title", i));

            qBtn.setPrefWidth(80);
            int finalI = i;
            qBtn.setOnAction(e -> {
                selectedQubitIndex = qBtn.isSelected() ? finalI : -1;
                updateQubitSelection();
            });

            line.getChildren().add(qBtn);

            // Wire visual lines can be drawn by background or separator,
            // but for simple HBox, we just append buttons.

            circuitPane.getChildren().add(line);
            qubitLines.add(line);
        }
    }

    private void updateQubitSelection() {
        for (int i = 0; i < qubitLines.size(); i++) {
            if (!qubitLines.get(i).getChildren().isEmpty()
                    && qubitLines.get(i).getChildren().get(0) instanceof ToggleButton) {
                ToggleButton btn = (ToggleButton) qubitLines.get(i).getChildren().get(0);
                btn.setSelected(i == selectedQubitIndex);
            }
        }
    }

    private void addGateToSelectedLine(String gate) {
        if (selectedQubitIndex >= 0 && selectedQubitIndex < qubitLines.size()) {
            HBox line = qubitLines.get(selectedQubitIndex);
            Button gateBtn = new Button(gate);
            gateBtn.setPrefWidth(40);
            gateBtn.setStyle("-fx-base: #e0f0ff;");
            // Allow removing gate on click
            gateBtn.setOnAction(e -> line.getChildren().remove(gateBtn));
            line.getChildren().add(gateBtn);
        }
    }

    private void clearCircuit() {
        initializeCircuitOrEmpty();
        stateVectorLabel.setText(i18n.get("quantum.label.statevector") + ": |0...0>");
        probChart.getData().clear();
        selectedQubitIndex = -1;
    }

    private void runSimulation() {
        // Mock simulation
        Random rand = new Random();
        probChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(i18n.get("quantum.series.probabilities"));

        // Generate random probabilities for 3 qubits = 8 states
        double[] probs = new double[8];
        double sum = 0;
        for (int i = 0; i < 8; i++) {
            probs[i] = rand.nextDouble();
            sum += probs[i];
        }
        for (int i = 0; i < 8; i++) {
            probs[i] /= sum;
            String state = String.format("|%3sÃ¢Å¸Â©", Integer.toBinaryString(i)).replace(' ', '0');
            series.getData().add(new XYChart.Data<>(state, probs[i]));
        }
        probChart.getData().add(series);
        stateVectorLabel
                .setText(i18n.get("quantum.label.statevector") + ": " + i18n.get("quantum.status.superposition"));

    }
}


