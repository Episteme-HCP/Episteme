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

import org.w3c.dom.DOMException;


/**
 * This interface represents the mmultiscripts element, which is used to 
 * attach multiple subscripts and superscripts to a base element.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MathMLMultiScriptsElement extends MathMLPresentationElement {
    /**
     * Returns the shift amount for subscripts.
     *
     * @return the subscriptshift attribute value.
     */
    public String getSubscriptshift();

    /**
     * Sets the shift amount for subscripts.
     *
     * @param subscriptshift the shift amount string.
     */
    public void setSubscriptshift(String subscriptshift);

    /**
     * Returns the shift amount for superscripts.
     *
     * @return the superscriptshift attribute value.
     */
    public String getSuperscriptshift();

    /**
     * Sets the shift amount for superscripts.
     *
     * @param superscriptshift the shift amount string.
     */
    public void setSuperscriptshift(String superscriptshift);

    /**
     * Returns the base element to which scripts are attached.
     *
     * @return the base MathMLElement.
     */
    public MathMLElement getBase();

    /**
     * Sets the base element to which scripts are attached.
     *
     * @param base the base MathMLElement to set.
     */
    public void setBase(MathMLElement base);

    /**
     * Returns the list of prescripts attached to the base.
     *
     * @return a MathMLNodeList of prescripts.
     */
    public MathMLNodeList getPrescripts();

    /**
     * Returns the list of postscripts (scripts) attached to the base.
     *
     * @return a MathMLNodeList of postscripts.
     */
    public MathMLNodeList getScripts();

    /**
     * Returns the number of columns of prescripts.
     *
     * @return the number of prescript columns.
     */
    public int getNumprescriptcolumns();

    /**
     * Returns the number of columns of postscripts (scripts).
     *
     * @return the number of script columns.
     */
    public int getNumscriptcolumns();

    /**
     * Retrieves the prescript subscript at the specified column index.
     *
     * @param colIndex the column index (1-based).
     * @return the requested prescript subscript.
     */
    public MathMLElement getPreSubScript(int colIndex);

    /**
     * Retrieves the postscript subscript at the specified column index.
     *
     * @param colIndex the column index (1-based).
     * @return the requested postscript subscript.
     */
    public MathMLElement getSubScript(int colIndex);

    /**
     * Retrieves the prescript superscript at the specified column index.
     *
     * @param colIndex the column index (1-based).
     * @return the requested prescript superscript.
     */
    public MathMLElement getPreSuperScript(int colIndex);

    /**
     * Retrieves the postscript superscript at the specified column index.
     *
     * @param colIndex the column index (1-based).
     * @return the requested postscript superscript.
     */
    public MathMLElement getSuperScript(int colIndex);

    /**
     * Inserts a new prescript subscript before the specified column.
     *
     * @param colIndex the column index before which to insert.
     * @param newScript the new subscript element.
     * @return the newly inserted element.
     * @throws DOMException INDEX_SIZE_ERR: if the column index is out of range.
     */
    public MathMLElement insertPreSubScriptBefore(int colIndex,
        MathMLElement newScript) throws DOMException;

    /**
     * Sets the prescript subscript at the specified column.
     *
     * @param colIndex the column index.
     * @param newScript the replacement subscript element.
     * @return the newly set element.
     * @throws DOMException INDEX_SIZE_ERR: if the column index is out of range.
     */
    public MathMLElement setPreSubScriptAt(int colIndex, MathMLElement newScript)
        throws DOMException;

    /**
     * Inserts a new postscript subscript before the specified column.
     *
     * @param colIndex the column index before which to insert.
     * @param newScript the new subscript element.
     * @return the newly inserted element.
     * @throws DOMException INDEX_SIZE_ERR: if the column index is out of range.
     */
    public MathMLElement insertSubScriptBefore(int colIndex,
        MathMLElement newScript) throws DOMException;

    /**
     * Sets the postscript subscript at the specified column.
     *
     * @param colIndex the column index.
     * @param newScript the replacement subscript element.
     * @return the newly set element.
     * @throws DOMException INDEX_SIZE_ERR: if the column index is out of range.
     */
    public MathMLElement setSubScriptAt(int colIndex, MathMLElement newScript)
        throws DOMException;

    /**
     * Inserts a new prescript superscript before the specified column.
     *
     * @param colIndex the column index before which to insert.
     * @param newScript the new superscript element.
     * @return the newly inserted element.
     * @throws DOMException INDEX_SIZE_ERR: if the column index is out of range.
     */
    public MathMLElement insertPreSuperScriptBefore(int colIndex,
        MathMLElement newScript) throws DOMException;

    /**
     * Sets the prescript superscript at the specified column.
     *
     * @param colIndex the column index.
     * @param newScript the replacement superscript element.
     * @return the newly set element.
     * @throws DOMException INDEX_SIZE_ERR: if the column index is out of range.
     */
    public MathMLElement setPreSuperScriptAt(int colIndex,
        MathMLElement newScript) throws DOMException;

    /**
     * Inserts a new postscript superscript before the specified column.
     *
     * @param colIndex the column index before which to insert.
     * @param newScript the new superscript element.
     * @return the newly inserted element.
     * @throws DOMException INDEX_SIZE_ERR: if the column index is out of range.
     */
    public MathMLElement insertSuperScriptBefore(int colIndex,
        MathMLElement newScript) throws DOMException;

    /**
     * Sets the postscript superscript at the specified column.
     *
     * @param colIndex the column index.
     * @param newScript the replacement superscript element.
     * @return the newly set element.
     * @throws DOMException INDEX_SIZE_ERR: if the column index is out of range.
     */
    public MathMLElement setSuperScriptAt(int colIndex, MathMLElement newScript)
        throws DOMException;
}
;
