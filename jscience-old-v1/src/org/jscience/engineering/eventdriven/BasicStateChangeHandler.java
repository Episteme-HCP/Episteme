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

package org.jscience.engineering.eventdriven;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * <p/>
 * Provides a basic reporting mechanism for logging state changes during
 * run-time. This allows for some basic debugging of state models and would
 * not normally be used in production code, so by default no handler is
 * associated with an Engine and there will be no output.
 * </p>
 * <p/>
 * <p/>
 * This handler reports state change information on the standard output stream.
 * </p>
 *
 * @author Pete Ford, May 31, 2005 &copy;Pete Ford &amp; CodeXombie.com 2005
 */

//**********************************************
//
//This package is rebundled after the code from JSpasm
//
// Project Homepage : http://jspasm.sourceforge.net/
// Original Developer : Pete Ford
// Official Domain : CodeXombie.com
//
//**********************************************

public class BasicStateChangeHandler implements IStateChangeHandler {
    /**
     * <p/>
     * Date formatter.
     * </p>
     */
    DateFormat df;

    /**
     * <p/>
     * Constructor.
     * </p>
     */
    public BasicStateChangeHandler() {
        df = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS] ");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jscience.engineering.eventdriven.IStateChangeHandler#handleStateChange(org.jscience.engineering.eventdriven.StateChangeRecord)
     */
    public void handleStateChange(StateChangeRecord scr) {
        String record = df.format(scr.timeStamp) + scr.entityId + ": " +
                scr.startStateId + " + " + scr.eventSpecId + "(" +
                objectList(scr.eventArgs) + ")" +
                transitionTypeString(scr.transitionType) + " -> " + scr.endStateId;
        System.out.println(record);
    }

    /**
     * <p/>
     * Creates a String representation of an array of Objects.
     * </p>
     *
     * @param list A list of Object references.
     * @return a comma-separated String.
     */
    private static String objectList(Object[] list) {
        StringBuffer sb = new StringBuffer();

        for (int index = 0; index < list.length; index++) {
            if (index > 0) {
                sb.append(", ");
            }

            sb.append(list[index]);
        }

        return sb.toString();
    }

    /**
     * <p/>
     * Returns a shorthand representation of a transition type code.
     * </p>
     *
     * @param transitionType The transition type code.
     * @return an equivalent String representation.
     */
    private static String transitionTypeString(int transitionType) {
        String str = "";

        switch (transitionType) {
            case ITransitionType.DO_NOT_EXECUTE:
                str = " (DNX)";

                break;

            case ITransitionType.EXCURSION:
                str = " (EXC)";

                break;

            case ITransitionType.IGNORE:
                str = " (IGN)";

                break;

            case ITransitionType.NORMAL:
                break;
        }

        return str;
    }
}
