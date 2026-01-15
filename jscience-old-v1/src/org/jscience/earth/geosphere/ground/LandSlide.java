package org.jscience.earth.geosphere.ground;

/**
 * Not really a mud river flow but rather a part of the soil that just falls
 * bellow, usually happens with major precipitations.
 */
import org.jscience.earth.geosphere.ClimaticEvent;
import org.jscience.earth.geosphere.GeosphereBranchGroup;

import javax.media.j3d.Bounds;
import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;


//Describes the climatic event of the same name
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class LandSlide extends ClimaticEvent {
/**
     * Creates a new LandSlide object.
     *
     * @param time                 DOCUMENT ME!
     * @param position             DOCUMENT ME!
     * @param bounds               DOCUMENT ME!
     * @param duration             DOCUMENT ME!
     * @param strength             DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public LandSlide(long time, Transform3D position, Bounds bounds,
        long duration, int strength, GeosphereBranchGroup geosphereBranchGroup) {
        super(time, position, bounds, duration, strength, geosphereBranchGroup);
    }

/**
     * Creates a new LandSlide object.
     *
     * @param time                 DOCUMENT ME!
     * @param position             DOCUMENT ME!
     * @param bounds               DOCUMENT ME!
     * @param duration             DOCUMENT ME!
     * @param strength             DOCUMENT ME!
     * @param effect               DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public LandSlide(long time, Transform3D position, Bounds bounds,
        long duration, int strength, Group effect,
        GeosphereBranchGroup geosphereBranchGroup) {
        super(time, position, bounds, duration, strength, effect,
            geosphereBranchGroup);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Group getGroup() {
        return super.getGroup();
    }
}
