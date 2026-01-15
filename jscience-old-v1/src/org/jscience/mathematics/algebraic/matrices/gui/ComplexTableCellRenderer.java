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

import org.jscience.mathematics.algebraic.numbers.Complex;

import java.awt.*;

import java.text.DecimalFormat;

import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;


/**
 * This special TableCellRenderer made for ComplexMatrix and ComplexVector
 * changes especially those values, which are too long to fit in the cell. The
 * decimal places of both parts (the real part and the imaginary part) the
 * complex number consits of were cut as much as necessary but as few as
 * possible. Trunks and decimal powers are even displayed, to give the user an
 * imagine of the range the value has. To show the user that a value was
 * changed in its precision, the text color changes to green.
 */
public class ComplexTableCellRenderer extends JLabel
    implements TableCellRenderer {
    /** DOCUMENT ME! */
    private Color lightBlue;

    /** DOCUMENT ME! */
    private Color darkBlue;

    /** DOCUMENT ME! */
    private Color darkGreen;

    /** DOCUMENT ME! */
    private Color lightGreen;

    /** DOCUMENT ME! */
    Border b;

    /** DOCUMENT ME! */
    String complexStr;

    /** DOCUMENT ME! */
    Complex complexVal;

    /** DOCUMENT ME! */
    StringBuffer buffer;

    /** DOCUMENT ME! */
    StringTokenizer tokenizer;

    /** DOCUMENT ME! */
    FontMetrics fontMetrics;

    /** DOCUMENT ME! */
    int columnWidth;

    /** DOCUMENT ME! */
    int stringWidth;

    /** DOCUMENT ME! */
    boolean isCut = false;

    /** DOCUMENT ME! */
    boolean noMoreDigs = false;

/**
     * Creates a new instance of a ComplexTableCellRenderer.
     */
    public ComplexTableCellRenderer() {
        lightBlue = new Color(160, 160, 255);
        darkBlue = new Color(64, 64, 128);
        lightGreen = new Color(192, 255, 192);
        darkGreen = new Color(32, 96, 32);
        b = BorderFactory.createEmptyBorder(1, 1, 1, 1);
        setOpaque(true);
        setBorder(b);
    }

    /**
     * Returns the Component which renders the cells of the specified
     * table.
     *
     * @param table DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param isSelected DOCUMENT ME!
     * @param hasFocus DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return <code>this</code>
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
        isCut = false;
        noMoreDigs = false;
        complexVal = (Complex) value;

        DecimalFormat format = new DecimalFormat("0.");
        String re = format.format(complexVal.real());
        String im = format.format(complexVal.imag());
        complexStr = value.toString();
        fontMetrics = table.getFontMetrics(table.getFont());
        stringWidth = SwingUtilities.computeStringWidth(fontMetrics, complexStr);
        columnWidth = table.getColumnModel().getColumn(column).getWidth() - 2;
        setFont(table.getFont());
        setForeground(table.getForeground());
        setBackground(table.getBackground());

        if (stringWidth > columnWidth) {
            setForeground(darkGreen);
            isCut = true;
            tokenizer = new StringTokenizer(complexStr, ".+-Ei", true);

            String t;
            buffer = new StringBuffer("");

            int beginIndex = 0;
            int endIndex = -1;
            int cutIndex1 = -1;
            int cutEnd1 = -1;
            int cutIndex2 = -1;
            int cutEnd2 = -1;

            while (tokenizer.hasMoreTokens()) {
                t = tokenizer.nextToken();
                beginIndex = endIndex + 1;
                endIndex = beginIndex + (t.length() - 1);

                if (t.equals(".")) {
                    buffer.append(t);
                    t = tokenizer.nextToken();
                    beginIndex = endIndex + 1;
                    endIndex = beginIndex + (t.length() - 1);

                    if ((cutIndex1 < 0) && (cutEnd1 < 0)) {
                        cutEnd1 = beginIndex;
                        cutIndex1 = endIndex;
                    } else {
                        cutEnd2 = beginIndex;
                        cutIndex2 = endIndex;
                    }
                }

                buffer.append(t);
            }

            if (cutIndex1 >= 0) {
                int possibleCut1 = (cutIndex1 - cutEnd1) + 1;

                if (cutIndex2 >= 0) {
                    int possibleCut2 = (cutIndex2 - cutEnd2) + 1;

                    while ((stringWidth >= columnWidth) &&
                            (noMoreDigs == false)) {
                        if (possibleCut1 >= possibleCut2) {
                            buffer.deleteCharAt(cutIndex1--);
                            cutIndex2--;
                            cutEnd2--;
                            possibleCut1--;
                        } else {
                            buffer.deleteCharAt(cutIndex2--);
                            possibleCut2--;
                        }

                        if ((possibleCut1 < 1) && (possibleCut2 < 1)) {
                            noMoreDigs = true;
                        }

                        stringWidth = SwingUtilities.computeStringWidth(fontMetrics,
                                buffer.toString());
                    }
                } else {
                    while ((stringWidth >= columnWidth) && (possibleCut1 > 0)) {
                        buffer.deleteCharAt(cutIndex1--);
                        possibleCut1--;
                        stringWidth = SwingUtilities.computeStringWidth(fontMetrics,
                                buffer.toString());
                    }
                }
            }

            complexStr = buffer.toString();
        }

        setText(complexStr);

        if (hasFocus) {
            if (isCut) {
                setForeground(lightGreen);
            } else {
                setForeground(Color.white);
            }

            setBackground(darkBlue);
        } else if (isSelected) {
            setBackground(lightBlue);
        }

        return this;
    }
}
