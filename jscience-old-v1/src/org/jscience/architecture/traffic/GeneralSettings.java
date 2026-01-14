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

package org.jscience.architecture.traffic;

import org.jscience.architecture.traffic.algorithms.tlc.SignController;
import org.jscience.architecture.traffic.configuration.ConfigDialog;
import org.jscience.architecture.traffic.infrastructure.RoaduserFactory;
import org.jscience.architecture.traffic.simulation.SimModel;
import org.jscience.architecture.traffic.simulation.statistics.StatisticsModel;
import org.jscience.architecture.traffic.simulation.statistics.TrackingView;
import org.jscience.architecture.traffic.xml.*;

import java.io.File;
import java.io.IOException;

import java.util.NoSuchElementException;


/**
 * This class manages the general settings
 */
public class GeneralSettings implements Settings {
    /** DOCUMENT ME! */
    public static final int COMPRESSION_NO = 0;

    /** DOCUMENT ME! */
    public static final int COMPRESSION_GZIP = 1;

    /** DOCUMENT ME! */
    public static final String settingsFile = "org.jscience.architecture.traffic/settings.conf";

    /** DOCUMENT ME! */
    public static Settings currentSettings;

    /**
     * Indicates if we should use our (slow) internal browser to show
     * the help files
     */
    protected boolean useInternalHelpBrowser = true;

    /** Sound on/off */
    protected boolean sound = true;

    /**
     * Name and command for starting an external browser. These values
     * only have meaning if the internal browser is off
     */
    protected String browserName = "";

    /**
     * Name and command for starting an external browser. These values
     * only have meaning if the internal browser is off
     */
    protected String browserCommand = "";

    /**
     * Compression method to use on the XML files we generate (see
     * constants above
     */
    protected int compressionMethod = COMPRESSION_NO;

    /** The standard path for file dialogs */
    protected String standardPath = ".";

/**
     * Create empty GeneralSettings (for loading)
     */
    public GeneralSettings() {
        currentSettings = this;
    }

    /**
     * Gets the current settings
     *
     * @return DOCUMENT ME!
     */
    public static Settings getCurrentSettings() {
        return currentSettings;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws XMLTreeException DOCUMENT ME!
     * @throws XMLInvalidInputException DOCUMENT ME!
     */
    public void doLoad()
        throws IOException, XMLTreeException, XMLInvalidInputException {
        File file = new File(settingsFile);

        if (file.exists()) {
            XMLLoader loader = new XMLLoader(new File(settingsFile));
            load(loader.getNextElement(null, "settings"), loader);
            loader.close();
        } else {
            System.out.println(
                "Settings file not found. Loading default settings");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws XMLTreeException DOCUMENT ME!
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public void doSave()
        throws IOException, XMLTreeException, XMLCannotSaveException {
        XMLSaver saver = new XMLSaver(new File(settingsFile));
        saver.saveObject(this);
        saver.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isPredefined() {
        return true;
    }

    // Set methods
    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public void setProperty(String name, String value)
        throws NoSuchElementException {
        if ("browser-name".equals(name)) {
            browserName = value;
        } else if ("browser-command".equals(name)) {
            browserCommand = value;
        } else if ("standard-path".equals(name)) {
            standardPath = value;
        } else {
            throw new NoSuchElementException(
                "GeneralSettings has no String property named " + name);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public void setProperty(String name, int value)
        throws NoSuchElementException {
        if ("compression".equals(name)) {
            compressionMethod = value;
        } else {
            throw new NoSuchElementException(
                "GeneralSettings has no int property named " + name);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public void setProperty(String name, boolean value)
        throws NoSuchElementException {
        if ("use-jbrowser".equals(name)) {
            useInternalHelpBrowser = value;
        } else if ("sound".equals(name)) {
            sound = value;
        } else {
            throw new NoSuchElementException(
                "GeneralSettings has no boolean property named " + name);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public void setPropertyFloatValue(String name, float value)
        throws NoSuchElementException {
        throw new NoSuchElementException(
            "GeneralSettings has no float property named " + name);
    }

    // Get methods
    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public String getPropertyStringValue(String name)
        throws NoSuchElementException {
        if ("browser-name".equals(name)) {
            return browserName;
        } else if ("browser-command".equals(name)) {
            return browserCommand;
        } else if ("standard-path".equals(name)) {
            return standardPath;
        } else {
            throw new NoSuchElementException(
                "GeneralSettings has no String property named " + name);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public boolean getPropertyBooleanValue(String name)
        throws NoSuchElementException {
        if ("use-jbrowser".equals(name)) {
            return useInternalHelpBrowser;
        } else if ("sound".equals(name)) {
            return sound;
        } else {
            throw new NoSuchElementException(
                "GeneralSettings has no boolean property named " + name);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public int getPropertyIntValue(String name) throws NoSuchElementException {
        if ("compression".equals(name)) {
            return compressionMethod;
        } else {
            throw new NoSuchElementException(
                "GeneralSettings has no int property named " + name);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public float getPropertyFloatValue(String name)
        throws NoSuchElementException {
        throw new NoSuchElementException(
            "GeneralSettings has no float property named " + name);
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
        useInternalHelpBrowser = myElement.getAttribute("use-jbrowser")
                                          .getBoolValue();
        sound = myElement.getAttribute("sound").getBoolValue();
        browserName = myElement.getAttribute("browser-name").getValue();
        browserCommand = myElement.getAttribute("browser-command").getValue();
        compressionMethod = myElement.getAttribute("compression").getIntValue();
        standardPath = myElement.getAttribute("standard-path").getValue();

        RoaduserFactory.PacChance = myElement.getAttribute("paccar-prob")
                                             .getFloatValue();
        ConfigDialog.AlwaysOnTop = myElement.getAttribute("confd-alwaysontop")
                                            .getBoolValue();
        SignController.CrossNodesSafely = myElement.getAttribute(
                "signc-safenodecross").getBoolValue();
        SimModel.CrossNodes = myElement.getAttribute("simm-crossnodes")
                                       .getBoolValue();
        TrackingView.SEP = myElement.getAttribute("stats-sep").getValue();
        RoaduserFactory.UseCustoms = myElement.getAttribute("infra-usecustoms")
                                              .getBoolValue();

        StatisticsModel.SEP = TrackingView.SEP;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        XMLElement result = new XMLElement(getXMLName());
        result.addAttribute(new XMLAttribute("use-jbrowser",
                useInternalHelpBrowser));
        result.addAttribute(new XMLAttribute("sound", sound));
        result.addAttribute(new XMLAttribute("browser-name", browserName));
        result.addAttribute(new XMLAttribute("browser-command", browserCommand));
        result.addAttribute(new XMLAttribute("compression", compressionMethod));
        result.addAttribute(new XMLAttribute("standard-path", standardPath));

        result.addAttribute(new XMLAttribute("paccar-prob",
                RoaduserFactory.PacChance));
        result.addAttribute(new XMLAttribute("confd-alwaysontop",
                ConfigDialog.AlwaysOnTop));
        result.addAttribute(new XMLAttribute("signc-safenodecross",
                SignController.CrossNodesSafely));
        result.addAttribute(new XMLAttribute("simm-crossnodes",
                SimModel.CrossNodes));
        result.addAttribute(new XMLAttribute("stats-sep", TrackingView.SEP));
        result.addAttribute(new XMLAttribute("infra-usecustoms",
                RoaduserFactory.UseCustoms));

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
        throws XMLTreeException, IOException, XMLCannotSaveException { // GeneralSettings has no child objects. childSettings are saved to

        // other settings files
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return "settings";
    }

    /**
     * DOCUMENT ME!
     *
     * @param parentName DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     */
    public void setParentName(String parentName) throws XMLTreeException {
        throw new XMLTreeException(
            "GeneralSettings does not support setParentName(String)");
    }
}
