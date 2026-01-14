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

package org.jscience.media.pictures.filters;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.MemoryImageSource;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class Morph extends JPanel implements PropertyChangeListener,
    ChangeListener, ActionListener, ItemListener {
    /** DOCUMENT ME! */
    private int rows = 7;

    /** DOCUMENT ME! */
    private int cols = 7;

    /** DOCUMENT ME! */
    private WarpGridEditor editor1;

    /** DOCUMENT ME! */
    private WarpGridEditor editor2;

    /** DOCUMENT ME! */
    private WarpGridEditor editor3;

    /** DOCUMENT ME! */
    private WarpGrid warpGrid1;

    /** DOCUMENT ME! */
    private WarpGrid warpGrid2;

    /** DOCUMENT ME! */
    private WarpGrid warpGrid3;

    /** DOCUMENT ME! */
    private WarpFilter filter = new WarpFilter();

    /** DOCUMENT ME! */
    private Image sourceImage;

    /** DOCUMENT ME! */
    private Image destImage;

    /** DOCUMENT ME! */
    private JSlider tSlider;

    /** DOCUMENT ME! */
    private JCheckBox dissolveCheck;

    /** DOCUMENT ME! */
    private JCheckBox showGridCheck;

    /** DOCUMENT ME! */
    private JComboBox gridCombo;

    /** DOCUMENT ME! */
    private JButton animateButton;

    /** DOCUMENT ME! */
    private float t = 1.0f;

    /** DOCUMENT ME! */
    private boolean dissolveEm = true;

/**
     * Creates a new Morph object.
     *
     * @param imageName1 DOCUMENT ME!
     * @param imageName2 DOCUMENT ME!
     */
    public Morph(String imageName1, String imageName2) {
        JPanel panel = new JPanel();

        setLayout(new BorderLayout());
        panel.setLayout(new GridLayout(1, 3));
        editor1 = new WarpGridEditor();
        editor2 = new WarpGridEditor();
        editor3 = new WarpGridEditor();
        sourceImage = getToolkit().getImage(imageName1);
        destImage = getToolkit().getImage(imageName2);

        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(sourceImage, 0);
        tracker.addImage(destImage, 0);

        try {
            tracker.waitForID(0);
        } catch (InterruptedException ex) {
        }

        warpGrid1 = new WarpGrid(rows, cols, sourceImage.getWidth(this),
                sourceImage.getHeight(this));
        warpGrid2 = new WarpGrid(rows, cols, destImage.getWidth(this),
                destImage.getHeight(this));
        warpGrid3 = new WarpGrid(rows, cols, sourceImage.getWidth(this),
                sourceImage.getHeight(this));

        editor1.setBorder(new TitledBorder("Source"));
        editor1.setImage(sourceImage);
        editor1.setWarpGrid(warpGrid1);
        editor1.addPropertyChangeListener(this);

        editor2.setBorder(new TitledBorder("Destination"));
        editor2.setImage(destImage);
        editor2.setWarpGrid(warpGrid2);
        editor2.addPropertyChangeListener(this);

        editor3.setBorder(new TitledBorder("Intermediate"));
        editor3.setImage(sourceImage);
        editor3.setWarpGrid(warpGrid3);
        editor3.setEnabled(false);

        panel.add(editor1);
        panel.add(editor2);
        panel.add(editor3);
        add(panel, BorderLayout.CENTER);

        panel = new JPanel();
        panel.add(tSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0));
        tSlider.setPaintTicks(true);
        tSlider.setMajorTickSpacing(50);
        tSlider.setMinorTickSpacing(10);
        tSlider.setPaintLabels(true);
        tSlider.setValue((int) (t * 100.0));
        tSlider.addChangeListener(this);

        panel.add(new JLabel("Grid Size:", JLabel.RIGHT));
        panel.add(gridCombo = new JComboBox());

        for (int i = 0; i < 12; i++)
            gridCombo.addItem((i + 3) + "x" + (i + 3));

        gridCombo.setSelectedIndex(rows - 3);
        gridCombo.addItemListener(this);

        panel.add(dissolveCheck = new JCheckBox("Dissolve"));
        dissolveCheck.setSelected(dissolveEm);
        dissolveCheck.addChangeListener(this);
        panel.add(showGridCheck = new JCheckBox("Show Grids"));
        showGridCheck.setSelected(true);
        showGridCheck.addChangeListener(this);
        panel.add(animateButton = new JButton("Animate"));
        animateButton.addActionListener(this);

        add(panel, BorderLayout.SOUTH);
    }

    /*
            public void setObject(Object o) {
                    super.setObject(o);
                    filter = (WarpFilter)o;
            }
    */
    public void setGridSize(int rows, int cols) {
        warpGrid1 = new WarpGrid(rows, cols, sourceImage.getWidth(this),
                sourceImage.getHeight(this));
        warpGrid2 = new WarpGrid(rows, cols, destImage.getWidth(this),
                destImage.getHeight(this));
        warpGrid3 = new WarpGrid(rows, cols, sourceImage.getWidth(this),
                sourceImage.getHeight(this));
        editor1.setWarpGrid(warpGrid1);
        editor2.setWarpGrid(warpGrid2);
        editor3.setWarpGrid(warpGrid3);
        editor1.repaint();
        editor2.repaint();
        editor3.repaint();
        preview();
    }

    /**
     * DOCUMENT ME!
     */
    public void preview() {
        if (dissolveEm) {
            int width = sourceImage.getWidth(this);
            int height = sourceImage.getHeight(this);
            int[] srcPixels = filter.getPixels(sourceImage, width, height);
            int[] destPixels = filter.getPixels(destImage, width, height);
            int[] outPixels = new int[width * height];
            filter.morph(srcPixels, destPixels, outPixels, warpGrid1,
                warpGrid2, width, height, t);
            editor3.setImage(createImage(
                    new MemoryImageSource(width, height, outPixels, 0, width)));
        } else {
            filter.setSourceGrid(warpGrid1);
            warpGrid1.lerp(t, warpGrid2, warpGrid3);
            filter.setDestGrid(warpGrid3);
            editor3.setImage(createImage(
                    new FilteredImageSource(sourceImage.getSource(), filter)));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void propertyChange(PropertyChangeEvent e) {
        preview();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        if (filter != null) {
            Object source = e.getSource();

            if (source instanceof JSlider &&
                    ((JSlider) source).getValueIsAdjusting()) {
                return;
            }

            if (source == tSlider) {
                t = tSlider.getValue() / 100.0f;
            } else if (source == dissolveCheck) {
                dissolveEm = dissolveCheck.isSelected();
            } else if (source == showGridCheck) {
                boolean b = showGridCheck.isSelected();
                editor1.setShowGrid(b);
                editor2.setShowGrid(b);
                editor3.setShowGrid(b);

                return;
            }

            preview();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void itemStateChanged(ItemEvent e) {
        if (filter != null) {
            Object source = e.getSource();

            if (source == gridCombo) {
                int i = gridCombo.getSelectedIndex();
                setGridSize(i + 3, i + 3);
            }

            preview();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == animateButton) {
            String text = (String) JOptionPane.showInputDialog(this,
                    "Number of Frames:", "Animate", JOptionPane.PLAIN_MESSAGE,
                    null, null, "10");

            if (text != null) {
                try {
                    int numFrames = Integer.parseInt(text);
                    WarpFilter wf = (WarpFilter) filter.clone();
                    wf.setSourceGrid(warpGrid1);
                    wf.setDestGrid(warpGrid2);

                    //					wf.setFrames(numFrames);
                    //					Image image = createImage(new FilteredImageSource(sourceImage.getSource(), wf));
                    int width = sourceImage.getWidth(this);
                    int height = sourceImage.getHeight(this);
                    Image image = createImage(numFrames * width, height);
                    Graphics g = image.getGraphics();

                    for (int i = 0; i < numFrames; i++) {
                        float t = (float) i / (numFrames - 1);
                        int[] srcPixels = filter.getPixels(sourceImage, width,
                                height);
                        int[] destPixels = filter.getPixels(destImage, width,
                                height);
                        int[] outPixels = new int[width * height];
                        filter.morph(srcPixels, destPixels, outPixels,
                            warpGrid1, warpGrid2, width, height, t);

                        Image f = createImage(new MemoryImageSource(width,
                                    height, outPixels, 0, width));
                        g.drawImage(f, i * width, 0, this);
                    }

                    g.dispose();

                    Frame frame = new Frame();
                    ImageDisplay canvas = new ImageDisplay();
                    canvas.setImage(image);
                    frame.add(new JScrollPane(canvas), BorderLayout.CENTER);
                    frame.pack();
                    frame.show();
                } catch (NumberFormatException ex) {
                    getToolkit().beep();
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        if (args.length >= 2) {
            Frame f = new Frame("Warp");
            f.add(new Morph(args[0], args[1]));
            f.pack();
            f.show();
        } else {
            System.out.println("Usage: Morph image1 image2");
        }
    }
}


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
class WarpGridEditor extends JPanel {
    /** DOCUMENT ME! */
    private Image image;

    /** DOCUMENT ME! */
    private WarpGrid warpGrid;

    /** DOCUMENT ME! */
    private boolean showGrid = true;

/**
     * Creates a new WarpGridEditor object.
     */
    public WarpGridEditor() {
        JScrollPane scrollPane;
        setLayout(new BorderLayout());

        WarpGridCanvas canvas = new WarpGridCanvas();
        add(scrollPane = new JScrollPane(canvas), BorderLayout.CENTER);
    }

    /**
     * DOCUMENT ME!
     *
     * @param warpGrid DOCUMENT ME!
     */
    public void setWarpGrid(WarpGrid warpGrid) {
        this.warpGrid = warpGrid;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WarpGrid getWarpGrid() {
        return warpGrid;
    }

    /**
     * DOCUMENT ME!
     *
     * @param image DOCUMENT ME!
     */
    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param showGrid DOCUMENT ME!
     */
    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getShowGrid() {
        return showGrid;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getMinimumSize() {
        return new Dimension(100, 100);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    /**
     * DOCUMENT ME!
     */
    private void gridChanged() {
        firePropertyChange("warpGrid", null, warpGrid);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.4 $
     */
    class WarpGridCanvas extends JComponent {
        /** DOCUMENT ME! */
        private Graphics dragGraphics;

        /** DOCUMENT ME! */
        private int dragRow = -1;

        /** DOCUMENT ME! */
        private int dragCol = -1;

        /** DOCUMENT ME! */
        private int dragX = -1;

        /** DOCUMENT ME! */
        private int dragY = -1;

/**
         * Creates a new WarpGridCanvas object.
         */
        public WarpGridCanvas() {
            enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Dimension getMinimumSize() {
            return new Dimension(64, 64);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Dimension getPreferredSize() {
            if (image != null) {
                return new Dimension(image.getWidth(this), image.getHeight(this));
            }

            return new Dimension(164, 164);
        }

        /**
         * DOCUMENT ME!
         *
         * @param g DOCUMENT ME!
         */
        public void paintComponent(Graphics g) {
            Dimension size = getSize();

            if (image != null) {
                g.drawImage(image, 0, 0, this);
            }

            if (showGrid) {
                g.setColor(Color.yellow);

                int index = 0;

                for (int row = 0; row < warpGrid.rows; row++) {
                    for (int col = 0; col < warpGrid.cols; col++) {
                        int x = (int) warpGrid.xGrid[index];
                        int y = (int) warpGrid.yGrid[index];

                        if (row > 0) {
                            g.drawLine(x, y,
                                (int) warpGrid.xGrid[index - warpGrid.cols],
                                (int) warpGrid.yGrid[index - warpGrid.cols]);
                        }

                        if (col > 0) {
                            g.drawLine(x, y, (int) warpGrid.xGrid[index - 1],
                                (int) warpGrid.yGrid[index - 1]);
                        }

                        g.fillOval(x - 2, y - 2, 5, 5);
                        index++;
                    }
                }
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        protected void processMouseEvent(MouseEvent e) {
            if (!isEnabled() || (warpGrid == null)) {
                return;
            }

            Dimension size = getSize();
            int id = e.getID();
            int x = e.getX();
            int y = e.getY();

            switch (id) {
            case MouseEvent.MOUSE_PRESSED:
                dragRow = -1;

                int index = 0;

                for (int row = 0; row < warpGrid.rows; row++) {
                    for (int col = 0; col < warpGrid.cols; col++) {
                        int wx = (int) warpGrid.xGrid[index];
                        int wy = (int) warpGrid.yGrid[index];

                        if (((wx - 2) <= x) && (x <= (wx + 2)) &&
                                ((wy - 2) <= y) && (y <= (wy + 2))) {
                            dragX = x;
                            dragY = y;
                            dragRow = row;
                            dragCol = col;
                            dragGraphics = getGraphics();
                            dragGraphics.setXORMode(getBackground());
                            dragGraphics.fillOval(dragX - 2, dragY - 2, 5, 5);
                            enableEvents(AWTEvent.MOUSE_EVENT_MASK |
                                AWTEvent.MOUSE_MOTION_EVENT_MASK);

                            return;
                        }

                        index++;
                    }
                }

                break;

            case MouseEvent.MOUSE_RELEASED:
                enableEvents(AWTEvent.MOUSE_EVENT_MASK);

                if (dragRow != -1) {
                    dragGraphics.fillOval(dragX - 2, dragY - 2, 5, 5);
                    dragGraphics.dispose();
                    dragGraphics = null;
                    index = (dragRow * warpGrid.cols) + dragCol;
                    warpGrid.xGrid[index] = (float) ImageMath.clamp(x, 0,
                            image.getWidth(this));
                    warpGrid.yGrid[index] = (float) ImageMath.clamp(y, 0,
                            image.getHeight(this));
                    repaint();
                    gridChanged();
                }

                dragRow = dragCol = -1;

                break;
            }

            super.processMouseEvent(e);
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        protected void processMouseMotionEvent(MouseEvent e) {
            int id = e.getID();
            int x = e.getX();
            int y = e.getY();

            switch (id) {
            case MouseEvent.MOUSE_DRAGGED:

                if (dragRow != -1) {
                    dragGraphics.fillOval(dragX - 2, dragY - 2, 5, 5);
                    dragX = ImageMath.clamp(x, 0, image.getWidth(this));
                    dragY = ImageMath.clamp(y, 0, image.getHeight(this));
                    dragGraphics.fillOval(dragX - 2, dragY - 2, 5, 5);
                }

                break;
            }

            super.processMouseMotionEvent(e);
        }
    }
}


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
class ImageDisplay extends JComponent {
    /** DOCUMENT ME! */
    private Image image;

/**
     * Creates a new ImageDisplay object.
     */
    public ImageDisplay() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param image DOCUMENT ME!
     */
    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getMinimumSize() {
        return new Dimension(64, 64);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        if (image != null) {
            return new Dimension(image.getWidth(this), image.getHeight(this));
        }

        return new Dimension(164, 164);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintComponent(Graphics g) {
        if (image != null) {
            Dimension size = getSize();
            int x = (size.width - image.getWidth(this)) / 2;
            int y = (size.height - image.getHeight(this)) / 2;
            g.drawImage(image, x, y, this);
        }
    }
}
