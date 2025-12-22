/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.physics;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import org.jscience.apps.framework.KillerAppBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Quantum Circuit Simulator Application.
 * <p>
 * Interactive quantum circuit builder with state vector visualization
 * and Bloch sphere representation.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QuantumCircuitApp extends KillerAppBase {

    // Circuit components
    private static final int MAX_QUBITS = 4;
    private static final int MAX_GATES = 12;
    
    private List<List<String>> circuit; // [qubit][gate position]
    private double[] stateReal;
    private double[] stateImag;
    
    // UI
    private Canvas circuitCanvas;
    private Canvas blochCanvas;
    private Canvas probabilityCanvas;
    private Label stateVectorLabel;
    private ComboBox<Integer> qubitSelector;
    private ComboBox<String> gateSelector;
    private ListView<String> logView;

    @Override
    protected String getAppTitle() {
        return i18n.get("quantum.title") + " - JScience";
    }

    @Override
    protected Region createMainContent() {
        initializeCircuit();

        SplitPane mainSplit = new SplitPane();
        mainSplit.setOrientation(Orientation.HORIZONTAL);

        // Left: Circuit and visualization
        VBox leftPane = createCircuitPane();
        
        // Right: Controls and state
        VBox rightPane = createControlPane();
        rightPane.setPrefWidth(350);

        mainSplit.getItems().addAll(leftPane, rightPane);
        mainSplit.setDividerPositions(0.65);

        return mainSplit;
    }

    private void initializeCircuit() {
        circuit = new ArrayList<>();
        for (int i = 0; i < MAX_QUBITS; i++) {
            List<String> qubitGates = new ArrayList<>();
            for (int j = 0; j < MAX_GATES; j++) {
                qubitGates.add("-");
            }
            circuit.add(qubitGates);
        }
        resetState();
    }

    private void resetState() {
        int dim = (int) Math.pow(2, MAX_QUBITS);
        stateReal = new double[dim];
        stateImag = new double[dim];
        stateReal[0] = 1.0; // |0000⟩
    }

    private VBox createCircuitPane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(15));

        // Circuit canvas
        Label circuitLabel = new Label("🔲 Quantum Circuit");
        circuitLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        circuitCanvas = new Canvas(600, 200);
        circuitCanvas.setOnMouseClicked(this::handleCircuitClick);
        drawCircuit();

        // Visualizations
        HBox visuals = new HBox(20);
        
        // Bloch sphere
        VBox blochBox = new VBox(5);
        Label blochLabel = new Label("🎯 Bloch Sphere (Q0)");
        blochLabel.setStyle("-fx-font-weight: bold;");
        blochCanvas = new Canvas(180, 180);
        drawBlochSphere();
        blochBox.getChildren().addAll(blochLabel, blochCanvas);

        // Probability histogram
        VBox probBox = new VBox(5);
        Label probLabel = new Label("📊 Measurement Probabilities");
        probLabel.setStyle("-fx-font-weight: bold;");
        probabilityCanvas = new Canvas(350, 180);
        drawProbabilities();
        probBox.getChildren().addAll(probLabel, probabilityCanvas);

        visuals.getChildren().addAll(blochBox, probBox);

        pane.getChildren().addAll(circuitLabel, circuitCanvas, new Separator(), visuals);
        return pane;
    }

    private VBox createControlPane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(15));
        pane.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ddd; -fx-border-width: 0 0 0 1;");

        // Gate palette
        Label gateLabel = new Label("🔧 Add Gate");
        gateLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        HBox gateRow = new HBox(10);
        qubitSelector = new ComboBox<>();
        qubitSelector.getItems().addAll(0, 1, 2, 3);
        qubitSelector.setValue(0);
        qubitSelector.setPromptText("Qubit");

        gateSelector = new ComboBox<>();
        gateSelector.getItems().addAll("H", "X", "Y", "Z", "S", "T", "CNOT");
        gateSelector.setValue("H");
        gateSelector.setPromptText("Gate");

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> addGate());

        gateRow.getChildren().addAll(new Label("Qubit:"), qubitSelector, new Label("Gate:"), gateSelector, addBtn);

        // Quick gates
        Label quickLabel = new Label("⚡ Quick Gates");
        quickLabel.setStyle("-fx-font-weight: bold;");

        HBox quickGates = new HBox(5);
        for (String gate : new String[]{"H", "X", "Y", "Z"}) {
            Button btn = new Button(gate);
            btn.setPrefWidth(40);
            btn.setOnAction(e -> { gateSelector.setValue(gate); addGate(); });
            quickGates.getChildren().add(btn);
        }

        // State vector display
        Label stateLabel = new Label("📐 State Vector");
        stateLabel.setStyle("-fx-font-weight: bold;");

        stateVectorLabel = new Label("|ψ⟩ = |0000⟩");
        stateVectorLabel.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");
        stateVectorLabel.setWrapText(true);

        ScrollPane stateScroll = new ScrollPane(stateVectorLabel);
        stateScroll.setPrefHeight(100);
        stateScroll.setFitToWidth(true);

        // Log
        Label logLabel = new Label("📋 Execution Log");
        logLabel.setStyle("-fx-font-weight: bold;");
        logView = new ListView<>();
        logView.setPrefHeight(150);

        pane.getChildren().addAll(
            gateLabel, gateRow, quickLabel, quickGates,
            new Separator(),
            stateLabel, stateScroll,
            new Separator(),
            logLabel, logView
        );

        return pane;
    }

    private void addGate() {
        int qubit = qubitSelector.getValue();
        String gate = gateSelector.getValue();
        
        // Find first empty slot
        List<String> qubitGates = circuit.get(qubit);
        for (int i = 0; i < MAX_GATES; i++) {
            if ("-".equals(qubitGates.get(i))) {
                qubitGates.set(i, gate);
                log("Added " + gate + " gate to qubit " + qubit);
                drawCircuit();
                return;
            }
        }
        log("Circuit full for qubit " + qubit);
    }

    private void handleCircuitClick(javafx.scene.input.MouseEvent e) {
        // Calculate which gate was clicked
        double x = e.getX();
        double y = e.getY();
        int gateIdx = (int) ((x - 60) / 40);
        int qubitIdx = (int) ((y - 20) / 40);
        
        if (gateIdx >= 0 && gateIdx < MAX_GATES && qubitIdx >= 0 && qubitIdx < MAX_QUBITS) {
            String current = circuit.get(qubitIdx).get(gateIdx);
            if (!"-".equals(current)) {
                circuit.get(qubitIdx).set(gateIdx, "-");
                log("Removed gate from Q" + qubitIdx + " position " + gateIdx);
                drawCircuit();
            }
        }
    }

    private void drawCircuit() {
        GraphicsContext gc = circuitCanvas.getGraphicsContext2D();
        double w = circuitCanvas.getWidth();
        double h = circuitCanvas.getHeight();
        
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, w, h);

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);

        // Draw qubit lines
        for (int q = 0; q < MAX_QUBITS; q++) {
            double y = 40 + q * 40;
            gc.setFill(Color.BLACK);
            gc.fillText("|q" + q + "⟩", 10, y + 5);
            gc.strokeLine(50, y, w - 20, y);

            // Draw gates
            List<String> gates = circuit.get(q);
            for (int g = 0; g < MAX_GATES; g++) {
                String gate = gates.get(g);
                if (!"-".equals(gate)) {
                    double x = 60 + g * 40;
                    drawGate(gc, x, y, gate);
                }
            }
        }
    }

    private void drawGate(GraphicsContext gc, double x, double y, String gate) {
        gc.setFill(getGateColor(gate));
        gc.fillRoundRect(x - 15, y - 15, 30, 30, 5, 5);
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(x - 15, y - 15, 30, 30, 5, 5);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        gc.fillText(gate, x - 6, y + 4);
    }

    private Color getGateColor(String gate) {
        return switch (gate) {
            case "H" -> Color.ROYALBLUE;
            case "X" -> Color.CRIMSON;
            case "Y" -> Color.FORESTGREEN;
            case "Z" -> Color.PURPLE;
            case "S", "T" -> Color.ORANGE;
            case "CNOT" -> Color.DARKSLATEGRAY;
            default -> Color.GRAY;
        };
    }

    private void drawBlochSphere() {
        GraphicsContext gc = blochCanvas.getGraphicsContext2D();
        double w = blochCanvas.getWidth();
        double h = blochCanvas.getHeight();
        double cx = w / 2, cy = h / 2, r = 70;

        gc.setFill(Color.web("#f0f0f0"));
        gc.fillRect(0, 0, w, h);

        // Sphere outline
        gc.setStroke(Color.LIGHTGRAY);
        gc.strokeOval(cx - r, cy - r, 2 * r, 2 * r);

        // Axes
        gc.setStroke(Color.GRAY);
        gc.strokeLine(cx - r, cy, cx + r, cy); // X
        gc.strokeLine(cx, cy - r, cx, cy + r); // Z

        // State vector (simplified: just |0⟩ for now)
        gc.setFill(Color.BLUE);
        gc.fillOval(cx - 5, cy - r - 5, 10, 10);
        gc.setFill(Color.BLACK);
        gc.fillText("|0⟩", cx + 10, cy - r);
        gc.fillText("|1⟩", cx + 10, cy + r + 10);
    }

    private void drawProbabilities() {
        GraphicsContext gc = probabilityCanvas.getGraphicsContext2D();
        double w = probabilityCanvas.getWidth();
        double h = probabilityCanvas.getHeight();

        gc.setFill(Color.web("#f8f8f8"));
        gc.fillRect(0, 0, w, h);

        int dim = (int) Math.pow(2, MAX_QUBITS);
        double barWidth = (w - 40) / dim;
        double maxH = h - 40;

        gc.setFill(Color.STEELBLUE);
        for (int i = 0; i < dim; i++) {
            double prob = stateReal[i] * stateReal[i] + stateImag[i] * stateImag[i];
            double barH = prob * maxH;
            gc.fillRect(20 + i * barWidth, h - 20 - barH, barWidth - 2, barH);
        }

        gc.setFill(Color.BLACK);
        gc.fillText("|0000⟩", 18, h - 5);
        gc.fillText("|1111⟩", w - 45, h - 5);
    }

    @Override
    public void onRun() {
        log("Executing circuit...");
        resetState();
        
        // Apply gates column by column
        for (int col = 0; col < MAX_GATES; col++) {
            for (int q = 0; q < MAX_QUBITS; q++) {
                String gate = circuit.get(q).get(col);
                if (!"-".equals(gate)) {
                    applyGate(gate, q);
                }
            }
        }

        updateStateDisplay();
        drawProbabilities();
        log("Execution complete");
        setStatus(i18n.get("status.complete"));
    }

    private void applyGate(String gate, int qubit) {
        // Simplified gate application (demonstration)
        log("Applied " + gate + " to Q" + qubit);
        
        int dim = stateReal.length;
        int mask = 1 << (MAX_QUBITS - 1 - qubit);

        if ("X".equals(gate)) {
            // Bit flip
            for (int i = 0; i < dim; i++) {
                int j = i ^ mask;
                if (i < j) {
                    double tr = stateReal[i]; stateReal[i] = stateReal[j]; stateReal[j] = tr;
                    double ti = stateImag[i]; stateImag[i] = stateImag[j]; stateImag[j] = ti;
                }
            }
        } else if ("H".equals(gate)) {
            // Hadamard
            double sqrt2 = Math.sqrt(2);
            for (int i = 0; i < dim; i++) {
                int j = i ^ mask;
                if (i < j) {
                    double ar = stateReal[i], ai = stateImag[i];
                    double br = stateReal[j], bi = stateImag[j];
                    stateReal[i] = (ar + br) / sqrt2;
                    stateImag[i] = (ai + bi) / sqrt2;
                    stateReal[j] = (ar - br) / sqrt2;
                    stateImag[j] = (ai - bi) / sqrt2;
                }
            }
        } else if ("Z".equals(gate)) {
            // Phase flip
            for (int i = 0; i < dim; i++) {
                if ((i & mask) != 0) {
                    stateReal[i] = -stateReal[i];
                    stateImag[i] = -stateImag[i];
                }
            }
        }
    }

    private void updateStateDisplay() {
        StringBuilder sb = new StringBuilder("|ψ⟩ = ");
        int dim = stateReal.length;
        boolean first = true;
        for (int i = 0; i < dim; i++) {
            double r = stateReal[i], im = stateImag[i];
            double prob = r * r + im * im;
            if (prob > 0.001) {
                if (!first) sb.append(" + ");
                sb.append(String.format("%.3f|%s⟩", Math.sqrt(prob), 
                    String.format("%" + MAX_QUBITS + "s", Integer.toBinaryString(i)).replace(' ', '0')));
                first = false;
            }
        }
        stateVectorLabel.setText(sb.toString());
    }

    @Override
    public void onReset() {
        initializeCircuit();
        drawCircuit();
        drawProbabilities();
        stateVectorLabel.setText("|ψ⟩ = |0000⟩");
        log("Circuit cleared");
        setStatus(i18n.get("status.ready"));
    }

    private void log(String msg) {
        String ts = java.time.LocalTime.now().toString().substring(0, 8);
        logView.getItems().add(0, "[" + ts + "] " + msg);
        if (logView.getItems().size() > 50) logView.getItems().remove(logView.getItems().size() - 1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
