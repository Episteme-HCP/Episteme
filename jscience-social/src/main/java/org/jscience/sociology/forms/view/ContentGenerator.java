/*
 * @(#)ContentGenerator.java  2.0, 2001/06
 *
 * Copyright (C) 2001 Ilirjan Ostrovica. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
 */
package org.jscience.sociology.forms.view;

import org.jscience.sociology.forms.Form;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Defines the interface for a class that will be responsible for <br>
 * a- generating form presentation<br>
 * b- forwarding the request to the form's "action"<br>
 * This interface gives the user freedom to apply all kinds of presentation techniques.
 * <p/>
 * <p/>
 * <code>org.jscience.sociology.forms.util.SimpleContentGenerator</code> is an implementing class that
 * can be used for building presentation with JSP/JavaBeans/Tag Libraries.
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 * @see SimpleContentGenerator
 */
public interface ContentGenerator {
    /**
     * This method is responsible for generating form presentation and
     * sending it back to client. By implementing this method, the user is
     * free to apply his own presentation technique.
     *
     * @param req the HttpServletRequest object.
     * @param resp the HttpServletResponse object.
     * @param form the Form object going to be presented.
     *
     * @throws ServletException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void sendFormContent(HttpServletRequest req,
        HttpServletResponse resp, Form form)
        throws ServletException, IOException;

    /**
     * This method is responsible for doing the clean up work and
     * forwarding the request to be processed.
     *
     * @param req the HttpServletRequest object.
     * @param resp the HttpServletResponse object.
     * @param form the Form object that was presented.
     *
     * @throws ServletException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void forwardToFormAction(HttpServletRequest req,
        HttpServletResponse resp, Form form)
        throws ServletException, IOException;
}
