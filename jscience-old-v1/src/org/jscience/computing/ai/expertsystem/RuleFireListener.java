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
 * The listener interface for receiving rule fire events. The class that
 * is interested in acting in response to some rule being fired should
 * implement this interface, and the object created with this class is
 * registered with a rule base, using the <code>addRuleFireListener</code>
 * method. When a rule is about to be fired, that object's
 * <code>ruleFiring</code> method is invoked; when a rule is fired, that
 * object's <code>ruleFired</code> method is invoked.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  09.03.2000
 */
public interface RuleFireListener {
    /**
     * Invoked right after a rule is fired.
     *
     * @param e the rule event.
     */
    public void ruleFired(RuleEvent e);

    /**
     * Invoked right before a rule is fired.
     *
     * @param e the rule event.
     */
    public void ruleFiring(RuleEvent e);
}
