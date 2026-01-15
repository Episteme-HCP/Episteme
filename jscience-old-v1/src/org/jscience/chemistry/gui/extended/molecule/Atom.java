package org.jscience.chemistry.gui.extended.molecule;

import java.awt.*;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Tripos base Atom class
 *
 * @author Jason Plurad (jplurad@org.jscience.chemistry.gui.extended.com)
 *         Orignal Version
 * @author Mike Brusati (brusati@org.jscience.chemistry.gui.extended.com)
 *         Converted to JMol, the new Tripos Java molecule format.
 *         <p/>
 *         Modified: Zhidong Xie (zxie@org.jscience.chemistry.gui.extended.com)
 *         Added colorDefault, getColorFromType();
 *         date: 9/11/97
 * @version 0.5 29 Jul 96
 */
public class Atom implements PropAttributesInter {
    /**
     * Atom color map
     */
    public static Hashtable atomColor = new Hashtable(10);

    static {
        atomColor.put("C", Color.black);
        atomColor.put("H", new Color(0, 200, 200));
        atomColor.put("N", Color.blue);
        atomColor.put("O", Color.red);
        atomColor.put("P", new Color(255, 110, 0));
        atomColor.put("S", new Color(240, 180, 0));
        atomColor.put("Cl", new Color(0, 196, 0));
        atomColor.put("F", new Color(255, 0, 255));
        atomColor.put("Br", new Color(255, 0, 255));
        atomColor.put("I", new Color(255, 0, 255));
    }

    /**
     * Default color
     */
    public static final Color colorDefault = new Color(64, 255, 255);

    /**
     * Atom id
     */
    protected int id;

    /**
     * Elemental name
     */
    protected String name;

    /**
     * SYBYL force field atom type
     */
    protected String type;

    /**
     * Atom coordinates
     */
    protected float x;

    /**
     * Atom coordinates
     */
    protected float y;

    /**
     * Atom coordinates
     */
    protected float z;

    /**
     * Transformed atom coordinates
     */
    protected float tx;

    /**
     * Transformed atom coordinates
     */
    protected float ty;

    /**
     * Transformed atom coordinates
     */
    protected float tz;

    /**
     * Atomic charge
     */
    protected int charge;

    /**
     * Flag indicating if atom is selcted
     */
    protected boolean select = false;

    /**
     * Flag indicating if atom is highlighted
     */
    protected boolean highlight = false;

    /**
     * Flag indicating if atom label should be displayed
     */
    protected boolean display = true;

    /**
     * Flag indicating if atom is a carbon atom
     */
    private boolean carbon = false;

    /**
     * Flag indicating if atom is a hydrogen atom
     */
    private boolean hydrogen = false;

    /**
     * Property list at the end of the atom string
     */
    public PropAttributes prop;

    /**
     * Current atom color
     */
    protected Color color = colorDefault;

    /**
     * Full constructor
     *
     * @param id     atom id
     * @param name   elemental name of atom
     * @param x      x coordinate of atom
     * @param y      y coordinate of atom
     * @param z      z coordinate of atom
     * @param tx     tx transformed coordinate of atom
     * @param ty     ty transformed coordinate of atom
     * @param tz     tz transformed coordinate of atom
     * @param charge atom charge
     * @param prop   atom attributes strings
     */
    public Atom(int id, String name, float x, float y, float z, float tx,
                float ty, float tz, int charge, Properties prop) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
        this.charge = charge;

        if (prop != null) {
            this.prop = new PropAttributes(prop);
        } else {
            this.prop = new PropAttributes();
        }

        this.type = new String();
        setName(name);
    }

    /**
     * constructor, no prop
     *
     * @param id     atom id
     * @param name   elemental name of atom
     * @param x      x coordinate of atom
     * @param y      y coordinate of atom
     * @param z      z coordinate of atom
     * @param tx     tx transformed coordinate of atom
     * @param ty     ty transformed coordinate of atom
     * @param tz     tz transformed coordinate of atom
     * @param charge atom charge
     */
    public Atom(int id, String name, float x, float y, float z, float tx,
                float ty, float tz, int charge) {
        this(id, name, x, y, z, tx, ty, tz, charge, null);
    }

    /**
     * Default constructor
     */
    public Atom() {
        this(0, "", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0, null);
    }

    /**
     * Real coordinates constructor
     *
     * @param x x coordinate of atom
     * @param y y coordinate of atom
     * @param z z coordinate of atom
     */
    public Atom(float x, float y, float z) {
        this(0, "", x, y, z, x, y, z, 0);
    }

    /**
     * Real and transformed coordinates constructor
     *
     * @param x  x coordinate of atom
     * @param y  y coordinate of atom
     * @param z  z coordinate of atom
     * @param tx tx transformed coordinate of atom
     * @param ty ty transformed coordinate of atom
     * @param tz tz transformed coordinate of atom
     */
    public Atom(float x, float y, float z, float tx, float ty, float tz) {
        this(0, "", x, y, z, tx, ty, tz, 0);
    }

    /**
     * Copy constructor
     *
     * @param a atom to be copied
     */
    public Atom(Atom a) {
        if (a == null) {
            a = new Atom();
        }

        id = a.id;
        name = a.name;
        x = a.x;
        y = a.y;
        z = a.z;
        tx = a.tx;
        ty = a.ty;
        tz = a.tz;
        charge = a.charge;
        select = a.select;
        highlight = a.highlight;
        color = a.color;
        prop = new PropAttributes(a.prop);
        type = a.type;
    }

    /**
     * Set the name
     *
     * @param s elemental name of atom
     * @see #getName
     */
    public void setName(String s) {
        if (s == null) {
            s = new String();
        }

        name = s;
        carbon = name.equals("C") ? true : false;
        hydrogen = name.equals("H") ? true : false;
        display = true;
        color = atomColor.containsKey(name) ? (Color) atomColor.get(name)
                : Color.red;

        if (!type.startsWith(name)) {
            setType(name);
        }
    }

    /**
     * Set the SYBYL force field type
     *
     * @para s SYBYL force field atom type
     * @see #getType
     */
    public void setType(String s) {
        if (s == null) {
            s = new String();
        }

        type = s;

        int i = s.indexOf('.');

        if (i != -1) {
            setName(type.substring(0, i));
        } else {
            setName(type);
        }
    }

    /**
     * Set the atomic charge
     *
     * @param c charge
     * @see #getCharge
     */
    public void setCharge(int c) {
        charge = c;

        setType(name);
    }

    /**
     * Returns the elemental name
     *
     * @see #setName
     */
    public String getName() {
        return name;
    }

    /**
     * Returns SYBYL force field atom type
     *
     * @see #setType
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the atomic charge
     *
     * @see #setCharge
     */
    public int getCharge() {
        return charge;
    }

    /**
     * Compares this atom with another
     *
     * @param a atom to compare with
     * @return <tt>true</tt> if atoms are equal, else <tt>false</tt>
     */
    public boolean equals(Atom a) {
        if (a == null) {
            return false;
        }

        return super.equals(a);
    }

    /**
     * Returns String representation of Atom
     */
    public String toString() {
        return (id + " " + name + " " + x + " " + y + " " + z + " " + tx + " " +
                ty + " " + tz + " " + charge + prop.toString());
    }

    /**
     * Return the property containing attributes
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
     * Set the attributes for the atom
     *
     * @param p Property for the atom
     */
    public void setAttributes(Properties p) {
        prop = new PropAttributes(p);
    }

    /**
     * Set the attributes for the atom
     *
     * @param p Property for the atom
     */
    public void setPropAttributes(PropAttributes p) {
        prop = new PropAttributes(p);
    }

    /**
     * Returns id of the atom
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the x coordinate of the atom
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the atom
     */
    public float getY() {
        return y;
    }

    /**
     * Returns the z coordinate of the atom
     */
    public float getZ() {
        return z;
    }

    /**
     * Returns the transformed x coordinate of the atom
     */
    public float getTx() {
        return tx;
    }

    /**
     * Returns the transformed y coordinate of the atom
     */
    public float getTy() {
        return ty;
    }

    /**
     * Returns the transformed z coordinate of the atom
     */
    public float getTz() {
        return tz;
    }

    /**
     * Returns the color with which the atom is to be drawn when displayed
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns color according to the atom type
     */
    public Color getColorFromType() {
        if (atomColor.containsKey(name)) {
            return (Color) atomColor.get(name);
        } else {
            return colorDefault;
        }
    }

    /**
     * Returns true if the atom is selected by user
     */
    public boolean isSelected() {
        return select;
    }

    /**
     * Returns true if the atom is highlighted by user
     */
    public boolean isHighlighted() {
        return highlight;
    }

    /**
     * Return true if the atom need to be display
     */
    public boolean needDisplay() {
        return display;
    }

    /**
     * Return true if the atom is a carbon atom
     */
    public boolean isCarbon() {
        return name.equals("C");
    }

    /**
     * Return true if the atom is a hydrogen atom
     */
    public boolean isHydrogen() {
        return name.equals("H");
    }

    /**
     * Set the atom id
     *
     * @param id id
     * @see #getId
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the x coordinate of the atom
     *
     * @param x X-coordinate of the atom
     * @see #getX
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Set the y coordinate of the atom
     *
     * @param y Y-coordinate of the atom
     * @see #getY
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Set the z coordinate of the atom
     *
     * @param z Z-coordinate of the atom
     * @see #getZ
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     * Set the transformed x coordinate of the atom
     *
     * @param tx transformed X-coordinate of the atom
     * @see #getTx
     */
    public void setTx(float tx) {
        this.tx = tx;
    }

    /**
     * Set the transformed y coordinate of the atom
     *
     * @param ty transformed Y-coordinate of the atom
     * @see #getTy
     */
    public void setTy(float ty) {
        this.ty = ty;
    }

    /**
     * Set the transformed z coordinate of the atom
     *
     * @param tz transformed Z-coordinate of the atom
     * @see #getTz
     */
    public void setTz(float tz) {
        this.tz = tz;
    }

    /**
     * Set the state to indicate whether the atom is selected
     *
     * @param selectOrNot select state of the atom
     * @see #isSelected
     */
    public void setSelect(boolean selectOrNot) {
        select = selectOrNot;
    }

    /**
     * Set the state to indicate whether the atom is hightlighted
     *
     * @param highlightOrNot highlight state of the atom
     * @see #isHighlighted
     */
    public void setHighlight(boolean highlightOrNot) {
        highlight = highlightOrNot;
    }

    /**
     * Set the state to indicate whether the atom is to be formally displayed
     *
     * @param displayOrNot highlight state of the atom
     * @see #needDisplay
     */
    public void setDisplay(boolean displayOrNot) {
        display = displayOrNot;
    }

    /**
     * Set the color with which the atom is drawn
     *
     * @param color color for the atom
     * @see #getColor
     */
    public void setColor(Color color) {
        this.color = color;
    }
} // end of class Atom
