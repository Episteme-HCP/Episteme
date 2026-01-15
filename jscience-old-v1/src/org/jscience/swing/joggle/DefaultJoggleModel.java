//JTEM - Java Tools for Experimental Mathematics
//Copyright (C) 2001 JTEM-Group
//
//This program is free software; you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation; either version 2 of the License, or
//any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program; if not, write to the Free Software
//Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
package org.jscience.swing.joggle;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;


/**
 * A default implementation of the {@link JoggleModel} interface.
 *
 * @author marcel
 */
public class DefaultJoggleModel implements JoggleModel {
    /** DOCUMENT ME! */
    EventListenerList listenerList = new EventListenerList();

    /** DOCUMENT ME! */
    ChangeEvent changeEvent = null;

    /** DOCUMENT ME! */
    private double maximum;

    /** DOCUMENT ME! */
    private double minimum;

    /** DOCUMENT ME! */
    private double value;

    /** DOCUMENT ME! */
    private double ratio;

/**
     * Creates a new <code>DefaultJoggleModel</code> with an initial value of
     * 0.0, a transmissionRatio of 1.0 and no maximum and no minimum.
     */
    public DefaultJoggleModel() {
        this(0.0, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

/**
     * Creates a new <code>DefaultJoggleModel</code> with a transmissionRatio
     * of 1.0 and no maximum and no minimum.
     *
     * @param value the initial value for this model
     */
    public DefaultJoggleModel(double value) {
        this(value, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

/**
     * Creates a new DefaultJoggleModel with the specified value and
     * transmission ratio and no maximum and no minimum.
     *
     * @param value             the initial value for this model
     * @param transmissionRatio factor between the physical {@link
     *                          Joggle#getRotationAngle() rotationAngle} of a {@link Joggle} and
     *                          the value in this model.
     */
    public DefaultJoggleModel(double value, double transmissionRatio) {
        this(value, transmissionRatio, Double.NEGATIVE_INFINITY,
            Double.POSITIVE_INFINITY);
    }

/**
     * Creates a new DefaultJoggleModel with the specified value, transmission
     * ratio, minimum and maximum.
     *
     * @param value             the initial value for this model
     * @param transmissionRatio factor between the physical {@link
     *                          Joggle#getRotationAngle() rotationAngle} of a {@link Joggle} and
     *                          the value in this model.
     * @param min               a lower limit this model's value is not able to exceed; use
     *                          {@link Double#NEGATIVE_INFINITY} for no limitation
     * @param max               an upper limit this model's value is not able to exceed; use
     *                          {@link Double#POSITIVE_INFINITY} for no limitation
     */
    public DefaultJoggleModel(double value, double transmissionRatio,
        double min, double max) {
        maximum = max;
        minimum = min;
        ratio = transmissionRatio;
        this.value = value;
    }

    /**
     * Sets the maximum to newMax and fits the minimum respectively the
     * value to newMax if they are larger than newMax.
     *
     * @param newMax DOCUMENT ME!
     */
    public void setMaximum(double newMax) {
        if (newMax < minimum) {
            setMinimum(newMax);
        }

        if (newMax < value) {
            setValue(newMax);
        }

        maximum = newMax;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMaximum() {
        return maximum;
    }

    /**
     * Sets the minimum to newMin and fits the maximum respectively the
     * value to newMin if they are less than newMin.
     *
     * @param newMin DOCUMENT ME!
     */
    public void setMinimum(double newMin) {
        if (newMin > maximum) {
            setMaximum(newMin);
        }

        if (newMin > value) {
            setValue(newMin);
        }

        minimum = newMin;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMinimum() {
        return minimum;
    }

    /**
     * Sets value to newVal but only if newVal is between the current
     * minimum and maximum.
     *
     * @param newVal DOCUMENT ME!
     */
    public void setValue(double newVal) {
        if (newVal < minimum) {
            value = minimum;
        } else if (newVal > maximum) {
            value = maximum;
        } else {
            value = newVal;
        }

        fireStateChanged();
    }

    /**
     * Returns the current value held in this model. This does not need
     * to be the current {@link Joggle#getRotationAngle() rotationAngle} of a
     * Joggle. To get the rotationAngle of a Joggle you have to divide this
     * value by the transmissionRatio.
     *
     * @see #getTransmissionRatio()
     */
    public double getValue() {
        return value;
    }

    /**
     * Return the ratio the {@link Joggle#getRotationAngle()
     * rotationAngle} of a {@link Joggle} must be multiplied with to get the
     * value in this model.
     *
     * @return the ratio the {@link Joggle#getRotationAngle() rotationAngle} of
     *         a {@link Joggle} must be multiplied with to get the value in
     *         this model.
     */
    public double getTransmissionRatio() {
        return ratio;
    }

    /**
     * Sets the ratio the {@link Joggle#getRotationAngle()
     * rotationAngle} of a {@link Joggle} must be multiplied with to get the
     * value in this model.
     *
     * @param newRatio factor between the physical {@link
     *        Joggle#getRotationAngle() rotationAngle} of a {@link Joggle} and
     *        the value in this model.
     */
    public void setTransmissionRatio(double newRatio) {
        ratio = newRatio;
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
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }

                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
}
