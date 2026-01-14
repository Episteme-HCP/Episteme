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

import org.w3c.dom.Element;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface MathMLElement extends Element {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getClassName();

    /**
     * DOCUMENT ME!
     *
     * @param className DOCUMENT ME!
     */
    public void setClassName(String className);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMathElementStyle();

    /**
     * DOCUMENT ME!
     *
     * @param mathElementStyle DOCUMENT ME!
     */
    public void setMathElementStyle(String mathElementStyle);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getId();

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     */
    public void setId(String id);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXref();

    /**
     * DOCUMENT ME!
     *
     * @param xref DOCUMENT ME!
     */
    public void setXref(String xref);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getHref();

    /**
     * DOCUMENT ME!
     *
     * @param href DOCUMENT ME!
     */
    public void setHref(String href);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLMathElement getOwnerMathElement();
}
;
