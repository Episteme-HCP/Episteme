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

package org.jscience.astronomy.solarsystem.artificialsatellites.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.*;
import javax.swing.border.EtchedBorder;


/**
 * 
 */
public class SwingMain {
/**
     * Deny the ability to construct an instance of this class.
     */
    private SwingMain() {
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param args Array of command line arguments
     */
    public static void main(String[] args) {
        try {
            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle(
                "org.jscience.astronomy.solarsystem.artificialsatellites");
            frame.setSize(1000, 700);

            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");
            menuBar.add(fileMenu);

            JMenuItem exitMenuItem = new JMenuItem("Exit");
            exitMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.setVisible(false);
                        frame.dispose();
                        System.exit(0);
                    }
                });
            fileMenu.add(exitMenuItem);
            frame.setJMenuBar(menuBar);

            JPanel contentPane = (JPanel) frame.getContentPane();
            contentPane.setLayout(new BorderLayout());

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BorderLayout());

            JPanel timeInputPanel = new JPanel();
            GridBagLayout gridbag = new GridBagLayout();
            timeInputPanel.setLayout(gridbag);
            timeInputPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Time Input"),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));

            String propagatorFieldString = "Propagator";
            String startFieldString = "Start Time (SSE)";
            String endFieldString = "End Time (SSE)";
            String deltaFieldString = "Time Step (sec)";

            JComboBox propagatorField = new JComboBox(new String[] {
                        "SGP", "SGP4", "SGP8", "SDP4", "SDP8"
                    });
            propagatorField.setSelectedIndex(1);
            propagatorField.setActionCommand(propagatorFieldString);

            JTextField startField = new JTextField(10);
            startField.setText("0.0");
            startField.setActionCommand(startFieldString);

            JTextField endField = new JTextField(10);
            endField.setText("1440.0");
            endField.setActionCommand(endFieldString);

            JTextField deltaField = new JTextField(10);
            deltaField.setText("360.0");
            deltaField.setActionCommand(deltaFieldString);

            JLabel propagatorFieldLabel = new JLabel(propagatorFieldString +
                    ": ");
            propagatorFieldLabel.setLabelFor(propagatorField);

            JLabel startFieldLabel = new JLabel(startFieldString + ": ");
            startFieldLabel.setLabelFor(startField);

            JLabel endFieldLabel = new JLabel(endFieldString + ": ");
            endFieldLabel.setLabelFor(endField);

            JLabel deltaFieldLabel = new JLabel(deltaFieldString + ": ");
            deltaFieldLabel.setLabelFor(deltaField);

            JLabel[] labels = {
                    propagatorFieldLabel, startFieldLabel, endFieldLabel,
                    deltaFieldLabel
                };
            JComponent[] textFields = {
                    propagatorField, startField, endField, deltaField
                };
            addLabelTextRows(labels, textFields, gridbag, timeInputPanel);

            inputPanel.add(timeInputPanel, BorderLayout.WEST);

            JTextArea tleInputArea = new JTextArea(
                    "1 88888U          80275.98708465  .00073094  13844-3  66816-4 0    8            \n" +
                    "2 88888  72.8435 115.9689 0086731  52.6988 110.5714 16.05824518  105            ");
            tleInputArea.setFont(new Font("Monospace", Font.PLAIN, 14));
            tleInputArea.setLineWrap(false);
            tleInputArea.setWrapStyleWord(true);

            JScrollPane tleScrollPane = new JScrollPane(tleInputArea);
            tleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            tleScrollPane.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(
                                EtchedBorder.LOWERED), "TLE Input"),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                    tleScrollPane.getBorder()));

            inputPanel.add(tleScrollPane, BorderLayout.CENTER);
            contentPane.add(inputPanel, BorderLayout.NORTH);

            JEditorPane outputArea = new JEditorPane();
            outputArea.setContentType("text/html");
            outputArea.setFont(new Font("Monospace", Font.PLAIN, 14));

            JScrollPane outputScrollPane = new JScrollPane(outputArea);
            outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            outputScrollPane.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(
                                EtchedBorder.LOWERED), "Output"),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                    outputScrollPane.getBorder()));

            contentPane.add(outputScrollPane, BorderLayout.CENTER);

            frame.setVisible(true);

            outputArea.setText("<html>" + "<body>" + "<ul>" + "<li>First Item" +
                "<li>Second Item" + "<li>Third Item" + "<li>Forth Item" +
                "<li>Fifth Item" + "</ul>" + "</body>" + "</html>");
        } catch (Throwable t) {
            StringWriter stackTrace = new StringWriter();
            t.printStackTrace(new PrintWriter(stackTrace));
            System.out.println(stackTrace.toString());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param labels DOCUMENT ME!
     * @param textFields DOCUMENT ME!
     * @param gridbag DOCUMENT ME!
     * @param container DOCUMENT ME!
     */
    private static void addLabelTextRows(JLabel[] labels,
        JComponent[] textFields, GridBagLayout gridbag, Container container) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;

        int numLabels = labels.length;

        for (int i = 0; i < numLabels; i++) {
            c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c.fill = GridBagConstraints.NONE; //reset to default
            c.weightx = 0.0; //reset to default
            container.add(labels[i], c);

            c.gridwidth = GridBagConstraints.REMAINDER; //end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            container.add(textFields[i], c);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param exitStatus DOCUMENT ME!
     */
    private static void shutdown(int exitStatus) {
        System.exit(exitStatus);
    }
}
