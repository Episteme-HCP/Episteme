/*
---------------------------------------------------------------------------
VIRTUAL PLANTS
==============

This Diploma work is a computer graphics project made at the University
of applied sciences in Biel, Switzerland. http://www.hta-bi.bfh.ch
The taks is to simulate the growth of 3 dimensional plants and show
them in a virtual world.
This work is based on the ideas of Lindenmayer and Prusinkiewicz which
are taken from the book 'The algorithmic beauty of plants'.
The Java and Java3D classes have to be used for this work. This file
is one class of the program. For more information look at the VirtualPlants
homepage at: http://www.hta-bi.bfh.ch/Projects/VirtualPlants

Hosted by Claude Schwab

Developed by Rene Gressly
http://www.gressly.ch/rene

25.Oct.1999 - 17.Dec.1999

Copyright by the University of applied sciences Biel, Switzerland
----------------------------------------------------------------------------
*/
package org.jscience.biology.lsystems.growing.gui;

import com.sun.j3d.utils.applet.MainFrame;

import org.jscience.biology.lsystems.common.Log;
import org.jscience.biology.lsystems.growing.Converter;
import org.jscience.biology.lsystems.growing.Plant;
import org.jscience.biology.lsystems.growing.Scene;
import org.jscience.biology.lsystems.growing.SuffixFileFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.File;

import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * Ths class holds all the functionality of the graphical user interface.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class Settings extends SettingsGUI implements ActionListener,
    WindowListener {
    /** File chooser object to display file chooser dialog. */
    private JFileChooser m_chooser;

    /** List of selected plants for this scene */
    private Vector m_vPlants;

/**
     * Constructor. Adds all listener for the input fields and buttons.
     */
    public Settings() {
        m_vPlants = new Vector();

        //add all needed action listener
        tfFileFolder.addActionListener(this);
        tfTimeFactor.addActionListener(this);
        btnBrowse.addActionListener(this);
        btnAdd.addActionListener(this);
        btnRemove.addActionListener(this);
        btnGo.addActionListener(this);
        lstPlants.addListSelectionListener(new PlantListSelectionListener());
        addWindowListener(this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); //manually destroy the GUI on close
        pnlMap.setSettings(this); //pass the MapPanel instance the reference to this object
    }

    /**
     * Override this method to handle all action events made by the
     * user.
     *
     * @param evt Passed by the caller (event handler).
     */
    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();

        if (src == btnBrowse) //the browse button has been pressed
         {
            //make file choose dialog
            m_chooser = new JFileChooser("../lsy");

            SuffixFileFilter ffLsy = new SuffixFileFilter("lsy", "LSystem"); //show only *.lsy files
            m_chooser.setFileFilter(ffLsy);

            int returnVal = m_chooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                tfFileFolder.setText(m_chooser.getSelectedFile().getPath());
            }
        }

        if (src == btnAdd) //the add button has been pressed
         {
            try {
                File file = new File(tfFileFolder.getText());
                Plant plant = new Plant(file, 100, 100);
                plant.setSelected();
                m_vPlants.add(plant);
                pnlMap.repaint();
                lstPlants.setListData(m_vPlants);
            } catch (Exception e) {
                Log.log(
                    "Exception catched in Settings:actionPerformed():btnAdd");
                Log.debug(e.toString());
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        if (src == btnRemove) //the remove button has been pressed
         {
            Log.debug("Remove pressed");

            Object obj;

            if ((obj = lstPlants.getSelectedValue()) != null) {
                m_vPlants.remove(obj);
                lstPlants.setListData(m_vPlants);
                pnlMap.repaint();
            }
        }

        if (src == btnGo) //the go button has been pressed
         {
            Log.debug("Go pressed");

            String strTimeFactor = tfTimeFactor.getText();

            float fTimeFactor = 0.0f;

            if (strTimeFactor.length() > 0) {
                fTimeFactor = Converter.readFloat(strTimeFactor);
            }

            if ((fTimeFactor <= 0.0f) || (fTimeFactor > 1000f)) {
                JOptionPane.showMessageDialog(null,
                    "Time factor must be a valid number greather than zero and less than 1000.",
                    "Error", JOptionPane.ERROR_MESSAGE);

                return;
            }

            dispose();

            try {
                MainFrame mf = new MainFrame(new Scene(m_vPlants, fTimeFactor),
                        1000, 900);
            } catch (Exception e) {
                Log.log("Exeption catched in Settings:actionPerformed:btnGo");
                Log.log(e.toString());
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    /**
     * Override method from Listener interface
     *
     * @param e DOCUMENT ME!
     */
    public void windowActivated(WindowEvent e) {
        Log.debug("Window activated");
    }

    /**
     * Override method from Listener interface
     *
     * @param e DOCUMENT ME!
     */
    public void windowClosed(WindowEvent e) {
        Log.debug("Window closed");
    }

    /**
     * Override method from Listener interface. Close window and exit
     * program if close button is pressed.
     *
     * @param e DOCUMENT ME!
     */
    public void windowClosing(WindowEvent e) {
        Log.debug("Window closing");
        System.exit(0);
    }

    /**
     * Override method from Listener interface
     *
     * @param e DOCUMENT ME!
     */
    public void windowDeactivated(WindowEvent e) {
        Log.debug("Window deactivated");
    }

    /**
     * Override method from Listener interface
     *
     * @param e DOCUMENT ME!
     */
    public void windowDeiconified(WindowEvent e) {
        Log.debug("Window deiconified");
    }

    /**
     * Override method from Listener interface
     *
     * @param e DOCUMENT ME!
     */
    public void windowIconified(WindowEvent e) {
        Log.debug("Window iconified");
    }

    /**
     * Override method from Listener interface
     *
     * @param e DOCUMENT ME!
     */
    public void windowOpened(WindowEvent e) {
        Log.debug("Window opened");
    }

    /**
     * Retrieves the list of plants selected for the scene.
     *
     * @return The list of plants.
     */
    public Vector getPlants() {
        return m_vPlants;
    }

    /**
     * Class to handle the list selection of the plant list.
     */
    class PlantListSelectionListener implements ListSelectionListener {
        /**
         * Overrride method from interface ListSelectionListener to
         * handle events.
         *
         * @param evt Passed by the caller (event handler).
         */
        public void valueChanged(ListSelectionEvent evt) {
            if (!evt.getValueIsAdjusting()) {
                //get the selected list item
                JList list = (JList) evt.getSource();
                Plant selected = (Plant) list.getSelectedValue();

                if (selected != null) { //an item is selected
                    Log.debug("Selected: " + selected.toString());
                    selected.setSelected(); //set plant as selected
                }

                pnlMap.repaint(); //repaint map to make dot of selected plant red
            }
        }
    }
}
