/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import java.util.Date;

import javax.swing.*;


/**
 * A simple specialized <code>JSpinner</code> using
 * <code>java.util.Date</code> for a value.
 *
 * @author Holger Antelmann
 */
public class JTimeChooser extends JSpinner {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -5524090997350592980L;

/**
     * Creates a new JTimeChooser object.
     */
    public JTimeChooser() {
        super(new SpinnerDateModel());

        JSpinner.DateEditor editor = new JSpinner.DateEditor(this);
        setEditor(editor);
    }

/**
     * Creates a new JTimeChooser object.
     *
     * @param time DOCUMENT ME!
     */
    public JTimeChooser(Date time) {
        this();
        setTime(time);
    }

    /**
     * DOCUMENT ME!
     *
     * @param date DOCUMENT ME!
     */
    public void setTime(Date date) {
        setValue(date);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getTime() {
        return (Date) getValue();
    }
}
