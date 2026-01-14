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
