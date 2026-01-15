/* Condition.java */
package org.jscience.computing.ai.blackboard;

import org.jscience.computing.ai.blackboard.util.ValuePair;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Condition class.  This class implements a condition data structure, it
 * is subclassed by both the Rule and KnowledgeSource classes.  The condition
 * is comprised of a disjunction of conjunctions.
 *
 * @author:   Paul Brown
 * @version:  1.3, 04/26/96
 *
 * @see org.jscience.computing.ai.blackboard.KnowledgeSource
 * @see org.jscience.computing.ai.blackboard.Rule
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public class Condition {
    /** This is a reference to the global blackboard data structure. */
    private Hashtable blackboard;

    /** This variable contains the conjunctions which form the condition. */
    private ValuePair[] disjunctions;

    /**
     * During condition evaluation, enumerated variables are stored
     * temporarily in this variable.
     */
    private Hashtable enumerated_variables;

/**
         * Constructs a new condition initialised with the supplied disjunction.
         * @param blackboard a reference to the blackboard
         * @param disjunctions an initialised disjunction structure
         */
    public Condition(Hashtable blackboard, ValuePair[] disjunctions) {
        Integer level;
        this.blackboard = blackboard;
        this.disjunctions = disjunctions;
        enumerated_variables = new Hashtable();

        for (int i = 0; i < disjunctions.length; i++)
            for (int j = 0; j < ((ValuePair[]) disjunctions[i].key()).length;
                    j++) {
                level = (Integer) ((ValuePair) ((ValuePair[]) disjunctions[i].key())[j].key()).key();

                if (!enumerated_variables.containsKey(level)) {
                    enumerated_variables.put(level, new Hashtable());
                }
            }
    }

    /**
     * Specifies whether a given blackboard level is referenced by this
     * condition.
     *
     * @param level a blackboard level identifier
     *
     * @return true if the level is referenced, false otherwise
     */
    public boolean references(Integer level) {
        boolean found_level = false;
        ValuePair[] disjunct;

        for (int i = 0; ((i < disjunctions.length) && (!found_level)); i++) {
            // loop through disjunctions looking for a reference to the specified level
            disjunct = (ValuePair[]) disjunctions[i].key();

            for (int j = 0; ((j < disjunct.length) && (!found_level)); j++) {
                // loop through conditions of this disjunct
                found_level = level.equals(((ValuePair) disjunct[j].key()).key());
            }
        }

        return (found_level);
    }

    /**
     * Used by knowledge sources to evaluate the condition without
     * binding any variables.
     *
     * @return true if the condition evaluates successfully, false otherwise
     */
    public boolean triggered() {
        boolean matched = false;
        Integer level;
        ValuePair[] constants;

        for (int i = 0; ((i < disjunctions.length) && (!matched)); i++) {
            // loop through disjunctions looking for a match
            matched = true;

            for (int j = 0;
                    ((j < ((ValuePair[]) disjunctions[i].key()).length) &&
                    (matched)); j++) {
                // loop through conditions of this disjunct
                level = (Integer) ((ValuePair) ((ValuePair[]) disjunctions[i].key())[j].key()).key();
                constants = (ValuePair[]) ((ValuePair) ((ValuePair[]) disjunctions[i].key())[j].data()).key();

                if (constants != null) {
                    if (((BlackboardLevel) blackboard.get(level)).get(constants) == null) {
                        matched = false;
                    }
                } else if (((BlackboardLevel) blackboard.get(level)).getAll() == null) {
                    matched = false;
                }
            }
        }

        return (matched);
    }

    /**
     * Used by rules to fully evaluate the condition, variables are
     * bound.
     *
     * @return a collection of all possible variable binding combinations, or
     *         null if the condition cannot be matched.
     */
    public Vector evaluate() {
        Vector permutations = new Vector();
        Vector entries = new Vector();
        Vector bindings = new Vector();
        ValuePair[] disjunct;
        ValuePair[] conditions;
        ValuePair[] constants;
        ValuePair[] variables;
        boolean matched;
        Integer level;
        Integer entry_id;
        Integer attribute;
        BlackboardLevel blackboard_level;
        BitSet matcher;
        Hashtable attribute_table;
        Enumeration enumerations;

        for (int i = 0; i < disjunctions.length; i++) {
            // loop through disjunctions looking for all matches
            disjunct = (ValuePair[]) disjunctions[i].key();
            conditions = new ValuePair[disjunct.length];
            matched = true;

            for (int j = 0; ((j < disjunct.length) && (matched)); j++) {
                // loop through conditions of this disjunct
                level = (Integer) ((ValuePair) disjunct[j].key()).key();
                blackboard_level = (BlackboardLevel) blackboard.get(level);
                entry_id = (Integer) ((ValuePair) disjunct[j].key()).data();
                constants = (ValuePair[]) ((ValuePair) disjunct[j].data()).key();

                if (constants != null) {
                    matcher = blackboard_level.get(constants);
                } else {
                    matcher = blackboard_level.getAll();
                }

                if (matcher != null) {
                    // record matched entries and bind variables
                    for (int k = 0; k < matcher.size(); k++)
                        if (matcher.get(k) == true) {
                            entries.addElement(new Integer(k));
                        }

                    variables = (ValuePair[]) ((ValuePair) disjunct[j].data()).data();

                    if (variables != null) {
                        attribute_table = (Hashtable) enumerated_variables.get(level);

                        for (int l = 0; l < variables.length; l++) {
                            // check if variable is already bound
                            attribute = (Integer) variables[l].key();

                            if (!attribute_table.containsKey(attribute)) {
                                attribute_table.put(attribute,
                                    blackboard_level.get(attribute));
                            }

                            // record binding details
                            bindings.addElement(new ValuePair(
                                    variables[l].data(),
                                    attribute_table.get(attribute)));
                        }

                        conditions[j] = new ValuePair(new ValuePair(entry_id,
                                    new Integer[entries.size()]),
                                new ValuePair[bindings.size()]);
                        entries.copyInto((Integer[]) ((ValuePair) conditions[j].key()).data());
                        entries.removeAllElements();
                        bindings.copyInto((ValuePair[]) conditions[j].data());
                        bindings.removeAllElements();
                    } else {
                        conditions[j] = new ValuePair(new ValuePair(entry_id,
                                    new Integer[entries.size()]), null);
                        entries.copyInto((Integer[]) ((ValuePair) conditions[j].key()).data());
                        entries.removeAllElements();
                    }
                } else {
                    matched = false;
                }
            }

            if (matched) {
                permutations(permutations, new Hashtable(), conditions, 0);
            }
        }

        if (permutations.isEmpty()) {
            permutations = null;
        }

        // clear all variable enumerations
        enumerations = enumerated_variables.elements();

        while (enumerations.hasMoreElements())
            ((Hashtable) enumerations.nextElement()).clear();

        return (permutations);
    }

    /**
     * Utility function used by evaluate() method to generate all
     * permutations of variable binding combinations.
     *
     * @param permutations DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param conditions DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    private void permutations(Vector permutations, Hashtable p,
        ValuePair[] conditions, int c) {
        Integer entry_id;
        Integer[] entry_matches;
        Integer binding_id;
        ValuePair[] bindings;
        ValuePair[] binding_matches;
        boolean found_value;

        if (c == conditions.length) {
            // save this permutation
            permutations.addElement(p.clone());
        } else {
            entry_id = (Integer) ((ValuePair) conditions[c].key()).key();
            entry_matches = (Integer[]) ((ValuePair) conditions[c].key()).data();
            bindings = (ValuePair[]) conditions[c].data();

            for (int i = 0; i < entry_matches.length; i++) {
                // loop through matching entries
                p.put(entry_id, entry_matches[i]);

                if (bindings != null) {
                    for (int j = 0; j < bindings.length; j++) {
                        // loop through variable bindings
                        binding_id = (Integer) bindings[j].key();
                        binding_matches = (ValuePair[]) bindings[j].data();
                        found_value = false;

                        for (int k = 0;
                                ((k < binding_matches.length) &&
                                (!found_value)); k++) {
                            // find value of binding for current entry
                            if (((BitSet) binding_matches[k].data()).get(
                                        entry_matches[i].intValue()) == true) {
                                p.put(binding_id, binding_matches[k].key());
                                found_value = true;
                            }
                        }
                    }
                }

                permutations(permutations, p, conditions, c + 1);
            }
        }
    }
}
