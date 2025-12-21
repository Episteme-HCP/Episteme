/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.ui.mathematics;

import javafx.scene.control.SkinBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Default skin for MatrixViewer using TableView. * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
 */
public class MatrixViewerSkin<E> extends SkinBase<MatrixViewer<E>> {

    private final TableView<List<String>> tableView;

    public MatrixViewerSkin(MatrixViewer<E> control) {
        super(control);
        tableView = new TableView<>();

        // Initialize table from matrix
        int rows = control.getMatrix().rows();
        int cols = control.getMatrix().cols();

        // Create columns
        for (int j = 0; j < cols; j++) {
            final int colIndex = j;
            TableColumn<List<String>, String> col = new TableColumn<>(String.valueOf(j));
            col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(colIndex)));
            tableView.getColumns().add(col);
        }

        // Create data
        List<List<String>> data = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<String> rowData = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                rowData.add(control.getMatrix().get(i, j).toString());
            }
            data.add(rowData);
        }

        ObservableList<List<String>> observableData = FXCollections.observableArrayList(data);
        tableView.setItems(observableData);

        getChildren().add(tableView);
    }
}
