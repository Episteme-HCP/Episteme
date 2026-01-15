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

import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;

import java.awt.*;


/**
 * Editing a DoubleMatrix in a table.
 */
public class DoubleMatrixEditor extends AbstractMatrixEditor {
/**
     * Creates a new DoubleMatrixEditor object.
     */
    public DoubleMatrixEditor() {
        //Commented out by SMM
        // setValue(new DoubleMatrix());
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     */
    public void setValue(Object obj) {
        value = (DoubleMatrix) obj;

        if (editor != null) {
            editor.setMatrix((DoubleMatrix) value);
        }

        super.setValue(obj);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getCustomEditor() {
        if (editor != null) {
            return editor;
        }

        editor = new MatrixEditorPanel();
        editor.setMatrix((DoubleMatrix) value);

        return editor;
    }
}
