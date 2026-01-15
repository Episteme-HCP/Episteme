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

import org.jscience.mathematics.algebraic.matrices.IntegerMatrix;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class IntegerMatrixTableModel extends AbstractMatrixTableModel {
/**
     * Creates a new IntegerMatrixTableModel object.
     */
    public IntegerMatrixTableModel() {
        matrix = new IntegerMatrix(1, 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized Object getValueAt(int row, int column) {
        return new Integer(((IntegerMatrix) matrix).getPrimitiveElement(row,
                column));
    }

    /**
     * DOCUMENT ME!
     *
     * @param aValue DOCUMENT ME!
     * @param rowIndex DOCUMENT ME!
     * @param columnIndex DOCUMENT ME!
     */
    public synchronized void setValueAt(Object aValue, int rowIndex,
        int columnIndex) {
        Number d = (Number) aValue;

        ((IntegerMatrix) matrix).setElement(rowIndex, columnIndex, d.intValue());
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * DOCUMENT ME!
     *
     * @param anIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getColumnClass(int anIndex) {
        return Integer.class;
    }
}
