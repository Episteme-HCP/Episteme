package org.jscience.chemistry.gui.extended.molecule;

import java.util.Properties;

/**
 * Tripos base Bond class
 *
 * @author Jason Plurad (jplurad@org.jscience.chemistry.gui.extended.com)
 *         Orignal Version
 * @author Mike Brusati (brusati@org.jscience.chemistry.gui.extended.com)
 *         Converted to JMol, the new Tripos Java molecule format.
 *         <p/>
 *         Modified: Zhidong Xie (zxie@org.jscience.chemistry.gui.extended.com)
 *         Replaced molecule package, from jmol package
 *         Added one constructor and four accessors
 *         Changed some instance variables from public to protected
 *         date: 11 Aug 97
 * @see Atom
 */
public class Bond implements PropAttributesInter {
    /**
     * Single bond type
     */
    public static final int SINGLE = 1;

    /**
     * Double bond type
     */
    public static final int DOUBLE = 2;

    /**
     * Triple bond type
     */
    public static final int TRIPLE = 3;

    /**
     * Aromatic bond type
     */
    public static final int AROMATIC = 4;

    /**
     * Wedge up bond type
     */
    public static final int WEDGE = 5;

    /**
     * Wedge down bond type
     */
    public static final int DASH = 6;

    /**
     * Any bond type
     */
    public static final int ANY = 7;

    /**
     * Bond id
     */
    protected int id;

    /**
     * Bond type
     */
    protected int type;

    /**
     * From and to atoms, respectively
     */
    public Atom a1;

    /**
     * From and to atoms, respectively
     */
    public Atom a2;

    /**
     * Id of ring atom belongs to (0 means atom is not in a ring)
     */
    protected int ringId;

    /**
     * Property list at the end of the bond string
     */
    public PropAttributes prop;

    /**
     * Full constructor
     *
     * @param id     bond id
     * @param type   bond type
     * @param from   from atom
     * @param to     to atom
     * @param ringId id of ring atom belongs to (0 means atom is not in a ring)
     * @param prop   properties of the bond, (string pairs on the end)
     */
    public Bond(int id, int type, Atom from, Atom to, int ringId,
                Properties prop) {
        if (from == null) {
            from = new Atom();
        }

        if (to == null) {
            to = new Atom();
        }

        this.id = id;
        this.type = ((type < SINGLE) || (type > ANY)) ? SINGLE : type;
        a1 = from;
        a2 = to;
        this.ringId = ringId;

        if (prop == null) {
            this.prop = new PropAttributes();
        } else {
            this.prop = new PropAttributes(prop);
        }
    }

    /**
     * Constructor without Properties
     *
     * @param type bond type
     * @param from from atom
     * @param to   to atom
     */
    public Bond(Atom from, Atom to, int btype) {
        this(0, btype, from, to, 0);
    }

    /**
     * Constructor without properties
     *
     * @param id     bond id
     * @param type   bond type
     * @param from   from atom
     * @param to     to atom
     * @param ringId id of ring atom belongs to (0 means atom is not in a ring)
     */
    public Bond(int id, int type, Atom from, Atom to, int ringId) {
        this(id, type, from, to, ringId, null);
    }

    /**
     * Default constructor
     */
    public Bond() {
        this(0, 0, new Atom(), new Atom(), 0);
    }

    /**
     * Copy constructor
     *
     * @param b bond to be copied
     */
    public Bond(Bond b) {
        if (b == null) {
            b = new Bond();
        }

        id = b.id;
        type = b.type;
        a1 = new Atom(b.a1);
        a2 = new Atom(b.a2);
        ringId = b.ringId;
        prop = new PropAttributes(b.prop);
    }

    /**
     * Set bond type
     *
     * @param n bond type
     * @throws IllegalArgumentException if illegal bond type
     * @see #getType
     */
    public void setType(int n) throws IllegalArgumentException {
        if ((n < SINGLE) || (n > ANY)) {
            throw new IllegalArgumentException();
        } else {
            type = n;
        }
    }

    /**
     * Returns bond type
     *
     * @see #setType
     */
    public int getType() {
        return type;
    }

    /**
     * Compares this bond with another
     *
     * @param b bond to compare with
     * @return <tt>true</tt> if bonds are equal, else <tt>false</tt>
     */
    public boolean equals(Bond b) {
        if (b == null) {
            return false;
        }

        return ((b.a1.equals(a1) && b.a2.equals(a2)) ||
                (b.a1.equals(a2) && b.a2.equals(a1)));
    }

    /**
     * Returns a String representation of bond
     *
     * @returns a String representation of bond
     */
    public String toString() {
        return ("Bond: " + id + " " + String.valueOf(type) + " " +
                a1.toString() + " " + a2.toString() + " " + ringId + prop.toString());
    }

    /**
     * Retrun the length of this bond
     *
     * @return the length of this bond
     */
    public float length() {
        float xdiff = a1.getX() - a2.getX();
        float ydiff = a1.getY() - a2.getY();
        float zdiff = a1.getZ() - a2.getZ();

        float lenSquare = (xdiff * xdiff) + (ydiff * ydiff) + (zdiff * zdiff);

        return (float) Math.sqrt(lenSquare);
    }

    /**
     * Get the actual properties object that holds the attributes
     */
    public Properties getAttributes() {
        return (prop.getProperties());
    }

    /**
     * Get the encapsluation of the properties object which holds the attributes
     */
    public PropAttributes getPropAttributes() {
        return (prop);
    }

    /**
     * Set the attributes for the bond
     *
     * @param p Property for the bond
     */
    public void setAttributes(Properties p) {
        prop = new PropAttributes(p);
    }

    /**
     * Set the attributes for the bond
     *
     * @param p Property for the bond
     */
    public void setPropAttributes(PropAttributes p) {
        prop = new PropAttributes(p);
    }

    /**
     * Returns id of the bond
     */
    public int getId() {
        return id;
    }

    /**
     * Returns id of the ring which this bond associates with
     */
    public int getRingId() {
        return ringId;
    }

    /**
     * Set id of the bond
     *
     * @param id bond id
     * @see #getId
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set id of the ring which this bond associate with
     *
     * @param id ring id
     * @see #getRingId
     */
    public void setRingId(int id) {
        this.ringId = id;
    }
} // end of class Bond
