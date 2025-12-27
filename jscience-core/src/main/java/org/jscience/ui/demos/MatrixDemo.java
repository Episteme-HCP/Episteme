/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.MatrixFactory;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.mathematics.MatrixViewer;
import org.jscience.ui.ThemeManager;

/**
 * Enhanced Matrix Viewer with multiple display modes.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MatrixDemo implements DemoProvider {

    private Matrix<Real> matrix;
    private Canvas heatmapCanvas;
    private MatrixViewer<Real> tableViewer;
    private StackPane viewContainer;
    private Spinner<Integer> rowsSpinner, colsSpinner;
    private Label infoLabel;

    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.mathematics");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("matrix.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("matrix.desc");
    }

    @Override
    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Header
        Label header = new Label(I18n.getInstance().get("matrix.label.header"));
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #00d9ff;");
        HBox headerBox = new HBox(header);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #16213e;");
        root.setTop(headerBox);

        // Control Panel (Left)
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(15));
        controls.setPrefWidth(220);
        controls.setStyle("-fx-background-color: #16213e;");

        Label sizeLabel = new Label(I18n.getInstance().get("matrix.label.size"));
        sizeLabel.setStyle("-fx-text-fill: #aaa; -fx-font-weight: bold;");

        HBox rowBox = new HBox(10);
        rowBox.setAlignment(Pos.CENTER_LEFT);
        Label rowLabel = new Label(I18n.getInstance().get("matrix.label.rows") + ":");
        rowLabel.setStyle("-fx-text-fill: #888;");
        rowsSpinner = new Spinner<>(2, 20, 8);
        rowsSpinner.setPrefWidth(80);
        rowBox.getChildren().addAll(rowLabel, rowsSpinner);

        HBox colBox = new HBox(10);
        colBox.setAlignment(Pos.CENTER_LEFT);
        Label colLabel = new Label(I18n.getInstance().get("matrix.label.cols") + ":");
        colLabel.setStyle("-fx-text-fill: #888;");
        colsSpinner = new Spinner<>(2, 20, 8);
        colsSpinner.setPrefWidth(80);
        colBox.getChildren().addAll(colLabel, colsSpinner);

        Button generateBtn = new Button(I18n.getInstance().get("matrix.button.random"));
        generateBtn.setMaxWidth(Double.MAX_VALUE);
        generateBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        generateBtn.setOnAction(e -> generateMatrix());

        Button identityBtn = new Button(I18n.getInstance().get("matrix.button.identity"));
        identityBtn.setMaxWidth(Double.MAX_VALUE);
        identityBtn.setOnAction(e -> generateIdentity());

        Separator sep1 = new Separator();

        Label viewLabel = new Label(I18n.getInstance().get("matrix.view.label"));
        viewLabel.setStyle("-fx-text-fill: #aaa; -fx-font-weight: bold;");

        ToggleGroup viewGroup = new ToggleGroup();
        RadioButton tableRadio = new RadioButton(I18n.getInstance().get("matrix.view.table"));
        tableRadio.setStyle("-fx-text-fill: #888;");
        tableRadio.setToggleGroup(viewGroup);
        tableRadio.setSelected(true);
        tableRadio.setOnAction(e -> showTableView());

        RadioButton heatmapRadio = new RadioButton(I18n.getInstance().get("matrix.view.heatmap"));
        heatmapRadio.setStyle("-fx-text-fill: #888;");
        heatmapRadio.setToggleGroup(viewGroup);
        heatmapRadio.setOnAction(e -> showHeatmapView());

        Separator sep2 = new Separator();

        Label opsLabel = new Label(I18n.getInstance().get("matrix.ops.label"));
        opsLabel.setStyle("-fx-text-fill: #aaa; -fx-font-weight: bold;");

        Button transposeBtn = new Button(I18n.getInstance().get("matrix.ops.transpose"));
        transposeBtn.setMaxWidth(Double.MAX_VALUE);
        transposeBtn.setOnAction(e -> transpose());

        Button scaleBtn = new Button(I18n.getInstance().get("matrix.ops.scale"));
        scaleBtn.setMaxWidth(Double.MAX_VALUE);
        scaleBtn.setOnAction(e -> scale(2.0));

        infoLabel = new Label("8x8 " + I18n.getInstance().get("matrix.info.matrix"));
        infoLabel.setStyle("-fx-text-fill: #00d9ff; -fx-font-size: 12px;");

        controls.getChildren().addAll(
                sizeLabel, rowBox, colBox, generateBtn, identityBtn,
                sep1, viewLabel, tableRadio, heatmapRadio,
                sep2, opsLabel, transposeBtn, scaleBtn,
                new Separator(), infoLabel);
        root.setLeft(controls);

        // View Container (Center)
        viewContainer = new StackPane();
        viewContainer.setStyle("-fx-background-color: #0f3460;");
        viewContainer.setPadding(new Insets(20));
        root.setCenter(viewContainer);

        // Initialize with random matrix
        generateMatrix();

        Scene scene = new Scene(root, 900, 650);
        stage.setTitle(I18n.getInstance().get("matrix.title"));
        stage.setScene(scene);
        ThemeManager.getInstance().applyTheme(scene);
        stage.show();
    }

    private void generateMatrix() {
        int rows = rowsSpinner.getValue();
        int cols = colsSpinner.getValue();
        Real[][] data = new Real[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = Real.of(Math.random() * 100);
            }
        }
        matrix = MatrixFactory.create(data, Real.ZERO);
        updateViews();
        infoLabel.setText(rows + "\u00D7" + cols + " " + I18n.getInstance().get("matrix.info.matrix"));
    }

    private void generateIdentity() {
        int size = Math.min(rowsSpinner.getValue(), colsSpinner.getValue());
        Real[][] data = new Real[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[i][j] = (i == j) ? Real.ONE : Real.ZERO;
            }
        }
        matrix = MatrixFactory.create(data, Real.ZERO);
        rowsSpinner.getValueFactory().setValue(size);
        colsSpinner.getValueFactory().setValue(size);
        updateViews();
        infoLabel.setText(size + "\u00D7" + size + " " + I18n.getInstance().get("matrix.info.identity"));
    }

    private void transpose() {
        if (matrix != null) {
            matrix = matrix.transpose();
            // Swap spinner values
            int r = rowsSpinner.getValue();
            int c = colsSpinner.getValue();
            rowsSpinner.getValueFactory().setValue(c);
            colsSpinner.getValueFactory().setValue(r);
            updateViews();
            infoLabel.setText(matrix.rows() + "\u00D7" + matrix.cols() + " ("
                    + I18n.getInstance().get("matrix.info.transposed") + ")");
        }
    }

    private void scale(double factor) {
        if (matrix != null) {
            int rows = matrix.rows();
            int cols = matrix.cols();
            Real[][] data = new Real[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    data[i][j] = matrix.get(i, j).multiply(Real.of(factor));
                }
            }
            matrix = MatrixFactory.create(data, Real.ZERO);
            updateViews();
            infoLabel.setText(matrix.rows() + "\u00D7" + matrix.cols() + " ("
                    + I18n.getInstance().get("matrix.info.scaled") + " \u00D7" + factor + ")");
        }
    }

    private void updateViews() {
        tableViewer = new MatrixViewer<>(matrix);
        createHeatmap();
        showTableView();
    }

    private void showTableView() {
        viewContainer.getChildren().clear();
        if (tableViewer != null) {
            viewContainer.getChildren().add(tableViewer);
        }
    }

    private void showHeatmapView() {
        viewContainer.getChildren().clear();
        if (heatmapCanvas != null) {
            viewContainer.getChildren().add(heatmapCanvas);
        }
    }

    private void createHeatmap() {
        if (matrix == null)
            return;

        int rows = matrix.rows();
        int cols = matrix.cols();
        int cellSize = Math.min(50, Math.max(20, 500 / Math.max(rows, cols)));

        heatmapCanvas = new Canvas(cols * cellSize + 2, rows * cellSize + 2);
        GraphicsContext gc = heatmapCanvas.getGraphicsContext2D();

        // Find min/max for normalization
        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double val = matrix.get(i, j).doubleValue();
                min = Math.min(min, val);
                max = Math.max(max, val);
            }
        }
        double range = max - min;
        if (range == 0)
            range = 1;

        // Draw cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double val = matrix.get(i, j).doubleValue();
                double norm = (val - min) / range;

                // Blue to Red gradient
                Color color = Color.hsb(240 - norm * 240, 0.8, 0.9);
                gc.setFill(color);
                gc.fillRect(j * cellSize + 1, i * cellSize + 1, cellSize - 1, cellSize - 1);

                // Value text (if cells are large enough)
                if (cellSize >= 35) {
                    gc.setFill(norm > 0.5 ? Color.WHITE : Color.BLACK);
                    gc.fillText(String.format("%.1f", val),
                            j * cellSize + 4, i * cellSize + cellSize / 2 + 4);
                }
            }
        }

        // Grid
        gc.setStroke(Color.web("#333"));
        gc.setLineWidth(1);
        for (int i = 0; i <= rows; i++) {
            gc.strokeLine(0, i * cellSize, cols * cellSize, i * cellSize);
        }
        for (int j = 0; j <= cols; j++) {
            gc.strokeLine(j * cellSize, 0, j * cellSize, rows * cellSize);
        }
    }
}
