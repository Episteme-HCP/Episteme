/*
 * ContractedGaussian.java
 *
 * Created on July 22, 2004, 7:10 AM
 */

package org.jscience.chemistry.quantum.basis;

import org.jscience.chemistry.quantum.math.util.Point3D;

import java.util.ArrayList;

/**
 * The class defines a contracted gaussian and the operations on it.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class ContractedGaussian {

    /**
     * Holds value of property origin.
     */
    private Point3D origin;

    /**
     * Holds value of property powers, whoes array size is 3.
     */
    private Power powers;

    /**
     * Holds value of property primitives.
     * The Primitive Gaussians (PGs) that make up this Contracted Gaussian.
     */
    private ArrayList primitives;

    /**
     * Holds value of property exponents.
     * The list of exponents for this gaussian.
     */
    private ArrayList exponents;

    /**
     * Holds value of property coefficients.
     */
    private ArrayList coefficients;

    /**
     * Holds value of property normalization.
     */
    private double normalization;

    /**
     * Holds value of property primNorms.
     * <p/>
     * normalization factors for PGs
     */
    private ArrayList primNorms;

    /**
     * Creates a new instance of ContractedGaussian
     *
     * @param origin - the (x, y, z) on which this gaussian is centered
     * @param powers - the powers of this gaussian
     */
    public ContractedGaussian(Point3D origin, Power powers) {
        this.origin = origin;
        this.powers = powers;

        primitives = new ArrayList(10);
        exponents = new ArrayList(10);
        coefficients = new ArrayList(10);
        primNorms = new ArrayList(10);

        this.normalization = 1;
    }

    /**
     * Adds a primitive gaussian (PG) to this contracted gaussian list
     *
     * @param exponent    the exponent for this PG
     * @param coefficient the coefficient of this PG
     */
    public void addPrimitive(double exponent, double coefficient) {
        primitives.add(new PrimitiveGaussian(origin, powers,
                exponent, coefficient));

        exponents.add(new Double(exponent));
        coefficients.add(new Double(coefficient));
    }

    /**
     * Getter for property origin.
     *
     * @return Value of property origin.
     */
    public Point3D getOrigin() {
        return this.origin;
    }

    /**
     * Setter for property origin.
     *
     * @param origin New value of property origin.
     */
    public void setOrigin(Point3D origin) {
        this.origin = origin;
    }

    /**
     * Getter for property powers.
     *
     * @return Value of property powers.
     */
    public Power getPowers() {
        return this.powers;
    }

    /**
     * Setter for property powers.
     *
     * @param powers New value of property powers.
     */
    public void setPowers(Power powers) {
        this.powers = powers;
    }

    /**
     * Getter for property primitives.
     *
     * @return Value of property primitives.
     */
    public ArrayList getPrimitives() {
        return this.primitives;
    }

    /**
     * Setter for property primitives.
     *
     * @param primitives New value of property primitives.
     */
    public void setPrimitives(ArrayList primitives) {
        this.primitives = primitives;
    }

    /**
     * Getter for property exponents.
     *
     * @return Value of property exponents.
     */
    public ArrayList getExponents() {
        return this.exponents;
    }

    /**
     * Setter for property exponents.
     *
     * @param exponents New value of property exponents.
     */
    public void setExponents(ArrayList exponents) {
        this.exponents = exponents;
    }

    /**
     * Getter for property coefficients.
     *
     * @return Value of property coefficients.
     */
    public ArrayList getCoefficients() {
        return this.coefficients;
    }

    /**
     * Setter for property coefficients.
     *
     * @param coefficients New value of property coefficients.
     */
    public void setCoefficients(ArrayList coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * Getter for property normalization.
     *
     * @return Value of property normalization.
     */
    public double getNormalization() {
        return this.normalization;
    }

    /**
     * Setter for property normalization.
     *
     * @param normalization New value of property normalization.
     */
    public void setNormalization(double normalization) {
        this.normalization = normalization;
    }

    /**
     * Normalize this basis function.
     */
    public void normalize() {
        normalization = 1.0 / Math.sqrt(this.overlap(this));

        for (int i = 0; i < primitives.size(); i++) {
            primNorms.add(new Double(((PrimitiveGaussian)
                    primitives.get(i)).getNormalization()));
        } // end for
    }

    /**
     * Overlap matrix element with another ContractedGaussian
     *
     * @param cg the ContractedGaussian with which the overlap is to be
     *           be determined.
     * @return the overlap value
     */
    public double overlap(ContractedGaussian cg) {
        double sij = 0.0;
        int i, j;

        ArrayList cgPrimitives = cg.getPrimitives();

        for (i = 0; i < primitives.size(); i++) {
            PrimitiveGaussian iPG = (PrimitiveGaussian) primitives.get(i);
            for (j = 0; j < cgPrimitives.size(); j++) {
                PrimitiveGaussian jPG = (PrimitiveGaussian) cgPrimitives.get(j);

                sij += iPG.getCoefficient() * jPG.getCoefficient()
                        * iPG.overlap(jPG);
            } // end for
        } // end for

        return normalization * cg.normalization * sij;
    }

    /**
     * Kinetic Energy (KE) matrix element with another ContractedGaussian
     *
     * @param cg the ContractedGaussian with which KE is to be determined.
     * @return the KE value
     */
    public double kinetic(ContractedGaussian cg) {
        double tij = 0.0;
        int i, j;

        ArrayList cgPrimitives = cg.getPrimitives();

        for (i = 0; i < primitives.size(); i++) {
            PrimitiveGaussian iPG = (PrimitiveGaussian) primitives.get(i);
            for (j = 0; j < cgPrimitives.size(); j++) {
                PrimitiveGaussian jPG = (PrimitiveGaussian) cgPrimitives.get(j);

                tij += iPG.getCoefficient() * jPG.getCoefficient()
                        * iPG.kinetic(jPG);
            } // end for
        } // end for

        return normalization * cg.normalization * tij;
    }

    /**
     * Nuclear matrix element with another ContractedGaussian and a center
     *
     * @param cg     the ContractedGaussian with which nuclear interaction
     *               is to determined.
     * @param center the center at which nuclear energy is to be computed
     * @return the nuclear value
     */
    public double nuclear(ContractedGaussian cg, Point3D center) {
        double vij = 0.0;
        int i, j;

        ArrayList cgPrimitives = cg.getPrimitives();

        for (i = 0; i < primitives.size(); i++) {
            PrimitiveGaussian iPG = (PrimitiveGaussian) primitives.get(i);
            for (j = 0; j < cgPrimitives.size(); j++) {
                PrimitiveGaussian jPG = (PrimitiveGaussian) cgPrimitives.get(j);

                vij += iPG.getCoefficient() * jPG.getCoefficient()
                        * iPG.nuclear(jPG, center);
            } // end for
        } // end for

        return normalization * cg.normalization * vij;
    }

    /**
     * Getter for property primNorms.
     *
     * @return Value of property primNorms.
     */
    public ArrayList getPrimNorms() {
        return this.primNorms;
    }

    /**
     * Setter for property primNorms.
     *
     * @param primNorms New value of property primNorms.
     */
    public void setPrimNorms(ArrayList primNorms) {
        this.primNorms = primNorms;
    }

    /**
     * overloaded toString()
     */
    public String toString() {
        return "Origin : " + origin + " Powers : " + powers
                + " Normalization : " + normalization
                + " Primitives : " + primitives;
    }
} // end of class ContractedGaussian
