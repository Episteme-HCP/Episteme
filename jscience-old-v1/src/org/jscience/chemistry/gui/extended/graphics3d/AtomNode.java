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


//import com.sun.j3d.utils.geometry.Sphere;
//import com.sun.j3d.utils.geometry.ColorCube;
import org.jscience.chemistry.gui.extended.molecule.Atom;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import javax.vecmath.Vector3f;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class AtomNode extends BranchGroup {
    /** DOCUMENT ME! */
    Atom myAtom;

    /** DOCUMENT ME! */
    TransformGroup myTrans;

    /** DOCUMENT ME! */
    Transform3D myLoc;

/**
     * Creates a new AtomNode object.
     *
     * @param a DOCUMENT ME!
     */
    protected AtomNode(Atom a) {
        super();
        myAtom = a;

        //System.out.println("Node atom");
        myLoc = new Transform3D();
        myLoc.set(new Vector3f((float) a.getX(), (float) a.getY(),
                (float) a.getZ()));
        myTrans = new TransformGroup(myLoc);

        addChild(myTrans);
        myTrans.addChild(RenderTable.getTable().getSharedAtomGroup(myAtom));
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     */
    void setCoor(float x, float y, float z) {
        myLoc.set(new Vector3f(x, y, z));
    }
}
