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

import java.awt.*;


/**
 * Editing a RealVector in a table.
 */
public class DoubleVectorEditor extends AbstractVectorEditor {
/**
     * Creates a new DoubleVectorEditor object.
     */
    public DoubleVectorEditor() {
        //Commented out by SMM
        // setValue(new DoubleVector());
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     */
    public void setValue(Object obj) {
        value = (DoubleVector) obj;

        if (editor != null) {
            editor.setVector((DoubleVector) value);
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

        editor = new VectorEditorPanel();
        editor.setVector((DoubleVector) value);

        return editor;
    }
}
