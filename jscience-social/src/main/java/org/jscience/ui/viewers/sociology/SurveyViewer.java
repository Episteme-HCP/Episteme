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

package org.jscience.ui.viewers.sociology;

import javax.swing.*;
import java.awt.*;
import org.jscience.sociology.survey.Question;
import org.jscience.sociology.survey.Survey;
import org.jscience.sociology.survey.ChoiceQuestion;
import org.jscience.sociology.survey.QuestionType;

/**
 * A Swing-based viewer for rendering a Survey.
 * * @version 1.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SurveyViewer extends JPanel {

    private final Survey survey;

    /**
     * Creates a viewer for the specified survey.
     * @param survey the survey to display
     */
    public SurveyViewer(Survey survey) {
        this.survey = survey;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel(survey.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titleLabel);
        add(Box.createVerticalStrut(5));

        // Description
        if (survey.getDescription() != null) {
            JLabel descLabel = new JLabel("<html>" + survey.getDescription() + "</html>");
            add(descLabel);
            add(Box.createVerticalStrut(15));
        }

        // Questions
        for (Question q : survey.getQuestions()) {
            add(createQuestionPanel(q));
            add(Box.createVerticalStrut(10));
        }
    }

    private JPanel createQuestionPanel(Question q) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createTitledBorder(q.getText() + (q.isRequired() ? " *" : "")));

        QuestionType type = q.getType();
        if (QuestionType.TEXT.equals(type)) {
            panel.add(new JTextField(20));
        } else if (QuestionType.PARAGRAPH.equals(type)) {
            panel.add(new JScrollPane(new JTextArea(3, 20)));
        } else if (QuestionType.MULTIPLE_CHOICE.equals(type)) {
            ButtonGroup bg = new ButtonGroup();
            if (q instanceof ChoiceQuestion cq) {
                for (String opt : cq.getOptions()) {
                    JRadioButton rb = new JRadioButton(opt);
                    bg.add(rb);
                    panel.add(rb);
                }
            }
        } else if (QuestionType.CHECKBOXES.equals(type)) {
            if (q instanceof ChoiceQuestion cq) {
                for (String opt : cq.getOptions()) {
                    panel.add(new JCheckBox(opt));
                }
            }
        } else if (QuestionType.DROPDOWN.equals(type)) {
            if (q instanceof ChoiceQuestion cq) {
                panel.add(new JComboBox<>(cq.getOptions().toArray(new String[0])));
            }
        } else {
            panel.add(new JLabel("[Unsupported Types: " + type + "]"));
        }

        return panel;
    }

    /**
     * Utility method to show the viewer in a dialog.
     */
    public static void showInDialog(Survey survey) {
        JFrame frame = new JFrame("Survey Preview");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new JScrollPane(new SurveyViewer(survey)));
        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
