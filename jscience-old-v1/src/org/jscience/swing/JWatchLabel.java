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

package org.jscience.swing;

import org.jscience.util.Stopwatch;
import org.jscience.util.TimeSystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.DateFormat;

import java.util.Date;

import javax.swing.*;


/**
 * JWatchLabel is a JComponent that displays either the current time or a
 * given Stopwatch updated every second
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.util.Stopwatch
 */
public class JWatchLabel extends JLabel {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 4898200148947939967L;

    /** DOCUMENT ME! */
    private Date date;

    /** DOCUMENT ME! */
    private DateFormat format;

    /** DOCUMENT ME! */
    private TimeSystem ts;

    /** DOCUMENT ME! */
    private Stopwatch watch;

    /** DOCUMENT ME! */
    private Timer timer;

    /** DOCUMENT ME! */
    private boolean active;

    /** DOCUMENT ME! */
    int interval = 1000;

/**
     * initiates a JWatchLabel that displays the current time
     */
    public JWatchLabel() {
        this(DateFormat.getTimeInstance(DateFormat.MEDIUM));
    }

/**
     * Creates a new JWatchLabel object.
     *
     * @param ts DOCUMENT ME!
     */
    public JWatchLabel(TimeSystem ts) {
        this(null, ts);
    }

/**
     * Creates a new JWatchLabel object.
     *
     * @param format DOCUMENT ME!
     */
    public JWatchLabel(DateFormat format) {
        this(format, null);
    }

/**
     * Creates a new JWatchLabel object.
     *
     * @param format DOCUMENT ME!
     * @param ts     DOCUMENT ME!
     */
    public JWatchLabel(DateFormat format, TimeSystem ts) {
        super(format.format(new Date()));
        this.format = format;
        this.ts = ts;
        date = new Date();
        activate();
    }

/**
     * initiates a JWatchLabel that displays the given Stopwatch
     *
     * @param watch DOCUMENT ME!
     */
    public JWatchLabel(Stopwatch watch) {
        super(watch.display());
        this.watch = watch;
        activate();
    }

    /**
     * DOCUMENT ME!
     */
    private void activate() {
        timer = new Timer(interval,
                new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        if (watch == null) {
                            date.setTime((ts == null)
                                ? System.currentTimeMillis()
                                : ts.currentTimeMillis());
                            setText(format.format(date));
                        } else {
                            setText(watch.display());
                        }
                    }
                });
        timer.start();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TimeSystem getTimeSystem() {
        return ts;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DateFormat getDateFormat() {
        return format;
    }

    /**
     * DOCUMENT ME!
     *
     * @param format DOCUMENT ME!
     */
    public void setDateFormat(DateFormat format) {
        this.format = format;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Stopwatch getStopwatch() {
        return watch;
    }
}
