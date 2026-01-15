package org.jscience.chemistry.gui.extended.graphics3d;

import org.jscience.chemistry.gui.extended.molecule.Atom;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.Switch;

/**
 * SharedBondGroup.java
 * <p/>
 * <p/>
 * Created: Sat Nov 28 20:46:06 1998
 *
 * @author Stephan Reiling
 */
public class SharedBondGroup extends SharedGroup implements RenderStyle {
    Switch mySwitch;

    /**
     * Creates a new SharedBondGroup object.
     *
     * @param a DOCUMENT ME!
     */
    public SharedBondGroup(Atom a) {
        super();
        mySwitch = new Switch(Switch.CHILD_MASK);
        mySwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        addChild(mySwitch);

        RenderTable rTable = RenderTable.getTable();

        Material m = rTable.getMaterial(a);
        Appearance appearance = new Appearance();
        appearance.setMaterial(m);

        Cylinder cyl = new Cylinder(RenderTable.STICK_RADIUS, 1.0f,
                RenderTable.STICK_QUALITY, appearance);

        mySwitch.addChild(cyl.getShape());
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
            case RenderStyle.STICK:
                mySwitch.setWhichChild(0);

                break;

            case RenderStyle.CPK:
                mySwitch.setWhichChild(Switch.CHILD_NONE);

                break;
        }
    }
} // SharedBondGroup
