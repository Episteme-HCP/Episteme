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

package org.jscience.biology.lsystems.growing.gui;

import java.awt.*;

import javax.swing.*;


/**
 * This class holds all the information of the graphical elements of the
 * settins interface. No functionality is defined in this class.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class SettingsGUI extends JFrame {
    /** DOCUMENT ME! */
    public JLabel lblChoose;

    /** DOCUMENT ME! */
    public JTextField tfFileFolder;

    /** DOCUMENT ME! */
    public JButton btnBrowse;

    /** DOCUMENT ME! */
    public JButton btnAdd;

    /** DOCUMENT ME! */
    public JSeparator jseparator7;

    /** DOCUMENT ME! */
    public JLabel lblSelected;

    /** DOCUMENT ME! */
    public JList lstPlants;

    /** DOCUMENT ME! */
    public JScrollPane scrollpane;

    /** DOCUMENT ME! */
    public JLabel lblPosition;

    /** DOCUMENT ME! */
    public JSeparator jseparator8;

    /** DOCUMENT ME! */
    public JLabel lblFactor;

    /** DOCUMENT ME! */
    public JTextField tfTimeFactor;

    /** DOCUMENT ME! */
    public JLabel lblYears;

    /** DOCUMENT ME! */
    public JSeparator jseparator10;

    /** DOCUMENT ME! */
    public JButton btnGo;

    /** DOCUMENT ME! */
    public MapPanel pnlMap;

    /** DOCUMENT ME! */
    public JButton btnRemove;

    /** DOCUMENT ME! */
    public JSeparator jseparator0;

/**
     * Creates the graphical interface for the user.
     */
    public SettingsGUI() {
        // Setup GUI
        Container thisContent = this.getContentPane();
        this.setBounds(10, 30, 410, 750);
        this.setFont(new java.awt.Font("dialog", 0, 12));
        this.setTitle("Virtual Plants");
        this.setName("this");
        this.setResizable(false);

        lblChoose = new JLabel();
        lblChoose.setSize(new Dimension(170, 20));
        lblChoose.setPreferredSize(new Dimension(148, 17));
        lblChoose.setMaximumSize(new Dimension(148, 17));
        lblChoose.setMinimumSize(new Dimension(148, 17));
        lblChoose.setBounds(new Rectangle(17, 19, 170, 20));
        lblChoose.setText("Choose LSystem file");
        lblChoose.setLocation(new Point(17, 19));
        lblChoose.setName("lblChoose");
        lblChoose.setLayout(null);

        tfFileFolder = new JTextField();
        tfFileFolder.setLocation(new Point(17, 49));
        tfFileFolder.setSize(new Dimension(280, 20));
        tfFileFolder.setBounds(new Rectangle(17, 49, 280, 20));
        tfFileFolder.setName("tfFileFolder");
        tfFileFolder.setLayout(null);

        btnBrowse = new JButton();
        btnBrowse.setLocation(new Point(307, 49));
        btnBrowse.setSize(new Dimension(80, 20));
        btnBrowse.setPreferredSize(new Dimension(77, 27));
        btnBrowse.setMaximumSize(new Dimension(77, 27));
        btnBrowse.setBounds(new Rectangle(307, 49, 80, 20));
        btnBrowse.setText("Browse");
        btnBrowse.setName("btnBrowse");
        btnBrowse.setActionCommand("B");
        btnBrowse.setMinimumSize(new Dimension(77, 27));

        btnAdd = new JButton();
        btnAdd.setLocation(new Point(17, 89));
        btnAdd.setSize(new Dimension(80, 20));
        btnAdd.setPreferredSize(new Dimension(55, 27));
        btnAdd.setMaximumSize(new Dimension(55, 27));
        btnAdd.setBounds(new Rectangle(17, 89, 80, 20));
        btnAdd.setText("Add");
        btnAdd.setName("btnAdd");
        btnAdd.setActionCommand("A");
        btnAdd.setMinimumSize(new Dimension(55, 27));

        jseparator7 = new JSeparator();
        jseparator7.setSize(new Dimension(370, 10));
        jseparator7.setMinimumSize(new Dimension(1, 1));
        jseparator7.setBounds(new Rectangle(17, 129, 370, 10));
        jseparator7.setLocation(new Point(17, 129));
        jseparator7.setName("jseparator7");
        jseparator7.setFont(new java.awt.Font("dialog", 0, 12));
        jseparator7.setLayout(null);

        lblSelected = new JLabel();
        lblSelected.setSize(new Dimension(370, 20));
        lblSelected.setPreferredSize(new Dimension(85, 17));
        lblSelected.setMaximumSize(new Dimension(85, 17));
        lblSelected.setMinimumSize(new Dimension(85, 17));
        lblSelected.setBounds(new Rectangle(17, 139, 370, 20));
        lblSelected.setText("Selected plants");
        lblSelected.setLocation(new Point(17, 139));
        lblSelected.setName("lblSelected");
        lblSelected.setLayout(null);

        lstPlants = new JList();
        lstPlants.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollpane = new JScrollPane(lstPlants);
        scrollpane.setSize(new Dimension(370, 100));
        scrollpane.setBounds(new Rectangle(17, 169, 370, 100));
        scrollpane.setLocation(new Point(17, 169));
        scrollpane.setName("spScroll");

        lblPosition = new JLabel();
        lblPosition.setSize(new Dimension(370, 20));
        lblPosition.setPreferredSize(new Dimension(165, 17));
        lblPosition.setMaximumSize(new Dimension(165, 17));
        lblPosition.setMinimumSize(new Dimension(165, 17));
        lblPosition.setBounds(new Rectangle(19, 339, 370, 20));
        lblPosition.setText("Position of the selected plants");
        lblPosition.setLocation(new Point(19, 339));
        lblPosition.setName("lblPosition");
        lblPosition.setLayout(null);

        jseparator8 = new JSeparator();
        jseparator8.setSize(new Dimension(370, 10));
        jseparator8.setMinimumSize(new Dimension(1, 1));
        jseparator8.setBounds(new Rectangle(17, 619, 370, 10));
        jseparator8.setLocation(new Point(17, 619));
        jseparator8.setName("jseparator8");
        jseparator8.setFont(new java.awt.Font("dialog", 0, 12));
        jseparator8.setLayout(null);

        lblFactor = new JLabel();
        lblFactor.setSize(new Dimension(80, 20));
        lblFactor.setPreferredSize(new Dimension(61, 17));
        lblFactor.setMaximumSize(new Dimension(61, 17));
        lblFactor.setMinimumSize(new Dimension(61, 17));
        lblFactor.setBounds(new Rectangle(19, 629, 80, 20));
        lblFactor.setText("Time factor");
        lblFactor.setLocation(new Point(19, 629));
        lblFactor.setName("lblFactor");
        lblFactor.setLayout(null);

        tfTimeFactor = new JTextField();
        tfTimeFactor.setLocation(new Point(99, 629));
        tfTimeFactor.setSize(new Dimension(70, 20));
        tfTimeFactor.setBounds(new Rectangle(99, 629, 70, 20));
        tfTimeFactor.setName("tfTimeFactor");
        tfTimeFactor.setLayout(null);
        tfTimeFactor.setText("1.0");

        lblYears = new JLabel();
        lblYears.setSize(new Dimension(170, 20));
        lblYears.setPreferredSize(new Dimension(77, 17));
        lblYears.setMaximumSize(new Dimension(77, 17));
        lblYears.setMinimumSize(new Dimension(77, 17));
        lblYears.setBounds(new Rectangle(189, 629, 170, 20));
        lblYears.setText("years / minute (frames / sec)");
        lblYears.setLocation(new Point(189, 629));
        lblYears.setName("lblYears");
        lblYears.setLayout(null);

        jseparator10 = new JSeparator();
        jseparator10.setSize(new Dimension(370, 20));
        jseparator10.setMinimumSize(new Dimension(1, 1));
        jseparator10.setBounds(new Rectangle(17, 659, 370, 20));
        jseparator10.setLocation(new Point(17, 659));
        jseparator10.setName("jseparator10");
        jseparator10.setFont(new java.awt.Font("dialog", 0, 12));
        jseparator10.setLayout(null);

        btnGo = new JButton();
        btnGo.setLocation(new Point(19, 679));
        btnGo.setSize(new Dimension(370, 30));
        btnGo.setPreferredSize(new Dimension(127, 27));
        btnGo.setMaximumSize(new Dimension(127, 27));
        btnGo.setBounds(new Rectangle(19, 679, 370, 30));
        btnGo.setText("This scene in 3D");
        btnGo.setName("btnGo");
        btnGo.setActionCommand("T");
        btnGo.setMinimumSize(new Dimension(127, 27));

        pnlMap = new MapPanel();
        pnlMap.setSize(new Dimension(370, 230));
        pnlMap.setBackground(new Color(0, 0, 0));
        pnlMap.setBounds(new Rectangle(17, 369, 370, 230));
        pnlMap.setLocation(new Point(17, 369));
        pnlMap.setName("pnlMap");

        btnRemove = new JButton();
        btnRemove.setLocation(new Point(17, 289));
        btnRemove.setSize(new Dimension(80, 20));
        btnRemove.setPreferredSize(new Dimension(81, 27));
        btnRemove.setMaximumSize(new Dimension(81, 27));
        btnRemove.setBounds(new Rectangle(17, 289, 80, 20));
        btnRemove.setText("Remove");
        btnRemove.setName("btnRemove");
        btnRemove.setActionCommand("R");
        btnRemove.setMinimumSize(new Dimension(81, 27));

        jseparator0 = new JSeparator();
        jseparator0.setSize(new Dimension(370, 20));
        jseparator0.setMinimumSize(new Dimension(1, 1));
        jseparator0.setBounds(new Rectangle(17, 329, 370, 20));
        jseparator0.setLocation(new Point(17, 329));
        jseparator0.setName("jseparator0");
        jseparator0.setFont(new java.awt.Font("dialog", 0, 12));
        jseparator0.setLayout(null);

        thisContent.setLayout(null);
        thisContent.add(lblChoose);
        thisContent.add(tfFileFolder);
        thisContent.add(btnBrowse);
        thisContent.add(btnAdd);
        thisContent.add(jseparator7);
        thisContent.add(lblSelected);
        thisContent.add(scrollpane);
        thisContent.add(lblPosition);
        thisContent.add(jseparator8);
        thisContent.add(lblFactor);
        thisContent.add(tfTimeFactor);
        thisContent.add(lblYears);
        thisContent.add(jseparator10);
        thisContent.add(btnGo);
        thisContent.add(pnlMap);
        thisContent.add(btnRemove);
        thisContent.add(jseparator0);
    }

    /**
     * Gets the list of the plants.
     *
     * @return The list item.
     */
    public JList getList() {
        return lstPlants;
    }
}
