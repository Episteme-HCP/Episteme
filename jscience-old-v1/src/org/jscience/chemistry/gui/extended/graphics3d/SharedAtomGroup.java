/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
