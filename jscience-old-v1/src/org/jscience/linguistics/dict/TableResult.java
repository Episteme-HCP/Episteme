package org.jscience.linguistics.dict;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;


/**
 * Tabulka vysledku
 *
 * @author Stepan Bechynsky
 * @author bechynsky@bdflow.com
 * @author BD Flow, a. s.
 * @version 0.1
 */
public class TableResult extends AbstractTableModel {
    /**
     * DOCUMENT ME!
     */
    private static String[] columnName = { "Word", "Database" };

    /**
     * DOCUMENT ME!
     */
    private Vector data = new Vector();

/**
     * Creates a new TableResult object.
     *
     * @param data DOCUMENT ME!
     */
    public TableResult(Vector data) {
        this.data = data;
    } // END: TableResult(Vector data)

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return data.size();
    } // END: getRowCount()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return columnName.length;
    } // END: getColumnCount()

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValueAt(int row, int column) {
        if (column == 0) {
            return data.elementAt(row);
        } else if (column == 1) {
            return ((DSItem) data.elementAt(row)).getName();
        } else {
            return null;
        }
    } // END: getValueAt(int row, int column)

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getColumnName(int column) {
        return columnName[column];
    } // END: getColumnName(int column)

    /**
     * DOCUMENT ME!
     *
     * @param columns DOCUMENT ME!
     */
    public void setColumnNames(String[] columns) {
        columnName = columns;
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     */
    public void setData(Vector data) {
        this.data = data;
    } // END: setData(Vector data)
}
