<html>
<body>

<jsp:useBean id="satisfied" class="com.codepassion.form.RadioButton" scope="session"/>
<jsp:useBean id="totalService" class="com.codepassion.form.MenuBox" scope="session"/>
<jsp:useBean id="services" class="com.codepassion.form.MenuBox" scope="session"/>
<jsp:useBean id="basicService" class="com.codepassion.form.MenuBox" scope="session"/>
<jsp:useBean id="extendedService" class="com.codepassion.form.MenuBox" scope="session"/>
<jsp:useBean id="specialService" class="com.codepassion.form.MenuBox" scope="session"/>
<jsp:useBean id="weakPoint" class="com.codepassion.form.RadioButton" scope="session"/>
<jsp:useBean id="comments" class="com.codepassion.form.TextBox" scope="session"/>

<form action="<%= response.encodeURL(request.getContextPath() + "/forms/questionnaire") %>">
<center>
<table cellpadding=4 cellspacing=1 border=5>

<th colspan=3>
    <font size=5>Questionnaire Form</font>
</th>

<tr>
    <td valign=top colspan=3>
        <b>How satisfied are you with our business ?</b><br>
        <input type="radio"
               name="<jsp:getProperty name="satisfied" property="name"/>"
               value="very satisfied"   <%= satisfied.chosen("very satisfied")   %> >very satisfied
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp
        <input type="radio"
               name="<jsp:getProperty name="satisfied" property="name"/>"
               value="worth having" <%= satisfied.chosen("worth having") %> >worth having
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp
        <input type="radio"
               name="<jsp:getProperty name="satisfied" property="name"/>"
               value="not satisfied" <%= satisfied.chosen("not satisfied") %> >not satisfied
        <sub><font color="red">
            <jsp:getProperty name="satisfied" property="errorMessage"/>
        </font></sub>
    </td>
</tr>

<tr bgcolor="#DEF3BD">
    <td valign=top colspan=2>
        <b>How many years<br> have you been with us ?</b><br>
        <select name="<jsp:getProperty name="totalService" property="name"/>">
            <option>
            <option <%= totalService.chosen("1 year")   %> >1 year
            <option <%= totalService.chosen("2 years")  %> >2 years
            <option <%= totalService.chosen("3 years")  %> >3 years
            <option <%= totalService.chosen("4 years")  %> >4 years
            <option <%= totalService.chosen("5 years")  %> >5 years
            <option <%= totalService.chosen("6 years")  %> >6 years
            <option <%= totalService.chosen("7 years")  %> >7 years
        </select>
        <sub><font color="red">
            <jsp:getProperty name="totalService" property="errorMessage"/>
        </font></sub>
    </td>
    <td valign=top>
        <b>Which services you liked more ?</b><br>
        <select name="<jsp:getProperty name="services" property="name"/>" multiple size="3">
            <option <%= services.chosen("Basic Service")    %> >Basic Service
            <option <%= services.chosen("Extended Service")   %> >Extended Service
            <option <%= services.chosen("Special Service") %> >Special Service
        </select>
        <sub><font color="red">
            <jsp:getProperty name="services" property="errorMessage"/>
        </font></sub>
    </td>

</tr>

<tr bgcolor="#DEF3BD">
    <td valign=top>
        <b>Years of basic service</b><br>
        <select name="<jsp:getProperty name="basicService" property="name"/>">
            <option>
            <option <%= basicService.chosen("1 year")   %> >1 year
            <option <%= basicService.chosen("2 years")  %> >2 years
            <option <%= basicService.chosen("3 years")  %> >3 years
            <option <%= basicService.chosen("4 years")  %> >4 years
            <option <%= basicService.chosen("5 years")  %> >5 years
            <option <%= basicService.chosen("6 years")  %> >6 years
            <option <%= basicService.chosen("7 years")  %> >7 years
        </select><br>
        <sub><font color="red">
            <jsp:getProperty name="basicService" property="errorMessage"/>
        </font></sub>
    </td>

    <td valign=top>
        <b>Years of extended service</b><br>
        <select name="<jsp:getProperty name="extendedService" property="name"/>">
            <option>
            <option <%= extendedService.chosen("1 year")   %> >1 year
            <option <%= extendedService.chosen("2 years")  %> >2 years
            <option <%= extendedService.chosen("3 years")  %> >3 years
            <option <%= extendedService.chosen("4 years")  %> >4 years
            <option <%= extendedService.chosen("5 years")  %> >5 years
            <option <%= extendedService.chosen("6 years")  %> >6 years
            <option <%= extendedService.chosen("7 years")  %> >7 years
        </select><br>
        <sub><font color="red">
            <jsp:getProperty name="extendedService" property="errorMessage"/>
        </font></sub>
    </td>

    <td valign=top>
        <b>Years of special service</b><br>
        <select name="<jsp:getProperty name="specialService" property="name"/>">
            <option>
            <option <%= specialService.chosen("1 year")   %> >1 year
            <option <%= specialService.chosen("2 years")  %> >2 years
            <option <%= specialService.chosen("3 years")  %> >3 years
            <option <%= specialService.chosen("4 years")  %> >4 years
            <option <%= specialService.chosen("5 years")  %> >5 years
            <option <%= specialService.chosen("6 years")  %> >6 years
            <option <%= specialService.chosen("7 years")  %> >7 years
        </select><br>
        <sub><font color="red">
            <jsp:getProperty name="specialService" property="errorMessage"/>
        </font></sub>
    </td>
</tr>

<tr bgcolor="#C6EFF7">
    <td valign=top colspan=3>
        <b>What is our weakest point ?</b><br>
        <input type="radio"
               name="<jsp:getProperty name="weakPoint" property="name"/>"
               value="marketing"   <%= weakPoint.chosen("marketing")   %> >marketing
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp
        <input type="radio"
               name="<jsp:getProperty name="weakPoint" property="name"/>"
               value="customer service" <%= weakPoint.chosen("customer service") %> >customer service
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp
        <input type="radio"
               name="<jsp:getProperty name="weakPoint" property="name"/>"
               value="quality" <%= weakPoint.chosen("quality") %> >quality
        <sub><font color="red">
            <jsp:getProperty name="weakPoint" property="errorMessage"/>
        </font></sub>
    </td>
</tr>

<tr bgcolor="#C6EFF7">
    <td valign=top colspan=3>
        <b>Please describe why :</b><br>
        <textarea name="<jsp:getProperty name="comments" property="name"/>" rows=2 cols=50>
            <jsp:getProperty name="comments" property="value"/>
        </textarea>
        <sub><font color="red">
            <jsp:getProperty name="comments" property="errorMessage"/>
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