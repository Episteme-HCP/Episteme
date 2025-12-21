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
package org.jscience.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.matrices.MatrixFactory;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.mathematics.MatrixViewer;

public class MatrixViewerDemo extends Application {

    public static void show(Stage stage) {
        new MatrixViewerDemo().start(stage);
    }

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
