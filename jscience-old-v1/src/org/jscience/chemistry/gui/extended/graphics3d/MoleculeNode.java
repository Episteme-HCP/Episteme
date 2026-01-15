package org.jscience.chemistry.gui.extended.graphics3d;

import org.jscience.chemistry.gui.extended.molecule.AtomVector;
import org.jscience.chemistry.gui.extended.molecule.BondVector;
import org.jscience.chemistry.gui.extended.molecule.Molecule;

import java.util.Vector;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class MoleculeNode extends BranchGroup implements RenderStyle {
    /** DOCUMENT ME! */
    BranchGroup atomGroup;

    /** DOCUMENT ME! */
    BranchGroup bondGroup;

    /** DOCUMENT ME! */
    Vector atoms;

    /** DOCUMENT ME! */
    Vector bonds;

    // Maybe also others
    /** DOCUMENT ME! */
    Molecule myMolecule;

    /** DOCUMENT ME! */
    TransformGroup moleculeTrans;

/**
     * Creates a new MoleculeNode object.
     *
     * @param m DOCUMENT ME!
     */
    protected MoleculeNode(Molecule m) {
        this(m, RenderStyle.CPK);
    }

/**
     * Creates a new MoleculeNode object.
     *
     * @param m     DOCUMENT ME!
     * @param style DOCUMENT ME!
     */
    protected MoleculeNode(Molecule m, int style) {
        this();
        myMolecule = m;

        if (style != RenderStyle.WIRE) {
            atoms = new Vector();
            bonds = new Vector();

            AtomVector v = m.getMyAtoms();
            int s = v.size();

            for (int i = 0; i < s; i++) {
                AtomNode a = new AtomNode(v.getAtom(i));
                addAtomNode(a);
            }

            BondVector b = m.getMyBonds();
            s = b.size();

            for (int i = 0; i < s; i++) {
                BondNode bn = new BondNode(b.getBond(i));
                addBondNode(bn);
            }
        } else {
            BondVector b = m.getMyBonds();
            int s = b.size();

            for (int i = 0; i < s; i++) {
                bondGroup.addChild(BondNode.createWire(b.getBond(i)));
            }
        }
    }

/**
     * Creates a new MoleculeNode object.
     */
    public MoleculeNode() {
        super();
        setCapability(BranchGroup.ALLOW_DETACH);
        buildRoot();
    }

    /**
     * DOCUMENT ME!
     */
    void buildRoot() {
        //moleculeRoot = new BranchGroup();
        //Transform3D t = new Transform3D();
        //t.set(new Vector3f(0.0f, 0.0f, 20.0f));
        // This will allow to transform individual molecules
        moleculeTrans = new TransformGroup();
        moleculeTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        //BoundingSphere bounds =
        // new BoundingSphere(new Point3d(0.0,0.0,0.0), 500.0);
        // Create the behavior node
        //DragBehavior behavior = new DragBehavior(moleculeTrans);
        //behavior.setSchedulingBounds(bounds);
        //moleculeTrans.addChild(behavior);
        //moleculeRoot.addChild(moleculeTrans);
        addChild(moleculeTrans);

        // currently things are sorted by atoms and bonds
        atomGroup = new BranchGroup();
        bondGroup = new BranchGroup();
        moleculeTrans.addChild(atomGroup);
        moleculeTrans.addChild(bondGroup);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     */
    public void addAtomNode(AtomNode a) {
        atomGroup.addChild(a);
        atoms.addElement(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void addBondNode(BondNode b) {
        bondGroup.addChild(b);
        bonds.addElement(b);
    }

    /**
     * DOCUMENT ME!
     *
     * @param style DOCUMENT ME!
     */
    public void setStyle(int style) {
    }
}
