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

package org.jscience.physics.waves.optics.materials;

import org.jscience.util.IllegalDimensionException;

import java.util.Hashtable;
import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Material {
    /** DOCUMENT ME! */
    static final String parseSeparator = "\t";

    /** DOCUMENT ME! */
    private static final double lambdad = 0.5876;

    /** DOCUMENT ME! */
    private static final double lambdaF = 0.4861;

    /** DOCUMENT ME! */
    private static final double lambdaC = 0.6563;

    /** DOCUMENT ME! */
    private String name = "vaccum";

    /** DOCUMENT ME! */
    private boolean valid = false;

    /** DOCUMENT ME! */
    public Hashtable Parameters = new Hashtable();

    /** DOCUMENT ME! */
    private String DefaultFormula = "Constant";

/**
     * Creates a new Material object.
     */
    public Material() {
        super();
    }

    /*public Material( Material m )
    {
        super();
        name = new String( m.name );
        DefaultFormula = m.defaultFormula;
        valid = m.valid;
    
        Parameters = (Hashtable)m.Parameters.clone();
    }*/
    public Material(String name, Parameter p) {
        super();
        this.name = new String(name);

        try {
            addParameterSet(p);
            DefaultFormula = p.type();
            valid = true;
        } catch (Exception e) {
            e.printStackTrace();
            valid = false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     * @param def DOCUMENT ME!
     *
     * @throws InvalidMethodTypeException DOCUMENT ME!
     */
    public void addParameterSet(Parameter p, boolean def)
        throws InvalidMethodTypeException {
        if (p != null) {
            Parameters.put(p.type(), p);
            DefaultFormula = p.type();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     *
     * @throws InvalidMethodTypeException DOCUMENT ME!
     */
    public void addParameterSet(Parameter p) throws InvalidMethodTypeException {
        addParameterSet(p, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     * @param def DOCUMENT ME!
     *
     * @throws InvalidMethodTypeException DOCUMENT ME!
     * @throws NumberFormatException DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public void addParameterSet(String s, boolean def)
        throws InvalidMethodTypeException, NumberFormatException,
            IllegalDimensionException {
        Parameter p = null;
        StringTokenizer st = new StringTokenizer(s);
        int size = st.countTokens();
        int i = 0;

        if (size > 0) {
            p = NewParameterSet(st.nextToken());

            if (size > 1) {
                double[] d = new double[size - 1];

                while (st.hasMoreTokens()) {
                    d[i] = (Double.valueOf(st.nextToken())).doubleValue();
                    i++;
                }

                p.setArray(d);
            }

            addParameterSet(p, def);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @throws InvalidMethodTypeException DOCUMENT ME!
     * @throws NumberFormatException DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public void addParameterSet(String s)
        throws InvalidMethodTypeException, NumberFormatException,
            IllegalDimensionException {
        addParameterSet(s, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InvalidMethodTypeException DOCUMENT ME!
     */
    private Parameter NewParameterSet(String s)
        throws InvalidMethodTypeException {
        if (s.equalsIgnoreCase("Constant")) {
            return new ConstantParameter();
        }

        if (s.equalsIgnoreCase("Schott")) {
            return new SchottParameters();
        }

        if (s.equalsIgnoreCase("Herzberger")) {
            return new HerzbergerParameters();
        }

        if (s.equalsIgnoreCase("Conrady")) {
            return new ConradyParameters();
        }

        if (s.equalsIgnoreCase("Sellmeier1")) {
            return new Sellmeier1Parameters();
        }

        if (s.equalsIgnoreCase("Sellmeier2")) {
            return new Sellmeier2Parameters();
        }

        if (s.equalsIgnoreCase("Sellmeier3")) {
            return new Sellmeier3Parameters();
        }

        if (s.equalsIgnoreCase("Sellmeier4")) {
            return new Sellmeier4Parameters();
        }

        if (s.equalsIgnoreCase("HoO1")) {
            return new HoO1Parameters();
        }

        if (s.equalsIgnoreCase("HoO2")) {
            return new HoO2Parameters();
        } else {
            throw new InvalidMethodTypeException(s);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setName(String s) {
        name = new String(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     */
    public void setDefaultFormula(String f) {
        if (Parameters.containsKey(f)) {
            DefaultFormula = f;
            valid = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param wavelength DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double indexAtWavelength(double wavelength) {
        return indexAtWavelength(wavelength, DefaultFormula);
    }

    /**
     * DOCUMENT ME!
     *
     * @param wavelength DOCUMENT ME!
     * @param fd DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double indexAtWavelength(double wavelength, String fd) {
        if (Parameters.containsKey(fd)) {
            return ((Parameter) Parameters.get(fd)).indexAtWavelength(wavelength);
        } else {
            return 0.0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double abbeNumber() {
        return abbeNumber(DefaultFormula);
    }

    /**
     * DOCUMENT ME!
     *
     * @param fd DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double abbeNumber(String fd) {
        if (Parameters.containsKey(fd)) {
            double nd;
            double nF;
            double nC;

            nd = ((Parameter) Parameters.get(fd)).indexAtWavelength(lambdad);
            nF = ((Parameter) Parameters.get(fd)).indexAtWavelength(lambdaF);
            nC = ((Parameter) Parameters.get(fd)).indexAtWavelength(lambdaC);

            return (nd - 1) / (nF - nC);
        } else {
            return 0.0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double abbeIndex() {
        return abbeIndex(DefaultFormula);
    }

    /**
     * DOCUMENT ME!
     *
     * @param fd DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double abbeIndex(String fd) {
        if (Parameters.containsKey(fd)) {
            return ((Parameter) Parameters.get(fd)).indexAtWavelength(lambdad);
        } else {
            return 0.0;
        }
    }
}
