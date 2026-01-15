package org.jscience.io;

import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;
import org.jscience.mathematics.algebraic.matrices.IntegerMatrix;

import java.io.*;


/**
 * Text writer, writes data text files/streams. This class uses buffered
 * I/O.
 *
 * @author Mark Hale
 * @version 1.7
 */
public final class TextWriter extends OutputStreamWriter {
    /** DOCUMENT ME! */
    private final BufferedWriter writer = new BufferedWriter(this);

    /** DOCUMENT ME! */
    private char delimiter;

/**
     * Writes text data to an output stream.
     *
     * @param stream DOCUMENT ME!
     */
    public TextWriter(OutputStream stream) {
        super(stream);
    }

/**
     * Writes to a text file with the specified system dependent file name.
     *
     * @param name the system dependent file name.
     * @param ch   the character that delimits data columns.
     * @throws IOException If the file is not found.
     */
    public TextWriter(String name, char ch) throws IOException {
        super(new FileOutputStream(name));
        delimiter = ch;
    }

/**
     * Writes to a text file with the specified File object.
     *
     * @param file the file to be opened for writing.
     * @param ch   the character that delimits data columns.
     * @throws IOException If the file is not found.
     */
    public TextWriter(File file, char ch) throws IOException {
        super(new FileOutputStream(file));
        delimiter = ch;
    }

    /**
     * Writes a single character.
     *
     * @param c DOCUMENT ME!
     *
     * @throws IOException If an I/O error occurs.
     */
    public void write(int c) throws IOException {
        writer.write(c);
    }

    /**
     * Writes a string.
     *
     * @param str DOCUMENT ME!
     *
     * @throws IOException If an I/O error occurs.
     */
    public void write(String str) throws IOException {
        writer.write(str);
    }

    /**
     * Close the stream.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void close() throws IOException {
        writer.flush();
        super.close();
    }

    /**
     * Writes an array of data.
     *
     * @param data the data to be written.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void write(double[] data) throws IOException {
        int i;

        for (i = 0; i < (data.length - 1); i++)
            write(data[i] + " " + delimiter + " ");

        write(data[i] + "\n");
    }

    /**
     * Writes an array of data.
     *
     * @param data the data to be written.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void write(double[][] data) throws IOException {
        for (int j, i = 0; i < data.length; i++) {
            for (j = 0; j < (data[i].length - 1); j++)
                write(data[i][j] + " " + delimiter + " ");

            write(data[i][j] + "\n");
        }
    }

    /**
     * Writes an array of data.
     *
     * @param data the data to be written.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void write(int[] data) throws IOException {
        int i;

        for (i = 0; i < (data.length - 1); i++)
            write(data[i] + " " + delimiter + " ");

        write(data[i] + "\n");
    }

    /**
     * Writes an array of data.
     *
     * @param data the data to be written.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void write(int[][] data) throws IOException {
        for (int j, i = 0; i < data.length; i++) {
            for (j = 0; j < (data[i].length - 1); j++)
                write(data[i][j] + " " + delimiter + " ");

            write(data[i][j] + "\n");
        }
    }

    /**
     * Writes a matrix.
     *
     * @param matrix the matrix to be written.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void write(DoubleMatrix matrix) throws IOException {
        final int mRow = matrix.numRows();
        final int mCol = matrix.numColumns();

        for (int j, i = 0; i < mRow; i++) {
            for (j = 0; j < (mCol - 1); j++)
                write(matrix.getElement(i, j) + " " + delimiter + " ");

            write(matrix.getElement(i, j) + "\n");
        }
    }

    /**
     * Writes a matrix.
     *
     * @param matrix the matrix to be written.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void write(IntegerMatrix matrix) throws IOException {
        final int mRow = matrix.numRows();
        final int mCol = matrix.numColumns();

        for (int j, i = 0; i < mRow; i++) {
            for (j = 0; j < (mCol - 1); j++)
                write(matrix.getElement(i, j) + " " + delimiter + " ");

            write(matrix.getElement(i, j) + "\n");
        }
    }
}
