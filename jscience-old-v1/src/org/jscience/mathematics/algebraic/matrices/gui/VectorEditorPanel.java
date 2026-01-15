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
import org.jscience.mathematics.algebraic.matrices.ComplexVector;
import org.jscience.mathematics.algebraic.matrices.DoubleVector;
import org.jscience.mathematics.algebraic.matrices.IntegerVector;
import org.jscience.mathematics.algebraic.numbers.Complex;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;


/**
 * An instance of this class is a JPanel containing a one columned JTable
 * representing a Vector. A VectorEditorPanel instance is used for every kind
 * of VectorEditor.
 */
public class VectorEditorPanel extends JPanel implements TableModelListener,
    ActionListener, java.io.Serializable {
    /** DOCUMENT ME! */
    private JTable table;

    /** DOCUMENT ME! */
    private JTable rowHeaderTable;

    /** DOCUMENT ME! */
    private JScrollPane sPane;

    /** DOCUMENT ME! */
    private JTextField elementsTF;

    /** DOCUMENT ME! */
    private AbstractVectorTableModel tableModel;

    /** DOCUMENT ME! */
    private LineNumberModel lineNumberModel;

    /** DOCUMENT ME! */
    private AbstractVector vector;

    /** DOCUMENT ME! */
    private GridBagConstraints gbc = new GridBagConstraints();

/**
     * Constructs a new VectorEditorPanel.
     */
    public VectorEditorPanel() {
        setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets.top = 10;
        gbc.insets.bottom = gbc.insets.right = gbc.insets.left = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 1.f;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        add(new JLabel("number of elements", JLabel.RIGHT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 0.f;
        add(elementsTF = new JTextField(), gbc);
        elementsTF.addActionListener(this);
        elementsTF.setColumns(8);
        gbc.insets.top = 0;

        table = new JTable();
        table.setDefaultRenderer(Complex.class, new ComplexTableCellRenderer());
        table.setDefaultEditor(Complex.class, new ComplexTableCellEditor());

        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setCellSelectionEnabled(true);
        sPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        lineNumberModel = new LineNumberModel();
        rowHeaderTable = new JTable(lineNumberModel);

        Color color = table.getTableHeader().getBackground();
        rowHeaderTable.setBackground(color);
        rowHeaderTable.setIntercellSpacing(new Dimension(20, 0));

        //rowHeaderTable-cells should not be selectable
        rowHeaderTable.setEnabled(false);
        rowHeaderTable.setPreferredScrollableViewportSize(new Dimension(60, 20));
        sPane.setRowHeaderView(rowHeaderTable);

        //sPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, new JButton("element"));
        add(sPane, gbc);
    }

    /**
     * Sets the relation between this object and the used Vector
     * object. The specified AbstractVector object defines the kind of vector
     * working together and provides the needed reference to this class. The
     * AbstractVectorTableModel object transfers the right TableModel storing
     * all data for the vector.
     *
     * @param v DOCUMENT ME!
     * @param model DOCUMENT ME!
     */
    public void setVector(AbstractVector v, AbstractVectorTableModel model) {
        vector = v;

        tableModel = model;
        tableModel.setVector(vector);
        tableModel.addTableModelListener(this);
        table.setModel(tableModel);

        int elements = tableModel.getRowCount();

        elementsTF.setText(String.valueOf(elements));

        //maximal 20 Elemente sichtbar
        if (((elements * 16) + 21) > 341) {
            sPane.setPreferredSize(new Dimension(100, 341));
        } else {
            sPane.setPreferredSize(new Dimension(100, (elements * 16) + 21));
        }

        lineNumberModel.setRowCount(elements);
    }

    /**
     * Sets the relation between this object and the specified
     * IntegerVector object.
     *
     * @param v DOCUMENT ME!
     */
    public void setVector(IntegerVector v) {
        setVector(v, new IntegerVectorTableModel());
    }

    /**
     * Sets the relation between this object and the specified
     * RealVector object.
     *
     * @param v DOCUMENT ME!
     */
    public void setVector(DoubleVector v) {
        setVector(v, new DoubleVectorTableModel());
    }

    /**
     * Sets the relation between this object and the specified
     * ComplexVector object.
     *
     * @param v DOCUMENT ME!
     */
    public void setVector(ComplexVector v) {
        setVector(v, new ComplexVectorTableModel());
    }

    /**
     * Updates the related Vector object whenever TableModel datas were
     * changed.
     *
     * @param ev DOCUMENT ME!
     */
    public void tableChanged(TableModelEvent ev) {
        vector = tableModel.getVector();
    }

    /**
     * Updates the count of rows of the displayed table and the related
     * vector whenever the responsible textfield was edited.
     *
     * @param ev DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent ev) {
        JTextField source = (JTextField) ev.getSource();
        int n = Integer.parseInt(source.getText());

        if (n == tableModel.getRowCount()) {
            return;
        }

        if (((n * 16) + 21) > 341) {
            sPane.setPreferredSize(new Dimension(100, 341));
        } else {
            sPane.setPreferredSize(new Dimension(100, (n * 16) + 21));
        }

        //todo the code below is really unoptimized!!!!
        //if we could resize the matrix internally that would be much better, especially using arrayCopy
        if (vector instanceof IntegerVector) {
            IntegerVector newVector;
            newVector = new IntegerVector(n);

            int rows;

            if (n < vector.getDimension()) {
                rows = Math.min(n, vector.getDimension());
            } else {
                rows = n;
            }

            for (int i = 0; i < rows; i++) {
                newVector.setElement(i,
                    ((IntegerVector) vector).getPrimitiveElement(i));
            }

            lineNumberModel.setRowCount(n);
            tableModel.setVector(newVector);
        } else if (vector instanceof DoubleVector) {
            DoubleVector newVector;
            newVector = new DoubleVector(n);

            int rows;

            if (n < vector.getDimension()) {
                rows = Math.min(n, vector.getDimension());
            } else {
                rows = n;
            }

            for (int i = 0; i < rows; i++) {
                newVector.setElement(i,
                    ((DoubleVector) vector).getPrimitiveElement(i));
            }

            lineNumberModel.setRowCount(n);
            tableModel.setVector(newVector);
        } else if (vector instanceof ComplexVector) {
            ComplexVector newVector;
            newVector = new ComplexVector(n);

            int rows;

            if (n < vector.getDimension()) {
                rows = Math.min(n, vector.getDimension());
            } else {
                rows = n;
            }

            for (int i = 0; i < rows; i++) {
                newVector.setElement(i, ((ComplexVector) vector).getElement(i));
            }

            lineNumberModel.setRowCount(n);
            tableModel.setVector(newVector);
        }
    }
}
