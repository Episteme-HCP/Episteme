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

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.*;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.MouseInputListener;


/**
 * ImageViewer simply displays images in a frame. The viewer supports a
 * context menue with right-mouse-click.
 *
 * @author Holger Antelmann
 */
public class ImageViewer extends JMainFrame {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 5845591419328408028L;

    /** DOCUMENT ME! */
    static int SCALING_HINTS = Image.SCALE_DEFAULT;

    /** DOCUMENT ME! */
    private ImageIcon imageIcon;

    /** DOCUMENT ME! */
    private Image image;

    /** DOCUMENT ME! */
    private ImageComponent imageComponent;

    /** DOCUMENT ME! */
    private JScrollPane scrollPane = new JScrollPane();

    /** DOCUMENT ME! */
    private JCheckBoxMenuItem maintainProportions = new JCheckBoxMenuItem(
            "maintain proportions");

    /** DOCUMENT ME! */
    private JCheckBoxMenuItem sizeToFit = new JCheckBoxMenuItem("size to fit");

    //private Point dragBegin = null;
/**
     * Creates a new ImageViewer object.
     *
     * @param imageFile DOCUMENT ME!
     * @throws MalformedURLException DOCUMENT ME!
     */
    public ImageViewer(File imageFile) throws MalformedURLException {
        this(new ImageIcon(imageFile.toURI().toURL()), imageFile.getName());
    }

/**
     * Creates a new ImageViewer object.
     *
     * @param url DOCUMENT ME!
     */
    public ImageViewer(URL url) {
        this(new ImageIcon(url), url.getFile());
    }

/**
     * Creates a new ImageViewer object.
     *
     * @param imageIcon DOCUMENT ME!
     */
    public ImageViewer(ImageIcon imageIcon) {
        this(imageIcon, imageIcon.getDescription());
    }

/**
     * Creates a new ImageViewer object.
     *
     * @param imageIcon   DOCUMENT ME!
     * @param description DOCUMENT ME!
     */
    public ImageViewer(ImageIcon imageIcon, String description) {
        super(description);
        imageIcon.setDescription(description);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.imageIcon = imageIcon;
        image = imageIcon.getImage();
        maintainProportions.setEnabled(false);
        imageComponent = new ImageComponent();
        getContentPane().setLayout(new BorderLayout());

        //getContentPane().add(imageComponent, BorderLayout.CENTER);
        scrollPane = new JScrollPane(imageComponent);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        MyMouseInputListener listener = new MyMouseInputListener();
        scrollPane.addMouseListener(listener);
        scrollPane.addMouseMotionListener(listener);
        imageComponent.addMouseListener(listener);
        imageComponent.addMouseMotionListener(listener);
        maintainProportions.setSelected(true);
        pack();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     */
    private void cropImage(Rectangle view) {
        CropImageFilter filter = new CropImageFilter(view.x, view.y,
                view.width, view.height);
        FilteredImageSource producer = new FilteredImageSource(image.getSource(),
                filter);
        image = createImage(producer);
        System.out.println("width : " + image.getWidth(this));
        System.out.println("height: " + image.getHeight(this));
    }

    //static final double SCALE_DELTA = .2;
    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    class ImageComponent extends Component {
        /** DOCUMENT ME! */
        static final long serialVersionUID = -8954241710732001597L;

        /** DOCUMENT ME! */
        double q = (double) imageIcon.getIconWidth() / (double) imageIcon.getIconHeight();

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Dimension getPreferredSize() {
            return new Dimension(image.getWidth(this), image.getHeight(this));
        }

        /**
         * DOCUMENT ME!
         *
         * @param g DOCUMENT ME!
         */
        public void paint(Graphics g) {
            if (sizeToFit.isSelected()) {
                if (maintainProportions.isSelected()) {
                    double cq = (double) getWidth() / (double) getHeight();

                    if ((q / cq) > 1) {
                        g.drawImage(image, 0, 0, getWidth(),
                            (int) (getWidth() / q), this);
                    } else {
                        g.drawImage(image, 0, 0, (int) (getHeight() * q),
                            getHeight(), this);
                    }
                } else {
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                }
            } else {
                g.drawImage(image, 0, 0, image.getWidth(this),
                    image.getHeight(this), this);
            }

            /*
            if (dragBegin != null) {
                g.drawRect(Math.min(dragBegin.x, currentDrag.x),
                    Math.min(dragBegin.y, currentDrag.y),
                    Math.abs(dragBegin.x - currentDrag.x),
                    Math.abs(dragBegin.y - currentDrag.y));
            }
            */
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    class MyMouseInputListener extends MouseAdapter
        implements MouseInputListener {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void mouseDragged(MouseEvent e) {
            /*
            if (dragBegin == null) dragBegin = new Point(e.getX(), e.getY());
            currentDrag.x = e.getX();
            currentDrag.y = e.getY();
            imageComponent.repaint();
            */
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void mouseMoved(MouseEvent e) {
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void mousePressed(MouseEvent e) {
            popup(e);
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void mouseReleased(MouseEvent e) {
            popup(e);

            /*
            if (dragBegin != null) {
                Point end = new Point(e.getX(), e.getY());
                Rectangle view = new Rectangle(
                    Math.min(dragBegin.x, currentDrag.x),
                    Math.min(dragBegin.y, currentDrag.y),
                    Math.abs(dragBegin.x - currentDrag.x),
                    Math.abs(dragBegin.y - currentDrag.y));
                dragBegin = null;
                if (view.width + view.height > 10) {
                    cropImage(view);
                }
                imageComponent.repaint();
            }
            */
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         *
         * @throws Error DOCUMENT ME!
         */
        void popup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                JPopupMenu menu = new JPopupMenu();
                JMenuItem item = new JMenuItem("reset image");
                item.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ev) {
                            image = imageIcon.getImage();
                            imageComponent.setSize(imageComponent.getPreferredSize());
                            imageComponent.repaint();
                            pack();
                        }
                    });
                menu.add(item);
                item = new JMenuItem("copy image");
                item.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ev) {
                            Clipboard cb = Toolkit.getDefaultToolkit()
                                                  .getSystemClipboard();
                            ImageTransfer it = new ImageTransfer(imageIcon.getImage());
                            cb.setContents(it, it);
                        }
                    });
                menu.add(item);
                menu.add(maintainProportions);
                menu.add(sizeToFit);
                sizeToFit.addItemListener(new ItemListener() {
                        public void itemStateChanged(ItemEvent ev) {
                            ImageViewer.this.getContentPane().removeAll();

                            switch (ev.getStateChange()) {
                            case ItemEvent.SELECTED:
                                maintainProportions.setEnabled(true);
                                ImageViewer.this.getContentPane()
                                                .add(imageComponent,
                                    BorderLayout.CENTER);

                                break;

                            case ItemEvent.DESELECTED:
                                maintainProportions.setEnabled(false);
                                ImageViewer.this.getContentPane()
                                                .add(scrollPane,
                                    BorderLayout.CENTER);
                                scrollPane.setViewportView(imageComponent);

                                break;

                            default:
                                throw new Error();
                            }
                        }
                    });
                menu.show(e.getComponent(), e.getX(), e.getY());
                imageComponent.repaint(); // doesn't quite have the desired effect
            }
        }
    }
}
