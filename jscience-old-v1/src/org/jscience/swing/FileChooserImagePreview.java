/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import java.awt.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File;

import javax.swing.*;


/**
 * ImagePreview can preview an image for a JFileChooser. This
 * implementation is based on the Java Swing Tutorial. usage example:<pre>
 * JFileChooser chooser = ...
 * chooser.setAccessory(new FileChooserImagePreview(chooser));</pre>
 *
 * @author Holger Antelmann
 */
class FileChooserImagePreview extends JComponent
    implements PropertyChangeListener {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 691927346036332936L;

    /** DOCUMENT ME! */
    JComponent imageComponent = new JComponent() {
            static final long serialVersionUID = -4887261897816454945L;

            public void paintComponent(Graphics g) {
                if (thumbnail == null) {
                    loadImage();
                }

                if (thumbnail != null) {
                    int x = (getWidth() / 2) - (thumbnail.getIconWidth() / 2);
                    int y = (getHeight() / 2) -
                        (thumbnail.getIconHeight() / 2);

                    if (y < 0) {
                        y = 0;
                    }

                    if (x < 5) {
                        x = 5;
                    }

                    thumbnail.paintIcon(this, g, x, y);
                }
            }
        };

    /** DOCUMENT ME! */
    int width;

    /** DOCUMENT ME! */
    int height;

    /** DOCUMENT ME! */
    boolean proportinal;

    /** DOCUMENT ME! */
    ImageIcon thumbnail = null;

    /** DOCUMENT ME! */
    File file = null;

    /** DOCUMENT ME! */
    JLabel label = new JLabel("(width x height)");

/**
     * Creates a new FileChooserImagePreview object.
     *
     * @param fc DOCUMENT ME!
     */
    public FileChooserImagePreview(JFileChooser fc) {
        this(fc, 100, 50, false);
    }

/**
     * Creates a new FileChooserImagePreview object.
     *
     * @param fc          DOCUMENT ME!
     * @param width       DOCUMENT ME!
     * @param height      DOCUMENT ME!
     * @param proportinal DOCUMENT ME!
     */
    public FileChooserImagePreview(JFileChooser fc, int width, int height,
        boolean proportinal) {
        this.proportinal = proportinal;
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        fc.addPropertyChangeListener(this);
        setLayout(new BorderLayout());
        add(imageComponent, BorderLayout.CENTER);
        add(label, BorderLayout.SOUTH);
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * DOCUMENT ME!
     */
    public void loadImage() {
        if (file == null) {
            return;
        }

        ImageIcon tmpIcon = new ImageIcon(file.getPath());

        if (tmpIcon.getIconWidth() > 90) {
            thumbnail = new ImageIcon(tmpIcon.getImage()
                                             .getScaledInstance(90, -1,
                        Image.SCALE_DEFAULT));
        } else {
            thumbnail = tmpIcon;
        }

        label.setText(tmpIcon.getIconWidth() + " x " + tmpIcon.getIconHeight());
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            file = (File) e.getNewValue();

            if (isShowing()) {
                loadImage();
                repaint();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintComponent(Graphics g) {
        if (thumbnail == null) {
            loadImage();
        }

        if (thumbnail != null) {
            int x = (getWidth() / 2) - (thumbnail.getIconWidth() / 2);
            int y = (getHeight() / 2) - (thumbnail.getIconHeight() / 2);

            if (y < 0) {
                y = 0;
            }

            if (x < 5) {
                x = 5;
            }

            thumbnail.paintIcon(this, g, x, y);

            //g.drawString(text, 0, 0);
        }
    }
}
