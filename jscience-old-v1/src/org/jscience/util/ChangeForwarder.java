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
 * This is a ChangeListener that is designed to adapt events of one type from
 * one source to events of another type emitted by another source. For example,
 * you could adapt events made by edits in a database to being events fired by
 * a sequence implementation.
 *
 * @author Matthew Pocock
 * @since 1.1
 */
public class ChangeForwarder implements ChangeListener {
    private final Object source;
    private final transient ChangeSupport changeSupport;

    /**
     * Create a new ChangeForwarder for forwarding events.
     *
     * @param source        the new source Object
     * @param changeSupport the ChangeSupport managing the listeners
     */
    public ChangeForwarder(Object source, ChangeSupport changeSupport) {
        this.source = source;
        this.changeSupport = changeSupport;
    }

    /**
     * Retrieve the 'source' object for <code>ChangeEvent</code>s fired by this forwarder.
     *
     * @return the source Object
     */
    public Object getSource() {
        return source;
    }

    /**
     * Return the underlying <code>ChangeSupport</code> instance that can be used to
     * fire <code>ChangeEvent</code>s and mannage listeners.
     *
     * @return the ChangeSupport delegate
     */
    public ChangeSupport changeSupport() {
        return changeSupport;
    }

    /**
     * <p/>
     * Return the new event to represent the originating event ce.
     * </p>
     * <p/>
     * <p/>
     * The returned ChangeEvent is the event that will be fired, and should be
     * built from information in the original event. If it is null, then no event
     * will be fired.
     * </p>
     * <p/>
     * <p/>
     * The default implementation just constructs a ChangeEvent of the same type
     * that chains back to ce.
     * </p>
     *
     * @param ce the originating ChangeEvent
     * @return a new ChangeEvent to pass on, or null if no event should be sent
     * @throws ChangeVetoException if for any reason this event can't be handled
     */
    protected ChangeEvent generateEvent(ChangeEvent ce)
            throws ChangeVetoException {
        return new ChangeEvent(getSource(), ce.getType(), null, null, ce);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ce DOCUMENT ME!
     * @throws ChangeVetoException DOCUMENT ME!
     */
    public void preChange(ChangeEvent ce) throws ChangeVetoException {
        ChangeEvent nce = generateEvent(ce);

        if (nce != null) {
            // todo: this should be coupled with the synchronization in postChange
            synchronized (changeSupport) {
                changeSupport.firePreChangeEvent(nce);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param ce DOCUMENT ME!
     */
    public void postChange(ChangeEvent ce) {
        try {
            ChangeEvent nce = generateEvent(ce);

            if (nce != null) {
                // todo: this should be coupled with the synchronization in preChange
                synchronized (changeSupport) {
                    changeSupport.firePostChangeEvent(nce);
                }
            }
        } catch (ChangeVetoException cve) {
            throw new AssertionFailure("Assertion Failure: Change was vetoed after it had been accepted by preChange",
                    cve);
        }
    }

    /**
     * A ChangeForwarder that systematically uses a given type and wraps the old
     * event.
     *
     * @author Matthew Pocock
     * @since 1.4
     */
    public static class Retyper extends ChangeForwarder {
        private final ChangeType type;

        /**
         * Create a new Retyper for forwarding events.
         *
         * @param source        the new source Object
         * @param changeSupport the ChangeSupport managing the listeners
         * @param type          the new ChangeType
         */
        public Retyper(Object source, ChangeSupport changeSupport,
                       ChangeType type) {
            super(source, changeSupport);

            this.type = type;
        }

        public ChangeType getType() {
            return type;
        }

        protected ChangeEvent generateEvent(ChangeEvent ce)
                throws ChangeVetoException {
            return new ChangeEvent(getSource(), getType(), null, null, ce);
        }
    }
}
