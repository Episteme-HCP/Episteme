<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="form">
        <html>
            <body>
                <form action="/xsltExample/forms/registration" method="post">
                    <center>
                        <table cellpadding="4" cellspacing="1" border="5">

                            <th bgcolor="FDF5E6" colspan="3">
                                <font size="5">
                                    Registration Form
                                </font>
                            </th>
                            <tr>
                                <td valign="top">
                                    <b>Name</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='name']"/>
                                </td>
                                <td valign="top" colspan="2">
                                    <b>Login</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='login']"/>
                                </td>
                            </tr>
                            <tr bgcolor="#C6E7DE">
                                <td valign="top">
                                    <b>Password</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='password']"/>
                                </td>
                                <td valign="top" colspan="2">
                                    <b>Password again</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='passwordAgain']"/>
                                </td>
                            </tr>
                            <tr bgcolor="#DEF3BD">
                                <td valign="top">
                                    <b>Can we contact you ?</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='contact']"/>
                                </td>
                                <td valign="top">
                                    <b>Phone number</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='phone']"/>
                                </td>
                                <td valign="top">
                                    <b>Best time to call</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='contactTime']"/>
                                </td>
                            </tr>
                            <tr bgcolor="#C6EFF7">
                                <td valign="top">
                                    <b>Country</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='country']"/>
                                </td>
                                <td valign="top">
                                    <b>City</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='city']"/>
                                </td>
                                <td valign="top">
                                    <b>Postal Code</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='postCode']"/>
                                </td>
                            </tr>
                            <tr bgcolor="#C6EFF7">
                                <td valign="top" colspan="2">
                                    <b>Street name</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='street']"/>
                                </td>
                                <td valign="top">
                                    <b>Number</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='number']"/>
                                </td>
                            </tr>
                            <tr bgcolor="FDF5E6">
                                <td align="center" colspan="3">
                                    <input type="submit" value="Submit"/>
                                    <input type="reset" value="Reset"/>
                                </td>
                            </tr>

                        </table>
                    </center>
                </form>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="form-element[field-name='name']">
        <xsl:variable name="FV">
            <xsl:value-of select="field-value"/>
        </xsl:variable>
        <xsl:value-of select="concat('&lt;',
         'input',
         ' type=&quot;text&quot;',
         ' name=&quot;name&quot;',
         ' value=&quot;', $FV, '&quot;/&gt;')"
                      disable-output-escaping="yes"/>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='login']">
        <xsl:variable name="FV">
            <xsl:value-of select="field-value"/>
        </xsl:variable>
        <xsl:value-of select="concat('&lt;',
         'input',
         ' type=&quot;text&quot;',
         ' name=&quot;login&quot;',
         ' value=&quot;', $FV, '&quot;/&gt;')"
                      disable-output-escaping="yes"/>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='password']">
        <xsl:variable name="FV">
            <xsl:value-of select="field-value"/>
        </xsl:variable>
        <xsl:value-of select="concat('&lt;',
         'input',
         ' type=&quot;password&quot;',
         ' name=&quot;password&quot;',
         ' value=&quot;', $FV, '&quot;/&gt;')"
                      disable-output-escaping="yes"/>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='passwordAgain']">
        <xsl:variable name="FV">
            <xsl:value-of select="field-value"/>
        </xsl:variable>
        <xsl:value-of select="concat('&lt;',
         'input',
         ' type=&quot;password&quot;',
         ' name=&quot;passwordAgain&quot;',
         ' value=&quot;', $FV, '&quot;/&gt;')"
                      disable-output-escaping="yes"/>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='contact']">
        <xsl:for-each select="field-value">
            <xsl:variable name="FV">
                <xsl:value-of select="."/>
            </xsl:variable>
            <xsl:variable name="CK">
                <xsl:value-of select="@state"/>
            </xsl:variable>
            <xsl:value-of select="concat(
             '&lt;', 
             'input',
             ' type=&quot;radio&quot;',
             ' name=&quot;contact&quot;',
             ' value=&quot;', $FV, '&quot;', 
             ' ', $CK, ' /&gt;')"
                          disable-output-escaping="yes"/>
            <xsl:value-of select="$FV"/>
        </xsl:for-each>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='phone']">
        <xsl:variable name="FV">
            <xsl:value-of select="field-value"/>
        </xsl:variable>
        <xsl:value-of select="concat('&lt;',
         'input',
         ' type=&quot;text&quot;',
         ' name=&quot;phone&quot;',
         ' value=&quot;', $FV, '&quot;', 
         ' size=&quot;15&quot; /&gt;')"
                      disable-output-escaping="yes"/>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='contactTime']">
        <xsl:for-each select="field-value">
            <xsl:variable name="FV">
                <xsl:value-of select="."/>
            </xsl:variable>
            <xsl:variable name="CK">
                <xsl:value-of select="@state"/>
            </xsl:variable>
            <xsl:value-of select="concat(
             '&lt;', 
             'input',
             ' type=&quot;checkbox&quot;',
             ' name=&quot;contactTime&quot;',
             ' value=&quot;', $FV, '&quot;', 
             ' ', $CK, ' /&gt;')"
                          disable-output-escaping="yes"/>
            <xsl:value-of select="$FV"/>
        </xsl:for-each>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='country']">
        <xsl:variable name="FV">
            <xsl:value-of select="field-value"/>
        </xsl:variable>
        <xsl:value-of select="concat('&lt;',
         'input',
         ' type=&quot;text&quot;',
         ' name=&quot;country&quot;',
         ' value=&quot;', $FV, '&quot;/&gt;')"
                      disable-output-escaping="yes"/>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='city']">
        <xsl:variable name="FV">
            <xsl:value-of select="field-value"/>
        </xsl:variable>
        <xsl:value-of select="concat('&lt;',
         'input',
         ' type=&quot;text&quot;',
         ' name=&quot;city&quot;',
         ' value=&quot;', $FV, '&quot;/&gt;')"
                      disable-output-escaping="yes"/>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='postCode']">
        <xsl:variable name="FV">
            <xsl:value-of select="field-value"/>
        </xsl:variable>
        <xsl:value-of select="concat('&lt;',
         'input',
         ' type=&quot;text&quot;',
         ' name=&quot;postCode&quot;',
         ' value=&quot;', $FV, '&quot;', 
         ' size=&quot;8&quot; /&gt;')"
                      disable-output-escaping="yes"/>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='street']">
        <xsl:variable name="FV">
            <xsl:value-of select="field-value"/>
        </xsl:variable>
        <xsl:value-of select="concat('&lt;',
         'input',
         ' type=&quot;text&quot;',
         ' name=&quot;street&quot;',
         ' value=&quot;', $FV, '&quot;', 
         ' size=&quot;40&quot; /&gt;')"
                      disable-output-escaping="yes"/>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='number']">
        <xsl:variable name="FV">
            <xsl:value-of select="field-value"/>
        </xsl:variable>
        <xsl:value-of select="concat('&lt;',
         'input',
         ' type=&quot;text&quot;',
         ' name=&quot;number&quot;',
         ' value=&quot;', $FV, '&quot;', 
         ' size=&quot;5&quot; /&gt;')"
                      disable-output-escaping="yes"/>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element/field-error">
        <sub>
            <font color="red">
                <xsl:value-of select="."/>
            </font>
        </sub>
    </xsl:template>

</xsl:stylesheet>