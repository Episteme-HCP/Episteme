/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.mathematics.units;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.measure.Unit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jscience.measure.Units.*;

/**
 * Scientific Unit Converter leveraging the JScience JSR-275 implementation.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class UnitConverterViewer extends Application {

    private ComboBox<String> categoryBox;
    private ComboBox<Unit<?>> fromUnitBox, toUnitBox;
    private TextField inputField, outputField;

    private Map<String, List<?>> unitsByCategory = new HashMap<>();

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f4f4;");

        setupUnits();

        Label title = new Label("Universal Measure Converter");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Category
        HBox catRow = new HBox(10);
        categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(unitsByCategory.keySet());
        categoryBox.setValue("Length");
        categoryBox.setOnAction(e -> updateUnitLists());
        catRow.getChildren().addAll(new Label("Category:"), categoryBox);

        // Conversion grid
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);

        inputField = new TextField("1.0");
        outputField = new TextField();
        outputField.setEditable(false);
        outputField.setStyle("-fx-background-color: #e8f5e9; -fx-font-weight: bold;");

        fromUnitBox = new ComboBox<>();
        toUnitBox = new ComboBox<>();

        grid.add(new Label("From:"), 0, 0);
        grid.add(inputField, 1, 0);
        grid.add(fromUnitBox, 2, 0);

        grid.add(new Label("To:"), 0, 1);
        grid.add(outputField, 1, 1);
        grid.add(toUnitBox, 2, 1);

        updateUnitLists();

        Button convertBtn = new Button("Convert");
        convertBtn.setMaxWidth(Double.MAX_VALUE);
        convertBtn.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-padding: 10;");
        convertBtn.setOnAction(e -> doConvert());

        inputField.setOnAction(e -> doConvert());
        fromUnitBox.setOnAction(e -> doConvert());
        toUnitBox.setOnAction(e -> doConvert());

        root.getChildren().addAll(title, catRow, grid, convertBtn);

        Scene scene = new Scene(root, 600, 350);
        stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("viewer.converter"));
        stage.setScene(scene);
        stage.show();
    }

    private void setupUnits() {
        unitsByCategory.put("Length",
                Arrays.asList(METER, CENTIMETER, MILLIMETER, KILOMETER, INCH, FOOT, MILE, NAUTICAL_MILE, LIGHT_YEAR));
        unitsByCategory.put("Mass", Arrays.asList(KILOGRAM, GRAM, MILLIGRAM, TONNE, POUND, OUNCE));
        unitsByCategory.put("Time", Arrays.asList(SECOND, MINUTE, HOUR, DAY, WEEK, YEAR));
        unitsByCategory.put("Temperature", Arrays.asList(KELVIN, CELSIUS, FAHRENHEIT));
        unitsByCategory.put("Velocity", Arrays.asList(METER_PER_SECOND, KILOMETER_PER_HOUR, MILE_PER_HOUR, KNOT, MACH));
        unitsByCategory.put("Energy", Arrays.asList(JOULE, KILOJOULE, CALORIE, KILOCALORIE, WATT_HOUR, KILOWATT_HOUR));
        unitsByCategory.put("Pressure", Arrays.asList(PASCAL, BAR, ATMOSPHERE, MILLIMETRE_OF_MERCURY));
    }

    @SuppressWarnings("unchecked")
    private void updateUnitLists() {
        List<Unit<?>> units = (List<Unit<?>>) unitsByCategory.get(categoryBox.getValue());
        fromUnitBox.getItems().setAll(units);
        toUnitBox.getItems().setAll(units);
        fromUnitBox.setValue(units.get(0));
        toUnitBox.setValue(units.get(1));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void doConvert() {
        try {
            double val = Double.parseDouble(inputField.getText());
            Unit<?> from = fromUnitBox.getValue();
            Unit<?> to = toUnitBox.getValue();

            double result = ((Unit) from).getConverterTo((Unit) to).convert(val);
            outputField.setText(String.format("%.6g", result));
        } catch (Exception e) {
            outputField.setText("Error");
        }
    }

    public static void show(Stage stage) {
        new UnitConverterViewer().start(stage);
    }
}
