package org.jscience.astronomy.catalogs.hipparcos;

import java.util.Enumeration;
import java.util.Iterator;

import javax.media.j3d.TransformGroup;


//this package is rebundled from Hipparcos Java package from
//William O'Mullane
//http://astro.estec.esa.nl/Hipparcos/hipparcos_java.html
//mailto:hipparcos@astro.estec.esa.nl
/**
 * Iterate over the children of a transform group and return only stars
 */
class GroupIterator implements Iterator {
    /** DOCUMENT ME! */
    protected Object theNext;

    /** DOCUMENT ME! */
    protected Enumeration factory;

/**
     * Creates a new GroupIterator object.
     *
     * @param f DOCUMENT ME!
     */
    public GroupIterator(Enumeration f) {
        factory = f;
        preNext();
    }

    /**
     * DOCUMENT ME!
     */
    public void remove() {
    }

    /**
     * Should be straight forward but actually not that simple. Dont no
     * if there is a next until we get it !
     *
     * @return DOCUMENT ME!
     */
    public boolean hasNext() {
        return (theNext != null);
    }

    /**
     * Return theNext and getthe next object of possible. Next is
     * always running one object ahead so it can respond to hasNext().
     *
     * @return DOCUMENT ME!
     */
    public Object next() {
        if (theNext == null) {
            return null;
        }

        HipparcosStar ret = (HipparcosStar) theNext;
        preNext();

        return ret.getStar();
    }

    /**
     * Find the next object but just hold on to it.
     */
    protected void preNext() {
        try {
            boolean keepgoing = true;

            while (keepgoing) {
                theNext = factory.nextElement();

                if (theNext instanceof TransformGroup) {
                    theNext = ((TransformGroup) theNext).getChild(0);
                    keepgoing = !(theNext instanceof HipparcosStar);
                }
            }
        } catch (Exception e) {
            theNext = null;
        }
    }
}
