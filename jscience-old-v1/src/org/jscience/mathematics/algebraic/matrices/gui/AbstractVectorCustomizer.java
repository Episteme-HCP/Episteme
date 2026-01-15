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

import org.jscience.mathematics.algebraic.AbstractVector;

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
abstract class AbstractVectorCustomizer extends JPanel implements Customizer,
    TableModelListener {
    /** DOCUMENT ME! */
    protected AbstractVectorTableModel tableModel;

    /** DOCUMENT ME! */
    protected JTable table;

    /** DOCUMENT ME! */
    protected JScrollPane sPane;

    /** DOCUMENT ME! */
    protected JTextField numRows;

    /** DOCUMENT ME! */
    protected boolean loop;

/**
     * Creates a new AbstractVectorCustomizer object.
     *
     * @param aTableModel DOCUMENT ME!
     */
    public AbstractVectorCustomizer(AbstractVectorTableModel aTableModel) {
        super(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = gbc.HORIZONTAL;
        tableModel = aTableModel;
        aTableModel.addTableModelListener(this);
        table = new JTable(tableModel);

        // table.setOpaque(true);
        table.setDoubleBuffered(false);
        sPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(new JLabel("Number of Elements: "), gbc);
        gbc.weightx = 1.0f;
        gbc.insets.left = 10;
        numRows = new JTextField();
        numRows.setEditable(false);
        add(numRows, gbc);
        gbc.weightx = 1.5f;
        gbc.gridwidth = gbc.REMAINDER;
        add(new JLabel(""), gbc);
        gbc.insets.left = 0;
        gbc.fill = gbc.BOTH;
        gbc.weighty = 1.0f;
        add(sPane, gbc);
        sPane.setDoubleBuffered(false);

        DefaultTableColumnModel mod = (DefaultTableColumnModel) table.getColumnModel();
        TableColumn tc = mod.getColumn(0);

        /*    tc.setMinWidth(80);
        tc.setPreferredWidth(130);
        tc.setMaxWidth(Integer.MAX_VALUE);
        tc.setResizable(false); */
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
        try {
            loop = true;

            AbstractVector m = (AbstractVector) o;
            tableModel.setVector(m);
            numRows.setText(String.valueOf(m.getDimension()));
        } finally {
            loop = false;
        }
    }
}
