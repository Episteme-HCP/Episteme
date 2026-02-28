<html>
<body>

<jsp:useBean id="login" class="com.codepassion.form.TextBox" scope="session"/>
<jsp:useBean id="name" class="com.codepassion.form.TextBox" scope="session"/>
<jsp:useBean id="password" class="com.codepassion.form.PasswordBox" scope="session"/>
<jsp:useBean id="passwordAgain" class="com.codepassion.form.PasswordBox" scope="session"/>
<jsp:useBean id="contact" class="com.codepassion.form.RadioButton" scope="session"/>
<jsp:useBean id="phone" class="com.codepassion.form.TextBox" scope="session"/>
<jsp:useBean id="contactTime" class="com.codepassion.form.CheckBox" scope="session"/>
<jsp:useBean id="street" class="com.codepassion.form.TextBox" scope="session"/>
<jsp:useBean id="number" class="com.codepassion.form.TextBox" scope="session"/>
<jsp:useBean id="postCode" class="com.codepassion.form.TextBox" scope="session"/>
<jsp:useBean id="city" class="com.codepassion.form.TextBox" scope="session"/>
<jsp:useBean id="country" class="com.codepassion.form.TextBox" scope="session"/>

<form action="<%= response.encodeURL(request.getContextPath() + "/forms/registration") %>" method="post">
<center>
<table cellpadding=4 cellspacing=1 border=5>

<th colspan=3>
    <font size=5>Registration Form</font>
</th>

<tr>
    <td valign=top>
        <b>Name</b><br>
        <input type="text"
               name="<jsp:getProperty name="name" property="name"/>"
               value="<jsp:getProperty name="name" property="value"/>">
        <sub><font color="red">
            <jsp:getProperty name="name" property="errorMessage"/>
        </font></sub>
    </td>

    <td valign=top colspan=2>
        <b>Login</b><br>
        <input type="text"
               name="<jsp:getProperty name="login" property="name"/>"
               value="<jsp:getProperty name="login" property="value"/>">
        <sub><font color="red">
            <jsp:getProperty name="login" property="errorMessage"/>
        </font></sub>
    </td>
</tr>

<tr bgcolor="#C6E7DE">
    <td valign=top>
        <b>Password</b><br>
        <input type="password"
               name="<jsp:getProperty name="password" property="name"/>"
               value="<jsp:getProperty name="password" property="value"/>"><br>
        <sub><font color="red">
            <jsp:getProperty name="password" property="errorMessage"/>
        </font></sub>
    </td>
    <td valign=top colspan=2>
        <b>Password again</b><br>
        <input type="password"
               name="<jsp:getProperty name="passwordAgain" property="name"/>"
               value="<jsp:getProperty name="passwordAgain" property="value"/>"><br>
        <sub><font color="red">
            <jsp:getProperty name="passwordAgain" property="errorMessage"/>
        </font></sub>
    </td>
</tr>

<tr bgcolor="#DEF3BD">
    <td valign=top>
        <b>Can we contact you ?</b><br>
        <input type="radio"
               name="<jsp:getProperty name="contact" property="name"/>"
               value="yes"   <%= contact.chosen("yes")   %> >yes
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp
        <input type="radio"
               name="<jsp:getProperty name="contact" property="name"/>"
               value="no" <%= contact.chosen("no") %> >no
        <sub><font color="red">
            <jsp:getProperty name="contact" property="errorMessage"/>
        </font></sub>
    </td>
    <td valign=top>
        <b>Phone number </b><br>
        <input type="text"
               name="<jsp:getProperty name="phone" property="name"/>"
               value="<jsp:getProperty name="phone" property="value"/>" size="15"><br>
        <sub><font color="red">
            <jsp:getProperty name="phone" property="errorMessage"/>
        </font></sub>
    </td>
    <td valign=top>
        <b>Best time to call </b><br>
        <input type="checkbox"
               name="<jsp:getProperty name="contactTime" property="name"/>"
               value="morning"  <%= contactTime.chosen("morning")  %> >morning
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp
        <input type="checkbox"
               name="<jsp:getProperty name="contactTime" property="name"/>"
               value="noon" <%= contactTime.chosen("noon") %> >noon
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp
        <input type="checkbox"
               name="<jsp:getProperty name="contactTime" property="name"/>"
               value="afternoon" <%= contactTime.chosen("afternoon") %> >afternoon<br>
        <sub><font color="red">
            <jsp:getProperty name="contactTime" property="errorMessage"/>
        </font></sub>
    </td>
</tr>

<tr bgcolor="#C6EFF7">
    <td valign=top>
        <b>Country </b><br>
        <input type="text"
               name="<jsp:getProperty name="country" property="name"/>"
               value="<jsp:getProperty name="country" property="value"/>"><br>
        <sub><font color="red">
            <jsp:getProperty name="country" property="errorMessage"/>
        </font></sub>
    </td>
    <td valign=top>
        <b>City </b><br>
        <input type="text"
               name="<jsp:getProperty name="city" property="name"/>"
               value="<jsp:getProperty name="city" property="value"/>"><br>
        <sub><font color="red">
            <jsp:getProperty name="city" property="errorMessage"/>
        </font></sub>
    </td>
    <td valign=top>
        <b>Postal Code</b><br>
        <input type="text"
               name="<jsp:getProperty name="postCode" property="name"/>"
               value="<jsp:getProperty name="postCode" property="value"/>" size="8"><br>
        <sub><font color="red">
            <jsp:getProperty name="postCode" property="errorMessage"/>
        </font></sub>
    </td>
</tr>

<tr bgcolor="#C6EFF7">
    <td valign=top colspan=2>
        <b>Street name</b><br>
        <input type="text"
               name="<jsp:getProperty name="street" property="name"/>"
               value="<jsp:getProperty name="street" property="value"/>" size="40"><br>
        <sub><font color="red">
            <jsp:getProperty name="street" property="errorMessage"/>
        </font></sub>
    </td>
    <td valign=top>
        <b>Number </b><br>
        <input type="text"
               name="<jsp:getProperty name="number" property="name"/>"
               value="<jsp:getProperty name="number" property="value"/>" size="5"><br>
        <sub><font color="red">
            <jsp:getProperty name="number" property="errorMessage"/>
        </font></sub>
    </td>
</tr>


<tr>
    <td align=center colspan=3>
        <input type="submit" value="Submit"> <input type="reset" value="Reset">
    </td>
</tr>

</table>
</center>
</form>
</body>
</html> 