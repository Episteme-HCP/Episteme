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
