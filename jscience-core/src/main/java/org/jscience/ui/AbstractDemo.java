/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
import org.jscience.ui.i18n.I18n;

/**
 * Abstract base class for all JScience Demonstrations.
 * Provides a consistent layout with a viewer area and a control panel.
 */
public abstract class AbstractDemo extends Application implements DemoProvider {

    protected ScientificViewer viewer;
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
        if (viewerNode instanceof ScientificViewer sv) {
            this.viewer = sv;
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

        Label sectionTitle = new Label(I18n.getInstance().get("demo.controls.title", "Parameters"));
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        panel.getChildren().add(sectionTitle);

        if (viewer != null) {
            for (Parameter<?> param : viewer.getParameters()) {
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

    protected abstract Node createViewerNode();

    protected abstract String getLongDescription();

    @Override
    public void show(Stage stage) {
        start(stage);
    }
}
