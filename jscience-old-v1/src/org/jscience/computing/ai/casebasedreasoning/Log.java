package org.jscience.computing.ai.casebasedreasoning;

/**
 * A temp class to help me centralize all my writes to stdout
 * <p/>
 * Doesn't need to be instantiated - just call Log.log
 */
public class Log {
    private static int messageNumber = 0;

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public static void log(String message) {
        messageNumber++;

        System.out.println(messageNumber + ". " + message);
    }
}
