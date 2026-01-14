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

package org.jscience.tests.biology.alignment.gui;

import org.jscience.biology.alignment.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;


/**
 * This class is the internal frame of NeoBio's graphical interface for
 * computing pairwise sequence alignments using one of the the algorithms
 * provided in the {@link neobio.alignment} package.
 *
 * @author Sergio A. de Carvalho Jr.
 */
public class PairwiseAlignmentFrame extends JInternalFrame {
    /** DOCUMENT ME! */
    private static int window_number = 1;

    /** DOCUMENT ME! */
    private Frame parent_frame;

    /** DOCUMENT ME! */
    private JPanel input_panel;

    /** DOCUMENT ME! */
    private JPanel scoring_panel;

    /** DOCUMENT ME! */
    private JPanel algorithm_panel;

    /** DOCUMENT ME! */
    private JPanel output_panel;

    /** DOCUMENT ME! */
    private JPanel progress_tab_panel;

    /** DOCUMENT ME! */
    private JPanel output_tab_panel;

    /** DOCUMENT ME! */
    private JTextField seq1_field;

    /** DOCUMENT ME! */
    private JTextField seq2_field;

    /** DOCUMENT ME! */
    private JTextField matrix_field;

    /** DOCUMENT ME! */
    private JTextField output_field;

    /** DOCUMENT ME! */
    private JTextField match_field;

    /** DOCUMENT ME! */
    private JTextField mismatch_field;

    /** DOCUMENT ME! */
    private JTextField gap_field;

    /** DOCUMENT ME! */
    private JTextArea progress_area;

    /** DOCUMENT ME! */
    private JTextArea output_area;

    /** DOCUMENT ME! */
    private JButton find_seq1_button;

    /** DOCUMENT ME! */
    private JButton find_seq2_button;

    /** DOCUMENT ME! */
    private JButton find_output_button;

    /** DOCUMENT ME! */
    private JButton find_matrix_button;

    /** DOCUMENT ME! */
    private JButton run_button;

    /** DOCUMENT ME! */
    private JComboBox algorithm_combo;

    /** DOCUMENT ME! */
    private JTabbedPane output_tab;

    /** DOCUMENT ME! */
    private JRadioButton screen_button;

    /** DOCUMENT ME! */
    private JRadioButton file_button;

    /** DOCUMENT ME! */
    private JRadioButton basic_button;

    /** DOCUMENT ME! */
    private JRadioButton matrix_button;

    /** DOCUMENT ME! */
    private ButtonGroup scoring_group;

    /** DOCUMENT ME! */
    private ButtonGroup output_group;

    /** DOCUMENT ME! */
    private JLabel seq1_label;

    /** DOCUMENT ME! */
    private JLabel seq2_label;

    /** DOCUMENT ME! */
    private JLabel match_label;

    /** DOCUMENT ME! */
    private JLabel mismatch_label;

    /** DOCUMENT ME! */
    private JLabel gap_label;

    /** DOCUMENT ME! */
    private JFileChooser find_dialog;

    /** DOCUMENT ME! */
    private boolean output_to_file;

    /** DOCUMENT ME! */
    private boolean basic_scheme;

    /** DOCUMENT ME! */
    private String[] algorithm_name = {
            "Needleman & Wunsch (global alignment)",
            "Smith & Waterman (local alignment)",
            "Crochemore, Landau & Ziv-Ukelson for global alignment",
            "Crochemore, Landau & Ziv-Ukelson for local alignment"
        };

    /** DOCUMENT ME! */
    private PairwiseAlignmentAlgorithm[] algorithm = {
            new NeedlemanWunsch(), new SmithWaterman(),
            new CrochemoreLandauZivUkelsonGlobalAlignment(),
            new CrochemoreLandauZivUkelsonLocalAlignment()
        };

/**
     * Creates a new instance of the internal frame.
     *
     * @param parent_frame the parent frame
     */
    public PairwiseAlignmentFrame(Frame parent_frame) {
        this.parent_frame = parent_frame;
        initComponents();
    }

    /**
     * DOCUMENT ME!
     */
    private void initComponents() {
        JComponent pane;
        GridBagConstraints c;

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setClosable(true);
        setTitle("Pairwise Sequence Alignment " + window_number++);
        setMinimumSize(new Dimension(500, 500));

        pane = (JComponent) getContentPane();
        pane.setLayout(new GridBagLayout());

        c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0;

        // input panel
        input_panel = new JPanel();
        add(pane, input_panel, c, 0, 0);

        // scoring panel
        scoring_panel = new JPanel();
        add(pane, scoring_panel, c, 0, 1);

        // output panel
        output_panel = new JPanel();
        add(pane, output_panel, c, 0, 2);

        // algorithm panel
        algorithm_panel = new JPanel();
        add(pane, algorithm_panel, c, 0, 3);

        c.weightx = 1.0;
        c.weighty = 1.0;

        // output tab
        output_tab = new JTabbedPane();
        add(pane, output_tab, c, 0, 4);

        find_dialog = new JFileChooser();
        find_dialog.setDialogTitle("Find...");
        find_dialog.setDialogType(JFileChooser.OPEN_DIALOG);

        // ***************** INPUT PANEL *****************
        input_panel.setLayout(new GridBagLayout());
        input_panel.setBorder(BorderFactory.createTitledBorder(
                new EtchedBorder(EtchedBorder.LOWERED), "Input"));

        seq1_label = new JLabel("Sequence 1:");
        seq2_label = new JLabel("Sequence 2:");

        seq1_field = new JTextField();
        seq1_field.addCaretListener(new CaretListener() {
                public void caretUpdate(CaretEvent e) {
                    checkRunButtonStatus();
                }
            });

        seq2_field = new JTextField();
        seq2_field.addCaretListener(new CaretListener() {
                public void caretUpdate(CaretEvent e) {
                    checkRunButtonStatus();
                }
            });

        find_seq1_button = new JButton("Find...");
        find_seq1_button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    findSeq1ButtonActionPerformed();
                }
            });

        find_seq2_button = new JButton("Find...");
        find_seq2_button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    findSeq2ButtonActionPerformed();
                }
            });

        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.EAST;
        add(input_panel, seq1_label, c, 0, 0);
        add(input_panel, seq2_label, c, 0, 1);

        c.anchor = GridBagConstraints.CENTER;
        add(input_panel, find_seq1_button, c, 2, 0);
        add(input_panel, find_seq2_button, c, 2, 1);

        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(input_panel, seq1_field, c, 1, 0);
        add(input_panel, seq2_field, c, 1, 1);

        // ***************** SCORING SCHEME PANEL *****************
        scoring_panel.setLayout(new GridBagLayout());
        scoring_panel.setBorder(BorderFactory.createTitledBorder(
                new EtchedBorder(EtchedBorder.LOWERED), "Scoring Scheme"));

        basic_scheme = true;
        basic_button = new JRadioButton("Basic:");
        basic_button.setSelected(true);
        basic_button.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    schemeOptionStateChanged();
                }
            });

        matrix_button = new JRadioButton("Substitution Matrix:");
        matrix_button.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    schemeOptionStateChanged();
                }
            });

        match_label = new JLabel("Match:");
        mismatch_label = new JLabel("Mismatch:");
        gap_label = new JLabel("Gap:");

        match_field = new JTextField("1", 2);
        match_field.setHorizontalAlignment(JTextField.RIGHT);
        match_field.addCaretListener(new CaretListener() {
                public void caretUpdate(CaretEvent e) {
                    checkRunButtonStatus();
                }
            });

        mismatch_field = new JTextField("-1", 2);
        mismatch_field.setHorizontalAlignment(JTextField.RIGHT);
        mismatch_field.addCaretListener(new CaretListener() {
                public void caretUpdate(CaretEvent e) {
                    checkRunButtonStatus();
                }
            });

        gap_field = new JTextField("-1", 2);
        gap_field.setHorizontalAlignment(JTextField.RIGHT);
        gap_field.addCaretListener(new CaretListener() {
                public void caretUpdate(CaretEvent e) {
                    checkRunButtonStatus();
                }
            });

        matrix_field = new JTextField();
        matrix_field.setEnabled(false);
        matrix_field.addCaretListener(new CaretListener() {
                public void caretUpdate(CaretEvent e) {
                    checkRunButtonStatus();
                }
            });

        find_matrix_button = new JButton("Find...");
        find_matrix_button.setEnabled(false);
        find_matrix_button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    findMatrixButtonActionPerformed();
                }
            });

        scoring_group = new ButtonGroup();
        scoring_group.add(basic_button);
        scoring_group.add(matrix_button);

        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        add(scoring_panel, basic_button, c, 0, 0);

        c.anchor = GridBagConstraints.EAST;
        add(scoring_panel, match_label, c, 1, 0);
        add(scoring_panel, mismatch_label, c, 3, 0);
        add(scoring_panel, gap_label, c, 5, 0);

        c.anchor = GridBagConstraints.WEST;
        add(scoring_panel, matrix_button, c, 0, 1);

        c.anchor = GridBagConstraints.CENTER;
        add(scoring_panel, find_matrix_button, c, 7, 1);

        c.weightx = 1.0 / 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(scoring_panel, match_field, c, 2, 0);
        add(scoring_panel, mismatch_field, c, 4, 0);
        add(scoring_panel, gap_field, c, 6, 0);

        c.weightx = 1.0;
        c.gridwidth = 6;
        add(scoring_panel, matrix_field, c, 1, 1);
        c.gridwidth = 1;

        // ***************** OUTPUT PANEL *****************
        output_panel.setLayout(new GridBagLayout());
        output_panel.setBorder(BorderFactory.createTitledBorder(
                new EtchedBorder(EtchedBorder.LOWERED), "Output"));

        screen_button = new JRadioButton("Screen");
        screen_button.setSelected(true);

        output_to_file = false;
        file_button = new JRadioButton("File:");
        file_button.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    outputOptionStateChanged();
                }
            });

        output_field = new JTextField();
        output_field.setEnabled(false);
        output_field.addCaretListener(new CaretListener() {
                public void caretUpdate(CaretEvent e) {
                    checkRunButtonStatus();
                }
            });

        find_output_button = new JButton("Find...");
        find_output_button.setEnabled(false);
        find_output_button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    findOutputButtonActionPerformed();
                }
            });

        output_group = new ButtonGroup();
        output_group.add(screen_button);
        output_group.add(file_button);

        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        add(output_panel, screen_button, c, 0, 0);
        add(output_panel, file_button, c, 1, 0);
        add(output_panel, find_output_button, c, 3, 0);

        c.weightx = 1.0;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(output_panel, output_field, c, 2, 0);

        // ***************** ALGORITHM PANEL *****************
        algorithm_panel.setLayout(new GridBagLayout());
        algorithm_panel.setBorder(BorderFactory.createTitledBorder(
                new EtchedBorder(EtchedBorder.LOWERED), "Alignment Algorithm"));

        algorithm_combo = new JComboBox(algorithm_name);

        run_button = new JButton("Run");
        run_button.setEnabled(false);
        run_button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    runButtonActionPerformed();
                }
            });

        c.weightx = 1.0;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(algorithm_panel, algorithm_combo, c, 0, 0);

        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        add(algorithm_panel, run_button, c, 1, 0);

        // ***************** OUTPUT TAB *****************
        progress_area = new JTextArea();
        progress_area.setEditable(false);
        progress_area.setBorder(BorderFactory.createBevelBorder(
                BevelBorder.LOWERED));
        progress_tab_panel = new JPanel();
        progress_tab_panel.setLayout(new GridLayout());
        progress_tab_panel.add(new JScrollPane(progress_area));
        output_tab.addTab("Progress", progress_tab_panel);

        output_area = new JTextArea();
        output_area.setEditable(false);
        output_area.setBorder(BorderFactory.createBevelBorder(
                BevelBorder.LOWERED));
        output_area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        output_tab_panel = new JPanel();
        output_tab_panel.setLayout(new GridLayout());
        output_tab_panel.add(new JScrollPane(output_area));
        output_tab.addTab("Output", output_tab_panel);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    private void add(JComponent a, JComponent b, GridBagConstraints c, int x,
        int y) {
        c.gridx = x;
        c.gridy = y;
        a.add(b, c);
    }

    /**
     * DOCUMENT ME!
     */
    private void findSeq1ButtonActionPerformed() {
        int c = find_dialog.showOpenDialog(this);

        if (c != JFileChooser.APPROVE_OPTION) {
            return;
        }

        seq1_field.setText(find_dialog.getSelectedFile().getPath());
    }

    /**
     * DOCUMENT ME!
     */
    private void findSeq2ButtonActionPerformed() {
        int c = find_dialog.showOpenDialog(this);

        if (c != JFileChooser.APPROVE_OPTION) {
            return;
        }

        seq2_field.setText(find_dialog.getSelectedFile().getPath());
    }

    /**
     * DOCUMENT ME!
     */
    private void findMatrixButtonActionPerformed() {
        int c = find_dialog.showOpenDialog(this);

        if (c != JFileChooser.APPROVE_OPTION) {
            return;
        }

        matrix_field.setText(find_dialog.getSelectedFile().getPath());
    }

    /**
     * DOCUMENT ME!
     */
    private void findOutputButtonActionPerformed() {
        int c = find_dialog.showOpenDialog(this);

        if (c != JFileChooser.APPROVE_OPTION) {
            return;
        }

        output_field.setText(find_dialog.getSelectedFile().getPath());
    }

    /**
     * DOCUMENT ME!
     */
    private void schemeOptionStateChanged() {
        basic_scheme = basic_button.isSelected();

        match_label.setEnabled(basic_scheme);
        match_field.setEnabled(basic_scheme);
        mismatch_label.setEnabled(basic_scheme);
        mismatch_field.setEnabled(basic_scheme);
        gap_label.setEnabled(basic_scheme);
        gap_field.setEnabled(basic_scheme);

        matrix_field.setEnabled(!basic_scheme);
        find_matrix_button.setEnabled(!basic_scheme);

        checkRunButtonStatus();
    }

    /**
     * DOCUMENT ME!
     */
    private void outputOptionStateChanged() {
        output_to_file = file_button.isSelected();

        output_field.setEnabled(output_to_file);
        find_output_button.setEnabled(output_to_file);

        checkRunButtonStatus();
    }

    /**
     * DOCUMENT ME!
     */
    private void checkRunButtonStatus() {
        boolean run = true;

        if ((seq1_field.getText().length() == 0) ||
                (seq2_field.getText().length() == 0)) {
            run = false;
        } else {
            if (file_button.isSelected() &&
                    (output_field.getText().length() == 0)) {
                run = false;
            } else {
                if (matrix_button.isSelected()) {
                    if (matrix_field.getText().length() == 0) {
                        run = false;
                    }
                } else {
                    if ((match_field.getText().length() == 0) ||
                            (mismatch_field.getText().length() == 0) ||
                            (gap_field.getText().length() == 0)) {
                        run = false;
                    }
                }
            }
        }

        if ((run_button.isEnabled() && !run) ||
                (!run_button.isEnabled() && run)) {
            run_button.setEnabled(run);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void runButtonActionPerformed() {
        ScoringScheme scoring;
        PairwiseAlignment alignment;
        FileReader seq1_file;
        FileReader seq2_file;
        FileReader matrix_file;
        BufferedWriter output_file;
        String seq1_filename;
        String seq2_filename;
        String matrix_filename;
        String output_filename;
        String message;
        int alg;
        int match;
        int mismatch;
        int gap;
        long start;
        long elapsed;

        alg = algorithm_combo.getSelectedIndex();

        output_tab.setSelectedIndex(0);

        output_area.setText("");

        // ***************** SET SCORING SCHEME *****************
        if (basic_scheme) {
            progress_area.setText("Creating scoring scheme... ");

            try {
                match = Integer.parseInt(match_field.getText());
                mismatch = Integer.parseInt(mismatch_field.getText());
                gap = Integer.parseInt(gap_field.getText());

                scoring = new BasicScoringScheme(match, mismatch, gap);
                algorithm[alg].setScoringScheme(scoring);
                progress_area.append("OK");
            } catch (NumberFormatException e) {
                message = "Invalid scoring arguments.";
                progress_area.append("\n" + message);
                showError(message);

                return;
            }
        } else {
            matrix_filename = matrix_field.getText();
            progress_area.setText("Loading matrix file... ");

            try {
                matrix_file = new FileReader(matrix_filename);
            } catch (FileNotFoundException e) {
                message = "File \"" + matrix_filename + "\" not found.";
                progress_area.append("\n" + message);
                showError(message);

                return;
            }

            try {
                try {
                    scoring = new ScoringMatrix(matrix_file);
                    algorithm[alg].setScoringScheme(scoring);
                    progress_area.append("OK");
                } catch (InvalidScoringMatrixException e) {
                    matrix_file.close();
                    message = "Invalid matrix file \"" + matrix_filename +
                        "\".";
                    progress_area.append("\n" + message);
                    showError(message);

                    return;
                }

                matrix_file.close();
            } catch (IOException e) {
                message = "Error reading file.";
                progress_area.append("\n" + message);
                showError(message);

                return;
            }
        }

        // ***************** LOAD SEQUENCES *****************
        progress_area.append("\n\nLoading sequences... ");

        seq1_filename = seq1_field.getText();

        try {
            seq1_file = new FileReader(seq1_filename);
        } catch (FileNotFoundException e) {
            message = "File \"" + seq1_filename + "\" not found.";
            progress_area.append("\n" + message);
            showError(message);

            return;
        }

        seq2_filename = seq2_field.getText();

        try {
            seq2_file = new FileReader(seq2_filename);
        } catch (FileNotFoundException e) {
            message = "File \"" + seq2_filename + "\" not found.";
            progress_area.append("\n" + message);
            showError(message);

            return;
        }

        try {
            try {
                start = System.currentTimeMillis();
                algorithm[alg].loadSequences(seq1_file, seq2_file);
                elapsed = System.currentTimeMillis() - start;
                progress_area.append("OK");
                progress_area.append("\n[ Elapsed time: " + elapsed +
                    " milliseconds ]");
            } catch (InvalidSequenceException e) {
                seq1_file.close();
                seq2_file.close();
                message = "Invalid sequence files.";
                progress_area.append("\n" + message);
                showError(message);

                return;
            }

            seq1_file.close();
            seq2_file.close();
        } catch (IOException e) {
            message = "Error reading sequence files.";
            progress_area.append("\n" + message);
            showError(message);

            return;
        }

        // ***************** EXECUTE ALGORITHM *****************
        progress_area.append("\n\nRunning " +
            algorithm_combo.getSelectedItem() + "... ");

        try {
            start = System.currentTimeMillis();
            alignment = algorithm[alg].getPairwiseAlignment();
            elapsed = System.currentTimeMillis() - start;
            progress_area.append("OK");
            progress_area.append("\n[ Elapsed time: " + elapsed +
                " milliseconds ]");
        } catch (IncompatibleScoringSchemeException e) {
            message = "Scoring matrix is not compatible with loaded sequences.";
            progress_area.append("\n" + message);
            showError(message);

            return;
        } catch (OutOfMemoryError e) {
            message = "Insufficient memory to compute an alignment";
            progress_area.append("\n" + message);
            showError(message);

            return;
        }

        // ***************** DISPLAY / SAVE OUTPUT *****************
        if (output_to_file) {
            output_filename = output_field.getText();
            progress_area.append("\n\nSaving alignment... ");

            try {
                int length = alignment.getGappedSequence1().length();

                output_file = new BufferedWriter(new FileWriter(output_filename));
                output_file.write(alignment.getGappedSequence1(), 0, length);
                output_file.newLine();
                output_file.write(alignment.getScoreTagLine(), 0, length);
                output_file.newLine();
                output_file.write(alignment.getGappedSequence2(), 0, length);
                output_file.newLine();

                String tmp = "Score: " + alignment.getScore();
                output_file.write(tmp, 0, tmp.length());
                output_file.close();
            } catch (IOException e) {
                message = "Error writing file \"" + output_filename + "\".";
                progress_area.append("\n" + message);
                showError(message);

                return;
            }

            progress_area.append("OK");
        } else {
            output_area.setText(alignment.toString());
            output_tab.setSelectedIndex(1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
            JOptionPane.ERROR_MESSAGE);
    }
}
