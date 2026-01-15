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
