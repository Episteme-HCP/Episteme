package org.jscience.chemistry.gui.extended.graphics3d;

import org.jscience.chemistry.gui.extended.molecule.Atom;

import javax.media.j3d.*;

/**
 * SharedAtomGroup.java
 * <p/>
 * <p/>
 * Created: Sat Nov 28 20:22:12 1998
 *
 * @author Stephan Reiling
 */
public class SharedAtomGroup extends SharedGroup implements RenderStyle {
    Switch mySwitch;

    /**
     * Creates a new SharedAtomGroup object.
     *
     * @param a DOCUMENT ME!
     */
    public SharedAtomGroup(Atom a) {
        super();
        mySwitch = new Switch(Switch.CHILD_MASK);
        mySwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        addChild(mySwitch);

        RenderTable rTable = RenderTable.getTable();

        Material m = rTable.getMaterial(a);
        Appearance appearance = new Appearance();
        appearance.setMaterial(m);

        // Ball and Stick:
        Node n = new IcoSphere(rTable.BALL_RADIUS, 1, appearance).getShape();
        mySwitch.addChild(n);

        //CPK:
        n = new IcoSphere(rTable.getRadius(a), 2, appearance).getShape();
        mySwitch.addChild(n);

        // STICK:
        n = new IcoSphere(rTable.STICK_RADIUS, 0, appearance).getShape();
        mySwitch.addChild(n);

        mySwitch.setWhichChild(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param style DOCUMENT ME!
     */
    public void setStyle(int style) {
        switch (style) {
            case RenderStyle.BALL_AND_STICK:
                mySwitch.setWhichChild(0);

                break;

            case RenderStyle.CPK:
                mySwitch.setWhichChild(1);

                break;

            case RenderStyle.STICK:
                mySwitch.setWhichChild(2);

                break;
        }
    }
} // SharedAtomGroup
