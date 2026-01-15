// JTEM - Java Tools for Experimental Mathematics
// Copyright (C) 2001 JTEM-Group
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package org.jscience.swing.spinner;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;

/**
 * This <code>JPanel</code> contains a {@link JSpinner} and a
 * {@link TimerControlPanel}
 * handling a timer iterating through the {@link SpinnerModel SpinnerModel's}
 * sequence.
 * <p/>
 * Clicks on the timer's buttons have the following effects:
 * <table border="1" width="100%" CELLPADDING="3" CELLSPACING="0">
 * <th>action</th>
 * <th width="25%"><img src="doc-files/leftDirectedArrow.gif" alt="left directed arrow button"></th>
 * <th width="25%"><img src="doc-files/square.gif" alt="stop button"></th>
 * <th width="25%"><img src="doc-files/rightDirectedArrow.gif" alt="right directed arrow button"></th>
 * <th width="25%"><img src="doc-files/rectangles.gif" alt="break button"></th>
 * </tr>
 * <tr>
 * <th>left mouse click</th>
 * <td>runs the timer backward through the spinner's sequence</td>
 * <td>resets to value 0 if it is in the domain and the <code>SpinnerModel</code>
 * is a {@link SpinnerNumberModel}, otherwise does nothing</td>
 * <td>runs the timer forward through the spinner's sequence</td>
 * <td>stops the timer</td>
 * </tr>
 * <tr>
 * <th>right mouse click</th>
 * <td>jump to the minimum value of the spinner's sequence (only if the
 * <code>SpinnerModel</code> is a {@link SpinnerNumberModel} and a lower
 * limit is set, otherwise the timer if running will stop at the current value)</td>
 * <td>does nothing</td>
 * <td>jump to the maximum value of the spinner's sequence (only if the
 * <code>SpinnerModel</code> is a {@link SpinnerNumberModel} and an upper
 * limit is set, otherwise the timer if running will stop at the current value)</td>
 * <td>does nothing</td>
 * </tr>
 * </table>
 * </p>
 * <p/>
 * <b>Note:</b><br>
 * <img src="doc-files/square.gif" alt="stop button"> is only seen, if the timer is <b>not</b> running<br>
 * <img src="doc-files/rectangles.gif" alt="break button"> is only seen, if the timer <b>is</b> running
 * </p>
 *
 * @see org.jscience.swing.spinner.icons.ArrowIcon
 * @see org.jscience.swing.spinner.icons.SquareIcon
 * @see org.jscience.swing.spinner.icons.RectanglesIcon
 */
public class SpinnerTimerControl extends JPanel {

    /**
     * To avoid several classes for "right click on forward" and
     * "right click on backward".
     * This class defines the method to get the value from if a right
     * click on the forward or backward button was proceeded.
     *
     * @author rettkow
     */
    private abstract class ExplicitValueSource {
        public abstract Comparable getExplicitValue(SpinnerNumberModel model);
    }

    private class RightMouseClickAction extends AbstractAction {
        Comparable explicitValue;
        ExplicitValueSource explicitValueSource;
        SpinnerNumberModel model;

        RightMouseClickAction(ExplicitValueSource valueSource) {
            explicitValueSource = valueSource;
        }

        public void actionPerformed(ActionEvent ev) {
            try {
                model = (SpinnerNumberModel) getSpinnerModel();
            } catch (ClassCastException ex) {
                return;
            }
            explicitValue = explicitValueSource.getExplicitValue(model);
            if (explicitValue != null) {
                spinner.setValue(explicitValue);
            }
        }
    }

    /**
     * One class for both, backward and forward action.
     *
     * @author marcel
     */
    private class SpinnerAction extends AbstractAction {
        //SpinnerNumberModel model;
        //Comparable explicitValue;
        Object value;

        SpinnerAction() {
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent ev) {
            spinner.setValue(value);
        }

        public void setValue(Object v) {
            value = v;
            setEnabled(value != null);
        }
    }

    /**
     * Listens to changes on the <code>SpinnerModel</code>.
     *
     * @author marcel
     */
    class SpinnerModelListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            spinnerForward.setValue(spinner.getNextValue());
            spinnerBackward.setValue(spinner.getPreviousValue());
            fireStateChanged();
        }
    }

    /**
     * Final variable to adjust the two UI components {@link JSpinner} and
     * {@link TimerControlPanel} horizontally.
     *
     * @see SpinnerTimerControl#SpinnerTimerControl(SpinnerModel,int)
     */
    static public final int HORIZONTAL = 0;
    /**
     * Final variable to adjust the two UI components {@link JSpinner} and
     * {@link TimerControlPanel} vertically.
     *
     * @see SpinnerTimerControl#SpinnerTimerControl(SpinnerModel,int)
     */
    static public final int VERTICAL = 1;
    final static public String ZERO_STRING = "0";
    //---------------------------------------------------------------------------

    private int adjustment;
    ChangeEvent changeEvent = null;

    private Class numberClass;
    AbstractAction rightClickOnBackwardAction = new RightMouseClickAction(new ExplicitValueSource() {
        public Comparable getExplicitValue(SpinnerNumberModel model) {
            return model.getMinimum();
        }
    });
    AbstractAction rightClickOnForwardAction = new RightMouseClickAction(new ExplicitValueSource() {
        public Comparable getExplicitValue(SpinnerNumberModel model) {
            return model.getMaximum();
        }
    });
    JSpinner spinner;
    private SpinnerAction spinnerBackward = new SpinnerAction();

    private Object spinnerConstraints;
    private SpinnerAction spinnerForward = new SpinnerAction();
    private Action spinnerReset = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            SpinnerNumberModel model;
            try {
                model = (SpinnerNumberModel) getSpinnerModel();
            } catch (ClassCastException ex) {
                return;
            }
            Number number = model.getNumber();
            if (number != null) {
                Class c = number.getClass();
                if (numberClass != c) {
                    try {
                        Constructor numberConstructor = c.getConstructor(new Class[]{ZERO_STRING.getClass()});
                        zero = numberConstructor.newInstance(new Object[]{ZERO_STRING});
                        numberClass = c;
                    } catch (Exception ex) {
                        numberClass = null;
                    }
                }
                if (numberClass != null &&
                        (model.getMinimum() == null ||
                                model.getMinimum().compareTo(zero) <= 0) &&
                        (model.getMaximum() == null ||
                                model.getMaximum().compareTo(zero) >= 0))
                    spinner.setValue(zero);
            }
        }
    };
    private TimerControlPanel timerPanel;
    private Object timerPanelConstraints;
    private Object zero;

    /**
     * Creates a new <code>SpinnerTimerControl</code> with a
     * {@link SpinnerNumberModel} as the current
     * {@link SpinnerModel}. The adjustment of the {@link JSpinner}
     * and {@link TimerControlPanel}
     * is HORIZONTAL.
     */
    public SpinnerTimerControl() {
        this(new SpinnerNumberModel(), HORIZONTAL);
    }

    /**
     * Creates a new <code>SpinnerTimerControl</code> with the
     * specified <code>SpinnerModel</code> as the current
     * <code>SpinnerModel</code>. The adjustment of the
     * {@link JSpinner} and {@link TimerControlPanel}
     * is HORIZONTAL.
     */
    public SpinnerTimerControl(SpinnerModel spinnerModel) {
        this(spinnerModel, HORIZONTAL);
    }

    /**
     * Creates a new <code>SpinnerTimerControl</code> with the specified
     * <code>SpinnerModel</code> as the current <code>SpinnerModel</code>.
     * The int value specifies the adjustment of the UI components
     * {@link JSpinner} and {@link TimerControlPanel} to each other. Use one of the final fields
     * {@link #HORIZONTAL} and {@link #VERTICAL}.
     *
     * @param adjustment {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public SpinnerTimerControl(SpinnerModel spinnerModel, int adjustment) {
        super(new BorderLayout());
        setConstraints(adjustment);
        this.adjustment = adjustment;
        spinner = new JSpinner(spinnerModel);
        spinner.addChangeListener(new SpinnerModelListener());
        spinnerForward.setValue(spinner.getNextValue());
        spinnerBackward.setValue(spinner.getPreviousValue());
        timerPanel = new TimerControlPanel(spinnerForward, spinnerBackward, spinnerReset,
                rightClickOnForwardAction, rightClickOnBackwardAction);
        Dimension spinnerDimension = spinner.getPreferredSize();
        spinnerDimension.width = timerPanel.getPreferredSize().width;
        //getPreferredSize() returned a copy of Dimension, that's why:
        spinner.setPreferredSize(spinnerDimension);
        add(spinner, spinnerConstraints);
        add(timerPanel, timerPanelConstraints);
    }

    /**
     * Adds a listener to the list that is notified each time a change to the
     * spinner's model occurs.
     *
     * @param l the ChangeListener to add
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * Sends a ChangeEvent, whose source is this SpinnerTimerControl, to each listener
     * which was added to this SpinnerTimerControl. This method is called each time a
     * ChangeEvent is received from the spinner.
     *
     * @see #addChangeListener(ChangeListenerl)
     */
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    /**
     * Returns the current layout adjustment.
     *
     * @return the current layout adjustment.
     */
    public int getLayoutAdjustment() {
        return adjustment;
    }

    /**
     * Returns the current <code>SpinnerModel</code>.
     *
     * @return the current <code>SpinnerModel</code>.
     * @see #setSpinnerModel(SpinnerModel)
     */
    public SpinnerModel getSpinnerModel() {
        return spinner.getModel();
    }

    /**
     * Returns the SharableTimer this' TimerControlPanel is
     * using.
     *
     * @return the SharableTimer of the TimerControlPanel.
     * @see TimerControlPanel#getTimer()
     */
    public SharableTimer getTimer() {
        return timerPanel.getTimer();
    }

    /**
     * Returns the current timer delay in msec.
     *
     * @return the current timer delay in msec.
     * @see #setTimerDelay(int)
     * @see javax.swing.Timer#getDelay()
     */
    public int getTimerDelay() {
        return timerPanel.getTimerDelay();
    }

    /**
     * Removes a ChangeListener from this SpinnerTimerControl.
     *
     * @param l the listener to remove.
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /**
     * The adjustment of the {@link JSpinner} and the {@link TimerControlPanel}
     * depends on both, {@link #spinnerConstraints} and {@link #timerPanelConstraints}.
     * The method sets these constraints in relation to the specified int.
     *
     * @param adjustment {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private void setConstraints(int adjustment) {
        switch (adjustment) {
            case HORIZONTAL:
                spinnerConstraints = BorderLayout.WEST;
                timerPanelConstraints = BorderLayout.CENTER;
                break;
            case VERTICAL:
                spinnerConstraints = BorderLayout.NORTH;
                timerPanelConstraints = BorderLayout.SOUTH;
                break;
            default:
                throw new IllegalArgumentException("Non supported adjustment.");
        }
    }

    /**
     * Sets a new layout adjustment and revalidates this <code>JPanel's</code> component tree.
     * Possible adjustments are: {@link #HORIZONTAL}, {@link #VERTICAL}.
     *
     * @param newAdjustment The new layout adjustment.
     */
    public void setLayoutAdjustment(int newAdjustment) {
        if (adjustment != newAdjustment) {
            remove(timerPanel);
            remove(spinner);
            setConstraints(newAdjustment);
            adjustment = newAdjustment;
            add(spinner, spinnerConstraints);
            add(timerPanel, timerPanelConstraints);
            validate();
            repaint();
        }
    }

    /**
     * Sets this' <code>SpinnerModel</code> to the specified one.
     *
     * @param newModel The new <code>SpinnerModel</code>.
     * @see #getSpinnerModel()
     */
    public void setSpinnerModel(SpinnerModel newModel) {
        spinner.setModel(newModel);
        numberClass = null;
    }

    /**
     * Sets a new SharableTimer this' TimerControlPanel
     * should use.
     *
     * @param timer the new Timer.
     * @see TimerControlPanel#setTimer(SharableTimer)
     */
    public void setTimer(SharableTimer timer) {
        timerPanel.setTimer(timer);
    }

    /**
     * Sets the timer delay (in msec) to <code>newDelay</code>.
     *
     * @param newDelay The new timer delay in msec.
     * @see #getTimerDelay()
     */
    public void setTimerDelay(int newDelay) {
        timerPanel.setTimerDelay(newDelay);
    }
}
