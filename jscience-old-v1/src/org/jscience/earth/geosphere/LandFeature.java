package org.jscience.earth.geosphere;

/**
 * Something that can be recognized (visible, acts as a unit) and lasts for
 * long. Can actually be quite large.
 */
import javax.media.j3d.Bounds;
import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class LandFeature extends Group {
    /** DOCUMENT ME! */
    private Transform3D position;

    /** DOCUMENT ME! */
    private GeosphereBranchGroup geosphereBranchGroup; //the branchGroup this Event is added to

/**
     * Creates a new LandFeature object.
     *
     * @param position             DOCUMENT ME!
     * @param bounds               DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public LandFeature(Transform3D position, Bounds bounds,
        GeosphereBranchGroup geosphereBranchGroup) {
        TransformGroup transformGroup;

        transformGroup = new TransformGroup(position);
        transformGroup.addChild(this);
        setBounds(bounds);
        geosphereBranchGroup.addChild(transformGroup);
    }

    //given place
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Transform3D getPosition() {
        Transform3D transform3D;

        transform3D = new Transform3D();

        return ((TransformGroup) getParent()).getTransform(transform3D);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GeosphereBranchGroup getGeosphereBranchGroup() {
        return getParent().getParent();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract ClimaticEvent fireEvent();
}
