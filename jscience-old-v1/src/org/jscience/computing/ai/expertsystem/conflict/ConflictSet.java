package org.jscience.computing.ai.expertsystem.conflict;


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
 * Defines the required operations that a conflict set should implement.
 * The conflict set is the place where the fireable rules are stored, and
 * one of them is to be chosen to fire at some moment.<br>
 * It's up to the implementing classes to determine the policy used to
 * select which rule to fire.<p>
 * The implementations of the conflict set must also be able to notify any
 * registered listener that some element has been added to or removed from
 * the conflict set.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.03  08 Jun 2000    The conflict set is now a source of
 *          <code>ConflictSetEvent</code>s.
 * @history 0.01  29 Mar 2000    Class adapted from classes ConflictSet and
 * RuleSorter from previous version of JEOPS.
 * @history 0.02  31 Mar 2000    The conflict set now stores ConflictSetElement
 * objects, instead of AbstractRuleBasersion ones.
 */
public interface ConflictSet {
    /**
     * Adds the specified listener to receive events from this conflict
     * set.
     *
     * @param l the conflict set listener
     */
    public void addInternalConflictSetListener(InternalConflictSetListener l);

    /**
     * Removes all rules from this conflict set, as well as cleaning
     * any history that might have been stored.
     */
    public void flush();

    /**
     * Inserts a rule instantiation.
     *
     * @param element a conflict set element that holds the rule index as well
     *        as the objects bound to the rule declarations.
     */
    public void insertElement(ConflictSetElement element);

    /**
     * Checks whether this set has any elements.
     *
     * @return <code>false</code> if there is at least one fireable rule in
     *         this set; <code>true</code> otherwise.
     */
    public boolean isEmpty();

    /**
     * Returns the next rule to be fired.
     *
     * @return a conflict set element among those that have been inserted in
     *         this object, according to the policy defined in the conflict
     *         set.
     *
     * @throws NoMoreElementsException if there aren't any more elements in
     *         this conflict set.
     */
    public ConflictSetElement nextElement() throws NoMoreElementsException;

    /**
     * Remove all elements from this set that uses the given object in
     * its instantiations.
     *
     * @param obj the given object
     */
    public void removeElementsWith(Object obj);

    /**
     * Removes the specified listener so that it no longer receives
     * events from this conflict set.
     *
     * @param l the conflict set listener
     */
    public void removeInternalConflictSetListener(InternalConflictSetListener l);
}
