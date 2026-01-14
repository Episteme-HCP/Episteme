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

/* ====================================================================
 * /IFinding.java
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.ml.om;

/**
 * A IFinding describes the impressions a observer had during a observation
 * of an astronomical object.<br>
 * A IFinding is a very general description without observation or object
 * typical parameters. Subclasses of IFinding have to provide a specialised
 * way to describe different astronmical observartions or obejects
 * (e.g. variable Stars, DeepSky, Planets...).
 *
 * @author doergn@users.sourceforge.net
 * @since 1.0
 */
public interface IFinding extends ISchemaElement, IExtendableSchemaElement {
    // ---------
    // Constants ---------------------------------------------------------
    // ---------
    /**
     * Constant for XML representation: IFinding element name.<br>
     * Example:<br>
     * &lt;result&gt;<i>More stuff goes here</i>&lt;/result&gt;
     */
    public static final String XML_ELEMENT_FINDING = "result";

    /**
     * Constant for XML representation: Description element name.<br>
     * Example:<br>
     * &lt;result&gt; <br>
     * <i>More stuff goes here</i> &lt;description&gt;<code>Finding description
     * goes here</code>&lt;/description&gt; <i>More stuff goes here</i>
     * &lt;/result&gt;
     */
    public static final String XML_ELEMENT_DESCRIPTION = "description";

    // --------------
    // Public Methods ---------------------------------------------------------
    // --------------

    // -------------------------------------------------------------------
    /**
     * Adds the IFinding implementation to an given parent XML DOM
     * Element. The finding Element will be set as a child element of the
     * passed Element.<br>
     * Example:<br>
     * &lt;parentElement&gt;<br>
     * &lt;result&gt;<br>
     * <i>More stuff goes here</i><br>
     * &lt;/result&gt;<br>
     * &lt;/parentElement&gt;
     *
     * @param parent The parent element for the IFinding implementation
     *
     * @return Returns the Element given as parameter with the IFinding
     *         implementation as child Element.
     *
     * @see org.w3c.dom.Element
     */
    public org.w3c.dom.Element addToXmlElement(org.w3c.dom.Element parent);

    // -------------------------------------------------------------------    
    /**
     * Returns the description of the IFinding. The string describes
     * the impressions the observer had during the observation of an object.
     *
     * @return The description of the finding.
     */
    public String getDescription();

    // -------------------------------------------------------------------    
    /**
     * Sets the description of the IFinding. The string should describe
     * the impressions the observer had during the observation of an object.
     *
     * @param description A description of the finding.
     */
    public void setDescription(String description);
}
