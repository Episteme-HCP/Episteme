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

package org.jscience.linguistics.loaders.tigerxml.tools;

import org.jscience.linguistics.loaders.tigerxml.GraphNode;
import org.jscience.linguistics.loaders.tigerxml.NT;
import org.jscience.linguistics.loaders.tigerxml.T;

import java.util.List;
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
    // the priority of the person number and gender information
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
            NT non_terminal = (NT) node;
            List<GraphNode> daughters = non_terminal.getDaughters();
            
            // get feature information from NK or from HD nodes
            for (GraphNode next_node : daughters) {
                String edge = next_node.getEdge2Mother();
                if (edge.equals("NK") || edge.equals("HD")) {
                    if (next_node.isTerminal()) {
                        T terminal = (T) next_node;
                        String morph = terminal.getMorph();
                        // if it is a determiner its features
                        // will be recorded separately in order
                        // to resolve possible conflicts
                        if (terminal.getPos().equals(ART)) {
                            morph2Features(morph, ART);
                        } else if (terminal.getPos().equals(NE)) {
                            morph2Features(morph, NE);
                        } else {
                            morph2Features(morph, OTHER);
                        }
                    }
                }
            }
            // set CNPs to plural
            if (non_terminal.getCat().equals("CNP")) {
                this.number = "Pl";
            }
        } // not a terminal
        
        // default person = 3 for markables
        if (SyncMMAX.isMarkable(node) && isUndefined(person)) {
            person = "3";
        }
        
        if (this.conflict && this.verbosity > 0) {
            System.err.println("IndexFeatures: WARNING: Couldn't resolve feature conflict at node " + this.my_node.getId());
        }
    }

    private void morph2Features(String morph, String pos_tag) {
        if (morph == null || morph.isEmpty()) return;
        
        int my_priority = 0;
        // determiners get the highest priority
        if (pos_tag.equals(ART)) {
            my_priority = 3;
        } else if (pos_tag.equals(OTHER)) {
            my_priority = 2;
        } else if (pos_tag.equals(NE)) {
            my_priority = 1;
        }
        
        StringTokenizer tokenizer = new StringTokenizer(morph, ".", false);
        while (tokenizer.hasMoreTokens()) {
            String feature = tokenizer.nextToken();
            if (isPersonFeature(feature)) {
                if (isDefined(person)) {
                    if (!person.equals(feature)) {
                        if (my_priority > person_priority) {
                            this.person = feature;
                            person_priority = my_priority;
                            conflict = false;
                        } else if (my_priority == person_priority) {
                            conflict = true;
                        }
                    }
                } else {
                    this.person = feature;
                    this.person_priority = my_priority;
                    this.conflict = false;
                }
            }
            if (isNumberFeature(feature)) {
                if (isDefined(number)) {
                    if (!number.equals(feature)) {
                        if (my_priority > number_priority) {
                            this.number = feature;
                            number_priority = my_priority;
                            conflict = false;
                        } else if (my_priority == number_priority) {
                            conflict = true;
                        }
                    }
                } else {
                    this.number = feature;
                    number_priority = my_priority;
                    conflict = false;
                }
            }
            if (isGenderFeature(feature)) {
                if (isDefined(gender)) {
                    if (!gender.equals(feature)) {
                        if (my_priority > gender_priority) {
                            this.gender = feature;
                            gender_priority = my_priority;
                            conflict = false;
                        } else if (my_priority == gender_priority) {
                            conflict = true;
                        }
                    }
                } else {
                    this.gender = feature;
                    gender_priority = my_priority;
                    conflict = false;
                }
            }
        }
    }

    private static boolean isUndefined(String feature) {
        return feature == null || feature.equals(UNDEFINED);
    }

    private static boolean isDefined(String feature) {
        return !isUndefined(feature);
    }

    private static boolean isPersonFeature(String feature) {
        return feature.equals("1") || feature.equals("2") || feature.equals("3");
    }

    public boolean isFirstPerson() { return "1".equals(this.person); }
    public boolean isSecondPerson() { return "2".equals(this.person); }
    public boolean isThirdPerson() { return "3".equals(this.person); }
    public boolean hasUndefinedPerson() { return isUndefined(this.person); }

    private static boolean isGenderFeature(String feature) {
        return feature.equals("Fem") || feature.equals("Masc") || feature.equals("Neut");
    }

    public boolean isFeminine() { return "Fem".equals(this.gender); }
    public boolean isMasculine() { return "Masc".equals(this.gender); }
    public boolean isNeuter() { return "Neut".equals(this.gender); }
    public boolean hasUndefinedGender() { return isUndefined(this.gender); }

    private static boolean isNumberFeature(String feature) {
        return feature.equals("Sg") || feature.equals("Pl");
    }

    public boolean isSingular() { return "Sg".equals(this.number); }
    public boolean isPlural() { return "Pl".equals(this.number); }
    public boolean hasUndefinedNumber() { return isUndefined(this.number); }

    public String getGender() { return this.gender; }
    public String getNumber() { return this.number; }
    public String getPerson() { return this.person; }

    /**
     * returns true if the two GraphNodes agree in their index features.
     */
    public static boolean indexFeaturesMatch(GraphNode node1, GraphNode node2, int verbosity) {
        IndexFeatures feat1 = new IndexFeatures(node1, verbosity);
        IndexFeatures feat2 = new IndexFeatures(node2, verbosity);
        
        boolean genderMatch = isUndefined(feat1.getGender()) || isUndefined(feat2.getGender()) || feat1.getGender().equals(feat2.getGender());
        boolean numberMatch = isUndefined(feat1.getNumber()) || isUndefined(feat2.getNumber()) || feat1.getNumber().equals(feat2.getNumber());
        boolean personMatch = isUndefined(feat1.getPerson()) || isUndefined(feat2.getPerson()) || feat1.getPerson().equals(feat2.getPerson());
        
        return genderMatch && numberMatch && personMatch;
    }

    public static boolean indexFeaturesMatch(GraphNode node1, GraphNode node2) {
        return indexFeaturesMatch(node1, node2, 0);
    }
}
