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

// JTEM - Java Tools for Experimental Mathematics
// Copyright (C) 2001 JEM-Group
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
package org.jscience.mathematics.algebraic.matrices.gui;

/**
 * An instance of this class is a TableModel object used for one columned
 * tables which cell content is restricted to integer values.
 */
class LineNumberModel extends javax.swing.table.AbstractTableModel {
    /** DOCUMENT ME! */
    int numRows;

    /**
     * Returns the type of class which instances can be content of a
     * cell of the table, in this case only integer values.
     *
     * @param columnIndex DOCUMENT ME!
     *
     * @return <code>Integer.class</code>
     */
    public Class getColumnClass(int columnIndex) {
        return Integer.class;
    }

    /**
     * Returns the number of colums the table consists of, in this case
     * only one column.
     *
     * @return <code>1</code>
     */
    public int getColumnCount() {
        return 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param columnIndex DOCUMENT ME!
     *
     * @return <code>Line</code>
     */
    public String getColumnName(int columnIndex) {
        return "Line";
    }

    /**
     * Returns the number of rows the table consists of.
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return numRows;
    }

    /**
     * Sets the number of rows the table consists of to the specified
     * int value.
     *
     * @param rows DOCUMENT ME!
     */
    public void setRowCount(int rows) {
        if (rows == numRows) {
            return;
        }

        int old = numRows;
        numRows = rows;

        if (rows > old) {
            fireTableRowsInserted(old, rows);
        } else {
            fireTableRowsDeleted(rows, old);
        }
    }

    /**
     * Returns the value for the cell at columnIndex and rowIndex.
     *
     * @param rowIndex DOCUMENT ME!
     * @param columnIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        return new Integer(rowIndex);
    }
}
