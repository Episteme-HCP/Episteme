/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.earth.geosphere.earth;

/**
 * 
DOCUMENT ME!
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//the basic class for soils (whether flat or with caves, mines, holes)

//there are things we do not take into account: permeability, erosion
public class EarthSoilReader extends Object {
    //use LandCharacterization
    //and OlsonGeographicReader

    //composition would be great

    //map reader for:
    //GTOPO30 + etopo
    //srtm + antartica + etopo + srtm30 (for latitudes superior to 60 degrees)
    //user generated height maps of the full earth

    //hydro1K, BlueMarble

    //provide information with that for sea, lakes, river, geyzer, gazsource, underwater gaz source
    //http://earth-info.nima.mil/gns/html/
    //and vmap

    //may be we should provide a class for roads, railroads, major cities
    //http://www.esri.com/data/download/basemap/index.html for small pieces
    //http://geoengine.nima.mil/servlet/geoEngine.nima.Geospatial?Cmd=START&CurrentTab=find&CenterX=0&CenterY=0&defaultRadio=PANZOOM&MinX=-180&MaxX=180&MinY=-90&MaxY=90&FindDMSLat=0000000N&FindDMSLon=0000000E&FindDecimalLat=0.0&FindDecimalLon=0.0&findCountry=&findCity=&findType=CITY&findExact=&ul_DMSLat=0000000N&ul_DMSLon=0000000E&lr_DMSLat=0000000N&lr_DMSLon=0000000E&ul_DecimalLat=0.0&ul_DecimalLon=0.0&lr_DecimalLat=0.0&lr_DecimalLon=0.0&AOIFlag=NONE&dropDownProduct=&ViewImageSize=SMALL&ShowProduct=000000000000000000000000&EnableProduct=000000000000000000000000&Dirty=1&CrossHair=off&LatLonGrid=off&LatLonBorder=off&MapSize=small&ImgURL=null&ovMinX=-180&ovMinY=-90&ovMaxX=180&ovMaxY=90&LegendURL=null

    //geopolitical
    //http://www.dbis.informatik.uni-goettingen.de/Mondial/

    //old earths
    //http://www.scotese.com/
    //and information for this at: http://www.handprint.com/PS/GEO/geoevo.html and http://jan.ucc.nau.edu/%7Ercb7/paleogeographic.html
}
