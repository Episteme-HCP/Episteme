<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:external="http://ExternalFunction.xalan-c++.xml.apache.org" exclude-result-prefixes="external">

    <!-- Winkelformatierung mit Ausgabe der Maßeinheit -->
    <xsl:template name="angle">
        <xsl:param name="angle"/>
        <xsl:value-of select="$angle"/>
        <xsl:choose>
            <xsl:when test="$angle[@unit='arcsec']">&#8221;</xsl:when>
            <xsl:when test="$angle[@unit='arcmin']">&#8242;</xsl:when>
            <xsl:when test="$angle[@unit='deg']">&#176;</xsl:when>
            <xsl:when test="$angle[@unit='rad']">rad</xsl:when>
        </xsl:choose>
    </xsl:template>


    <xsl:template match="session">
        <p>
            <xsl:text disable-output-escaping="yes">&lt;a name="session</xsl:text>
            <xsl:value-of select="@id"/>
            <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
            <b>Sitzungen: (
                <xsl:value-of select="@id"/>
                )
            </b>
            <xsl:value-of select="session"/>
            <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
        </p>
        <!-- Zeitpunkt bzw. Intervall der Beobachtung ausgeben -->
        <table border="0" cellspacing="0" cellpadding="2" style="font-size:12;font-family:Verdana, Arial">
            <tr>
                <td>Beginn:</td>
                <td>
                    <xsl:value-of select="substring-before(begin, 'T')"/>
                    um
                    <xsl:value-of select="substring-after(begin, 'T')"/>
                </td>
            </tr>
            <tr>
                <td>Ende:</td>
                <td>
                    <xsl:value-of select="substring-before(end, 'T')"/>
                    um
                    <xsl:value-of select="substring-after(end, 'T')"/>
                </td>
            </tr>
        </table>
        <!-- Mitbeobachter -->
        <xsl:if test="count(coObserver)>0">
            <p>Mitbeobachter:
                <ul>
                    <xsl:for-each select="coObserver">
                        <xsl:sort select="key('observerKey', .)/name"/>
                        <li>
                            <xsl:text disable-output-escaping="yes">&lt;a href="#observer</xsl:text>
                            <xsl:value-of select="."/>
                            <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
                            <xsl:value-of select="key('observerKey', .)/name"/>
                            ,
                            <xsl:value-of select="key('observerKey', .)/surname"/>
                            <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
                        </li>
                    </xsl:for-each>
                </ul>
            </p>
        </xsl:if>
        <xsl:if test="count(weather)>0 or count(equipment)>0 or count(comments)>0">
            <table border="0" cellspacing="0" cellpadding="2" style="font-size:12;font-family:Verdana, Arial">
                <!-- Wetter -->
                <xsl:if test="count(weather)>0">
                    <tr>
                        <td valign="top">Wetter:</td>
                        <td valign="top">
                            <xsl:value-of select="weather"/>
                        </td>
                    </tr>
                </xsl:if>
                <!-- Ausrüstung -->
                <xsl:if test="count(equipment)>0">
                    <tr>
                        <td valign="top">Ausr&#252;stung:</td>
                        <td valign="top">
                            <xsl:value-of select="equipment"/>
                        </td>
                    </tr>
                </xsl:if>
                <!-- Kommentare -->
                <xsl:if test="count(comments)>0">
                    <tr>
                        <td valign="top">Kommentare:</td>
                        <td valign="top">
                            <xsl:value-of select="comments"/>
                        </td>
                    </tr>
                </xsl:if>
            </table>
        </xsl:if>
        <xsl:call-template name="linkTop"/>
    </xsl:template>


    <xsl:template match="target">
        <p>
            <xsl:text disable-output-escaping="yes">&lt;a name="target</xsl:text>
            <xsl:value-of select="@id"/>
            <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
            <b>Objekt:</b>
            <xsl:value-of select="name"/>
            <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
        </p>
        <xsl:choose>
            <xsl:when test="@xsi:type='fgca:deepSkyGX'">Galaxie</xsl:when>
            <xsl:when test="@xsi:type='fgca:deepSkyGC'">Kugelsternhaufen</xsl:when>
            <xsl:when test="@xsi:type='fgca:deepSkyGN'">Galaktischer Nebel</xsl:when>
            <xsl:when test="@xsi:type='fgca:deepSkyOC'">Offener Sternhaufen</xsl:when>
            <xsl:when test="@xsi:type='fgca:deepSkyPN'">Planetarischer Nebel</xsl:when>
            <xsl:when test="@xsi:type='fgca:deepSkyQS'">Quasar</xsl:when>
            <xsl:when test="@xsi:type='fgca:deepSkyDS'">Doppelstern</xsl:when>
            <xsl:when test="@xsi:type='fgca:deepSkyDN'">Dunkelnebel</xsl:when>
            <xsl:when test="@xsi:type='fgca:SunTargetType'">Sonne</xsl:when>
            <xsl:when test="@xsi:type='fgca:MoonTargetType'">Mond</xsl:when>
            <xsl:when test="@xsi:type='fgca:PlanetTargetType'">Planet</xsl:when>
            <xsl:when test="@xsi:type='fgca:MinorPlanetTargetType'">Kleinplanet</xsl:when>
            <xsl:when test="@xsi:type='fgca:CometTargetType'">Komet</xsl:when>
            <xsl:when test="@xsi:type='fgca:UndefinedTargetType'">(sonstiges Objekt)</xsl:when>
            <xsl:otherwise>(unbekannter Typ)</xsl:otherwise>
        </xsl:choose>
        <xsl:if test="count(constellation)>0">in
            <xsl:value-of select="constellation"/>
        </xsl:if>
        <p/>
        <xsl:if test="count(alias)>0">
            <div style="font-size:10">Aliasnamen:
                <xsl:for-each select="alias">
                    <xsl:value-of select="."/>
                    <xsl:if test="position() != last()">,</xsl:if>
                </xsl:for-each>
            </div>
            <br/>
        </xsl:if>
        <table border="0" cellspacing="0" cellpadding="2" style="font-size:12;font-family:Verdana, Arial">
            <xsl:if test="boolean(position/ra)">
                <tr>
                    <td>RA:</td>
                    <td>
                        <xsl:call-template name="formatHHMM">
                            <xsl:with-param name="node" select="position/ra"/>
                        </xsl:call-template>
                    </td>
                </tr>
            </xsl:if>
            <xsl:if test="boolean(position/dec)">
                <tr>
                    <td>Dec:</td>
                    <td>
                        <xsl:call-template name="formatDDMM">
                            <xsl:with-param name="node" select="position/dec"/>
                        </xsl:call-template>
                    </td>
                </tr>
            </xsl:if>
            <!-- Ausgabe von Attributen der Subklassen -->
            <xsl:if test="contains(@xsi:type,'fgca:deepSky')">
                <!-- Deep Sky -->
                <xsl:if test="boolean(smallDiameter) and boolean(largeDiameter)">
                    <tr>
                        <td>Gr&#246;&#223;e:</td>
                        <td>
                            <xsl:call-template name="angle">
                                <xsl:with-param name="angle" select="smallDiameter"/>
                            </xsl:call-template>
                            &#215;
                            <xsl:call-template name="angle">
                                <xsl:with-param name="angle" select="largeDiameter"/>
                            </xsl:call-template>
                        </td>
                    </tr>
                </xsl:if>
                <xsl:if test="boolean(visMag)">
                    <tr>
                        <td>m(vis):</td>
                        <td>
                            <xsl:value-of select="visMag"/>
                            mag
                        </td>
                    </tr>
                </xsl:if>
                <xsl:if test="boolean(surfBr)">
                    <tr>
                        <td>SB:</td>
                        <td>
                            <xsl:value-of select="surfBr"/>
                            mags/sq.arcmin
                        </td>
                    </tr>
                </xsl:if>
                <!-- TODO: um nicht nur Elementnamen und -inhalte auszugeben, wäre hier mehr zu machen -->
                <xsl:for-each select="surfBr/following-sibling::*">
                    <tr>
                        <td>
                            <xsl:value-of select="local-name()"/>
                            :
                        </td>
                        <td>
                            <xsl:value-of select="."/>
                        </td>
                    </tr>
                </xsl:for-each>
            </xsl:if>
            <!-- ################################################################### -->
            <!-- TODO: Ausgabe von Attributen anderer Subklassen (z.B. für Planeten) -->
            <!-- ################################################################### -->
            <xsl:if test="boolean(observer)">
                <tr>
                    <td>Datenherkunft:</td>
                    <td>
                        <xsl:value-of select="key('observerKey', observer)/surname"/>
                        <xsl:text/>
                        <xsl:value-of select="key('observerKey', observer)/name"/>
                    </td>
                </tr>
            </xsl:if>
            <xsl:if test="boolean(datasource)">
                <tr>
                    <td>Datenherkunft:</td>
                    <td>
                        <xsl:value-of select="datasource"/>
                    </td>
                </tr>
            </xsl:if>
        </table>
    </xsl:template>


    <xsl:template name="formatHHMM">
        <xsl:param name="node"/>
        <xsl:choose>
            <xsl:when test="function-available('external:hms')">
                <result>
                    <xsl:value-of select="external:hms($node)"/>
                </result>
            </xsl:when>
            <xsl:otherwise>
                <result>
                    <xsl:call-template name="angle">
                        <xsl:with-param name="angle" select="$node"/>
                    </xsl:call-template>
                </result>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <xsl:template name="formatDDMM">
        <xsl:param name="node"/>
        <xsl:choose>
            <xsl:when test="function-available('external:dms')">
                <result>
                    <xsl:value-of select="external:dms($node)"/>
                </result>
            </xsl:when>
            <xsl:otherwise>
                <result>
                    <xsl:call-template name="angle">
                        <xsl:with-param name="angle" select="$node"/>
                    </xsl:call-template>
                </result>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <xsl:template match="observer">
        <p>
            <xsl:text disable-output-escaping="yes">&lt;a name="observer</xsl:text>
            <xsl:value-of select="@id"/>
            <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
            <b>Beobachter:</b>
            <xsl:value-of select="name"/>
            <xsl:text></xsl:text>
            <xsl:value-of select="surname"/>
            <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
        </p>
        <xsl:if test="count(contact) > 0">Kontaktinfo:
            <br/>
            <ul>
                <xsl:for-each select="contact">
                    <li>
                        <xsl:value-of select="."/>
                    </li>
                </xsl:for-each>
            </ul>
        </xsl:if>
        <xsl:call-template name="linkTop"/>
    </xsl:template>


    <xsl:template match="site">
        <p>
            <xsl:text disable-output-escaping="yes">&lt;a name="site</xsl:text>
            <xsl:value-of select="@id"/>
            <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
            <b>Beobachtungsort:</b>
            <xsl:value-of select="name"/>
            <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
        </p>
        <table border="0" cellspacing="0" cellpadding="2" style="font-size:12;font-family:Verdana, Arial">
            <tr>
                <td>L&#228;nge:</td>
                <td>
                    <xsl:call-template name="angle">
                        <xsl:with-param name="angle" select="longitude"/>
                    </xsl:call-template>
                </td>
            </tr>
            <tr>
                <td>Breite:</td>
                <td>
                    <xsl:call-template name="angle">
                        <xsl:with-param name="angle" select="latitude"/>
                    </xsl:call-template>
                </td>
            </tr>
            <tr>
                <td>Zeitzone:</td>
                <td>UT
                    <xsl:if test="timezone >= 0">+</xsl:if>
                    <xsl:value-of select="timezone"/>
                    min
                </td>
            </tr>
        </table>
        <xsl:call-template name="linkTop"/>
    </xsl:template>


    <xsl:template match="scope">
        <p>
            <xsl:text disable-output-escaping="yes">&lt;a name="scope</xsl:text>
            <xsl:value-of select="@id"/>
            <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
            <b>Optik:</b>
            <xsl:value-of select="model"/>
            <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
        </p>
        <table border="0" cellspacing="0" cellpadding="2" style="font-size:12;font-family:Verdana, Arial">
            <xsl:if test="count(type)>0">
                <tr>
                    <td>Bauart:</td>
                    <td>
                        <xsl:value-of select="type"/>
                    </td>
                </tr>
            </xsl:if>
            <xsl:if test="count(vendor)>0">
                <tr>
                    <td>Hersteller:</td>
                    <td>
                        <xsl:value-of select="vendor"/>
                    </td>
                </tr>
            </xsl:if>
            <tr>
                <td>&#214;ffnung:</td>
                <td>
                    <xsl:value-of select="aperture"/>
                    mm
                </td>
            </tr>
            <xsl:if test="count(focalLength)>0">
                <tr>
                    <td>Brennweite:</td>
                    <td>
                        <xsl:value-of select="focalLength"/>
                        mm
                    </td>
                </tr>
            </xsl:if>
            <xsl:if test="count(magnification)>0">
                <tr>
                    <td>Vergr&#246;&#223;erung:</td>
                    <td>
                        <xsl:value-of select="magnification"/>
                        &#215;
                    </td>
                </tr>
            </xsl:if>
            <xsl:if test="count(trueField)>0">
                <tr>
                    <td>Gesichtsfeld:</td>
                    <td>
                        <xsl:call-template name="angle">
                            <xsl:with-param name="angle" select="trueField"/>
                        </xsl:call-template>
                    </td>
                </tr>
            </xsl:if>
            <xsl:if test="count(lightGrasp)>0">
                <tr>
                    <td>Lichtausbeute:</td>
                    <td>
                        <xsl:value-of select="lightGrasp"/>
                    </td>
                </tr>
            </xsl:if>
        </table>
        <xsl:call-template name="linkTop"/>
    </xsl:template>


    <xsl:template match="eyepiece">
        <p>
            <xsl:text disable-output-escaping="yes">&lt;a name="eyepiece</xsl:text>
            <xsl:value-of select="@id"/>
            <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
            <b>Okular:</b>
            <xsl:value-of select="model"/>
            <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
        </p>
        <table border="0" cellspacing="0" cellpadding="2" style="font-size:12;font-family:Verdana, Arial">
            <xsl:if test="count(vendor)>0">
                <tr>
                    <td>Hersteller:</td>
                    <td>
                        <xsl:value-of select="vendor"/>
                    </td>
                </tr>
            </xsl:if>
            <tr>
                <td>Brennweite:</td>
                <td>
                    <xsl:value-of select="focalLength"/>
                    mm
                </td>
            </tr>
            <xsl:if test="count(apparentFOV)>0">
                <tr>
                    <td>Gesichtsfeld:</td>
                    <td>
                        <xsl:call-template name="angle">
                            <xsl:with-param name="angle" select="apparentFOV"/>
                        </xsl:call-template>
                    </td>
                </tr>
            </xsl:if>
        </table>
        <xsl:call-template name="linkTop"/>
    </xsl:template>


    <xsl:template match="result">
        <ul>
            <xsl:if test="string-length(description)>0">
                <li>
                    <xsl:value-of select="description"/>
                    <br/>
                </li>
            </xsl:if>
            <xsl:if test="contains(./@xsi:type,'findingsDeepSkyType')">
                <!-- Ausgabe der Bewertung laut Skala der Deep Sky Liste -->
                <li>Bewertung:
                    <xsl:choose>
                        <xsl:when
                                test="contains(key('targetKey', ../target)/@xsi:type,'fgca:deepSkyOC') and ./@resolved='true'">
                            <!-- aufgelöster Offener Sternhaufen -->
                            <xsl:choose>
                                <xsl:when test="rating = '1'">Sehr auffälliger, besonders schöner Sternhaufen;
                                    "Vorführobjekt"
                                </xsl:when>
                                <xsl:when test="rating = '2'">Auffälliger, schöner Sternhaufen</xsl:when>
                                <xsl:when test="rating = '3'">Deutlich sichtbarer Sternhaufen</xsl:when>
                                <xsl:when test="rating = '4'">Sternhaufen fällt kaum auf</xsl:when>
                                <xsl:when test="rating = '5'">Sehr unauffälliger Haufen; kann beim "Vorbeischwenken"
                                    leicht übersehen werden
                                </xsl:when>
                                <xsl:when test="rating = '6'">Sichtung fraglich; Sterndichte an der Stelle entspricht
                                    der Umgebungsdichte
                                </xsl:when>
                                <xsl:when test="rating = '7'">An der eingezeichneten Stelle sind praktisch keine Sterne
                                    sichtbar
                                </xsl:when>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:when test="contains(key('targetKey', ../target)/@xsi:type,'fgca:deepSkyDS')">
                            <!-- Doppelstern -->
                            <xsl:choose>
                                <xsl:when test="rating = '1'">Doppelstern kann getrennt werden</xsl:when>
                                <xsl:when test="rating = '2'">Doppelstern erscheint als "8"</xsl:when>
                                <xsl:when test="rating = '3'">Doppelstern kann nicht getrennt werden</xsl:when>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise>
                            <!-- anderer Objekttyp -->
                            <xsl:choose>
                                <xsl:when test="rating = '1'">Sehr einfaches, auffälliges Objekt im Okular</xsl:when>
                                <xsl:when test="rating = '2'">Objekt bei direktem Beobachten gut zu sehen</xsl:when>
                                <xsl:when test="rating = '3'">Objekt bei direktem Beobachten zu sehen</xsl:when>
                                <xsl:when test="rating = '4'">Indirektes Beobachten ist notwendig, um das Objekt zu
                                    sehen
                                </xsl:when>
                                <xsl:when test="rating = '5'">Objekt bei indirektem Beobachten gerade noch wahrnehmbar
                                </xsl:when>
                                <xsl:when test="rating = '6'">Sichtung des Objekts sehr fraglich</xsl:when>
                                <xsl:when test="rating = '7'">Objekt sicher nicht zu sehen</xsl:when>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>
                </li>
                <xsl:if test="./@stellar">
                    <li>Erscheint sternf&#246;rmig
                        <br/>
                    </li>
                </xsl:if>
                <xsl:if test="./@resolved">
                    <li>Erscheint aufgel&#246;st
                        <br/>
                    </li>
                </xsl:if>
                <xsl:if test="./@mottled">
                    <li>Erscheint strukturiert
                        <br/>
                    </li>
                </xsl:if>
                <xsl:if test="count(smallDiameter)>0 and count(largeDiameter)>0">
                    <li>Scheinbare Gr&#246;&#223;e:
                        <xsl:call-template name="angle">
                            <xsl:with-param name="angle" select="smallDiameter"/>
                        </xsl:call-template>
                        &#215;
                        <xsl:call-template name="angle">
                            <xsl:with-param name="angle" select="largeDiameter"/>
                        </xsl:call-template>
                    </li>
                </xsl:if>
            </xsl:if>
        </ul>
    </xsl:template>


    <xsl:template match="image">
        <xsl:param name="imgFile" select="."/>
        <xsl:param name="imgTag" select="concat('img src=&quot;', $imgFile, '&quot;')"/>
        <p>
            <xsl:text disable-output-escaping="yes">&lt;</xsl:text>
            <xsl:value-of select="$imgTag"/>
            <xsl:text disable-output-escaping="yes">&gt;</xsl:text>
            <br/>
            <xsl:value-of select="$imgFile"/>
        </p>
    </xsl:template>


    <xsl:output method="html"/>
    <!-- mit diesen Schluesselelementen loesen wir die Referenzen komfortabel wieder auf :-) -->
    <xsl:key name="sessionKey" match="sessions/session" use="@id"/>
    <xsl:key name="targetKey" match="targets/target" use="@id"/>
    <xsl:key name="observerKey" match="observers/observer" use="@id"/>
    <xsl:key name="siteKey" match="sites/site" use="@id"/>
    <xsl:key name="scopeKey" match="scopes/scope" use="@id"/>
    <xsl:key name="eyepieceKey" match="eyepieces/eyepiece" use="@id"/>


    <xsl:template match="/">
        <HTML>
            <HEAD>
                <TITLE>Deep Sky Beobachtungen</TITLE>
            </HEAD>
            <BODY>
                <div align="center" style="font-size:24;font-family:Verdana,Arial;color:#0000C0">Deep Sky Logbuch</div>
                <div style="font-size:12;font-family:Verdana, Arial">
                    <!-- Beobachtungen in Dokumentenreihenfolge ausgeben -->
                    <h3>Beobachtungen</h3>
                    <a name="obslist"/>
                    <xsl:apply-templates select="//observation"/>

                    <!-- Stammdaten ausgeben -->
                    <h3>Referenzdaten</h3>

                    <!-- Stammdaten zu den Sitzungen ausgeben -->
                    <xsl:for-each select="//sessions/session">
                        <xsl:sort select="begin"/>
                        <xsl:apply-templates select="."/>
                    </xsl:for-each>

                    <!-- Stammdaten der Beobachter ausgeben -->
                    <xsl:for-each select="//observers/observer">
                        <xsl:sort select="name"/>
                        <xsl:sort select="surname"/>
                        <xsl:apply-templates select="."/>
                    </xsl:for-each>

                    <!-- Stammdaten der Beobachtungsorte ausgeben -->
                    <xsl:for-each select="//sites/site">
                        <xsl:sort select="name"/>
                        <xsl:apply-templates select="."/>
                    </xsl:for-each>

                    <!-- Stammdaten der Optiken ausgeben -->
                    <xsl:for-each select="//scopes/scope">
                        <xsl:sort select="model"/>
                        <xsl:apply-templates select="."/>
                    </xsl:for-each>

                    <!-- Stammdaten der Okulare ausgeben -->
                    <xsl:for-each select="//eyepieces/eyepiece">
                        <xsl:sort select="focalLength"/>
                        <xsl:sort select="model"/>
                        <xsl:apply-templates select="."/>
                    </xsl:for-each>

                    <!-- Generierungsdatum ausgeben -->
                    <script type="text/javascript">
                        <xsl:text disable-output-escaping="yes">
                            &#60;!--
                            document.write("Erstellt: " + document.lastModified);
                            //--&#62;
                        </xsl:text>
                    </script>
                </div>
            </BODY>
        </HTML>
    </xsl:template>


    <xsl:template match="observation">
        <!-- Objektname und Stammdaten zum Objekt ausgeben -->
        <xsl:apply-templates select="key('targetKey', target)"/>

        <table border="0" cellspacing="3" cellpadding="3" style="font-size:14;font-family:Verdana,Arial">
            <tr>
                <td valign="top">
                    <!-- Beschreibende Daten und Referenzdaten zur Beobachtung -->
                    <table border="1" cellspacing="0" cellpadding="2" width="400"
                           style="font-size:14;font-family:Verdana,Arial">
                        <!-- Namen des Beobachters ermitteln und einen Anker setzen -->
                        <tr>
                            <td>Beobachter</td>
                            <td>
                                <xsl:text disable-output-escaping="yes">&lt;a href="#observer</xsl:text>
                                <xsl:value-of select="observer"/>
                                <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
                                <xsl:value-of select="key('observerKey', observer)/name"/>
                                <xsl:text/>
                                <xsl:text></xsl:text>
                                <xsl:value-of select="key('observerKey', observer)/surname"/>
                                <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
                            </td>
                        </tr>

                        <!-- Namen des Beobachtungsortes ermitteln und einen Anker setzen -->
                        <xsl:if test="count(site) = 1">
                            <tr>
                                <td>Ort</td>
                                <td>
                                    <xsl:text disable-output-escaping="yes">&lt;a href="#site</xsl:text>
                                    <xsl:value-of select="site"/>
                                    <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
                                    <xsl:value-of select="key('siteKey', site)/name"/>
                                    <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
                                </td>
                            </tr>
                        </xsl:if>

                        <!-- Zeitpunkt bzw. Intervall der Beobachtung ausgeben -->
                        <tr>
                            <td>
                                <xsl:choose>
                                    <xsl:when test="count(end) = 1">Beginn</xsl:when>
                                    <xsl:otherwise>Zeit</xsl:otherwise>
                                </xsl:choose>
                            </td>
                            <td>
                                <xsl:value-of select="substring-before(begin, 'T')"/>
                                um
                                <xsl:value-of select="substring-after(begin, 'T')"/>
                            </td>
                            <xsl:if test="count(end) = 1">
                                <tr>
                                    <td>Ende</td>
                                    <td>
                                        <xsl:value-of select="substring-before(end, 'T')"/>
                                        um
                                        <xsl:value-of select="substring-after(end, 'T')"/>
                                    </td>
                                </tr>
                            </xsl:if>
                        </tr>

                        <!-- Grenzgröße ausgeben -->
                        <xsl:if test="count(faintestStar) = 1">
                            <tr>
                                <td>Grenzgr&#246;&#223;e</td>
                                <td>
                                    <xsl:value-of select="faintestStar"/>
                                    mag
                                </td>
                            </tr>
                        </xsl:if>

                        <!-- Seeing ausgeben -->
                        <xsl:if test="count(seeing) = 1">
                            <tr>
                                <td>Seeing</td>
                                <td>
                                    <xsl:value-of select="seeing"/>
                                    <xsl:choose>
                                        <xsl:when test="seeing = 1">(sehr gut)</xsl:when>
                                        <xsl:when test="seeing = 2">(gut)</xsl:when>
                                        <xsl:when test="seeing = 3">(mittelmäßig)</xsl:when>
                                        <xsl:when test="seeing = 4">(schlecht)</xsl:when>
                                        <xsl:when test="seeing = 5">(sehr schlecht)</xsl:when>
                                    </xsl:choose>
                                </td>
                            </tr>
                        </xsl:if>

                        <!-- Modellbezeichnung der Optik ermitteln und einen Anker setzen -->
                        <xsl:if test="count(scope) = 1">
                            <tr>
                                <td>Optik</td>
                                <td>
                                    <xsl:text disable-output-escaping="yes">&lt;a href="#scope</xsl:text>
                                    <xsl:value-of select="scope"/>
                                    <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
                                    <xsl:value-of select="key('scopeKey', scope)/model"/>
                                    <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
                                </td>
                            </tr>
                        </xsl:if>

                        <!-- Modellbezeichnung des Okulars ermitteln und einen Anker setzen -->
                        <xsl:if test="count(eyepiece) = 1 or count(magnification) = 1">
                            <tr>
                                <td>Okular</td>
                                <td>
                                    <xsl:choose>
                                        <xsl:when test="count(eyepiece) = 1">
                                            <xsl:text disable-output-escaping="yes">&lt;a href="#eyepiece</xsl:text>
                                            <xsl:value-of select="eyepiece"/>
                                            <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
                                            <xsl:value-of select="key('eyepieceKey', eyepiece)/model"/>
                                            <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
                                            <xsl:if test="count(magnification) = 1">
                                                <xsl:text disable-output-escaping="yes">(V=</xsl:text>
                                                <xsl:value-of select="magnification"/>
                                                <xsl:text>)</xsl:text>
                                            </xsl:if>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:choose>
                                                <xsl:when test="count(magnification) = 1">
                                                    <xsl:text disable-output-escaping="yes">V=</xsl:text>
                                                    <xsl:value-of select="magnification"/>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:otherwise>
                                    </xsl:choose>

                                </td>
                            </tr>
                        </xsl:if>


                        <!-- ################################################### -->
                        <!-- Beschreibung von Kameras ist vorerst zurückgestellt -->
                        <!-- ################################################### -->


                        <!-- Link zu Sitzungen -->
                        <xsl:if test="count(session) = 1">
                            <tr>
                                <td>Sitzung</td>
                                <td>
                                    <xsl:text disable-output-escaping="yes">&lt;a href="#session</xsl:text>
                                    <xsl:value-of select="session"/>
                                    <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
                                    (
                                    <xsl:value-of select="session"/>
                                    )
                                    <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
                                </td>
                            </tr>
                        </xsl:if>
                    </table>
                </td>

                <td width="100%" valign="top" bgcolor="#EEEEEE">
                    <!-- Beobachtungsergebnisse -->
                    <h5>Visueller Eindruck</h5>
                    <p>
                        <xsl:for-each select="result">
                            <xsl:apply-templates select="."/>
                            <xsl:if test="position()!=last()">
                                <hr/>
                            </xsl:if>
                        </xsl:for-each>
                    </p>
                </td>
            </tr>
        </table>

        <!-- Eventuell vorhandene Bilder ausgeben -->
        <xsl:for-each select="image">
            <xsl:apply-templates select="."/>
        </xsl:for-each>

        <hr/>
    </xsl:template>
    <!-- Link zurueck zur Liste der Beobachtungen -->
    <xsl:template name="linkTop">
        <xsl:text disable-output-escaping="yes">&lt;a href="#obslist"&gt; &gt;&gt; Beobachtungen
            &lt;&lt;&lt;/a&gt;</xsl:text>
        <hr/>
    </xsl:template>
</xsl:stylesheet>
