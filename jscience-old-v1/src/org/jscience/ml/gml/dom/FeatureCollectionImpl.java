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

/*****************************************************************************
 * Name               Date          Change
 * --------------     ----------    ----------------
 * amilanovic         4-Sep-2001    Fixed bug #430473 so now this GML construct
 *                                  is preserving the information about the
 *                                  ordering of its child elements.
 * amilanovic         29-Mar-2002   Updated for the new package name.
 ***************************************************************************/
package org.jscience.ml.gml.dom;

import org.jscience.ml.gml.GMLSchema;
import org.jscience.ml.gml.infoset.Feature;
import org.jscience.ml.gml.infoset.FeatureCollection;
import org.jscience.ml.gml.infoset.FeatureOwner;
import org.jscience.ml.gml.infoset.Property;
import org.jscience.ml.gml.util.FeatureIterator;
import org.jscience.ml.gml.util.PropertyIterator;

import org.w3c.dom.Element;

import java.util.Vector;


/**
 * A DOM-based implementation of the FeatureCollection interface.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class FeatureCollectionImpl extends FeatureImpl
    implements FeatureCollection {
/**
     * Calls the superclass constructor.
     * This constructor should be called only from the method
     * Feature.newFeature().
     * <p/>
     * #see Feature.newFeature()
     */
    protected FeatureCollectionImpl(FeatureOwner owner, Element domElement,
        GMLDocument document) {
        super(owner, domElement, document);
    }

    // FeatureOwner interface implementation.
    /**
     * Provides access to features owned by this FeatureOwner. Works
     * only for features contained via featureMember element.
     *
     * @return FeatureIterator that can be used for iterating on features.
     */
    public FeatureIterator getFeatureIterator() {
        // read in all features
        Vector features = new Vector();
        PropertyIterator propertyIterator = getPropertyIterator();

        while (propertyIterator.hasNext()) {
            Property property = propertyIterator.nextProperty();
            String propertyTag = property.getXMLDescriptor().getLocalName();
            String propertyNamespace = property.getXMLDescriptor().getNamespace();

            if (GMLSchema.isFeatureMemberProperty(propertyTag, propertyNamespace)) {
                FeatureIterator memberFeatureIterator = property.getFeatureIterator();

                // !!! assuming there's one member feature
                Feature memberFeature = memberFeatureIterator.nextFeature();
                features.addElement(memberFeature);
            }
        } // end of while loop

        FeatureIterator featureIterator = new FeatureIteratorImpl(features.iterator());

        return featureIterator;
    }

    // redefined Feature methods
    /**
     * Returns a one-line string description of this object.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String result = "Feature collection " +
            getXMLDescriptor().getLocalName() + ' ' + getAttributeLine();

        return result;
    }

    /**
     * Refreshes the internal data cache from the DOM source tree. This
     * method should be called each time the underlying DOM structure has
     * changed.
     */
    protected void refreshInternals() {
        // first refresh the Feature internals
        super.refreshInternals();

        // do nothing extra
    }
}
