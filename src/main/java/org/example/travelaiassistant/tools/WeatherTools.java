package org.example.travelaiassistant.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class WeatherTools {

    @Tool(description = "Get tomorrow's weather forecast for a given city")
    public String getWeather(String city) {

        return switch (city.toLowerCase()) {
            case "paris" -> "Tomorrow in Paris: 22°C, partly cloudy.";
            case "tokyo" -> "Tomorrow in Tokyo: 28°C, light rain.";
            case "dubai" -> "Tomorrow in Dubai: 36°C, sunny";
            default -> "Sorry, I don't have weather information for " + city;
        };
    }
}
