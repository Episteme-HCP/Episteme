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
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface MathMLMultiScriptsElement extends MathMLPresentationElement {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSubscriptshift();

    /**
     * DOCUMENT ME!
     *
     * @param subscriptshift DOCUMENT ME!
     */
    public void setSubscriptshift(String subscriptshift);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSuperscriptshift();

    /**
     * DOCUMENT ME!
     *
     * @param superscriptshift DOCUMENT ME!
     */
    public void setSuperscriptshift(String superscriptshift);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getBase();

    /**
     * DOCUMENT ME!
     *
     * @param base DOCUMENT ME!
     */
    public void setBase(MathMLElement base);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLNodeList getPrescripts();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLNodeList getScripts();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumprescriptcolumns();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumscriptcolumns();

    /**
     * DOCUMENT ME!
     *
     * @param colIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getPreSubScript(int colIndex);

    /**
     * DOCUMENT ME!
     *
     * @param colIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getSubScript(int colIndex);

    /**
     * DOCUMENT ME!
     *
     * @param colIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getPreSuperScript(int colIndex);

    /**
     * DOCUMENT ME!
     *
     * @param colIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getSuperScript(int colIndex);

    /**
     * DOCUMENT ME!
     *
     * @param colIndex DOCUMENT ME!
     * @param newScript DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLElement insertPreSubScriptBefore(int colIndex,
        MathMLElement newScript) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param colIndex DOCUMENT ME!
     * @param newScript DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLElement setPreSubScriptAt(int colIndex, MathMLElement newScript)
        throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param colIndex DOCUMENT ME!
     * @param newScript DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLElement insertSubScriptBefore(int colIndex,
        MathMLElement newScript) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param colIndex DOCUMENT ME!
     * @param newScript DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLElement setSubScriptAt(int colIndex, MathMLElement newScript)
        throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param colIndex DOCUMENT ME!
     * @param newScript DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLElement insertPreSuperScriptBefore(int colIndex,
        MathMLElement newScript) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param colIndex DOCUMENT ME!
     * @param newScript DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLElement setPreSuperScriptAt(int colIndex,
        MathMLElement newScript) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param colIndex DOCUMENT ME!
     * @param newScript DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLElement insertSuperScriptBefore(int colIndex,
        MathMLElement newScript) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param colIndex DOCUMENT ME!
     * @param newScript DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLElement setSuperScriptAt(int colIndex, MathMLElement newScript)
        throws DOMException;
}
;
