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
package org.jscience.physics.waves.optics.materials;

import org.jscience.util.IllegalDimensionException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
abstract public class Parameter extends Object implements Cloneable {
/**
     * Creates a new Parameter object.
     */
    public Parameter() {
    }

/**
     * Creates a new Parameter object.
     *
     * @param n DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public Parameter(double[] n) throws IllegalDimensionException {
    }

    // Every subclass must override this method returning its
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    abstract public String type();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    abstract public double[] asArray();

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    abstract public void setArray(double[] n) throws IllegalDimensionException;

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    abstract public double indexAtWavelength(double w);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
