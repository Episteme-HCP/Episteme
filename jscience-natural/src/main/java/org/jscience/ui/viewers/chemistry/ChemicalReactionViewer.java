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
import org.jscience.ui.Parameter;
import org.jscience.ui.StringParameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Chemical Reaction Viewer and Parser.
 * Refactored to be parameter-based.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ChemicalReactionViewer extends AbstractViewer {

    private String equation = "2H2 + O2 -> 2H2O";
    private TextArea outputArea;
    private Label statusLabel;
    
    private List<Parameter<?>> parameters = new ArrayList<>();

    @Override
    public String getCategory() { return I18n.getInstance().get("category.chemistry", "Chemistry"); }
    
    @Override
    public String getName() { return I18n.getInstance().get("viewer.chemicalreactionviewer.name", "Chemical Reaction Parser"); }

    public ChemicalReactionViewer() {
        setupParameters();
        initUI();
        parseReaction();
    }

    private void setupParameters() {
        parameters.add(new StringParameter(I18n.getInstance().get("chemical.label.eqn", "Equation"), 
            I18n.getInstance().get("chemical.prompt.eqn", "Enter chemical equation"), 
            equation, true, v -> {
                equation = v;
                parseReaction();
            }));
    }

    private void initUI() {
        this.getStyleClass().add("viewer-root");
        this.setPadding(new Insets(15));

        // Output area
        VBox outputBox = new VBox(5);
        outputBox.setPadding(new Insets(10));
        outputBox.getStyleClass().add("viewer-sidebar");

        Label outputLabel = new Label(I18n.getInstance().get("chemical.label.results", "Results"));
        outputLabel.getStyleClass().add("description-label");

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefRowCount(15);
        outputArea.setStyle("-fx-font-family: 'Consolas', monospace;");

        statusLabel = new Label("");
        statusLabel.getStyleClass().add("font-bold");

        outputBox.getChildren().addAll(outputLabel, statusLabel, outputArea);

        this.setCenter(outputBox);

        // Quick Formula Parser on right
        VBox formulaPanel = new VBox(10);
        formulaPanel.setPadding(new Insets(10));
        formulaPanel.getStyleClass().add("viewer-sidebar");
        formulaPanel.setPrefWidth(250);

        Label formulaTitle = new Label(I18n.getInstance().get("chemical.label.quick", "Quick Formula"));
        formulaTitle.getStyleClass().add("header-label");
        formulaTitle.setStyle("-fx-font-size: 14px;");

        TextField formulaInput = new TextField();
        formulaInput.setPromptText(I18n.getInstance().get("chemical.prompt.formula", "e.g., Ca(OH)2"));

        TextArea formulaOutput = new TextArea();
        formulaOutput.setEditable(false);
        formulaOutput.setPrefRowCount(6);
        formulaOutput.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 11px;");
        formulaOutput.getStyleClass().add("description-label");

        Button parseFormulaBtn = new Button(I18n.getInstance().get("chemical.btn.formula", "Parse Formula"));
        parseFormulaBtn.setMaxWidth(Double.MAX_VALUE);
        parseFormulaBtn.setOnAction(e -> {
            try {
                Formula f = ChemicalReactionParser.parseFormula(formulaInput.getText());
                StringBuilder sb = new StringBuilder();
                sb.append(I18n.getInstance().get("chemical.label.formula", "Formula: {0}", f.toString())).append("\n");
                sb.append(I18n.getInstance().get("chemical.label.coefficient", "Coefficient: {0}", (long) f.getCoefficient())).append("\n");
                sb.append(I18n.getInstance().get("chemical.label.elements", "Elements:")).append("\n");
                for (var entry : f.getElements().entrySet()) sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                if (f.getState() != null) sb.append(I18n.getInstance().get("chemical.label.state", "State: {0}", f.getState())).append("\n");
                if (f.getCharge() != 0) sb.append(I18n.getInstance().get("chemical.label.charge", "Charge: {0}", (long) f.getCharge())).append("\n");
                formulaOutput.setText(sb.toString());
            } catch (Exception ex) {
                formulaOutput.setText(I18n.getInstance().get("chemical.status.error", "Error: {0}", ex.getMessage()));
            }
        });

        formulaPanel.getChildren().addAll(formulaTitle, formulaInput, parseFormulaBtn, formulaOutput);
        this.setRight(formulaPanel);
    }

    private void parseReaction() {
        if (outputArea == null) return;
        String input = equation.trim();
        if (input.isEmpty()) {
            outputArea.setText(I18n.getInstance().get("chemical.msg.enter", "Please enter a reaction equation."));
            return;
        }

        try {
            Reaction reaction = ChemicalReactionParser.parse(input);

            StringBuilder sb = new StringBuilder();
            sb.append("══════════════════════════════════════\n");
            sb.append(I18n.getInstance().get("chemical.header.parsed", "PARSED REACTION")).append("\n");
            sb.append("══════════════════════════════════════\n\n");
            sb.append(I18n.getInstance().get("chemical.label.formated", "Formatted: {0}", reaction.toString())).append("\n\n");

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
                statusLabel.setText(I18n.getInstance().get("chemical.status.balanced", "Balanced"));
            } else {
                statusLabel.setText(I18n.getInstance().get("chemical.status.unbalanced", "Unbalanced"));
            }

        } catch (Exception e) {
            outputArea.setText(I18n.getInstance().get("chemical.error.parse", "Error parsing equation:\n{0}", e.getMessage()));
            statusLabel.setText(I18n.getInstance().get("chemical.status.error", "Error"));
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

    @Override public String getDescription() { return I18n.getInstance().get("viewer.chemicalreactionviewer.desc", "Parse and analyze chemical equations."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.chemicalreactionviewer.longdesc", "Advanced chemical equation analyzer."); }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
