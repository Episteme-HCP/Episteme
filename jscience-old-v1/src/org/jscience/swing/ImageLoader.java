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
