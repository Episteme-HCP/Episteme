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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;
import org.jscience.chemistry.loaders.PeriodicTableReader;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Parameter;
import org.jscience.ui.NumericParameter;
import org.jscience.ui.StringParameter;
import org.jscience.ui.BooleanParameter;
import org.jscience.ui.i18n.I18n;
import org.jscience.io.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactive periodic table viewer.
 * Refactored to be 100% parameter-based.
 */
public class PeriodicTableViewer extends AbstractViewer {

    private static final String[][] LAYOUT = {
            { "H", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "He" },
            { "Li", "Be", null, null, null, null, null, null, null, null, null, null, "B", "C", "N", "O", "F", "Ne" },
            { "Na", "Mg", null, null, null, null, null, null, null, null, null, null, "Al", "Si", "P", "S", "Cl", "Ar" },
            { "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr" },
            { "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe" },
            { "Cs", "Ba", "*", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn" },
            { "Fr", "Ra", "**", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds", "Rg", "Cn", "Nh", "Fl", "Mc", "Lv", "Ts", "Og" },
            {},
            { null, null, "*", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu" },
            { null, null, "**", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr" }
    };

    private VBox detailPanel;
    private Group contentGroup;
    private double zoom = 0.9;
    private String searchString = "";
    private final List<Parameter<?>> parameters = new ArrayList<>();

    public PeriodicTableViewer() {
        setupParameters();
        List<Element> elements = PeriodicTableReader.loadFromResource("/org/jscience/chemistry/elements.json");
        for (Element el : elements) PeriodicTable.registerElement(el);

        GridPane tableGrid = createTableGrid();
        contentGroup = new Group(tableGrid); contentGroup.setScaleX(zoom); contentGroup.setScaleY(zoom);
        
        StackPane zoomPane = new StackPane(contentGroup); zoomPane.setAlignment(Pos.CENTER);
        zoomPane.getStyleClass().add("viewer-root");

        ScrollPane scrollPane = new ScrollPane(zoomPane); scrollPane.setFitToWidth(true); scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("viewer-root");

        detailPanel = new VBox(10); detailPanel.setPadding(new Insets(10)); detailPanel.setPrefWidth(300);
        detailPanel.getStyleClass().add("viewer-sidebar");
        
        detailPanel.getChildren().add(new Label(I18n.getInstance().get("viewer.periodictable.details.hint", "Select an element to view details")));

        setCenter(scrollPane); setRight(detailPanel);
    }

    private void setupParameters() {
        parameters.add(new NumericParameter("periodic.zoom", I18n.getInstance().get("viewer.periodictable.zoom", "Zoom"), 0.5, 2.0, 0.1, zoom, v -> {
            zoom = v; if (contentGroup != null) { contentGroup.setScaleX(zoom); contentGroup.setScaleY(zoom); }
        }));
        
        parameters.add(new StringParameter("periodic.search", I18n.getInstance().get("viewer.periodictable.search.prompt", "Compound Search"), searchString, v -> searchString = v));
        parameters.add(new BooleanParameter("periodic.dosearch", I18n.getInstance().get("viewer.periodictable.search.button", "Search"), false, v -> {
            if (v) performSearch();
        }));
    }

    private void performSearch() {
        // ... (PubChem search logic would go here, maybe showing results in the sidebar list)
    }

    private GridPane createTableGrid() {
        GridPane grid = new GridPane(); grid.setHgap(3); grid.setVgap(3); grid.setPadding(new Insets(20));
        for (int r = 0; r < LAYOUT.length; r++) {
            for (int c = 0; c < LAYOUT[r].length; c++) {
                String sym = LAYOUT[r][c];
                if (sym == null || sym.equals("*") || sym.equals("**")) continue;
                StackPane cell = createElementCell(sym);
                if (cell != null) grid.add(cell, c, r);
            }
        }
        return grid;
    }

    private StackPane createElementCell(String symbol) {
        Element el = PeriodicTable.bySymbol(symbol);
        if (el == null) { el = new Element("Unknown", symbol); el.setAtomicNumber(0); }
        Element finalEl = el;
        VBox content = new VBox(2); content.setAlignment(Pos.CENTER);
        Label symLbl = new Label(finalEl.getSymbol()); symLbl.getStyleClass().add("font-large");
        content.getChildren().addAll(new Label(String.valueOf(finalEl.getAtomicNumber())), symLbl);
        StackPane cell = new StackPane(content); cell.setPrefSize(60, 70);
        String style = getCategoryStyle(finalEl); cell.setStyle(style);
        cell.setOnMouseClicked(e -> showElementDetails(finalEl));
        return cell;
    }

    private void showElementDetails(Element element) {
        detailPanel.getChildren().clear();
        Label title = new Label(element.getName() + " (" + element.getSymbol() + ")");
        title.getStyleClass().add("font-large");
        detailPanel.getChildren().addAll(title, new Separator(), new Label("Atomic Number: " + element.getAtomicNumber()));
        if (element.getAtomicMass() != null) detailPanel.getChildren().add(new Label("Atomic Mass: " + element.getAtomicMass()));
    }

    private String getCategoryStyle(Element e) {
        String base = "-fx-background-radius: 5; -fx-cursor: hand;";
        if (e.getCategory() == null) return "-fx-background-color: #576574;" + base;
        return switch (e.getCategory()) {
            case ALKALI_METAL -> "-fx-background-color: #ff6b6b;" + base;
            case NOBLE_GAS -> "-fx-background-color: #c8d6e5;" + base;
            default -> "-fx-background-color: #8395a7;" + base;
        };
    }

    @Override public String getName() { return I18n.getInstance().get("viewer.periodictableviewer.name", "Periodic Table"); }
    @Override public String getCategory() { return I18n.getInstance().get("category.chemistry", "Chemistry"); }
    @Override public String getDescription() { return I18n.getInstance().get("viewer.periodictableviewer.desc", "Interactive periodic table."); }
    @Override public String getLongDescription() { return I18n.getInstance().get("viewer.periodictableviewer.longdesc", "Detailed interactive periodic table of elements. Explore properties like atomic number, symbol, name, and atomic mass. This viewer provides a comprehensive overview of chemical elements organized by their properties and trends."); }
    @Override public List<Parameter<?>> getViewerParameters() { return parameters; }
}
