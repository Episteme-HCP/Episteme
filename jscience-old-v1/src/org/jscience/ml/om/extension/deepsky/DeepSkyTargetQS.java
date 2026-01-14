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
 * extension/deepSky/DeepSkyTargetQS.java
 * 
 * (c) by Dirk Lehmann
 * ====================================================================
 */


package org.jscience.ml.om.extension.deepsky;

import org.jscience.ml.om.IObserver;
import org.jscience.ml.om.util.SchemaException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * DeepSkyTargetQS extends the org.jscience.ml.om.extension.deepsky.DeepSkyTarget class.<br>
 * Its specialised for quasars.<br>
 *
 * @author doergn@users.sourceforge.net
 * @since 1.0
 */
public class DeepSkyTargetQS extends DeepSkyTarget {

    // ---------
    // Constants ---------------------------------------------------------
    // ---------

    // XSML schema instance value. Enables class/schema loaders to identify this
    // class
    private static final String XML_XSI_TYPE_VALUE = "fgca:deepSkyQS";

    // ------------
    // Constructors ------------------------------------------------------
    // ------------

    // ------------------------------------------------------------------- 

    /**
     * Constructs a new instance of a DeepSkyTargetQS from a given DOM
     * target Element.<br>
     * Normally this constructor is called by org.jscience.ml.om.util.SchemaLoader.
     * Please mind that Target has to have a <observer> element, or a <datasource>
     * element. If a <observer> element is set, a array with Observers
     * must be passed to check, whether the <observer> link is valid.
     *
     * @param observers     Array of IObserver that might be linked from this observation,
     *                      can be <code>NULL</code> if datasource element is set
     * @param targetElement The origin XML DOM <target> Element
     * @throws SchemaException if given targetElement was <code>null</code>
     */
    public DeepSkyTargetQS(Node targetElement,
                           IObserver[] observers)
            throws SchemaException {

        super(targetElement, observers);

    }


    // -------------------------------------------------------------------
    /**
     * Constructs a new instance of a DeepSkyTargetQS.
     *
     * @param name       The name of the quasar
     * @param datasource The datasource of the quasar
     */
    public DeepSkyTargetQS(String name, String datasource) {

        super(name, datasource);

    }


    // -------------------------------------------------------------------
    /**
     * Constructs a new instance of a DeepSkyTargetQS.
     *
     * @param name     The name of the quasar
     * @param observer The observer who is the originator of the quasar
     */
    public DeepSkyTargetQS(String name, IObserver observer) {

        super(name, observer);

    }

    // ------
    // Target ------------------------------------------------------------
    // ------

    // -------------------------------------------------------------------

    /**
     * Adds this Target to a given parent XML DOM Element.
     * The Target element will be set as a child element of
     * the passed element.
     *
     * @param parent The parent element for this Target
     * @return Returns the element given as parameter with this
     *         Target as child element.<br>
     *         Might return <code>null</code> if parent was <code>null</code>.
     * @see org.w3c.dom.Element
     */
    public Element addToXmlElement(Element element) {

        if (element == null) {
            return null;
        }

        super.createXmlDeepSkyTargetElement(element, DeepSkyTargetQS.XML_XSI_TYPE_VALUE);

        return element;

    }

    // ------------------------
    // IExtendableSchemaElement ------------------------------------------
    // ------------------------

    // -------------------------------------------------------------------

    /**
     * Returns the XML schema instance type of the implementation.<br>
     * Example:<br>
     * <target xsi:type="myOwnTarget"><br>
     * </target><br>
     *
     * @return The xsi:type value of this implementation
     */
    public String getXSIType() {

        return DeepSkyTargetQS.XML_XSI_TYPE_VALUE;

    }

}
