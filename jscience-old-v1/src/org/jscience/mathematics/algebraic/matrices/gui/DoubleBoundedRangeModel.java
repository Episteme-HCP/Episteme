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

package org.jscience.mathematics.algebraic.matrices.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;


/**
 * An implementation of BoundedRangeModel providing the storage of double
 * values.
 */
public class DoubleBoundedRangeModel implements BoundedRangeModel {
    /** DOCUMENT ME! */
    private double min;

    /** DOCUMENT ME! */
    private double max;

    /** DOCUMENT ME! */
    private double value;

    /** DOCUMENT ME! */
    private double divider;

    /** DOCUMENT ME! */
    private boolean isAdjusting;

    //---------------------------------------------------------------------------
    /** DOCUMENT ME! */
    EventListenerList listenerList = new EventListenerList();

    /** DOCUMENT ME! */
    ChangeEvent changeEvent = null;

/**
     * Constructs a new DoubleBoundedRangeModel with a default minimum of -1.0
     * and maximum of 1.0.
     */
    public DoubleBoundedRangeModel() {
        min = -1.;
        max = 1.;
        updateRange();
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     */
    public void setDoubleMinimum(double min) {
        if (this.min == min) {
            return;
        } else if (min > this.min) {
            if (min > max) {
                max = min;
            }

            if (min > value) {
                value = min;
            }
        }

        this.min = min;

        updateRange();

        fireChangeEvent();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDoubleMinimum() {
        return min;
    }

    /**
     * DOCUMENT ME!
     *
     * @param max DOCUMENT ME!
     */
    public void setDoubleMaximum(double max) {
        if (this.max == max) {
            return;
        }

        if (max < this.max) {
            if (max < min) {
                min = max;
            }

            if (max < value) {
                value = max;
            }
        }

        this.max = max;

        updateRange();

        fireChangeEvent();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDoubleMaximum() {
        return max;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newValue DOCUMENT ME!
     */
    public void setDoubleValue(double newValue) {
        if (value == newValue) {
            return;
        }

        value = newValue;

        boolean rangeChanged = false;

        if (value < min) {
            min = value;
            rangeChanged = true;
        } else if (value > max) {
            max = value;
            rangeChanged = true;
        } else if (!isAdjusting) {
            if (value == min) {
                min = relativeMin(value);
                rangeChanged = true;
            } else if (value == max) {
                max = relativeMax(value);
                rangeChanged = true;
            }
        }

        if (rangeChanged) {
            updateRange();
        }

        fireChangeEvent();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDoubleValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     */
    private void updateRange() {
        divider = (max - min) / Integer.MAX_VALUE;
    }

    /**
     * Returns the largest decimal power that is still less than the
     * argument.
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double relativeMin(double value) {
        if (value == 0) {
            return -1;
        }

        double digits = Math.ceil(Math.log(Math.abs(value)) / Math.log(10));

        if (value < 0) {
            if (value > -1) {
                return -1;
            }

            if (value == -Math.pow(10., digits)) {
                return -Math.pow(10., digits + 1);
            }

            return -Math.pow(10., digits);
        } else {
            if (value <= 1) {
                return 0;
            }

            return Math.pow(10., digits - 1);
        }
    }

    /**
     * Returns the smallest decimal power that is still greater than
     * the argument.
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double relativeMax(double value) {
        if (value == 0) {
            return 1;
        }

        double digits = Math.ceil(Math.log(Math.abs(value)) / Math.log(10));

        if (value < 0) {
            if (value >= -1) {
                return 0;
            }

            return -Math.pow(10., digits - 1);
        } else {
            if (value < 1) {
                return 1;
            }

            if (value == Math.pow(10., digits)) {
                return Math.pow(10., digits + 1);
            }

            return Math.pow(10., digits);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return <code>1</code>
     */
    public int getExtent() {
        return 1;
    }

    /**
     * Returns the biggest integer value representing every possible
     * double maximum as an integer.
     *
     * @return <code>Integer.MAX_VALUE</code>
     */
    public int getMaximum() {
        return Integer.MAX_VALUE;
    }

    /**
     * Returns the integer value 0 representing every possible double
     * minimum as an integer.
     *
     * @return <code>0</code>
     */
    public int getMinimum() {
        return 0;
    }

    /**
     * Returns the relativ int value of the current double value.
     *
     * @return DOCUMENT ME!
     */
    public int getValue() {
        return (int) ((value - min) / divider);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getValueIsAdjusting() {
        return isAdjusting;
    }

    /**
     * An empty method, because getExtend() returns a constant value.
     *
     * @param newExtent DOCUMENT ME!
     */
    public void setExtent(int newExtent) {
    }

    /**
     * An empty method, because getMaximum() returns a constant value.
     *
     * @param newMaximum DOCUMENT ME!
     */
    public void setMaximum(int newMaximum) {
    }

    /**
     * An empty method, because getMinimum() returns a constant value.
     *
     * @param newMinimum DOCUMENT ME!
     */
    public void setMinimum(int newMinimum) {
    }

    /**
     * Calculates the double value, the specified int value is
     * representing.
     *
     * @param newValue DOCUMENT ME!
     */
    public void setValue(int newValue) {
        setDoubleValue((newValue * divider) + min);
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void setValueIsAdjusting(boolean b) {
        boolean fireLater = false;

        if (b != isAdjusting) {
            isAdjusting = b;

            if (!isAdjusting) {
                if (value == min) {
                    min = relativeMin(value);
                    fireLater = true;
                } else if (value == max) {
                    fireLater = true;
                    max = relativeMax(value);
                }

                if (fireLater) {
                    updateRange();
                }
            }
        }

        // required for JSlider (at least in JDK1.3.1)
        if (fireLater) {
            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        fireChangeEvent();
                    }
                });
        } else {
            fireChangeEvent();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param newValue DOCUMENT ME!
     * @param extent DOCUMENT ME!
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     * @param adjusting DOCUMENT ME!
     */
    public void setRangeProperties(int newValue, int extent, int min, int max,
        boolean adjusting) {
        boolean valueChanged = (value != newValue);
        boolean adjustingChanged = (isAdjusting != adjusting);
        value = newValue;
        isAdjusting = adjusting;

        if (valueChanged || adjustingChanged) {
            fireChangeEvent();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /**
     * DOCUMENT ME!
     */
    public void fireChangeEvent() {
        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2)
            if (listeners[i] == ChangeListener.class) //in diesem Fall immer so!
             {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }

                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
    }
}
