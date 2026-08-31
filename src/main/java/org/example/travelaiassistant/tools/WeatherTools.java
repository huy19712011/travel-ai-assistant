package org.example.travelaiassistant.tools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.travelaiassistant.model.ForecastResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherTools {

    private final RestTemplate restTemplate;

    @Value("${app.weather.api-key}")
    private String apiKey;

    @Tool(description = "Get weather forecast for a given city and date (yyyy-MM-dd)")
    public String getWeather(String city, String date) {

        try {
            log.info("Fetching weather for {} on {}", city, date);
            String url = UriComponentsBuilder
                    .fromUriString("http://api.weatherapi.com/v1/forecast.json")
                    .queryParam("key", apiKey)
                    .queryParam("q", city)
                    .queryParam("dt", date)
                    .toUriString();

            ForecastResponse response = restTemplate.getForObject(url, ForecastResponse.class);
            if (response == null) {
                return "No response from Weather API.";
            }

            ForecastResponse.ForecastDay forecast =
                    response.getForecast().getForecastday().get(0);

            String condition = forecast.getDay().getCondition().getText();
            double temperature = forecast.getDay().getAvgtemp_c();

            return String.format(
                    "Weather in %s on %s:  %.1f°C, %s", city, date, temperature, condition
            );
        } catch (Exception e) {
            log.error("Error fetching weather for {} on {}: {}", city, date, e.getMessage(), e);
            return  "Unable to fetch weather information.";
        }
    }
}
