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

package org.jscience.ml.tigerxml.tools;

import org.jscience.ml.tigerxml.GraphNode;
import org.jscience.ml.tigerxml.NT;
import org.jscience.ml.tigerxml.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private final String OTHER = "OTHER";

    /**
     * DOCUMENT ME!
     */
    private HashMap my_type2gen_type;

    /**
     * DOCUMENT ME!
     */
    private HashMap my_label2gen_label;

    /**
     * DOCUMENT ME!
     */
    private HashMap my_tag2gen_tag;

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
     * Each SyntaxGeneralizer is initialized with three
     * HashMaps. The HashMaps map strings that are used to
     * create
     * regular expressions into strings that represent general
     * linguisticallly relevant categories.
     * The hash maps are used to group together concepts of
     * Tiger syntax and thereby abstract away from
     * their distinctions. <p>
     * The first hash maps Tiger phrase type designators
     * (like &quot;S&quot;, &quot;CS&quot;, &quot;CVP&quot; and so forth)
     * into general phrase type labels. For example you
     * might find it useful to group together the two Tiger
     * phrase type tags &quot;S&quot; and &quot;CS&quot; into
     * a single general phrase type tag called &quot;S&quot;. Then
     * your first hash should contain the mapping from
     * &quot;^(S|CS)$&quot; to &quot;S&quot;. <p>
     * The second hash maps Tiger edge labels
     * (like &quot;SB&quot;, &quot;HD&quot;, and &quot;MO&quot;)
     * into general edge labels. <p>
     * The third hash maps Tiger part of speech tags
     * (like &quot;NN&quot;, &quot;NE&quot;, and &quot;VVFIN&quot;
     * into general POS tags. <p>
     * Types, labels or tags that are not taken into
     * account by the hash maps will automatically
     * mapped onto a tag  &quot;OTHER&quot;.
     *
     * @see java.util.regex.Pattern
     */
    public SyntaxGeneralizer(HashMap type2gen_type, HashMap label2gen_label,
        HashMap tag2gen_tag) {
        my_type2gen_type = type2gen_type;
        my_tag2gen_tag = tag2gen_tag;
        my_label2gen_label = label2gen_label;
    }

/**
     * Creates a SyntaxGeneralizer object with predefinned
     * generalization settings.
     * As for phrase type,
     * &quot;NP&quot;, &quot;CNP&quot;,
     * &quot;NM&quot;, and &quot;PN&quot; are mapped onto &quot;NP&quot;;
     * &quot;PP&quot;, and &quot;CPP&quot; are mapped onto &quot;PP&quot;;
     * &quot;AVP&quot;, &quot;AA&quot;, and &quot;CAVP&quot; are mapped onto &quot;AVP&quot;;
     * &quot;AP&quot;, &quot;MTA&quot;, and &quot;CAP&quot; are mapped onto &quot;AP&quot;;
     * &quot;CVP&quot;, and &quot;VP&quot; are mapped onto &quot;VP&quot;;
     * &quot;S&quot;, &quot;CS&quot;, and &quot;DL&quot; are mapped onto &quot;S&quot;.
     * <p/>
     * As for edge label,
     * &quot;NN&quot;, &quot;NE&quot;, &quot;NNE&quot;, &quot;PNC&quot;, &quot;PRF&quot;, &quot;PDS&quot;, &quot;PIS&quot;, &quot;PPER&quot;, &quot;PPOS&quot;, &quot;PRELS&quot;, and &quot;PWS&quot; are mapped onto &quot;NP&quot;;
     * &quot;PROAV&quot;, and &quot;PWAV&quot; are mapped onto &quot;PP&quot;
     * &quot;ADJA&quot;, &quot;PDAT&quot;, &quot;PIAT&quot;, &quot;PPOSAT&quot;, &quot;PRELAT&quot;, &quot;PWAT&quot;, and &quot;PRELS&quot; are mapped onto &quot;AP&quot;;
     * &quot;ADJD&quot;, and &quot;ADV&quot; are mapped onto &quot;AVP&quot;;
     * <p/>
     * As for POS tag,
     * &quot;EP&quot;, &quot;SB&quot;, and &quot;SP&quot; are mapped onto &quot;SB&quot;;
     * &quot;DA&quot; is mapped onto &quot;DA&quot;;
     * &quot;OA&quot;, and &quot;OA2&quot; are mapped onto &quot;OA&quot;;
     * &quot;OG&quot; is mapped onto &quot;OG&quot;;
     * &quot;OP&quot; is mapped onto &quot;OP&quot;;
     * &quot;NK&quot; is mapped onto &quot;NK&quot;;
     * &quot;HD&quot; is mapped onto &quot;HD&quot;;
     * &quot;PD&quot;, &quot;CVC&quot;, &quot;MO&quot;, &quot;SBP&quot;, &quot;AMS&quot;, and &quot;CC&quot; is mapped onto &quot;MO&quot;;
     * &quot;GR&quot;, &quot;GL&quot;, &quot;AG&quot;, &quot;PG&quot;, and &quot;MNR&quot; are mapped onto &quot;MNR&quot;;
     * &quot;RC&quot; is mapped onto &quot;RC&quot;;
     * &quot;OC&quot;, &quot;RE&quot;, &quot;RS&quot;, and &quot;DH&quot; are mapped onto &quot;OC&quot;;
     * &quot;DA&quot; is mapped onto &quot;DA&quot;.
     */
    public SyntaxGeneralizer() {
        HashMap type2gen_type = new HashMap();
        type2gen_type.put("^(NP|CNP|NM|PN)$", "NP");
        type2gen_type.put("^(PP|CPP)$", "PP");
        type2gen_type.put("^(AVP|AA|CAVP)$", "AVP");
        type2gen_type.put("^(AP|MTA|CAP)$", "AP");
        type2gen_type.put("^(CVP|VP)$", "VP");
        type2gen_type.put("^(S|CS|DL)$", "S");
        my_type2gen_type = type2gen_type;

        HashMap tag2gen_tag = new HashMap();
        tag2gen_tag.put("^(NN|NE|NNE|PNC|PRF|PDS|PIS|PPER|PPOS|PRELS|PWS)$",
            "NP");
        tag2gen_tag.put("^(PROAV|PWAV)$", "PP");
        tag2gen_tag.put("^(ADJA|PDAT|PIAT|PPOSAT|PRELAT|PWAT|PRELS)$", "AP");
        tag2gen_tag.put("^(ADJD|ADV)$", "AVP");
        my_tag2gen_tag = tag2gen_tag;

        HashMap label2gen_label = new HashMap();
        label2gen_label.put("^(EP|SB|SP)$", "SB");
        label2gen_label.put("^DA$", "DA");
        label2gen_label.put("^(OA|OA2)$", "OA");
        label2gen_label.put("^OG$", "OG");
        label2gen_label.put("^OP$", "OP");
        label2gen_label.put("^NK$", "NK");
        label2gen_label.put("^HD$", "HD");
        label2gen_label.put("^(PD|CVC|MO|SBP|AMS|CC)$", "MO");
        label2gen_label.put("^(GR|GL|AG|PG|MNR)$", "MNR");
        label2gen_label.put("^RC$", "RC");
        label2gen_label.put("^(OC|RE|RS|DH)$", "OC");
        label2gen_label.put("^DA$", "DA");
        my_label2gen_label = label2gen_label;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getGeneralType(String type) {
        Iterator iterator = (my_type2gen_type.keySet()).iterator();

        while (iterator.hasNext()) {
            String regex = (String) iterator.next();
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(type);

            if (matcher.matches()) {
                return (String) my_type2gen_type.get(regex);
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
        Iterator iterator = (my_tag2gen_tag.keySet()).iterator();

        while (iterator.hasNext()) {
            String regex = (String) iterator.next();
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(tag);

            if (matcher.matches()) {
                return (String) my_tag2gen_tag.get(regex);
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
        Iterator iterator = (my_label2gen_label.keySet()).iterator();

        while (iterator.hasNext()) {
            String regex = (String) iterator.next();
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(label);

            if (matcher.matches()) {
                return (String) my_label2gen_label.get(regex);
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
     * Returns an ArrayList of the daughter nodes with a given general
     * edge label. The list is ordered left to right according to word order.
     *
     * @param node DOCUMENT ME!
     * @param gen_label DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getDaughtersByGeneralLabel(NT node, String gen_label) {
        ArrayList return_daughters = new ArrayList();
        ArrayList daughters = node.getDaughters();

        for (int i = 0; i < daughters.size(); i++) {
            GraphNode next_node = (GraphNode) daughters.get(i);

            if (this.isCaseOf(next_node.getEdge2Mother(), gen_label)) {
                return_daughters.add(next_node);
            }
        } // for i

        return GeneralTools.sortNodes(return_daughters);
    }

    /**
     * Returns an ArrayList of the descendant nodes with a given
     * general edge label. The descendants are ordered breadth first then left
     * to right.
     *
     * @param node DOCUMENT ME!
     * @param gen_label DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getDescendantsByGeneralLabel(NT node, String gen_label) {
        ArrayList return_daughters = new ArrayList(this.getDaughtersByGeneralLabel(
                    node, gen_label));
        ArrayList agenda = new ArrayList(node.getDaughters());

        while (!(agenda.isEmpty())) {
            GraphNode next_node = (GraphNode) agenda.remove(0);

            if (!(next_node.isTerminal())) {
                NT nt = (NT) next_node;
                return_daughters.addAll(this.getDaughtersByGeneralLabel(nt,
                        gen_label));
                agenda.addAll(nt.getDaughters());
            }
        }

        return return_daughters;
    }

    /**
     * Returns true if there is a dominating node that has the general
     * category &quot;cat&quot;
     *
     * @param node DOCUMENT ME!
     * @param gen_cat DOCUMENT ME!
     *
     * @return A truth value indicating whether there is a dominating cat node.
     */
    public boolean isDominatedBy(GraphNode node, String gen_cat) {
        return (getDominatingNode(node, gen_cat) != null);
    }

    /**
     * Returns the nearest dominating node that has the general
     * category gen_cat, and is not identical with the input node itself. The
     * method returns null if there is no such node.
     *
     * @param node DOCUMENT ME!
     * @param gen_cat DOCUMENT ME!
     *
     * @return The nearest node with &quot;cat&quot; that dominates this node.
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
        String string = this.getGeneralType(node.getCat());

        return string;
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
