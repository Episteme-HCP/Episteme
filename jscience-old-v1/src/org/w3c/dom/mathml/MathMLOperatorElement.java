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
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface MathMLOperatorElement extends MathMLPresentationToken {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getForm();

    /**
     * DOCUMENT ME!
     *
     * @param form DOCUMENT ME!
     */
    public void setForm(String form);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFence();

    /**
     * DOCUMENT ME!
     *
     * @param fence DOCUMENT ME!
     */
    public void setFence(String fence);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSeparator();

    /**
     * DOCUMENT ME!
     *
     * @param separator DOCUMENT ME!
     */
    public void setSeparator(String separator);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLspace();

    /**
     * DOCUMENT ME!
     *
     * @param lspace DOCUMENT ME!
     */
    public void setLspace(String lspace);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getRspace();

    /**
     * DOCUMENT ME!
     *
     * @param rspace DOCUMENT ME!
     */
    public void setRspace(String rspace);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStretchy();

    /**
     * DOCUMENT ME!
     *
     * @param stretchy DOCUMENT ME!
     */
    public void setStretchy(String stretchy);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSymmetric();

    /**
     * DOCUMENT ME!
     *
     * @param symmetric DOCUMENT ME!
     */
    public void setSymmetric(String symmetric);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMaxsize();

    /**
     * DOCUMENT ME!
     *
     * @param maxsize DOCUMENT ME!
     */
    public void setMaxsize(String maxsize);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMinsize();

    /**
     * DOCUMENT ME!
     *
     * @param minsize DOCUMENT ME!
     */
    public void setMinsize(String minsize);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLargeop();

    /**
     * DOCUMENT ME!
     *
     * @param largeop DOCUMENT ME!
     */
    public void setLargeop(String largeop);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMovablelimits();

    /**
     * DOCUMENT ME!
     *
     * @param movablelimits DOCUMENT ME!
     */
    public void setMovablelimits(String movablelimits);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAccent();

    /**
     * DOCUMENT ME!
     *
     * @param accent DOCUMENT ME!
     */
    public void setAccent(String accent);
}
;
