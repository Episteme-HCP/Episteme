/*
 * SyntaxTools.java
 *
 * Created on September 20, 2003, 0:18 AM
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
package org.jscience.ml.tigerxml.tools;

import org.jscience.ml.tigerxml.GraphNode;
import org.jscience.ml.tigerxml.NT;
import org.jscience.ml.tigerxml.T;

import java.util.ArrayList;


/**
 * Provides methods that define a number of higher-level linguistic concepts.
 * Included are &quot;subject&quot;, and &quot;voice&quot;. This class is for
 * static use.
 *
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @version 1.84 $Id: SyntaxTools.java,v 1.2 2007-10-21 17:47:09 virtualcall Exp $
 */
public class SyntaxTools {
    /**
     * Returns the preposition of a PP
     *
     * @param pp DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public String getPreposition(GraphNode pp) {
        SyntaxGeneralizer gen = new SyntaxGeneralizer();

        if (pp.isTerminal()) {
            T t = (T) pp;

            if (!gen.isCaseOf(t.getPos(), "PP")) {
                System.err.println(
                        "SyntaxGeneralizer: WARNING: Attempt to determine preposition of non-pp node " +
                                pp.getId());

                return "";
            }

            return t.getWord();
        } else {
            NT nt = (NT) pp;

            if (!gen.isCaseOf(nt.getCat(), "PP")) {
                System.err.println(
                        "SyntaxGeneralizer: WARNING: Attempt to determine preposition of non-pp node " +
                                pp.getId());

                return "";
            }

            ArrayList to_check = new ArrayList();
            to_check.add(nt.getDaughtersByLabel("PH"));
            to_check.add(nt.getDaughtersByLabel("AC"));
            to_check.add(nt.getDescendantsByLabel("PH"));
            to_check.add(nt.getDescendantsByLabel("AC"));

            for (int c = 0; c < to_check.size(); c++) {
                ArrayList next2check = (ArrayList) to_check.get(c);

                for (int j = 0; j < next2check.size(); j++) {
                    if (((GraphNode) (next2check.get(j))).isTerminal()) {
                        return (((T) next2check.get(j)).getWord());
                    }
                } // for j
            } // for c

            return "";
        } // else
    }

    /**
     * Generalizes a preposition. For example, &quot;am&quot;, &quot;ans&quot;,
     * and &quot;daran&quot; will all be generalized to &quot;an&quot;.
     *
     * @param prep DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public String generalizePreposition(String prep) {
        String an_forms = "^(daran|hieran|woran|an|am|ans)$";
        Lemma AN = new Lemma("an", an_forms);
        String auf_forms = "^(darauf|hierauf|worauf|auf|aufs)$";
        Lemma AUF = new Lemma("auf", auf_forms);
        String aus_forms = "^(daraus|hieraus|woraus|aus)$";
        Lemma AUS = new Lemma("aus", aus_forms);
        String bei_forms = "^(dabei|bei|beim)$";
        Lemma BEI = new Lemma("bei", bei_forms);
        String durch_forms = "^(dadurch|hierdurch|wodurch|durch|durchs)$";
        Lemma DURCH = new Lemma("durch", durch_forms);
        String fuer_forms = "^(daf�r|hierf�r|wof�r|f�r|f�rs)$";
        Lemma FUER = new Lemma("fuer", fuer_forms);
        String hinter_forms = "^(dahinter|hierhinter|wohinter|hinter|hinters|hintern|hinterm)$";
        Lemma HINTER = new Lemma("hinter", hinter_forms);
        String in_forms = "^(darin|hierin|worin|in|im|ins)$";
        Lemma IN = new Lemma("in", in_forms);
        String mit_forms = "^(damit|hiermit|womit|mit)$";
        Lemma MIT = new Lemma("mit", mit_forms);
        String nach_forms = "^(danach|hiernach|wonach|nach)$";
        Lemma NACH = new Lemma("nach", nach_forms);
        String neben_forms = "^(daneben|hierneben|woneben|neben|nebens|nebem)$";
        Lemma NEBEN = new Lemma("neben", neben_forms);
        String ueber_forms = "^(dar�ber|hier�ber|wor�ber|�ber|�bers|�berm|�bern)$";
        Lemma UEBER = new Lemma("�ber", ueber_forms);
        String unter_forms = "^(darunter|hierunter|worunter|unter|unterm|unters|untern)$";
        Lemma UNTER = new Lemma("unter", unter_forms);
        String von_forms = "^(davon|hiervon|wovon|von|vom)$";
        Lemma VON = new Lemma("von", von_forms);
        String vor_forms = "^(davor|hiervor|wovor|vor|vors|vorm)$";
        Lemma VOR = new Lemma("vor", vor_forms);
        String zu_forms = "^(dazu|zu|zum|zur)$";
        Lemma ZU = new Lemma("zu", zu_forms);
        String zwischen_forms = "^(dazwischen|hierzwischen|wozwischen|zwischen|zwischens)$";
        Lemma ZWISCHEN = new Lemma("zwischen", zwischen_forms);
        ArrayList german_preps = new ArrayList();
        german_preps.add(AN);
        german_preps.add(AUF);
        german_preps.add(AUS);
        german_preps.add(BEI);
        german_preps.add(DURCH);
        german_preps.add(FUER);
        german_preps.add(HINTER);
        german_preps.add(IN);
        german_preps.add(MIT);
        german_preps.add(NACH);
        german_preps.add(NEBEN);
        german_preps.add(UEBER);
        german_preps.add(UNTER);
        german_preps.add(VON);
        german_preps.add(VOR);
        german_preps.add(ZU);
        german_preps.add(ZWISCHEN);

        for (int i = 0; i < german_preps.size(); i++) {
            Lemma german_prep = (Lemma) german_preps.get(i);

            if (german_prep.hasWordForm(prep)) {
                return german_prep.getName();
            }
        } // for i

        return prep;
    } // method generalizePreposition

    /**
     * Returns the PP signature of the clause or NP the node belongs to. The PP
     * signature is an alphabetically ordered list of the (generalized)
     * prepositions of the PPs that occur in the same clause or NP as the
     * input node. If the node occurs in a nominal context, the NP will be
     * considered, otherwise the clause.
     *
     * @param node DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public ArrayList getPpSignature(GraphNode node) {
        SyntaxGeneralizer gen = new SyntaxGeneralizer();
        NT mother = null;

        // identify mother
        if (occursInNominalContext(node)) {
            mother = getDominatingNominalNode(node);
        } else {
            mother = getDominatingClausalNode(node);
        }

        ArrayList pp_list = new ArrayList();

        if (mother == null) {
            // no mother found
            return (pp_list);
        } else {
            // get all prepositions
            ArrayList daughters = mother.getDaughters();

            for (int i = 0; i < daughters.size(); i++) {
                GraphNode next_node = (GraphNode) daughters.get(i);

                if ((next_node.isTerminal() &&
                        gen.isCaseOf(((T) next_node).getPos(), "PP")) ||
                        ((!next_node.isTerminal()) &&
                                gen.isCaseOf(((NT) next_node).getCat(), "PP"))) {
                    String prep = getPreposition(next_node);
                    String gen_prep = generalizePreposition(prep);

                    // insert preposition found in the result
                    // list according to alphabetical order
                    for (int j = 0; j <= pp_list.size(); j++) {
                        // pp appended to end of list
                        if (j == pp_list.size()) {
                            pp_list.add(gen_prep);

                            break;
                        }
                        // pp inserted
                        else {
                            String next_prep = (String) pp_list.get(j);

                            if (prep.compareTo(next_prep) <= 0) {
                                pp_list.add(j, prep);

                                break;
                            }
                        }
                    } // for j
                }
            } // for i

            return pp_list;
        }
    } // method getPPSignature

    /**
     * Defines the term NP as it is commonly understood in terms of TIGER
     * syntax.
     *
     * @param node The node to be classified as NP or not NP.
     * @return True if node is an NP as commonly understood in terms of TIGER
     *         Syntax.
     */
    public static final boolean isNpLikeNode(NT node) {
        boolean value = false;

        if (node != null) {
            String cat = node.getCat();

            if ((cat.equals("CNP")) || (cat.equals("PN")) ||
                    (cat.equals("NM")) || (cat.equals("CPP")) ||
                    (cat.equals("MPN")) || (cat.equals("PP"))) {
                value = true;
            }

            if ((cat.equals("PP")) || (cat.equals("NP"))) {
                // nps or pps with placeholder elements
                // are not np-like items
                value = true;

                ArrayList daughters = node.getDaughters();

                for (int i = 0; i < daughters.size(); i++) {
                    GraphNode next_node = (GraphNode) daughters.get(i);

                    if ((next_node.getEdge2Mother()).equals("PH")) {
                        value = false;
                    } //if
                } // for
            }
        }

        return value;
    }

    /**
     * Returns the leftmost constituent of the Mittelfeld that belongs to the
     * verbal node.
     *
     * @param verbal_node DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public GraphNode getLeftmostConstituent(GraphNode verbal_node) {
        NT mother = getDominatingClausalNode(verbal_node);

        if (mother == null) {
            return null;
        } else {
            ArrayList con_list = mother.getDaughters();
            int cursor = (con_list.size() - 1);

            while (cursor >= 0) {
                GraphNode next_node = (GraphNode) con_list.get(cursor);

                if ((next_node.getEdge2Mother()).equals("HD")) {
                    cursor--;
                } else if ((!next_node.isTerminal()) &&
                        ((isVpLikeNode((NT) next_node)) ||
                                (isSLikeNode((NT) next_node)))) {
                    cursor--;
                } else {
                    return next_node;
                }
            } // while

            return null;
        } // else
    } // method getLeftmostConstituent

    /**
     * Returns the head word of the constituent. In case of &quot;an den
     * Kanzler&quot; this would be &quot;Kanzler&quot;.
     *
     * @param node DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public String getHeadWord(GraphNode node) {
        // if the node is a terminal return its word
        if (node.isTerminal()) {
            T t = (T) node;

            return t.getWord();
        } else {
            NT nt = (NT) node;

            // if it is an NP-like node
            // get the first nk descendant tagged np
            if (isNpLikeNode(nt)) {
                ArrayList nk_daughters = nt.getDescendantsByLabel("NK");
                SyntaxGeneralizer gen = new SyntaxGeneralizer();

                for (int i = 0; i < nk_daughters.size(); i++) {
                    GraphNode next_node = (GraphNode) nk_daughters.get(i);

                    if (next_node.isTerminal()) {
                        T t = (T) next_node;

                        if ((gen.getGeneralTag(t.getPos())).equals("NP")) {
                            return t.getWord();
                        }
                    }
                } // for i
            }

            // else
            // get the first terminal descendant labeled 'HD'
            ArrayList hd_daughters = nt.getDescendantsByLabel("HD");

            for (int i = 0; i < hd_daughters.size(); i++) {
                GraphNode next_node = (GraphNode) hd_daughters.get(i);

                if (next_node.isTerminal()) {
                    T t = (T) next_node;

                    return t.getWord();
                }
            } // for i
            // no heuristic left - return the lemma of the
            // node's last terminal

            ArrayList terminals = nt.getTerminals();

            if (terminals.size() < 1) {
                System.err.println("SyntaxGeneralizer: WARNING: " +
                        "non-terminal without terminals: " + node.getId());

                return "none";
            } else {
                T t = (T) terminals.get(terminals.size() - 1);

                return t.getWord();
            }
        }
    } // method getHeadWord

    /**
     * Defines the term S as it is commonly understood in terms of TIGER
     * syntax.
     *
     * @param node The node to be classified as S or not S.
     * @return True if node is an S as commonly understood in terms of TIGER
     *         Syntax.
     */
    public static final boolean isSLikeNode(NT node) {
        boolean value = false;
        String cat = node.getCat();

        if ((cat.equals("S")) || (cat.equals("CS")) || (cat.equals("DL"))) {
            value = true;
        }

        return value;
    }

    /**
     * Defines the term VP as it is commonly understood in terms of TIGER
     * syntax.
     *
     * @param node The node to be classified as VP or not VP.
     * @return True if node is an VP as commonly understood in terms of TIGER
     *         Syntax.
     */
    public static final boolean isVpLikeNode(NT node) {
        boolean value = false;
        String cat = node.getCat();

        if ((cat.equals("VP")) || (cat.equals("CVP"))) {
            value = true;
        }

        return value;
    }

    /**
     * Returns the nearest dominating clausal node of this GraphNode. It can be
     * either an S or a VP node as commonly understood. The method returns
     * null if there is no such node.
     *
     * @param node DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public NT getDominatingClausalNode(GraphNode node) {
        String s = "S";
        String vp = "VP";
        SyntaxGeneralizer gen = new SyntaxGeneralizer();

        if (gen.isDominatedBy(node, s)) {
            NT s_node = gen.getDominatingNode(node, s);

            if (gen.isDominatedBy(node, vp)) {
                NT vp_node = gen.getDominatingNode(node, vp);

                if (s_node.dominates(vp_node)) {
                    return vp_node;
                } else if (vp_node.dominates(s_node)) {
                    return s_node;
                } else {
                    System.err.println("SyntaxGeneralizer: " + "WARNING: " +
                            "Expected that either " + vp_node.getId() +
                            " dominates " + s_node.getId() + "or vice versa");

                    return s_node;
                }
            } else {
                return s_node;
            }
        } else if (gen.isDominatedBy(node, vp)) {
            NT vp_node = gen.getDominatingNode(node, vp);

            return vp_node;
        } else {
            return null;
        }
    }

    /**
     * Returns the nearest dominating nominal node of this GraphNode. It will
     * be an NP as commonly understood. The method returns null if there is no
     * such node.
     *
     * @param node DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public NT getDominatingNominalNode(GraphNode node) {
        SyntaxGeneralizer gen = new SyntaxGeneralizer();
        String np = "NP";
        String pp = "PP";

        if (gen.isDominatedBy(node, pp)) {
            NT pp_node = gen.getDominatingNode(node, pp);

            if (gen.isDominatedBy(node, np)) {
                NT np_node = gen.getDominatingNode(node, np);

                if (pp_node.dominates(np_node)) {
                    return np_node;
                } else if (np_node.dominates(pp_node)) {
                    return pp_node;
                } else {
                    System.err.println("SyntaxGeneralizer: " + "WARNING: " +
                            "Expected that either " + pp_node.getId() +
                            " dominates " + np_node.getId() + " or vice versa");

                    return np_node;
                }
            } else {
                return pp_node;
            }
        } else if (gen.isDominatedBy(node, np)) {
            NT np_node = gen.getDominatingNode(node, np);

            return np_node;
        } else {
            return null;
        }
    }

    /**
     * Returns true if the nearest dominating clausal node is nearer than the
     * nearest dominating nominal node.
     *
     * @param node DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean occursInNominalContext(GraphNode node) {
        NT dnn = getDominatingNominalNode(node);

        if (dnn == null) {
            return false;
        } else {
            NT dcn = getDominatingClausalNode(node);

            if (dcn == null) {
                return true;
            } else {
                if (dnn.dominates(dcn)) {
                    return false;
                } else if (dcn.dominates(dnn)) {
                    return true;
                } else {
                    System.err.println("SyntaxGeneralizer: " + "WARNING: " +
                            "Expected that either " + dnn.getId() + " dominates " +
                            dcn.getId() + " or vice versa");

                    return false;
                }
            }
        }
    }

    /**
     * Returns true if the verbal node has the same argument domain as the
     * other node. Having the same argument domain is defined as follows: a
     * verbal node and another node have the same argument domain if the
     * shortest path leading from the verbal node to the other does not cross
     * an s node and does not cross a vp node from above to below. Usually,
     * all nodes the content of which enters as an argument in the relation
     * expressed by the verb are syntactically located within the argument
     * domain of the verb.
     *
     * @param verbal_node this node is supposed to be a full verb node like
     *                    &quot;(zu) sagen&quot;, &quot;gesagt&quot;, or
     *                    &quot;sagte&quot;.
     * @param other_node  this can be any GraphNode.
     * @return a truth value indicating whether the second node belongs to the
     *         argument domain of the first.
     */
    public boolean haveSameArgumentDomain(GraphNode verbal_node,
                                          GraphNode other_node) {
        NT verbal_dom = getDominatingClausalNode(verbal_node);
        NT other_dom = getDominatingClausalNode(other_node);

        if ((verbal_dom == null) || ((other_dom) == null)) {
            return false;
        } else {
            if (isSLikeNode(verbal_dom)) {
                return (verbal_dom.equals(other_dom));
            } else if (isVpLikeNode(verbal_dom)) {
                if (isVpLikeNode(other_dom)) {
                    return (verbal_dom.equals(other_dom));
                } else if (isSLikeNode(other_dom)) {
                    SyntaxGeneralizer gen = new SyntaxGeneralizer();

                    if (gen.isDominatedBy(verbal_dom, "S")) {
                        NT next_dom = gen.getDominatingNode(verbal_dom, "S");

                        return (next_dom.equals(other_dom));
                    } else {
                        return false;
                    }
                } else {
                    System.err.println("SyntaxTools: WARNING: " + "node " +
                            other_dom.getId() + " should be clausal node");

                    return false;
                }
            } else {
                System.err.println("SyntaxTools: WARNING: " + "node " +
                        other_dom.getId() + " should be clausal node");

                return false;
            }
        }
    }

    /**
     * Returns true if the input node is a full verb node and occurs in an
     * active clause.
     * <p/>
     * <p/>
     * E.g. in case of &quot;Peter glaubt, gerufen zu werden&quot; it returns
     * <code>true</code> for &quot;glaubt&quot; and <code>false</code> for
     * &quot;gerufen&quot;.
     * </p>
     * <p/>
     * <p/>
     * The method is meant to be applied to full verb terminals like
     * &quot;sagen&quot;, &quot;sagte&quot;, &quot;gesagt&quot; and so forth,
     * but not to non-full-verb terminals like &quot;Peter&quot; oder
     * &quot;werden&quot;.
     * </p>
     *
     * @param verbal_node DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean hasActiveVoice(T verbal_node) {
        String werden_forms = "wirst|wird|werd(e|en|et|est)|wurd(e|est|en|et)|werden(d|der|des|de|den|dem)|geworde(n|ner|nes|ne|nen|nem)";
        Lemma WERDEN = new Lemma("werden", werden_forms);
        String vp = "VP";

        if ((verbal_node.getPos()).equals("VVPP")) {
            SyntaxGeneralizer gen = new SyntaxGeneralizer();

            if (gen.isDominatedBy(verbal_node, "S")) {
                NT s = gen.getDominatingNode(verbal_node, "S");
                ArrayList terminals = s.getTerminals();

                for (int i = 0; i < terminals.size(); i++) {
                    T next_terminal = (T) terminals.get(i);
                    String next_word = next_terminal.getWord();

                    if (WERDEN.hasWordForm(next_word)) {
                        if (haveSameArgumentDomain(verbal_node, next_terminal)) {
                            return false;
                        }
                    } //for i
                }
            }
        }

        return true;
    }

    /**
     * Returns the subject of the sentence the node belongs to. Note that this
     * will always be the subject of a higher structure. That is, if applied
     * to &quot;dass Doris verliebt ist&quot; in &quot;Frank glaubt, dass
     * Doris verliebt ist&quot;&quot; it will return &quot;Frank&quot;, not
     * &quot;Doris&quot;. The method will return <code>null</code> if there is
     * no subject as in &quot;VfL Bochum-Eintracht Frankfurt 3:2&quot;.
     *
     * @param node DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public GraphNode getSubject(GraphNode node) {
        ArrayList subj_list = new ArrayList();
        SyntaxGeneralizer gen = new SyntaxGeneralizer();
        NT s;

        if (gen.isDominatedBy(node, "S")) {
            s = gen.getDominatingNode(node, "S");
            subj_list = gen.getDaughtersByGeneralLabel(s, "SB");

            for (int i = 0; i < subj_list.size(); i++) {
                GraphNode next_node = (GraphNode) subj_list.get(i);

                if (haveSameArgumentDomain(node, next_node)) {
                    return next_node;
                }
            } // for i
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param terminal DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    protected static boolean isSubstitutingMarkablePronoun(T terminal) {
        boolean value = false;
        String pos = terminal.getPos();

        //Relative pronouns are excluded from being markable pronouns
        if ((pos.equals("PPER")) || (pos.equals("PIS")) || (pos.equals("PWS")) ||
                (pos.equals("PPOSS")) || (pos.equals("PRF")) ||
                (pos.equals("PDS"))) {
            value = true;
        }

        return value;
    }

    /**
     * Returns true if the node is a personal pronoun like &quot;er&quot;,
     * &quot;mir&quot; and so forth.
     *
     * @param terminal DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static final boolean isPronoun(T terminal) {
        boolean value = false;
        String pos = terminal.getPos();

        if (pos.equals("PPER")) {
            value = true;
        }

        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param terminal DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    protected static boolean isAttributiveMarkablePronoun(T terminal) {
        boolean value = false;
        String pos = terminal.getPos();

        if (pos.equals("PPOSAT")) {
            value = true;
        }

        return value;
    }

    /**
     * Defines the term noun as it is commonly understood in terms of TIGER
     * syntax.
     *
     * @param terminal The node to be classified as noun or not a noun.
     * @return True if node is a noun as commonly understood in terms of TIGER
     *         syntax.
     */
    public static final boolean isNoun(T terminal) {
        boolean value = false;
        String pos = terminal.getPos();

        if ((pos.equals("NE")) || (pos.equals("NNE")) || (pos.equals("NN"))) {
            value = true;
        }

        return value;
    }

    /**
     * Returns true if the input node is an apposition.
     *
     * @param node The node to be classified as apposition.
     * @return True if node is an apposition.
     */
    public static final boolean isApposition(GraphNode node) {
        boolean value = false;
        NT mother = node.getMother();
        String label = "";

        if (mother != null) {
            label = node.getEdge2Mother();
        }

        if (label.equals("APP")) {
            value = true;
        }

        return value;
    }
} // class
