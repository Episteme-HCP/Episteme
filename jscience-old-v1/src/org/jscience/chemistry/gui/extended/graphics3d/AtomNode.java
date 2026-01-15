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
