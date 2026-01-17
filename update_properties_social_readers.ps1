$propFile = "c:\Silvere\Encours\Developpement\JScience\jscience-social\src\main\resources\org\jscience\ui\i18n\messages_social_en.properties"
$content = Get-Content -Path $propFile -Raw

$updates = @{
    "reader.worldbank.name" = "World Bank Reader"
    "reader.worldbank.desc" = "World Bank Data (Indicators & Countries)."
    "reader.worldbank.longdesc" = "Fetcher for World Bank Open Data, providing access to global development data indicators and country information."
    
    "reader.factbook.name" = "CIA Factbook Reader"
    "reader.factbook.desc" = "CIA World Factbook Reader (XML)."
    "reader.factbook.longdesc" = "Parses XML data from the CIA World Factbook, providing detailed country information including demographics, geography, and government."
    
    "reader.csvtimeseries.name" = "CSV Time Series Reader"
    "reader.csvtimeseries.desc" = "Historical Time Series Reader (CSV)."
    "reader.csvtimeseries.longdesc" = "Reads historical time-series data from CSV files for economic and sociological analysis."
    
    "reader.srtmelevation.name" = "SRTM Elevation Reader"
    "reader.srtmelevation.desc" = "SRTM Data Reader (HGT format)."
    "reader.srtmelevation.longdesc" = "Reads high-resolution elevation data from the Shuttle Radar Topography Mission (SRTM) files in HGT format."
    
    "reader.etopoelevation.name" = "ETOPO1 Elevation Reader"
    "reader.etopo.desc" = "ETOPO1 Global Relief Model Reader."
    "reader.etopo.longdesc" = "Reads global relief model data from ETOPO1, including ice surface and bedrock elevation."
    
    "reader.elevation.name" = "Elevation Reader"
    "reader.elevation.description" = "Elevation Data Reader (Altimetry/Bathymetry)."
    "reader.elevation.longdesc" = "Reads elevation data from various sources including Google Maps API, SRTM, and ETOPO1."
    
    "reader.geojson.name" = "GeoJSON Reader"
    "reader.geojson.desc" = "GeoJSON Geographic Data Reader."
    "reader.geojson.longdesc" = "Reads geographic features and regions from GeoJSON format files."
    
    "reader.financial.name" = "Financial Market Reader"
    "reader.financial.desc" = "Financial Market Data Reader."
    "reader.financial.longdesc" = "Parses financial market data candlesticks (Open, High, Low, Close, Volume) from CSV sources."
    
    "reader.googleelevation.name" = "Google Elevation Reader"
    "reader.googleelevation.desc" = "Google Elevation API Reader."
    "reader.googleelevation.longdesc" = "Retrieves elevation data from the Google Maps Elevation API."

    "category.politics" = "Politics"
    "category.history" = "History"
    "category.geography" = "Geography"
    "category.economics" = "Economics"
}

foreach ($key in $updates.Keys) {
    if ($content -notmatch "^$key=") {
        $content += "`n$key=" + $updates[$key]
        Write-Host "Added $key"
    }
}

Set-Content -Path $propFile -Value $content
Write-Host "Updated messages_social_en.properties"
