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

package org.jscience.chemistry.vapor.util.chart;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.text.DecimalFormat;

import java.util.ArrayList;

import javax.swing.*;


/**
 * Basic X-Y Plot charting tool.
 */
public class Chart extends JPanel {
    /**
     * DOCUMENT ME!
     */
    private static final double EPSILON = 1E-10;

    /**
     * DOCUMENT ME!
     */
    public static final int AXIS_TOP = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int AXIS_BOTTOM = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int AXIS_LEFT = 3;

    /**
     * DOCUMENT ME!
     */
    public static final int AXIS_RIGHT = 4;

    /**
     * DOCUMENT ME!
     */
    public static final int AXIS_NONE = 5;

    /**
     * DOCUMENT ME!
     */
    public static final int VALIGN_ABOVE = 10;

    /**
     * DOCUMENT ME!
     */
    public static final int VALIGN_MIDDLE = 11;

    /**
     * DOCUMENT ME!
     */
    public static final int VALIGN_BELOW = 12;

    /**
     * DOCUMENT ME!
     */
    public static final int HALIGN_LEFT = 20;

    /**
     * DOCUMENT ME!
     */
    public static final int HALIGN_CENTER = 21;

    /**
     * DOCUMENT ME!
     */
    public static final int HALIGN_RIGHT = 22;

    /**
     * DOCUMENT ME!
     */
    private JLabel subTitle = new JLabel();

    /**
     * DOCUMENT ME!
     */
    private JLabel title = new JLabel();

    /**
     * DOCUMENT ME!
     */
    private JLabel xLabel = new JLabel();

    /**
     * DOCUMENT ME!
     */
    private JLabel yLabel = new JLabel();

    /**
     * DOCUMENT ME!
     */
    private Color bgColor = Color.black;

    /**
     * DOCUMENT ME!
     */
    private Color titleColor = Color.yellow;

    /**
     * DOCUMENT ME!
     */
    private Color subTitleColor = Color.white;

    /**
     * DOCUMENT ME!
     */
    private Color axisLabelColor = Color.white;

    /**
     * DOCUMENT ME!
     */
    private Color axisValueColor = Color.gray;

    /**
     * DOCUMENT ME!
     */
    private Color hlFontColor = Color.yellow;

    /**
     * DOCUMENT ME!
     */
    private Font titleFont = this.getFont().deriveFont(Font.BOLD, 16);

    /**
     * DOCUMENT ME!
     */
    private Font subTitleFont = this.getFont().deriveFont(Font.ITALIC, 12);

    /**
     * DOCUMENT ME!
     */
    private Font axisLabelFont = this.getFont().deriveFont(Font.PLAIN, 10);

    /**
     * DOCUMENT ME!
     */
    private Font axisValueFont = this.getFont().deriveFont(Font.PLAIN, 10);

    /**
     * DOCUMENT ME!
     */
    private ChartPanel chartPanel = new ChartPanel();

    /**
     * DOCUMENT ME!
     */
    private DecimalFormat decimalFormat = new DecimalFormat("#.###");

/**
     * Default Constructor. This initializes the panel
     * to display the charting area.
     */
    public Chart() {
        GridBagConstraints gbConsts;
        setLayout(new GridBagLayout());

        gbConsts = new GridBagConstraints();
        gbConsts.gridx = 0;
        gbConsts.gridy = 0;
        gbConsts.gridwidth = 2;
        gbConsts.insets = new Insets(10, 0, 0, 0);
        add(title, gbConsts);

        gbConsts = new GridBagConstraints();
        gbConsts.gridx = 0;
        gbConsts.gridy = 1;
        gbConsts.gridwidth = 2;
        gbConsts.insets = new Insets(5, 0, 0, 0);
        add(subTitle, gbConsts);

        gbConsts = new GridBagConstraints();
        gbConsts.gridx = 0;
        gbConsts.gridy = 2;
        gbConsts.insets = new Insets(0, 10, 0, 0);
        add(yLabel, gbConsts);

        gbConsts = new GridBagConstraints();
        gbConsts.gridx = 1;
        gbConsts.gridy = 3;
        gbConsts.insets = new Insets(0, 0, 10, 0);
        add(xLabel, gbConsts);

        gbConsts = new GridBagConstraints();
        gbConsts.gridx = 1;
        gbConsts.gridy = 2;
        gbConsts.fill = GridBagConstraints.BOTH;
        gbConsts.insets = new Insets(10, 10, 10, 10);
        gbConsts.weightx = 1.0;
        gbConsts.weighty = 1.0;
        add(chartPanel, gbConsts);

        setBGColor(bgColor);
        setTitleColor(titleColor);
        setSubTitleColor(subTitleColor);
        setAxisLabelColor(axisLabelColor);
        setTitleFont(titleFont);
        setSubTitleFont(subTitleFont);
        setAxisLabelFont(axisLabelFont);
        setAutoXGridSpacing(true);
        setAutoYGridSpacing(true);
    }

    /**
     * Returns whether grid is displayed.
     *
     * @return boolean
     */
    public boolean isShowGrid() {
        return chartPanel.isShowGrid();
    }

    /**
     * Sets whether grid is displayed.
     *
     * @param showGrid
     */
    public void setShowGrid(boolean showGrid) {
        chartPanel.setShowGrid(showGrid);
    }

    /**
     * Returns whether grid spacing for X-Axis is automatic.
     *
     * @return boolean
     */
    public boolean isAutoXGridSpacing() {
        return chartPanel.isAutoXGridSpacing();
    }

    /**
     * Sets automatic grid spacing for X-Axis
     *
     * @param autoXSpacing
     */
    public void setAutoXGridSpacing(boolean autoXSpacing) {
        chartPanel.setAutoXGridSpacing(autoXSpacing);
    }

    /**
     * Returns whether grid spacing for Y-Axis is automatic.
     *
     * @return boolean
     */
    public boolean isAutoYGridSpacing() {
        return chartPanel.isAutoYGridSpacing();
    }

    /**
     * Sets automatic grid spacing for Y-Axis
     *
     * @param autoYSpacing
     */
    public void setAutoYGridSpacing(boolean autoYSpacing) {
        chartPanel.setAutoYGridSpacing(autoYSpacing);
    }

    /**
     * Returns the grid spacing for X-Axis.
     *
     * @return double
     */
    public double getXGridSpacing() {
        return chartPanel.getXGridSpacing();
    }

    /**
     * Sets the grid spacing for X-Axis.
     *
     * @param xSpacing
     */
    public void setXGridSpacing(double xSpacing) {
        chartPanel.setXGridSpacing(xSpacing);
    }

    /**
     * Returns the grid spacing for Y-Axis.
     *
     * @return double
     */
    public double getYGridSpacing() {
        return chartPanel.getYGridSpacing();
    }

    /**
     * Sets the grid spacing for Y-Axis.
     *
     * @param ySpacing
     */
    public void setYGridSpacing(double ySpacing) {
        chartPanel.setYGridSpacing(ySpacing);
    }

    /**
     * Returns the current background color
     *
     * @return bgColor
     */
    public Color getBGColor() {
        return bgColor;
    }

    /**
     * Sets the current background color
     *
     * @param bgColor
     */
    public void setBGColor(Color bgColor) {
        this.bgColor = bgColor;

        this.setBackground(bgColor);
        title.setBackground(bgColor);
        subTitle.setBackground(bgColor);
        xLabel.setBackground(bgColor);
        yLabel.setBackground(bgColor);
        chartPanel.setBGColor(bgColor);
    }

    /**
     * Returns the current border color
     *
     * @return borderColor
     */
    public Color getBorderColor() {
        return chartPanel.getBorderColor();
    }

    /**
     * Sets the current border color
     *
     * @param borderColor
     */
    public void setBorderColor(Color borderColor) {
        chartPanel.setBorderColor(borderColor);
    }

    /**
     * Returns the main title text
     *
     * @return String
     */
    public String getTitle() {
        return title.getText();
    }

    /**
     * Sets the main title text
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title.setText(title);
    }

    /**
     * Returns the sub-title text
     *
     * @return String
     */
    public String getSubTitle() {
        return subTitle.getText();
    }

    /**
     * Sets the sub-title text
     *
     * @param subTitle
     */
    public void setSubTitle(String subTitle) {
        this.subTitle.setText(subTitle);
    }

    /**
     * Returns the main title font color
     *
     * @return color
     */
    public Color getTitleColor() {
        return titleColor;
    }

    /**
     * Sets the main title font color
     *
     * @param titleColor
     */
    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;

        title.setForeground(titleColor);
    }

    /**
     * Returns the sub-title font color
     *
     * @return color
     */
    public Color getSubTitleColor() {
        return subTitleColor;
    }

    /**
     * Sets the sub-title font color
     *
     * @param subTitleColor
     */
    public void setSubTitleColor(Color subTitleColor) {
        this.subTitleColor = subTitleColor;

        subTitle.setForeground(subTitleColor);
    }

    /**
     * Returns the main title font
     *
     * @return font
     */
    public Font getTitleFont() {
        return titleFont;
    }

    /**
     * Sets the main title font
     *
     * @param titleFont
     */
    public void setTitleFont(Font titleFont) {
        this.titleFont = titleFont;

        title.setFont(titleFont);
    }

    /**
     * Returns the sub-title font
     *
     * @return font
     */
    public Font getSubTitleFont() {
        return subTitleFont;
    }

    /**
     * Sets the sub-title font
     *
     * @param subTitleFont
     */
    public void setSubTitleFont(Font subTitleFont) {
        this.subTitleFont = subTitleFont;

        subTitle.setFont(subTitleFont);
    }

    /**
     * Returns the X-Axis label text
     *
     * @return String
     */
    public String getXAxisLabel() {
        return xLabel.getText();
    }

    /**
     * Sets the X-Axis label text
     *
     * @param xLabel
     */
    public void setXAxisLabel(String xLabel) {
        this.xLabel.setText(xLabel);
    }

    /**
     * Returns the Y-Axis label text
     *
     * @return String
     */
    public String getYAxisLabel() {
        return yLabel.getText();
    }

    /**
     * Sets the Y-Axis label text
     *
     * @param yLabel
     */
    public void setYAxisLabel(String yLabel) {
        this.yLabel.setText(yLabel);
    }

    /**
     * Returns the Axes label color
     *
     * @return Color
     */
    public Color getAxisLabelColor() {
        return axisLabelColor;
    }

    /**
     * Sets the Axes label color
     *
     * @param axisLabelColor
     */
    public void setAxisLabelColor(Color axisLabelColor) {
        this.axisLabelColor = axisLabelColor;

        xLabel.setForeground(axisLabelColor);
        yLabel.setForeground(axisLabelColor);
    }

    /**
     * Returns the Axes label font
     *
     * @return font
     */
    public Font getAxisLabelFont() {
        return axisLabelFont;
    }

    /**
     * Sets the Axes label font
     *
     * @param axisLabelFont
     */
    public void setAxisLabelFont(Font axisLabelFont) {
        this.axisLabelFont = axisLabelFont;

        xLabel.setFont(axisLabelFont);
        yLabel.setFont(axisLabelFont);
    }

    /**
     * Returns the current list of datasets
     *
     * @return ArrayList
     */
    public ArrayList getDatasets() {
        return chartPanel.getDatasets();
    }

    /**
     * Adds the given dataset to the current list of datasets
     *
     * @param dataset
     */
    public void addDataset(Dataset dataset) {
        chartPanel.addDataset(dataset);
    }

    /**
     * Highlights the given point by means of adding dashed line
     * projections on the given axes.
     *
     * @param point - the point to be highlighted
     * @param xAxis - can be AXIS_TOP, AXIS_BOTTOM or AXIS_NONE
     * @param yAxis - can be AXIS_LEFT, AXIS_RIGHT or AXIS_NONE
     */
    public void hilitePoint(Point2D.Double point, int xAxis, int yAxis) {
        chartPanel.hilitePoint(point, xAxis, yAxis);
    }

    /**
     * Returns a buffered image of the current chart.
     *
     * @return DOCUMENT ME!
     */
    public BufferedImage getChartImage() {
        Image image = null;
        Graphics g = null;

        image = createImage(getWidth(), getHeight());
        g = image.getGraphics();
        paint(g);

        return (BufferedImage) image;
    }

    /**
     * The panel class which holds the actual graph plot. The parent
     * panel includes this panel in addition to the labels, etc. for the the
     * axes, title.
     */
    class ChartPanel extends JPanel {
        /**
         * DOCUMENT ME!
         */
        private final int X_PAD = 50;

        /**
         * DOCUMENT ME!
         */
        private final int Y_PAD = 20;

        /**
         * DOCUMENT ME!
         */
        private final int TEXT_PAD = 4;

        /**
         * DOCUMENT ME!
         */
        private boolean startXAxisAtZero = false;

        /**
         * DOCUMENT ME!
         */
        private boolean startYAxisAtZero = false;

        /**
         * DOCUMENT ME!
         */
        private boolean showGrid = true;

        /**
         * DOCUMENT ME!
         */
        private boolean autoXGridSpacing = false;

        /**
         * DOCUMENT ME!
         */
        private boolean autoYGridSpacing = false;

        /**
         * DOCUMENT ME!
         */
        private double xGridSpacing = 0;

        /**
         * DOCUMENT ME!
         */
        private double yGridSpacing = 0;

        /**
         * DOCUMENT ME!
         */
        private Color gridColor = new Color(50, 50, 50);

        /**
         * DOCUMENT ME!
         */
        private Color backColor = Color.black;

        /**
         * DOCUMENT ME!
         */
        private Color borderColor = Color.gray;

        /**
         * DOCUMENT ME!
         */
        private ArrayList datasets = new ArrayList();

        /**
         * DOCUMENT ME!
         */
        private ArrayList hilitePoints = new ArrayList();

        /**
         * DOCUMENT ME!
         */
        private Stroke dashStroke = new BasicStroke(1, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER, 10, new float[] { 5 }, 0);

        /**
         * DOCUMENT ME!
         */
        private GraphBounds bounds = new GraphBounds();

/**
         * Default Constructor
         */
        public ChartPanel() {
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isShowGrid() {
            return showGrid;
        }

        /**
         * DOCUMENT ME!
         *
         * @param showGrid DOCUMENT ME!
         */
        public void setShowGrid(boolean showGrid) {
            this.showGrid = showGrid;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isAutoXGridSpacing() {
            return autoXGridSpacing;
        }

        /**
         * DOCUMENT ME!
         *
         * @param autoXSpacing DOCUMENT ME!
         */
        public void setAutoXGridSpacing(boolean autoXSpacing) {
            autoXGridSpacing = autoXSpacing;

            if (autoXGridSpacing) {
                repaint();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isAutoYGridSpacing() {
            return autoYGridSpacing;
        }

        /**
         * DOCUMENT ME!
         *
         * @param autoYSpacing DOCUMENT ME!
         */
        public void setAutoYGridSpacing(boolean autoYSpacing) {
            autoYGridSpacing = autoYSpacing;

            if (autoYGridSpacing) {
                repaint();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double getXGridSpacing() {
            return xGridSpacing;
        }

        /**
         * DOCUMENT ME!
         *
         * @param xSpacing DOCUMENT ME!
         */
        public void setXGridSpacing(double xSpacing) {
            xGridSpacing = xSpacing;

            if (xGridSpacing <= 0) {
                setAutoXGridSpacing(true);
            } else {
                repaint();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double getYGridSpacing() {
            return yGridSpacing;
        }

        /**
         * DOCUMENT ME!
         *
         * @param ySpacing DOCUMENT ME!
         */
        public void setYGridSpacing(double ySpacing) {
            yGridSpacing = ySpacing;

            if (yGridSpacing <= 0) {
                setAutoYGridSpacing(true);
            } else {
                repaint();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ArrayList getDatasets() {
            return datasets;
        }

        /**
         * DOCUMENT ME!
         *
         * @param dataset DOCUMENT ME!
         */
        public void addDataset(Dataset dataset) {
            if (datasets.size() == 0) {
                bounds.minX = dataset.getMinX();
                bounds.minY = dataset.getMinY();
                bounds.maxX = dataset.getMaxX();
                bounds.maxY = dataset.getMaxY();
            } else {
                if (dataset.getMinX() < bounds.minX) {
                    bounds.minX = dataset.getMinX();
                }

                if (dataset.getMaxX() > bounds.maxX) {
                    bounds.maxX = dataset.getMaxX();
                }

                if (dataset.getMinY() < bounds.minY) {
                    bounds.minY = dataset.getMinY();
                }

                if (dataset.getMaxY() > bounds.maxY) {
                    bounds.maxY = dataset.getMaxY();
                }
            }

            // Round off the boundaries to nearest number
            // based upon the grid spacing
            roundOffBounds();
            datasets.add(dataset);
        }

        /**
         * DOCUMENT ME!
         *
         * @param bgColor DOCUMENT ME!
         */
        public void setBGColor(Color bgColor) {
            backColor = bgColor;
            repaint();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Color getBorderColor() {
            return borderColor;
        }

        /**
         * DOCUMENT ME!
         *
         * @param borderColor DOCUMENT ME!
         */
        public void setBorderColor(Color borderColor) {
            this.borderColor = borderColor;
            repaint();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private int getDrawableWidth() {
            return getWidth() - (2 * X_PAD);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private int getDrawableHeight() {
            return getHeight() - (2 * Y_PAD);
        }

        /**
         * DOCUMENT ME!
         *
         * @param point DOCUMENT ME!
         * @param xAxis DOCUMENT ME!
         * @param yAxis DOCUMENT ME!
         */
        public void hilitePoint(Point2D.Double point, int xAxis, int yAxis) {
            HilitePoint hlPoint = new HilitePoint();
            hlPoint.point = point;
            hlPoint.xAxis = xAxis;
            hlPoint.yAxis = yAxis;

            hilitePoints.add(hlPoint);
            repaint();
        }

        /**
         * This method paints the full graph area. It is called
         * through <code>repaint()</code> method whenever there is a change in
         * any of the properties of the chart.
         *
         * @param gNorm DOCUMENT ME!
         */
        public void paint(Graphics gNorm) {
            Graphics2D g = (Graphics2D) gNorm;

            // Paint the background
            g.setBackground(bgColor);
            g.setColor(backColor);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Paint the grid
            drawGrid(g);

            // Paint the chart border
            g.setColor(borderColor);
            g.drawRect(X_PAD, Y_PAD, getDrawableWidth(), getDrawableHeight());

            // Plot highlighted points
            plotHilitePoints(g);

            // Plot the actual data
            plotDatasets(g);
        }

        /**
         * Method to plot the grid.
         *
         * @param g DOCUMENT ME!
         */
        private void drawGrid(Graphics2D g) {
            Point2D.Double start = null;
            Point2D.Double end = null;
            double val = 0;

            g.setFont(axisValueFont);

            if (!showGrid) {
                g.setColor(axisValueColor);

                start = convertScale(new Point2D.Double(bounds.minX, bounds.minY));
                drawText(g, decimalFormat.format(bounds.minX), start.getX(),
                    start.getY() + TEXT_PAD, HALIGN_CENTER, VALIGN_BELOW);

                start = convertScale(new Point2D.Double(bounds.maxX, bounds.minY));
                drawText(g, decimalFormat.format(bounds.maxX), start.getX(),
                    start.getY() + TEXT_PAD, HALIGN_CENTER, VALIGN_BELOW);

                start = convertScale(new Point2D.Double(bounds.minX, bounds.minY));
                drawText(g, decimalFormat.format(bounds.minY),
                    start.getX() - TEXT_PAD, start.getY(), HALIGN_LEFT,
                    VALIGN_MIDDLE);

                start = convertScale(new Point2D.Double(bounds.minX, bounds.maxY));
                drawText(g, decimalFormat.format(bounds.maxY),
                    start.getX() - TEXT_PAD, start.getY(), HALIGN_LEFT,
                    VALIGN_MIDDLE);

                return;
            }

            val = bounds.minX;

            while (val <= bounds.maxX) {
                start = convertScale(new Point2D.Double(val, bounds.minY));
                end = convertScale(new Point2D.Double(val, bounds.maxY));

                g.setColor(gridColor);
                drawLine(g, start, end);

                g.setColor(axisValueColor);
                drawText(g, decimalFormat.format(val), start.getX(),
                    start.getY() + TEXT_PAD, HALIGN_CENTER, VALIGN_BELOW);

                val += xGridSpacing;
            }

            val = bounds.minY;

            while (val <= bounds.maxY) {
                start = convertScale(new Point2D.Double(bounds.minX, val));
                end = convertScale(new Point2D.Double(bounds.maxX, val));

                g.setColor(gridColor);
                drawLine(g, start, end);

                g.setColor(axisValueColor);
                drawText(g, decimalFormat.format(val), start.getX() - TEXT_PAD,
                    start.getY(), HALIGN_LEFT, VALIGN_MIDDLE);

                val += yGridSpacing;
            }
        }

        /**
         * Method to plot the actual datasets.
         *
         * @param g DOCUMENT ME!
         */
        private void plotDatasets(Graphics2D g) {
            int i = 0;
            int index = 0;
            Dataset dataset = null;
            Point2D.Double point = null;
            Point2D.Double prevPoint = null;

            for (index = 0; index < datasets.size(); index++) {
                prevPoint = null;
                dataset = (Dataset) datasets.get(index);
                g.setColor(dataset.getLineColor());

                for (i = 0; i < dataset.getNumOfPoints(); i++) {
                    point = convertScale(dataset.getValue(i));

                    if (prevPoint != null) {
                        drawLine(g, prevPoint, point);
                    }

                    prevPoint = point;
                }
            }
        }

        /**
         * Method to plot the highlighted points.
         *
         * @param g DOCUMENT ME!
         */
        private void plotHilitePoints(Graphics2D g) {
            int i = 0;
            int width = getDrawableWidth();
            int height = getDrawableHeight();
            HilitePoint hlPoint = null;
            Point2D.Double point = null;
            Point2D.Double start = null;
            Point2D.Double end = null;
            Stroke origStroke = g.getStroke();

            g.setStroke(dashStroke);
            g.setFont(axisValueFont);

            for (i = 0; i < hilitePoints.size(); i++) {
                hlPoint = (HilitePoint) hilitePoints.get(i);
                point = hlPoint.point;
                end = convertScale(point);

                if (hlPoint.xAxis == AXIS_TOP) {
                    plotPointValue(g, decimalFormat.format(point.getX()), end,
                        end.getX(), Y_PAD, 0, -TEXT_PAD, HALIGN_CENTER,
                        VALIGN_ABOVE);
                } else if (hlPoint.xAxis == AXIS_BOTTOM) {
                    plotPointValue(g, decimalFormat.format(point.getX()), end,
                        end.getX(), height + Y_PAD, 0, TEXT_PAD, HALIGN_CENTER,
                        VALIGN_BELOW);
                } else if (hlPoint.xAxis == AXIS_NONE) {
                }

                if (hlPoint.yAxis == AXIS_LEFT) {
                    plotPointValue(g, decimalFormat.format(point.getY()), end,
                        X_PAD, end.getY(), -TEXT_PAD, 0, HALIGN_LEFT,
                        VALIGN_MIDDLE);
                } else if (hlPoint.yAxis == AXIS_RIGHT) {
                    plotPointValue(g, decimalFormat.format(point.getY()), end,
                        width + X_PAD, end.getY(), TEXT_PAD, 0, HALIGN_RIGHT,
                        VALIGN_MIDDLE);
                } else if (hlPoint.xAxis == AXIS_NONE) {
                }
            }

            g.setStroke(origStroke);
        }

        /**
         * Method to round off the boundaries according to the grid
         * spacing.
         */
        private void roundOffBounds() {
            if (autoXGridSpacing) {
                xGridSpacing = calcGridSpacing(bounds.minX, bounds.maxX);
            }

            if (autoYGridSpacing) {
                yGridSpacing = calcGridSpacing(bounds.minY, bounds.maxY);
            }

            bounds.minX = bounds.minX - (bounds.minX % xGridSpacing);
            bounds.minY = bounds.minY - (bounds.minY % yGridSpacing);

            // We don't like the plot almost touching the top of the area.
            // If it does then readjust the top Y bounds.
            if ((bounds.maxY % yGridSpacing) > (0.75 * yGridSpacing)) {
                bounds.maxY = bounds.maxY + yGridSpacing;
            }

            if ((bounds.maxX % xGridSpacing) > EPSILON) {
                bounds.maxX = bounds.maxX - (bounds.maxX % xGridSpacing) +
                    xGridSpacing;
            } else {
                bounds.maxX = bounds.maxX - (bounds.maxX % xGridSpacing);
            }

            if ((bounds.maxY % yGridSpacing) > EPSILON) {
                bounds.maxY = bounds.maxY - (bounds.maxY % yGridSpacing) +
                    yGridSpacing;
            } else {
                bounds.maxY = bounds.maxY - (bounds.maxY % yGridSpacing);
            }
        }

        /**
         * Helper method for automatic calculation of grid spacing.
         *
         * @param min DOCUMENT ME!
         * @param max DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private double calcGridSpacing(double min, double max) {
            final int AVG_SECTIONS = 8;
            double power = 0;
            double gridSpacing = 0;

            gridSpacing = (max - min) / (AVG_SECTIONS - 1);
            gridSpacing = Math.log(gridSpacing) / Math.log(10);

            // Calculate the exponential power for log
            if (gridSpacing < 0) {
                power = (int) gridSpacing - 1;
            } else {
                power = (int) gridSpacing;
            }

            gridSpacing = gridSpacing - power;
            gridSpacing = Math.round(Math.pow(10, gridSpacing));

            if (gridSpacing < 1.5) {
                gridSpacing = 1;
            } else if (gridSpacing < 3.5) {
                gridSpacing = 2;
            } else if (gridSpacing < 7.5) {
                gridSpacing = 5;
            } else {
                gridSpacing = 10;
            }

            gridSpacing = gridSpacing * Math.pow(10, power);

            return gridSpacing;
        }

        /**
         * Helper method for plotting highlight points.
         *
         * @param g DOCUMENT ME!
         * @param text DOCUMENT ME!
         * @param source DOCUMENT ME!
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         * @param xPad DOCUMENT ME!
         * @param yPad DOCUMENT ME!
         * @param hAlign DOCUMENT ME!
         * @param vAlign DOCUMENT ME!
         */
        private void plotPointValue(Graphics2D g, String text,
            Point2D.Double source, double x, double y, double xPad,
            double yPad, int hAlign, int vAlign) {
            g.setColor(borderColor);
            drawLine(g, new Point2D.Double(x, y), source);

            g.setColor(hlFontColor);
            drawText(g, text, x + xPad, y + yPad, hAlign, vAlign);
        }

        /**
         * Helper method to draw lines.
         *
         * @param g DOCUMENT ME!
         * @param start DOCUMENT ME!
         * @param end DOCUMENT ME!
         */
        private void drawLine(Graphics2D g, Point2D.Double start,
            Point2D.Double end) {
            g.drawLine((int) start.getX(), (int) start.getY(),
                (int) end.getX(), (int) end.getY());
        }

        /**
         * Helper method to convert actual values to graph scales.
         *
         * @param point DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private Point2D.Double convertScale(Point2D.Double point) {
            int width = getDrawableWidth();
            int height = getDrawableHeight();

            double minX = startXAxisAtZero ? 0 : bounds.minX;
            double minY = startYAxisAtZero ? 0 : bounds.minY;

            double x = X_PAD +
                ((point.getX() - minX) / (bounds.maxX - minX) * width);
            double y = (height + Y_PAD) -
                ((point.getY() - minY) / (bounds.maxY - minY) * height);

            return new Point2D.Double(x, y);
        }

        /**
         * Helper method to draw strings on the graph.
         *
         * @param g DOCUMENT ME!
         * @param text DOCUMENT ME!
         * @param x DOCUMENT ME!
         * @param y DOCUMENT ME!
         * @param hAlign DOCUMENT ME!
         * @param vAlign DOCUMENT ME!
         */
        private void drawText(Graphics2D g, String text, double x, double y,
            int hAlign, int vAlign) {
            GlyphVector glyph = null;
            Rectangle2D glyphBounds = null;
            Font font = g.getFont();

            glyph = font.createGlyphVector(g.getFontRenderContext(), text);
            glyphBounds = glyph.getLogicalBounds();

            switch (hAlign) {
            case HALIGN_LEFT:
                x = x - glyphBounds.getWidth();

                break;

            case HALIGN_CENTER:
                x = x - (glyphBounds.getWidth() / 2);

                break;

            case HALIGN_RIGHT:
                break;
            }

            switch (vAlign) {
            case VALIGN_ABOVE:
                break;

            case VALIGN_MIDDLE:
                y = y + (glyphBounds.getHeight() / 2);

                break;

            case VALIGN_BELOW:
                y = y + glyphBounds.getHeight();

                break;
            }

            g.drawGlyphVector(glyph, (float) x, (float) y);
        }
    }

    /**
     * Data class to hold boundaries of graph.
     */
    class GraphBounds {
        /**
         * DOCUMENT ME!
         */
        public double minX = +1E10;

        /**
         * DOCUMENT ME!
         */
        public double maxX = -1E10;

        /**
         * DOCUMENT ME!
         */
        public double minY = +1E10;

        /**
         * DOCUMENT ME!
         */
        public double maxY = -1E10;
    }

    /**
     * Data class to hold values for highlight points.
     */
    class HilitePoint {
        /**
         * DOCUMENT ME!
         */
        public Point2D.Double point = null;

        /**
         * DOCUMENT ME!
         */
        public int xAxis = AXIS_BOTTOM;

        /**
         * DOCUMENT ME!
         */
        public int yAxis = AXIS_LEFT;
    }
}
