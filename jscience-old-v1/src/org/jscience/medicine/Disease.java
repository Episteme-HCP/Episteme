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

package org.jscience.medicine;

import org.jscience.biology.*;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


/**
 * A class representing a disease.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//look at the ICD for example disease although there is no field here to support their coding standards
//http://www.wolfbane.com/icd/
//http://www.eicd.com/ (official site)
public class Disease extends Pathology {
    //constant used both for transmission and origin
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0; //unknown or unreferenced

    //multiple sources can be considered
    /** DOCUMENT ME! */
    public final static int GENETIC = 1; //1<<0;

    /** DOCUMENT ME! */
    public final static int WATER = 2; //1<<1;//poisoned source

    /** DOCUMENT ME! */
    public final static int FOOD = 4; //poisoned source

    /** DOCUMENT ME! */
    public final static int AIR = 8; //breathing poisoned air

    /** DOCUMENT ME! */
    public final static int CONTACT = 16; //for example through sex or blood

    /** DOCUMENT ME! */
    public final static int PARASIT = 32; //a parasit of the specie (should set the corresponding specie in the vector Set)

    //origin, mutually exclusive
    /** DOCUMENT ME! */
    public final static int VIRUS = 1;

    /** DOCUMENT ME! */
    public final static int BACTERIA = 2;

    /** DOCUMENT ME! */
    public final static int PRION = 3; //protein induced

    /** DOCUMENT ME! */
    public final static int DNA = 4; //genetic diseases

    /** DOCUMENT ME! */
    private int transmission; //how the disease in transmitted

    /** DOCUMENT ME! */
    private int origin; //what is the corresponding microorganism

    /** DOCUMENT ME! */
    private Set vectors; //the specie that propagates the virus but is immune

    /** DOCUMENT ME! */
    private Set targets; //the infected species

    /** DOCUMENT ME! */
    private Object microorganism;

    //vectors should be a Set of species (may be empty)
    /**
     * Creates a new Disease object.
     *
     * @param name DOCUMENT ME!
     * @param transmission DOCUMENT ME!
     * @param origin DOCUMENT ME!
     * @param vectors DOCUMENT ME!
     * @param targets DOCUMENT ME!
     */
    public Disease(String name, int transmission, int origin, Set vectors,
        Set targets) {
        super(name);

        Iterator iterator;
        boolean valid;

        if ((vectors != null) && (targets != null) && (targets.size() > 0)) {
            iterator = targets.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Species;
            }

            if (valid) {
                iterator = vectors.iterator();
                valid = true;

                while (iterator.hasNext() && valid) {
                    valid = iterator.next() instanceof Species;
                }

                if (valid) {
                    this.transmission = transmission;
                    this.origin = origin;
                    this.vectors = vectors;
                    this.targets = targets;
                    microorganism = null;
                } else {
                    throw new IllegalArgumentException(
                        "The set of vectors should contain only Species.");
                }
            } else {
                throw new IllegalArgumentException(
                    "The set of targets should contain only Species.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Disease constructor can't have null arguments (and name and targets shouldn't be empty).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTransmission() {
        return transmission;
    }

    /**
     * DOCUMENT ME!
     *
     * @param transmission DOCUMENT ME!
     */
    public void setTransmission(int transmission) {
        this.transmission = transmission;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getOrigin() {
        return origin;
    }

    /**
     * DOCUMENT ME!
     *
     * @param origin DOCUMENT ME!
     */
    public void setOrigin(int origin) {
        this.origin = origin;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getVectors() {
        return vectors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vector DOCUMENT ME!
     */
    public void addVector(Vector vector) {
        vectors.add(vector);
    }

    /**
     * DOCUMENT ME!
     *
     * @param vector DOCUMENT ME!
     */
    public void removeVector(Vector vector) {
        vectors.remove(vector);
    }

    /**
     * DOCUMENT ME!
     *
     * @param vectors DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setVectors(Set vectors) {
        Iterator iterator;
        boolean valid;

        if (vectors != null) {
            iterator = vectors.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Species;
            }

            if (valid) {
                this.vectors = vectors;
            } else {
                throw new IllegalArgumentException(
                    "The set of vectors should contain only Species.");
            }
        } else {
            throw new IllegalArgumentException("The vectors can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getTargets() {
        return targets;
    }

    /**
     * DOCUMENT ME!
     *
     * @param target DOCUMENT ME!
     */
    public void addTarget(Species target) {
        targets.add(target);
    }

    /**
     * DOCUMENT ME!
     *
     * @param target DOCUMENT ME!
     */
    public void removeTarget(Species target) {
        targets.remove(target);
    }

    /**
     * DOCUMENT ME!
     *
     * @param targets DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setTargets(Set targets) {
        Iterator iterator;
        boolean valid;

        if ((targets != null) && (targets.size() > 0)) {
            iterator = targets.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Species;
            }

            if (valid) {
                this.targets = targets;
            } else {
                throw new IllegalArgumentException(
                    "The set of targets should contain only Species.");
            }
        } else {
            throw new IllegalArgumentException(
                "The targets can't be null or empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getMicroorganism() {
        return microorganism;
    }

    //the microorganism corresponding to the disease, should be either a DNA (genetic), a Cell (bacteria), a Virus, or a Protein (prion)
    /**
     * DOCUMENT ME!
     *
     * @param microorganism DOCUMENT ME!
     */
    public void setMicroorganism(Object microorganism) {
        if (microorganism != null) {
            if (microorganism instanceof Virus) {
                origin = VIRUS;
            } else if (microorganism instanceof Cell) {
                origin = BACTERIA;
            } else if (microorganism instanceof Protein) {
                origin = PRION;
            } else if (microorganism instanceof DNA) {
                origin = DNA;
            } else {
                throw new IllegalArgumentException(
                    "You can only set micro-organisms that are Virus, Cells, or Proteins.");
            }
        }

        this.microorganism = microorganism;
    }
}
