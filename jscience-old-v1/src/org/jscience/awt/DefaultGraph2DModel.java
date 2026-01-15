package org.jscience.awt;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;


/**
 * The DefaultGraph2DModel class provides a default implementation of the
 * Graph2DModel interface.
 *
 * @author Mark Hale
 * @version 1.2
 */
public final class DefaultGraph2DModel extends AbstractGraphModel
    implements Graph2DModel, TableModelListener {
    /** DOCUMENT ME! */
    private static final int X_AXIS_COLUMN = 0;

    /** DOCUMENT ME! */
    private static final int SERIES_COLUMN = 1;

    /** DOCUMENT ME! */
    private float[] defaultXAxis = new float[0];

    /** DOCUMENT ME! */
    private final List series = new ArrayList();

    /** DOCUMENT ME! */
    private int pos = 0;

    /** DOCUMENT ME! */
    private DataSeries curSeries = null;

/**
     * Creates a new DefaultGraph2DModel object.
     */
    public DefaultGraph2DModel() {
    }

    /**
     * Sets the default x-axis values. A copy of the values is made.
     *
     * @param x DOCUMENT ME!
     */
    public void setXAxis(float[] x) {
        if (defaultXAxis.length != x.length) {
            defaultXAxis = new float[x.length];
        }

        System.arraycopy(x, 0, defaultXAxis, 0, x.length);
    }

    /**
     * Sets the default x-axis values. A copy of the values is made.
     *
     * @param x DOCUMENT ME!
     */
    public void setXAxis(double[] x) {
        if (defaultXAxis.length != x.length) {
            defaultXAxis = new float[x.length];
        }

        for (int i = 0; i < x.length; i++)
            defaultXAxis[i] = (float) x[i];
    }

    /**
     * Sets the default x-axis values.
     *
     * @param a start of interval.
     * @param b end of interval.
     * @param n number of values.
     */
    public void setXAxis(float a, float b, int n) {
        if (defaultXAxis.length != n) {
            defaultXAxis = new float[n];
        }

        final float scale = (b - a) / (n - 1);

        for (int i = 0; i < n; i++)
            defaultXAxis[i] = (scale * i) + a;
    }

    /**
     * Adds a data series for the default x-axis values.
     *
     * @param newSeries DOCUMENT ME!
     */
    public void addSeries(float[] newSeries) {
        addSeries(new DataSeries(defaultXAxis, newSeries));
    }

    /**
     * Adds a data series for the default x-axis values.
     *
     * @param newSeries DOCUMENT ME!
     */
    public void addSeries(double[] newSeries) {
        addSeries(new DataSeries(defaultXAxis, newSeries));
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
     * @param newXAxis DOCUMENT ME!
     * @param newSeries DOCUMENT ME!
     */
    public void addSeries(float[] newXAxis, float[] newSeries) {
        addSeries(new DataSeries(newXAxis, newSeries));
    }

    /**
     * Adds a data series. Convenience method.
     *
     * @param newXAxis DOCUMENT ME!
     * @param newSeries DOCUMENT ME!
     */
    public void addSeries(float[] newXAxis, double[] newSeries) {
        addSeries(new DataSeries(newXAxis, newSeries));
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
     * Changes a data series. Convenience method.
     *
     * @param i DOCUMENT ME!
     * @param newSeries DOCUMENT ME!
     */
    public void changeSeries(int i, double[] newSeries) {
        getSeries(i).setValues(newSeries);
    }

    /**
     * Remove a data series.
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
        } else {
            fireGraphDataChanged();
        }
    }

    // Graph2DModel interface
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getXCoord(int i) {
        return curSeries.getXCoord(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getYCoord(int i) {
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
         */
        public DataSeries(float[] xValues, float[] yValues) {
            setXAxis(xValues);
            setValues(yValues);
        }

/**
         * Creates a new DataSeries object.
         *
         * @param xValues DOCUMENT ME!
         * @param yValues DOCUMENT ME!
         */
        public DataSeries(float[] xValues, double[] yValues) {
            setXAxis(xValues);
            setValues(yValues);
        }

/**
         * Creates a new DataSeries object.
         *
         * @param xValues DOCUMENT ME!
         * @param yValues DOCUMENT ME!
         */
        public DataSeries(double[] xValues, double[] yValues) {
            setXAxis(xValues);
            setValues(yValues);
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
        public void setValues(float[] yValues) {
            if (series.length != yValues.length) {
                series = new float[yValues.length];
            }

            System.arraycopy(yValues, 0, series, 0, yValues.length);
            fireTableColumnUpdated(SERIES_COLUMN);
        }

        /**
         * DOCUMENT ME!
         *
         * @param yValues DOCUMENT ME!
         */
        public void setValues(double[] yValues) {
            if (series.length != yValues.length) {
                series = new float[yValues.length];
            }

            for (int i = 0; i < yValues.length; i++)
                series[i] = (float) yValues[i];

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
        public float getValue(int i) {
            return series[i];
        }

        /**
         * DOCUMENT ME!
         *
         * @param i DOCUMENT ME!
         * @param y DOCUMENT ME!
         */
        public void setValue(int i, float y) {
            series[i] = y;
            fireTableCellUpdated(i, SERIES_COLUMN);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int length() {
            return Math.min(xAxis.length, series.length);
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
            } else if (col == SERIES_COLUMN) {
                return "Y";
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
            return 2;
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
