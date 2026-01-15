package org.jscience.mathematics.algebraic.matrices.gui;

import org.jscience.mathematics.algebraic.numbers.Complex;

import java.awt.*;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class ComplexTableCellEditor extends DefaultCellEditor {
/**
     * Creates a new ComplexTableCellEditor object.
     */
    public ComplexTableCellEditor() {
        super(new JTextField());
        editorComponent.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param isSelected DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) {
        System.out.println("-- getTableCellEditorComponent");

        return super.getTableCellEditorComponent(table, value.toString(),
            isSelected, row, column);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getCellEditorValue() {
        System.out.println("-- getCellEditorValue()");

        String s = (String) super.getCellEditorValue();

        return new Complex(s);
    }
}
