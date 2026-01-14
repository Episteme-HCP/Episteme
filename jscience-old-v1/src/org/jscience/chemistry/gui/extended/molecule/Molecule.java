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

package org.jscience.chemistry.gui.extended.molecule;

import java.awt.*;
import java.util.Hashtable;

/**
 * Tripos base molecule class
 *
 * @author Jason Plurad (jplurad@org.jscience.chemistry.gui.extended.com)
 *         Orignal Version
 * @author Mike Brusati (brusati@org.jscience.chemistry.gui.extended.com)
 *         Converted to JMol, the new Tripos Java molecule format, and merged the
 *         drawing methods into the molecule class.
 *         <p/>
 *         Modified: Zhidong Xie (zxie@org.jscience.chemistry.gui.extended.com)
 *         Replaced molecule package, from jmol package
 *         Changed public instance variables to protected, added accessors
 *         Moved drawings to Renderer, this.draw still retains by calling a
 *         default renderer
 *         date: 9/11/97
 * @see AtomVector
 * @see BondVector
 * @see Atom
 * @see Bond
 * @see Matrix3D
 */
public class Molecule {
    /**
     * Default renderer so that pre-existing programs can
     * still call Molecule.draw( Graphics )
     */
    protected static Renderer rendererDefault = new Renderer();

    /**
     * Molecule id
     */
    protected int id;

    /**
     * Molecule name
     */
    protected String name;

    /**
     * Atom list
     */
    protected AtomVector myAtoms;

    /**
     * Atom id to index map.  Does not require atom ids to start at 1 or be
     * sequential.
     */
    protected Hashtable atomIdToIndex;

    /**
     * Bond list
     */
    protected BondVector myBonds;

    /**
     * Ring list
     */
    protected RingVector myRings;

    /**
     * Number of atoms
     */
    protected int numAtoms;

    /**
     * Number of bonds
     */
    protected int numBonds;

    /**
     * Number of rings
     */
    protected int numRings;

    /**
     * Properties list
     */
    protected Hashtable properties = new Hashtable(10);

    /**
     * The transformation matrix
     */
    protected Matrix3D mat;

    /**
     * The bounding extents of the molecule
     */
    protected float xmin;

    /**
     * The bounding extents of the molecule
     */
    protected float ymin;

    /**
     * The bounding extents of the molecule
     */
    protected float zmin;

    /**
     * The bounding extents of the molecule
     */
    protected float xmax;

    /**
     * The bounding extents of the molecule
     */
    protected float ymax;

    /**
     * The bounding extents of the molecule
     */
    protected float zmax;

    /**
     * List of selected atoms
     */
    protected AtomVector selectAtoms = new AtomVector(10);

    /**
     * List of highlighted atom
     */
    protected AtomVector highlightAtoms = new AtomVector(10);

    /**
     * Id/Name constructor
     *
     * @param id   molecule id
     * @param name molecule name
     */
    public Molecule(int id, String name) {
        this.id = id;
        this.name = new String(name);
        myAtoms = new AtomVector(30, 30);
        atomIdToIndex = new Hashtable(30);
        myBonds = new BondVector(30, 30);
        numAtoms = numBonds = 0;
        myRings = new RingVector(4, 2);
        mat = new Matrix3D();
        xmax = xmin = ymax = ymin = zmax = zmin = 0.0f;
    }

    /**
     * Default constructor
     */
    public Molecule() {
        this(0, "");
    }

    /**
     * Copy constructor
     *
     * @param jmol molecule to be copied
     */
    public Molecule(Molecule jmol) {
        if (jmol == null) {
            jmol = new Molecule();
        }

        id = jmol.id;
        name = jmol.name;
        myAtoms = new AtomVector(jmol.numAtoms, 30);
        atomIdToIndex = new Hashtable(30);
        myBonds = new BondVector(jmol.numBonds, 30);
        myRings = new RingVector(jmol.numRings, 2);

        for (int i = 0; i < jmol.numAtoms; i++) {
            myAtoms.append(new Atom(jmol.myAtoms.getAtom(i)));
            numAtoms++;
        }

        for (int i = 0; i < jmol.numBonds; i++) {
            Bond b = jmol.myBonds.getBond(i);
            int a1 = jmol.myAtoms.indexOf(b.a1);
            int a2 = jmol.myAtoms.indexOf(b.a2);
            Bond newb = new Bond(b.id, b.type, myAtoms.getAtom(a1),
                    myAtoms.getAtom(a2), b.ringId);
            myBonds.append(newb);
            numBonds++;
        }

        for (int i = 0; i < jmol.numRings; i++) {
            myRings.append(new Ring(jmol.myRings.getRing(i)));
            numRings++;
        }

        mat = (Matrix3D) jmol.mat.clone();

        findBB();

        if (jmol.hasSelectedAtoms()) {
            for (int i = 0; i < jmol.selectAtoms.size(); i++) {
                Atom a = jmol.selectAtoms.getAtom(i);
                int ai = jmol.myAtoms.indexOf(a);

                if (ai != -1) {
                    selectAtoms.append(myAtoms.getAtom(ai));
                }
            }
        }

        if (jmol.hasHighlightedAtoms()) {
            for (int i = 0; i < jmol.highlightAtoms.size(); i++) {
                Atom a = jmol.highlightAtoms.getAtom(i);
                int ai = jmol.myAtoms.indexOf(a);

                if (ai != -1) {
                    highlightAtoms.append(myAtoms.getAtom(ai));
                }
            }
        }
    }

    /**
     * Add an atom to this molecule
     *
     * @param a atom to be added
     * @see #deleteAtom
     */
    public void addAtom(Atom a) {
        myAtoms.append(a);
        atomIdToIndex.put(new Integer(a.id), new Integer(numAtoms));
        numAtoms++;
    }

    /**
     * Add a bond to this molecule
     *
     * @param b bond to be added
     * @see #deleteBond
     */
    public void addBond(Bond b) {
        myBonds.append(b);
        numBonds++;
    }

    /**
     * Add a ring to this molecule
     *
     * @param r ring to be added
     * @see #deleteRing
     */
    public void addRing(Ring r) {
        myRings.append(r);
        numRings++;
    }

    /**
     * Add a molecule to this molecule
     *
     * @param jmol molecule to be added
     */
    public void addMolecule(Molecule jmol) {
        if (jmol == null) {
            return;
        }

        int na = numAtoms;

        for (int i = 0; i < jmol.numAtoms; i++) {
            myAtoms.append(new Atom(jmol.myAtoms.getAtom(i)));
            numAtoms++;
        }

        for (int i = 0; i < jmol.numBonds; i++) {
            Bond b = jmol.myBonds.getBond(i);
            int a1 = jmol.myAtoms.indexOf(b.a1) + na;
            int a2 = jmol.myAtoms.indexOf(b.a2) + na;
            Bond newb = new Bond(b.id, b.type, myAtoms.getAtom(a1),
                    myAtoms.getAtom(a2), b.ringId);
            myBonds.append(newb);
            numBonds++;
        }

        for (int i = 0; i < jmol.numRings; i++) {
            myRings.append(new Ring(jmol.myRings.getRing(i)));
            numRings++;
        }
    }

    /**
     * Delete an atom and its bonds from this molecule
     *
     * @param a atom to be deleted
     * @see #deleteBond
     * @see #addAtom
     */
    public void deleteAtom(Atom a) {
        if (highlightAtoms.contains(a)) {
            highlightAtoms.removeAtom(a);
        }

        if (selectAtoms.contains(a)) {
            selectAtoms.removeAtom(a);
        }

        for (int i = numBonds - 1; i >= 0; i--) {
            Bond b = myBonds.getBond(i);

            if (b.a1.equals(a) || b.a2.equals(a)) {
                deleteBond(b);
            }
        }

        if (myAtoms.removeAtom(a)) {
            numAtoms--;
        }
    }

    /**
     * Delete a bond from this molecule
     *
     * @param b bond to be deleted
     * @see #addBond
     */
    public void deleteBond(Bond b) {
        if (myBonds.removeBond(b)) {
            numBonds--;
        }
    }

    /**
     * Delete a ring from this molecule
     *
     * @param r ring to be deleted
     * @see #addRing
     */
    public void deleteRing(Ring r) {
        if (myRings.remove(r)) {
            numRings--;
        }
    }

    /**
     * Add a property (i.e. a "name"/"value" pair) to this molecule.
     *
     * @param name  name of property to be added
     * @param value value associated with "name"
     */
    public void addProperty(String name, String value) {
        properties.put(name, value);
    }

    /**
     * Remove a property from this molecule.
     *
     * @param name name of property to be removed
     */
    public void deleteProperty(String name) {
        properties.remove(name);
    }

    /**
     * Get the property value associated with specified property name.
     *
     * @param name name of property whose value is sought
     */
    public String getPropertyValue(String name) {
        return (String) properties.get(name);
    }

    /**
     * Indicate if the molecule contains the specified property.
     *
     * @param name name of property to check for
     */
    public boolean isAProperty(String name) {
        return properties.containsKey(name);
    }

    /**
     * Flags specified atom as <tt>selected</tt>
     *
     * @param a atom to be selected
     */
    public void select(Atom a) {
        if ((a == null) || a.select) {
            return;
        }

        a.select = true;
        selectAtoms.append(a);
    }

    /**
     * Deselects all <tt>selected</tt> atoms
     *
     * @see #select
     */
    public void deselect() {
        if (selectAtoms != null) {
            for (int i = 0; i < selectAtoms.size(); i++) {
                Atom a = selectAtoms.getAtom(i);
                a.select = false;
            }
        }

        selectAtoms = new AtomVector(10);
    }

    /**
     * Deselect one <tt>selected</tt> atom
     *
     * @see #select
     * @see #deselect
     */
    public void deselect(Atom a) {
        if (selectAtoms != null) {
            if (a != null) {
                while (selectAtoms.contains(a)) {
                    a.select = false;
                    selectAtoms.removeAtom(a);
                }
            }
        }
    }

    /**
     * Returns <tt>true</tt> if molecule has <tt>selected</tt> atoms
     *
     * @see #select
     */
    public boolean hasSelectedAtoms() {
        return (selectAtoms.size() > 0);
    }

    /**
     * Returns <tt>true</tt> if molecule has <tt>highlighted</tt> atoms
     *
     * @see #highlight
     */
    protected boolean hasHighlightedAtoms() {
        return (highlightAtoms.size() > 0);
    }

    /**
     * Set name of this molecule
     *
     * @param s name of molecule
     * @see #getName
     */
    public void setName(String s) {
        if (s == null) {
            s = new String();
        }

        name = s;
    }

    /**
     * Returns name of molecule
     *
     * @see #setName
     */
    public String getName() {
        return name;
    }

    /**
     * Determines bounding box of a molecule (sets xmax,xmin,ymax,ymin,zmax,zmin)
     */
    public void findBB() {
        if (numAtoms == 0) {
            xmin = xmax = ymin = ymax = zmin = zmax = 0;

            return;
        }

        Atom a = myAtoms.getAtom(0);

        xmin = xmax = a.x;
        ymin = ymax = a.y;
        zmin = zmax = a.z;

        for (int i = 1; i < numAtoms; i++) {
            a = myAtoms.getAtom(i);

            if (a.x < xmin) {
                xmin = a.x;
            }

            if (a.x > xmax) {
                xmax = a.x;
            }

            if (a.y < ymin) {
                ymin = a.y;
            }

            if (a.y > ymax) {
                ymax = a.y;
            }

            if (a.z < zmin) {
                zmin = a.z;
            }

            if (a.z > zmax) {
                zmax = a.z;
            }
        }
    }

    /**
     * Compares this molecule with another
     *
     * @param jmol molecule to compare with
     * @return <tt>true</tt> if they are equal, else <tt>false</tt>
     */
    public boolean equals(Molecule jmol) {
        if (jmol == null) {
            return false;
        }

        if ((numAtoms != jmol.numAtoms) || (numBonds != jmol.numBonds)) {
            return false;
        }

        return (myAtoms.equals(jmol.myAtoms) && myBonds.equals(jmol.myBonds));
    }

    /**
     * Designates an atom as <tt>highlighted</tt>.  The specified atom gets
     * appended to the current highlight list.
     *
     * @param a atom to highlight
     */
    public void highlight(Atom a) {
        if ((a == null) || highlightAtoms.contains(a)) {
            return;
        }

        highlightAtoms.append(a);
        a.highlight = true;
    }

    /**
     * Designates an atom list as <tt>highlighted</tt>.  The new list replaces
     * any current list of highlighted atoms.
     *
     * @param av list (vector) of atoms to highlight
     */
    public void highlight(AtomVector av) {
        if (av == null) {
            return;
        }

        //
        // Unhighlight any currently highlighted atoms.
        //
        if (highlightAtoms != null) {
            for (int i = 0; i < highlightAtoms.size(); i++) {
                Atom a = highlightAtoms.getAtom(i);
                a.highlight = false;
            }
        }

        highlightAtoms = new AtomVector(10);

        for (int i = 0; i < av.size(); i++) {
            Atom a = av.getAtom(i);

            if (a != null) {
                highlightAtoms.append(a);
                a.highlight = true;
            }
        }
    }

    /**
     * Dehighlighted all highlighted atoms.
     */
    public void dehighlight() {
        if (highlightAtoms != null) {
            for (int i = 0; i < highlightAtoms.size(); i++) {
                Atom a = highlightAtoms.getAtom(i);
                a.highlight = false;
            }

            highlightAtoms = new AtomVector(10);
        }
    }

    /**
     * Returns true if bond is oriented counterclockwise with respect to
     * the centroid of the ring, else returns false.
     *
     * @param b   potential ring bond to be checked
     * @param tx1 transformed x coordinate of bond's from atom
     * @param ty1 transformed y coordinate of bond's from atom
     * @param tx2 transformed x coordinate of bond's to atom
     * @param ty2 transformed y coordinate of bond's to atom
     */
    public boolean isOuterBond(Bond b, float tx1, float ty1, float tx2,
                               float ty2) {
        if (b.ringId == 0) {
            return false;
        }

        float x1;
        float y1;
        float x2;
        float y2;
        Ring r = myRings.getRing(b.ringId - 1);

        x1 = tx2 - tx1;
        y1 = ty2 - ty1;
        x2 = r.tx - tx1;
        y2 = r.ty - ty1;

        return (((x1 * y2) - (y1 * x2)) >= 0); // right side
    }

    /**
     * Calculates 2D vector perpendicular to bond
     */
    public void calcPerpUnitVec(float x1, float y1, float x2, float y2,
                                float[] pex, float[] pey) {
        float len = (float) java.lang.Math.sqrt(((x2 - x1) * (x2 - x1)) +
                ((y2 - y1) * (y2 - y1)));

        //
        // Unit vector of the bond
        //
        float ex = (x2 - x1) / len;
        float ey = (y2 - y1) / len;

        //
        // Unit vector perpendicular to bond
        //
        pex[0] = -1.0f * ey;
        pey[0] = ex;
    }

    /**
     * Returns closest atom to point within FINDRADIUS, <tt>null</tt> if nothing is found,
     *
     * @see #FINDRADIUS
     */
    public Atom findAtom(float x, float y, float z, float FINDRADIUS) {
        int result = -1;
        float minDist = 1000000000.0f;

        for (int i = 0; i < numAtoms; i++) {
            Atom a = myAtoms.getAtom(i);
            float dist = ((a.tx - x) * (a.tx - x)) + ((a.ty - y) * (a.ty - y)); //+ (a.tz-z)*(a.tz-z);

            if ((dist < FINDRADIUS) && (dist < minDist)) {
                minDist = dist;
                result = i;
            }
        }

        if (result == -1) {
            return null;
        } else {
            return myAtoms.getAtom(result);
        }
    }

    /**
     * Draw the molecule
     * recommend to avoid, but to use renderer approach
     *
     * @param g Graphics
     */
    public void draw(Graphics g) {
        rendererDefault.setMol(this);
        rendererDefault.draw(g);
    }

    /**
     * Returns a String representation of molecule
     */
    public String toString() {
        String s = new String();

        s += ("Id: " + id + "\n");
        s += ("Name: " + name + "\n");
        s += ("Atoms: " + numAtoms + "\n");

        for (int i = 0; i < numAtoms; i++) {
            Atom a = myAtoms.getAtom(i);
            s += (String.valueOf(i + 1) + " " + a.toString() + "\n");
        }

        s += ("Bonds: " + numBonds + "\n");

        for (int i = 0; i < numBonds; i++) {
            Bond b = myBonds.getBond(i);
            int a1 = myAtoms.indexOf(b.a1) + 1;
            int a2 = myAtoms.indexOf(b.a2) + 1;
            s += (String.valueOf(i + 1) + " " + a1 + " " + a2 + " " + b.type +
                    " " + b.ringId + "\n");
        }

        return s;
    }

    /**
     * Returns id of this molecule
     */
    public int getId() {
        return id;
    }

    /**
     * Returns atom vector of this molecule
     */
    public AtomVector getMyAtoms() {
        return myAtoms;
    }

    /**
     * Return atomIdToIndex
     */
    public Hashtable getAtomIdToIndex() {
        return atomIdToIndex;
    }

    /**
     * Return bond vector of this molecule
     */
    public BondVector getMyBonds() {
        return myBonds;
    }

    /**
     * Return ring vector of this molecule
     */
    public RingVector getMyRings() {
        return myRings;
    }

    /**
     * Return number of atoms in this molecule
     */
    public int getNumAtoms() {
        return myAtoms.size();
    }

    /**
     * Return number of bonds in this molecule
     */
    public int getNumBonds() {
        return myBonds.size();
    }

    /**
     * Return number of rings in this molecule
     */
    public int getNumRings() {
        return numRings;
    }

    /**
     * Return property table in this molecule
     */
    public Hashtable getProperties() {
        return properties;
    }

    /**
     * Return 3D transformation matrix
     */
    public Matrix3D getMatrix3D() {
        return mat;
    }

    /**
     * Return the minimum x coordinate of this molelcule
     */
    public float getXmin() {
        return xmin;
    }

    /**
     * Return the minimum y coordinate
     */
    public float getYmin() {
        return ymin;
    }

    /**
     * Return the minimum z coordinate
     */
    public float getZmin() {
        return zmin;
    }

    /**
     * Return the maxmum x coordinate
     */
    public float getXmax() {
        return xmax;
    }

    /**
     * Return the maxmum y coordinate
     */
    public float getYmax() {
        return ymax;
    }

    /**
     * Return the maxmum z coordinate
     */
    public float getZmax() {
        return zmax;
    }

    /**
     * Return the selected atoms vector
     */
    public AtomVector getSelectAtoms() {
        return selectAtoms;
    }

    /**
     * Return the highlighted atoms vector
     */
    public AtomVector getHighlightAtoms() {
        return highlightAtoms;
    }

    /**
     * Return the Renderer which draws this molecule
     */
    protected static Renderer getRenderer() {
        return rendererDefault;
    }

    /**
     * Return true iff the atom in the parameter is one of the atoms in this molecule
     *
     * @param a atom in this query
     * @see contains( Bond )
     */
    public boolean contains(Atom a) {
        return myAtoms.contains(a);
    }

    /**
     * Return true iff the bond in the parameter is one of the bonds in this molecule
     *
     * @param b bond in this query
     * @see contains( Atom )
     */
    public boolean contains(Bond b) {
        return myBonds.contains(b);
    }

    /**
     * Return an atom in this molecule whose id matches the input id
     *
     * @param atomId id of queried atom
     * @see getBondFromId
     */
    public Atom getAtomFromId(int atomId) {
        Atom a;

        for (int i = 0; i < myAtoms.size(); i++) {
            a = myAtoms.getAtom(i);

            if (atomId == a.getId()) {
                return a;
            }
        }

        return null;
    }

    /**
     * Return a bond in this molecule whose id matches the input id
     *
     * @param bondId id of queried bond
     * @see getAtomFromId
     */
    public Bond getBondFromId(int bondId) {
        Bond b;

        for (int i = 0; i < myBonds.size(); i++) {
            b = myBonds.getBond(i);

            if (bondId == b.getId()) {
                return b;
            }
        }

        return null;
    }

    /**
     * Set id of this molecule
     *
     * @param id molecule id
     * @see #getId
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set atom vector
     *
     * @param id molecule id
     * @see #getMyAtoms
     */
    public void setMyAtoms(AtomVector atoms) {
        myAtoms = atoms;
    }

    /**
     * Set atomIdToIndex
     *
     * @param index atomIdToIndex
     * @see #getAtomIdToIndex
     */
    public void setAtomIdToIndex(Hashtable index) {
        atomIdToIndex = index;
    }

    /**
     * Set bond vector
     *
     * @param bv bond vector
     * @see #getMyBonds
     */
    public void setMyBonds(BondVector bv) {
        myBonds = bv;
    }

    /**
     * Set ring vector
     *
     * @param rv ring vector
     * @see #getMyRings
     */
    public void setMyRings(RingVector rv) {
        myRings = rv;
    }

    /**
     * Set number of atoms
     *
     * @param numAtoms number of atoms
     * @see #getNumAtoms
     */
    public void setNumAtoms(int numAtoms) {
        this.numAtoms = numAtoms;
    }

    /**
     * Set number of rings
     *
     * @param numRings number of rings
     * @see #getNumRings
     */
    public void setNumRings(int numRings) {
        this.numRings = numRings;
    }

    /**
     * Set property table
     *
     * @param prop property table
     * @see #getProperties
     */
    public void setProperties(Hashtable prop) {
        properties = prop;
    }

    /**
     * Set 3D transformation matrix
     *
     * @param mat Matrix3D
     * @see #getMatrix3D
     */
    public void setMatrix3D(Matrix3D mat) {
        this.mat = mat;
    }

    /**
     * Set minimum of X-coordinate of this molecule
     *
     * @param prop property table
     * @see #getProperties
     */
    public void setXmin(float xmin) {
        this.xmin = xmin;
    }

    /**
     * Set minimum of Y-coordinate
     *
     * @param ymin minimum Y-coordinate
     * @see #getYmin
     */
    public void setYmin(float ymin) {
        this.ymin = ymin;
    }

    /**
     * Set minimum of Z-coordinate
     *
     * @param ymin minimum Z-coordinate
     * @see #getZmin
     */
    public void setZmin(float zmin) {
        this.zmin = zmin;
    }

    /**
     * Set maxmum of X-coordinate
     *
     * @param xmax maximum X-coordinate
     * @see #getXmax
     */
    public void setXmax(float xmax) {
        this.xmax = xmax;
    }

    /**
     * Set maxmum of Y-coordinate
     *
     * @param ymax maximum Y-coordinate
     * @see #getYmax
     */
    public void setYmax(float ymax) {
        this.ymax = ymax;
    }

    /**
     * Set maxmum of Z-coordinate
     *
     * @param zmax maximum Z-coordinate
     * @see #getYmax
     */
    public void setZmax(float zmax) {
        this.zmax = zmax;
    }

    /**
     * Set selected atoms vector
     *
     * @param av atom vector
     * @see #getSelectAtoms
     */
    public void setSelectAtoms(AtomVector av) {
        selectAtoms = av;
    }

    /**
     * Set highlighted atoms vector
     *
     * @param av atom vector
     * @see #getHighlightAtoms
     */
    public void setHighlightAtoms(AtomVector av) {
        highlightAtoms = av;
    }

    /**
     * Set renderer which draws this molecule
     *
     * @param ir Renderer
     * @see #getRenderer
     */
    protected static void setRenderer(Renderer r) {
        rendererDefault = r;
    }
} // end of class JMol, or after rename, Molecule                    
