package org.jscience.earth.geosphere.ground;

/**
 * Describes the climatic event of the same name. Also accounts for dust.
 * Usually used in conjunction with fire, eruption, wind (sand wind or snow
 * wind for example).
 */
import org.jscience.earth.geosphere.ClimaticEvent;
import org.jscience.earth.geosphere.GeosphereBranchGroup;

import javax.media.j3d.Bounds;
import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Smoke extends ClimaticEvent {
/**
     * Creates a new Smoke object.
     *
     * @param time                 DOCUMENT ME!
     * @param position             DOCUMENT ME!
     * @param bounds               DOCUMENT ME!
     * @param duration             DOCUMENT ME!
     * @param strength             DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public Smoke(long time, Transform3D position, Bounds bounds, long duration,
        int strength, GeosphereBranchGroup geosphereBranchGroup) {
        super(time, position, bounds, duration, strength, geosphereBranchGroup);
    }

/**
     * Creates a new Smoke object.
     *
     * @param time                 DOCUMENT ME!
     * @param position             DOCUMENT ME!
     * @param bounds               DOCUMENT ME!
     * @param duration             DOCUMENT ME!
     * @param strength             DOCUMENT ME!
     * @param effect               DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public Smoke(long time, Transform3D position, Bounds bounds, long duration,
        int strength, Group effect, GeosphereBranchGroup geosphereBranchGroup) {
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
