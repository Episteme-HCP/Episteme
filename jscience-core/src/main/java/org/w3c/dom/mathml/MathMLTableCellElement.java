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

package org.w3c.dom.mathml;

/**
 * This interface represents the MathML MathMLTableCellElement element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLTableCellElement extends MathMLPresentationContainer {
    /**
 * This interface represents the MathML MathMLTableCellElement element.
     *
     * @return the requested attribute or element
     */
    public String getRowspan();

    /**
 * This interface represents the MathML MathMLTableCellElement element.
     *
     * @param rowspan the value to set
     */
    public void setRowspan(String rowspan);

    /**
 * This interface represents the MathML MathMLTableCellElement element.
     *
     * @return the requested attribute or element
     */
    public String getColumnspan();

    /**
 * This interface represents the MathML MathMLTableCellElement element.
     *
     * @param columnspan the value to set
     */
    public void setColumnspan(String columnspan);

    /**
 * This interface represents the MathML MathMLTableCellElement element.
     *
     * @return the requested attribute or element
     */
    public String getRowalign();

    /**
 * This interface represents the MathML MathMLTableCellElement element.
     *
     * @param rowalign the value to set
     */
    public void setRowalign(String rowalign);

    /**
 * This interface represents the MathML MathMLTableCellElement element.
     *
     * @return the requested attribute or element
     */
    public String getColumnalign();

    /**
 * This interface represents the MathML MathMLTableCellElement element.
     *
     * @param columnalign the value to set
     */
    public void setColumnalign(String columnalign);

    /**
 * This interface represents the MathML MathMLTableCellElement element.
     *
     * @return the requested attribute or element
     */
    public String getGroupalign();

    /**
 * This interface represents the MathML MathMLTableCellElement element.
     *
     * @param groupalign the value to set
     */
    public void setGroupalign(String groupalign);

    /**
 * This interface represents the MathML MathMLTableCellElement element.
     *
     * @return the requested attribute or element
     */
    public boolean getHasaligngroups();

    /**
 * This interface represents the MathML MathMLTableCellElement element.
     *
     * @return the requested attribute or element
     */
    public String getCellindex();
}
;
