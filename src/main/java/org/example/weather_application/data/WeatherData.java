package org.example.weather_application.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData {

    private String city;
    private Double temperature;
    private String description;
    private Integer humidity;
    private Double windSpeed;
    private Double pressure;
}


