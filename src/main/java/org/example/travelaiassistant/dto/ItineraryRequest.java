package org.example.travelaiassistant.dto;

import lombok.Data;

@Data
public class ItineraryRequest {

    private String destination;
    private int days; // 3 days, 5 days, ...
}
