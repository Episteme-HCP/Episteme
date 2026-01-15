package org.jscience.earth.geosphere.ground;

/**
 * Describes the climatic event of the same name.
 */
import org.jscience.earth.geosphere.GeosphereBranchGroup;
import org.jscience.earth.geosphere.LandFeature;

import javax.media.j3d.Bounds;
import javax.media.j3d.Transform3D;


//accounts for geyser, fumarole (also see GazSource), hot spring (see lake), black smoker, cold seep, lost city
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class HydrothermalVent extends LandFeature {
/**
     * Creates a new HydrothermalVent object.
     *
     * @param position             DOCUMENT ME!
     * @param bounds               DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public HydrothermalVent(Transform3D position, Bounds bounds,
        GeosphereBranchGroup geosphereBranchGroup) {
        super(position, bounds, geosphereBranchGroup);
    }
}
