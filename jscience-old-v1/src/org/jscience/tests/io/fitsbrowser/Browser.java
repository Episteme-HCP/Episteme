package org.jscience.tests.io.fitsbrowser;

import org.jscience.io.fits.FitsFile;

import java.awt.*;
import java.awt.event.*;

import java.io.IOException;

import javax.swing.*;


/**
 * Standalone application for displaying and editing FITS files. This
 * provides the browsing functionality of the applet, plus it allows the user
 * to edit and save FITS files.
 */
public class Browser extends JFrame implements ActionListener {
    /** DOCUMENT ME! */
    JMenuBar menu_bar;

    /** DOCUMENT ME! */
    JFileChooser file_chooser;

    /** DOCUMENT ME! */
    FITSFileDisplay display;

    /** DOCUMENT ME! */
    FitsFile fits;

/**
     * Create a new browser with no file displayed.
     */
    public Browser() {
/**
         * window listener
         */
        addWindowListener(new Closer());
/**
         * menu bar
         */
        menu_bar = new JMenuBar();
        setJMenuBar(menu_bar);

        JMenu menu = new JMenu("File");
        menu_bar.add(menu);

        JMenuItem menuItem = new JMenuItem("Open", KeyEvent.VK_T);
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Save", KeyEvent.VK_T);
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Exit", KeyEvent.VK_T);
        menuItem.addActionListener(this);
        menu.add(menuItem);
/**
         * tabbed plane to hold HDUs
         */
        display = new FITSFileDisplay();
        getContentPane().add(display);
        display.setPreferredSize(new Dimension(608, 337));
/**
         * put panes in frame and make it go.
         */
        pack();
        setVisible(true);
    } // end of constructor

    /**
     * Respond to actions from the menu bar
     *
     * @param e An event from the menu bar.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Open")) {
            try {
                openFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (e.getActionCommand().equals("Save")) {
            try {
                save();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (e.getActionCommand().equals("Exit")) {
            exit();
        }
    } // end of actionPerformed

    /**
     * Causes the browser to quit some day it might check for whether
     * you saved yet, but right not it just quits
     */
    private void exit() {
        System.exit(0);
    }

    /**
     * Opens a file chosen interactively by the user
     *
     * @throws IOException DOCUMENT ME!
     */
    public void openFile() throws IOException {
/**
         * create the file chooser if it hasn't been already
         */
        if (file_chooser == null) {
            file_chooser = new JFileChooser(System.getProperty("user.dir"));
        }

/**
         * get a file from the file choser
         */
        if (file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
/**
             * do it
             */
            display.load(file_chooser.getSelectedFile());
        }
    } // end of openFile method

    /**
     * Writes the current file to a file chosen by the user.
     *
     * @throws IOException DOCUMENT ME!
     */
    public void save() throws IOException {
/**
         * create the file chooser if it hasn't been already
         */
        if (file_chooser == null) {
            file_chooser = new JFileChooser(System.getProperty("user.dir"));
        }

/**
         * get a file from the file choser
         */
        if (file_chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
/**
             * do it
             */
            display.save(file_chooser.getSelectedFile());
        }
    } // end of save method

    /**
     * Main method for starting the browser. Takes the name of a FITS
     * file (possibly a URL) on the command line.
     *
     * @param args DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void main(String[] args) throws IOException {
        Browser browser = new Browser();

        if (args.length > 0) {
            browser.display.load(args[0]);
        } else {
            browser.display.load(FitsFile.createEmpty());
        }
    } // end of main method

    /**
     * Inner class for handing window closing events. Used to make the
     * browser exit when you close its window.
     */
    private class Closer extends WindowAdapter {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void windowClosing(WindowEvent e) {
            exit();
        }
    } // end of Closer inner class
} // end of Browser class
