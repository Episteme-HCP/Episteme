<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:param name="unitTypeDir">../unitTypes</xsl:param>
    <xsl:param name="unitTypeIndexFile" select="concat($unitTypeDir, '/index.xml')"/>
    <xsl:param name="unitTypeDoc" select="document($unitTypeIndexFile)"/>

    <xsl:key name='unitType' match="unitType" use="@id"/>

    <xsl:template match="/">
        <html>
            <style>
                .SELF {background: #fbb}
            </style>
            <table border="1">
                <tr>
                    <th>id</th>
                    <th>name</th>
                    <th>abbreviation</th>
                    <th>unitType</th>
                    <th>parentSI</th>
                    <th>multiplierToSI</th>
                    <th>unitTypeName</th>
                </tr>
                <xsl:for-each select="unitList/unit">
                    <xsl:variable name="unit" select="."/>
                    <xsl:variable name="unitTypeName">
                        <xsl:for-each select="$unitTypeDoc">
                            <xsl:value-of select="key('unitType', $unit/@unitType)/@name"/>
                        </xsl:for-each>
                    </xsl:variable>
                    <xsl:variable name="class" select="@parentSI"/>
                    <tr class="{$class}">
                        <td>
                            <xsl:value-of select="@id"/>
                        </td>
                        <td>
                            <xsl:value-of select="@name"/>
                        </td>
                        <td>
                            <xsl:value-of select="@abbreviation"/>
                        </td>
                        <td>
                            <xsl:value-of select="@unitType"/>
                        </td>
                        <td>
                            <xsl:value-of select="@parentSI"/>
                        </td>
                        <td>
                            <xsl:value-of select="@multiplierToSI"/>
                        </td>
                        <td>
                            <xsl:value-of select="$unitTypeName"/>
                        </td>
                    </tr>
                </xsl:for-each>
            </table>
        </html>
    </xsl:template>

</xsl:stylesheet>
