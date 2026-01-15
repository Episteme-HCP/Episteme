package org.jscience.earth.geosphere.ground;

/**
 * The basic class for moving water (not for river flows), this also accounts
 * for tsunami. Accounts for lakes and sea (salty) water waves.
 */
import org.jscience.earth.geosphere.ClimaticEvent;
import org.jscience.earth.geosphere.GeosphereBranchGroup;

import javax.media.j3d.Bounds;
import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;


//accounts for tsunami, freak wave, soliton, mascaret (tidal bore)
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class Wave extends ClimaticEvent {
/**
     * Creates a new Wave object.
     *
     * @param time                 DOCUMENT ME!
     * @param position             DOCUMENT ME!
     * @param bounds               DOCUMENT ME!
     * @param duration             DOCUMENT ME!
     * @param strength             DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public Wave(long time, Transform3D position, Bounds bounds, long duration,
        int strength, GeosphereBranchGroup geosphereBranchGroup) {
        super(time, position, bounds, duration, strength, geosphereBranchGroup);
    }

/**
     * Creates a new Wave object.
     *
     * @param time                 DOCUMENT ME!
     * @param position             DOCUMENT ME!
     * @param bounds               DOCUMENT ME!
     * @param duration             DOCUMENT ME!
     * @param strength             DOCUMENT ME!
     * @param effect               DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public Wave(long time, Transform3D position, Bounds bounds, long duration,
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
