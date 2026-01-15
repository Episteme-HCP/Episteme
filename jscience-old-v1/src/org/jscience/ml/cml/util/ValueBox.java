package org.jscience.ml.cml.util;

import java.awt.*;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class ValueBox extends JPanel {
    /** DOCUMENT ME! */
    JComboBox comboBox;

    /** DOCUMENT ME! */
    String[] values;

/**
     * Creates a new ValueBox object.
     *
     * @param prompt DOCUMENT ME!
     * @param values DOCUMENT ME!
     */
    public ValueBox(String prompt, String[] values) {
        this.values = values;
        this.setLayout(new BorderLayout());
        this.add(new JLabel(prompt), BorderLayout.WEST);
        comboBox = new JComboBox();

        this.add(comboBox, BorderLayout.CENTER);

        for (int i = 0; i < values.length; i++) {
            comboBox.addItem(values[i]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    public void setSelectedIndex(int index) {
        comboBox.setSelectedIndex(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSelectedIndex() {
        return comboBox.getSelectedIndex();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSelectedValue() {
        return (String) values[comboBox.getSelectedIndex()];
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void setSelectedValue(String value) {
        int index = CMLUtils.indexOf(value, values, values.length);
        comboBox.setSelectedIndex(index);
    }
}
