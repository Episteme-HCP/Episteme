/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.earth.loaders;

import org.jscience.io.cache.ResourceCache;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

/**
 * Connector to OpenWeatherMap API for meteorological data.
 * <p>
 * <b>What it does</b>: Fetches current weather conditions including
 * temperature, pressure, humidity, wind, and cloud cover.
 * </p>
 *
 * <p>
 * <b>Data Source</b>: OpenWeatherMap API (requires API key)
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OpenWeather {

    private static final String API_BASE = org.jscience.io.Configuration.get(
            "api.openweather.base", "https://api.openweathermap.org/data/2.5/weather");

    private static String apiKey = org.jscience.io.Configuration.get(
            "api.openweather.key", "");

    /**
     * Sets the OpenWeatherMap API key.
     * Get a free key at https://openweathermap.org/api
     */
    public static void setApiKey(String key) {
        apiKey = key;
    }

    /**
     * Gets current weather for a city.
     * 
     * @param city city name (e.g., "Paris", "London,UK")
     * @return WeatherInfo with type-safe Quantity measurements
     */
    public static WeatherInfo getByCity(String city) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("OpenWeather API key not set. Call setApiKey() first.");
        }

        try {
            String urlStr = API_BASE + "?q=" + java.net.URLEncoder.encode(city, "UTF-8")
                    + "&units=metric&appid=" + apiKey;
            String json = fetchUrl(urlStr);
            return parseWeatherJson(json);
        } catch (Exception e) {
            System.err.println("WARNING: Failed to fetch weather for '" + city + "': " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets current weather by coordinates.
     * 
     * @param lat latitude
     * @param lon longitude
     * @return WeatherInfo
     */
    public static WeatherInfo getByCoordinates(double lat, double lon) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("OpenWeather API key not set.");
        }

        try {
            String urlStr = API_BASE + "?lat=" + lat + "&lon=" + lon
                    + "&units=metric&appid=" + apiKey;
            String json = fetchUrl(urlStr);
            return parseWeatherJson(json);
        } catch (Exception e) {
            System.err.println("WARNING: Failed to fetch weather: " + e.getMessage());
            return null;
        }
    }

    private static String fetchUrl(String urlStr) {
        String cacheKey = "openweather_" + urlStr.hashCode();
        Optional<String> cached = ResourceCache.global().get(cacheKey);
        if (cached.isPresent())
            return cached.get();

        try {
            URL url = URI.create(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error: " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line);
            }
            conn.disconnect();

            String data = output.toString();
            ResourceCache.global().put(cacheKey, data);
            return data;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch from OpenWeather", e);
        }
    }

    private static WeatherInfo parseWeatherJson(String json) {
        WeatherInfo.Builder builder = new WeatherInfo.Builder();

        // Temperature (Celsius with metric units)
        String temp = extractJsonValue(json, "temp");
        if (temp != null)
            builder.temperatureCelsius(Double.parseDouble(temp));

        // Feels like
        String feelsLike = extractJsonValue(json, "feels_like");
        if (feelsLike != null)
            builder.feelsLikeCelsius(Double.parseDouble(feelsLike));

        // Pressure (hPa)
        String pressure = extractJsonValue(json, "pressure");
        if (pressure != null)
            builder.pressureHPa(Double.parseDouble(pressure));

        // Humidity (%)
        String humidity = extractJsonValue(json, "humidity");
        if (humidity != null)
            builder.humidityPercent(Double.parseDouble(humidity));

        // Wind speed (m/s)
        String windSpeed = extractJsonValue(json, "speed");
        if (windSpeed != null)
            builder.windSpeedMps(Double.parseDouble(windSpeed));

        // Cloud cover (%)
        String clouds = extractJsonValue(json, "all");
        if (clouds != null)
            builder.cloudPercent(Double.parseDouble(clouds));

        // Weather description
        String desc = extractJsonValue(json, "description");
        if (desc != null)
            builder.description(desc);

        // City name
        String name = extractJsonValue(json, "name");
        if (name != null)
            builder.location(name);

        return builder.build();
    }

    private static String extractJsonValue(String json, String key) {
        int idx = json.indexOf("\"" + key + "\":");
        if (idx == -1)
            return null;
        int start = idx + key.length() + 3;
        if (start >= json.length())
            return null;

        if (json.charAt(start) == '"') {
            int end = json.indexOf("\"", start + 1);
            return json.substring(start + 1, end);
        } else {
            int end = start;
            while (end < json.length() &&
                    (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.' || json.charAt(end) == '-')) {
                end++;
            }
            return json.substring(start, end);
        }
    }

    private OpenWeather() {
    }
}


