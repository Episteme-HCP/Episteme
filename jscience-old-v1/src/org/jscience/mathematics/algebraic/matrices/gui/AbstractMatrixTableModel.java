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

import org.jscience.mathematics.algebraic.AbstractMatrix;

import javax.swing.table.AbstractTableModel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
abstract class AbstractMatrixTableModel extends AbstractTableModel {
    /** DOCUMENT ME! */
    AbstractMatrix matrix;

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getColumnName(int column) {
        return String.valueOf(column);
    }

    /**
     * DOCUMENT ME!
     *
     * @param columnName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int findColumn(String columnName) {
        return Integer.parseInt(columnName);
    }

    /**
     * DOCUMENT ME!
     *
     * @param columnIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getColumnClass(int columnIndex) {
        return String.class;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return matrix.numRows();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return matrix.numColumns();
    }

    /**
     * DOCUMENT ME!
     *
     * @param rowIndex DOCUMENT ME!
     * @param columnIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mat DOCUMENT ME!
     */
    public void setMatrix(AbstractMatrix mat) {
        matrix = mat;
        fireTableStructureChanged();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractMatrix getMatrix() {
        return matrix;
    }
}
