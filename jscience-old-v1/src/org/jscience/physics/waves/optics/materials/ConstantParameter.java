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
public class ConstantParameter extends Parameter implements Cloneable {
    /** DOCUMENT ME! */
    static final int nParameters = 1;

    /** DOCUMENT ME! */
    private double n = 1;

/**
     * Creates a new ConstantParameter object.
     */
    public ConstantParameter() {
        super();
    }

/**
     * Creates a new ConstantParameter object.
     *
     * @param n DOCUMENT ME!
     */
    public ConstantParameter(double n) {
        super();
        this.n = n;
    }

/**
     * Creates a new ConstantParameter object.
     *
     * @param p DOCUMENT ME!
     */
    public ConstantParameter(ConstantParameter p) {
        super();
        this.n = p.n;
    }

/**
     * Creates a new ConstantParameter object.
     *
     * @param n DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public ConstantParameter(double[] n) throws IllegalDimensionException {
        super();
        setArray(n);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String type() {
        return "Constant";
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double indexAtWavelength(double w) {
        return n;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] asArray() {
        double[] a = { n };

        return a;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public void setArray(double[] n) throws IllegalDimensionException {
        if (n.length != nParameters) {
            throw new IllegalDimensionException();
        }

        //WrongArraySizeException( type(), n.length, nParameters );
        this.n = n[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double N() {
        return n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     */
    public void setN(double n) {
        this.n = n;
    }
}
