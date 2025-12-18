package org.jscience.ui.matrix;

import javafx.scene.control.SkinBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Default skin for MatrixViewer using TableView.
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
