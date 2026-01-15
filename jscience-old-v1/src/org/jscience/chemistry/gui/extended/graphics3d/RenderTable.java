package org.jscience.chemistry.gui.extended.graphics3d;

import org.jscience.chemistry.gui.extended.molecule.Atom;

import java.awt.*;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.media.j3d.Link;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.SharedGroup;

import javax.vecmath.Color3f;


// Singleton for rendering info lookup
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class RenderTable {
    /** DOCUMENT ME! */
    static RenderTable table = null;

    /** The Radius of the sticks */
    public static float STICK_RADIUS = 0.14f;

    /** The Radius of the balls int ball and stick mode */
    public static float BALL_RADIUS = 0.3f;

    /** DOCUMENT ME! */
    static final int STICK_QUALITY = 7;

    /** The look-up table for the atom colors */
    static Hashtable atomColors = new Hashtable();

    /** The look-up table for the atom radii */
    static Hashtable atomRadii = new Hashtable();

    static {
        // Fill in some default atomColors
        atomColors.put("C", new Color(0, 160, 160)); //Color.cyan);
        atomColors.put("H", new Color(190, 190, 190));
        atomColors.put("N", Color.blue);
        atomColors.put("O", Color.red);
        atomColors.put("P", new Color(255, 110, 0));
        atomColors.put("S", new Color(240, 180, 0)); // 245,180,0 //180,180,0
        atomColors.put("Cl", new Color(0, 196, 0));
        atomColors.put("F", new Color(255, 0, 255));
        atomColors.put("Br", new Color(255, 0, 255));
        atomColors.put("I", new Color(255, 0, 255));

        // X: it's unknown
        // maybe this should be <unknown>
        atomColors.put("X", new Color(255, 0, 255));

        // Fill in some default atom radii
        atomRadii.put("C", new Float(1.53));
        atomRadii.put("H", new Float(1.08));
        atomRadii.put("N", new Float(1.48));
        atomRadii.put("O", new Float(1.36));
        atomRadii.put("P", new Float(1.75));
        atomRadii.put("S", new Float(1.70));
        atomRadii.put("Cl", new Float(1.65));
        atomRadii.put("F", new Float(1.30));
        atomRadii.put("Br", new Float(1.80));
        atomRadii.put("I", new Float(2.05));
        atomRadii.put("X", new Float(1.4));
    }

    /** DOCUMENT ME! */
    Color3f aColor = new Color3f(1.0f, 1.0f, 1.0f);

    /** DOCUMENT ME! */
    Color3f eColor = new Color3f(0.0f, 0.0f, 0.0f);

    /** DOCUMENT ME! */
    Color3f dColor = new Color3f(0.1f, 0.1f, 0.1f);

    /** DOCUMENT ME! */
    Color3f sColor = new Color3f(1.0f, 1.0f, 1.0f);

    /** DOCUMENT ME! */
    int renderStyle = RenderStyle.WIRE;

    /** DOCUMENT ME! */
    Hashtable nodeCache = new Hashtable();

    /** DOCUMENT ME! */
    Vector renderList = new Vector(100);

/**
     * non public constructor
     */
    private RenderTable() {
    }

    /**
     * initializes the table
     */
    static void init() {
        table = new RenderTable();
    }

    /**
     * the accessor method for rthe instance of the RenderTable
     *
     * @return DOCUMENT ME!
     */
    public static RenderTable getTable() {
        if (table == null) {
            init();
        }

        return table;
    }

    /**
     * get the radius for atom a
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getRadius(Atom a) {
        String name = getLookupKey(a);
        Float r = null;

        if (atomRadii.containsKey(name)) {
            r = (Float) atomRadii.get(name);
        } else {
            r = (Float) atomRadii.get("X");
        }

        if (r == null) {
            return 0.0f;
        }

        return r.floatValue();
    }

    /**
     * Get the lookup key for atom a, currently the name
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    String getLookupKey(Atom a) {
        return a.getName();
    }

    /**
     * Get the (awt)color for atom a
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getColor(Atom a) {
        String name = getLookupKey(a);
        Color c = null;

        if (atomColors.containsKey(name)) {
            c = (Color) atomColors.get(name);
        } else {
            c = (Color) atomColors.get("X");
        }

        return c;
    }

    /**
     * get the R,G,B values (between 0.0f and 1.0f) of the color for
     * atom a
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float[] getRGBFloats(Atom a) {
        Color c = getColor(a);

        if (c == null) {
            return null;
        }

        float[] res = new float[3];
        float d = 1.0f / 255.0f;
        res[0] = (float) c.getRed() * d;
        res[1] = (float) c.getGreen() * d;
        res[2] = (float) c.getBlue() * d;

        return res;
    }

    /**
     * get the material for atom a
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Material getMaterial(Atom a) {
        float[] f = getRGBFloats(a);
        Color3f aColor = new Color3f(f[0], f[1], f[2]);
        Color3f dColor = new Color3f(f[0], f[1], f[2]);

        return new Material(aColor, eColor, dColor, sColor, 20.0f);
    }

    /**
     * Set the current rendering style
     *
     * @param style DOCUMENT ME!
     */
    public void setStyle(int style) {
        renderStyle = style;

        //Loop over renderList
        Enumeration enumeration = renderList.elements();

        while (enumeration.hasMoreElements()) {
            RenderStyle rs = (RenderStyle) enumeration.nextElement();
            rs.setStyle(style);
        }
    }

    /**
     * What is the current rendering style ?
     *
     * @return DOCUMENT ME!
     */
    public int getStyle() {
        return renderStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pref DOCUMENT ME!
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    String makeCacheKey(String pref, Atom a) {
        String k = getLookupKey(a);
        String key = pref + "_" + k;

        return key;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getSharedAtomGroup(Atom a) {
        String pref = "sag"; // SharedAtomGroup
        String key = makeCacheKey(pref, a);
        SharedAtomGroup sag = (SharedAtomGroup) nodeCache.get(key);

        if (sag != null) {
            //System.out.println("Using shared Cyl");
            return new Link(sag);
        }

        sag = new SharedAtomGroup(a);
        addCachedNode(key, sag);

        return new Link(sag);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getSharedBondGroup(Atom a) {
        String pref = "sbg"; // SharedBondGroup
        String key = makeCacheKey(pref, a);
        SharedBondGroup sbg = (SharedBondGroup) nodeCache.get(key);

        if (sbg != null) {
            //System.out.println("Using shared Cyl");
            return new Link(sbg);
        }

        sbg = new SharedBondGroup(a);
        addCachedNode(key, sbg);

        return new Link(sbg);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param gr DOCUMENT ME!
     */
    void addCachedNode(String key, SharedGroup gr) {
        nodeCache.put(key, gr);
        renderList.addElement(gr);
    }
}
