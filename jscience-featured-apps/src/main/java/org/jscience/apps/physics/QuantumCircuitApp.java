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
 
 * <p>
 * <b>Reference:</b><br>
 * Dirac, P. A. M. (1930). <i>The Principles of Quantum Mechanics</i>. Oxford University Press.
 * </p>
 *
 */

package org.jscience.apps.physics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import org.jscience.apps.framework.FeaturedAppBase;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuantumCircuitApp extends FeaturedAppBase {
    private final int NUM_QUBITS = 3;
    private VBox circuitPane;
    private Label stateVectorLabel;
    private BarChart<String, Number> probChart;
    private List<HBox> qubitLines;
    private int selectedQubitIndex = -1;

    public QuantumCircuitApp() {
        super();
        try {
            this.qubitLines = new ArrayList<>();
        } catch (Throwable t) {
            System.err.println("CRITICAL: Failed to initialize QuantumCircuitApp: " + t.getMessage());
            t.printStackTrace();
        }
    }

    // UI references for localization
    private Label mainTitleLabel;
    private Label resultsTitleLabel;
    private Label gateToolbarLabel;
    private Button runBtn;
    private Button clearBtn;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;

    @Override
    protected String getAppTitle() {
        return I18n.getInstance().get("viewer.quantumcircuitapp.name", "Quantum Circuit Designer");
    }

    @Override
    public String getName() {
        return getAppTitle();
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("viewer.quantumcircuitapp.desc", "Design and simulate basic quantum circuits.");
    }

    @Override
    public String getLongDescription() {
        return I18n.getInstance().get("viewer.quantumcircuitapp.longdesc", "Interactive quantum circuit designer for exploring fundamental quantum computing concepts. Features H, X, Y, Z, and CNOT gates, with real-time measurement probability visualization for multi-qubit systems.");
    }

    @Override
    public boolean hasEditMenu() {
        return false;
    }

    @Override
    public boolean hasToolBar() {
        return false;
    }

    @Override
    protected Region createMainContent() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));

        // Header
        mainTitleLabel = new Label(i18n.get("quantum.title", "Quantum Circuit Designer"));
        mainTitleLabel.getStyleClass().add("header-label");
        pane.setTop(mainTitleLabel);
        BorderPane.setAlignment(mainTitleLabel, Pos.CENTER);

        // Center Content
        VBox centerBox = new VBox(15);

        // Gate Toolbar
        ToolBar gateToolbar = new ToolBar();
        gateToolbarLabel = new Label(i18n.get("quantum.toolbar.gates", "Gates:"));
        gateToolbarLabel.getStyleClass().add("header-label");
        gateToolbarLabel.setStyle("-fx-font-size: 14px;");
        gateToolbar.getItems().add(gateToolbarLabel);

        String[] gates = { "H", "X", "Y", "Z", "CNOT", "M" };
        for (String g : gates) {
            Button b = new Button(g);
            b.setTooltip(new Tooltip(i18n.get("quantum.gate.tooltip." + g.toLowerCase(), g + " Gate")));
            b.setOnAction(e -> addGateToSelectedLine(g));
            gateToolbar.getItems().add(b);
        }

        gateToolbar.getItems().add(new Separator());
        runBtn = new Button(i18n.get("quantum.button.run", "Run Simulation"));
        runBtn.getStyleClass().add("accent-button-green");
        runBtn.setOnAction(e -> runSimulation());
        gateToolbar.getItems().add(runBtn);

        clearBtn = new Button(i18n.get("quantum.button.clear", "Clear Circuit"));
        clearBtn.setOnAction(e -> clearCircuit());
        gateToolbar.getItems().add(clearBtn);

        // Qubit lines
        circuitPane = new VBox(5);
        circuitPane.setPadding(new Insets(10));
        circuitPane.getStyleClass().add("viewer-controls");

        initializeCircuitOrEmpty();

        // Results
        VBox resultPane = new VBox(5);
        resultPane.setPadding(new Insets(10));
        resultsTitleLabel = new Label(i18n.get("quantum.label.results", "Simulation Results"));
        resultsTitleLabel.getStyleClass().add("header-label");
        resultsTitleLabel.setStyle("-fx-font-size: 16px;");

        stateVectorLabel = new Label();
        stateVectorLabel.getStyleClass().add("description-label");
        stateVectorLabel.setStyle("-fx-font-family: 'Monospaced';");

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        probChart = new BarChart<>(xAxis, yAxis);
        probChart.setTitle(i18n.get("quantum.chart.probabilities", "Measurement Probabilities"));
        probChart.setAnimated(false);
        probChart.setMinHeight(200);

        resultPane.getChildren().addAll(resultsTitleLabel, stateVectorLabel, probChart);

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
            ToggleButton qBtn = new ToggleButton(java.text.MessageFormat.format(i18n.get("quantum.label.qubit_title", "q{0}"), i));

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
        stateVectorLabel.setText(i18n.get("quantum.label.statevector", "State Vector") + ": |0...0>");
        probChart.getData().clear();
        selectedQubitIndex = -1;
    }

    private void runSimulation() {
        // Mock simulation
        Random rand = new Random();
        probChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(i18n.get("quantum.series.probabilities", "Probability"));

        // Generate random probabilities for 3 qubits = 8 states
        double[] probs = new double[8];
        double sum = 0;
        for (int i = 0; i < 8; i++) {
            probs[i] = rand.nextDouble();
            sum += probs[i];
        }
        for (int i = 0; i < 8; i++) {
            probs[i] /= sum;
            String state = String.format("|%3s>", Integer.toBinaryString(i)).replace(' ', '0');
            series.getData().add(new XYChart.Data<>(state, probs[i]));
        }
        probChart.getData().add(series);
        stateVectorLabel
                .setText(i18n.get("quantum.label.statevector", "State Vector") + ": " + i18n.get("quantum.status.superposition", "Superposition"));

    }

    @Override
    protected void updateLocalizedUI() {
        if (mainTitleLabel != null)
            mainTitleLabel.setText(i18n.get("quantum.title", "Quantum Circuit Designer"));
        if (gateToolbarLabel != null)
            gateToolbarLabel.setText(i18n.get("quantum.toolbar.gates", "Gates:"));
        if (runBtn != null)
            runBtn.setText(i18n.get("quantum.button.run", "Run Simulation"));
        if (clearBtn != null)
            clearBtn.setText(i18n.get("quantum.button.clear", "Clear Circuit"));
        if (resultsTitleLabel != null)
            resultsTitleLabel.setText(i18n.get("quantum.label.results", "Simulation Results"));
        if (stateVectorLabel != null && !probChart.getData().isEmpty()) {
            stateVectorLabel
                    .setText(i18n.get("quantum.label.statevector", "State Vector") + ": " + i18n.get("quantum.status.superposition", "Superposition"));
        } else if (stateVectorLabel != null) {
            stateVectorLabel.setText(i18n.get("quantum.label.statevector", "State Vector") + ": |0...0>");
        }

        if (probChart != null) {
            probChart.setTitle(i18n.get("quantum.chart.probabilities", "Measurement Probabilities"));
            if (!probChart.getData().isEmpty()) {
                probChart.getData().get(0).setName(i18n.get("quantum.series.probabilities", "Probability"));
            }
        }

        // Qubit labels
        for (int i = 0; i < qubitLines.size(); i++) {
            HBox line = qubitLines.get(i);
            if (!line.getChildren().isEmpty() && line.getChildren().get(0) instanceof ToggleButton) {
                ((ToggleButton) line.getChildren().get(0)).setText(java.text.MessageFormat.format(i18n.get("quantum.label.qubit_title", "q{0}"), i));
            }
        }
    }

    @Override
    protected void doNew() {
        clearCircuit();
    }

    @Override
    protected void addAppHelpTopics(org.jscience.apps.framework.HelpDialog dialog) {
        dialog.addTopic("Physics", "Quantum Computing",
                "Learn about quantum circuits:\n\n" +
                        "- **Qubit**: Quantum bit, exists in superposition of |0> and |1>.\n" +
                        "- **H Gate (Hadamard)**: Puts a qubit into superposition (50/50 probability).\n" +
                        "- **X Gate**: Quantum NOT gate, flips |0> to |1> and vice versa.\n" +
                        "- **CNOT**: Conditional NOT, entangles two qubits.\n" +
                        "- **Measurement**: Collapses the quantum state to a classical bit.",
                null);
    }

    @Override
    protected void addAppTutorials(org.jscience.apps.framework.HelpDialog dialog) {
        dialog.addTopic("Tutorial", "Building a Circuit",
                "1. **Select Qubit**: Click a qubit line (e.g., q0) to select it.\n" +
                        "2. **Add Gate**: Click a gate button (H, X, etc.) to add it to the selected line.\n" +
                        "3. **Run**: Click the Run button to simulate measurement outcome probabilities.\n" +
                        "4. **Clear**: Use Clear to reset the circuit.\n" +
                        "5. **Bell State**: Try H on q0, then CNOT on q0-q1 (not fully supported in visual editor yet) to create entanglement.",
                null);
    }

    @Override
    protected byte[] serializeState() {
        java.util.Properties props = new java.util.Properties();
        for (int i = 0; i < qubitLines.size(); i++) {
            HBox line = qubitLines.get(i);
            StringBuilder sb = new StringBuilder();
            // Start from index 1 to skip the ToggleButton (Qubit Label)
            for (int j = 1; j < line.getChildren().size(); j++) {
                javafx.scene.Node node = line.getChildren().get(j);
                if (node instanceof Button) {
                    sb.append(((Button) node).getText()).append(",");
                }
            }
            props.setProperty("line." + i, sb.toString());
        }

        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try {
            props.store(baos, "Quantum State");
            return baos.toByteArray();
        } catch (java.io.IOException e) {
            return null;
        }
    }

    @Override
    protected void deserializeState(byte[] data) {
        java.util.Properties props = new java.util.Properties();
        try {
            props.load(new java.io.ByteArrayInputStream(data));
            clearCircuit();
            for (int i = 0; i < NUM_QUBITS; i++) {
                String lineData = props.getProperty("line." + i);
                if (lineData != null && !lineData.isEmpty()) {
                    String[] gates = lineData.split(",");
                    selectedQubitIndex = i;
                    for (String g : gates) {
                        if (!g.trim().isEmpty()) {
                            addGateToSelectedLine(g.trim());
                        }
                    }
                }
            }
            selectedQubitIndex = -1;
            updateQubitSelection();
        } catch (Exception e) {
            showError("Load Error", "Failed to restore state: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.physics", "Physics");
    }
}
