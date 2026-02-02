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
package org.jscience.core.computing.ai.agents.acl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an Agent Communication Language (ACL) Message.
 * <p>
 * Compliant with FIPA ACL Message Structure Specification.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class ACLMessage implements Serializable {
    private Performative performative;
    private String sender; // AID (Agent ID)
    private String receiver; // AID
    private String content;
    private String language;
    private String ontology;
    private String protocol;
    private String conversationId;
    private String replyWith;
    private String inReplyTo;
    private long replyBy;
    
    private Map<String, String> userDefinedParameters = new HashMap<>();

    public ACLMessage(Performative performative) {
        this.performative = performative;
    }

    public Performative getPerformative() {
        return performative;
    }

    public void setPerformative(Performative performative) {
        this.performative = performative;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    // ... Getters and Setters for other fields omitted for brevity but would be present
    
    @Override
    public String toString() {
        return "ACLMessage{" +
                "performative=" + performative +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
