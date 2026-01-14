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
