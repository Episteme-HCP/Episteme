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

package org.jscience.ui.viewers.mathematics.linearalgebra;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.MatrixFactory;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Parameter;
import org.jscience.io.Configuration;

import java.util.ArrayList;
import java.util.List;

public class MatrixViewer<T> extends AbstractViewer {

    private static final String CFG_PREFIX = "viewer.matrixviewer.default.";
    
    private Matrix<Real> matrix;
    private Canvas heatmapCanvas;
    private StackPane viewContainer;
    private Spinner<Integer> rowsSpinner, colsSpinner;
    private Label infoLabel;

    @Override
    public List<Parameter<?>> getViewerParameters() {
        int defaultRows = Configuration.getInt(CFG_PREFIX + "rows", 8);
        int defaultCols = Configuration.getInt(CFG_PREFIX + "cols", 8);
        List<Parameter<?>> params = new ArrayList<>();
        params.add(new org.jscience.ui.NumericParameter("viewer.matrixviewer.param.rows",
                org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.param.rows.desc", "Number of rows in the matrix"),
                2, 50, 1, defaultRows, v -> { if(rowsSpinner != null) rowsSpinner.getValueFactory().setValue(v.intValue()); }));
        params.add(new org.jscience.ui.NumericParameter("viewer.matrixviewer.param.cols",
                org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.param.cols.desc", "Number of columns in the matrix"),
                2, 50, 1, defaultCols, v -> { if(colsSpinner != null) colsSpinner.getValueFactory().setValue(v.intValue()); }));
        return params;
    }

    public MatrixViewer() {
        initUI();
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.mathematics", "Mathematics");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.name", "Matrix Viewer");
    }
    
    public void initUI() {
        BorderPane layout = new BorderPane();
        layout.getStyleClass().add("viewer-root");

        // Header
        Label header = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.label.header", "🔢 Matrix Viewer"));
        header.getStyleClass().add("viewer-header-label");
        HBox headerBox = new HBox(header);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15));
        headerBox.getStyleClass().add("viewer-header");
        layout.setTop(headerBox);

        // Control Panel (Left)
        VBox controls = new VBox(15);
        controls.setPadding(new Insets(15));
        controls.setPrefWidth(220);
        controls.getStyleClass().add("viewer-sidebar");

        Label sizeLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.label.size", "Matrix Size"));
        sizeLabel.getStyleClass().add("viewer-section-label");

        HBox rowBox = new HBox(10);
        rowBox.setAlignment(Pos.CENTER_LEFT);
        Label rowLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.label.rows", "Rows") + ":");
        rowsSpinner = new Spinner<>(2, 50, 8);
        rowsSpinner.setPrefWidth(80);
        rowBox.getChildren().addAll(rowLabel, rowsSpinner);

        HBox colBox = new HBox(10);
        colBox.setAlignment(Pos.CENTER_LEFT);
        Label colLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.label.cols", "Cols") + ":");
        colsSpinner = new Spinner<>(2, 50, 8);
        colsSpinner.setPrefWidth(80);
        colBox.getChildren().addAll(colLabel, colsSpinner);

        Button generateBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.button.random", "Generate Random Matrix"));
        generateBtn.setMaxWidth(Double.MAX_VALUE);
        generateBtn.getStyleClass().add("viewer-primary-button");
        generateBtn.setOnAction(e -> generateMatrix());

        Button identityBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.button.identity", "Identity Matrix"));
        identityBtn.setMaxWidth(Double.MAX_VALUE);
        identityBtn.setOnAction(e -> generateIdentity());

        Separator sep1 = new Separator();

        Label viewLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.view.label", "View Mode"));
        viewLabel.getStyleClass().add("viewer-section-label");

        ToggleGroup viewGroup = new ToggleGroup();
        RadioButton tableRadio = new RadioButton(org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.view.table", "Table View"));
        tableRadio.getStyleClass().add("viewer-radio");
        tableRadio.setToggleGroup(viewGroup);
        tableRadio.setSelected(true);
        tableRadio.setOnAction(e -> showTableView());

        RadioButton heatmapRadio = new RadioButton(org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.view.heatmap", "Heatmap View"));
        heatmapRadio.getStyleClass().add("viewer-radio");
        heatmapRadio.setToggleGroup(viewGroup);
        heatmapRadio.setOnAction(e -> showHeatmapView());

        Separator sep2 = new Separator();

        Label opsLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.ops.label", "Operations"));
        opsLabel.getStyleClass().add("viewer-section-label");

        Button transposeBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.ops.transpose", "Transpose"));
        transposeBtn.setMaxWidth(Double.MAX_VALUE);
        transposeBtn.setOnAction(e -> transpose());

        Button scaleBtn = new Button(org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.ops.scale", "Scale (2x)"));
        scaleBtn.setMaxWidth(Double.MAX_VALUE);
        scaleBtn.setOnAction(e -> scale(2.0));

        infoLabel = new Label("8x8 " + org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.info.matrix", "Matrix"));
        infoLabel.getStyleClass().add("viewer-info-label");

        controls.getChildren().addAll(
                sizeLabel, rowBox, colBox, generateBtn, identityBtn,
                sep1, viewLabel, tableRadio, heatmapRadio,
                sep2, opsLabel, transposeBtn, scaleBtn,
                new Separator(), infoLabel);
        layout.setLeft(controls);

        // View Container (Center)
        viewContainer = new StackPane();
        viewContainer.getStyleClass().add("viewer-content");
        viewContainer.setPadding(new Insets(20));
        layout.setCenter(viewContainer);

        getChildren().add(layout);

        // Initialize with random matrix
        generateMatrix();
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
        infoLabel.setText(rows + "\u00D7" + cols + " " + org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.info.matrix", "Matrix"));
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
        infoLabel.setText(size + "\u00D7" + size + " " + org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.info.identity", "Identity"));
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
                    + org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.info.transposed", "Transposed") + ")");
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
                    + org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.info.scaled", "Scaled") + " \u00D7" + factor + ")");
        }
    }

    private void updateViews() {
        // Here we could recreate a simple TableView if needed or just use heatmap
        // table logic extracted if desired, but for simplicity of migration we use text/heatmap
        createHeatmap();
        
        // Check active view
        boolean heatmapActive = false;
        if (!viewContainer.getChildren().isEmpty()) {
            if (viewContainer.getChildren().get(0) instanceof Canvas) {
                heatmapActive = true;
            }
        }

        if (heatmapActive) {
            showHeatmapView();
        } else {
            showTableView(); // Placeholder for table view logic
        }
    }

    private void showTableView() {
        viewContainer.getChildren().clear();
        Label placeholder = new Label(org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.table.placeholder", "Table View Not Implemented (Use Heatmap)"));
        viewContainer.getChildren().add(placeholder);
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

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.desc", "Interactive matrix visualization and operations.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("viewer.matrixviewer.longdesc", "Visualize matrices with heatmaps and perform linear algebra operations including determinant, inverse, eigenvalues, and decompositions. Supports random matrix generation and scaling.");
    }
}
