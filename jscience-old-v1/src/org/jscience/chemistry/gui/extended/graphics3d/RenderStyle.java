package org.jscience.chemistry.gui.extended.graphics3d;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public interface RenderStyle {
    /** DOCUMENT ME! */
    public static final int NONE = -1;

    /** DOCUMENT ME! */
    public static final int INVISIBLE = 0;

    /** DOCUMENT ME! */
    public static final int CPK = 1;

    /** DOCUMENT ME! */
    public static final int BALL_AND_STICK = 2;

    /** DOCUMENT ME! */
    public static final int STICK = 3;

    /** DOCUMENT ME! */
    public static final int WIRE = 4;

    /**
     * DOCUMENT ME!
     *
     * @param style DOCUMENT ME!
     */
    public void setStyle(int style);
}
