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
