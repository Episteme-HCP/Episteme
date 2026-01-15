package org.jscience.ml.cml.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;


/**
 * provides a console logger for Jumbo. this may become obsolete
 */
class JumboConsoleHandler extends ConsoleHandler {
/**
     * Creates a new JumboConsoleHandler object.
     */
    public JumboConsoleHandler() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param record DOCUMENT ME!
     */
    public void publish(LogRecord record) {
        //System.out.println(record.getLevel()+"/"+record.getMessage());
    }

    /**
     * DOCUMENT ME!
     */
    public void flush() {
        System.out.flush();
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        super.close();

        //        System.out.println("CLOSECONSOLE");
    }
}
