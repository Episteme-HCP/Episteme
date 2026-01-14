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

package org.jscience.physics.solids.gui;

import java.awt.*;

import javax.swing.*;


/**
 * A JPanel that registers log events.
 *
 * @author Default
 */
public class LogPanel { //implements Appender {

    /**
     * DOCUMENT ME!
     */
    private JPanel panel = new JPanel();

    /**
     * DOCUMENT ME!
     */
    private JTextArea text = new JTextArea();

/**
     * Creates a new instance of LogPanel
     */
    public LogPanel() {
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(text), "Center");
    }

    //public void addFilter(org.apache.log4j.spi.Filter filter) {
    /**
     * DOCUMENT ME!
     */
    public void clearFilters() {
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
    }

    //public void doAppend(org.apache.log4j.spi.LoggingEvent loggingEvent) {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean requiresLayout() {
        return false;
    }

    //public void setErrorHandler(org.apache.log4j.spi.ErrorHandler errorHandler) {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "Log Panel";
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setName(String s) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JPanel getGUIPanel() {
        return panel;
    }
}
