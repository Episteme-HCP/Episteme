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

package org.jscience.linguistics.loaders.tigerxml;

import org.jscience.util.identity.Identified;
import org.jscience.linguistics.loaders.tigerxml.tools.GeneralTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a node in the syntax tree, either a terminal node or a
 * non-terminal node.
 * <p>
 * {@code GraphNode} is a generalization over {@link NT} (non-terminal) and 
 * {@link T} (terminal) nodes. It implements shared functionality such as 
 * tree traversal, path finding, and identification.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @version 2.0 (Modernized)
 * @see NT
 * @see T
 */
public abstract class GraphNode implements Identified<String>, Serializable {

    private static final long serialVersionUID = 1L;

    /** The unique identifier of the node. */
    private String id;

    /** The sentence this GraphNode belongs to. */
    private Sentence sentence;

    /** The immediate mother (parent) node. */
    private NT mother;

    /** The label of the edge connecting to the mother node. */
    private String edge2mother;

    /** Secondary mothers for non-tree (graph) structures. */
    private List<NT> secMothers;

    /** Edge labels for secondary mothers. */
    private List<String> secLabels;

    /** Verbosity level for diagnostics. */
    protected int verbosity = 0;

    /** Positional index in the sentence list. */
    private int index;

    /** Cached paths to other nodes in the tree. */
    private final Map<GraphNode, Path> node2path;

    /**
     * Creates an empty GraphNode.
     */
    protected GraphNode() {
        this.id = "";
        this.edge2mother = "";
        this.index = -1;
        this.node2path = new HashMap<>();
    }

    /**
     * Creates a GraphNode with specified properties.
     *
     * @param id          the node identifier.
     * @param sentence    the parent sentence.
     * @param mother      the mother node.
     * @param edge2mother the edge label to mother.
     * @param index       the position index.
     */
    protected GraphNode(String id, Sentence sentence, NT mother, String edge2mother, int index) {
        this.id = Objects.requireNonNull(id);
        this.sentence = sentence;
        this.mother = mother;
        this.edge2mother = edge2mother;
        this.index = index;
        this.node2path = new HashMap<>();
    }

    /**
     * Checks if this node is terminal.
     *
     * @return {@code true} if this is a terminal (T) node.
     */
    public abstract boolean isTerminal();

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = Objects.requireNonNull(id);
    }

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    public Corpus getCorpus() {
        return (sentence != null) ? sentence.getCorpus() : null;
    }

    public NT getMother() {
        return mother;
    }

    public void setMother(NT mother) {
        this.mother = mother;
    }

    public String getEdge2Mother() {
        return edge2mother;
    }

    public void setEdge2Mother(String edge2mother) {
        this.edge2mother = edge2mother;
    }

    /**
     * Adds a secondary mother with an edge label.
     *
     * @param mother the secondary mother.
     * @param label  the edge label.
     */
    public void setSecMother(NT mother, String label) {
        if (secMothers == null) {
            secMothers = new ArrayList<>();
            secLabels = new ArrayList<>();
        }
        secMothers.add(mother);
        secLabels.add(label);
    }

    public boolean hasSecMothers() {
        return secMothers != null && !secMothers.isEmpty();
    }

    public List<NT> getSecMothers() {
        return (secMothers != null) ? secMothers : List.of();
    }

    public String getEdge2SecMother(NT sMother) {
        if (hasSecMothers()) {
            int i = secMothers.indexOf(sMother);
            if (i >= 0) {
                return secLabels.get(i);
            }
        }
        return "";
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GraphNode)) return false;
        GraphNode other = (GraphNode) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean hasMother() {
        return mother != null;
    }

    /**
     * Returns the textual content of this node.
     *
     * @return the surface text.
     */
    public abstract String getText();

    public boolean isDominatedBy(String cat) {
        return getDominatingNodeByCat(cat) != null;
    }

    /**
     * Finds the nearest ancestor with the given category.
     *
     * @param cat the category to look for.
     * @return the dominating NT node, or {@code null}.
     */
    public NT getDominatingNodeByCat(String cat) {
        if (!isTerminal() && ((NT) this).getCat().equals(cat)) {
            return (NT) this;
        }
        NT current = mother;
        while (current != null) {
            if (current.getCat().equals(cat)) {
                return current;
            }
            current = current.getMother();
        }
        return null;
    }

    public Path getPath(GraphNode toNode) {
        return node2path.computeIfAbsent(toNode, n -> Path.makePath(this, n));
    }

    public boolean isDominatedBy(GraphNode node) {
        return getPath(node).start_below_end;
    }

    public boolean dominates(GraphNode node) {
        return getPath(node).end_below_start;
    }

    /**
     * Returns a string representation of the tree structure.
     *
     * @return tree string.
     */
    public String toTreeString() {
        StringBuilder sb = new StringBuilder();
        buildTreeString(0, sb);
        return sb.toString();
    }

    protected abstract void buildTreeString(int depth, StringBuilder sb);

    public int getVerbosity() {
        return verbosity;
    }

    public void setVerbosity(int verbosity) {
        this.verbosity = verbosity;
    }

    /**
     * Finds the leftmost terminal node in the subtree.
     *
     * @return the leftmost T node.
     */
    public T getLeftmostTerminal() {
        if (isTerminal()) {
            return (T) this;
        }
        List<T> terminals = ((NT) this).getTerminals();
        return (terminals != null && !terminals.isEmpty()) ? terminals.get(0) : null;
    }
}
