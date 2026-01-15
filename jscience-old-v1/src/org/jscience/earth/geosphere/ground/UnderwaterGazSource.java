package org.jscience.earth.geosphere.ground;

/**
 * Describes an underwater gaz source (that can not be ignited) but pushes
 * bubbles to the surface or a vapor source (gaz + vapor) but not a geyser
 * (which is a recurent process, not a source).
 */
import org.jscience.earth.geosphere.GeosphereBranchGroup;
import org.jscience.earth.geosphere.LandFeature;

import javax.media.j3d.Bounds;
import javax.media.j3d.Transform3D;


//may have coloration
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class UnderwaterGazSource extends LandFeature {
/**
     * Creates a new UnderwaterGazSource object.
     *
     * @param position             DOCUMENT ME!
     * @param bounds               DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public UnderwaterGazSource(Transform3D position, Bounds bounds,
        GeosphereBranchGroup geosphereBranchGroup) {
        super(position, bounds, geosphereBranchGroup);
    }
}
