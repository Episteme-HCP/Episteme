/*
 Behaviour to give back info about a star clicked on*/
package org.jscience.tests.astronomy.milkyway.hipparcos;


//this package is rebundled from Hipparcos Java package from
//William O'Mullane
//http://astro.estec.esa.nl/Hipparcos/hipparcos_java.html
//mailto:hipparcos@astro.estec.esa.nl
import com.sun.j3d.utils.behaviors.picking.PickMouseBehavior;
import com.sun.j3d.utils.behaviors.picking.PickObject;

import org.jscience.astronomy.catalogs.hipparcos.Browser;
import org.jscience.astronomy.catalogs.hipparcos.Star3D;

import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class ClickBehaviour extends PickMouseBehavior {
    /** DOCUMENT ME! */
    int pickMode = PickObject.USE_BOUNDS;

    /** DOCUMENT ME! */
    org.jscience.tests.astronomy.milkyway.hipparcos.StarPanel starPanel;

/**
     * Creates a behavior that waits for user mouse events for the scene graph.
     *
     * @param root   DOCUMENT ME!
     * @param canvas DOCUMENT ME!
     * @param bounds DOCUMENT ME!
     */
    public ClickBehaviour(BranchGroup root, Canvas3D canvas, Bounds bounds) {
        super(canvas, root, bounds);
        this.setSchedulingBounds(bounds);
    }

/**
     * Creates a click behavior that waits for user mouse events for the scene
     * graph.
     *
     * @param root     Root of your scene graph.
     * @param canvas   Java 3D drawing canvas.
     * @param bounds   Bounds of your scene.
     * @param pickMode specifys PickObject.USE_BOUNDS or
     *                 PickObject.USE_GEOMETRY. Note: If pickMode is set to
     *                 PickObject.USE_GEOMETRY, all geometry object in the scene graph
     *                 that allows pickable must have its ALLOW_INTERSECT bit set.
     */
    public ClickBehaviour(BranchGroup root, Canvas3D canvas, Bounds bounds,
        int pickMode) {
        super(canvas, root, bounds);
        this.setSchedulingBounds(bounds);
        this.pickMode = pickMode;
    }

    /**
     * Sets the STarPanel - the one ot be update when a star is clicked
     * on
     *
     * @param starPanel - the StarPanel to be used.
     */
    public void setStarPanel(StarPanel starPanel) {
        this.starPanel = starPanel;
        System.out.println("Panel set on clicker");
    }

    /**
     * Sets the pickMode component of this ClickBehaviour to the value
     * of the passed pickMode.
     *
     * @param pickMode the pickMode to be copied.
     */
    public void setPickMode(int pickMode) {
        this.pickMode = pickMode;
    }

    /**
     * Return the pickMode component of this ClickBehaviour.
     *
     * @return DOCUMENT ME!
     */
    public int getPickMode() {
        return pickMode;
    }

    /**
     * Update the scene to manipulate any nodes. This is not meant to
     * be called by users. Behavior automatically calls this. You can call
     * this only if you know what you are doing.
     *
     * @param xpos Current mouse X pos.
     * @param ypos Current mouse Y pos.
     */
    public void updateScene(int xpos, int ypos) {
        Star3D tg = null;

        if (!mevent.isMetaDown() && !mevent.isAltDown()) {
            tg = (Star3D) pickScene.pickNode(pickScene.pickClosest(xpos, ypos,
                        pickMode), PickObject.PRIMITIVE);

            // Make sure the selection exists and is movable. 
            if (tg != null) {
                if (starPanel == null) {
                    Browser.goTo(tg.getStar().makeUrl());
                } else {
                    starPanel.setStar(tg);
                }
            }
        }
    }
}
