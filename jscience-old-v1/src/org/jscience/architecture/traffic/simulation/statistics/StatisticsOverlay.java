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

package org.jscience.architecture.traffic.simulation.statistics;

import org.jscience.architecture.traffic.Overlay;
import org.jscience.architecture.traffic.TrafficException;
import org.jscience.architecture.traffic.View;
import org.jscience.architecture.traffic.infrastructure.Infrastructure;
import org.jscience.architecture.traffic.infrastructure.Junction;
import org.jscience.architecture.traffic.infrastructure.SpecialNode;
import org.jscience.architecture.traffic.simulation.SimModel;
import org.jscience.architecture.traffic.xml.*;

import java.awt.*;

import java.io.IOException;

import java.util.Observable;
import java.util.Observer;


/**
 * Overlay for <code>gld.View</code>. Shows waiting queue lengths and
 * relative average junction waiting times.
 *
 * @author Group GUI
 * @version 1.0
 */
public class StatisticsOverlay implements Overlay, Observer, XMLSerializable {
    /** Current infrastructure. */
    Infrastructure infra;

    /** DOCUMENT ME! */
    SpecialNode[] specialNodes;

    /** DOCUMENT ME! */
    Junction[] junctions;

    /** DOCUMENT ME! */
    float[] specialData;

    /** DOCUMENT ME! */
    float[] junctionData;

    /** DOCUMENT ME! */
    float specialMax;

    /** DOCUMENT ME! */
    float junctionMax;

    /** DOCUMENT ME! */
    int specialNum;

    /** DOCUMENT ME! */
    int junctionNum;

    /** DOCUMENT ME! */
    String parentName = "controller";

/**
     * Creates a default <code>StatisticsOverlay</code>.
     *
     * @param _view  DOCUMENT ME!
     * @param _infra DOCUMENT ME!
     */
    public StatisticsOverlay(View _view, Infrastructure _infra) {
        setInfrastructure(_infra);
    }

    /**
     * Sets a new infrastructure as the current one and rereads all
     * data.
     *
     * @param _infra DOCUMENT ME!
     */
    public void setInfrastructure(Infrastructure _infra) {
        infra = _infra;
        specialNodes = infra.getSpecialNodes();
        specialNum = specialNodes.length;
        specialData = new float[specialNum];
        junctions = infra.getJunctions();
        junctionNum = junctions.length;
        junctionData = new float[junctionNum];

        specialMax = junctionMax = 0;

        for (int i = 0; i < specialNum; i++)
            specialData[i] = 0;

        for (int i = 0; i < junctionNum; i++)
            junctionData[i] = 0;
    }

    /**
     * Updates the current data.
     *
     * @param obs DOCUMENT ME!
     * @param obj DOCUMENT ME!
     */
    public void update(Observable obs, Object obj) {
        Infrastructure _infra = ((SimModel) obs).getInfrastructure();

        if (infra != _infra) {
            setInfrastructure(_infra);
        }

        refreshData();
    }

    /**
     * Rereads the statistical data from the model.
     */
    public void refreshData() {
        specialMax = 0;
        junctionMax = 0.01f;

        for (int i = 0; i < specialNum; i++)
            if ((specialData[i] = specialNodes[i].getWaitingQueueLength()) > specialMax) {
                specialMax = specialData[i];
            }

        for (int i = 0; i < junctionNum; i++)
            if ((junctionData[i] = junctions[i].getStatistics(0)
                                                   .getAvgWaitingTime(true)) > junctionMax) {
                junctionMax = junctionData[i];
            }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int overlayType() {
        return 2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     *
     * @throws TrafficException DOCUMENT ME!
     */
    public void paint(Graphics g) throws TrafficException {
        g.setPaintMode();

        // Waiting queues
        g.setColor(Color.blue);

        for (int i = 0; i < specialNum; i++) {
            Rectangle r = specialNodes[i].getBounds();
            int dy = (int) (specialData[i] / 2);

            if (specialNodes[i].getRoadPos() == 3) {
                r.x += (r.width + 5);
            } else {
                r.x -= 10;
            }

            r.y += (r.height - dy + 1);
            r.width = 5;
            r.height = dy;
            g.fillRect(r.x, r.y, r.width, r.height);
        }

        // Average junction waiting times
        for (int i = 0; i < junctionNum; i++) {
            Rectangle r = junctions[i].getBounds();
            g.setColor(new Color(0, 0, 0.7f,
                    (0.6f * junctionData[i]) / junctionMax));
            g.fillRect(r.x, r.y, r.width, r.height);
        }
    }

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
        specialMax = myElement.getAttribute("special-max").getFloatValue();
        junctionMax = myElement.getAttribute("junction-max").getFloatValue();
        specialData = (float[]) XMLArray.loadArray(this, loader);
        junctionData = (float[]) XMLArray.loadArray(this, loader);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        XMLElement result = new XMLElement("overlaystats");
        result.addAttribute(new XMLAttribute("special-max", specialMax));
        result.addAttribute(new XMLAttribute("junction-max", junctionMax));

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
        XMLArray.saveArray(specialData, this, saver, "special-data");
        XMLArray.saveArray(junctionData, this, saver, "junction-data");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return parentName + ".overlaystats";
    }

    /**
     * DOCUMENT ME!
     *
     * @param parentName DOCUMENT ME!
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
