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
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
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
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class GraphNode implements Identified<Identification>, Serializable {

    private static final long serialVersionUID = 1L;

    /** The unique identifier of the node. */
    private Identification id;

    private Sentence sentence;
    private NT mother;
    private String edge2mother;
    private List<NT> secMothers;
    private List<String> secLabels;
    protected int verbosity = 0;
    private int index;
    private final Map<GraphNode, Path> node2path;

    protected GraphNode() {
        this.id = new SimpleIdentification("");
        this.edge2mother = "";
        this.index = -1;
        this.node2path = new HashMap<>();
    }

    protected GraphNode(String id, Sentence sentence, NT mother, String edge2mother, int index) {
        this.id = new SimpleIdentification(Objects.requireNonNull(id));
        this.sentence = sentence;
        this.mother = mother;
        this.edge2mother = edge2mother;
        this.index = index;
        this.node2path = new HashMap<>();
    }

    public abstract boolean isTerminal();

    @Override
    public Identification getId() {
        return id;
    }

    public void setId(String id) {
        this.id = new SimpleIdentification(Objects.requireNonNull(id));
    }
    
    public void setId(Identification id) {
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
        return id.toString();
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

    public abstract String getText();

    public boolean isDominatedBy(String cat) {
        return getDominatingNodeByCat(cat) != null;
    }

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

    public T getLeftmostTerminal() {
        if (isTerminal()) {
            return (T) this;
        }
        List<T> terminals = ((NT) this).getTerminals();
        return (terminals != null && !terminals.isEmpty()) ? terminals.get(0) : null;
    }
}
