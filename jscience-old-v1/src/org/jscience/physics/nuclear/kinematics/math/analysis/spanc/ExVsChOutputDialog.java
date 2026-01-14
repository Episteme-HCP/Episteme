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

package org.jscience.physics.nuclear.kinematics.math.analysis.spanc;

import org.jscience.physics.nuclear.kinematics.math.MathException;
import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;
import org.jscience.physics.nuclear.kinematics.math.statistics.StatisticsException;
import org.jscience.physics.nuclear.kinematics.nuclear.KinematicsException;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * @author org.jscience.physics.nuclear.kinematics
 *         <p/>
 *         To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class ExVsChOutputDialog
        extends JDialog
        implements ActionListener, ChangeListener {

    JTextField tFileName = new JTextField(30);
    JTextField tMin = new JTextField("0    ");
    JTextField tMax = new JTextField("4095 ");
    JTextField tEx = new JTextField("0    ");
    JButton ok = new JButton("OK");
    JButton apply = new JButton("Apply");
    CalibrationFit fit;

    public ExVsChOutputDialog(CalibrationFit cf) {
        fit = cf;
        Container contents = getContentPane();
        contents.setLayout(new BorderLayout());
        setResizable(false);

        //south "act on it" panel
        JPanel south = new JPanel(new GridLayout(1, 3));
        ok.addActionListener(this);
        ok.setEnabled(false);
        south.add(ok);
        apply.addActionListener(this);
        apply.setEnabled(false);
        south.add(apply);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        south.add(cancel);
        contents.add(south, BorderLayout.SOUTH);

        //north file panel
        JPanel north = new JPanel(new FlowLayout());
        north.add(new JLabel("Output File"));
        north.add(tFileName);
        JButton browse = new JButton("Browse");
        browse.addActionListener(this);
        north.add(browse);
        contents.add(north, BorderLayout.NORTH);

        //reaction selector
        JPanel center = new JPanel(new GridLayout(2, 1));
        JPanel selector = new JPanel(new FlowLayout());
        setupReactionSlider();
        selector.add(_reaction);
        selector.add(new JLabel("Reaction"));
        center.add(selector);

        //ch range
        JPanel range = new JPanel(new GridLayout(1, 6));
        range.add(new JLabel("Low Channel"));
        range.add(tMin);
        range.add(new JLabel("High Channel"));
        range.add(tMax);
        range.add(new JLabel("Projectile Ex"));
        range.add(tEx);
        center.add(range);

        contents.add(center, BorderLayout.CENTER);
        pack();
        show();
    }

    private JSlider _reaction =
            new JSlider(0,
                    SpancReaction.getAllReactions().length - 1,
                    JSlider.HORIZONTAL);

    private void setupReactionSlider() {
        _reaction.setMinorTickSpacing(1);
        _reaction.setMajorTickSpacing(1);
        _reaction.setPaintTicks(true);
        _reaction.setPaintLabels(true);
        _reaction.setSnapToTicks(true);
        _reaction.addChangeListener(this);
        _reaction.setValue(0);
    }

    SpancReaction reaction;

    public void stateChanged(ChangeEvent change) {
        Object source = change.getSource();
        if (source == _reaction) {
            reaction =
                    (SpancReaction) SpancReaction.getReaction(_reaction.getModel().getValue());
            ok.setEnabled(true);
            apply.setEnabled(true);
        }
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        String text = e.getActionCommand();
        if (text.equals("Browse")) {
            browseForDir();
        }
        if (text.equals("OK") || text.equals("Apply")) {
            try {
                outputFile();
            } catch (Exception except) {
                JOptionPane.showConfirmDialog(this,
                        except.getMessage(),
                        "Error writing to file.",
                        JOptionPane.WARNING_MESSAGE);
            }

        }
        if (text.equals("Cancel") || text.equals("OK")) {
            this.dispose();
        }
    }

    /**
     * add all files in a directory to sort
     */
    private void browseForDir() {
        JFileChooser fd =
                new JFileChooser(new File(tFileName.getText().trim()));
        fd.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int option = fd.showOpenDialog(this);
        //save current values
        if (option == JFileChooser.APPROVE_OPTION
                && fd.getSelectedFile() != null) {
            tFileName.setText(fd.getSelectedFile().getPath());
        }
    }

    private void outputFile()
            throws
            KinematicsException,
            MathException,
            StatisticsException,
            FileNotFoundException,
            NuclearException {
        PrintWriter pw = null;
        OutputPeak calc = null;
        File file = new File(tFileName.getText().trim());
        String tableHead = "Channel\tEx\n";
        pw = new PrintWriter(new FileOutputStream(file));
        pw.print(tableHead);
        int min = Integer.parseInt(tMin.getText().trim());
        int max = Integer.parseInt(tMax.getText().trim());
        double ExProj = Double.parseDouble(tEx.getText().trim());
        calc =
                new OutputPeak(reaction,
                        ExProj,
                        new UncertainNumber((min + max) / 2),
                        fit);
        for (int i = min; i <= max; i++) {
            calc.setValues(reaction, ExProj, new UncertainNumber(i));
            double Ex = calc.getExResidual(false).value;
            pw.print(i + "\t" + Ex + "\n");
        }
        pw.close();
    }

}
