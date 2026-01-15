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

import org.jscience.mathematics.algebraic.matrices.DoubleVector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class DoubleVectorTableModel extends AbstractVectorTableModel {
/**
     * Creates a new DoubleVectorTableModel object.
     */
    public DoubleVectorTableModel() {
        vector = new DoubleVector(1);
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
        return new Double(((DoubleVector) vector).getPrimitiveElement(row));
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

        ((DoubleVector) vector).setElement(rowIndex, d.doubleValue());
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
        return Double.class;
    }
}
