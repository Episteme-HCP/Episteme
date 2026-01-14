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

import java.util.EventObject;

/**
 * A semantic event that indicates that a rule was fired.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.02  22.09.2000 The mapping from declarations to values has been
 *          removed - it can be obtained from the knowledge
 *          base.
 * @history 0.01  02.03.2000
 */
public class RuleEvent extends EventObject {

    /**
     * The index of the fired rule.
     */
    private int ruleIndex;

    /**
     * Class constructor.
     *
     * @param source    the knowledge base that fired the rule.
     * @param ruleIndex the index of the fired rule.
     */
    public RuleEvent(AbstractKnowledgeBase source, int ruleIndex) {
        super(source);
        this.ruleIndex = ruleIndex;
    }

    /**
     * Returns the knowledge base that fired the rule.
     *
     * @return the knowledge base that fired the rule.
     */
    public AbstractKnowledgeBase getKnowledgeBase() {
        return (AbstractKnowledgeBase) super.getSource();
    }

    /**
     * Returns the index of the fired rule.
     *
     * @return the index of the fired rule.
     */
    public int getRuleIndex() {
        return ruleIndex;
    }

}
