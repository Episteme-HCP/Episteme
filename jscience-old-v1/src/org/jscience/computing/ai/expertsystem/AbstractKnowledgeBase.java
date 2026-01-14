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

package org.jscience.computing.ai.expertsystem;

/*
 * JEOPS - The Java Embedded Object Production System
 * Copyright (c) 2000   Carlos Figueira Filho
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Contact: Carlos Figueira Filho (csff@cin.ufpe.br)
 */

import org.jscience.computing.ai.expertsystem.conflict.*;
import org.jscience.computing.ai.expertsystem.rete.*;

import java.util.*;

/**
 * The main class of JEOPS. This class models the knowledge the agent
 * has about the world. In it are stored the facts (objects) and
 * (production) rules that act on the first. This class must be subclassed
 * by a concrete knowledge base so that it is provided with the object
 * and rule base needed for work.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 2.1   13 Jul 2000   Implementation of a variation of Rete network.
 * @history 0.01  12 Mar 2000   Class adapted from previous version of JEOPS.
 */
public abstract class AbstractKnowledgeBase implements InternalConflictSetListener {

    /**
     * The fact (object) base.
     */
    private ObjectBase objectBase;

    /**
     * The (production) rule base.
     */
    private AbstractRuleBase ruleBase;

    /**
     * The number of rules in the rule base.
     */
    private int numberOfRules;

    /**
     * The conflict set associated with this knowledge base.
     */
    private ConflictSet conflictSet;

    /**
     * The set of rule firing listeners.
     */
    private Vector ruleFiringListeners;

    /**
     * The set of conflict set listeners.
     */
    private Vector conflictSetListeners;

    /**
     * Indicates whether this knowledge base is a listener of its
     * conflict set.
     */
    private boolean isListener = false;

    /**
     * Flag indicating whether there is any listener for rule events in
     * this knowledge base.
     */
    private boolean debug = false;

    /**
     * The map that takes class names into the entry points of the Rete
     * network. Each object enters the network at a node of its actual
     * class.
     */
    protected Map entryPoints = new HashMap();

    /**
     * All join nodes of the Rete network. Useful for removing
     * objects from Rete.
     */
    private Vector allJoinNodes = new Vector();

    /**
     * The set of classes whose objects can't be inserted into the Rete
     * network (because there is no compatible entry node).
     */
    private Set forbiddenClasses = new HashSet();

    /**
     * Creates a new knowledge base.
     *
     * @param conflictSet the conflict set associated with this knowledge base.
     */
    public AbstractKnowledgeBase(ConflictSet conflictSet) {
        setRuleBase(createRuleBase());
        setConflictSet(conflictSet);
        this.objectBase = new ObjectBase();
        this.ruleFiringListeners = new Vector();
        this.conflictSetListeners = new Vector();
        createReteNetwork();
    }

    /**
     * Factory method, used to instantiate the actual rule base.
     *
     * @return the rule base to be used with this knowledge base.
     */
    protected abstract AbstractRuleBase createRuleBase();

    /**
     * Adds the specified conflict set listener to receive events from
     * this knowledge base.
     *
     * @param l the conflict set listener
     */
    public void addConflictSetListener(RuleFireListener l) {
        if (!isListener) {
            conflictSet.addInternalConflictSetListener(this);
            isListener = true;
        }
        if (!conflictSetListeners.contains(l)) {
            conflictSetListeners.addElement(l);
            debug = true;
        }
    }

    /**
     * Tries to find a way from which a class object may get into some
     * entry point of the Rete network. If so, creates the subnetwork
     * that leads from the given class to the preexisting network,
     * connecting them.
     *
     * @param classType the class object whose hierarchy is to be
     *                  analysed to see if it fits into the current Rete
     *                  network.
     * @return the ClassFilterReteNode corresponding to the given
     *         class object, if some way could be found to connect it
     *         to the rest of the network; <code>null</code>
     *         otherwise.
     */
    private ClassFilterReteNode addNewEntryNode(Class classType) {

        ClassFilterReteNode result = null;

        // Searching the inheritance hierarchy
        Class superClass = classType.getSuperclass();
        if (superClass != null) {
            String superName = superClass.getName();
            ClassFilterReteNode superNode;
            superNode = (ClassFilterReteNode) entryPoints.get(superName);
            if (superNode == null) {
                superNode = addNewEntryNode(superClass);
            }
            if (superNode != null) {
                result = new ClassFilterReteNode(classType);
                result.addSuccessor(superNode);
                entryPoints.put(classType.getName(), result);
            }
        }
        result = searchInterfaces(classType, result);
        return result;
    }

    /**
     * Adds the specified rule fire listener to receive events from this
     * knowledge base.
     *
     * @param l the rule base listener
     */
    public void addRuleFireListener(RuleFireListener l) {
        if (!ruleFiringListeners.contains(l)) {
            ruleFiringListeners.addElement(l);
            debug = true;
        }
    }

    /**
     * Inserts a new object in this knowledge base.
     *
     * @param obj the object being inserted.
     */
    public void insert(Object obj) {
        if (objectBase.insert(obj)) {
            ClassFilterReteNode entryNode;
            entryNode = getEntryPoint(obj.getClass());
            if (entryNode != null) {
                entryNode.newObject(obj);
            }
        }
    }

    /**
     * Stores in the <code>entryPoints</code> field all the entry
     * points for a given rule.
     *
     * @param ruleIndex the index of the rule whose entry points will
     *                  be created.
     */
    private void createReteEntryPoints(int ruleIndex) {
        int numberOfDeclarations = ruleBase.getNumberOfDeclarations()[ruleIndex];
        for (int i = 0; i < numberOfDeclarations; i++) {
            String className = ruleBase.getDeclaredClassName(ruleIndex, i);
            ClassFilterReteNode node;
            node = (ClassFilterReteNode) entryPoints.get(className);
            Class classType = ruleBase.getDeclaredClass(ruleIndex, i);
            if (node == null) {
                node = addNewEntryNode(classType);
            }
            if (node == null) {
                node = new ClassFilterReteNode(classType);
            }
            entryPoints.put(className, node);
        }
    }

    /**
     * Creates the Rete network that will be used in this knowledge
     * base. At the end of this method, the field
     * <code>entryPoints</code> will be filled with the main entry
     * points of the network.
     */
    private void createReteNetwork() {
        int[] numberOfDeclarations = ruleBase.getNumberOfDeclarations();
        for (int i = 0; i < numberOfRules; i++) {
            createReteEntryPoints(i);

            // Adding the discriminant nodes.
            ReteNode[] discriminantNodes = new ReteNode[numberOfDeclarations[i]];
            for (int j = 0; j < numberOfDeclarations[i]; j++) {
                String className = ruleBase.getDeclaredClassName(i, j);
                ClassFilterReteNode entryNode;
                entryNode = (ClassFilterReteNode) entryPoints.get(className);
                discriminantNodes[j] = new FilterReteNode(this, ruleBase, i, j);
                entryNode.addSuccessor(discriminantNodes[j]);
            }
            ReteNode[] joinNodes = new ReteNode[numberOfDeclarations[i]];
            joinNodes[0] = discriminantNodes[0];
            for (int j = 1; j < numberOfDeclarations[i]; j++) {
                joinNodes[j] = new JoinReteNode(j + 1, ruleBase, i, j);
                allJoinNodes.addElement(joinNodes[j]);
                ReteNode anterior = joinNodes[j - 1];
                ReteNode current = joinNodes[j];
                for (int k = 0; k < anterior.getNumberOutputs(); k++) {
                    anterior.addSuccessor(k, current, k);
                }
                discriminantNodes[j].addSuccessor(0, current, j);
            }
            ReteNode finalNode = new FinalReteNode(numberOfDeclarations[i], this, i);
            ReteNode ultimo = joinNodes[numberOfDeclarations[i] - 1];
            for (int j = 0; j < ultimo.getNumberOutputs(); j++) {
                ultimo.addSuccessor(j, finalNode, j);
            }
        }
    }

    /**
     * Dispatch an element added event to all registered listeners.
     */
    private void fireElementAddedEvent(ConflictSetEvent e) {
        for (int i = 0; i < conflictSetListeners.size(); i++) {
            ((ConflictSetListener) conflictSetListeners.elementAt(i)).elementAdded(e);
        }
    }

    /**
     * Dispatch an element removed event to all registered listeners.
     */
    private void fireElementRemovedEvent(ConflictSetEvent e) {
        for (int i = 0; i < conflictSetListeners.size(); i++) {
            ((ConflictSetListener) conflictSetListeners.elementAt(i)).elementRemoved(e);
        }
    }

    /**
     * Dispatch a rule fired event to all registered listeners.
     */
    private void fireRuleFiredEvent(RuleEvent e) {
        for (int i = 0; i < ruleFiringListeners.size(); i++) {
            ((RuleFireListener) ruleFiringListeners.elementAt(i)).ruleFired(e);
        }
    }

    /**
     * Dispatch a rule firing event to all registered listeners.
     */
    private void fireRuleFiringEvent(RuleEvent e) {
        for (int i = 0; i < ruleFiringListeners.size(); i++) {
            ((RuleFireListener) ruleFiringListeners.elementAt(i)).ruleFiring(e);
        }
    }

    /**
     * Remove all facts (objects) of the object base.
     */
    public void flush() {
        objectBase.flush();
        conflictSet.flush();
        for (Iterator i = allJoinNodes.iterator(); i.hasNext();) {
            JoinReteNode joinNode = (JoinReteNode) i.next();
            joinNode.flush();
        }
    }

    /**
     * Returns the entry point of the rete network from where an object
     * of a given class must enter. If there is no entry point for the
     * given class, it may be due to two reasons: the objects of the
     * class can't be matched against any declaration of the rules; or
     * the class is not explicitly declared in the rules, but one of
     * its superclasses or implemented interfaces appear in the
     * declaration of some rule. In the latter case, the Rete network
     * will be extended in order to accomodate the incoming object.
     * In the former, <code>null</code> will be returned.
     *
     * @param classType the class object whose entry point is desired.
     * @return the node from where the objects of the given class must
     *         enter in the Rete network, or <code>null</code> if
     *         such entry point doesn't exist and can't even be
     *         created.
     */
    private ClassFilterReteNode getEntryPoint(Class classType) {
        String className = classType.getName();
        ClassFilterReteNode node;
        node = (ClassFilterReteNode) entryPoints.get(className);
        if (node == null && !forbiddenClasses.contains(className)) {
            node = addNewEntryNode(classType);
            if (node == null) {
                forbiddenClasses.add(className);
            }
        }
        return node;
    }

    /**
     * Returns the (production) rule base of this knowledge base.
     *
     * @return the (production) rule base of this knowledge base.
     */
    public AbstractRuleBase getRuleBase() {
        return ruleBase;
    }

    /**
     * Invoked when an element is added to the conflict set.
     *
     * @param e the conflict set event.
     */
    public void internalElementAdded(InternalConflictSetEvent e) {
        fireElementAddedEvent(new ConflictSetEvent(this, e.getElement()));
    }

    /**
     * Invoked when an element is removed from the conflict set.
     *
     * @param e the conflict set event.
     */
    public void internalElementRemoved(InternalConflictSetEvent e) {
        fireElementRemovedEvent(new ConflictSetEvent(this, e.getElement()));
    }

    /**
     * Informs this base that an object was modified, so that the rules
     * can be retested against the object.
     *
     * @param obj the object that was modified.
     */
    protected void modified(Object obj) {
        boolean toAssert = retract(obj);

        // inserting the object back only if it has actually
        // been removed.
        if (toAssert) {
            insert(obj);
        }
    }

    /**
     * Receives a message from the Rete network indicating that the
     * given rule and objects should be inserted into the conflict
     * set.
     *
     * @param ruleIndex the index of the firable rule.
     * @param objects   the objects that made the rule firable.
     */
    public void newFirableRule(int ruleIndex, Object[] objects) {
        Object[] array = new Object[objects.length];
        System.arraycopy(objects, 0, array, 0, objects.length);
        ConflictSetElement e;
        e = new ConflictSetElement(ruleIndex, array);
        conflictSet.insertElement(e);
    }

    /**
     * Returns the objects of a given class.
     *
     * @param className the name of the class.
     */
    public Vector objects(String className) {
        return objectBase.objects(className);
    }

    /**
     * Removes the specified conflict set listener so that it no longer
     * receives conflict set events from this knowledge base.
     *
     * @param l the conflict set listener
     */
    public void removeConflictSetListener(RuleFireListener l) {
        conflictSetListeners.removeElement(l);
        if (conflictSetListeners.size() == 0) {
            debug = false;
            conflictSet.removeInternalConflictSetListener(this);
            isListener = false;
        } else {
            debug = ruleFiringListeners.size() != 0;
        }
    }

    /**
     * Removes the specified rule fire listener so that it no longer
     * receives rule fire events from this knowledge base.
     *
     * @param l the rule fire listener
     */
    public void removeRuleFireListener(RuleFireListener l) {
        ruleFiringListeners.removeElement(l);
        if (ruleFiringListeners.size() == 0 &&
                conflictSetListeners.size() == 0) {
            debug = false;
        }
    }

    /**
     * Removes a given object from this knowledge base.
     *
     * @param obj the object being removed.
     * @return <code>true</code> if the given object belonged to this base;
     *         <code>false</code> otherwise.
     */
    public boolean retract(Object obj) {
        boolean result = objectBase.remove(obj);
        conflictSet.removeElementsWith(obj);
        for (Iterator i = allJoinNodes.iterator(); i.hasNext();) {
            JoinReteNode joinNode = (JoinReteNode) i.next();
            joinNode.remove(obj);
        }
        return result;
    }

    /**
     * Fires the rules in the rule base with the objects present in the
     * object base, until no rule is fireable anymore.
     */
    public void run() {
        while (!conflictSet.isEmpty()) {
            try {
                ConflictSetElement element = conflictSet.nextElement();
                int ruleIndex = element.getRuleIndex();
                ruleBase.setRuleIndex(ruleIndex);
                Object[] objects = element.getObjects();
                ruleBase.setObjects(ruleIndex, objects);
                if (debug) {
                    fireRuleFiringEvent(new RuleEvent(this, ruleIndex));
                }
                ruleBase.internalFireRule(ruleIndex);
                if (debug) {
                    fireRuleFiredEvent(new RuleEvent(this, ruleIndex));
                }
            } catch (NoMoreElementsException e) {
            } // It won't happen.
        }
    }

    /**
     * Searches the interface implementation hierarchy of the given
     * class to see if it can be connected to some entry point of the
     * network.
     *
     * @param classType      the class object whose interface implementation
     *                       hierarchy is to be analysed to see if it fits into the
     *                       current rete network.
     * @param classEntryNode the entry node corresponding to the given
     *                       class, if such exists. If it already exists and some
     *                       path is found, a new one won't have to be created.
     * @return the ClassFilterReteNode corresponding to the given
     *         class object, if some way could be found to connect it
     *         to the rest of the network; <code>null</code>
     *         otherwise.
     */
    private ClassFilterReteNode searchInterfaces(Class classType,
                                                 ClassFilterReteNode classEntryNode) {
        Class[] interfaces = classType.getInterfaces();
        ClassFilterReteNode result = classEntryNode;
        for (int i = 0; i < interfaces.length; i++) {
            Class interfClass = interfaces[i];
            String interfName = interfClass.getName();
            ClassFilterReteNode node;
            node = (ClassFilterReteNode) entryPoints.get(interfName);
            if (node == null) {
                node = searchInterfaces(interfClass, null);
            }
            if (node != null) {
                if (result == null) {
                    result = new ClassFilterReteNode(classType);
                    entryPoints.put(classType.getName(), result);
                }
                result.addSuccessor(node);
            }
        }
        return result;
    }

    /**
     * Defines a conflict set policy for this knowledge base.
     *
     * @param conflictSet the new conflict set that implements its
     *                    associated conflict resolution policy.
     */
    public void setConflictSet(ConflictSet conflictSet) {
        this.conflictSet = conflictSet;
    }

    /**
     * Defines the (production) rule base of this knowledge base.
     *
     * @param ruleBase the rule base of this knowledge base.
     */
    private void setRuleBase(AbstractRuleBase ruleBase) {
        this.ruleBase = ruleBase;
        this.numberOfRules = ruleBase.getNumberOfRules();
    }
}
