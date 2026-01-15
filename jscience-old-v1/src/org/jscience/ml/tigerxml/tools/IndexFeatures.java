/*
 * IndexFeatures.java
 *
 * Created in September 2003
 *
 * Copyright (C) 2003 Hajo Keffer <hajokeffer@coli.uni-sb.de>
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
import org.jscience.ml.tigerxml.Sentence;
import org.jscience.ml.tigerxml.T;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Identifies and compares the features of a GraphNode that are
 * relevant for anaphoric reference, for example person, gender, and number.
 *
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @version 1.84
 *          $Id: IndexFeatures.java,v 1.2 2007-10-21 17:47:09 virtualcall Exp $
 */
public class IndexFeatures {
    private String person;
    private String gender;
    private String number;
    // the priority of the person number and gender
    // information
    private int person_priority;
    private int gender_priority;
    private int number_priority;
    private boolean conflict;
    private GraphNode my_node;
    // pos tag of determiner
    private static final String ART = "ART";
    // pos tag of proper noun
    private static final String NE = "NE";
    private static final String OTHER = "OTHER";
    private static final String UNDEFINED = "";
    private int verbosity = 0;

    public IndexFeatures(GraphNode node) {
        init(node);
    }

    public IndexFeatures(GraphNode node, int verbosity) {
        this.verbosity = verbosity;
        init(node);
    }

    private void init(GraphNode node) {
        this.my_node = node;
        this.person = UNDEFINED;
        this.number = UNDEFINED;
        this.gender = UNDEFINED;
        this.person_priority = 0;
        this.number_priority = 0;
        this.gender_priority = 0;
        this.conflict = false;
        if (node.isTerminal()) {
            T terminal = (T) node;
            String morph = terminal.getMorph();
            morph2Features(morph, OTHER);
        } else {
            // complex nodes
            Sentence sent = node.getSentence();
            NT non_terminal = (NT) node;
            ArrayList daughters =
                    non_terminal.getDaughters();
            // get feature information from NK or from HD
            // nodes
            for (int i = 0; i < daughters.size(); i++) {
                GraphNode next_node =
                        (GraphNode) daughters.get(i);
                String edge = next_node.getEdge2Mother();
                if ((edge.equals("NK")) ||
                        (edge.equals("HD"))) {
                    if (next_node.isTerminal()) {
                        T terminal = (T) next_node;
                        String morph = terminal.getMorph();
                        // if it is a determiner its features
                        // will be recorded separately in order
                        // to resolve possible conflicts
                        if ((terminal.getPos()).equals(ART)) {
                            morph2Features(morph, ART);
                        } else if ((terminal.getPos()).equals(NE)) {
                            morph2Features(morph, NE);
                        } else {
                            morph2Features(morph, OTHER);
                        }
                    }
                }
            } // for i
            // set CNPs to plural
            if ((non_terminal.getCat()).equals("CNP")) {
                this.number = "Pl";
            }
        } // not a terminal
        // default person = 3 for markables
        if ((SyncMMAX.isMarkable(node)) &&
                (isUndefined(person))) {
            person = "3";
        }
        if ((this.conflict) && (this.verbosity > 0)) {
            System.err.println
                    ("org.jscience.ml.tigerxml.tools.IndexFeatures: "
                            + "WARNING: Couldn't resolve feature conflict"
                            + " at node " +
                            (this.my_node).getId());
        }
    }

    private void morph2Features
            (String morph, String pos_tag) {
        int my_priority = 0;
        // determiners get the highest priority
        // i.e. their feature information is the most
        // valuable
        // proper names get the lowest priority
        if (pos_tag.equals(ART)) {
            my_priority = 3;
        }
        if (pos_tag.equals(OTHER)) {
            my_priority = 2;
        }
        if (pos_tag.equals(NE)) {
            my_priority = 1;
        }
        StringTokenizer tokenizer =
                new StringTokenizer(morph, ".", false);
        while (tokenizer.hasMoreTokens()) {
            String feature = tokenizer.nextToken();
            if (isPersonFeature(feature)) {
                if (isDefined(person)) {
                    // possible conflict
                    if (!(person.equals(feature))) {
                        // higher priority info resolves
                        // conflict
                        // equal priority info means conflict
                        // lower priority info won't be
                        // considered at all
                        if (my_priority > person_priority) {
                            this.person = feature;
                            person_priority = my_priority;
                            conflict = false;
                        } else if (my_priority == person_priority) {
                            conflict = true;
                        }
                    }
                } else { // feature is undefined
                    this.person = feature;
                    this.person_priority = my_priority;
                    this.conflict = false;
                }
            } // is person feature
            if (isNumberFeature(feature)) {
                if (isDefined(number)) {
                    // possible conflict
                    if (!(number.equals(feature))) {
                        // higher priority info resolves
                        // conflict
                        // equal priority info means conflict
                        // lower priority info won't be
                        // considered at all
                        if (my_priority > number_priority) {
                            this.number = feature;
                            number_priority = my_priority;
                            conflict = false;
                        } else if (my_priority == number_priority) {
                            conflict = true;
                        }
                    }
                } else { // feature is undefined
                    this.number = feature;
                    number_priority = my_priority;
                    conflict = false;
                }
            } // is number feature
            if (isGenderFeature(feature)) {
                if (isDefined(gender)) {
                    // possible conflict
                    if (!(gender.equals(feature))) {
                        // higher priority info resolves
                        // conflict
                        // equal priority info means conflict
                        // lower priority info won't be
                        // considered at all
                        if (my_priority > gender_priority) {
                            this.gender = feature;
                            gender_priority = my_priority;
                            conflict = false;
                        } else if (my_priority == gender_priority) {
                            conflict = true;
                        }
                    }
                } else { // feature is undefined
                    this.gender = feature;
                    gender_priority = my_priority;
                    conflict = false;
                }
            } // is gender feature
        } // while loop
    } // method morph2features

    private static boolean isUndefined(String feature) {
        return feature.equals(UNDEFINED);
    }

    private static boolean isDefined(String feature) {
        return (!(isUndefined(feature)));
    }

    private static boolean isPersonFeature
            (String feature) {
        return ((feature.equals("1")) ||
                (feature.equals("2")) ||
                (feature.equals("3")));
    }

    public boolean isFirstPerson() {
        return (this.person).equals("1");
    }

    public boolean isSecondPerson() {
        return (this.person).equals("2");
    }

    public boolean isThirdPerson() {
        return (this.person).equals("3");
    }

    public boolean hasUndefinedPerson() {
        return (this.person).equals(UNDEFINED);
    }

    private static boolean isGenderFeature
            (String feature) {
        return ((feature.equals("Fem")) ||
                (feature.equals("Masc")) ||
                (feature.equals("Neut")));
    }

    public boolean isFeminine() {
        return (this.gender).equals("Fem");
    }

    public boolean isMasculine() {
        return (this.gender).equals("Masc");
    }

    public boolean isNeuter() {
        return (this.gender).equals("Neut");
    }

    public boolean hasUndefinedGender() {
        return (this.gender).equals(UNDEFINED);
    }

    private static boolean isNumberFeature
            (String feature) {
        return ((feature.equals("Sg")) ||
                (feature.equals("Pl")));
    }

    public boolean isSingular() {
        return (this.number).equals("Sg");
    }

    public boolean isPlural() {
        return (this.number).equals("Pl");
    }

    public boolean hasUndefinedNumber() {
        return (this.number).equals(UNDEFINED);
    }

    public String getGender() {
        return this.gender;
    }

    public String getNumber() {
        return this.number;
    }

    public String getPerson() {
        return this.person;
    }

    private static boolean match(String feature1,
                                 String feature2) {
        return feature1.equals(feature2);
    }

    /**
     * This static method returns true if the two GraphNodes
     * agree in their index features and are therefore
     * candidates for the coreference relation
     * <p/>
     * Examples: "sie" and "die Frau",
     * "sie" und "die Kinder" and so forth.
     *
     * @param node1     The first node to be matched against the index features.
     * @param node2     The second node to be matched against the index features.
     * @param verbosity The level of verbosity.
     * @return True if the given GraphNodes agree in their index features.
     */
    public static final boolean indexFeaturesMatch(GraphNode node1,
                                                   GraphNode node2,
                                                   int verbosity) {
        boolean match = true;
        IndexFeatures feat1 = new IndexFeatures(node1, verbosity);
        IndexFeatures feat2 = new IndexFeatures(node2, verbosity);
        String gender1 = feat1.getGender();
        String person1 = feat1.getPerson();
        String number1 = feat1.getNumber();
        String gender2 = feat2.getGender();
        String person2 = feat2.getPerson();
        String number2 = feat2.getNumber();
        if (match) {
            match = match(gender1, gender2);
        }
        if (match) {
            match = match(number1, number2);
        }
        if (match) {
            match = match(person1, person2);
        }
        return match;
    }

    /**
     * This static method returns true if the two GraphNodes
     * agree in their index features and are therefore
     * candidates for the coreference relation
     * <p/>
     * Examples: "sie" and "die Frau",
     * "sie" und "die Kinder" and so forth.
     *
     * @param node1 The first node to be matched against the index features.
     * @param node2 The second node to be matched against the index features.
     * @return True if the given GraphNodes agree in their index features.
     */
    public static final boolean indexFeaturesMatch(GraphNode node1,
                                                   GraphNode node2) {
        return indexFeaturesMatch(node1, node2, 0);
    }

} // class IndexFeatures
