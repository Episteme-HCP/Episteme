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
import org.jscience.architecture.traffic.util.StringUtils;
import org.jscience.architecture.traffic.xml.*;

import java.io.IOException;

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ACGJ4 extends TLController implements InstantiationAssistant,
    XMLSerializable {
    /** A constant for the number of steps an individual may run */
    protected static int NUM_STEPS = 500;

    /** A constant for the chance that genes cross over */
    protected static float CROSSOVER_CHANCE = 0.1f;

    /** A constant for the chance that mutation occurs */
    protected static float MUTATION_CHANCE = 0.001f;

    /** DOCUMENT ME! */
    protected static float ELITISM_FACTOR = 0.1f;

    /** A constant for the number of individuals */
    protected static int NUMBER_INDIVIDUALS = 20;

    /** A constant for the number of generations */
    protected static int NUMBER_GENERATIONS = 100;

    // Stuff for our XML parser
    /** DOCUMENT ME! */
    protected final static String shortXMLName = "tlc-acgj4";

    /** counter for the number of steps the simulation has ran yet */
    protected int num_step;

    /** counter for how many roadusers had to wait a turn */
    protected int num_wait;

    /** counter for the number of roadusers that did move */
    protected int num_move;

    /** counter for the number of roadusers that did move */
    protected int num_nodes;

    /** the current TLController being used to determine gains */
    protected TLController tlc;

    /** the current Individual that is running */
    protected Individual ind;

    /** The Population of Individuals */
    protected Population pop;

    /**
     * The pseudo-random number generator for generating the chances
     * that certain events will occur
     */
    protected Random random;

    /** DOCUMENT ME! */
    protected InstantiationAssistant assistant = this;

/**
     * Creates a new ACGJ3 Algorithm. This TLC-algorithm is using genetic
     * techniques to find an optimum in calculating the gains for each
     * trafficlight.  Till date it hasnt functioned that well. Rather poorly,
     * to just say it.
     *
     * @param i The infrastructure this algorithm will have to operate on
     */
    public ACGJ4(Infrastructure i) {
        random = new Random();
        tlc = new BestFirstTLC(i);
        setInfrastructure(i);
    }

    /**
     * Changes the Infrastructure this algorithm is working on
     *
     * @param _i The new infrastructure for which the algorithm has to be set
     *        up
     */
    public void setInfrastructure(Infrastructure _i) {
        super.setInfrastructure(_i);
        tlc.setInfrastructure(_i);
        infra = _i;
        num_nodes = tld.length;
        pop = new Population(_i);
        ind = pop.getIndividual();
        num_wait = 0;
        num_move = 0;
        num_step = 0;
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
            pop.getNextIndividual(num_wait, num_move);
            System.out.println("New Individual gotten, previous: (wait,move) (" +
                num_wait + "," + num_move + ")");
            num_wait = 0;
            num_step = 0;
            num_move = 0;
        }

        tld = tlc.decideTLs();

        for (int i = 0; i < num_nodes; i++) {
            int num_tl = tld[i].length;

            for (int j = 0; j < num_tl; j++) {
                float gain = (ind.getFactor(i, j)) * tld[i][j].getGain();

                if (trackNode != -1) {
                    if (i == trackNode) {
                        Drivelane currentlane = tld[i][j].getTL().getLane();
                        boolean[] targets = currentlane.getTargets();
                        System.out.println("N:" + i + " L:" + j + " G:" + gain +
                            " <:" + targets[0] + " |:" + targets[1] + " >:" +
                            targets[2] + " W:" +
                            currentlane.getNumRoadusersWaiting());
                    }
                }

                tld[i][j].setGain(gain);
            }
        }

        num_step++;

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
            num_wait += _ru.getSpeed();
        } else if ((_prevsign != _signnow) && (_signnow != null) &&
                _prevsign instanceof TrafficLight) {
            //clearly passed a trafficlight.
            num_move += _ru.getSpeed();
        } else {
            // Roaduser did move
            if (_prevsign != _signnow) {
                // Passed a Sign, thus moved max.
                num_move += _ru.getSpeed();
            } else {
                // Didnt pass a Sign, so might've moved lessThanMax
                num_move += (_prevpos - _posnow);
                num_wait += (_ru.getSpeed() - (_prevpos - _posnow));
            }
        }
    }

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
        NUM_STEPS = myElement.getAttribute("s-num-steps").getIntValue();
        CROSSOVER_CHANCE = myElement.getAttribute("co-prob").getFloatValue();
        MUTATION_CHANCE = myElement.getAttribute("mut-prob").getFloatValue();
        NUMBER_INDIVIDUALS = myElement.getAttribute("num-ind").getIntValue();
        num_step = myElement.getAttribute("o-num-steps").getIntValue();
        num_wait = myElement.getAttribute("num-wait").getIntValue();
        num_move = myElement.getAttribute("num-move").getIntValue();
        System.out.println("LoadingACGJ3: num_wait:" + num_wait + " num-move:" +
            num_move);
        pop = new Population();
        loader.load(this, pop);
        ind = pop.inds[myElement.getAttribute("ind-index").getIntValue()];
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
        result.addAttribute(new XMLAttribute("s-num-steps", NUM_STEPS));
        result.addAttribute(new XMLAttribute("co-prob", CROSSOVER_CHANCE));
        result.addAttribute(new XMLAttribute("mut-prob", MUTATION_CHANCE));
        result.addAttribute(new XMLAttribute("num-ind", NUMBER_INDIVIDUALS));
        result.addAttribute(new XMLAttribute("o-num-steps", num_step));
        result.addAttribute(new XMLAttribute("num-wait", num_wait));
        result.addAttribute(new XMLAttribute("num-move", num_move));
        System.out.println("SavingACGJ3: num_wait:" + num_wait + " num-move:" +
            num_move);
        result.addAttribute(new XMLAttribute("ind-index",
                StringUtils.getIndexObject(pop.inds, ind)));

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
        if (Population.class.equals(request)) {
            return new Population();
        } else if (Individual.class.equals(request)) {
            return new Individual();
        } else {
            throw new ClassNotFoundException(
                "ACGJ4 InstantiationAssistant cannot make instances of " +
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
        return Population.class.equals(request) ||
        Individual.class.equals(request);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected class Population implements XMLSerializable {
        /** the ID of the current Individual that's showing off it's coolness */
        protected int this_ind;

        /** a counter for the current generation of Individuals */
        protected int this_gen;

        /** The Individuals in this population */
        protected Individual[] inds;

/**
         * Creates a new population of Individuals
         *
         * @param infra The Infrastructure the population will run on
         */
        protected Population(Infrastructure infra) {
            inds = new Individual[NUMBER_INDIVIDUALS];

            for (int i = 0; i < NUMBER_INDIVIDUALS; i++)
                inds[i] = new Individual(infra);

            this_ind = 0;
            this_gen = 0;
        }

/**
         * Creates a new Population object.
         */
        protected Population() { // Empty constructor for loading
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
        protected Individual getIndividual() {
            return inds[this_ind];
        }

        /**
         * DOCUMENT ME!
         *
         * @param wait The performance of the current Individual
         * @param move DOCUMENT ME!
         *
         * @return the next ACGJ3Individual. Either one of this generation, or
         *         if all individuals in this generation have been used, it
         *         evolves into a new generation and returns a new Individual
         *         from that generation.
         */
        protected Individual getNextIndividual(int wait, int move) {
            inds[this_ind].setWait(wait);
            inds[this_ind].setMove(move);

            this_ind++;

            if (this_ind >= NUMBER_INDIVIDUALS) {
                evolve();
                this_ind = 0;
            }

            return inds[this_ind];
        }

        /**
         * BubbleSorts an array of ACGJ3Individuals on the
         * parameter of performance
         *
         * @param ar the array to be sorted
         *
         * @return the sorted array
         */
        protected Individual[] sortIndsArr(Individual[] ar) {
            int num_ar = ar.length;
            Individual temp;

            for (int j = 0; j < (num_ar - 1); j++)
                for (int i = 0; i < (num_ar - 1 - j); i++)
                    if (ar[i].getFitness() < ar[i + 1].getFitness()) {
                        temp = ar[i + 1];
                        ar[i + 1] = ar[i];
                        ar[i] = temp;
                    }

            return ar;
        }

        /**
         * Calculates the fitness of this Individual
         *
         * @param ind the individual of which the fitness has to be calculated
         *
         * @return the fitness of this individual
         */
        protected float calcFitness(Individual ind) {
            double w = (double) ind.getWait();
            double g = (double) ind.getMove();

            return (float) (g / (w + g));
        }

        /**
         * Mates two Individuals creating two new Individuals
         *
         * @param ma The mamma-Individual
         * @param pa The pappa-Individual
         *
         * @return an array of length 2 of two newborn Individuals
         */
        protected Individual[] mate(Individual ma, Individual pa) {
            byte[] ma1 = ma.getReproGenes(CROSSOVER_CHANCE, MUTATION_CHANCE);
            byte[] pa1 = pa.getReproGenes(CROSSOVER_CHANCE, MUTATION_CHANCE);
            byte[] ma2 = ma.getReproGenes(CROSSOVER_CHANCE, MUTATION_CHANCE);
            byte[] pa2 = pa.getReproGenes(CROSSOVER_CHANCE, MUTATION_CHANCE);

            Individual[] kids = {
                    new Individual(ma1, pa1), new Individual(ma2, pa2)
                };

            return kids;
        }

        /**
         * Evolves the current generation of Individuals in the
         * Population into a new one.
         */
        protected void evolve() {
            if (this_gen < NUMBER_GENERATIONS) {
                // Create a new generation.
                float avg_fit = Float.MIN_VALUE;

                // Create a new generation.
                float max_fit = Float.MIN_VALUE;

                // Create a new generation.
                float sum_fit = 0;
                float tot_wait = 0;
                float avg_wait = 0;
                float tot_move = 0;
                float avg_move = 0;

                for (int i = 0; i < NUMBER_INDIVIDUALS; i++) {
                    float fit = calcFitness(inds[i]);
                    inds[i].setFitness(fit);
                    sum_fit += fit;
                    max_fit = (fit > max_fit) ? fit : max_fit;
                    tot_wait += inds[i].getWait();
                    tot_move += inds[i].getMove();
                }

                avg_wait = tot_wait / NUMBER_INDIVIDUALS;
                avg_move = tot_move / NUMBER_INDIVIDUALS;
                avg_fit = sum_fit / NUMBER_INDIVIDUALS;

                System.out.println("Stats of prev. gen: (a-wait,a-move)=(" +
                    avg_wait + "," + avg_move + ")" + " (a-fit,mx-fit)=(" +
                    avg_fit + "," + max_fit + ")");
                System.out.println("Evolving...");

                // Sorts the current Individual-array on fitness, descending
                inds = sortIndsArr(inds);

                int num_mating = (int) Math.floor(NUMBER_INDIVIDUALS * (1 -
                        ELITISM_FACTOR));
                int num_elitism = (int) Math.ceil(NUMBER_INDIVIDUALS * ELITISM_FACTOR);

                if ((num_mating % 2) != 0) {
                    num_mating--;
                    num_elitism++;
                }

                Individual[] newInds = new Individual[NUMBER_INDIVIDUALS];

                // Tournament selection
                for (int i = 0; i < num_mating; i += 2) {
                    Individual mamma = null;
                    Individual pappa = null;
                    float chn_mum = random.nextFloat();
                    float chn_pap = random.nextFloat();
                    float fit_mum;
                    float sum_fit2 = 0;
                    float sum_fit3 = 0;
                    int index_mum = -1;

                    for (int j = 0; j < NUMBER_INDIVIDUALS; j++) {
                        sum_fit2 += (inds[j].getFitness() / sum_fit);

                        if (chn_mum <= sum_fit2) {
                            mamma = inds[j];
                            index_mum = j;
                            fit_mum = mamma.getFitness();
                            sum_fit2 = sum_fit - fit_mum;

                            break;
                        }
                    }

                    for (int j = 0; j < NUMBER_INDIVIDUALS; j++) {
                        if (j != index_mum) {
                            sum_fit3 += (inds[j].getFitness() / sum_fit2);

                            if (chn_pap <= sum_fit3) {
                                pappa = inds[j];

                                break;
                            }
                        }
                    }

                    //System.out.println("Mating...: "+mamma.getFitness()+","+pappa.getFitness());
                    Individual[] kids = mate(mamma, pappa);
                    newInds[i] = kids[0];
                    newInds[i + 1] = kids[1];
                }

                // Elitism
                for (int i = 0; i < num_elitism; i++) {
                    newInds[i + num_mating] = inds[i];
                }

                inds = newInds;
                this_gen++;
            } else {
                System.out.println("Done with evolving");

                // set the best individual as the ruling champ?
                // do nothing
                return;
            }
        }

        // XMLSerializable implementation of ACGJ3Population
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
            this_ind = myElement.getAttribute("this-ind").getIntValue();
            this_gen = myElement.getAttribute("this-gen").getIntValue();
            inds = (Individual[]) XMLArray.loadArray(this, loader, assistant);
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
            result.addAttribute(new XMLAttribute("this-ind", this_ind));
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
            XMLArray.saveArray(inds, this, saver, "individuals");
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getXMLName() {
            return "model.tlc.population";
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
                "Operation not supported. ACGJ3Population has a fixed parentname");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    protected class Individual implements XMLSerializable {
        /** DOCUMENT ME! */
        protected byte[] bytema;

        /** DOCUMENT ME! */
        protected byte[] bytepa;

        /** DOCUMENT ME! */
        protected float[][] me;

        /** DOCUMENT ME! */
        protected float fitness;

        /** DOCUMENT ME! */
        protected int wait;

        /** DOCUMENT ME! */
        protected int move;

        //XML Parser stuff
        /** DOCUMENT ME! */
        protected String myParentName = "model.tlc.population";

/**
         * Creates a new Individual, providing the Infrastructure it should run
         * on
         *
         * @param infra DOCUMENT ME!
         */
        protected Individual(Infrastructure infra) {
            int num_genes = 0;

            for (int i = 0; i < num_nodes; i++) {
                num_genes += tld[i].length;
            }

            bytema = new byte[num_genes];
            bytepa = new byte[num_genes];
            random.nextBytes(bytema);
            random.nextBytes(bytepa);
            createMe();
            wait = 0;
            move = 0;
        }

/**
         * Creates a new Individual, providing the reproduction genes from
         * daddy and mummy
         *
         * @param ma DOCUMENT ME!
         * @param pa DOCUMENT ME!
         */
        protected Individual(byte[] ma, byte[] pa) {
            bytema = ma;
            bytepa = pa;
            createMe();
            wait = 0;
            move = 0;
        }

/**
         * Constructor for loading
         */
        protected Individual() {
        }

        /**
         * DOCUMENT ME!
         */
        protected void createMe() {
            me = new float[num_nodes][];

            for (int i = 0; i < num_nodes; i++) {
                me[i] = new float[tld[i].length];

                for (int j = 0; j < tld[i].length; j++) {
                    float m0 = bytema[i + j] + 128f;
                    float p0 = bytepa[i + j] + 128f;

                    me[i][j] = (m0 + p0) / 256f; // (256+256)/256 = [0,2]
                }
            }
        }

        /**
         * Resets the buckets and waiting values for this
         * Individual
         */
        protected void reset() {
        }

        /**
         * Returns the genes of this ACGJ3Individual
         *
         * @return DOCUMENT ME!
         */
        protected byte[][] getGenes() {
            byte[][] ret = { bytema, bytepa };

            return ret;
        }

        /**
         * Returns some reproduction genes from this
         * ACGJ3Individual.
         *
         * @param cross DOCUMENT ME!
         * @param mutate DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected byte[] getReproGenes(float cross, float mutate) {
            byte[] ar1;
            byte[] ar2;

            if (random.nextFloat() > 0.5f) {
                ar1 = bytema;
                ar2 = bytepa;
            } else {
                ar1 = bytepa;
                ar2 = bytema;
            }

            /* Crossover? */
            int num_genes = ar1.length;
            int l_bound = 0;
            int r_bound = num_genes - 1;

            if (random.nextFloat() < CROSSOVER_CHANCE) {
                // We gonna do CrossssOver!
                while (l_bound < r_bound) {
                    float diff = r_bound - l_bound;
                    diff = diff / 2;

                    int dif = (int) Math.ceil(diff);

                    if (random.nextBoolean()) {
                        r_bound = r_bound - dif;
                    } else {
                        l_bound = l_bound + dif;
                    }
                }

                byte temp;

                for (int i = l_bound; i < (num_genes - 1); i++)
                    ar1[i] = ar2[i];
            }

            /* Mutation? */
            for (int i = 0; i < num_genes; i++)
                for (int j = 0; j < 8; j++)
                    if (random.nextFloat() < MUTATION_CHANCE) {
                        ar1[i] ^= (byte) Math.pow(2, j);
                    }

            return ar1;
        }

        /* Returns the Fitness of this individual as calculated elsewhere */
        protected float getFitness() {
            return fitness;
        }

        /* Sets the Fitness of this individual */
        protected void setFitness(float fit) {
            fitness = fit;
        }

        /* Sets the number of Roadusers this Individual made waiting */
        protected void setWait(int _wait) {
            wait = _wait;
        }

        /* Returns the number of Roadusers I caused to wait */
        protected int getWait() {
            return wait;
        }

        /* Sets the number of Roadusers this Individual made moving */
        protected void setMove(int _move) {
            move = _move;
        }

        /* Returns the number of Roadusers I caused to move */
        protected int getMove() {
            return move;
        }

        /**
         * Calculates the gain-factor for the given TrafficLight
         *
         * @param node the Id of the Node this TrafficLight belongs to
         * @param tl The position of the TrafficLight in the TLDecision[][]
         *
         * @return the factor that should be used to calculate the real
         *         gain-value
         */
        protected float getFactor(int node, int tl) {
            return me[node][tl];
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
            wait = myElement.getAttribute("wait").getIntValue();
            move = myElement.getAttribute("move").getIntValue();
            fitness = myElement.getAttribute("fitness").getFloatValue();
            bytepa = (byte[]) XMLArray.loadArray(this, loader);
            bytema = (byte[]) XMLArray.loadArray(this, loader);
            me = (float[][]) XMLArray.loadArray(this, loader);
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
            result.addAttribute(new XMLAttribute("wait", wait));
            result.addAttribute(new XMLAttribute("move", move));
            result.addAttribute(new XMLAttribute("fitness", fitness));

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
            XMLArray.saveArray(bytepa, this, saver, "pa");
            XMLArray.saveArray(bytema, this, saver, "ma");
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
