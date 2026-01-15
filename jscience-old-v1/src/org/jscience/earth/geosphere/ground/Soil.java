package org.jscience.earth.geosphere.ground;

/**
 */
import org.jscience.earth.geosphere.GeosphereBranchGroup;
import org.jscience.earth.geosphere.LandFeature;

import javax.media.j3d.Bounds;
import javax.media.j3d.Transform3D;


//the basic class for soils (whether flat or with caves, mines, holes)
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class Soil extends LandFeature {
    // grass, rock, sand, mud, dust, salty lake (without water), ice
    /**
     * Creates a new Soil object.
     *
     * @param position DOCUMENT ME!
     * @param bounds DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public Soil(Transform3D position, Bounds bounds,
        GeosphereBranchGroup geosphereBranchGroup) {
        super(position, bounds, geosphereBranchGroup);
    }
}
