package org.jscience.earth.geosphere.ground;

/**
 * Describes water source that comes out of the soil (whether onland or
 * offshore), may be hot water. This accounts for natural fuel sources, mud,
 * lava, inondations
 */
import org.jscience.earth.geosphere.GeosphereBranchGroup;
import org.jscience.earth.geosphere.LandFeature;

import javax.media.j3d.Bounds;
import javax.media.j3d.Transform3D;


//and its path over the ground
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class River extends LandFeature {
/**
     * Creates a new River object.
     *
     * @param position             DOCUMENT ME!
     * @param bounds               DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public River(Transform3D position, Bounds bounds,
        GeosphereBranchGroup geosphereBranchGroup) {
        super(position, bounds, geosphereBranchGroup);
    }
}
