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

import org.jscience.architecture.traffic.Controller;
import org.jscience.architecture.traffic.infrastructure.*;
import org.jscience.architecture.traffic.xml.*;

import java.io.IOException;

import java.util.Random;


/**
 * Now with extra improved but still sometimes not too great performance!
 */
public class ACGJ3FixedValue extends TLController
    implements InstantiationAssistant, XMLSerializable {
    /** Constants used for calculating gains */
    protected static float DECREASE = 1.5f;

    /** Constants used for calculating gains */
    protected static float RUWAIT = 1f;

    /** Used constants for array addressing */
    protected static final int BUCK = 3;

    /** Used constants for array addressing */
    protected static final int WAIT = 2;

    /** Used constants for array addressing */
    protected static final int DECF = 1;

    /** Used constants for array addressing */
    protected static final int RUWF = 0;

    // Stuff for our XML parser
    /** DOCUMENT ME! */
    protected final static String shortXMLName = "tlc-acgj3-fixed-value";

    /** counter for the number of roadusers that did move */
    protected int num_nodes;

    /** the current ACGJ3Individual that is running */
    protected ACGJ3FixedValueIndividual ind;

    /**
     * The pseudo-random number generator for generating the chances
     * that certain events will occur
     */
    protected Random random;

    /** The infrastructure which this algorithm runs on */
    protected Infrastructure infra;

    /** DOCUMENT ME! */
    protected InstantiationAssistant assistant = this;

/**
     * Creates a new ACGJ3 Algorithm. This TLC-algorithm is using genetic
     * techniques to find an optimum in calculating the gains for each
     * trafficlight.  Till date it hasnt functioned that well. Rather poorly,
     * to just say it.
     *
     * @param _i The infrastructure this algorithm will have to operate on
     */
    public ACGJ3FixedValue(Infrastructure _i) { //super(_i);
        random = new Random();
        setInfrastructure(_i);
        System.out.println("ACGJ3.new ACGJ3 with " + tld.length + " nodes");
    }

    /**
     * Changes the Infrastructure this algorithm is working on
     *
     * @param _i The new infrastructure for which the algorithm has to be set
     *        up
     */
    public void setInfrastructure(Infrastructure _i) {
        super.setInfrastructure(_i);
        infra = _i;

        num_nodes = tld.length;
        ind = new ACGJ3FixedValueIndividual(_i);
    }

    /**
     * Calculates how every traffic light should be switched
     *
     * @return Returns a double array of TLDecision's. These are tuples of a
     *         TrafficLight and the gain-value of when it's set to green.
     *
     * @see gld.algo.tlc.TLDecision
     */
    public TLDecision[][] decideTLs() {
        for (int i = 0; i < num_nodes; i++) {
            int num_tl = tld[i].length;

            for (int j = 0; j < num_tl; j++) {
                float q = ind.getQValue(i, j);

                if (trackNode != -1) {
                    if (i == trackNode) {
                        Drivelane currentlane = tld[i][j].getTL().getLane();
                        boolean[] targets = currentlane.getTargets();
                        System.out.println("node: " + i + " light: " + j +
                            " gain: " + q + " " + targets[0] + " " +
                            targets[1] + " " + targets[2] + " " +
                            currentlane.getNumRoadusersWaiting());
                    }
                }

                tld[i][j].setGain(q);
            }
        }

        return tld;
    }

    /**
     * Resets the algorithm
     */
    public void reset() {
        ind.reset();
    }

    /**
     * Provides the TLC-algorithm with information about a roaduser
     * that has had it's go in the moveAllRoaduser-cycle. From this
     * information it can be distilled whether a roaduser has moved or had to
     * wait.
     *
     * @param _ru
     * @param _prevlane
     * @param _prevsign
     * @param _prevpos
     * @param _dlanenow
     * @param _signnow
     * @param _posnow
     * @param posMovs
     * @param _desired
     */
    public void updateRoaduserMove(Roaduser _ru, Drivelane _prevlane,
        Sign _prevsign, int _prevpos, Drivelane _dlanenow, Sign _signnow,
        int _posnow, PosMov[] posMovs, Drivelane _desired) {
        if ((_prevsign == _signnow) && (_prevpos == _posnow)) {
            // Previous sign is the same as the current one
            // Previous position is the same as the previous one
            // So, by definition we had to wait this turn. bad.
            if ((_prevsign.getType() == Sign.TRAFFICLIGHT) &&
                    (_prevsign.mayDrive() == true) && (_desired != null) &&
                    (_desired.getSign().getType() == Sign.TRAFFICLIGHT) &&
                    (_desired.getNodeLeadsTo().getType() == Node.JUNCTION)) {
                ind.siphonToOtherBucket((TrafficLight) _signnow,
                    (TrafficLight) _desired.getSign());
            }
        } else if ((_prevsign != _signnow) && (_signnow != null) &&
                _prevsign instanceof TrafficLight) {
            //clearly passed a trafficlight.
            //lets empty the bucket value a bit.
            //System.out.println("ACGJ3.updateRoaduserMove(..) Roaduser crossed Node.");
            ind.relaxBucket(_prevsign.getNode().getId(),
                (TrafficLight) _prevsign);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void showSettings(Controller c) {
        String[] descs = {
                "Standard weight per waiting Roaduser",
                "In/decrease factor for multiple waiters"
            };
        int[] ints = {  };
        float[] floats = { RUWAIT, DECREASE };
        TLCSettings settings = new TLCSettings(descs, ints, floats);

        settings = doSettingsDialog(c, settings);

        RUWAIT = settings.floats[0];
        DECREASE = settings.floats[1];
        setInfrastructure(infra);
    }

    // XMLSerializable implementation
    /**
     * DOCUMENT ME!
     *
     * @param myElement DOCUMENT ME!
     * @param loader DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLInvalidInputException DOCUMENT ME!
     */
    public void load(XMLElement myElement, XMLLoader loader)
        throws XMLTreeException, IOException, XMLInvalidInputException {
        super.load(myElement, loader);
        num_nodes = tld.length;
        DECREASE = myElement.getAttribute("decrease").getFloatValue();
        RUWAIT = myElement.getAttribute("ru-wait").getFloatValue();
        loader.load(this, ind = new ACGJ3FixedValueIndividual(infra));
    }

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
        result.addAttribute(new XMLAttribute("decrease", DECREASE));
        result.addAttribute(new XMLAttribute("ru-wait", RUWAIT));

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param saver DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public void saveChilds(XMLSaver saver)
        throws XMLTreeException, IOException, XMLCannotSaveException {
        super.saveChilds(saver);
        saver.saveObject(ind);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return "model." + shortXMLName;
    }

    // InstantiationAssistant implementation
    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ClassNotFoundException DOCUMENT ME!
     * @throws InstantiationException DOCUMENT ME!
     * @throws IllegalAccessException DOCUMENT ME!
     */
    public Object createInstance(Class request)
        throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        if (ACGJ3FixedValueIndividual.class.equals(request)) {
            return new ACGJ3FixedValueIndividual(infra);
        } else {
            throw new ClassNotFoundException(
                "ACGJ3 IntstantiationAssistant cannot make instances of " +
                request);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canCreateInstance(Class request) {
        return ACGJ3FixedValueIndividual.class.equals(request);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected class ACGJ3FixedValueIndividual implements XMLSerializable {
        /**
         * The array of float values that describe this
         * ACGJ3Individual. Hence the name of the array. Encoded are for every
         * TrafficLight in the Infrastructure: a float value 'weight per
         * waiting roaduser' a float value 'degrading/increasement weight per
         * Roaduser waiting further up the Queue' a float value 'gain-bucket'
         * in which all the weights of waiting Roadusers are collected a float
         * value 'number of Roadusers waiting' which is needed to make
         * decreasement of the gain-bucket possible
         */
        protected float[][][] me;

        //XML Parser stuff
        /** DOCUMENT ME! */
        protected String myParentName = "model.tlc";

/**
         * Creates a new ACGJ3Individual, providing the Infrastructure it
         * should run on
         *
         * @param infra DOCUMENT ME!
         */
        protected ACGJ3FixedValueIndividual(Infrastructure infra) {
            createMe();
        }

        /**
         * DOCUMENT ME!
         */
        protected void createMe() {
            me = new float[infra.getNumNodes()][][];

            for (int i = 0; i < infra.getNumNodes(); i++) {
                me[i] = new float[tld[i].length][];

                for (int j = 0; j < tld[i].length; j++) {
                    //Qweight per waiting roaduser, degradingfunction factor, build-up Q
                    me[i][j] = new float[4];
                    me[i][j][RUWF] = RUWAIT;
                    me[i][j][DECF] = DECREASE;
                    me[i][j][WAIT] = 0;
                    me[i][j][BUCK] = 0;
                }
            }
        }

        /**
         * Resets the buckets and waiting values for this
         * Individual
         */
        protected void reset() {
            for (int i = 0; i < me.length; i++) {
                for (int j = 0; j < me[i].length; j++) {
                    me[i][j][WAIT] = 0;
                    me[i][j][BUCK] = 0;
                }
            }
        }

        /**
         * Calculates the current gain of the given TrafficLight
         *
         * @param node the Id of the Node this TrafficLight belongs to
         * @param tl The position of the TrafficLight in the TLDecision[][]
         *
         * @return the gain-value of when this TrafficLight is set to green
         */

        /* Returns the gain for the provided TrafficLight to be set to Green */
        protected float getQValue(int node, int tl) {
            int num_waiting = tld[node][tl].getTL().getLane()
                                           .getNumRoadusersWaiting();
            me[node][tl][2] = num_waiting;

            float oldQ = me[node][tl][BUCK];
            float newQ = 0;
            float ruW = me[node][tl][RUWF];
            float dFS = me[node][tl][DECF];
            float dec = 1;

            for (int i = 0; i < num_waiting; i++) {
                newQ += (ruW * dec);
                dec *= dFS;
            }

            if ((oldQ + newQ) >= 100000) {
                me[node][tl][BUCK] = 10000;
            } else {
                me[node][tl][BUCK] = oldQ + newQ;
            }

            return newQ + oldQ;
        }

        /**
         * Empties the 'gain-value' bucket partly, which is being
         * filled when Roadusers are  waiting/voting for their TrafficLight to
         * be set to green.
         *
         * @param node The Id of the Node of which a bucket has to be emptied a
         *        bit
         * @param tl The TrafficLight of which a bucket has to be partly
         *        emptied in the TLDecision[][]
         */

        /* Empties the build-up Gain as a roaduser left the scene */
        protected void relaxBucket(int node, TrafficLight tl) {
            int tli = -1;
            int num_tls = me[node].length;

            for (int i = 0; i < num_tls; i++)
                if (tld[node][i].getTL() == tl) {
                    tli = i;

                    break;
                }

            float curWait = me[node][tli][WAIT];
            float curBuck = me[node][tli][BUCK];

            if (curWait > 0) {
                me[node][tli][BUCK] = curBuck * ((curWait - 1) / curWait);
                me[node][tli][WAIT]--;
            } else {
                me[node][tli][BUCK] = 0;
                me[node][tli][WAIT] = 0;
            }
        }

        /**
         * When a Roaduser may drive according to it's Sign, but
         * cant as the desired Drivelane is full, (some/all) of the current
         * gain-bucket of it's current TrafficLight is siphoned over to the
         * desired Drivelane, making it more probable that that lane will
         * move, making it possible for this Drivelane to move.
         *
         * @param here the Id of the Node at which the Roaduser is waiting
         * @param there The TrafficLight the Roaduser is waiting for
         */
        protected void siphonToOtherBucket(TrafficLight here, TrafficLight there) {
            int hereId = here.getId();
            int thereId = there.getId();

            int hereNodeId = here.getNode().getId();
            int thereNodeId = there.getNode().getId();

            int hereRelId = -1;
            int thereRelId = -1;

            TLDecision[] heretlds = tld[hereNodeId];
            int num_heretlds = heretlds.length;

            for (int i = 0; i < num_heretlds; i++) {
                if (heretlds[i].getTL() == here) {
                    hereRelId = i;
                }
            }

            TLDecision[] theretlds = tld[thereNodeId];
            int num_theretlds = theretlds.length;

            for (int j = 0; j < num_theretlds; j++) {
                //System.out.println("Checking: "+there+"=="+tld[thereNodeId][j].getTL());
                if (theretlds[j].getTL() == there) {
                    thereRelId = j;
                }
            }

            float buck = me[hereNodeId][hereRelId][BUCK];
            float curWait = me[hereNodeId][hereRelId][WAIT];

            if (curWait > 0) {
                if (me[thereNodeId][thereRelId][BUCK] < 100000) {
                    me[thereNodeId][thereRelId][BUCK] += (buck / curWait); // Aka: make sure that lane gets mmmmooving!
                }
            } else {
                //System.out.println("Something weird.");
                me[hereNodeId][hereRelId][BUCK] = 0;
                me[hereNodeId][hereRelId][WAIT] = 0;
            }
        }

        // XMLSerializable implementation of ACGJ3Individual
        /**
         * DOCUMENT ME!
         *
         * @param myElement DOCUMENT ME!
         * @param loader DOCUMENT ME!
         *
         * @throws XMLTreeException DOCUMENT ME!
         * @throws IOException DOCUMENT ME!
         * @throws XMLInvalidInputException DOCUMENT ME!
         */
        public void load(XMLElement myElement, XMLLoader loader)
            throws XMLTreeException, IOException, XMLInvalidInputException {
            me = (float[][][]) XMLArray.loadArray(this, loader);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws XMLCannotSaveException DOCUMENT ME!
         */
        public XMLElement saveSelf() throws XMLCannotSaveException {
            XMLElement result = new XMLElement("individual");

            return result;
        }

        /**
         * DOCUMENT ME!
         *
         * @param saver DOCUMENT ME!
         *
         * @throws XMLTreeException DOCUMENT ME!
         * @throws IOException DOCUMENT ME!
         * @throws XMLCannotSaveException DOCUMENT ME!
         */
        public void saveChilds(XMLSaver saver)
            throws XMLTreeException, IOException, XMLCannotSaveException {
            XMLArray.saveArray(me, this, saver, "me");
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getXMLName() {
            return myParentName + ".individual";
        }

        /**
         * DOCUMENT ME!
         *
         * @param parentName DOCUMENT ME!
         *
         * @throws XMLTreeException DOCUMENT ME!
         */
        public void setParentName(String parentName) throws XMLTreeException {
            myParentName = parentName;
        }
    }
}
