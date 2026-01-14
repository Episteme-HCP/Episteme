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

/* ExampleKBS.java */

package org.jscience.tests.computing.ai.blackboard;

import org.jscience.computing.ai.blackboard.BlackboardLevel;
import org.jscience.computing.ai.blackboard.Controller;
import org.jscience.computing.ai.blackboard.KBSClient;
import org.jscience.computing.ai.blackboard.KnowledgeSource;
import org.jscience.computing.ai.blackboard.Modify;
import org.jscience.computing.ai.blackboard.Query;
import org.jscience.computing.ai.blackboard.Quit;
import org.jscience.computing.ai.blackboard.Rule;
import org.jscience.computing.ai.blackboard.Write;
import pb.util.Executable;
import pb.util.SymbolTable;
import pb.util.ValuePair;
import java.util.Hashtable;

/**
 * ExampleKBS class.  This class implements a knowledge base definition.
 *
 * @version:  1.4, 04/26/96 
 * @author:   Paul Brown
 *
 */
// This author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/

public class ExampleKBS extends org.jscience.computing.ai.blackboard.BlackboardSystem {
	/**
	 * Constant value used for I/O
	 */
	public static final Integer STRING_VAL = new Integer(0);

	/**
	 * Constant value used for I/O
	 */
	public static final Integer STRING_VAR = new Integer(1);

	/**
	 * Constant value used for I/O
	 */
	public static final Integer INPUT_VAL = new Integer(2);

	/**
	 * Constant value used for I/O
	 */
	public static final Integer APPLET_PIC = new Integer(3);

	/**
	 * Constructs a new instance of this class.
	 */
	public ExampleKBS(KBSClient client) {
		this.client = client;
		symbols = new SymbolTable();
	}

	/**
	 * Blackboard and blackboard level initialisation.
	 */
	public void initBlackboard() {
		ValuePair level_defaults[];
		// create blackboard
		blackboard = new Hashtable(2, 1.0f);
		// create goal blackboard level
		level_defaults = new ValuePair[3];
		level_defaults[0] = new ValuePair(symbols.put("type"), symbols.put("unknown"));
		level_defaults[1] = new ValuePair(symbols.put("subtype"), symbols.put("unknown"));
		level_defaults[2] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		blackboard.put(symbols.put("goal"), new BlackboardLevel(level_defaults));
		// create animal blackboard level
		level_defaults = new ValuePair[18];
		level_defaults[0] = new ValuePair(symbols.put("type"), symbols.put("unknown"));
		level_defaults[1] = new ValuePair(symbols.put("subtype"), symbols.put("unknown"));
		level_defaults[2] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		level_defaults[3] = new ValuePair(symbols.put("can-fly"), symbols.put("unknown"));
		level_defaults[4] = new ValuePair(symbols.put("can-swim"), symbols.put("unknown"));
		level_defaults[5] = new ValuePair(symbols.put("chews-cud"), symbols.put("unknown"));
		level_defaults[6] = new ValuePair(symbols.put("coat"), symbols.put("unknown"));
		level_defaults[7] = new ValuePair(symbols.put("colour"), symbols.put("unknown"));
		level_defaults[8] = new ValuePair(symbols.put("eats-meat"), symbols.put("unknown"));
		level_defaults[9] = new ValuePair(symbols.put("eye-position"), symbols.put("unknown"));
		level_defaults[10] = new ValuePair(symbols.put("extremities"), symbols.put("unknown"));
		level_defaults[11] = new ValuePair(symbols.put("feeds-offspring-milk"), symbols.put("unknown"));
		level_defaults[12] = new ValuePair(symbols.put("has-pointed-teeth"), symbols.put("unknown"));
		level_defaults[13] = new ValuePair(symbols.put("inhabits-becalmed-ships"), symbols.put("unknown"));
		level_defaults[14] = new ValuePair(symbols.put("lays-eggs"), symbols.put("unknown"));
		level_defaults[15] = new ValuePair(symbols.put("leg-length"), symbols.put("unknown"));
		level_defaults[16] = new ValuePair(symbols.put("marking"), symbols.put("unknown"));
		level_defaults[17] = new ValuePair(symbols.put("neck-length"), symbols.put("unknown"));
		blackboard.put(symbols.put("animal"), new BlackboardLevel(level_defaults));
	}

	/**
	 * System controller initialisation.
	 */
	public void initController() {
		ValuePair initial_entries[] = new ValuePair[2];
		initial_entries[0] = new ValuePair(symbols.put("goal"), null);
		initial_entries[1] = new ValuePair(symbols.put("animal"), null);
		controller = new Controller(blackboard, initial_entries);
	}

	/**
	 * Initialise knowledge sources.
	 */
	public void initKnowledgeSources() {
		ValuePair constants[], variables[], attributes, entry, conditions[], disjunctions[];
		knowledge_sources = new KnowledgeSource[4];
		// create type KS
		// Trigger: if (goal (type unknown))
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[1];
		conditions[0] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		knowledge_sources[0] = new KnowledgeSource(blackboard, controller, disjunctions);
		initTypeRules(knowledge_sources[0]);
		// create subtype KS
		// Trigger: if (goal (type known) (subtype unknown)) & (animal (type mammal))
		constants = new ValuePair[2];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("known"));
		constants[1] = new ValuePair(symbols.put("subtype"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("mammal"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		knowledge_sources[1] = new KnowledgeSource(blackboard, controller, disjunctions);
		initSubtypeRules(knowledge_sources[1]);
		// create identity KS
		// Trigger: if (goal (type known) (identity unknown)) & (animal (type bird))
		//          or (goal (subtype known) (identity unknown))
		constants = new ValuePair[2];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("known"));
		constants[1] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("bird"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[2];
		disjunctions[0] = new ValuePair(conditions, null);
		constants = new ValuePair[2];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("known"));
		constants[1] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[1];
		conditions[0] = new ValuePair(entry, attributes);
		disjunctions[1] = new ValuePair(conditions, null);
		knowledge_sources[2] = new KnowledgeSource(blackboard, controller, disjunctions);
		initIdentityRules(knowledge_sources[2]);
		// create report KS
		// Trigger: if (goal)
		attributes = new ValuePair(null, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[1];
		conditions[0] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		knowledge_sources[3] = new KnowledgeSource(blackboard, controller, -10, disjunctions);
		initReportRules(knowledge_sources[3]);
	}

	/**
	 * Initialise rules belonging to the type knowledge source.
	 */
	public void initTypeRules(KnowledgeSource ks) {
		ValuePair constants[], variables[], attributes, entry, conditions[], disjunctions[], output[];
		Executable actions[];
		// Rule: if (goal (type unknown)) & (animal (coat unknown))
		//       then query(coat)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("coat"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[4];
		output[0] = new ValuePair(STRING_VAL, "Describe the body covering of the unidentified animal");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("hair", "hairy"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("feathers", "feathery"));
		output[3] = new ValuePair(INPUT_VAL, new ValuePair("other", "other"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("coat"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (type unknown)) & (animal (feeds-offspring-milk unknown))
		//       then query(feeds-offspring-milk)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("feeds-offspring-milk"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[3];
		output[0] = new ValuePair(STRING_VAL, "Does the unidentified animal feed it's offspring milk?");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("yes", "true"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("no", "false"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("feeds-offspring-milk"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (type unknown)) & (animal (can-fly unknown))
		//       then query(can-fly)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("can-fly"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[3];
		output[0] = new ValuePair(STRING_VAL, "Can the unidentified animal fly?");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("yes", "true"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("no", "false"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("can-fly"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (type unknown)) & (animal (can-fly true) (lays-eggs unknown))
		//       then query(lays-eggs)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[2];
		constants[0] = new ValuePair(symbols.put("can-fly"), symbols.put("true"));
		constants[1] = new ValuePair(symbols.put("lays-eggs"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[3];
		output[0] = new ValuePair(STRING_VAL, "Does the unidentified animal lay eggs?");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("yes", "true"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("no", "false"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("lays-eggs"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (type unknown)) & (animal (coat hairy))
		//       or (goal (type unknown)) & (animal (feeds-offspring-milk true))
		//       then modify(type known)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("coat"), symbols.put("hairy"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[2];
		disjunctions[0] = new ValuePair(conditions, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("feeds-offspring-milk"), symbols.put("true"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions[1] = new ValuePair(conditions, null);
		actions = new Executable[2];
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("known"));
		actions[0] = new Modify(blackboard, symbols.put("goal"), symbols.put("var1"), constants, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("mammal"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), constants, null);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (type unknown)) & (animal (coat feathery))
		//       or (goal (type unknown)) & (animal (can-fly true) (lays-eggs true))
		//       then modify(type known)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("coat"), symbols.put("feathery"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[2];
		disjunctions[0] = new ValuePair(conditions, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[2];
		constants[0] = new ValuePair(symbols.put("can-fly"), symbols.put("true"));
		constants[1] = new ValuePair(symbols.put("lays-eggs"), symbols.put("true"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions[1] = new ValuePair(conditions, null);
		actions = new Executable[2];
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("known"));
		actions[0] = new Modify(blackboard, symbols.put("goal"), symbols.put("var1"), constants, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("bird"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), constants, null);
		new Rule(blackboard, ks, disjunctions, actions);
	}

	/**
	 * Initialise rules belonging to the subtype knowledge source.
	 */
	public void initSubtypeRules(KnowledgeSource ks) {
		ValuePair constants[], variables[], attributes, entry, conditions[], disjunctions[], output[];
		Executable actions[];
		// Rule: if (goal (subtype unknown)) & (animal (eats-meat unknown))
		//       then query(eats-meat)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("eats-meat"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[3];
		output[0] = new ValuePair(STRING_VAL, "Does the unidentified animal eat meat?");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("yes", "true"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("no", "false"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("eats-meat"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (subtype unknown)) & (animal (has-pointed-teeth unknown))
		//       then query(has-pointed-teeth)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("has-pointed-teeth"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[3];
		output[0] = new ValuePair(STRING_VAL, "Does the unidentified animal have pointed teeth?");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("yes", "true"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("no", "false"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("has-pointed-teeth"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (subtype unknown)) & (animal (extremities unknown))
		//       then query(extremities)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("extremities"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[4];
		output[0] = new ValuePair(STRING_VAL, "Describe the unidentified animal's extremities");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("claws", "claws"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("hooves", "hooves"));
		output[3] = new ValuePair(INPUT_VAL, new ValuePair("other", "other"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("extremities"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (subtype unknown)) & (animal (eye-position unknown))
		//       then query(eye-position)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("eye-position"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[3];
		output[0] = new ValuePair(STRING_VAL, "Do the unidentified animal's eyes face forward?");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("yes", "forward"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("no", "other"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("eye-position"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (subtype unknown)) & (animal (chews-cud))
		//       then query(chews-cud)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("chews-cud"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[3];
		output[0] = new ValuePair(STRING_VAL, "Does the unidentified animal chew cud?");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("yes", "true"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("no", "false"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("chews-cud"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (subtype unknown)) & (animal (eats-meat true))
		//       or (goal (subtype unknown)) & (animal (has-pointed-teeth true) (extremities claws) (eye-position forward))
		//       then modify(subtype known)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("eats-meat"), symbols.put("true"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[2];
		disjunctions[0] = new ValuePair(conditions, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[3];
		constants[0] = new ValuePair(symbols.put("has-pointed-teeth"), symbols.put("true"));
		constants[1] = new ValuePair(symbols.put("extremities"), symbols.put("claws"));
		constants[2] = new ValuePair(symbols.put("eye-position"), symbols.put("forward"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions[1] = new ValuePair(conditions, null);
		actions = new Executable[2];
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("known"));
		actions[0] = new Modify(blackboard, symbols.put("goal"), symbols.put("var1"), constants, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("carnivore"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), constants, null);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (subtype unknown)) & (animal (type mammal) (extremities hooves))
		//       or (goal (subtype unknown)) & (animal (type mammal) (chews-cud true))
		//       then modify(subtype known)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[2];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("mammal"));
		constants[1] = new ValuePair(symbols.put("extremities"), symbols.put("hooves"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[2];
		disjunctions[0] = new ValuePair(conditions, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[2];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("mammal"));
		constants[1] = new ValuePair(symbols.put("chews-cud"), symbols.put("true"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions[1] = new ValuePair(conditions, null);
		actions = new Executable[2];
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("known"));
		actions[0] = new Modify(blackboard, symbols.put("goal"), symbols.put("var1"), constants, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("ungulate"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), constants, null);
		new Rule(blackboard, ks, disjunctions, actions);
	}

	/**
	 * Initialise rules belonging to the identity knowledge source.
	 */
	public void initIdentityRules(KnowledgeSource ks) {
		ValuePair constants[], variables[], attributes, entry, conditions[], disjunctions[], output[];
		Executable actions[];
		// Rule: if (goal (identity unknown)) & (animal (inhabits-becalmed-ships unknown))
		//       then query(inhabits-becalmed-ships)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("inhabits-becalmed-ships"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[3];
		output[0] = new ValuePair(STRING_VAL, "Does the unidentified animal inhabit becalmed ships?");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("yes", "true"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("no", "false"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("inhabits-becalmed-ships"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (identity unknown)) & (animal (can-fly unknown))
		//       then query(can-fly)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("can-fly"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[3];
		output[0] = new ValuePair(STRING_VAL, "Can the unidentified animal fly?");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("yes", "true"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("no", "false"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("can-fly"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (identity unknown)) & (animal (can-swim unknown))
		//       then query(can-swim)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("can-swim"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[3];
		output[0] = new ValuePair(STRING_VAL, "Can the unidentified animal swim?");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("yes", "true"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("no", "false"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("can-swim"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (identity unknown)) & (animal (colour unknown))
		//       then query(colour)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("colour"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[4];
		output[0] = new ValuePair(STRING_VAL, "Describe the unidentified animal's colour");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("black and white", "black-and-white"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("tawny", "tawny"));
		output[3] = new ValuePair(INPUT_VAL, new ValuePair("other", "other"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("colour"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (identity unknown)) & (animal (neck-length unknown))
		//       then query(neck-length)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("neck-length"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[3];
		output[0] = new ValuePair(STRING_VAL, "Does the unidentified animal have a long neck?");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("yes", "long"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("no", "other"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("neck-length"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (identity unknown)) & (animal (leg-length unknown))
		//       then query(leg-length)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("leg-length"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[3];
		output[0] = new ValuePair(STRING_VAL, "Does the unidentified animal have long legs?");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("yes", "long"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("no", "other"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("leg-length"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (identity unknown)) & (animal (marking unknown))
		//       then query(marking)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("marking"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[4];
		output[0] = new ValuePair(STRING_VAL, "Describe any markings which may distinguish the unidentified animal");
		output[1] = new ValuePair(INPUT_VAL, new ValuePair("black stripes", "black-stripes"));
		output[2] = new ValuePair(INPUT_VAL, new ValuePair("dark spots", "dark-spots"));
		output[3] = new ValuePair(INPUT_VAL, new ValuePair("other", "other"));
		actions[0] = new Query(client, symbols, symbols.put("var3"), output);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("marking"), symbols.put("var3"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), null, variables);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (identity unknown)) & (animal (type bird) (inhabits-becalmed-ships true))
		//       then modify(identity known)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[2];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("bird"));
		constants[1] = new ValuePair(symbols.put("inhabits-becalmed-ships"), symbols.put("true"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("known"));
		actions[0] = new Modify(blackboard, symbols.put("goal"), symbols.put("var1"), constants, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("albatross"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), constants, null);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (identity unknown)) & (animal (type bird) (can-fly false) (can-swim true) (colour black-and-white))
		//       then modify(identity known)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[4];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("bird"));
		constants[1] = new ValuePair(symbols.put("can-fly"), symbols.put("false"));
		constants[2] = new ValuePair(symbols.put("can-swim"), symbols.put("true"));
		constants[3] = new ValuePair(symbols.put("colour"), symbols.put("black-and-white"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("known"));
		actions[0] = new Modify(blackboard, symbols.put("goal"), symbols.put("var1"), constants, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("penguin"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), constants, null);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (identity unknown)) & (animal (type bird) (can-fly false) (neck-length long) (leg-length long)
		//          (colour black-and-white))
		//       then modify(identity known)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[5];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("bird"));
		constants[1] = new ValuePair(symbols.put("can-fly"), symbols.put("false"));
		constants[2] = new ValuePair(symbols.put("neck-length"), symbols.put("long"));
		constants[3] = new ValuePair(symbols.put("leg-length"), symbols.put("long"));
		constants[4] = new ValuePair(symbols.put("colour"), symbols.put("black-and-white"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("known"));
		actions[0] = new Modify(blackboard, symbols.put("goal"), symbols.put("var1"), constants, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("ostrich"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), constants, null);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (identity unknown)) & (animal (subtype ungulate) (marking black-stripes))
		//       then modify(identity known)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[2];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("ungulate"));
		constants[1] = new ValuePair(symbols.put("marking"), symbols.put("black-stripes"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("known"));
		actions[0] = new Modify(blackboard, symbols.put("goal"), symbols.put("var1"), constants, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("zebra"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), constants, null);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (identity unknown)) & (animal (subtype ungulate) (neck-length long) (leg-length long) (marking dark-spots))
		//       then modify(identity known)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[4];
		constants[0] = new ValuePair(symbols.put("subtype"), symbols.put("ungulate"));
		constants[1] = new ValuePair(symbols.put("neck-length"), symbols.put("long"));
		constants[2] = new ValuePair(symbols.put("leg-length"), symbols.put("long"));
		constants[3] = new ValuePair(symbols.put("marking"), symbols.put("dark-spots"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("known"));
		actions[0] = new Modify(blackboard, symbols.put("goal"), symbols.put("var1"), constants, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("giraffe"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), constants, null);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (identity unknown)) & (animal (type mammal) (subtype carnivore) (colour tawny) (marking black-stripes))
		//       then modify(identity known)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[4];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("mammal"));
		constants[1] = new ValuePair(symbols.put("subtype"), symbols.put("carnivore"));
		constants[2] = new ValuePair(symbols.put("colour"), symbols.put("tawny"));
		constants[3] = new ValuePair(symbols.put("marking"), symbols.put("black-stripes"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("known"));
		actions[0] = new Modify(blackboard, symbols.put("goal"), symbols.put("var1"), constants, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("tiger"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), constants, null);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (identity unknown)) & (animal (type mammal) (subtype carnivore) (colour tawny) (marking dark-spots))
		//       then modify(identity known)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		constants = new ValuePair[4];
		constants[0] = new ValuePair(symbols.put("type"), symbols.put("mammal"));
		constants[1] = new ValuePair(symbols.put("subtype"), symbols.put("carnivore"));
		constants[2] = new ValuePair(symbols.put("colour"), symbols.put("tawny"));
		constants[3] = new ValuePair(symbols.put("marking"), symbols.put("dark-spots"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("known"));
		actions[0] = new Modify(blackboard, symbols.put("goal"), symbols.put("var1"), constants, null);
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("cheetah"));
		actions[1] = new Modify(blackboard, symbols.put("animal"), symbols.put("var2"), constants, null);
		new Rule(blackboard, ks, disjunctions, actions);
	}

	/**
	 * Initialise rules belonging to the report knowledge source.
	 */
	public void initReportRules(KnowledgeSource ks) {
		ValuePair constants[], variables[], attributes, entry, conditions[], disjunctions[], output[];
		Executable actions[];
		// Rule: if (goal (identity known)) & (animal (identity ?id))
		//       then write(identity ?id)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("known"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[2];
		conditions[0] = new ValuePair(entry, attributes);
		variables = new ValuePair[1];
		variables[0] = new ValuePair(symbols.put("identity"), symbols.put("var3"));
		attributes = new ValuePair(null, variables);
		entry = new ValuePair(symbols.put("animal"), symbols.put("var2"));
		conditions[1] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[3];
		output[0] = new ValuePair(STRING_VAL, "Animal: ");
		output[1] = new ValuePair(STRING_VAR, "var3");
		output[2] = new ValuePair(APPLET_PIC, "var3");
		actions[0] = new Write(client, symbols, output);
		actions[1] = new Quit(controller, ks);
		new Rule(blackboard, ks, disjunctions, actions);
		// Rule: if (goal (identity unknown))
		//       then write(identity unknown)
		constants = new ValuePair[1];
		constants[0] = new ValuePair(symbols.put("identity"), symbols.put("unknown"));
		attributes = new ValuePair(constants, null);
		entry = new ValuePair(symbols.put("goal"), symbols.put("var1"));
		conditions = new ValuePair[1];
		conditions[0] = new ValuePair(entry, attributes);
		disjunctions = new ValuePair[1];
		disjunctions[0] = new ValuePair(conditions, null);
		actions = new Executable[2];
		output = new ValuePair[1];
		output[0] = new ValuePair(STRING_VAL, "Unable to identify animal");
		actions[0] = new Write(client, symbols, output);
		actions[1] = new Quit(controller, ks);
		new Rule(blackboard, ks, disjunctions, actions);
	}
}
