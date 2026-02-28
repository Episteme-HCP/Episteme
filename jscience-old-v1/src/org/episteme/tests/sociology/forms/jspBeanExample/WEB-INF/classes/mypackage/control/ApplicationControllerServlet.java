package mypackage.control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This servlet plays the role of the Controller in the jspBeanExample
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
        // request forwarded from SimpleContentGenerator's
        // forwardToFormAction() method, after a successful validation
        else if (action.equals("registration")) {
            // processing of (already validated) form entries can
            // be done here.
            gotoPage("/jsp/registrationDone.jsp", req, resp);
        }
        // request forwarded from SimpleContentGenerator's
        // forwardToFormAction() method, after a successful validation
        else if (action.equals("questionnaire")) {
            // processing of (already validated) form entries can
            // be done here.
            gotoPage("/jsp/questionnaireDone.jsp", req, resp);
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
