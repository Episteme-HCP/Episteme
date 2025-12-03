<html>

<center>
    <table cellpadding=4 cellspacing=2 border=5>

        <th bgcolor="#FDF5E6" colspan=2>
            <font size=5>Form entries successfully validated</font>
        </th>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>Name</b>
                <%= request.getParameter("name") %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>Login</b>
                <%= request.getParameter("login") %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top>
                <b>Password</b>
                <%= request.getParameter("password") %>
            </td>
            <td valign=top>
                <b>PasswordAgain:</b>
                <%= request.getParameter("passwordAgain") %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>Can we contact you ?</b>
                <% if (request.getParameter("contact") != null)
                    out.print(request.getParameter("contact"));
                else out.print(""); %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>Phone number:</b>
                <%= request.getParameter("phone") %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>Best time to call:</b>
                <% String[] contactTime = request.getParameterValues("contactTime");
                    if (contactTime != null) {
                        for (int i = 0; i < contactTime.length; i++)
                            out.print(contactTime[i] + ",  ");
                    } else out.print(""); %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>Country:</b>
                <%= request.getParameter("country") %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>City:</b>
                <%= request.getParameter("city") %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>Postal Code:</b>
                <%= request.getParameter("postCode") %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>Street Name:</b>
                <%= request.getParameter("street") %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>Number:</b>
                <%= request.getParameter("number") %>
            </td>
        </tr>

    </table>
    <p>
        <a href="/jspBeanExample/servlet/applicationController">Try again</a>

</center>
</html> 