/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.linguistics.loaders.tigerxml;

import org.episteme.social.linguistics.loaders.tigerxml.core.NTBuilder;
import org.episteme.social.linguistics.loaders.tigerxml.tools.GeneralTools;
import org.episteme.social.linguistics.loaders.tigerxml.tools.SyncMMAX;
import org.w3c.dom.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a non-terminal node in a syntax tree.
 * <p>
 * Non-terminal nodes (NT) represent syntactic units (categories) such as 
 * NP (Noun Phrase), VP (Verb Phrase), etc. They contain other nodes (daughters) 
 * which can be either terminals (T) or non-terminals (NT).
 * </p>
 * * @see GraphNode
 * @see T
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NT extends GraphNode {

    private static final long serialVersionUID = 1L;

    /** The category attribute (e.g., "S", "NP", "VP"). */
    private String cat;

    /** Cache for terminal nodes subordinated to this node. */
    private List<T> terminals;

    /** List of primary children nodes. */
    private final List<GraphNode> daughters;

    /** List of secondary children nodes for non-tree edges. */
    private List<GraphNode> secDaughters;

    /** MMAX-compatible span string. */
    private String span;

    /**
     * Creates an empty NT node.
     */
    public NT() {
        super();
        this.daughters = new ArrayList<>();
        this.cat = "";
    }

    /**
     * Creates an NT node from a DOM element.
     *
     * @param ntElement the DOM element.
     * @param sent      the parent sentence.
     */
    public NT(Element ntElement, Sentence sent) {
        this();
        setSentence(sent);
        NTBuilder.buildNT(this, sent, ntElement);
    }

    /**
     * Creates an NT node from a DOM element with specified verbosity.
     */
    public NT(Element ntElement, Sentence sent, int verbosity) {
        this();
        setSentence(sent);
        this.verbosity = verbosity;
        NTBuilder.buildNT(this, sent, ntElement);
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    public void setCat(String cat) {
        this.cat = (cat != null) ? cat : "";
    }

    public String getCat() {
        return cat;
    }

    /**
     * Returns a compact MMAX-style span of all terminal IDs.
     *
     * @return the span string (e.g., "s1_1..s1_5").
     */
    public String getSpan() {
        if (span == null) {
            List<T> terms = getTerminals();
            if (terms == null || terms.isEmpty()) return "";
            
            String rawSpan = terms.stream()
                .map(t -> t.getId().toString())
                .collect(Collectors.joining(","));
            span = SyncMMAX.condenseSpan(rawSpan);
        }
        return span;
    }

    public void addDaughter(GraphNode daughter) {
        daughters.add(daughter);
    }

    public void addSecDaughter(GraphNode daughter) {
        if (secDaughters == null) {
            secDaughters = new ArrayList<>();
        }
        secDaughters.add(daughter);
    }

    /**
     * Returns primary daughters sorted by linear precedence.
     */
    public List<GraphNode> getDaughters() {
        return GeneralTools.sortNodes(new ArrayList<>(daughters));
    }

    public List<GraphNode> getSecDaughters() {
        return (secDaughters != null) ? GeneralTools.sortNodes(new ArrayList<>(secDaughters)) : List.of();
    }

    public List<GraphNode> getAllDaughters() {
        List<GraphNode> all = new ArrayList<>(daughters);
        if (secDaughters != null) {
            all.addAll(secDaughters);
        }
        return GeneralTools.sortNodes(all);
    }

    /**
     * Recursively collects all terminal nodes in this subtree.
     *
     * @return list of terminal nodes sorted by precedence.
     */
    public List<T> getTerminals() {
        if (terminals == null) {
            List<GraphNode> result = new ArrayList<>();
            collectTerminals(this, result);
            this.terminals = result.stream()
                .filter(n -> n instanceof T)
                .map(n -> (T) n)
                .distinct()
                .collect(Collectors.toList());
            this.terminals = GeneralTools.sortTerminals(new ArrayList<>(this.terminals));
        }
        return terminals;
    }

    private void collectTerminals(GraphNode node, List<GraphNode> result) {
        if (node.isTerminal()) {
            result.add(node);
        } else {
            for (GraphNode d : ((NT) node).daughters) {
                collectTerminals(d, result);
            }
        }
    }

    @Override
    public String getText() {
        return getTerminals().stream()
            .map(T::getWord)
            .collect(Collectors.joining(" "));
    }

    public List<GraphNode> getDaughtersByLabel(String label) {
        return getDaughters().stream()
            .filter(n -> n.getEdge2Mother().equals(label))
            .collect(Collectors.toList());
    }

    public List<GraphNode> getAllDaughtersByLabel(String label) {
        return getAllDaughters().stream()
            .filter(n -> n.getEdge2Mother().equals(label))
            .collect(Collectors.toList());
    }

    @Override
    protected void buildTreeString(int depth, StringBuilder sb) {
        for (int i = 0; i < depth; i++) {
            sb.append("|   ");
        }
        sb.append("|--").append(getEdge2Mother()).append("--");
        sb.append("[").append(cat).append("] \"").append(getText()).append("\" <").append(getId()).append(">\n");
        
        for (GraphNode d : getDaughters()) {
            d.buildTreeString(depth + 1, sb);
        }
    }

    public void print2XML(FileWriter out) throws IOException {
        out.write(" <nt id=\"" + getId() + "\" cat=\"" + cat + "\">\n");
        for (GraphNode d : daughters) {
            out.write("  <edge label=\"" + d.getEdge2Mother() + "\" idref=\"" + d.getId() + "\"/>\n");
        }
        out.write(" </nt>\n");
    }
}

