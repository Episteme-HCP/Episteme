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

package org.jscience.sociology.forms.control;

import org.jscience.sociology.forms.FormHandler;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This is the servlet where all form presentation's actions will be
 * addressed to. All form action URL-s are mapped to this servlet and the
 * mapping is done in web.xml.
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 */
public class FormControllerServlet extends HttpServlet {
    /**
     * DOCUMENT ME!
     */
    private static FormHandler formHandler;

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        if (formHandler == null) {
            formHandler = new FormHandler();
            formHandler.init(config);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param req DOCUMENT ME!
     * @param resp DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        doPost(req, resp);
    }

    /**
     * DOCUMENT ME!
     *
     * @param req DOCUMENT ME!
     * @param resp DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        String formName = req.getPathInfo();

        if ((formName == null) || formName.equals("")) {
            throw new ServletException("The URL : " + req.getRequestURI() +
                " is malformed, it must end with the form name");
        }

        formName = formName.substring(1);
        formHandler.process(formName, req, resp);
    }
}
