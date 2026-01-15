package mypackage.contentGenerator;

import org.jscience.sociology.forms.Form;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * This class is an implementation of
 * <code>org.jscience.sociology.forms.view.ContentGenerator</code> intended
 * for use with Servlet 2.3 filters in xsltExample application..
 */
public class FilterContentGenerator implements org.jscience.sociology.forms.view.ContentGenerator {
    /**
     * DOCUMENT ME!
     *
     * @param req DOCUMENT ME!
     * @param resp DOCUMENT ME!
     * @param form DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void sendFormContent(HttpServletRequest req,
        HttpServletResponse resp, Form form)
        throws ServletException, IOException {
        // filter takes care of form presentation
    }

    /**
     * DOCUMENT ME!
     *
     * @param req DOCUMENT ME!
     * @param resp DOCUMENT ME!
     * @param form DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void forwardToFormAction(HttpServletRequest req,
        HttpServletResponse resp, Form form)
        throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (form.isUnboundBeforeActionPage()) {
            session.removeAttribute(form.getName());
        }

        gotoPage(form.getFormActionPath(), req, resp);
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
