/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util;

import javax.swing.*;


/**
 * The relevant calls to the monitor are delegated to a ProgressMonitor or
 * JProgressBar.
 *
 * @author Holger Antelmann
 */
public class MonitorDelegator extends Monitor {
    /** DOCUMENT ME! */
    ProgressMonitor mon = null;

    /** DOCUMENT ME! */
    JProgressBar bar = null;

/**
     * Creates a new MonitorDelegator object.
     */
    public MonitorDelegator() {
    }

/**
     * Creates a new MonitorDelegator object.
     *
     * @param mon DOCUMENT ME!
     */
    public MonitorDelegator(ProgressMonitor mon) {
        this.mon = mon;
    }

/**
     * Creates a new MonitorDelegator object.
     *
     * @param bar DOCUMENT ME!
     */
    public MonitorDelegator(JProgressBar bar) {
        this.bar = bar;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ProgressMonitor getProgressMonitor() {
        return mon;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JProgressBar getJProgressBar() {
        return bar;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mon DOCUMENT ME!
     */
    public void setProgressMonitor(ProgressMonitor mon) {
        this.mon = mon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bar DOCUMENT ME!
     */
    public void setJProgressBar(JProgressBar bar) {
        this.bar = bar;
    }

    /**
     * DOCUMENT ME!
     *
     * @param number DOCUMENT ME!
     */
    public void setNumber(int number) {
        super.setNumber(number);

        if (mon != null) {
            mon.setProgress(getNumber());
        }

        if (bar != null) {
            bar.setValue(getNumber());
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void increment() {
        super.increment();

        if (mon != null) {
            mon.setProgress(getNumber());
        }

        if (bar != null) {
            bar.setValue(getNumber());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param msg DOCUMENT ME!
     */
    public void setMessage(String msg) {
        super.setMessage(msg);

        if (mon != null) {
            mon.setNote(getMessage());
        }

        if (bar != null) {
            bar.setString(getMessage());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param number DOCUMENT ME!
     */
    public void setMin(int number) {
        super.setMin(number);

        if (mon != null) {
            mon.setMinimum(getMin());
        }

        if (bar != null) {
            bar.setMinimum(getMin());
            bar.setIndeterminate(getMin() >= getMax());
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void done() {
        super.done();

        if (mon != null) {
            mon.close();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean enabled() {
        return !disabled();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean disabled() {
        if (mon != null) {
            if (mon.isCanceled()) {
                disable();
            }
        }

        return super.disabled();
    }

    /**
     * DOCUMENT ME!
     *
     * @param number DOCUMENT ME!
     */
    public void setMax(int number) {
        super.setMax(number);

        if (mon != null) {
            mon.setMaximum(getMax());
        }

        if (bar != null) {
            bar.setMaximum(getMax());
            bar.setIndeterminate(getMin() >= getMax());
        }
    }
}
