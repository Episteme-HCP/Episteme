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
import org.jscience.ui.AppProvider;
import org.jscience.ui.i18n.I18n;
import org.jscience.ui.mathematics.linearalgebra.MatrixViewer;
// import org.jscience.ui.ThemeManager;

/**
 * Enhanced Matrix Viewer with multiple display modes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MatrixDemo implements AppProvider {

    private Matrix<Real> matrix;
    private Canvas heatmapCanvas;
    private MatrixViewer<Real> tableViewer;
    private StackPane viewContainer;
    private Spinner<Integer> rowsSpinner, colsSpinner;
    private Label infoLabel;

    @Override
    public boolean isDemo() {
        return true;
    }

    @Override
    public String getCategory() {
        return "Mathematics";
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
        root.setStyle("-fx-background-color: white;");

        // Header
        Label header = new Label(I18n.getInstance().get("matrix.label.header"));
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: black;");
        HBox headerBox = new HBox(header);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #16213e;");
        root.setTop(headerBox);

        // Control Panel (Left)
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(15));
        controls.setPrefWidth(220);
        controls.setStyle("-fx-background-color: #f4f4f4;");

        Label sizeLabel = new Label(I18n.getInstance().get("matrix.label.size"));
        sizeLabel.setStyle("-fx-text-fill: #aaa; -fx-font-weight: bold;");

        HBox rowBox = new HBox(10);
        rowBox.setAlignment(Pos.CENTER_LEFT);
        Label rowLabel = new Label(I18n.getInstance().get("matrix.label.rows") + ":");
        // rowLabel.setStyle("-fx-text-fill: #888;");
        rowsSpinner = new Spinner<>(2, 50, 8);
        rowsSpinner.setPrefWidth(80);
        rowBox.getChildren().addAll(rowLabel, rowsSpinner);

        HBox colBox = new HBox(10);
        colBox.setAlignment(Pos.CENTER_LEFT);
        Label colLabel = new Label(I18n.getInstance().get("matrix.label.cols") + ":");
        // colLabel.setStyle("-fx-text-fill: #888;");
        colsSpinner = new Spinner<>(2, 50, 8);
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
        infoLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;"); // Removed blue

        controls.getChildren().addAll(
                sizeLabel, rowBox, colBox, generateBtn, identityBtn,
                sep1, viewLabel, tableRadio, heatmapRadio,
                sep2, opsLabel, transposeBtn, scaleBtn,
                new Separator(), infoLabel);
        root.setLeft(controls);

        // View Container (Center)
        viewContainer = new StackPane();
        viewContainer.setStyle("-fx-background-color: white; -fx-border-color: lightgray;");
        viewContainer.setPadding(new Insets(20));
        root.setCenter(viewContainer);

        // Initialize with random matrix
        generateMatrix();

        Scene scene = new Scene(root, 900, 650);
        stage.setTitle(I18n.getInstance().get("matrix.title"));
        stage.setScene(scene);
        // ThemeManager.getInstance().applyTheme(scene);
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
        // Persist view mode
        // Persist view mode (logic checks container children)
        // Hacky way to get radio, better to store reference or check toggle group.
        // Actually I have references to radios in show(), but they are local variables.
        // I should make them fields or check logic.
        // Wait, 'updateViews' is called from generateMatrix, generateIdentity, etc.
        // I should check existing children of viewContainer to see what was shown?
        // Or check toggle group if I had access.
        // Refactoring to make ToggleGroup a field would be best, but let's try to just
        // check viewContainer content?
        // Actually, let's keep it simple: defaulting to TableView is annoying.
        // I will rely on the ToggleGroup which I should promote to field or access
        // differently.
        // Since I can't easily promote to field in replace_file_content without
        // changing class definition,
        // I will assume TableView for now but wait, I can change the class definition
        // block if I include it.
        // But I am replacing lines 133-313.

        // Let's check viewContainer.
        boolean heatmapActive = false;
        if (!viewContainer.getChildren().isEmpty()) {
            if (viewContainer.getChildren().get(0) instanceof Canvas) {
                heatmapActive = true;
            }
        }

        if (heatmapActive) {
            showHeatmapView();
        } else {
            showTableView();
        }
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

        int legendWidth = 60;
        int width = cols * cellSize + legendWidth + 20;
        int height = rows * cellSize + 2;

        heatmapCanvas = new Canvas(Math.max(width, 200), Math.max(height, 200));
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
                gc.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);

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
            // gc.strokeLine(0, i * cellSize, cols * cellSize, i * cellSize);
            // Better grid: rects
            for (int j = 0; j < cols; j++) {
                gc.strokeRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }

        // Legend on Right
        double legendX = cols * cellSize + 10;
        double legendH = rows * cellSize;
        if (legendH < 100)
            legendH = 100; // Min height for legend

        // Gradient Bar
        for (int i = 0; i < legendH; i++) {
            double ratio = 1.0 - (double) i / legendH; // 1 at top (Red), 0 at bottom (Blue)
            gc.setFill(Color.hsb(240 - ratio * 240, 0.8, 0.9));
            gc.fillRect(legendX, i, 15, 1);
        }
        gc.setStroke(Color.BLACK);
        gc.strokeRect(legendX, 0, 15, legendH);

        // Labels
        gc.setFill(Color.BLACK);
        gc.fillText(String.format("%.1f", max), legendX + 20, 10);
        gc.fillText(String.format("%.1f", min), legendX + 20, legendH);
    }
}


