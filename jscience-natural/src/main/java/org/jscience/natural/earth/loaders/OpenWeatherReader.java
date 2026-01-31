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

package org.jscience.natural.earth.loaders;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.jscience.core.io.AbstractResourceReader;
import org.jscience.core.io.MiniCatalog;
import org.jscience.natural.earth.atmosphere.WeatherInfo;
import org.jscience.core.ui.i18n.I18N;

/**
 * OpenWeather API loader for meteorological data.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OpenWeatherReader extends AbstractResourceReader<WeatherInfo> {

    private static final Logger LOGGER = Logger.getLogger(OpenWeatherReader.class.getName());
    private static final String API_URL = org.jscience.core.io.Configuration.get("api.openweathermap.base");
    private static final String SAMPLES_PATH = "/org/jscience/earth/loaders/weather_samples.json";
    private String apiKey;
    private final ObjectMapper mapper = new ObjectMapper();

    public OpenWeatherReader() {
        this(org.jscience.core.io.Configuration.get("api.openweathermap.key", "YOUR_API_KEY"));
    }

    public OpenWeatherReader(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getCategory() {
        return I18N.getInstance().get("reader.openweather.category", "Earth");
    }

    @Override
    public String getName() {
        return I18N.getInstance().get("reader.openweatherreader.name", "OpenWeather Reader");
    }

    @Override
    public String getDescription() {
        return I18N.getInstance().get("reader.openweatherreader.desc", "Meteorological Data Reader.");
    }

    @Override
    public String getLongDescription() {
        return I18N.getInstance().get("reader.openweatherreader.longdesc", "Fetches current weather data from OpenWeatherMap API.");
    }

    @Override
    protected WeatherInfo loadFromSource(String resourceId) throws Exception {
        // resourceId can be city name or "lat,lon"
        if (resourceId.contains(",")) {
            try {
                String[] parts = resourceId.split(",");
                return getWeatherByCoordinates(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
            } catch (NumberFormatException e) {
                 // Fallback to city name
            }
        }
        return getWeatherByCity(resourceId);
    }

    @Override
    protected MiniCatalog<WeatherInfo> getMiniCatalog() {
        return new MiniCatalog<>() {
            @Override
            public List<WeatherInfo> getAll() {
                try (InputStream is = getClass().getResourceAsStream(SAMPLES_PATH)) {
                    if (is == null) return new ArrayList<>();
                    return loadSamples(is);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }

            @Override
            public Optional<WeatherInfo> findByName(String name) {
                return getAll().stream()
                        .filter(w -> w.getLocation().equalsIgnoreCase(name))
                        .findFirst();
            }

            @Override
            public int size() {
                return getAll().size();
            }
        };
    }
    
    private List<WeatherInfo> loadSamples(InputStream is) {
        List<WeatherInfo> samples = new ArrayList<>();
        try {
            JsonNode root = mapper.readTree(is);
            if (root.isArray()) {
                for (JsonNode node : root) {
                    WeatherInfo.Builder builder = new WeatherInfo.Builder()
                        .location(node.path("location").asText("Unknown"))
                        .description(node.path("description").asText(""))
                        .temperatureCelsius(node.path("temperature").asDouble())
                        .humidityPercent(node.path("humidity").asDouble())
                        .windSpeedMps(node.path("windSpeed").asDouble())
                        .solarIrradianceWm2(node.path("solarIrradiance").asDouble());
                    samples.add(builder.build());
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return samples;
    }



    @Override
    public String getResourcePath() {
        return API_URL;
    }

    @Override public Class<WeatherInfo> getResourceType() { return WeatherInfo.class; }

    @Override
    public String[] getSupportedVersions() {
        return new String[] { "2.5" };
    }

    /**
     * Fetches current weather by city name.
     */
    public WeatherInfo getWeatherByCity(String city) {
        try {
            String urlStr = API_URL + "?q=" + java.net.URLEncoder.encode(city, "UTF-8")
                    + "&appid=" + apiKey + "&units=metric";
            String json = fetchUrl(urlStr);
            return parseWeatherJSON(json);
        } catch (Exception e) {
            System.err.println("WARNING: Failed to fetch weather for '" + city + "': " + e.getMessage());
            return null;
        }
    }

    /**
     * Fetches current weather by coordinates.
     */
    public WeatherInfo getWeatherByCoordinates(double lat, double lon) {
        try {
            String urlStr = API_URL + "?lat=" + lat + "&lon=" + lon
                    + "&appid=" + apiKey + "&units=metric";
            String json = fetchUrl(urlStr);
            return parseWeatherJSON(json);
        } catch (Exception e) {
            System.err.println("WARNING: Failed to fetch weather: " + e.getMessage());
            return null;
        }
    }

    private String fetchUrl(String urlStr) throws Exception {
        URL url = java.net.URI.create(urlStr).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("HTTP " + conn.getResponseCode());
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private WeatherInfo parseWeatherJSON(String json) {
        try {
            JsonNode root = mapper.readTree(json);
            WeatherInfo.Builder builder = new WeatherInfo.Builder();

            // City name
            builder.location(root.path("name").asText("Unknown"));

            // Main observations
            JsonNode main = root.get("main");
            if (main != null) {
                builder.temperatureCelsius(main.path("temp").asDouble(0.0));
                builder.feelsLikeCelsius(main.path("feels_like").asDouble(0.0));
                builder.pressureHPa(main.path("pressure").asDouble(1013.25));
                builder.humidityPercent(main.path("humidity").asDouble(0.0));
            }

            // Wind
            JsonNode wind = root.get("wind");
            if (wind != null) {
                builder.windSpeedMps(wind.path("speed").asDouble(0.0));
            }

            // Clouds
            JsonNode clouds = root.get("clouds");
            if (clouds != null) {
                builder.cloudPercent(clouds.path("all").asDouble(0.0));
            }

            // Description
            JsonNode weather = root.get("weather");
            if (weather != null && weather.isArray() && weather.size() > 0) {
                builder.description(weather.get(0).path("description").asText(""));
            }

            return builder.build();
        } catch (Exception e) {
            LOGGER.severe("Failed to parse weather JSON: " + e.getMessage());
            return null;
        }
    }
}


