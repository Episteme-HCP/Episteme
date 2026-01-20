package org.jscience.economics;

import java.util.Set;


//human process some tasks,
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public interface TaskProcessor {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getResources();

    //returns true if the task is consumed
    /**
     * DOCUMENT ME!
     *
     * @param task DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean consumeResources(Task task);
}
