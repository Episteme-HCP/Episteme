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
 * /IExtendableSchemaElement.java
 * 
 * (c) by Dirk Lehmann
 * ====================================================================
 */


package org.jscience.ml.om;


/**
 * Some schema elements (e.g. IFinding, ITarget) have to be extended
 * in several different types (e.g. DeepSkyTarget, VariableStarTarget..)
 * representing several different astronomical objects.<br>
 * These interfaces extend IExtendableSchemaElement, as it provides access
 * to e.g. XML XSI Type information.<br>
 *
 * @author doergn@users.sourceforge.net
 * @since 1.0
 */
public interface IExtendableSchemaElement {

    // ---------
    // Constants ---------------------------------------------------------
    // ---------

    /**
     * Constant for XML Schema Instance type.<br>
     * As target elements my differ from type to type (i.e. DeepSkyTarget,
     * VariableStarTarget...) this constant can identifies a type.<br>
     * Example:<br>
     * &lt;target xsi:type="fgca:deepSkyGX"&gt;<i>More stuff goes here</i>&lt;/target&gt;
     */
    public static final String XML_XSI_TYPE = "xsi:type";

    // --------------
    // Public Methods ----------------------------------------------------
    // --------------

    // -------------------------------------------------------------------

    /**
     * Returns the XML schema instance type of the implementation.<br>
     * Example:<br>
     * <target xsi:type="myOwnTarget"><br>
     * </target><br>
     *
     * @return The xsi:type value of this implementation
     */
    public String getXSIType();

}
