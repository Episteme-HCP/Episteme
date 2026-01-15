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
import javax.swing.event.EventListenerList;
import java.awt.event.ActionListener;

/**
 * This Timer is optimized for sharable usage from different contexts,
 * for example: Two Objects want to share one Timer. (This
 * is similar to a Model-View-Architecture.)
 * <p/>
 * To start this timer, the starting context must call one of the following
 * methods:
 * <ul>
 * <li>{@link #start(int,ActionListener[],boolean)}</li>
 * <li>{@link #start(int,ActionListener,boolean)}</li>
 * </ul>
 * and give this Timer the delay, the coalesce state and all relevant
 * <code>ActionListeners</code> listening to this Timer.
 * Before beginning to send ActionEvents to the Listeners,
 * this Timer will add the specified ActionListeners.
 * A call to {@link #stop()} will remove these added ActionListeners.
 * Of course, all other ActionListeners which were added by the
 * inherited method {@link Timer#addActionListener(ActionListener)}
 * wont be removed.
 * </p>
 * <p/>
 * Further more this Timer fires a ChangeEvent to all added
 * ChangeListeners whenever it has been started or stopped.
 * </p>
 * <p/>
 * As an example you can check up both {@link Joggle} and
 * {@link TimerControlPanel}.
 * </p>
 *
 * @author marcel
 */
public class SharableTimer extends Timer {
    private ChangeEvent changeEvent = null;
    protected EventListenerList changeListenerList = new EventListenerList();
    private ActionListener[] currentListener;

    /**
     * Creates a new SharableTimer with a delay of 0 and no ActionListener.
     */
    public SharableTimer() {
        super(0, null);
    }

    /**
     * Adds all <code>listenerToAdd</code>, starts this Timer and calls
     * {@link #fireStateChanged()}.
     * If this timer is already running it will previously be stopped
     * and starts running with the new parameters - in other words:
     * this Timer will be restarted.
     *
     * @param delay         the timer delay.
     * @param listenerToAdd all ActionListeners to add before starting this timer.
     * @param coalesce      the coalesce state this Timer should work with.
     * @see Timer#setDelay(int)
     * @see Timer#setCoalesce(boolean)
     * @see Timer#addActionListener(ActionListener)
     */
    public void start(int delay, ActionListener[] listenerToAdd, boolean coalesce) {
        if (isRunning())
            stop();
        setDelay(delay);
        setCoalesce(coalesce);
        for (int i = 0; i < listenerToAdd.length; i++)
            addActionListener(listenerToAdd[i]);
        currentListener = listenerToAdd;
        super.start();
        fireStateChanged();
    }

    /**
     * Similar to {@link #start(int,ActionListener[],boolean)}, but expecting
     * only one ActionListener to add.
     *
     * @param delay         the timer delay.
     * @param listenerToAdd an ActionListener to add before starting this timer.
     * @param coalesce      the coalesce state this Timer should work with.
     */
    public void start(int delay, ActionListener listenerToAdd, boolean coalesce) {
        ActionListener[] listenerArray = {listenerToAdd};
        start(delay, listenerArray, coalesce);
    }

    /**
     * Does nothing - please use one of the other start methods to
     * start this Timer.
     *
     * @see #start(int,ActionListener[],boolean)
     * @see #start(int,ActionListener,boolean)
     */
    public void start() {
    }

    /**
     * Stops this Timer, removes the added ActionListeners recently given
     * to one of the start-methods and calls {@link #fireStateChanged()}.
     * If this Timer is already stopped, this method does nothing.
     */
    public void stop() {
        if (isRunning()) {
            super.stop();
            for (int i = 0; i < currentListener.length; i++)
                removeActionListener(currentListener[i]);
            fireStateChanged();
        }
    }

    //-----event-handling-----
    /**
     * Adds a listener to the list that is notified each time this Timer
     * will be started or stopped.
     *
     * @param l the ChangeListener to add
     */
    public void addChangeListener(ChangeListener l) {
        changeListenerList.add(ChangeListener.class, l);
    }

    /**
     * Removes a ChangeListener from this Timer.
     *
     * @param l the listener to remove.
     */
    public void removeChangeListener(ChangeListener l) {
        changeListenerList.remove(ChangeListener.class, l);
    }

    /**
     * Sends a ChangeEvent, whose source is this Timer, to each
     * ChangeListener which was added to this Timer.
     * This method is called each time this Timer will be started or
     * stopped.
     *
     * @see #addChangeListener(ChangeListenerl)
     */
    protected void fireStateChanged() {
        Object[] listeners = changeListenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
}
