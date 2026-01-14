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
import org.jscience.architecture.traffic.util.ArrayEnumeration;
import org.jscience.architecture.traffic.xml.*;

import java.io.IOException;

import java.util.Dictionary;
import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class GenNeuralTLC extends TLController implements XMLSerializable,
    TwoStageLoader, InstantiationAssistant {
    /** A constant for the number of steps an individual may run */
    protected static int NUM_STEPS = 20;

    /** A constant for the chance that genes cross over */
    protected static float CROSSOVER_CHANCE = 0.1f;

    /** A constant for the chance that mutation occurs */
    protected static float MUTATION_CHANCE = 0.01f;

    /** A constant for the number of individuals */
    protected static int NUMBER_INDIVIDUALS = 20;

    /** A constant for the number of generations */
    protected static int NUMBER_GENERATIONS = 100;

    /** DOCUMENT ME! */
    protected final static String shortXMLName = "tlc-genneural";

    /** the number of nodes in the current infrastructure */
    protected int num_nodes;

    /** counter for the number of steps the simulation has ran yet */
    protected int num_step;

    /** counter for how many roadusers had to wait a turn */
    protected int num_wait;

    /** counter for the number of roadusers that did move */
    protected int num_move;

    /** the current GenNeural that is running */
    protected GenNeuralIndividual[] ind;

    /** The Population of GenNeuralIndividuals */
    protected GenNeuralPopulation pop;

    /**
     * The pseudo-random number generator for generating the chances
     * that certain events will occur
     */
    protected Random random;

    /** DOCUMENT ME! */
    protected Infrastructure infra;

    /** DOCUMENT ME! */
    protected InstantiationAssistant assistant;

/**
     * Creates a new GenNeural Algorithm. This TLC-algorithm is using genetic
     * and neural-network techniques to find an optimum in calculating the
     * gains for each trafficlight.  Till date it hasnt functioned that well.
     * Rather poorly, to just say it.
     *
     * @param i The infrastructure this algorithm will have to operate on
     */
    public GenNeuralTLC(Infrastructure i) {
        super(i);
        assistant = this;
        random = new Random();
    }

    /**
     * Changes the Infrastructure this algorithm is working on
     *
     * @param i The new infrastructure for which the algorithm has to be set up
     */
    public void setInfrastructure(Infrastructure i) {
        super.setInfrastructure(i);
        infra = i;
        pop = new GenNeuralPopulation(i);
        ind = pop.getIndividuals();
        num_wait = 0;
        num_step = 0;
        num_nodes = tld.length;
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
        if (num_step == NUM_STEPS) {
            /*GenNeuralIndividual indOl = ind[6];
            System.out.println("Next Individual gotten, previous:(wait,move,fitness):("+indOl.getWait()+","+indOl.getMove()+","+indOl.calcFitness());*/
            ind = pop.getNextIndividuals();
            num_wait = 0;
            num_step = 0;
            num_move = 0;
        }

        for (int i = 0; i < num_nodes; i++) {
            ind[i].calcGains();
        }

        for (int i = 0; i < num_nodes; i++) {
            int num_tl = tld[i].length;

            for (int j = 0; j < num_tl; j++) {
                float q = ind[i].getQValue(j);

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

        num_step++;

        return tld;
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
        // Should keep track of waits and moves per Node
        if ((_prevsign == _signnow) && (_prevpos == _posnow)) {
            // Previous sign is the same as the current one
            // Previous position is the same as the previous one
            // So, by definition we had to wait this turn. bad.
            // Find the Node where ru had to wait:
            int nodeId = _prevlane.getNodeLeadsTo().getId();
            num_wait += _ru.getSpeed();
            ind[nodeId].setWait(ind[nodeId].getWait() + _ru.getSpeed());
        } else {
            // Roaduser did move
            // Find the Node where ru moved:
            int nodeId = _prevlane.getNodeLeadsTo().getId();
            ind[nodeId].setMove(ind[nodeId].getMove() + _ru.getSpeed());
        }

        if ((_prevsign == _signnow) && (_prevpos == _posnow)) {
            // Previous sign is the same as the current one
            // Previous position is the same as the previous one
            // So, by definition we had to wait this turn. bad.
            // Find the Node where ru had to wait:
            int nodeId = _prevlane.getNodeLeadsTo().getId();
            num_wait += _ru.getSpeed();
            ind[nodeId].setWait(ind[nodeId].getWait() + _ru.getSpeed());
        } else if (_prevsign != _signnow) {
            //clearly passed a sign.
            // Roaduser did move
            // Find the Node where ru moved:
            int nodeId = _prevlane.getNodeLeadsTo().getId();
            ind[nodeId].setMove(ind[nodeId].getMove() + _ru.getSpeed());
        } else {
            // Didnt pass a Sign, so might've moved lessThanMax
            int old_move = num_move;
            int thesemoves = (_prevpos - _posnow);
            int thesewaits = _ru.getSpeed() - (_prevpos - _posnow);
            int nodeId = _prevlane.getNodeLeadsTo().getId();
            ind[nodeId].setMove(ind[nodeId].getMove() + thesemoves);
            ind[nodeId].setWait(ind[nodeId].getWait() + thesewaits);
        }
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
        NUM_STEPS = myElement.getAttribute("num-steps-s").getIntValue();
        CROSSOVER_CHANCE = myElement.getAttribute("cross-prob").getFloatValue();
        MUTATION_CHANCE = myElement.getAttribute("mut-prob").getFloatValue();
        NUMBER_INDIVIDUALS = myElement.getAttribute("num-ind").getIntValue();
        NUMBER_GENERATIONS = myElement.getAttribute("num-gen").getIntValue();
        num_nodes = myElement.getAttribute("num-nodes").getIntValue();
        num_step = myElement.getAttribute("num-steps-o").getIntValue();
        num_wait = myElement.getAttribute("num-wait").getIntValue();
        num_move = myElement.getAttribute("num-move").getIntValue();
        ind = (GenNeuralIndividual[]) XMLArray.loadArray(this, loader, assistant);
        pop = new GenNeuralPopulation();
        loader.load(this, pop);
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
        result.addAttribute(new XMLAttribute("num-steps-s", NUM_STEPS));
        result.addAttribute(new XMLAttribute("cross-pob", CROSSOVER_CHANCE));
        result.addAttribute(new XMLAttribute("mut-prob", MUTATION_CHANCE));
        result.addAttribute(new XMLAttribute("num-ind", NUMBER_INDIVIDUALS));
        result.addAttribute(new XMLAttribute("num-gen", NUMBER_GENERATIONS));
        result.addAttribute(new XMLAttribute("num-nodes", num_nodes));
        result.addAttribute(new XMLAttribute("num-steps-o", num_step));
        result.addAttribute(new XMLAttribute("num-wait", num_wait));
        result.addAttribute(new XMLAttribute("num-move", num_move));

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
        XMLArray.saveArray(ind, this, saver, "members");
        saver.saveObject(pop);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return "model." + shortXMLName;
    }

    // TwoStageLoader implementation
    /**
     * DOCUMENT ME!
     *
     * @param dictionaries DOCUMENT ME!
     *
     * @throws XMLInvalidInputException DOCUMENT ME!
     * @throws XMLTreeException DOCUMENT ME!
     */
    public void loadSecondStage(Dictionary dictionaries)
        throws XMLInvalidInputException, XMLTreeException {
        super.loadSecondStage(dictionaries);
        pop.loadSecondStage(dictionaries);
        XMLUtils.loadSecondStage(new ArrayEnumeration(ind), dictionaries);
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
        if (GenNeuralPopulation.class.equals(request)) {
            return new GenNeuralPopulation();
        } else if (GenNeuralIndividual.class.equals(request)) {
            return new GenNeuralIndividual();
        } else {
            throw new ClassNotFoundException(
                "GenNeural InstantiationAssistant cannot make instances of " +
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
        return GenNeuralPopulation.class.equals(request) ||
        GenNeuralIndividual.class.equals(request);
    }

    // Settings dialog
    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void showSettings(Controller c) {
        String[] descs = {
                "# of steps an individual may run",
                "Number of Individuals in Population", "Crossover chance",
                "Mutation chance"
            };
        int[] ints = { NUM_STEPS, NUMBER_INDIVIDUALS };
        float[] floats = { CROSSOVER_CHANCE, MUTATION_CHANCE };
        TLCSettings settings = new TLCSettings(descs, ints, floats);

        settings = doSettingsDialog(c, settings);

        NUM_STEPS = settings.ints[0];
        NUMBER_INDIVIDUALS = settings.ints[1];
        CROSSOVER_CHANCE = settings.floats[0];
        MUTATION_CHANCE = settings.floats[1];
        setInfrastructure(infra);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected class GenNeuralPopulation implements XMLSerializable,
        TwoStageLoader {
        /* the current group of Individuals that's showing off it's coolness */
        /** DOCUMENT ME! */
        protected int this_grp;

        /* a counter for the current generation of Individuals */
        /** DOCUMENT ME! */
        protected int this_gen;

        /* the array [#nodeIndividual][#cityIndividual] */
        /** DOCUMENT ME! */
        protected GenNeuralIndividual[][] inds;

/**
         * Creates a new population of groups of GenNeural Individuals
         *
         * @param infra The Infrastructure the population will run on
         */
        protected GenNeuralPopulation(Infrastructure infra) {
            inds = new GenNeuralIndividual[num_nodes][NUMBER_INDIVIDUALS];

            for (int i = 0; i < num_nodes; i++) {
                for (int k = 0; k < NUMBER_INDIVIDUALS; k++) {
                    inds[i][k] = new GenNeuralIndividual(infra, i);
                }
            }

            this_grp = 0;
            this_gen = 0;
        }

        // Empty constructor for loading
/**
         * Creates a new GenNeuralPopulation object.
         */
        protected GenNeuralPopulation() {
        }

        /**
         * DOCUMENT ME!
         *
         * @return the number of the current generation of individuals
         */
        protected int getCurrentGenerationNum() {
            return this_gen;
        }

        /**
         * DOCUMENT ME!
         *
         * @return the current individual
         */
        protected GenNeuralIndividual[] getIndividuals() {
            GenNeuralIndividual[] theseInds = new GenNeuralIndividual[num_nodes];

            for (int i = 0; i < num_nodes; i++) {
                theseInds[i] = inds[i][this_grp];
            }

            return theseInds;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected GenNeuralIndividual[] getNextIndividuals() {
            this_grp++;

            if (this_grp >= NUMBER_INDIVIDUALS) {
                evolve();
                this_grp = 0;
            }

            GenNeuralIndividual[] theseInds = new GenNeuralIndividual[num_nodes];

            for (int i = 0; i < num_nodes; i++) {
                theseInds[i] = inds[i][this_grp];
            }

            return theseInds;
        }

        /**
         * BubbleSorts an array of GenNeuralIndividuals on the
         * parameter of performance
         *
         * @param ar the array to be sorted
         *
         * @return the sorted array
         */
        protected GenNeuralIndividual[] sortIndsArr(GenNeuralIndividual[] ar) {
            int num_ar = ar.length;

            for (int j = 0; j < (num_ar - 1); j++) {
                for (int i = 0; i < (num_ar - 1 - j); i++) {
                    if (ar[i].getFitness() < ar[i + 1].getFitness()) {
                        GenNeuralIndividual temp = ar[i + 1];
                        ar[i + 1] = ar[i];
                        ar[i] = temp;
                    }
                }
            }

            return ar;
        }

        /**
         * Mates two GenNeuralIndividuals creating two new
         * GenNeuralIndividuals
         *
         * @param ma The mamma-GenNeuralIndividual
         * @param pa The pappa-GenNeuralIndividual
         *
         * @return an array of length 2 of two newborn GenNeuralIndividuals
         */
        protected GenNeuralIndividual[] mate(GenNeuralIndividual ma,
            GenNeuralIndividual pa) {
            //System.out.println("Mating ("+ma.getWait()+","+ma.getMove()+") and ("+pa.getWait()+","+pa.getMove()+")");
            byte[] maG = ma.getGenes();
            byte[] paG = pa.getGenes();

            GenNeuralIndividual[] kids = {
                    new GenNeuralIndividual(maG, paG, infra, ma.getNodeNr()),
                    new GenNeuralIndividual(maG, paG, infra, ma.getNodeNr())
                };

            return kids;
        }

        /**
         * DOCUMENT ME!
         *
         * @param group DOCUMENT ME!
         * @param totalFit DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected GenNeuralIndividual[] mateGroup(GenNeuralIndividual[] group,
            float totalFit) {
            int num_grpinds = group.length;
            GenNeuralIndividual[] newGroup = new GenNeuralIndividual[num_grpinds];
            GenNeuralIndividual ind1;
            GenNeuralIndividual ind2;

            for (int i = 0; i < num_grpinds; i += 2) {
                ind1 = getNext(group, totalFit);
                ind2 = getNext(group, totalFit);

                GenNeuralIndividual[] kids = mate(ind1, ind2);
                newGroup[i] = kids[0];
                newGroup[i + 1] = kids[1];
            }

            return newGroup;
        }

        /**
         * DOCUMENT ME!
         *
         * @param group DOCUMENT ME!
         * @param totalFit DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected GenNeuralIndividual getNext(GenNeuralIndividual[] group,
            float totalFit) {
            boolean found = false;
            int i = 0;
            int num_grp = group.length;
            GenNeuralIndividual ind1 = null;

            while (!found) {
                /*ind1 = group[i%num_grp];
                
                if(ind1!=null && (ind1.getFitness()/totalFit)> random.nextFloat()) {
                        found = true;
                }
                else
                        i++;
                */
                ind1 = group[i % num_grp];

                if (ind1 != null) {
                    found = true;
                }

                i++;
            }

            group[i % num_grp] = null;

            return ind1;
        }

        /**
         * Evolves the current generation of GenNeuralIndividuals
         * in the GenNeuralPopulation into a new one.
         */
        protected void evolve() {
            if (this_gen < NUMBER_GENERATIONS) {
                // Create a new generation.
                System.out.println("Evolving...");

                for (int i = 0; i < num_nodes; i++) {
                    GenNeuralIndividual[] group = inds[i];
                    float totalFit = 0;

                    for (int j = 0; j < NUMBER_INDIVIDUALS; j++) {
                        group[j].calcFitness();
                        totalFit += group[j].getFitness();
                    }

                    //group = sortIndsArr(group);
                    group = mateGroup(group, totalFit);

                    //inds[i] = (GenNeuralIndividual[]) ArrayUtils.randomizeArray(group);
                    inds[i] = group;
                }
            } else {
                // set the best individual as the ruling champ?
                System.out.println("Done with evolving");

                // what to do?
            }
        }

        // XMLSerializable implementation of GenNeuralPopulation
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
            this_grp = myElement.getAttribute("this-grp").getIntValue();
            this_gen = myElement.getAttribute("this-gen").getIntValue();
            inds = (GenNeuralIndividual[][]) XMLArray.loadArray(this, loader,
                    assistant);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws XMLCannotSaveException DOCUMENT ME!
         */
        public XMLElement saveSelf() throws XMLCannotSaveException {
            XMLElement result = new XMLElement("population");
            result.addAttribute(new XMLAttribute("this-grp", this_grp));
            result.addAttribute(new XMLAttribute("this-gen", this_gen));

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
            XMLArray.saveArray(inds, this, saver, "members");
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getXMLName() {
            return "model." + shortXMLName + ".population";
        }

        /**
         * DOCUMENT ME!
         *
         * @param parentName_ DOCUMENT ME!
         *
         * @throws XMLTreeException DOCUMENT ME!
         */
        public void setParentName(String parentName_) throws XMLTreeException {
            throw new XMLTreeException(
                "Operation not supported. GenNeuralPopulation has a fixed parentname");
        }

        // GenNeuralPopulation TwoStageLoader implementation
        /**
         * DOCUMENT ME!
         *
         * @param dictionaries DOCUMENT ME!
         *
         * @throws XMLTreeException DOCUMENT ME!
         * @throws XMLInvalidInputException DOCUMENT ME!
         */
        public void loadSecondStage(Dictionary dictionaries)
            throws XMLTreeException, XMLInvalidInputException {
            XMLUtils.loadSecondStage(new ArrayEnumeration(inds), dictionaries);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected class GenNeuralIndividual implements XMLSerializable,
        TwoStageLoader {
        /** DOCUMENT ME! */
        protected final static int num_hidden = 5;

        /** DOCUMENT ME! */
        protected int num_tls_here;

        /** DOCUMENT ME! */
        protected int num_tls_there;

        /* Our most precious of information. The building blocks of ourselves. Our DNA. */
        /** DOCUMENT ME! */
        protected byte[] dna;

        /* The array of float values that describe this GenNeuralIndividual. Hence the name of the array. */
        /** DOCUMENT ME! */
        protected float[][] me;

        /** DOCUMENT ME! */
        protected int wait;

        /** DOCUMENT ME! */
        protected int move;

        /** DOCUMENT ME! */
        protected float fitness;

        /** DOCUMENT ME! */
        protected float[] gains;

        /** DOCUMENT ME! */
        protected Node[] my_nodes;

        /** DOCUMENT ME! */
        protected int thisNode;

        /** DOCUMENT ME! */
        protected String myParentName = "model.tlc";

        /** DOCUMENT ME! */
        protected TwoStageLoaderData loadData = new TwoStageLoaderData();

/**
         * Creates a new GenNeuralIndividual, providing the Infrastructure it
         * should run on
         *
         * @param infra DOCUMENT ME!
         * @param node  DOCUMENT ME!
         */
        protected GenNeuralIndividual(Infrastructure infra, int node) {
            thisNode = node;
            num_tls_here = tld[node].length;
            num_tls_there = 0;

            int my_nodes_length = 1;

            Node thisNode = infra.getAllNodes()[node];
            Node other;
            int otherId;
            Road[] roads = thisNode.getAllRoads();

            for (int i = 0; i < roads.length; i++) {
                if (roads[i] != null) {
                    other = roads[i].getOtherNode(thisNode);
                    otherId = other.getId();
                    my_nodes_length++;
                    num_tls_there += tld[otherId].length;
                }
            }

            my_nodes = new Node[my_nodes_length];
            my_nodes[0] = thisNode;

            int index = 1;

            for (int i = 0; i < roads.length; i++) {
                if (roads[i] != null) {
                    my_nodes[index] = roads[i].getOtherNode(thisNode);
                    index++;
                }
            }

            int dnalen = ((num_tls_here + num_tls_there) * num_hidden) +
                num_hidden + (num_tls_here * num_hidden) + num_tls_here;
            dna = new byte[dnalen];

            // generate random DNA
            random.nextBytes(dna);

            // generate 'me' from DNA
            createMe();

            gains = new float[num_tls_here];

            wait = 0;
            move = 0;
            fitness = 0;

            //System.out.println("Done creating a GenNeuralInd");
        }

        // Empty constructor for loading
/**
         * Creates a new GenNeuralIndividual object.
         */
        protected GenNeuralIndividual() {
        }

/**
         * Creates a new GenNeuralIndividual, providing the reproduction genes
         * from daddy and mummy
         *
         * @param ma    DOCUMENT ME!
         * @param pa    DOCUMENT ME!
         * @param infra DOCUMENT ME!
         * @param node  DOCUMENT ME!
         */
        protected GenNeuralIndividual(byte[] ma, byte[] pa,
            Infrastructure infra, int node) {
            thisNode = node;

            num_tls_here = tld[node].length;
            num_tls_there = 0;

            int my_nodes_length = 1;

            Node thisNode = infra.getAllNodes()[node];
            Node other;
            int otherId;
            Road[] roads = thisNode.getAllRoads();

            for (int i = 0; i < roads.length; i++) {
                if (roads[i] != null) {
                    other = roads[i].getOtherNode(thisNode);
                    otherId = other.getId();
                    my_nodes_length++;
                    num_tls_there += tld[otherId].length;
                }
            }

            my_nodes = new Node[my_nodes_length];
            my_nodes[0] = thisNode;

            int index = 1;

            for (int i = 0; i < roads.length; i++) {
                if (roads[i] != null) {
                    my_nodes[index] = roads[i].getOtherNode(thisNode);
                    index++;
                }
            }

            // do crossover, mutate on ma, pa, -> one DNA string
            // todo;
            int dnalen = ma.length;
            byte[] newdna = new byte[dnalen];

            for (int i = 0; i < dnalen; i++) {
                if (random.nextFloat() < CROSSOVER_CHANCE) {
                    newdna[i] = pa[i];
                } else {
                    newdna[i] = ma[i];
                }
            }

            for (int i = 0; i < dnalen; i++) {
                // For each chromosome in the gene
                for (int j = 0; j < 8; j++) {
                    // For each gene in the chromosome
                    if (random.nextFloat() < MUTATION_CHANCE) {
                        newdna[i] ^= (byte) Math.pow(2, j);
                    }
                }
            }

            dna = newdna;

            // create 'me'
            createMe();

            gains = new float[num_tls_here];

            // reset wait, move, fitness
            wait = 0;
            move = 0;
            fitness = 0;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected int getNodeNr() {
            return thisNode;
        }

        /**
         * Building this individual from it's DNA
         */
        protected void createMe() {
            // First layer weights
            me = new float[4][];
            me[0] = new float[(num_tls_here + num_tls_there) * num_hidden];

            for (int i = 0; i < ((num_tls_here + num_tls_there) * num_hidden);
                    i++) {
                float thisGene = (float) dna[i];
                me[0][i] = thisGene / 128f;
            }

            // Hidden layer thresholds
            me[1] = new float[num_hidden];

            for (int i = 0; i < num_hidden; i++) {
                float thisGene = (float) dna[i +
                    ((num_tls_here + num_tls_there) * num_hidden)];
                me[1][i] = (thisGene + 128f) / 256f;
            }

            // Hidden layer weights
            me[2] = new float[num_hidden * num_tls_here];

            for (int i = 0; i < (num_hidden * num_tls_here); i++) {
                float thisGene = (float) dna[i +
                    ((num_tls_here + num_tls_there) * num_hidden) + num_hidden];
                me[2][i] = thisGene / 128f; // Value between -1 and 1
            }

            // Outer layer thresholds
            me[3] = new float[num_tls_here];

            for (int i = 0; i < num_tls_here; i++) {
                float thisGene = (float) dna[i +
                    ((num_tls_here + num_tls_there) * num_hidden) + num_hidden +
                    (num_hidden * num_tls_here)];
                me[3][i] = (thisGene + 128f) / 256f; // Value between 0 and 1
                                                     //System.out.println(me[3][i]+"");
            }
        }

        /**
         * DOCUMENT ME!
         */
        protected void calcGains() {
            // run our nn, see what the gains become for each our tls, put in gains[]
            try {
                int input_size = num_tls_here + num_tls_there;
                float[] input_set = new float[input_size];

                int input_index = 0;

                int num_nodes = my_nodes.length;

                for (int i = 0; i < num_nodes; i++) {
                    int thisNodeId = my_nodes[i].getId();
                    int num_lanes = tld[thisNodeId].length;

                    for (int j = 0; j < num_lanes; j++) {
                        Drivelane d;
                        input_set[input_index] = (float) tld[thisNodeId][j].getTL()
                                                                           .getLane()
                                                                           .getNumBlocksWaiting() / (float) tld[thisNodeId][j].getTL()
                                                                                                                              .getLane()
                                                                                                                              .getLength();
                        input_index++;
                    }
                }

                int output_size = num_tls_here;
                float[] hidden_output = new float[num_hidden];

                for (int j = 0; j < num_hidden; j++) {
                    // Each of the hidden nodes
                    for (int i = 0; i < input_size; i++) {
                        // Gets input from the first layer, times the weight of that layer
                        hidden_output[j] += (input_set[i] * me[0][i +
                        (input_size * j)]);
                    }

                    hidden_output[j] = transfer_function(hidden_output[j] -
                            me[1][j]);
                }

                for (int j = 0; j < output_size; j++) {
                    // for each of the outer layer nodes
                    gains[j] = 0;

                    for (int i = 0; i < num_hidden; i++) {
                        // gets input from the hidden nodes
                        gains[j] += (hidden_output[i] * me[2][i +
                        (num_hidden * j)]);
                    }

                    gains[j] = transfer_function(gains[j] - me[3][j]);
                }
            } catch (Exception e) {
                System.out.println(
                    "Something went wrong with calculating the Gain values...");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param value DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected float transfer_function(float value) {
            // This function will calculate the output value of the neuron
            if (value > 0) {
                return 1;
            } else {
                return 0;
            }
        }

        /**
         * Returns the genes of this GenNeuralIndividual
         *
         * @return DOCUMENT ME!
         */
        protected byte[] getGenes() {
            return dna;
        }

        /**
         * Returns the fitness of the current Individual, as was
         * set at iterating through this generation
         *
         * @return DOCUMENT ME!
         */
        protected float getFitness() {
            return fitness;
        }

        /**
         * Sets the fitness of this Individual
         *
         * @param fit DOCUMENT ME!
         */
        protected void setFitness(float fit) {
            fitness = fit;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected float calcFitness() {
            float fit = 0;
            float moves = (float) getMove();
            float waits = (float) getWait();

            if ((moves + waits) > 0) {
                fit = moves / (moves + waits);
            }

            setFitness(fit);

            return fit;
        }

        /**
         * Sets the number of cars that this Individual caused to
         * wait
         *
         * @param _wait
         */
        protected void setWait(int _wait) {
            wait = _wait;
        }

        /**
         * returns the number of Roadusers this Individual caused
         * to wait
         *
         * @return DOCUMENT ME!
         */
        protected int getWait() {
            return wait;
        }

        /**
         * Sets the amount of Roadusers that did move during this
         * Individual's lifespan
         *
         * @param _move
         */
        protected void setMove(int _move) {
            move = _move;
        }

        /**
         * Returns the amount of Roadusers that did move during
         * this Individuals lifespan
         *
         * @return DOCUMENT ME!
         */
        protected int getMove() {
            return move;
        }

        /**
         * Calculates the current gain of the given TrafficLight
         *
         * @param tl The position of the TrafficLight in the
         *        TLDecision[#this_ind's_node][]
         *
         * @return the gain-value of when this TrafficLight is set to green
         */
        protected float getQValue(int tl) {
            return gains[tl];
        }

        // XMLSerializable implementation of GenNeuralIndividual
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
            num_tls_here = myElement.getAttribute("num-tls-h").getIntValue();
            num_tls_there = myElement.getAttribute("num-tls-t").getIntValue();
            wait = myElement.getAttribute("wait").getIntValue();
            move = myElement.getAttribute("move").getIntValue();
            fitness = myElement.getAttribute("fitness").getFloatValue();
            thisNode = myElement.getAttribute("this-node").getIntValue();
            dna = (byte[]) XMLArray.loadArray(this, loader);
            me = (float[][]) XMLArray.loadArray(this, loader);
            gains = (float[]) XMLArray.loadArray(this, loader);
            loadData.nodeIds = (int[]) XMLArray.loadArray(this, loader);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws XMLCannotSaveException DOCUMENT ME!
         */
        public XMLElement saveSelf() throws XMLCannotSaveException {
            XMLElement result = new XMLElement("ind");
            result.addAttribute(new XMLAttribute("num-tls-h", num_tls_here));
            result.addAttribute(new XMLAttribute("num-tls-t", num_tls_there));
            result.addAttribute(new XMLAttribute("wait", wait));
            result.addAttribute(new XMLAttribute("move", move));
            result.addAttribute(new XMLAttribute("fitness", fitness));
            result.addAttribute(new XMLAttribute("this-node", thisNode));

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
            XMLArray.saveArray(dna, this, saver, "dna");
            XMLArray.saveArray(me, this, saver, "me");
            XMLArray.saveArray(gains, this, saver, "gains");
            XMLArray.saveArray(getNodeIdArray(), this, saver, "node-ids");
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getXMLName() {
            return myParentName + ".ind";
        }

        /**
         * DOCUMENT ME!
         *
         * @param newParentName DOCUMENT ME!
         */
        public void setParentName(String newParentName) {
            myParentName = newParentName;
        }

        /**
         * DOCUMENT ME!
         *
         * @param dictionaries DOCUMENT ME!
         *
         * @throws XMLInvalidInputException DOCUMENT ME!
         * @throws XMLTreeException DOCUMENT ME!
         */
        public void loadSecondStage(Dictionary dictionaries)
            throws XMLInvalidInputException, XMLTreeException {
            Dictionary nodeDictionary = (Dictionary) (dictionaries.get("node"));
            my_nodes = new Node[loadData.nodeIds.length];

            for (int t = 0; t < my_nodes.length; t++) {
                my_nodes[t] = (loadData.nodeIds[t] == -1) ? null
                                                          : (Node) (nodeDictionary.get(new Integer(
                            loadData.nodeIds[t])));
            }
        }

        /**
         * Gets an array with the ids of the nodes
         *
         * @return DOCUMENT ME!
         */
        public int[] getNodeIdArray() {
            int[] result = new int[my_nodes.length];

            for (int i = 0; i < my_nodes.length; i++)
                result[i] = (my_nodes[i] == null) ? (-1) : my_nodes[i].getId();

            return result;
        }

        // TwoStageLoader implementation of GenNeuralIndividual
        /**
         * DOCUMENT ME!
         *
         * @author $author$
         * @version $Revision: 1.3 $
         */
        class TwoStageLoaderData {
            /** DOCUMENT ME! */
            int[] nodeIds;
        }
    }
}
