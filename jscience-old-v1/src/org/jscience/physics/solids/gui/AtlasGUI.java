/*
 * AtlasGUI.java
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

import org.jscience.physics.solids.AtlasModel;
import org.jscience.physics.solids.AtlasPreferences;
import org.jscience.physics.solids.examples.PlateHoleModel;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;

import javax.swing.*;


/**
 * 
DOCUMENT ME!
 *
 * @author Wegge
 */
public class AtlasGUI extends JFrame {
    //static Logger logger = Logger.getLogger((AtlasGUI.class).getName());
    /**
     * DOCUMENT ME!
     */
    private AtlasModel fem;

    /**
     * DOCUMENT ME!
     */
    private AtlasTree tree;

    /**
     * DOCUMENT ME!
     */
    private AtlasViewer viewer;

    /**
     * DOCUMENT ME!
     */
    private JInternalFrame viewerFrame;

    /**
     * DOCUMENT ME!
     */
    private LogPanel logPanel;

/**
     * Creates a new instance of AtlasGUI
     */
    public AtlasGUI() {
        //Set up logging stuff
        logPanel = new LogPanel();
        //logger.addAppender( logPanel );
        //logger.info("Starting ATLAS GUI");
        setTitle("ATLAS GUI");

        //Make the same size as the screen- minus something on the bottom.
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screen.width);
        int h = (int) (screen.height - 25);
        setSize(w, h);

        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    exit();
                }
            });

        initComponents();
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AtlasPreferences pref = new AtlasPreferences();
        pref.setLoggingLevel(1);

        AtlasGUI gui = new AtlasGUI();
        gui.setVisible(true);

        //Load any files specified on the command line
        /*try {
        
            String filename = "C:\\temp\\model.xml";
            AtlasModel fem = new AtlasModel("Whuh");
            fem.readXML( filename );
            gui.setAtlasModel(fem);
        
        } catch (IOException io) {
            io.printStackTrace();
        }*/
        PlateHoleModel model = new PlateHoleModel();

        gui.setAtlasModel(model.getModel());
    }

    /**
     * Exits the application.
     */
    public void exit() {
        //logger.info("Exiting ATLAS GUI");
        System.exit(0);
    }

    /**
     * Lays out all of the GUI components
     */
    public void initComponents() {
        this.setJMenuBar(new AtlasMenu(this));

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        //Tabbed pane with logger, etc..
        JTabbedPane panes = new JTabbedPane();

        panes.add("Logger", logPanel.getGUIPanel());
        panes.add("Solution", new JPanel());

        //Top Split Pane
        tree = new AtlasTree();
        viewer = new AtlasViewer();

        JDesktopPane viewerDesktop = new JDesktopPane();
        viewerFrame = new JInternalFrame("ATLAS Viewer", true, true, true, true);
        viewerFrame.getContentPane().add(viewer);
        viewerDesktop.add(viewerFrame);
        viewerFrame.setVisible(true);

        JPanel displayPanel = new JPanel();

        //Set up tabbed panes that holds the tress,etc..
        JTabbedPane treeTabs = new JTabbedPane();

        treeTabs.add("ATLAS Model", new JScrollPane(tree));
        treeTabs.add("Display Control", displayPanel);
        treeTabs.add("Solution DIsplay", new JPanel());

        JSplitPane topSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                treeTabs, viewerDesktop);

        //Main splitpane
        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                topSplit, panes);

        container.add(mainSplit, BorderLayout.CENTER);

        // Make sure the split panes are in a good location
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screen.width);
        int h = (int) (screen.height - 25);
        topSplit.setDividerLocation((int) (.3 * w));
        mainSplit.setDividerLocation((int) (.7 * h));
        viewerFrame.setSize(200, 200);

        try {
            viewerFrame.setMaximum(true);
        } catch (java.beans.PropertyVetoException veto) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param model DOCUMENT ME!
     */
    public void setAtlasModel(AtlasModel model) {
        this.fem = model;

        viewer.setModel(model);
        viewer.fitView();
        viewerFrame.setTitle(model.getName());

        tree.setAtlasModel(model);

        //logger.info(model.getName() + " is being displayed in AtlasGUI.");
    }

    /**
     * Opens the specified file.
     *
     * @param filename DOCUMENT ME!
     */
    public void openAtlasModel(String filename) {
        AtlasModel newFEM = new AtlasModel();

        try {
            newFEM.readXML(filename);

            this.setAtlasModel(newFEM);
        } catch (IOException io) {
            //logger.error( "Couldn't read file.", io);
        }
    }
}
