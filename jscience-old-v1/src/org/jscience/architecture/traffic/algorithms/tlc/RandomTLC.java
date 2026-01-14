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

package org.jscience.architecture.traffic.algorithms.tlc;

import org.jscience.architecture.traffic.infrastructure.Drivelane;
import org.jscience.architecture.traffic.infrastructure.Infrastructure;
import org.jscience.architecture.traffic.infrastructure.Roaduser;
import org.jscience.architecture.traffic.infrastructure.Sign;
import org.jscience.architecture.traffic.xml.XMLCannotSaveException;
import org.jscience.architecture.traffic.xml.XMLElement;

import java.util.Random;


/**
 * This controller will switch TrafficLights at random.
 *
 * @author Group Algorithms
 * @version 1.0
 */
public class RandomTLC extends TLController {
    /** DOCUMENT ME! */
    protected final static String shortXMLName = "tlc-random";

    /** DOCUMENT ME! */
    protected int num_nodes;

    /** DOCUMENT ME! */
    protected Random seed;

/**
     * Creates a new RandomTLC object.
     *
     * @param i     DOCUMENT ME!
     * @param _seed DOCUMENT ME!
     */
    public RandomTLC(Infrastructure i, Random _seed) {
        super(i);
        seed = _seed;
        num_nodes = tld.length;
    }

/**
     * Creates a new RandomTLC object.
     *
     * @param infra DOCUMENT ME!
     */
    public RandomTLC(Infrastructure infra) {
        super(infra);
        seed = new Random();
        num_nodes = tld.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void setInfrastructure(Infrastructure i) {
        super.setInfrastructure(i);
        num_nodes = tld.length;
    }

    /**
     * Calculates how every traffic light should be switched
     *
     * @return DOCUMENT ME!
     *
     * @see gld.algo.tlc.TLDecision
     */
    public TLDecision[][] decideTLs() {
        //System.out.println("RandomTLC.decideTLs");
        int num_lanes;

        for (int i = 0; i < num_nodes; i++) {
            num_lanes = tld[i].length;

            for (int j = 0; j < num_lanes; j++)
                tld[i][j].setGain(seed.nextFloat());
        }

        return tld;
    }

    /**
     * DOCUMENT ME!
     *
     * @param _ru DOCUMENT ME!
     * @param _prevlane DOCUMENT ME!
     * @param _prevsign DOCUMENT ME!
     * @param _prevpos DOCUMENT ME!
     * @param _dlanenow DOCUMENT ME!
     * @param _signnow DOCUMENT ME!
     * @param _posnow DOCUMENT ME!
     * @param posMovs DOCUMENT ME!
     * @param desired DOCUMENT ME!
     */
    public void updateRoaduserMove(Roaduser _ru, Drivelane _prevlane,
        Sign _prevsign, int _prevpos, Drivelane _dlanenow, Sign _signnow,
        int _posnow, PosMov[] posMovs, Drivelane desired) {
    }

    // XMLSerializable implementation
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        XMLElement result = super.saveSelf();
        result.setName(shortXMLName);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return "model." + shortXMLName;
    }
}
