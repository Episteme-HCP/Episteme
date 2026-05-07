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

package org.episteme.social.linguistics.loaders.tigerxml.tools;

import org.episteme.social.linguistics.loaders.tigerxml.GraphNode;
import org.episteme.social.linguistics.loaders.tigerxml.NT;
import org.episteme.social.linguistics.loaders.tigerxml.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The purpose of this class is to generalize over some distinctions made
 * in Tiger Syntax. The distinctions concern phrase type, part of speech and
 * grammatical function.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SyntaxGeneralizer {
    /** Represents a generic or unknown category for generalization. */
    private static final String OTHER = "OTHER";

    /** Mapping from specific phrase types to generalized types. */
    private final Map<String, String> my_type2gen_type;

    /** Mapping from specific edge labels to generalized labels. */
    private final Map<String, String> my_label2gen_label;

    /** Mapping from specific POS tags to generalized tags. */
    private final Map<String, String> my_tag2gen_tag;

    /** Reusable regex pattern for generalization matching. */
    private Pattern pattern;

    /** Reusable regex matcher for generalization matching. */
    private Matcher matcher;

/**
     * Creates a SyntaxGeneralizer object with user-definned
     * generalization settings.
     */
    public SyntaxGeneralizer(Map<String, String> type2gen_type, Map<String, String> label2gen_label,
        Map<String, String> tag2gen_tag) {
        my_type2gen_type = type2gen_type;
        my_tag2gen_tag = tag2gen_tag;
        my_label2gen_label = label2gen_label;
    }

/**
     * Creates a SyntaxGeneralizer object with predefinned
     * generalization settings.
     */
    public SyntaxGeneralizer() {
        my_type2gen_type = new HashMap<>();
        my_type2gen_type.put("^(NP|CNP|NM|PN)$", "NP");
        my_type2gen_type.put("^(PP|CPP)$", "PP");
        my_type2gen_type.put("^(AVP|AA|CAVP)$", "AVP");
        my_type2gen_type.put("^(AP|MTA|CAP)$", "AP");
        my_type2gen_type.put("^(CVP|VP)$", "VP");
        my_type2gen_type.put("^(S|CS|DL)$", "S");

        my_tag2gen_tag = new HashMap<>();
        my_tag2gen_tag.put("^(NN|NE|NNE|PNC|PRF|PDS|PIS|PPER|PPOS|PRELS|PWS)$", "NP");
        my_tag2gen_tag.put("^(PROAV|PWAV)$", "PP");
        my_tag2gen_tag.put("^(ADJA|PDAT|PIAT|PPOSAT|PRELAT|PWAT|PRELS)$", "AP");
        my_tag2gen_tag.put("^(ADJD|ADV)$", "AVP");

        my_label2gen_label = new HashMap<>();
        my_label2gen_label.put("^(EP|SB|SP)$", "SB");
        my_label2gen_label.put("^DA$", "DA");
        my_label2gen_label.put("^(OA|OA2)$", "OA");
        my_label2gen_label.put("^OG$", "OG");
        my_label2gen_label.put("^OP$", "OP");
        my_label2gen_label.put("^NK$", "NK");
        my_label2gen_label.put("^HD$", "HD");
        my_label2gen_label.put("^(PD|CVC|MO|SBP|AMS|CC)$", "MO");
        my_label2gen_label.put("^(GR|GL|AG|PG|MNR)$", "MNR");
        my_label2gen_label.put("^RC$", "RC");
        my_label2gen_label.put("^(OC|RE|RS|DH)$", "OC");
        my_label2gen_label.put("^DA$", "DA");
    }

    /**
     * Generalizes a phrase type using the internal mapping.
     *
     * @param type the specific phrase type to generalize
     *
     * @return the generalized phrase type, or "OTHER" if no match is found
     */
    protected String getGeneralType(String type) {
        for (String regex : my_type2gen_type.keySet()) {
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(type);

            if (matcher.matches()) {
                return my_type2gen_type.get(regex);
            }
        }

        return OTHER;
    }

    /**
     * Generalizes a POS tag using the internal mapping.
     *
     * @param tag the specific POS tag to generalize
     *
     * @return the generalized POS tag, or "OTHER" if no match is found
     */
    protected String getGeneralTag(String tag) {
        for (String regex : my_tag2gen_tag.keySet()) {
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(tag);

            if (matcher.matches()) {
                return my_tag2gen_tag.get(regex);
            }
        }

        return OTHER;
    }

    /**
     * Generalizes an edge label using the internal mapping.
     *
     * @param label the specific edge label to generalize
     *
     * @return the generalized label, or "OTHER" if no match is found
     */
    protected String getGeneralLabel(String label) {
        for (String regex : my_label2gen_label.keySet()) {
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(label);

            if (matcher.matches()) {
                return my_label2gen_label.get(regex);
            }
        }

        return OTHER;
    }

    /**
     * Checks if an item (type, label, or tag) matches a specific general category.
     *
     * @param item         the item to check
     * @param general_item the general category to match against
     *
     * @return true if the item belongs to the general category, false otherwise
     */
    protected boolean isCaseOf(String item, String general_item) {
        if ((getGeneralLabel(item)).equals(general_item)) {
            return true;
        }

        if ((getGeneralType(item)).equals(general_item)) {
            return true;
        }

        if ((getGeneralTag(item)).equals(general_item)) {
            return true;
        }

        return false;
    }

    /**
     * Returns a List of the daughter nodes with a given general
     * edge label. The list is ordered left to right according to word order.
     */
    public List<GraphNode> getDaughtersByGeneralLabel(NT node, String gen_label) {
        List<GraphNode> return_daughters = new ArrayList<>();
        List<GraphNode> daughters = node.getDaughters();

        for (GraphNode next_node : daughters) {
            if (this.isCaseOf(next_node.getEdge2Mother(), gen_label)) {
                return_daughters.add(next_node);
            }
        }

        return GeneralTools.sortNodes(return_daughters);
    }

    /**
     * Returns a List of the descendant nodes with a given
     * general edge label. The descendants are ordered breadth first then left
     * to right.
     */
    public List<GraphNode> getDescendantsByGeneralLabel(NT node, String gen_label) {
        List<GraphNode> return_daughters = new ArrayList<>(this.getDaughtersByGeneralLabel(node, gen_label));
        List<GraphNode> agenda = new ArrayList<>(node.getDaughters());

        while (!(agenda.isEmpty())) {
            GraphNode next_node = agenda.remove(0);

            if (!(next_node.isTerminal())) {
                NT nt = (NT) next_node;
                return_daughters.addAll(this.getDaughtersByGeneralLabel(nt, gen_label));
                agenda.addAll(nt.getDaughters());
            }
        }

        return return_daughters;
    }

    /**
     * Returns true if there is a dominating node that has the general
     * category &quot;cat&quot;
     */
    public boolean isDominatedBy(GraphNode node, String gen_cat) {
        return (getDominatingNode(node, gen_cat) != null);
    }

    /**
     * Returns the nearest dominating node that has the general
     * category gen_cat, and is not identical with the input node itself. The
     * method returns null if there is no such node.
     */
    public NT getDominatingNode(GraphNode node, String gen_cat) {
        if (!(node.hasMother())) {
            return null;
        } else {
            NT mother = node.getMother();

            while (mother != null) {
                if (this.isCaseOf(mother.getCat(), gen_cat)) {
                    return mother;
                }

                mother = mother.getMother();
            } //while

            return null;
        }
    } // method getDominatingNode()

    /**
     * Returns the (general) grammatical function of this node.
     *
     * @param node the node to inspect
     *
     * @return the generalized grammatical function (edge label)
     */
    public String getGrammaticalFunction(GraphNode node) {
        if (node.hasMother()) {
            String edge = node.getEdge2Mother();
            String gen_edge = this.getGeneralLabel(edge);

            return gen_edge;
        } else {
            return OTHER;
        }
    }

    /**
     * Returns the (general) phrase type of this node.
     *
     * @param node the non-terminal node to inspect
     *
     * @return the generalized phrase type
     */
    public String getPhraseType(NT node) {
        return this.getGeneralType(node.getCat());
    }

    /**
     * Returns the (general) POS tag of this terminal.
     *
     * @param node the terminal node to inspect
     *
     * @return the generalized POS tag
     */
    public String getPos(T node) {
        return this.getGeneralTag(node.getPos());
    }
} // class SyntaxGeneralizer

