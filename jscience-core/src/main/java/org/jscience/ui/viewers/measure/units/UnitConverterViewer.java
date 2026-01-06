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

package org.jscience.ui.viewers.measure.units;

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

import org.jscience.measure.Unit;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;

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
 * @since 1.0
 */
public class UnitConverterViewer extends Application {

    private ComboBox<String> categoryBox;
    private ComboBox<Unit<?>> fromUnitBox, toUnitBox;
    private TextField inputField, outputField;

    private Map<String, List<?>> unitsByCategory = new HashMap<>();

    @Override
    public void start(Stage stage) {
        try {
            VBox root = new VBox(20);
            root.setPadding(new Insets(20));

            setupUnits();

            Label title = new Label(
                    org.jscience.ui.i18n.I18n.getInstance().get("converter.title", "Universal Measure Converter"));
            title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

            // Category
            HBox catRow = new HBox(10);
            categoryBox = new ComboBox<>();
            categoryBox.getItems().addAll(unitsByCategory.keySet());
            categoryBox.setValue("Length");
            categoryBox.setConverter(new javafx.util.StringConverter<String>() {
                @Override
                public String toString(String object) {
                    return org.jscience.ui.i18n.I18n.getInstance().get("converter.cat." + object, object);
                }

                @Override
                public String fromString(String string) {
                    return string; // Not editable
                }
            });
            categoryBox.setOnAction(e -> updateUnitLists());
            catRow.getChildren().addAll(
                    new Label(org.jscience.ui.i18n.I18n.getInstance().get("converter.category", "Category:")),
                    categoryBox);

            // Conversion grid
            GridPane grid = new GridPane();
            grid.setHgap(20);
            grid.setVgap(15);

            inputField = new TextField("1.0");
            outputField = new TextField();
            outputField.setEditable(false);
            outputField.setStyle("-fx-font-weight: bold;");

            fromUnitBox = new ComboBox<>();
            toUnitBox = new ComboBox<>();

            grid.add(new Label(org.jscience.ui.i18n.I18n.getInstance().get("converter.from", "From:")), 0, 0);
            grid.add(inputField, 1, 0);
            grid.add(fromUnitBox, 2, 0);

            grid.add(new Label(org.jscience.ui.i18n.I18n.getInstance().get("converter.to", "To:")), 0, 1);
            grid.add(outputField, 1, 1);
            grid.add(toUnitBox, 2, 1);

            updateUnitLists();

            Button convertBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("converter.convert", "Convert"));
            convertBtn.setMaxWidth(Double.MAX_VALUE);
            convertBtn.setOnAction(e -> doConvert());

            inputField.setOnAction(e -> doConvert());
            fromUnitBox.setOnAction(e -> doConvert());
            toUnitBox.setOnAction(e -> doConvert());

            root.getChildren().addAll(title, catRow, grid, convertBtn);

            Scene scene = new Scene(root, 600, 350);
            stage.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("viewer.converter"));
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            VBox errRoot = new VBox(20);
            errRoot.setPadding(new Insets(20));
            errRoot.getChildren().add(new Label(String.format(org.jscience.ui.i18n.I18n.getInstance()
                    .get("converter.error.launch", "Error launching Unit Converter:\n%s"), e.getMessage())));
            Scene errScene = new Scene(errRoot, 400, 200);
            stage.setScene(errScene);
            stage.show();
            e.printStackTrace();
        }
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
            Unit from = fromUnitBox.getValue();
            Unit to = toUnitBox.getValue();

            Quantity q = Quantities.create(val, from);
            Quantity result = q.to(to);

            outputField.setText(String.format("%.6g", result.getValue().doubleValue()));
        } catch (Exception e) {
            outputField.setText(org.jscience.ui.i18n.I18n.getInstance().get("converter.error", "Error"));
        }
    }

    public static void show(Stage stage) {
        new UnitConverterViewer().start(stage);
    }
}
