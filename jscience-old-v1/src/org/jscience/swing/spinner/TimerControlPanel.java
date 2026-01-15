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

import org.jscience.swing.spinner.icons.ArrowIcon;
import org.jscience.swing.spinner.icons.RectanglesIcon;
import org.jscience.swing.spinner.icons.SquareIcon;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A <code>TimerControlPanel</code> is useful whenever you want to control value
 * sequences by an easy-to-use UI. The following image shows that UI:
 * <p/>
 * <img src="doc-files/timerControlPanel.gif" alt="TimerControlPanel UI">
 * </p>
 * <p/>
 * This <code>JPanel</code> contains three <code>JButtons</code> for several user
 * definable actions:
 * <table border="1" CELLPADDING="3" CELLSPACING="0">
 * <tr>
 * <td><img src="doc-files/leftDirectedArrow.gif" alt="left directed arrow button"></td>
 * <td>repeated actions, e.g.: run backward through a sequence</td>
 * </tr>
 * <tr>
 * <td><img src="doc-files/rightDirectedArrow.gif" alt="right directed arrow button"></td>
 * <td>repeated actions, e.g.: run forward through a sequence</td>
 * </tr>
 * <tr>
 * <td><img src="doc-files/square.gif" alt="stop/reset button"></td>
 * <td>an action, e.g.: reset to a default value</td>
 * </tr>
 * </table>
 * </p>
 * <p/>
 * If the timer is running (because the user clicked on one of the arrow buttons),
 * <img src="doc-files/square.gif" alt="stop/reset button">
 * will change to <img src="doc-files/rectangles.gif" alt="stop/break button">.
 * A click on it will stop the timer at the current value.
 * </p>
 * <h3>Use a TimerControlPanel</h3>
 * <p/>
 * To combine the timer functionality with data, you need
 * three or five several
 * <code>Action</code> objects and create a <code>TimerControlPanel</code> with them.
 * These user specific <code>Action</code> objects specify what to do if somebody clicks on a
 * certain button in a certain way.
 * Three <code>Action</code> objects present actions regarding to
 * <ul>
 * <li>a <b>left mouse click</b> on
 * <img src="doc-files/leftDirectedArrow.gif" alt="left directed arrow button">,
 * <img src="doc-files/rightDirectedArrow.gif" alt="right directed arrow button"> and
 * <img src="doc-files/square.gif" alt="stop/reset button">.</li>
 * </ul>
 * Two more
 * <code>Action</code> objects are optional and present actions regarding to
 * <ul>
 * <li>a <b>right mouse click</b>
 * on <img src="doc-files/leftDirectedArrow.gif" alt="left directed arrow button"> and
 * <img src="doc-files/rightDirectedArrow.gif" alt="right directed arrow button">.</li>
 * </ul>
 * The following listings show both, TimerControlPanel specific and user specific
 * actions. The second table is irrelevant if the constructor expecting the left mouse click Actions only
 * is used.
 * </p>
 * <p/>
 * <table border="1" width="100%" CELLPADDING="3" CELLSPACING="0">
 * <tr>
 * <th width="14%">left mouse click</th>
 * <th width="43%">TimerControlPanel specific action</th>
 * <th width="43%">User specific action</th>
 * <tr>
 * <th><img src="doc-files/leftDirectedArrow.gif" alt="left directed arrow button"></th>
 * <td>The timer starts running if the user action for the button is enabled.<br>
 * The button becomes disabled.</td>
 * <td>The method <code>actionPerformed(ActionEvent)</code> of the
 * user's <code>Action</code> object will be repeatedly called by the timer.<br>
 * The <code>ActionEvent's ActionCommand</code> will be
 * {@link #TIMER_CMD}.</td>
 * </tr>
 * <tr>
 * <th><img src="doc-files/rightDirectedArrow.gif" alt="right directed arrow button"></th>
 * <td>The timer starts running if the user action for the button is enabled.<br>
 * The button becomes disabled.</td>
 * <td>The method <code>actionPerformed(ActionEvent)</code> of the
 * user's <code>Action</code> object will be repeatedly called by the timer.<br>
 * The <code>ActionEvent's ActionCommand</code> will be
 * {@link #TIMER_CMD}.</td>
 * </tr>
 * <tr>
 * <th><img src="doc-files/square.gif" alt="stop/reset button"></th>
 * <td>no action</td>
 * <td>The method <code>actionPerformed(ActionEvent)</code> of the
 * user's <code>Action</code> object will be called.<br>
 * The <code>ActionEvent's ActionCommand</code> will be
 * {@link #RESET_CMD}.</td>
 * </tr>
 * <tr>
 * <th><img src="doc-files/rectangles.gif" alt="stop/break button"></th>
 * <td>The timer stops.</td>
 * <td>no action</td>
 * </tr>
 * </table>
 * </p>
 * <p/>
 * <table border="1" width="100%" CELLPADDING="3" CELLSPACING="0">
 * <tr>
 * <th width="14%">right mouse click</th>
 * <th width="43%">TimerControlPanel specific action</th>
 * <th width="43%">User specific action</th>
 * <tr>
 * <th><img src="doc-files/leftDirectedArrow.gif" alt="left directed arrow button"></th>
 * <td>The timer stops if running.</td>
 * <td>The method <code>actionPerformed(ActionEvent)</code> of the
 * user's <code>Action</code> object will be called.<br>
 * The <code>ActionEvent's ActionCommand</code> will be
 * {@link #RIGHT_MOUSE_CLICK_CMD}.</td>
 * </tr>
 * <tr>
 * <th><img src="doc-files/rightDirectedArrow.gif" alt="right directed arrow button"></th>
 * <td>The timer stops if running.</td>
 * <td>The method <code>actionPerformed(ActionEvent)</code> of the
 * user's <code>Action</code> object will be called.<br>
 * The <code>ActionEvent's ActionCommand</code> will be
 * {@link #RIGHT_MOUSE_CLICK_CMD}.</td>
 * </tr>
 * </table>
 * </p>
 * <p/>
 * See source of
 * <a href="doc-files/TimerControlPanelExample_SourceCode.html">TimerControlPanelExample</a>
 * for a very simple implementation. Download
 * <a href="doc-files/TimerControlPanelExample.java.txt">TimerControlPanelExample.java</a>
 * for testing.<br>
 * See {@link SpinnerTimerControl} for a more complex example.
 * </p><p>
 * The enabled state of the directed Buttons is for timer actions only (left
 * mouse clicks). A right mouse click also works if the buttons are disabled.
 * </p>
 *
 * @author marcel
 */
public class TimerControlPanel extends JPanel {
    /**
     * Command string of an <code>ActionEvent</code> given to the method
     * <code>actionPerformed(ActionEvent)</code> of the user specific actions
     * <i>forward</i> and <i>backward</i> (see {@link
     * #TimerControlPanel(Action,Action,Action) constructor}).
     */
    public static final String TIMER_CMD = "timerStarted";
    /**
     * Command string of an <code>ActionEvent</code> given to the method
     * <code>actionPerformed(ActionEvent)</code> of the user specific action
     * <i>reset</i> (see {@link #TimerControlPanel(Action,Action,Action)
     * constructor}).
     */
    public static final String RESET_CMD = "resetButtonClicked";
    /**
     * Command string of an <code>ActionEvent</code> given to the method
     * <code>actionPerformed(ActionEvent)</code> of the user specific actions
     * <i>rightClickOnForward</i> and <i>rightClickOnBackward</i> (see {@link
     * #TimerControlPanel(Action,Action,Action,Action,Action) constructor}).
     */
    public static final String RIGHT_MOUSE_CLICK_CMD =
            "rightMouseButtonClicked";
    private SharableTimer timer;
    private ActionListener timerListener;
    private int timerDelay = 0;
    private boolean coalesce = true;
    private boolean timerStartedByThis = false;
    Icon resetIcon;
    Icon pauseIcon;
    Icon forwardIcon;
    Icon backwardIcon;

    {
        updateUI();
    }

    Action forwardAction, backwardAction, resetAction;
    Action currentUserAction;
    //Action rightClickOnForwardAction, rightClickOnBackwardAction;
    JButton backwardButton, forwardButton, resetButton;
    final ActionEvent timerEvent = new ActionEvent(this,
            ActionEvent.ACTION_PERFORMED, TIMER_CMD);
    final ActionEvent resetEvent = new ActionEvent(this,
            ActionEvent.ACTION_PERFORMED, RESET_CMD);
    final ActionEvent rightMouseClickEvent = new ActionEvent(this,
            ActionEvent.ACTION_PERFORMED, RIGHT_MOUSE_CLICK_CMD);
    Action currentButtonAction;

    /*
    * Class for TimerControlPanel specific actions for "run timer forward" and
    * "run timer backward".
    *
    * @author marcel
    */
    class DirectedButtonAction extends AbstractAction {
        /*
        * If appropriateTimerAction (userAction) changed to disabled or enabled
        * (because of reaching or leaving minimum or maximum),
        * this Action must set to disabled or enabled.
        */
        PropertyChangeListener buttonStateChanger = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent ev) {
                //System.out.println("-- property change");
                if (!((Action) ev.getSource()).isEnabled()) {
                    if (timer.isRunning())
                        timer.stop();
                    setEnabled(false);
                } else
                    setEnabled(true);
            }
        };
        Action userAction;

        DirectedButtonAction(Icon icon, Action appropriateUserAction) {
            super("", icon);
            appropriateUserAction.addPropertyChangeListener(buttonStateChanger);
            userAction = appropriateUserAction;
        }

        public void actionPerformed(ActionEvent e) {
            if (userAction.isEnabled()) {
                if (timer.isRunning())
                    timer.stop();
                if (userAction != currentUserAction) {
                    currentUserAction = userAction;
                    currentButtonAction = this;
                }
                timerStartedByThis = true;
                timer.start(timerDelay, timerListener, coalesce);
                //after starting, the timer sends a ChangeEvent to the ChangeListener
                //added in constructor
            }
        }
    }

    Action forwardButtonAction;
    Action backwardButtonAction;
    /*
    * Class for TimerControlPanel specific Action for "stop timer".
    *
    * @author marcel
    */
    Action resetButtonAction = new AbstractAction("", resetIcon) {
        public void actionPerformed(ActionEvent e) {
            if (timer.isRunning()) {
                timer.stop();
            } else {
                resetAction.actionPerformed(resetEvent);
            }
        }
    };

    /**
     * Creates a new <code>TimerControlPanel</code> with the specified user
     * <code>Action</code> objects. See class documentation for details.
     *
     * @param forward  User specific action listening to clicks on
     *                 <img src="doc-files/rightDirectedArrow.gif" alt="right directed arrow button">
     * @param backward User specific action listening to clicks on
     *                 <img src="doc-files/leftDirectedArrow.gif" alt="left directed arrow button">
     * @param reset    User specific action listening to clicks on
     *                 <img src="doc-files/square.gif" alt="stop/reset button">
     */
    public TimerControlPanel(Action forward, Action backward, Action reset) {
        super(new GridLayout(0, 3));

        forwardAction = forward;
        backwardAction = backward;
        resetAction = reset;

        forwardButtonAction = new DirectedButtonAction(forwardIcon, forwardAction);
        backwardButtonAction = new DirectedButtonAction(backwardIcon, backwardAction);

        backwardButton = new JButton(backwardButtonAction);
        forwardButton = new JButton(forwardButtonAction);
        resetButton = new JButton(resetButtonAction);
        add(backwardButton);
        add(resetButton);
        add(forwardButton);
        timerListener = new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                currentUserAction.actionPerformed(timerEvent);
            }
        };
        timer = new SharableTimer();
        /*
        * listens to ChangeEvents of StateFiringTimer - so
        * it checks if the timer is going to run or to stop and
        * it brings Actions to the right state (disable Buttons, etc.)
        */
        timer.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ev) {
                if (timer.isRunning()) //timer has been started
                {
                    resetButtonAction.putValue(Action.SMALL_ICON, pauseIcon);
                    if (timerStartedByThis)
                        currentButtonAction.setEnabled(false);
                } else //timer has been stoped
                {
                    resetButtonAction.putValue(Action.SMALL_ICON, resetIcon);
                    if (timerStartedByThis)
                        currentButtonAction.setEnabled(true);
                    timerStartedByThis = false;
                }
            }
        });
    }

    /**
     * Creates a new <code>TimerControlPanel</code> with the specified user
     * <code>Action</code> objects. See class documentation for details.
     *
     * @param forward              User specific action listening to clicks on
     *                             <img src="doc-files/rightDirectedArrow.gif" alt="right directed arrow button">
     * @param backward             User specific action listening to clicks on
     *                             <img src="doc-files/leftDirectedArrow.gif" alt="left directed arrow button">
     * @param reset                User specific action listening to clicks on
     *                             <img src="doc-files/square.gif" alt="stop/reset button">
     * @param rightClickOnForward  User specific action listening to a right mouse
     *                             click on <img src="doc-files/rightDirectedArrow.gif" alt="right directed arrow button">
     * @param rightClickOnBackward User specific action listening to a right mouse
     *                             click on <img src="doc-files/leftDirectedArrow.gif" alt="left directed arrow button">
     */
    public TimerControlPanel(Action forward, Action backward, Action reset,
                             Action rightClickOnForward, Action rightClickOnBackward) {
        this(forward, backward, reset);
        forwardButton.addMouseListener(new RightMouseClickListener(rightClickOnForward));
        backwardButton.addMouseListener(new RightMouseClickListener(rightClickOnBackward));
    }

    /**
     * Sets a new delay in msec between two timer events.
     *
     * @param newDelay The new delay between two timer events.
     */
    public void setTimerDelay(int newDelay) {
        timerDelay = newDelay;
        if (timer.isRunning() && timerStartedByThis) {
            //restarts the timer
            timerStartedByThis = true;
            timer.start(timerDelay, timerListener, true);
        }
    }

    /**
     * Returns the delay in msec between two timer events.
     *
     * @return the delay in msec between two timer events.
     */
    public int getTimerDelay() {
        return timerDelay;
    }

    public SharableTimer getTimer() {
        return timer;
    }

    public void setTimer(SharableTimer newTimer) {
        if (timer.isRunning())
            timer.stop();
        timer = newTimer;
    }

    /**
     * MouseListener for the directed Buttons. The method mouseClicked(...)
     * is calling actionPerformed(...) on the appropriate user specific action if the
     * right mouse button was clicked.
     */
    class RightMouseClickListener extends MouseAdapter {
        Action appropriateAction;

        RightMouseClickListener(Action action) {
            appropriateAction = action;
        }

        public void mouseClicked(MouseEvent ev) {
            if (ev.getModifiers() == MouseEvent.BUTTON3_MASK) {
                if (timer.isRunning())
                    timer.stop();
                appropriateAction.actionPerformed(rightMouseClickEvent);
            }
        }
    }

    ImageIcon metalStop, metalPause, metalForward, metalBackward;
    Icon otherStop, otherPause, otherForward, otherBackward;

    public void updateUI() {
        super.updateUI();
        if (UIManager.getLookAndFeel() instanceof MetalLookAndFeel) {
            //System.out.println("-- MetalLookAndFeel");
            if (metalStop == null) {
                metalStop = new ImageIcon(getClass().getResource("icons/Stop16.gif"));
                metalPause = new ImageIcon(getClass().getResource("icons/Pause16.gif"));
                metalForward = new ImageIcon(getClass().getResource("icons/Play16.gif"));
                metalBackward = new ImageIcon(getClass().getResource("icons/PlayBackward16.gif"));
            }
            resetIcon = metalStop;
            pauseIcon = metalPause;
            forwardIcon = metalForward;
            backwardIcon = metalBackward;
        } else {
            //System.out.println("-- Other LookAndFeel");
            if (otherStop == null) {
                otherStop = new SquareIcon();
                otherPause = new RectanglesIcon();
                otherForward = new ArrowIcon(SwingConstants.EAST);
                otherBackward = new ArrowIcon(SwingConstants.WEST);
            }
            resetIcon = otherStop;
            pauseIcon = otherPause;
            forwardIcon = otherForward;
            backwardIcon = otherBackward;
        }
    }

    /**
     * Returns the coalesce state the internal SharableTimer
     * is using. The default is true.
     *
     * @return the coalesce state the internal Timer is using.
     * @see SharableTimer#start(int,ActionListener[],boolean)
     * @see Timer#isCoalesce()
     */
    public boolean isCoalesce() {
        return coalesce;
    }

    /**
     * Sets the coalesce state for the internal SharableTimer.
     * If the Timer will be started, the coalesce state will
     * be given to it.
     *
     * @param b the new coalesce state for the internal Timer
     * @see SharableTimer#start(int,ActionListener[],boolean)
     * @see Timer#setCoalesce(boolean)
     */
    public void setCoalesce(boolean b) {
        coalesce = b;
    }

}
