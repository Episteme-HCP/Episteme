/**
 * Title:        NewProj
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright:    Copyright (c) imt
 * </p>
 *
 * <p>
 * Company:      imt
 * </p>
 *
 * <p></p>
 */
package org.jscience.physics.waves.optics.rays;

import java.util.Enumeration;
import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Ray {
    /** DOCUMENT ME! */
    private Vector raypoints;

    //private Color color;
    /**
     * Creates a new Ray object.
     */
    public Ray() {
        super();
        raypoints = new Vector(5, 5);

        //color = Color.blue;
    }

    /*public Color getColor()
    {
        return new Color( color.getRed(), color.getGreen(), color.getBlue() );
    }
    
    public void setColor( Color color )
    {
        this.color = new Color( color.getRed(), color.getGreen(), color.getBlue() );
    }*/
    public Ray(RayPoint r) {
        super();
        raypoints = new Vector(5, 5);
        raypoints.addElement(r);

        //color = Color.blue;
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        //RayPoint r = (RayPoint)(raypoints.firstElement());
        raypoints.setSize(1);

        //raypoints.addElement( r );
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     */
    public void setWavelength(double w) {
        Enumeration e = raypoints.elements();

        while (e.hasMoreElements()) {
            RayPoint o = (RayPoint) e.nextElement();
            o.setWavelength(w);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector raypoints() {
        return raypoints;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void append(RayPoint r) {
        raypoints.addElement(r);
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void append(Ray r) {
        int i;
        int s = r.raypoints().size();

        if (s != 0) {
            for (i = 0; i < s; i++)
                raypoints.addElement(r.raypoints().elementAt(i));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RayPoint last() {
        return (RayPoint) (raypoints.lastElement());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean empty() {
        return raypoints.isEmpty();
    }
}
