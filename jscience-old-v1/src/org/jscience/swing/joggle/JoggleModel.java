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

import javax.swing.event.ChangeListener;


/**
 * A model representing a bounded or unbounded double value sequence. In
 * relation to a {@link Joggle} this model keeps a value depending on the
 * current rotationAngle of the joggle. To convert a model's value to a
 * rotationAngle, a joggle calls {@link #getTransmissionRatio()}.
 *
 * @author marcel
 */
public interface JoggleModel {
    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    void addChangeListener(ChangeListener l);

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    void removeChangeListener(ChangeListener l);

    /**
     * Set the upper limit of the represented value sequence.
     *
     * @param newMax a new upper limit.
     */
    void setMaximum(double newMax);

    /**
     * Return the upper limit of the represented value sequence.
     *
     * @return the upper limit.
     */
    double getMaximum();

    /**
     * Set the lower limit of the represented value sequence.
     *
     * @param newMin a new lower limit.
     */
    void setMinimum(double newMin);

    /**
     * Return the lower limit of the represented value sequence.
     *
     * @return the lower limit.
     */
    double getMinimum();

    /**
     * Set a new value contained in the value sequence.
     *
     * @param value the value to be set.
     */
    void setValue(double value);

    /**
     * Returns the current value. If you are using this model with a
     * joggle instance the returned value will be the result of the
     * transmissionRatio multiplied with the current rotationAngle of the joggle.<br>
     * <br>
     * value=rotationAngletransmissionRatio;
     *
     * @return DOCUMENT ME!
     *
     * @see #getTransmissionRatio()
     * @see Joggle#getRotationAngle()
     */
    double getValue();

    /**
     * Return the ratio converting the rotationAngle of a Joggle to the
     * value in this Model. Usually this Model's value should be the {@link
     * Joggle#getRotationAngle() rotationAngle} of the Joggle multiplied with
     * the transmission ratio.
     *
     * @return DOCUMENT ME!
     */
    double getTransmissionRatio();
}
