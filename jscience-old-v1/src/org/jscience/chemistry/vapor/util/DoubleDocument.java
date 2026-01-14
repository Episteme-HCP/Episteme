/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
