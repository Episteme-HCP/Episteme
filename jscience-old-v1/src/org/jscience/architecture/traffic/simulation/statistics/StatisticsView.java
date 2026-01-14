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

import java.awt.*;

import java.util.Observable;
import java.util.Observer;


/**
 * The view of the statistics viewer, controlled by StatisticsController.
 *
 * @author Group GUI
 * @version 1.0
 */
public abstract class StatisticsView extends Canvas implements Observer {
    /** Separator used when saving data. */
    public static String SEP = "\t";

    /** DOCUMENT ME! */
    protected StatisticsController parent;

    /** DOCUMENT ME! */
    protected StatisticsModel stats;

    /** DOCUMENT ME! */
    protected int horScroll;

    /** DOCUMENT ME! */
    protected int verScroll;

    /** DOCUMENT ME! */
    protected Rectangle paintArea;

    /** DOCUMENT ME! */
    protected final Font titleFont = new Font("arial", Font.BOLD, 14);

    /** DOCUMENT ME! */
    protected final Font infoFont = new Font("arial", Font.PLAIN, 12);

    /** DOCUMENT ME! */
    protected final Font tableFont = new Font("arial", Font.PLAIN, 11);

/**
     * Creates a <code>StatisticsView</code>.
     *
     * @param _parent The<code>SimModel</code> statistics should be read from.
     * @param _stats  DOCUMENT ME!
     */
    public StatisticsView(StatisticsController _parent, StatisticsModel _stats) {
        parent = _parent;
        stats = _stats;
        horScroll = verScroll = 0;
        paintArea = getBounds();
    }

    /**
     * Invoked when the StatisticsModel is changed.
     *
     * @param obs DOCUMENT ME!
     * @param obj DOCUMENT ME!
     */
    public void update(Observable obs, Object obj) {
        update();
    }

    /**
     * Invoked when the view should be redone.
     */
    public void update() {
        paintAreaChanged();
        repaint();
    }

    /**
     * Paints the view.
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.black);
        g.setFont(titleFont);
        g.drawString("Statistics for simulation \"" + stats.getSimName() +
            "\" (at cycle " + stats.getCycle() + ")", 20, 20);
        g.setFont(infoFont);
        g.drawString("Infrastructure: \"" + stats.getInfraName() + "\" by " +
            stats.getInfraAuthor(), 20, 40);

        paintStats(g);
    }

    /*============================================*/
    /* GET AND SET                                */
    /*============================================*/
    /**
     * Sets the StatisticsModel to be shown.
     *
     * @param _stats DOCUMENT ME!
     */
    public void setStatisticsModel(StatisticsModel _stats) {
        stats = _stats;
        update();
    }

    /**
     * Returns the StatisticsModel to be shown.
     *
     * @return DOCUMENT ME!
     */
    public StatisticsModel getStatisticsModel() {
        return stats;
    }

    /**
     * Sets horScroll, the horizontal scrolling value.
     *
     * @param hs DOCUMENT ME!
     */
    public void setHorScroll(int hs) {
        horScroll = hs;
        update();
    }

    /**
     * Returns horScroll, the horizontal scrolling value.
     *
     * @return DOCUMENT ME!
     */
    public int getHorScroll() {
        return horScroll;
    }

    /**
     * Sets verScroll, the vertical scrolling value.
     *
     * @param vs DOCUMENT ME!
     */
    public void setVerScroll(int vs) {
        verScroll = vs;
        update();
    }

    /**
     * Returns verScroll, the vertical scrolling value.
     *
     * @return DOCUMENT ME!
     */
    public int getVerScroll() {
        return verScroll;
    }

    /**
     * Sets the extension-specific paint area of this view.
     *
     * @param r DOCUMENT ME!
     */
    protected void setPaintArea(Rectangle r) {
        paintArea = r;
        update();
    }

    /**
     * Returns the extension-specific paint area of this view.
     *
     * @return DOCUMENT ME!
     */
    protected Rectangle getPaintArea() {
        return paintArea;
    }

    /**
     * Overrides default method to change paintArea accordingly.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     */
    public void setBounds(int x, int y, int w, int h) {
        int paw = Math.max(1, w - 40);
        int pah = Math.max(1, h - 75);
        setPaintArea(new Rectangle(20, 55, paw, pah));
        super.setBounds(x, y, w, h);
    }

    /*============================================*/
    /* ABSTRACT METHODS                           */
    /*============================================*/
    /**
     * Invoked when the paintArea is changed.
     */
    protected abstract void paintAreaChanged();

    /**
     * Paints the statistics in Rectangle r on the view.
     *
     * @param g DOCUMENT ME!
     */
    protected abstract void paintStats(Graphics g);
}
