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

package org.jscience.awt;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;


/**
 * The DefaultGraph3DModel class provides a default implementation of the
 * Graph3DModel interface.
 *
 * @author Mark Hale
 * @version 1.2
 */
public final class DefaultGraph3DModel extends AbstractGraphModel
    implements Graph3DModel, TableModelListener {
    /** DOCUMENT ME! */
    private static final int X_AXIS_COLUMN = 0;

    /** DOCUMENT ME! */
    private static final int Y_AXIS_COLUMN = 1;

    /** DOCUMENT ME! */
    private static final int SERIES_COLUMN = 2;

    /** DOCUMENT ME! */
    private float[] xAxis = new float[0];

    /** DOCUMENT ME! */
    private float[] yAxis = new float[0];

    /** DOCUMENT ME! */
    private final List series = new ArrayList();

    /** DOCUMENT ME! */
    private int pos = 0;

    /** DOCUMENT ME! */
    private DataSeries curSeries = null;

/**
     * Creates a new DefaultGraph3DModel object.
     */
    public DefaultGraph3DModel() {
    }

    /**
     * Sets the x-axis values. A copy of the values is made.
     *
     * @param x DOCUMENT ME!
     */
    public void setXAxis(float[] x) {
        if (xAxis.length != x.length) {
            xAxis = new float[x.length];
        }

        System.arraycopy(x, 0, xAxis, 0, x.length);
        fireGraphDataChanged();
    }

    /**
     * Sets the y-axis values. A copy of the values is made.
     *
     * @param y DOCUMENT ME!
     */
    public void setYAxis(float[] y) {
        if (yAxis.length != y.length) {
            yAxis = new float[y.length];
        }

        System.arraycopy(y, 0, yAxis, 0, y.length);
        fireGraphDataChanged();
    }

    /**
     * Adds a data series.
     *
     * @param newSeries DOCUMENT ME!
     */
    public void addSeries(DataSeries newSeries) {
        series.add(newSeries);
        fireGraphDataChanged();
        newSeries.addTableModelListener(this);
    }

    /**
     * Adds a data series. Convenience method.
     *
     * @param newSeries DOCUMENT ME!
     */
    public void addSeries(float[] newSeries) {
        addSeries(new DataSeries(xAxis, yAxis, newSeries));
    }

    /**
     * Changes a data series.
     *
     * @param i DOCUMENT ME!
     * @param newSeries DOCUMENT ME!
     */
    public void changeSeries(int i, DataSeries newSeries) {
        getSeries(i).removeTableModelListener(this);
        series.set(i, newSeries);
        fireGraphDataChanged();
        newSeries.addTableModelListener(this);
    }

    /**
     * Changes a data series. Convenience method.
     *
     * @param i DOCUMENT ME!
     * @param newSeries DOCUMENT ME!
     */
    public void changeSeries(int i, float[] newSeries) {
        getSeries(i).setValues(newSeries);
    }

    /**
     * Removes a data series.
     *
     * @param i DOCUMENT ME!
     */
    public void removeSeries(int i) {
        getSeries(i).removeTableModelListener(this);
        series.remove(i);
        fireGraphDataChanged();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DataSeries getSeries(int i) {
        return (DataSeries) series.get(i);
    }

    /**
     * Convenience method.
     *
     * @param i DOCUMENT ME!
     * @param flag DOCUMENT ME!
     */
    public void setSeriesVisible(int i, boolean flag) {
        getSeries(i).setVisible(flag);
    }

    /**
     * Implementation of TabelModelListener. Application code will not
     * use this method explicitly, it is used internally.
     *
     * @param evt DOCUMENT ME!
     */
    public void tableChanged(TableModelEvent evt) {
        if (evt.getColumn() == SERIES_COLUMN) {
            fireGraphSeriesUpdated(series.indexOf(evt.getSource()));
        }
    }

    // Graph3DModel interface
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getXCoord(int i) {
        return xAxis[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getYCoord(int i) {
        return yAxis[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getZCoord(int i) {
        return curSeries.getValue(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int seriesLength() {
        return curSeries.length();
    }

    /**
     * DOCUMENT ME!
     */
    public void firstSeries() {
        curSeries = getSeries(0);

        for (pos = 0; !curSeries.isVisible() && (pos < (series.size() - 1));)
            curSeries = getSeries(++pos);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean nextSeries() {
        if (pos == (series.size() - 1)) {
            return false;
        }

        do {
            curSeries = getSeries(++pos);
        } while (!curSeries.isVisible() && (pos < (series.size() - 1)));

        return curSeries.isVisible();
    }

    /**
     * The DataSeries class encapsulates a data series for a graph.
     */
    public static class DataSeries extends AbstractTableModel {
        /** DOCUMENT ME! */
        protected float[] xAxis = new float[0];

        /** DOCUMENT ME! */
        protected float[] yAxis = new float[0];

        /** DOCUMENT ME! */
        protected float[] series = new float[0];

        /** DOCUMENT ME! */
        private boolean isVis = true;

/**
         * Creates a new DataSeries object.
         */
        protected DataSeries() {
        }

/**
         * Creates a new DataSeries object.
         *
         * @param xValues DOCUMENT ME!
         * @param yValues DOCUMENT ME!
         * @param zValues DOCUMENT ME!
         */
        public DataSeries(float[] xValues, float[] yValues, float[] zValues) {
            setXAxis(xValues);
            setYAxis(yValues);
            setValues(zValues);
        }

/**
         * Creates a new DataSeries object.
         *
         * @param xValues DOCUMENT ME!
         * @param yValues DOCUMENT ME!
         * @param zValues DOCUMENT ME!
         */
        public DataSeries(float[] xValues, float[] yValues, double[] zValues) {
            setXAxis(xValues);
            setYAxis(yValues);
            setValues(zValues);
        }

/**
         * Creates a new DataSeries object.
         *
         * @param xValues DOCUMENT ME!
         * @param yValues DOCUMENT ME!
         * @param zValues DOCUMENT ME!
         */
        public DataSeries(double[] xValues, double[] yValues, double[] zValues) {
            setXAxis(xValues);
            setYAxis(yValues);
            setValues(zValues);
        }

        /**
         * DOCUMENT ME!
         *
         * @param xValues DOCUMENT ME!
         */
        public void setXAxis(float[] xValues) {
            if (xAxis.length != xValues.length) {
                xAxis = new float[xValues.length];
            }

            System.arraycopy(xValues, 0, xAxis, 0, xValues.length);
            fireTableColumnUpdated(X_AXIS_COLUMN);
        }

        /**
         * DOCUMENT ME!
         *
         * @param xValues DOCUMENT ME!
         */
        public void setXAxis(double[] xValues) {
            if (xAxis.length != xValues.length) {
                xAxis = new float[xValues.length];
            }

            for (int i = 0; i < xValues.length; i++)
                xAxis[i] = (float) xValues[i];

            fireTableColumnUpdated(X_AXIS_COLUMN);
        }

        /**
         * DOCUMENT ME!
         *
         * @param yValues DOCUMENT ME!
         */
        public void setYAxis(float[] yValues) {
            if (yAxis.length != yValues.length) {
                yAxis = new float[yValues.length];
            }

            System.arraycopy(yValues, 0, yAxis, 0, yValues.length);
            fireTableColumnUpdated(Y_AXIS_COLUMN);
        }

        /**
         * DOCUMENT ME!
         *
         * @param yValues DOCUMENT ME!
         */
        public void setYAxis(double[] yValues) {
            if (yAxis.length != yValues.length) {
                yAxis = new float[yValues.length];
            }

            for (int i = 0; i < yValues.length; i++)
                yAxis[i] = (float) yValues[i];

            fireTableColumnUpdated(Y_AXIS_COLUMN);
        }

        /**
         * DOCUMENT ME!
         *
         * @param zValues DOCUMENT ME!
         */
        public void setValues(float[] zValues) {
            if (series.length != zValues.length) {
                series = new float[zValues.length];
            }

            System.arraycopy(zValues, 0, series, 0, zValues.length);
            fireTableColumnUpdated(SERIES_COLUMN);
        }

        /**
         * DOCUMENT ME!
         *
         * @param zValues DOCUMENT ME!
         */
        public void setValues(double[] zValues) {
            if (series.length != zValues.length) {
                series = new float[zValues.length];
            }

            for (int i = 0; i < zValues.length; i++)
                series[i] = (float) zValues[i];

            fireTableColumnUpdated(SERIES_COLUMN);
        }

        /**
         * DOCUMENT ME!
         *
         * @param i DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getXCoord(int i) {
            return xAxis[i];
        }

        /**
         * DOCUMENT ME!
         *
         * @param i DOCUMENT ME!
         * @param x DOCUMENT ME!
         */
        public void setXCoord(int i, float x) {
            xAxis[i] = x;
            fireTableCellUpdated(i, X_AXIS_COLUMN);
        }

        /**
         * DOCUMENT ME!
         *
         * @param i DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getYCoord(int i) {
            return yAxis[i];
        }

        /**
         * DOCUMENT ME!
         *
         * @param i DOCUMENT ME!
         * @param y DOCUMENT ME!
         */
        public void setYCoord(int i, float y) {
            yAxis[i] = y;
            fireTableCellUpdated(i, Y_AXIS_COLUMN);
        }

        /**
         * DOCUMENT ME!
         *
         * @param i DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public float getValue(int i) {
            return series[i];
        }

        /**
         * DOCUMENT ME!
         *
         * @param i DOCUMENT ME!
         * @param z DOCUMENT ME!
         */
        public void setValue(int i, float z) {
            series[i] = z;
            fireTableCellUpdated(i, SERIES_COLUMN);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int length() {
            return Math.min(Math.min(xAxis.length, yAxis.length), series.length);
        }

        /**
         * DOCUMENT ME!
         *
         * @param flag DOCUMENT ME!
         */
        public final void setVisible(boolean flag) {
            if (flag != isVis) {
                isVis = flag;
                fireTableDataChanged();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public final boolean isVisible() {
            return isVis;
        }

        /**
         * DOCUMENT ME!
         *
         * @param column DOCUMENT ME!
         */
        private void fireTableColumnUpdated(int column) {
            if (column == X_AXIS_COLUMN) {
                fireTableChanged(new TableModelEvent(this, 0, xAxis.length - 1,
                        column));
            } else if (column == Y_AXIS_COLUMN) {
                fireTableChanged(new TableModelEvent(this, 0, yAxis.length - 1,
                        column));
            } else if (column == SERIES_COLUMN) {
                fireTableChanged(new TableModelEvent(this, 0,
                        series.length - 1, column));
            }
        }

        // TableModelInterface
        /**
         * DOCUMENT ME!
         *
         * @param col DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getColumnName(int col) {
            if (col == X_AXIS_COLUMN) {
                return "X";
            } else if (col == Y_AXIS_COLUMN) {
                return "Y";
            } else if (col == SERIES_COLUMN) {
                return "Z";
            } else {
                return null;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param col DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Class getColumnClass(int col) {
            return Float.class;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public final int getRowCount() {
            return length();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public final int getColumnCount() {
            return 3;
        }

        /**
         * DOCUMENT ME!
         *
         * @param row DOCUMENT ME!
         * @param col DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object getValueAt(int row, int col) {
            if (col == X_AXIS_COLUMN) {
                return new Float(getXCoord(row));
            } else if (col == Y_AXIS_COLUMN) {
                return new Float(getYCoord(row));
            } else if (col == SERIES_COLUMN) {
                return new Float(getValue(row));
            } else {
                return null;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param value DOCUMENT ME!
         * @param row DOCUMENT ME!
         * @param col DOCUMENT ME!
         */
        public void setValueAt(Object value, int row, int col) {
            if (col == X_AXIS_COLUMN) {
                setXCoord(row, ((Float) value).floatValue());
            } else if (col == Y_AXIS_COLUMN) {
                setYCoord(row, ((Float) value).floatValue());
            } else if (col == SERIES_COLUMN) {
                setValue(row, ((Float) value).floatValue());
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param row DOCUMENT ME!
         * @param col DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isCellEditable(int row, int col) {
            return true;
        }
    }
}
