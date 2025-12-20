package org.jscience.ui.chemistry;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;
import org.jscience.chemistry.loaders.ChemistryDataLoader;

/**
 * Interactive periodic table viewer using JavaFX.
 * <p>
 * Displays all elements in standard periodic table layout with:
 * <ul>
 * <li>Color-coded element categories</li>
 * <li>Hover tooltips with element details</li>
 * <li>Click for detailed element information</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class PeriodicTableViewer extends Application {

    // Element positions in periodic table [row, col] (1-indexed)
    private static final String[][] LAYOUT = {
            { "H", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                    "He" },
            { "Li", "Be", null, null, null, null, null, null, null, null, null, null, "B", "C", "N", "O", "F", "Ne" },
            { "Na", "Mg", null, null, null, null, null, null, null, null, null, null, "Al", "Si", "P", "S", "Cl",
                    "Ar" },
            { "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br",
                    "Kr" },
            { "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I",
                    "Xe" },
            { "Cs", "Ba", "*", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At",
                    "Rn" },
            { "Fr", "Ra", "**", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds", "Rg", "Cn", "Nh", "Fl", "Mc", "Lv", "Ts",
                    "Og" },
            {},
            { null, null, "*", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb",
                    "Lu" },
            { null, null, "**", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No",
                    "Lr" }
    };

    private VBox detailPanel;

    @Override
    public void start(Stage primaryStage) {
        // Load element data
        ChemistryDataLoader.loadElements();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Main table grid
        GridPane tableGrid = createTableGrid();
        ScrollPane scrollPane = new ScrollPane(tableGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #1a1a2e;");

        // Detail panel
        detailPanel = createDetailPanel();

        root.setCenter(scrollPane);
        root.setRight(detailPanel);

        Scene scene = new Scene(root, 1400, 700);
        primaryStage.setTitle("JScience Periodic Table Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createTableGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(3);
        grid.setVgap(3);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: #1a1a2e;");

        for (int row = 0; row < LAYOUT.length; row++) {
            for (int col = 0; col < LAYOUT[row].length; col++) {
                String symbol = LAYOUT[row][col];
                if (symbol == null || symbol.equals("*") || symbol.equals("**")) {
                    if (symbol != null) {
                        Label marker = new Label(symbol);
                        marker.setTextFill(Color.GRAY);
                        grid.add(marker, col, row);
                    }
                    continue;
                }

                StackPane cell = createElementCell(symbol);
                if (cell != null) {
                    grid.add(cell, col, row);
                }
            }
        }

        return grid;
    }

    private StackPane createElementCell(String symbol) {
        Element element = PeriodicTable.bySymbol(symbol);
        if (element == null) {
            // Create placeholder for missing elements
            StackPane placeholder = new StackPane();
            Label lbl = new Label(symbol);
            lbl.setTextFill(Color.GRAY);
            placeholder.getChildren().add(lbl);
            placeholder.setPrefSize(60, 70);
            placeholder.setStyle("-fx-background-color: #2d2d44; -fx-background-radius: 5;");
            return placeholder;
        }

        VBox content = new VBox(2);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(5));

        Label atomicNum = new Label(String.valueOf(element.getAtomicNumber()));
        atomicNum.setFont(Font.font("Arial", 10));
        atomicNum.setTextFill(Color.WHITE);

        Label symbolLbl = new Label(element.getSymbol());
        symbolLbl.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        symbolLbl.setTextFill(Color.WHITE);

        Label name = new Label(truncate(element.getName(), 8));
        name.setFont(Font.font("Arial", 9));
        name.setTextFill(Color.LIGHTGRAY);

        content.getChildren().addAll(atomicNum, symbolLbl, name);

        StackPane cell = new StackPane(content);
        cell.setPrefSize(60, 70);
        cell.setStyle(getCategoryStyle(element));

        // Tooltip
        Tooltip tooltip = new Tooltip(getTooltipText(element));
        tooltip.setStyle("-fx-font-size: 12px;");
        Tooltip.install(cell, tooltip);

        // Click handler
        cell.setOnMouseClicked(e -> showElementDetails(element));

        // Hover effect
        cell.setOnMouseEntered(e -> cell.setStyle(getCategoryStyle(element) + " -fx-opacity: 0.8;"));
        cell.setOnMouseExited(e -> cell.setStyle(getCategoryStyle(element)));

        return cell;
    }

    private String getCategoryStyle(Element element) {
        String baseStyle = "-fx-background-radius: 5; -fx-cursor: hand;";
        if (element.getCategory() == null) {
            return "-fx-background-color: #4a4a5a;" + baseStyle;
        }
        return switch (element.getCategory()) {
            case ALKALI_METAL -> "-fx-background-color: #ff6b6b;" + baseStyle;
            case ALKALINE_EARTH_METAL -> "-fx-background-color: #ffa94d;" + baseStyle;
            case TRANSITION_METAL -> "-fx-background-color: #74b9ff;" + baseStyle;
            case POST_TRANSITION_METAL -> "-fx-background-color: #81ecec;" + baseStyle;
            case METALLOID -> "-fx-background-color: #a29bfe;" + baseStyle;
            case NONMETAL -> "-fx-background-color: #55efc4;" + baseStyle;
            case HALOGEN -> "-fx-background-color: #fdcb6e;" + baseStyle;
            case NOBLE_GAS -> "-fx-background-color: #fd79a8;" + baseStyle;
            case LANTHANIDE -> "-fx-background-color: #e17055;" + baseStyle;
            case ACTINIDE -> "-fx-background-color: #d63031;" + baseStyle;
            default -> "-fx-background-color: #636e72;" + baseStyle;
        };
    }

    private String getTooltipText(Element element) {
        StringBuilder sb = new StringBuilder();
        sb.append(element.getName()).append(" (").append(element.getSymbol()).append(")\n");
        sb.append("Atomic Number: ").append(element.getAtomicNumber()).append("\n");
        if (element.getAtomicMass() != null) {
            sb.append("Atomic Mass: ").append(String.format("%.4f", element.getAtomicMass().getValue().doubleValue()))
                    .append(" kg\n");
        }
        if (element.getMeltingPoint() != null) {
            sb.append("Melting Point: ").append(element.getMeltingPoint().getValue()).append(" K\n");
        }
        if (element.getBoilingPoint() != null) {
            sb.append("Boiling Point: ").append(element.getBoilingPoint().getValue()).append(" K\n");
        }
        return sb.toString();
    }

    private VBox createDetailPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));
        panel.setPrefWidth(300);
        panel.setStyle("-fx-background-color: #16213e;");

        Label title = new Label("Element Details");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.WHITE);

        Label hint = new Label("Click an element to see details");
        hint.setTextFill(Color.LIGHTGRAY);
        hint.setWrapText(true);

        panel.getChildren().addAll(title, hint);
        return panel;
    }

    private void showElementDetails(Element element) {
        detailPanel.getChildren().clear();

        Label title = new Label(element.getName());
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        Label symbol = new Label(element.getSymbol());
        symbol.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        symbol.setTextFill(Color.LIGHTBLUE);

        VBox props = new VBox(5);
        props.getChildren().add(createPropLabel("Atomic Number", String.valueOf(element.getAtomicNumber())));
        props.getChildren().add(createPropLabel("Group", String.valueOf(element.getGroup())));
        props.getChildren().add(createPropLabel("Period", String.valueOf(element.getPeriod())));

        if (element.getCategory() != null) {
            props.getChildren().add(createPropLabel("Category", element.getCategory().name()));
        }
        if (element.getAtomicMass() != null) {
            props.getChildren().add(createPropLabel("Atomic Mass",
                    String.format("%.6e kg", element.getAtomicMass().getValue().doubleValue())));
        }
        if (element.getElectronegativity() > 0) {
            props.getChildren()
                    .add(createPropLabel("Electronegativity", String.format("%.2f", element.getElectronegativity())));
        }
        if (element.getMeltingPoint() != null) {
            props.getChildren()
                    .add(createPropLabel("Melting Point", element.getMeltingPoint().getValue() + " K"));
        }
        if (element.getBoilingPoint() != null) {
            props.getChildren()
                    .add(createPropLabel("Boiling Point", element.getBoilingPoint().getValue() + " K"));
        }
        if (element.getDensity() != null) {
            props.getChildren()
                    .add(createPropLabel("Density", element.getDensity().getValue() + " g/cm³"));
        }

        detailPanel.getChildren().addAll(title, symbol, props);
    }

    private HBox createPropLabel(String name, String value) {
        HBox row = new HBox(10);
        Label nameLbl = new Label(name + ":");
        nameLbl.setTextFill(Color.LIGHTGRAY);
        nameLbl.setMinWidth(120);
        Label valueLbl = new Label(value);
        valueLbl.setTextFill(Color.WHITE);
        row.getChildren().addAll(nameLbl, valueLbl);
        return row;
    }

    private String truncate(String s, int max) {
        return s.length() > max ? s.substring(0, max) : s;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
