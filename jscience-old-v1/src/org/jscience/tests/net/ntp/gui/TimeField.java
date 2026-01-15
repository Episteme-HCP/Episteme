package org.jscience.tests.net.ntp.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class TimeField extends JPanel implements ActionListener {
    /** DOCUMENT ME! */
    private Font dateFont;

    /** DOCUMENT ME! */
    private Font timeFont;

    /** DOCUMENT ME! */
    private AlarmTime alarmTime;

    /** DOCUMENT ME! */
    private boolean alarmOn;

    /** DOCUMENT ME! */
    private String alarmMessage;

    /** DOCUMENT ME! */
    private long offset = 0;

    /** DOCUMENT ME! */
    private JLabel dateLabel = new JLabel();

    /** DOCUMENT ME! */
    private JLabel timeLabel = new JLabel();

    /** DOCUMENT ME! */
    private Date date = new Date();

    /** DOCUMENT ME! */
    private DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

    /** DOCUMENT ME! */
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    /** DOCUMENT ME! */
    private int timerDelay = 100;

    /** DOCUMENT ME! */
    private Timer timer = new Timer(timerDelay, this);

    /** DOCUMENT ME! */
    private Thread t;

/**
     * Creates a new TimeField object.
     */
    public TimeField() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        timeLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        date.setTime(System.currentTimeMillis() + offset);
        dateLabel.setAlignmentX((float) 0.5);
        timeLabel.setAlignmentX((float) 0.5);
        dateLabel.setText(dateFormat.format(date));
        timeLabel.setText(timeFormat.format(date));
        add(dateLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(timeLabel);
        timer.start();
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param ae DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent ae) {
        date.setTime(System.currentTimeMillis() + offset);
        dateLabel.setText(dateFormat.format(date));
        timeLabel.setText(timeFormat.format(date));
        doAlarm();
    }

    /**
     * DOCUMENT ME!
     */
    public void timerReset() {
        timer.stop();
        timer = new Timer(timerDelay, this);
        timer.start();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getOffset() {
        return offset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     */
    protected void setOffset(long offset) {
        this.offset = offset;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Font getDateFont() {
        return dateFont;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dateFont DOCUMENT ME!
     */
    public void setDateFont(Font dateFont) {
        this.dateFont = dateFont;
        dateLabel.setFont(dateFont);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Font getTimeFont() {
        return timeFont;
    }

    /**
     * DOCUMENT ME!
     *
     * @param timeFont DOCUMENT ME!
     */
    public void setTimeFont(Font timeFont) {
        this.timeFont = timeFont;
        timeLabel.setFont(timeFont);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AlarmTime getAlarmTime() {
        return alarmTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param alarmTime DOCUMENT ME!
     */
    public void setAlarmTime(AlarmTime alarmTime) {
        this.alarmTime = alarmTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAlarmMessage() {
        return alarmMessage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param alarmMessage DOCUMENT ME!
     */
    public void setAlarmMessage(String alarmMessage) {
        this.alarmMessage = alarmMessage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param alarmOn DOCUMENT ME!
     */
    public void setAlarmOn(boolean alarmOn) {
        this.alarmOn = alarmOn;
    }

    /**
     * DOCUMENT ME!
     */
    public void doAlarm() {
        if (alarmOn && alarmTime.isAlarm(date)) {
            if (t == null) {
                Runnable b = new Runnable() {
                        public void run() {
                            while (true) {
                                Toolkit.getDefaultToolkit().beep();

                                try {
                                    Thread.sleep(300);
                                } catch (Exception e) {
                                }
                            }
                        }
                    };

                t = new Thread(b);
                t.setPriority(Thread.MIN_PRIORITY);
                t.start();
            } else {
                t.resume();
            }

            JOptionPane.showMessageDialog(this, alarmMessage, "",
                JOptionPane.INFORMATION_MESSAGE);
            t.suspend();
        }
    }
}
