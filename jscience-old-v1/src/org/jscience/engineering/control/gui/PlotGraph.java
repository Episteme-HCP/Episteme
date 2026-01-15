/*
*   Class PlotGraph
*
*   A class that creates a window and displays within that window
*   a graph of one or more x-y data sets
*
*   This class extends Plot (also from Michael Thomas Flanagan's Library)
*
*   For use if you are incorporating a plot into your own Java program
*   See Plotter for a free standing graph plotting application
*
*   WRITTEN BY: Dr Michael Thomas Flanagan
*
*   DATE:         February 2002
*   UPDATED:  22 April 2004 and 14 August 2004
*
*   DOCUMENTATION:
*   See Michael Thomas Flanagan's Java library on-line web page:
*   PlotGraph.html
*
*   Copyright (c) April 2004, Auguswt 2004
*
*   PERMISSION TO COPY:
*   Permission to use, copy and modify this software and its documentation for
*   NON-COMMERCIAL purposes is granted, without fee, provided that an acknowledgement
*   to the author, Michael Thomas Flanagan at www.ee.ucl.ac.uk/~mflanaga, appears in all copies.
*
*   Dr Michael Thomas Flanagan makes no representations about the suitability
*   or fitness of the software for any or for a particular purpose.
*   Michael Thomas Flanagan shall not be liable for any damages suffered
*   as a result of using, modifying or distributing this software or its derivatives.
*
***************************************************************************************/
package org.jscience.engineering.control.gui;

import java.awt.*;

// Include the windowing libraries
import javax.swing.*;


// Declare a class that creates a window capable of being drawn to
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class PlotGraph extends Plot {
    /**
     * DOCUMENT ME!
     */
    protected int graphWidth = 800; // width of the window for the graph in pixels

    /**
     * DOCUMENT ME!
     */
    protected int graphHeight = 600; // height of the window for the graph in pixels

    /**
     * DOCUMENT ME!
     */
    protected int closeChoice = 1; // =1 clicking on close icon causes window to close
                                   //    and the the program is exited.
                                   // =2 clicking on close icon causes window to close
                                   //    leaving the program running.
                                   // Create the window object

    /**
     * DOCUMENT ME!
     */
    protected JFrame window = new JFrame(
            "Michael T Flanagan's plotting program - PlotGraph");

    // Constructor
    /**
     * Creates a new PlotGraph object.
     *
     * @param data DOCUMENT ME!
     */
    public PlotGraph(double[][] data) {
        super(data);
    }

    // Constructor
    /**
     * Creates a new PlotGraph object.
     *
     * @param xData DOCUMENT ME!
     * @param yData DOCUMENT ME!
     */
    public PlotGraph(double[] xData, double[] yData) {
        super(xData, yData);
    }

    // Rescale the y dimension of the graph window and graph
    /**
     * DOCUMENT ME!
     *
     * @param yScaleFactor DOCUMENT ME!
     */
    public void rescaleY(double yScaleFactor) {
        this.graphHeight = (int) Math.round((double) graphHeight * yScaleFactor);
        super.yLen = (int) Math.round((double) super.yLen * yScaleFactor);
        super.yTop = (int) Math.round((double) super.yTop * yScaleFactor);
        super.yBot = super.yTop + super.yLen;
    }

    // Rescale the x dimension of the graph window and graph
    /**
     * DOCUMENT ME!
     *
     * @param xScaleFactor DOCUMENT ME!
     */
    public void rescaleX(double xScaleFactor) {
        this.graphWidth = (int) Math.round((double) graphWidth * xScaleFactor);
        super.xLen = (int) Math.round((double) super.xLen * xScaleFactor);
        super.xBot = (int) Math.round((double) super.xBot * xScaleFactor);
        super.xTop = super.xBot + super.xLen;
    }

    // Get pixel width of the PlotGraph window
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getGraphWidth() {
        return this.graphWidth;
    }

    // Get pixel height of the PlotGraph window
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getGraphHeight() {
        return this.graphHeight;
    }

    // Reset height of graph window (pixels)
    /**
     * DOCUMENT ME!
     *
     * @param graphHeight DOCUMENT ME!
     */
    public void setGraphHeight(int graphHeight) {
        this.graphHeight = graphHeight;
    }

    // Reset width of graph window (pixels)
    /**
     * DOCUMENT ME!
     *
     * @param graphWidth DOCUMENT ME!
     */
    public void setGraphWidth(int graphWidth) {
        this.graphWidth = graphWidth;
    }

    // Get close choice
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCloseChoice() {
        return this.closeChoice;
    }

    // Reset close choice
    /**
     * DOCUMENT ME!
     *
     * @param choice DOCUMENT ME!
     */
    public void setCloseChoice(int choice) {
        this.closeChoice = choice;
    }

    // The paint method to draw the graph.
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        // Rescale - needed for redrawing if graph window is resized by dragging
        double newGraphWidth = this.getSize().width;
        double newGraphHeight = this.getSize().height;
        double xScale = (double) newGraphWidth / (double) this.graphWidth;
        double yScale = (double) newGraphHeight / (double) this.graphHeight;
        rescaleX(xScale);
        rescaleY(yScale);

        // Call graphing method
        graph(g);
    }

    // Set up the window and show graph
    /**
     * DOCUMENT ME!
     */
    public void plot() {
        // Set the initial size of the graph window
        setSize(this.graphWidth, this.graphHeight);

        // Set background colour
        window.getContentPane().setBackground(Color.white);

        // Choose close box
        if (this.closeChoice == 1) {
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }

        // Add graph canvas
        window.getContentPane().add("Center", this);

        // Set the window up
        window.pack();
        window.setResizable(true);
        window.toFront();

        // Show the window
        window.setVisible(true);
    }

    // Displays dialogue box asking if you wish to exit program
    /**
     * DOCUMENT ME!
     */
    public void endProgram() {
        int ans = JOptionPane.showConfirmDialog(null,
                "Do you wish to end the program\n" +
                "This will also close the graph window or windows",
                "End Program", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (ans == 0) {
            System.exit(0);
        } else {
            String message = "Now you must press the appropriate escape key/s, e.g. Ctrl C, to exit this program\n";

            if (this.closeChoice == 1) {
                message += "or close a graph window";
            }

            JOptionPane.showMessageDialog(null, message);
        }
    }
}
