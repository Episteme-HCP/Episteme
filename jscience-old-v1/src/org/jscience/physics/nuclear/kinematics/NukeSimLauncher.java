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

package org.jscience.physics.nuclear.kinematics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author Administrator
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NukeSimLauncher extends JFrame {

    private String[] noArgs = {""};

    private NukeSimLauncher() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        setupMenu();
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        ClassLoader loader = getClass().getClassLoader();
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        JPanel labelPanel = new JPanel(new GridLayout(0, 1));
        Icon tempIcon = new ImageIcon(loader.getResource("org.jscience.physics.nuclear.kinematics/det96.png"));
        JButton tempButton = new JButton(tempIcon);
        tempButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hide();
                org.jscience.physics.nuclear.kinematics.nuclear.PIDsimulation.main(noArgs);
            }
        });
        labelPanel.add(new JLabel("Det--Yale Enge Focal Plane PID", JLabel.RIGHT));
        ;
        buttonPanel.add(tempButton);
        //pane.add(buttonPanel);
        //buttonPanel=new JPanel(new BorderLayout());
        tempIcon = new ImageIcon(loader.getResource("org.jscience.physics.nuclear.kinematics/jrelkin96.png"));
        tempButton = new JButton(tempIcon);
        tempButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hide();
                org.jscience.physics.nuclear.kinematics.JRelKin.main(noArgs);
            }
        });
        labelPanel.add(new JLabel("JRelKin", JLabel.RIGHT));
        ;
        buttonPanel.add(tempButton);
        //pane.add(buttonPanel);
        //buttonPanel=new JPanel(new BorderLayout());
        tempIcon = new ImageIcon(loader.getResource("org.jscience.physics.nuclear.kinematics/plotter96.png"));
        tempButton = new JButton(tempIcon);
        tempButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hide();
                org.jscience.physics.nuclear.kinematics.nuclear.FocalPlanePlot.main(noArgs);
            }
        });
        labelPanel.add(new JLabel("Focal Plane Plotter", JLabel.RIGHT));
        ;
        buttonPanel.add(tempButton);
        //pane.add(buttonPanel);
        //buttonPanel=new JPanel(new BorderLayout());
        tempIcon = new ImageIcon(loader.getResource("org.jscience.physics.nuclear.kinematics/spanc96.png"));
        tempButton = new JButton(tempIcon);
        tempButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hide();
                org.jscience.physics.nuclear.kinematics.Spanc.main(noArgs);
            }
        });
        labelPanel.add(new JLabel("Spanc--SPlitpole ANalysis Code", JLabel.RIGHT));
        ;
        buttonPanel.add(tempButton);
        pane.add(buttonPanel, BorderLayout.CENTER);
        pane.add(labelPanel, BorderLayout.WEST);
        this.setTitle("NukeSim-classes Launcher");
        this.setResizable(false);
        pack();
        show();
    }

    private JDialog licenseD;

    private void createLicenseDialog() {
        licenseD =
                new JDialog(this,
                        "University of Illinois/NCSA Open Source License",
                        false);
        Container contents = licenseD.getContentPane();
        licenseD.setForeground(Color.black);
        licenseD.setBackground(Color.lightGray);
        licenseD.setResizable(false);
        licenseD.setLocation(20, 50);
        contents.setLayout(new BorderLayout());
        JPanel center = new JPanel(new GridLayout(0, 1));
        InputStream license_in =
                getClass().getClassLoader().getResourceAsStream("license.txt");
        Reader reader = new InputStreamReader(license_in);
        String text = "";
        int length = 0;
        char[] textarray = new char[2000];
        try {
            length = reader.read(textarray);
        } catch (IOException e) {
            System.err.println(e);
        }
        text = new String(textarray, 0, length);
        System.out.println(text);

        JTextArea textarea = new JTextArea(text);
        center.add(new JScrollPane(textarea));
        contents.add(center, BorderLayout.CENTER);
        JPanel south = new JPanel(new GridLayout(1, 0));
        contents.add(south, BorderLayout.SOUTH);
        JButton bok = new JButton("OK");
        bok.setActionCommand("l_ok");
        bok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                licenseD.dispose();
            }
        });
        south.add(bok);
        licenseD.pack();
        //Recieves events for closing the dialog box and closes it.
        licenseD.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                licenseD.dispose();
            }
        });
    }

    private void setupMenu() {
        createLicenseDialog();
        JMenuBar mbar = new JMenuBar();

        JMenu file = new JMenu("File");
        mbar.add(file);
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);
        exit.setActionCommand("exit");
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        JMenu help = new JMenu("Help");
        mbar.add(help);
        JMenuItem license = new JMenuItem("License...");
        help.add(license);
        license.setActionCommand("license");
        license.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                licenseD.show();
            }
        });
        setJMenuBar(mbar);
    }

    public static void main(String[] args) {
        //int arg=Integer.parseInt(args[0]);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println(e);
        }
        new NukeSimLauncher();
    }
}
