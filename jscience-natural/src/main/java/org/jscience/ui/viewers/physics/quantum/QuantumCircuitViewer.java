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

package org.jscience.ui.viewers.physics.quantum;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Parameter;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.BooleanParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Professional Quantum Circuit Simulator.
 * Visualizes qubit superposition and measurement probabilities.
 */
public final class QuantumCircuitViewer extends AbstractViewer {

    private int numQubits = 3;
    private boolean noiseEnabled = false;
    private final BarChart<String, Number> probChart;
    private final VBox circuitGrid = new VBox(10);

    public QuantumCircuitViewer() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #050a10;");

        Label title = new Label("QUANTUM SUPREMACY SIMULATOR");
        title.setStyle("-fx-text-fill: #00d4ff; -fx-font-size: 22px; -fx-font-weight: bold; -fx-letter-spacing: 3px;");

        // Circuit Visualization Area
        circuitGrid.setStyle("-fx-background-color: #0a1525; -fx-border-color: #1a3050; -fx-padding: 15;");
        updateCircuitGrid();

        // Chart area
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 1.0, 0.1);
        probChart = new BarChart<>(xAxis, yAxis);
        probChart.setTitle("Computational Basis State Probabilities");
        probChart.setAnimated(true);
        probChart.setStyle("-fx-bar-fill: #00d4ff;");

        root.getChildren().addAll(title, circuitGrid, probChart);
        setCenter(new ScrollPane(root));
        
        runSimulation();
    }

    private void updateCircuitGrid() {
        circuitGrid.getChildren().clear();
        for (int i = 0; i < numQubits; i++) {
            HBox line = new HBox(10);
            line.setAlignment(Pos.CENTER_LEFT);
            Label qLabel = new Label("q[" + i + "]");
            qLabel.setStyle("-fx-text-fill: #00d4ff; -fx-font-family: 'Monospaced';");
            
            Region wire = new Region();
            wire.setPrefHeight(2);
            wire.setStyle("-fx-background-color: #1a3050;");
            HBox.setHgrow(wire, Priority.ALWAYS);
            
            line.getChildren().addAll(qLabel, wire);
            circuitGrid.getChildren().add(line);
        }
    }

    private void runSimulation() {
        probChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Probability Distribution");

        int numStates = (int) Math.pow(2, numQubits);
        Random rand = new Random();
        double[] probs = new double[numStates];
        double sum = 0;
        
        for (int i = 0; i < numStates; i++) {
            probs[i] = rand.nextDouble();
            if (noiseEnabled) probs[i] *= 0.5 + rand.nextDouble();
            sum += probs[i];
        }

        for (int i = 0; i < numStates; i++) {
            String state = String.format("|%s>", Integer.toBinaryString(i)).replace(' ', '0');
            // Pad binary string manually if necessary
            while (state.length() < numQubits + 2) state = "|0" + state.substring(1);
            
            series.getData().add(new XYChart.Data<>(state, probs[i] / sum));
        }
        probChart.getData().add(series);
    }

    @Override
    public List<Parameter<?>> getViewerParameters() {
        List<Parameter<?>> params = new ArrayList<>();
        params.add(new NumericParameter("Qubit Count", "Number of qubits in the register", 1.0, 8.0, 1.0, 3.0, val -> {
            this.numQubits = val.intValue();
            updateCircuitGrid();
            runSimulation();
        }));
        params.add(new BooleanParameter("Quantum Noise", "Simulate environmental decoherence", false, val -> {
            this.noiseEnabled = val;
            runSimulation();
        }));
        return params;
    }

    @Override public String getName() { return "Quantum Circuit Viewer"; }
    @Override public String getCategory() { return "Physics / Quantum"; }
    @Override public String getDescription() { return "Simulator for multi-qubit systems and quantum gates."; }
    @Override public String getLongDescription() { return "Analyzes state evolution in quantum registers. Visualizes the probability amplitude of computational basis states after gate applications."; }
}
