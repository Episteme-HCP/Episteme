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

package org.jscience.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;



import java.util.List;
import java.util.ArrayList;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Unit;
import org.jscience.measure.Quantities;
import org.jscience.ui.i18n.I18n;

/**
 * Abstract base class for all JScience Demonstrations.
 * Provides a consistent layout with a viewer area and a control panel.
 * 
 * Subclasses MUST override: getCategory(), getName(), getDescription(), getLongDescription()
 * with proper I18n support.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class AbstractDemo extends Application implements App {

    @Override
    public boolean isDemo() {
        return true;
    }

    protected Node viewer;
    private VBox controlPanel;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Header
        VBox header = createHeader();
        root.setTop(header);

        // Center: The Viewer Node
        Node viewerNode = createViewerNode();
        if (viewerNode instanceof Viewer) {
            this.viewer = viewerNode;
        }

        StackPane viewerContainer = new StackPane(viewerNode);
        viewerContainer.setStyle("-fx-border-color: #333333; -fx-border-width: 1; -fx-background-color: black;");
        root.setCenter(viewerContainer);

        // Right: Controls
        controlPanel = createControlPanel();
        ScrollPane scrollControls = new ScrollPane(controlPanel);
        scrollControls.setFitToWidth(true);
        scrollControls.setPrefWidth(280);
        root.setRight(scrollControls);

        Scene scene = new Scene(root, 1200, 800);
        ThemeManager.getInstance().applyTheme(scene);

        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }

    private VBox createHeader() {
        VBox header = new VBox(5);
        header.setPadding(new Insets(0, 0, 15, 0));

        Label title = new Label(getName());
        title.setFont(Font.font("System", FontWeight.BOLD, 24));

        Label longDesc = new Label(getLongDescription());
        longDesc.setWrapText(true);
        longDesc.setStyle("-fx-font-size: 14px; -fx-text-fill: #aaaaaa;");

        header.getChildren().addAll(title, longDesc);
        return header;
    }

    protected VBox createControlPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(10));
        panel.setAlignment(Pos.TOP_LEFT);

        Label sectionTitle = new Label(I18n.getInstance().get("demo.controls.title"));
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        panel.getChildren().add(sectionTitle);

        if (viewer instanceof Viewer v) {
            for (Parameter<?> param : v.getViewerParameters()) {
                panel.getChildren().add(createParameterControl(param));
            }
        }

        return panel;
    }

    private Node createParameterControl(Parameter<?> param) {
        VBox box = new VBox(3);
        Label label = new Label(param.getName());
        label.setFont(Font.font("System", FontWeight.NORMAL, 12));

        if (param instanceof NumericParameter numParam) {
            Slider slider = new Slider(numParam.getMin(), numParam.getMax(), numParam.getValue());
            slider.setBlockIncrement(numParam.getStep());
            slider.setShowTickLabels(true);

            Label valLabel = new Label(String.format("%.2f", numParam.getValue()));
            slider.valueProperty().addListener((obs, oldVal, newVal) -> {
                double v = newVal.doubleValue();
                if (numParam.getStep() > 0) {
                    v = Math.round(v / numParam.getStep()) * numParam.getStep();
                }
                numParam.setValue(v);
                valLabel.setText(String.format("%.2f", v));
            });

            HBox valBox = new HBox(label, new Region(), valLabel);
            HBox.setHgrow(valBox.getChildren().get(1), Priority.ALWAYS);
            box.getChildren().addAll(valBox, slider);
        } else if (param instanceof RealParameter realParam) {
            double minV = realParam.getMin().doubleValue();
            double maxV = realParam.getMax().doubleValue();
            double curV = realParam.getValue().doubleValue();
            double stepV = realParam.getStep().doubleValue();

            Slider slider = new Slider(minV, maxV, curV);
            slider.setBlockIncrement(stepV);
            slider.setShowTickLabels(true);

            Label valLabel = new Label(String.format("%.4f", curV));
            slider.valueProperty().addListener((obs, oldVal, newVal) -> {
                double v = newVal.doubleValue();
                if (stepV > 0) {
                    v = Math.round(v / stepV) * stepV;
                }
                realParam.setValue(Real.of(v));
                valLabel.setText(String.format("%.4f", v));
            });

            HBox valBox = new HBox(label, new Region(), valLabel);
            HBox.setHgrow(valBox.getChildren().get(1), Priority.ALWAYS);
            box.getChildren().addAll(valBox, slider);
        } else if (param instanceof QuantityParameter<?> qParam) { // Wildcard capture for type safety inside
             createQuantityControl(box, label, qParam);
        } else if (param instanceof BooleanParameter) {
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected((Boolean) param.getValue());
            checkBox.setOnAction(e -> {
                ((BooleanParameter) param).setValue(checkBox.isSelected());
            });
            box.getChildren().addAll(label, checkBox);
        } else if (param.getValue() instanceof Color) {
            @SuppressWarnings("unchecked")
            Parameter<Color> colorParam = (Parameter<Color>) param;
            ColorPicker picker = new ColorPicker(colorParam.getValue());
            picker.setMaxWidth(Double.MAX_VALUE);
            picker.setOnAction(e -> colorParam.setValue(picker.getValue()));
            box.getChildren().addAll(label, picker);
        } else {
            // Default generic control
            Label valLabel = new Label(String.valueOf(param.getValue()));
            box.getChildren().addAll(label, valLabel);
        }

        Tooltip tooltip = new Tooltip(param.getDescription());
        Tooltip.install(box, tooltip);

        return box;
    }

    // === MANDATORY ABSTRACT METHODS - MUST BE OVERRIDDEN WITH I18N ===

    @Override
    public abstract String getCategory();

    @Override
    public abstract String getName();

    @Override
    public abstract String getDescription();

    @Override
    public abstract String getLongDescription();

    protected abstract Node createViewerNode();

    @Override
    public List<Parameter<?>> getViewerParameters() {
        if (viewer instanceof Viewer) {
            Viewer v = (Viewer) viewer;
            return v.getViewerParameters();
        }
        return new ArrayList<>();
    }

    private <Q extends Quantity<Q>> void createQuantityControl(VBox box, Label label, QuantityParameter<Q> qParam) {
        Unit<Q> unit = qParam.getUnit();
        double minV = qParam.getMin().to(unit).getValue().doubleValue();
        double maxV = qParam.getMax().to(unit).getValue().doubleValue();
        double curV = qParam.getValue().to(unit).getValue().doubleValue();
        double stepV = qParam.getStep().to(unit).getValue().doubleValue();

        Slider slider = new Slider(minV, maxV, curV);
        slider.setBlockIncrement(stepV);
        slider.setShowTickLabels(true);

        Label valLabel = new Label(String.format("%.2f %s", curV, unit.toString()));
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double v = newVal.doubleValue();
            if (stepV > 0) {
              v = Math.round(v / stepV) * stepV;
            }
            // Create new quantity
            Quantity<Q> newQ = Quantities.create(v, unit);
            qParam.setValue(newQ);
            valLabel.setText(String.format("%.2f %s", v, unit.toString()));
        });

        HBox valBox = new HBox(label, new Region(), valLabel);
        HBox.setHgrow(valBox.getChildren().get(1), Priority.ALWAYS);
        box.getChildren().addAll(valBox, slider);
    }

    @Override
    public void show(Stage stage) {
        start(stage);
    }
}
