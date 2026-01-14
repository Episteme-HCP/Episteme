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

package org.jscience.chemistry;

/**
 * The crystal class defines crystals (solid repetitive structure).
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//see http://www.gsw.edu/~tjw/crslnk.htm, http://www.users.bigpond.com/murphyjim/minerals.html
//http://www.geo.arizona.edu/AMS/amcsd.php  for minerals 
//or http://webmineral.com/
public abstract class Crystal extends Solid {

    private Molecule repeatingPart;

    //the 14 Bravais networks
    public final static int UNKNOWN = 0;
    public final static int CUBIC_P = 1;
    public final static int CUBIC_I = 2;
    public final static int CUBIC_F = 3;
    public final static int QUADRATIC_P = 4;
    public final static int QUADRATIC_I = 5;
    public final static int ORTHORHOMBIC_P = 6;
    public final static int ORTHORHOMBIC_C = 7;
    public final static int ORTHORHOMBIC_I = 8;
    public final static int ORTHORHOMBIC_F = 9;
    public final static int MONOCLINIC_P = 10;
    public final static int MONOCLINIC_C = 11;
    public final static int TRICLINIC = 12;
    public final static int HEXAGONAL = 13;
    public final static int RHOMBOEDRIC_R = 14;

    private int structure;

    private double a;//a length, defaults to 0
    private double b;//b length, defaults to 0
    private double c;//c length, defaults to 0

    private double alpha;//alpha angle, defaults to Math.PI/2
    private double beta;//beta angle, defaults to Math.PI/2
    private double gamma;//gamma angle, defaults to Math.PI/2

    public Crystal(Molecule repeatingPart) {

        super(repeatingPart, mass, chemicalConditions);
        if (repeatingPart != null) {
            this.repeatingPart = repeatingPart;
            this.structure = UNKNOWN;
            this.a = 0;
            this.b = 0;
            this.c = 0;
            this.alpha = Math.PI / 2;
            this.beta = Math.PI / 2;
            this.gamma = Math.PI / 2;
        } else
            throw new IllegalArgumentException("The Crystal constructor doesn't accept null arguments.");

    }

    //a crystal is a solid polymer which grows with a specific network at macromolecular scale and depends on the atomic scale
    //the actual crystal molecule is a repetition of specific formula
    //normally there is only one structure corresponding to the repeating part, but I don't know any way to compute that.
    public Crystal(Molecule repeatingPart, int structure, double a, double b, double c, double alpha, double beta, double gamma) {

        if (repeatingPart != null) {
            this.repeatingPart = repeatingPart;
            this.structure = structure;
            this.a = a;
            this.b = b;
            this.c = c;
            this.alpha = alpha;
            this.beta = beta;
            this.gamma = gamma;
        } else
            throw new IllegalArgumentException("The Crystal constructor doesn't accept null arguments.");

    }

    public Molecule getRepeatingPart() {

        return repeatingPart;

    }

    public int getStructure() {

        return structure;

    }

    public double getA() {

        return a;

    }

    public double getB() {

        return b;

    }

    public double getC() {

        return c;

    }

    public double getAlpha() {

        return alpha;

    }

    public double getBeta() {

        return beta;

    }

    public double getGamma() {

        return gamma;

    }

    //uses the repeating part to produce an actual small molecule
    public abstract Molecule getMolecule() {
    }

    //we could propose a method to compute specific heat
    //specific heat capacity is equal to 3R/M, where R is the gas constant and M is the molar mass (Dulong-Petit law)
    //for temperatures below 30 kelvins, s=e(-E/kt) (Debye model)

    //we could offer phonons equations

    //compute Bravais kind from a,b,c,alpha, beta, delta
    XXXX

//mineral group

//hardness

//color

//specific gravity

//extra comments

//also see http://en.wikipedia.org/wiki/Reciprocal_lattice
            XXX

//may be class in earth subpackage

}
