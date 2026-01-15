/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import java.lang.reflect.InvocationTargetException;

import java.util.TimerTask;

import javax.swing.*;


/**
 * a component that displays current memory consumption
 *
 * @author Holger Antelmann
 */
public class JMemoryGauge extends JComponent {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -8356961775615026372L;

    /** DOCUMENT ME! */
    Runtime rt = Runtime.getRuntime();

    /** DOCUMENT ME! */
    JProgressBar usedOfTotal;

    /** DOCUMENT ME! */
    JProgressBar totalOfMax;

    /** DOCUMENT ME! */
    long interval = 500;

    /** DOCUMENT ME! */
    java.util.Timer timer = null;

/**
     * Creates a new JMemoryGauge object.
     */
    public JMemoryGauge() {
        this(true);
    }

/**
     * Creates a new JMemoryGauge object.
     *
     * @param activateNow DOCUMENT ME!
     */
    public JMemoryGauge(boolean activateNow) {
        setName("MemoryGauge");
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        usedOfTotal = new JProgressBar(JProgressBar.HORIZONTAL);
        totalOfMax = new JProgressBar(JProgressBar.HORIZONTAL);
        add(new JLabel("used of total"));
        add(usedOfTotal);
        add(new JLabel("total of max"));
        add(totalOfMax);
        usedOfTotal.setStringPainted(true);
        totalOfMax.setStringPainted(true);
        usedOfTotal.setMinimum(0);
        totalOfMax.setMinimum(0);
        totalOfMax.setMaximum((int) (rt.maxMemory() / 1024));

        if (activateNow) {
            activate();
        }
    }

    /**
     * returns 500 by default (half a second)
     *
     * @return DOCUMENT ME!
     */
    public long getIntervalMillis() {
        return interval;
    }

    /**
     * DOCUMENT ME!
     *
     * @param millis DOCUMENT ME!
     *
     * @throws IllegalStateException if the gauge is currently activated
     */
    public synchronized void setIntervalMillis(long millis) {
        if (timer != null) {
            throw new IllegalStateException();
        }

        this.interval = millis;
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void activate() {
        if (timer != null) {
            return;
        }

        timer = new java.util.Timer("JMemoryGauge.Updater", true);
        timer.schedule(new Updater(), 0, interval);
        totalOfMax.setEnabled(true);
        usedOfTotal.setEnabled(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean isActivated() {
        return timer != null;
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void deactivate() {
        if (timer == null) {
            return;
        }

        timer.cancel();
        timer = null;
        totalOfMax.setEnabled(false);
        usedOfTotal.setEnabled(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag DOCUMENT ME!
     */
    public void setVisible(boolean flag) {
        if (flag) {
            activate();
        } else {
            deactivate();
        }

        super.setVisible(true);
    }

    /**
     * DOCUMENT ME!
     */
    protected void finalize() {
        deactivate();
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    final class Updater extends TimerTask {
        /**
         * DOCUMENT ME!
         */
        public void run() {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            int free = (int) (rt.freeMemory() / 1024);
                            int total = (int) (rt.totalMemory() / 1024);
                            int used = total - free;
                            usedOfTotal.setValue(used);
                            usedOfTotal.setMaximum(free + total);
                            usedOfTotal.setString("used: " + used + " kb");
                            totalOfMax.setValue(total);
                            totalOfMax.setString("total: " + total + " kb");
                        }
                    });
            } catch (InterruptedException ignore) {
            } catch (InvocationTargetException ignore) {
            }
        }
    }
}
