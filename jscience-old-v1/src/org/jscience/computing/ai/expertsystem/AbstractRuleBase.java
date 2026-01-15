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

/**
 * A generic rule base of Jeops. Objects of this class represent rule bases
 * that can be treated generically by any knowledge base.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 2.1   07.04.2000   Method for checking all conditions of a rule
 *          that depend on only one of the declared
 *          objects - checkConditionsOnlyOf.
 * @history 0.01  09.03.2000
 * @history 0.02  01.04.2000   Methods for getting and setting all objects
 * in a single time. It's used to retrieve the
 * values bound to the declarations to store in
 * the conflict set.
 * @history 0.03  07.04.2000   Method for retrieving an array of dependencies
 * between local declarations and declarations.
 * The two-dimensional array will have as many
 * rows as there are local declarations; each
 * row will have the object bound to the local
 * declaration in the first column, and the
 * remaining columns will have the objects bound
 * to the regular declaration that created the
 * locally declared object.
 */
public abstract class AbstractRuleBase implements Cloneable {

    /**
     * The knowledge base that contains this rule base.
     */
    private AbstractKnowledgeBase knowledgeBase;

    /**
     * The index of the rule that has the focus. A rule is said to have
     * focus when it's ready to be fired, i.e., the variables are filled
     * such as the rule's conditions are satisfied.
     */
    private int ruleIndex;

    /**
     * Class constructor.
     *
     * @param knowledgeBase the knowledge base that contains this rule base.
     */
    public AbstractRuleBase(AbstractKnowledgeBase knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    /**
     * Adds an object into this rule base.
     *
     * @param obj the object to be inserted into this base.
     */
    public void insert(Object obj) {
        // Saving the state of this base
        Object[] declarations = getObjects(ruleIndex);
//		Object[][] localDecl = getLocalDeclarationDependency(ruleIndex);

        knowledgeBase.insert(obj);

        // Restoring the previous state.
//		setLocalObjects(ruleIndex, localDecl);
        setObjects(ruleIndex, declarations);
    }

    /**
     * Checks whether all conditions of some rule that depend only on
     * the given object are satisfied, not counting the local declarations.
     *
     * @param ruleIndex the index of the rule to be checked
     * @param declIndex the index of the declaration to be checked
     * @return <code>true</code> if all corresponding conditions for
     *         the given rule are satisfied;
     *         <code>false</code> otherwise.
     */
    public abstract boolean checkConditionsOnlyOf(int ruleIndex, int declIndex);

    /**
     * Checks whether all the conditions of a rule which
     * reference only the elements declared up to the given index
     * are true.
     *
     * @param ruleIndex the index of the rule to be checked
     * @param declIndex the index of the declaration to be checked
     * @return <code>true</code> if all the conditions of a rule which
     *         reference only the elements declared up to the given index
     *         are satisfied; <code>false</code> otherwise.
     */
    public abstract boolean checkCondForDeclaration(int ruleIndex, int declIndex);

    /**
     * Checks whether a condition of some rule in this rule base is
     * satisfied.
     *
     * @param ruleIndex the index of the rule to be checked
     * @param condIndex the index of the condition to be checked
     * @return <code>true</code> if the corresponding condition for the
     *         given rule is satisfied. <code>false</code> otherwise.
     */
    public abstract boolean checkCondition(int ruleIndex, int condIndex);

    /**
     * Creates a copy of this object.
     *
     * @return a clone of this object.
     * @throws CloneNotSupportedException if the class Object doesn't
     *                                    support cloning; it should not happen.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /*
    * Compares this object with the given one.
    *
    * @param obj the object to be compared with this one
    * @return <code>true</code> if they're the same object;
    *          <code>false</code> otherwise.
    */
    public boolean equals(Object obj) {
        boolean result = true;
        if (obj instanceof AbstractRuleBase) {
            AbstractRuleBase tmp = (AbstractRuleBase) obj;
            if (tmp.getRuleIndex() != this.getRuleIndex()) {
                result = false;
            } else {
                int noDecl = getNumberOfDeclarations()[ruleIndex];
                for (int i = 0; result && i < noDecl; i++) {
                    Object obj1, obj2;
                    obj1 = this.getObject(ruleIndex, i);
                    obj2 = tmp.getObject(ruleIndex, i);
                    if (!obj1.equals(obj2)) {
                        result = false;
                    }
                }
            }
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Fires one of the rules in this rule base.
     *
     * @param ruleIndex the index of the rule to be fired.
     */
    public void fireRule(int ruleIndex) {
        this.ruleIndex = ruleIndex;
        internalFireRule(ruleIndex);
    }

    /**
     * Removes all the objects from this rule base.
     */
    public void flush() {
        knowledgeBase.flush();
    }

    /**
     * Returns the class of an object declared in a rule.
     *
     * @param ruleIndex        the index of the rule
     * @param declarationIndex the index of the declaration.
     * @return the class of the declared object.
     */
    public abstract Class getDeclaredClass(int ruleIndex, int declarationIndex);

    /**
     * Returns the class name of an object declared in a rule.
     *
     * @param ruleIndex        the index of the rule
     * @param declarationIndex the index of the declaration.
     * @return the class name of the declared object.
     */
    public abstract String getDeclaredClassName(int ruleIndex, int declarationIndex);

    /**
     * Returns the identifiers declared in a given rule.
     *
     * @param ruleIndex the index of the rule.
     * @return an array with the identifiers of the rule declarations.
     */
    public abstract String[] getDeclaredIdentifiers(int ruleIndex);

    /**
     * Returns the number of declarations of the rules in this rule base.
     *
     * @return the number of declarations  of the rules in this rule base.
     */
    public abstract int[] getNumberOfDeclarations();

    /**
     * Returns the number of conditions of the rules in this rule base.
     *
     * @return the number of conditions  of the rules in this rule base.
     */
    public abstract int[] getNumberOfConditions();

    /**
     * Returns the number of rules in this base.
     *
     * @return the number of rules in this base.
     */
    public abstract int getNumberOfRules();

    /**
     * Returns an object that represents a declaration of some rule.
     *
     * @param ruleIndex        the index of the rule
     * @param declarationIndex the index of the declaration in the rule
     * @return the value of the corresponding object.
     */
    public abstract Object getObject(int ruleIndex, int declarationIndex);

    /**
     * Returns all variables bound to the declarations of some rule.
     *
     * @param ruleIndex the index of the rule
     * @return an object array of the variables bound to the declarations
     *         of some rule.
     */
    public abstract Object[] getObjects(int ruleIndex);

    /**
     * Returns the index of the rule that has the focus. A rule is said to
     * have focus when it's ready to be fired, i.e., the variables are
     * filled such as the rule's conditions are satisfied.
     *
     * @return the index of the rule that has the focus.
     */
    public int getRuleIndex() {
        return ruleIndex;
    }

    /**
     * Returns the name of the rules in this rule base.
     *
     * @return the name of the rules in this rule base.
     */
    public abstract String[] getRuleNames();

    /*
    * Returns a hash code for this object.
    *
    * @return a hash code for this object.
    */
    public int hashCode() {
        int result;
        result = getRuleNames()[ruleIndex].hashCode();
        int noDecl = getNumberOfDeclarations()[ruleIndex];
        for (int i = 0; i < noDecl; i++) {
            Object obj;
            obj = this.getObject(ruleIndex, i);
            result += obj.hashCode();
        }
        return result;
    }

    /**
     * Fires one of the rules in this rule base.
     *
     * @param ruleIndex the index of the rule to be fired.
     */
    protected abstract void internalFireRule(int ruleIndex);

    /**
     * Tells this base that an object was modified, so that the rules
     * can be retested against the object.
     *
     * @param obj the object that was modified.
     */
    public void modified(Object obj) {
        // Saving the state of this base
        Object[] declarations = getObjects(ruleIndex);
//		Object[][] localDecl = getLocalDeclarationDependency(ruleIndex);

        knowledgeBase.modified(obj);

        // Restoring the previous state.
//		setLocalObjects(ruleIndex, localDecl);
        setObjects(ruleIndex, declarations);
    }

    /**
     * Removes an object from this rule base.
     *
     * @param obj the object to be removed from this base.
     */
    public void retract(Object obj) {
        knowledgeBase.retract(obj);
    }

    /**
     * Sets an object that represents a declaration of some rule.
     *
     * @param ruleIndex        the index of the rule
     * @param declarationIndex the index of the declaration in the rule
     * @param value            the value of the object being set.
     */
    public abstract void setObject(int ruleIndex, int declarationIndex, Object value);

    /**
     * Defines all variables bound to the declarations of some rule.
     *
     * @param ruleIndex the index of the rule
     * @param objects   an an object array of the variables bound to the
     *                  declarations of some rule.
     */
    public abstract void setObjects(int ruleIndex, Object[] objects);

    /**
     * Defines the index of the rule that has the focus. A rule is said to
     * have focus when it's ready to be fired, i.e., the variables are
     * filled such as the rule's conditions are satisfied.
     *
     * @param value the index of the rule that has the focus.
     */
    public void setRuleIndex(int value) {
        this.ruleIndex = value;
    }
}
