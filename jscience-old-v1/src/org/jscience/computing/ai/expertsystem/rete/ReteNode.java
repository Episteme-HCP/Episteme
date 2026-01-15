package org.jscience.computing.ai.expertsystem.rete;


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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A node in the Rete network. This class is the base for all kinds of
 * nodes that exist in the implementation of the Rete network for JEOPS.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 1.0   13 Jul 2000
 */
public abstract class ReteNode {
    /**
     * The nodes that succede this one. It's actually a list of lists,
     * where each row represents the input index of this node, and the columns
     * represent the connections whose source is the corresponding input.
     */
    private List successors;

    /** The number of inputs of this node. */
    private int numberInputs;

    /** The number of outputs of this node. */
    private int numberOutputs;

/**
     * Creates a new rete node.
     *
     * @param numberInputs  the number of inputs of this node.
     * @param numberOutputs the number of inputs of this node.
     */
    public ReteNode(int numberInputs, int numberOutputs) {
        this.numberInputs = numberInputs;
        this.numberOutputs = numberOutputs;
        successors = new ArrayList();
    }

    /**
     * Adds a connection from some input of this node to a given input
     * of the given node.
     *
     * @param input the input of this node that is being connected to the given
     *        input of the successor node.
     * @param succNode the successor to be added to this node.
     * @param succInput the input in the successor node to where the propagated
     *        objects must be sent.
     */
    public void addSuccessor(int input, ReteNode succNode, int succInput) {
        while (successors.size() <= input) {
            successors.add(new ArrayList());
        }

        List l = (List) successors.get(input);
        l.add(new PairIntReteNode(succInput, succNode));
    }

    /**
     * Adds a connection from the only input of this node to the only
     * input of the given node.
     *
     * @param succNode the successor to be added to this node.
     */
    public void addSuccessor(ReteNode succNode) {
        addSuccessor(0, succNode, 0);
    }

    /**
     * Adds a connection from the only input of this node to a given
     * input of the given node.
     *
     * @param succNode the successor to be added to this node.
     * @param succIndex the input in the successor node to where the propagated
     *        objects must be sent.
     */
    public void addSuccessor(ReteNode succNode, int succIndex) {
        addSuccessor(0, succNode, succIndex);
    }

    /**
     * Returns the number of inputs of this node.
     *
     * @return the number of inputs of this node.
     */
    public int getNumberInputs() {
        return numberInputs;
    }

    /**
     * Returns the number of outputs of this node.
     *
     * @return the number of outputs of this node.
     */
    public int getNumberOutputs() {
        return numberOutputs;
    }

    /**
     * Returns the successors of this node. Available only for
     * subclasses.
     *
     * @return the successors of this node.
     */
    protected List getSuccessors() {
        return successors;
    }

    /**
     * Informs this node that an object has arrived.
     *
     * @param obj the object that arrived at this node.
     * @param input the input number of this node that is to receive the
     *        object.
     */
    public abstract void newObject(Object obj, int input);

    /**
     * Propagates the objects from this node to the successors. It's an
     * auxiliar method that must be invoked by the subclasses in order to
     * propagate the objects to the registered successors.
     *
     * @param obj the object to be propagated.
     * @param input the input from which the object entered.
     */
    protected void propagate(Object obj, int input) {
        List l = (List) successors.get(input);

        for (Iterator i = l.iterator(); i.hasNext();) {
            PairIntReteNode pair = (PairIntReteNode) i.next();
            pair.getNode().newObject(obj, pair.getIntValue());
        }
    }
}
