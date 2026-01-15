package org.jscience.tests.net.ntp.gui;

import java.awt.*;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class FontChooser extends JPanel {
    /** DOCUMENT ME! */
    private static int maxFontSize = 255;

    /** DOCUMENT ME! */
    private static int minFontSize = 1;

    /** DOCUMENT ME! */
    private JComboBox fontComboBox;

    /** DOCUMENT ME! */
    private JTextField sizeTextField = new JTextField(10);

    /** DOCUMENT ME! */
    private String[] fontList;

    /** DOCUMENT ME! */
    private int originalSize;

/**
     * Creates a new FontChooser object.
     *
     * @param title DOCUMENT ME!
     */
    public FontChooser(String title) {
        sizeTextField.setMaximumSize(sizeTextField.getPreferredSize());
        fontList = Toolkit.getDefaultToolkit().getFontList();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        fontComboBox = new JComboBox(fontList);
        add(fontComboBox);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(sizeTextField);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFontName() {
        return (String) fontComboBox.getSelectedItem();
    }

    /**
     * DOCUMENT ME!
     *
     * @param fontName DOCUMENT ME!
     */
    public void setFontName(String fontName) {
        fontComboBox.setSelectedIndex(0);

        for (int i = 0; i < fontList.length; i++) {
            if (fontList[i].equals(fontName)) {
                fontComboBox.setSelectedIndex(i);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFontSize() {
        try {
            originalSize = Integer.parseInt(sizeTextField.getText());

            if (originalSize > maxFontSize) {
                originalSize = maxFontSize;
            } else if (originalSize < minFontSize) {
                originalSize = minFontSize;
            }
        } catch (NumberFormatException e) {
        }

        sizeTextField.setText("" + originalSize);

        return originalSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param size DOCUMENT ME!
     */
    public void setFontSize(int size) {
        originalSize = size;
        sizeTextField.setText("" + size);
    }
}
