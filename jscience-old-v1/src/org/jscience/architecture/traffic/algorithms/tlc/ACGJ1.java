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
import org.jscience.architecture.traffic.xml.XMLUtils;

import java.io.IOException;

import java.util.*;


/**
 * This controller has been designed by the GLD-Algo group.  This algorithm
 * creates for every new cycle of iteration a genetic population and tries to
 * find the optimal city-wide configuration. This algorithm prevents deadlocks
 * and stimulates green waves. This algorithm prevents endless waiting of
 * roadusers. The fitness function "calcfitness" is the most important
 * function of this algorithm.
 *
 * @author Group Algorithms
 * @version 1.0
 */

// ideeen voor ACGJ1
// dit algoritme zal als het opstart ongeveer zo handelen als longest queue, maar:
// * het voorkomt oneindig wachten van weggebruikers
// * er wordt gebruik gemaakt van genetische algoritmen
// * het algoritme kijkt d.m.v. genetische dingen over heel het netwerk om zo groene golven te maken etc. etc.
public class ACGJ1 extends TLController implements XMLSerializable,
    TwoStageLoader, InstantiationAssistant {
    /** DOCUMENT ME! */
    public final static String shortXMLName = "tlc-acgj1";

    /** DOCUMENT ME! */
    protected static float mutationFactor = 0.05f;

    /** DOCUMENT ME! */
    protected static int populationSize = 200;

    /** DOCUMENT ME! */
    protected static int maxGeneration = 100;

    /** DOCUMENT ME! */
    Population genPopulation;

    /** DOCUMENT ME! */
    int ruMoves;

    /** DOCUMENT ME! */
    int avgWaitingTime;

    /** DOCUMENT ME! */
    protected Infrastructure infra;

    /** DOCUMENT ME! */
    protected InstantiationAssistant assistant;

/**
     * Creates a new ACGJ1 object.
     *
     * @param i DOCUMENT ME!
     */
    public ACGJ1(Infrastructure i) {
        super(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void setInfrastructure(Infrastructure i) {
        super.setInfrastructure(i);
        genPopulation = new Population(i);
        genPopulation.initialize();
        ruMoves = 0;
    }

    /**
     * Calculates how every traffic light should be switched
     *
     * @return DOCUMENT ME!
     *
     * @see gld.algo.tlc.TLDecision
     */
    public TLDecision[][] decideTLs() {
        int maxLength;
        int num_lanes;
        int num_nodes;
        int maxId;
        int temp_len;
        int ru_pos;
        num_nodes = tld.length;

        for (int i = 0; i < num_nodes; i++) {
            maxLength = -1;
            maxId = -1;
            num_lanes = tld[i].length;

            for (int j = 0; j < num_lanes; j++)
                tld[i][j].setGain(tld[i][j].getTL().getLane()
                                           .getNumRoadusersWaiting());
        }

        try {
            genPopulation.resetGeneration();
            genPopulation.evaluateGeneration();

            for (int i = 0; i < maxGeneration; i++) {
                if (genPopulation.createNextGeneration()) {
                    break;
                }

                genPopulation.evaluateGeneration();
            }
        } catch (Exception e) {
            System.out.println(e + "");
            e.printStackTrace();
        }

        genPopulation.sortMembers();

        Person p = genPopulation.getFirstPerson();

        if (p != null) {
            try {
                p.fillTld(tld);
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }

        ruMoves = 0;

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
        ruMoves++;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void showSettings(Controller c) {
        String[] descs = {
                "Population Size", "Maximal generation number",
                "Mutation factor"
            };
        float[] floats = { mutationFactor };
        int[] ints = { populationSize, maxGeneration };
        TLCSettings settings = new TLCSettings(descs, ints, floats);

        settings = doSettingsDialog(c, settings);

        mutationFactor = settings.floats[0];
        populationSize = settings.ints[0];
        maxGeneration = settings.ints[1];
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
        ruMoves = myElement.getAttribute("ru-moves").getIntValue();
        avgWaitingTime = myElement.getAttribute("avg-waittime").getIntValue();
        mutationFactor = myElement.getAttribute("mut-factor").getFloatValue();
        populationSize = myElement.getAttribute("pop-size").getIntValue();
        maxGeneration = myElement.getAttribute("max-gen").getIntValue();
        genPopulation = new Population(infra);
        loader.load(this, genPopulation);
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
        result.addAttribute(new XMLAttribute("ru-moves", ruMoves));
        result.addAttribute(new XMLAttribute("avg-waittime", avgWaitingTime));
        result.addAttribute(new XMLAttribute("mut-factor", mutationFactor));
        result.addAttribute(new XMLAttribute("pop-size", populationSize));
        result.addAttribute(new XMLAttribute("max-gen", maxGeneration));

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
        saver.saveObject(genPopulation);
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
        genPopulation.loadSecondStage(dictionaries);
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
            return new Population(infra);
        } else if (Person.class.equals(request)) {
            return new Person(infra,
                (genPopulation == null) ? new Random() : genPopulation.rnd);
        } else if (NodeInfo.class.equals(request)) {
            return new NodeInfo();
        } else {
            throw new ClassNotFoundException(
                "ACGJ1 InstantiationAssistant cannot make instances of " +
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
        Person.class.equals(request) || NodeInfo.class.equals(request);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class Population implements XMLSerializable, TwoStageLoader {
        /** DOCUMENT ME! */
        Vector members;

        /** DOCUMENT ME! */
        Infrastructure infra;

        /** DOCUMENT ME! */
        Random rnd;

        /** DOCUMENT ME! */
        float currentMax;

        /** DOCUMENT ME! */
        int numMaxTimes;

/**
         * Creates a new Population object.
         *
         * @param infra DOCUMENT ME!
         */
        public Population(Infrastructure infra) {
            this.infra = infra;
            members = new Vector();
            rnd = new Random();
            currentMax = 0;
            numMaxTimes = 0;
            initialize();
        }

        // Initializes this population
        /**
         * DOCUMENT ME!
         */
        public void initialize() {
            members.removeAllElements();

            if (ACGJ1.populationSize < 10) {
                ACGJ1.populationSize = 10;
            }

            for (int i = 0; i < ACGJ1.populationSize; i++) {
                Person p = new Person(infra, rnd);
                p.randomizeData();
                members.addElement(p);
            }

            currentMax = 0;
            numMaxTimes = 0;
        }

        /**
         * DOCUMENT ME!
         */
        public void resetGeneration() {
            float total = members.size();
            float current = 0;

            for (Enumeration e = members.elements(); e.hasMoreElements();) {
                Person p = (Person) e.nextElement();

                if (rnd.nextFloat() < (current / total)) {
                    p.randomizeData();
                }

                current++;
            }

            currentMax = 0;
            numMaxTimes = 0;
        }

        /**
         * DOCUMENT ME!
         *
         * @throws InfraException DOCUMENT ME!
         */
        public void evaluateGeneration() throws InfraException {
            calcFitnesses();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean createNextGeneration() {
            int popSize = members.size();

            if (calcRelativeFitnesses()) {
                return true;
            }

            // Kill some members of this population, about one half is killed here
            for (Iterator i = members.iterator(); i.hasNext();) {
                Person p = (Person) i.next();

                if (p.relFitness < (4 * rnd.nextFloat() * rnd.nextFloat() * rnd.nextFloat())) {
                    i.remove();
                }
            }

            // Generate new childs
            sortMembers();

            int memSize = members.size();

            if (memSize == 0) {
                initialize();
            } else {
                while (members.size() < popSize) {
                    float rand = rnd.nextFloat();
                    int p1 = (int) ((1.0f - (rand * rand)) * memSize);
                    rand = rnd.nextFloat();

                    int p2 = (int) ((1.0f - (rand * rand)) * memSize);

                    if ((p1 >= memSize) || (p2 >= memSize)) {
                        continue;
                    }

                    Person parent1 = (Person) members.elementAt(p1);
                    Person parent2 = (Person) members.elementAt(p2);
                    Person child = generateChild(parent1, parent2);
                    members.addElement(child);
                }
            }

            // Mutate this generation
            for (Enumeration e = members.elements(); e.hasMoreElements();) {
                Person p = (Person) e.nextElement();
                mutatePerson(p);
            }

            return false;
        }

        /**
         * DOCUMENT ME!
         *
         * @throws InfraException DOCUMENT ME!
         */
        private void calcFitnesses() throws InfraException {
            for (Enumeration e = members.elements(); e.hasMoreElements();) {
                Person p = (Person) e.nextElement();
                p.calcFitness();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private boolean calcRelativeFitnesses() {
            float min = 0;
            float max = 0;

            boolean first = true;

            for (Enumeration e = members.elements(); e.hasMoreElements();) {
                Person p = (Person) e.nextElement();

                if (first || (p.fitness < min)) {
                    min = p.fitness;
                }

                if (first || (p.fitness > max)) {
                    max = p.fitness;
                }

                first = false;
            }

            if (min == max) {
                return true;
            }

            if ((max - min) < 0.01) {
                return true;
            }

            for (Enumeration e = members.elements(); e.hasMoreElements();) {
                Person p = (Person) e.nextElement();
                p.relFitness = (p.fitness - min) / (max - min);
            }

            if (max == currentMax) {
                numMaxTimes++;

                if (numMaxTimes > 4) {
                    return true;
                }
            } else {
                numMaxTimes = 0;
                currentMax = max;
            }

            return false;
        }

        /**
         * DOCUMENT ME!
         *
         * @param parent1 DOCUMENT ME!
         * @param parent2 DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Person generateChild(Person parent1, Person parent2) {
            Person child = new Person(infra, rnd);

            for (int i = 0; i < child.ndinf.length; i++) {
                // choose random one parent
                int config = parent1.ndinf[i].config;

                if (rnd.nextFloat() >= 0.5) {
                    config = parent2.ndinf[i].config;
                }

                child.ndinf[i].config = config;
            }

            return child;
        }

        /**
         * DOCUMENT ME!
         *
         * @param person DOCUMENT ME!
         */
        public void mutatePerson(Person person) {
            if (rnd.nextFloat() <= mutationFactor) {
                int mutNode = (int) (rnd.nextFloat() * person.ndinf.length);
                mutateNodeInfo(person.ndinf[mutNode]);

                // Another mutation is possible too
                if (mutationFactor < 0.8) {
                    mutatePerson(person);
                }
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param ndi DOCUMENT ME!
         */
        public void mutateNodeInfo(NodeInfo ndi) {
            ndi.config = (int) (rnd.nextFloat() * ndi.configsize);
        }

        /**
         * DOCUMENT ME!
         */
        public void sortMembers() {
            //sort members so that the first has the highest fitness
            Person p1 = null;

            //sort members so that the first has the highest fitness
            Person p2 = null;

            for (int i = 0; i < members.size(); i++) {
                p1 = (Person) members.elementAt(i);

                for (int j = members.size() - 1; j >= i; j--) {
                    p2 = (Person) members.elementAt(j);

                    // if p2>p1...
                    if (p2.fitness > p1.fitness) {
                        members.setElementAt(p2, i);
                        members.setElementAt(p1, j);
                        p1 = p2;
                    }
                }
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Person getFirstPerson() {
            if (members.size() == 0) {
                return null;
            }

            return (Person) (members.elementAt(0));
        }

        // XMLSerializable implementation of Population
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
            numMaxTimes = myElement.getAttribute("max-times").getIntValue();
            currentMax = myElement.getAttribute("current-max").getFloatValue();
            members = (Vector) (XMLArray.loadArray(this, loader, assistant));
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
            result.addAttribute(new XMLAttribute("current-max", currentMax));
            result.addAttribute(new XMLAttribute("max-times", numMaxTimes));

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
            XMLArray.saveArray(members, this, saver, "members");
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
                "Operation not supported. ACGJ1Population has a fixed parentname");
        }

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
            XMLUtils.loadSecondStage(members.elements(), dictionaries);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class Person implements XMLSerializable, TwoStageLoader {
        /** DOCUMENT ME! */
        Infrastructure infra;

        /** DOCUMENT ME! */
        NodeInfo[] ndinf;

        /** DOCUMENT ME! */
        Random rnd;

        /** DOCUMENT ME! */
        float fitness;

        /** DOCUMENT ME! */
        float relFitness;

        /** DOCUMENT ME! */
        protected String myParentName = "model.tlc.population";

/**
         * Creates a new Person object.
         *
         * @param infra DOCUMENT ME!
         * @param rnd   DOCUMENT ME!
         */
        public Person(Infrastructure infra, Random rnd) {
            this.infra = infra;
            this.rnd = rnd;

            Node[] allNodes = infra.getAllNodes();
            ndinf = new NodeInfo[allNodes.length];

            for (int j = 0; j < allNodes.length; j++) {
                Node nd = allNodes[j];
                ndinf[nd.getId()] = new NodeInfo(nd);
            }

            fitness = -1;
            relFitness = -1;
        }

        /**
         * DOCUMENT ME!
         */
        public void randomizeData() {
            for (int i = 0; i < ndinf.length; i++) {
                NodeInfo ndi = ndinf[i];
                ndi.config = (int) (ndi.configsize * rnd.nextFloat());
            }
        }

        /**
         * This is the fitness-function, it now returns the number
         * of cars that can drive with the given config.
         *
         * @throws InfraException DOCUMENT ME!
         */
        public void calcFitness() throws InfraException {
            float totalFitness = 0.0f;

            for (int i = 0; i < ndinf.length; i++) {
                NodeInfo ndi = ndinf[i];
                Junction ju = null;

                if (ndi.nd instanceof Junction) {
                    ju = (Junction) ndi.nd;
                } else {
                    continue;
                }

                Sign[] config = ju.getSignConfigs()[ndi.config];

                for (int j = 0; j < config.length; j++) {
                    int numRUWaiting = config[j].getLane()
                                                .getNumRoadusersWaiting();

                    if (numRUWaiting > 0) {
                        Roaduser ru = config[j].getLane().getFirstRoaduser();
                        totalFitness += (1.0 + (0.1 * ru.getDelay())); // prevents infinite waiting times
                    }

                    totalFitness += (numRUWaiting * 0.3f);
                }

                // Stimulate green waves, if a next lane is also green, give an extra reward
                for (int l = 0; l < config.length; l++) {
                    Drivelane dl = config[l].getLane();
                    Drivelane[] dls = dl.getSign().getNode()
                                        .getLanesLeadingFrom(dl, 0);

                    for (int j = 0; j < dls.length; j++) {
                        Sign s2 = dls[j].getSign();
                        NodeInfo ndi2 = ndinf[s2.getNode().getId()];

                        if (!(ndi2.nd instanceof Junction)) {
                            continue;
                        }

                        Sign[] cfg2 = ((Junction) (ndi2.nd)).getSignConfigs()[ndi2.config];

                        for (int k = 0; k < cfg2.length; k++) {
                            if (cfg2[k] == s2) {
                                totalFitness += 0.1;
                            }
                        }
                    }
                }
            }

            fitness = totalFitness;
        }

        /**
         * DOCUMENT ME!
         *
         * @param tld DOCUMENT ME!
         *
         * @throws InfraException DOCUMENT ME!
         */
        public void fillTld(TLDecision[][] tld) throws InfraException {
            for (int i = 0; i < ndinf.length; i++) {
                NodeInfo ndi = ndinf[i];
                int nodeID = ndi.nd.getId();
                setConfiguration(ndi, tld[nodeID]);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param ndi DOCUMENT ME!
         * @param tl DOCUMENT ME!
         *
         * @throws InfraException DOCUMENT ME!
         */
        private void setConfiguration(NodeInfo ndi, TLDecision[] tl)
            throws InfraException {
            if (tl.length <= 0) {
                return;
            }

            Junction ju = null;

            if (ndi.nd instanceof Junction) {
                ju = (Junction) ndi.nd;
            } else {
                return;
            }

            Sign[] config = ju.getSignConfigs()[ndi.config];

            for (int j = 0; j < tl.length; j++) {
                tl[j].setGain(0);

                for (int k = 0; k < config.length; k++)
                    if (tl[j].getTL() == config[k]) {
                        tl[j].setGain(1);
                    }
            }
        }

        // XMLSerializable implementation of Person
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
            fitness = myElement.getAttribute("fitness").getFloatValue();
            relFitness = myElement.getAttribute("rel-fitness").getFloatValue();
            ndinf = (NodeInfo[]) XMLArray.loadArray(this, loader, assistant);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws XMLCannotSaveException DOCUMENT ME!
         */
        public XMLElement saveSelf() throws XMLCannotSaveException {
            XMLElement result = new XMLElement("person");
            result.addAttribute(new XMLAttribute("fitness", fitness));
            result.addAttribute(new XMLAttribute("rel-fitness", relFitness));

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
            XMLArray.saveArray(ndinf, this, saver, "node-info");
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getXMLName() {
            return myParentName + ".person";
        }

        /**
         * DOCUMENT ME!
         *
         * @param newParentName DOCUMENT ME!
         */
        public void setParentName(String newParentName) {
            myParentName = newParentName;
        }

        // TwoStageLoader implementation of Person
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
            XMLUtils.loadSecondStage(new ArrayEnumeration(ndinf), dictionaries);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class NodeInfo implements XMLSerializable, TwoStageLoader {
        /** DOCUMENT ME! */
        Node nd;

        /** DOCUMENT ME! */
        int config;

        /** DOCUMENT ME! */
        int configsize;

        /** DOCUMENT ME! */
        protected String myParentName = "model.tlc.population.person";

        /** DOCUMENT ME! */
        protected TwoStageLoaderData loadData = new TwoStageLoaderData();

        // Empty constructor for loading
        /**
         * Creates a new NodeInfo object.
         */
        public NodeInfo() {
        }

/**
         * Creates a new NodeInfo object.
         *
         * @param nd DOCUMENT ME!
         */
        public NodeInfo(Node nd) {
            this.nd = nd;
            config = -1;

            if (nd instanceof Junction) {
                configsize = ((Junction) nd).getSignConfigs().length;
            } else {
                configsize = 0;
            }
        }

        // XMLSerializable implementation of NodeInfo
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
            config = myElement.getAttribute("config").getIntValue();
            configsize = myElement.getAttribute("config-size").getIntValue();
            loadData.nodeId = myElement.getAttribute("node-id").getIntValue();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws XMLCannotSaveException DOCUMENT ME!
         */
        public XMLElement saveSelf() throws XMLCannotSaveException {
            XMLElement result = new XMLElement("nodeinfo");
            result.addAttribute(new XMLAttribute("config", config));
            result.addAttribute(new XMLAttribute("config-size", configsize));
            result.addAttribute(new XMLAttribute("node-id", nd.getId()));

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
            throws XMLTreeException, IOException, XMLCannotSaveException { // NodeInfo objects don't have child objects
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getXMLName() {
            return myParentName + ".nodeinfo";
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
            nd = (Node) ((Dictionary) dictionaries.get("node")).get(new Integer(
                        loadData.nodeId));
        }

        // TwoStageLoader implementation of NodeInfo
        /**
         * DOCUMENT ME!
         *
         * @author $author$
         * @version $Revision: 1.3 $
          */
        class TwoStageLoaderData {
            /** DOCUMENT ME! */
            int nodeId;
        }
    }
}
