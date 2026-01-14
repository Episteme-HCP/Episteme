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

package org.jscience.util;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;


/**
 * ResultSetMetaDataTableModel provides a simple way to display the meta
 * data of a ResultSet.
 *
 * @author Holger Antelmann
 *
 * @see javax.swing.JTable
 * @see org.jscience.util.JDBC
 * @since 10/24/2002
 */
public class ResultSetMetaDataTableModel extends AbstractTableModel {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 7177233771789817572L;

    /** DOCUMENT ME! */
    ResultSetMetaData metaData;

    /** DOCUMENT ME! */
    String[] columnNames;

    /** DOCUMENT ME! */
    String[] columnTypes;

    /** DOCUMENT ME! */
    int[] sizes;

    /** DOCUMENT ME! */
    int rows;

/**
     * Creates a new ResultSetMetaDataTableModel object.
     *
     * @param metaData DOCUMENT ME!
     * @throws SQLException DOCUMENT ME!
     */
    public ResultSetMetaDataTableModel(ResultSetMetaData metaData)
        throws SQLException {
        this.metaData = metaData;
        rows = metaData.getColumnCount();
        columnNames = new String[rows];
        columnTypes = new String[rows];
        sizes = new int[rows];

        for (int i = 0; i < rows; i++) {
            columnNames[i] = metaData.getColumnName(i + 1);
            columnTypes[i] = metaData.getColumnTypeName(i + 1);
            sizes[i] = metaData.getPrecision(i + 1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ResultSetMetaData getResultSetMetaData() {
        return metaData;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return rows;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return 3;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public Object getValueAt(int row, int column) {
        switch (column) {
        case 0:
            return columnNames[row];

        case 1:
            return columnTypes[row];

        case 2:
            return new Integer(sizes[row]);

        default:
            throw new RuntimeException("invalid column: " + column);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public String getColumnName(int column) {
        switch (column) {
        case 0:
            return "name";

        case 1:
            return "type";

        case 2:
            return "size";

        default:
            throw new RuntimeException("invalid column: " + column);
        }
    }
}
