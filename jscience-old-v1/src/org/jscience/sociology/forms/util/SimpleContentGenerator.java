/*
 * @(#)SimpleContentGenerator.java  2.0, 2001/06
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
package org.jscience.sociology.forms.util;

import org.jscience.sociology.forms.Form;
import org.jscience.sociology.forms.FormElement;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * This class is an implementation of
 * <code>org.jscience.sociology.forms.view.ContentGenerator</code> intended
 * for use with Java Server Pages.
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 *
 * @see ContentGenerator
 */
public class SimpleContentGenerator implements org.jscience.sociology.forms.view.ContentGenerator {
    /**
     * Implements the same method of <code>ContentGenerator</code>
     * interface.
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
        throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (form.isFirstime()) {
            bound(session, form);
        }

        gotoPage(form.getFormPagePath(), req, resp);
    }

    /**
     * Implements the same method of <code>ContentGenerator</code>
     * interface.
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
        throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (form.isUnboundBeforeActionPage()) {
            unbound(session, form);
        }

        gotoPage(form.getFormActionPath(), req, resp);
    }

    /**
     * DOCUMENT ME!
     *
     * @param session DOCUMENT ME!
     * @param form DOCUMENT ME!
     */
    private void bound(HttpSession session, Form form) {
        FormElement[] formElements = form.getFormElements();

        for (int i = 0; i < formElements.length; i++) {
            session.setAttribute(formElements[i].getName(), formElements[i]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param session DOCUMENT ME!
     * @param form DOCUMENT ME!
     */
    private void unbound(HttpSession session, Form form) {
        FormElement[] formElements = form.getFormElements();

        for (int i = 0; i < formElements.length; i++) {
            session.removeAttribute(formElements[i].getName());
        }

        session.removeAttribute(form.getName());
    }

    /**
     * DOCUMENT ME!
     *
     * @param address DOCUMENT ME!
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    private void gotoPage(String address, HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }
}
