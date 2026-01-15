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

import java.awt.*;

import java.beans.Customizer;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class AbstractMatrixCustomizer extends JPanel implements Customizer,
    TableModelListener {
    /** DOCUMENT ME! */
    protected AbstractMatrixTableModel tableModel;

    /** DOCUMENT ME! */
    protected JTable table;

    /** DOCUMENT ME! */
    protected JScrollPane sPane;

    /** DOCUMENT ME! */
    protected JTextField numCols;

    /** DOCUMENT ME! */
    protected JTextField numRows;

    /** DOCUMENT ME! */
    protected boolean loop;

    /** DOCUMENT ME! */
    protected int initializedCols = 0;

/**
     * Creates a new AbstractMatrixCustomizer object.
     *
     * @param aTableModel DOCUMENT ME!
     */
    public AbstractMatrixCustomizer(AbstractMatrixTableModel aTableModel) {
        super(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = gbc.HORIZONTAL;
        tableModel = aTableModel;
        aTableModel.addTableModelListener(this);
        table = new JTable(tableModel);

        //   table.setAutoResizeMode(table.AUTO_RESIZE_OFF);
        // table.setOpaque(true);
        table.setDoubleBuffered(false);
        sPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(new JLabel("Number of Rows: "), gbc);
        gbc.weightx = 1.0f;
        gbc.insets.left = 10;
        numRows = new JTextField();
        numRows.setEditable(false);
        add(numRows, gbc);
        gbc.weightx = 2.0f;
        add(new JLabel("Number of Columns: ", JLabel.CENTER), gbc);
        gbc.weightx = 1.0f;
        numCols = new JTextField();
        numCols.setEditable(false);
        add(numCols, gbc);
        gbc.weightx = 3.0f;
        gbc.gridwidth = gbc.REMAINDER;
        add(new Label(""), gbc);
        gbc.insets.left = 0;
        gbc.fill = gbc.BOTH;
        gbc.weighty = 1.0f;
        add(sPane, gbc);
        sPane.setDoubleBuffered(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void tableChanged(TableModelEvent e) {
        if (!loop) {
            try {
                loop = true;
                firePropertyChange(null, null, null);
            } finally {
                loop = false;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     */
    public void setObject(Object o) {
        if (tableModel.getMatrix() == o) {
            return;
        }

        try {
            loop = true;

            AbstractMatrix m = (AbstractMatrix) o;
            tableModel.setMatrix(m);

            int num = m.numColumns();

            if (initializedCols < num) {
                DefaultTableColumnModel mod = (DefaultTableColumnModel) table.getColumnModel();

                for (int i = initializedCols; i < num; i++) {
                    TableColumn tc = mod.getColumn(i);
                    tc.setMinWidth(80);
                }

                initializedCols = num;
            }

            numRows.setText(String.valueOf(m.numRows()));
            numCols.setText(String.valueOf(m.numColumns()));
        } finally {
            loop = false;
        }
    }
}
