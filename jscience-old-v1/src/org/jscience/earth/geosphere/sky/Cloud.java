package org.jscience.earth.geosphere.sky;

/**
 * Describes the climatic event of the same name. Can also account for fog
 * although actual rendering might be quite strange.
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
public class Cloud extends ClimaticEvent {
    /** DOCUMENT ME! */
    public final static int CIRRUS = 1;

    /** DOCUMENT ME! */
    public final static int STRATUS = 2;

    /** DOCUMENT ME! */
    public final static int CUMULUS = 4;

    /** DOCUMENT ME! */
    public final static int CIRROCUMULUS = 8;

    /** DOCUMENT ME! */
    public final static int CIRROSTRATUS = 16;

    /** DOCUMENT ME! */
    public final static int CUMULONIMBUS = 32; //rainy clouds

    /** DOCUMENT ME! */
    public final static int NIMBOSTRATUS = 64;

    /** DOCUMENT ME! */
    public final static int ALTOSTRATUS = 128;

    /** DOCUMENT ME! */
    public final static int ALTOCUMULUS = 256;

    //just to mention these
    /**
     * Creates a new Cloud object.
     *
     * @param time DOCUMENT ME!
     * @param position DOCUMENT ME!
     * @param bounds DOCUMENT ME!
     * @param duration DOCUMENT ME!
     * @param strength DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public Cloud(long time, Transform3D position, Bounds bounds, long duration,
        int strength, GeosphereBranchGroup geosphereBranchGroup) {
        super(time, position, bounds, duration, strength, geosphereBranchGroup);
    }

/**
     * Creates a new Cloud object.
     *
     * @param time                 DOCUMENT ME!
     * @param position             DOCUMENT ME!
     * @param bounds               DOCUMENT ME!
     * @param duration             DOCUMENT ME!
     * @param strength             DOCUMENT ME!
     * @param effect               DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public Cloud(long time, Transform3D position, Bounds bounds, long duration,
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
