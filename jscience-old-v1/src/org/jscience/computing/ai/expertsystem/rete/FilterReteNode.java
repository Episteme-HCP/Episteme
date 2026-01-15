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
import org.jscience.computing.ai.expertsystem.AbstractKnowledgeBase;
import org.jscience.computing.ai.expertsystem.AbstractRuleBase;


/**
 * A discriminant node in the Rete network. A discriminant node is one that
 * has only one input and one output, and it will propagate the incoming
 * objects if some property (i.e., a boolean method call) is satisfied. This
 * kind of node doesn't have a memory of objects, filtering out those that
 * don't comply with the property.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 1.0   14 Jul 2000
 */
public class FilterReteNode extends ReteNode {
    /**
     * The rule base used to check the validity of the property of the
     * incoming objects.
     */
    private AbstractKnowledgeBase knowledgeBase;

    /**
     * The rule base used to check the validity of the property of the
     * incoming objects.
     */
    private AbstractRuleBase ruleBase;

    /**
     * The index of the rule that contains the condition to be checked
     * by this node.
     */
    private int ruleIndex;

    /*
      * The index of the declaration whose property is checked by this
      * node.
      */
    private int declIndex;

/**
     * Creates a new discriminant rete node.
     *
     * @param knowledgeBase the knowledge base that contains this node.
     * @param ruleBase      the rule base used to check the validity of the
     *                      property of the incoming objects.
     * @param ruleIndex     the index of the rule that contains the
     *                      condition to be checked by this node.
     * @param declIndex     the index of the declaration whose property is
     *                      checked by this node.
     */
    public FilterReteNode(AbstractKnowledgeBase knowledgeBase,
        AbstractRuleBase ruleBase, int ruleIndex, int declIndex) {
        super(1, 1);
        this.knowledgeBase = knowledgeBase;
        this.ruleBase = ruleBase;
        this.ruleIndex = ruleIndex;
        this.declIndex = declIndex;
    }

    /**
     * Informs this node that an object has arrived.
     *
     * @param obj the object that arrived at this node.
     * @param input the input number of this node that is to receive the
     *        object. This node has only one input, so this parameter is not
     *        considered in objects of this class.
     */
    public void newObject(Object obj, int input) {
        ruleBase.setObject(ruleIndex, declIndex, obj);

        boolean condOk;

        try {
            condOk = ruleBase.checkConditionsOnlyOf(ruleIndex, declIndex);
        } catch (Exception e) {
            condOk = false;
        }

        if (condOk) {
            propagate(obj, input);
        }
    }

    /**
     * Returns a string representation of this object. Useful for
     * debugging.
     *
     * @return a string representation of this object.
     */
    public String toString() {
        return ("FilterReteNode[ruleIndex=" + ruleIndex + ",declIndex=" +
        declIndex + "]");
    }
}
