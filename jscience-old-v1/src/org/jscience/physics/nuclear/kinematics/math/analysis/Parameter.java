/***************************************************************
 * Nuclear Simulation Java Class Libraries
 * Copyright (C) 2003 Yale University
 *
 * Original Developer
 *     Dale Visser (dale@visser.name)
 *
 * OSI Certified Open Source Software
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the University of Illinois/NCSA 
 * Open Source License.
 *
 * This program is distributed in the hope that it will be 
 * useful, but without any warranty; without even the implied 
 * warranty of merchantability or fitness for a particular 
 * purpose. See the University of Illinois/NCSA Open Source 
 * License for more details.
 *
 * You should have received a copy of the University of 
 * Illinois/NCSA Open Source License along with this program; if 
 * not, see http://www.opensource.org/
 **************************************************************/
package org.jscience.physics.nuclear.kinematics.math.analysis;

/**
 * <p>Parameters used to fit.</p>
 *  <dl><dt>INT</dt><dd>integer, such as Number of Peaks, or Minimum
 * Channel</dd><dt>DOUBLE</dt><dd>standard variable fit parameter, includes
 * a "fix value" checkbox</dd><dt>TEXT</dt><dd>field showing fit function
 * and/or brief instructions</dd><dt>BOOLEAN</dt><dd>true/false option,
 * such as Include Background, or Display Output (how this would be
 * implemented, I'm not sure)</dd></dl><p>Options. You can use as many
 * options as you want.</p>
 *  <dl><dt>(NO_)OUTPUT</dt><dd>is calculated and has no associated error
 * bars (e.g. Chi-Squared)</dd><dt>(NO_)MOUSE</dt><dd>value can be obtained
 * with mouse from screen</dd><dt>(NO_)ESTIMATE</dt><dd>can be estimated</dd><dt>(NO_)FIX</dt><dd>value
 * is fixed..do not vary during fit</dd></dl>
 */
public class Parameter {
    // options
    /**
     * DOCUMENT ME!
     */
    public final static int TYPE = 7;

    // different types
    /** Parameter is an integer number, e.g., a histogram channel number. */
    public final static int INT = 1;

    /** Parameter is a floating point number. */
    public final static int DOUBLE = 2;

    /** Parameter is a boolean value...displayed as a checkbox. */
    public final static int BOOLEAN = 3;

    /** Parameter is simply a text box. */
    public final static int TEXT = 4;

    // other options
    /**
     * DOCUMENT ME!
     */
    public final static int ERROR = 0; //default

    /**
     * DOCUMENT ME!
     */
    public final static int NO_ERROR = 8;

    /**
     * DOCUMENT ME!
     */
    public final static int FIX = 16;

    /**
     * DOCUMENT ME!
     */
    public final static int NO_FIX = 0; //default

    /**
     * DOCUMENT ME!
     */
    public final static int ESTIMATE = 32;

    /**
     * DOCUMENT ME!
     */
    public final static int NO_ESTIMATE = 0; //default

    /**
     * DOCUMENT ME!
     */
    public final static int MOUSE = 64;

    /**
     * DOCUMENT ME!
     */
    public final static int NO_MOUSE = 0; //default

    /**
     * DOCUMENT ME!
     */
    public final static int OUTPUT = 128;

    /**
     * DOCUMENT ME!
     */
    public final static int NO_OUTPUT = 0; //default

    /**
     * DOCUMENT ME!
     */
    public final static int KNOWN = 256;

    /**
     * DOCUMENT ME!
     */
    public final static int NO_KNOWN = 0; //default

    /* *
    * type contains the parameter type.  Its value is available via getType().
    * The types are: boolean,double,text, or int
    *        input/output or output only
    *        clickable
    *        estimable
    */
    int options;

    /**
     * DOCUMENT ME!
     */
    int type;

    //    int index;			    //not currently used
    /**
     * DOCUMENT ME!
     */
    boolean errorOption = true;

    /**
     * DOCUMENT ME!
     */
    boolean estimateOption = false;

    /**
     * DOCUMENT ME!
     */
    boolean fixOption = false;

    /**
     * DOCUMENT ME!
     */
    boolean mouseOption = false;

    /**
     * DOCUMENT ME!
     */
    boolean outputOption = false;

    /**
     * DOCUMENT ME!
     */
    boolean knownOption = false;

    //parameter name
    /**
     * DOCUMENT ME!
     */
    String name;

    //double fields
    /**
     * DOCUMENT ME!
     */
    double valueDbl;

    // error field
    /**
     * DOCUMENT ME!
     */
    double errorDbl;

    //int fields
    /**
     * DOCUMENT ME!
     */
    int valueInt;

    //boolean fields
    /**
     * DOCUMENT ME!
     */
    boolean valueBln;

    //TEXT fields
    /**
     * DOCUMENT ME!
     */
    String valueTxt;

    /**
     * DOCUMENT ME!
     */
    double known;

    /**
     * DOCUMENT ME!
     */
    double valueDefaultInt;

    /**
     * DOCUMENT ME!
     */
    double valueDefaultDbl;

    /** Whether or not the parameter is currently fixed. */
    protected boolean fix;

    /**
     * Whether or not this parameter should be estimated automatically
     * before doing fit.
     */
    protected boolean estimate;

    /**
     * Creates a new Parameter object.
     *
     * @param name DOCUMENT ME!
     * @param options DOCUMENT ME!
     */
    public Parameter(String name, int options) { //default variable parameter instance
        this.name = name;
        this.options = options;
        type = options & TYPE;

        if (type == Parameter.TEXT) {
            options |= NO_ERROR; //change default for TEXT
        }

        valueInt = 0;
        valueDbl = 0.0;
        errorDbl = 0.0;
        fix = false;
        estimate = false;

        //default type
        if (type == 0) {
            type = DOUBLE;
        }

        if (type == BOOLEAN) {
            errorOption = false;
        }

        if ((options & Parameter.NO_ERROR) != 0) {
            errorOption = false;
        }

        if ((options & Parameter.FIX) != 0) {
            fixOption = true;
        }

        if ((options & Parameter.ESTIMATE) != 0) {
            estimateOption = true;
        }

        if ((options & Parameter.MOUSE) != 0) {
            mouseOption = true;
        }

        if ((options & Parameter.OUTPUT) != 0) {
            outputOption = true;
        }

        if ((options & Parameter.KNOWN) != 0) {
            knownOption = true;
        }
    }

    /**
     * Creates a new Parameter object.
     *
     * @param name DOCUMENT ME!
     * @param option1 DOCUMENT ME!
     * @param option2 DOCUMENT ME!
     */
    public Parameter(String name, int option1, int option2) {
        this(name, option1 | option2);
    }

    /**
     * Creates a new Parameter object.
     *
     * @param name DOCUMENT ME!
     * @param option1 DOCUMENT ME!
     * @param option2 DOCUMENT ME!
     * @param option3 DOCUMENT ME!
     */
    public Parameter(String name, int option1, int option2, int option3) {
        this(name, option1 | option2 | option3);
    }

    /**
     * Creates a new Parameter object.
     *
     * @param name DOCUMENT ME!
     * @param option1 DOCUMENT ME!
     * @param option2 DOCUMENT ME!
     * @param option3 DOCUMENT ME!
     * @param option4 DOCUMENT ME!
     */
    public Parameter(String name, int option1, int option2, int option3,
        int option4) {
        this(name, option1 | option2 | option3 | option4);
    }

    /**
     * Creates a new Parameter object.
     *
     * @param name DOCUMENT ME!
     * @param option1 DOCUMENT ME!
     * @param option2 DOCUMENT ME!
     * @param option3 DOCUMENT ME!
     * @param option4 DOCUMENT ME!
     * @param option5 DOCUMENT ME!
     */
    public Parameter(String name, int option1, int option2, int option3,
        int option4, int option5) {
        this(name, option1 | option2 | option3 | option4 | option5);
    }

    /**
     * Creates a new Parameter object.
     *
     * @param name DOCUMENT ME!
     * @param option1 DOCUMENT ME!
     * @param option2 DOCUMENT ME!
     * @param option3 DOCUMENT ME!
     * @param option4 DOCUMENT ME!
     * @param option5 DOCUMENT ME!
     * @param option6 DOCUMENT ME!
     */
    public Parameter(String name, int option1, int option2, int option3,
        int option4, int option5, int option6) {
        this(name, option1 | option2 | option3 | option4 | option5 | option6);
    }

    //END OF CONSTRUCTORS
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getOptions() {
        return options;
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void setFix(boolean state) {
        this.fix = state;
    }

    /**
     * Tells whether the parameter is currently fixed.
     *
     * @return DOCUMENT ME!
     */
    public boolean isFix() {
        return fix;
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void setEstimate(boolean state) {
        estimate = state;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEstimate() {
        return (fix ? false : estimate);
    }

    /**
     * Set the floating point value.
     *
     * @param value DOCUMENT ME!
     *
     * @throws FitException thrown if unrecoverable error occurs
     */
    public void setValue(double value) throws FitException {
        if (type == DOUBLE) {
            valueDbl = value;
        } else {
            throw new FitException("Parameter '" + name +
                "' can't set a double value.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param error DOCUMENT ME!
     */
    public void setValue(double value, double error) {
        valueDbl = value;
        errorDbl = error;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void setValue(int value) {
        valueInt = value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setValue(String text) {
        valueTxt = text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag DOCUMENT ME!
     */
    public void setValue(boolean flag) {
        valueBln = flag;
    }

    /**
     * DOCUMENT ME!
     *
     * @param err DOCUMENT ME!
     */
    public void setError(double err) {
        errorDbl = err;
    }

    /**
     * DOCUMENT ME!
     *
     * @param inKnown DOCUMENT ME!
     */
    public void setKnown(double inKnown) {
        known = inKnown;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDoubleValue() {
        return valueDbl;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIntValue() {
        return valueInt;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getBooleanValue() {
        return valueBln;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDoubleError() {
        return errorDbl;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getKnown() {
        return known;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isBoolean() {
        return (type == BOOLEAN);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNumberField() {
        return ((type == INT) || (type == DOUBLE));
    }

    /**
     * Returns true if the parameter is represented by a text field in
     * the dialog box.
     *
     * @return DOCUMENT ME!
     */
    public boolean isTextField() {
        return ((type == INT) || (type == DOUBLE) || (type == TEXT));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean canBeEstimated() {
        return ((options & ESTIMATE) != 0);
    }
}
