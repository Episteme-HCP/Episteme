package org.jscience.earth.loaders;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * OpenWeather API loader for meteorological data.
 * Note: Requires API key from openweathermap.org
 */
public class OpenWeatherLoader {

    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather";
    private String apiKey;

    public OpenWeatherLoader(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Fetches current weather by city name.
     */
    public Map<String, String> getWeatherByCity(String city) {
        try {
            String urlStr = API_URL + "?q=" + java.net.URLEncoder.encode(city, "UTF-8")
                    + "&appid=" + apiKey + "&units=metric";
            URL url = java.net.URI.create(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() != 200) {
                Map<String, String> error = new LinkedHashMap<>();
                error.put("error", "HTTP " + conn.getResponseCode());
                return error;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return parseWeatherResponse(response.toString());
        } catch (Exception e) {
            Map<String, String> error = new LinkedHashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * Fetches current weather by coordinates.
     */
    public Map<String, String> getWeatherByCoordinates(double lat, double lon) {
        try {
            String urlStr = API_URL + "?lat=" + lat + "&lon=" + lon
                    + "&appid=" + apiKey + "&units=metric";
            URL url = java.net.URI.create(urlStr).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() != 200) {
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return parseWeatherResponse(response.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, String> parseWeatherResponse(String json) {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("raw_json", json);

        extractNumericField(json, "temp", result, "temperature_celsius");
        extractNumericField(json, "humidity", result, "humidity_percent");
        extractNumericField(json, "pressure", result, "pressure_hpa");
        extractNumericField(json, "speed", result, "wind_speed_mps");
        extractStringField(json, "description", result, "description");
        extractStringField(json, "name", result, "city");

        return result;
    }

    private void extractNumericField(String json, String field, Map<String, String> result, String key) {
        String pattern = "\"" + field + "\":";
        int idx = json.indexOf(pattern);
        if (idx > 0) {
            int start = idx + pattern.length();
            int end = start;
            while (end < json.length()
                    && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.' || json.charAt(end) == '-')) {
                end++;
            }
            if (end > start) {
                result.put(key, json.substring(start, end));
            }
        }
    }

    private void extractStringField(String json, String field, Map<String, String> result, String key) {
        String pattern = "\"" + field + "\":\"";
        int idx = json.indexOf(pattern);
        if (idx > 0) {
            int start = idx + pattern.length();
            int end = json.indexOf("\"", start);
            if (end > start) {
                result.put(key, json.substring(start, end));
            }
        }
    }
}
