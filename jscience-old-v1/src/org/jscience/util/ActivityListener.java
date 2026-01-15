/*
 * BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 * http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 * http://www.biojava.org
 */
package org.jscience.util;

/**
 * Interface for object which monitor long-running activities. (may be subject
 * to change before 1.2-final)
 *
 * @author Thomas Down
 * @since 1.2
 */
public interface ActivityListener {
    /**
     * Notification that an activity has started.
     *
     * @param source DOCUMENT ME!
     */
    public void startedActivity(Object source);

    /**
     * Notification that an activity is complete.
     *
     * @param source DOCUMENT ME!
     */
    public void completedActivity(Object source);

    /**
     * Notification of errors behind the scenes.
     *
     * @param source DOCUMENT ME!
     * @param ex DOCUMENT ME!
     */
    public void activityFailed(Object source, Exception ex);

    /**
     * Estimated progress of an activity.  This indicated that
     * <code>current</code> parts of the activity have been completed, out of
     * a target <code>target</code>.
     *
     * @param source DOCUMENT ME!
     * @param current DOCUMENT ME!
     * @param target DOCUMENT ME!
     */
    public void activityProgress(Object source, int current, int target);
}
