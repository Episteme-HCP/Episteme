/*-----------------------------------------------------------------------
 * Copyright (C) 2001 Green Light District Team, Utrecht University
 *
 * This program (Green Light District) is free software.
 * You may redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by
 * the Free Software Foundation (version 2 or later).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * See the documentation of Green Light District for further information.
 *------------------------------------------------------------------------*/
package org.jscience.architecture.traffic.algorithms.tlc;

import org.jscience.architecture.traffic.Controller;
import org.jscience.architecture.traffic.infrastructure.*;
import org.jscience.architecture.traffic.util.ArrayEnumeration;
import org.jscience.architecture.traffic.xml.*;

import java.io.IOException;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ListIterator;


/**
 * This algorithm will, when it starts, handle like Longest Queue, but it
 * can do more things: 1. It prevents infinite waiting of Roadusers 2. It
 * tries to create green waves between busy nodes. The green_wave-factor is
 * editable as a parameter. 3. The algorithm can be stimulated to develop
 * patterns in TL-configuration-settings, therefore it remembers the last 100
 * configurations. This pattern factor is also editable 4. This algorithm has
 * a keep green factor. This editable factor is an extra weight to keep the
 * lights green, that were green in the previous cycle.
 */
public class ACGJ2 extends TLController implements XMLSerializable,
    TwoStageLoader, InstantiationAssistant {
    /** DOCUMENT ME! */
    protected static final String shortXMLName = "tlc-acgj2";

    /** DOCUMENT ME! */
    protected static float PAT_FACTOR = 0;

    /** DOCUMENT ME! */
    protected static float KEEP_GREEN_FACTOR = 0.05f;

    /** DOCUMENT ME! */
    protected static float GREEN_WAVE_FACTOR = 2.0f;

    /** DOCUMENT ME! */
    protected static float LOOK_AHEAD_FACTOR = 1.0f;

    /** DOCUMENT ME! */
    protected NodeInfo[] ndinf;

    /** DOCUMENT ME! */
    protected NextCycles bestSoFar;

/**
     * Creates a new ACGJ2 object.
     *
     * @param i DOCUMENT ME!
     */
    public ACGJ2(Infrastructure i) {
        super(i);
        bestSoFar = null;

        for (int j = 0; j < ndinf.length; j++) {
            NodeInfo ndi = ndinf[j];

            for (int k = 0; k < tld.length; k++) {
                TLDecision[] tl = tld[k];

                if (tl.length > 0) {
                    if (tl[0].getTL().getNode() == ndi.nd) {
                        ndi.tldIndex = k;
                    }
                }

                for (int l = 0; l < ndi.dli.length; l++) {
                    DrivelaneInfo dl = ndi.dli[l];

                    for (int m = 0; m < tld[k].length; m++) {
                        if (tld[k][m].getTL() == dl.dl.getSign()) {
                            dl.tldIndex = m;
                        }
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void setInfrastructure(Infrastructure i) {
        super.setInfrastructure(i);

        Node[] allNodes = i.getAllNodes();
        ndinf = new NodeInfo[allNodes.length];

        for (int j = 0; j < ndinf.length; j++)
            ndinf[j] = new NodeInfo(allNodes[j], this);
    }

    /**
     * Calculates how every traffic light should be switched
     *
     * @return DOCUMENT ME!
     *
     * @see gld.algo.tlc.TLDecision
     */
    public TLDecision[][] decideTLs() {
        for (int i = 0; i < ndinf.length; i++)
            ndinf[i].updateVariables();

        NodeInfo[] sortedNdi = sortNodeInfo(ndinf);

        // Normal weights
        for (int i = 0; i < sortedNdi.length; i++) {
            NodeInfo currentNode = sortedNdi[i];

            if (currentNode.tldIndex != -1) {
                currentNode.calcPatValues(PAT_FACTOR);

                for (int j = 0; j < currentNode.dli.length; j++) {
                    DrivelaneInfo currentDL = currentNode.dli[j];

                    if (currentDL.tldIndex != -1) {
                        float qval = currentNode.getQValue(j);
                        qval += currentNode.pat[j];

                        if (currentDL.dl.getSign().mayDrive()) {
                            qval *= (1 + KEEP_GREEN_FACTOR);
                        }

                        tld[currentNode.tldIndex][currentDL.tldIndex].setGain(qval);
                    } else {
                        System.out.println("Negative index");
                    }
                }
            }
        }

        return tld;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ndi DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeInfo[] sortNodeInfo(NodeInfo[] ndi) {
        NodeInfo[] result = new NodeInfo[ndi.length];

        for (int i = 0; i < result.length; i++)
            result[i] = ndi[i];

        // bubble sort algorithm
        for (int j = result.length; j > 0; j--) {
            for (int i = 0; i < (j - 1); i++) {
                if (result[i].currentBusyness > result[i + 1].currentBusyness) {
                    //swap
                    NodeInfo temp = result[i];
                    result[i] = result[i + 1];
                    result[i + 1] = temp;
                }
            }
        }

        return result;
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
        // No implementation necessary
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void showSettings(Controller c) {
        String[] descs = {
                "Pattern factor", "Keep green light factor", "Green wave factor",
                "Look ahead factor"
            };
        float[] floats = {
                PAT_FACTOR, KEEP_GREEN_FACTOR, GREEN_WAVE_FACTOR,
                LOOK_AHEAD_FACTOR
            };
        TLCSettings settings = new TLCSettings(descs, null, floats);

        settings = doSettingsDialog(c, settings);
        PAT_FACTOR = settings.floats[0];
        KEEP_GREEN_FACTOR = settings.floats[1];
        GREEN_WAVE_FACTOR = settings.floats[2];
        LOOK_AHEAD_FACTOR = settings.floats[3];
    }

    //  XMLSerializable implementation
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
        PAT_FACTOR = myElement.getAttribute("pat-factor").getFloatValue();
        KEEP_GREEN_FACTOR = myElement.getAttribute("keep-green").getFloatValue();
        GREEN_WAVE_FACTOR = myElement.getAttribute("green-wave").getFloatValue();
        LOOK_AHEAD_FACTOR = myElement.getAttribute("look-ahead").getFloatValue();
        ndinf = (NodeInfo[]) XMLArray.loadArray(this, loader, this);

        if (!myElement.getAttribute("best-null").getBoolValue()) {
            bestSoFar = new NextCycles();
            loader.load(this, bestSoFar);
        }
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
        result.addAttribute(new XMLAttribute("pat-factor", PAT_FACTOR));
        result.addAttribute(new XMLAttribute("keep-green", KEEP_GREEN_FACTOR));
        result.addAttribute(new XMLAttribute("green-wave", GREEN_WAVE_FACTOR));
        result.addAttribute(new XMLAttribute("look-ahead", LOOK_AHEAD_FACTOR));
        result.addAttribute(new XMLAttribute("best-null", bestSoFar == null));

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

        if (bestSoFar != null) {
            saver.saveObject(bestSoFar);
        }
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
        XMLUtils.loadSecondStage(new ArrayEnumeration(ndinf), dictionaries);
        loadThirdStage(dictionaries);
    }

    // Yeah. Baby. A three-stage loader
    /**
     * DOCUMENT ME!
     *
     * @param dictionaries DOCUMENT ME!
     *
     * @throws XMLInvalidInputException DOCUMENT ME!
     * @throws XMLTreeException DOCUMENT ME!
     */
    public void loadThirdStage(Dictionary dictionaries)
        throws XMLInvalidInputException, XMLTreeException {
        Enumeration ni = new ArrayEnumeration(ndinf);
        Dictionary nodeInfo = new Hashtable();
        NodeInfo tmp;

        while (ni.hasMoreElements()) {
            tmp = (NodeInfo) ni.nextElement();
            nodeInfo.put(new Integer(tmp.getId()), tmp);
        }

        dictionaries.put("node-info", nodeInfo);
        ni = new ArrayEnumeration(ndinf);

        while (ni.hasMoreElements())
            ((NodeInfo) (ni.nextElement())).loadThirdStage(dictionaries);
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
        if (NextCycles.class.equals(request)) {
            return new NextCycles();
        } else if (NodeInfo.class.equals(request)) {
            return new NodeInfo(this);
        } else if (DrivelaneInfo.class.equals(request)) {
            return new DrivelaneInfo(this);
        } else if (Configuration.class.equals(request)) {
            return new Configuration();
        } else {
            throw new ClassNotFoundException(
                "ACGJ2 InstantiationAssistant cannot make instances of " +
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
        return NextCycles.class.equals(request) ||
        NodeInfo.class.equals(request) || DrivelaneInfo.class.equals(request) ||
        Configuration.class.equals(request);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class NextCycles implements XMLSerializable {
        /** DOCUMENT ME! */
        int cyclesForward;

        /** DOCUMENT ME! */
        Infrastructure currentInfra;

        /** DOCUMENT ME! */
        protected String myParentName = "model.tlc";

/**
         * Creates a new NextCycles object.
         */
        public NextCycles() {
            currentInfra = infra;
        }

        // XMLSerializable implementation of NextCycles
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
            cyclesForward = myElement.getAttribute("cycles-forward")
                                     .getIntValue();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws XMLCannotSaveException DOCUMENT ME!
         */
        public XMLElement saveSelf() throws XMLCannotSaveException {
            XMLElement result = new XMLElement("nextcycles");
            result.addAttribute(new XMLAttribute("cycles-forward", cyclesForward));

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
            throws XMLTreeException, IOException, XMLCannotSaveException { // A NextCycles object has no child objects
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getXMLName() {
            return myParentName + ".nextcycles";
        }

        /**
         * DOCUMENT ME!
         *
         * @param newParentName DOCUMENT ME!
         */
        public void setParentName(String newParentName) {
            myParentName = newParentName;
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
        float currentWaiting;

        /** DOCUMENT ME! */
        float currentBusyness;

        /** DOCUMENT ME! */
        Configuration[] lastConfigs; //history of the last 10 configs

        /** DOCUMENT ME! */
        DrivelaneInfo[] dli; //i=inbound o=outbound

        /** DOCUMENT ME! */
        DrivelaneInfo[] dlo; //i=inbound o=outbound

        /** DOCUMENT ME! */
        Node nd;

        /** DOCUMENT ME! */
        int tldIndex;

        /** DOCUMENT ME! */
        Sign[] bestSignSoFar;

        /** DOCUMENT ME! */
        float[] pat;

        /** DOCUMENT ME! */
        ACGJ2 acgj;

        // XML stuff
        /** DOCUMENT ME! */
        String myParentName = "model.tlc";

        /** DOCUMENT ME! */
        TwoStageLoaderData loadData = new TwoStageLoaderData();

        // Empty constructor for loading
        /**
         * Creates a new NodeInfo object.
         *
         * @param acgj DOCUMENT ME!
         */
        public NodeInfo(ACGJ2 acgj) {
            this.acgj = acgj;
        }

/**
         * Creates a new NodeInfo object.
         *
         * @param nd   DOCUMENT ME!
         * @param acgj DOCUMENT ME!
         */
        public NodeInfo(Node nd, ACGJ2 acgj) {
            this.acgj = acgj;
            tldIndex = -1;
            currentWaiting = 0;
            currentBusyness = 0;

            this.nd = nd;
            lastConfigs = new Configuration[10];

            for (int i = 0; i < lastConfigs.length; i++)
                lastConfigs[i] = null;

            // create dli
            Drivelane[] dls;

            try {
                dls = nd.getInboundLanes();
            } catch (Exception e) {
                dls = new Drivelane[0];
            }

            dli = new DrivelaneInfo[dls.length];

            for (int i = 0; i < dli.length; i++) {
                dli[i] = new DrivelaneInfo(dls[i], this, acgj);
            }

            try {
                dls = nd.getInboundLanes();
            } catch (Exception e) {
                dls = new Drivelane[0];
            }

            dlo = new DrivelaneInfo[dls.length];

            for (int i = 0; i < dlo.length; i++) {
                dlo[i] = new DrivelaneInfo(dls[i], this, acgj);
            }

            bestSignSoFar = null;
            pat = new float[dli.length];
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getId() {
            return (nd == null) ? loadData.nodeId : nd.getId();
        }

        /**
         * DOCUMENT ME!
         */
        public void updateVariables() {
            // update waiting and busyness
            currentWaiting = 0;

            Drivelane[] dls;

            try {
                dls = nd.getInboundLanes();
            } catch (Exception e) {
                dls = new Drivelane[0];
            }

            for (int i = 0; i < dls.length; i++) {
                currentWaiting += dls[i].getNumRoadusersWaiting();
            }

            currentBusyness = (currentBusyness * 0.9f) +
                (currentWaiting * 0.1f);

            // update lastConfigs
            boolean[] green = new boolean[dls.length];
            int numGreen = 0;

            for (int i = 0; i < green.length; i++) {
                if (dls[i].getSign().mayDrive()) {
                    numGreen++;
                    green[i] = true;
                } else {
                    green[i] = false;
                }
            }

            boolean[] currentGreenSigns = new boolean[green.length];
            int j = 0;

            for (int i = 0; i < green.length; i++) {
                if (green[i]) {
                    currentGreenSigns[j] = dls[i].getSign().mayDrive();
                    j++;
                }
            }

            if (lastConfigs[0] != null) {
                if (lastConfigs[0].signEquals(currentGreenSigns)) {
                    lastConfigs[0].incNumCycles();
                } else {
                    // Shift all configs
                    for (int i = lastConfigs.length - 2; i >= 0; i--)
                        lastConfigs[i + 1] = lastConfigs[i];
                }
            }

            lastConfigs[0] = new Configuration(currentGreenSigns, 1);
            bestSignSoFar = null;
        }

        /**
         * DOCUMENT ME!
         *
         * @param DLindex DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getQValue(int DLindex) {
            DrivelaneInfo dlInf = dli[DLindex];
            int result = dlInf.dl.getNumRoadusersWaiting();
            float laFactor = dlInf.getLookAheadDisc() * acgj.LOOK_AHEAD_FACTOR;

            return result + laFactor;
        }

        /**
         * Calculates the pattern values
         *
         * @param patFactor DOCUMENT ME!
         */
        public void calcPatValues(float patFactor) {
            for (int i = 0; i < pat.length; i++)
                pat[i] = 0;

            if (lastConfigs[0] == null) {
                return;
            }

            if (patFactor == 0) {
                return;
            }

            boolean[] currentSigns = lastConfigs[0].signs;
            Configuration[] temp = new Configuration[lastConfigs.length];

            int count = 0;
            int totCycles = 0;

            for (int i = 1; i < temp.length; i++) {
                if (lastConfigs[i] != null) {
                    if (lastConfigs[0].signEquals(lastConfigs[i].signs)) {
                        temp[i] = lastConfigs[i];
                        count++;
                        totCycles += lastConfigs[i].numCycles;
                    } else {
                        temp[i] = null;
                    }
                } else {
                    break;
                }
            }

            if (totCycles > 0) {
                for (int i = 1; i < temp.length; i++) {
                    if (lastConfigs[i] != null) {
                        if (lastConfigs[0].signEquals(lastConfigs[i].signs)) {
                            int leftSecs = 0;
                            Configuration old = null;

                            if (lastConfigs[i].numCycles > lastConfigs[0].numCycles) {
                                leftSecs = lastConfigs[i].numCycles -
                                    lastConfigs[0].numCycles;
                                old = lastConfigs[i];
                            } else {
                                old = lastConfigs[i - 1];
                                leftSecs = lastConfigs[i].numCycles;
                            }

                            boolean[] sgs = old.signs;

                            for (int j = 0; j < dli.length; j++) {
                                DrivelaneInfo dl = dli[j];

                                for (int k = 0; k < sgs.length; k++) {
                                    if (dl.dl.getSign().mayDrive() == sgs[k]) {
                                        pat[dl.tldIndex] += (patFactor * (leftSecs / totCycles));
                                    }
                                }
                            }
                        } else {
                            temp[i] = null;
                        }
                    } else {
                        break;
                    }
                }
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
            lastConfigs = (Configuration[]) XMLArray.loadArray(this, loader,
                    acgj);
            dli = (DrivelaneInfo[]) XMLArray.loadArray(this, loader, acgj);
            dlo = (DrivelaneInfo[]) XMLArray.loadArray(this, loader, acgj);

            if (!myElement.getAttribute("signs-null").getBoolValue()) {
                bestSignSoFar = (Sign[]) XMLArray.loadArray(this, loader, acgj);
            }

            pat = (float[]) XMLArray.loadArray(this, loader);
            currentWaiting = myElement.getAttribute("current-waiting")
                                      .getFloatValue();
            currentBusyness = myElement.getAttribute("current-busy")
                                       .getFloatValue();
            tldIndex = myElement.getAttribute("tld-index").getIntValue();
            loadData.nodeId = myElement.getAttribute("node-id").getIntValue();

            // Set NodeInfo in DrivelaneInfo
            Enumeration di1 = new ArrayEnumeration(dli);

            while (di1.hasMoreElements()) {
                ((DrivelaneInfo) (di1.nextElement())).setNodeInfo(this);
            }

            Enumeration di2 = new ArrayEnumeration(dlo);

            while (di2.hasMoreElements()) {
                ((DrivelaneInfo) (di2.nextElement())).setNodeInfo(this);
            }
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
            result.addAttribute(new XMLAttribute("current-waiting",
                    currentWaiting));
            result.addAttribute(new XMLAttribute("current-busy", currentBusyness));
            result.addAttribute(new XMLAttribute("tld-index", tldIndex));
            result.addAttribute(new XMLAttribute("node-id", nd.getId()));
            result.addAttribute(new XMLAttribute("signs-null",
                    bestSignSoFar == null));

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
            XMLArray.saveArray(lastConfigs, this, saver, "last-configs");
            XMLArray.saveArray(dli, this, saver, "dl-inbound");
            XMLArray.saveArray(dlo, this, saver, "dl-outbound");

            if (bestSignSoFar != null) {
                XMLUtils.setParentName(new ArrayEnumeration(bestSignSoFar),
                    getXMLName());
                XMLArray.saveArray(bestSignSoFar, this, saver, "best-sign");
            }

            XMLArray.saveArray(pat, this, saver, "pat");
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

            if (bestSignSoFar != null) {
                XMLUtils.loadSecondStage(new ArrayEnumeration(bestSignSoFar),
                    dictionaries);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param dictionaries DOCUMENT ME!
         *
         * @throws XMLInvalidInputException DOCUMENT ME!
         * @throws XMLTreeException DOCUMENT ME!
         */
        public void loadThirdStage(Dictionary dictionaries)
            throws XMLInvalidInputException, XMLTreeException {
            XMLUtils.loadSecondStage(new ArrayEnumeration(dli), dictionaries);
            XMLUtils.loadSecondStage(new ArrayEnumeration(dlo), dictionaries);
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

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class DrivelaneInfo implements XMLSerializable, TwoStageLoader {
        /** DOCUMENT ME! */
        Drivelane dl;

        /** DOCUMENT ME! */
        NodeInfo ndi; // node with sign at this DL

        /** DOCUMENT ME! */
        private NodeInfo otherNode; // node without sign at this DL

        /** DOCUMENT ME! */
        int[] target; // 0 = left 1= straight 2=right

        /** DOCUMENT ME! */
        float[] drivers;

        /** DOCUMENT ME! */
        int tldIndex;

        /** DOCUMENT ME! */
        ACGJ2 acgj;

        // XML stuff
        /** DOCUMENT ME! */
        String myParentName = "model.tlc.nodeinfo";

        /** DOCUMENT ME! */
        TwoStageLoaderData loadData = new TwoStageLoaderData();

        // Empty constructor for loading
        /**
         * Creates a new DrivelaneInfo object.
         *
         * @param acgj DOCUMENT ME!
         */
        public DrivelaneInfo(ACGJ2 acgj) {
            this.acgj = acgj;
        }

/**
         * Creates a new DrivelaneInfo object.
         *
         * @param dl   DOCUMENT ME!
         * @param ndi  DOCUMENT ME!
         * @param acgj DOCUMENT ME!
         */
        public DrivelaneInfo(Drivelane dl, NodeInfo ndi, ACGJ2 acgj) {
            this.dl = dl;
            this.ndi = ndi;
            this.otherNode = null;
            this.acgj = acgj;
            tldIndex = -1;

            boolean[] targets = dl.getTargets();
            int count = 0;

            for (int i = 0; i < targets.length; i++)
                if (targets[i]) {
                    count++;
                }

            target = new int[count];

            int count2 = 0;

            for (int j = 0; j < targets.length; j++)
                if (targets[j]) {
                    target[count2] = j;
                    count2++;
                }

            drivers = new float[count];

            for (int i = 0; i < drivers.length; i++)
                drivers[i] = 0;
        }

        /**
         * DOCUMENT ME!
         *
         * @param ni DOCUMENT ME!
         */
        public void setNodeInfo(NodeInfo ni) {
            ndi = ni;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public NodeInfo getOtherNode() {
            if (otherNode != null) {
                return otherNode;
            }

            Node an = dl.getRoad().getAlphaNode();
            Node bn = dl.getRoad().getBetaNode();
            Node cn = null;

            if (ndi.nd == an) {
                cn = bn;
            } else {
                cn = an;
            }

            for (int i = 0; i < acgj.ndinf.length; i++) {
                if (acgj.ndinf[i].nd == cn) {
                    otherNode = acgj.ndinf[i];

                    return otherNode;
                }
            }

            return null;
        }

        /**
         * DOCUMENT ME!
         *
         * @param direction DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getTurnChance(int direction) {
            return 1.0f / target.length;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getLookAheadDisc() {
            ListIterator li = dl.getQueue().listIterator();
            Roaduser ru = null;
            int pos = 0;
            int ru_pos;
            int count = 0;

            while (li.hasNext()) {
                ru = (Roaduser) li.next();
                ru_pos = ru.getPosition();

                if (ru_pos > pos) {
                    break;
                } else if (ru_pos == pos) {
                    pos += ru.getLength();
                    count++;
                }
            }

            float laFactor = 0.0f;

            while (li.hasNext()) {
                ru = (Roaduser) li.next();
                ru_pos = ru.getPosition();
                laFactor -= (1 / (ru_pos - pos));
            }

            return laFactor;
        }

        // XMLSerializable implementation of DrivelaneInfo
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
            tldIndex = myElement.getAttribute("tld-index").getIntValue();
            loadData.nodeId = myElement.getAttribute("other-node").getIntValue();
            loadData.laneId = myElement.getAttribute("lane-id").getIntValue();
            target = (int[]) XMLArray.loadArray(this, loader);
            drivers = (float[]) XMLArray.loadArray(this, loader);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws XMLCannotSaveException DOCUMENT ME!
         */
        public XMLElement saveSelf() throws XMLCannotSaveException {
            XMLElement result = new XMLElement("laneinfo");
            result.addAttribute(new XMLAttribute("tld-index", tldIndex));
            result.addAttribute(new XMLAttribute("other-node",
                    (otherNode == null) ? (-1) : otherNode.getId()));
            result.addAttribute(new XMLAttribute("lane-id",
                    (dl == null) ? (-1) : dl.getId()));

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
            XMLArray.saveArray(target, this, saver, "target");
            XMLArray.saveArray(drivers, this, saver, "drivers");
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getXMLName() {
            return myParentName + ".laneinfo";
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
         */
        public void loadSecondStage(Dictionary dictionaries) {
            otherNode = (NodeInfo) ((Dictionary) dictionaries.get("node-info")).get(new Integer(
                        loadData.nodeId));
            dl = (Drivelane) ((Dictionary) dictionaries.get("lane")).get(new Integer(
                        loadData.laneId));
        }

        // TwoStageLoader implementation of DrivelaneInfo
        /**
         * DOCUMENT ME!
         *
         * @author $author$
         * @version $Revision: 1.3 $
          */
        class TwoStageLoaderData {
            /** DOCUMENT ME! */
            int nodeId;

            /** DOCUMENT ME! */
            int laneId;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class Configuration implements XMLSerializable {
        /** DOCUMENT ME! */
        protected boolean[] signs; // red=false, green=true

        /** DOCUMENT ME! */
        protected int numCycles;

        /** DOCUMENT ME! */
        protected String myParentName = "model.tlc.nodeinfo";

        // Empty constructor for loading
        /**
         * Creates a new Configuration object.
         */
        public Configuration() {
        }

/**
         * Creates a new Configuration object.
         *
         * @param s         DOCUMENT ME!
         * @param numCycles DOCUMENT ME!
         */
        public Configuration(Sign[] s, int numCycles) {
            //this.signs     = signs;
            signs = new boolean[s.length];

            for (int i = 0; i < s.length; i++)
                signs[i] = s[i].mayDrive();

            this.numCycles = numCycles;
        }

/**
         * Creates a new Configuration object.
         *
         * @param b         DOCUMENT ME!
         * @param numCycles DOCUMENT ME!
         */
        public Configuration(boolean[] b, int numCycles) {
            //this.signs     = signs;
            this.signs = b;
            this.numCycles = numCycles;
        }

        /**
         * DOCUMENT ME!
         */
        public void incNumCycles() {
            numCycles++;
        }

        /**
         * DOCUMENT ME!
         *
         * @param sgs DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean signEquals(boolean[] sgs) {
            if (sgs.length != signs.length) {
                return false;
            }

            int count = 0;

            for (int i = 0; i < signs.length; i++) {
                for (int j = i; j < sgs.length; j++) {
                    if (signs[i] == sgs[j]) {
                        count++;
                    }
                }
            }

            if (count != signs.length) {
                return false;
            }

            return true;
        }

        // XMLSerializable implementation of NextCycles
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
            numCycles = myElement.getAttribute("num-cycles").getIntValue();
            signs = (boolean[]) XMLArray.loadArray(this, loader);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws XMLCannotSaveException DOCUMENT ME!
         */
        public XMLElement saveSelf() throws XMLCannotSaveException {
            XMLElement result = new XMLElement("config");
            result.addAttribute(new XMLAttribute("num-cycles", numCycles));

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
            XMLArray.saveArray(signs, this, saver, "signs");
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getXMLName() {
            return myParentName + ".config";
        }

        /**
         * DOCUMENT ME!
         *
         * @param newParentName DOCUMENT ME!
         */
        public void setParentName(String newParentName) {
            myParentName = newParentName;
        }
    }
}
