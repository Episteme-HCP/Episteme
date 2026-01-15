/*
 *  LogPanel.java
 *
    Copyright (C) 2005  Brandon Wegge and Herb Smith

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
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
