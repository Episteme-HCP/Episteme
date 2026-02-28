<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="form">
        <html>
            <body>
                <form action="/xsltExample/forms/questionnaire">

                    <center>
                        <table cellpadding="4" cellspacing="1" border="5">

                            <th colspan="3">
                                <font size="5">Questionnaire Form</font>
                            </th>
                            <tr>
                                <td valign="top" colspan="3">
                                    <b>How satisfied are you with our business ?</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='satisfied']"/>
                                </td>
                            </tr>
                            <tr bgcolor="#DEF3BD">
                                <td valign="top" colspan="2">
                                    <b>How many years
                                        <br/>
                                        have you been with us ?
                                    </b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='totalService']"/>
                                </td>
                                <td valign="top">
                                    <b>Which services you liked more ?</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='services']"/>
                                </td>
                            </tr>
                            <tr bgcolor="#DEF3BD">
                                <td valign="top">
                                    <b>Years of basic service</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='basicService']"/>
                                </td>
                                <td valign="top">
                                    <b>Years of extended service</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='extendedService']"/>
                                </td>
                                <td valign="top">
                                    <b>Years of special service</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='specialService']"/>
                                </td>
                            </tr>
                            <tr bgcolor="#C6EFF7">
                                <td valign="top" colspan="3">
                                    <b>What is our weakest point ?</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='weakPoint']"/>
                                </td>
                            </tr>
                            <tr bgcolor="#C6EFF7">
                                <td valign="top" colspan="3">
                                    <b>Please describe why :</b>
                                    <br/>
                                    <xsl:apply-templates select="form-element[field-name='comments']"/>
                                </td>
                            </tr>
                            <tr>
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

    <xsl:template match="form-element[field-name='satisfied']">
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
             ' name=&quot;satisfied&quot;',
             ' value=&quot;', $FV, '&quot;', 
             ' ', $CK, ' /&gt;')"
                          disable-output-escaping="yes"/>
            <xsl:value-of select="$FV"/>
        </xsl:for-each>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='totalService']">
        <select name="totalService">
            <option/>
            <xsl:for-each select="field-value">
                <xsl:variable name="FV">
                    <xsl:value-of select="."/>
                </xsl:variable>
                <xsl:variable name="CK">
                    <xsl:value-of select="@state"/>
                </xsl:variable>
                <xsl:value-of select="concat('&lt;option ', $CK, ' /&gt;')" disable-output-escaping="yes"/>
                <xsl:value-of select="$FV"/>
            </xsl:for-each>
        </select>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='services']">

        <xsl:value-of select="concat('&lt;', 'select',
                                    ' name=&quot;services&quot;', ' multiple',
                                    ' size=&quot;3&quot;', '&gt;')" disable-output-escaping="yes"/>
        <xsl:for-each select="field-value">
            <xsl:variable name="FV">
                <xsl:value-of select="."/>
            </xsl:variable>
            <xsl:variable name="CK">
                <xsl:value-of select="@state"/>
            </xsl:variable>
            <xsl:value-of select="concat('&lt;option ', $CK, ' &gt;')" disable-output-escaping="yes"/>
            <xsl:value-of select="$FV"/>
        </xsl:for-each>
        <xsl:value-of select="concat('&lt;', '/select', '&gt;')" disable-output-escaping="yes"/>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='basicService']">
        <select name="basicService">
            <option/>
            <xsl:for-each select="field-value">
                <xsl:variable name="FV">
                    <xsl:value-of select="."/>
                </xsl:variable>
                <xsl:variable name="CK">
                    <xsl:value-of select="@state"/>
                </xsl:variable>
                <xsl:value-of select="concat('&lt;option ', $CK, ' /&gt;')"
                              disable-output-escaping="yes"/>
                <xsl:value-of select="$FV"/>
            </xsl:for-each>
        </select>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='extendedService']">
        <select name="extendedService">
            <option/>
            <xsl:for-each select="field-value">
                <xsl:variable name="FV">
                    <xsl:value-of select="."/>
                </xsl:variable>
                <xsl:variable name="CK">
                    <xsl:value-of select="@state"/>
                </xsl:variable>
                <xsl:value-of select="concat('&lt;option ', $CK, ' /&gt;')"
                              disable-output-escaping="yes"/>
                <xsl:value-of select="$FV"/>
            </xsl:for-each>
        </select>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='specialService']">
        <select name="specialService">
            <option/>
            <xsl:for-each select="field-value">
                <xsl:variable name="FV">
                    <xsl:value-of select="."/>
                </xsl:variable>
                <xsl:variable name="CK">
                    <xsl:value-of select="@state"/>
                </xsl:variable>
                <xsl:value-of select="concat('&lt;option ', $CK, ' /&gt;')"
                              disable-output-escaping="yes"/>
                <xsl:value-of select="$FV"/>
            </xsl:for-each>
        </select>
        <br/>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='weakPoint']">
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
             ' name=&quot;weakPoint&quot;',
             ' value=&quot;', $FV, '&quot;', 
             ' ', $CK, ' /&gt;')"
                          disable-output-escaping="yes"/>
            <xsl:value-of select="$FV"/>
        </xsl:for-each>
        <xsl:apply-templates select="./field-error"/>
    </xsl:template>

    <xsl:template match="form-element[field-name='comments']">
        <xsl:variable name="FV">
            <xsl:value-of select="field-value"/>
        </xsl:variable>
        <xsl:value-of select="concat('&lt;',
         'textarea',
         ' name=&quot;comments&quot;',
         ' rows=&quot;2&quot; cols=&quot;50&quot;&gt;',
          $FV, '&lt;/textarea&gt;')"
                      disable-output-escaping="yes"/>
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
        <!--
        -->