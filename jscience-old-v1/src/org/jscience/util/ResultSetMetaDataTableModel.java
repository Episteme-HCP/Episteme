/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
