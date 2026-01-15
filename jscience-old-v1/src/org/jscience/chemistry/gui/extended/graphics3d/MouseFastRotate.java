package org.jscience.chemistry.gui.extended.graphics3d;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;

import javax.media.j3d.TransformGroup;
import java.awt.event.MouseEvent;

/**
 * MouseFastRotate.java
 * <p/>
 * <p/>
 * Created: Sat Nov 21 19:45:34 1998
 *
 * @author Stephan Reiling
 */
public class MouseFastRotate extends MouseRotate {
    MolecularScene myScene;

    /**
     * Creates a new MouseFastRotate object.
     *
     * @param transformGroup DOCUMENT ME!
     * @param scene          DOCUMENT ME!
     */
    public MouseFastRotate(TransformGroup transformGroup, MolecularScene scene) {
        super(transformGroup);
        myScene = scene;
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void processMouseEvent(MouseEvent evt) {
        if (evt.getID() == MouseEvent.MOUSE_PRESSED) {
            //System.out.println("mouse down");
            myScene.setFast();
        } else if (evt.getID() == MouseEvent.MOUSE_RELEASED) {
            //System.out.println("mouse up");
            myScene.setNice();
        } /*
            else if (evt.getID() == MouseEvent.MOUSE_MOVED) {
            // Process mouse move event
            }*/
        super.processMouseEvent(evt);
    }
} // MouseFastRotate
