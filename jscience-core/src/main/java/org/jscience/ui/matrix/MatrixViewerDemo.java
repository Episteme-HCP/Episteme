package org.jscience.ui.matrix;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.MatrixFactory;
import org.jscience.mathematics.numbers.real.Real;

public class MatrixViewerDemo extends Application {

    @Override
    public void start(Stage stage) {
        // Create a sample matrix
        Real[][] data = new Real[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                data[i][j] = Real.of(Math.random() * 100);
            }
        }
        Matrix<Real> matrix = MatrixFactory.create(data, Real.ZERO);

        MatrixViewer<Real> viewer = new MatrixViewer<>(matrix);

        StackPane root = new StackPane();
        root.getChildren().add(viewer);

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Matrix Viewer Demo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
