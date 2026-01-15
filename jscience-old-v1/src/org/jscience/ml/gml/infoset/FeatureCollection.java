/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

/**
 * Defines the interface every FeatureCollection must implement. This class may
 * contain properties and other features (!). Note that a feature collection
 * may not contain features directly but only via properties (such as
 * featureMember).
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface FeatureCollection extends Feature,
    FeatureOwner // indirect ownership (via featureMember properties)
 {
    // no additional methods
}
