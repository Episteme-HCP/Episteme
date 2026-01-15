/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import org.jscience.io.ExtensionFileFilter;

import org.jscience.util.Debug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.net.MalformedURLException;

import javax.swing.*;


/**
 * Just a GUI for loading images in an ImageViewer object
 *
 * @author Holger Antelmann
 *
 * @see ImageViewer
 */
public class ImageLoader extends JMainFrame {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -2421313911981695344L;

    /** DOCUMENT ME! */
    JFileChooser chooser;

    /** DOCUMENT ME! */
    ExtensionFileFilter filter;

/**
     * Creates a new ImageLoader object.
     */
    public ImageLoader() {
        super("ImageViewer", true, false);

        //setJMenuBar(getMenu());
        filter = new ExtensionFileFilter("jpg", "Images");
        filter.addType("jpeg");
        filter.addType("gif");
        filter.addType("tif");
        filter.addType("tiff");
        chooser = new JFileChooser();
        chooser.addChoosableFileFilter(filter);
        chooser.setAccessory(new FileChooserImagePreview(chooser));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        final JButton button = new JButton("view image from file");
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int open = chooser.showOpenDialog(button);

                    if (open == JFileChooser.APPROVE_OPTION) {
                        try {
                            File file = chooser.getSelectedFile();
                            new ImageViewer(file).setVisible(true);
                        } catch (MalformedURLException ignore) {
                        }
                    }
                }
            });
        getContentPane().add(button);
        pack();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JFileChooser getJFileChooser() {
        return chooser;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(Debug.dialogExceptionHandler);

        ImageLoader il = new ImageLoader();
        il.setDefaultCloseOperation(il.EXIT_ON_CLOSE);
        il.setVisible(true);
    }
}
