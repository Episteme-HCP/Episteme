/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.demos;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.MatrixFactory;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.mathematics.MatrixViewer;

public class MatrixDemo implements DemoProvider {
    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return "Matrix Viewer";
    }

    @Override
    public String getDescription() {
        return "Visualizes a matrix of Real numbers.";
    }

    @Override
    public void show(Stage stage) {
        // Create a sample matrix
        Real[][] data = new Real[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                data[i][j] = Real.of(Math.random() * 100);
            }
        }
        Matrix<Real> matrix = MatrixFactory.create(data, Real.ZERO);
        MatrixViewer<Real> viewer = new MatrixViewer<>(matrix);
        StackPane root = new StackPane(viewer);
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Matrix Viewer");
        stage.show();
    }
}
