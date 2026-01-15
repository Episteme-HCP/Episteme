package org.jscience.earth.geosphere.ground;

/**
 * Describes a floating block of ice.
 */
import org.jscience.earth.geosphere.GeosphereBranchGroup;
import org.jscience.earth.geosphere.LandFeature;

import javax.media.j3d.Bounds;
import javax.media.j3d.Transform3D;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Iceberg extends LandFeature {
/**
     * Creates a new Iceberg object.
     *
     * @param position             DOCUMENT ME!
     * @param bounds               DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public Iceberg(Transform3D position, Bounds bounds,
        GeosphereBranchGroup geosphereBranchGroup) {
        super(position, bounds, geosphereBranchGroup);
    }
}
