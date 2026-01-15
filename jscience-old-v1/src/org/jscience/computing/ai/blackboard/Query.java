/* Query.java */
package org.jscience.computing.ai.blackboard;

import org.jscience.computing.ai.blackboard.util.SymbolTable;
import org.jscience.computing.ai.blackboard.util.ValuePair;

import java.util.Hashtable;


/**
 * Query class.  This class implements a client query rule action.
 *
 * @author:   Paul Brown
 * @version:  1.3, 04/26/96
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public class Query implements org.jscience.computing.ai.blackboard.util.Executable {
    /** A reference to the KBS client application. */
    private KBSClient client;

    /** A reference to the system symbol table. */
    private SymbolTable symbols;

    /** This is a variable identifier used for storing the client's response. */
    private Integer binding_id;

    /** These are output and client response specifications. */
    private ValuePair[] args;

/**
         * Constructs a query rule action.
         * @param client a reference to the client application
         * @param symbols a reference to the system symbol table
         * @param binding_id a variable identifier for binding a client's
         * response
         * @param args output and client response specifications
         */
    public Query(KBSClient client, SymbolTable symbols, Integer binding_id,
        ValuePair[] args) {
        this.client = client;
        this.symbols = symbols;
        this.binding_id = binding_id;
        this.args = args;
    }

    /**
     * Executes this action.
     *
     * @param arg variable bindings instantiated by rule condition evaluation
     */
    public void execute(Object arg) {
        Hashtable bindings = (Hashtable) arg;
        Object binding_value = client.query(args, bindings, symbols);

        if (binding_value instanceof String) {
            bindings.put(binding_id, symbols.put((String) binding_value));
        } else {
            bindings.put(binding_id, binding_value);
        }
    }
}
