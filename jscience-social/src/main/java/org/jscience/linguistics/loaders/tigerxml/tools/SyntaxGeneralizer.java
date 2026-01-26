/*
 * SyntaxGeneralizer.java
 *
 * Created in November, 2003
 *
 * Copyright (C) 2003 Hajo Keffer <hajokeffer@coli.uni-sb.de>,
 *                    Oezguer Demir <oeze@coli.uni-sb.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jscience.linguistics.loaders.tigerxml.tools;

import org.jscience.linguistics.loaders.tigerxml.GraphNode;
import org.jscience.linguistics.loaders.tigerxml.NT;
import org.jscience.linguistics.loaders.tigerxml.T;

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
 * @author <a href="mailto:hajo_keffer@gmx.de">Hajo Keffer</a>
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @version 1.84 $Id: SyntaxGeneralizer.java,v 1.3 2007-10-23 18:21:44 virtualcall Exp $
 */
public class SyntaxGeneralizer {
    /**
     * DOCUMENT ME!
     */
    private static final String OTHER = "OTHER";

    /**
     * DOCUMENT ME!
     */
    private final Map<String, String> my_type2gen_type;

    /**
     * DOCUMENT ME!
     */
    private final Map<String, String> my_label2gen_label;

    /**
     * DOCUMENT ME!
     */
    private final Map<String, String> my_tag2gen_tag;

    /**
     * DOCUMENT ME!
     */
    private Pattern pattern;

    /**
     * DOCUMENT ME!
     */
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
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param tag DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param label DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param item DOCUMENT ME!
     * @param general_item DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPhraseType(NT node) {
        return this.getGeneralType(node.getCat());
    }

    /**
     * Returns the (general) POS tag of this terminal.
     *
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPos(T node) {
        return this.getGeneralTag(node.getPos());
    }
} // class SyntaxGeneralizer
