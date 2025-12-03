package org.jscience.mathematics.vector.backend;

import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.algebra.Field;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;
import org.jscience.mathematics.vector.Matrix;
import org.jscience.mathematics.vector.Vector;

/**
 * Default implementation of {@link LinearAlgebraProvider} using pure Java
 * loops.
 * <p>
 * This provider runs on the CPU and serves as the baseline/fallback
 * implementation.
 * </p>
 * 
 * @param <E> the type of scalar elements
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class JavaLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    protected final Field<E> field;

    public JavaLinearAlgebraProvider(Field<E> field) {
        this.field = field;
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vector dimensions must match");
        }
        List<E> result = new ArrayList<>(a.dimension());
        for (int i = 0; i < a.dimension(); i++) {
            result.add(field.add(a.get(i), b.get(i)));
        }
        return new DenseVector<>(result, field);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vector dimensions must match");
        }
        List<E> result = new ArrayList<>(a.dimension());
        for (int i = 0; i < a.dimension(); i++) {
            E negB = field.negate(b.get(i));
            result.add(field.add(a.get(i), negB));
        }
        return new DenseVector<>(result, field);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        List<E> result = new ArrayList<>(vector.dimension());
        for (int i = 0; i < vector.dimension(); i++) {
            result.add(field.multiply(vector.get(i), scalar));
        }
        return new DenseVector<>(result, field);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("Vector dimensions must match");
        }
        E sum = field.zero();
        for (int i = 0; i < a.dimension(); i++) {
            E product = field.multiply(a.get(i), b.get(i));
            sum = field.add(sum, product);
        }
        return sum;
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        if (a.rows() != b.rows() || a.cols() != b.cols()) {
            throw new IllegalArgumentException("Matrix dimensions must match");
        }
        List<List<E>> resultRows = new ArrayList<>();
        for (int i = 0; i < a.rows(); i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < a.cols(); j++) {
                row.add(field.add(a.get(i, j), b.get(i, j)));
            }
            resultRows.add(row);
        }
        return new DenseMatrix<>(resultRows, field);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        if (a.cols() != b.rows()) {
            throw new IllegalArgumentException("Matrix inner dimensions must match");
        }
        List<List<E>> resultRows = new ArrayList<>();
        for (int i = 0; i < a.rows(); i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < b.cols(); j++) {
                E sum = field.zero();
                for (int k = 0; k < a.cols(); k++) {
                    E product = field.multiply(a.get(i, k), b.get(k, j));
                    sum = field.add(sum, product);
                }
                row.add(sum);
            }
            resultRows.add(row);
        }
        return new DenseMatrix<>(resultRows, field);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        if (a.cols() != b.dimension()) {
            throw new IllegalArgumentException("Matrix columns must match vector dimension");
        }
        List<E> result = new ArrayList<>(a.rows());
        for (int i = 0; i < a.rows(); i++) {
            E sum = field.zero();
            for (int j = 0; j < a.cols(); j++) {
                E product = field.multiply(a.get(i, j), b.get(j));
                sum = field.add(sum, product);
            }
            result.add(sum);
        }
        return new DenseVector<>(result, field);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        if (a.rows() != a.cols()) {
            throw new ArithmeticException("Matrix must be square to compute inverse");
        }
        int n = a.rows();

        // Use Gaussian elimination with partial pivoting
        // Create augmented matrix [A | I]
        List<List<E>> aug = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(a.get(i, j));
            }
            for (int j = 0; j < n; j++) {
                row.add(i == j ? field.one() : field.zero());
            }
            aug.add(row);
        }

        // Forward elimination with partial pivoting
        for (int col = 0; col < n; col++) {
            // Find pivot
            int pivotRow = col;
            for (int i = col + 1; i < n; i++) {
                // Simple magnitude comparison (not perfect for all field types)
                if (!aug.get(i).get(col).equals(field.zero())) {
                    pivotRow = i;
                    break;
                }
            }

            // Swap rows if needed
            if (pivotRow != col) {
                List<E> temp = aug.get(col);
                aug.set(col, aug.get(pivotRow));
                aug.set(pivotRow, temp);
            }

            E pivot = aug.get(col).get(col);
            if (pivot.equals(field.zero())) {
                throw new ArithmeticException("Matrix is singular");
            }

            // Normalize pivot row
            E pivotInv = field.divide(field.one(), pivot);
            for (int j = 0; j < 2 * n; j++) {
                aug.get(col).set(j, field.multiply(aug.get(col).get(j), pivotInv));
            }

            // Eliminate column
            for (int i = 0; i < n; i++) {
                if (i != col) {
                    E factor = aug.get(i).get(col);
                    for (int j = 0; j < 2 * n; j++) {
                        E val = aug.get(i).get(j);
                        E sub = field.multiply(factor, aug.get(col).get(j));
                        aug.get(i).set(j, field.add(val, field.negate(sub)));
                    }
                }
            }
        }

        // Extract inverse from right half
        List<List<E>> invRows = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<E> row = new ArrayList<>();
            for (int j = n; j < 2 * n; j++) {
                row.add(aug.get(i).get(j));
            }
            invRows.add(row);
        }

        return new DenseMatrix<>(invRows, field);
    }

    @Override
    public E determinant(Matrix<E> a) {
        if (a.rows() != a.cols()) {
            throw new ArithmeticException("Matrix must be square to compute determinant");
        }
        int n = a.rows();
        if (n == 1)
            return a.get(0, 0);
        if (n == 2) {
            E ad = field.multiply(a.get(0, 0), a.get(1, 1));
            E bc = field.multiply(a.get(0, 1), a.get(1, 0));
            return field.add(ad, field.negate(bc));
        }

        // Use LU decomposition for larger matrices
        // Create a mutable copy
        List<List<E>> mat = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<E> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(a.get(i, j));
            }
            mat.add(row);
        }

        E det = field.one();

        // Gaussian elimination to get upper triangular form
        for (int col = 0; col < n; col++) {
            // Find pivot
            int pivotRow = col;
            for (int i = col + 1; i < n; i++) {
                if (!mat.get(i).get(col).equals(field.zero())) {
                    pivotRow = i;
                    break;
                }
            }

            // Swap rows if needed (changes sign of determinant)
            if (pivotRow != col) {
                List<E> temp = mat.get(col);
                mat.set(col, mat.get(pivotRow));
                mat.set(pivotRow, temp);
                det = field.negate(det);
            }

            E pivot = mat.get(col).get(col);
            if (pivot.equals(field.zero())) {
                return field.zero(); // Singular matrix
            }

            det = field.multiply(det, pivot);

            // Eliminate below pivot
            for (int i = col + 1; i < n; i++) {
                E factor = field.divide(mat.get(i).get(col), pivot);
                for (int j = col; j < n; j++) {
                    E val = mat.get(i).get(j);
                    E sub = field.multiply(factor, mat.get(col).get(j));
                    mat.get(i).set(j, field.add(val, field.negate(sub)));
                }
            }
        }

        return det;
    }
}
