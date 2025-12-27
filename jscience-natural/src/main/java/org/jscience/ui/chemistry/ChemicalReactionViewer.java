/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.chemistry;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jscience.chemistry.ChemicalReactionParser;
import org.jscience.chemistry.ChemicalReactionParser.Formula;
import org.jscience.chemistry.ChemicalReactionParser.Reaction;
import org.jscience.ui.i18n.I18n;

/**
 * Chemical Reaction Viewer and Parser Demo.
 * Interactive tool for parsing and analyzing chemical equations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class ChemicalReactionViewer extends Application {

    private TextArea inputArea;
    private TextArea outputArea;
    private Label statusLabel;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dark-viewer-root");
        root.setPadding(new Insets(15));

        // Header
        VBox header = new VBox(5);
        Label title = new Label(I18n.getInstance().get("chemical.title.parser"));
        title.getStyleClass().add("dark-label-accent");
        Label subtitle = new Label(I18n.getInstance().get("chemical.subtitle"));
        subtitle.getStyleClass().add("dark-label-muted");
        header.getChildren().addAll(title, subtitle);
        root.setTop(header);
        BorderPane.setMargin(header, new Insets(0, 0, 15, 0));

        // Input area
        VBox inputBox = new VBox(5);
        inputBox.setPadding(new Insets(10));
        inputBox.getStyleClass().add("dark-viewer-sidebar");

        Label inputLabel = new Label(I18n.getInstance().get("chemical.label.eqn"));
        inputLabel.getStyleClass().add("dark-label-muted");

        inputArea = new TextArea();
        inputArea.setPromptText("Enter chemical equation, e.g.: 2H2 + O2 -> 2H2O");
        inputArea.setPrefRowCount(3);
        inputArea.setWrapText(true);

        // Example buttons
        HBox examples = new HBox(10);
        examples.getChildren().addAll(
                createExampleButton("Water synthesis", "2H2 + O2 -> 2H2O"),
                createExampleButton("Combustion", "CH4 + 2O2 -> CO2 + 2H2O"),
                createExampleButton("Neutralization", "HCl + NaOH -> NaCl + H2O"),
                createExampleButton("Photosynthesis", "6CO2 + 6H2O -> C6H12O6 + 6O2"));

        Button parseBtn = new Button(I18n.getInstance().get("chemical.btn.parse"));
        parseBtn.getStyleClass().add("accent-button-green");
        parseBtn.setOnAction(e -> parseReaction());

        inputBox.getChildren().addAll(inputLabel, inputArea, examples, parseBtn);

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

        // Layout
        VBox center = new VBox(15);
        center.getChildren().addAll(inputBox, outputBox);
        root.setCenter(center);

        // Formula parser panel on right
        VBox formulaPanel = new VBox(10);
        formulaPanel.setPadding(new Insets(10));
        formulaPanel.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 5;");
        formulaPanel.setPrefWidth(250);

        Label formulaTitle = new Label(I18n.getInstance().get("chemical.label.quick"));
        formulaTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #00d9ff;");

        TextField formulaInput = new TextField();
        formulaInput.setPromptText("e.g., Ca(OH)2");

        TextArea formulaOutput = new TextArea();
        formulaOutput.setEditable(false);
        formulaOutput.setPrefRowCount(6);
        formulaOutput.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 11px;");

        Button parseFormulaBtn = new Button(I18n.getInstance().get("chemical.btn.formula"));
        parseFormulaBtn.setMaxWidth(Double.MAX_VALUE);
        parseFormulaBtn.setOnAction(e -> {
            try {
                Formula f = ChemicalReactionParser.parseFormula(formulaInput.getText());
                StringBuilder sb = new StringBuilder();
                sb.append("Formula: ").append(f.toString()).append("\n");
                sb.append("Coefficient: ").append(f.getCoefficient()).append("\n");
                sb.append("Elements:\n");
                for (var entry : f.getElements().entrySet()) {
                    sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
                if (f.getState() != null) {
                    sb.append("State: ").append(f.getState()).append("\n");
                }
                if (f.getCharge() != 0) {
                    sb.append("Charge: ").append(f.getCharge()).append("\n");
                }
                formulaOutput.setText(sb.toString());
            } catch (Exception ex) {
                formulaOutput.setText("Error: " + ex.getMessage());
            }
        });

        formulaPanel.getChildren().addAll(formulaTitle, formulaInput, parseFormulaBtn, formulaOutput);
        root.setRight(formulaPanel);
        BorderPane.setMargin(formulaPanel, new Insets(0, 0, 0, 15));

        Scene scene = new Scene(root, 900, 600);
        org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("viewer.chemical"));
        stage.setScene(scene);
        stage.show();
    }

    private Button createExampleButton(String label, String equation) {
        Button btn = new Button(label);
        btn.setStyle("-fx-font-size: 10px;");
        btn.setOnAction(e -> {
            inputArea.setText(equation);
            parseReaction();
        });
        return btn;
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
            sb.append("═══════════════════════════════════════\n");
            sb.append("PARSED REACTION\n");
            sb.append("═══════════════════════════════════════\n\n");
            sb.append("Formatted: ").append(reaction.toString()).append("\n\n");

            sb.append("─── REACTANTS ───\n");
            for (Formula f : reaction.getReactants()) {
                sb.append("  ").append(formatFormula(f)).append("\n");
            }

            sb.append("\n─── PRODUCTS ───\n");
            for (Formula f : reaction.getProducts()) {
                sb.append("  ").append(formatFormula(f)).append("\n");
            }

            sb.append("\n─── ELEMENT BALANCE ───\n");
            sb.append(reaction.getElementBalance());

            boolean balanced = reaction.isBalanced();
            sb.append("\n═══════════════════════════════════════\n");
            sb.append("RESULT: ").append(balanced ? "✓ BALANCED" : "✗ NOT BALANCED").append("\n");
            sb.append("═══════════════════════════════════════\n");

            outputArea.setText(sb.toString());

            if (balanced) {
                statusLabel.setText(I18n.getInstance().get("chemical.status.balanced"));
                statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
            } else {
                statusLabel.setText(I18n.getInstance().get("chemical.status.unbalanced"));
                statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            }

        } catch (Exception e) {
            outputArea.setText("Error parsing equation:\n" + e.getMessage());
            statusLabel.setText(I18n.getInstance().get("chemical.status.error"));
            statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
        }
    }

    private String formatFormula(Formula f) {
        StringBuilder sb = new StringBuilder();
        sb.append(f.toString());
        sb.append(" → Elements: ");
        for (var e : f.getTotalElements().entrySet()) {
            sb.append(e.getKey()).append("=").append(e.getValue()).append(" ");
        }
        return sb.toString();
    }

    public static void show(Stage stage) {
        new ChemicalReactionViewer().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
