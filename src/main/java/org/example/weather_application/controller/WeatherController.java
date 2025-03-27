package org.example.weather_application.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.weather_application.data.WeatherData;
import org.example.weather_application.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;


    @GetMapping("/")
    public String home() {
        return "index";
    }


    @GetMapping("/weather")
    public String getWeather(@RequestParam("city") @NotBlank(message = "City cannot be blank!")
                             @Valid String city, Model model) {
        WeatherData weather = weatherService.getWeather(city);
        model.addAttribute("cityName", weather.getCity());
        model.addAttribute("temperature", weather.getTemperature());
        model.addAttribute("description", weather.getDescription());
        model.addAttribute("humidity", weather.getHumidity());
        model.addAttribute("windSpeed", weather.getWindSpeed());
        model.addAttribute("pressure", weather.getPressure());

        return "weather";
    }
}