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

package org.jscience.computing.kmeans.gui;

import org.jscience.computing.kmeans.DataSet;
import org.jscience.computing.kmeans.KMeans;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Draws the main window for visualizing the data.
 *
 * @author Harlan Crystal <hpc4@cornell.edu>
 * @version $Id: DataSetView.java,v 1.2 2007-10-21 21:08:01 virtualcall Exp $
 * @date April 17, 2003
 */
public class DataSetView extends JFrame implements ActionListener,
        ChangeListener {
    /**
     * The dataset being represented
     */
    private DataSet dataset;

    /**
     * The graph object displaying the dataset
     */
    private Graph graph;

    /**
     * The spinbox
     */
    private JSpinner spin;

    /**
     * The object used for generating clusters
     */
    private KMeans kmeans;

    /**
     * The number of clusters we're seaching for
     */
    private int numClusters;

    /**
     * Constructs a new window.
     */
    DataSetView() {
        super("DataSetView");
        numClusters = 0;

        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        JMenu menu = new JMenu("File");
        menubar.add(menu);
        JMenuItem menuitem = new JMenuItem("Open DataFile");
        menuitem.addActionListener(this);
        menu.add(menuitem);
        menuitem = new JMenuItem("Quit");
        menuitem.addActionListener(this);
        menu.add(menuitem);
        menu = new JMenu("Settings");
        menubar.add(menu);
        JCheckBoxMenuItem cmenuitem = new JCheckBoxMenuItem("Display Means",
                true);
        cmenuitem.addActionListener(this);
        menu.add(cmenuitem);
        cmenuitem = new JCheckBoxMenuItem("Display Axis", true);
        cmenuitem.addActionListener(this);
        menu.add(cmenuitem);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.dataset = dataset;
        graph = new Graph();

        getContentPane().add(graph, BorderLayout.CENTER);

        JPanel controls = new JPanel();

        JLabel label = new JLabel("Number of Clusters: ");
        controls.add(label);
        spin = new JSpinner();
        spin.addChangeListener(this);
        controls.add(spin);

        JButton button = new JButton("Show Clusters");
        button.addActionListener(this);
        controls.add(button);

        getContentPane().add(controls, BorderLayout.SOUTH);
        setSize(400, 400);
    }

    /**
     * Handles events from the spinbox.
     */
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == spin) {
            this.numClusters = ((Integer) spin.getValue()).intValue();
        }
    }

    /**
     * Recomputes the KMeans stuff.
     */
    private void computeClusters() {
        kmeans = new KMeans(dataset, numClusters);
        graph.set(kmeans.clusters());
        graph.repaint();
    }

    /**
     * Handles events from menu.
     */
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand() == "Quit") {
                System.exit(0);
            } else if (e.getActionCommand() == "Open DataFile") {
                String curdir = System.getProperty("user.dir");
                JFileChooser chooser = new JFileChooser(curdir);
                int returnVal = chooser.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String filename = chooser.getSelectedFile().getPath();
                    DataSet data = new DataSet(filename);
                    dataset = data;
                    numClusters = 1;
                    spin.setValue(new Integer(1));
                    computeClusters();
                }
            } else if (e.getActionCommand() == "Show Clusters") {
                if (dataset != null)
                    computeClusters();
            } else if (e.getActionCommand() == "Display Means") {
                JCheckBoxMenuItem menu = (JCheckBoxMenuItem) e.getSource();
                graph.setShowMeans(menu.isSelected());
            } else if (e.getActionCommand() == "Display Axis") {
                JCheckBoxMenuItem menu = (JCheckBoxMenuItem) e.getSource();
                graph.setShowAxis(menu.isSelected());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Regular main method.
     *
     * @param args Command line arguments are ignored.
     */
    public static void main(String[] args) {
        try {
            DataSetView view = new DataSetView();

            view.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
