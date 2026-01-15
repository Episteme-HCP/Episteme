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
import org.jscience.mathematics.algebraic.matrices.ComplexMatrix;
import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;
import org.jscience.mathematics.algebraic.matrices.IntegerMatrix;
import org.jscience.mathematics.algebraic.numbers.Complex;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;


/**
 * An instance of this class is a JPanel containing a JTable representing a
 * Matrix. A MatrixEditorPanel instance is used for every kind of
 * MatrixEditor.
 */
public class MatrixEditorPanel extends JPanel implements TableModelListener,
    ActionListener, java.io.Serializable {
    /** DOCUMENT ME! */
    private JTable table;

    /** DOCUMENT ME! */
    private JTable rowHeaderTable;

    /** DOCUMENT ME! */
    private IntegerMatrix rowHeaderMatrix;

    /** DOCUMENT ME! */
    private JScrollPane sPane;

    /** DOCUMENT ME! */
    private JTextField rowTF;

    /** DOCUMENT ME! */
    private JTextField colTF;

    /** DOCUMENT ME! */
    private AbstractMatrixTableModel tableModel;

    /** DOCUMENT ME! */
    private LineNumberModel lineNumberModel;

    /** DOCUMENT ME! */
    private AbstractMatrix matrix;

    /** DOCUMENT ME! */
    private GridBagConstraints gbc = new GridBagConstraints();

    //finals
    /** DOCUMENT ME! */
    private final int ROW_HEIGHT = 16;

    /** DOCUMENT ME! */
    private final int REST_HEIGHT = 36;

    /** DOCUMENT ME! */
    private final int MAX_VISIBLE_HEIGHT = 356;

    /** DOCUMENT ME! */
    private DefaultTableColumnModel columnModel;

    /** DOCUMENT ME! */
    private int columnWidth;

    /** DOCUMENT ME! */
    PropertyChangeListener columnListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                if (!"width".equals(ev.getPropertyName())) {
                    return;
                }

                TableColumn c = (TableColumn) ev.getSource();
                int tabWidth = table.getPreferredSize().width;
                Insets ins = sPane.getViewport().getInsets();
                int panWidth = sPane.getViewport().getWidth() - ins.left -
                    ins.right;

                if (tabWidth < panWidth) {
                    int delta = panWidth - tabWidth;
                    TableColumn lastColumn = columnModel.getColumn(table.getColumnCount() -
                            1);

                    if (c != lastColumn) {
                        lastColumn.setPreferredWidth(lastColumn.getPreferredWidth() +
                            delta);
                    } else {
                        TableColumn firstColumn = columnModel.getColumn(0);
                        firstColumn.setPreferredWidth(firstColumn.getPreferredWidth() +
                            delta);
                    }
                }
            }
        };

/**
     * Constructs a new MatrixEditorPanel.
     */
    public MatrixEditorPanel() {
        setLayout(new GridBagLayout());
        gbc.insets.top = 10;
        gbc.insets.bottom = gbc.insets.right = gbc.insets.left = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0.f;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        add(new JLabel("rows"), gbc);
        gbc.gridwidth = 2;
        add(rowTF = new JTextField(), gbc);
        rowTF.addActionListener(this);
        rowTF.setColumns(8);
        gbc.gridwidth = 3;
        gbc.weightx = 1.f;
        add(new JLabel("colums", JLabel.RIGHT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 0.f;
        add(colTF = new JTextField(), gbc);
        colTF.addActionListener(this);
        colTF.setColumns(8);
        gbc.insets.top = 0;

        table = new JTable() {
                    public void validate() {
                        tableFitsInPane();
                        super.validate();
                    }
                };
        table.setDefaultRenderer(Complex.class, new ComplexTableCellRenderer());
        table.setDefaultEditor(Complex.class, new ComplexTableCellEditor());
        table.setCellSelectionEnabled(true);
        sPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        columnModel = (DefaultTableColumnModel) table.getColumnModel();

        lineNumberModel = new LineNumberModel();
        rowHeaderTable = new JTable(lineNumberModel);

        Color color = table.getTableHeader().getBackground();
        rowHeaderTable.setBackground(color);
        rowHeaderTable.setIntercellSpacing(new Dimension(20, 0));
        rowHeaderTable.setEnabled(false);
        rowHeaderTable.setPreferredScrollableViewportSize(new Dimension(60, 20));
        sPane.setRowHeaderView(rowHeaderTable);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(sPane, gbc);
    }

    /**
     * Sets the relation between this object and the used Matrix
     * object. The specified AbstractMatrix object defines the kind of matrix
     * working together and provides the needed reference to this class. The
     * AbstractMatrixTableModel object transfers the right TableModel storing
     * all data for the matrix.
     *
     * @param m DOCUMENT ME!
     * @param model DOCUMENT ME!
     */
    public void setMatrix(AbstractMatrix m, AbstractMatrixTableModel model) {
        matrix = m;

        tableModel = model;
        tableModel.setMatrix(matrix);
        tableModel.addTableModelListener(this);
        table.setModel(tableModel);

        int rows = tableModel.getRowCount();
        int cols = tableModel.getColumnCount();

        rowTF.setText(String.valueOf(rows));
        colTF.setText(String.valueOf(cols));

        if (((rows * ROW_HEIGHT) + REST_HEIGHT) > MAX_VISIBLE_HEIGHT) {
            sPane.setPreferredSize(new Dimension(100, MAX_VISIBLE_HEIGHT));
        } else {
            sPane.setPreferredSize(new Dimension(100,
                    (rows * ROW_HEIGHT) + REST_HEIGHT));
        }

        lineNumberModel.setRowCount(rows);
        noCells();
        tableFitsInPane();
    }

    /**
     * DOCUMENT ME!
     */
    private void noCells() {
        if (rowTF.getText().equals("0") || colTF.getText().equals("0")) {
            table.getTableHeader().setVisible(false);
            rowHeaderTable.setVisible(false);
            sPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            sPane.setPreferredSize(new Dimension(100, REST_HEIGHT));
        } else {
            table.getTableHeader().setVisible(true);
            rowHeaderTable.setVisible(true);
            sPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

            if (((tableModel.getRowCount() * ROW_HEIGHT) + REST_HEIGHT) > MAX_VISIBLE_HEIGHT) {
                sPane.setPreferredSize(new Dimension(100, MAX_VISIBLE_HEIGHT));
            } else {
                sPane.setPreferredSize(new Dimension(100,
                        (tableModel.getRowCount() * ROW_HEIGHT) + REST_HEIGHT));
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void tableFitsInPane() {
        final int columnCount = tableModel.getColumnCount();

        if (columnCount > 0) {
            int tableWidth = table.getPreferredSize().width;
            Insets ins = sPane.getViewport().getInsets();
            int paneWidth = sPane.getViewport().getWidth() - ins.left -
                ins.right;

            if (tableWidth < paneWidth) {
                columnWidth = paneWidth / columnCount;

                int rest = paneWidth - (columnWidth * columnCount);

                for (int i = 0; i < (columnCount - 1); i++)
                    columnModel.getColumn(i).setPreferredWidth(columnWidth);

                columnModel.getColumn(columnCount - 1)
                           .setPreferredWidth(columnWidth + rest);
            }

            for (int i = 0; i < columnCount; i++)
                columnModel.getColumn(i)
                           .addPropertyChangeListener(columnListener);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void validate() {
        super.validate();
        tableFitsInPane();
    }

    /**
     * Sets the relation between this object and the specified
     * IntegerMatrix object.
     *
     * @param m DOCUMENT ME!
     */
    public void setMatrix(IntegerMatrix m) {
        setMatrix(m, new IntegerMatrixTableModel());
    }

    /**
     * Sets the relation between this object and the specified
     * RealMatrix object.
     *
     * @param m DOCUMENT ME!
     */
    public void setMatrix(DoubleMatrix m) {
        setMatrix(m, new DoubleMatrixTableModel());
    }

    /**
     * Sets the relation between this object and the specified
     * ComplexMatrix object.
     *
     * @param m DOCUMENT ME!
     */
    public void setMatrix(ComplexMatrix m) {
        setMatrix(m, new ComplexMatrixTableModel());
    }

    /**
     * Updates the related Matrix object whenever TableModel datas were
     * changed.
     *
     * @param ev DOCUMENT ME!
     */
    public void tableChanged(TableModelEvent ev) {
        matrix = tableModel.getMatrix();
    }

    /**
     * Updates the count of rows respectively columns of the displayed
     * table and the related matrix whenever the responsible textfield was
     * edited.
     *
     * @param ev DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent ev) {
        JTextField source = (JTextField) ev.getSource();
        int n = Integer.parseInt(source.getText());

        if (source == rowTF) {
            if (n == tableModel.getRowCount()) {
                return;
            }

            //todo the code below is really unoptimized!!!!
            //if we could resize the matrix internally that would be much better, especially using arrayCopy
            if (matrix instanceof IntegerMatrix) {
                IntegerMatrix newMatrix;
                newMatrix = new IntegerMatrix(n, matrix.numColumns());

                int rows;

                if (n < matrix.numRows()) {
                    rows = Math.min(n, matrix.numRows());
                } else {
                    rows = n;
                }

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < matrix.numColumns(); j++) {
                        newMatrix.setElement(i, j,
                            ((IntegerMatrix) matrix).getPrimitiveElement(i, j));
                    }
                }

                lineNumberModel.setRowCount(n);
                tableModel.setMatrix(newMatrix);
            } else if (matrix instanceof DoubleMatrix) {
                DoubleMatrix newMatrix;
                newMatrix = new DoubleMatrix(n, matrix.numColumns());

                int rows;

                if (n < matrix.numRows()) {
                    rows = Math.min(n, matrix.numRows());
                } else {
                    rows = n;
                }

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < matrix.numColumns(); j++) {
                        newMatrix.setElement(i, j,
                            ((DoubleMatrix) matrix).getPrimitiveElement(i, j));
                    }
                }

                lineNumberModel.setRowCount(n);
                tableModel.setMatrix(newMatrix);
            } else if (matrix instanceof ComplexMatrix) {
                ComplexMatrix newMatrix;
                newMatrix = new ComplexMatrix(n, matrix.numColumns());

                int rows;

                if (n < matrix.numRows()) {
                    rows = Math.min(n, matrix.numRows());
                } else {
                    rows = n;
                }

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < matrix.numColumns(); j++) {
                        newMatrix.setElement(i, j,
                            ((ComplexMatrix) matrix).getElement(i, j));
                    }
                }

                lineNumberModel.setRowCount(n);
                tableModel.setMatrix(newMatrix);
            }
        } else if (source == colTF) {
            if (n == tableModel.getColumnCount()) {
                return;
            }

            //todo the code below is really unoptimized!!!!
            //if we could resize the matrix internally that would be much better, especially using arrayCopy
            if (matrix instanceof IntegerMatrix) {
                IntegerMatrix newMatrix;
                newMatrix = new IntegerMatrix(matrix.numRows(), n);

                int cols;

                if (n < matrix.numColumns()) {
                    cols = Math.min(n, matrix.numColumns());
                } else {
                    cols = n;
                }

                for (int i = 0; i < matrix.numRows(); i++) {
                    for (int j = 0; j < cols; j++) {
                        newMatrix.setElement(i, j,
                            ((IntegerMatrix) matrix).getPrimitiveElement(i, j));
                    }
                }

                tableModel.setMatrix(newMatrix);
            } else if (matrix instanceof DoubleMatrix) {
                DoubleMatrix newMatrix;
                newMatrix = new DoubleMatrix(matrix.numRows(), n);

                int cols;

                if (n < matrix.numColumns()) {
                    cols = Math.min(n, matrix.numColumns());
                } else {
                    cols = n;
                }

                for (int i = 0; i < matrix.numRows(); i++) {
                    for (int j = 0; j < cols; j++) {
                        newMatrix.setElement(i, j,
                            ((DoubleMatrix) matrix).getPrimitiveElement(i, j));
                    }
                }

                tableModel.setMatrix(newMatrix);
            } else if (matrix instanceof ComplexMatrix) {
                ComplexMatrix newMatrix;
                newMatrix = new ComplexMatrix(matrix.numRows(), n);

                int cols;

                if (n < matrix.numColumns()) {
                    cols = Math.min(n, matrix.numColumns());
                } else {
                    cols = n;
                }

                for (int i = 0; i < matrix.numRows(); i++) {
                    for (int j = 0; j < cols; j++) {
                        newMatrix.setElement(i, j,
                            ((ComplexMatrix) matrix).getElement(i, j));
                    }
                }

                tableModel.setMatrix(newMatrix);
            }
        }

        noCells();
        tableFitsInPane();
    }
}
