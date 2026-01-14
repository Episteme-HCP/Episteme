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

package org.jscience.architecture.traffic.simulation;

import org.jscience.architecture.traffic.*;
import org.jscience.architecture.traffic.algorithms.dp.DPFactory;
import org.jscience.architecture.traffic.algorithms.tlc.Colearning;
import org.jscience.architecture.traffic.algorithms.tlc.TLCFactory;
import org.jscience.architecture.traffic.algorithms.tlc.TLController;
import org.jscience.architecture.traffic.edit.EditController;
import org.jscience.architecture.traffic.edit.EditModel;
import org.jscience.architecture.traffic.edit.Validation;
import org.jscience.architecture.traffic.infrastructure.InfraException;
import org.jscience.architecture.traffic.infrastructure.Infrastructure;
import org.jscience.architecture.traffic.infrastructure.LessSimpleInfra;
import org.jscience.architecture.traffic.infrastructure.SimpleInfra;
import org.jscience.architecture.traffic.simulation.statistics.*;
import org.jscience.architecture.traffic.util.ArrayUtils;
import org.jscience.architecture.traffic.xml.*;

import java.awt.*;

import java.io.File;
import java.io.IOException;

import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;


/**
 * The main controller for the simulation part of the application.
 *
 * @author Group GUI
 * @version 1.0
 */
public class SimController extends Controller implements Observer {
    /** DOCUMENT ME! */
    public static final String[] speedTexts = {
            "Low", "Medium", "High", "Maximum"
        };

    /** DOCUMENT ME! */
    public static final int[] speedSettings = { 1000, 400, 50, 10 };

    /** DOCUMENT ME! */
    protected EditController editController = null;

    /** DOCUMENT ME! */
    protected SimMenuBar menuBar;

    /** DOCUMENT ME! */
    protected Choice speedChoice;

    /** DOCUMENT ME! */
    protected StatisticsOverlay statsOverlay;

/**
     * Creates the main frame.
     *
     * @param m      The <code>SimModel</code> to be controlled.
     * @param splash DOCUMENT ME!
     */
    public SimController(SimModel m, boolean splash) {
        super(m, splash);
        setSimModel(m);
        m.setSimController(this);

        speedChoice = new Choice();

        Enumeration e = ArrayUtils.getEnumeration(speedTexts);

        while (e.hasMoreElements())
            speedChoice.add((String) (e.nextElement()));

        setSpeed((int) (speedTexts.length / 2));
        setCycleCounterEnabled(true);

        statsOverlay = new StatisticsOverlay(view, m.getInfrastructure());

        setTLC(0, 0);
        setDrivingPolicy(0);
    }

    /*============================================*/
    /* GET and SET methods                        */
    /*============================================*/
    /**
     * Returns the current <code>SimModel</code>
     *
     * @return DOCUMENT ME!
     */
    public SimModel getSimModel() {
        return (SimModel) model;
    }

    /**
     * Sets a new <code>SimModel</code> to be controlled
     *
     * @param m DOCUMENT ME!
     */
    public void setSimModel(SimModel m) {
        model = m;
    }

    /**
     * Enables or disables the cycle counter.
     *
     * @param b DOCUMENT ME!
     */
    public void setCycleCounterEnabled(boolean b) {
        if (b) {
            getSimModel().addObserver(this);
        } else {
            setStatus("Cycle counter disabled at cycle " +
                getSimModel().getCurCycle() + ".");
            getSimModel().deleteObserver(this);
        }
    }

    /*============================================*/
    /* Load and save                              */
    /*============================================*/
    public void load(XMLElement myElement, XMLLoader loader)
        throws XMLTreeException, IOException, XMLInvalidInputException {
        super.load(myElement, loader);

        // TODO restore menu options/choices in GUI
        statsOverlay = new StatisticsOverlay(view,
                getSimModel().getInfrastructure());

        if (XMLUtils.getLastName(statsOverlay)
                        .equals(loader.getNextElementName())) {
            System.out.println("Loading statistics");
            loader.load(this, statsOverlay);
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

        /* This code is buggy
        result.addAttribute(new XMLAttribute("saved-by", "simulator"));
         result.addAttribute(new XMLAttribute("tlc-category",
                        menuBar.getTLCMenu().getCategory()));
        result.addAttribute(new XMLAttribute("tlc-number",
                        menuBar.getTLCMenu().getTLC()));
        result.addAttribute(new XMLAttribute("driving-policy",
                        menuBar.getDPMenu().getSelectedIndex()));
        result.addAttribute(new XMLAttribute("speed",
                        speedChoice.getSelectedIndex()));
        */
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
        saver.saveObject(statsOverlay);
    }

    /**
     * DOCUMENT ME!
     *
     * @param filename DOCUMENT ME!
     *
     * @throws InvalidFilenameException DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    public void doSave(String filename)
        throws InvalidFilenameException, Exception {
        if (!filename.endsWith(".sim")) {
            throw new InvalidFilenameException(
                "Filename must have .sim extension.");
        }

        setStatus("Saving simulation to " + filename);

        XMLSaver saver = new XMLSaver(new File(filename));
        saveAll(saver, getSimModel());
        saver.close();
        setStatus("Saved simulation to " + filename);
    }

    /**
     * DOCUMENT ME!
     *
     * @param filename DOCUMENT ME!
     *
     * @throws InvalidFilenameException DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    public void doLoad(String filename)
        throws InvalidFilenameException, Exception {
        if (!filename.endsWith(".infra") && !filename.endsWith(".sim")) {
            throw new InvalidFilenameException(
                "You can only load .infra and .sim files.");
        }

        stop();
        TrackerFactory.purgeTrackers();

        XMLLoader loader = new XMLLoader(new File(filename));
        loadAll(loader, getSimModel());
        newInfrastructure(model.getInfrastructure());
        loader.close();
    }

    /*============================================*/
    /* Miscellanous                               */
    /*============================================*/
    /**
     * Called by observable SimModel (if view enabled).
     *
     * @param o DOCUMENT ME!
     * @param arg DOCUMENT ME!
     */
    public void update(Observable o, Object arg) {
        int cycle = ((SimModel) o).getCurCycle();

        if (cycle != 0) {
            setStatus("Cycle: " + cycle);
        }
    }

    /**
     * Returns the name of this controller extension.
     *
     * @return DOCUMENT ME!
     */
    protected String appName() {
        return "simulator";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected MenuBar createMenuBar() {
        menuBar = new SimMenuBar(this, speedTexts);

        return menuBar;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected TrafficToolBar createToolBar() {
        return new SimToolBar(this);
    }

    /*============================================*/
    /* Invoked by Listeners                       */
    /*============================================*/
    /**
     * Opens the statistics viewer.
     */
    public void showStatistics() {
        new StatisticsController(getSimModel(), this);
    }

    /**
     * Shows the tracking window.
     *
     * @param type DOCUMENT ME!
     */
    public void showTracker(int type) {
        try {
            TrackerFactory.showTracker(getSimModel(), this, type);
        } catch (TrafficException e) {
            reportError(e.fillInStackTrace());
        }
    }

    /**
     * Enables the statistics overlay
     */
    public void enableOverlay() {
        statsOverlay = new StatisticsOverlay(view,
                getSimModel().getInfrastructure());
        getSimModel().addObserver(statsOverlay);
        view.addOverlay(statsOverlay);
    }

    /**
     * Enables the statistics overlay
     */
    public void disableOverlay() {
        getSimModel().deleteObserver(statsOverlay);
        view.remOverlay(statsOverlay);
    }

    /**
     * DOCUMENT ME!
     *
     * @param dp DOCUMENT ME!
     */
    public void setDrivingPolicy(int dp) {
        try {
            getSimModel()
                .setDrivingPolicy((new DPFactory(getSimModel(),
                    getSimModel().getTLController())).getInstance(dp));
        } catch (Exception e) {
            reportError(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param cat DOCUMENT ME!
     * @param nr DOCUMENT ME!
     */
    public void setTLC(int cat, int nr) {
        setColearningEnabled(cat == 1);

        try {
            SimModel sm = getSimModel();
            TLCFactory tlcf = new TLCFactory(sm.getInfrastructure(),
                    sm.getRandom());
            TLController tlc = tlcf.genTLC(cat, nr);
            tlc.showSettings(this);
            sm.setTLController(tlc);
            setColearningEnabled((tlc instanceof Colearning));
        } catch (TrafficException e) {
            reportError(e.fillInStackTrace());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    private void setColearningEnabled(boolean b) {
        if (!b &&
                (menuBar.getDPMenu().getSelectedIndex() == DPFactory.COLEARNING)) {
            menuBar.getDPMenu().select(DPFactory.SHORTEST_PATH);
            setDrivingPolicy(DPFactory.SHORTEST_PATH);
        }

        ((CheckboxMenuItem) menuBar.getDPMenu().getItem(DPFactory.COLEARNING)).setEnabled(b);
    }

    /**
     * Shows the file properties dialog
     */
    public void showFilePropertiesDialog() {
        String simName = getSimModel().getSimName();
        Infrastructure infra = getSimModel().getInfrastructure();
        String comments = infra.getComments();

        SimPropDialog propDialog = new SimPropDialog(this, simName, comments);

        propDialog.show();

        if (propDialog.ok()) {
            getSimModel().setSimName(propDialog.getSimName());
            infra.setComments(propDialog.getComments());
        }

        this.setStatus("Simulation \"" + getSimModel().getSimName() + "\".");
    }

    /**
     * Creates a right-click popup-menu for the given object
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws PopupException DOCUMENT ME!
     */
    public PopupMenu getPopupMenuFor(Selectable obj) throws PopupException {
        SimPopupMenuFactory pmf = new SimPopupMenuFactory(this);

        return pmf.getPopupMenuFor(obj);
    }

    /**
     * Returns the filename of the currently loaded file
     *
     * @return DOCUMENT ME!
     */
    public String getCurrentFilename() {
        return currentFilename;
    }

    /**
     * Sets the speed of the simulation
     *
     * @param speed DOCUMENT ME!
     */
    public void setSpeed(int speed) {
        ((SimToolBar) toolBar).getSpeed().select(speed);
        menuBar.getSpeedMenu().select(speed);
        getSimModel().setSpeed(speedSettings[speed]);
    }

    /**
     * Makes model do one step
     */
    public void doStep() {
        getSimModel().doStep();
    }

    /**
     * Paues the simulation
     */
    public void pause() {
        setStatus("Paused at cycle " + getSimModel().getCurCycle() + ".");
        getSimModel().pause();
    }

    /**
     * Resumes or starts the simulation
     */
    public void unpause() {
        setStatus("Simulation running.");
        getSimModel().unpause();
    }

    /**
     * Stops the simulation and resets the infrastructure
     */
    public void stop() {
        int cycle = getSimModel().getCurCycle();

        if (cycle != 0) {
            setStatus("Stopped at cycle " + ".");
        }

        try {
            getSimModel().pause();
            getSimModel().reset();
        } catch (SimulationRunningException ex) {
            reportError(ex.fillInStackTrace());
            getSimModel().unpause();
        }
    }

    /**
     * Starts a series of 10 simulations
     */
    public void runSeries() {
        setStatus("Running a Series of simulations.");
        TrackerFactory.purgeTrackers();

        try {
            TrackerFactory.showTracker(getSimModel(), this,
                TrackerFactory.TOTAL_QUEUE);
            TrackerFactory.showTracker(getSimModel(), this,
                TrackerFactory.TOTAL_WAIT);
            TrackerFactory.showTracker(getSimModel(), this,
                TrackerFactory.TOTAL_ROADUSERS);
            TrackerFactory.showTracker(getSimModel(), this,
                TrackerFactory.TOTAL_JUNCTION);
            TrackerFactory.disableTrackerViews();
        } catch (TrafficException e) {
            reportError(e.fillInStackTrace());
        }

        menuBar.setViewEnabled(false);
        menuBar.setCycleCounterEnabled(false);
        this.setViewEnabled(false);
        this.setCycleCounterEnabled(false);

        this.setSpeed((speedSettings.length - 1));

        getSimModel().runSeries();
    }

    /**
     * DOCUMENT ME!
     */
    public void nextSeries() {
        getSimModel().pause();

        int curSeries = getSimModel().getCurSeries();

        // If we have data, save it
        if (curSeries > 0) {
            String simName = getSimModel().getSimName();
            String tlcName = TLCFactory.getDescription(TLCFactory.getNumberByXMLTagName(
                        XMLUtils.getLastName(getSimModel().getTLController()
                                                 .getXMLName())));
            String dpName = DPFactory.getDescription(DPFactory.getNumberByXMLTagName(
                        XMLUtils.getLastName(getSimModel().getDrivingPolicy()
                                                 .getXMLName())));
            TrackingController[] tca = TrackerFactory.getTrackingControllers();

            for (int i = 0; i < tca.length; i++) {
                TrackingView tv = tca[i].getTrackingView();
                String filename = simName + " - " + tlcName + " - " + dpName +
                    " - " + tv.getDescription() + " - run " + curSeries +
                    ".dat";

                try {
                    tv.saveData(filename, getSimModel());
                } catch (IOException exc) {
                    showError("Couldn't save statistical data from series!");
                }
            }
        }

        // If we have more runs to run, do so.
        if (curSeries < getSimModel().getNumSeries()) {
            setStatus("Running a series of simulations, currently at: " +
                curSeries);

            try {
                getSimModel().reset();
            } catch (SimulationRunningException e) {
            }

            getSimModel().nextCurSeries();
            getSimModel().unpause();
        } else {
            setStatus("Done running Series of simulations.");
            getSimModel().stopSeries();
            TrackerFactory.purgeTrackers();
        }
    }

    /**
     * Opens the editor
     */
    public void openEditor() {
        if (editController == null) {
            editController = new EditController(new EditModel(), false);
        }

        editController.show();
        editController.requestFocus();
    }

    /**
     * Set temp debug infra
     *
     * @param nr DOCUMENT ME!
     */
    protected void setInfra(int nr) {
        Infrastructure infra;

        switch (nr) {
        case 1:
            infra = new SimpleInfra();

            break;

        case 2:
            infra = new LessSimpleInfra();

            break;

        // case 3:
        //infra = new NetTunnelTest1();

        //break;

        // case 4:
        //infra = new NetTunnelTest2();

        //break;
        default:
            infra = new Infrastructure();

            break;
        }

        try {
            Vector errors = (new Validation(infra)).validate();

            if (!errors.isEmpty()) {
                showError(errors.toString());
            }
        } catch (InfraException e) {
            reportError(e);
        }

        getSimModel().setCurCycle(0);
        newInfrastructure(infra);
    }
}
