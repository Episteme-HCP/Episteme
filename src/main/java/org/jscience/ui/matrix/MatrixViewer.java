package org.jscience.ui.matrix;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.jscience.mathematics.linearalgebra.Matrix;

public class MatrixViewer<E> extends Control {

    private final Matrix<E> matrix;

    public MatrixViewer(Matrix<E> matrix) {
        this.matrix = matrix;
        getStyleClass().add("matrix-viewer");
    }

    public Matrix<E> getMatrix() {
        return matrix;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MatrixViewerSkin<>(this);
    }
}
