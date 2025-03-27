package org.example.weather_application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.weather_application.data.WeatherData;
import org.example.weather_application.exception.CityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
@RequiredArgsConstructor
public class WeatherService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    public WeatherData getWeather(String city) {
        String url = apiUrl + "?q=" + city + "&appid=" + apiKey + "&units=metric";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 404) {
                throw new CityNotFoundException("City not found: " + city);
            } else if (response.statusCode() != 200) {
                throw new RuntimeException("Error fetching weather data: HTTP " + response.statusCode());
            }

            String responseBody = response.body();

            // Parsowanie JSON response with Jackson
            JsonNode root = objectMapper.readTree(responseBody);

            JsonNode mainNode = root.path("main");
            JsonNode weatherArray = root.path("weather");
            JsonNode weatherNode = !weatherArray.isEmpty() ? weatherArray.get(0) : null;
            JsonNode windNode = root.path("wind");

            String cityName = root.path("name").asText();
            double temp = mainNode.path("temp").asDouble();
            String desc = weatherNode != null ? weatherNode.path("description").asText() : "";
            int humidity = mainNode.path("humidity").asInt();
            double windSpeed = windNode.path("speed").asDouble();
            double pressure = mainNode.path("pressure").asDouble();

            return new WeatherData(cityName, temp, desc, humidity, windSpeed, pressure);

        } catch (IOException e) {
            throw new RuntimeException("Error parsing weather data", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrupted", e);
        }
    }
}