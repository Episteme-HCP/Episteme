/*
 * PlainDocument extension for numeric textfields.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor.util;

import java.awt.*;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


/**
 * PlainDocument extension for numeric textfields.
 */
public class DoubleDocument extends PlainDocument {
    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     * @param string DOCUMENT ME!
     * @param attr DOCUMENT ME!
     *
     * @throws BadLocationException DOCUMENT ME!
     */
    public void insertString(int offset, String string, AttributeSet attr)
        throws BadLocationException {
        int len;
        String newValue;
        String currentContent;
        StringBuffer currentBuffer;

        if (string == null) {
            return;
        } else {
            len = getLength();

            if (len == 0) {
                newValue = string;
            } else {
                currentContent = getText(0, len);
                currentBuffer = new StringBuffer(currentContent);
                currentBuffer.insert(offset, string);
                newValue = currentBuffer.toString();
            }

            try {
                Double.parseDouble(newValue);
                super.insertString(offset, string, attr);
            } catch (NumberFormatException exception) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
