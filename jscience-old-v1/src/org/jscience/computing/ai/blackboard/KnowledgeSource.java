/* KnowledgeSource.java */
package org.jscience.computing.ai.blackboard;

import org.jscience.computing.ai.blackboard.util.EnquirableExecutable;
import org.jscience.computing.ai.blackboard.util.Executable;
import org.jscience.computing.ai.blackboard.util.Prioritised;
import org.jscience.computing.ai.blackboard.util.ValuePair;

import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;


/**
 * KnowledgeSource class.  This class defines one of the major components
 * of the blackboard architecture.  Knowledge sources subclass the Condition
 * class, utilising the inherited behaviour to implement a triggering
 * condition. These structures also contain an agenda of rule activations.
 *
 * @author:        Paul Brown
 * @version:        1.4, 04/26/96
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public class KnowledgeSource extends org.jscience.computing.ai.blackboard.Condition
    implements org.jscience.computing.ai.blackboard.util.Executable,
        org.jscience.computing.ai.blackboard.util.Prioritised,
        java.util.Observer {
    /** A reference to the system controller. */
    private Controller controller;

    /** The priority value of this knowledge source. */
    private int priority;

    /** The knowledge source's rule activation agenda. */
    private Vector agenda;

    /** The highest priority non-empty level of the rule agenda. */
    private int top_priority;

/**
         * Constructs a new knowledge source with the specified priority and
         * triggering condition.
         * @param blackboard a reference to the blackboard
         * @param controller a reference to the system controller
         * @param priority the specified priority value
         * @param disjunctions the knowledge source's initialised triggering
         * condition
         */
    public KnowledgeSource(Hashtable blackboard, Controller controller,
        int priority, ValuePair[] disjunctions) {
        super(blackboard, disjunctions);
        this.controller = controller;
        this.priority = priority;
        agenda = new Vector();
        top_priority = 0;
    }

/**
         * Constructs a new knowledge source with the specified triggering
         * condition and a default priority value.
         * @param blackboard a reference to the blackboard
         * @param controller a reference to the system controller
         * @param disjunctions the knowledge source's initialised triggering
         * condition
         */
    public KnowledgeSource(Hashtable blackboard, Controller controller,
        ValuePair[] disjunctions) {
        this(blackboard, controller, 0, disjunctions);
    }

    /**
     * Registers a rule with this knowledge source; the rule is
     * considered to be within the knowledge source's containment.  Normally,
     * this method is called by the rule constructor.
     *
     * @param rule the rule to register
     */
    public void register(Prioritised rule) {
        boolean found_position = false;

        if (top_priority == agenda.size()) {
            top_priority++;
        }

        if (agenda.isEmpty()) {
            agenda.addElement(new ValuePair(rule, null));
        } else {
            for (int i = 0; ((i < agenda.size()) && (!found_position)); i++)
                if (rule.priority() >= ((Prioritised) ((ValuePair) agenda.elementAt(
                            i)).key()).priority()) {
                    agenda.insertElementAt(new ValuePair(rule, null), i);

                    if ((top_priority != agenda.size()) && (i <= top_priority)) {
                        top_priority++;
                    }

                    found_position = true;
                }

            if (!found_position) {
                agenda.addElement(new ValuePair(rule, null));
            }
        }
    }

    /**
     * Initialises dependencies between the contained rules and
     * associated rule actions.  This method is called during system
     * initialisation.
     */
    public void initRuleDependencies() {
        Executable[] actions;
        Condition rule;

        for (int i = 0; i < agenda.size(); i++) {
            // loop through rules retrieving actions
            actions = ((EnquirableExecutable) ((ValuePair) agenda.elementAt(i)).key()).actions();

            for (int j = 0; j < actions.length; j++) {
                // loop through actions
                if (actions[j] instanceof Observable) {
                    for (int k = 0; k < agenda.size(); k++) {
                        // loop through rules
                        rule = (Condition) ((ValuePair) agenda.elementAt(k)).key();

                        if (actions[j] instanceof Create) {
                            if (rule.references(((Create) actions[j]).level())) {
                                ((Observable) actions[j]).addObserver((Observer) rule);
                            }
                        } else if (actions[j] instanceof Delete) {
                            if (rule.references(((Delete) actions[j]).level())) {
                                ((Observable) actions[j]).addObserver((Observer) rule);
                            }
                        } else if (rule.references(
                                    ((Modify) actions[j]).level())) {
                            ((Observable) actions[j]).addObserver((Observer) rule);
                        }
                    }
                }
            }
        }
    }

    /**
     * Contained rules call this method to signal their requirement for
     * re-evaluation.
     *
     * @param rule the rule requiring re-evaluation
     */
    public void updateAgenda(Condition rule) {
        boolean found_position = false;

        for (int i = 0; !found_position; i++)
            if (((ValuePair) agenda.elementAt(i)).key() == rule) {
                ((ValuePair) agenda.elementAt(i)).data(rule.evaluate());

                // adjust top_priority
                if (((ValuePair) agenda.elementAt(i)).data() != null) {
                    if (i < top_priority) {
                        top_priority = i;
                    }
                } else if (i == top_priority) {
                    do
                        top_priority++;
                    while ((top_priority < agenda.size()) &&
                            (((ValuePair) agenda.elementAt(top_priority)).data() == null));
                }

                found_position = true;
            }
    }

    /**
     * Removes all rule activations from the knowledge source's agenda.
     */
    public void clearAgenda() {
        for (int i = top_priority; i < agenda.size(); i++)
            ((ValuePair) agenda.elementAt(i)).data(null);

        top_priority = agenda.size();
    }

    /**
     * Executes the knowledge source's execution cycle.
     *
     * @param arg DOCUMENT ME!
     */
    public void execute(Object arg) {
        ValuePair rule;
        Vector binding_list;
        Object bindings;
        top_priority = agenda.size();

        for (int i = 0; i < agenda.size(); i++) {
            rule = (ValuePair) agenda.elementAt(i);
            rule.data(((Condition) rule.key()).evaluate());

            if ((i < top_priority) && (rule.data() != null)) {
                top_priority = i;
            }
        }

        while (top_priority < agenda.size()) {
            rule = (ValuePair) agenda.elementAt(top_priority);
            binding_list = (Vector) rule.data();
            bindings = binding_list.lastElement();
            binding_list.removeElementAt(binding_list.size() - 1);

            if (binding_list.isEmpty()) {
                rule.data(null);

                do
                    top_priority++;
                while ((top_priority < agenda.size()) &&
                        (((ValuePair) agenda.elementAt(top_priority)).data() == null));
            }

            ((Executable) rule.key()).execute(bindings);
        }
    }

    /**
     * Accessor method to return the knowledge source's priority.
     *
     * @return the priority value
     */
    public int priority() {
        return (priority);
    }

    /**
     * This method is called by blackboard levels which the knowledge
     * source is observing.  This method results in the re-evaluation of the
     * knowledge source's triggering condition.
     *
     * @param o DOCUMENT ME!
     * @param arg DOCUMENT ME!
     */
    public void update(Observable o, Object arg) {
        if (triggered()) {
            controller.putOnce(this, priority);
        } else {
            controller.remove(this, priority);
        }
    }
}
