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

package org.jscience.ml.tigerxml;

import java.util.ArrayList;


/**
 * Represents a path leading through the syntax tree that connects two
 * nodes. To instantiate this class use {@link #getPathToTop(GraphNodenode)}
 * or {@link #makePath(GraphNodestart_node,GraphNodeend_node)}.
 *
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @version 1.84 $Id: Path.java,v 1.3 2007-10-23 18:21:34 virtualcall Exp $
 */
public class Path {
    /** DOCUMENT ME! */
    protected ArrayList steps;

    /** DOCUMENT ME! */
    private int cursor;

    /** DOCUMENT ME! */
    protected boolean start_below_end;

    /** DOCUMENT ME! */
    protected boolean end_below_start;

    /**
     * The higher this value the more process and debug information
     * will written to stderr.
     */
    protected int verbosity = 0;

/**
     * Creates a new Path object.
     *
     * @param node DOCUMENT ME!
     */
    private Path(GraphNode node) {
        steps = new ArrayList();

        Step step = new Step(node, Step.UNDEF, this.verbosity);
        steps.add(step);
        start_below_end = false;
        end_below_start = false;
    }

/**
     * Creates a new Path object.
     *
     * @param node      DOCUMENT ME!
     * @param verbosity DOCUMENT ME!
     */
    private Path(GraphNode node, int verbosity) {
        this.verbosity = verbosity;
        steps = new ArrayList();

        Step step = new Step(node, Step.UNDEF, this.verbosity);
        steps.add(step);
        start_below_end = false;
        end_below_start = false;
    }

    /**
     * Returns a string representation of this <tt>Path</tt>.
     *
     * @return The string representation of this <tt>Path</tt>.
     */
    public String toString() {
        Step first_step = this.getFirstStep();
        StringBuffer string_buffer = new StringBuffer((first_step.getNode()).getId());

        while (this.hasNext()) {
            string_buffer.append("-");

            Step next_step = this.getNextStep();
            string_buffer.append(next_step.getDirection());
            string_buffer.append("->");
            string_buffer.append((next_step.getNode()).getId());
        } // while loop

        return string_buffer.toString();
    }

    /**
     * Returns a string representation of this <tt>Path</tt> similar to
     * that used by Daniel Gildea.
     *
     * @return A string representation of this <tt>Path</tt> similar to that
     *         used by Daniel Gildea.
     */
    public String toGildeaStyleString() {
        Step first_step = this.getFirstStep();
        StringBuffer string_buffer = new StringBuffer();
        String cat = "";

        while (this.hasNext()) {
            if (!cat.equals("")) {
                string_buffer.append(cat);
                string_buffer.append("-");
            }

            Step next_step = this.getNextStep();

            string_buffer.append(next_step.getDirection());
            string_buffer.append("->");

            if (this.hasNext()) {
                if (!((next_step.getNode()).isTerminal())) {
                    NT nt = (NT) next_step.getNode();
                    cat = nt.getCat();
                } else {
                    cat = "";
                }
            }
        } // while loop

        return string_buffer.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean oneArgumentDomain() {
        boolean came_to_s_from_above = false;
        boolean came_to_s_from_below = false;
        boolean came_to_vp_from_above = false;
        Step current_step = this.getFirstStep();

        while (this.hasNext()) {
            GraphNode current_node = current_step.getNode();
            String current_cat = "";

            if (!current_node.isTerminal()) {
                current_cat = ((NT) current_node).getCat();
            }

            String current_direction = current_step.getDirection();

            if (came_to_s_from_below) {
                if (current_direction.equals(Step.UP)) {
                    return false;
                } else {
                    came_to_s_from_below = false;
                }
            }

            if (came_to_s_from_above) {
                if (current_direction.equals(Step.DOWN)) {
                    return false;
                } else {
                    came_to_s_from_above = false;
                }
            }

            if (came_to_vp_from_above) {
                if (current_direction.equals(Step.DOWN)) {
                    return false;
                } else {
                    came_to_vp_from_above = false;
                }
            }

            if (current_cat.equals("S")) {
                if (current_direction.equals(Step.DOWN)) {
                    came_to_s_from_above = true;
                }

                if (current_direction.equals(Step.UP)) {
                    came_to_s_from_below = true;
                }
            }

            if (current_cat.equals("VP")) {
                if (current_direction.equals(Step.DOWN)) {
                    came_to_vp_from_above = true;
                }
            }

            current_step = this.getNextStep();
        } // while

        return true;
    }

    /**
     * This static method creates a path leading from the input node to
     * the root node of the sentence. Use this method or {@link
     * #makePath(GraphNodestart_node,GraphNodeend_node)} to instantiate this
     * class.
     *
     * @param node The <tt>GraphNode</tt> to start the path from.
     *
     * @return A <tt>Path</tt> instance representing a path from <tt>node</tt>
     *         to the root node.
     */
    public static final Path getPathToTop(GraphNode node) {
        Path path = new Path(node);

        while (node.hasMother()) {
            node = node.getMother();
            path.suffixNode(node, Step.UP);
            path.start_below_end = true;
            path.end_below_start = false;
        }

        return path;
    }

    /**
     * This static method creates a path leading from the input node to
     * the root node of the sentence. Use this method or {@link
     * #makePath(GraphNodestart_node,GraphNodeend_node)} to instantiate this
     * class.
     *
     * @param node The <tt>GraphNode</tt> to start the path from.
     * @param verbosity DOCUMENT ME!
     *
     * @return A <tt>Path</tt> instance representing a path from <tt>node</tt>
     *         to the root node.
     */
    public static final Path getPathToTop(GraphNode node, int verbosity) {
        Path path = new Path(node, verbosity);

        while (node.hasMother()) {
            node = node.getMother();
            path.suffixNode(node, Step.UP);
            path.start_below_end = true;
            path.end_below_start = false;
        }

        return path;
    }

    /**
     * This static method returns the shortest path that connects the
     * two nodes.
     *
     * @param start_node The first <tt>GraphNode</tt> to connect.
     * @param end_node The second <tt>GraphNode</tt> to connect.
     *
     * @return A <tt>Path</tt> instance representing the shortest path between
     *         <tt>start_node</tt> and <tt>end_node</tt>
     */
    public static final Path makePath(GraphNode start_node, GraphNode end_node) {
        Path start = getPathToTop(start_node);
        Path end = getPathToTop(end_node);
        Step last_start = start.getLastStep();
        Step last_end = end.getLastStep();

        if (!last_start.hasEqualNode(last_end)) {
            if ((start.getVerbosity() > 0) || (end.getVerbosity() > 0)) {
                System.err.println(
                    "org.jscience.ml.tigerxml.Path: WARNING: cannot connect node " +
                    start_node.getId() + " and " + end_node.getId());
            } // if verbosity

            return (new Path(start_node));
        } // if hasEqualNode
        else {
            while (true) {
                if (!end.hasPrev()) {
                    if (start.hasPrev()) {
                        start.start_below_end = true;
                    }

                    return start;
                } else if (!start.hasPrev()) {
                    if (end.hasPrev()) {
                        end.end_below_start = true;
                    }

                    end.invert();

                    return end;
                } else {
                    Step prev_end = end.getPrevStep();
                    end.removeLast();

                    Step prev_start = start.getPrevStep();
                    boolean one_is_first_node = (prev_end.isFirstNode() ||
                        prev_start.isFirstNode());
                    boolean first_and_equal = (one_is_first_node &&
                        prev_end.hasEqualNode(prev_start));

                    if (first_and_equal || (prev_start.equals(prev_end))) {
                        start.removeLast();
                    } else {
                        end.invert();
                        start.append(end, Step.DOWN);
                        start.start_below_end = false;
                        start.end_below_start = false;

                        return start;
                    }
                }
            } // while
        }
    } // method makePath

    /**
     * Appends a <tt>Path</tt> to this <tt>Path</tt> setting the
     * direction attribute (<tt>String</tt>) to <tt>direction</tt>.
     *
     * @param path The <tt>Path</tt> to be appended.
     * @param direction A <tt>String</tt> denoting the direction, ("up" or
     *        "down")
     */
    private void append(Path path, String direction) {
        Step first = path.getFirstStep();
        this.suffixNode(first.getNode(), direction);

        while (path.hasNext()) {
            (this.steps).add(path.getNextStep());
        } // while loop
    } // method append

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @param dir DOCUMENT ME!
     */
    private void suffixNode(GraphNode node, String dir) {
        (this.steps).add(new Step(node, dir, this.verbosity));
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @param dir DOCUMENT ME!
     */
    private void prefixNode(GraphNode node, String dir) {
        Step first = this.getFirstStep();
        first.setDirection(dir);
        (this.steps).add(0, (new Step(node, Step.UNDEF, this.verbosity)));
    }

    /**
     * Inverts this path by reversing the order of the nodes and
     * re-setting their directions (UP --> DOWN; DOWN --> UP).
     */
    public void invert() {
        int to_swap = (steps.size() - 1);

        for (int i = 0; i < steps.size(); i++) {
            Step current = (Step) steps.get(i);
            current.invertDirection();

            if (i < to_swap) {
                GraphNode tmp = current.getNode();
                Step swap = (Step) steps.get(to_swap);
                current.setNode(swap.getNode());
                swap.setNode(tmp);
            }

            to_swap--;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Step getPrevStep() {
        cursor--;

        return (Step) steps.get(cursor);
    }

    /**
     * Returns the previous <tt>GraphNode</tt> in this <tt>Path</tt>
     * relative to the current position of this <tt>Path</tt> (iteration based
     * method).
     *
     * @return The previous <tt>GraphNode</tt> in this <tt>Path</tt>.
     */
    public GraphNode getPrev() {
        return getPrevStep().getNode();
    }

    /**
     * Checks if there are more <tt>GraphNode</tt>s available on this
     * path before the current cursor position of this instance.
     *
     * @return True iff there are more nodes on this path left to the cursor.
     */
    public boolean hasPrev() {
        return (cursor >= 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Step getNextStep() {
        cursor++;

        return (Step) steps.get(cursor);
    }

    /**
     * Returns the next <tt>GraphNode</tt> in this <tt>Path</tt>
     * relative to the current position of this <tt>Path</tt> (iteration based
     * method).
     *
     * @return The next <tt>GraphNode</tt> in this <tt>Path</tt>.
     */
    public GraphNode getNext() {
        return getNextStep().getNode();
    }

    /**
     * Checks if there are more <tt>GraphNode</tt>s available on this
     * path after the current cursor position of this instance.
     *
     * @return True iff there are more nodes on this path right to the cursor.
     */
    public boolean hasNext() {
        return (cursor < (steps.size() - 1));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Step getFirstStep() {
        cursor = 0;

        return (Step) steps.get(cursor);
    }

    /**
     * Returns the first <tt>GraphNode</tt> in this <tt>Path</tt>.
     *
     * @return The first <tt>GraphNode</tt> in this <tt>Path</tt>.
     */
    public GraphNode getFirst() {
        return ((Step) steps.get(0)).getNode();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Step getLastStep() {
        cursor = (steps.size() - 1);

        return (Step) steps.get(cursor);
    }

    /**
     * Returns the last <tt>GraphNode</tt> in this <tt>Path</tt>.
     *
     * @return The last <tt>GraphNode</tt> in this <tt>Path</tt>.
     */
    public GraphNode getLast() {
        return ((Step) steps.get(steps.size() - 1)).getNode();
    }

    /**
     * DOCUMENT ME!
     */
    public void removeFirst() {
        steps.remove(0);
    }

    /**
     * DOCUMENT ME!
     */
    public void removeLast() {
        steps.remove(steps.size() - 1);
    }

    /**
     * Gets the currently set level of verbosity of this instance. The
     * higher the value the more information is written to stderr.
     *
     * @return The level of verbosity.
     */
    public int getVerbosity() {
        return this.verbosity;
    }

    /**
     * Sets the currently set level of verbosity of this instance. The
     * higher the value the more information is written to stderr.
     *
     * @param verbosity The level of verbosity.
     */
    public void setVerbosity(int verbosity) {
        this.verbosity = verbosity;
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class Step {
        /** DOCUMENT ME! */
        protected static final String UP = "up";

        /** DOCUMENT ME! */
        protected static final String DOWN = "down";

        /** DOCUMENT ME! */
        protected static final String UNDEF = "";

        /** DOCUMENT ME! */
        private GraphNode node;

        /** DOCUMENT ME! */
        private String direction;

/**
         * Creates a new Step object.
         *
         * @param new_node      DOCUMENT ME!
         * @param new_direction DOCUMENT ME!
         * @param verbosity     DOCUMENT ME!
         */
        Step(GraphNode new_node, String new_direction, int verbosity) {
            setNode(new_node);
            setDirection(new_direction);
        } // constructor Step

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        GraphNode getNode() {
            return node;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        String getDirection() {
            return direction;
        }

        /**
         * DOCUMENT ME!
         *
         * @param new_node DOCUMENT ME!
         */
        void setNode(GraphNode new_node) {
            node = new_node;
        }

        /**
         * DOCUMENT ME!
         *
         * @param new_direction DOCUMENT ME!
         */
        void setDirection(String new_direction) {
            direction = new_direction;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        boolean isFirstNode() {
            return (direction.equals(UNDEF));
        }

        /**
         * DOCUMENT ME!
         *
         * @param step DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        boolean equals(Step step) {
            return (((this.getNode()).equals(step.getNode())) &&
            ((this.getDirection()).equals(step.getDirection())));
        } // method equals

        /**
         * DOCUMENT ME!
         *
         * @param step DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        boolean hasEqualNode(Step step) {
            return ((this.getNode()).equals(step.getNode()));
        } // method equals

        /**
         * DOCUMENT ME!
         */
        void invertDirection() {
            if (direction.equals(UP)) {
                direction = DOWN;
            } else if (direction.equals(DOWN)) {
                direction = UP;
            } else if (!direction.equals(UNDEF)) {
                if (verbosity > 0) {
                    System.err.println("org.jscience.ml.tigerxml.Path.Step: " +
                        "WARNING: Encountered unknown direction label");
                }
            }
        } // method invertDirection
    } // class Step
} // class Path
