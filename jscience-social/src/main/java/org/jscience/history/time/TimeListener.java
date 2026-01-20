package org.jscience.history.time;

import java.util.EventListener;


/**
   * A class representing a way to have time related events.
   *
   * @author Silvere Martin-Michiellot
   * @version 1.0
   */
public interface TimeListener extends EventListener {
    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void timeChanged(TimeEvent event);
}
