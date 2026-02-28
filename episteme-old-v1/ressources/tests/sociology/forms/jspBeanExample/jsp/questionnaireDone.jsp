<html>

<center>
    <table cellpadding=4 cellspacing=2 border=5>

        <th bgcolor="#FDF5E6" colspan=2>
            <font size=5>Form entries successfully validated</font>
        </th>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>How satisfied are you with our business ?</b>
                <% if (request.getParameter("satisfied") != null)
                    out.print(request.getParameter("satisfied"));
                else out.print(""); %>
            </td>
        </tr>


        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>How many years have you been with us ?</b>
                <%= request.getParameter("totalService") %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>Which services you liked more ?</b>
                <% String[] services = request.getParameterValues("services");
                    if (services != null) {
                        for (int i = 0; i < services.length; i++)
                            out.print(services[i] + ",  ");
                    } else out.print(""); %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>Years of basic service</b>
                <%= request.getParameter("basicService") %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>Years of extended service:</b>
                <%= request.getParameter("extendedService") %>
            </td>
        </tr>


        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>Years of special service:</b>
                <%= request.getParameter("specialService") %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>What is our weakest point ?:</b>
                <% if (request.getParameter("weakPoint") != null)
                    out.print(request.getParameter("weakPoint"));
                else out.print(""); %>
            </td>
        </tr>

        <tr bgcolor="#FDF5E6">
            <td valign=top colspan=2>
                <b>Please describe why :</b>
                <%= request.getParameter("comments") %>
            </td>
        </tr>

    </table>
    <p>
        <a href="/jspBeanExample/servlet/applicationController">Try again</a>

</center>
</html> 