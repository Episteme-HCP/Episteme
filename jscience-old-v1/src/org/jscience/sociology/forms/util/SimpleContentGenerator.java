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
