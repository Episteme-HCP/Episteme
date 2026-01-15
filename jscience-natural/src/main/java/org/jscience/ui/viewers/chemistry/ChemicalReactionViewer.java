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

package org.jscience.ui.viewers.chemistry;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.chemistry.ChemicalReactionParser;
import org.jscience.chemistry.ChemicalReactionParser.Formula;
import org.jscience.chemistry.ChemicalReactionParser.Reaction;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.i18n.I18n;

/**
 * Chemical Reaction Viewer and Parser.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ChemicalReactionViewer extends AbstractViewer {

    private TextArea inputArea;
    private TextArea outputArea;
    private Label statusLabel;
    
    @Override
    public String getCategory() { return "Chemistry"; }
    
    @Override
    public String getName() { return I18n.getInstance().get("chemical.title.parser", "Chemical Reaction Parser"); }

    public ChemicalReactionViewer() {
        initUI();
    }

    private void initUI() { // Refactored from start(Stage) to build into 'this'
        this.getStyleClass().add("dark-viewer-root");
        this.setPadding(new Insets(15));

        // Header (Optional inside Viewer, or leave to Demo? Viewer usually fills center)
        // We'll keep layout similar to previous Application start method, but adapting to BorderPane 'this'
        
        // Input area
        VBox inputBox = new VBox(5);
        inputBox.setPadding(new Insets(10));
        inputBox.getStyleClass().add("dark-viewer-sidebar");

        Label inputLabel = new Label(I18n.getInstance().get("chemical.label.eqn"));
        inputLabel.getStyleClass().add("dark-label-muted");

        inputArea = new TextArea();
        inputArea.setPromptText(I18n.getInstance().get("chemical.prompt.eqn", "Enter chemical equation, e.g.: 2H2 + O2 -> 2H2O"));
        inputArea.setPrefRowCount(3);
        inputArea.setWrapText(true);

        ComboBox<String> exampleCombo = new ComboBox<>();
        exampleCombo.setPromptText(I18n.getInstance().get("chemical.prompt.example", "Select an example..."));
        exampleCombo.getItems().addAll(
                "2H2 + O2 -> 2H2O",
                "CH4 + 2O2 -> CO2 + 2H2O",
                "HCl + NaOH -> NaCl + H2O",
                "6CO2 + 6H2O -> C6H12O6 + 6O2",
                "C8H18 + 12.5O2 -> 8CO2 + 9H2O",
                "Fe2O3 + 3CO -> 2Fe + 3CO2"
        );
        exampleCombo.setMaxWidth(Double.MAX_VALUE);
        exampleCombo.setOnAction(e -> {
            if (exampleCombo.getValue() != null) {
                inputArea.setText(exampleCombo.getValue());
                parseReaction();
            }
        });

        Button loadFileBtn = new Button(I18n.getInstance().get("chemical.btn.load"));
        loadFileBtn.setMaxWidth(Double.MAX_VALUE);
        loadFileBtn.setOnAction(e -> {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle(I18n.getInstance().get("chemical.file.open", "Open Reaction File"));
            fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter(I18n.getInstance().get("chemical.file.type", "Text Files"), "*.txt", "*.rxn"));
            java.io.File file = fileChooser.showOpenDialog(getScene().getWindow());
            if (file != null) {
                try {
                    String content = java.nio.file.Files.readString(file.toPath());
                    inputArea.setText(content.trim());
                    parseReaction();
                } catch (Exception ex) {
                    outputArea.setText(I18n.getInstance().get("chemical.error.read", "Error reading file: %s", ex.getMessage()));
                }
            }
        });

        HBox controls = new HBox(10, exampleCombo, loadFileBtn);
        HBox.setHgrow(exampleCombo, Priority.ALWAYS);

        Button parseBtn = new Button(I18n.getInstance().get("chemical.btn.parse"));
        parseBtn.getStyleClass().add("accent-button-green");
        parseBtn.setMaxWidth(Double.MAX_VALUE);
        parseBtn.setOnAction(e -> parseReaction());

        inputBox.getChildren().addAll(inputLabel, inputArea, controls, parseBtn);

        // Output area
        VBox outputBox = new VBox(5);
        outputBox.setPadding(new Insets(10));
        outputBox.getStyleClass().add("dark-viewer-sidebar");

        Label outputLabel = new Label(I18n.getInstance().get("chemical.label.results"));
        outputLabel.getStyleClass().add("dark-label-muted");

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefRowCount(15);
        outputArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-weight: bold;");

        outputBox.getChildren().addAll(outputLabel, statusLabel, outputArea);

        VBox center = new VBox(15);
        center.getChildren().addAll(inputBox, outputBox);
        this.setCenter(center);

        // Formula parser panel on right (Sidebar)
        VBox formulaPanel = new VBox(10);
        formulaPanel.setPadding(new Insets(10));
        formulaPanel.setStyle("-fx-background-color: #222222; -fx-background-radius: 5;"); // Or dark-viewer-sidebar
        formulaPanel.setPrefWidth(250);

        Label formulaTitle = new Label(I18n.getInstance().get("chemical.label.quick"));
        formulaTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #00d9ff;");

        TextField formulaInput = new TextField();
        formulaInput.setPromptText(I18n.getInstance().get("chemical.prompt.formula", "e.g., Ca(OH)2"));

        TextArea formulaOutput = new TextArea();
        formulaOutput.setEditable(false);
        formulaOutput.setPrefRowCount(6);
        formulaOutput.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 11px; -fx-text-fill: #aaaaaa; -fx-control-inner-background: #333333;");

        Button parseFormulaBtn = new Button(I18n.getInstance().get("chemical.btn.formula"));
        parseFormulaBtn.setMaxWidth(Double.MAX_VALUE);
        parseFormulaBtn.setOnAction(e -> {
            try {
                Formula f = ChemicalReactionParser.parseFormula(formulaInput.getText());
                StringBuilder sb = new StringBuilder();
                sb.append(I18n.getInstance().get("chemical.label.formula", "Formula: %s", f.toString())).append("\n");
                sb.append(I18n.getInstance().get("chemical.label.coefficient", "Coefficient: %d", (long) f.getCoefficient())).append("\n");
                sb.append(I18n.getInstance().get("chemical.label.elements", "Elements:")).append("\n");
                for (var entry : f.getElements().entrySet()) sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                if (f.getState() != null) sb.append(I18n.getInstance().get("chemical.label.state", "State: %s", f.getState())).append("\n");
                if (f.getCharge() != 0) sb.append(I18n.getInstance().get("chemical.label.charge", "Charge: %d", (long) f.getCharge())).append("\n");
                formulaOutput.setText(sb.toString());
            } catch (Exception ex) {
                formulaOutput.setText(I18n.getInstance().get("chemical.status.error", "Error: %s", ex.getMessage()));
            }
        });

        formulaPanel.getChildren().addAll(formulaTitle, formulaInput, parseFormulaBtn, formulaOutput);
        this.setRight(formulaPanel);
    }

    private void parseReaction() {
        String input = inputArea.getText().trim();
        if (input.isEmpty()) {
            outputArea.setText(I18n.getInstance().get("chemical.msg.enter"));
            return;
        }

        try {
            Reaction reaction = ChemicalReactionParser.parse(input);

            StringBuilder sb = new StringBuilder();
            sb.append("══════════════════════════════════════\n");
            sb.append(I18n.getInstance().get("chemical.header.parsed", "PARSED REACTION")).append("\n");
            sb.append("══════════════════════════════════════\n\n");
            sb.append(I18n.getInstance().get("chemical.label.formula", "Formatted: %s", reaction.toString())).append("\n\n");

            sb.append("─── ").append(I18n.getInstance().get("chemical.header.reactants", "REACTANTS")).append(" ───\n");
            for (Formula f : reaction.getReactants()) {
                sb.append("  ").append(formatFormula(f)).append("\n");
            }

            sb.append("\n─── ").append(I18n.getInstance().get("chemical.header.products", "PRODUCTS")).append(" ───\n");
            for (Formula f : reaction.getProducts()) {
                sb.append("  ").append(formatFormula(f)).append("\n");
            }

            sb.append("\n─── ").append(I18n.getInstance().get("chemical.header.balance", "ELEMENT BALANCE")).append(" ───\n");
            sb.append(reaction.getElementBalance());

            boolean balanced = reaction.isBalanced();
            sb.append("\n══════════════════════════════════════\n");
            sb.append(I18n.getInstance().get("chemical.result.prefix", "RESULT: "))
                    .append(balanced ? I18n.getInstance().get("chemical.result.balanced", "✓ BALANCED")
                            : I18n.getInstance().get("chemical.result.unbalanced", "✗ NOT BALANCED"))
                    .append("\n");
            sb.append("══════════════════════════════════════\n");

            outputArea.setText(sb.toString());

            if (balanced) {
                statusLabel.setText(I18n.getInstance().get("chemical.status.balanced"));
                statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
            } else {
                statusLabel.setText(I18n.getInstance().get("chemical.status.unbalanced"));
                statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            }

        } catch (Exception e) {
            outputArea.setText(I18n.getInstance().get("chemical.error.parse", "Error parsing equation:\n%s", e.getMessage()));
            statusLabel.setText(I18n.getInstance().get("chemical.status.error"));
            statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
        }
    }

    private String formatFormula(Formula f) {
        StringBuilder sb = new StringBuilder();
        sb.append(f.toString());
        sb.append(I18n.getInstance().get("chemical.out.elements", " \u2192 Elements: "));
        for (var e : f.getTotalElements().entrySet()) {
            sb.append(e.getKey()).append("=").append(e.getValue()).append(" ");
        }
        return sb.toString();
    }

    @Override public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.chemicalreaction.desc"); }
    @Override public String getLongDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("viewer.chemicalreaction.longdesc"); }
    @Override public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() { return new java.util.ArrayList<>(); }
}
