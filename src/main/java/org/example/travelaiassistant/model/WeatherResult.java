package org.example.travelaiassistant.model;

public record WeatherResult(
        String city,
        String date,
        String temperature,
        String condition
) { }
