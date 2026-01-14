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
 * /ISchemaElement.java
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.ml.om;

/**
 * The ISchemaElement is the root interface for almost all
 * astro XML schema elements. It contains only element
 * informations that are common for all (or almost all)
 * elements.
 *
 * @author doergn@users.sourceforge.net
 * @since 1.0
 */
public interface ISchemaElement {
    // ---------
    // Constants ---------------------------------------------------------
    // ---------
    /**
     * Constant for XML representation: ID attribute<br>
     * Almost all astro XML schema elements contain the ID attribute which is
     * used for linking the different elements logical together.<br>
     * Example:<br>
     * &lt;AnAstroXmlElement id=&quot;someID&quot;&gt;<br>
     * <i>More stuff goes here</i><br>
     * &lt;/AnAstroXmlElement&gt;<br>
     * <br>
     * &lt;AnotherAstroXmlElement id=&quot;someOtherID&quot;&gt; <br>
     * <i>More stuff goes here</i><br>
     * &lt;AnAstroXmlElement&gt;someID;&lt;/AnAstroXmlElement&gt;<br>
     * <br>
     * <i>More stuff goes here</i> &lt;/AnotherAstroXmlElement&gt;
     */
    public static final String XML_ELEMENT_ATTRIBUTE_ID = "id";

    // --------------
    // Public Methods ----------------------------------------------------
    // --------------

    // -------------------------------------------------------------------
    /**
     * Returns a unique ID of this schema element.<br>
     * The ID is used to link this element with other XML elements in the
     * schema.
     *
     * @return Returns a String representing a unique ID of this schema
     *         element.
     */
    public String getID();

    // -------------------------------------------------------------------
    /**
     * Returns a display name for this element.<br>
     * The method differs from the toString() method as toString() shows more
     * technical information about the element. Also the formating of
     * toString() can spread over several lines.<br>
     * This method returns a string (in one line) that can be used as
     * displayname in e.g. a UI dropdown box.
     *
     * @return Returns a String with a one line display name
     *
     * @see java.lang.Object.toString();
     */
    public String getDisplayName();
}
