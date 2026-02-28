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
import java.util.List;


/**
 * Provides methods that define a number of higher-level linguistic concepts.
 * Included are &quot;subject&quot;, and &quot;voice&quot;. This class is for
 * static use.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SyntaxTools {

    private final SyntaxGeneralizer gen = new SyntaxGeneralizer();

    /**
     * Returns the preposition of a PP
     *
     * @param pp the PP node
     * @return the preposition word
     */
    public String getPreposition(GraphNode pp) {
        if (pp.isTerminal()) {
            T t = (T) pp;

            if (!gen.isCaseOf(t.getPos(), "PP")) {
                System.err.println("SyntaxGeneralizer: WARNING: Attempt to determine preposition of non-pp node " + pp.getId());
                return "";
            }

            return t.getWord();
        } else {
            NT nt = (NT) pp;

            if (!gen.isCaseOf(nt.getCat(), "PP")) {
                System.err.println("SyntaxGeneralizer: WARNING: Attempt to determine preposition of non-pp node " + pp.getId());
                return "";
            }

            List<List<GraphNode>> to_check = new ArrayList<>();
            to_check.add(nt.getDaughtersByLabel("PH"));
            to_check.add(nt.getDaughtersByLabel("AC"));
            to_check.add(nt.getDescendantsByLabel("PH"));
            to_check.add(nt.getDescendantsByLabel("AC"));

            for (List<GraphNode> next2check : to_check) {
                for (GraphNode node : next2check) {
                    if (node.isTerminal()) {
                        return (((T) node).getWord());
                    }
                }
            }

            return "";
        }
    }

    /**
     * Generalizes a preposition. For example, &quot;am&quot;, &quot;ans&quot;,
     * and &quot;daran&quot; will all be generalized to &quot;an&quot;.
     *
     * @param prep the preposition form
     * @return the generalized lemma
     */
    public String generalizePreposition(String prep) {
        List<Lemma> german_preps = new ArrayList<>();
        german_preps.add(new Lemma("an", "^(daran|hieran|woran|an|am|ans)$"));
        german_preps.add(new Lemma("auf", "^(darauf|hierauf|worauf|auf|aufs)$"));
        german_preps.add(new Lemma("aus", "^(daraus|hieraus|woraus|aus)$"));
        german_preps.add(new Lemma("bei", "^(dabei|bei|beim)$"));
        german_preps.add(new Lemma("durch", "^(dadurch|hierdurch|wodurch|durch|durchs)$"));
        german_preps.add(new Lemma("fuer", "^(dafÃ¼r|hierfÃ¼r|wofÃ¼r|fÃ¼r|fÃ¼rs)$"));
        german_preps.add(new Lemma("hinter", "^(dahinter|hierhinter|wohinter|hinter|hinters|hintern|hinterm)$"));
        german_preps.add(new Lemma("in", "^(darin|hierin|worin|in|im|ins)$"));
        german_preps.add(new Lemma("mit", "^(damit|hiermit|womit|mit)$"));
        german_preps.add(new Lemma("nach", "^(danach|hiernach|wonach|nach)$"));
        german_preps.add(new Lemma("neben", "^(daneben|hierneben|woneben|neben|nebens|nebem)$"));
        german_preps.add(new Lemma("Ã¼ber", "^(darÃ¼ber|hierÃ¼ber|worÃ¼ber|Ã¼ber|Ã¼bers|Ã¼berm|Ã¼bern)$"));
        german_preps.add(new Lemma("unter", "^(darunter|hierunter|worunter|unter|unterm|unters|untern)$"));
        german_preps.add(new Lemma("von", "^(davon|hiervon|wovon|von|vom)$"));
        german_preps.add(new Lemma("vor", "^(davor|hiervor|wovor|vor|vors|vorm)$"));
        german_preps.add(new Lemma("zu", "^(dazu|zu|zum|zur)$"));
        german_preps.add(new Lemma("zwischen", "^(dazwischen|hierzwischen|wozwischen|zwischen|zwischens)$"));

        for (Lemma german_prep : german_preps) {
            if (german_prep.hasWordForm(prep)) {
                return german_prep.getName();
            }
        }

        return prep;
    }

    /**
     * Returns the PP signature of the clause or NP the node belongs to.
     */
    public List<String> getPpSignature(GraphNode node) {
        NT mother;

        // identify mother
        if (occursInNominalContext(node)) {
            mother = getDominatingNominalNode(node);
        } else {
            mother = getDominatingClausalNode(node);
        }

        List<String> pp_list = new ArrayList<>();

        if (mother == null) {
            return pp_list;
        } else {
            List<GraphNode> daughters = mother.getDaughters();

            for (GraphNode next_node : daughters) {
                if ((next_node.isTerminal() && gen.isCaseOf(((T) next_node).getPos(), "PP")) ||
                        ((!next_node.isTerminal()) && gen.isCaseOf(((NT) next_node).getCat(), "PP"))) {
                    String prep = getPreposition(next_node);
                    String gen_prep = generalizePreposition(prep);

                    // insert alphabetically
                    int index = 0;
                    while (index < pp_list.size() && gen_prep.compareTo(pp_list.get(index)) > 0) {
                        index++;
                    }
                    pp_list.add(index, gen_prep);
                }
            }

            return pp_list;
        }
    }

    /**
     * Defines the term NP as it is commonly understood in terms of TIGER
     * syntax.
     */
    public static boolean isNpLikeNode(NT node) {
        if (node == null) return false;
        String cat = node.getCat();

        if (cat.equals("CNP") || cat.equals("PN") || cat.equals("NM") || 
            cat.equals("CPP") || cat.equals("MPN") || cat.equals("PP") || cat.equals("NP")) {
            
            // nps or pps with placeholder elements are not np-like items
            for (GraphNode next_node : node.getDaughters()) {
                if (next_node.getEdge2Mother().equals("PH")) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns the leftmost constituent of the Mittelfeld that belongs to the
     * verbal node.
     */
    public GraphNode getLeftmostConstituent(GraphNode verbal_node) {
        NT mother = getDominatingClausalNode(verbal_node);

        if (mother == null) {
            return null;
        } else {
            List<GraphNode> con_list = mother.getDaughters();
            int cursor = (con_list.size() - 1);

            while (cursor >= 0) {
                GraphNode next_node = con_list.get(cursor);

                if (next_node.getEdge2Mother().equals("HD")) {
                    cursor--;
                } else if (!next_node.isTerminal() && (isVpLikeNode((NT) next_node) || isSLikeNode((NT) next_node))) {
                    cursor--;
                } else {
                    return next_node;
                }
            }

            return null;
        }
    }

    /**
     * Returns the head word of the constituent. In case of &quot;an den
     * Kanzler&quot; this would be &quot;Kanzler&quot;.
     */
    public String getHeadWord(GraphNode node) {
        if (node.isTerminal()) {
            return ((T) node).getWord();
        } else {
            NT nt = (NT) node;

            if (isNpLikeNode(nt)) {
                List<GraphNode> nk_daughters = nt.getDescendantsByLabel("NK");
                for (GraphNode next_node : nk_daughters) {
                    if (next_node.isTerminal()) {
                        T t = (T) next_node;
                        if (gen.getGeneralTag(t.getPos()).equals("NP")) {
                            return t.getWord();
                        }
                    }
                }
            }

            List<GraphNode> hd_daughters = nt.getDescendantsByLabel("HD");
            for (GraphNode next_node : hd_daughters) {
                if (next_node.isTerminal()) {
                    return ((T) next_node).getWord();
                }
            }

            List<T> terminals = nt.getTerminals();
            if (terminals.isEmpty()) {
                System.err.println("SyntaxGeneralizer: WARNING: non-terminal without terminals: " + node.getId());
                return "none";
            } else {
                return terminals.get(terminals.size() - 1).getWord();
            }
        }
    }

    /**
     * Defines the term S as it is commonly understood in terms of TIGER
     * syntax.
     */
    public static boolean isSLikeNode(NT node) {
        if (node == null) return false;
        String cat = node.getCat();
        return cat.equals("S") || cat.equals("CS") || cat.equals("DL");
    }

    /**
     * Defines the term VP as it is commonly understood in terms of TIGER
     * syntax.
     */
    public static boolean isVpLikeNode(NT node) {
        if (node == null) return false;
        String cat = node.getCat();
        return cat.equals("VP") || cat.equals("CVP");
    }

    /**
     * Returns the nearest dominating clausal node of this GraphNode.
     */
    public NT getDominatingClausalNode(GraphNode node) {
        String s = "S";
        String vp = "VP";

        if (gen.isDominatedBy(node, s)) {
            NT s_node = gen.getDominatingNode(node, s);

            if (gen.isDominatedBy(node, vp)) {
                NT vp_node = gen.getDominatingNode(node, vp);

                if (s_node.dominates(vp_node)) {
                    return vp_node;
                } else if (vp_node.dominates(s_node)) {
                    return s_node;
                } else {
                    return s_node;
                }
            } else {
                return s_node;
            }
        } else if (gen.isDominatedBy(node, vp)) {
            return gen.getDominatingNode(node, vp);
        } else {
            return null;
        }
    }

    /**
     * Returns the nearest dominating nominal node of this GraphNode.
     */
    public NT getDominatingNominalNode(GraphNode node) {
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
                    return np_node;
                }
            } else {
                return pp_node;
            }
        } else if (gen.isDominatedBy(node, np)) {
            return gen.getDominatingNode(node, np);
        } else {
            return null;
        }
    }

    /**
     * Returns true if the nearest dominating clausal node is nearer than the
     * nearest dominating nominal node.
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
                    return false;
                }
            }
        }
    }

    /**
     * Returns true if the verbal node has the same argument domain as the
     * other node.
     */
    public boolean haveSameArgumentDomain(GraphNode verbal_node, GraphNode other_node) {
        NT verbal_dom = getDominatingClausalNode(verbal_node);
        NT other_dom = getDominatingClausalNode(other_node);

        if (verbal_dom == null || other_dom == null) {
            return false;
        } else {
            if (isSLikeNode(verbal_dom)) {
                return (verbal_dom.equals(other_dom));
            } else if (isVpLikeNode(verbal_dom)) {
                if (isVpLikeNode(other_dom)) {
                    return (verbal_dom.equals(other_dom));
                } else if (isSLikeNode(other_dom)) {
                    if (gen.isDominatedBy(verbal_dom, "S")) {
                        NT next_dom = gen.getDominatingNode(verbal_dom, "S");
                        return (next_dom.equals(other_dom));
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    /**
     * Returns true if the input node is a full verb node and occurs in an
     * active clause.
     */
    public boolean hasActiveVoice(T verbal_node) {
        Lemma WERDEN = new Lemma("werden", "wirst|wird|werd(e|en|et|est)|wurd(e|est|en|et)|werden(d|der|des|de|den|dem)|geworde(n|ner|nes|ne|nen|nem)");

        if ((verbal_node.getPos()).equals("VVPP")) {
            if (gen.isDominatedBy(verbal_node, "S")) {
                NT s = gen.getDominatingNode(verbal_node, "S");
                List<T> terminals = s.getTerminals();

                for (T next_terminal : terminals) {
                    if (WERDEN.hasWordForm(next_terminal.getWord())) {
                        if (haveSameArgumentDomain(verbal_node, next_terminal)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Returns the subject of the sentence the node belongs to.
     */
    public GraphNode getSubject(GraphNode node) {
        if (gen.isDominatedBy(node, "S")) {
            NT s = gen.getDominatingNode(node, "S");
            List<GraphNode> subj_list = gen.getDaughtersByGeneralLabel(s, "SB");

            for (GraphNode next_node : subj_list) {
                if (haveSameArgumentDomain(node, next_node)) {
                    return next_node;
                }
            }
        }

        return null;
    }

    protected static boolean isSubstitutingMarkablePronoun(T terminal) {
        String pos = terminal.getPos();
        return pos.equals("PPER") || pos.equals("PIS") || pos.equals("PWS") ||
               pos.equals("PPOSS") || pos.equals("PRF") || pos.equals("PDS");
    }

    public static boolean isPronoun(T terminal) {
        return terminal.getPos().equals("PPER");
    }

    protected static boolean isAttributiveMarkablePronoun(T terminal) {
        return terminal.getPos().equals("PPOSAT");
    }

    public static boolean isNoun(T terminal) {
        String pos = terminal.getPos();
        return pos.equals("NE") || pos.equals("NNE") || pos.equals("NN");
    }

    public static boolean isApposition(GraphNode node) {
        NT mother = node.getMother();
        return mother != null && node.getEdge2Mother().equals("APP");
    }
} // class

