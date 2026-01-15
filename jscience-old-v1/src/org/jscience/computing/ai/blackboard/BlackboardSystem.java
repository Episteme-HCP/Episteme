/* BlackboardSystem.java */
package org.jscience.computing.ai.blackboard;

import org.jscience.computing.ai.blackboard.util.SymbolTable;

import java.util.Enumeration;
import java.util.Hashtable;


/**
 * BlackboardSystem class.  This is an abstract template class for KBS
 * definition. A subclass is required to implement the methods:
 * initBlackboard(), initController() and initKnowledgeSources().  The
 * subclass is also required to initialise the variables defined by this
 * class.
 *
 * @author:   Paul Brown
 * @version:  1.3, 04/26/96
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public abstract class BlackboardSystem {
    /** This is the client application of the KBS. */
    protected KBSClient client;

    /** This is the global blackboard data structure. */
    protected Hashtable blackboard;

    /** This is a reference to the system controller. */
    protected Controller controller;

    /** All system knowledge sources are contained by this variable. */
    protected KnowledgeSource[] knowledge_sources;

    /** This is the symbol table used by the system. */
    protected SymbolTable symbols;

    /**
     * Initialises the system by calling a subclass' initialisation
     * methods and specifies knowledge source/blackboard level and rule
     * condition/ rule action dependencies.
     */
    public void initKBS() {
        Enumeration levels;
        Integer level;
        initBlackboard();
        initController();
        initKnowledgeSources();
        // initialise ks/blackboard level dependencies
        levels = blackboard.keys();

        while (levels.hasMoreElements()) {
            level = (Integer) levels.nextElement();

            for (int i = 0; i < knowledge_sources.length; i++)
                if (knowledge_sources[i].references(level)) {
                    ((BlackboardLevel) blackboard.get(level)).addObserver(knowledge_sources[i]);
                }
        }

        // initialise rule/action dependencies
        for (int j = 0; j < knowledge_sources.length; j++)
            knowledge_sources[j].initRuleDependencies();
    }

    /**
     * This method is implemented by subclasses of this class, it is
     * intended to initialise the blackboard structure and it's contained
     * levels.
     */
    public abstract void initBlackboard();

    /**
     * This method is implemented by subclasses of this class, it is
     * intended to initialise the system controller.
     */
    public abstract void initController();

    /**
     * This method is implemented by subclasses of this class, it is
     * intended to initialise knowledge sources and their contained rules.
     */
    public abstract void initKnowledgeSources();

    /**
     * Resets the blackboard system by clearing all entries from the
     * blackboard.
     */
    public void reset() {
        Enumeration levels = blackboard.elements();

        while (levels.hasMoreElements())
            ((BlackboardLevel) levels.nextElement()).clear();
    }

    /**
     * Executes the blackboard system.
     */
    public void execute() {
        controller.execute();
    }
}
