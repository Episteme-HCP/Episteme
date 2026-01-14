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

package mypackage.control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This servlet plays the role of the Controller in the xsltExample
 * application's Model-View-Controller design.
 */
public class ApplicationControllerServlet extends HttpServlet {
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
        String action = req.getParameter("action");

        // first time the client hits this servlet.
        if (action == null) {
            gotoPage("/welcome.html", req, resp);
        }
        // request forwarded from FilterContentGenerator's
        // forwardToFormAction() method, after a successful validation
        else if (action.equals("registration")) {
            // processing of (already validated) form entries can
            // be done here.
            gotoPage("/done.html", req, resp);
        }
        // request forwarded from FilterContentGenerator's
        // forwardToFormAction() method, after a successful validation
        else if (action.equals("questionnaire")) {
            // processing of (already validated) form entries can
            // be done here.
            gotoPage("/done.html", req, resp);
        }
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
